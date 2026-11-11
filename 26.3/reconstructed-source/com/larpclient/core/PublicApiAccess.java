package com.larpclient.core;

import java.net.URI;
import java.net.http.HttpRequest.Builder;

public final class PublicApiAccess {
   public static PublicApiAccess anonymous() {
      return new PublicApiAccess();
   }

   public void applyAuth(Builder var1, String var2, URI var3, byte[] var4) {
   }

   public String getToken() {
      return "";
   }
}
