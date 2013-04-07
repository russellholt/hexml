/**
 * Copyright (c) 2000 Destiny WebSolutions, Inc.
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package org.webcycle.hexml;

import java.io.*;
import java.util.*;

import CA.carleton.freenet.ak117.util.*;

/**
 * A simple xml-like parser base class which uses a Trie data structure to recognize
 * tag names and map them to java classes. This results is a tree of objects.
 * 
 * Subclasses must implement the Parser Trie via getvocab().
 */
public class Parser {
	
	/** the current input stream. set by load() or setStream(), used
	 *	by parse().
	 */
	protected BufferedInputStream source = null;
	
	/**
	 * current File being parsed, if any
	 */
	protected File currentFile = null;
	
	/**
	 * whether tags are considered case sensitive.
	 * default is false.
	 */
	protected boolean caseSensitive = false;
	
	/** whether tag case is part of tag matching  */
	public boolean isCaseSensitive() { return caseSensitive; }
	
	/**
	 * set whether to pay attention to the case of tags when parsing.
	 * <p>
	 * the following is no longer implemented:
	 * <i>can only be set if the parse trie is null (ie it has not yet been created)
	 * or if it has no entries. If the parser is set to ignore case, then the
	 * parser vocabulary is built with uppercase tag names (unless your parser
	 * subclass overrides insertVocabEntry().)</i>
	 * <p>
	 * Note regardless of tag matching case sensitivity, stored tag names
	 * will always be the exact text from the input file.
	 * 
	 * @see insertVocabEntry
	 */
	public void setCaseSensitive(boolean cs)
	{
		//if (vocab == null || vocab.size() <= 0)
			caseSensitive = cs;

	}
	
	/**
	 * Set the internal input stream, used by subsequent calls to parse().
	 * Note that load() sets it internally.
	 */
	public void setStream(BufferedInputStream bis) {
		source = bis;
	}

	/**
	 * Read an object from the given filename by creating an empty iCollection
	 * and parsing from a new filestream created with the given name. Sets
	 * the internal input stream <code>source</code>, to be referenced within parse().
	 * Sets currentFile to point to the file openend for reading by parse().
	 * 
	 * Sets <code>source</code> to null when completed.
	 * 
	 * @exception java.io.IOException if read() fails
	 * @exception java.io.FileNotFoundException if fname is not found.
	 */
	public void load(iParseContext context, String fname) throws FileNotFoundException, IOException
	{
		FileInputStream fis = new FileInputStream(fname);
		
		if (currentFile == null)
			currentFile = new File(fname);

		source = new BufferedInputStream(fis, 1024);

		// do it.
		parse(context);

		// clean up
		source.close();
		source = null;
		currentFile = null;
	}

	/**
	 * Parse from the given File by passing thru to load(String)
	 * via File.getPath().
	 * 
	 * @see java.io.File#getPath
	 * @exception java.io.IOException if read() fails
	 * @exception java.io.FileNotFoundException if fname is not found.
	 * 
	 * @returns the parsed "document" object
	 */
	public void load(iParseContext context, File f) throws FileNotFoundException, IOException
	{
		currentFile = f;
		load(context, f.getPath());
		currentFile = null;
	}
	
	/**
	 * Parse from 
	 * Scan for the character '<' and invoke subParse()
	 * at that point in the stream.
	 * @exception java.io.IOException if read() fails
	 */
	public void parse(iParseContext context) throws IOException
	{
		StringBuffer buf = new StringBuffer();

		for(;;) {
			int b = source.read();
			
			if (b == -1) {
				if (buf.length() > 0)
					context.unrecognizedText(buf);
				return;
			}

			if (b == '<') {
				if (buf.length() > 0)
					context.unrecognizedText(buf);

				buf = new StringBuffer();
				if (subParse(context, buf))
					return;
			}
			else {
				buf.append((char) b);
			}
		}
	}

	/**
	 * parse from an open bracket '<'.
	 * Read the tag name (accumulate chars until a space is encountered) and attempt
	 * to instantiate an object for the tag. The tag name to Class object map
	 * returned by Parser.getvocab() is used to find a Class to instantiate;
	 * if found, this new object is given the parse stream.
	 * If this new object is an iCollection, then we enter a new parsing context
	 * and recurse.
	 * If we do not recongize the tag name, then all scanned text is appended to the
	 * argument 'extra'. That is, if we encounter '<abc ', and abc is a known tag,
	 * an instance of abc is created and is handed the parse stream. Otherwise,
	 * the parsed text ('<abc ') is appended to extra and we return.
	 */
	boolean subParse(iParseContext context, StringBuffer extra) throws IOException
	{
		boolean ok = true;
		boolean closing = false;
		StringBuffer tagname = new StringBuffer();
		Object ctx = context.getStorage();

		int ch = source.read();
		
		// if we're a closing tag, eg </
		if (ch == '/') {
			closing = true;
			ch = source.read();
		}

		Vocabulary v = context.getVocab();
		Trie subTrie = v.getImpl();	// subTrie points to each Trie node

		while(ok)
		{
			if (ch == -1)
			{
				ch = ' ';
				break;
			}
			
			// test for /> sequence
			if ((char) ch == '/')
			{
				source.mark(1);
				ch = source.read();
				if (ch != '>') {
					source.reset();
					ch = '/';
				}
			}

			
			if ((char) ch == ' ' || (char) ch == '>')
			{
				Object o = subTrie.getTV();	// will be a Class

				if (o == null)
				{
					tagname.append((char) ch);
					break;
				}
				
				//System.err.println("- recognized tagname `" + tagname.toString() + "'");
				
				if (closing)	// found a match for a close tag in our parse trie
					return true;
				
				try {
					Class c = (Class) o;
					Object newobject = c.newInstance();
					iHexmlObject to = null;

					if (newobject instanceof iHexmlObject)
					{
						to = (iHexmlObject) newobject;

						to.setName(tagname.toString());
	
						if ((char) ch == ' ')
							to.parseAttributes(source);
					}

					// iCollection objects are containers, so we continue parsing in
					// this new context.
					if (to != null && to instanceof iCollection) {
						//parse((iCollection) to);

						// just reusing the same context object.
						// in the future, may do:
						// 	iParseContext subc = context.push(to);
						// 	parse(context);
						context.setStorage(to);

						parse(context);

						context.setStorage(ctx);	// reset to previous state
					}
					
					
					// hey, we have an object.
					context.foundNewObject(to);

					return false;

				} catch (java.lang.InstantiationException ie) {
					System.err.println(ie.toString());
				} catch (java.lang.IllegalAccessException iae) {
					System.err.println(iae.toString());
				} catch (RuntimeException re) {
					System.err.println(re.toString());
                    re.printStackTrace();
					break;
				}
			}
			
			tagname.append((char) ch);

			if (caseSensitive)
				subTrie = subTrie.getChar((char) ch);
			else
				subTrie = subTrie.getChar(Character.toUpperCase((char) ch));
			
			if (subTrie == null) {
				break;
			}

			ch = source.read();
		}
		
		extra.append("<" + (closing? "/":"") + tagname.toString());

		return false;
	}
	
}

