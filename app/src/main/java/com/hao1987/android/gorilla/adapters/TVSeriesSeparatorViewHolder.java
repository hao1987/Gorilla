package com.hao1987.android.gorilla.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hao1987.android.gorilla.R;

public class TVSeriesSeparatorViewHolder extends RecyclerView.ViewHolder {

    protected TextView title;

    protected TextView showMore;
    protected TextView total;


    public TVSeriesSeparatorViewHolder(View view, int viewType) {
        super(view);
        if(viewType % 2 == 0) {
            title = (TextView) view.findViewById(R.id.title);
        } else {
            showMore = (TextView) view.findViewById(R.id.showMore);
            total = (TextView) view.findViewById(R.id.total);
        }
    }
}
