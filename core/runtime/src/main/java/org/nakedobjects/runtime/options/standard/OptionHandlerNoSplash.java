package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.NO_SPLASH_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.NO_SPLASH_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;


public class OptionHandlerNoSplash extends OptionHandlerAbstract {

    private boolean noSplash;

    public OptionHandlerNoSplash() {
        super();
    }

    public void addOption(Options options) {
        options.addOption(NO_SPLASH_OPT, NO_SPLASH_LONG_OPT, false, "don't show splash window");
    }

    public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        noSplash = commandLine.hasOption(NO_SPLASH_OPT);
        return true;
    }

    public void primeConfigurationBuilder(ConfigurationBuilder configurationBuilder) {
        configurationBuilder.add(SystemConstants.NOSPLASH_KEY, noSplash ? "true" : "false");
    }

}
