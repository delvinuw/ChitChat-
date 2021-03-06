package edu.uw.chitchat;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import edu.uw.chitchat.utils.PrefHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    /** a field to refer to the Google map objedt.*/
    private GoogleMap mMap;

    /** a field to refer to the location object.*/
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mCurrentLocation = (Location)getIntent().getParcelableExtra("LOCATION");

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


        // Add a marker in current device location and move the camera
        LatLng current = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(current).title("Current Location"));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15.0f));
        mMap.setOnMapClickListener(this);
    }

    /**
     * this will end the map activity and pass the lat lng to the weather fragment.
     * @param latLng the lat lng where user click
     */
    @Override
    public void onMapClick(LatLng latLng) {
        Log.wtf("LAT/LONG", latLng.toString());
        Marker marker = mMap.addMarker(new MarkerOptions()
        .position(latLng)
        .title("New Marker"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18.0f));
        PrefHelper.putStringPreference("latitude", latLng.latitude + "", this);
        PrefHelper.putStringPreference("longtitude", latLng.longitude + "", this);
        PrefHelper.putStringPreference("FromWhere", "map", this);
        finish();
    }
}
