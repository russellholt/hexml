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
import java.util.Enumeration;

import CA.carleton.freenet.ak117.util.Trie;

/**
 * a parser's vocabulary. Maps tag names (keys)
 * to Class objects (values) to be instantiated when the tags
 * are found. Can load itself from a file.
 */
public class Vocabulary {
	/** tag for defining the vocabulary tagname to classname map */
	public final static String VOCABTAGNAME = "VocabEntry";

	/** class name that represents one map from a vocab tagname to classname */
	public final static String VOCABTAGCLASS = "org.webcycle.hexml.VocabEntry";

	/**
	 * maps tag names (keys) to Class objects
	 */
	public Trie vocab;
	
	/**
	 * Initialize the vocabulary with a new Trie storage.
	 */
	public Vocabulary() {
		vocab = new Trie();
	}
	
	/**
	 * Initialize the vocabulary with the given Trie storage.
	 */
	public Vocabulary(Trie v) {
		vocab = v;
	}
	
	/**
	 * Add the vocab word to the tag map.
	 * 
	 * not implemented:
	 * [ if caseSensitive, insert the toUpperCase() of the vocabulary word.
	 * Note regardless of tag matching case sensitivity, stored tag names
	 * will always be the exact text from the input file. ]
	 */
	public void insertEntry(String tag, String classname)
		throws ClassNotFoundException
	{
		Class c = Class.forName(classname);
		
//		if (caseSensitive)
			vocab.put(tag, c);
//		else
//			vocab.put(tag.toUpperCase(), c);
	}
	
	/**
	 * @returns the implementation Trie
	 */
	public Trie getImpl() {
		return vocab;
	}
	
	/**
	 * @returns a Class object if tag is present, null if not.
	 */
	public Class get(String tag) {
		return (Class) (vocab.get(tag));
	}

	/**
	 * Simple debugging presentation of the vocabulary.
	 * Will be proper XML eventually.
	 */
	public String toString()
	{
		StringBuffer buf = new StringBuffer();
		Enumeration e = vocab.elements();
		Enumeration ek = vocab.keys();

		while (e.hasMoreElements())
			buf.append(ek.nextElement() + " -> " + e.nextElement().toString());

		return buf.toString();
	}
	
	/**
	 * A member class to assist Vocabulary in loading 
	 */
	private class VParseContext extends ParseContextImpl
	{
		//		/**
		//		 * whether entities instantiated by the parser
		//		 * should be accepted
		//		 */
		//		public boolean allowVocabEntryOnly = false;

		/**
		 * initialize the superclass with null storage,
		 * and the enclosing Vocabulary instance (Vocabulary.this)
		 * as the vocabulary object.
		 */
		public VParseContext() {
			super(null, Vocabulary.this);
		}
		
		/**
		 * totally ignore unrecognized text - this includes newlines
		 * and whitespace between VocabEntry instances..
		 */
		public void unrecognizedText(StringBuffer text) {
		}

		/**
		 * Check for instances of VocabEntry. when found, add their definition
		 * to our parse trie. If obj is not a VocabEntry, defer to
		 * super.foundNewObject() which should simply add it to currentContext.
		 */
		public void foundNewObject(Object obj)
		{
			try {
				VocabEntry v = (VocabEntry) obj;

				System.err.println("Found: `" + v.toString() + "'");
				Vocabulary.this.insertEntry(v.vocabTag(), v.vocabClass());
			}
			catch (ClassCastException cce) {
				// ignore all other objects

				//				if (!allowVocabEntryOnly)
				//					super.foundNewObject(obj);
			}
			catch (ClassNotFoundException cnfe) {
				System.err.println("hexml.Vocabulary.VParseContext: " + cnfe.toString());
			}
			
		}

	}	// end class VParseContext
	

	/**
	 * Load our vocabulary by parsing from the given file.
	 * 
	 * @param f the file from which to parse
	 * @param allowOnlyVocabEntries whether to ignore everything except VocabEntry instances
	 * 
	 * @throws FileNotFoundException when f isn't found
	 * @throws IOException on an error reading f
	 */
	public void loadFromFile(File f)		//, boolean allowOnlyVocabEntries)
		throws FileNotFoundException, IOException
	{
		try {
			insertEntry(VOCABTAGNAME, VOCABTAGCLASS);
			VParseContext pcl = new VParseContext();
			//			pcl.allowVocabEntryOnly = allowOnlyVocabEntries;

			Parser p = new Parser();
			p.load(pcl, f);
		}
		catch (ClassNotFoundException cnfe) {
			System.err.println(cnfe.toString());
		}
		
	}
	
}
