package com.garin.cinema.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailModel {

    @SerializedName("id")
    private Integer id;
    @SerializedName("title")
    private final String title;
    @SerializedName("backdrop_path")
    private final String backdrop_path;
    @SerializedName("overview")
    private final String overview;
    @SerializedName("vote_average")
    private final Double vote_average;
    @SerializedName("genres")
    private final List<Genres> genres;

    public DetailModel(String title, String backdrop_path, String overview, Double vote_average, List<Genres> genres) {
        this.title = title;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.vote_average = vote_average;
        this.genres = genres;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVote_average() {
        return vote_average;
    }

    public List<Genres> getGenres() {
        return genres;
    }

    public static class Genres {
        @SerializedName("id")
        private Long id;
        @SerializedName("name")
        private final String name;

        public Genres(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

    }
}
