package gueei.binding.obligatoryAttributeProviders;

import android.view.View;
import gueei.binding.BindingMap;
import gueei.binding.iConst;

/**
 * User: =ra=
 * Date: 03.08.11
 * Time: 11:26
 */
public abstract class ObligatoryAttributeProvider {

	public void merge(View view, BindingMap targetMap) {
		if (mType.isAssignableFrom(view.getClass())) {
			merge(targetMap);
		}
	}

	protected ObligatoryAttributeProvider(Class<?> type) {
		mType = type;
		initObligatoryAttributes();
	}

	protected abstract void initObligatoryAttributes();

	private void merge(BindingMap targetMap) {
		for (String attribute : mObligatoryAttributesMap.getAllKeys()) {
			if (!targetMap.containsKey(attribute)) {
				targetMap.put(attribute, mObligatoryAttributesMap.get(attribute));
			}
		}
	}

	protected String makeDefaultObservableName(String attributeName) {
		return mType.getSimpleName() + attributeName.substring(0, 1).toUpperCase() + attributeName.substring(1) +
			   iConst.ATTR_DEF_STR;
	}

	protected final Class<?> mType;
	protected final BindingMap mObligatoryAttributesMap = new BindingMap();
}
