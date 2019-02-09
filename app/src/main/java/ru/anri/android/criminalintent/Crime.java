package ru.anri.android.criminalintent;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private boolean mRequiresPolice;
    private String mSuspect;

    public Crime(UUID id) {
        this.mId = id;
        this.mDate = new Date();
    }

    public Crime() {
        this(UUID.randomUUID());
    }

    public boolean isRequiresPolice() {
        return this.mRequiresPolice;
    }

    public void setRequiresPolice(boolean mReqPolice) {
        this.mRequiresPolice = mReqPolice;
    }

    public UUID getId() {
        return this.mId;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Date getDate() {
        return this.mDate;
    }

    public void setDate(Date mDate) {
        this.mDate = mDate;
    }

    public boolean isSolved() {
        return this.mSolved;
    }

    public void setSolved(boolean mSolved) {
        this.mSolved = mSolved;
    }

    public long getTime() {
        return this.mDate.getTime();
    }

    public void setTime(long mTime) { this.mDate.setTime(mTime); }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getPhotoFilename() {
        return "IMG" + getId().toString() + ".jpg";
    }
}
