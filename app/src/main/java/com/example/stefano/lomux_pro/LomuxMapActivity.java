package com.example.stefano.lomux_pro;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebHistoryItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import com.example.stefano.lomux_pro.callbacks.PinsCallback;
import com.example.stefano.lomux_pro.listener.DrawnerItemClickListener;
import com.example.stefano.lomux_pro.listener.MapChangesListener;
import com.example.stefano.lomux_pro.model.Pin;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class LomuxMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback, GoogleMap.OnMapClickListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private final static LatLng london_center = new LatLng(51.509865, -0.118092);
    private final  int panelInfoHeigth = 200;
    private ClusterManager<Pin> mClusterManager;
    private SearchView searchView;
    private List<String> ids;
    SlidingUpPanelLayout slidingUpPanelLayout;

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
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        slidingUpPanelLayout.setOverlayed(true);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        searchView.setIconifiedByDefault(true);
        searchView.setQueryHint("Search a Pin");
        searchView.clearFocus();
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(searchView.isIconified()){
                    searchView.setIconified(false);
                    //searchView.performClick();
                }
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //the keyboard is shown, change the map fragment
// Create new fragment and transaction
             /*   Fragment newFragment = new MapInfoFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

// Replace whatever is in the fragment_container view with this fragment,
// and add the transaction to the back stack if needed
                transaction.replace(R.id.map, newFragment);
                transaction.addToBackStack(null);

// Commit the transaction
                transaction.commit();*/
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
    }

    public void addPins(List<Pin> pins){

        for(Pin elem:pins){
            ids.add(elem.getIdPin());
            mClusterManager.addItem(elem);
        }
        mClusterManager.cluster();
    }

    public void clusterManagerOnCameraIdle(){
        mClusterManager.onCameraIdle();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onMapClick(LatLng latLng) {
        if (slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else if(slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.COLLAPSED)
            slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
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
        else if (slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.EXPANDED)
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        else if(slidingUpPanelLayout.getPanelState()== SlidingUpPanelLayout.PanelState.COLLAPSED)
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        else
            super.onBackPressed();


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onMarkerClick(Marker marker) {
        if(searchView.getVisibility()==View.VISIBLE)
            onMapClick(mMap.getCameraPosition().target);

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        return mClusterManager.onMarkerClick(marker);
    }


}
