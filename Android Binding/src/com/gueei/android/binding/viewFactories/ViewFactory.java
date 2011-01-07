package com.gueei.android.binding.viewFactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import com.gueei.android.binding.BindedView;
import com.gueei.android.binding.Binder;
import com.gueei.android.binding.exception.BindingException;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater.Factory;

public class ViewFactory implements Factory {
	
	private Binder mBinder;
	public ViewFactory(Binder binder){
		mBinder = binder;
		factories.add(new Views());
		factories.add(new TextViews());
		factories.add(new CompoundButtons());
		factories.add(new AdapterViews());
		InitFactories();
	}
	
	public void InitFactories(){
		for(BindingViewFactory factory : factories){
			factory.Init();
		}
	}
	
	public ArrayList<BindingViewFactory> factories = new
		ArrayList<BindingViewFactory>();

	public View onCreateView(String name, Context context, AttributeSet attrs) {
		View view = null;
		for(BindingViewFactory factory : factories){
			view = factory.CreateView(name, context, attrs);
			if (view!=null) break;
		}
		if (view==null) return null;
		
		AttributeMap map = new AttributeMap();
		int count = attrs.getAttributeCount();
		BindedView bindedView = new BindedView();
		bindedView.setView(view);

		for(int i=0; i<count; i++){
			String aName = attrs.getAttributeName(i);
			String aValue = attrs.getAttributeValue(BindingViewFactory.defaultNS, aName);
			if (aValue!=null){
				if ("name".equals(aName)){
					if (!mBinder.getNameViewMap().containsKey(aValue)){
						mBinder.getNameViewMap().put(aValue, bindedView);
					}
					else
						Log.e("Binder", "Name must be unique");
				}
				else
					map.put(aName, aValue);
			}
		}
		
		viewAttributes.put(bindedView, map);
		
		for(BindingViewFactory factory : factories){
			factory.CreateViewAttributes(bindedView, mBinder);
		}
		
		return view;
	}

	private HashMap<BindedView, AttributeMap> viewAttributes = 
		new HashMap<BindedView, AttributeMap>();
	
	public void BindView(Object model){

		for(BindedView view:viewAttributes.keySet()){
			AttributeMap attrs = viewAttributes.get(view);
			for(BindingViewFactory factory : factories){
				factory.BindView(view, mBinder, attrs, model);
			}
		}
	}
	
	public class AttributeMap extends HashMap<String, String>{
		private static final long serialVersionUID = 7058620998920658022L;
	}
}
