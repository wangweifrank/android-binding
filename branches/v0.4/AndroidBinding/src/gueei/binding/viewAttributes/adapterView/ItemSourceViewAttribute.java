package gueei.binding.viewAttributes.adapterView;

import android.view.View;
import android.widget.AbsSpinner;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Filter;
import gueei.binding.*;
import gueei.binding.cursor.CursorRowTypeMap;
import gueei.binding.exception.AttributeNotDefinedException;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.Collection;

public class ItemSourceViewAttribute extends ViewAttribute<AdapterView<Adapter>, Object> {

	Layout template, spinnerTemplate;
	Filter                   filter;
	ViewAttribute<?, Layout> itemTemplateAttr, spinnerTemplateAttr;
	ViewAttribute<?, Filter> filterAttr;
	Object                   mValue;
	private Observer templateObserver = new Observer() {
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			template = itemTemplateAttr.get();
			spinnerTemplate = spinnerTemplateAttr.get();
		}
	};

	@SuppressWarnings("unchecked")
	public ItemSourceViewAttribute(AdapterView<Adapter> view, String attributeName) {
		super(Object.class, view, attributeName);
		try {
			// local cache
			View cachedView = getView();
			itemTemplateAttr = (ViewAttribute<?, Layout>) Binder.getAttributeForView(cachedView, "itemTemplate");
			itemTemplateAttr.subscribe(templateObserver);
			spinnerTemplateAttr = (ViewAttribute<?, Layout>) Binder.getAttributeForView(cachedView, "spinnerTemplate");
			spinnerTemplateAttr.subscribe(templateObserver);
			template = itemTemplateAttr.get();
			spinnerTemplate = spinnerTemplateAttr.get();
			// Spinner doesn't support filter stuff
			if (!(cachedView instanceof AbsSpinner)) {
				filterAttr = (ViewAttribute<?, Filter>) Binder.getAttributeForView(cachedView, "filter");
				filter = filterAttr.get();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override @SuppressWarnings("unchecked")
	protected void doSetAttributeValue(Object newValue) {
		if (newValue == null) {
			return;
		}
		// local cache
		View cachedView = getView();
		if (newValue instanceof Adapter) {
			try {
				((ViewAttribute<?, Adapter>) Binder.getAttributeForView(cachedView, iConst.ATTR_ADAPTER))
					.set((Adapter) newValue);
			}
			catch (AttributeNotDefinedException e) {
				e.printStackTrace();
			}
			return;
		}
		if (template == null) {
			return;
		}
		spinnerTemplate = spinnerTemplate == null ? template : spinnerTemplate;
		try {
			Adapter adapter = gueei.binding.collections.Utility
				.getSimpleAdapter(getView().getContext(), newValue, spinnerTemplate, template, filter);
			((ViewAttribute<?, Adapter>) Binder.getAttributeForView(cachedView, iConst.ATTR_ADAPTER)).set(adapter);
			ViewAttribute<?, Integer> SelectedPosition =
				(ViewAttribute<?, Integer>) Binder.getAttributeForView(cachedView, iConst.ATTR_SELECTED_POSITION);
			getView().setSelection(SelectedPosition.get());
			mValue = newValue;
			return;
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public Object get() {
		return mValue;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		if (type.isArray() || CursorRowTypeMap.class.isAssignableFrom(type)) {
			return BindingType.OneWay;
		}
		return BindingType.OneWay;
	}
}
