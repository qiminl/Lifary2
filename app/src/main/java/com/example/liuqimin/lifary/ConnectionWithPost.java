package com.example.liuqimin.lifary;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * This class handle HTTP POST request with data to be sent to server
 * Created by liuqi on 2015-11-11.
 */
public class ConnectionWithPost {
    int serverResponseCode = 0;
    private final String upLoadServerUri = "http://192.168.1.71:8080/wala/UploadToServer.php";

    /**********  File Path *************/
    //todo change file path
    //final String uploadFilePath = "/mnt/sdcard/";
    private final String uploadFilePath = "C:\\Users\\liuqi\\Desktop\\";
    private final String uploadFileName = "2.png";

    /**********  Responds *************/
    private HttpURLConnection conn;
    Message object = null;

    /**
     * This method is used to send HTTP POST request to target php server
     * with Diary data as url parameter.
     * It also returns the respond message from server.
     *
     * @param link This is a url of the target php server
     * @param values This is a HashMap<String, String> array contains data to be sent;
     *               mostly json objects
     * @return String This returns the responds from server
     */
    public String sendRequest(String link, HashMap<String, String> values) {

        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();
            if (values != null) {
                OutputStream os = conn.getOutputStream();
                //@todo find out about stream writer
                OutputStreamWriter osWriter = new OutputStreamWriter(os,
                        "UTF-8");
                BufferedWriter writer = new BufferedWriter(osWriter);
                writer.write(getPostData(values));

                writer.flush();
                writer.close();
                os.close();
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("connect http ok");
                InputStream is = conn.getInputStream();
                InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isReader);

                String result = "";
                String line = "";
                while ((line = reader.readLine()) != null) {
                    result += line;
                }

                if (result.trim().length() > 2) {
                    Gson gson = new Gson();
                    object = gson.fromJson(result, Message.class);
                }
            }

        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return object.toString();
    }

    /**
     * This method convert HashMap data to be sent into url parameter structure.
     *
     * @param values This is a HashMap<String, String> stores data using in POST request
     * @return String This is a String contains the parameter of url POST request
     */
    private String getPostData(HashMap<String, String> values) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            if (first)
                first = false;
            else
                builder.append("&");

            try {
                builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
            }

        }
        return builder.toString();
    }


    /**
     * This is a method that uploads file to target server location.
     *
     * @param sourceFileUri This is the absolute path of the file that need to be uploaded
     * @return int This is the server respond code
     */
    public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        /********** Test for file existence *********/
        if (!sourceFile.isFile()) {

            Log.e("uploadFile", "Source File not exist :"
                    + uploadFilePath + "" + uploadFileName);
            return 0;
        }
        else {
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=uploaded_file;filename="
                        + fileName + "" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of maximum size
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necessary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                            +" http://www.androidexample.com/media/uploads/"
                            +uploadFileName;
                    Log.i("uploadFile", msg);
                }
                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return serverResponseCode;
    }

    /**
     *Class Message is created to handle php response message
     *      contains structure:
     *              [success]: 1 & 0 indicate success or fail
     *              [message]: String of message
     */
    private class Message {
        private String message = "";
        private String success = "";
        Message(){}
        public String toString(){
            String result = "{\"success\":"+success+",\"message\":\""+message+"\"}";
            return result;
        }
    }

}