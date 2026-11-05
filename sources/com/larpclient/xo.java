package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class xo {
   private static final Properties v = new Properties();
   public static final String y;
   public static final String D;
   public static final String h;
   public static final String X;
   public static final String T;
   public static final String A;
   public static final String i;
   public static final String z;

   private xo() {
   }

   static {
      try (InputStream var0 = xo.class.getResourceAsStream("/larp-version.properties")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing build metadata");
         }

         v.load(var0);
      } catch (IOException var5) {
         throw new ExceptionInInitializerError(var5);
      }

      y = v.getProperty("larpVersion");
      D = v.getProperty("minecraftVersion");
      h = v.getProperty("product");
      X = v.getProperty("edition");
      T = v.getProperty("loader");
      A = v.getProperty("channel");
      i = v.getProperty("flavor");
      z = v.getProperty("buildId");
   }
}
