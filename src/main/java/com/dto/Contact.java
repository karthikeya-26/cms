package com.dto;

import java.util.List;


import com.dbObjects.*;

public class Contact {
	
	public ContactsObj contactDetails;
	public List<ContactMobileNumbersObj> numbers;
	public List<ContactMailsObj> mails;
	public List<UserGroupsObj> groups;
	@Override
	public String toString() {
		return "Contact [contactDetails=" + contactDetails + ", numbers=" + numbers + ", mails=" + mails + ", groups="
				+ groups + "]";
	}
	
	
	
}


