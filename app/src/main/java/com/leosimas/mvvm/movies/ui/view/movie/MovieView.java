package com.leosimas.mvvm.movies.ui.view.movie;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.leosimas.mvvm.movies.GlideApp;
import com.leosimas.mvvm.movies.R;
import com.leosimas.mvvm.movies.bean.Genre;
import com.leosimas.mvvm.movies.bean.Movie;
import com.leosimas.mvvm.movies.ui.activity.movie.MovieDetailsActivity_;
import com.leosimas.mvvm.movies.util.FormatUtils;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EViewGroup(R.layout.view_movie)
public class MovieView extends RelativeLayout {

    @ViewById
    TextView textTitle, textOriginalTitle, textReleaseDate;

    @ViewById
    ChipGroup chipGroup;

    @ViewById
    ImageView imagePoster;

    @ViewById
    View rootView;

    private boolean isDetailsStyle = false;
    private Movie movie;

    public MovieView(Context context) {
        super(context);
    }

    public MovieView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MovieView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void bind(Movie movie) {
        this.movie = movie;

        GlideApp.with(this.getContext())
            .load( movie.getPosterFullUrl() )
            .placeholder(R.drawable.picture)
            .fallback(R.drawable.picture)
            .error(R.drawable.cloud_error)
            .fitCenter()
            .into(imagePoster);

        textTitle.setText(movie.getTitle());
        if ( movie.getTitle().equals( movie.getOriginalTitle() ) ) {
            textOriginalTitle.setVisibility(View.GONE);
        } else {
            textOriginalTitle.setText(movie.getOriginalTitle());
            textOriginalTitle.setVisibility(View.VISIBLE);
        }

        List<Genre> genreList = movie.getGenres();
        if ( genreList != null && !genreList.isEmpty() ) {
            for (Genre genre : genreList) {
                Chip chip = this.createChip();
                chip.setText( genre.getName() );
                chipGroup.addView( chip );
            }
        }

        String date = FormatUtils.formatDate(getContext(), movie.getReleaseDate());
        textReleaseDate.setText( date );
    }

    private Chip createChip() {
        Chip chip = new Chip(getContext());
        chip.setEnabled(false);
        chip.setChipBackgroundColorResource(R.color.gray_light);
        chip.setTextColor(getResources().getColor(android.R.color.black));
        return chip;
    }

    @Click
    void rootView() {
        if (isDetailsStyle) {
            return;
        }
        MovieDetailsActivity_.intent(this.getContext())
                .movie(movie)
                .start();
    }

    public void setDetailsStyle() {
        isDetailsStyle = true;
        textTitle.setTextAppearance(this.getContext(), R.style.TextAppearance_AppCompat_Large );
        textTitle.setTypeface(textTitle.getTypeface(), Typeface.BOLD);
        rootView.setBackgroundResource( android.R.color.transparent );
    }

}
