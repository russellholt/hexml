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
 * A collection of attributes as a java.util.Hashtable
 */
public class HFAttributeTable implements iHexmlAttributes {
	public Hashtable attributes;
	
	public HFAttributeTable() {
		attributes = new Hashtable();
	}
	
	public HFAttributeTable(int size) {
		attributes = new Hashtable(size);
	}
	
	/**
	 * Put value into the hashtable with key name
	 */
	public void put(String name, String value) {
		attributes.put(name,value);
	}

	/**
	 * Get a String from the hashtable with key name
	 */
	public String get(String name) {
		return attributes.get(name).toString();
	}
	
	/**
	 * attributes.elements()
	 */
	public Enumeration elements() {
		return attributes.elements();
	}
	
	/**
	 * @see HexmlObject#attTable_toString(Hashtable)
	 */
	public String toString() {
		return HexmlObject.attTable_toString(attributes);
	}
}
