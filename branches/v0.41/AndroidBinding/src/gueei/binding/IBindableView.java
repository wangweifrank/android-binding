package gueei.binding;

import android.view.View;

/*
 * IBindableView is the interface that supports Custom Views
 * (For views that is not supported in Android-Binding)
 * For any custom view, you should implement this interface 
 * in order for the Attribute BinderV30 to recognize it.
 * 
 * By Design, the Attribute BinderV30 would put custom ViewAttributes to higher
 * precedence, for example, if you want to override the default behavior
 * of the "visibility" tag, you can capture that value and provide another ViewAttribute
 * associated with that tag. In this case, Attribute BinderV30 will stop looking for any 
 * other "parent" to create this attribute. 
 */
public interface IBindableView<T extends View & IBindableView<T>> {
	/**
	 * The View are required to create and manage the life cycle of the view Attribute.
	 * Every time the Binder.getViewAttribute() is called, it will look for the View Attribute in this,
	 * so it is the responsibility for the custom class to return the same instance of the View Attribute
	 * every time it is called.
	 * If you want to override the default behavior of other ViewAttributes, you can return it here
	 * or else, returning null will pass the control to super classes' implementation
	 * @param attributeName
	 * @return the ViewAttribute, or null if don't want to handle
	 */
	public ViewAttribute<T, ?> getViewAttribute(String attributeId);
}