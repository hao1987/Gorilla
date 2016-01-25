package com.hao1987.android.gorilla.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hao1987.android.gorilla.ProcessMovieCreditsData;
import com.hao1987.android.gorilla.ProcessMovieDetailsData;
import com.hao1987.android.gorilla.ProcessMovieOverviewData;
import com.hao1987.android.gorilla.activities.MainActivity;
import com.hao1987.android.gorilla.ProcessRawData;
import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.adapters.MovieOverviewAdapter;
import com.hao1987.android.gorilla.helpers.APIManager;
import com.hao1987.android.gorilla.models.Cast;
import com.hao1987.android.gorilla.models.Movie;
import com.hao1987.android.gorilla.models.People;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MovieFragment extends Fragment {

    private String LOG_TAG = MovieFragment.class.getSimpleName();

    private Context mContext;
    protected String mTitle;
    private RecyclerView mRecyclerView;
    private MovieOverviewAdapter mMovieOverviewAdapter;
    private List<Movie> mMovieList = new ArrayList<>();
    private Handler mHandler;
    private int mPage = 1;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public static MovieFragment newInstance(CharSequence label) {
        MovieFragment f = new MovieFragment();

        Bundle b = new Bundle();
        b.putCharSequence("title", label);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, container, false);

        mTitle = getArguments() != null ? getArguments().getCharSequence("title").toString().toUpperCase() : "";


        mRecyclerView = (RecyclerView) view.findViewById(R.id.movieOverviewList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mMovieOverviewAdapter = new MovieOverviewAdapter(mContext, mMovieList, mRecyclerView);

        ((MainActivity) getActivity()).executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getMovieUpcommings(String.valueOf(mPage++)), null,
                responseListener(), ((MainActivity) getActivity()).errorListener()));

        mMovieOverviewAdapter.setOnLoadMoreListener(new MovieOverviewAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //add progress item
                mMovieList.add(null);
                mMovieOverviewAdapter.notifyItemInserted(mMovieList.size() - 1);

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //remove progress item
                        mMovieList.remove(mMovieList.size() - 1);
                        mMovieOverviewAdapter.notifyItemRemoved(mMovieList.size());

                        ((MainActivity) getActivity()).executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getMovieUpcommings(String.valueOf(mPage++)), null,
                                responseListener(), ((MainActivity) getActivity()).errorListener()));

                        mMovieOverviewAdapter.setLoaded();
                    }
                }, 2000);
            }
        });

        mRecyclerView.setAdapter(mMovieOverviewAdapter);

        return view;
    }

    private Response.Listener<JSONObject> responseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                final ProcessRawData handler = new ProcessMovieOverviewData(response);
                handler.processResult();
                final List<Movie> movieList = (List<Movie>) handler.getFilmItemProcessedData();

                mMovieOverviewAdapter.loadDataSet((List<Movie>) handler.getFilmItemProcessedData());

                try {
                    final JSONArray results = new JSONArray(response.getString("results"));
                    for(int i=0; i<results.length(); i++) {
                        final Movie m = movieList.get(i);
                        final JSONObject result = results.getJSONObject(i);
                        final int curr = i;
                        ((MainActivity)getActivity()).executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getMovieCredits(result.getString("id"), 1 + ""), null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        ProcessRawData creditsHandler = new ProcessMovieCreditsData(response);
                                        creditsHandler.processResult();

                                        m.setCast((List<People>) creditsHandler.getPeopleProcessedData("cast"));
                                        m.setCrew((List<People>) creditsHandler.getPeopleProcessedData("crew"));

                                        int offset = mMovieOverviewAdapter.getItemCount() - results.length();
                                        mMovieOverviewAdapter.updateDataSet(curr + offset, m);
                                    }
                                }, ((MainActivity) getActivity()).errorListener()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
    }
}
