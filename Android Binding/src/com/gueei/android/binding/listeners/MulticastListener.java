package com.gueei.android.binding.listeners;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

import com.gueei.android.binding.Command;

import android.view.View;

public abstract class MulticastListener<T> {
	public abstract void registerToView(View v);
	
	protected ArrayList<Command> commands = new ArrayList<Command>(2);
	public void register(Command command){
		commands.add(command);
	}
	
	public void invoke(View view, Object... args){
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
