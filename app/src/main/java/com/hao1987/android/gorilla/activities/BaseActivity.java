package com.hao1987.android.gorilla.activities;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.helpers.VolleyManager;


public class BaseActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    public static final String FILMITEM_MOVIE = "movie";
    public static final String FILMITEM_TVSERIES = "tvseries";
    public static final String FILMITEM_QUERY = "MOVIE_QUERY";
    public static final String FILMITEM_ID = "ID";

    protected Toolbar activateToolbar() {
        if(mToolbar == null) {
            mToolbar = (Toolbar) findViewById(R.id.app_bar);
            if(mToolbar != null) {
                setSupportActionBar(mToolbar);
            }
        }
        return mToolbar;
    }

    protected Toolbar activateToolbarWithHomeEnabled() {
        activateToolbar();
        if(mToolbar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        return mToolbar;
    }

    public void executeRequest(Request<?> request) {
        VolleyManager.getInstance(this).addToRequestQueue(request);
   }

    public Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BaseActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };
    }
}
