package gueei.binding.utility;

/*
 *                 Sun Public License Notice
 * 
 * The contents of this file are subject to the Sun Public License
 * Version 1.0 (the "License"). You may not use this file except in
 * compliance with the License. A copy of the License is available at
 * http://www.sun.com/
 * 
 * The Original Code is NetBeans. The Initial Developer of the Original
 * Code is Sun Microsystems, Inc. Portions Copyright 1997-2001 Sun
 * Microsystems, Inc. All Rights Reserved.
 */

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A simple list which holds only weak references to the original objects.
 * original_author  Martin Entlicher
 * Modified to Generic List, while the original is not Generic 
 */
public class WeakList<E> extends AbstractList<E> {
    
    private ArrayList<WeakReference<E>> items;

    /** Creates new WeakList */
    public WeakList() {
        items = new ArrayList<WeakReference<E>>();
    }
    
    public WeakList(Collection<E> c) {
        items = new ArrayList<WeakReference<E>>();
        addAll(0, c);
    }

    public WeakList(int initCapcacity) {
        items = new ArrayList<WeakReference<E>>(initCapcacity);
    }

    
    public void add(int index, E element) {
        items.add(index, new WeakReference<E>(element));
    }
    
    public Iterator<E> iterator() {
        return new WeakListIterator();
    }
    
    public int size() {
        removeReleased();
        return items.size();
    }    
    
    public E get(int index) {
        return ((WeakReference<E>) items.get(index)).get();
    }
    
    private void removeReleased() {
        for (Iterator<WeakReference<E>> it = items.iterator(); it.hasNext(); ) {
            WeakReference<E> ref = (WeakReference<E>) it.next();
            if (ref.get() == null) items.remove(ref);
        }
    }
    
    private class WeakListIterator implements Iterator<E> {
        
        private int n;
        private int i;
        
        public WeakListIterator() {
            n = size();
            i = 0;
        }
        
        public boolean hasNext() {
            return i < n;
        }
        
        public E next() {
            return get(i++);
        }
        
        public void remove() {
        	if (i-1 >= 0)
        		items.remove(i-1);
        }
        
    }

	@Override
	public boolean remove(Object object) {
		int len = items.size();
		for(int i=0; i<len; i++){
			if (items.get(i).get().equals(object)){
				items.remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean add(E object) {
		return items.add(new WeakReference<E>(object));
	}
}
