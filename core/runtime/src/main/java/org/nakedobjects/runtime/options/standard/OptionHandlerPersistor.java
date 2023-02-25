package org.nakedobjects.runtime.options.standard;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.installers.InstallerRepository;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.persistence.PersistenceMechanismInstaller;
import org.nakedobjects.runtime.system.SystemConstants;

import static org.nakedobjects.runtime.options.Constants.OBJECT_PERSISTENCE_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.OBJECT_PERSISTENCE_OPT;

public class OptionHandlerPersistor extends OptionHandlerAbstract {

	private InstallerRepository installerRepository;
	private String persistorName;

	public OptionHandlerPersistor(final InstallerRepository installerRepository) {
		this.installerRepository = installerRepository;
	}

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
        Object[] objectPersistenceMechanisms = installerRepository.getInstallers(PersistenceMechanismInstaller.class);
        Option option = OptionBuilder.withArgName("name|class name").hasArg().withLongOpt(OBJECT_PERSISTENCE_LONG_OPT).withDescription(
                "object persistence mechanism to use (ignored if type is prototype or client): " + availableInstallers(objectPersistenceMechanisms)
                        + "; or class name").create(OBJECT_PERSISTENCE_OPT);
        options.addOption(option);
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		persistorName = commandLine.getOptionValue(Constants.OBJECT_PERSISTENCE_OPT);		
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		configurationBuilder.add(SystemConstants.OBJECT_PERSISTOR_INSTALLER_KEY, persistorName);
	}

	
	public String getPersistorName() {
		return persistorName;
	}

}
