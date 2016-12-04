package com.androidworks.nikhil.infymoney.model;

/**
 * Created by Nikhil on 23-Nov-16.
 */
public class SMS {

    private String address;
    private String messageBody;
    private long longDate;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public long getLongDate() {
        return longDate;
    }

    public void setLongDate(long longDate) {
        this.longDate = longDate;
    }
}
