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

import java.util.Enumeration;

/**
 * a basic implementation of an object that has both
 * attributes and elements.
 */
public class BasicContainer extends HexmlObject implements iCollection {

	//public iFeatureSet hexmlFeatures = new iFeatureSet() = {
		public String hexmlName;
		public HFAttributeTable hexmlAttributes;
		public HFElementVector hexmlElements;
	//}

	/** set the name (tag) */
	public void setName(String name) {
		hexmlName = name;
	}
	
	/** get the name (tag) */
	public String getName() {
		return hexmlName;
	}

	/** set or add an attribute */
	public void setAttribute(String name, String value) {
		hexmlAttributes.put(name,value);
	}

	/** get the named attribute */
	public String getAttribute(String name) {
		return hexmlAttributes.get(name);
	}

	/** the number of elements */
	public int size() {
		return hexmlElements.size();
	}
	
	/** get the elements */
	public Enumeration elements() {
		return hexmlAttributes.elements();
	}
	
	/** add an element */
	public void addElement(Object obj) {
		hexmlElements.addElement(obj);
	}
	
	/**
	 * invoked by the parser for chunks of
	 * unrecognized text when this iCollection
	 * is the current context of the parse
	 */
	public void unrecognizedText(String text) {
		hexmlElements.addElement(text);
	}
	
	/** stream */
	public String toString() {
		return hexml.streamContainer(hexmlName, hexmlAttributes, hexmlElements);
	}


}
