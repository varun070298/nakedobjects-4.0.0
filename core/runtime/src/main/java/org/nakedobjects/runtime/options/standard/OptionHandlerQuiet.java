package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.QUIET_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;

public class OptionHandlerQuiet extends OptionHandlerAbstract {

	public OptionHandlerQuiet() {
		super();
	}

	public void addOption(Options options) {
		options.addOption(QUIET_OPT, false, "print error messages only");
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		// TODO need to do what, exactly???
	}


}
