package com.hao1987.android.gorilla.helpers;


import android.net.Uri;
import android.util.Log;

public class APIManager {

    private String LOG_TAG = APIManager.class.getSimpleName();

    private static final String API_KEY = "1419277c31b39f8ca591b8da5d77b5f8";
    private static final String TMDB_BASE = "http://api.themoviedb.org/3/";

    private static String createAndUpdateUri(String endpoint, String page) {
        String url = TMDB_BASE + endpoint;
        final String API_KEY_PARAM = "api_key";
        final String PAGE_PARAM = "page";

        Uri mTargetUri = Uri.parse(url).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        return mTargetUri.toString();
    }

    private static String createAndUpdateUri(String endpoint, String query, String page) {

        String url = TMDB_BASE + endpoint;
        final String API_KEY_PARAM = "api_key";
        final String QUERY_PARAM = "query";
        final String PAGE_PARAM = "page";

        Uri mTargetUri = Uri.parse(url).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, API_KEY)
                .appendQueryParameter(QUERY_PARAM, query)
                .appendQueryParameter(PAGE_PARAM, page)
                .build();

        return mTargetUri.toString();
    }

    public static String getMovieUpcommings(String page) {
        return createAndUpdateUri("movie/upcoming", page);
    }

    public static String getMovieDetail(String id, String page) {
        return createAndUpdateUri("movie/" + id, page);
    }

    public static String getMovieCredits(String id, String page) {
        return createAndUpdateUri("movie/" + id + "/credits", page);
    }

    public static String searchMovie(String criteria, String page) {
        return createAndUpdateUri("search/movie", criteria, page);
    }

    public static String searchTVSeries(String criteria, String page) {
        return createAndUpdateUri("search/tv", criteria, page);
    }

    public static String getTVSeriesAiringToday(String page) {
        return createAndUpdateUri("tv/airing_today", page);
    }

    public static String getTVSeriesDetail(String id, String page) {
        return createAndUpdateUri("tv/" + id, page);
    }

    public static String getTVSeriesCredits(String id, String page) {
        return createAndUpdateUri("tv/" + id + "/credits", page);
    }

    public static String getTVEpisode(String id, String season, String episode, String page) {
        return createAndUpdateUri("tv/" + id + "/season/" + season + "/episode/" + episode, page);
    }

}
