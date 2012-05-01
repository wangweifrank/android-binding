package gueei.binding.converters;

import gueei.binding.BindingSyntaxResolver;
import gueei.binding.Converter;
import gueei.binding.DynamicObject;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.Observer;
import gueei.binding.Undetermined;

import java.util.ArrayList;
import java.util.Collection;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JS extends Converter<Object> implements Undetermined{
	private ArrayList<IObservable<?>> observingScriptObjects =
			new ArrayList<IObservable<?>>();
	
	public JS(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... arg0) throws Exception {
		if (arg0.length < 2) return null;
		DynamicObject args;
		if (arg0.length > 2 && arg0[2] instanceof DynamicObject){
			args = (DynamicObject)arg0[2];
		}else{
			args = new DynamicObject();
		}
		for(IObservable<?> obs : observingScriptObjects){
			obs.unsubscribe(ScriptObjectObserver);
		}
		return runScript(arg0[0], args, arg0[1].toString());
	}
	
	private Observer ScriptObjectObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> arg0,
				Collection<Object> arg1) {
			JS.this.onPropertyChanged(arg0, arg1);
		}
	};
	
	private Object runScript(Object vm, DynamicObject args, String script){
    	Context cx = Context.enter();
    	cx.setOptimizationLevel(-1);
    	try{
    		ObservableScriptable scope = new ObservableScriptable(vm, args);
	    	scope.setParentScope(cx.initStandardObjects());
	    	Object result = cx.evaluateString(scope, script, "<cmd>", 1, null);
	    	return evalResult(result);
    	}catch(Exception e){
    		return e;
    	}finally{
    		Context.exit();
    	}
    }
	
	private static Object evalResult(Object result){
		if (result==null) return null;
		if (result instanceof ObservableScriptable)
			return ((ObservableScriptable)result).getVm();
		return result;
	}
	
	private class ObservableScriptable extends ScriptableObject{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8607713699623612453L;
		private final Object mVm;
		private final DynamicObject mArgs;
		public ObservableScriptable(Object vm, DynamicObject args){
			mVm = vm;
			mArgs = args;
		}
		
		public Object getVm(){
			return mVm;
		}
		
		@Override
		public String getClassName() {
			return null;
		}

		@Override
		public Object get(int index, Scriptable arg1) {
			Object local = super.get(index, arg1);
			if (!local.equals(NOT_FOUND)) return local;
			if (mVm instanceof IObservableCollection)
				return getObject(((IObservableCollection<?>)mVm).getItem(index));
			
			return NOT_FOUND;
		}

		private Object getObject(Object value){
			if (value==null) return null;
			
			if (value instanceof Number || value instanceof String || value instanceof Boolean)
				return value;
			
			return new ObservableScriptable(value, null);
		}
		
		@Override
		public Object get(String arg0, Scriptable arg1) {
			if (arg0.equals("$")) return new ConverterStatementScriptable(mVm);
			
			Object local = super.get(arg0, arg1);
			if (!local.equals(NOT_FOUND)) return local;

			if (mArgs!=null){
				try {
					IObservable<?> obs = mArgs.getObservableByName(arg0);
					if (obs!=null){
						Object value = obs.get();
						//observingScriptObjects.add(obs);
						//obs.subscribe(ScriptObjectObserver);

						return getObject(value);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			IObservable<?> obs = 
					BindingSyntaxResolver.constructObservableFromStatement(getContext(), arg0, mVm);
			
			if (obs!=null){
				Object value = obs.get();
				observingScriptObjects.add(obs);
				obs.subscribe(ScriptObjectObserver);

				return getObject(value);
			}
			
			return NOT_FOUND;
		}

		@Override
		public Object getDefaultValue(Class<?> arg0) {
			return mVm.toString();
		}
	}
	
	private class ConverterStatementScriptable extends BaseFunction{
		/**
		 * 
		 */
		private static final long serialVersionUID = -996514627750549487L;
		private final Object mVm;
		public ConverterStatementScriptable(Object vm){
			mVm = vm;
		}
		
		@Override
		public Object call(Context arg0, Scriptable arg1, Scriptable arg2,
				Object[] arg3) {
			if (arg3.length < 1) return NOT_FOUND;
			IObservable<?> obs = 
					BindingSyntaxResolver.constructObservableFromStatement(getContext(), arg3[0].toString(), mVm);
			if (obs!=null){
				Object value = obs.get();
				observingScriptObjects.add(obs);
				obs.subscribe(ScriptObjectObserver);
				return value;
			}
			return super.call(arg0, arg1, arg2, arg3);
		}
	}
}
