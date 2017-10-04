package com.example.borisgrunwald.memorableplaces;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Handler;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LocationManager locationManager;
    LocationListener locationListener;
    Intent intent;

    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setUpLocationListener();


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        intent = getIntent();

        if (intent.hasExtra("Lng") && intent.hasExtra("Lat")) {
            moveCameraToGivenLocation(Double.parseDouble(intent.getStringExtra("Lng")), Double.parseDouble(intent.getStringExtra("Lat")));
        } else {
            moveCameraToLastLocation();

        }

        intent = new Intent(getApplicationContext(), MainActivity.class);


        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng clickedLocation) {

                Toast.makeText(MapsActivity.this, "Location Saved!", Toast.LENGTH_SHORT).show();

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


                List<Address> listAddress;

                try {
                    listAddress = geocoder.getFromLocation(clickedLocation.latitude, clickedLocation.longitude, 1);
                    mMap.addMarker(new MarkerOptions().position(clickedLocation).title(listAddress.get(0).getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(clickedLocation, 5));

                    if (listAddress.size() > 0) {
                        intent.putExtra("Location", listAddress.get(0).getAddressLine(0));
                        intent.putExtra("Lng", String.valueOf(clickedLocation.longitude));
                        intent.putExtra("Lat", String.valueOf(clickedLocation.latitude));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void moveCameraToGivenLocation(double lng, double lat) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        List<Address> listAddress;
        try {
            LatLng latLng = new LatLng(lng, lat);
            listAddress = geocoder.getFromLocation(lat, lng, 1);
            mMap.addMarker(new MarkerOptions().position(latLng).title(listAddress.get(0).getAddressLine(0)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveCameraToLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng myCurrentLocation = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myCurrentLocation));
        }
    }

    private void setUpLocationListener() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               /* mMap.clear();

                Log.i("Location", location.toString());

                LatLng myCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(myCurrentLocation).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myCurrentLocation, 5));

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());*/
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }


        };

        /*public boolean onTouchEvent(MotionEvent e) {
            return gestureDetector.onTouchEvent(e);
        }*/
    }

    public void onBackPressed() {
        if (intent != null) {
            startActivity(intent);
            super.onBackPressed();
        }
    }
}
