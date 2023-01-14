package com.garin.cinema.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garin.cinema.activity.DetailActivity;
import com.garin.cinema.adapter.MainAdapter;
import com.garin.cinema.model.MovieModel;
import com.garin.cinema.retrofit.Constant;
import com.garin.cinema.retrofit.MovieService;
import com.garin.cinema.retrofit.RetrofitInstance;
import com.garin.cinema.databinding.FragmentNowPlayingBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {
    private static final String TAG = "PopularFragment";

    private FragmentNowPlayingBinding binding;

    MovieService service = RetrofitInstance.getUrl().create(MovieService.class);
    private List<MovieModel.Results> movies = new ArrayList<>();
    private MainAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_now_playing, container, false);
        binding = FragmentNowPlayingBinding.inflate(inflater, container, false);
        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        getMovieNowPlaying();
    }

    private void setupRecyclerView() {

        adapter = new MainAdapter(getContext(), movies, () -> startActivity(new Intent(getContext(), DetailActivity.class)));

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter( adapter );
    }

    private void getMovieNowPlaying(){

        showLoading(true);

        Call<MovieModel> call = service.getNowPlaying(Constant.KEY, Constant.LANGUAGE, "1");
        call.enqueue(new Callback<MovieModel>() {
            @Override
            public void onResponse(@NonNull Call<MovieModel> call, @NonNull Response<MovieModel> response) {
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

    private void showMovie(MovieModel movie) {
        movies = movie.getResults();
        adapter.setData(movies);
    }

    private void showLoading(Boolean loading) {
        if (loading) {
            binding.progressBar.setVisibility( View.VISIBLE );
        } else {
            binding.progressBar.setVisibility( View.GONE );
        }
    }
}
