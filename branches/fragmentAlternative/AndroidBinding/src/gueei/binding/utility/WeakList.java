package gueei.binding.utility;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

// experimental replacement of currently used WeakList implementation
// inspired by android reference

public class WeakList<E> extends AbstractList<E> {
	private final List<WeakReference<E>> mReferences = new LinkedList<WeakReference<E>>();
	private final ReferenceQueue<E> mReleasedQueue = new ReferenceQueue<E>();

	@SuppressWarnings("unchecked")
	private void processQueue() {
		WeakReference<E> clearedReference;
		while (null != (clearedReference = (WeakReference<E>) mReleasedQueue
				.poll())) {
			mReferences.remove(clearedReference);
		}
	}

	public WeakList() {
	}

	public WeakList(Collection<E> initialCollection) {
		addAll(0, initialCollection);
	}

	public void add(int index, E element) {
		synchronized (this) {
			mReferences.add(index,
					new WeakReference<E>(element, mReleasedQueue));
		}
	}

	@Override
	public boolean add(E element) {
		synchronized (this) {
			return mReferences
					.add(new WeakReference<E>(element, mReleasedQueue));
		}
	}

	public Iterator<E> iterator() {
		throw new UnsupportedOperationException();
	}

	public int size() {
		synchronized (this) {
			processQueue();
			return mReferences.size();
		}
	}

	public E get(int index) {
		synchronized (this) {
			processQueue();
			return mReferences.get(index).get();
		}
	}

	@Override
	public boolean remove(Object object) {
		synchronized (this) {
			ListIterator<WeakReference<E>> referenceListIterator = mReferences
					.listIterator();
			WeakReference<E> weakReference;
			E element;
			while (referenceListIterator.hasNext()) {
				weakReference = referenceListIterator.next();
				if (null != (element = weakReference.get())
						&& element.equals(object)) {
					return mReferences.remove(weakReference);
				}
			}
			return false;
		}
	}

	public Object[] toArray() {
		synchronized (this) {
			processQueue();
			WeakReference[] weakReferences = mReferences.toArray(new WeakReference[mReferences.size()]);
			int len = weakReferences.length;
			Object[] elementsArray = new Object[len];
			for (int i = 0; i < len; i++) {
				elementsArray[i] = weakReferences[i].get();
			}
			return elementsArray;
		}
	}
}

// public class WeakList<E> extends AbstractList<E> {
//
// /*
// * Sun Public License Notice
// *
// * The contents of this file are subject to the Sun Public License
// * Version 1.0 (the "License"). You may not use this file except in
// * compliance with the License. A copy of the License is available at
// * http://www.sun.com/
// *
// * The Original Code is NetBeans. The Initial Developer of the Original
// * Code is Sun Microsystems, Inc. Portions Copyright 1997-2001 Sun
// * Microsystems, Inc. All Rights Reserved.
// *
// *
// * A simple list which holds only weak references to the original objects.
// * original_author Martin Entlicher Modified to Generic List, while the
// * original is not Generic
// */
// private volatile ArrayList<WeakReference<E>> items;
//
// /** Creates new WeakList */
// public WeakList() {
// items = new ArrayList<WeakReference<E>>();
// }
//
// public WeakList(Collection<E> c) {
// items = new ArrayList<WeakReference<E>>();
// addAll(0, c);
// }
//
// public WeakList(int initCapcacity) {
// items = new ArrayList<WeakReference<E>>(initCapcacity);
// }
//
// public void add(int index, E element) {
// synchronized (this) {
// items.add(index, new WeakReference<E>(element));
// }
// }
//
// public Iterator<E> iterator() {
// throw new UnsupportedOperationException();
// // synchronized(this){
// // return new WeakListIterator();
// // }
// }
//
// public int size() {
// synchronized (this) {
// removeReleased();
// return items.size();
// }
// }
//
// public E get(int index) {
// synchronized (this) {
// return ((WeakReference<E>) items.get(index)).get();
// }
// }
//
// private void removeReleased() {
// synchronized (this) {
// ArrayList<WeakReference<E>> removeList = new ArrayList<WeakReference<E>>();
// for (Iterator<WeakReference<E>> it = items.iterator(); it.hasNext();) {
// WeakReference<E> ref = (WeakReference<E>) it.next();
// if (ref.get() == null)
// removeList.add(ref);
// }
// for (int i = 0; i < removeList.size(); i++) {
// items.remove(removeList.get(i));
// }
// }
// }
//
// public Object[] toArray() {
// synchronized (this) {
// removeReleased();
// @SuppressWarnings("unchecked")
// WeakReference<E>[] itemArray = items.toArray(new WeakReference[0]);
// int len = itemArray.length;
// Object[] eArray = new Object[len];
// for (int i = 0; i < len; i++) {
// eArray[i] = itemArray[i].get();
// }
// return eArray;
// }
// }
//
// @Override
// public boolean remove(Object object) {
// synchronized (this) {
// int len = items.size();
// for (int i = 0; i < len; i++) {
// if (items.get(i).get() == null) {
// items.remove(i);
// return remove(object);
// }
// if (items.get(i).get().equals(object)) {
// items.remove(i);
// return true;
// }
// }
// return false;
// }
// }
//
// @Override
// public boolean add(E object) {
// synchronized (this) {
// return items.add(new WeakReference<E>(object));
// }
// }
// }
