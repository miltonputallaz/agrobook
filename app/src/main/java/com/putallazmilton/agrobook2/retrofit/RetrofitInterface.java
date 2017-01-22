package com.putallazmilton.agrobook2.retrofit;

import android.database.Observable;
import android.graphics.Bitmap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Milton on 23/11/2016.
 */

public interface RetrofitInterface {
    @Multipart
    @POST("problemas")
    Call<ResponseBody> upload(
                                @Part MultipartBody.Part imagen,
                              @Part("descripcion") RequestBody descripcion,
                              @Part ("usuario") RequestBody usuario,
                                @Part ("idUsuario") RequestBody idusuario
    );



}
