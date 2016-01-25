package com.hao1987.android.gorilla.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.List;

public class TVSeries extends FilmItem implements MovieConfig, Parcelable{
    protected String[] mGenres;
    protected long mId;
    protected String mFirstAirDate;
    protected String[] mNetworks;

    protected List<Integer> mSeasons;
    protected List<Integer> mEpisodes;

    private List<People> mCast;
    private List<People> mCrew;

    public TVSeries() {super();}

    // browse
    public TVSeries(long id, String lang, String title, String poster, String firstAirDate, float popularity, int voteAverage, String overview) {
        super(lang, title, poster, popularity, voteAverage, overview, "tvseries");
        mId = id;
        mFirstAirDate = firstAirDate;
    }

    // detail
    public TVSeries(String[] genres, long id, String lang, String title, String poster, String firstAirDate, String[] networks, List<Integer> seasons, List<Integer> episodes, float popularity, int voteAverage, String overview) {
        super(lang, title, poster, popularity, voteAverage, overview, "tvseries");
        mGenres = genres;
        mId = id;
        mFirstAirDate = firstAirDate;
        mNetworks = networks;
        mSeasons = seasons;
        mEpisodes = episodes;
    }

    public String[] getGenres() {
        return mGenres;
    }

    public void setGenres(String[] genres) {
        mGenres = genres;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getFirstAirDate() {
        return mFirstAirDate;
    }

    public void setFirstAirDate(String firstAirDate) {
        mFirstAirDate = firstAirDate;
    }

    public String[] getNetworks() {
        return mNetworks;
    }

    public void setNetworks(String[] networks) {
        mNetworks = networks;
    }

    public List<Integer> getSeasons() {
        return mSeasons;
    }

    public void setSeasons(List<Integer> seasons) {
        mSeasons = seasons;
    }

    public List<Integer> getEpisodes() {
        return mEpisodes;
    }

    public void setEpisodes(List<Integer> episodes) {
        mEpisodes = episodes;
    }

    public List<People> getCast() {
        return mCast;
    }

    public void setCast(List<People> cast) {
        mCast = cast;
    }

    public List<People> getCrew() {
        return mCrew;
    }

    public void setCrew(List<People> crew) {
        mCrew = crew;
    }

    public String getImage(int size) {
        return this.IMAGE_BASE_URL + this.POSTER_SIZE[size] + "/" + mPoster;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);

        out.writeArray(mGenres);
        out.writeLong(mId);
        out.writeString(mFirstAirDate);
    }

    public static final Creator<TVSeries> CREATOR
            = new Creator<TVSeries>() {
        public TVSeries createFromParcel(Parcel in) {
            return new TVSeries(in);
        }

        public TVSeries[] newArray(int size) {
            return new TVSeries[size];
        }
    };

    protected TVSeries(Parcel in) {
        super(in);
        in.readStringArray(mGenres);
        mId = in.readLong();
        mFirstAirDate = in.readString();
    }
}
