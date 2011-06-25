package gueei.binding.collections;

/**
 * Collection with this interface will not load all children
 * immediately, rather they are loaded when required (displaying, for example)
 * @author andy
 *
 */
public interface LazyLoadParent {
	public void onLoadChildren();
}