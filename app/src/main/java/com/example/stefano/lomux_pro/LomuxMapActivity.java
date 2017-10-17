package com.example.stefano.lomux_pro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;

import com.example.stefano.lomux_pro.callbacks.PinsCallback;
import com.example.stefano.lomux_pro.listener.DrawnerItemClickListener;
import com.example.stefano.lomux_pro.listener.MapChangesListener;
import com.example.stefano.lomux_pro.model.Pin;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.client.cache.Resource;

import static android.R.attr.button;
import static android.R.attr.theme;

public class LomuxMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private final static LatLng london_center = new LatLng(51.509865, -0.118092);
    private ClusterManager<Pin> mClusterManager;
    private SearchView searchView;
    private Marker clickedMarker=null;
    private List<String> ids;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lomux_map);
        // create our manager instance after the content view is set

        //super.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new DrawnerItemClickListener(this));
        searchView = findViewById(R.id.searchbar);
        searchView = findViewById(R.id.searchbar);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("Search a Pin");
        searchView.clearFocus();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchView.isIconified()){
                    searchView.setIconified(false);
                    searchView.performClick();
                }
            }
        });

        ids = new ArrayList<>();
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

        float zoom=13.5f;
        // Add a marker in London and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(london_center));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));

        mMap.getUiSettings().setMapToolbarEnabled(false);
        mClusterManager = new ClusterManager<Pin>(this, mMap);
        mClusterManager.setAnimation(true);
        mClusterManager.setRenderer(new PinRenderer(this.getApplicationContext(), mMap, mClusterManager));
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLoadedCallback(this);

    }
    public List<String> getListIds(){
        return ids;
    }

    @Override
    public void onMapLoaded() {
        MapChangesListener mapChangesListener =new MapChangesListener(mMap,this);
        mMap.setOnCameraIdleListener(mapChangesListener);
        PinsCallback.getInstance().get_local_pins(mMap,mapChangesListener.getActualVisibleArea(),ids,this);
        Log.d("PIN","MAR "+mClusterManager.getMarkerCollection().getMarkers().size());
    }

    public void cluster(){
        mClusterManager.cluster();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void addPins(List<Pin> pins){
       // mClusterManager.clearItems();
      /*  if(mClusterManager.getMarkerCollection().getMarkers().isEmpty()){
            mClusterManager.clearItems();
            mClusterManager.addItems(pins);
        }
        /*TODO: Is it efficent??*//*
        else{
            for (Pin elem : pins) {
                if (!isInMarker(elem))
                    mClusterManager.addItem(elem);
            }
        }
*/
       // mClusterManager.clearItems();
        Log.d("PIN"," dim "+pins.size());
        for(Pin elem:pins){
            ids.add(elem.getIdPin());
            mClusterManager.addItem(elem);
        }
        mClusterManager.cluster();
       // mClusterManager.cluster();
      /* if(clickedMarker!=null) {
            for(Marker marker:mClusterManager.getMarkerCollection().getMarkers()){
                if(clickedMarker.getPosition().equals(marker.getPosition())){
                   mClusterManager.onMarkerClick(marker);
                    clickedMarker = null;
                    return;
                }
            }
        }*/
    }

    public void clusterManagerOnCameraIdle(){
        mClusterManager.onCameraIdle();
    }
    public boolean isInMarker (Pin  pin){
        for (Marker mark : mClusterManager.getMarkerCollection().getMarkers()){

            if (pin.getPosition().latitude==mark.getPosition().latitude && pin.getPosition().longitude==mark.getPosition().longitude)
                return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapClick(LatLng latLng) {
        View v =getCurrentFocus();
        if(v!=null && (searchView.getVisibility()!=View.GONE)){
            if(searchView.isIconified()){
                // get the center for the clipping circle
                int cx = searchView.getWidth() / 2;
                int cy = searchView.getHeight() / 2;

                float initialRadius = (float) Math.hypot(cx, cy);

                Animator anim =
                        ViewAnimationUtils.createCircularReveal(searchView, cx, cy, initialRadius, 0);

                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        searchView.setVisibility(View.GONE);
                    }
                });
                AlphaAnimation animation1 = new AlphaAnimation(searchView.getAlpha(), 0.1f);
                animation1.setDuration(anim.getDuration());
                searchView.startAnimation(animation1);
                anim.start();

             /*   searchView.animate().
                        scaleX(1.4f).
                        scaleY(1.4f)
                        .alpha(.0f)*/
                return;
            }

            if(searchView.getQuery().length()!=0){
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            else{
                searchView.setQuery(searchView.getQuery(),false);
                searchView.setIconified(true);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(searchView.getVisibility()==View.VISIBLE){
            onMapClick(mMap.getCameraPosition().target);
        }
        else {
            super.onBackPressed();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onMarkerClick(Marker marker) {
       clickedMarker=marker;
        if(searchView.getVisibility()==View.VISIBLE)
            onMapClick(mMap.getCameraPosition().target);
        return mClusterManager.onMarkerClick(marker);
    }
}
