package com.ram.my.movies.data;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ram.my.movies.data.api.ApiModule;
import com.ram.my.movies.data.provider.ProviderModule;
import com.ram.my.movies.data.repository.RepositoryModule;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static java.util.concurrent.TimeUnit.SECONDS;

@Module(
        includes = {
                ApiModule.class,
                ProviderModule.class,
                RepositoryModule.class
        },
        injects = {
                GlideSetup.class
        },
        complete = false,
        library = true
)
public final class DataModule {
    public static final int DISK_CACHE_SIZE = 50 * 1024 * 1024;

    @Provides
    @Singleton Gson provideGson() {
        return new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Provides
    @Singleton OkHttpClient provideOkHttpClient(Application app) {
        return createOkHttpClient(app);
    }

    static OkHttpClient createOkHttpClient(Application app) {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(20, SECONDS);
        client.setReadTimeout(10, SECONDS);
        client.setWriteTimeout(10, SECONDS);

        File cacheDir = new File(app.getCacheDir(), "http");
        Cache cache = new Cache(cacheDir, DISK_CACHE_SIZE);
        client.setCache(cache);

        return client;
    }
}
