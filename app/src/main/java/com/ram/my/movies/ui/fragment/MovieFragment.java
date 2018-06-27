package com.ram.my.movies.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.ram.my.movies.R;
import com.ram.my.movies.data.model.Genre;
import com.ram.my.movies.data.model.Movie;
import com.ram.my.movies.data.model.Review;
import com.ram.my.movies.data.model.Video;
import com.ram.my.movies.data.repository.MoviesRepository;
import com.ram.my.movies.ui.activity.DetailsActivity;
import com.ram.my.movies.ui.helper.MoviesHelper;
import com.ram.my.movies.ui.module.MoviesModule;
import com.ram.my.movies.utils.Lists;
import com.ram.my.movies.utils.UiUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.BindColor;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

import static butterknife.ButterKnife.findById;

public final class MovieFragment extends BaseFragment implements ObservableScrollViewCallbacks {


    @Nullable
    Toolbar mToolbar;

    @Bind(R.id.movie_scroll_view)
    ObservableScrollView mScrollView;
    @Bind(R.id.movie_poster)
    ImageView mPosterImage;
    @Bind(R.id.movie_poster_play)
    ImageView mPosterPlayImage;
    @Bind(R.id.movie_cover)
    ImageView mCoverImage;
    @Bind(R.id.movie_cover_container)
    FrameLayout mCoverContainer;

    @Bind(R.id.movie_title)
    TextView mTitle;
    @Bind(R.id.movie_genre)
    TextView mGenre;
    @Bind(R.id.movie_rating)
    RatingBar movie_rating;
    @Bind(R.id.movie_overview)
    TextView mOverview;
    @Bind(R.id.movie_reviews_container)
    ViewGroup mReviewsGroup;
    @Bind(R.id.movie_videos_container)
    ViewGroup mVideosGroup;
    @Bind(R.id.img_release)
    ImageView img_release;
    @Bind(R.id.txt_release)
    TextView txt_release;
    @Bind(R.id.overview_collapse)
    TextView overview_collapse;
    @Bind(R.id.btnreleasedate)
    Button btnreleasedate;
    @Bind(R.id.btnstar)
    Button btnstar;
    @Bind(R.id.btnlanguage)
    Button btnlanguage;


    @BindColor(R.color.theme_primary)
    int mColorThemePrimary;
    @BindColor(R.color.body_text_white)
    int mColorTextWhite;

    @Inject
    MoviesRepository mMoviesRepository;

    private MoviesHelper mHelper;
    private CompositeSubscription mSubscriptions;
    private List<Runnable> mDeferredUiOperations = new ArrayList<>();
    private Movie mMovie;
    private List<Review> mReviews;
    private List<Video> mVideos;
    private Video mTrailer;

    public static MovieFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putParcelable("arg_movie", movie);

        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        mHelper = new MoviesHelper(activity, mMoviesRepository);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trySetupToolbar();
        mScrollView.setScrollViewCallbacks(this);

        if (savedInstanceState != null) {
            mVideos = savedInstanceState.getParcelableArrayList("state_trailers");
            mReviews = savedInstanceState.getParcelableArrayList("state_reviews");
            mScrollView.onRestoreInstanceState(savedInstanceState.getParcelable("state_scroll_view"));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSubscriptions = new CompositeSubscription();
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);

        onMovieLoaded(getArguments().getParcelable("arg_movie"));

        if (mVideos != null) onVideosLoaded(mVideos);
        else loadVideos();

        if (mReviews != null) onReviewsLoaded(mReviews);
        else loadReviews();


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("state_scroll_view", mScrollView.onSaveInstanceState());
        if (mReviews != null)
            outState.putParcelableArrayList("state_reviews", new ArrayList<>(mReviews));
        if (mVideos != null)
            outState.putParcelableArrayList("state_trailers", new ArrayList<>(mVideos));
    }

    @Override
    public void onDestroyView() {
        mSubscriptions.unsubscribe();
        super.onDestroyView();
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewCompat.setTranslationY(mCoverContainer, scrollY / 2);

        if (mToolbar != null) {
            int parallaxImageHeight = mCoverContainer.getMeasuredHeight();
            float alpha = Math.min(1, (float) scrollY / parallaxImageHeight);
            mToolbar.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, mColorThemePrimary));
            mToolbar.setTitleTextColor(ScrollUtils.getColorWithAlpha(alpha, mColorTextWhite));
        }
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void trySetupToolbar() {
        if (getActivity() instanceof DetailsActivity) {
            DetailsActivity activity = ((DetailsActivity) getActivity());
            mToolbar = activity.getToolbar();
        }
    }

    private void onMovieLoaded(Movie movie) {
        mMovie = movie;
       String strgenre="";
       List<Integer> genreid = mMovie.getGenreIds();
        List<Genre> genrelist = mHelper.genrelist();

        for (Genre item : genrelist) {
            if (genreid.contains(item.getId())) {
                strgenre += item.getName()+",";
            } else {

            }
        }
        if (mToolbar != null) {
            mToolbar.setTitle(mMovie.getTitle());
        }

        mTitle.setText(mMovie.getTitle());
        mGenre.setText(strgenre.length()>0?strgenre.substring(0,strgenre.length()-1):strgenre);
        float rating = (float) mMovie.getVoteAverage();
        movie_rating.setRating(rating / 2);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd");

        Date strDate = null;
        String isrelaesed = "";
        try {
            strDate = dateFormat.parse(mMovie.getReleaseDate());
            if (new Date().before(strDate)) {
                img_release.setBackground(getResources().getDrawable(android.R.drawable.presence_video_busy));
                txt_release.setTextColor(getResources().getColor(R.color.notreleased));
                isrelaesed = "Not Released";
            } else {
                isrelaesed = "Released";
                img_release.setBackground(getResources().getDrawable(android.R.drawable.presence_video_online));
                txt_release.setTextColor(getResources().getColor(R.color.released));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        btnreleasedate.setText(UiUtils.getDisplayReleaseDate(mMovie.getReleaseDate()));
        btnlanguage.setText(mMovie.getLanguage());
        btnstar.setText(String.valueOf(mMovie.getVoteCount()));

        txt_release.setText(isrelaesed);
        mOverview.setText(mMovie.getOverview());

        overview_collapse.setOnClickListener(v -> {
            if (overview_collapse.getText().toString().equalsIgnoreCase("EXPAND")) {
                expand(mOverview);
                overview_collapse.setText("COLLAPSE");
            } else {
                collapse(mOverview);
                overview_collapse.setText("EXPAND");
            }
        });




        Glide.with(this).load(mMovie.getBackdropPath())
                .placeholder(R.color.movie_cover_placeholder)
                .centerCrop()
                .crossFade()
                .into(mCoverImage);

        Glide.with(this).load(mMovie.getPosterPath())
                .centerCrop()
                .crossFade()
                .into(mPosterImage);
    }

    private void loadReviews() {
        mSubscriptions.add(mMoviesRepository.reviews(mMovie.getId())
                .subscribe(reviews -> {
                    Timber.d(String.format("Reviews loaded, %d items.", reviews.size()));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReviewsLoaded(reviews);
                        }
                    });

                }, throwable -> {
                    Timber.e(throwable, "Reviews loading failed.");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onReviewsLoaded(null);
                        }
                    });

                }));
    }

    private void onReviewsLoaded(List<Review> reviews) {
        mReviews = reviews;
        try {

            for (int i = mReviewsGroup.getChildCount() - 1; i >= 2; i--) {
            mReviewsGroup.removeViewAt(i);
        }

        final LayoutInflater inflater = LayoutInflater.from(getActivity());
        boolean hasReviews = false;

        if (!Lists.isEmpty(reviews)) {
            for (Review review : reviews) {
                if (TextUtils.isEmpty(review.getAuthor())) {
                    continue;
                }

                final View reviewView = inflater.inflate(R.layout.item_review_detail, mReviewsGroup, false);
                final TextView reviewAuthorView = findById(reviewView, R.id.review_author);
                final TextView reviewContentView = findById(reviewView, R.id.review_content);

                reviewAuthorView.setText(review.getAuthor());
                reviewContentView.setText(review.getContent());

                mReviewsGroup.addView(reviewView);
                hasReviews = true;
            }
        }

        mReviewsGroup.setVisibility(hasReviews ? View.VISIBLE : View.GONE);
        }catch (Exception ex)
        {
            Timber.d("Review Loaded ex: " + ex.getMessage().toString());
        }

    }


    private void loadVideos() {
        mSubscriptions.add(mMoviesRepository.videos(mMovie.getId()).subscribe(videos -> {
            Timber.d(String.format("Videos loaded, %d items.", videos.size()));
            Timber.d("Videos: " + videos);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onVideosLoaded(videos);
                }
            });

        }, throwable -> {
            Timber.e(throwable, "Videos loading failed.");
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onVideosLoaded(null);
                }
            });
        }));
    }

    private void onVideosLoaded(List<Video> videos) {
        mVideos = videos;

      try {

          for (int i = mVideosGroup.getChildCount() - 1; i >= 2; i--) {
              mVideosGroup.removeViewAt(i);
          }
          boolean hasVideos = false;
          final LayoutInflater inflater = LayoutInflater.from(getActivity());
          for (Video video : videos) {
              final View videoView = inflater.inflate(R.layout.item_video, mVideosGroup, false);
              final TextView videoNameView = findById(videoView, R.id.video_name);

              videoNameView.setText(video.getSite() + ": " + video.getName());
              videoView.setTag(video);
              videoView.setOnClickListener(v -> {
                  mHelper.playVideo((Video) v.getTag());
              });

              mVideosGroup.addView(videoView);
              hasVideos = true;
          }

          if (!Lists.isEmpty(videos)) {
              for (Video video : mVideos)
                  if (video.getType().equals(Video.TYPE_TRAILER)) {
                      Timber.d("Found trailer!");
                      mTrailer = video;

                      mCoverContainer.setTag(video);
                      mCoverContainer.setOnClickListener(view -> mHelper.playVideo((Video) view.getTag()));
                      break;
                  }

          }

          mCoverContainer.setClickable(mTrailer != null);
          mPosterPlayImage.setVisibility(mTrailer != null ? View.VISIBLE : View.GONE);
          mVideosGroup.setVisibility(hasVideos ? View.VISIBLE : View.GONE);
      }catch (Exception ex)
      {
          Timber.d("Video Loaded ex: " + ex.getMessage().toString());
        }


    }


    @Override
    protected List<Object> getModules() {
        return Collections.<Object>singletonList(new MoviesModule());
    }

    public static void expand(final TextView v) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setMaxLines(Integer.MAX_VALUE);
        //v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height =  LinearLayout.LayoutParams.WRAP_CONTENT;
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final TextView v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setMaxLines(3);
                }else{
                    v.setMaxLines(Integer.MAX_VALUE);
                    v.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }
}