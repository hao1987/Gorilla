package com.hao1987.android.gorilla.models;

import android.os.Parcel;
import android.os.Parcelable;


public class Cast extends People{

    private String cCharacter;

    public String getcCharacter() {
        return cCharacter;
    }

    public Cast(String pName, String pProfilePath, String pCreditId, String cCharacter) {
        super(pName, pProfilePath, pCreditId, "cast");
        this.cCharacter = cCharacter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getpType());
        super.writeToParcel(out, flags);
        out.writeString(cCharacter);
    }

    public static final Parcelable.Creator<Cast> CREATOR = new Parcelable.Creator<Cast>() {
        public Cast createFromParcel(Parcel in) {
            return new Cast(in);
        }

        public Cast[] newArray(int size) {
            return new Cast[size];
        }
    };

    public Cast(Parcel in) {
        super(in);
        cCharacter = in.readString();
    }

}
