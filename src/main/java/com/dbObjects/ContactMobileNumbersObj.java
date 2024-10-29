package com.dbObjects;

public class ContactMobileNumbersObj extends ResultObject{
	private Integer contact_id ;
	private Long number ;
    
	public Integer getContact_id() {
		return contact_id;
	}
	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	@Override
	public String toString() {
		return "ContactMobileNumbersObj [contact_id=" + contact_id + ", number=" + number + "]";
	}
}