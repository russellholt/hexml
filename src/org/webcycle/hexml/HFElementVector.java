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
 * Storage for elements in a Vector.
 */
public class HFElementVector extends HexmlElements
{
	public Vector stuff = new Vector();
	
	public void addElement(Object o) {
		stuff.addElement(o);
	}
	
	public int size() {
		return stuff.size();
	}
	
	public java.util.Enumeration elements() {
		return stuff.elements();
	}
	

}
