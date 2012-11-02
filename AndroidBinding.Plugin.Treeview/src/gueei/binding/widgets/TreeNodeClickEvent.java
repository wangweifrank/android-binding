package gueei.binding.widgets;

public class TreeNodeClickEvent {
	public final int rowPosition;
	public final Object treeNode;
	public boolean ignoreExpandCollapse = false;
	
	public TreeNodeClickEvent(int rowPosition, Object treeNode) {
		this.rowPosition = rowPosition;
		this.treeNode = treeNode;		
	}
}
