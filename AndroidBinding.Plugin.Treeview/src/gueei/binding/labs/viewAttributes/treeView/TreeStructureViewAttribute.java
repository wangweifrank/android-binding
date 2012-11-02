package gueei.binding.labs.viewAttributes.treeView;

import gueei.binding.BindingType;
import gueei.binding.ViewAttribute;
import gueei.binding.widgets.TreeStructure;
import gueei.binding.widgets.TreeViewList;
import android.widget.Adapter;

public class TreeStructureViewAttribute extends ViewAttribute<TreeViewList, TreeStructure> {
			
		public  TreeStructureViewAttribute 
			(TreeViewList view) {
			super(TreeStructure.class,view, "treeStructure");
		}
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			if(getView()==null) return;
			if (newValue instanceof TreeStructure){
				getView().setTreeStructure((TreeStructure)newValue);
			}
		}

		@Override
		public TreeStructure get() {
			if(getView()==null) return null;
			return getView().getTreeStructure();
		}
		
		@Override
        protected BindingType AcceptThisTypeAs(Class<?> type) {
			if (Adapter.class.isAssignableFrom(type)) return BindingType.OneWay;
			return super.AcceptThisTypeAs(type);
        }
}
