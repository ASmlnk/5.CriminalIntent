package com.example.pgotogallery_retrofit.api

import retrofit2.http.GET

interface FlickrApi {

    @GET("/")
    suspend fun fetchContents(): String
}