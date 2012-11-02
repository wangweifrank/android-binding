package gueei.binding.collections;

import gueei.binding.IObservableCollection;

public abstract class LazyLoadRowModelBase implements LazyLoadRowModel {
	private boolean displaying = false;
	
	@Override public final void display(IObservableCollection<?> collection, int index) {
		if (displaying) return;
		// if (!mapped) return;
		displaying = true;
		onDisplay(index);
	}

	@Override public final void hide(IObservableCollection<?> collection, int index) {
		if (!displaying) return;
		displaying = false;
		onHide(index);
	}

	public abstract void onDisplay(int index);

	public abstract void onHide(int index);

	protected boolean mapped = false;
	
	@Override
    public void setMapped(boolean mapped) {
		this.mapped = mapped;
    }

	@Override
    public boolean isMapped() {
	    return this.mapped;
    }
}
