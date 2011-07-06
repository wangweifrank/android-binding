package gueei.binding;

import gueei.binding.listeners.MulticastListener;
import android.view.View;

/*
 * IBindableView is the interface that supports Custom Views
 * (For views that is not supported in Android-Binding)
 * For any custom view, you should implement this interface 
 * in order for the Attribute Binder to recognize it.
 * 
 * By Design, the Attribute Binder would put custom ViewAttributes to higher
 * precedence, for example, if you want to override the default behavior
 * of the "visibility" tag, you can capture that value and provide another ViewAttribute
 * associated with that tag. In this case, Attribute Binder will stop looking for any 
 * other "parent" to create this attribute. 
 */
public interface IBindableView<T extends View & IBindableView<T>> {
	public enum AttributeHandlingMethod{
		Command, ViewAttribute, Ignore
	}
	
	/** Implementation is responsible to tell what type is the provided attributeName
	 * is binding. 
	 * Recommended naming style is:
	 * View Attributes: viewAttributeName
	 * Commands: onEventName 
	 * 
	 * @param attributeName the name of the attribute to bind to.
	 * @return AttributeHandlingMethod
	 * Attribute, Command or Ignore to anything unknown (will be handle by super class)
	 */
	public AttributeHandlingMethod getAttributeHandlingMethod(String attributeName);
	
	/**
	 * The view class is recommended to create their View Attributes
	 * If you want to override the default behavior of other ViewAttributes, you can return it here
	 * or else, returning null will pass the control to super classes' implementation
	 * @param attributeName
	 * @return the ViewAttribute, or null if don't want to handle
	 */
	public ViewAttribute<T, ?> getViewAttribute(String attributeId);

	/**
	 * Provide the type of the multicast listener that associated with the command name
	 * @param attributeName
	 * @return
	 */
	public MulticastListener<?> getMulticastListener(String attributeId);
}