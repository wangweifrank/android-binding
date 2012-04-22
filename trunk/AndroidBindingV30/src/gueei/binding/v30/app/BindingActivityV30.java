package gueei.binding.v30.app;

import java.io.IOException;
import java.util.Collection;

import org.xmlpull.v1.XmlPullParserException;

import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import gueei.binding.BindingSyntaxResolver;
import gueei.binding.DependentObservable;
import gueei.binding.IObservable;
import gueei.binding.Observer;
import gueei.binding.Utility;
import gueei.binding.app.BindingActivity;
import gueei.binding.menu.OptionsMenuBinder;
import gueei.binding.v30.BinderV30;
import gueei.binding.v30.actionbar.ActionBarBinder;
import gueei.binding.viewAttributes.templates.Layout;
import android.content.res.XmlResourceParser;
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

	// TODO: Need to rewrite options menu binder
	private OptionsMenuBinder mMenuBinder;
	
	private IObservable<?> optionsMenu_source, optionsMenu_id;
	private Observer optionsMenuSourceObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> prop,
				Collection<Object> initiators) {
			rebindOptionsMenu();
		}
	};
	
	private void rebindOptionsMenu(){
		if (optionsMenu_source==null || optionsMenu_id==null)
			return;
		if (!(optionsMenu_id.get() instanceof Integer)) return;
		if (optionsMenu_source instanceof IObservable){
			this.setAndBindOptionsMenu((Integer)optionsMenu_id.get(), ((IObservable<?>)optionsMenu_source).get());
		}
		else
			this.setAndBindOptionsMenu((Integer)optionsMenu_id.get(), optionsMenu_source);
	}
	
	/**
	 * Bind this Activity to the provided metadata file. 
	 * @param xmlId XML Metadata file
	 * @param model Root View Model
	 */
	protected void bind(int xmlId, Object model){
		XmlResourceParser parser = getResources().getXml(xmlId);
		try{
			int eventType= parser.getEventType();
			while(eventType != XmlResourceParser.END_DOCUMENT){
				if (eventType == XmlResourceParser.START_TAG){
					if (parser.getName().equals("actionBar")){
						// Bind Action Bar
					}
					if (parser.getName().equals("optionsMenu")){
						// Bind Options Menu
						String source = parser.getAttributeValue(Binder.BINDING_NAMESPACE, "dataSource");
						String id = parser.getAttributeValue(Binder.BINDING_NAMESPACE, "menu");

						optionsMenu_source = 
								BindingSyntaxResolver.constructObservableFromStatement(this, source, model);
						optionsMenu_id = 
								BindingSyntaxResolver.constructObservableFromStatement(this, id, model);

						optionsMenu_source.subscribe(optionsMenuSourceObserver);
						optionsMenu_id.subscribe(optionsMenuSourceObserver);
						
						rebindOptionsMenu();
					}
					if (parser.getName().equals("rootView")){
						// Bind Root View
						String layout = parser.getAttributeValue(null, "layout");
						
						this.setAndBindRootView(Utility.resolveLayoutResource(layout, this), model);
					}
				}
				eventType = parser.next();
			}
		}catch(XmlPullParserException e){
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
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
	protected void setAndBindOptionsMenu(int menuId, Object menuViewModel) {
		super.setAndBindOptionsMenu(menuId, menuViewModel);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	protected void bindActionBar(int xmlId, Object model){
		ActionBarBinder.BindActionBar(this, xmlId, model);
	}
}
