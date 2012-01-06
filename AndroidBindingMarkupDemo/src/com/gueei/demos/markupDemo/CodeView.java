package com.gueei.demos.markupDemo;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.webkit.WebView;

import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

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
		this.loadData(bos.toString().replace("%", "&#37;"), "text/html", "UTF-8");
		return this;
	}	
}
