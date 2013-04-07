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

import java.util.*;

/**
 * A container of the form
 * 
 * <pre>
 * &lt;tag&gt;text&lt;/tag&gt;
 * </pre>
 * 
 * where text is an opaque string.

 * */
public class HexmlString extends HexmlObject implements iCollection {
	
	/** the xml tag */
	public String hexmlname = null;
	
	/** all contents */
	public StringBuffer contents = new StringBuffer();
	
	/** set the tag name */
	public void setName(String name) {
		hexmlname = name;
	}

	/** get the tag name */
	public String getName() {
		return hexmlname;
	}
	
	/** no attributes; does nothing. */
	public void setAttribute(String name, String value) { }
	
	/** no attributes; returns blank string. */
	public String getAttribute(String name) {
		return "";
	}

	/** xml-ify */
	public String toString() {
		return openTag(hexmlname) + ">" + contents.toString() + closeCollectionTag(hexmlname);
	}
	
	/**
	 * invoked by the parser for chunks of
	 * unrecognized text when this iCollection
	 * is the current context of the parse
	 */
	public void unrecognizedText(String text) {
		contents.append(text);
	}

	/** add an element to the collection */
	public void addElement(Object object) {
		contents.append(object.toString());
	}

	/** the number of elements in the collection: always one. */
	public int size() {
		return 1;
	}
	
	/**
	 * enumerate this collection, which has one object: the contents.
	 */
	public Enumeration elements() {
		return new SE(contents.toString());
	}

	/**
	 * a simple way to fulfill the enumeration requirement of iCollections.
	 * This makes the String look like a collection that has one object:
	 * the String.
	 */
	public class SE implements Enumeration {
		String str = null;

		/** initialize with the String to "enumerate" */
		public SE(String s) {
			str = s;
		}
		
		/** Only works once */
		public boolean hasMoreElements() {
			return (str == null);
		}

		/**
		 * @throws NoSuchElementException if the String is null.
		 */
		public Object nextElement()
			throws NoSuchElementException
		{
			if (str == null)
				throw new NoSuchElementException("No String.");

			String s = str;
			str = null;
			return s;
		}
}


}
