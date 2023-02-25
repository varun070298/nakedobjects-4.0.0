package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.USER_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.USER_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;

public class OptionHandlerUser extends OptionHandlerAbstract {

	private String userName;

	public OptionHandlerUser() {
		super();
	}

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
        Option option = OptionBuilder.withArgName("user name").hasArg().withLongOpt(USER_LONG_OPT).withDescription(
        	"user name to log in with").create(USER_OPT);
        options.addOption(option);

	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		userName = commandLine.getOptionValue(Constants.USER_OPT);
		return true;		
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		configurationBuilder.add(SystemConstants.USER_KEY, userName);
	}

	public String getUserName() {
		return userName;
	}

}
