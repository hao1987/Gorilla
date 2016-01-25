package com.hao1987.android.gorilla.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.models.Expander;
import com.hao1987.android.gorilla.models.FilmItem;
import com.hao1987.android.gorilla.models.Movie;
import com.hao1987.android.gorilla.models.People;
import com.hao1987.android.gorilla.models.Placeholder;
import com.hao1987.android.gorilla.models.TVSeries;
import com.hao1987.android.gorilla.models.Header;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String LOG_TAG = SearchResultAdapter.class.getSimpleName();

    private SharedPreferences mSharedPreferences;
    private Context mContext;

    private final int mViewMovieItem = 0;
    private final int mViewTVSeriesItem = 1;

    private final int mViewMovieHeader = 2;
    private final int mViewMovieExpander = 3;

    private final int mViewTVSeriesHeader = 4;
    private final int mViewTVSeriesExpander = 5;

    private final int mViewPlaceholderItem = 6;

    private final int mSuggestThrehold = 4;

    private List<FilmItem> mMoviesList;
    private List<FilmItem> mTVSeriesList;

    private List<FilmItem> mFilmItemsList;


    public SearchResultAdapter(Context mContext, List<FilmItem> filmItemsList) {
        this.mFilmItemsList = filmItemsList;
        this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        FilmItem filmItem = mFilmItemsList.get(position);

        if(filmItem instanceof Header) {
            if(filmItem.getType() == "movie") {return mViewMovieHeader;}
            return mViewTVSeriesHeader;
        }

        if(filmItem instanceof Expander) {
            if(filmItem.getType() == "movie") {return mViewMovieExpander;}
            return mViewTVSeriesExpander;
        }

        if(filmItem instanceof Placeholder) {return mViewPlaceholderItem;}

        if(filmItem instanceof Movie) { return mViewMovieItem; }
        if(filmItem instanceof TVSeries) { return mViewTVSeriesItem; }

        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        RecyclerView.ViewHolder vh;
        switch (i) {
            case mViewMovieItem:
                vh = new MovieOverviewViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_movie_browse, null));
                break;

            case mViewTVSeriesItem:
                vh = new TVSeriesOverviewViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_tvseries_browse, null));
                break;
            case mViewMovieHeader:
                vh = new MovieSeparatorViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.filmitems_header, null), mViewMovieHeader);
                break;

            case mViewMovieExpander:
                vh = new MovieSeparatorViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.filmitems_expander, null), mViewMovieExpander);
                break;

            case mViewTVSeriesHeader:
                vh = new TVSeriesSeparatorViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.filmitems_header, null), mViewTVSeriesHeader);
                break;

            case mViewTVSeriesExpander:
                vh = new TVSeriesSeparatorViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.filmitems_expander, null), mViewTVSeriesExpander);
                break;

            default:
                vh = new PlaceholderViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.filmitems_placeholder, null), mViewPlaceholderItem);
                break;
        }

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof MovieOverviewViewHolder) {
            final MovieOverviewViewHolder movieOverviewViewHolder = (MovieOverviewViewHolder) holder;
            final Movie movieOverview = (Movie) mFilmItemsList.get(i);
            Picasso.with(mContext)
                    .load(movieOverview.getImage(1))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(movieOverviewViewHolder.thumbnail);

            movieOverviewViewHolder.title.setText(movieOverview.getTitle());

            Set<String> set = mSharedPreferences.getStringSet(mContext.getString(R.string.saved_movies), new HashSet<String>());
            if(movieOverview.isSelected() && set.contains(movieOverview.getId()+"")) {
                movieOverviewViewHolder.viewIcon.setVisibility(View.VISIBLE);
                Log.d(LOG_TAG, movieOverview.getTitle());
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
                movieOverviewViewHolder.releaseDate.append(Html.fromHtml("<font color='#000000'>" + (date.length() > 0 ? date : "not available".toUpperCase()) + "</font>"));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if(movieOverview.getCrew() != null && movieOverview.getCrew().size() > 0) {
                People p = movieOverview.getCrew().get(0);
                movieOverviewViewHolder.director.setText("Directed by: ");
                movieOverviewViewHolder.director.append(Html.fromHtml("<font color='#000000'>" + p.getpName() + "</font>"));
            }


        } else if(holder instanceof TVSeriesOverviewViewHolder) {
            final TVSeriesOverviewViewHolder tvSeriesOverviewViewHolder = (TVSeriesOverviewViewHolder) holder;
            final TVSeries tvSeriesOverview = (TVSeries) mFilmItemsList.get(i);
            Picasso.with(mContext)
                    .load(tvSeriesOverview.getImage(1))
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(tvSeriesOverviewViewHolder.thumbnail);

            tvSeriesOverviewViewHolder.title.setText(tvSeriesOverview.getTitle());

            Set<String> set = mSharedPreferences.getStringSet(mContext.getString(R.string.saved_movies), new HashSet<String>());
            if(tvSeriesOverview.isSelected() && set.contains(tvSeriesOverview.getId()+"")) {
                tvSeriesOverviewViewHolder.viewIcon.setVisibility(View.VISIBLE);
                Log.d(LOG_TAG, tvSeriesOverview.getTitle());
            } else {
                tvSeriesOverviewViewHolder.viewIcon.setVisibility(View.INVISIBLE);
            }

            int voteAverage = tvSeriesOverview.getVoteAverage();
            if (voteAverage < 5) {
                tvSeriesOverviewViewHolder.rateIcon.setImageResource(R.drawable.splat);
            } else {
                tvSeriesOverviewViewHolder.rateIcon.setImageResource(R.drawable.fresh);
            }
            tvSeriesOverviewViewHolder.voteAverage.setText(String.valueOf(voteAverage) + "/10");

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date newDate = null;
            String releaseDate = tvSeriesOverview.getFirstAirDate(), date = "";
            try {
                if(releaseDate != null && releaseDate.length() > 0) {
                    newDate = format.parse(tvSeriesOverview.getFirstAirDate());

                    SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
                    date = format2.format(newDate);
                }
                tvSeriesOverviewViewHolder.releaseDate.setText(date.length() > 0 ? date : "not available".toUpperCase());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            tvSeriesOverviewViewHolder.language.setText(" | " + tvSeriesOverview.getLang().toUpperCase());
        } else if(holder instanceof MovieSeparatorViewHolder) {

            MovieSeparatorViewHolder h = (MovieSeparatorViewHolder) holder;
            if(h.title != null) {
                h.title.setText("Movies");
            } else {
                h.showMore.setText("Show all movie results");
                h.total.setText(mMoviesList.size() + " total");
            }
        } else if(holder instanceof TVSeriesSeparatorViewHolder) {

            TVSeriesSeparatorViewHolder h = (TVSeriesSeparatorViewHolder) holder;
            if(h.title != null) {
                h.title.setText("TV series");
            } else {
                h.showMore.setText("Show all TV series results");
                h.total.setText(mTVSeriesList.size() + " total");
            }
        }
    }

    @Override
    public int getItemCount() {
        return mFilmItemsList != null ? mFilmItemsList.size() : 0;
    }

    public void mergeDataSet(List<FilmItem> mFilmItems, String type) {
        mFilmItemsList.add(new Header(type));
        if(mFilmItems != null) {
            if(mFilmItems.size() < 1) {
                mFilmItemsList.add(new Placeholder(type));
            } else {
                if (mFilmItems.size() > mSuggestThrehold) {
                    for (int i = 0; i < mSuggestThrehold; i++) {
                        mFilmItemsList.add(mFilmItems.get(i));
                    }
                    mFilmItemsList.add(new Expander(type));
                } else {
                    mFilmItemsList.addAll(mFilmItems);
                }

                if (type.equals("movie")) {
                    mMoviesList = mFilmItems;
                } else if (type.equals("tvseries")) {
                    mTVSeriesList = mFilmItems;
                }
            }
        }

        Log.d(LOG_TAG, mFilmItems.size()+" added......");
        notifyDataSetChanged();
    }

    // ONLY apply for movies
    public void updateDataSet(int i, FilmItem item) {
        if(item.getType().equals("movie")) {
            mMoviesList.set(i, item);

            if (i < mSuggestThrehold) {
                Header h = (Header) mFilmItemsList.get(0);
                if (h.getType().equals("movie")) {
                    mFilmItemsList.set(i + 1, mMoviesList.get(i));

                    notifyItemChanged(i + 1);
                } else {
                    int offset = mTVSeriesList.size() < 1 ? 1 : (mTVSeriesList.size() > mSuggestThrehold ? mSuggestThrehold : mTVSeriesList.size());
                    mFilmItemsList.set(i + 1 + offset + 1, item);

                    notifyItemChanged(i + 1 + offset + 1);
                }
            }
        }

    }

    public void clearDataSet() {
        if(mFilmItemsList != null) {
            mFilmItemsList.clear();
            notifyDataSetChanged();
        }
    }

    public void setSelected(int pos) {
        try {
            FilmItem curr = mFilmItemsList.get(pos);
            curr.setSelected(true);

            SharedPreferences.Editor editor = mSharedPreferences.edit();

            if(curr instanceof Movie) {
                Set<String> movieIds = mSharedPreferences.getStringSet(mContext.getString(R.string.saved_movies), new HashSet<String>());
                movieIds.add(((Movie) curr).getId() + "");
                editor.putStringSet(mContext.getString(R.string.saved_movies), movieIds);

            } else if(curr instanceof TVSeries) {
                Set<String> tvseriesIds = mSharedPreferences.getStringSet(mContext.getString(R.string.saved_tvseries), new HashSet<String>());
                tvseriesIds.add(((TVSeries) curr).getId() + "");
                editor.putStringSet(mContext.getString(R.string.saved_movies), tvseriesIds);

            }

            editor.commit();

//            Log.d(LOG_TAG, curr.getId() + " : " + curr.getTitle());
//
//            for (String id : movieIds) {
//                Log.d(LOG_TAG, id);
//            }


            notifyItemChanged(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getSuggestThrehold() {
        return mSuggestThrehold;
    }

    public List<FilmItem> getMoviesList() {
        return mMoviesList;
    }

    public List<FilmItem> getTVSeriesList() {
        return mTVSeriesList;
    }

    public List<FilmItem> getFilmItemsList() {
        return mFilmItemsList != null ? mFilmItemsList : null;
    }

    public FilmItem getFilmItemOverview(int position) {
        return mFilmItemsList != null ? mFilmItemsList.get(position) : null;
    }
}
