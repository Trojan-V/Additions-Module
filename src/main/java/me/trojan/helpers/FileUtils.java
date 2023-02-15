package me.trojan.helpers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class FileUtils {
    private FileUtils() {}

    /* Returns false if the method failed to execute correctly and entered the catch-block, otherwise returns true. */
    public static boolean writeDataToFile(File file, String dataToWrite) {
        // Check user permissions before trying to write to the file.
        if(!userHasPermissions(file)) {
            return false;
        }
        // Write data to the previously created file.
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(dataToWrite);
            bw.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /* Returns true if the user can read the file and write to it, otherwise returns false. */
    private static boolean userHasPermissions(File file) {
        return file.canWrite() && file.canRead();
    }

    /* Creates a new directory if it doesn't exist yet in the supplied path. */
    public static File createDirectory(String pathname) {
        File directory = new File(pathname);
        if(directory.exists())
            return directory;
        directory.mkdir();
        return directory;
    }

    /*
     Creates a file if it doesn't exist yet in the supplied path.
     Use the following syntax here, otherwise the you might mess up the file creation due to an invalid usage of this method.
     --------------------------------------------------------------------------------
     The String 'pathname' should be the entire directory, but not the file itself.
     Example:  'C:/Users/Desktop/SomeRandomFolder'
     The String 'filename' should solely be the filename, nothing else.
     Example: 'TestFile.txt'
     --------------------------------------------------------------------------------
     The delimiters within the pathname have to be passed in as either '/' or better, by using 'File.separator'.
    */
    public static File createFile(String pathname, String filename) {
        /* Create the file within the directory. */
        File file = new File(pathname + File.separator + filename);
        if(file.exists())
            return file;
        try {
            if(!file.createNewFile()) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /* Returns the current timestamp in a specific format, stored in dateFormat. */
    public static String getTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return dateFormat.format(timestamp);
    }
}

