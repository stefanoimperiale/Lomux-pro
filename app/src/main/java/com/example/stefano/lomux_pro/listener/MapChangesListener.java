package com.example.stefano.lomux_pro.listener;

import android.content.Context;
import android.util.Log;

import com.example.stefano.lomux_pro.LomuxMapActivity;
import com.example.stefano.lomux_pro.callbacks.PinsCallback;
import com.example.stefano.lomux_pro.model.Pin;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefano on 16/10/2017.
 */

public class MapChangesListener implements GoogleMap.OnCameraIdleListener{

    private GoogleMap mMap;
    private LatLngBounds actualMaxVisibleArea;
    private LomuxMapActivity context;


    public MapChangesListener (GoogleMap mMap, LomuxMapActivity context){
        this.mMap=mMap;
        this.actualMaxVisibleArea =  mMap.getProjection().getVisibleRegion().latLngBounds;
        apply_bounds();
        this.context = context;
        Log.d("AREA",actualMaxVisibleArea.toString());

    }

    @Override
    public void onCameraIdle() {
        LatLngBounds updateBounds = mMap.getProjection().getVisibleRegion().latLngBounds;
        if(actualMaxVisibleArea.contains(updateBounds.northeast)
                && actualMaxVisibleArea.contains(updateBounds.southwest)){
            // the actual visible area is smaller than before, so it not need to call the database
            Log.d("Camera","NO UPDATE");
            context.clusterManagerOnCameraIdle();
        }
        else
        {
            //the actual visible area is bigger than before or the user has moved the map, need to refresh
            actualMaxVisibleArea = updateBounds;
            apply_bounds();
            PinsCallback.getInstance().get_local_pins(mMap, actualMaxVisibleArea, context.getListIds(), context);
            Log.d("Camera","UPDATE");
        }

    }

    private void apply_bounds(){
        LatLng ne = actualMaxVisibleArea.northeast;
        LatLng sw = actualMaxVisibleArea.southwest;
        double newNeLa = ne.latitude+1;
        double newNeLo = ne.longitude+1;

        double newSwLa = sw.latitude-1;
        double newSwLo = sw.longitude-1;

        actualMaxVisibleArea = new LatLngBounds(new LatLng(newSwLa,newSwLo),new LatLng(newNeLa, newNeLo));
    }
    public LatLngBounds getActualVisibleArea(){
        return actualMaxVisibleArea;
    }
}
