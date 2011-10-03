package gueei.binding.v30.actionbar;

import android.app.ActionBar;
import gueei.binding.Attribute;

public abstract class ActionBarAttribute<T> extends Attribute<ActionBar, T> {
	public ActionBarAttribute(Class<T> type, ActionBar bar, String attributeName) {
		super(type, bar, attributeName);
	}
}
