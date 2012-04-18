package gueei.binding;

public interface IReferenceObservableProvider<T> {
	public IObservable<?> getReferenceObservable(int referenceId, String field);
	public void setHostContext(T host);
}
