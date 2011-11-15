package com.gueei.demo.abgallery.viewModels;

import java.util.Collection;

import gueei.binding.Observable;

/**
 * Observable that may not be initialized until first accessed
 * @author andy
 *
 * @param <T>
 */
public abstract class LazyLoadVM<T> extends Observable<T> {
	public LazyLoadVM(Class<T> type) {
		super(type);
	}

	private boolean inited = false;
	
	@Override
	public T get() {
		if (!inited){
			this.setWithoutNotify(initValue());
			inited = true;
		}
		return super.get();
	}
	
	@Override
	protected void doSetValue(T newValue, Collection<Object> initiators) {
		inited = true;
		super.doSetValue(newValue, initiators);
	}

	protected abstract T initValue();
}
