/*
 * @(#)Trie.java	0.1beta, 02/09/96
 *
 * Copyright (c) 1996 by David Megginson. All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation is hereby granted provided that this copyright
 * notice appears in all copies and that all modifications are clearly
 * marked.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. THE AUTHOR
 * SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 *
 * -- David Megginson, ak117@freenet.carleton.ca
 *                     dmeggins@aix1.uottawa.ca
 *                     dmeggins@uottawa.ca
 */

package CA.carleton.freenet.ak117.util;
import java.util.*;

/**
 * <p>The Trie class, like the Hashtable class, is a descendant of the
 * Dictionary class, and implements all of the same methods.  The
 * value may be any arbitrary Object, but the key <strong>must</strong>
 * belong to a class which supports the toString() method.</p>
 *
 * <p>This example creates a hashtable of numbers, using the names of the
 * numbers as keys:</p>
 *
 * <pre>
 *      Trie numbers = new Trie();
 *      numbers.put("one",new Integer(1));
 *      numbers.put("two",new Integer(2));
 *      numbers.put("three",new Integer(3));
 * </pre>
 *
 * <p>This example will retrieve a number:</p>
 *
 * <pre>
 *      Integer n = (Integer)numbers.get("two");
 *      if (n != null) {
 *          System.out.println("two = " + n);
 *      }
 * </pre>
 *
 * @see java.lang.Dictionary
 * @see java.lang.Hashtable
 * @version 0.1beta, 02/09/96
 * @author David Megginson
 */
public
class Trie extends Dictionary {


    /** 
     * The total number of terminal nodes in this Trie.
     */
    private int count = 0;

    /**
     * The total number of nonterminals in the current node.
     */
    private int nonterminalNodeCount = 0;

    /**
     * The array of nonterminals in the current node.
     */
    private Trie nonterminalNodes[];

    /**
     * If the node has only one nonterminal, this is its character key.
     */
    private char nonterminalChar;

    /**
     * The terminal value for this node, if any.
     */
    private Object terminalValue = null;


    /*
     * PUBLIC INTERFACE
     */
    

    /**
     * Returns the number of elements contained within the Trie.
     * @see java.util.Dictionary#size
     */
    public int size () {
      return count + (terminalValue != null ? 1 : 0);
    }

    /**
     * Returns true if the Trie contains no elements.
     * @see java.util.Dictionary@isEmpty
     */
    public boolean isEmpty () {
      return (size() == 0) ? true : false;
    }

    /**
     * Returns an enumeration of the Tries's keys.
     * @see java.util.Dictionary#elements
     * @see java.util.Enumeration
     */
    public Enumeration keys () {
      Vector list = new Vector();
      keys("",list);
      return list.elements();
    }

    /**
     * Returns an enumeration of the elements. Use the Enumeration methods 
     * on the returned object to fetch the elements sequentially.
     * @see java.util.Dictionary#keys
     * @see java.util.Enumeration
     */
    public Enumeration elements () {
      Vector list = new Vector();
      elements(list);
      return list.elements();
    }

    /**
     * Returns an enumeration of the first characters present in the
     * current Trie.  Use the Enumeration methods on the returned
     * object to fetch the characters sequentially.  Note that the
     * characters will be objects rather than simple types.
     * @see java.util.Enumeration
     * @see java.lang.Character
     */
    public Enumeration chars () {
      Vector list = new Vector();
      chars(list);
      return list.elements();
    }
 

    /**
     * Returns the object associated with the specified key in the Trie.
     * @param key the key in the Trie
     * @returns the element for the key or null if the key
     * 		is not defined in the Trie
     * @see java.util.Dictionary#get
     */
    public Object get (Object key) {
      return get(key.toString().toCharArray(),0);
    }

    public Object get (String key) {
      return get(key.toCharArray(),0);
    }

    public Object get (char key[]) {
      return get(key,0);
    }

    /**
     * @returns the terminal value for this node.
     */
    public Object getTV() {
        return terminalValue;
    }


    /**
     * Returns the sub-Trie (if any) associated with the prefix provided.
     * @param prefix the prefix in the Trie
     * @returns a Trie containing all possible completions for the prefix.
     */
    public Trie getPrefix (Object prefix) {
      return getPrefix(prefix.toString().toCharArray(),0);
    }

    public Trie getPrefix (String prefix) {
      return getPrefix(prefix.toCharArray(),0);
    }

    public Trie getPrefix (char prefix[]) {
      return getPrefix(prefix,0);
    }

    /**
     * Returns the sub-Trie (if any) associated with the specified
     * character.
     * @param ch the prefix character
     * @returns a Trie representing all possible completions
     */
    public Trie getChar (char ch) {
      return getNode(ch);
    }

    /**
     * Puts the specified element into the Trie, using the specified
     * key.  The element may be reTrieved by doing a get() with the same 
     * key.  The key and the element cannot be null.
     * @param key the specified Trie key
     * @param value the specified element 
     * @return the old value of the key, or null if it did not have one.
     * @exception NullPointerException If the value of the specified
     * element is null.
     * @see java.util.Dictionary#get
     */
    public Object put (Object key, Object value) {
      return put(key.toString().toCharArray(),0,value);
    }

    public Object put (String key, Object value) {
      return put(key.toCharArray(),0,value);
    }

    public Object put (char key[], Object value) {
      return put(key,0,value);
    }

    /**
     * Removes the element corresponding to the key. Does nothing if the
     * key is not present.
     * @param key the key that needs to be removed
     * @return the value of key, or null if the key was not found.
     */
    public Object remove (Object key) {
      return remove(key.toString().toCharArray(),0);
    }

    public Object remove (String key) {
      return remove(key.toCharArray(),0);
    }

    public Object remove (char key[]) {
      return remove(key,0);
    }


    /*
     * INTERNAL INTERFACE
     */


    /*
     * Place all of the keys in a vector (depth first).  This one is
     * quite simple to define recursively.  (Note that we must construct
     * the keys manually rather than caching them, since they could be
     * requested from any point in the Trie.)
     */
    void keys (String prefix,Vector list) {
      if (terminalValue != null) {
	list.addElement(prefix);
      }
      if (nonterminalNodeCount == 1) {
	nonterminalNodes[0].keys(prefix+nonterminalChar,list);
      } else if (nonterminalNodeCount >1) {
	for (char i = 0; i < nonterminalNodes.length; i++) {
	  if (nonterminalNodes[i] != null) {
	    nonterminalNodes[i].keys(prefix+i,list);
	  }
	}
      }
    }


    /*
     * Place all of the elements into a vector (depth first).  This
     * one is simple to define recursively.
     */
    void elements (Vector list) {
      if (terminalValue != null) {
	list.addElement(terminalValue);
      }
      if (nonterminalNodeCount > 0) {
	for (int i = 0; i < nonterminalNodes.length; i++) {
	  if (nonterminalNodes[i] != null) {
	    nonterminalNodes[i].elements(list);
	  }
	}
      }
    }


    /*
     * Place all of the first characters into a vector.
     */
    void chars (Vector list) {
      switch (nonterminalNodeCount) {

      case 0:
	return;

      case 1:
	list.addElement(new Character(nonterminalChar));
	return;

      default:
	for (char i = 0; i < 256; i++) {
	  if (nonterminalNodes[i] != null) {
	    list.addElement(new Character(i));
	  }
	}
	return;
      }
    }

    /*
     * Follow the path indicated by the char array, starting at the index,
     * and return the associated terminal Object (or null on failure).
     */
    Object get (char key[],int index) {
      if (index == key.length) {
				// The key is exhausted.
	return terminalValue;
      } else {
				// The key is not exhausted -- recurse if
				// possible, or fail.
	Trie node = getNode(key[index]);
	if (node != null) {
	  return node.get(key,index+1);
	} else {
	  return null;
	}
      }
    }

    /*
     * Returns the sub-Trie with the specified prefix.
     */
    Trie getPrefix(char prefix[],int index) {
      if (index == prefix.length) {
	return this;
      } else {
	char c = prefix[index];
	Trie node = getNode(c);
	if (node != null) {
	  return node.getPrefix(prefix,index+1);
	} else {
	  return null;
	}
      }
    }

    /*
     * Follow the path indicated by the char array, starting at the index.
     * At the end of the path, set the terminal, returning any previous
     * value; otherwise, create a new subnode if necessary.
     */
    Object put (char key[],int index,Object value) {
      Object old = null;
      if (index == key.length) {
				// The key is exhausted.
	old = terminalValue;
	terminalValue = value;
      } else {
				// The key is not exhausted.
	char c = key[index];
	Trie node = getNode(c);
	if (node == null) {
				// Generate a new node, if necessary.
	  node = new Trie();
	  putNode(c,node);
	}
				// Recurse.
	old = node.put(key,index+1,value);
	if (old == null) {
				// Has a new terminal value appeared
				// somewhere in the sub-Trie?
	  count++;
	}
      }
				// Return the old value, if any.
      return old;
    }

    /*
     * Follow the path indicated by the char array, removing the
     * entire subtree as soon as possible.  Return the previous
     * value, if any.
     */
    Object remove (char key[],int index)
	{
		Object old = null;
		if (index == key.length)
		{
			// The key is exhausted.
			old = terminalValue;
			terminalValue = null;
		}
		else {
			// The key is not exhausted.
			char c = key[index];
			Trie node = getNode(c);

			if (node != null)
			{
				// There is nothing to do unless the
				// subnode exists.
				old = node.remove(key,index+1);

				if (old != null) {
					// Has the number of terminal values
					// in the sub-Trie changed?
					count--;
				}

				if (node.size() == 0) {
					// Delete empty nodes.
					delNode(c);
				}
			}
		}
		// Return the old value, if any.
		return old;
	}

    /*
     * Lookup a nonterminal node.
     */
    private Trie getNode (char c) {

      switch (nonterminalNodeCount) {

      case 0:
	return null;

      case 1:
	if (c == nonterminalChar) {
	  return nonterminalNodes[0];
	} else {
	  return null;
	}

      default:
	return nonterminalNodes[c];

      }
    }

    /*
     * Set a nonterminal node.
     */
    private void putNode (char c,Trie node) {
      switch (nonterminalNodeCount) {

      case 0:
	nonterminalNodeCount = 1;
	nonterminalNodes = new Trie[1];
	nonterminalNodes[0] = node;
	nonterminalChar = c;
	break;

      case 1:
	if (c == nonterminalChar) {
	  nonterminalNodes[0] = node;
	} else {
	  Trie current = nonterminalNodes[0];
	  nonterminalNodes = new Trie[256];
	  nonterminalNodes[nonterminalChar] = current;
	  nonterminalNodes[c] = node;
	  nonterminalNodeCount = 2;
	}
	break;
	  
      default:
	if (nonterminalNodes[c] == null) {
	  nonterminalNodeCount++;
	}
	nonterminalNodes[c] = node;
	break;

      }
    }

    /*
     * Remove a nonterminal node.
     */
    private void delNode (char c) {

      switch (nonterminalNodeCount) {

      case 0:
	break;

      case 1:
	if (c == nonterminalChar) {
	  nonterminalNodeCount = 0;
	  nonterminalNodes = null;
	}
	break;

      default:
	if (nonterminalNodes[c] != null) {
	  nonterminalNodes[c] = null;
	  nonterminalNodeCount--;
	  if (nonterminalNodeCount == 1) {
	    for (char i = 0; i < 256; i++) {
	      if (nonterminalNodes[i] != null) {
		Trie node = nonterminalNodes[i];
		nonterminalChar = i;
		nonterminalNodes = new Trie[1];
		nonterminalNodes[0] = node;
		break;
	      }
	    }
	  }
	}
	break;
      }
    }

}


