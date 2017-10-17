package com.example.stefano.lomux_pro.rest;


import com.example.stefano.lomux_pro.model.Pin;

import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by FrancescoMargiotta on 18/07/2017.
 */

public interface PinClient {
    //@POST("read_database/get_local_pins")
    @GET("read_database/get_local_pins")
    //@FormUrlEncoded
    Call<List<Pin>> getLocalPins(@Query("neLat") double neLat, @Query("neLon") double neLon, @Query("swLat") double swLat, @Query("swLon") double swLon,@Query("ids") List<String> ids);

}
