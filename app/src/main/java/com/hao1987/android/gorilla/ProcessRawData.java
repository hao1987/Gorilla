package com.hao1987.android.gorilla;


import com.hao1987.android.gorilla.models.FilmItem;
import com.hao1987.android.gorilla.models.People;

import org.json.JSONObject;
import java.util.List;


public abstract class ProcessRawData {

    private String LOG_TAG = ProcessRawData.class.getSimpleName();

    private JSONObject mData;

    public ProcessRawData(JSONObject jsonObject) {
        this.mData = jsonObject;
    }

    public void reset() {
        this.mData = null;
    }

    public JSONObject getmData() {
        return mData;
    }

    public abstract void processResult();

    public abstract List<? extends FilmItem> getFilmItemProcessedData();
    public List<? extends People> getPeopleProcessedData(String type) {return null;};

}
