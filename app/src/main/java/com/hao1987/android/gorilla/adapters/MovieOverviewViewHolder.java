package com.hao1987.android.gorilla.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hao1987.android.gorilla.R;


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
    }


}
