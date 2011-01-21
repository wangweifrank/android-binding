package com.gueei.android.binding.viewAttributes;

import java.util.AbstractCollection;

import android.widget.TextView;

import com.gueei.android.binding.Observable;
import com.gueei.android.binding.ViewAttribute;

public class TextViewAttribute extends ViewAttribute<TextView, CharSequence> {

	public TextViewAttribute(TextView view, String attributeName) {
		super(view, attributeName);
	}

	@Override
	public CharSequence get() {
		return view.get().getText().toString();
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
			if (view.get().getText().length()==0) return;
			view.get().setText("");
			return;
		}
		if (!(newValue instanceof CharSequence)){
			view.get().setText(newValue.toString());
			return;
		}
		if (compareCharSequence((CharSequence)newValue, get())) return;
		view.get().setText(cloneCharSequence((CharSequence)newValue));
	}
}
