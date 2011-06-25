package gueei.binding.listeners;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OnSeekBarChangeListenerMulticast
	extends MulticastListener<CompoundButton.OnCheckedChangeListener>
	implements CompoundButton.OnCheckedChangeListener{

	@Override
	public void registerToView(View v) {
		if (CompoundButton.class.isInstance(v)){
			((CompoundButton)v).setOnCheckedChangeListener(this);
		}
	}

	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		for(OnCheckedChangeListener l: listeners){
			l.onCheckedChanged(arg0, arg1);
		}
		if (this.isFromUser()){
			this.invokeCommands(arg0, arg1);
		}
		this.clearBroadcastState();
	}
}
