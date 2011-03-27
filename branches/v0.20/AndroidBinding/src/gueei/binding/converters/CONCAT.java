package gueei.binding.converters;

import gueei.binding.DependentObservable;
import gueei.binding.IObservable;
import android.text.SpannableStringBuilder;


public class CONCAT extends DependentObservable<CharSequence> {

	public CONCAT(IObservable<?>[] dependents) {
		super(CharSequence.class, dependents);
	}

	@Override
	public CharSequence calculateValue(Object... args) throws Exception {
		int len = args.length;
		SpannableStringBuilder result = new SpannableStringBuilder("");
		for(int i=0; i<len; i++){
			if (args[i]==null) continue;
			if (args[i] instanceof CharSequence)
				result.append((CharSequence)args[i]);
			else
				result.append(args[i].toString());
		}
		return result;
	}
}
