package com.hao1987.android.gorilla.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.adapters.MovieOverviewAdapter;
import com.hao1987.android.gorilla.adapters.TabAdapter;
import com.hao1987.android.gorilla.fragments.MovieFragment;
import com.hao1987.android.gorilla.fragments.TVSeriesFragment;
import com.hao1987.android.gorilla.ui.SlidingTabLayout;


public class MainActivity extends BaseActivity {

    private String LOG_TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private MovieOverviewAdapter mMovieOverviewAdapter;


    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;

    private TabAdapter mTabAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar();

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.tabs);


        mTabAdapter = new TabAdapter(getSupportFragmentManager());

        mTabAdapter.addTab(MovieFragment.newInstance("movies"), 0, "movies");
        mTabAdapter.addTab(TVSeriesFragment.newInstance("tv shows"), 1, "tv shows");

        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOnPageChangeListener(mTabAdapter);

        mSlidingTabLayout.setCustomTabView(R.layout.slide_tab, R.id.slideTabTextView);
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.MovieDBAccent));
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.MovieDBPrimary));
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

//
//
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        mMovieOverviewAdapter = new MovieOverviewAdapter(this, new ArrayList<Movie>());
//        mRecyclerView.setAdapter(mMovieOverviewAdapter);
//        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView, new RecyclerItemClickListener.OnItemClickLister() {
//
//            @Override
//            public void onItemClick(View view, int position) {
//                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
//                intent.putExtra(MOVIE_ID, mMovieOverviewAdapter.getMovieOverview(position).getId());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(View view, int position) {
//            }
//        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_feedback) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog.setTitle("Feedback");

            final LinearLayout layout = new LinearLayout(MainActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 10, 50, 0);

            final TextView textView = new TextView(new ContextThemeWrapper(MainActivity.this, R.style.UserEditText));
            textView.setText("to: developer@moviedb.com");

            final EditText editText = new EditText(new ContextThemeWrapper(MainActivity.this, R.style.UserEditText));

            layout.addView(textView, params);
            layout.addView(editText, params);
            alertDialog.setView(layout);

            alertDialog
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    if (!editText.getText().toString().equals("")) {

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("message/rfc822");
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"waynephoenix@live.com"});
                        i.putExtra(Intent.EXTRA_SUBJECT, "How about this ?");
                        i.putExtra(Intent.EXTRA_TEXT, editText.getText().toString());
                        try {
                            startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.cancel();
                    }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();

        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

/*
    @Override
    protected void onResume() {
        super.onResume();

        String query = getSavedPreferenceData(FILMITEM_QUERY);

        if (query.length() > 0) {
            ProcessMovieOverviews processMovieOverviews = new ProcessMovieOverviews(query);
            processMovieOverviews.execute();
        } else {
            ProcessMovieUpcomings processMovieUpcomings = new ProcessMovieUpcomings(1);
            processMovieUpcomings.execute();
        }
    }

    private String getSavedPreferenceData(String key) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        return sharedPref.getString(key, "");
    }

    public class ProcessMovieOverviews extends GetMovieOverviewData {

        public ProcessMovieOverviews(String searchCriteria) {
            super(searchCriteria);
        }

        public void execute() {
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mMovieOverviewAdapter.loadDataSet(getMovieOverviews());
            }

            @Override
            protected String doInBackground(String... strings) {
                return super.doInBackground(strings);
            }
        }
    }


    public class ProcessMovieUpcomings extends GetMovieUpcomingData {

        public ProcessMovieUpcomings(int searchCriteria) {
            super(searchCriteria);
        }

        public void execute() {
            super.execute();
            ProcessData processData = new ProcessData();
            processData.execute();
        }

        public class ProcessData extends DownloadJsonData {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mMovieOverviewAdapter.loadDataSet(getMovieUpcomings());
            }

            @Override
            protected String doInBackground(String... strings) {
                return super.doInBackground(strings);
            }
        }
    }
    */
}
