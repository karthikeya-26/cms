package com.dao;


import java.util.ArrayList;
import java.util.List;

import com.dbObjects.ContactMobileNumbersObj;
import com.dbObjects.ResultObject;
import com.enums.ContactMobileNumbers;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.*;

public class ContactMobileNumbersDao {
	
	//SELECT
	public List<ContactMobileNumbersObj> getContactMobileNumbers(Integer contactId){
		List<ContactMobileNumbersObj> numbers = new ArrayList<ContactMobileNumbersObj>();
		Select s = new Select();
		s.table(Table.ContactMobileNumbers)
		.condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contactId.toString());
		List<ResultObject> resultlist = s.executeQuery(ContactMobileNumbersObj.class);
		for(ResultObject number : resultlist) {
			numbers.add((ContactMobileNumbersObj)number);
		}
		System.out.println(s.build());
		return numbers;
	}
	public static void main(String[] args) {
		ContactMobileNumbersDao d = new ContactMobileNumbersDao();
		System.out.println(d.getContactMobileNumbers(2));
	}
	
	//INSERT 
	public boolean addNumbertoContactId(Integer ContactId, Long number) {
		Insert i = new Insert();
		i.table(Table.ContactMobileNumbers)
		.columns(ContactMobileNumbers.CONTACT_ID,ContactMobileNumbers.NUMBER)
		.values(ContactId.toString(),number.toString());
		return i.executeUpdate()>0;
	}
	
	//UPDATE
	public boolean updateNumberWithContactId(Integer contactId, Long number) {
		Update u = new Update();
		u.table(Table.ContactMobileNumbers)
		.columns(ContactMobileNumbers.NUMBER)
		.values(number.toString())
		.condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contactId.toString());
		return u.executeUpdate()==0;
	}
	
	//DELETE 
	public boolean deleteNumberWithContactId(Integer contactId, Long number) {
		Delete d = new Delete();
		d.table(Table.ContactMobileNumbers)
		.condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contactId.toString())
		.condition(ContactMobileNumbers.NUMBER, Operators.Equals, number.toString());
		return d.executeUpdate() >0;
	}
	
	//VALIDATION (NOT NEEDED BUT..)
	public boolean checkNumberWithCotactId(Integer contactId, Long number) {
		Select s = new Select();
		s.table(Table.ContactMobileNumbers).columns(ContactMobileNumbers.CONTACT_ID,ContactMobileNumbers.NUMBER)
		.condition(ContactMobileNumbers.CONTACT_ID, Operators.Equals, contactId.toString())
		.condition(ContactMobileNumbers.NUMBER, Operators.Equals, number.toString());
		return s.executeQuery().size()>0;
	}
	
}
