package com.gueei.extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingSyntaxResolver;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.ConstantObservable;
import gueei.binding.IBindableView;
import gueei.binding.IObservable;
import gueei.binding.InnerFieldObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.collections.DependentCollectionObservable;
import gueei.binding.converters.ITEM_LAYOUT;
import gueei.binding.utility.WeakList;
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
			public Boolean calculateValue(CollectionChangedEventArg e, Object... args) throws Exception {
				if(args.length == 0) return false;
				if(!(args[0] instanceof ArrayListObservable<?>))
					return false;		
								
				@SuppressWarnings("unchecked")
				ArrayListObservable<Object> list = (ArrayListObservable<Object>)args[0];
				
				if( e == null ) {
					newList(list);
				} else {
					listChanged(e, list);
				}
				
				if( list == null || list.size() == 0)
					return true;
				return false;
			}
		};
	}	
	
	private void newList(List<Object> list) {
		this.removeAllViews();		

		for (Iterator<Entry<LayoutIdObserver, Object>> iter = htObservable.entrySet().iterator(); 
			iter.hasNext();) {
				Entry<LayoutIdObserver, Object> entry = iter.next();
				LayoutIdObserver e = entry.getKey();
				if( e.observable != null || e.observer != null)
					e.observable.unsubscribe(e.observer);
		}
		
		htObservable.clear();
		
		weakList = new WeakList<Object>();
		if( list == null)
			return;	
		
		int pos=0;
		for(Object item : list) {
			insertItem(pos, item);
			pos++;
		}
		
		weakList.addAll(list);
	}
	
	private void listChanged(CollectionChangedEventArg e, ArrayListObservable<Object> list) {
		if( e == null || list == null)
			return;
		
		int pos=-1;
		switch( e.getAction()) {
			case Add:
				pos = e.getNewStartingIndex();
				for(Object item : e.getNewItems()) {
					insertItem(pos, item);
					pos++;
				}
				break;
			case Move:
				break;
			case Remove:
				removeItems(e.getOldItems());	
				break;
			case Replace:
				break;
			case Reset:
				newList(list);
				break;
			default:
				throw new IllegalArgumentException("unknown action " + e.getAction().toString());
		}
		
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
	
	private void removeItems(List<?> deleteList) {
		if( deleteList == null || deleteList.size() == 0 || weakList == null)
			return;
		
		ArrayList<Object> currentPositionList = new ArrayList<Object>(Arrays.asList(weakList.toArray()));
		
		for(Object item : deleteList){
			int pos = currentPositionList.indexOf(item);

			if (htObservable.containsValue(item)) {
				for (Iterator<Entry<LayoutIdObserver, Object>> iter = htObservable.entrySet().iterator(); 
					iter.hasNext();) {
					Entry<LayoutIdObserver, Object> entry = iter.next();
					if (item.equals(entry.getValue())) {
						LayoutIdObserver e = entry.getKey();
						if( e.observable != null || e.observer != null)
							e.observable.unsubscribe(e.observer);
						iter.remove();
					}
				}
			}
			currentPositionList.remove(item);
			this.removeViewAt(pos);	
		}
	}
	
	private Hashtable<LayoutIdObserver,Object> htObservable = new Hashtable<LayoutIdObserver,Object>();
	
	private static class LayoutIdObserver {		
		public Observer observer = null;
		public IObservable<?> observable = null;	
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void insertItem(int pos, Object item) {		
		if( layout == null )
			return;
		
		int layoutId = layout.staticLayoutId;		
		if( layoutId < 1 && layout.layoutIdName != null ) {									
			IObservable<?> observable = null;			
			InnerFieldObservable ifo = new InnerFieldObservable(layout.layoutIdName);
			if (ifo.createNodes(item)) {
				observable = ifo;				
				buildObserverForLayoutIdChanges(observable, item);											
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

	private void buildObserverForLayoutIdChanges(IObservable<?> observable, Object item) {
		if( observable == null || item == null )
			return;
		
		Observer observer = new Observer() {					
			@Override
			public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
				if( htObservable == null )
					return;
				
				Object obj = null;
				
				for (Iterator<Entry<LayoutIdObserver, Object>> iter = htObservable.entrySet().iterator(); 
						iter.hasNext();) {
						Entry<LayoutIdObserver, Object> entry = iter.next();
						LayoutIdObserver e = entry.getKey();
						if( e == null || !this.equals(e.observer))
							continue;
						obj = entry.getValue();
						break;
				}						

				if( obj == null  || weakList == null )
					return;											
				
				int pos = weakList.indexOf(obj);						
				ArrayList<Object> list = new ArrayList<Object>();
				list.add(obj);
				removeItems(list);						
				insertItem(pos, obj);
			}
		};
		
		observable.subscribe(observer);
		LayoutIdObserver entry = new LayoutIdObserver();
		entry.observable = observable;
		entry.observer = observer;						
		
		htObservable.put(entry,item);		
	}

}
