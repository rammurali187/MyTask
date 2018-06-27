package com.ram.my.movies.data.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class PopularMoviesAuthenticatorService extends Service {
    private PopularMoviesAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        mAuthenticator = new PopularMoviesAuthenticator(this);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
