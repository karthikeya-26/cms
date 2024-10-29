package com.dbObjects;

public class UserMailsObj  extends ResultObject{
	private  Integer mail_id ;
	private  Integer user_id ;
	private  String mail ;
	private  Integer is_primary ;
    
	public Integer getMail_id() {
		return mail_id;
	}
	public void setMail_id(Integer mail_id) {
		this.mail_id = mail_id;
	}
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public Integer getIs_primary() {
		return is_primary;
	}
	public void setIs_primary(Integer is_primary) {
		this.is_primary = is_primary;
	}
	@Override
	public String toString() {
		return "UserMailsObj [mail_id=" + mail_id + ", user_id=" + user_id + ", mail=" + mail + ", is_primary="
				+ is_primary + "]";
	}
}