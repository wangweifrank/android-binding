package gueei.binding.viewAttributes.templates;

public class SingleTemplateLayout extends Layout {
	public SingleTemplateLayout(int defaultId) {
		super(defaultId);
	}

	@Override
	public int getLayoutId(Object model) {
		return getDefaultLayoutId();
	}

	@Override
	public int getTemplateCount() {
		return 1;
	}

	@Override
	public int getLayoutType(Object model) {
		return 0;
	}
}
