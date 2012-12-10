package gueei.binding.widgets.treeview;

import java.util.Collection;

import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.Observer;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.ObjectObservable;
import gueei.binding.widgets.TreeViewList;

public class TreeViewItemWrapper {	
	public final IntegerObservable WrapperSpace = new IntegerObservable(0);
	public final ObjectObservable WrapperImage = new ObjectObservable(null);	
	public final IntegerObservable WrapperNodeLayoutId = new IntegerObservable(0);
	public final ObjectObservable WrapperNodeDataSource = new ObjectObservable(null);
	public IObservableCollection<?> children;
	public int level = 1;
	private TreeViewList parent;
	
	public TreeViewItemWrapper(TreeViewList parent) {
		this.parent = parent;
	}
	
	private IObservable<?> layoutIdObservable=null;
	private IObservable<Object> isExpandedObservable=null;
	
	private Observer observerLayoutId = new Observer() {		
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			if(prop!=null && prop.get() instanceof Integer)
				WrapperNodeLayoutId.set((Integer)prop.get());
			else
				WrapperNodeLayoutId.set(0);
		}
	};	
	
	private Observer observerIsExpanded = new Observer() {		
		@Override
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			parent.expandCollapseFromDataSource(TreeViewItemWrapper.this);
		}
	};	
	
	public void setLayoutIdObservable(IObservable<?> prop) {
		if(layoutIdObservable!=null)
			layoutIdObservable.unsubscribe(observerLayoutId);
		layoutIdObservable=prop;
		if(layoutIdObservable!=null) {
			layoutIdObservable.subscribe(observerLayoutId);
			if(prop!=null && prop.get() instanceof Integer)
				WrapperNodeLayoutId.set((Integer)prop.get());
			else
				WrapperNodeLayoutId.set(0);
		} else {
			WrapperNodeLayoutId.set(0);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void setIsExpandedObservable(IObservable<?> prop) {
		if(isExpandedObservable!=null)
			isExpandedObservable.unsubscribe(observerIsExpanded);
		isExpandedObservable=(IObservable<Object>)prop;
		if(isExpandedObservable!=null)
			isExpandedObservable.subscribe(observerIsExpanded);
	}	

	public void detach() {
		setLayoutIdObservable(null);
		setIsExpandedObservable(null);
	}

	public void setExpanded(Boolean newValue) {
		if(isExpandedObservable==null)
			return;
		isExpandedObservable.unsubscribe(observerIsExpanded);
		isExpandedObservable.set(newValue);
		isExpandedObservable.subscribe(observerIsExpanded);		
	}
	
	public Boolean isExpanded() {
		if(isExpandedObservable==null || isExpandedObservable.get() == null || !(isExpandedObservable.get() instanceof Boolean))
			return null;
		return (Boolean)isExpandedObservable.get();
	}
	
}
