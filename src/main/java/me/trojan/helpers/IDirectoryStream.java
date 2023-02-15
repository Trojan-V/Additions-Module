package me.trojan.helpers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public interface IDirectoryStream {
    File getDirectory();
    boolean filterFiles();
    String filterFileNameRegex();

    default Iterator<File> getIteratorForDirectory() throws Exception {
        File directory = getDirectory();
        if(!directory.exists()) {
            throw new Exception("Directory doesn't exist.");
        }

        File[] files = directory.listFiles();
        if (files == null)
            throw new AssertionError();

        if(filterFiles()) {
            List<File> fileList = new ArrayList<>();
            for(File file : files) {
                if(file.getName().matches(filterFileNameRegex()))
                    fileList.add(file);
            }
            return fileList.stream().iterator();
        }
        return Arrays.stream(files).iterator();
    }
}
