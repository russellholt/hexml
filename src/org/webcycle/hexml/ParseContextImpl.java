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

import CA.carleton.freenet.ak117.util.*;
import java.io.File;

/**
 * A simple iParseContext implementation. Storage for elements
 * and a vocabulary map. When new objects are found, they are
 * initialized (if they are iHexmlObjects) and inserted into
 * the element storage. Unrecognized text is simply added (as String
 * objects) to the element storage.
 */
public class ParseContextImpl implements iParseContext {
	iCollection storage;
	Vocabulary vocab;

	/**
	 * Initialize element storage and vocabulary.
	 */
	public ParseContextImpl(iCollection container, Vocabulary v) {
		storage = container;
		vocab = v;
	}
	
	/**
	 * @returns the element storage, which is an iCollection in this
	 * implementation.
	 */
	public Object getStorage() {
		return storage;
	}

	/**
	 * Set the element storage. This implementation expects an iCollection.
	 */
	public void setStorage(Object o) {
		storage = (iCollection) o;
	}
	
	/**
	 * @returns the Vocabulary
	 */
	public Vocabulary getVocab() {
		return vocab;
	}

	/**
	 * add text.toString() to the element storage.
	 */
	public void unrecognizedText(StringBuffer text) {
		storage.unrecognizedText(text.toString());
	}
	
	/**
	 * <p>invoked when a new object (a recognized tag) has been instantiated.
	 * 
	 * If obj is an iHexmlObject, then it is initialize()'d.
	 * 
	 * it may be added to currentContext if
	 * initialize returns true, or obj is not an iHexmlObject.
	 * 
	 * <b>Note!</b> currentContext has not itself been initialize()'d yet,
	 * because it hasn't been completely parsed.
	 */
	public void foundNewObject(Object obj)
	{
		try {
			// tell the object it is completely parsed
			if ( ((iHexmlObject) obj).initialize(this) )
				storage.addElement(obj);	

		} catch (ClassCastException cce) {
			storage.addElement(obj);
		}		
	}
	
	/**
	 * Report an error - called by the parser.
	 */
	public void error(File f, int line, int pos) {
		System.err.println("Parse error in file `"
						   + f.getName() + "', line "
						   + Integer.toString(line) + ", pos "
						   + Integer.toString(pos));
	}

}
