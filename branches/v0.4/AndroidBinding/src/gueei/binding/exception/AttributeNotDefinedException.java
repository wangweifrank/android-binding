package gueei.binding.exception;

public class AttributeNotDefinedException extends BindingException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3063420613201292120L;

	public AttributeNotDefinedException() {
		super();
	}

	public AttributeNotDefinedException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public AttributeNotDefinedException(String detailMessage) {
		super(detailMessage);
	}

	public AttributeNotDefinedException(Throwable throwable) {
		super(throwable);
	}

}
