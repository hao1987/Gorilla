package com.hao1987.android.gorilla.models;

import android.os.Parcel;
import android.os.Parcelable;


public abstract class FilmItem implements Parcelable{

    protected String mType;
    protected boolean mSelected;

    protected String mLang;
    protected String mTitle;
    protected String mPoster;
    protected float mPopularity;
    protected int mVoteAverage;
    protected String mOverview;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public boolean isSelected() {
        return mSelected;
    }

    public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public String getLang() {
        return mLang;
    }

    public void setLang(String lang) {
        mLang = lang;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getPoster() {
        return mPoster;
    }

    public void setPoster(String poster) {
        mPoster = poster;
    }

    public float getPopularity() {
        return mPopularity;
    }

    public void setPopularity(float popularity) {
        mPopularity = popularity;
    }

    public int getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(int voteAverage) {
        mVoteAverage = voteAverage;
    }

    public String getOverview() {
        return mOverview.length() > 0 ? mOverview : "Not available";
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public FilmItem() {}

    public FilmItem(String type) {mType = type;}

    public FilmItem(String lang, String title, String poster, float popularity, int voteAverage, String overview, String type) {
        mType = type;

        mLang = lang;
        mTitle = title;
        mPoster = poster;
        mPopularity = popularity;
        mVoteAverage = voteAverage;
        mOverview = overview;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mType);

        out.writeString(mLang);
        out.writeString(mTitle);
        out.writeString(mPoster);
        out.writeFloat(mPopularity);
        out.writeInt(mVoteAverage);
        out.writeString(mOverview);
    }

    public static final Parcelable.Creator<FilmItem> CREATOR = new Parcelable.Creator<FilmItem>() {
        public FilmItem createFromParcel(Parcel in) {
            String type = in.readString();
            FilmItem filmItem = null;

            if(type.equals("movie"))
                filmItem = (FilmItem) new Movie(in);

            if(type.equals("tvseries"))
                filmItem = (FilmItem) new TVSeries(in);

            return filmItem;
        }

        public FilmItem[] newArray(int size) {
            return new FilmItem[size];
        }
    };

    protected FilmItem(Parcel in) {
        mType = in.readString();

        mLang = in.readString();
        mTitle = in.readString();
        mPoster = in.readString();
        mPopularity = in.readFloat();
        mVoteAverage = in.readInt();
        mOverview = in.readString();
    }
}
