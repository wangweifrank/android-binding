package gueei.binding.listeners;

import android.view.View;

public class OnClickListenerMulticast extends MulticastListener<View.OnClickListener> implements View.OnClickListener {
	public void onClick(View arg0) {
		for(View.OnClickListener l : listeners){
			l.onClick(arg0);
		}
		this.invokeCommands(arg0);
	}

	@Override
	public void registerToView(View v) {
		v.setOnClickListener(this);
	}
}