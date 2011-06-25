package gueei.binding.bindingProviders;

import gueei.binding.BindingMap;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.OnRatingBarChangeListenerMulticast;
import gueei.binding.viewAttributes.RatingViewAttribute;
import android.view.View;
import android.widget.RatingBar;


public class RatingBarProvider extends BindingProvider {
	@SuppressWarnings("unchecked")
	@Override
	public <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId) {
		if (!(view instanceof RatingBar)) return null;
		if (attributeId.equals("rating")){
			RatingViewAttribute attr = new RatingViewAttribute((RatingBar)view);
			return (ViewAttribute<Tv, ?>)attr;
		}
		return null;
	}


	@Override
	public void bind(View view, BindingMap map, Object model) {
		if (!(view instanceof RatingBar)) return;
		bindViewAttribute(view, map, model, "rating");
		bindCommand(view, map, model, "onRatingChanged", OnRatingBarChangeListenerMulticast.class);
	}
}
