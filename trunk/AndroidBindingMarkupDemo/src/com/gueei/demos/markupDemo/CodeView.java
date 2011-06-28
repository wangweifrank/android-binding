package com.gueei.demos.markupDemo;

import java.io.ByteArrayOutputStream;

import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

public class CodeView extends WebView {
	private CodeView(Context context) {
		super(context);
	}

	public static CodeView create(Context context){
		return new CodeView(context);
	}
	
	public CodeView setCodeResource(String title, int resId, String type){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
			XhtmlRendererFactory.getRenderer(type).highlight(title, 
					getContext().getResources().openRawResource(resId), 
					bos, "UTF-8", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.loadData(bos.toString(), "text/html", "UTF-8");
		return this;
	}	
}
