package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class zq {
   private static final Properties Q = new Properties();
   public static final String x;
   public static final String C;
   public static final String f;
   public static final String X;
   public static final String k;
   public static final String T;
   public static final String R;
   public static final String w;

   private zq() {
   }

   static {
      try (InputStream var0 = zq.class.getResourceAsStream("/larp-version.properties")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing build metadata");
         }

         Q.load(var0);
      } catch (IOException var5) {
         throw new ExceptionInInitializerError(var5);
      }

      x = Q.getProperty("larpVersion");
      C = Q.getProperty("minecraftVersion");
      f = Q.getProperty("product");
      X = Q.getProperty("edition");
      k = Q.getProperty("loader");
      T = Q.getProperty("channel");
      R = Q.getProperty("flavor");
      w = Q.getProperty("buildId");
   }
}
