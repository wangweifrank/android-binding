package gueei.binding.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.os.Parcel;
import android.os.Parcelable;

public class ArrayListObservable<T> 
	extends ObservableCollection<T> 
	implements List<T>, Parcelable{
	
	private final Class<T> mType;
	protected ArrayList<T> mArray;
	
	public ArrayListObservable(Class<T> type){
		this(type, null);
	}
	
	public ArrayListObservable(Class<T> type, T[] initArray){
		mType = type;
		mArray = new ArrayList<T>();
		if (initArray!=null)
		{
			for(int i=0; i<initArray.length; i++){
				mArray.add(initArray[i]);
			}
		}
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
	
	public int indexOf(Object item){
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
	
	public T remove(int index){
		T obj = mArray.remove(index);
		this.notifyCollectionChanged();
		return obj;
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

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel dest, int flags) {
		try{
			dest.writeArray(this.toArray());
		}catch(Exception e){
			// The array is not parcelable.. ok?
		}
	}
	
	@Override
	@SuppressWarnings({ "unchecked"})
	public void _setObject(Object newValue, Collection<Object> initiators) {
		if (newValue instanceof ArrayListObservable && this != newValue) { // HERE
			this.clear(); // why to clear
			this.setArray(
					(T[])((ArrayListObservable<T>)newValue).toArray());
		}
	}

	public static final Parcelable.Creator<ArrayListObservable<?>> CREATOR =
			new Parcelable.Creator<ArrayListObservable<?>>() {
				@SuppressWarnings({ "rawtypes", "unchecked" })
				public ArrayListObservable<?> createFromParcel(Parcel source) {
					Object[] arr = source.readArray(this.getClass().getClassLoader());
					return new ArrayListObservable(arr.getClass().getComponentType(), arr);
				}

				public ArrayListObservable<?>[] newArray(int size) {
					return new ArrayListObservable[size];
				}
			};

	public void add(int location, T object) {
	}

	public boolean addAll(int arg0, Collection<? extends T> arg1) {
		return false;
	}

	public T get(int location) {
		return mArray.get(location);
	}

	public int lastIndexOf(Object object) {
		return mArray.lastIndexOf(object);
	}

	public ListIterator<T> listIterator() {
		return mArray.listIterator();
	}

	public ListIterator<T> listIterator(int location) {
		return mArray.listIterator(location);
	}

	public T set(int location, T object) {
		T temp = mArray.set(location, object);
		notifyCollectionChanged();
		return temp; 
	}

	public List<T> subList(int start, int end) {
		return mArray.subList(start, end);
	}

	public void setVisibleChildrenCount(Object setter, int total) {
	}
}
