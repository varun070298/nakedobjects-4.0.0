package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.DIAGNOSTICS_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;

public class OptionHandlerDiagnostics extends OptionHandlerAbstract {

	public OptionHandlerDiagnostics() {
		super();
	}

	public void addOption(Options options) {
		options.addOption(DIAGNOSTICS_OPT, false, "print information that can be used diagnose or report problems");
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        if (commandLine.hasOption(Constants.DIAGNOSTICS_OPT)) {
            bootPrinter.printDiagnostics();
            return false;
        }
        return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		// TODO need to do what, exactly?
	}


}
