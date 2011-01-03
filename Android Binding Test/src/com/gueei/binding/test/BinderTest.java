package com.gueei.binding.test;

import com.gueei.android.binding.Binder;
import com.gueei.android.binding.Observable;

import android.test.InstrumentationTestCase;
import android.util.Log;
import android.widget.TextView;

public class BinderTest extends InstrumentationTestCase {

	private Binder binder = new Binder();
	
	public void testBindTextView() {
		TextView view = new TextView(this.getInstrumentation().getContext());
		Observable<CharSequence> observableProperty = new Observable<CharSequence>("HELLO");
		Log.d("Binder", "Class name: " + view.getClass().getName());
		
		binder.bind(view, "Text", observableProperty);
		assertEquals("HELLO", view.getText());
		
		observableProperty.set("Good");
		assertEquals("Good", view.getText());
	}
	
	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}
	
}
