package gueei.binding.viewAttributes.templates;

import java.util.ArrayList;

public class MultiLayoutTemplate extends LayoutTemplate {
	private ArrayList<Integer> templates = new ArrayList<Integer>();
	
	public void addTemplate(int id){
		templates.add(id);
	}
	
	@Override
	public int getTemplate() {
		if (templates.size()>0)
			return templates.get(0);
		return -1;
	}

	@Override
	public int getTemplate(int index) {
		if (templates.size() > index)
			return templates.get(index);
		return getTemplate();
	}
}
