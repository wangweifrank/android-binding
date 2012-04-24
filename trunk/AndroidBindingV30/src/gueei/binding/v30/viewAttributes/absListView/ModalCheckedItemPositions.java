package gueei.binding.v30.viewAttributes.absListView;

import gueei.binding.Binder;
import gueei.binding.ViewAttribute;
import gueei.binding.v30.listeners.MultiChoiceModeListenerMulticast;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;


public class ModalCheckedItemPositions extends ViewAttribute<ListView, SparseBooleanArray>
	implements AbsListView.MultiChoiceModeListener{
	
	public ModalCheckedItemPositions(ListView view) {
		super(SparseBooleanArray.class, view, "modalCheckedItemPositions");
		Binder.getMulticastListenerForView(view, MultiChoiceModeListenerMulticast.class).register(this);
	}

	@Override
	public SparseBooleanArray get() {
		return getView().getCheckedItemPositions();
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (!(newValue instanceof SparseBooleanArray)){
			getView().clearChoices();
			return;
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (!getView().equals(parent)) return;
		this.notifyChanged();
	}

	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		return false;
	}

	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	public void onDestroyActionMode(ActionMode mode) {
	}

	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		this.notifyChanged(this);
	}
}
