package gueei.binding.viewAttributes.textView;


import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnClickListenerMulticast;
import android.view.View;
import android.widget.CheckedTextView;


public class CheckedViewAttribute extends ViewAttribute<CheckedTextView, Boolean>
	implements View.OnClickListener {

	// we have to use the click handler here, because there is no Checked Listener for CheckedTextViews	
	
	public CheckedViewAttribute(CheckedTextView view) {
		super(Boolean.class, view, "checked");
		Binder.getMulticastListenerForView(view, OnClickListenerMulticast.class)
			.register(this);
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		boolean changeTo = getView().isChecked();
		if (newValue==null){
			changeTo = false;
		}
		if (newValue instanceof Boolean){
			changeTo = (Boolean)newValue;
		}
		if (newValue instanceof Number){
			changeTo = !((Number)newValue).equals(0);
		}
		if (changeTo != getView().isChecked()){
			Binder.getMulticastListenerForView(getView(), OnClickListenerMulticast.class).nextActionIsNotFromUser();
			getView().setChecked(changeTo);
		}
	}

	@Override
	public Boolean get() {
		return getView().isChecked();
	}

	@Override
	public void onClick(View v) {
		if( !getView().isEnabled() )
			return;
		this.notifyChanged();		
	}
}