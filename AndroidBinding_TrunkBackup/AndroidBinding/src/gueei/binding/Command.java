package gueei.binding;

import android.view.View;

public interface Command {
	public void Invoke(View view, Object... args);
}
