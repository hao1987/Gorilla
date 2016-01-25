package com.hao1987.android.gorilla.models;

import android.os.Parcel;

public class Header extends FilmItem{

    public Header(String type) {
        super(type);
    }

    private Header(Parcel in) {
        super(in);
    }

    public static final Creator<Header> CREATOR
            = new Creator<Header>() {
        public Header createFromParcel(Parcel in) {
            return new Header(in);
        }

        public Header[] newArray(int size) {
            return new Header[size];
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
