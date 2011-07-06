package gueei.binding.viewAttributes.templates;

public class SingleLayoutTemplate extends LayoutTemplate {
	private final int mId;
	public SingleLayoutTemplate(int id){
		mId = id;
	}
	
	@Override
	public int getTemplate() {
		return mId;
	}

	@Override
	public int getTemplate(int index) {
		return mId;
	}

}
