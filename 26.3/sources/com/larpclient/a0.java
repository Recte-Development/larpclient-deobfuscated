package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class a0 {
   private static final Properties D = new Properties();
   public static final String r;
   public static final String N;
   public static final String t;
   public static final String Q;
   public static final String P;
   public static final String M;
   public static final String B;
   public static final String V;

   private a0() {
   }

   static {
      try (InputStream var0 = a0.class.getResourceAsStream("/larp-version.properties")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing build metadata");
         }

         D.load(var0);
      } catch (IOException var5) {
         throw new ExceptionInInitializerError(var5);
      }

      r = D.getProperty("larpVersion");
      N = D.getProperty("minecraftVersion");
      t = D.getProperty("product");
      Q = D.getProperty("edition");
      P = D.getProperty("loader");
      M = D.getProperty("channel");
      B = D.getProperty("flavor");
      V = D.getProperty("buildId");
   }
}
