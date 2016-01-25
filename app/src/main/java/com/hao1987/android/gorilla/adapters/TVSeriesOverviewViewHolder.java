package com.hao1987.android.gorilla.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hao1987.android.gorilla.R;


public class TVSeriesOverviewViewHolder extends RecyclerView.ViewHolder {

    protected ImageView thumbnail;
    protected ImageView rateIcon;
    protected TextView language;
    protected TextView title;
    protected TextView voteAverage;
    protected TextView releaseDate;
    protected ImageView viewIcon;


    public TVSeriesOverviewViewHolder(View view) {
        super(view);
        thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        rateIcon = (ImageView) view.findViewById(R.id.rateIcon);
        language = (TextView) view.findViewById(R.id.language);
        title = (TextView) view.findViewById(R.id.title);
        voteAverage = (TextView) view.findViewById(R.id.voteAverage);
        releaseDate = (TextView) view.findViewById(R.id.releaseDate);
        viewIcon = (ImageView) view.findViewById(R.id.viewedIcon);
    }


}
