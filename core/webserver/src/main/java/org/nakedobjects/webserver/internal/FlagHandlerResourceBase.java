package org.nakedobjects.webserver.internal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.OptionHandler;
import org.nakedobjects.webserver.WebServerConstants;

public final class FlagHandlerResourceBase implements OptionHandler {
	private String resourceBase;
	static final String RESOURCE_BASE_LONG_OPT = "webapp";
	static final String RESOURCE_BASE_OPT = "w";

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
	    Option option = OptionBuilder.withArgName("webapp directory").hasArg().withLongOpt(FlagHandlerResourceBase.RESOURCE_BASE_LONG_OPT).withDescription(
	    "directory holding webapp").create(FlagHandlerResourceBase.RESOURCE_BASE_OPT);
	    options.addOption(option);
	}

	public boolean handle(CommandLine commandLine,
			BootPrinter bootPrinter, Options options) {
	    resourceBase = commandLine.getOptionValue(FlagHandlerResourceBase.RESOURCE_BASE_OPT, resourceBase);
		return true;
	}

	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		if (resourceBase == null) {
			return;
		}
		configurationBuilder.add(WebServerConstants.EMBEDDED_WEB_SERVER_RESOURCE_BASE_KEY, resourceBase);
	}
}