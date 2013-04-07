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
import java.io.*;
import java.lang.reflect.*;


/**
 * Simple implementation for parsable objects,
 * providing basic utility functions for xmlish parsable objects,
 * such as parsing/printing attribute lists, etc.
 * 
 * The parsing method(s) - eg parseAttributes() and parseAttributeValue()
 * require a subclass, but the printing methods - att_toString(), etc.,
 * are static.
 */
public abstract class HexmlObject implements iHexmlObject {

	/**
	 * Parse the attributes of a tag.
	 * The next read() will be the first char of
	 * the first attribute.
	 */
	public void parseAttributes(BufferedInputStream source)
	{
		StringBuffer attname;
		int ch;
		
		// parse a bunch of tag names and values
		// of the form tagname="value".
		try {
			
			nextAttribute: for(;;)
			{
				attname = new StringBuffer();
				
				while (Character.isWhitespace( (char) (ch = source.read()) ) )
					;
				
				if ((char) ch == '>')
					return;

				if (ch == -1)
					return;
				
				// ch the first char of the attribute name
						
				// parse attribute name
				while ((char) ch != '=')
				{
					attname.append((char) ch);
		
					ch = source.read();
					
					if ((char) ch == '>' || ch == -1) {
						setAttribute(attname.toString(), ""); // no value						
						return;
					}
					
					if ((char) ch == ' ') {
						// there is no '='
						setAttribute(attname.toString(), ""); // no value
						continue nextAttribute;
					}
				}
				
				// ch is '='

				ch = source.read();
				
				if (ch == -1) {
					// just the name is ok..
					setAttribute(attname.toString(), ""); // no value
					return;
				}

				// quote
				parseAttributeValue(source, attname, ch);
			}
		
		}
		catch(Exception e) {
			System.err.println(e.toString());
		}
	}
	
	/**
	 * parse the value of the attribute. The next read() will give the first
	 * character after the double quote. This default implementation will
	 * read all characters until a double quote is found, and invoke
	 * setAttribute(attname, value).
	 * 
	 * Subclasses can override this to implement more interesting functionality,
	 * such as interpreting specific notation like "a.b.c" or "a.b()", etc.
	 */
	public void parseAttributeValue(BufferedInputStream source, StringBuffer attname, int firstChar)
		throws IOException
	{
		int ch;
		StringBuffer value = new StringBuffer();
		boolean inQuote = false;
		
		if (firstChar == '\"')
		{
			ch = source.read();
			
			// special case.
			if (ch == '\"') {
				setAttribute(attname.toString(), value.toString());
				return;
			}
				
			inQuote = true;
		}
		else {
			ch = (int) firstChar;
			inQuote = false;
		}

		for (;;)
		//while ((char) (ch=source.read()) != '\"')
		{
			value.append((char) ch);

			ch = source.read();

			if (!inQuote && ch == ' ')
				break;

			if (inQuote && ch == '\"')
				break;
		}

		setAttribute(attname.toString(), value.toString());		
	}

	/**
	 * produce the opening tag, &lt;tagname.
	 */
	public static String openTag(String tag)
	{
		return "<" + tag;
	}
	
	/**
	 * produce the &lt;/tagname&gt; closing tag.
	 */
	public static String closeCollectionTag(String tag) {
		return "</" + tag + ">";
	}
	
	/**
	 * convenience stringification used by subclasses in toString().
	 * @returns a String of the form n="v" preceded by a space so
	 * that a tag can be constructed as tagname + att_toString(n,v)
	 */
	public static String att_toString(String n, String v)
	{
		return " " + n + "=\"" + v + "\"";
	}
	
	/**
	 * convenience stringification for a set of attributes.
	 * @returns a String of the form n="v"
	 */
	public static String attList_toString(Vector names, Vector values)
	{
		if (names == null || values == null)
			return "";
		
		int l = names.size();
		StringBuffer sbuf = new StringBuffer();

		String sv;
		for (int i=0; i < l; i++)
		{				
			sbuf.append(" " + names.elementAt(i).toString());
			
			sv = values.elementAt(i).toString();

			if (sv.length() > 0)
				sbuf.append("=\"" + sv + "\"");
		}
	
		return sbuf.toString();
	}
	
	public static String attTable_toString(Dictionary attributes)
	{
		if (attributes == null)
			return "";

		StringBuffer buf = new StringBuffer();
		Enumeration enum = attributes.elements();
		Enumeration att_keys = attributes.keys();
		Object o, a;

		// enumerate keys and values of attributes table.
		while(enum.hasMoreElements())
		{
			o = enum.nextElement();
			a = att_keys.nextElement();
			buf.append(" " + att_toString(a.toString(), o.toString()) );
		}
		return buf.toString();
	}

	/**
	 * convenience method to set a field (String) by name on the given object.
	 * can be invoked by setAttribute().
	 * 
	 * @see java.lang.reflect.Field#set
	 */
	public static void setField(Object o, String fieldName, String value)
		throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
	{
		Field f = o.getClass().getField(fieldName);

		f.set(o, value);
	}
	
	/**
	 * Serialize the public java fields of the object o as attributes of the xml tag.
	 * Returns a string of the form f1="v1" f2="v2" f3="v3" ... where f are the
	 * names of the java fields, and v are the result of invoking toString() on
	 * each field object.
	 * 
	 * invokes att_toString on each public java Field object of
	 * the object o. The resulting String begins with a space.
	 * 
	 * If an IllegalAccessException is caught when trying to access a field,
	 * it is silently skipped.
	 * 
	 * @see java.lang.reflect.Field
	 */
	public static String fields_toString(Object o)
	{
		StringBuffer buf = new StringBuffer();
		java.lang.reflect.Field fs[] = o.getClass().getFields();
		int i;

		for (i=0; i< fs.length; i++) {
			try {
				buf.append(att_toString(fs[i].getName(), fs[i].get(o).toString()));
			} catch (IllegalAccessException iae) {
				continue;
			}
		}

		return buf.toString();
	}

	/**
	 * Default initialization; does nothing other than return true.
	 * When invoked by the parser, this behavior tells the parser to
	 * add this object to the currentContext with its default way.
	 * Subclasses can change that behavior by overriding initialize()
	 * to do it themselves, in which case they should return false
	 * (unless they also want the default behavior).
	 * 
	 * @see org.webcycle.hexml.Parser#foundNewObject
	 */
	public boolean initialize(iParseContext context)
	{
		return true;
	}
	
	/**
	 * old interface - made final so no subclasses can implement it.
	 * @deprecated
	 */
	public final static void initialize() {
		System.err.println("old initialize() - can't be overridden");
	}
}
