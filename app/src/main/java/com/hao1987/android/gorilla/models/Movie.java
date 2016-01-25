package com.hao1987.android.gorilla.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Movie extends FilmItem implements MovieConfig{
    protected String[] mGenres;
    protected long mId;
    protected String mReleaseDate;
    protected int mRuntime;

    protected List<People> mCast;
    protected List<People> mCrew;

    public Movie() {super(); mCast = new ArrayList<People>(); mCrew = new ArrayList<People>();}

    // browse
    public Movie(long id, String lang, String title, String poster, String releaseDate, float popularity, int voteAverage, String overview) {
        super(lang, title, poster, popularity, voteAverage, overview, "movie");
        mId = id;
        mReleaseDate = releaseDate;
    }

    // detail
    public Movie(String[] genres, long id, String lang, String title, String poster, String releaseDate, float popularity, int voteAverage, int runtime, String overview) {
        super(lang, title, poster, popularity, voteAverage, overview, "movie");
        mGenres = genres;
        mId = id;
        mReleaseDate = releaseDate;
        mRuntime = runtime;
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

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        mReleaseDate = releaseDate;
    }

    public int getRuntime() {
        return mRuntime;
    }

    public void setRuntime(int runtime) {
        mRuntime = runtime;
    }

    public String getImage(int size) {
        return this.IMAGE_BASE_URL + this.POSTER_SIZE[size] + "/" + mPoster;
    }

    public List<People> getCrew() {
        return mCrew;
    }

    public void setCrew(List<People> crew) {
        mCrew = crew;
    }

    public List<People> getCast() {
        return mCast;
    }

    public void setCast(List<People> cast) {
        mCast = cast;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);

        out.writeStringArray(mGenres);
        out.writeLong(mId);
        out.writeString(mReleaseDate);
        out.writeInt(mRuntime);

        out.writeTypedList(getCast());
        out.writeTypedList(getCrew());
    }

    public static final Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    protected Movie(Parcel in) {
        super(in);

        mGenres = in.createStringArray();
        mId = in.readLong();
        mReleaseDate = in.readString();
        mRuntime = in.readInt();

        this.setCast(new ArrayList<People>());
        in.readTypedList(getCast(), People.CREATOR);

        this.setCrew(new ArrayList<People>());
        in.readTypedList(getCrew(), People.CREATOR);
    }
}
