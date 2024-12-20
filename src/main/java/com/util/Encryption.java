package com.util;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import com.enums.Columns;
import com.enums.Configuration;
import com.enums.Configurations;
import com.enums.Operators;
import com.enums.Table;
import com.queryLayer.Insert;
import com.queryLayer.Select;

public class Encryption {
	
	public static String EncryptionKey = null;

	public void GenEncKeyAndStoreInDb() {
		if(EncryptionKey == null) {
			EncryptionKey =	checkForEncKey();
			
			if(EncryptionKey == null) {
				try {
					KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
					keyGenerator.init(256);
					SecretKey skey =  keyGenerator.generateKey();
					
					Insert i = new Insert();
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					
				}
			}
		}
		
		
	}
	
	
	public static String checkForEncKey() {
		Select cheEncKey = new Select();
		cheEncKey.table(Table.Configurations)
		.condition(Configurations.NAME, Operators.Equals, "Aes_Key");
		List<HashMap<Columns, Object>> res =cheEncKey.executeQuery();
		if(res.size()>0) {
			return null;
		}
		return res.get(0).get(Configurations.VALUE).toString();
	}
	
	
	
}
