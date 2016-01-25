package com.hao1987.android.gorilla.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hao1987.android.gorilla.ProcessMovieCreditsData;
import com.hao1987.android.gorilla.ProcessMovieDetailsData;
import com.hao1987.android.gorilla.ProcessMovieOverviewData;
import com.hao1987.android.gorilla.ProcessRawData;
import com.hao1987.android.gorilla.ProcessTVSeriesOverviewData;
import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.RecyclerItemClickListener;
import com.hao1987.android.gorilla.adapters.SearchResultAdapter;
import com.hao1987.android.gorilla.helpers.APIManager;
import com.hao1987.android.gorilla.models.Expander;
import com.hao1987.android.gorilla.models.FilmItem;
import com.hao1987.android.gorilla.models.Movie;
import com.hao1987.android.gorilla.models.People;
import com.hao1987.android.gorilla.models.TVSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchActivity extends BaseActivity {
    private String LOG_TAG = SearchActivity.class.getSimpleName();

    private Context mContext;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private SearchResultAdapter mSearchResultAdapter;
    private List<FilmItem> mFilmItemList;
    private String mQueryHistory;
    private int mPage = 1;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mContext = this;
        mFilmItemList = new ArrayList<>();
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mQueryHistory =  sharedPref.getString(FILMITEM_QUERY, "");

        Log.d(LOG_TAG, mQueryHistory);

        if (mQueryHistory != null && mQueryHistory.length() > 0) {
            executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.searchMovie(mQueryHistory, mPage + ""), null,
                    searchMovieResponseListener(), errorListener()));

            executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.searchTVSeries(mQueryHistory, mPage + ""), null,
                    searchTVSeriesResponseListener(), errorListener()));
        }


        activateToolbarWithHomeEnabled();

        mRecyclerView = (RecyclerView) findViewById(R.id.searchOverviewList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSearchResultAdapter = new SearchResultAdapter(this, new ArrayList<FilmItem>());
        mRecyclerView.setAdapter(mSearchResultAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, mRecyclerView, new RecyclerItemClickListener.OnItemClickLister() {

            @Override
            public void onItemClick(View view, int position) {
                FilmItem item = mSearchResultAdapter.getFilmItemOverview(position);

                if(item instanceof Movie) {
                    mSearchResultAdapter.setSelected(position);

                    Intent intent = new Intent(mContext, MovieDetailsActivity.class);
                    intent.putExtra(FILMITEM_MOVIE, mSearchResultAdapter.getFilmItemOverview(position));
                    mContext.startActivity(intent);

                } else if(item instanceof TVSeries) {

                    mSearchResultAdapter.setSelected(position);

                    Intent intent = new Intent(mContext, TVSeriesDetailsActivity.class);
                    intent.putExtra(MainActivity.FILMITEM_ID, ((TVSeries) mSearchResultAdapter.getFilmItemOverview(position)).getId());
                    mContext.startActivity(intent);

                } else if(item instanceof Expander) {
                    int mSuggestThrehold = mSearchResultAdapter.getSuggestThrehold();
                    List<FilmItem> mFilmItemsList = mSearchResultAdapter.getFilmItemsList();
                    List<FilmItem> mMoviesList = mSearchResultAdapter.getMoviesList();
                    List<FilmItem> mTVSeriesList = mSearchResultAdapter.getTVSeriesList();

                    if(item.getType().equals("movie")) {
                        mFilmItemsList.remove(mSuggestThrehold + 1);
                        mSearchResultAdapter.notifyItemRemoved(mSuggestThrehold + 1);
                        mFilmItemsList.addAll(mSuggestThrehold + 1, mMoviesList.subList(mSuggestThrehold, mMoviesList.size()));
                        mSearchResultAdapter.notifyItemRangeInserted(mSuggestThrehold + 1, mMoviesList.size() - mSuggestThrehold);

                    } else if(item.getType().equals("tvseries")) {
                        int total = mFilmItemsList.size();
                        mFilmItemsList.remove(total - 1);
                        mSearchResultAdapter.notifyItemRemoved(total - 1);
                        mFilmItemsList.addAll(total - 1, mTVSeriesList.subList(mSuggestThrehold, mTVSeriesList.size()));
                        mSearchResultAdapter.notifyItemRangeInserted(total, mTVSeriesList.size() - mSuggestThrehold);
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {}
        }));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);

        final MenuItem menuItem = menu.findItem(R.id.search_view);
        mSearchView = (SearchView) menuItem.getActionView();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        mSearchView.setIconified(false);
        if(mQueryHistory != null) {
            mSearchView.setQuery(mQueryHistory, false);
        }

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchView.clearFocus();

                if(!query.equals(mQueryHistory)) {
                    sharedPref.edit().putString(FILMITEM_QUERY, query).commit();

                    mSearchResultAdapter.clearDataSet();

                    executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.searchMovie(query, mPage + ""), null,
                            searchMovieResponseListener(), errorListener()));

                    executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.searchTVSeries(query, mPage + ""), null,
                            searchTVSeriesResponseListener(), errorListener()));
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String query) { return false; }
        });

        return true;
    }


    private Response.Listener<JSONObject> searchMovieResponseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                final ProcessRawData handler = new ProcessMovieOverviewData(response);
                handler.processResult();
                final List<FilmItem> movieList = (List<FilmItem>) handler.getFilmItemProcessedData();

                mSearchResultAdapter.mergeDataSet(movieList, "movie");

                try {
                    if(response.getInt("total_results") > 0) {

                        final JSONArray results = new JSONArray(response.getString("results"));
                        for (int i = 0; i < results.length(); i++) {
                            final Movie m = (Movie) movieList.get(i);
                            final JSONObject result = results.getJSONObject(i);
                            final int curr = i;
                            executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getMovieCredits(result.getString("id"), 1 + ""), null,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            ProcessRawData creditsHandler = new ProcessMovieCreditsData(response);
                                            creditsHandler.processResult();

                                            m.setCast((List<People>) creditsHandler.getPeopleProcessedData("cast"));
                                            m.setCrew((List<People>) creditsHandler.getPeopleProcessedData("crew"));

                                            mSearchResultAdapter.updateDataSet(curr, m);
                                        }
                                    }, errorListener()));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.Listener<JSONObject> searchTVSeriesResponseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                ProcessRawData processRawData = new ProcessTVSeriesOverviewData(response);
                processRawData.processResult();
                mSearchResultAdapter.mergeDataSet((List<FilmItem>) processRawData.getFilmItemProcessedData(), "tvseries");
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
