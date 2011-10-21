package gueei.extensions.alertdialog;

import gueei.binding.Binder;
import gueei.binding.Binder.InflateResult;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;


public class BindingAlertDialogBuilderHelper {
	private InflateResult inflateResult;
	
	public void inflateDialogView(Activity activity, int layoutId) {
		if (inflateResult!=null){
			throw new IllegalStateException("Root view is already created");
		}		
		inflateResult = Binder.inflateView(activity.getApplicationContext(), layoutId, null, false);		
	}

	public AlertDialog createAndBind(Activity activity, AlertDialog.Builder builder, Object... contentViewModel) {
		if (inflateResult==null){
			throw new IllegalStateException("Root view is not created");
		}

		builder.setView(inflateResult.rootView);

		for(int i=0; i<contentViewModel.length; i++){
			if( contentViewModel[i] instanceof IBindingAlertDialogPositiveButtonText ) {
				IBindingAlertDialogPositiveButtonText iface = (IBindingAlertDialogPositiveButtonText)contentViewModel[i];
				String text = iface.getPositiveButtonText();
				if( text == null )
					text = "";
				
				if( contentViewModel[i] instanceof IBindingAlertDialogPositiveButtonHandler ) {
					final IBindingAlertDialogPositiveButtonHandler ifaceHandler 
						= (IBindingAlertDialogPositiveButtonHandler)contentViewModel[i];
					builder.setPositiveButton(text, new DialogInterface.OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ifaceHandler.onPositiveButtonPressed();						
						}
					});
				} else {
					builder.setPositiveButton(text, null);
				}
			}		
			
			if( contentViewModel[i] instanceof IBindingAlertDialogNeutralButtonText ) {
				IBindingAlertDialogNeutralButtonText iface = (IBindingAlertDialogNeutralButtonText)contentViewModel[i];
				String text = iface.getNeutralButtonText();
				if( text == null )
					text = "";
				
				if( contentViewModel[i] instanceof IBindingAlertDialogNeutralButtonHandler ) {		
					final IBindingAlertDialogNeutralButtonHandler ifaceHandler 
						= (IBindingAlertDialogNeutralButtonHandler)contentViewModel[i];
					builder.setNeutralButton(text, new DialogInterface.OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ifaceHandler.onNeutralButtonPressed();						
						}
					});
				} else {
					builder.setNeutralButton(text, null);
				}
			}		
			
			if( contentViewModel[i] instanceof IBindingAlertDialogNegativeButtonText ) {
				IBindingAlertDialogNegativeButtonText iface = (IBindingAlertDialogNegativeButtonText)contentViewModel[i];
				String text = iface.getNegativeButtonText();
				if( text == null )
					text = "";
				
				if( contentViewModel[i] instanceof IBindingAlertDialogNegativeButtonHandler ) {
					final IBindingAlertDialogNegativeButtonHandler ifaceHandler 
						= (IBindingAlertDialogNegativeButtonHandler)contentViewModel[i];
					builder.setNegativeButton(text, new DialogInterface.OnClickListener() {					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							ifaceHandler.onNegativeButtonPressed();						
						}
					});
				} else {
					builder.setNegativeButton(text, null);
				}
			}		
			
			if( contentViewModel[i] instanceof IBindingAlertDialogCancel ) {
				final IBindingAlertDialogCancel iface = (IBindingAlertDialogCancel)contentViewModel[i];

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {					
					@Override
					public void onCancel(DialogInterface dialog) {
						iface.onCancel();
					}
				});
			}	
			
			if( contentViewModel[i] instanceof IBindingAlertDialogTitleText ) {
				final IBindingAlertDialogTitleText iface = (IBindingAlertDialogTitleText)contentViewModel[i];
				
				String text = iface.getTitleText();
				if( text == null )
					text = "";
								
				builder.setTitle(text);
			}			
		}
		
		AlertDialog dialog = builder.create();
		for(int i=0; i<contentViewModel.length; i++){
			Binder.bindView(activity.getApplicationContext(), inflateResult, contentViewModel[i]);
			if( contentViewModel[i] instanceof IBindingAlertDialogWeakReference ) {
				IBindingAlertDialogWeakReference iface = (IBindingAlertDialogWeakReference)contentViewModel[i];
				final DialogWeakReference ref = iface.getDialogDialogWeakReference();
				if( ref != null ) {
					ref.setDialog(dialog);
					dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {					
						@Override
						public void onCancel(DialogInterface dialog) {
							ref.setDialog(null);
						}
					});
					dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {						
						@Override
						public void onDismiss(DialogInterface dialog) {
							ref.setDialog(null);
						}
					});
				}
			}					
		}		

		inflateResult = null;		
		return dialog;
	}

	public static void bindAndShowDialog(Activity activity, int layoutId, Object... viewModels) {		
		if( activity == null )
			return;
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);					
		BindingAlertDialogBuilderHelper helper = new BindingAlertDialogBuilderHelper();
		helper.inflateDialogView(activity, layoutId);							
		AlertDialog alertDialog = helper.createAndBind(activity, builder, viewModels);		
		alertDialog.show();	
	}
	
	public static void bindAndShowDialog(Activity activity, int theme, int layoutId, Object... viewModels) {		
		if( activity == null )
			return;
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, theme));					
		BindingAlertDialogBuilderHelper helper = new BindingAlertDialogBuilderHelper();
		helper.inflateDialogView(activity, layoutId);							
		AlertDialog alertDialog = helper.createAndBind(activity, builder, viewModels);		
		alertDialog.show();	
	}	
}
