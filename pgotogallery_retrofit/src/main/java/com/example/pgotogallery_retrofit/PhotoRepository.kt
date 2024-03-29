package com.example.pgotogallery_retrofit

import com.example.pgotogallery_retrofit.api.FlickrApi
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class PhotoRepository {

    private val flickrApi: FlickrApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://www.flickr.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

        flickrApi = retrofit.create<FlickrApi>()
    }

    suspend fun fetchContents() = flickrApi.fetchContents()
}