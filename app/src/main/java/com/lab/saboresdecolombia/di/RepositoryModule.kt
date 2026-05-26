package com.lab.saboresdecolombia.di

import com.lab.saboresdecolombia.core.data.repository.FavoritesRepositoryImpl
import com.lab.saboresdecolombia.core.data.repository.RecipeRepositoryImpl
import com.lab.saboresdecolombia.core.domain.repository.FavoritesRepository
import com.lab.saboresdecolombia.core.domain.repository.RecipeRepository
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
    abstract fun bindRecipeRepository(impl: RecipeRepositoryImpl): RecipeRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
}
