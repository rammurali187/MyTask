package com.ram.my.movies.data.repository;

import com.ram.my.movies.data.api.Sort;
import com.ram.my.movies.data.model.Movie;
import com.ram.my.movies.data.model.Review;
import com.ram.my.movies.data.model.Video;

import java.util.List;

import rx.Observable;

public interface MoviesRepository {

    Observable<List<Movie>> discoverMovies(Sort sort, int page);

    Observable<List<Video>> videos(long movieId);

    Observable<List<Review>> reviews(long movieId);

}
