package com.hao1987.android.gorilla;

import android.util.Log;

import com.hao1987.android.gorilla.models.TVSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProcessTVSeriesOverviewData extends ProcessRawData {

    private String LOG_TAG = ProcessTVSeriesOverviewData.class.getSimpleName();

    private List<TVSeries> mTVSeriesList;

    public ProcessTVSeriesOverviewData(JSONObject jsonObject) {
        super(jsonObject);
        mTVSeriesList = new ArrayList<>();
    }

    @Override
    public List<TVSeries> getFilmItemProcessedData() {
        return mTVSeriesList;
    }

    public void processResult() {
        final String M_TOTAL_RESULTS = "total_results";

        final String M_RESULTS = "results";

        final String M_ID = "id";
        final String M_LANG = "original_language";
        final String M_NAME = "name";
        final String M_POSTER = "poster_path";
        final String M_FIRST_AIR_DATE = "first_air_date";
        final String M_POPULARITY = "popularity";
        final String M_VOTE_AVERAGE = "vote_average";
        final String M_OVERVIEW = "overview";

        try {
            JSONObject jsonObject = getmData();
            if (jsonObject.getInt(M_TOTAL_RESULTS) > 0) {
                JSONArray jsonArray = jsonObject.getJSONArray(M_RESULTS);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject tvseries = jsonArray.getJSONObject(i);

                    long id = Long.valueOf(tvseries.getString(M_ID));
                    String lang = tvseries.getString(M_LANG);
                    String name = tvseries.getString(M_NAME);
                    String poster = tvseries.getString(M_POSTER);
                    String first_air_date = tvseries.getString(M_FIRST_AIR_DATE);

                    float popularity = Float.valueOf(tvseries.getString(M_POPULARITY));
                    int vote_average = tvseries.getInt(M_VOTE_AVERAGE);
                    String overview = tvseries.getString(M_OVERVIEW) == "null" ? "Not available" : tvseries.getString(M_OVERVIEW);

                    this.mTVSeriesList.add(new TVSeries(id, lang, name, poster, first_air_date, popularity, vote_average, overview));
                }
            }
        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
