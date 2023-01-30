//
// This is an activity class 
// In this class we implemented Google map
// Google map is implement and show on current device location
// Also in this class we implement capture map functionality
//

package com.google.maps.android;

import android.Manifest;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.checker.sa.android.data.FilterData;
import com.checker.sa.android.data.orderListItem;
import com.checker.sa.android.dialog.JobFilterDialog;
import com.google.LocationGPS;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
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
import com.google.android.gms.vision.barcode.Barcode;
import com.mor.sa.android.activities.JobDetailActivity;
import com.mor.sa.android.activities.JobListActivity;
import com.mor.sa.android.activities.R;
import com.mor.sa.android.activities.comunicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.widget.Toast.makeText;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
    //
    private RelativeLayout layout_map;

    private static final int JOB_DETAIL_ACTIVITY_CODE = 321;
    private GoogleMap googleMap;
    private HashMap<Marker, orderListItem> markersHash;
    private String orderid;
    private ImageView imgFilter;


    // Google Map
    private LatLng latlon_marker_1 =null;
    private LatLng latlon_marker_2=null;

    SupportMapFragment mapfragment;
    private static final int DEFAULT_ZOOM = 18;
    private float currentZoom = 15;
    private FrameLayout framView;
    private Location currentLot;
    int resultCode = -1;
    String location_description = "";
    private android.content.res.Resources res;

    private void updateFiler(String object) {
        final View v = findViewById(R.id.layout_filter);

        TextView tx = (TextView) findViewById(R.id.txtfilter);
        tx.setText(object);
        ImageView btnCross = (ImageView) findViewById(R.id.crossbtn);
        btnCross.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                v.setVisibility(RelativeLayout.GONE);
                String nulll = getString(R.string.job_filter_default_dd_option);
                String dateNull = "1/1/1900";
                FilterJobList(new FilterData(nulll, nulll,nulll,nulll,nulll,nulll, dateNull, dateNull));
                v.setVisibility(RelativeLayout.GONE);
            }
        });
        if (object == null) {
            v.setVisibility(RelativeLayout.GONE);
        } else {
            tx.setText(object);
            v.setVisibility(RelativeLayout.VISIBLE);
        }

    }

    public void FilterJobList(FilterData fd) {

        String filter = ((JobListActivity) comunicator.JobList).FilterJobList(fd);
        updateFiler(filter);
        setMarkers(JobListActivity.joborders, 0);

    }


    public void setMarkers(List<orderListItem> orders, int index) {
        if (orders == null)
            return;
        setMapView(orders, index);

    }

    private void setMapView(List<orderListItem> orders, int index) {
        // Clears the previously touched position
        googleMap.clear();
        for (int i = 0; i < orders.size(); i++) {
            try {
                MarkerOptions markerOptions = new MarkerOptions();

                double latitude = Double.valueOf(orders.get(i).orderItem
                        .getBranchLat());
                double longitude = Double.valueOf(orders.get(i).orderItem
                        .getBranchLong());
                LatLng latlng = new LatLng(latitude, longitude);
                // Setting the position for the marker
                markerOptions.position(latlng);
                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latlng.latitude + " : " + latlng.longitude);

                if (markersHash == null)
                    markersHash = new HashMap<Marker, orderListItem>();
                if ((orderid == null || orderid.equals("-1"))
                        || orders.get(i).orderItem.getOrderID().equals(orderid)) {
                    // Animating to the touched position
                    googleMap.animateCamera(CameraUpdateFactory
                            .newLatLng(latlng));

                    // Placing a marker on the touched position
                    Marker thismarker = googleMap.addMarker(markerOptions);
                    markersHash.put(thismarker, orders.get(i));

                    // Setting a custom info window adapter for the google map
                    googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoContents(Marker arg0) {
                            // Getting view from the layout file
                            // info_window_layout
                            View v = getLayoutInflater().inflate(
                                    R.layout.custom_marker, null);

                            TextView tvBranch = (TextView) v
                                    .findViewById(R.id.txtbranch);

                            ImageView imgNext = (ImageView) v
                                    .findViewById(R.id.btnopen);

                            final orderListItem thiItem = markersHash.get(arg0);

                            if (thiItem != null) {
                                tvBranch.setText(thiItem.orderItem
                                        .getBranchFullname());
                            }

                            return v;
                        }

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            // TODO Auto-generated method stub
                            return null;
                        }

                    });

                    googleMap
                            .setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {

                                    orderListItem thiItem = markersHash
                                            .get(marker);

                                    Intent intent = new Intent(
                                            MapActivity.this,
                                            JobDetailActivity.class);

                                    intent.putExtra("OrderID",
                                            thiItem.orderItem.getOrderID());
                                    startActivityForResult(intent,
                                            JOB_DETAIL_ACTIVITY_CODE);
                                }
                            });
                }
            } catch (Exception ex) {

            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        // No call for super(). Bug on API Level > 11.
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        imgFilter = (ImageView) findViewById(R.id.filterBtn);
        imgFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (comunicator.JobList != null) {
                    JobFilterDialog dialog = new JobFilterDialog(
                            MapActivity.this);
                    dialog.show();
                }
            }
        });

        //this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);

        framView = (FrameLayout) findViewById(R.id.framView);
        layout_map = (RelativeLayout) findViewById(R.id.layout_map);

        // By using this fragment map to load map into fragment and show in
        // screen
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        mapfragment = new

                SupportMapFragment();
        transaction.add(R.id.mapView, mapfragment);
        transaction.commit();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        loadMap();
        // This listener used for to close Google map and went to previous Field
        // Map screen
        onStartDevicePermissions();
    }

    public void onStartDevicePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int hasrecordaudio = this.checkSelfPermission(android.Manifest.permission.RECORD_AUDIO);
            int hasaccessc = this.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION);
           // int hasaccfinelocation = this.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);
            //int hasaccesslocationextracommand = this.checkSelfPermission(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
            int hasaccessnetworkstate = this.checkSelfPermission(android.Manifest.permission.ACCESS_NETWORK_STATE);
            int hasintermission = this.checkSelfPermission(android.Manifest.permission.INTERNET);
            int hascamera = this.checkSelfPermission(android.Manifest.permission.CAMERA);
           // int haswriteexternalstorage = this.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int hasreadexternalstorage = this.checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
            int haswakelock = this.checkSelfPermission(android.Manifest.permission.WAKE_LOCK);


            List<String> permissions = new ArrayList<String>();

            if (hasrecordaudio != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.RECORD_AUDIO);

            }

            if (hasaccessc != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);

            }
//            if (hasaccfinelocation != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
//
//            }
//            if (hasaccesslocationextracommand != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS);
//
//            }
            if (hasaccessnetworkstate != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.ACCESS_NETWORK_STATE);

            }

            if (hasintermission != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.INTERNET);

            }
            if (hascamera != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.CAMERA);

            }

//            if (haswriteexternalstorage != PackageManager.PERMISSION_GRANTED) {
//                permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//            }
            ////hasreadexternalstorage
            if (hasreadexternalstorage != PackageManager.PERMISSION_GRANTED) {
                permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);

            }
            if (haswakelock != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WAKE_LOCK);

            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), 111);
            }
        }


    }
    private void loadMap() {

        final LocationGPS gps = new LocationGPS(this);
        currentLot = gps.getLocation();

        // Here check if Google play service are available in device
        resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Builder builder = new Builder(MapActivity.this);
            if (resultCode == ConnectionResult.SERVICE_MISSING)
                builder.setMessage("Error loaing map");
            if (resultCode == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED)
                builder.setMessage("Error loaing map");
            if (resultCode == ConnectionResult.SERVICE_DISABLED)
                builder.setMessage("Error loaing map");
            if (resultCode == ConnectionResult.SERVICE_INVALID)
                builder.setMessage("Error loaing map");

            builder.setCancelable(true);
            builder.setPositiveButton("OK", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {

            // This handler is using for to to load map after 100 mili sec delay
            // If we load map with handler it does not load and in googlemap
            // variable we receive null
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentLot = gps.getLocation();

                        mapfragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(final GoogleMap googleMap) {
MapActivity.this.googleMap=googleMap;
                                if (currentLot != null)
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
                                            currentLot.getLatitude(),
                                            currentLot.getLongitude()), DEFAULT_ZOOM));
                                orderid = getIntent().getExtras().getString("orderid");

                                setMarkers(JobListActivity.joborders, 0);
                            }
                        });


                        if (currentLot != null) {
                            double lat = Math.abs(currentLot.getLatitude());
                            double lng = Math.abs(currentLot.getLongitude());

                            if (lat > 0 && lng > 0 && googleMap != null) {
                                googleMap.animateCamera(CameraUpdateFactory
                                        .newLatLngZoom(
                                                new LatLng(
                                                        currentLot.getLatitude(),
                                                        currentLot.getLongitude()),
                                                17.0f));
                                // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new
                                // LatLng(currentLot.getLatitude(),
                                // currentLot.getLongitude()), 14.0f));
                                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                    @Override
                                    public void onMapClick(LatLng latLng) {

                                        googleMap.addMarker(new MarkerOptions()
                                                .position(latLng)
                                                .title("Marker"));
                                    }
                                });
                            } else {
                                makeText(getApplicationContext(),
                                        "Searching current location.",
                                        Toast.LENGTH_SHORT);
                            }

                        } else {
                            makeText(getBaseContext(),
                                    "Location not found. Please try again. ",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // This listener for Google map cameralister when dragging
                        // on map or zooming and changing locaiton view then this
                        // listener called

                    }
                }, 100);
            } catch (Exception ee) {

            }
        }
    }

//    private void showGPSDisabledAlertToUser() {
//        Builder alertDialog = new Builder(this);
//        alertDialog.setTitle("gps not enabled");
//        alertDialog.setMessage("not enabled");
//        alertDialog.setPositiveButton(
//                 "Settings",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        startActivityForResult(
//                                new Intent(
//                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
//                                111);
//                    }
//                });
//        alertDialog.setNegativeButton(
//                "use without gps", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//        alertDialog.show();
//    }

    @Override
    protected void onActivityResult(int request, int result, Intent data) {

        super.onActivityResult(request, result, data);
        LocationGPS gps = new LocationGPS(MapActivity.this);

        if (request == 111) {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (gps.getLatitude() == null && gps.getLongitude() == null) {
                    loadMap();
                }
            } else {
                return;
            }
        } else if (request == JOB_DETAIL_ACTIVITY_CODE) {
            setResult(RESULT_OK, data);
            finish();
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        String location = "";
        if (!location_description.equals("")) {
            location = location_description;
        }


        if (location != null && !location.equals("")) {
            new GeocoderTask().execute(location);
        }
    }

    // This AsyncTask class used for to find location according to typed address
    // in adress field in screen
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>> {

        @Override
        protected List<Address> doInBackground(String... locationName) {

            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;

            try {

                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(final List<Address> addresses) {
            mapfragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap googleMap) {
                    if (addresses == null || addresses.size() == 0) {
                        makeText(getBaseContext(), "No Location found ",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (googleMap != null)
                        googleMap.clear();

                    // for (int i = 0; i < addresses.size(); i++) {

                    Address address = (Address) addresses.get(0);

                    if (!address.equals(null)) {

                        LatLng latLng = new LatLng(address.getLatitude(),
                                address.getLongitude());
                      //  Toast.makeText(MapActivity.this,"Latitude: "+address.getLatitude()+" Longitude: "+address.getLongitude(),Toast.LENGTH_SHORT).show();
                        String addressText = String.format(
                                "%s, %s",
                                address.getMaxAddressLineIndex() > 0 ? address
                                        .getAddressLine(0) : "", address
                                        .getCountryName());

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        markerOptions.title(addressText);
                        if (googleMap != null)
                            googleMap.addMarker(markerOptions);

                        // Locate the first location
                        // if (i == 0)
                        if (googleMap != null)
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    latLng, 12));
                        // }
                    }
                }
            });

        }
    }

    public static class PlaceAPI_AutoComplete {

        private final String TAG = PlaceAPI_AutoComplete.class.getSimpleName();

        private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
        private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
        private static final String OUT_JSON = "/json";

        private static final String API_KEY = "AIzaSyC6Qkj4ovgxlTbOVFtkGySpKelAeXFu6JM";

        public ArrayList<String> autocomplete(String input) {
            ArrayList<String> resultList = null;

            HttpURLConnection conn = null;
            StringBuilder jsonResults = new StringBuilder();

            try {

                StringBuilder sb = new StringBuilder(PLACES_API_BASE
                        + TYPE_AUTOCOMPLETE + OUT_JSON);
                sb.append("?key=" + API_KEY);
                sb.append("");
                sb.append("&input=" + URLEncoder.encode(input, "utf8"));

                URL url = new URL(sb.toString());
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(
                        conn.getInputStream());

                // Load the results into a StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    jsonResults.append(buff, 0, read);
                }
            } catch (MalformedURLException e) {
                Log.e(TAG, "Error processing Places API URL", e);
                return resultList;
            } catch (IOException e) {
                Log.e(TAG, "Error connecting to Places API", e);
                return resultList;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }

            try {
                // Log.d(TAG, jsonResults.toString());

                // Create a JSON object hierarchy from the results
                JSONObject jsonObj = new JSONObject(jsonResults.toString());
                JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

                // Extract the Place descriptions from the results
                resultList = new ArrayList<String>(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    resultList.add(predsJsonArray.getJSONObject(i).getString(
                            "description"));
                }
            } catch (JSONException e) {
                Log.e(TAG, "Cannot process JSON results", e);
            }

            return resultList;
        }
    }

    // This is function is used for to implement static google map api that
    // return us map image according to given latitude and longitude

}
