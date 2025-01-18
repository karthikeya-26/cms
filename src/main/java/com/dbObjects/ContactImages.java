package com.dbObjects;

import java.sql.Blob;

public class ContactImages extends ResultObject{
	Integer contact_id;
	Blob low_res;
	Blob high_ress;
	public Integer getContact_id() {
		return contact_id;
	}
	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
	}
	public Blob getLow_res() {
		return low_res;
	}
	public void setLow_res(Blob low_res) {
		this.low_res = low_res;
	}
	public Blob getHigh_ress() {
		return high_ress;
	}
	public void setHigh_ress(Blob high_ress) {
		this.high_ress = high_ress;
	}
	@Override
	public String toString() {
		return "ContactImages [contact_id=" + contact_id + ", low_res=" + low_res + ", high_ress=" + high_ress + "]";
	}
	
	
	
}
