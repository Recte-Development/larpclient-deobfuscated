package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class z0 {
   private static final Properties W = new Properties();
   public static final String f;
   public static final String n;
   public static final String p;
   public static final String l;
   public static final String q;
   public static final String o;
   public static final String Y;
   public static final String A;

   private z0() {
   }

   static {
      try (InputStream var0 = z0.class.getResourceAsStream("/larp-version.properties")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing build metadata");
         }

         W.load(var0);
      } catch (IOException var5) {
         throw new ExceptionInInitializerError(var5);
      }

      f = W.getProperty("larpVersion");
      n = W.getProperty("minecraftVersion");
      p = W.getProperty("product");
      l = W.getProperty("edition");
      q = W.getProperty("loader");
      o = W.getProperty("channel");
      Y = W.getProperty("flavor");
      A = W.getProperty("buildId");
   }
}
