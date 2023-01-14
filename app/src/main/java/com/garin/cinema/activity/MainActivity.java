package com.garin.cinema.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garin.cinema.adapter.MainAdapter;
import com.garin.cinema.model.MovieModel;
import com.garin.cinema.retrofit.Constant;
import com.garin.cinema.retrofit.MovieService;
import com.garin.cinema.retrofit.RetrofitInstance;
import com.garin.cinema.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private RecyclerView recyclerView;
    private ProgressBar progressBar, progressBarNextPage;
    private NestedScrollView scrollView;

    private final MovieService service = RetrofitInstance.getUrl().create(MovieService.class);
    private MainAdapter adapter;
    private List<MovieModel.Results> movies = new ArrayList<>();

    private int currentPage = 1;
    private int totalPages  = 0;
    private boolean isScrolling  = false;
    private String movieCategory  = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupView();
        setupListener();
        setupRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (movieCategory.equals("")) {
            movieCategory = Constant.POPULAR;
            getMovie();
            showLoadingNextPage(false);
        }
    }

    private void setupView() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        progressBarNextPage = findViewById(R.id.progressBarNextPage);
        scrollView = findViewById(R.id.scrollView);
    }

    private void setupListener(){

        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                if (!isScrolling) {
                    if (currentPage <= totalPages) {
                        getMovieNextPage();
                    }

                }
            }
        });
    }

    private void setupRecyclerView(){

        adapter = new MainAdapter(this, movies, () -> startActivity(new Intent(MainActivity.this, DetailActivity.class)));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter( adapter );
    }

    private void getMovie() {

        scrollView.scrollTo(0, 0);
        currentPage = 1;
        showLoading( true );

        Call<MovieModel> call = null;
        switch (movieCategory) {
            case Constant.POPULAR:
                call = service.getPopular(Constant.KEY, Constant.LANGUAGE,
                        String.valueOf(currentPage) );
                break;
            case Constant.NOW_PLAYING:
                call = service.getNowPlaying(Constant.KEY, Constant.LANGUAGE,
                        String.valueOf(currentPage) );
                break;
        }

        assert call != null;
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieModel> call, @NonNull Response<MovieModel> response) {
                Log.d(TAG, response.toString());
                showLoading( false );
                if (response.isSuccessful()) {
                    MovieModel movie = response.body();
                    assert movie != null;
                    showMovie( movie );
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieModel> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
                showLoading( false );
            }
        });

    }

    private void getMovieNextPage() {

        currentPage += 1;
        showLoadingNextPage( true );

        Call<MovieModel> call = null;
        switch (movieCategory) {
            case Constant.POPULAR:
                call = service.getPopular(Constant.KEY, Constant.LANGUAGE,
                        String.valueOf(currentPage) );
                break;
            case Constant.NOW_PLAYING:
                call = service.getNowPlaying(Constant.KEY, Constant.LANGUAGE,
                        String.valueOf(currentPage) );
                break;
        }

        assert call != null;
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieModel> call, @NonNull Response<MovieModel> response) {
                Log.d(TAG, response.toString());
                showLoadingNextPage( false );
                if (response.isSuccessful()) {
                    MovieModel movie = response.body();
                    assert movie != null;
                    showMovieNextPage( movie );
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieModel> call, @NonNull Throwable t) {
                Log.d(TAG, t.toString());
                showLoadingNextPage( false );
            }
        });

    }

    private void showLoading(Boolean loading) {
        if (loading) {
            progressBar.setVisibility( View.VISIBLE );
        } else {
            progressBar.setVisibility( View.GONE );
        }
    }

    private void showLoadingNextPage(Boolean loading) {
        if (loading) {
            isScrolling = true;
            progressBarNextPage.setVisibility( View.VISIBLE );
        } else {
            isScrolling = false;
            progressBarNextPage.setVisibility( View.GONE );
        }
    }

    private void showMovie(MovieModel movie) {
        totalPages = movie.getTotal_pages();
        movies = movie.getResults();
        adapter.setData(movies);
    }

    private void showMovieNextPage(MovieModel movie) {
        totalPages = movie.getTotal_pages();
        movies = movie.getResults();
        adapter.setDataNextPage(movies);
        showMessage( "Page " + currentPage + " loaded");
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_popular) {

            Objects.requireNonNull(getSupportActionBar()).setTitle("Popular");
            movieCategory = Constant.POPULAR;
            getMovie();
            showMessage("Popular");
            return true;

        } else if (id == R.id.action_now_playing) {

            Objects.requireNonNull(getSupportActionBar()).setTitle("Now Playing");
            movieCategory = Constant.NOW_PLAYING;
            getMovie();
            showMessage("Now Playing");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
