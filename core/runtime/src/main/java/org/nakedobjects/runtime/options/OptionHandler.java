package org.nakedobjects.runtime.options;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationPrimer;

public interface OptionHandler extends ConfigurationPrimer {

	public void addOption(Options options);
	
	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options);
}
