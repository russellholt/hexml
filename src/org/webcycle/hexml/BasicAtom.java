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

/**
 * An atomic entity with attributes
 */
public class BasicAtom extends HexmlObject {
	
	public String hexmlName;
	public HFAttributeTable hexmlAttributes;
	
	public void setName(String name) {
		hexmlName = name;
	}
	
	public String getName() {
		return hexmlName;
	}

	public void setAttribute(String name, String value)
	{
		if (hexmlAttributes == null)
			hexmlAttributes = new HFAttributeTable();
		hexmlAttributes.put(name, value);
	}

	public String getAttribute(String name) {
		if (hexmlAttributes != null)
			return hexmlAttributes.get(name);
		return null;
	}

	public String toString() {
		return hexml.streamAtom(hexmlName, hexmlAttributes);
	}


}
