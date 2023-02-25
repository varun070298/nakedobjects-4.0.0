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

public final class FlagHandlerAddress implements OptionHandler {
	private String address;
	static final String ADDRESS_OPT = "a";
	static final String ADDRESS_LONG_OPT = "address";

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
	    Option option = OptionBuilder.withArgName("address").hasArg().withLongOpt(FlagHandlerAddress.ADDRESS_LONG_OPT).withDescription(
	    "address to listen on").create(FlagHandlerAddress.ADDRESS_OPT);
	    options.addOption(option);
	}

	public boolean handle(CommandLine commandLine,
			BootPrinter bootPrinter, Options options) {
	    address = commandLine.getOptionValue(FlagHandlerAddress.ADDRESS_OPT);
		return true;
	}

	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		if (address == null) {
			return;
		}
		configurationBuilder.add(WebServerConstants.EMBEDDED_WEB_SERVER_ADDRESS_KEY, address);
	}
}