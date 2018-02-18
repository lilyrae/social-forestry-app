package com.example.lily.socialforestryapp;

import android.graphics.BitmapFactory;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.entity.mime.Header;
import cz.msebera.android.httpclient.entity.mime.HttpMultipartMode;
import cz.msebera.android.httpclient.entity.mime.content.FileBody;

public class UploadPlantImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String path = getIntent().getExtras().getString("path");
        uploadImage(path);
        setContentView(R.layout.activity_upload_plant_image);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewSquare);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    private void uploadImage(String path) {

        File file = new File(path);
        RequestParams params = new RequestParams();
        try {
            params.put("image", file);
        } catch(FileNotFoundException e) {

        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://192.168.100.148:8000/api/plant/diseased/image", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                Toast.makeText(getApplicationContext(), "Successfully uploaded image", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
