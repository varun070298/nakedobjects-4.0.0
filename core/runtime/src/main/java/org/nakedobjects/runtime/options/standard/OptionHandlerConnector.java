package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.CONNECTOR_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.CONNECTOR_OPT;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.commons.lang.ListUtils;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.runtime.installers.InstallerRepository;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.SystemConstants;
import org.nakedobjects.runtime.viewer.NakedObjectsViewerInstaller;

public class OptionHandlerConnector extends OptionHandlerAbstract {

	private InstallerRepository installerRepository;
	private List<String> connectorNames;
	
	public OptionHandlerConnector(final InstallerRepository installerRepository) {
		this.installerRepository = installerRepository;
	}

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
        Object[] connectors = installerRepository.getInstallers(NakedObjectsViewerInstaller.class);
        Option option = OptionBuilder.withArgName("name|class name").hasArg().withLongOpt(CONNECTOR_LONG_OPT).withDescription(
                "connector to use for client requests, or for server to listen on: " + availableInstallers(connectors)).create(
                CONNECTOR_OPT);
        options.addOption(option);
		
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		connectorNames = getOptionValues(commandLine, Constants.CONNECTOR_OPT);
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		configurationBuilder.add(SystemConstants.CLIENT_CONNECTION_KEY, ListUtils.listToString(connectorNames));
	}
	
	public List<String> getConnectorNames() {
		return connectorNames;
	}

}
