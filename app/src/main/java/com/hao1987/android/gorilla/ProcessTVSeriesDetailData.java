package com.hao1987.android.gorilla;

import android.util.Log;

import com.hao1987.android.gorilla.models.People;
import com.hao1987.android.gorilla.models.TVSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ProcessTVSeriesDetailData extends ProcessRawData {

    private String LOG_TAG = ProcessTVSeriesDetailData.class.getSimpleName();
    private List<TVSeries> mTVSeriesDetail;

    public ProcessTVSeriesDetailData(JSONObject jsonObject) {
        super(jsonObject);
        mTVSeriesDetail = new ArrayList<>();
    }

    @Override
    public List<TVSeries> getFilmItemProcessedData() {
        return mTVSeriesDetail;
    }

    public void processResult() {

        final String M_GENRES = "genres";

        final String M_ID = "id";
        final String M_LANG = "original_language";
        final String M_NAME = "name";
        final String M_POSTER = "poster_path";
        final String M_FIRST_AIR_DATE = "first_air_date";
        final String M_NETWORKS = "networks";

        final String M_POPULARITY = "popularity";
        final String M_VOTE_AVERAGE = "vote_average";
        final String M_OVERVIEW = "overview";

        final String M_SEASONS = "seasons";
        final String M_SEASON_NUMBER = "season_number";
        final String M_EPISODE_COUNT = "episode_count";


        try {
            JSONObject tvseries = getmData();

            JSONArray genresArr = tvseries.getJSONArray(M_GENRES);
            String[] genres = new String[genresArr.length()];
            for (int i = 0; i < genresArr.length(); i++) {
                JSONObject genresObj = genresArr.getJSONObject(i);
                String name = genresObj.getString(M_NAME);
                genres[i] = name;
            }

            JSONArray networksArr = tvseries.getJSONArray(M_NETWORKS);
            String[] networks = new String[networksArr.length()];
            for (int i = 0; i < networksArr.length(); i++) {
                JSONObject networkObj = networksArr.getJSONObject(i);
                String name = networkObj.getString(M_NAME);
                networks[i] = name;
            }

            long id = Long.valueOf(tvseries.getString(M_ID));
            String lang = tvseries.getString(M_LANG);
            String name = tvseries.getString(M_NAME);
            String poster = tvseries.getString(M_POSTER);
            String fist_air_date = tvseries.getString(M_FIRST_AIR_DATE);

            float popularity = Float.valueOf(tvseries.getString(M_POPULARITY));
            int vote_average = tvseries.getInt(M_VOTE_AVERAGE);
            String overview = tvseries.getString(M_OVERVIEW) == "null" ? "Not available" : tvseries.getString(M_OVERVIEW);

            JSONArray seasonArr = tvseries.getJSONArray(M_SEASONS);
            List<Integer> seasonInfo = new ArrayList<>();
            List<Integer> episodeInfo = new ArrayList<>();

            for (int i = 0; i < seasonArr.length(); i++) {
                JSONObject seasonObj = seasonArr.getJSONObject(i);
                seasonInfo.add(Integer.valueOf(seasonObj.getString(M_SEASON_NUMBER)));
                episodeInfo.add(Integer.valueOf(seasonObj.getString(M_EPISODE_COUNT)));
            }

            mTVSeriesDetail.add(new TVSeries(genres, id, lang, name, poster, fist_air_date, networks, seasonInfo, episodeInfo, popularity, vote_average, overview));

        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

    }
}
