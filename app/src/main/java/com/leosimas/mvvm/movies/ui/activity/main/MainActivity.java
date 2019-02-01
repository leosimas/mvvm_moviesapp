package com.leosimas.mvvm.movies.ui.activity.main;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.leosimas.mvvm.movies.R;
import com.leosimas.mvvm.movies.bean.ViewState;
import com.leosimas.mvvm.movies.bean.Movie;
import com.leosimas.mvvm.movies.ui.adapter.MoviesViewAdapter;
import com.leosimas.mvvm.movies.ui.view.InfiniteRecyclerView;
import com.leosimas.mvvm.movies.util.AppUtils;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

@SuppressLint("Registered")
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TESTE";

    @ViewById
    protected InfiniteRecyclerView recyclerView;

    @ViewById
    protected AppBarLayout appBarLayout;

    @ViewById
    protected Toolbar toolbar;

    @ViewById
    protected EditText editSearch;

    @ViewById
    protected View textHeader, buttonSearch;

    private MainViewModel mainViewModel;

    @AfterViews
    protected void init() {
        initLayout();
        initViewModel();
    }

    private void initLayout() {
        setSupportActionBar(toolbar);

        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    buttonSearch();
                    return true;
                }
                return false;
            }
        });

        recyclerView.initInfiniteRecyclerView(this, new MoviesViewAdapter(),
                new InfiniteRecyclerView.InfiniteScrollListener() {
            @Override
            public void loadNextPage() {
                mainViewModel.requestMovies();
            }

            @Override
            public boolean hasMorePages() {
                return mainViewModel.hasMorePages();
            }
        });
    }

    private void initViewModel() {
        Log.d(TAG, "initViewModel");
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                Log.d(TAG, "observe movies: " + movies);
                recyclerView.showList(movies);
            }
        });
        mainViewModel.getViewState().observe(this, new Observer<ViewState>() {
            @Override
            public void onChanged(ViewState viewState) {
                Log.d(TAG, "observe viewState: " + viewState);
                if (viewState.isError()) {
                    recyclerView.showError();
                } else if ( viewState.isLoading() ) {
                    recyclerView.showLoading();
                }
            }
        });
        mainViewModel.getSearchState().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSearchState) {
                Log.d(TAG, "observe search: " + isSearchState);
                changeSearchMode(isSearchState);
            }
        });
        mainViewModel.getToastMessage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer resString) {
                Log.d(TAG, "observe toast: " + resString);
                if (resString != null) {
                    AppUtils.showToastShort(MainActivity.this, resString);
                }
            }
        });
    }

    private void changeSearchMode(Boolean isSearchState) {
        if (isSearchState) {
            appBarLayout.setExpanded(true, true);
            textHeader.setVisibility(View.INVISIBLE);
            editSearch.setVisibility(View.VISIBLE);
            buttonSearch.setVisibility(View.VISIBLE);
            editSearch.setText("");
            AppUtils.showKeyboard(this, editSearch);
        } else {
            textHeader.setVisibility(View.VISIBLE);
            editSearch.setVisibility(View.INVISIBLE);
            buttonSearch.setVisibility(View.INVISIBLE);
            AppUtils.hideKeyboard(this, editSearch);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(isSearchState);

        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ( id == R.id.search ) {
            mainViewModel.setSearchMode(true);
        } else if ( id == android.R.id.home) {
            this.doBack();
        }
        return true;
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem searchItem = menu.getItem(0);
        searchItem.setVisible( !mainViewModel.getSearchState().getValue() );
        return super.onPrepareOptionsMenu(menu);
    }

    private void doBack() {
        if ( mainViewModel.getSearchState().getValue() ) {
            mainViewModel.setSearchMode(false);
            return;
        }

        this.finish();
    }

    @Override
    public void onBackPressed() {
        this.doBack();
    }

    @Click
    void buttonSearch() {
        mainViewModel.searchMovies(editSearch.getText().toString());
        AppUtils.hideKeyboard(this, editSearch);
    }

}
