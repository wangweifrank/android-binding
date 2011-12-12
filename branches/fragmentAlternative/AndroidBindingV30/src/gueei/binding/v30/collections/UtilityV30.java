package gueei.binding.v30.collections;

import gueei.binding.IObservableCollection;
import gueei.binding.viewAttributes.templates.LayoutItem;
import android.content.Context;
import android.support.v4.view.PagerAdapter;

public class UtilityV30 {
	@SuppressWarnings({ "rawtypes" })
	public static PagerAdapter getSimplePagerAdapter(
			Context context, Object collection, LayoutItem layout) throws Exception{
		if ((collection instanceof IObservableCollection)){
			IObservableCollection obsCollection = (IObservableCollection)collection;
			return new CollectionPagerAdapter(
					context, 
					obsCollection, 
					layout);
		}

		return null;
	}
}
