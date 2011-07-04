package gueei.binding.bindingProviders;

import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.BindingMap;
import gueei.binding.BindingType;
import gueei.binding.IObservable;
import gueei.binding.Utility;
import gueei.binding.ViewAttribute;
import gueei.binding.exception.AttributeNotDefinedException;

import java.util.ArrayList;

import android.view.View;


/** 
 * Base class for binding providers. Any special types of views should also inherit this 
 * to provide binding syntax parsing and view attributes creation
 * @author andytsui
 *
 */
public abstract class BindingProvider {
	public static final String BindingNamespace = "http://www.gueei.com/android-binding/";
	public abstract <Tv extends View> ViewAttribute<Tv, ?> createAttributeForView(View view, String attributeId);
	public abstract void bind(View view, BindingMap map, Object model);
	
	protected final boolean bindAttributeWithObservable
		(View view, String viewAttributeName, String statement, Object model){
		IObservable<?> property;
		property = Utility.getObservableForModel(view.getContext(), statement, model);
		if (property!=null){
			try {
				ViewAttribute<?,?> attr = Binder.getAttributeForView(view, viewAttributeName);
				BindingType result = attr.BindTo(property);
				if (result.equals(BindingType.NoBinding)){
					BindingLog.warning("Binding Provider", statement + " cannot setup bind with attribute");
				}
				return true;
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
				return false;
			}
		}else{
			// Bind just the value
			Object value = Utility.getFieldForModel(statement, model);
			try {
				ViewAttribute<?,?> attr = Binder.getAttributeForView(view, viewAttributeName);
				attr._setObject(value, new ArrayList<Object>());
				return true;
			} catch (AttributeNotDefinedException e) {
				e.printStackTrace();
				return false;
			}
		}
	}
	
	protected final void bindViewAttribute(View view, BindingMap map, Object model, String attrName) {
		if (map.containsKey(attrName)){
			bindAttributeWithObservable(view, attrName, map.get(attrName), model);
		}
	}
	
//	protected final void bindCommand(View view, BindingMap map, Object model, 
//			String commandName, Class<? extends MulticastListener<?>> multicastType) {
//		if (map.containsKey(commandName)){
//			Command command = Utility.getCommandForModel(map.get(commandName), model);
//			if (command!=null){
//				MulticastListener<?> listener = Binder.getMulticastListenerForView(view, multicastType);
//				if (listener!=null)
//					listener.register(command);
//			}
//		}
//	}
//
}
