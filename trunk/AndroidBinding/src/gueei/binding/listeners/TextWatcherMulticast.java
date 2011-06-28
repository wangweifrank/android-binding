package gueei.binding.listeners;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

public class TextWatcherMulticast
	extends MulticastListener<TextWatcher>
	implements TextWatcher{

	private View mView;
	
	@Override
	public void registerToView(View v) {
		if (TextView.class.isInstance(v)){
			((TextView)v).addTextChangedListener(this);
		}
		mView = v;
	}

	public void afterTextChanged(Editable arg0) {
		for(TextWatcher l : listeners){
			l.afterTextChanged(arg0);
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int before,
			int count) {
		for(TextWatcher l : listeners){
			l.beforeTextChanged(s, start, before, count);
		}
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		for(TextWatcher l : listeners){
			l.onTextChanged(s, start, before, count);
		}
		this.invokeCommands(mView, s, start, before, count);
	}
}