package gueei.binding.markupDemov30.viewModels;

import gueei.binding.Command;
import gueei.binding.markupDemov30.R;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.widgets.ILayoutLoadEvent;
import android.view.View;

public class BindableFrameLayout {

	public final IntegerObservable LayoutId = new IntegerObservable(0);
	
	public final Command ToggleLayout = new Command(){
		public void Invoke(View view, Object... args) {
			
			if( LayoutId.get() == null || LayoutId.get() == 0) {
				LayoutId.set(R.layout.bindableframelayout_frame1);
			} else {
				switch( LayoutId.get() ) {
					case R.layout.bindableframelayout_frame1:
						LayoutId.set(R.layout.bindableframelayout_frame2);
						break;
					case R.layout.bindableframelayout_frame2:
						LayoutId.set(R.layout.bindableframelayout_frame3);
						break;
					default:
						LayoutId.set(null);
						break;
				}
			}
		}
	};
	
	public final Command OnLoad = new Command(){
		public void Invoke(View view, Object... args) {		
			if( args == null || args.length < 1 )
				return;
			
			ILayoutLoadEvent loader = (ILayoutLoadEvent)args[0];			
			loader.setLayoutId(R.layout.bindableframelayout_frame_on_load);
		}
	};

}