package com.garin.cinema.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerModel {

    @SerializedName("results")
    private final List<Results> results;

    public TrailerModel(List<Results> results) {
        this.results = results;
    }

    public List<Results> getResults() {
        return results;
    }

    public static class Results{
        @SerializedName("key")
        private final String key;
        @SerializedName("name")
        private final String name;

        public Results(String key, String name) {
            this.key = key;
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public String getName() {
            return name;
        }

    }

}
