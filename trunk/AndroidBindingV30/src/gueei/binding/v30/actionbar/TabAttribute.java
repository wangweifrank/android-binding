package gueei.binding.v30.actionbar;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import gueei.binding.Attribute;

public abstract class TabAttribute<T> extends Attribute<ActionBar.Tab, T> {
	public TabAttribute(Class<T> type, Tab view, String attributeName) {
		super(type, view, attributeName);
	}
}
