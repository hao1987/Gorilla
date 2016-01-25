package com.hao1987.android.gorilla;

import android.util.Log;

import com.hao1987.android.gorilla.models.Cast;
import com.hao1987.android.gorilla.models.Crew;
import com.hao1987.android.gorilla.models.FilmItem;
import com.hao1987.android.gorilla.models.Movie;
import com.hao1987.android.gorilla.models.People;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProcessMovieCreditsData extends ProcessRawData {

    private String LOG_TAG = ProcessMovieCreditsData.class.getSimpleName();
    private List<People> mCastList;
    private List<People> mCrewList;

    public ProcessMovieCreditsData(JSONObject jsonObject) {
        super(jsonObject);
        mCastList = new ArrayList<>();
        mCrewList = new ArrayList<>();
    }

    @Override
    public List<? extends FilmItem> getFilmItemProcessedData() {
        return null;
    }

    public List<? extends People> getPeopleProcessedData(String type) {
        if(type.equals("cast")) {return mCastList;}
        return mCrewList;
    }

    public void processResult() {

        final String M_CAST = "cast";

        final String M_CREDIT_ID = "credit_id";
        final String M_NAME = "name";
        final String M_PROFILE_PATH = "profile_path";
        final String M_CHARACTER = "character";

        final String M_CREW = "crew";
        final String M_JOB = "job";

        try {
            JSONObject credits = getmData();

            JSONArray castObjs = credits.getJSONArray(M_CAST);
            for(int i=0; i<castObjs.length(); i++) {
                JSONObject castObj = castObjs.getJSONObject(i);
                String profile_path = castObj.getString(M_PROFILE_PATH);
                String character = castObj.getString(M_CHARACTER);
                if(profile_path == "null" || character == null || character.length() < 1) {continue;}

                String credit_id = castObj.getString(M_CREDIT_ID);
                String name = castObj.getString(M_NAME);
                mCastList.add(new Cast(name, profile_path, credit_id, character));
            }

            JSONArray crewObjs = credits.getJSONArray(M_CREW);
            for(int i=0; i<crewObjs.length(); i++) {
                JSONObject crewObj = crewObjs.getJSONObject(i);
                String job = crewObj.getString(M_JOB);
                if(!job.toLowerCase().equals("director")) {continue;}

                String profile_path = crewObj.getString(M_PROFILE_PATH);
                String credit_id = crewObj.getString(M_CREDIT_ID);
                String name = crewObj.getString(M_NAME);
                mCrewList.add(new Crew(name, profile_path, credit_id, job));
            }

        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
