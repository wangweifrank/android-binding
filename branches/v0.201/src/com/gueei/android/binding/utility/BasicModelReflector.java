package com.gueei.android.binding.utility;

import com.gueei.android.binding.Command;
import com.gueei.android.binding.IObservable;
import com.gueei.android.binding.Utility;

public class BasicModelReflector implements IModelReflector{
	public Command getCommandByName(String name, Object object)
			throws Exception {
		return Utility.getCommandForModel(name, object);
	}

	public IObservable<?> getObservableByName(String name, Object object)
			throws Exception {
		return Utility.getObservableForModel(name, object);
	}

	public Object getValueByName(String name, Object object) throws Exception {
		return null;
	}

	public Class<?> getValueTypeByName(String name) {
		return null;
	}
}
