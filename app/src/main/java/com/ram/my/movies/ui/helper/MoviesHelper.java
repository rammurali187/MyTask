package com.ram.my.movies.ui.helper;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.ram.my.movies.data.model.Genre;
import com.ram.my.movies.data.model.Video;
import com.ram.my.movies.data.repository.MoviesRepository;

import java.util.ArrayList;
import java.util.List;

import rx.subjects.PublishSubject;
import timber.log.Timber;

public class MoviesHelper {

    private static final PublishSubject<FavoredEvent> FAVORED_SUBJECT = PublishSubject.create();

    private final Activity mActivity;
    private final MoviesRepository mRepository;

    public MoviesHelper(Activity activity, MoviesRepository moviesRepository) {
        mActivity = activity;
        mRepository = moviesRepository;
    }



    public void playVideo(Video video) {
        if (video.getSite().equals(Video.SITE_YOUTUBE))
            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
        else
            Timber.w("Unsupported video format");
    }


    public static class FavoredEvent {
        public long movieId;
        public boolean favored;

        private FavoredEvent(long movieId, boolean favored) {
            this.movieId = movieId;
            this.favored = favored;
        }
    }

    public List<Genre> genrelist() {
        List<Genre> lst = new ArrayList<Genre>();
        lst.add(new Genre(28,"Action"));
        lst.add(new Genre(12,"Adventure"));
        lst.add(new Genre(16,"Animation"));
        lst.add(new Genre(35,"Comedy"));
        lst.add(new Genre(80,"Crime"));
        lst.add(new Genre(99,"Documentary"));
        lst.add(new Genre(18,"Drama"));
        lst.add(new Genre(10751,"Family"));
        lst.add(new Genre(14,"Fantasy"));
        lst.add(new Genre(10765,"Foreign"));
        lst.add(new Genre(36,"History"));
        lst.add(new Genre(27,"Horror"));
        lst.add(new Genre(10402,"Music"));
        lst.add(new Genre(9648,"Mystery"));
        lst.add(new Genre(10749,"Romance"));
        lst.add(new Genre(878,"Science Fiction"));
        lst.add(new Genre(10770,"TV Movie"));
        lst.add(new Genre(53,"Thriller"));
        lst.add(new Genre(10752,"War"));
        lst.add(new Genre(37,"Western"));

        return  lst;
    }
}
