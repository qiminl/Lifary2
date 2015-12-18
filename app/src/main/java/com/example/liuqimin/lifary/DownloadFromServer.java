package com.example.liuqimin.lifary;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class implements funtions that help communicate with server
 *
 * Created by liuqi on 2015-11-11.
 */
public class DownloadFromServer {

    //@todo change to local file
    private final String DIR = "C:\\Users\\liuqi\\Desktop\\";
    private final String albumName = "ho";

    /**
     * This method create a local file that is downloaded from server
     *
     * @param dwnload_file_path This is the url of the target file
     * @param file_name This is the local file name
     * @return String That indicate the absolute path of downloaded file;
     * @throws IOException
     */
    public String DownloadFromServer (String dwnload_file_path, String file_name) throws IOException {
        int downloadedSize = 0;
        int totalSize = 0;

        /*******  Using External Storage********/
        //todo make it compatible for android machine
        if(isExternalStorageWritable()) {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), albumName);
        }


        URL url = new URL(dwnload_file_path);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setDoOutput(true);
        urlConnection.connect();

        //set the path where we want to save the file
        //todo update on real test
        //File SDCardRoot = Environment.getExternalStorageDirectory();
        //create a new file, to save the downloaded file
        File file = new File(DIR,file_name);

        int duplicate = 0;

        /**
         * Check for duplicate
         */
        while (file.exists() || duplicate >100){
            StringBuilder builder = new StringBuilder(file_name);
            if(file_name.contains("(duplicate"))
                builder.replace(file_name.indexOf("(duplicate") + 10, file_name.indexOf(")."), Integer.toString(duplicate++));
            else
                builder.insert(file_name.indexOf("."), "(duplicate" + Integer.toString(duplicate++) + ")");
            file_name = builder.toString();
            file = new File(DIR,file_name);
        }

        FileOutputStream fileOutput = new FileOutputStream(file);

        //Stream used for reading the data from the internet
        InputStream inputStream = urlConnection.getInputStream();

        //this is the total size of the file which we are downloading
        totalSize = urlConnection.getContentLength();

        //create a buffer...
        byte[] buffer = new byte[1024];
        int bufferLength = 0;

        while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
            fileOutput.write(buffer, 0, bufferLength);
            downloadedSize += bufferLength;
            // update the progressbar //
        }
        //close the output stream when complete //
        fileOutput.close();
        //todo handle errors


        return file.getAbsolutePath();
    }

    /* Checks if external storage is available for read and write */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
