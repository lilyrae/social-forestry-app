package com.example.lily.socialforestryapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.loopj.android.http.*;
import java.io.File;
import java.io.FileNotFoundException;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadPlantImageActivity extends AppCompatActivity {

    static final int GET_GPS_COORDINATES = 1;  // The request code

    private String imagePath;
    private double longitude;
    private double latitude;
    private boolean submitted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String path = getIntent().getExtras().getString("path");
        uploadImage(path);
        setContentView(R.layout.activity_upload_plant_image);
        ImageView imageView = (ImageView) findViewById(R.id.imageViewSquare);
        imageView.setImageBitmap(BitmapFactory.decodeFile(path));
    }

    public void submitImageDetails(View view) {
        if(longitude == 0 || latitude == 0) {
            Toast.makeText(getApplicationContext(), "Please select the plant's GPS location", Toast.LENGTH_SHORT).show();
        } else {
            submitted = true;
            sendImageDetails();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private void sendImageDetails()
    {
        if(imagePath == null || longitude == 0 || latitude == 0) {
            return;
        }

        RequestParams params = new RequestParams();

        EditText editAddress = (EditText) findViewById(R.id.editTextAddress);
        params.put("address", editAddress.getText().toString());

        EditText editLandmark = (EditText) findViewById(R.id.editTextLandmark);
        params.put("landmark", editLandmark.getText().toString());

        params.put("image_path", imagePath);
        params.put("geo_latitude", latitude);
        params.put("geo_longitude", longitude);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getString(R.string.api_root) + getString(R.string.api_image_details_path), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                Toast.makeText(getApplicationContext(), "Successfully sent plant location", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Failed to send plant location. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addGPSLocation(View view) {
        startActivityForResult(new Intent(this, SelectLocationMapActivity.class), GET_GPS_COORDINATES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GET_GPS_COORDINATES && resultCode == RESULT_OK) {
            longitude = data.getExtras().getDouble("longitude");
            latitude = data.getExtras().getDouble("latitude");

            Button submitButton = (Button) findViewById(R.id.buttonSubmit);
            submitButton.setVisibility(View.VISIBLE);
        }
    }

    private void uploadImage(String path) {

        File file = new File(path);
        RequestParams params = new RequestParams();
        try {
            params.put("image", file);
        } catch (FileNotFoundException e) {

        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getString(R.string.api_root) + getString(R.string.api_image_upload_path), params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] response) {
                Toast.makeText(getApplicationContext(), "Successfully uploaded image", Toast.LENGTH_SHORT).show();

                try {
                    JSONObject responseObj =new JSONObject(new String(response));
                    imagePath = responseObj.getJSONObject("data").getString("image_path");

                    if(submitted) {
                        sendImageDetails();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
