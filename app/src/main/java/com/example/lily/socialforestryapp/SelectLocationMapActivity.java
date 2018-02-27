package com.example.lily.socialforestryapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class SelectLocationMapActivity extends AppCompatActivity
        implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_select_location_map);
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void enterPlantLocation(View view)
    {
        Intent intent = new Intent(this, UploadPlantImageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putDouble("longitude", location.longitude);
        bundle.putDouble("latitude", location.latitude);
        intent.putExtras(bundle);

        if(getParent() == null) {
            setResult(Activity.RESULT_OK, intent);
        } else {
            getParent().setResult(Activity.RESULT_OK, intent);
        }
        finish();
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        location = getLastKnownLongitudeLatitude();
        // TODO: Modify Title
        // Consider adding more details to marker time
        // e.g. time last updated OR failed GPS permissions in such a case
        googleMap.addMarker(new MarkerOptions().position(location)
                .draggable(true)
                .title("Last Known Location"));

        googleMap.setOnMarkerDragListener(this);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }

    /**
     * Map marker listener
     */
    @Override
    public void onMarkerDrag(Marker marker) {
    }

    /**
     * Map marker listener
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        location = marker.getPosition();
    }

    /**
     * Map marker listener
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    private LatLng getLastKnownLongitudeLatitude() {
        Location lastLocation = getLastLocation();

        if(lastLocation != null) {
            return new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }

        // default: latlng of Patna Rural, Bihar, India
        return new LatLng(25.5940947, 85.1375645);
    }

    private Location getLastLocation()
    {
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
