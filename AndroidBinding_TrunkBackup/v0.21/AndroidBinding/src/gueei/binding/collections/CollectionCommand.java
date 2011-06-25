package gueei.binding.collections;

import gueei.binding.Command;

public interface CollectionCommand extends Command {
	public void Invoke(int position);
}
