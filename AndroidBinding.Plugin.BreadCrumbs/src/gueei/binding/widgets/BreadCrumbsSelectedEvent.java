package gueei.binding.widgets;

public class BreadCrumbsSelectedEvent {
	public final int siblingPosition;
	public final Object breadCrumbNodeNew;
	public final Object breadCrumbNodeOld;
	public boolean eventHandled = false;
	private BreadCrumbs parent;

	public BreadCrumbsSelectedEvent(BreadCrumbs parent, int siblingPosition, Object breadCrumbNodeNew, Object breadCrumbNodeOld) {
		this.parent = parent;
		this.siblingPosition = siblingPosition;
		this.breadCrumbNodeNew = breadCrumbNodeNew;
		this.breadCrumbNodeOld = breadCrumbNodeOld;
	}
	
	public Object [] getOldPath() {
		return parent.getCurrentPath();
	}
}
