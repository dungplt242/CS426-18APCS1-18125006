package hcmus.selab.ntan.demotouristapp;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnPolylineClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.MapboxDirections;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.DirectionsWaypoint;
import com.mapbox.api.directions.v5.models.LegStep;
import com.mapbox.api.directions.v5.models.StepIntersection;
import com.mapbox.geojson.Point;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapboxDirections mapboxDirections;
    private Landmark mLandmark;
    private Marker mMarker;
    private TextToSpeech mText2Speech;
    private boolean mIsText2SpeechReady = false;
    private List<LatLng> directionLatLng;
    private Polyline mPolyline;
    OnPolylineClickListener onPolylineClickListener = new OnPolylineClickListener() {
        @Override
        public void onPolylineClick(Polyline polyline) {
            mPolyline.remove();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        loadData();
        initComponents();
    }

    private void initComponents() {
        mText2Speech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                mIsText2SpeechReady = true;
            }
        });
    }

    private void loadData() {
        Intent intent = getIntent();
        mLandmark = new Landmark(
                intent.getStringExtra("name"),
                intent.getStringExtra("description"),
                intent.getIntExtra("logoid", 0),
                new LatLng(intent.getDoubleExtra("lat", 0),
                        intent.getDoubleExtra("long", 0))
        );
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        checkAndRequestPermission();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (mIsText2SpeechReady) {
                    mText2Speech.speak(mLandmark.getDescription(),
                            TextToSpeech.QUEUE_FLUSH, null);
                    Toast.makeText(getApplicationContext(),
                            mLandmark.getDescription(),
                            Toast.LENGTH_SHORT
                    ).show();
                }
                return false;
            }
        });
        displayMarkers();
    }



    private void checkAndRequestPermission() {
        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 225);
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
        }
    }

    private void displayMarkers() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), mLandmark.getLogoID());
        bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 4, bmp.getHeight() / 4, false);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(bmp);
        mMarker = mMap.addMarker(new MarkerOptions()
                .position(mLandmark.getLatlong())
                .title(mLandmark.getName())
                .snippet(mLandmark.getDescription())
                .icon(bitmapDescriptor)
        );

        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mLandmark.getLatlong())     // Sets the center of the map to Mountain View
                .zoom(15)                           // Sets the zoom
                .bearing(90)                        // Sets the orientation of the camera to east
                .tilt(30)                           // Sets the tilt of the camera to 30 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void btn_direct_onclick(View view) {
        String msg = "" +
                "Cài đặt chức năng khi nhấn vào button Direct, " +
                "vẽ một polygon để chỉ đường từ vị trí hiện tại tới vị trí mLandmark\n" +
                "Gợi ý:\n" +
                "* dùng AsyncTask để call API chỉ đường của google (đăng ký API key nếu cần)\n" +
                "* path = mMap.addPolygon()\n" +
                "* khi cần xóa polygon: path.remove()";

        Toast.makeText(this, msg,
                Toast.LENGTH_LONG).show();

        directionLatLng = new ArrayList<>();
        buildMapboxRequest();
        handleMapboxResponse();
        mMap.setOnPolylineClickListener(onPolylineClickListener);
    }

    private void drawDirection() {
        PolylineOptions polylineOptions = new PolylineOptions()
                .clickable(true)
                .color(0xff00ff00);
        for (int i=0; i<directionLatLng.size() - 1; i++)
            polylineOptions.add(directionLatLng.get(i));
        mPolyline = mMap.addPolyline(polylineOptions);
    }


    private void handleMapboxResponse() {
        mapboxDirections.enqueueCall(new Callback<DirectionsResponse>() {
            @Override public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Log.e("1","No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.e("2","No routes found");
                    return;
                }
                retrieveDirections(response);
                drawDirection();
            }

            private void retrieveDirections(Response<DirectionsResponse> response) {
                DirectionsRoute directionsRoute = response.body().routes().get(0);
                List<LegStep> legSteps = directionsRoute.legs().get(0).steps();
                for (LegStep legStep : legSteps) {
                    for (StepIntersection stepIntersection : legStep.intersections()) {
                        LatLng step = new LatLng(stepIntersection.location().latitude(), stepIntersection.location().longitude());
                        directionLatLng.add(step);
                    }
                }
            }

            @Override public void onFailure(Call<DirectionsResponse> call, Throwable throwable) {
                Log.e("3", "Error: " + throwable.getMessage());
            }
        });
    }

    private void buildMapboxRequest() {
        LatLng llCurrentLocation = getCurrentLocation(),
               llLandmark = mLandmark.getLatlong();
        Point origin = Point.fromLngLat(llCurrentLocation.longitude, llCurrentLocation.latitude);
        Point destination = Point.fromLngLat(llLandmark.longitude, llLandmark.latitude);
        mapboxDirections = MapboxDirections.builder()
                .origin(origin)
                .destination(destination)
                .steps(true)
                .overview(DirectionsCriteria.OVERVIEW_FULL)
                .profile(DirectionsCriteria.PROFILE_DRIVING)
                .accessToken(getResources().getString(R.string.mapbox_access_token))
                .build();
    }

    private LatLng getCurrentLocation() {
        LatLng myLatLng = null;
        LocationManager locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        checkAndRequestPermission();
        Location loc = null;
                //locationManager.getLastKnownLocation(
        //        LocationManager.GPS_PROVIDER);
        if (loc == null) {
            // fall back to network if GPS is not available
            loc = locationManager.getLastKnownLocation(
                    LocationManager.NETWORK_PROVIDER);
        }
        if (loc != null) {
            double myLat = loc.getLatitude();
            double myLng = loc.getLongitude();
            myLatLng = new LatLng(myLat, myLng);
        }
        return myLatLng;
    }
}
