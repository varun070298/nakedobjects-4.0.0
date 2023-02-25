/**
 * 
 */
package org.nakedobjects.webserver.internal;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.OptionHandler;
import org.nakedobjects.webserver.WebServerConstants;

public final class FlagHandlerPort implements OptionHandler {
	private Integer port;
	static final String PORT_LONG_OPT = "port";
	static final String PORT_OPT = "p";

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
	    OptionBuilder.withArgName("port");
		Option option = OptionBuilder.hasArg().withLongOpt(FlagHandlerPort.PORT_LONG_OPT)
	    .withDescription("port to listen on").create(FlagHandlerPort.PORT_OPT);
	    options.addOption(option);
	}

	public boolean handle(CommandLine commandLine,
			BootPrinter bootPrinter, Options options) {
	    String portStr = commandLine.getOptionValue(FlagHandlerPort.PORT_OPT);
	    if (portStr != null){
	    	port = Integer.parseInt(portStr);
	    }
	    return true;
	}

	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		if (port == null) {
			return;
		}
		configurationBuilder.add(WebServerConstants.EMBEDDED_WEB_SERVER_PORT_KEY, ""+port);
	}
}