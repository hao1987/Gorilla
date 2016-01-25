package com.hao1987.android.gorilla.models;

import android.os.Parcel;

// object serve empty search result
public class Placeholder extends FilmItem{

    public Placeholder(String type) {
        super(type);
    }

    private Placeholder(Parcel in) {
        super(in);
    }

    public static final Creator<Placeholder> CREATOR
            = new Creator<Placeholder>() {
        public Placeholder createFromParcel(Parcel in) {
            return new Placeholder(in);
        }

        public Placeholder[] newArray(int size) {
            return new Placeholder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getType());
        super.writeToParcel(out, flags);
    }
}
