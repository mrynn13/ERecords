package com.example.erecordsv2;

import java.util.Comparator;

public class SubjectModel {

    String ClassName,ClassCode,Instructor;

    public SubjectModel(){

    }

    public SubjectModel(String className, String classCode,String instructor) {
        ClassName = className;
        ClassCode = classCode;
        Instructor = instructor;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getClassCode() {
        return ClassCode;
    }

    public void setClassCode(String classCode) {
        ClassCode = classCode;
    }

    public String getInstructor() {
        return Instructor;
    }

    public void setInstructor(String instructor) {
        Instructor = instructor;
    }

    //a - z compare
    public  static Comparator<SubjectModel> ClassNameAZComparator = (c1, c2) -> c1.getClassName().compareToIgnoreCase(c2.getClassName());
    //z - a compare
    public  static Comparator<SubjectModel> ClassNameZAComparator = (c1, c2) -> c2.getClassName().compareToIgnoreCase(c1.getClassName());

}
