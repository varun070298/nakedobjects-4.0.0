package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.FIXTURE_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.FIXTURE_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;

public class OptionHandlerFixture extends OptionHandlerAbstract {

	private String fixture;

	public OptionHandlerFixture() {
		super();
	}

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
		Option option = OptionBuilder.withArgName("class name").hasArg().withLongOpt(FIXTURE_LONG_OPT).withDescription(
        "fully qualified fixture class").create(FIXTURE_OPT);
		options.addOption(option);
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		fixture = commandLine.getOptionValue(Constants.FIXTURE_OPT);		
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		configurationBuilder.add(SystemConstants.FIXTURE_KEY, fixture);
	}


}
