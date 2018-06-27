package com.ram.my.movies.data.repository;

import android.content.ContentResolver;

import com.ram.my.movies.data.api.MoviesApi;
import com.ram.my.movies.data.api.Sort;
import com.ram.my.movies.data.model.Movie;
import com.ram.my.movies.data.model.Review;
import com.ram.my.movies.data.model.Video;
import com.squareup.sqlbrite.BriteContentResolver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.schedulers.Schedulers;

final class MoviesRepositoryImpl implements MoviesRepository {

    private final MoviesApi mMoviesApi;
    private final ContentResolver mContentResolver;
    private final BriteContentResolver mBriteContentResolver;


    public MoviesRepositoryImpl(MoviesApi moviesApi, ContentResolver contentResolver,
                                BriteContentResolver briteContentResolver) {
        mMoviesApi = moviesApi;
        mContentResolver = contentResolver;
        mBriteContentResolver = briteContentResolver;
    }

    @Override
    public Observable<List<Movie>> discoverMovies(Sort sort, int page) {
        Observable<List<Movie>> test =  mMoviesApi.discoverMovies(sort, page)
                .timeout(5, TimeUnit.SECONDS)
                .retry(2)
                .map(response -> response.movies)
                .subscribeOn(Schedulers.io());

        return test;
    }

    @Override
    public Observable<List<Review>> reviews(long movieId) {
        return mMoviesApi.reviews(movieId, 1)
                .timeout(5, TimeUnit.SECONDS)
                .retry(2)
                .map(response -> response.reviews);
    }

    @Override
    public Observable<List<Video>> videos(long movieId) {
        return mMoviesApi.videos(movieId)
                .timeout(2, TimeUnit.SECONDS)
                .retry(2)
                .map(response -> response.videos);
    }

}
