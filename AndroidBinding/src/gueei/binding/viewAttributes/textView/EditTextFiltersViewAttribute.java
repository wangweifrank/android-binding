package gueei.binding.viewAttributes.textView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import android.text.InputFilter;
import android.widget.EditText;

public class EditTextFiltersViewAttribute extends ViewAttribute<EditText, InputFilter[]> {

	public EditTextFiltersViewAttribute(EditText view) {
		super(InputFilter[].class, view, "filters");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if(getView()==null) return;
		if (newValue==null){
			return;
		}
		if (newValue instanceof InputFilter[]){
			getView().setFilters((InputFilter[])newValue);
		}
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public InputFilter[] get() {
		return null;
	}
}
