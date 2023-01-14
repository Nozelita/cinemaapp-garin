package com.garin.cinema.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garin.cinema.activity.DetailActivity;
import com.garin.cinema.adapter.MainAdapter;
import com.garin.cinema.model.MovieModel;
import com.garin.cinema.retrofit.Constant;
import com.garin.cinema.retrofit.MovieService;
import com.garin.cinema.retrofit.RetrofitInstance;
import com.garin.cinema.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularFragment extends Fragment {
    private static final String TAG = "PopularFragment";

//    FragmentPop

    private ProgressBar progressBar, progressBarNextPage;
    private NestedScrollView scrollView;

    MovieService service = RetrofitInstance.getUrl().create(MovieService.class);
    private List<MovieModel.Results> movies = new ArrayList<>();
    private MainAdapter adapter;

    private int currentPage = 1;
    private int totalPages  = 0;
    private boolean isScrolling  = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_popular, container, false);

        setupView(view);
        setupListener();
        setupRecyclerView(view);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMoviePopular();
        showLoadingNextPage(false);
    }

    private void setupView(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        progressBarNextPage = view.findViewById(R.id.progressBarNextPage);
        scrollView = view.findViewById(R.id.scrollView);
    }

    private void setupListener(){
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                if (!isScrolling) {
                    if (currentPage <= totalPages) {
                        getMoviePopularNextPage();
                    }

                }
            }
        });
    }

    private void setupRecyclerView(View view){
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        adapter = new MainAdapter(getContext(), movies, () -> startActivity(new Intent(getContext(), DetailActivity.class)));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter( adapter );
    }

    private void getMoviePopular(){

        scrollView.scrollTo(0, 0);
        currentPage = 1;
        showLoading(true);

        Call<MovieModel> call = service.getPopular(Constant.KEY, Constant.LANGUAGE,
                String.valueOf(currentPage) );
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
                showLoading( false );
                Log.d(TAG, t.toString());
            }
        });
    }

    private void getMoviePopularNextPage(){

        currentPage += 1;
        showLoadingNextPage(true);

        Call<MovieModel> call = service.getPopular(Constant.KEY, Constant.LANGUAGE,
                String.valueOf(currentPage) );
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

    private void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
