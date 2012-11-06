package gueei.binding.widgets;

public class BreadCrumbsClickEvent {
	public final int rowPosition;
	public final Object breadCrumbNode;
	public boolean eventHandled = false;
	
	public BreadCrumbsClickEvent(int rowPosition, Object breadCrumbNode) {
		this.rowPosition = rowPosition;
		this.breadCrumbNode = breadCrumbNode;		
	}
}
