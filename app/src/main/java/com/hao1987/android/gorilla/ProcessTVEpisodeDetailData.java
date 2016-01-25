//package com.hao1987.android.gorilla;
//
//import android.util.Log;
//
//import com.hao1987.android.gorilla.models.People;
//import com.hao1987.android.gorilla.models.TVEpisode;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ProcessTVEpisodeDetailData extends ProcessRawData {
//
//    private String LOG_TAG = ProcessTVEpisodeDetailData.class.getSimpleName();
//    private List<TVEpisode> mTVEpisodeDetail;
//
//    public ProcessTVEpisodeDetailData(JSONObject jsonObject) {
//        super(jsonObject);
//        mTVEpisodeDetail = new ArrayList<>();
//    }
//
//    @Override
//    public List<TVEpisode> getFilmItemProcessedData() {
//        return mTVEpisodeDetail;
//    }
//
//
//    public void processResult() {
//
//        final String M_GENRES = "genres";
//
//        final String M_ID = "id";
//        final String M_NAME = "name";
//
//        final String M_LANG = "original_language";
//        final String M_TITLE = "title";
//        final String M_POSTER = "poster_path";
//        final String M_RELEASE_DATE = "release_date";
//        final String M_VOTE_AVERAGE = "vote_average";
//        final String M_RUNTIME = "runtime";
//        final String M_OVERVIEW = "overview";
//
//        try {
//            JSONObject movie = getmData();
//
//            JSONArray genresObjs = movie.getJSONArray(M_GENRES);
//            String[] genres = new String[genresObjs.length()];
//            for(int i=0; i<genresObjs.length(); i++) {
//                JSONObject genresObj = genresObjs.getJSONObject(i);
////                int id = genresObj.getInt(M_ID);
//                String name = genresObj.getString(M_NAME);
//                genres[i] = name;
//            }
//
//            long id = Long.valueOf(movie.getString(M_ID));
//            String lang = movie.getString(M_LANG);
//            String title = movie.getString(M_TITLE);
//            String poster = movie.getString(M_POSTER);
//            String release_date = movie.getString(M_RELEASE_DATE);
//
//            // have to do this to compare
//            int vote_average = movie.getString(M_VOTE_AVERAGE) == "null" ? 0 : movie.getInt(M_VOTE_AVERAGE);
//            int runtime = movie.getString(M_RUNTIME) == "null" ? 0 : movie.getInt(M_RUNTIME);
//            String overview = movie.getString(M_OVERVIEW) == "null" ? "Not available" : movie.getString(M_OVERVIEW);
//
////            mEpisodeDetail = new TVEpisode(genres, id, lang, title, poster, release_date, vote_average, runtime, overview);
//
//        } catch (JSONException e) {
//            Log.d(LOG_TAG, e.getMessage());
//        }
//
//    }
//
//}
