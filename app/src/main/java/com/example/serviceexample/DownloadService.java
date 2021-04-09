package com.example.serviceexample;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadService extends IntentService {

    private static final String TAG ="DOWNLOAD SERVICE" ;
    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    public static final String NOTIFICATION = "com.example.serviceexample";
    OutputStream output;
    File file;
    public DownloadService() {
        super("DownloadService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);
        try {
            URL url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            connection.connect();
            int lenghtOfFile = connection.getContentLength();//length of file
            InputStream input = new BufferedInputStream(url.openStream(), lenghtOfFile);//file from url
            File myDir=new File(getFilesDir(),"Test");//location of directory
            myDir.mkdirs();//create directory
            file=new File(myDir,fileName);
            if(file.exists()){
                file.delete();
            }
            output=new FileOutputStream(file);
            Log.d(TAG, "onHandleIntent: output file location"+output);
            byte data[] = new byte[1024];

            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }
            result = Activity.RESULT_OK;
        publishResults(file.getAbsolutePath(),result);
    }

    private void publishResults(String outputPath, int result) {
        Log.d(TAG, "publishResults: success");
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }
}
