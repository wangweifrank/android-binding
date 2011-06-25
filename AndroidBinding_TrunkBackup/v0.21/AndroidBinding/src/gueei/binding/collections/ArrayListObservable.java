package gueei.binding.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ArrayListObservable<T> extends ObservableCollection<T> implements Collection<T> {
	private final Class<T> mType;
	private ArrayList<T> mArray;
	
	public ArrayListObservable(Class<T> type){
		mType = type;
		mArray = new ArrayList<T>();
	}
	
	public T getItem(int position) {
		return mArray.get(position);
	}

	public void onLoad(int position) {
	}

	public void clear(){
		mArray.clear();
		this.notifyCollectionChanged();
	}
	
	public void setArray(T[] newArray){
		mArray.clear();
		int size = newArray.length;
		for (int i=0; i<size; i++){
			mArray.add(newArray[i]);
		}
		this.notifyCollectionChanged();
	}
	

	public void replaceItem(int position, T item){
		mArray.set(position, item);
		this.notifyCollectionChanged();
	}
	
	public int indexOf(T item){
		return mArray.indexOf(item);
	}

	public Class<T> getComponentType() {
		return mType;
	}

	public boolean addAll(Collection<? extends T> arg0) {
		boolean result = mArray.addAll(arg0);
		if (result){
			this.notifyCollectionChanged();
		}
		return result;
	}

	public boolean containsAll(Collection<?> arg0) {
		return mArray.containsAll(arg0);
	}

	public boolean isEmpty() {
		return mArray.isEmpty();
	}

	public Iterator<T> iterator() {
		return mArray.iterator();
	}

	public boolean removeAll(Collection<?> arg0) {
		boolean result = mArray.removeAll(arg0);
		if (result){
			this.notifyCollectionChanged();
		}
		return result;
	}

	public boolean retainAll(Collection<?> arg0) {
		boolean result = mArray.retainAll(arg0);
		if (result){
			this.notifyCollectionChanged();
		}
		return result;
	}

	public int size() {
		return mArray.size();
	}

	public Object[] toArray() {
		return mArray.toArray();
	}

	public <Ta> Ta[] toArray(Ta[] array) {
		return mArray.toArray(array);
	}

	public boolean add(T object) {
		boolean result = mArray.add(object);
		if (result){
			this.notifyCollectionChanged();
		}
		return result;
	}

	public boolean contains(Object object) {
		return mArray.contains(object);
	}

	public boolean remove(Object object) {
		boolean result = mArray.remove(object);
		if (result){
			this.notifyCollectionChanged();
		}
		return result;
	}
}
