package com.ram.my.movies.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;


public final class Movie implements Parcelable {

    @Expose
    long id;


    @Expose @SerializedName("genre_ids")
    List<Integer> genreIds = new ArrayList<>();

    @Expose
    String overview;

    @Expose @SerializedName("release_date")
    String releaseDate;

    @Expose @SerializedName("poster_path")
    String posterPath;

    @Expose @SerializedName("backdrop_path")
    String backdropPath;

    @Expose
    double popularity;

    @Expose
    String title;

    @Expose @SerializedName("vote_average")
    double voteAverage;

    @Expose @SerializedName("vote_count")
    long voteCount;

    @Expose @SerializedName("original_language")
    String language;

    @Expose
    String adult;

    boolean favored = false;
    List<Genre> genres;

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Movie() {}

    public long getId() {
        return id;
    }

    public Movie setId(long id) {
        this.id = id;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Movie setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }


    public String getOverview() {
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public double getPopularity() {
        return popularity;
    }

    public Movie setPopularity(double popularity) {
        this.popularity = popularity;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public Movie setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public long getVoteCount() {
        return voteCount;
    }

    public Movie setVoteCount(long voteCount) {
        this.voteCount = voteCount;
        return this;
    }



    public boolean isFavored() {
        return favored;
    }

    public String getLanguage() {
        return language;
    }

    public Movie setLanguage(String language) {
        this.language = language;
        return this;
    }



    public Movie setFavored(boolean favored) {
        this.favored = favored;
        return this;
    }

    public String getAdult() {
        return adult;
    }

    public Movie setAdult(String adult) {
        this.adult = adult;
        return this;
    }



    @Override
    public String toString() {
        return "Movie{" + " title='" + title + '}';
    }

    public static final class Response {

        @Expose
        public int page;

        @Expose @SerializedName("total_pages")
        public int totalPages;

        @Expose @SerializedName("total_results")
        public int totalMovies;

        @Expose @SerializedName("results")
        public List<Movie> movies = new ArrayList<>();
    }

    // --------------------------------------------------------------------------------------

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeList(this.genreIds);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeDouble(this.popularity);
        dest.writeString(this.title);
        dest.writeDouble(this.voteAverage);
        dest.writeLong(this.voteCount);
        dest.writeString(this.language);
        dest.writeString(this.adult);
        dest.writeByte(favored ? (byte) 1 : (byte) 0);

    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.genreIds = new ArrayList<Integer>();
        in.readList(this.genreIds, List.class.getClassLoader());
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.popularity = in.readDouble();
        this.title = in.readString();
        this.voteAverage = in.readDouble();
        this.voteCount = in.readLong();
        this.language = in.readString();
        this.adult = in.readString();
        this.favored = in.readByte() != 0;

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {return new Movie(source);}

        public Movie[] newArray(int size) {return new Movie[size];}
    };
}
