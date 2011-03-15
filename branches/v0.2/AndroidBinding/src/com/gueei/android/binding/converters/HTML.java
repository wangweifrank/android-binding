package com.gueei.android.binding.converters;

import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.gueei.android.binding.Converter;
import com.gueei.android.binding.DependentObservable;
import com.gueei.android.binding.IObservable;

/**
 * Styles, Convert HTML Charsequence into Styled Spanned. 
 * Takes only one argument
 * If input is already a span, it will be ignored
 */
public class HTML extends DependentObservable<Spanned> {
	public HTML(IObservable<?>[] dependents) {
		super(Spanned.class, dependents);
	}

	@Override
	public Spanned calculateValue(Object... args) throws Exception {
		if ((args.length<1)||(args[0]==null)) throw new IllegalArgumentException();
		return Html.fromHtml(args[0].toString());
	}
}
