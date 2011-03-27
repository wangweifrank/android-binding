package gueei.binding.utility;

import gueei.binding.Command;
import gueei.binding.IObservable;
import gueei.binding.Utility;

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
