package gueei.binding.test;

import gueei.binding.exception.AttributeNotDefinedException;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;

public class ConverterTest extends ActivityInstrumentationTestCase2<TestButtonActivity> {

	public ConverterTest() {
	    super(TestButtonActivity.class);
    }
	
	public void test_IF_Switchable_Command() throws AttributeNotDefinedException{
		getActivity().runOnUiThread(new Runnable(){
			public void run() {
				assertEquals(3, (int)getActivity().HelloWorld.get());
				((Button)getActivity().findViewById(R.id.button_say)).performClick();				
				assertEquals(0, (int)getActivity().HelloWorld.get());
				getActivity().IsHello.set(false);
				((Button)getActivity().findViewById(R.id.button_say)).performClick();
				assertEquals(1, (int)getActivity().HelloWorld.get());
			}
		});
	}
}