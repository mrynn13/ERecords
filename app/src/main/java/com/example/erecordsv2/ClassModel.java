package com.example.erecordsv2;

import java.util.Comparator;

public class ClassModel {

    String subjectCode,subjectName,subjectSched,subjectRoom;
    String prelimActTotal,prelimAssTotal,prelimQTotal,prelimExtotal;
    String midtermActTotal,midtermAssTotal,midtermQTotal,midtermExtotal;
    String finalsActTotal,finalsAssTotal,finalsQTotal,finalsExtotal;

    ClassModel(){

    }

    public ClassModel(String subjectCode, String subjectName,String subjectSched, String subjectRoom, String prelimActTotal, String prelimAssTotal, String prelimQTotal, String prelimExtotal, String midtermActTotal, String midtermAssTotal, String midtermQTotal, String midtermExtotal, String finalsActTotal, String finalsAssTotal, String finalsQTotal, String finalsExtotal) {
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.subjectSched = subjectSched;
        this.subjectRoom = subjectRoom;
        this.prelimActTotal = prelimActTotal;
        this.prelimAssTotal = prelimAssTotal;
        this.prelimQTotal = prelimQTotal;
        this.prelimExtotal = prelimExtotal;
        this.midtermActTotal = midtermActTotal;
        this.midtermAssTotal = midtermAssTotal;
        this.midtermQTotal = midtermQTotal;
        this.midtermExtotal = midtermExtotal;
        this.finalsActTotal = finalsActTotal;
        this.finalsAssTotal = finalsAssTotal;
        this.finalsQTotal = finalsQTotal;
        this.finalsExtotal = finalsExtotal;
    }

    /*public ClassModel(String subjectCode, String subjectName) {
            this.subjectCode = subjectCode;
            this.subjectName = subjectName;
        }*/
    //a - z compare
    public  static Comparator<ClassModel> ClassNameAZComparator = new Comparator<ClassModel>() {
        @Override
        public int compare( ClassModel c1,  ClassModel c2) {
            return c1.getSubjectName().compareToIgnoreCase(c2.getSubjectName());
        }
    };
    //z - a compare
    public  static Comparator<ClassModel> ClassNameCodeComparator = new Comparator<ClassModel>() {
        @Override
        public int compare( ClassModel c1,  ClassModel c2) {
            return c1.getSubjectCode().compareToIgnoreCase(c2.getSubjectCode());
        }
    };

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getPrelimActTotal() {
        return prelimActTotal;
    }

    public void setPrelimActTotal(String prelimActTotal) {
        this.prelimActTotal = prelimActTotal;
    }

    public String getPrelimAssTotal() {
        return prelimAssTotal;
    }

    public void setPrelimAssTotal(String prelimAssTotal) {
        this.prelimAssTotal = prelimAssTotal;
    }

    public String getPrelimQTotal() {
        return prelimQTotal;
    }

    public void setPrelimQTotal(String prelimQTotal) {
        this.prelimQTotal = prelimQTotal;
    }

    public String getPrelimExtotal() {
        return prelimExtotal;
    }

    public void setPrelimExtotal(String prelimExtotal) {
        this.prelimExtotal = prelimExtotal;
    }

    public String getMidtermActTotal() {
        return midtermActTotal;
    }

    public void setMidtermActTotal(String midtermActTotal) {
        this.midtermActTotal = midtermActTotal;
    }

    public String getMidtermAssTotal() {
        return midtermAssTotal;
    }

    public void setMidtermAssTotal(String midtermAssTotal) {
        this.midtermAssTotal = midtermAssTotal;
    }

    public String getMidtermQTotal() {
        return midtermQTotal;
    }

    public void setMidtermQTotal(String midtermQTotal) {
        this.midtermQTotal = midtermQTotal;
    }

    public String getMidtermExtotal() {
        return midtermExtotal;
    }

    public void setMidtermExtotal(String midtermExtotal) {
        this.midtermExtotal = midtermExtotal;
    }

    public String getFinalsActTotal() {
        return finalsActTotal;
    }

    public void setFinalsActTotal(String finalsActTotal) {
        this.finalsActTotal = finalsActTotal;
    }

    public String getFinalsAssTotal() {
        return finalsAssTotal;
    }

    public void setFinalsAssTotal(String finalsAssTotal) {
        this.finalsAssTotal = finalsAssTotal;
    }

    public String getFinalsQTotal() {
        return finalsQTotal;
    }

    public void setFinalsQTotal(String finalsQTotal) {
        this.finalsQTotal = finalsQTotal;
    }

    public String getFinalsExtotal() {
        return finalsExtotal;
    }

    public void setFinalsExtotal(String finalsExtotal) {
        this.finalsExtotal = finalsExtotal;
    }

    public String getSubjectSched() {
        return subjectSched;
    }

    public void setSubjectSched(String subjectSched) {
        this.subjectSched = subjectSched;
    }

    public String getSubjectRoom() {
        return subjectRoom;
    }

    public void setSubjectRoom(String subjectRoom) {
        this.subjectRoom = subjectRoom;
    }
    /*
    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }*/
}
