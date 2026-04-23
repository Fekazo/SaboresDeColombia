package com.previo.p2.data.mapper

import com.previo.p2.data.local.entity.FavoriteEntity
import com.previo.p2.domain.model.Favorite
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun FavoriteEntity.toDomain(): Favorite = Favorite(
    idMeal = idMeal,
    strMeal = strMeal,
    strMealThumb = strMealThumb,
    strArea = strArea,
    translatedName = translatedName,
    savedAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(savedAt), ZoneId.systemDefault())
)

fun Favorite.toEntity(): FavoriteEntity = FavoriteEntity(
    idMeal = idMeal,
    strMeal = strMeal,
    strMealThumb = strMealThumb,
    strArea = strArea,
    translatedName = translatedName,
    savedAt = savedAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
)