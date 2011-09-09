package gueei.binding.converters;

import android.view.View;
import gueei.binding.Command;
import gueei.binding.Converter;
import gueei.binding.IObservable;

/**
 * Command with custom arguments
 * @author andytsui
 *
 */
public class ARG extends Converter<Command>{
	private Command mCommand;
	private Object[] mArgs;
	
	private Command mOut = new Command(){
		@Override
		public void Invoke(View view, Object... args) {
			if (mCommand!=null)
				mCommand.Invoke(view, mArgs); // Original Argument will not include
		}
	};
	
	public ARG(IObservable<?>[] dependents) {
		super(Command.class, dependents);
	}

	@Override
	public Command calculateValue(Object... args) throws Exception {
		if (args.length<1) return null;
		if (args[0] instanceof Command){
			mCommand = (Command)args[0];
		}
		if (args.length>1){
			mArgs = new Object[args.length-1];
			System.arraycopy(args, 1, mArgs, 0, args.length-1);
		}
		return mOut;
	}
}