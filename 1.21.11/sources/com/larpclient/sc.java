package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class sc {
   private static final Properties m = new Properties();
   public static final String v;
   public static final String E;
   public static final String L;
   public static final String J;
   public static final String Y;
   public static final String g;
   public static final String K;
   public static final String A;

   private sc() {
   }

   static {
      try (InputStream var0 = sc.class.getResourceAsStream("/larp-version.properties")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing build metadata");
         }

         m.load(var0);
      } catch (IOException var5) {
         throw new ExceptionInInitializerError(var5);
      }

      v = m.getProperty("larpVersion");
      E = m.getProperty("minecraftVersion");
      L = m.getProperty("product");
      J = m.getProperty("edition");
      Y = m.getProperty("loader");
      g = m.getProperty("channel");
      K = m.getProperty("flavor");
      A = m.getProperty("buildId");
   }
}
