package org.nakedobjects.runtime.options.standard;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;

import static org.nakedobjects.runtime.options.Constants.DEBUG_OPT;


public class OptionHandlerDebug extends OptionHandlerAbstract {

    public OptionHandlerDebug() {
        super();
    }

    public void addOption(Options options) {
        options.addOption(DEBUG_OPT, false, "print debugging messages");
    }

    public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        return true;
    }

    public void primeConfigurationBuilder(ConfigurationBuilder configurationBuilder) {
        // TODO need to prime or otherwise set logging.
    }

}
