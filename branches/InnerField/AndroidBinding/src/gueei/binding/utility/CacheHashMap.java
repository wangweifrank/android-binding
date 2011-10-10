package gueei.binding.utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A HashMap that will have fixed size, 
 * once the allocated size is over, the
 * oldest entry will be removed
 * @author andy
 *
 * @param <E>
 */
public class CacheHashMap<K,V> extends HashMap<K,V> {

	private static final long serialVersionUID = 1L;

	private ArrayList<K> keyList = new ArrayList<K>();

	private int mCacheSize = 50;
	
	public CacheHashMap(int cacheSize){
		mCacheSize = cacheSize;
	}
	
	@Override
	public V put(K key, V value) {
		// Check whether it is oversize
		while (keyList.size() >= mCacheSize){
			// Pop out oldest item
			K removed = keyList.remove(0);
			this.remove(removed);
		}
		
		keyList.add(key);
		return super.put(key, value);
	}
}
