package com.previo.p2.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Query

interface TranslationService {
    @GET("translate_a/single")
    suspend fun translate(
        @Query("client") client: String = "gtx",
        @Query("sl") sourceLang: String = "auto",
        @Query("tl") targetLang: String = "es",
        @Query("dt") dt: String = "t",
        @Query("q") text: String
    ): List<Any>
}