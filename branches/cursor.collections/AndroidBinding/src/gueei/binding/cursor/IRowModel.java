package gueei.binding.cursor;

/**
 * User: =ra=
 * Date: 08.10.11
 * Time: 12:01
 */
public interface IRowModel {
	/**
	 * The main purpose of onInitialize is to perform initialization after data is read from source Data Set
	 */
	void onInitialize();
	/**
	 * Returns unique record identifier in source Data Set
	 *
	 * @param proposedId can be ignored, invented for capability with collection based data sources where defaultId
	 *                   is an index or object in collection
	 * @return unique record identifier in source Data Set
	 */
	public long getId(int proposedId);
	/**
	 * Provides interaction with LazyLoadCollection container collection
	 */
	public void onDisplay();
	/**
	 * Provides interaction with LazyLoadCollection container collection
	 */
	public void onHide();
}