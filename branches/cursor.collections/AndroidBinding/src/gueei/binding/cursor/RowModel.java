package gueei.binding.cursor;

/**
 * User: =ra=
 * Date: 11.10.11
 * Time: 21:05
 */
public class RowModel implements IRowModel {
	@Override public void onInitializeFromDS() {}

	@Override public long getId(int proposedId) {
		return proposedId;
	}

	@Override public void onDisplay() {}

	@Override public void onHide() {}
}