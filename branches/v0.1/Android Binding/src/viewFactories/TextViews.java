package viewFactories;

import java.lang.reflect.Field;

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

public class TextViews implements BindingViewFactory {

	public View CreateView(String name, Binder binder, Context context,
			AttributeSet attrs, Object model) {
		try {
			String prefix = "android.widget.";
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
	public boolean BindView(View view, Binder binder, AttributeSet attrs,
			Object model) {
		try {
			if (TextView.class.isInstance(view)) {
				// Bind text
				String text = attrs.getAttributeValue(
						defaultNS, "text");
				Field f = model.getClass().getField(text);
				Observable<CharSequence> prop = (Observable<CharSequence>) f.get(model);
				binder.bind(view, "Text",
						TextView.class.getMethod("getText"),
						TextView.class.getMethod("setText", CharSequence.class), prop);
			}
		} catch (Exception e) {
			e.printStackTrace();
			
		}
		return false;
	}

}
