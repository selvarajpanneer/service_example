package com.example.serviceexample;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MainActivity extends Activity {
    private static final String TAG = "MAINACTIVITY";
    private TextView textView;
    private ImageView imageView;
    Context context;
    ProgressBar progressBar;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: success");
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String filePath = bundle.getString(DownloadService.FILEPATH);
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(MainActivity.this,
                            "Download complete. Download URI: " + filePath,
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download done");
                    Bitmap image = BitmapFactory.decodeFile(filePath);
                    BitmapDrawable bitmapDrawable = new BitmapDrawable(image);
                    imageView.setImageDrawable(bitmapDrawable);
//                    Bitmap image = BitmapFactory.decodeFile(filePath);
//                    imageView.setImageBitmap(image);
                    if(imageView!=null){
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Download failed",
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.status);
        imageView=(ImageView)findViewById(R.id.image_1);
        progressBar=findViewById(R.id.progressBar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(
                DownloadService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onClick(View view) {
        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "onClick: service started");
        Intent intent = new Intent(this, DownloadService.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(DownloadService.FILENAME, "activity_lifecycle.png");
        intent.putExtra(DownloadService.URL, "https://developer.android.com/images/activity_lifecycle.png");
        startService(intent);
        textView.setText("Service started");
    }
}