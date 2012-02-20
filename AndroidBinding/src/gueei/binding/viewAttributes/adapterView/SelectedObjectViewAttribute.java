package gueei.binding.viewAttributes.adapterView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnItemSelectedListenerMulticast;

public class SelectedObjectViewAttribute extends ViewAttribute<AdapterView<?>, Object> implements OnItemSelectedListener {
	public SelectedObjectViewAttribute(AdapterView<?> view) {
		super(Object.class, view, "selectedObject");
		Binder.getMulticastListenerForView(view, OnItemSelectedListenerMulticast.class).register(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {	
		if( getView().getSelectedItem() != null ) {
			if( getView().getSelectedItem().equals(newValue))
				return;
		}
		
		mPosition = -1;
		
		if( newValue != null ) {
			int c = getView().getAdapter().getCount();
		
			for( int i=0; i<c; i++ ) {
				Object o = getView().getAdapter().getItem(i);
				if( o == null )
					continue;
				if( o.equals(newValue)) {
					mPosition = i;
					break;
				}
			}
		}

		getView().setSelection(mPosition);
	}

	@Override
	public Object get() {
		if( mPosition == -1 )
			return null;
		
		return getView().getSelectedItem();
	}

	int mPosition = -1;
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		mPosition = pos;
		this.notifyChanged();
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		mPosition = -1;
		this.notifyChanged();
	}
}
