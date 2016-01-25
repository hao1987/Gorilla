package com.hao1987.android.gorilla.models;

import android.os.Parcel;


public class Expander extends FilmItem{

    public Expander(String type) {
        super(type);
    }

    private Expander(Parcel in) {
        super(in);
    }

    public static final Creator<Expander> CREATOR
            = new Creator<Expander>() {
        public Expander createFromParcel(Parcel in) {
            return new Expander(in);
        }

        public Expander[] newArray(int size) {
            return new Expander[size];
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
