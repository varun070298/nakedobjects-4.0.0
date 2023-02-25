package org.nakedobjects.runtime.options.standard;

import static org.nakedobjects.runtime.options.Constants.VIEWER_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.VIEWER_OPT;

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

public class OptionHandlerViewer extends OptionHandlerAbstract {

	private InstallerRepository installerRepository;
	private List<String> viewerNames;
	public OptionHandlerViewer(final InstallerRepository installerRepository) {
		this.installerRepository = installerRepository;
	}

	@SuppressWarnings("static-access")
	public void addOption(Options options) {
        Object[] viewers = installerRepository.getInstallers(NakedObjectsViewerInstaller.class);
        Option option = OptionBuilder.withArgName("name|class name").hasArg().withLongOpt(VIEWER_LONG_OPT).withDescription(
                "viewer to use, or for server to listen on: " + availableInstallers(viewers) + "; or class name").create(
                VIEWER_OPT);
        options.addOption(option);
		
	}

	public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
		viewerNames = getOptionValues(commandLine, Constants.VIEWER_OPT);
		return true;
	}
	
	public void primeConfigurationBuilder(
			ConfigurationBuilder configurationBuilder) {
		configurationBuilder.add(SystemConstants.VIEWER_KEY, ListUtils.listToString(viewerNames));
	}

	public List<String> getViewerNames() {
		return viewerNames;
	}

}
