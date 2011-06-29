package gueei.binding;

/*
 * BindableView is the interface that supports Custom Views
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
public interface BindableView {

}
