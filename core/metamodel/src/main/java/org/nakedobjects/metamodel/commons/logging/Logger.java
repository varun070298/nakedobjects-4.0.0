package org.nakedobjects.metamodel.commons.logging;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class Logger {
    private String fileName;
    private DateFormat format;
    private boolean logAlso;
    private org.apache.log4j.Logger logger;
    private boolean showTime;
    private final long start;
    private PrintStream stream;

    public Logger() {
        start = time();
        logAlso = true;
        showTime = true;
        format = new SimpleDateFormat("yyyyMMdd-hhmm-ss.SSS");
    }

    public Logger(final String fileName, final boolean logAlso) {
        this();
        this.fileName = fileName;
        this.logAlso = logAlso;
    }

    public void close() {
        if (stream != null) {
            stream.close();
        }
    }

    protected abstract Class<?> getDecoratedClass();

    public boolean isLogToFile() {
        return fileName != null;
    }

    public boolean isLogToLog4j() {
        return logAlso;
    }

    public void log(final String message) {
        if (logAlso) {
            logger().info(message);
        }
        if (fileName != null) {
            if (stream == null) {
                try {
                    if (fileName == null) {
                        return;
                    }
                    stream = new PrintStream(new FileOutputStream(fileName));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
            if (showTime) {
                stream.print(format.format(new Date(time())));
            } else {
                stream.print(time() - start);
            }
            stream.print("  ");
            stream.println(message);
            stream.flush();
        }
    }

    public void log(final String request, final Object result) {
        log(request + "  -> " + result);
    }

    private org.apache.log4j.Logger logger() {
        if (logger == null) {
            logger = org.apache.log4j.Logger.getLogger(getDecoratedClass());
        }
        return logger;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    public void setLogAlso(final boolean logAlso) {
        this.logAlso = logAlso;
    }

    public void setShowTime(final boolean showTime) {
        this.showTime = showTime;
    }

    public void setTimeFormat(final String format) {
        this.format = new SimpleDateFormat(format);
    }

    private long time() {
        return System.currentTimeMillis();
    }
}
// Copyright (c) Naked Objects Group Ltd.
