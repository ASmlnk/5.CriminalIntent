package com.example.photogallery

import com.example.photogallery.model.FlickrResponse
import retrofit2.http.GET

private const val API_KEY = "9bbe1ccb0d6f93a57889bba5ca223d23"


interface FlickrApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
        "&api_key=$API_KEY" +
        "&format=json" +
        "&nojsoncallback=1" +
        "&extras=url_s"
    )
    suspend fun fetchPhotos(): FlickrResponse
}


/*
interface FlickrApi {
    @GET("/")
    suspend fun fetchContents(): String
}*/
