package gueei.binding;

import java.util.AbstractCollection;

public interface Observer {
	public void onPropertyChanged(IObservable<?> prop, AbstractCollection<Object> initiators);
}
