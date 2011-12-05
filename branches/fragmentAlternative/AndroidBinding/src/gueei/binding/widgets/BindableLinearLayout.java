package gueei.binding.widgets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingSyntaxResolver;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.ConstantObservable;
import gueei.binding.IBindableView;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.InnerFieldObservable;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.converters.ITEM_LAYOUT;
import gueei.binding.utility.ObservableMultiplexer;
import gueei.binding.utility.WeakList;
import gueei.binding.Observer;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BindableLinearLayout extends LinearLayout implements IBindableView<BindableLinearLayout> {
	private WeakList<Object> currentList = null;
	private CollectionObserver collectionObserver = null;
	
	private ArrayListObservable<Object> itemList = null;
	private ITEM_LAYOUT.ItemLayout layout = null;	

	private ObservableMultiplexer<Object> observableItemsLayoutID = new ObservableMultiplexer<Object>(new Observer() {
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if( initiators == null || initiators.size() < 1)
				return;
			Object parent = initiators.toArray()[0];
			int pos = currentList.indexOf(parent);						
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(parent);
			removeItems(list);						
			insertItem(pos, parent);	 
		}
	});
	
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
	
	private void createItemSourceList(ArrayListObservable<Object> newList) {		
		if( itemList != null && collectionObserver != null)
			itemList.unsubscribe(collectionObserver);
		
		collectionObserver = null;
		itemList = newList;
		
		if(newList==null)
			return;

		currentList = null;	
		collectionObserver = new CollectionObserver() {			
			@SuppressWarnings("unchecked")
			@Override
			public void onCollectionChanged(IObservableCollection<?> collection, CollectionChangedEventArg args) {
				listChanged(args, (List<Object>)collection);
			}
		};
		
		itemList.subscribe(collectionObserver);
		newList(newList);
	}	
	
	private void newList(List<Object> list) {
		this.removeAllViews();	
		
		observableItemsLayoutID.clear();
				
		currentList = new WeakList<Object>();
		if( list == null)
			return;	
		
		int pos=0;
		for(Object item : list) {
			insertItem(pos, item);
			pos++;
		}
		
		currentList.addAll(list);
	}

	private void listChanged(CollectionChangedEventArg e, List<Object> list) {
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
			case Remove:
				removeItems(e.getOldItems());	
				break;
			case Replace:
				removeItems(e.getOldItems());	
				pos = e.getNewStartingIndex();
				for(Object item : e.getNewItems()) {
					insertItem(pos, item);
					pos++;
				}
				break;
			case Reset:
				newList(list);
				break;
			case Move:
				// currently the observable array list doesn't create this action
				throw new IllegalArgumentException("move not implemented");				
			default:
				throw new IllegalArgumentException("unknown action " + e.getAction().toString());
		}
		
		currentList = new WeakList<Object>(list);
	}	
	
	private ViewAttribute<BindableLinearLayout, Object> ItemSourceAttribute = 
			new ViewAttribute<BindableLinearLayout, Object>(Object.class, BindableLinearLayout.this, "ItemSource") {		
				@SuppressWarnings("unchecked")
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if( !(newValue instanceof ArrayListObservable<?> ))
						return;
					if( layout != null )
						createItemSourceList((ArrayListObservable<Object>)newValue);
				}

				@Override
				public Object get() {
					return itemList;
				}				
	};	
					
	private ViewAttribute<BindableLinearLayout, Object> ItemLayoutAttribute =
			new ViewAttribute<BindableLinearLayout, Object>(Object.class, BindableLinearLayout.this, "ItemLayout"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					layout = null;
					if( newValue instanceof ITEM_LAYOUT.ItemLayout ) {
						layout = (ITEM_LAYOUT.ItemLayout) newValue;
						if( itemList != null )
							createItemSourceList(itemList);
					}
				}

				@Override
				public Object get() {
					return layout;
				}
	};				

	@Override
	public ViewAttribute<BindableLinearLayout, ?> createViewAttribute(String attributeId) {	
		if (attributeId.equals("itemSource")) return ItemSourceAttribute;
		if (attributeId.equals("itemLayout")) return ItemLayoutAttribute;
		return null;
	}
	
	private void removeItems(List<?> deleteList) {
		if( deleteList == null || deleteList.size() == 0 || currentList == null)
			return;
		
		ArrayList<Object> currentPositionList = new ArrayList<Object>(Arrays.asList(currentList.toArray()));
		
		for(Object item : deleteList){
			int pos = currentPositionList.indexOf(item);			
			observableItemsLayoutID.removeParent(item);
			currentPositionList.remove(item);
			this.removeViewAt(pos);	
		}
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
			} else {			
				Object rawField = BindingSyntaxResolver.getFieldForModel(layout.layoutIdName, item);
				if (rawField instanceof IObservable<?>)
					observable = (IObservable<?>)rawField;
				else if (rawField!=null)
					observable= new ConstantObservable(rawField.getClass(), rawField);
			}
			
			if( observable != null) {	
				observableItemsLayoutID.add(observable, item);	
				Object obj = observable.get();
				if(obj instanceof Integer)
					layoutId = (Integer)obj;
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
