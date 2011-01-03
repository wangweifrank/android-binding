package viewFactories;

import java.util.ArrayList;

import com.gueei.android.binding.Binder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.LayoutInflater.Factory;

public class ViewFactory implements Factory {
	
	private Binder mBinder;
	private Object model;
	public ViewFactory(Binder binder, Object model){
		mBinder = binder;
		this.model = model;
		factories.add(new Views());
		factories.add(new TextViews());
		factories.add(new CompoundButtons());
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
			view = factory.CreateView(name, mBinder, context, attrs, model);
			if (view!=null) break;
		}
		if (view==null) return null;
		
		for(BindingViewFactory factory : factories){
			if (factory.BindView(view, mBinder, attrs, model))
				break;
		}
		
		return view;
	}

}
