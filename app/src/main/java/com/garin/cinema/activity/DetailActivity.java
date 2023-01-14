package com.garin.cinema.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.garin.cinema.R;
import com.garin.cinema.model.DetailModel;
import com.garin.cinema.retrofit.Constant;
import com.garin.cinema.retrofit.MovieService;
import com.garin.cinema.retrofit.RetrofitInstance;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {
    private final String TAG = "DetailActivity";

    private final MovieService service = RetrofitInstance.getUrl().create(MovieService.class);

    private ImageView imageView;
    private ProgressBar progressBar;
    private TextView txvTitle, txvVoteAverage, txvGenre, txvOverview;
    private FloatingActionButton fabTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setView();

        if (Constant.MOVIE_ID != null) {
            getMovieDetail( Constant.MOVIE_ID );
        } else {
            finish();
        }

    }

    private void setView () {

        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar);
        txvTitle = findViewById(R.id.txvTitle);
        txvVoteAverage = findViewById(R.id.txvVoteAverage);
        txvGenre = findViewById(R.id.txvGenre);
        txvOverview = findViewById(R.id.txvOverview);
        fabTrailer = findViewById(R.id.fabTrailer);

        fabTrailer.setOnClickListener(v -> startActivity(new Intent(DetailActivity.this, TrailerActivity.class)));

    }

    private void getMovieDetail(String movieId) {

        showLoading( true );

        Call<DetailModel> call = service.getDetail(movieId, Constant.KEY);
        call.enqueue(new Callback<DetailModel>() {

            @Override
            public void onResponse(@NonNull Call<DetailModel> call, @NonNull Response<DetailModel> response) {
                Log.e(TAG, response.toString());
                showLoading( false );
                if (response.isSuccessful()) {
                    DetailModel detailModel = response.body();
                    assert detailModel != null;
                    showDetail(detailModel);
                }
            }

            @Override
            public void onFailure(@NonNull Call<DetailModel> call, @NonNull Throwable t) {
                Log.e(TAG, t.toString());
                showLoading( false );
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void showDetail(DetailModel detail){

        txvTitle.setText( detail.getTitle() );
        txvVoteAverage.setText( detail.getVote_average().toString() );
        txvOverview.setText( detail.getOverview() );

        for (DetailModel.Genres genre: detail.getGenres() ) {
            txvGenre.setText( genre.getName() + " " );
        }

        Picasso.get()
                .load(Constant.BACKDROP_PATH + detail.getBackdrop_path() )
                .placeholder(R.drawable.placeholder_portrait)
                .error(R.drawable.placeholder_portrait)
                .fit().centerCrop()
                .into(imageView);
    }

    private void showLoading(Boolean loading) {
        if (loading) {
            progressBar.setVisibility( View.VISIBLE );
            fabTrailer.hide();
        } else {
            progressBar.setVisibility( View.GONE );
            fabTrailer.show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}
