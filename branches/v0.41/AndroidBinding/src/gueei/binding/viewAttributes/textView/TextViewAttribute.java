package gueei.binding.viewAttributes.textView;

import gueei.binding.Binder;
import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.listeners.TextWatcherMulticast;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;


public class TextViewAttribute extends ViewAttribute<TextView, CharSequence>
	implements TextWatcher{

	private CharSequence mValue = "";
	
	public TextViewAttribute(TextView view, String attributeName) {
		super(CharSequence.class, view, attributeName);
		if (view instanceof EditText){
			Binder.getMulticastListenerForView(view, TextWatcherMulticast.class)
				.registerWithHighPriority(this);
		}
	}

	@Override
	public CharSequence get() {
		return cloneCharSequence(getView().getText());
	}
	
	private CharSequence cloneCharSequence(CharSequence o){
		return o.subSequence(0, o.length());
	}
	
	private boolean compareCharSequence(CharSequence a, CharSequence b){
		if(a==null||b==null) return false;
		if(a.length()!=b.length()) return false;
		for(int i=0; i<a.length(); i++){
			if (a.charAt(i)!= b.charAt(i)) return false;
		}
		return true;
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		synchronized(this){
			if (newValue == null){
				if (getView().getText().length()==0) return;
				suppressChange = true;
				getView().setText("");
				return;
			}
			if (!(newValue instanceof CharSequence)){
				suppressChange = true;
				getView().setText(newValue.toString());
				return;
			}
			if (compareCharSequence((CharSequence)newValue, get())) return;
			suppressChange = true;
			getView().setText(cloneCharSequence((CharSequence)newValue));
		}
	}
	
	private boolean suppressChange = false;

	public void afterTextChanged(Editable arg0) {
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		//mValue = this.getView().getText().toString();
	}
	
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		synchronized(this){
			Log.i("BinderV30", "onTextChagnged, mV=" + mValue + "\t arg0=" + arg0);
			if (compareCharSequence(mValue, arg0)) return;
			if (!suppressChange){
				mValue = cloneCharSequence(arg0);
				this.notifyChanged();
			}
			suppressChange = false;
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (CharSequence.class.isAssignableFrom(type))
			return BindingType.TwoWay;
		if (type.isAssignableFrom(CharSequence.class))
			return BindingType.TwoWay;
		return BindingType.OneWay;
	}
}
