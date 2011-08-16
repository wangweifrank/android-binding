package com.gueei.demos.markupDemo.custom_actionbar.observables;


import com.gueei.demos.markupDemo.custom_actionbar.widgets.QuickAction;
import gueei.binding.Observable;


/**
 * User: =ra=
 * Date: 14.08.11
 * Time: 1:00
 */
public class QuickActionObservable extends Observable<QuickAction> {

	public QuickActionObservable() {
		super(QuickAction.class);
	}

	public QuickActionObservable(QuickAction value) {
		super(QuickAction.class, value);
	}
}
