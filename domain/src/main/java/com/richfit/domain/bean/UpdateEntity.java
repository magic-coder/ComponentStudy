package com.richfit.domain.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by monday on 2016/3/14.
 */
public class UpdateEntity implements Parcelable{

    public String appVersion;
    public String appUpdateDesc;
    public String appDownloadUrl;
    public String appName;
    public int appNum;

    protected UpdateEntity(Parcel in) {
        appVersion = in.readString();
        appUpdateDesc = in.readString();
        appDownloadUrl = in.readString();
        appName = in.readString();
        appNum = in.readInt();
    }

    public static final Creator<UpdateEntity> CREATOR = new Creator<UpdateEntity>() {
        @Override
        public UpdateEntity createFromParcel(Parcel in) {
            return new UpdateEntity(in);
        }

        @Override
        public UpdateEntity[] newArray(int size) {
            return new UpdateEntity[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(appVersion);
        parcel.writeString(appUpdateDesc);
        parcel.writeString(appDownloadUrl);
        parcel.writeString(appName);
        parcel.writeInt(appNum);
    }

    @Override
    public String toString() {
        return "UpdateEntity{" +
                "appVersion='" + appVersion + '\'' +
                ", appUpdateDesc='" + appUpdateDesc + '\'' +
                ", appDownloadUrl='" + appDownloadUrl + '\'' +
                ", appName='" + appName + '\'' +
                ", appNum='" + appNum + '\'' +
                '}';
    }

}
