package gueei.binding.v30.viewAttributes.absListView;

import java.util.Collection;

import gueei.binding.Binder;
import gueei.binding.BindingLog;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.ViewAttribute;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.v30.listeners.MultiChoiceModeListenerMulticast;
import gueei.binding.v30.widget.ActionModeBinder;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

public class MultiChoiceMode extends ViewAttribute<AbsListView, DynamicObject> 
	implements AbsListView.MultiChoiceModeListener{

	private int mMenuId;
	private Object mModel;
	ListViewMultiChoiceActionModeBinder binder;
	
	public MultiChoiceMode(AbsListView view) {
		super(DynamicObject.class, view, "multiChoiceMode");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		try {
			DynamicObject obj = (DynamicObject)newValue;
			mMenuId = (Integer)obj.getObservableByName("menu").get();
			mModel = obj.getObservableByName("model").get();
			binder = new ListViewMultiChoiceActionModeBinder(getView().getContext(), mMenuId, mModel);
			Binder.getMulticastListenerForView(getView(), MultiChoiceModeListenerMulticast.class)
				.register(this);
			Binder.getAttributeForView(getView(), "modalCheckedItemPositions")
				.subscribe(checkedItemPositionsWatcher);
		} catch (Exception e) {
			BindingLog.warning("MultiChoiceMode", "Dynamic Object format error; it should be {menu, model}");
		}
	}

	@Override
	public DynamicObject get() {
		// TODO Auto-generated method stub
		return null;
	}

	private Observer checkedItemPositionsWatcher = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			SparseBooleanArray arr = ((SparseBooleanArray)prop.get());
			for(int i=0; i<arr.size(); i++){
				if (arr.valueAt(i)) return;
			}
			
			if (binder!=null && binder.getActionMode()!=null){
				binder.getActionMode().finish();
			}
		}
	};
	
	private class ListViewMultiChoiceActionModeBinder extends ActionModeBinder
		implements AbsListView.MultiChoiceModeListener{
		protected ListViewMultiChoiceActionModeBinder(Context context,
				int menuResId, Object model) {
			super(context, menuResId, model);
		}

		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
		}
	}

	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if (binder!=null)
			return binder.onActionItemClicked(mode, item);
		return false;
	}

	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		if (binder!=null)
			return binder.onCreateActionMode(mode, menu);
		return false;
	}

	public void onDestroyActionMode(ActionMode mode) {
		if (binder!=null)
			binder.onDestroyActionMode(mode);
	}

	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		if (binder!=null)
			return binder.onPrepareActionMode(mode, menu);
		return false;
	}

	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		if (binder!=null)
			binder.onItemCheckedStateChanged(mode, position, id, checked);
	}
}
