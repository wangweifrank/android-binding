package gueei.binding.test;

import gueei.binding.IObservable;
import gueei.binding.InnerFieldObservable;
import gueei.binding.Observable;
import gueei.binding.Observer;
import gueei.binding.observables.StringObservable;

import java.util.ArrayList;
import java.util.Collection;

import android.test.AndroidTestCase;

@SuppressWarnings("rawtypes")
public class InnerFieldObservableTest extends AndroidTestCase {
	
	// Simple get and set
	public void testInnerField_OA_OB(){
		InnerFieldObservable<?> ifo = new InnerFieldObservable("TestObservable.STRING");
		ifo.createNodes(this);
		assertEquals("test", ifo.get());
		ifo._setObject("hello", new ArrayList<Object>());
		assertEquals("hello", ifo.get());
	}
	
	// Field is readonly
	public void testInnerField_OA_FB(){
		InnerFieldObservable<?> ifo = new InnerFieldObservable("TestObservable.FIELD");
		ifo.createNodes(this);
		assertEquals("StringField", ifo.get());
		ifo._setObject("hello", new ArrayList<Object>());
		assertNotSame("hello", ifo.get());
	}
	
	// This is getting complicated
	public void testInnerField_OA_OB_OC(){
		InnerFieldObservable<?> ifo = new InnerFieldObservable("TestObservable.TESTB.STRINGB");
		ifo.createNodes(this);
		assertEquals("test", ifo.get());
		ifo._setObject("hello", new ArrayList<Object>());
		assertEquals("hello", ifo.get());
		
		// Now modify B to see if it works
		TestObservable.get().TESTB.set(new TestClassB());
		assertEquals("test", ifo.get());
		ifo._setObject("hello", new ArrayList<Object>());
		assertEquals("hello", ifo.get());
		
		// Now modify A to see
		TestObservable.set(new TestClassA());
		assertEquals("test", ifo.get());
	}
	
	// This is getting complicated
	public void testInnerField_OA_OB_OC_OD(){
		InnerFieldObservable<?> ifo = new InnerFieldObservable("TestObservable.TESTB.TESTC.STRINGC");
		ifo.createNodes(this);
		assertEquals("test", ifo.get());
		ifo._setObject("hello", new ArrayList<Object>());
		assertEquals("hello", ifo.get());
		
		// Now modify B to see if it works
		TestObservable.get().TESTB.set(new TestClassB());
		assertEquals("test", ifo.get());
		ifo._setObject("hello", new ArrayList<Object>());
		assertEquals("hello", ifo.get());
		
		// Now modify A to see
		TestObservable.set(new TestClassA());
		assertEquals("test", ifo.get());
	}
		
	public void testInnerField_Notification_OA_OB_OC(){
		InnerFieldObservable<String> ifo = new InnerFieldObservable("TestObservable.TESTB.STRINGB");
		ifo.createNodes(this);
		ifo.subscribe(new Observer(){
			@Override
			public void onPropertyChanged(IObservable<?> prop,
					Collection<Object> initiators) {
				
			}
		});
	}
	
	// Observable A -> Field B -> Observable C
	public void testInnerField_OA_FB_OC(){
		InnerFieldObservable<?> ifo = new InnerFieldObservable("TestObservable.FIELDB.STRINGB");
		ifo.createNodes(this);
		assertEquals("test", ifo.get());
		ifo._setObject("hello", new ArrayList<Object>());
		assertEquals("hello", ifo.get());
	}
	
	
	public final Observable<TestClassA> TestObservable =
			new Observable<TestClassA>(TestClassA.class, new TestClassA());
	
	public static class TestClassA{
		public final Observable<TestClassB> TESTB = 
				new Observable<TestClassB>(TestClassB.class, new TestClassB());
		
		public TestClassB FIELDB = new TestClassB();
		
		public final StringObservable STRING = new StringObservable("test");
		public String FIELD = "StringField";
	}
	
	public static class TestClassB{
		public final StringObservable STRINGB = new StringObservable("test");
		public final Observable<TestClassC> TESTC = 
				new Observable<TestClassC>(TestClassC.class, new TestClassC());
	}
	
	public static class TestClassC{
		public final StringObservable STRINGC = new StringObservable("test");
	}
}
