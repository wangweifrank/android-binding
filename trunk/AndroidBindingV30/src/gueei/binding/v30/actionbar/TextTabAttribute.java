package gueei.binding.v30.actionbar;

import android.app.ActionBar.Tab;

public class TextTabAttribute extends TabAttribute<CharSequence> {

	public TextTabAttribute(Tab tab) {
		super(CharSequence.class, tab, "text");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null){ 
			getHost().setText("");
			return;
		}
		getHost().setText(newValue.toString());
	}

	@Override
	public CharSequence get() {
		return getHost().getText();
	}
}
