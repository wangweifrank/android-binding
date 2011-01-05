package viewFactories;

import java.lang.reflect.Field;

import viewFactories.ViewFactory.AttributeMap;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.listeners.MulticastListener;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;
import com.gueei.android.binding.listeners.OnKeyListenerMulticast;

public class Views implements BindingViewFactory {

	public View CreateView(String name, Binder binder, Context context,
			AttributeSet attrs) {
		try {
			String prefix = "android.widget.";
			if ((name=="View") || (name=="ViewGroup"))
				prefix = "android.view.";
			if (name.contains("."))
				prefix = null;
			
			View view = LayoutInflater.from(context).createView(name, prefix,
					attrs);
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Binder", e.getMessage());
			return null;
		}
	}

	public void Init() {
	}

	@SuppressWarnings("unchecked")
	public boolean BindView(View view, Binder binder, ViewFactory.AttributeMap attrs,
			Object model) {
		try {
				// Bind enabled
				if (attrs.containsKey("enabled")){
					Field f = model.getClass().getField(attrs.get("enabled"));
					Observable<Boolean> prop = (Observable<Boolean>) f.get(model);
					binder.bind(view, "Enabled",
							View.class.getMethod("isEnabled"),
							View.class.getMethod("setEnabled", boolean.class), prop);
				}
				if (attrs.containsKey("click")){
					// Bind onClick
					Field command = model.getClass().getDeclaredField(attrs.get("click"));
					Command c = (Command) command.get(model);
					binder.bindCommand(view, OnClickListenerMulticast.class, c);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
