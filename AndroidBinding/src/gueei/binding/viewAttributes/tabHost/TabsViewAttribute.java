package gueei.binding.viewAttributes.tabHost;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import gueei.binding.*;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.viewAttributes.templates.Layout;

import java.util.Collection;

@SuppressWarnings("rawtypes")
public class TabsViewAttribute extends ViewAttribute<TabHost, ArrayListObservable> {

	ViewAttribute<?, Layout>  mTabTemplateAttr;
	ViewAttribute<?, Integer> mTabWidth;
	ViewAttribute<?, Integer> mTabSelectedPosition;
	private Observer mTabWidthObserver = new Observer() {
		public void onPropertyChanged(IObservable<?> prop, Collection<Object> initiators) {
			resetTabsWidth();
		}
	};
	//

	public TabsViewAttribute(TabHost view) {
		super(ArrayListObservable.class, view, "tabs");
		try {
			mTabTemplateAttr = (ViewAttribute<?, Layout>) Binder.getAttributeForView(getView(), "tabTemplate");
			mTabWidth = (ViewAttribute<?, Integer>) Binder.getAttributeForView(getView(), "tabWidth");
			mTabWidth.subscribe(mTabWidthObserver);
			mTabSelectedPosition = (ViewAttribute<?, Integer>) Binder.getAttributeForView(getView(), "selectedPosition");
		}
		catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	private ArrayListObservable<Tab> mTabs;

	@SuppressWarnings("unchecked") @Override
	protected void doSetAttributeValue(Object newValue) {
		if (!(newValue instanceof ArrayListObservable)) {
			return;
		}
		// Type is not Tab
		if (!Tab.class.isAssignableFrom(((ArrayListObservable) newValue).getComponentType())) {
			return;
		}
		mTabs = (ArrayListObservable<Tab>) newValue;
		for (Tab t : mTabs) {
			mTabHost.addTab(constructTabSpec(t));
		}
		resetTabsWidth();
		mTabHost.setCurrentTab(mTabSelectedPosition.get());
	}

	private TabSpec constructTabSpec(Tab tab) {
		TabSpec spec = mTabHost.newTabSpec(tab.Tag.get());
		if (null != mTabTemplateAttr.get()) {
			// not sure about this...
			Context context = mTabHost.getContext();
			Binder.InflateResult inflateResult = Binder.inflateView(context, mTabTemplateAttr.get().getDefaultLayoutId(), null, false);
			final View tabView = Binder.bindView(context, inflateResult, tab);
			spec.setIndicator(tabView);
		}
		else {
			if (tab.Icon.get() != null) {
				spec.setIndicator(tab.Label.get(), tab.Icon.get());
			}
			else {
				spec.setIndicator(tab.Label.get());
			}
		}
		if (tab.Activity.get() != null) {
			Intent intent;
			try {
				intent = new Intent(getView().getContext(), Class.forName(tab.Activity.get()));
				spec.setContent(intent);
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		else {
			spec.setContent(tab.ViewId.get());
		}
		return spec;
	}

	TabHost mTabHost;

	@Override
	protected void onBind(Context context, IObservable<?> prop, BindingType binding) {
		if (!(context instanceof TabActivity)) {
			return;
		}
		mTabHost = ((TabActivity) context).getTabHost();
		super.onBind(context, prop, binding);
	}

	@Override
	public ArrayListObservable<Tab> get() {
		return mTabs;
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	private void resetTabsWidth() {
		int tabWidth = mTabWidth.get();
		if (tabWidth > 0) {
			TabWidget tabWidget = mTabHost.getTabWidget();
			for (int i = 0; i < tabWidget.getChildCount(); ++i) {
				tabWidget.getChildAt(i).getLayoutParams().width = tabWidth;
			}
		}
	}
}