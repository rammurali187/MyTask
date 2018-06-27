package com.ram.my.movies.ui.module;


import com.ram.my.movies.AppModule;
import com.ram.my.movies.ui.fragment.MovieFragment;
import com.ram.my.movies.ui.fragment.SortedMoviesFragment;

import dagger.Module;

@Module(
        injects = {
                SortedMoviesFragment.class,
                MovieFragment.class
        },
        addsTo = AppModule.class
)
public final class MoviesModule {}
