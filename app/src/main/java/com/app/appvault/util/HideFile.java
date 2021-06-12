package com.app.appvault.util;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;

import java.io.File;
import java.net.URLConnection;
import java.util.Iterator;

public class HideFile {
    public static File getNewNameToHide(File file) {
        String name = file.getName();
        String title = name.split("\\.")[0];
        String type = name.split("\\.")[1];
        return new File(file.getParentFile(), "." + title + "$" + type + ".appvaultnomedia");
    }

    public static File getNewNameToTrash(File file) {
        String name = file.getName();
        String title = name.replace("appvaultnomedia", "appvaultnomediatrash");
        return new File(file.getParentFile(), title);
    }

    public static Iterator<File> getAllHiddenFiles() {
        return FileUtils.iterateFiles(
                Environment.getExternalStorageDirectory(),
                FileFilterUtils.suffixFileFilter("appvaultnomedia"),
                TrueFileFilter.INSTANCE);
    }

    public static Iterator<File> getAllFaudFiles() {
        return FileUtils.iterateFiles(
                Environment.getExternalStorageDirectory(),
                FileFilterUtils.suffixFileFilter("appvaultnomediafaud"),
                TrueFileFilter.INSTANCE);
    }

    public static Iterator<File> getAllTrash() {
        return FileUtils.iterateFiles(
                Environment.getExternalStorageDirectory(),
                FileFilterUtils.suffixFileFilter("appvaultnomediatrash"),
                TrueFileFilter.INSTANCE);
    }

    public static File restoreHideFile(File file) {
        String name = file.getName();

        String title = (name.split("\\.")[1]).split("\\$")[0];

        String type = (name.split("\\.")[1]).split("\\$")[1];
        return new File(file.getParentFile(), title + "." + type);
    }

    public static File restoreDelete(File file) {
        String name = file.getName();
        String title = name.replace("appvaultnomediatrash", "appvaultnomedia");
        return new File(file.getParentFile(), title);
    }

    public static boolean isImage(File file) {
        String name = (file.getName()).split("\\.")[1].replace("$", ".");
        String mimeType = URLConnection.guessContentTypeFromName(name);
        if (mimeType != null) {
            if (mimeType.startsWith("image")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isVideo(File file) {
        String name = (file.getName()).split("\\.")[1].replace("$", ".");
        String mimeType = URLConnection.guessContentTypeFromName(name);
        if (mimeType != null) {
            if (mimeType.startsWith("video")) {
                return true;
            }
        }
        return false;
    }
}
