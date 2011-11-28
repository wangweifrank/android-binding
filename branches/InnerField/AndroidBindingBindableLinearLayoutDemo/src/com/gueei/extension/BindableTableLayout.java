package com.gueei.extension;

import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TableLayout;

public class BindableTableLayout extends TableLayout implements IBindableView<BindableTableLayout> {

	public BindableTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BindableTableLayout(Context context) {
		super(context);
		init();
	}
	
	private void init() {
	}

	@Override
	public ViewAttribute<BindableTableLayout, ?> createViewAttribute(String attributeId) {
		return null;
	}

}
