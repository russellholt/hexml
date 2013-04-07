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

import java.lang.reflect.*;

/**
 * The primary utility class; methods for streaming XML with hexml types.
 */
public class hexml {
	
	/**
	 * name of the public field that names the object (the tag name, typically)
	 */
	public static String HEXMLNAME = "hexmlName";
	
	/**
	 * name of the public field containing the attributes feature
	 */
	public static String HEXMLATTRIBUTES = "hexmlAttributes";
	
	/**
	 * name of the public field elements feature
	 */
	public static String HEXMLELEMENTS = "hexmlElements";
	
//	/**
//	 * name of the public field that contains other Hexml features
//	 */
//	public static String HexmlFeatureSet = "hexmlfeatures";


	/**
	 * Convenience method to get an arbitrary field fname from the Object o
	 * @throws NoSuchFieldException when there is no field named by fname
	 * @throws IllegalAccessException when the field is not public.
	 */
	public static Object getField(String fname, Object o)
		throws NoSuchFieldException, IllegalAccessException
	{
		return o.getClass().getField(fname).get(o);
	}
	
	/**
	 * Convenience method to get the standard Hexml field for storing
	 * the name of an object, which is usually the tag name.
	 * The field should be public and is named by the HEXMLNAME constant.
	 * 
	 * @throws NoSuchFieldException when there is no field named by HEXMLNAME
	 * @throws IllegalAccessException when the field is not public.
	 */
	public static String nameOf(Object o)
		throws NoSuchFieldException, IllegalAccessException
	{
		return (o.getClass().getField(HEXMLELEMENTS).get(o)).toString();
	}
	
	/**
	 * Convenience method to get the standard Hexml field for storing
	 * elements in a container. The field should be public and is named by the
	 * HEXMLELEMENTS constant.
	 * 
	 * @throws NoSuchFieldException when there is no field named by HEXMLELEMENTS
	 * @throws IllegalAccessException when the field is not public.
	 */
	public static HexmlElements elementsOf(Object o)
		throws NoSuchFieldException, IllegalAccessException
	{
		return (HexmlElements) (o.getClass().getField(HEXMLELEMENTS).get(o));
	}

	/**
	 * Convenience method to get the standard Hexml field for storing
	 * attributes. The field should be public and is named by the
	 * HEXMLATTRIBUTES constant.
	 * 
	 * @throws NoSuchFieldException when there is no field named by HEXMLATTRIBUTES
	 * @throws IllegalAccessException when the field is not public.
	 */
	public static iHexmlAttributes attributesOf(Object o)
		throws NoSuchFieldException, IllegalAccessException
	{
		return (iHexmlAttributes) (o.getClass().getField(HEXMLATTRIBUTES).get(o));
	}

//    public static iFeatureSet featuresOf(Object o)
//    {
//        Object field_obj = getField(HexmlConstants.HexmlFeatureSet);
//        
//        if (field_obj instanceof iFeatureSet)
//            return (iFeatureSet) field_obj;
//        
//        return null;
//    }
		
	/**
	 * Stream a collection.
	 * Both attributes and elements may be null.
	 */
	public static String streamContainer(String name, iHexmlAttributes a, HexmlElements e)
	{
		String as = "";
		String es = "";
		int blen = 0;	// final buffer length

		if (a != null) {
			as = a.toString();
			blen += as.length();
		}
		
		if (e != null) {
			es = e.toString();
			blen += es.length();
		}
		
		// 2 names (open and close)
		// 5 bracket chars: <></>
		blen += 2 * name.length() + 5;
		
		StringBuffer buf = new StringBuffer(blen);

		buf.append("<");
		buf.append(name);
		buf.append(as);
		buf.append(">");
		
		buf.append(es);
		
		buf.append("</");
		buf.append(name);
		buf.append(">");

		return buf.toString();
	}
	
	/**
	 * stream an atomic object, of the form
	 * &lt;name attributes /&gt;
	 * 
	 * where attributes could be null.
	 */
	public static String streamAtom(String name, iHexmlAttributes a)
	{
		StringBuffer buf = new StringBuffer();
		buf.append("<");
		buf.append(name);
		
		if (a != null)
			buf.append(a.toString());

		buf.append("/>");
		return buf.toString();
	}
	
	/**
	 * Using reflection, attempt to stream the object.
	 * This looks for fields that represent the name, the attributes,
	 * and the elements by their standard names given by
	 * HEXMLNAME, HEXMLATTRIBUTES, HEXMLELEMENTS.
	 * The object is considered a container if there is a field
	 * for the elements, even if the field is itself null.
	 */
	public static String stream(Object o)
	{
		Class c = o.getClass();
		Field name_f = null, att_f = null, elem_f = null;
		Object name_o=null, att_o=null, elem_o=null;
		try {
			name_f = c.getField(HEXMLNAME);
			name_o = name_f.get(o);
		}
		catch (Exception e1) {
			return "";
		}
		
		try {
			att_f = c.getField(HEXMLATTRIBUTES);
			att_o = att_f.get(o);
		}
		catch (Exception e2) {
			att_o = null;
		}
	
		try {
			elem_f = c.getField(HEXMLELEMENTS);
			elem_o = elem_f.get(o);

			return streamContainer(name_o.toString(),
				 (iHexmlAttributes) att_o,
				 (HexmlElements) elem_o);
		}
		catch (Exception e3) {
			elem_o = null;
			elem_f = null;
		}
		
		return streamAtom(name_o.toString(),
				 (iHexmlAttributes) att_o);		
	}
	
}

