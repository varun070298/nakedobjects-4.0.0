package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.HELP_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.HELP_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;

public class OptionHandlerHelp extends OptionHandlerAbstract {

	public OptionHandlerHelp() {
		super();
	}

	public void addOption(Options options) {
		options.addOption(HELP_OPT, HELP_LONG_OPT, false, "show this help");
		
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        if (commandLine.hasOption(Constants.HELP_OPT)) {
        	bootPrinter.printHelp(options);
            return false;
        }
        return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		// nothing to do
		
	}


}
