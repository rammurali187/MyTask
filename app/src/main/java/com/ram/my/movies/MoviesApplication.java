package com.ram.my.movies;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.RefWatcher;

import dagger.ObjectGraph;
import timber.log.Timber;

public final class MoviesApplication extends Application {

    private ObjectGraph objectGraph;
    private RefWatcher refWatcher;


    public static MoviesApplication get(Context context) {
        return (MoviesApplication) context.getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();

        refWatcher = installLeakCanary();
        objectGraph = initializeObjectGraph();

        Timber.plant(new Timber.DebugTree());
    }

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    public ObjectGraph buildScopedObjectGraph(Object... modules) {
        return objectGraph.plus(modules);
    }

    public void inject(Object o) {
        objectGraph.inject(o);
    }

    protected RefWatcher installLeakCanary() {
        return RefWatcher.DISABLED;
    }

    private ObjectGraph initializeObjectGraph() {
        return buildInitialObjectGraph(new AppModule(this));
    }

    private ObjectGraph buildInitialObjectGraph(Object... modules) {
        return ObjectGraph.create(modules);
    }


}
