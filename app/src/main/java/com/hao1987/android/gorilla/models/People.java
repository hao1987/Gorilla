package com.hao1987.android.gorilla.models;

import android.os.Parcel;
import android.os.Parcelable;

public abstract class People implements MovieConfig, Parcelable{

    protected String pName;
    protected String pProfilePath;
    protected String pCreditId;

    protected String pType;

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpProfilePath() {
        return pProfilePath;
    }

    public void setpProfilePath(String pProfilePath) {
        this.pProfilePath = pProfilePath;
    }

    public String getImage(int size) {
        return this.IMAGE_BASE_URL + this.POSTER_SIZE[size] + "/" + pProfilePath;
    }

    public String getpCreditId() {
        return pCreditId;
    }

    public void setpCreditId(String pCreditId) {
        this.pCreditId = pCreditId;
    }

    public String getpType() {
        return pType;
    }

    public void setpType(String pType) {
        this.pType = pType;
    }

    protected People(String pName, String pProfilePath, String pCreditId, String pType) {
        this.pName = pName;
        this.pProfilePath = pProfilePath;
        this.pCreditId = pCreditId;
        this.pType = pType;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(pName);
        out.writeString(pProfilePath);
        out.writeString(pCreditId);
    }

    public static final Parcelable.Creator<People> CREATOR = new Parcelable.Creator<People>() {
        public People createFromParcel(Parcel in) {
            String type = in.readString();
            People people = null;

            if(type.equals("cast"))
                people = (People) new Cast(in);

            if(type.equals("crew"))
                people = (People) new Crew(in);

            return people;
        }

        public People[] newArray(int size) {
            return new People[size];
        }
    };

    protected People(Parcel in) {
        pName = in.readString();
        pProfilePath = in.readString();
        pCreditId = in.readString();
    }
}
