package gueei.binding;

import java.util.Collection;
import java.util.ArrayList;

public abstract class DependentObservable<T> extends Observable<T> implements Observer{

	protected IObservable<?>[] dependents;
	
	public DependentObservable(Class<T> type, IObservable<?>... dependents) {
		super(type);
		for(IObservable<?> o : dependents){
			o.subscribe(this);
		}
		this.dependents = dependents;
		this.onPropertyChanged(null, new ArrayList<Object>());
	}

	public abstract T calculateValue(Object... args) throws Exception;
	
	public final void onPropertyChanged(IObservable<?> prop,
			Collection<Object> initiators) {
		dirty = true;
		initiators.add(this);
		this.notifyChanged(initiators);
	}

	private boolean dirty = false;
	
	@Override
	public T get() {
		if (dirty){
			int len = dependents.length;
			Object[] values = new Object[len];
			for(int i=0; i<len; i++){
				values[i] = dependents[i].get();
			}
			try{
				T value = this.calculateValue(values);
				this.setWithoutNotify(value);
			}catch(Exception e){
				BindingLog.exception
					("DependentObservable.CalculateValue()", e);
			}
			dirty = false;
		}
		return super.get();
	}
}