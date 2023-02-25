package org.nakedobjects.plugins.xml.objectstore.internal.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class CopyXmlObjectStore {
    public static void main(final String[] args) {
        final String workingDirectory = args[0];
        final String testDirectory = args[1];

        copyAllFiles(testDirectory, workingDirectory);
    }

    private static void copyAllFiles(final String testDirectory, final String workingDirectory) {
        final File from = new File(testDirectory);
        final File to = new File(workingDirectory);

        if (!to.exists()) {
            to.mkdirs();
        }
        if (to.isFile()) {
            throw new NakedObjectException("To directory is actually a file " + to.getAbsolutePath());
        }

        final String list[] = from.list();
        for (int i = 0; i < list.length; i++) {
            copyFile(new File(from, list[i]), new File(to, list[i]));
        }
    }

    private static void copyFile(final File from, final File to) {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(from));
            bos = new BufferedOutputStream(new FileOutputStream(to));

            final byte buffer[] = new byte[2048];

            int len = 0;
            while ((len = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
        } catch (final IOException e) {
            throw new NakedObjectException("Error copying file " + from.getAbsolutePath() + " to " + to.getAbsolutePath(),
                    e);
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (final IOException ignore) {}
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (final IOException ignore) {}
            }
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
