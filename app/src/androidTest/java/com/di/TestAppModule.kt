package com.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by karim on 22,November,2020
 */
@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

//    @Provides
//    @Named("test_db")
//    fun provideInMemoryDB(@ApplicationContext context: Context) =
//        Room.inMemoryDatabaseBuilder(context, ShoppingItemDataBase::class.java)
//            .allowMainThreadQueries()
//            .build()


}