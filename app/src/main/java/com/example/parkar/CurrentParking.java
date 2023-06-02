package com.example.parkar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.parkar.model.global_parking_model;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentParking extends Fragment implements OnMapReadyCallback, LocationListener, GoogleMap.OnMarkerClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private Map<Marker, String> markerMap = new HashMap<>();
    public static final int ROUND = 10;
    private List<global_parking_model> parkingData;
    ExtendedFloatingActionButton currparking;
    Marker[] marker1 = new Marker[20]; //change length of array according to you

    FirebaseAuth mAuth;
    public GoogleApiClient googleApiClient;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mUsers;
    Marker marker;
    public FusedLocationProviderClient fusedLocationProviderClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    private static final int PERMISSION_REQUEST_CAMERA = 0;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;


        //new
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }else
            {


                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CAMERA);

                //  mMap.setMyLocationEnabled(true);
                //startActivity(new Intent(getApplicationContext(),find_garage_map.class));
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        //new ends
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.google_style));
            if (!success) {
                // Handle map style load failure
                Log.e("map_style","map style updated please do check it");
            }
        } catch (Resources.NotFoundException e) {
            // Oops, looks like the map style resource couldn't be found!
            Log.e("map_style","map is not updated yet ... do some other stuff");
        }
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener((GoogleMap.OnInfoWindowClickListener) this);
        googleMap.setMapType(R.raw.google_style);

        //setting the size of marker in map by using Bitmap Class
        int height = 100;
        int width = 100;
        BitmapDrawable bitmapdraw=(BitmapDrawable)getResources().getDrawable(R.drawable.car);
        Bitmap b=bitmapdraw.getBitmap();
        final Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        mUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()){
                    String gname = s.child("vehicle_no").getValue().toString();
                    Double glat =  Double.parseDouble(s.child("lat").getValue().toString());
                    Double glong =  Double.parseDouble(s.child("longi").getValue().toString());
                    String gid = s.child("vehicle_no").getValue().toString();
                    global_parking_model data = new global_parking_model(gname,glat,glong,"","","","",gid,mAuth.getCurrentUser().getUid());
                    parkingData.add(data);
                }
                Log.d("check data list", "onDataChange: " + parkingData.size());
                for(int i = 0 ;i<parkingData.size();i++) {
                    final int x=i;


                    Log.d("check this", "onDataChange: " + parkingData.get(i).getPark_name());


                    LatLng location = new LatLng(parkingData.get(i).getLatitude(), parkingData.get(i).getLongitude());
                    //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
                    mMap.addMarker(new MarkerOptions().snippet(parkingData.get(i).getId()).position(location).title(parkingData.get(i).getPark_name())).setIcon(BitmapDescriptorFactory.fromBitmap(smallMarker));
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            // Retrieve the data from the marker.

                            String str = marker.getId();
                            String[] part = str.split("(?<=\\D)(?=\\d)");
                            Log.d("marker value", "val: " + part[1]);

//                            Toast.makeText(getActivity().getApplicationContext(),parkingData.get(Integer.parseInt(part[1])).getId(),Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                    Uri.parse("https://www.google.com/maps/search/"
                                            + parkingData.get(Integer.parseInt(part[1])).getLatitude()                                                 //passing the coordinates as default parameters in maps Intent as source
                                            + ","
                                            + parkingData.get(Integer.parseInt(part[1])).getLongitude()                                                 //passing the coordinates as default parameters in maps Intent as source



                                    ));
                            startActivity(intent);

//                            Intent intent = new Intent(getActivity().getBaseContext(), ParkingDetails.class);
//                            intent.putExtra("EXTRA_SESSION_ID",parkingData.get(Integer.parseInt(part[1])).getId() );
//                            startActivity(intent);


                            return false;
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
    }
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        marker.getPosition();
        String abc = marker.getTitle();
        String b = marker.getId();
        String c = (String) marker.getTag();
        Toast.makeText(getActivity().getApplicationContext(),abc,Toast.LENGTH_SHORT).show();




        return false;
    }


    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
////        MarkerOptions markerOptions = new MarkerOptions();
////        markerOptions.position(latLng);
////        markerOptions.title("Current Position");
////        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
////        mCurrLocationMarker = mMap.addMarker(markerOptions);
//
//        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(), marker.getSnippet(),

                Toast.LENGTH_SHORT).show();

    }
    public void onRequestPermissionsResults(int requestCode, String[] permissions,
                                            int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CAMERA

                    :
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(getActivity().getApplicationContext(),ViewParking.class));
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {

                }
                return;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_parking, container, false);
        // Inflate the layout for this fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        ChildEventListener mChildEventListener;
        mAuth = FirebaseAuth.getInstance();
        mUsers= FirebaseDatabase.getInstance().getReference("user/"+mAuth.getCurrentUser().getUid()+"/currentParking");
        mUsers.push().setValue(marker);
        parkingData=new ArrayList<>();

        currparking = view.findViewById(R.id.addcurr_fab);

        currparking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), AddCurrentParking  .class);
                startActivity(i);
            }
        });
        return view;
    }
}