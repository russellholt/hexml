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
 * a container of elements, with
 * default functionality for toString() and
 * unrecognizedText().
 */
abstract public class HexmlElements
	implements iHexmlFeature, iCollection
{
	/**
	 * Whether unrecognized text should be inserted into the collection.
	 * <b><code>true</code></b> by default.
	 */
	public boolean acceptUnrecognizedText = true;

	abstract public void addElement(Object o);
	abstract public int size();
	abstract public Enumeration elements();
	
	public void unrecognizedText(String text) {
		if (acceptUnrecognizedText)
			addElement(text);
	}
	
	public String toString()
	{
		Enumeration e = elements();
		StringBuffer buf = new StringBuffer();

		while (e.hasMoreElements())
			buf.append(e.nextElement());

		return buf.toString();
	}
}
