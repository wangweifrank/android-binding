package com.gueei.android.binding.utility;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.IObservable;

public interface IModelReflector {
	public abstract Command getCommandByName(String name, Object object)
			throws Exception;

	public abstract IObservable<?> getObservableByName(String name,
			Object object) throws Exception;

	public abstract Object getValueByName(String name, Object object)
			throws Exception;

	public abstract Class<?> getValueTypeByName(String name);
}