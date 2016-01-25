package com.hao1987.android.gorilla.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hao1987.android.gorilla.R;
import com.hao1987.android.gorilla.activities.MainActivity;
import com.hao1987.android.gorilla.activities.MovieDetailsActivity;
import com.hao1987.android.gorilla.models.TVSeries;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TVShowOverviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<TVSeries> mTVSeriesOverviewList;

    public class TVSeriesOverviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView thumbnail;
        protected ImageView rateIcon;
        protected TextView language;
        protected TextView title;
        protected TextView voteAverage;
        protected TextView releaseDate;

        public TVSeriesOverviewViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            rateIcon = (ImageView) view.findViewById(R.id.rateIcon);
            language = (TextView) view.findViewById(R.id.language);
            title = (TextView) view.findViewById(R.id.title);
            voteAverage = (TextView) view.findViewById(R.id.voteAverage);
            releaseDate = (TextView) view.findViewById(R.id.releaseDate);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, MovieDetailsActivity.class);
            intent.putExtra(MainActivity.FILMITEM_ID, getTVShowOverview(getLayoutPosition()).getId());
            mContext.startActivity(intent);
        }
    }

    public TVShowOverviewAdapter(Context mContext, List<TVSeries> tvshowOverviewList) {
        this.mTVSeriesOverviewList = tvshowOverviewList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_tvseries_browse, null);

        RecyclerView.ViewHolder vh = new TVSeriesOverviewViewHolder(view);

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        final TVSeriesOverviewViewHolder tvseriesOverviewViewHolder = (TVSeriesOverviewViewHolder) holder;

        TVSeries tvshowOverview = mTVSeriesOverviewList.get(i);
        Picasso.with(mContext)
                .load(tvshowOverview.getImage(1))
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(tvseriesOverviewViewHolder.thumbnail);

        tvseriesOverviewViewHolder.title.setText(tvshowOverview.getTitle());
        int voteAverage = tvshowOverview.getVoteAverage();
        if(voteAverage < 5 ) {
            tvseriesOverviewViewHolder.rateIcon.setImageResource(R.drawable.splat);
        } else {
            tvseriesOverviewViewHolder.rateIcon.setImageResource(R.drawable.fresh);
        }
        tvseriesOverviewViewHolder.voteAverage.setText(String.valueOf(voteAverage) + "/10");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = format.parse(tvshowOverview.getFirstAirDate());

            SimpleDateFormat format2 = new SimpleDateFormat("MMM dd, yyyy");
            String date = format2.format(newDate);

            tvseriesOverviewViewHolder.releaseDate.setText(date.length() > 0 ? date : "not available");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvseriesOverviewViewHolder.language.setText(" | " + tvshowOverview.getLang().toUpperCase());
    }

    @Override
    public int getItemCount() {
        return mTVSeriesOverviewList != null ? mTVSeriesOverviewList.size() : 0;
    }

    public void loadDataSet(List<TVSeries> tvshowOverviewList) {
        mTVSeriesOverviewList = tvshowOverviewList;
        notifyDataSetChanged();
    }

    public TVSeries getTVShowOverview(int position) {
        return mTVSeriesOverviewList != null ? mTVSeriesOverviewList.get(position) : null;
    }
}
