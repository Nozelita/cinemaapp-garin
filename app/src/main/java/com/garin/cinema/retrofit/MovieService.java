package com.garin.cinema.retrofit;
import com.garin.cinema.model.DetailModel;
import com.garin.cinema.model.MovieModel;
import com.garin.cinema.model.TrailerModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/popular")
    Call<MovieModel> getPopular(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("page") String page
    );

    @GET("movie/now_playing")
    Call<MovieModel> getNowPlaying(
            @Query("api_key") String key,
            @Query("language") String language,
            @Query("page") String page
    );

    @GET("movie/{movie_id}")
    Call<DetailModel> getDetail(
            @Path("movie_id") String movie_id,
            @Query("api_key") String key
    );

    @GET("movie/{id}/videos")
    Call<TrailerModel> getTrailer(
            @Path("id") String movie_id,
            @Query("api_key") String key
    );

}
