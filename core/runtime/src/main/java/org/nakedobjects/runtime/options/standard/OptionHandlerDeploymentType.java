package org.nakedobjects.runtime.options.standard;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.nakedobjects.metamodel.config.ConfigurationBuilder;
import org.nakedobjects.metamodel.config.NotFoundPolicy;
import org.nakedobjects.runtime.options.BootPrinter;
import org.nakedobjects.runtime.options.Constants;
import org.nakedobjects.runtime.options.OptionHandlerAbstract;
import org.nakedobjects.runtime.system.DeploymentType;
import org.nakedobjects.runtime.system.SystemConstants;

import static org.nakedobjects.runtime.options.Constants.TYPE_LONG_OPT;
import static org.nakedobjects.runtime.options.Constants.TYPE_OPT;


public abstract class OptionHandlerDeploymentType extends OptionHandlerAbstract {

    private DeploymentType deploymentType;
    private boolean defaulted;
    private boolean primeIfDefaulted;

    public OptionHandlerDeploymentType() {
        this(true);
    }

    public OptionHandlerDeploymentType(boolean primeIfDefaulted) {
        this.primeIfDefaulted = primeIfDefaulted;
    }

    @SuppressWarnings("static-access")
    public void addOption(Options options) {
        Option option = OptionBuilder.withArgName("name").hasArg().withLongOpt(TYPE_LONG_OPT).withDescription(
                "deployment type: " + types()).create(TYPE_OPT);
        options.addOption(option);
    }

    protected abstract String types();

    public boolean handle(CommandLine commandLine, BootPrinter bootPrinter, Options options) {
        String deploymentTypeName = commandLine.getOptionValue(Constants.TYPE_OPT);
        if (deploymentTypeName == null) {
            deploymentType = defaultType();
            defaulted = true;
            return true;
        }

        deploymentType = DeploymentType.lookup(deploymentTypeName.toUpperCase());
        if (deploymentType != null) {
            return true;
        }
        bootPrinter.printErrorAndHelp(options, "Unable to determine deployment type");
        return false;
    }

    protected abstract DeploymentType defaultType();

    public void primeConfigurationBuilder(ConfigurationBuilder configurationBuilder) {
        if (defaulted && !primeIfDefaulted) {
            return;
        }
        String type = deploymentType.name().toLowerCase();
        configurationBuilder.addConfigurationResource(type + ".properties", NotFoundPolicy.CONTINUE);

        configurationBuilder.add(SystemConstants.DEPLOYMENT_TYPE_KEY, deploymentType.name());
        configurationBuilder.add(SystemConstants.DEPLOYMENT_TYPE_DEFAULTED_KEY, "" + defaulted);
    }

    public DeploymentType getDeploymentType() {
        return deploymentType;
    }

}

