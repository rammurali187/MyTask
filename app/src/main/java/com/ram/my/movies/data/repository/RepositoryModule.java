package com.ram.my.movies.data.repository;


import android.content.ContentResolver;

import com.ram.my.movies.data.api.MoviesApi;
import com.squareup.sqlbrite.BriteContentResolver;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public final class RepositoryModule {


    @Singleton
    @Provides
    public MoviesRepository providesMoviesRepository(MoviesApi moviesApi, ContentResolver contentResolver,
                                                     BriteContentResolver briteContentResolver) {
        MoviesRepository movrepos = new MoviesRepositoryImpl(moviesApi, contentResolver, briteContentResolver);

        return  movrepos;
    }

}
