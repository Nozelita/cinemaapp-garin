package com.garin.cinema.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.garin.cinema.adapter.TrailerAdapter;
import com.garin.cinema.model.TrailerModel;
import com.garin.cinema.retrofit.Constant;
import com.garin.cinema.retrofit.MovieService;
import com.garin.cinema.retrofit.RetrofitInstance;
import com.garin.cinema.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailerActivity extends AppCompatActivity {
    private final String TAG = "TrailerActivity";

    private ProgressBar progressBar;
    private YouTubePlayer youTubePlayer;
    private TrailerAdapter trailerAdapter;
    private final List<TrailerModel.Results> trailerModels = new ArrayList<>();
    private String cueKey = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trailer);

        progressBar = findViewById(R.id.progressBar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        trailerAdapter = new TrailerAdapter(this, trailerModels, new TrailerAdapter.OnAdapterListener() {
            @Override
            public void OnClick(String key) {
                if (youTubePlayer != null) {
                    youTubePlayer.loadVideo( key, 0 );
                }
            }

            @Override
            public void OnVideo(String key) {
                cueKey = key;
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter( trailerAdapter );

        getVideos();

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youTubePlayerView);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                youTubePlayer = player;
                if (cueKey != null) {
                    youTubePlayer.cueVideo( cueKey, 0);
                }
            }
        });

        Objects.requireNonNull(getSupportActionBar()).setTitle( Constant.MOVIE_TITLE );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void getVideos(){

        MovieService apiInterface = RetrofitInstance.getUrl().create(MovieService.class);
        Call<TrailerModel> call = apiInterface.getTrailer( String.valueOf(Constant.MOVIE_ID), Constant.KEY);
        call.enqueue(new Callback<TrailerModel>() {
            @Override
            public void onResponse(@NonNull Call<TrailerModel> call, @NonNull Response<TrailerModel> response) {
                Log.e(TAG, response.toString());
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    TrailerModel video = response.body();
                    assert video != null;
                    List<TrailerModel.Results> results = video.getResults();
                    trailerAdapter.setData( results );
                }

            }

            @Override
            public void onFailure(@NonNull Call<TrailerModel> call, @NonNull Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}
