package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.REFLECTOR_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.REFLECTOR_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.specloader.NakedObjectReflectorInstaller;
import org.nakedobjects.runtime.installers.InstallerRepository;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;

public class OptionHandlerReflector extends OptionHandlerAbstract {

	private InstallerRepository installerRepository;
	private String reflector;
	public OptionHandlerReflector(final InstallerRepository installerRepository) {
		this.installerRepository = installerRepository;
	}

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
        Object[] reflectors = installerRepository.getInstallers(NakedObjectReflectorInstaller.class);
        Option option = OptionBuilder.withArgName("name|class name").hasArg().withLongOpt(REFLECTOR_LONG_OPT).withDescription(
                "reflector to use (ignored if type is prototype or client): " + availableInstallers(reflectors)
                        + "; or class name").create(REFLECTOR_OPT);
        options.addOption(option);
		
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		reflector = commandLine.getOptionValue(Constants.REFLECTOR_OPT);
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		configurationBuilder.add(SystemConstants.REFLECTOR_KEY, reflector);
	}


}
