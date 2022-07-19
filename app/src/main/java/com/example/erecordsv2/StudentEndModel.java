package com.example.erecordsv2;

public class StudentEndModel {
    String Name,StudentID,DeviceToken;

    public StudentEndModel() {
    }

    public StudentEndModel(String name, String studentID, String deviceToken) {
        Name = name;
        StudentID = studentID;
        DeviceToken = deviceToken;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getStudentID() {
        return StudentID;
    }

    public void setStudentID(String studentID) {
        StudentID = studentID;
    }

    public String getDeviceToken() {
        return DeviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        DeviceToken = deviceToken;
    }
}
