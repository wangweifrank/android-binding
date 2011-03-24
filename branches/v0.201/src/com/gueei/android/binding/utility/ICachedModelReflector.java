package com.gueei.android.binding.utility;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.gueei.android.binding.Command;

public interface ICachedModelReflector<T> extends IModelReflector {

	public abstract HashMap<String, Field> getObservables();

	public abstract HashMap<String, Field> getCommands();

	public abstract HashMap<String, Field> getValues();

	public abstract Command getCommandByName(String name, T object)
			throws Exception;
}