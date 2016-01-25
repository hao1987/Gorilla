package com.hao1987.android.gorilla.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Crew extends People {

    private String cJob;

    public String getcJob() {
        return cJob;
    }

    public Crew(String pName, String pProfilePath, String pCreditId, String cJob) {
        super(pName, pProfilePath, pCreditId, "crew");
        this.cJob = cJob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(getpType());
        super.writeToParcel(out, flags);
        out.writeString(cJob);
    }

    public static final Parcelable.Creator<Crew> CREATOR = new Parcelable.Creator<Crew>() {
        public Crew createFromParcel(Parcel in) {
            return new Crew(in);
        }

        public Crew[] newArray(int size) {
            return new Crew[size];
        }
    };

    protected Crew(Parcel in) {
        super(in);
        cJob = in.readString();
    }


}
