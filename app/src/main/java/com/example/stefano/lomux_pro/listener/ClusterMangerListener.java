package com.example.stefano.lomux_pro.listener;

import android.util.Log;

import com.example.stefano.lomux_pro.PinRenderer;
import com.example.stefano.lomux_pro.model.Pin;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

/**
 * Created by Stefano on 18/10/2017.
 */

public class ClusterMangerListener implements GoogleMap.OnMarkerClickListener {

    ClusterManager clusterManager;
    PinRenderer pinRenderer;
    SlidingUpPanelLayout slidingUpPanelLayout;

    public ClusterMangerListener(ClusterManager<Pin> clusterManager, PinRenderer pinRenderer, SlidingUpPanelLayout slidingUpPanelLayout) {
        this.clusterManager = clusterManager;
        this.pinRenderer = pinRenderer;
        this.slidingUpPanelLayout = slidingUpPanelLayout;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Pin pin = pinRenderer.getClusterItem(marker);
        if(pin!=null)
            return onClusterItemClick(marker);
        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        return clusterManager.onMarkerClick(marker);
    }


    public boolean onClusterItemClick(Marker marker) {

        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        return clusterManager.onMarkerClick(marker);

    }
}
