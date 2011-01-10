package com.gueei.android.binding.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

public class TextWatcherMulticast
	extends MulticastListener<TextWatcher>
	implements TextWatcher{

	private View mView;
	
	@Override
	public void registerToView(View v) {
		mView = v;
		if (TextView.class.isInstance(v)){
			((TextView)v).addTextChangedListener(this);
		}
	}

	public void afterTextChanged(Editable arg0) {
	}

	private CharSequence original;
	public void beforeTextChanged(CharSequence s, int start, int before,
			int count) {
		original = cloneCharSequence(((TextView)mView).getText());
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!compareCharSequence(original, s)){
			this.invoke(mView, s, start, before, count);
		}
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