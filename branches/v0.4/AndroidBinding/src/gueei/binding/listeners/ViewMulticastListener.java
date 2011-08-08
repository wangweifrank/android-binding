package gueei.binding.listeners;

import gueei.binding.MulticastListener;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;


public abstract class ViewMulticastListener<T> extends MulticastListener<View, T> {
	public abstract void registerToView(View v);
	
	@Override
	public void registerToHost(View host) {
		registerToView(host);
	}

	protected ArrayList<T> listeners = new ArrayList<T>(0);

	public void removeListener(T listener){
		listeners.remove(listener);
	}
	
	public void register(T listener){
		listeners.add(listener);
	}
	
	public void registerWithHighPriority(T listener){
		listeners.add(0, listener);
	}
	
	private boolean mBroadcasting = true;
	public void nextActionIsNotFromUser(){
		mBroadcasting = false;
	}
	
	protected boolean isFromUser(){
		return mBroadcasting;
	}
	
	protected void clearBroadcastState(){
		mBroadcasting = true;
	}
	
	public static class Factory{
		public static void RegisterConstructorE(Class<?> type, Constructor<?> constructor){
			constructors.put(type, constructor);
		}
		
		public static void RegisterConstructor(Class<?> type, Class<?> listener){
			try {
				RegisterConstructorE(type, listener.getConstructor());
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		
		private static HashMap<Class<?>, Constructor<?>>
			constructors = new HashMap<Class<?>, Constructor<?>>();
		
		@SuppressWarnings("unchecked")
		public static <T> ViewMulticastListener<T> create(T listenerType, View v){
			if (constructors.containsKey(listenerType)){
				try {
					ViewMulticastListener<T> listener = (ViewMulticastListener<T>)constructors.get(listenerType).newInstance();
					listener.registerToView(v);
					return listener;
				} catch (Exception e) {
					return null;
				}
			}
			return null;
		}
	}
}
