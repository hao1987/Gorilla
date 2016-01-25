package com.hao1987.android.gorilla.activities;

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
import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.helpers.APIManager;
import com.hao1987.android.gorilla.models.Cast;
import com.hao1987.android.gorilla.models.Crew;
import com.hao1987.android.gorilla.models.Movie;
import com.hao1987.android.gorilla.models.People;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MovieDetailsActivity extends BaseActivity {

    private String LOG_TAG = MovieDetailsActivity.class.getSimpleName();

    private ImageView poster;
    private ImageView rateIcon;

    private TextView title;
    private TextView popularity;
    private TextView voteAverage;
    private TextView releaseDate;
    private TextView language;
    private TextView director;
    private TextView runtime;
    private TextView overview;
    private TextView genres;
    private TextView add2WatchList;

    private LinearLayout creditsLayout;

    Movie mMovie;
    List<People> cast;
    List<People> crew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_movie_detail);
        activateToolbarWithHomeEnabled();

        Bundle bundle = getIntent().getExtras();
        mMovie = bundle.getParcelable(FILMITEM_MOVIE);

        poster = (ImageView) findViewById(R.id.mPoster);
        rateIcon = (ImageView) findViewById(R.id.mRateIcon);

        title = (TextView) findViewById(R.id.mTitle);
        popularity = (TextView) findViewById(R.id.mPopularity);
        voteAverage = (TextView) findViewById(R.id.mVoteAverage);
        releaseDate = (TextView) findViewById(R.id.mReleaseDate);
        language = (TextView) findViewById(R.id.mLanguage);
        director = (TextView) findViewById(R.id.mDirector);
        runtime = (TextView) findViewById(R.id.mRuntime);
        overview = (TextView) findViewById(R.id.mOverview);
        genres = (TextView) findViewById(R.id.mGenres);
        add2WatchList = (TextView) findViewById(R.id.add2WatchList);
        add2WatchList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MovieDetailsActivity.this, "Feature is coming soon!", Toast.LENGTH_SHORT).show();
            }
        });

        cast = mMovie.getCast();
        crew = mMovie.getCrew();


        executeRequest(new JsonObjectRequest(Request.Method.GET, APIManager.getMovieDetail(mMovie.getId() + "", 1 + ""), null,
                    responseListener(), errorListener()));
    }

    private Response.Listener<JSONObject> responseListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                ProcessRawData handler = new ProcessMovieDetailsData(response, mMovie);
                handler.processResult();

                renderDetails((List<Movie>) handler.getFilmItemProcessedData());
            }
        };
    }

    public void renderDetails(List<Movie> mMovieDetail) {
        Movie m = mMovieDetail.get(0);

        title.setText(m.getTitle());

        Picasso.with(this)
                .load(m.getImage(1))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(poster);

        int vote = m.getVoteAverage();
        if(vote < 5 ) {
            rateIcon.setImageResource(R.drawable.splat);
        } else {
            rateIcon.setImageResource(R.drawable.fresh);
        }

        float pop = Math.round(m.getPopularity() * 100f) / 100f;

        popularity.setText(pop+"");
        voteAverage.setText(String.valueOf(vote) + "/10");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        String date = null;
        try {
            newDate = format.parse(m.getReleaseDate());

            SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
            date = format2.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        releaseDate.append(Html.fromHtml("<font color='#000000'>" + (date != null ? date : "not available") + "</font>"));
        overview.setText(m.getOverview());

        String tmp = "";
        String[] genresArray = m.getGenres();
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

        tmp = m.getRuntime() > 0 ? m.getRuntime() + " mins" : "not available";
        runtime.append(Html.fromHtml("<font color='#000000'>" + tmp + "</font>"));

        language.append(Html.fromHtml("<font color='#000000'>" + m.getLang().toUpperCase() + "</font>"));

        if(crew.size() > 0) {
            People p = crew.get(0);
            director.append(Html.fromHtml("<font color='#000000'>" + p.getpName() + "</font>"));
        } else {
            director.append(Html.fromHtml("<font color='#000000'>not available</font>"));
        }

        creditsLayout = (LinearLayout) findViewById(R.id.mCreditsLayout);

        for(People p : cast) {
            Cast c = (Cast) p;
            View view = getLayoutInflater().inflate(R.layout.list_movie_credit, null);
            ImageView thumbnail = (ImageView) view.findViewById(R.id.creditThumbnail);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView character = (TextView) view.findViewById(R.id.character);

            if(c.getpProfilePath() != null && c.getpProfilePath().length() > 0) {
                Picasso.with(this)
                        .load(c.getImage(0))
                        .placeholder(R.drawable.avatar)
                        .error(R.drawable.avatar)
                        .into(thumbnail);
            }

            name.setText(c.getpName());
            character.setText("as " + c.getcCharacter());

            creditsLayout.addView(view);
        }

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
            if(mMovie == null) {
                Toast.makeText(MovieDetailsActivity.this, "Couldn't share current movie.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, "Check out \"" + mMovie.getTitle() + "\" @MovieDB");
                this.startActivity(Intent.createChooser(intent, "Share with"));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
