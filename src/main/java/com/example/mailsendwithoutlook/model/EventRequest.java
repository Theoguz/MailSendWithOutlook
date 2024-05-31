package com.example.mailsendwithoutlook.model;

import com.microsoft.graph.models.*;

import java.util.LinkedList;


public class EventRequest {

    private String subject;
    private ItemBody body;
    private DateTimeTimeZone start;
    private DateTimeTimeZone end;
    private Location location;
    private LinkedList<Attendee> attendees;
    private Boolean isOnlineMeeting;
    private OnlineMeetingProviderType onlineMeetingProvider;
    private OnlineMeetingInfo onlineMeetingInfo;

    public OnlineMeetingInfo getOnlineMeetingInfo() {
        return onlineMeetingInfo;
    }

    public void setOnlineMeetingInfo(OnlineMeetingInfo onlineMeetingInfo) {
        this.onlineMeetingInfo = onlineMeetingInfo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ItemBody getBody() {
        return body;
    }

    public void setBody(ItemBody body) {
        this.body = body;
    }

    public DateTimeTimeZone getStart() {
        return start;
    }

    public void setStart(DateTimeTimeZone start) {
        this.start = start;
    }

    public DateTimeTimeZone getEnd() {
        return end;
    }

    public void setEnd(DateTimeTimeZone end) {
        this.end = end;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LinkedList<Attendee> getAttendees() {
        return attendees;
    }

    public void setAttendees(LinkedList<Attendee> attendees) {
        this.attendees = attendees;
    }

    public Boolean getOnlineMeeting() {
        return isOnlineMeeting;
    }

    public void setOnlineMeeting(Boolean onlineMeeting) {
        isOnlineMeeting = onlineMeeting;
    }

    public OnlineMeetingProviderType getOnlineMeetingProvider() {
        return onlineMeetingProvider;
    }

    public void setOnlineMeetingProvider(OnlineMeetingProviderType onlineMeetingProvider) {
        this.onlineMeetingProvider = onlineMeetingProvider;
    }
}
