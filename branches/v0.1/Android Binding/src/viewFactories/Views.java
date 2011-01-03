package viewFactories;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.listeners.MulticastListener;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;

public class Views implements BindingViewFactory {

	public View CreateView(String name, Binder binder, Context context,
			AttributeSet attrs, Object model) {
		return null;
	}

	public void Init() {
		MulticastListener.Factory.RegisterConstructor(View.OnClickListener.class, OnClickListenerMulticast.class);
	}

	@SuppressWarnings("unchecked")
	public boolean BindView(View view, Binder binder, AttributeSet attrs,
			Object model) {
		try {
				// Bind enabled
				String enabled = attrs.getAttributeValue(
						defaultNS, "enabled");
				if (enabled!=null){
					Field f = model.getClass().getField(enabled);
					Observable<Boolean> prop = (Observable<Boolean>) f.get(model);
					binder.bind(view, "Enabled",
							View.class.getMethod("isEnabled"),
							View.class.getMethod("setEnabled", boolean.class), prop);
				}
				// Bind onClick
				String onClick = attrs.getAttributeValue(
						defaultNS, "click");
				Field command = model.getClass().getDeclaredField(onClick);
				Command c = (Command) command.get(model);
				binder.bindCommand(view, View.OnClickListener.class, c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
