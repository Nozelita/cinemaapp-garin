package com.garin.cinema.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieModel {

    @SerializedName("total_pages")
    private final Integer total_pages;

    @SerializedName("results")
    private final List<Results> results;

    public MovieModel(Integer total_pages, List<Results> results) {
        this.total_pages = total_pages;
        this.results = results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public List<Results> getResults() {
        return results;
    }

    public static class Results{

        @SerializedName("id")
        private int id;
        @SerializedName("title")
        private final String title;
        @SerializedName("backdrop_path")
        private final String backdrop_path;

        public Results(String title, String backdrop_path) {
            this.title = title;
            this.backdrop_path = backdrop_path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

    }
}
