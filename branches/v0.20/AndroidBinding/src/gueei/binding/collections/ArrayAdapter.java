package gueei.binding.collections;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.utility.CachedModelReflector;
import gueei.binding.utility.ICachedModelReflector;
import gueei.binding.R;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@Deprecated
public class ArrayAdapter<T> extends BaseAdapter {
	protected final Context mContext;
	protected final int mLayoutId;
	protected final int mDropDownLayoutId;
	protected final T[] mArray;
	protected final ICachedModelReflector<T> mReflector;
	protected String[] observableNames = new String[0];
	protected String[] commandNames = new String[0];
	protected String[] valueNames = new String[0];
		
	public ArrayAdapter(Context context, Class<T> arrayType, T[] array, int layoutId, int dropDownLayoutId) throws Exception{
		mContext = context;
		mLayoutId = layoutId;
		mDropDownLayoutId = dropDownLayoutId;
		mArray = array;
		mReflector = new CachedModelReflector<T>(arrayType);
		observableNames = mReflector.getObservables().keySet().toArray(observableNames);
		commandNames = mReflector.getCommands().keySet().toArray(commandNames);
		valueNames = mReflector.getValues().keySet().toArray(valueNames);
	}

	public int getCount() {
		return mArray.length;
	}

	public Object getItem(int arg0) {
		return mArray[arg0];
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	private View getView(int position, View convertView, ViewGroup parent, int layoutId) {
		View returnView = convertView;
		if (position>=mArray.length) return returnView;
		try {
			ObservableMapper mapper;
			if ((convertView == null) || ((mapper = getAttachedMapper(convertView))==null)) {
			//if (true){
				Binder.InflateResult result = Binder.inflateView(mContext,
						layoutId, parent, false);
				mapper = new ObservableMapper();
				mapper.startCreateMapping(mReflector, mArray[position]);
				for(View view: result.processedViews){
					AttributeBinder.getInstance().bindView(view, mapper);
				}
				mapper.endCreateMapping();
				returnView = result.rootView;				
				this.putAttachedMapper(returnView, mapper);
			}
			synchronized(ArrayAdapter.class){
				mapper.changeMapping(mReflector, mArray[position]);
			}
			return returnView;
		} catch (Exception e) {
			e.printStackTrace();
			return returnView;
		}
	}
	
	private ObservableMapper getAttachedMapper(View convertView){
		Object mappers = convertView.getTag(R.id.tag_observableCollection_attachedObservable);
		if (mappers==null){
			return null;
		}
		return (ObservableMapper)mappers;
	}
	
	private void putAttachedMapper(View convertView, ObservableMapper mapper){
		convertView.setTag(R.id.tag_observableCollection_attachedObservable, mapper);
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return
			mDropDownLayoutId > 0 ?
					getView(position, convertView, parent, mDropDownLayoutId) :
						getView(position, convertView, parent, mLayoutId);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent, mLayoutId);
	}
}