package com.example.stefano.lomux_pro;

/**
 * Created by Stefano on 16/10/2017.
 */


        import android.content.Context;

        import com.example.stefano.lomux_pro.model.Pin;
        import com.example.stefano.lomux_pro.model.Pintype;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.model.BitmapDescriptorFactory;
        import com.google.android.gms.maps.model.Marker;
        import com.google.android.gms.maps.model.MarkerOptions;
        import com.google.maps.android.clustering.ClusterManager;
        import com.google.maps.android.clustering.*;
        import com.google.maps.android.clustering.view.DefaultClusterRenderer;


/**
 * Created by Franc on 07/10/2017.
 */

public class PinRenderer extends DefaultClusterRenderer<Pin> {


    public PinRenderer(Context context, GoogleMap map, ClusterManager clusterManager) {
        super(context, map, clusterManager);
    }

    @Override
    protected void onBeforeClusterItemRendered(Pin curPin, MarkerOptions markerOptions) {
        // Draw a single person.
        // Set the info window to show their name.
        switch (curPin.getPinTypeidPinType().getIdPinType()) {
            case Pintype.VENUE:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_v));
               break;
            case Pintype.STUDIO:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_r));
                break;
            case Pintype.WORK:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_w));
                break;
            case Pintype.PRIVATE:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_p));
                break;
            case Pintype.MONUMENT:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_m));
                break;
            default:
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon_l));

        }
    }

    @Override
    public Marker getMarker(Pin clusterItem) {
        return super.getMarker(clusterItem);
    }

    @Override
    public Pin getClusterItem(Marker marker) {
        return super.getClusterItem(marker);
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        // Always render clusters.
        return cluster.getSize() > 1;
    }
}