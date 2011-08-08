package gueei.binding;

import android.view.View;
import gueei.binding.bindingProviders.BindingProvider;
import gueei.binding.obligatoryAttributeProviders.ObligatoryAttributeProvider;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * User: =ra=
 * Date: 03.08.11
 * Time: 11:24
 */
public class ObligatoryAttributeKeeper {

	private static ObligatoryAttributeKeeper _attributeFactory;
	private List<ObligatoryAttributeProvider> providers = new ArrayList<ObligatoryAttributeProvider>();

	private ObligatoryAttributeKeeper() {
	}

	/**
	 * Ensure it is Singleton
	 *
	 * @return
	 */
	public static ObligatoryAttributeKeeper getInstance() {
		if (_attributeFactory == null) {
			_attributeFactory = new ObligatoryAttributeKeeper();
		}
		return _attributeFactory;
	}

	public void merge(View view, BindingMap targetMap) {
		for (ObligatoryAttributeProvider p : providers) {
			   p.merge(view,targetMap);
		}
	}

	public void registerProvider(ObligatoryAttributeProvider provider) {
		providers.add(provider);
	}
}
