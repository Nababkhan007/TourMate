package com.example.nabab.tourmate.PojoClass;

public class EventPojo {
    private String travelDestination;
    private String travelEstimatedBudget;
    private String travelStartingDate;
    private String travelEndingDate;
    private String eventId;
    private String travelCaptureMomentDetails;
    private String downloadImageUrlLink;

    public EventPojo(String travelCaptureMomentDetails, String downloadImageUrlLink) {
        this.travelCaptureMomentDetails = travelCaptureMomentDetails;
        this.downloadImageUrlLink = downloadImageUrlLink;
    }

    public EventPojo() {
    }

    public EventPojo(String travelDestination, String travelEstimatedBudget, String travelStartingDate, String travelEndingDate, String eventId) {
        this.travelDestination = travelDestination;
        this.travelEstimatedBudget = travelEstimatedBudget;
        this.travelStartingDate = travelStartingDate;
        this.travelEndingDate = travelEndingDate;
        this.eventId = eventId;
    }

    public String getTravelDestination() {
        return travelDestination;
    }

    public String getTravelEstimatedBudget() {
        return travelEstimatedBudget;
    }

    public String getTravelStartingDate() {
        return travelStartingDate;
    }

    public String getTravelEndingDate() {
        return travelEndingDate;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTravelCaptureMomentDetails() {
        return travelCaptureMomentDetails;
    }

    public String getDownloadImageUrlLink() {
        return downloadImageUrlLink;
    }
}
