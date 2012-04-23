package gueei.binding.markupDemoICS;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.markupDemoICS.viewModels.DemoEntry;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.serialization.ViewModelParceler;
import gueei.binding.v30.app.BindingActivityV30;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class ShowDemoActivity extends BindingActivityV30 {
	
	public final Observable<CharSequence> DemoName = new Observable<CharSequence>(CharSequence.class);
	public final IntegerObservable DemoLayout =
			new IntegerObservable(0);
	public final Observable<Object> DemoVm = new Observable<Object>(Object.class);
	public final Command Back = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			ShowDemoActivity.this.finish();
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    Bundle entryBundle = getIntent().getBundleExtra("Entry");
	    DemoEntry entry = new DemoEntry();
	    ViewModelParceler.restoreViewModel(entryBundle, entry);
	    
	    DemoName.set(entry.Name.get());
	    DemoLayout.set(entry.LayoutId.get());
	    DemoVm.set(createVm(entry.ModelClassName.get()));
	    
	    this.bind(R.xml.showdemo_metadata, this);
	}

	private Object createVm(String demoClassName){
		try {
			return Class.forName(demoClassName).newInstance();
		} catch (Exception e) {
			try {
				return Class.forName(demoClassName).getConstructor(Activity.class).newInstance(this);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return null;
	}
}