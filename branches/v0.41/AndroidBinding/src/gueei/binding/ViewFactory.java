package gueei.binding;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.View;


public class ViewFactory implements Factory {
	
	public static final String BINDING_NAMESPACE = "http://schemas.android.com/apk/res/gueei.binding";
	
	private LayoutInflater mInflater;
	
	private ArrayList<View> processedViews = new ArrayList<View>();
	
	public ViewFactory(LayoutInflater inflater){
		this.mInflater = inflater;
	}
		
	protected View CreateViewByInflater(String name, Context context,
			AttributeSet attrs) {
		try {
			String viewFullName = "android.widget." + name;
			if ((name=="View") || (name=="ViewGroup"))
				viewFullName = "android.view." + name;
			else if (name.startsWith("binding.")){
				viewFullName = "gueei.binding.widgets." + name.substring(name.indexOf('.') + 1);
			}else if (name.contains("."))
				viewFullName = name;
			
			View view = mInflater.createView(viewFullName, null,
					attrs);
			return view;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		View view = this.CreateViewByInflater(name, context, attrs);
		if (view==null) return null;
		
		BindingMap map = new BindingMap();
		int count = attrs.getAttributeCount();
		for(int i=0; i<count; i++){
			String attrName = attrs.getAttributeName(i);
			String attrValue = attrs.getAttributeValue(Binder.BINDING_NAMESPACE, attrName);
			if (attrValue!=null){
				map.put(attrName, attrValue);
			}
		}

		// Only save those with binding attributes
		if (!map.isEmpty()){
			Binder.putBindingMapToView(view, map);
			processedViews.add(view);
		}
		
		return view;
	}
	
	public ArrayList<View> getProcessedViews(){
		return processedViews;
	}
}
