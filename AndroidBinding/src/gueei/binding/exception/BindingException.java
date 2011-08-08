package gueei.binding.exception;

public class BindingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6230177679884976931L;

	public BindingException() {
		super();
	}

	public BindingException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public BindingException(String detailMessage) {
		super(detailMessage);
	}

	public BindingException(Throwable throwable) {
		super(throwable);
	}

}
