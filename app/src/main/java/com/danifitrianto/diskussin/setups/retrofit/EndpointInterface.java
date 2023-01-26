package com.danifitrianto.diskussin.setups.retrofit;

import android.util.Log;

import com.danifitrianto.diskussin.models.Rentings;
import com.danifitrianto.diskussin.models.Rooms;
import com.danifitrianto.diskussin.models.Users;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EndpointInterface {
    @GET("Rooms")
    Call<List<Rooms>> getRooms();

    @GET("Rooms/{id}")
    Call<Rooms> getSingleRoom(@Path("id") int id);

    @GET("Rentings")
    Call<List<Rentings>> getRentings();

    @POST("Login")
    Call<String> login(@Body Users users);

    @POST("Rentings")
    Call<Rentings> rentRoom(@Body Rentings rentings);

    @PUT("Rentings/{id}")
    Call<Rentings> editRent(@Body Rentings rentings, @Path("id") int id);

    @DELETE("Rentings/{id}")
    Call<String> deleteRent(@Path("id") int id);

}
