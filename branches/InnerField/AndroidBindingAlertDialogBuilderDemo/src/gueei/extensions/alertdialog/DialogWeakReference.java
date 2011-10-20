package gueei.extensions.alertdialog;

import android.app.AlertDialog;

public class DialogWeakReference {
	private AlertDialog dialog = null;
	
	public void setDialog(AlertDialog dialog) {
		this.dialog = dialog;
	}
	
	public void dismiss() {
		if(dialog==null)
			return;
		dialog.dismiss();
	}
	
	public void cancel() {
		if(dialog==null)
			return;
		dialog.cancel();
	}	
	
}
