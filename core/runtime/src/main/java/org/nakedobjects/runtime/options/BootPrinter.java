package org.nakedobjects.runtime.options;

import java.io.PrintStream;
import java.io.PrintWriter;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.nakedobjects.runtime.system.internal.monitor.SystemPrinter;


public class BootPrinter extends SystemPrinter {
    
    private final PrintWriter printWriter;
    private final String className;
    
    public BootPrinter(final Class cls, final PrintStream output) {
        super(output);
        this.printWriter = new PrintWriter(getOutput());
        className = cls.getName().substring(cls.getName().lastIndexOf('.') +1);
    }
    
    public BootPrinter(final Class cls) {
        this(cls, System.out);
    }

    public void printErrorAndHelp(final Options options, final String formatStr, Object... args) {
        getOutput().println(String.format(formatStr, args));
        printHelp(options);
        printWriter.flush();
    }

    public void printHelp(final Options options) {
        final HelpFormatter help = new HelpFormatter();
        help.printHelp(printWriter, 80, className + " [options]", null, options, 0, 0, null, false);
        printWriter.flush();
    }


}
