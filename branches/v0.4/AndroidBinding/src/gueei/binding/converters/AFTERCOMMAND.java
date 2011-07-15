package gueei.binding.converters;

import gueei.binding.Command;
import gueei.binding.Command.CommandListener;
import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.viewAttributes.view.AnimationTrigger;

public class AFTERCOMMAND extends Converter<AnimationTrigger> implements CommandListener {
	AnimationTrigger mTrigger = new AnimationTrigger();
	Command mCommand;
	boolean condition;

	public AFTERCOMMAND(IObservable<?>[] dependents) {
		super(AnimationTrigger.class, dependents);
	}

	/**
	 * this Requires three input
	 * 1. Animation Id
	 * 2. Any Command
	 * 3. Boolean Condition
	 */
	@Override
	public AnimationTrigger calculateValue(Object... args) throws Exception {
		if (args.length<3) return null;
		if (!(args[0] instanceof Integer)){
			return null;
		}
		mTrigger.setAnimationId((Integer)args[0]);
		if ( (mCommand==null) || (!mCommand.equals(args[1])) )
			if (args[1] instanceof Command){
				if (mCommand!=null) mCommand.removeCommandListener(this);
				mCommand = (Command)(args[1]);
				mCommand.addCommandListener(this);
			}
		condition = Boolean.TRUE.equals(args[2]);
		return mTrigger;
	}

	public void onBeforeInvoke() {
	}

	public void onAfterInvoke() {
		if (condition)
			mTrigger.notifyAnimationFire();
	}
}
