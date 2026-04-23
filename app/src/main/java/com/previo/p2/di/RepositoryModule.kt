package com.previo.p2.di

import com.previo.p2.data.repository.FavoriteRepositoryImpl
import com.previo.p2.data.repository.MealRepositoryImpl
import com.previo.p2.data.repository.NutritionRepositoryImpl
import com.previo.p2.data.repository.TranslationRepositoryImpl
import com.previo.p2.domain.repository.FavoriteRepository
import com.previo.p2.domain.repository.MealRepository
import com.previo.p2.domain.repository.NutritionRepository
import com.previo.p2.domain.repository.TranslationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMealRepository(impl: MealRepositoryImpl): MealRepository

    @Binds
    @Singleton
    abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @Binds
    @Singleton
    abstract fun bindNutritionRepository(impl: NutritionRepositoryImpl): NutritionRepository

    @Binds
    @Singleton
    abstract fun bindTranslationRepository(impl: TranslationRepositoryImpl): TranslationRepository
}