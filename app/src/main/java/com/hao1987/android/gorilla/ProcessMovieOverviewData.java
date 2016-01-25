package com.hao1987.android.gorilla;

import android.util.Log;

import com.hao1987.android.gorilla.models.FilmItem;
import com.hao1987.android.gorilla.models.Movie;
import com.hao1987.android.gorilla.models.People;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProcessMovieOverviewData extends ProcessRawData {

    private String LOG_TAG = ProcessMovieOverviewData.class.getSimpleName();

    private List<Movie> mMovieOverviews;

    public ProcessMovieOverviewData(JSONObject jsonObject) {
        super(jsonObject);
        mMovieOverviews = new ArrayList<>();
    }

    @Override
    public List<Movie> getFilmItemProcessedData() {
        return mMovieOverviews;
    }


    public void processResult() {
        final String M_TOTAL_RESULTS = "total_results";

        final String M_RESULTS = "results";

        final String M_ID = "id";
        final String M_LANG = "original_language";
        final String M_TITLE = "title";
        final String M_POSTER = "poster_path";
        final String M_RELEASE_DATE = "release_date";

        final String M_POPULARITY = "popularity";
        final String M_VOTE_AVERAGE = "vote_average";
        final String M_OVERVIEW = "overview";

        try {
            JSONObject jsonObject = getmData();
            if (jsonObject.getInt(M_TOTAL_RESULTS) > 0) {
                JSONArray jsonArray = jsonObject.getJSONArray(M_RESULTS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject movie = jsonArray.getJSONObject(i);
                    long id = Long.valueOf(movie.getString(M_ID));
                    String lang = movie.getString(M_LANG);
                    String title = movie.getString(M_TITLE);
                    String poster = movie.getString(M_POSTER);
                    String release_date = movie.getString(M_RELEASE_DATE);

                    float popularity = Float.valueOf(movie.getString(M_POPULARITY));
                    int vote_average = movie.getInt(M_VOTE_AVERAGE);
                    String overview = movie.getString(M_OVERVIEW) == "null" ? "Not available" : movie.getString(M_OVERVIEW);

                    this.mMovieOverviews.add(new Movie(id, lang, title, poster, release_date, popularity, vote_average, overview));
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
