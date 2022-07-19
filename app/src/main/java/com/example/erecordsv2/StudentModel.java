package com.example.erecordsv2;

import java.util.Comparator;

public class StudentModel {


    String sName,sId,sClass,pActCount,mActCount,fActCount,sToken;
    String pActAverage,pAssAverage,pQAverage,pEAverage;
    String mActAverage,mAssAverage,mQAverage,mEAverage;
    String fActAverage,fAssAverage,fQAverage,fEAverage;

    String pOverAllAverage,mOverAllAverage,fOverAllAverage,overallAverage;



    StudentModel(){
        //safe
    }

    public StudentModel(String sName, String sId, String sClass, String pActCount, String mActCount, String fActCount, String sToken, String pActAverage, String pAssAverage, String pQAverage, String pEAverage, String mActAverage, String mAssAverage, String mQAverage, String mEAverage, String fActAverage, String fAssAverage, String fQAverage, String fEAverage, String pOverAllAverage, String mOverAllAverage, String fOverAllAverage, String overallAverage) {
        this.sName = sName;
        this.sId = sId;
        this.sClass = sClass;
        this.pActCount = pActCount;
        this.mActCount = mActCount;
        this.fActCount = fActCount;
        this.sToken = sToken;
        this.pActAverage = pActAverage;
        this.pAssAverage = pAssAverage;
        this.pQAverage = pQAverage;
        this.pEAverage = pEAverage;
        this.mActAverage = mActAverage;
        this.mAssAverage = mAssAverage;
        this.mQAverage = mQAverage;
        this.mEAverage = mEAverage;
        this.fActAverage = fActAverage;
        this.fAssAverage = fAssAverage;
        this.fQAverage = fQAverage;
        this.fEAverage = fEAverage;
        this.pOverAllAverage = pOverAllAverage;
        this.mOverAllAverage = mOverAllAverage;
        this.fOverAllAverage = fOverAllAverage;
        this.overallAverage = overallAverage;
    }

    /*  public StudentModel(String sName, String sId,String sClass,String pActCount,String mActCount,String fActCount,String sToken) {
            this.sName = sName;
            this.sId = sId;
            this.sClass = sClass;
         //   this.sCountAct = sCountAct;
            this.pActCount = pActCount;
            this.mActCount = mActCount;
            this.fActCount = fActCount;
            this.sToken = sToken;

        }
    */
    //a - z compare
    public  static Comparator<StudentModel> StudentNameAZComparator = new Comparator<StudentModel>() {
        @Override
        public int compare( StudentModel s1,  StudentModel s2) {
            return s1.getsName().compareToIgnoreCase(s2.getsName());
        }
    };
    //z - a compare
    public  static Comparator<StudentModel> StudentNameZAComparator = new Comparator<StudentModel>() {
        @Override
        public int compare( StudentModel s1,  StudentModel s2) {
            return s2.getsName().compareToIgnoreCase(s1.getsName());
        }
    };

    public String getpActCount() {
        return pActCount;
    }

    public void setpActCount(String pActCount) {
        this.pActCount = pActCount;
    }

    public String getmActCount() {
        return mActCount;
    }

    public void setmActCount(String mActCount) {
        this.mActCount = mActCount;
    }

    public String getfActCount() {
        return fActCount;
    }

    public void setfActCount(String fActCount) {
        this.fActCount = fActCount;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getsClass() {
        return sClass;
    }

    public void setsClass(String sId) {
        this.sClass = sClass;
    }

    public String getsToken() {
        return sToken;
    }

    public void setsToken(String sToken) {
        this.sToken = sToken;
    }

    public String getpActAverage() {
        return pActAverage;
    }

    public void setpActAverage(String pActAverage) {
        this.pActAverage = pActAverage;
    }

    public String getpAssAverage() {
        return pAssAverage;
    }

    public void setpAssAverage(String pAssAverage) {
        this.pAssAverage = pAssAverage;
    }

    public String getpQAverage() {
        return pQAverage;
    }

    public void setpQAverage(String pQAverage) {
        this.pQAverage = pQAverage;
    }

    public String getpEAverage() {
        return pEAverage;
    }

    public void setpEAverage(String pEAverage) {
        this.pEAverage = pEAverage;
    }

    public String getmActAverage() {
        return mActAverage;
    }

    public void setmActAverage(String mActAverage) {
        this.mActAverage = mActAverage;
    }

    public String getmAssAverage() {
        return mAssAverage;
    }

    public void setmAssAverage(String mAssAverage) {
        this.mAssAverage = mAssAverage;
    }

    public String getmQAverage() {
        return mQAverage;
    }

    public void setmQAverage(String mQAverage) {
        this.mQAverage = mQAverage;
    }

    public String getmEAverage() {
        return mEAverage;
    }

    public void setmEAverage(String mEAverage) {
        this.mEAverage = mEAverage;
    }

    public String getfActAverage() {
        return fActAverage;
    }

    public void setfActAverage(String fActAverage) {
        this.fActAverage = fActAverage;
    }

    public String getfAssAverage() {
        return fAssAverage;
    }

    public void setfAssAverage(String fAssAverage) {
        this.fAssAverage = fAssAverage;
    }

    public String getfQAverage() {
        return fQAverage;
    }

    public void setfQAverage(String fQAverage) {
        this.fQAverage = fQAverage;
    }

    public String getfEAverage() {
        return fEAverage;
    }

    public void setfEAverage(String fEAverage) {
        this.fEAverage = fEAverage;
    }

    public String getpOverAllAverage() {
        return pOverAllAverage;
    }

    public void setpOverAllAverage(String pOverAllAverage) {
        this.pOverAllAverage = pOverAllAverage;
    }

    public String getmOverAllAverage() {
        return mOverAllAverage;
    }

    public void setmOverAllAverage(String mOverAllAverage) {
        this.mOverAllAverage = mOverAllAverage;
    }

    public String getfOverAllAverage() {
        return fOverAllAverage;
    }

    public void setfOverAllAverage(String fOverAllAverage) {
        this.fOverAllAverage = fOverAllAverage;
    }

    public String getOverallAverage() {
        return overallAverage;
    }

    public void setOverallAverage(String overallAverage) {
        this.overallAverage = overallAverage;
    }
}
