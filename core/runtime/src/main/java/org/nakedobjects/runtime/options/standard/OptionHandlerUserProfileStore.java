package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.USER_PROFILE_STORE_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.USER_PROFILE_STORE_OPT;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.installers.InstallerRepository;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;
import org.nakedobjects.runtime.userprofile.UserProfileStoreInstaller;

public class OptionHandlerUserProfileStore extends OptionHandlerAbstract {

	private InstallerRepository installerRepository;
	private String userProfileStoreName;
	public OptionHandlerUserProfileStore(final InstallerRepository installerRepository) {
		this.installerRepository = installerRepository;
	}

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
		Object[] persistenceMechanisms = installerRepository.getInstallers(UserProfileStoreInstaller.class);
        Option option = OptionBuilder.withArgName("name|class name").hasArg().withLongOpt(USER_PROFILE_STORE_LONG_OPT).withDescription(
                "user profile store to use: " + availableInstallers(persistenceMechanisms)
                        + "; or class name").create(USER_PROFILE_STORE_OPT);
        options.addOption(option);
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		userProfileStoreName = commandLine.getOptionValue(Constants.USER_PROFILE_STORE_OPT);		
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		configurationBuilder.add(SystemConstants.PROFILE_PERSISTOR_INSTALLER_KEY, userProfileStoreName);
	}

	public String getUserProfileStoreName() {
		return userProfileStoreName;
	}

}

