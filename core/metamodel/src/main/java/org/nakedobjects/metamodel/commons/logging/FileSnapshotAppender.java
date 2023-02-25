package org.nakedobjects.metamodel.commons.logging;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.TriggeringEventEvaluator;


public class FileSnapshotAppender extends SnapshotAppender {
    private static final Logger LOG = Logger.getLogger(FileSnapshotAppender.class);
    private String directoryPath;
    private String extension;
    private String fileName = "log-snapshot-";

    public FileSnapshotAppender(final TriggeringEventEvaluator evaluator) {
        super(evaluator);
    }

    public FileSnapshotAppender() {
        super();
    }

    public String getDirectory() {
        return directoryPath;
    }

    public String getExtension() {
        return extension;
    }

    public String getFileName() {
        return fileName;
    }

    public void setDirectory(final String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void setExtension(final String extension) {
        this.extension = extension;
    }

    public void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    @Override
    protected synchronized void writeSnapshot(final String message, final String details) {
        SnapshotWriter s;
        try {
            final String contentType = layout.getContentType();
            final String fileExtension = extension == null || extension.length() == 0 ? contentType.substring(contentType
                    .indexOf('/') + 1) : extension;
            s = new SnapshotWriter(directoryPath, fileName, fileExtension, message);
            s.appendLog(details);
            s.close();
        } catch (final FileNotFoundException e) {
            LOG.error("failed to open log file", e);
        } catch (final IOException e) {
            LOG.error("failed to write log file", e);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
