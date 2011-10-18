package gueei.binding.collections;

public interface LazyLoadCollection {
	public void onDisplay(int position);
	public void onHide(int position);
	/**
	 * Added: 18/10/2011
	 * Set the number of visible children (most probably by Adapter)
	 * A collection might not bound to only one adapter, so this is for reference only
	 * @param setter the object the initiate this
	 * @param total the count
	 */
	public void setVisibleChildrenCount(Object setter, int total);
}