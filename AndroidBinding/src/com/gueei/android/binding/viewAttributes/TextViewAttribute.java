package com.gueei.android.binding.viewAttributes;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.EditText;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.BindingLog;
import com.gueei.android.binding.BindingType;
import com.gueei.android.binding.ViewAttribute;
import com.gueei.android.binding.listeners.TextWatcherMulticast;

public class TextViewAttribute extends ViewAttribute<TextView, String>
	implements TextWatcher{

	public TextViewAttribute(TextView view, String attributeName) {
		super(String.class, view, attributeName);
		if (view instanceof EditText){
			Binder.getMulticastListenerForView(view, TextWatcherMulticast.class)
				.register(this);
		}
	}

	@Override
	public String get() {
		return getView().getText().toString();
	}
	
	private CharSequence cloneCharSequence(CharSequence o){
		return o.subSequence(0, o.length());
	}
	
	private boolean compareCharSequence(CharSequence a, CharSequence b){
		if(a==null||b==null) return false;
		if(a.length()!=b.length()) return false;
		for(int i=0; i<a.length(); i++){
			if (a.charAt(i)!= b.charAt(i)) return false;
		}
		return true;
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null){
			if (getView().getText().length()==0) return;
			getView().setText("");
			return;
		}
		if (!(newValue instanceof CharSequence)){
			getView().setText(newValue.toString());
			return;
		}
		if (compareCharSequence((CharSequence)newValue, get())) return;
		getView().setText(cloneCharSequence((CharSequence)newValue));
	}
	
	

	public void afterTextChanged(Editable arg0) {
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		mValue = this.getView().getText().toString();
	}
	
	private String mValue;

	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if (!arg0.toString().equals(mValue)){
			BindingLog.warning("TextViewAttribute", "onchange");
			this.notifyChanged();
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (CharSequence.class.isAssignableFrom(type))
			return BindingType.TwoWay;
		if (String.class.isAssignableFrom(type))
			return BindingType.TwoWay;
		return BindingType.OneWay;
	}
}
