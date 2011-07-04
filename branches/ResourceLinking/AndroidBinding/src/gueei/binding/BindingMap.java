package gueei.binding;

import java.util.HashMap;

public class BindingMap{
	private HashMap<String, Entry> mEntries = new HashMap<String, Entry>();
	
	public String put(String key, String value) {
		mEntries.put(key, new Entry(value));
		return null;
	}

	public boolean containsKey(String key){
		if (!mEntries.containsKey(key)) return false;
		return !mEntries.get(key).handled;
	}
	
	public String get(String key){
		return get(key, false);
	}

	public String get(String key, boolean inclHandled){
		Entry entry = mEntries.get(key);
		if (entry==null) return null;
		
		return !entry.handled || inclHandled ? entry.name : null;
	}

	public String[] getAllKeys(){
		int len = mEntries.size();
		String[] result = new String[len];
		int i=0; 
		for(String s: mEntries.keySet()){
			result[i] = s;
			i ++;
		}
		return result;
	}
	
	public String[] getAllEntries(){
		int len = mEntries.size();
		String[] result = new String[len];
		int i=0;
		for(Entry e : mEntries.values())
		{
			result[i] = e.name;
			i ++;
		}
		return result;
	}
	
	public boolean isEmpty(){
		return mEntries.isEmpty();
	}
	
	public void setAsHandled(String key){
		Entry entry = mEntries.get(key);
		if (entry==null) return;
		entry.handled = true;
	}
	
	private class Entry{
		public Entry(String name){
			this.name = name;
		}
		public String name;
		public boolean handled = false;
		
		@Override
		public String toString(){
			return name + "_handled=" + handled;
		}
	}
}