package com.leosimas.mvvm.movies.ui.activity.movie;

import android.annotation.SuppressLint;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.leosimas.mvvm.movies.GlideApp;
import com.leosimas.mvvm.movies.R;
import com.leosimas.mvvm.movies.bean.Movie;
import com.leosimas.mvvm.movies.service.MoviesAPI;
import com.leosimas.mvvm.movies.ui.view.movie.MovieView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("Registered")
@EActivity(R.layout.activity_movie_details)
public class MovieDetailsActivity extends AppCompatActivity {

    @Extra
    Movie movie;

    @ViewById
    TextView textOverview;

    @ViewById
    ImageView imageBackdrop;

    @ViewById
    MovieView moviewView;

    @AfterViews
    protected void init() {
        String title =  movie.getTitle();
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setTitle( title );
        }

        moviewView.setDetailsStyle();
        moviewView.bind(movie);

        textOverview.setText(movie.getOverview());

        String backdropUrl = MoviesAPI.getBackdropFullUrl(movie);
        if ( backdropUrl != null ) {
            GlideApp.with(this)
                .load( backdropUrl )
                .placeholder(R.drawable.picture)
                .error(R.drawable.cloud_error)
                .centerCrop()
                .into(imageBackdrop);
        } else {
            imageBackdrop.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
