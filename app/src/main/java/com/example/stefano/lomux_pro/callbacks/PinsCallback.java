package com.example.stefano.lomux_pro.callbacks;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.example.stefano.lomux_pro.LomuxMapActivity;
import com.example.stefano.lomux_pro.model.Pin;
import com.example.stefano.lomux_pro.rest.RestConfig;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Stefano on 16/10/2017.
 */

public class PinsCallback {
    private static PinsCallback pinsCallback = null;


    public static PinsCallback getInstance(){
        if(pinsCallback == null)
            pinsCallback = new PinsCallback();
        return pinsCallback;

    }

    public void get_local_pins(final GoogleMap mMap,LatLngBounds maxArea,List<String> ids, final LomuxMapActivity context){

        LatLng n_e = maxArea.northeast;
        LatLng s_w = maxArea.southwest;
        Call<List<Pin>> call = RestConfig.getInstance().getPinClient().getLocalPins(n_e.latitude, n_e.longitude, s_w.latitude, s_w.longitude, ids);

        call.enqueue(new Callback<List<Pin>>() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onResponse(Call<List<Pin>> call, Response<List<Pin>> response) {

                if (response.isSuccessful()&&response.body()!=null) {
                    context.addPins(response.body());
                }
                else{
                    onFailure(call,new Throwable());
                }
            }

            @Override
            public void onFailure(Call<List<Pin>> call, Throwable t) {
                Toast.makeText(context,"Server connection error", Toast.LENGTH_SHORT);
                Log.d("FAL","FALLITO"+ t.getMessage());

            }
        });

    }
}
