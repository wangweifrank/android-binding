package com.gueei.extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingSyntaxResolver;
import gueei.binding.ConstantObservable;
import gueei.binding.IBindableView;
import gueei.binding.IObservable;
import gueei.binding.InnerFieldObservable;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.collections.DependentCollectionObservable;
import gueei.binding.converters.ITEM_LAYOUT;
import gueei.binding.utility.WeakList;
import com.gueei.extension.ListIntersection;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BindableLinearLayout extends LinearLayout implements IBindableView<BindableLinearLayout> {


	public BindableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BindableLinearLayout(Context context) {
		super(context);
		init();
	}
	
	private void init() {
	}
	
	@SuppressWarnings("unused")
	private DependentCollectionObservable<Boolean> listIsEmpty = null;
	private WeakList<Object> weakList = null;
		
	private void createItemSourceList(ArrayListObservable<Object> list) {
		if(list==null)
			return;
		
		weakList = null;
					
		listIsEmpty = new DependentCollectionObservable<Boolean>(Boolean.class, list) {
			@Override
			public Boolean calculateValue(Object... args) throws Exception {
				if( args.length == 0) return false;
				if( !(args[0] instanceof ArrayListObservable<?>))
					return false;		
								
				@SuppressWarnings("unchecked")
				ArrayListObservable<Object> list = (ArrayListObservable<Object>)args[0];
				
				// the list has changed - do comparison with our saved weaklist
				listChanged(list);				
				
				if( list == null || list.size() == 0)
					return true;
				return false;
			}
		};

		// call list changed to update the layout
		listChanged(list);			
		weakList = new WeakList<Object>(list);			
	}	
	
	ArrayListObservable<Object> theList = null;	
	private ViewAttribute<BindableLinearLayout, Object> ItemSourceAttribute = 
			new ViewAttribute<BindableLinearLayout, Object>(Object.class, BindableLinearLayout.this, "ItemSource") {		
				@SuppressWarnings("unchecked")
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if( !(newValue instanceof ArrayListObservable<?> ))
						return;
					theList = (ArrayListObservable<Object>)newValue;
					
					if( layout != null )
						createItemSourceList(theList);
				}

				@Override
				public Object get() {
					return null;
				}				
	};	
			
	private ITEM_LAYOUT.ItemLayout layout = null;			
	private ViewAttribute<BindableLinearLayout, Object> ItemLayoutAttribute =
			new ViewAttribute<BindableLinearLayout, Object>(Object.class, BindableLinearLayout.this, "ItemLayout"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					layout = null;
					if( newValue instanceof ITEM_LAYOUT.ItemLayout ) {
						layout = (ITEM_LAYOUT.ItemLayout) newValue;
						if( theList != null )
							createItemSourceList(theList);
					}
				}

				@Override
				public Object get() {
					return null;
				}

			};				

	@Override
	public ViewAttribute<BindableLinearLayout, ?> createViewAttribute(String attributeId) {	
		
		// should we support this?
		
		// "itemSource"
		// "selectedItem"
		// "selectedPosition"
		
		if (attributeId.equals("itemSource")) return ItemSourceAttribute;
		if (attributeId.equals("itemLayout")) return ItemLayoutAttribute;
		return null;
	}
	
	private void listChanged(List<Object> currentList) {		
		ArrayList<Object> intersectWeakList = null;
		if( weakList != null )
			intersectWeakList = new ArrayList<Object>(Arrays.asList(weakList.toArray()));		
		ListIntersection<Object> inter = new ListIntersection<Object>();
		
		List<Object> intersect = inter.Intersection(intersectWeakList, currentList);	// equal in both
		List<Object> deleteList = inter.NotIn(intersectWeakList, intersect);
		List<Object> newList = inter.NotIn(currentList, intersect);
		
		if( intersectWeakList == null || intersectWeakList.size() == 0) {
			// check if this is our start			
			newList = currentList;
		} else if( intersectWeakList != null && intersectWeakList.size() >0 && (currentList == null || currentList.size() == 0)) {
			// check if this is the final remove
			deleteList = intersectWeakList;
		} else {		
			intersectWeakList.clear();
			intersect.clear();
		}
		
		// delete old items
		deleteOldItems(deleteList);
		
		// add new items
		for(Object item : newList){
			int pos = currentList.indexOf(item);
			newItem(pos,item);			
		}
		
		// keep the weak list of the current item
		weakList = new WeakList<Object>(currentList);
	}
	

	private void deleteOldItems(List<Object> deleteList) {
		if( deleteList == null || deleteList.size() == 0 || weakList == null)
			return;
		
		ArrayList<Object> currentPositionList = new ArrayList<Object>(Arrays.asList(weakList.toArray()));
		
		for(Object item : deleteList){
			int pos = currentPositionList.indexOf(item);
			currentPositionList.remove(item);
			this.removeViewAt(pos);	
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void newItem(int pos, Object item) {		
		if( layout == null )
			return;
		
		int layoutId = layout.staticLayoutId;		
		if( layoutId < 1 && layout.layoutIdName != null ) {									
			IObservable<?> observable = null;			
			InnerFieldObservable ifo = new InnerFieldObservable(layout.layoutIdName);
			if (ifo.createNodes(item)) {
				observable = ifo;
			} else {			
				Object rawField = BindingSyntaxResolver.getFieldForModel(layout.layoutIdName, item);
				if (rawField instanceof IObservable<?>)
					observable = (IObservable<?>)rawField;
				else if (rawField!=null)
					observable= new ConstantObservable(rawField.getClass(), rawField);
			}
			
			if( observable != null) {											
				Object obj = observable.get();
				if(obj instanceof Integer) {
					// TODO: keep the observable and do a rebinding on layout id changed
					layoutId = (Integer)obj;
				}
			}
		}
		
		View child = null;
		
		if( layoutId < 1 ) {
			TextView textView = new TextView(getContext());
			textView.setText("binding error - pos: " + pos + " has no layout - please check binding:itemPath or the layout id in viewmodel");
			textView.setTextColor(Color.RED);
			child = textView;
		} else {		
			Binder.InflateResult result = Binder.inflateView(getContext(), layoutId, this, false);
			for(View view: result.processedViews){
				AttributeBinder.getInstance().bindView(getContext(), view, item);
			}
			child = result.rootView;
		}
		 											
		this.addView(child,pos);
	}

}
