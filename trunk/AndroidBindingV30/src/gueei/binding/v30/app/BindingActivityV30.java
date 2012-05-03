package gueei.binding.v30.app;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import gueei.binding.BindingLog;
import gueei.binding.Utility;
import gueei.binding.app.BindingActivity;
import gueei.binding.app.rootView.BindableRootView;
import gueei.binding.labs.EventAggregator;
import gueei.binding.labs.EventSubscriber;
import gueei.binding.menu.BindableOptionsMenu;
import gueei.binding.v30.BinderV30;
import gueei.binding.v30.actionbar.BindableActionBar;
import android.content.res.XmlResourceParser;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Xml;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Binding Activity V30 is much more than a utility class, compare to prev versions
 * This provide a whole new level of support to HC/ICS features including
 * Action Bar, Action Buttons, Action Views, Options Menu
 *
 * Meta data file format: [http://code.google.com/p/android-binding/wiki/ActivityBinding]
 * @author andy
 *
 */
public class BindingActivityV30 extends BindingActivity {
	protected BindableOptionsMenu mBindableOptionsMenu;
	protected BindableActionBar mBindableActionBar;
	protected View mBindableRootView;
	
/*	private Observer optionsMenuSourceObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			rebindOptionsMenu();
		}
	};
*/	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventAggregator.getInstance(this).subscribe("invalidateOptionsMenu()", new EventSubscriber(){
			public void onEventTriggered(String eventName, Object publisher,
					Bundle data) {
				BindingActivityV30.this.invalidateOptionsMenu();
			}
		});
	}

/*	private void rebindOptionsMenu(){
		if (optionsMenu_source==null || optionsMenu_id==null)
			return;
		if (!(optionsMenu_id.get() instanceof Integer)) return;
		if (optionsMenu_source instanceof IObservable){
			this.setAndBindOptionsMenu((Integer)optionsMenu_id.get(), ((IObservable<?>)optionsMenu_source).get());
		}
		else
			this.setAndBindOptionsMenu((Integer)optionsMenu_id.get(), optionsMenu_source);
	}
*/	
	protected boolean inflate(int xmlId){
		XmlResourceParser parser = getResources().getXml(xmlId);
		try{
			int eventType= parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				if (eventType == XmlResourceParser.START_TAG){
					String tagName = parser.getName().toLowerCase();
					if (tagName.equals("optionsmenu")){
						mBindableOptionsMenu = this.createBindableOptionsMenu();
						Binder.putBindingMapToView(mBindableOptionsMenu, 
								Utility.createBindingMap(Xml.asAttributeSet(parser)));
					}
					else if (tagName.equals("rootview")){
						mBindableRootView = this.createBindableRootView();
						Binder.putBindingMapToView((View)mBindableRootView, 
								Utility.createBindingMap(Xml.asAttributeSet(parser)));
					}else if (tagName.equals("actionbar")){
						mBindableActionBar = createBindableActionBar();
						Binder.putBindingMapToView(mBindableActionBar, 
								Utility.createBindingMap(Xml.asAttributeSet(parser)));
					}
				}
				eventType = parser.next();
			}
		}catch(Exception e){
			BindingLog.exception("BindingActivityV30", new Exception("Problem with the Activity XML file", e));
			return false;
		}
		return true;
	}

	/**
	 * Hook to allow custom action bar
	 * @return
	 */
	protected BindableActionBar createBindableActionBar() {
		if (VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB || this.getActionBar()==null)
			return null;
		return new BindableActionBar(this);
	}
	
	protected View createBindableRootView() {
		return new BindableRootView(this);
	}

	protected BindableOptionsMenu createBindableOptionsMenu() {
		return new BindableOptionsMenu(this);
	}

	protected void bindActionBar(Object model){
		if (mBindableActionBar == null) return;
		AttributeBinder.getInstance().bindView(this, mBindableActionBar, model);
	}

	protected void bindOptionsMenu(Object model){
		if (mBindableOptionsMenu == null) return;
		AttributeBinder.getInstance().bindView(this, mBindableOptionsMenu, model);
	}

	protected void bindRootView(Object model){
		if (mBindableRootView == null) return;
		AttributeBinder.getInstance().bindView(this, (View)mBindableRootView, model);
	}
	
	/**
	 * Shortcut method to inflate the Activity xml, bind everything and set them up
	 * @param xmlId
	 * @param model
	 */
	protected void inflateAndBind(int xmlId, Object model){
		if (inflate(xmlId)){
			bindOptionsMenu(model);
			// Options menu must go first or else the create will have problem
			bindActionBar(model);
			bindRootView(model);
			setContentView(mBindableRootView);
		}
	}
	
	@Override
	@Deprecated
	protected View setAndBindRootView(int layoutId, Object... contentViewModel) {
		if (getRootView()!=null){
			throw new IllegalStateException("Root view is already created");
		}
		InflateResult result = BinderV30.inflateView(this, layoutId, null, false);
		setRootView(result.rootView);
		for(int i=0; i<contentViewModel.length; i++){
			BinderV30.bindView(this, result, contentViewModel[i]);
		}
		setContentView(getRootView());
		return getRootView();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return mBindableOptionsMenu.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return mBindableOptionsMenu.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home){
			EventAggregator.getInstance(this).publish("Clicked(android.R.id.home)", this, null);
			return true;
		}
		return mBindableOptionsMenu.onOptionsItemSelected(item);
	}
}
