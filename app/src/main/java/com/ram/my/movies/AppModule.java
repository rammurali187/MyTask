package com.ram.my.movies;

import android.app.Application;

import com.ram.my.movies.data.DataModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        includes = DataModule.class,
        injects = {
                MoviesApplication.class
        },
        library = true
)
public final class AppModule {
    private final MoviesApplication application;

    public AppModule(MoviesApplication application) {
        this.application = application;
    }

    @Provides @Singleton Application provideApplication() {
        return application;
    }

}

