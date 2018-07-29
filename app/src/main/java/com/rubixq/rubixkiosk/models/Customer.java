package com.rubixq.rubixkiosk.models;

public class Customer {
    private String id;
    private String msisdn;
    private String queueId;
    private String ticketNumber;

    public Customer(String id, String msisdn, String queueId, String ticketNumber) {
        this.id = id;
        this.msisdn = msisdn;
        this.queueId = queueId;
        this.ticketNumber = ticketNumber;
    }

    public String getId() {
        return id;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public String getQueueId() {
        return queueId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

}
