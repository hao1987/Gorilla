package com.hao1987.android.gorilla;

import android.util.Log;

import com.hao1987.android.gorilla.models.Cast;
import com.hao1987.android.gorilla.models.Crew;
import com.hao1987.android.gorilla.models.FilmItem;
import com.hao1987.android.gorilla.models.People;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProcessTVSeriesCreditsData extends ProcessRawData {

    private String LOG_TAG = ProcessTVSeriesCreditsData.class.getSimpleName();
    private List<People> mCastList;

    public ProcessTVSeriesCreditsData(JSONObject jsonObject) {
        super(jsonObject);
        mCastList = new ArrayList<>();
    }

    @Override
    public List<? extends FilmItem> getFilmItemProcessedData() {
        return null;
    }

    public List<? extends People> getPeopleProcessedData(String type) {
        if(type.equals("cast")) {return mCastList;}
        return null;
    }

    public void processResult() {

        final String M_CAST = "cast";

        final String M_CREDIT_ID = "credit_id";
        final String M_NAME = "name";
        final String M_PROFILE_PATH = "profile_path";
        final String M_CHARACTER = "character";

        try {
            JSONObject credits = getmData();

            JSONArray castObjs = credits.getJSONArray(M_CAST);
            for(int i=0; i<castObjs.length(); i++) {
                JSONObject castObj = castObjs.getJSONObject(i);
                String profile_path = castObj.getString(M_PROFILE_PATH);
                if(profile_path == "null" || profile_path.length() < 1) {continue;}

                String credit_id = castObj.getString(M_CREDIT_ID);
                String name = castObj.getString(M_NAME);
                String character = castObj.getString(M_CHARACTER);
                mCastList.add(new Cast(name, profile_path, credit_id, character));
            }

        } catch (JSONException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }
}
