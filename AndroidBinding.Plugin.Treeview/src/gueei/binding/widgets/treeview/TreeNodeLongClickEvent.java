package gueei.binding.widgets.treeview;

public class TreeNodeLongClickEvent {
	public final int rowPosition;
	public final Object treeNode;
	
	public TreeNodeLongClickEvent(int rowPosition, Object treeNode) {
		this.rowPosition = rowPosition;
		this.treeNode = treeNode;		
	}
}
