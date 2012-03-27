package gueei.binding.viewAttributes.templates;

public abstract class Layout {
	private int mDefaultId = -1;
	
	public Layout(int defaultId){
		setDefaultLayoutId(defaultId);
	}
	
	public void setDefaultLayoutId(int id){
		mDefaultId = id;
	}
	
	public int getDefaultLayoutId(){
		return mDefaultId;
	}
	
	public abstract int getLayoutTypeId(int pos);
	
	public abstract int getLayoutId(int pos);
	
	public abstract int getTemplateCount();
}