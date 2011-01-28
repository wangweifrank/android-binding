package com.gueei.android.binding.listeners;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;

import android.view.View;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.ViewAttribute;

public abstract class MulticastListener<T> {
	public abstract void registerToView(View v);
	
	protected ArrayList<T> listeners = new ArrayList<T>(0);
	protected ArrayList<Command> commands = new ArrayList<Command>(1);
	protected ArrayList<ViewAttribute<?,?>> attributes = new ArrayList<ViewAttribute<?,?>>(1);
	public void register(Command command){
		commands.add(command);
	}

	public void register(T listener){
		listeners.add(listener);
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
	
	public void invokeCommands(View view, Object... args){
		for(Command command : commands){
			command.Invoke(view, args);
		}
	}
		
	public static class Factory{
		public static void RegisterConstructorE(Class<?> type, Constructor<?> constructor){
			constructors.put(type, constructor);
		}
		
		public static void RegisterConstructor(Class<?> type, Class<?> listener){
			try {
				RegisterConstructorE(type, listener.getConstructor());
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private static HashMap<Class<?>, Constructor<?>>
			constructors = new HashMap<Class<?>, Constructor<?>>();
		
		@SuppressWarnings("unchecked")
		public static <T> MulticastListener<T> create(T listenerType, View v){
			if (constructors.containsKey(listenerType)){
				try {
					MulticastListener<T> listener = (MulticastListener<T>)constructors.get(listenerType).newInstance();
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
