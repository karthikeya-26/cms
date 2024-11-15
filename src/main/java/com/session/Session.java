// Source code is decompiled from a .class file using FernFlower decompiler.
package com.session;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Session {
   private static SecureRandom sr = new SecureRandom();

   public Session() {
   }

   public static String getSessionId() {
      return (new BigInteger(130, sr)).toString(32);
   }
}
