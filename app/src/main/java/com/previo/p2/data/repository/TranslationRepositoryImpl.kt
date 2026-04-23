package com.previo.p2.data.repository

import com.previo.p2.data.remote.api.TranslationService
import com.previo.p2.domain.repository.TranslationRepository
import com.previo.p2.domain.util.Result
import kotlinx.coroutines.delay
import javax.inject.Inject

class TranslationRepositoryImpl @Inject constructor(
    private val translationService: TranslationService
) : TranslationRepository {

    override suspend fun translate(text: String): Result<String> {
        return try {
            if (text.isBlank()) return Result.Success(text)
            val chunks = text.chunked(450)
            val translated = chunks.map { chunk ->
                val response = translationService.translate(text = chunk)
                parseGoogleTranslateResponse(response) ?: chunk
            }
            Result.Success(translated.joinToString(" "))
        } catch (e: Exception) {
            Result.Error(e, e.message)
        }
    }

    override suspend fun translateBatch(texts: List<String>): List<String> {
        val results = mutableListOf<String>()
        texts.chunked(5).forEach { batch ->
            batch.forEach { text ->
                val result = translate(text)
                results.add((result as? Result.Success)?.data ?: text)
            }
            delay(300)
        }
        return results
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseGoogleTranslateResponse(response: List<Any>): String? {
        return try {
            val sentences = response[0] as? List<*> ?: return null
            sentences.mapNotNull { sentence ->
                val parts = sentence as? List<*>
                parts?.getOrNull(0) as? String
            }.joinToString("").ifBlank { null }
        } catch (e: Exception) {
            null
        }
    }
}