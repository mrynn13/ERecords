package com.example.erecordsv2;

public class EventModel {
    String eventName,eventDate,eventId;
    public EventModel() {
    }

    public EventModel(String eventName, String eventDate, String eventId) {
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
