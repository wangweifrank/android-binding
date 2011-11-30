package com.gueei.extension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.BindingSyntaxResolver;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.ConstantObservable;
import gueei.binding.IBindableView;
import gueei.binding.IObservable;
import gueei.binding.InnerFieldObservable;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.collections.DependentCollectionObservable;
import gueei.binding.converters.ROW_CHILD;
import gueei.binding.utility.WeakList;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class BindableTableLayout extends TableLayout implements IBindableView<BindableTableLayout> {

	public BindableTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BindableTableLayout(Context context) {
		super(context);
		init();
	}
	
	private void init() {
	}
	/*
	
	ArrayListObservable<Object> theList = null;	
	private ViewAttribute<BindableTableLayout, Object> ItemSourceAttribute = 
			new ViewAttribute<BindableTableLayout, Object>(Object.class, BindableTableLayout.this, "ItemSource") {		
				@SuppressWarnings("unchecked")
				@Override
				protected void doSetAttributeValue(Object newValue) {
					if( !(newValue instanceof ArrayListObservable<?> ))
						return;
					theList = (ArrayListObservable<Object>)newValue;
					
					if( rowChild != null )
						createItemSourceList(theList);
				}

				@Override
				public Object get() {
					return null;
				}				
	};	
			
	private ROW_CHILD.RowChild rowChild = null;			
	private ViewAttribute<BindableTableLayout, Object> RowChildAttribute =
			new ViewAttribute<BindableTableLayout, Object>(Object.class, BindableTableLayout.this, "RowChild"){
				@Override
				protected void doSetAttributeValue(Object newValue) {	
					rowChild = null;
					if( newValue instanceof ROW_CHILD.RowChild ) {
						rowChild = (ROW_CHILD.RowChild) newValue;
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
	public ViewAttribute<BindableTableLayout, ?> createViewAttribute(String attributeId) {			
		if (attributeId.equals("itemSource")) return ItemSourceAttribute;
		if (attributeId.equals("rowChild")) return RowChildAttribute;
		return null;
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
	//	deleteOldItems(deleteList);
		
		// add new items
		for(Object item : newList){
			int pos = currentList.indexOf(item);
			newItem(pos,item);			
		}
		
		// keep the weak list of the current item
		weakList = new WeakList<Object>(currentList);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void newItem(int pos, Object item) {		
		if( rowChild == null )
			return;
		
		IObservable<?> childDataSource = null;			
		InnerFieldObservable ifo = new InnerFieldObservable(rowChild.childDataSource);
		if (ifo.createNodes(item)) {
			childDataSource = ifo;
		} else {			
			Object rawField = BindingSyntaxResolver.getFieldForModel(rowChild.childDataSource, item);
			if (rawField instanceof IObservable<?>)
				childDataSource = (IObservable<?>)rawField;
		}
		
		TableRow row = new TableRow(getContext());		
		if( childDataSource == null) {	
			TextView textView = new TextView(getContext());
			textView.setText("binding error - row: " + pos + " has no child datasource - please check binding:itemPath or the layout id in viewmodel");
			textView.setTextColor(Color.RED);
			row.addView(textView);
		} else {			
			Object dataSource = childDataSource.get();	
								
			if( dataSource instanceof ArrayListObservable<?>) {
				ArrayListObservable<?> childItems = (ArrayListObservable<?>)dataSource;				
				for(Object childItem : childItems) {
					
					int layoutId = rowChild.staticLayoutId;		
					if( layoutId < 1 && rowChild.layoutIdName != null ) {									
						IObservable<?> observable = null;			
						ifo = new InnerFieldObservable(rowChild.layoutIdName);
						if (ifo.createNodes(childItem)) {
							observable = ifo;
						} else {			
							Object rawField = BindingSyntaxResolver.getFieldForModel(rowChild.layoutIdName, childItem);
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
					
					if( childItem == null ) {
						// empty view - we do not support cols
						child = new View(getContext());
						child.setBackgroundColor(Color.TRANSPARENT);
					} else {					
						if( layoutId < 1 ) {
							TextView textView = new TextView(getContext());
							textView.setText("binding error - pos: " + pos + " has no layout - please check binding:itemPath or the layout id in viewmodel");
							textView.setTextColor(Color.RED);
							child = textView;
						} else {		
							Binder.InflateResult result = Binder.inflateView(getContext(), layoutId, row, false);
							for(View view: result.processedViews){
								AttributeBinder.getInstance().bindView(getContext(), view, childItem);
							}
							child = result.rootView;						
						}						
					}
					TableRow.LayoutParams params = null;					
					// colspan
					if( rowChild.colspanName != null ) {									
						IObservable<?> observable = null;			
						ifo = new InnerFieldObservable(rowChild.colspanName);
						if (ifo.createNodes(childItem)) {
							observable = ifo;
						} else {			
							Object rawField = BindingSyntaxResolver.getFieldForModel(rowChild.colspanName, childItem);
							if (rawField instanceof IObservable<?>)
								observable = (IObservable<?>)rawField;
							else if (rawField!=null)
								observable= new ConstantObservable(rawField.getClass(), rawField);
						}
						
						if( observable != null) {											
							Object obj = observable.get();
							if(obj instanceof Integer) {
								// TODO: keep the observable and do a rebinding on layout id changed
								int colSpan = (Integer)obj;																	
								if( colSpan > 1) {		
									if( row.getLayoutParams() != null ) {
										params = new TableRow.LayoutParams(row.getLayoutParams());
									} else {
										// ViewGroup.MarginLayoutParams ctor doesn't honor the margins
										// so this is a workaround - we have to use the child - not the row
										
										TableRow.LayoutParams rowParams =  (TableRow.LayoutParams)child.getLayoutParams();										
										ViewGroup.MarginLayoutParams margins = new ViewGroup.MarginLayoutParams(rowParams);
										margins.setMargins(rowParams.leftMargin, rowParams.topMargin, 
														   rowParams.rightMargin, rowParams.bottomMargin);

										params = new TableRow.LayoutParams(margins);
									}
									params.span = colSpan;
									row.setLayoutParams(params);									
								}
							}
						}
					}
										
					if( params != null )
						row.addView(child, params);
					else
						row.addView(child);
				}				
			}						
		}
		 											
		this.addView(row,pos);
	}	
	*/

	@Override
	public ViewAttribute<? extends View, ?> createViewAttribute(
			String attributeId) {
		// TODO Auto-generated method stub
		return null;
	}
}
