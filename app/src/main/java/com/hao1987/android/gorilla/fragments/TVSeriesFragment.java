package com.hao1987.android.gorilla.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.hao1987.android.gorilla.ProcessTVSeriesOverviewData;
import com.hao1987.android.gorilla.RecyclerItemClickListener;
import com.hao1987.android.gorilla.activities.MainActivity;
import com.hao1987.android.gorilla.ProcessRawData;
import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.activities.TVSeriesDetailsActivity;
import com.hao1987.android.gorilla.adapters.TVShowOverviewAdapter;
import com.hao1987.android.gorilla.helpers.APIManager;
import com.hao1987.android.gorilla.models.TVSeries;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TVSeriesFragment extends Fragment{

    private String LOG_TAG = TVSeriesFragment.class.getSimpleName();

    private Context mContext;
    protected String mTitle;
    private RecyclerView mRecyclerView;
    private TVShowOverviewAdapter mTVShowOverviewAdapter;

    private int mPage = 1;

    public static TVSeriesFragment newInstance(CharSequence label) {
        TVSeriesFragment f = new TVSeriesFragment();

        Bundle b = new Bundle();
        b.putCharSequence("title", label);
        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tvshow, container, false);

        mTitle = getArguments() != null ? getArguments().getCharSequence("title").toString().toUpperCase() : "";

        mRecyclerView = (RecyclerView) view.findViewById(R.id.tvshowOverviewList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        mTVShowOverviewAdapter = new TVShowOverviewAdapter(mContext, new ArrayList<TVSeries>());

        ((MainActivity) getActivity()).executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getTVSeriesAiringToday(String.valueOf(mPage++)), null,
                responseListener(), ((MainActivity) getActivity()).errorListener()));

        mRecyclerView.setAdapter(mTVShowOverviewAdapter);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(mContext, mRecyclerView, new RecyclerItemClickListener.OnItemClickLister() {

            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, TVSeriesDetailsActivity.class);
                intent.putExtra(MainActivity.FILMITEM_ID, mTVShowOverviewAdapter.getTVShowOverview(position).getId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
            }
        }));


        return view;
    }

    private Response.Listener<JSONObject> responseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, response.toString());
                ProcessRawData processRawData = new ProcessTVSeriesOverviewData(response);
                processRawData.processResult();
                mTVShowOverviewAdapter.loadDataSet((List<TVSeries>) processRawData.getFilmItemProcessedData());
            }
        };
    }
}
