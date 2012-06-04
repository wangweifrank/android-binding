package gueei.binding.markupDemoICS.viewModels;

import gueei.binding.Observable;
import gueei.binding.markupDemoICS.R;
import gueei.binding.observables.StringObservable;
import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class ViewPager {

	public final Observable<PagerAdapter> Adapter 
		= new Observable<PagerAdapter>(PagerAdapter.class, new MyPagerAdapter());

	public final StringObservable Vm1 = new StringObservable("String 1");
	public final StringObservable Vm2 = new StringObservable("String 2");
	public final StringObservable Vm3 = new StringObservable("String 3");
	public final StringObservable Vm4 = new StringObservable("String 4");
	
	private class MyPagerAdapter extends PagerAdapter {
		 
        public int getCount() {
            return 3;
        }
 
        public Object instantiateItem(View collection, int position) {
 
        	Log.d("ViewPager: instantiate", "pos: " + position);
        	
            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            int[] ids = new int[]{R.layout.validation, R.layout.javascript, R.layout.actionmode};
            View view = inflater.inflate(ids[(int)((Math.random() * 1000) % 3)], null);
 
            ((android.support.v4.view.ViewPager) collection).addView(view, 0);
 
            return view;
        }
 
        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((android.support.v4.view.ViewPager) arg0).removeView((View) arg2);
 
        }
 
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);
 
        }
 
        @Override
        public Parcelable saveState() {
            return null;
        }
}
}
