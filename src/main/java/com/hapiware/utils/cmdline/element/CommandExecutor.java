package com.hapiware.utils.cmdline.element;

import java.util.List;

public interface CommandExecutor
{
	public void execute(Command.Inner command, List<Option.Inner> globalOptions);
}
