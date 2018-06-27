package com.ram.my.movies.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.glidepalette.GlidePalette;
import com.ram.my.movies.R;
import com.ram.my.movies.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindColor;
import butterknife.ButterKnife;

public final class MoviesAdapter extends EndlessAdapter<Movie, MoviesAdapter.MovieHolder> {

    public interface OnMovieClickListener {
        void onContentClicked(@NonNull final Movie movie, View view, int position);

        void onFavoredClicked(@NonNull final Movie movie, int position);

        OnMovieClickListener DUMMY = new OnMovieClickListener() {
            @Override public void onContentClicked(@NonNull Movie movie, View view, int position) {}

            @Override public void onFavoredClicked(@NonNull Movie movie, int position) { }
        };
    }

    @NonNull private final Fragment mFragment;
    @NonNull private OnMovieClickListener mListener = OnMovieClickListener.DUMMY;

    public MoviesAdapter(@NonNull Fragment fragment, List<Movie> movies) {
        super(fragment.getActivity(), movies == null ? new ArrayList<>() : movies);
        mFragment = fragment;
        setHasStableIds(true);
    }

    public void setListener(@NonNull OnMovieClickListener listener) {
        this.mListener = listener;
    }

    @Override
    public long getItemId(int position) {
        return (!isLoadMore(position)) ? mItems.get(position).getId() : -1;
    }

    @Override
    protected MovieHolder onCreateItemHolder(ViewGroup parent, int viewType) {
        return new MovieHolder(mInflater.inflate(R.layout.item_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_ITEM) {
            ((MovieHolder) holder).bind(mItems.get(position));
        }
    }

    final class MovieHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.movie_item_container) View mContentContainer;
        @Bind(R.id.movie_item_image) ImageView mImageView;
        @Bind(R.id.movie_item_title) TextView mTitleView;
        @Bind(R.id.movie_item_genres) TextView mGenresView;
        @Bind(R.id.movie_item_footer) View mFooterView;

        @BindColor(R.color.transparent_footer) int mColorBackground;
        @BindColor(R.color.body_text_white) int mColorTitle;
        @BindColor(R.color.body_text_1_inverse) int mColorSubtitle;

        private final StringBuilder mBuilder = new StringBuilder(30);
        private long mMovieId;

        public MovieHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bind(@NonNull final Movie movie) {
            mContentContainer.setOnClickListener(view -> mListener.onContentClicked(movie, view, getAdapterPosition()));



            mTitleView.setText(movie.getTitle());
            mGenresView.setText(String.valueOf(movie.getVoteAverage())+"/10");

            if (mMovieId != movie.getId()) {
                resetColors();
                mMovieId = movie.getId();
            }

            Glide.with(mFragment)
                    .load(movie.getPosterPath())
                    .crossFade()
                    .placeholder(R.color.movie_poster_placeholder)
                    .listener(GlidePalette.with(movie.getPosterPath())
                            .intoCallBack(palette -> applyColors(palette.getVibrantSwatch())))
                    .into(mImageView);
        }

        private void resetColors() {
            mFooterView.setBackgroundColor(mColorBackground);
            mTitleView.setTextColor(mColorTitle);
            mGenresView.setTextColor(mColorSubtitle);

        }

        private void applyColors(Palette.Swatch swatch) {
            if (swatch != null) {
                mFooterView.setBackgroundColor(swatch.getRgb());
                mTitleView.setTextColor(swatch.getBodyTextColor());
                mGenresView.setTextColor(swatch.getTitleTextColor());

            }
        }
    }
}