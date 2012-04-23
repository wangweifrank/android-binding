package gueei.binding.markupDemoICS.widget;

import gueei.binding.IBindableView;
import gueei.binding.ViewAttribute;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

public class CodeView extends WebView implements IBindableView<CodeView> {

	public CodeView(Context context, AttributeSet attrs, int defStyle,
			boolean privateBrowsing) {
		super(context, attrs, defStyle, privateBrowsing);
	}

	public CodeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CodeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CodeView(Context context) {
		super(context);
	}

	@Override
	public ViewAttribute<? extends View, ?> createViewAttribute(
			String attributeId) {
		if ("resourceId".equals(attributeId)){
			return resId;
		}else if ("resourceType".equals(attributeId)){
			return resType;
		}
		return null;
	}

	private final ResourceIdViewAttribute resId = 
			new ResourceIdViewAttribute(this, "resourceId");
	
	private final ResourceTypeViewAttribute resType = 
			new ResourceTypeViewAttribute(this, "resourceType");
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		renderView();
		super.onDraw(canvas);
	}

	private void renderView(){
		if (isDirty){
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        try {
				XhtmlRendererFactory.getRenderer(resType.get()).highlight("title", 
						getContext().getResources().openRawResource(resId.get()), 
						bos, "UTF-8", false);
		        isDirty = false;
		        
		        this.loadData(bos.toString(), "text/html", "UTF-8");
		        //this.loadUrl("http://www.yahoo.com");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean isDirty = false;
	
	private class ResourceIdViewAttribute extends ViewAttribute<CodeView, Integer>{
		public ResourceIdViewAttribute(CodeView view,
				String attributeName) {
			super(Integer.class, view, attributeName);
		}

		private int mValue = 0;
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			mValue = Integer.parseInt(newValue.toString());
			isDirty = true;
			invalidate();
		}

		@Override
		public Integer get() {
			return mValue;
		}
	}

	private class ResourceTypeViewAttribute extends ViewAttribute<CodeView, String>{
		public ResourceTypeViewAttribute(CodeView view,
				String attributeName) {
			super(String.class, view, attributeName);
		}

		private String mValue = "xml";
		
		@Override
		protected void doSetAttributeValue(Object newValue) {
			mValue = newValue.toString();
			isDirty = true;
			invalidate();
		}

		@Override
		public String get() {
			return mValue;
		}
	}
}
