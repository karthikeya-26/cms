package com.dbObjects;

public class ContactMailsObj extends ResultObject{
	private Integer contact_id;
    private  String mail;
    
	public Integer getContact_id() {
		return contact_id;
	}
	public void setContact_id(Integer contact_id) {
		this.contact_id = contact_id;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	@Override
	public String toString() {
		return "ContactMailsObj [contact_id=" + contact_id + ", mail=" + mail + "]";
	}
	
}
