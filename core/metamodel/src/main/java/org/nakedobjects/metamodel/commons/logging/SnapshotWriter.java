package org.nakedobjects.metamodel.commons.logging;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;


public class SnapshotWriter {
    private static final Format FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
    private final PrintStream os;

    public SnapshotWriter(final String directoryPath, final String baseFileName, final String fileExtension, final String message)
            throws IOException {
        final File dir = new File(directoryPath == null || directoryPath.length() == 0 ? "." : directoryPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        final File indexFile = new File(dir, "index.txt");
        final Date date = new Date();
        final File logFile = new File(dir, baseFileName + FORMAT.format(date) + "." + fileExtension);

        final RandomAccessFile index = new RandomAccessFile(indexFile, "rw");
        index.seek(index.length());
        index.writeBytes(logFile.getName() + ": " + message + "\n");
        index.close();

        os = new PrintStream(new FileOutputStream(logFile));
    }

    public void appendLog(final String details) {
        os.println(details);
    }

    public void close() {
        if (os != null) {
            os.close();
        }
    }
}

// Copyright (c) Naked Objects Group Ltd.
