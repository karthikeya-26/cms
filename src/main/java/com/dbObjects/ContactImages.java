package com.dbObjects;

import java.sql.Blob;

public class ContactImages extends ResultObject {

    @Column("contact_id")
    private Integer contactId;

    @Column("low_res")
    private Blob lowRes;

    @Column("high_res")
    private Blob highRes;

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Blob getLowRes() {
        return lowRes;
    }

    public void setLowRes(Blob lowRes) {
        this.lowRes = lowRes;
    }

    public Blob getHighRes() {
        return highRes;
    }

    public void setHighRes(Blob highRes) {
        this.highRes = highRes;
    }

    @Override
    public String toString() {
        return "ContactImages [contactId=" + contactId + ", lowRes=" + lowRes + ", highRes=" + highRes + "]";
    }
}
