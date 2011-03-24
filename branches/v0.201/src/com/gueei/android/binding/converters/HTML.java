package com.gueei.android.binding.converters;

import android.text.Html;
import android.text.Spanned;

import com.gueei.android.binding.IObservable;

/**
 * Styles, Convert HTML Charsequence into Styled Spanned. 
 * Takes only one argument
 * If input is already a span, it will be ignored
 */
public class HTML extends CONCAT {
	public HTML(IObservable<?>[] dependents) {
		super(dependents);
	}

	@Override
	public Spanned calculateValue(Object... args) throws Exception {
		return Html.fromHtml(super.calculateValue(args).toString());
	}
}
