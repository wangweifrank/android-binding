package gueei.binding;

import android.view.View;

public abstract class Command extends Observable<Command> {
	public Command() {
		super(Command.class);
		this.set(this);
	}

	public abstract void Invoke(View view, Object... args);
}
