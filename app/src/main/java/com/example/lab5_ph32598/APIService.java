package com.example.lab5_ph32598;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {
     String DOMAIN = "http://192.168.13.104:3000";
     @GET("/api/list")
     Call<List<Model>> getModels();
     @POST("/api/add")
     Call<Model> addModel(@Body Model laptop);

     @DELETE("/api/delete/{id}") // Sử dụng Path Variable để truyền ID của laptop cần xóa
     Call<Model> deleteModel(@Path("id") String id);

     // Phương thức searchModels để tìm kiếm dữ liệu
     @GET("/api/search")
     Call<List<Model>> searchModels(@Query("keyword") String keyword);
}
