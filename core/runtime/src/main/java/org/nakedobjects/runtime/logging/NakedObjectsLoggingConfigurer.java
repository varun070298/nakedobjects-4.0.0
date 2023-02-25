package org.nakedobjects.runtime.logging;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Appender;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.PropertyConfigurator;
import org.nakedobjects.runtime.options.Constants;


public class NakedObjectsLoggingConfigurer {

    private static boolean loggingSetup;

    /**
     * As per {@link #configureLogging(String)}.
     * 
     * <p>
     * The root logging level can also be adjusted using command line arguments.
     */
    public static void configureLogging(final String configDirectory, String[] args) {
        if (loggingSetup) {
            return;
        }
        loggingSetup = true;
        configureLogging(configDirectory);
        applyLoggingLevelFromCommandLine(args);
    }

    private static void applyLoggingLevelFromCommandLine(String[] args) {
        Level loggingLevel = loggingLevel(args);
        if (loggingLevel != null) {
            Logger.getRootLogger().setLevel(loggingLevel);
        }
    }

    /**
     * Sets up logging using either a logging file or (if cannot be found) some sensible defaults.
     * 
     * <p>
     * If a {@link LoggingConstants#LOGGING_CONFIG_FILE logging config file} can be located in the provided
     * directory, then that is used. Otherwise, will set up the {@link Logger#getRootLogger() root logger} to
     * {@link Level#WARN warning}, a typical {@link PatternLayout} and logging to the {@link ConsoleAppender
     * console}.
     */
    private static void configureLogging(final String configDirectory) {
        Properties properties = new Properties();
        String path = configDirectory + "/" + LoggingConstants.LOGGING_CONFIG_FILE;
        try {
            FileInputStream inStream = new FileInputStream(path);
            properties.load(inStream);
        } catch (IOException ignore) {}

        if (properties.size() == 0) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                InputStream inStream = classLoader.getResourceAsStream(path);
                if (inStream != null) {
                    properties.load(inStream);
                }
            } catch (IOException ignore) {}
        }

        if (properties.size() > 0) {
            PropertyConfigurator.configure(properties);
        } else {
            configureFallbackLogging();
        }
    }

    private static void configureFallbackLogging() {
        final Layout layout = new PatternLayout("%-5r [%-25.25c{1} %-10.10t %-5.5p]  %m%n");
        final Appender appender = new ConsoleAppender(layout);
        BasicConfigurator.configure(appender);
        Logger.getRootLogger().setLevel(Level.WARN);
        Logger.getLogger("ui").setLevel(Level.OFF);
    }

    private static Level loggingLevel(final String[] args) {
        Level level = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-" + Constants.DEBUG_OPT)) {
                level = Level.DEBUG;
                break;
            } else if (args[i].equals("-" + Constants.QUIET_OPT)) {
                level = Level.ERROR;
                break;
            } else if (args[i].equals("-" + Constants.VERBOSE_OPT)) {
                level = Level.INFO;
                break;
            }
        }
        return level;
    }

}

// Copyright (c) Naked Objects Group Ltd.
