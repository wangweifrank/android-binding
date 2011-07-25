package gueei.binding.viewAttributes.adapterView;

import android.widget.Adapter;
import android.widget.AdapterView;
import gueei.binding.ViewAttribute;

public class AdapterViewAttribute extends ViewAttribute<AdapterView<Adapter>, Adapter> {
	public AdapterViewAttribute(AdapterView<Adapter> view) {
		super(Adapter.class, view, "adapter");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof Adapter){
			getView().setAdapter((Adapter)newValue);
		}
	}

	@Override
	public Adapter get() {
		return getView().getAdapter();
	}
}