package gueei.binding.viewAttributes.templates;

public class SingleTemplateLayout extends Layout {
	public SingleTemplateLayout(int defaultId) {
		super(defaultId);
	}

	@Override
	public int getTemplateCount() {
		return 1;
	}

	@Override
	public int getLayoutTypeId(Object item, int pos) {
		return 0;
	}

	@Override
	public int getLayoutId(Object item, int pos) {
		return getDefaultLayoutId();
	}
}
