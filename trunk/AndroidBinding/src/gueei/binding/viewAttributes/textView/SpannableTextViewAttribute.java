package gueei.binding.viewAttributes.textView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import gueei.binding.BindingType;
import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.IObservableCollection;
import gueei.binding.ViewAttribute;
import gueei.binding.collections.ObservableCollection;
import gueei.binding.observables.SpanObservable.Span;
import android.text.Spannable;
import android.widget.TextView;

public class SpannableTextViewAttribute extends ViewAttribute<TextView, Object> {
	
	Object oldValue = null;
	
	private CollectionObserver attrObserver = new CollectionObserver(){
		@Override
		public void onCollectionChanged(IObservableCollection<?> collection,
				CollectionChangedEventArg args, Collection<Object> initiators) {	
			updateInternal(collection);
		}
	};

	public SpannableTextViewAttribute(TextView view) {
		super(Object.class, view, "span");		

	}

	@Override
	protected void doSetAttributeValue(Object newValue) {
		updateInternal(newValue);
	}

	@Override
	protected BindingType AcceptThisTypeAs(Class<?> type) {
		return BindingType.OneWay;
	}

	@Override
	public Object get() {
		return null;
	}
	
	public boolean hasValue() {
		return oldValue != null;
	}

	protected void updateInternal(Object newValue) {
		
		if(!(getView().getText() instanceof Spannable)) {
			// make spanable (note this should be done somewhere else!)
			getView().setTextKeepState(getView().getText(), TextView.BufferType.SPANNABLE);
		}
		
		if(!(getView().getText() instanceof Spannable)) {
			return;
		}
		
		Spannable sText = (Spannable) getView().getText();
		int length = getView().getText().length();
		
		if(oldValue != null) {
			if(oldValue instanceof Span ) {
				sText.removeSpan(((Span) oldValue).What);
			} else if( oldValue instanceof List) {
				@SuppressWarnings("rawtypes")
				List list = (List)oldValue;				
				for( Object o : list) {
					if(o instanceof Span)
						sText.removeSpan(((Span) o).What);	
				}
			}
		}
		
		if( newValue instanceof Span ) {
			Span s = (Span) newValue;
			if(s.What != null)
				safeSetSpan(sText,s,length);
			oldValue = newValue;
		} else if(newValue instanceof List) {
			ArrayList<Object> newList = new ArrayList<Object>();
			oldValue = newList;
			
			List<?> list = (List<?>)newValue;		
			
			for( Object o : list) {
				if( o instanceof Span) {
					Span s = (Span) o;
					if(s.What != null) {
						if( safeSetSpan(sText,s,length) )						
							newList.add(s);
					}
				}
			}			
			
			if(newValue instanceof ObservableCollection<?>) {
				ObservableCollection<?> col = (ObservableCollection<?>)newValue;
				col.subscribe(attrObserver);
			}
		} else {
			oldValue = null;
		}
		
		/*
		 
		// idea from: http://www.chrisumbel.com/article/android_textview_rich_text_spannablestring
		// ClickableSpan / URLSpans needs this
		// but this should not be done for all texts - no idea how we can do this on demand
		
		if(oldValue != null) {
			// make ClickableSpans and URLSpans work  
			//getView().setMovementMethod(LinkMovementMethod.getInstance());  
		}
		*/		
		
		int x = 123;
		x++;
		x++;
		x++;
		x++;
		x++;
	}
	
	private boolean safeSetSpan(Spannable sText, Span s, int length) {
		if(sText == null || s == null || length < 1)
			return false;
		
		int start = s.Start;
		int end = s.End;
		
		if(start == 0 && end == 0)
			end = length;
		
		if(start > length)
			return false;		
		if(end > length)
			return false;			
		if(end < start)
			return false;
		
		sText.setSpan(s.What, start, end, s.Flags);
		return true;
	}

	public void update() {
		if(oldValue == null)
			return;		
		updateInternal(oldValue);
	}	
}
