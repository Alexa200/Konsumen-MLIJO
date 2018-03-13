package com.mlijo.aryaym.konsumen_mlijo.LokasiPenjual;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mlijo.aryaym.konsumen_mlijo.DBModel.PenjualModel;
import com.mlijo.aryaym.konsumen_mlijo.Penjual.DetailPenjualActivity;
import com.mlijo.aryaym.konsumen_mlijo.R;
import com.mlijo.aryaym.konsumen_mlijo.Utils.Constants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class LokasiPenjualActivity extends AppCompatActivity
        implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
        GeoQueryEventListener, GoogleMap.OnCameraChangeListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.set_mlijo)
    TextView setMlijo;
    @BindView(R.id.requestBar)
    ProgressBar requestBar;
    @BindView(R.id.myLocationButton)
    RelativeLayout myLocationButton;

    private static final String TAG = "LokasiPenjualActivity";
    double latitude, longtitude;
    private DatabaseReference mDatabase;
    GeoQuery geoQuery;
    GeoFire geoFire;
    double nearByDistanceRadius = 2;
    Location mCurrentLocation,filterLocation;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    GoogleApiClient mGoogleApiClient;
    CameraPosition cameraPosition;
    GoogleMap mGoogleMap;
    LatLng latLng;
    private Circle searchCircle;
    private Map<String, Marker> markers;
    private HashMap<Marker, PenjualModel> markerPenjual;
    private boolean mLocationPermissionGranted;
    private LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lokasi_penjual);
        ButterKnife.bind(this);
        setTitle("Peta Penjual");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Map Initialization
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //Google API Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

//        this.searchCircle = this.mGoogleMap.addCircle(new CircleOptions().center(mDefaultLocation).radius(1000));
//        this.searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
//        this.searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));
        // setup markers
        this.markers = new HashMap<>();
        this.markerPenjual = new HashMap<>();

        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                @SuppressLint("MissingPermission") Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (mLastLocation != null){
                    latitude = mLastLocation.getLatitude();
                    longtitude = mLastLocation.getLongitude();
                    latLng = new LatLng(latitude, longtitude);
                    cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
                    mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
                //getDeviceLocation();
            }
        });

        if ((ContextCompat.checkSelfPermission(LokasiPenjualActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(LokasiPenjualActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
        }else {
            mapFragment.getMapAsync(this);
        }

        //Geofire
        geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("geofire"));
        setGeofire(geoFire);
        try {
            geoQuery = getGeofire().queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), nearByDistanceRadius);
            geoQuery.addGeoQueryEventListener(LokasiPenjualActivity.this);
            //LogUtils.i("geo status: geo queery started in oncreate");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {

            geoFire = new GeoFire(FirebaseDatabase.getInstance().getReference().child("geofire"));
            setGeofire(geoFire);
            geoQuery = getGeofire().queryAtLocation(new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()), nearByDistanceRadius);
            if (this.geoQuery != null){
                this.geoQuery.setCenter(new GeoLocation(mLastLocation.getLatitude(),mLastLocation.getLongitude()));
                //dynamic radius
                this.geoQuery.setRadius(nearByDistanceRadius);
                this.geoQuery.addGeoQueryEventListener(LokasiPenjualActivity.this);
            }else {
                Toast.makeText(LokasiPenjualActivity.this, "geoquery null", Toast.LENGTH_SHORT).show();
            }


            latitude = mLastLocation.getLatitude();
            longtitude = mLastLocation.getLongitude();
            latLng = new LatLng(latitude, longtitude);
            cameraPosition = new CameraPosition.Builder().target(latLng).zoom(17).build();
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    public void onPause() {
        super.onPause();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
        mGoogleMap.setMyLocationEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(true);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //buildGoogleApiClient();
        mGoogleApiClient.connect();

        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng latLng1 = mGoogleMap.getCameraPosition().target;
                latitude = latLng1.latitude;
                longtitude = latLng1.longitude;
            }
        });

//        LatLng latLngCenter = new LatLng(INITIAL_LOCATION.latitude, INITIAL_LOCATION.longitude);
//        this.searchCircle = this.mGoogleMap.addCircle(new CircleOptions().center(mDefaultLocation).radius(1000));
//        this.searchCircle.setFillColor(Color.argb(66, 255, 0, 255));
//        this.searchCircle.setStrokeColor(Color.argb(66, 0, 0, 0));

        this.mGoogleMap.setOnMarkerClickListener(this);

    }

    public GeoFire getGeofire() {
        return geoFire;
    }

    public void setGeofire(GeoFire geofire) {
        this.geoFire = geofire;
    }

    @Override
    public void onKeyEntered(final String key, GeoLocation location) {
        if (location != null){
            setMlijo.setVisibility(View.GONE);

            //add marker
            final Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.latitude, location.longitude))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_maps)).flat(true))
                    ;
            marker.setFlat(true);
            marker.setAnchor(0.5f, 0.5f);
            this.markers.put(key, marker);
            marker.setTag(key);
        }

    }

    @Override
    public void onKeyExited(String key) {

        // Remove any old marker
        Marker marker = this.markers.get(key);
        if (marker != null) {
            // if (markers.size() == 0) {
            //Car not shown change it to NO CARS AVAILABLE
//                setMlijo.setText("NO CARS AVAILABLE");
//                setMlijo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

            //   }
            marker.remove();
            this.markers.remove(key);
            //  LogUtils.i("Check marker is empty" + markers.isEmpty());
        }
        setMlijo.setVisibility(View.VISIBLE);
        setMlijo.setText("Tidak Ada MLIJO disekitar lokasi Anda");
        setMlijo.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    @Override
    public void onKeyMoved(String key, GeoLocation location) {

        // Move the marker
        Marker marker = this.markers.get(key);
        if (marker != null){
            this.animateMarkerTo(marker, location.latitude, location.longitude);
        }
    }

    @Override
    public void onGeoQueryReady() {


    }

    @Override
    public void onGeoQueryError(DatabaseError error) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage("There was an unexpected error querying GeoFire: " + error.getMessage())
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    // Animation handler for old APIs without animation support
    private void animateMarkerTo(final Marker marker, final double lat, final double lng) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long DURATION_MS = 3000;
        final Interpolator interpolator = new AccelerateDecelerateInterpolator();
        final LatLng startPosition = marker.getPosition();
        handler.post(new Runnable() {
            @Override
            public void run() {
                float elapsed = SystemClock.uptimeMillis() - start;
                float t = elapsed/DURATION_MS;
                float v = interpolator.getInterpolation(t);

                double currentLat = (lat - startPosition.latitude) * v + startPosition.latitude;
                double currentLng = (lng - startPosition.longitude) * v + startPosition.longitude;
                marker.setPosition(new LatLng(currentLat, currentLng));

                // if animation is not finished yet, repeat
                if (t < 1) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        final Dialog dialog = new Dialog(this);
        Log.d(TAG, "data" + markerPenjual);
        dialog.setContentView(R.layout.custom_marker_penjual);
        final TextView namaPenjual = dialog.findViewById(R.id.txt_nama_penjual);
        final TextView hariOperasional = dialog.findViewById(R.id.txt_hari_operasional);
        final TextView jamOperasional = dialog.findViewById(R.id.txt_jam_operasional);
        final Button detail = dialog.findViewById(R.id.btn_detail_penjual);

        final String markerKey = (String) marker.getTag();
        mDatabase.child(Constants.PENJUAL).child(markerKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null){
                    final PenjualModel penjualModel = dataSnapshot.getValue(PenjualModel.class);
                    if (penjualModel != null){
                        namaPenjual.setText(penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                    }
                    try {
                        jamOperasional.setText(penjualModel.getInfoLokasi().get(Constants.JAM_MULAI).toString() + " - " + penjualModel.getInfoLokasi().get(Constants.JAM_SELESAI).toString());
                        hariOperasional.setText(penjualModel.getInfoLokasi().get(Constants.HARI_MULAI).toString() + " - " + penjualModel.getInfoLokasi().get(Constants.HARI_SELESAI).toString());
                    }catch (Exception e){

                    }
                    detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(LokasiPenjualActivity.this, DetailPenjualActivity.class);
                            intent.putExtra(Constants.ID_PENJUAL, penjualModel.getUid());
                            intent.putExtra(Constants.AVATAR, penjualModel.getDetailPenjual().get(Constants.AVATAR).toString());
                            intent.putExtra(Constants.NAMA, penjualModel.getDetailPenjual().get(Constants.NAMA).toString());
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dialog.show();
        return false;
    }

    private double zoomLevelToRadius(double zoomLevel) {
        // Approximation to fit circle into view
        return 16384000/Math.pow(2, zoomLevel);
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        // Update the search criteria for this geoQuery and the circle on the map
        LatLng center = cameraPosition.target;
        double radius = zoomLevelToRadius(cameraPosition.zoom);
        this.searchCircle.setCenter(center);
        this.searchCircle.setRadius(radius);
        this.geoQuery.setCenter(new GeoLocation(center.latitude, center.longitude));
        // radius in km
        this.geoQuery.setRadius(radius/1000);
    }
}