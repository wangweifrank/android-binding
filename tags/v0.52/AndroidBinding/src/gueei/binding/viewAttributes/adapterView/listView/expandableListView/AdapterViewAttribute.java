package gueei.binding.viewAttributes.adapterView.listView.expandableListView;

import gueei.binding.ViewAttribute;
import gueei.binding.collections.LazyLoadAdapter;
import android.widget.AbsListView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

public class AdapterViewAttribute extends ViewAttribute<ExpandableListView, ExpandableListAdapter> {
	public AdapterViewAttribute(ExpandableListView view) {
		super(ExpandableListAdapter.class, view, "adapter");
	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		if (newValue instanceof ExpandableListAdapter){
			getView().setAdapter((ExpandableListAdapter)newValue);
			if (newValue instanceof LazyLoadAdapter){
				if (getView() instanceof AbsListView)
				((LazyLoadAdapter)newValue).setRoot((AbsListView)getView());
			}
		}
	}

	@Override
	public ExpandableListAdapter get() {
		return (ExpandableListAdapter)getView().getAdapter();
	}
}