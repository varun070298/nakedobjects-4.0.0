package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.VERBOSE_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;

public class OptionHandlerVerbose extends OptionHandlerAbstract {

	public OptionHandlerVerbose() {
		super();
	}

	public void addOption(Options options) {
		options.addOption(VERBOSE_OPT, false, "print information, warning and error messages");
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		// TODO need to do what, exactly???
		
	}


}
