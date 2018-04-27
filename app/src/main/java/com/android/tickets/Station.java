package com.android.tickets;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;



public class Station implements Parcelable {

    private String name;
    private String zone;
    private String additionalInfo;
    public Station(String name, String zone) {
        this.name = name;
        this.zone = zone;
        this.additionalInfo = "";
    }
    public Station(String name, String zone, String additionalInfo) {
        this.name = name;
        this.zone = zone;
        this.additionalInfo = additionalInfo;
    }

    public Station() {
        this.additionalInfo  = "";
        this.name = "";
        this.zone = "-1";
    }

    protected Station(Parcel in) {
        this.name = in.readString();
        this.zone = in.readString();
        this.additionalInfo = in.readString();
    }

    public static final Creator<Station> CREATOR = new Creator<Station>() {
        @Override
        public Station createFromParcel(Parcel in) {
            return new Station(in);
        }

        @Override
        public Station[] newArray(int size) {
            return new Station[size];
        }
    };

    public boolean setName(String name) {
        this.name = name;
        return true;
    }
    public boolean setZone(String zone) {
        this.zone = zone;
        return true;
    }

    public boolean setInfo(String info) {
        this.additionalInfo = info;
        return true;
    }
    public String getName() {
        return this.name;
    }
    public String getZone() {
        return this.zone;
    }
    public String getAddInfo() {return this.additionalInfo;};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(zone);
        dest.writeString(additionalInfo);
    }


    public static Comparator<Station> COMPARE_BY_NAME = new Comparator<Station>() {
        public int compare(Station st1, Station st2) {
            return st1.getName().compareTo(st2.getName());
        }
    };
}

/**
 * A comparator by name, can be used to sort a list of stations alphabetically
 */
class NameComparator implements Comparator<Station> {

    @Override
    public int compare(Station st1, Station st2) {

        return st1.getName().compareTo(st2.getName());
    }
}


