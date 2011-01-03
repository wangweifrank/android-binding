package viewFactories;

import java.lang.reflect.Field;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Command;
import com.gueei.android.binding.Observable;
import com.gueei.android.binding.listeners.MulticastListener;
import com.gueei.android.binding.listeners.OnClickListenerMulticast;

public class CompoundButtons implements BindingViewFactory {

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
				String checked = attrs.getAttributeValue(
						defaultNS, "checked");
				if (checked!=null){
					Field f = model.getClass().getField(checked);
					Observable<Boolean> prop = (Observable<Boolean>) f.get(model);
					binder.bind(view, "Checked",
							CompoundButton.class.getMethod("isChecked"),
							CompoundButton.class.getMethod("setChecked", boolean.class), prop);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
