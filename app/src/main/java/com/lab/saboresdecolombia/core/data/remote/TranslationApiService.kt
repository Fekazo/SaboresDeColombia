package com.lab.saboresdecolombia.core.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationApiService {

    @GET("translate_a/single")
    suspend fun translate(
        @Query("client") client: String = "gtx",
        @Query("sl") sl: String = "auto",
        @Query("tl") tl: String = "es",
        @Query("dt") dt: String = "t",
        @Query("q") q: String
    ): ResponseBody
}
