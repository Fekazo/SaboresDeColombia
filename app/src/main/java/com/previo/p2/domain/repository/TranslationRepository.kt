package com.previo.p2.domain.repository

import com.previo.p2.domain.util.Result

interface TranslationRepository {
    suspend fun translate(text: String): Result<String>
    suspend fun translateBatch(texts: List<String>): List<String>
}