package com.android.example.shreebhagwatgita.dataSource

import com.android.example.shreebhagwatgita.Models.ChaptersItem
import com.android.example.shreebhagwatgita.versesItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface ApiInterface {



    @GET("/v2/chapters/")
    fun getAllChapter(): Call<List<ChaptersItem>>

    @GET("v2/chapters/{chapterNumber}/verses/")
    fun getVerses(@Path("chapterNumber") chapterNumber:Int):Call<List<versesItem>>


    @GET("v2/chapters/{chapterNum}/verses/{verseNum}/")
    fun getAParticularVerse(
        @Path("chapterNum") chapterNumber: Int,
        @Path("verseNum") verseNum:Int
    ):Call<versesItem>


}