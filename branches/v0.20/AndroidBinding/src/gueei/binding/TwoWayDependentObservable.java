package gueei.binding;

import java.util.AbstractCollection;

public abstract class TwoWayDependentObservable<T> extends DependentObservable<T> {

	public TwoWayDependentObservable(Class<T> type, IObservable<?>... dependents){
		super(type, dependents);
	}
	
	/**
	 * 
	 * @param value The value to convert back
	 * @param outResult Output of the converted result
	 * @return true if the converter can handle, false if doesn't
	 */
	public abstract boolean ConvertBack(Object value, Object[] outResult);

	@Override
	protected void doSetValue(T newValue, AbstractCollection<Object> initiators) {
		int count = dependents.length;
		Object[] outResult = new Object[count];
		if (!ConvertBack(newValue, outResult)) return;
		for(int i=0; i<count; i++){
			dependents[i]._setObject(outResult[i], initiators);
		}
	}
}