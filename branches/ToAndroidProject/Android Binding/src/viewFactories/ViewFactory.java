package viewFactories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import com.gueei.android.binding.Binder;

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
			view = factory.CreateView(name, mBinder, context, attrs);
			if (view!=null) break;
		}
		if (view==null) return null;
		
		AttributeMap map = new AttributeMap();
		int count = attrs.getAttributeCount();
		for(int i=0; i<count; i++){
			String aName = attrs.getAttributeName(i);
			String aValue = attrs.getAttributeValue(BindingViewFactory.defaultNS, aName);
			if (aValue!=null){
				map.put(aName, aValue);
			}
		}
		
		viewAttributes.put(view, map);
		
		return view;
	}

	private WeakHashMap<View, AttributeMap> viewAttributes = new WeakHashMap<View, AttributeMap>();
	
	public void BindView(Object model){
		for(View view:viewAttributes.keySet()){
			AttributeMap attrs = viewAttributes.get(view);
			for(BindingViewFactory factory : factories){
				if (factory.BindView(view, mBinder, attrs, model))
					break;
			}
		}
	}
	
	public class AttributeMap extends HashMap<String, String>{
		private static final long serialVersionUID = 7058620998920658022L;
	}
}
