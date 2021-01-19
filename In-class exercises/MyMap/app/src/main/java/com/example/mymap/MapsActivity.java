package com.example.mymap;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        Marker hcmus =       putMarker(10.7931813,106.7082603, "HCMUS");
        hcmus.setTag("http://www.hcmus.edu.vn");


        putMarker(10.7931813,106.7082603, "HCMUS");

        putPolyline(new LatLng(10.6, 106.68), new LatLng(34,151), new LatLng(11.76, 100));

        LatLng sydney = new LatLng(-34, 151);

    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(10.76, 106.68)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(10.76, 106.68), 17));

    //    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(10.7931813,106.7082603)));

        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
    }

    private Polyline putPolyline(LatLng latLng, LatLng latLng1, LatLng latLng2) {
        PolylineOptions polylineOptions = new PolylineOptions()
                .clickable(true)
                .add(latLng)
                .add(latLng1)
                .add(latLng2)
                .color(0xff00ff00);
        Polyline polyline = mMap.addPolyline(polylineOptions);
        return polyline;
    }


    private Marker putMarker(double v1, double v2, String name) {
        MarkerOptions markerOptions = new MarkerOptions()
                .alpha(0.5f) //50% map behind marker
                .draggable(true)
                .position(new LatLng(v1, v2))
                .title(name);

        Marker marker = mMap.addMarker(markerOptions);

        return marker;
    }


    private int counter = 0;
    @Override
    public void onMapClick(LatLng latLng) {
        counter++;
        putMarker(latLng.latitude, latLng.longitude, String.valueOf(counter));

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Object obj=marker.getTag();
        if (obj != null) {
            String s = (String)obj;
            Uri uri = Uri.parse("HCMUS");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        else marker.remove();
        return false;
    }
}