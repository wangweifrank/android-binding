package gueei.binding.converters;

import gueei.binding.BindingSyntaxResolver;
import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.Observer;

import java.util.ArrayList;
import java.util.Collection;

import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class JS extends Converter<Object> {
	private ArrayList<IObservable<?>> observingScriptObjects =
			new ArrayList<IObservable<?>>();
	
	public JS(IObservable<?>[] dependents) {
		super(Object.class, dependents);
	}

	@Override
	public Object calculateValue(Object... arg0) throws Exception {
		if (arg0.length < 2) return null;
		for(IObservable<?> obs : observingScriptObjects){
			obs.unsubscribe(ScriptObjectObserver);
		}
		return runScript(arg0[0], arg0[1].toString());
	}
	
	private Observer ScriptObjectObserver = new Observer(){
		public void onPropertyChanged(IObservable<?> arg0,
				Collection<Object> arg1) {
			JS.this.onPropertyChanged(arg0, arg1);
		}
	};
	
	private Object runScript(Object vm, String script){
    	Context cx = Context.enter();
    	cx.setOptimizationLevel(-1);
    	try{
	    	Scriptable scope = new ObservableScriptable(vm);
	    	scope.setParentScope(cx.initStandardObjects());
	    	Object result = cx.evaluateString(scope, script, "<cmd>", 1, null);
	    	return result;
    	}catch(Exception e){
    		return e;
    	}finally{
    		Context.exit();
    	}
    }
	
	private class ObservableScriptable extends ScriptableObject{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8607713699623612453L;
		private final Object mVm;
		public ObservableScriptable(Object vm){
			mVm = vm;
		}
		
		@Override
		public String getClassName() {
			return null;
		}

		@Override
		public Object get(int arg0, Scriptable arg1) {
			return super.get(arg0, arg1);
		}

		@Override
		public Object get(String arg0, Scriptable arg1) {
			if (arg0.equals("$")) return new ConverterStatementScriptable(mVm);
			if (arg0.equals("$set")) return new SetScriptable();
			
			Object local = super.get(arg0, arg1);
			if (!local.equals(NOT_FOUND)) return local;
			
			IObservable<?> obs = 
					BindingSyntaxResolver.constructObservableFromStatement(getContext(), arg0, mVm);
			
			if (obs!=null){
				Object value = obs.get();
				observingScriptObjects.add(obs);
				obs.subscribe(ScriptObjectObserver);

				if (value instanceof Number || value instanceof String || value instanceof Boolean)
					return value;
				
				return new ObservableScriptable(value);
			}
			
			return NOT_FOUND;
		}

		@Override
		public Object getDefaultValue(Class<?> arg0) {
			return mVm.toString();
		}
	}
	
	private class SetScriptable extends BaseFunction{
		/**
		 * 
		 */
		private static final long serialVersionUID = 77860699705725682L;

		@Override
		public Object call(Context arg0, Scriptable arg1, Scriptable arg2,
				Object[] arg3) {
			JS.this.setWithoutNotify(arg3[0]);
			return arg3[0];
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
