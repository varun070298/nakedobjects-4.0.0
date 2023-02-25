package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.ADDITIONAL_PROPERTY;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;

public class OptionHandlerAdditionalProperty extends OptionHandlerAbstract {

	private List<String> additionalProperties;

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
        Option option = OptionBuilder.withArgName("property=value").hasArg().withValueSeparator().withDescription(
        "use value for given property").create(ADDITIONAL_PROPERTY);
		option.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(option);
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        additionalProperties = getOptionValues(commandLine, Constants.ADDITIONAL_PROPERTY);
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		addConfigurationProperties(configurationBuilder, additionalProperties);
	}

    private void addConfigurationProperties(
    		final ConfigurationBuilder configurationBuilder, 
    		final List<String> additionalProperties) {
        if (additionalProperties == null) {
        	return;
        } 
        String key = null, value = null;
		for (String additionalProperty: additionalProperties) {
			if (key == null) {
				key = additionalProperty;
			} else {
				value = additionalProperty;
				configurationBuilder.add(key, value);
				key = null;
			}
		}
    }



}
