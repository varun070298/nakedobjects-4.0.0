package org.nakedobjects.runtime.system.internal.monitor;

import java.io.File;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

import org.nakedobjects.metamodel.commons.about.AboutNakedObjects;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class SystemPrinter {

    private PrintStream output;

    public SystemPrinter() {
        this(System.out);
    }

    public SystemPrinter(final PrintStream output) {
        this.output = output;
    }

    protected PrintStream getOutput() {
        return output;
    }

    void print(final String string) {
        output.println(string);
    }

    void printBlock(final String title) {
        print("");
        print("------------------------------------------");
        print(title);
        print("------------------------------------------");
    }

    public void printDiagnostics() {
        print("------- Naked Objects diagnostics report -------");
        printVersion();

        printBlock("System properties");
        final Properties properties = System.getProperties();
        final Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            final String name = (String) propertyNames.nextElement();
            String property = properties.getProperty(name);
            if (name.endsWith(".path") || name.endsWith(".dirs")) {
                String[] split = property.split(":");
                property = split[0];
                for (int i = 1; i < split.length; i++) {
                    property += "\n\t\t" + split[i];
                }
            }
            print(name + "= " + property);
        }

        File file = new File("../lib");
        if (file.isDirectory()) {
            final String[] files = file.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".jar");
                }
            });
            printBlock("Libs");
            for (int i = 0; i < files.length; i++) {
                print(files[i]);
            }
        }

        printBlock("Locale information");
        print("Default locale: " + Locale.getDefault());
        print("Default timezone: " + TimeZone.getDefault());

        file = new File("config");
        if (file.isDirectory()) {
            final String[] files = file.list(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return new File(dir, name).isFile();
                }
            });
            printBlock("Config files");
            for (int i = 0; i < files.length; i++) {
                print(files[i]);
            }

            for (int i = 0; i < files.length; i++) {
                print("");
                print("--------------------------------------------------------------------------------------------------------");
                print(files[i]);
                print("");
                try {
                    LineNumberReader fileInputStream = new LineNumberReader(new FileReader(new File(file, files[i])));
                    String line;
                    while ((line = fileInputStream.readLine()) != null) {
                        print(fileInputStream.getLineNumber() + "  " + line);
                    }
                } catch (Exception e) {
                    throw new NakedObjectException(e);
                }
                print("");
            }

        }
    }

    public void printVersion() {
        final String date = AboutNakedObjects.getFrameworkCompileDate();
        final String compileDate = date == null ? "" : ", compiled on " + date;
        print(AboutNakedObjects.getFrameworkName() + ", " + AboutNakedObjects.getFrameworkVersion() + compileDate);
    }

    public void printErrorMessage(String message) {
        output.println("Error: " + message);
    }

}
