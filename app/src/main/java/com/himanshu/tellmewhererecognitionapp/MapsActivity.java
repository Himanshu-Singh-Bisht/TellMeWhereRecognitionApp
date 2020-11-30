package com.himanshu.tellmewhererecognitionapp;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.himanshu.tellmewhererecognitionapp.Model.CountryDataSource;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String recievedCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // before making the fragment of map ready we need to handle the data which we are getting from main activity
        Intent mainActivityIntent = this.getIntent();
        recievedCountry = mainActivityIntent.getStringExtra(CountryDataSource.COUNRTY_KEY);

        if(recievedCountry == null)
        {
            recievedCountry = CountryDataSource.DEFAULT_COUNTRY_NAME;
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

    // this method got executed when the map is ready.
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//
//        // to move camera to that location
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney , 10.0f);
//        mMap.moveCamera(cameraUpdate);
//
//        // to add marker to that location
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(sydney);
//        markerOptions.title("Welcome To Sydney");
//        markerOptions.snippet("Fantastic Place");
//        mMap.addMarker(markerOptions);
//
//        // now creating a circle around the location present
//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.center(sydney);
//        circleOptions.radius(300);
//        circleOptions.strokeWidth(20.0f);
//        circleOptions.strokeColor(Color.YELLOW);
//        mMap.addCircle(circleOptions);


        double countryLatitude = Double.parseDouble(CountryDataSource.DEFAULT_COUNTRY_LATITUDE);
        double countryLongitude = Double.parseDouble(CountryDataSource.DEFAULT_COUNTRY_LONGITUDE);

        CountryDataSource countryDataSource = MainActivity.countryDataSource;       // to get the countryDataSource of mainActivity

        String countryMessage = countryDataSource.getTheInfoOfTheCountry(recievedCountry);

        // Now GeoCoding which is used to get the latitude and longitude of a place by their name
        Geocoder geocoder = new Geocoder(MapsActivity.this);

        try {
            String countryAddress = recievedCountry;

            List<Address> countryAddresses = geocoder.getFromLocationName(countryAddress , 10);
            if(countryAddresses != null)
            {
                countryLatitude = countryAddresses.get(0).getLatitude();  // as 0th index will have most precise alue for the address
                countryLongitude = countryAddresses.get(0).getLongitude();
            }
            else
            {
                recievedCountry = CountryDataSource.DEFAULT_COUNTRY_NAME;
            }
        }
        catch (IOException ioe)
        {
            recievedCountry = CountryDataSource.DEFAULT_COUNTRY_NAME;
        }



        // now making the country we found on to map
        LatLng myCountryLocation = new LatLng(countryLatitude , countryLongitude);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myCountryLocation , 17.2f);

        mMap.moveCamera(cameraUpdate);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myCountryLocation);
        markerOptions.title(countryMessage);
        markerOptions.snippet(CountryDataSource.DEFAULT_MESSAGE);
        mMap.addMarker(markerOptions);


        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(myCountryLocation);
        circleOptions.radius(400);
        circleOptions.strokeWidth(14.5f);
        circleOptions.strokeColor(Color.BLUE);
        mMap.addCircle(circleOptions);
    }
}