package io.taucoin.android.wallet.module.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class VersionBean implements Parcelable {

    private int number;
    private String name;
    private String content;
    private boolean isForced;
    private String link;

    private int forcedNum;

    // local param
    private String downloadFilePath;
    private String downloadFileName;


    protected VersionBean(Parcel in) {
        number = in.readInt();
        name = in.readString();
        content = in.readString();
        isForced = in.readByte() != 0;
        link = in.readString();
        forcedNum = in.readInt();
        downloadFilePath = in.readString();
        downloadFileName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeString(name);
        dest.writeString(content);
        dest.writeByte((byte) (isForced ? 1 : 0));
        dest.writeString(link);
        dest.writeInt(forcedNum);
        dest.writeString(downloadFilePath);
        dest.writeString(downloadFileName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VersionBean> CREATOR = new Creator<VersionBean>() {
        @Override
        public VersionBean createFromParcel(Parcel in) {
            return new VersionBean(in);
        }

        @Override
        public VersionBean[] newArray(int size) {
            return new VersionBean[size];
        }
    };

    public String getDownloadFilePath() {
        return downloadFilePath;
    }

    public void setDownloadFilePath(String downloadFilePath) {
        this.downloadFilePath = downloadFilePath;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isForced() {
        return isForced;
    }

    public void setForced(boolean forced) {
        isForced = forced;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIsForced() {
        return isForced;
    }

    public void setIsForced(boolean isForced) {
        this.isForced = isForced;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getForcedNum() {
        return forcedNum;
    }

    public void setForcedNum(int forcedNum) {
        this.forcedNum = forcedNum;
    }

}