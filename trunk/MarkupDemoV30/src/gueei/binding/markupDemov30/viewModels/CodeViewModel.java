package gueei.binding.markupDemov30.viewModels;

import gueei.binding.markupDemov30.R;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.StringObservable;

public class CodeViewModel {

	public final IntegerObservable Resource = new IntegerObservable(R.raw.main_metadata);
	public final StringObservable Type = new StringObservable("xml");

	public CodeViewModel() {
		super();
	}

}