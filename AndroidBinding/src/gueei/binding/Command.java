package gueei.binding;

import android.view.View;

public abstract class Command extends Observable<Command> {
	public Command() {
		super(Command.class);
	}

	@Override
	public Command get() {
		return this;
	}

	public abstract void Invoke(View view, Object... args);
}
