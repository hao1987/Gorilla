package com.hao1987.android.gorilla.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.activities.BaseActivity;
import com.hao1987.android.gorilla.activities.MainActivity;
import com.hao1987.android.gorilla.activities.MovieDetailsActivity;
import com.hao1987.android.gorilla.models.Crew;
import com.hao1987.android.gorilla.models.FilmItem;
import com.hao1987.android.gorilla.models.Movie;
import com.hao1987.android.gorilla.models.People;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String LOG_TAG = MovieOverviewAdapter.class.getSimpleName();

    private SharedPreferences mSharedPreferences;
    private Context mContext;

    private final int mViewItem = 1;
    private final int mViewPro = 0;
    private List<Movie> mMovieOverviewList;

    // The minimum amount of items to have below your current scroll position before loading more.
    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public class MovieOverviewViewHolder extends RecyclerView.ViewHolder {
        protected ImageView thumbnail;
        protected ImageView rateIcon;
        protected TextView title;
        protected TextView voteAverage;
        protected TextView releaseDate;
        protected TextView director;
        protected ImageView viewIcon;


        public MovieOverviewViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            rateIcon = (ImageView) view.findViewById(R.id.rateIcon);
            title = (TextView) view.findViewById(R.id.title);
            voteAverage = (TextView) view.findViewById(R.id.voteAverage);
            releaseDate = (TextView) view.findViewById(R.id.releaseDate);
            director = (TextView) view.findViewById(R.id.director);
            viewIcon = (ImageView) view.findViewById(R.id.viewedIcon);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    setSelected(getLayoutPosition());

                    Intent intent = new Intent(mContext, MovieDetailsActivity.class);

                    Movie m = getMovieOverview(getLayoutPosition());
                    intent.putExtra(MainActivity.FILMITEM_MOVIE, m);

                    mContext.startActivity(intent);
                }
            });
        }
    }

    public MovieOverviewAdapter(Context mContext, List<Movie> movieOverviewList, RecyclerView recyclerView) {
        this.mMovieOverviewList = movieOverviewList;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mContext = mContext;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mMovieOverviewList.get(position)!=null? mViewItem: mViewPro;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        RecyclerView.ViewHolder vh;
        if(i == mViewItem) {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.list_movie_browse, null);

            vh = new MovieOverviewViewHolder(v);
        } else {
            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.progress_bar, null);

            vh = new ProgressViewHolder(v);
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof MovieOverviewViewHolder) {
            final MovieOverviewViewHolder movieOverviewViewHolder = (MovieOverviewViewHolder) holder;
            final Movie movieOverview = mMovieOverviewList.get(i);
            Picasso.with(mContext)
                    .load(movieOverview.getImage(1))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(movieOverviewViewHolder.thumbnail);

            movieOverviewViewHolder.title.setText(movieOverview.getTitle());

            Set<String> set = mSharedPreferences.getStringSet(mContext.getString(R.string.saved_movies), new HashSet<String>());
            if(movieOverview.isSelected() && set.contains(movieOverview.getId()+"")) {
                movieOverviewViewHolder.viewIcon.setVisibility(View.VISIBLE);
            } else {
                movieOverviewViewHolder.viewIcon.setVisibility(View.INVISIBLE);
            }

            int voteAverage = movieOverview.getVoteAverage();
            if (voteAverage < 5) {
                movieOverviewViewHolder.rateIcon.setImageResource(R.drawable.splat);
            } else {
                movieOverviewViewHolder.rateIcon.setImageResource(R.drawable.fresh);
            }
            movieOverviewViewHolder.voteAverage.setText(String.valueOf(voteAverage) + "/10");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            String releaseDate = movieOverview.getReleaseDate(), date = "";
            try {
                if(releaseDate != null && releaseDate.length() > 0) {
                    newDate = format.parse(movieOverview.getReleaseDate());

                    SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
                    date = format2.format(newDate);
                }
                movieOverviewViewHolder.releaseDate.setText("Opens: ");
                movieOverviewViewHolder.releaseDate.append(Html.fromHtml("<font color='#000000'>" +(date.length() > 0 ? date : "not available".toUpperCase()) + "</font>"));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(movieOverview.getCrew() != null && movieOverview.getCrew().size() > 0) {
                People p = movieOverview.getCrew().get(0);
                movieOverviewViewHolder.director.setText("Directed by: ");
                movieOverviewViewHolder.director.append(Html.fromHtml("<font color='#000000'>" + p.getpName() + "</font>"));
            }

        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return mMovieOverviewList != null ? mMovieOverviewList.size() : 0;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public void loadDataSet(List<Movie> movieOverviewList) {
        if(mMovieOverviewList != null) {
            mMovieOverviewList.addAll(movieOverviewList);
        }

        notifyDataSetChanged();
    }

    public void updateDataSet(int i, Movie m) {
        mMovieOverviewList.set(i, m);
        notifyItemChanged(i);
    }

    public void updateDataSet(List<Movie> movieOverviewList) {
        if(mMovieOverviewList != null) {
            mMovieOverviewList.clear();
            mMovieOverviewList = movieOverviewList;
        }

        notifyDataSetChanged();
    }

    public void setSelected(int pos) {
        try {
            Movie curr = mMovieOverviewList.get(pos);
            curr.setSelected(true);

            Set<String> movieIds = mSharedPreferences.getStringSet(mContext.getString(R.string.saved_movies), new HashSet<String>());
            movieIds.add(curr.getId()+"");

//            Log.d(LOG_TAG, curr.getId() + " : " + curr.getTitle());
//
//            for (String id : movieIds) {
//                Log.d(LOG_TAG, id);
//            }

            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putStringSet(mContext.getString(R.string.saved_movies), movieIds);
            editor.commit();


            notifyItemChanged(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Movie getMovieOverview(int position) {
        return mMovieOverviewList != null ? mMovieOverviewList.get(position) : null;
    }
}
