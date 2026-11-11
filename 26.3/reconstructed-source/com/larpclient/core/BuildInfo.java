package com.larpclient.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class BuildInfo {
   private static final Properties properties = new Properties();
   public static final String larpVersion;
   public static final String minecraftVersion;
   public static final String product;
   public static final String edition;
   public static final String loader;
   public static final String channel;
   public static final String flavor;
   public static final String buildId;

   private BuildInfo() {
   }

   static {
      try (InputStream var0 = BuildInfo.class.getResourceAsStream("/larp-version.properties")) {
         if (var0 == null) {
            throw new IllegalStateException("Missing build metadata");
         }

         properties.load(var0);
      } catch (IOException var5) {
         throw new ExceptionInInitializerError(var5);
      }

      larpVersion = properties.getProperty("larpVersion");
      minecraftVersion = properties.getProperty("minecraftVersion");
      product = properties.getProperty("product");
      edition = properties.getProperty("edition");
      loader = properties.getProperty("loader");
      channel = properties.getProperty("channel");
      flavor = properties.getProperty("flavor");
      buildId = properties.getProperty("buildId");
   }
}
