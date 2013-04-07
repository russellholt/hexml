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

/**
 * An xml comment, of the form
 * <!-- this is a comment -->
 * 
 * an atomic object, which overrides HexmlObject's attribute parsing
 * methods to append the literal text to a buffer.
 */
class HexmlComment extends HexmlObject {
	
	/** name of the tag that is meaningful for HexmlComment */
	public final static String TAGNAME = "!--";

	/**
	 * Text of the comment, everything between <code>&lt;!--</code>
	 * and <code>--&gt;</code>
	 */
	public String commentText;

	/** does nothing; comments are always named by the value of TAGNAME */
	public void setName(String name) {  }
	
	/** always returns TAGNAME */
	public String getName() {
		return TAGNAME;
	}
	
	/** does nothing */
	public void setAttribute(String name, String value) {	}
	
	/** returns the body of the comment regardless of the argument. */
	public String getAttribute(String name) {
		return commentText;
	}
	
	/**
	 * Scans the attribute line until -->, adding all text to the
	 * commentText string.
	 * 
	 * The next read() is the first char of
	 * the first attribute name (after the space after the tag name).
	 */
	public void parseAttributes(BufferedInputStream source)
	{
		StringBuffer stuff = new StringBuffer();
		int ch;
		char c;
		try {
	
			for (;;) {
				c = (char) (ch = source.read());
				
				// ending condition is a dash....
				if (c == '-')
				{
					c = (char) (ch = source.read());
				
					// ...followed by another dash....
					if (c == '-') {
						c = (char) (ch = source.read());

						// ...followed by >
						if (c == '>') {
							commentText = stuff.toString();
							return;
						}
						stuff.append("-");
					}
					stuff.append("-");
				}
				stuff.append(c);
			}
		}
		catch (Exception e) {	// maybe IOException
			System.err.println(e);
			return;
		}
		
	}

	/** does nothing. there are no attribute values. */
	public void parseAttributeValue(BufferedInputStream source, StringBuffer attname, int firstChar)
		throws IOException
	{	}
	
	public String toString() {
		return "<!-- " + commentText + "-->";	
	}
	
}
