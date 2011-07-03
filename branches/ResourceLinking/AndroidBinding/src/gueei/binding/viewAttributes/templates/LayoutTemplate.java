package gueei.binding.viewAttributes.templates;

import gueei.binding.Observable;

/**
 * This class represents the templates resolved from xml file.
 * It supports multiple template in one single data object
 * @author andy
 *
 */
public abstract class LayoutTemplate extends Observable<LayoutTemplate> {
	
	public LayoutTemplate() {
		super(LayoutTemplate.class);
		this.set(this);
	}
	
	public abstract int getTemplate();
	public abstract int getTemplate(int index);
}