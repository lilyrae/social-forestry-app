package com.example.lily.socialforestryapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;

import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UploadPlantImageActivity extends AppCompatActivity {

    private String imagePath = "";

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
        RequestParams params = new RequestParams();

        EditText editAddress = (EditText) findViewById(R.id.editTextAddress);
        params.put("address", editAddress.getText().toString());

        EditText editLandmark = (EditText) findViewById(R.id.editTextLandmark);
        params.put("landmark", editLandmark.getText().toString());

        Location location = getLastKnownLocation();

        if(location != null) {
            params.put("geo_latitude", location.getLatitude());
            params.put("geo_longitude", location.getLongitude());
        }

        params.put("image_path", imagePath);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getString(R.string.api_root) + getString(R.string.api_image_details_path), params, new AsyncHttpResponseHandler() {
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

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }

        Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;

        if (locationGPS != null) {
            GPSLocationTime = locationGPS.getTime();
        }

        long NetLocationTime = 0;

        if (locationNet != null) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return locationGPS;
        }
        else {
            return locationNet;
        }
    }
}
