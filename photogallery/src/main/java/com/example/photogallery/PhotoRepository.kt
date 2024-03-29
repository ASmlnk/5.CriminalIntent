package com.example.photogallery

import com.example.photogallery.model.GalleryItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create

class PhotoRepository {

    private val flickrApi: FlickrApi
    private val moshi: Moshi

    init {
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        flickrApi = retrofit.create<FlickrApi>()
    }
    suspend fun fetchPhotos(): List<GalleryItem> = flickrApi.fetchPhotos().photos.galleryItems
}