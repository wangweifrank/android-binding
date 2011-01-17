package com.gueei.android.binding.viewAttributes;

import android.widget.TextView;

import com.gueei.android.binding.ViewAttribute;

public class TextViewAttribute extends ViewAttribute<TextView, CharSequence> {

	public TextViewAttribute(TextView view, String attributeName) {
		super(view, attributeName);
	}

	@Override
	protected void doSet(CharSequence newValue) {
		if (compareCharSequence(newValue, get())) return;
		if (newValue == null){
			view.get().setText("");
			return;
		}
		view.get().setText(cloneCharSequence(newValue));
	}

	@Override
	public CharSequence get() {
		return cloneCharSequence(view.get().getText());
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
}
