package com.hao1987.android.gorilla.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hao1987.android.gorilla.ProcessMovieCreditsData;
import com.hao1987.android.gorilla.ProcessMovieDetailsData;
import com.hao1987.android.gorilla.ProcessRawData;
import com.hao1987.android.gorilla.ProcessTVSeriesCreditsData;
import com.hao1987.android.gorilla.ProcessTVSeriesDetailData;
import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.helpers.APIManager;
import com.hao1987.android.gorilla.models.Cast;
import com.hao1987.android.gorilla.models.Movie;
import com.hao1987.android.gorilla.models.People;
import com.hao1987.android.gorilla.models.TVSeries;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TVSeriesDetailsActivity extends BaseActivity {

    private String LOG_TAG = TVSeriesDetailsActivity.class.getSimpleName();

    private Context mContext;

    private ImageView poster;
    private ImageView rateIcon;

    private TextView title;
    private TextView popularity;
    private TextView voteAverage;
    private TextView firstAirDate;
    private TextView language;
    private TextView networks;
    private TextView overview;
    private TextView genres;
    private TextView todaysEpisode;

    private LinearLayout creditsLayout;

    TVSeries tvseries;
    List<People> cast;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_tvseries_detail);
        activateToolbarWithHomeEnabled();

        mContext = this;

        Intent intent = getIntent();
        long mId = intent.getLongExtra(FILMITEM_ID, 0);
        if (mId > 0) {
            poster = (ImageView) findViewById(R.id.mPoster);
            rateIcon = (ImageView) findViewById(R.id.mRateIcon);

            title = (TextView) findViewById(R.id.mTitle);
            popularity = (TextView) findViewById(R.id.mPopularity);
            voteAverage = (TextView) findViewById(R.id.mVoteAverage);
            firstAirDate = (TextView) findViewById(R.id.mFirstAirDate);
            language = (TextView) findViewById(R.id.mLanguage);
            networks = (TextView) findViewById(R.id.mNetworks);
            overview = (TextView) findViewById(R.id.mOverview);
            genres = (TextView) findViewById(R.id.mGenres);
            todaysEpisode = (TextView) findViewById(R.id.mTodaysEpisode);
            todaysEpisode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<Integer> seasonsInfo = tvseries.getSeasons();
                    List<Integer> episodesInfo = tvseries.getEpisodes();

                    executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getTVEpisode(tvseries.getId() + "", seasonsInfo.get(seasonsInfo.size() - 1) + "", episodesInfo.get(episodesInfo.size() - 1) + "", 1 + ""), null,
                            new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        new AlertDialog.Builder(mContext)
                                                .setTitle(response.getString("name"))
                                                .setMessage(response.getString("overview"))
                                                .setCancelable(false)
                                                .setPositiveButton(R.string.close, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                })
                                                .show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, errorListener()));
                }
            });

            cast = new ArrayList<>();

            executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getTVSeriesDetail(mId + "", 1+""), null,
                    responseListener(), errorListener()));
        }
    }


    private Response.Listener<JSONObject> responseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                final ProcessRawData handler = new ProcessTVSeriesDetailData(response);
                handler.processResult();

                final TVSeries tvseries = (TVSeries) handler.getFilmItemProcessedData().get(0);

                try {
                    executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getTVSeriesCredits(response.getString("id"), 1 + ""), null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    ProcessRawData creditsHandler = new ProcessTVSeriesCreditsData(response);
                                    creditsHandler.processResult();

                                    tvseries.setCast((List<People>) creditsHandler.getPeopleProcessedData("cast"));

                                    renderDetails((List<TVSeries>) handler.getFilmItemProcessedData());

                                }
                            }, errorListener()));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        };
    }

    public void renderDetails(List<TVSeries> mTVSeriesDetail) {
        tvseries = mTVSeriesDetail.get(0);

        cast.addAll(tvseries.getCast());
        creditsLayout = (LinearLayout) findViewById(R.id.mCreditsLayout);

        for(People p : cast) {
            Cast c = (Cast) p;
            View view = getLayoutInflater().inflate(R.layout.list_tvseries_credit, null);
            ImageView thumbnail = (ImageView) view.findViewById(R.id.creditThumbnail);
            TextView character = (TextView) view.findViewById(R.id.character);

            if(c.getpProfilePath() != null && c.getpProfilePath().length() > 0) {
                Picasso.with(this)
                        .load(c.getImage(1))
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar)
                        .into(thumbnail);
            }

            character.setText(c.getcCharacter());

            creditsLayout.addView(view);
        }

        title.setText(tvseries.getTitle().length() > 0 ? tvseries.getTitle() : "Not Available");

        Picasso.with(this)
                .load(tvseries.getImage(1))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(poster);

        int vote = tvseries.getVoteAverage();
        if(vote < 5 ) {
            rateIcon.setImageResource(R.drawable.splat);
        } else {
            rateIcon.setImageResource(R.drawable.fresh);
        }

        float pop = Math.round(tvseries.getPopularity() * 100f) / 100f;
        popularity.setText(pop+"");
        voteAverage.setText(String.valueOf(vote) + "/10");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        String date = null;
        try {
            newDate = format.parse(tvseries.getFirstAirDate());

            SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
            date = format2.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        firstAirDate.append(Html.fromHtml("<font color='#000000'>" + (date != null ? date : "not available") + "</font>"));
        overview.setText(tvseries.getOverview());

        String tmp = "";
        String[] genresArray = tvseries.getGenres();
        if(genresArray.length == 0) {
            tmp = "not available";
        } else {
            for (int i = 0; i < genresArray.length; i++) {
                tmp += genresArray[i];
                if (i == genresArray.length - 1) {
                    break;
                }
                tmp += " & ";
            }
        }

        genres.append(Html.fromHtml("<font color='#000000'>" + tmp + "</font>"));
        language.append(Html.fromHtml("<font color='#000000'>" + tvseries.getLang().toUpperCase() + "</font>"));

        tmp = ""; int i = 0;
        for(String network : tvseries.getNetworks()) {
            tmp += network;
            if(++i == tvseries.getNetworks().length) {break;}
            tmp += " & ";
        }
        networks.append(Html.fromHtml("<font color='#000000'>" + tmp + "</font>"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_share) {
            if(tvseries == null) {
                Toast.makeText(TVSeriesDetailsActivity.this, "Couldn't share current tvseries.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Check out \"" + tvseries.getTitle() + "\" @MovieDB");
                this.startActivity(Intent.createChooser(intent, "Share with"));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
