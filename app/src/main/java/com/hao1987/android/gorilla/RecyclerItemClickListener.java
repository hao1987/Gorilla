package com.hao1987.android.gorilla;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {

    public static interface OnItemClickLister{
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view, int position);
    }

    private OnItemClickLister mOnItemClickLister;
    private GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickLister onItemClickLister) {
        this.mOnItemClickLister = onItemClickLister;
        this.mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            public boolean onSingleTapUp(MotionEvent event) {
                return true;
            }

            public void onLongPress(MotionEvent event) {
                View childView = recyclerView.findChildViewUnder(event.getX(), event.getY());
                if(childView != null && mOnItemClickLister != null) {
                    mOnItemClickLister.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if(childView != null && mOnItemClickLister != null && mGestureDetector.onTouchEvent(e)) {
            mOnItemClickLister.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }
}
