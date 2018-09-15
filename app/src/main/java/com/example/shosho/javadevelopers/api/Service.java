package com.example.shosho.javadevelopers.api;

import com.example.shosho.javadevelopers.model.ItemResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("/search/users?q=language:java+location:lagos")
    Call<ItemResponse> getitems();
}
