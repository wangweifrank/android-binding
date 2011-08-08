package gueei.binding.obligatoryAttributeProviders;

import android.widget.AbsSpinner;
import gueei.binding.iConst;

/**
 * User: =ra=
 * Date: 03.08.11
 * Time: 11:27
 */
public class AbsSpinnerObligatoryAttributeProvider extends ObligatoryAttributeProvider {

	public AbsSpinnerObligatoryAttributeProvider() {
		super(AbsSpinner.class);
	}

	@Override protected void initObligatoryAttributes() {
		mObligatoryAttributesMap.put(iConst.ATTR_SELECTED_ITEM, makeDefaultObservableName(iConst.ATTR_SELECTED_ITEM));
		mObligatoryAttributesMap
				.put(iConst.ATTR_SELECTED_POSITION, makeDefaultObservableName(iConst.ATTR_SELECTED_POSITION));
	}
}
