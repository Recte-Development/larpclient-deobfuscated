package com.larpclient.update;

import java.util.Locale;
import java.util.Set;

public record Host(String platform, int javaVersion, String loaderVersion) {
   public Host(String platform, int javaVersion, String loaderVersion) {
      if (platform != null
         && Set.of("linux-x64", "windows-x64", "other").contains(platform)
         && javaVersion >= 21
         && javaVersion <= 99
         && loaderVersion != null
         && loaderVersion.length() <= 128
         && loaderVersion.matches("[0-9]+(?:\\.[0-9]+){1,3}(?:[-+][A-Za-z0-9.-]+)?")) {
         this.platform = platform;
         this.javaVersion = javaVersion;
         this.loaderVersion = loaderVersion;
      } else {
         throw new IllegalArgumentException("Invalid update host");
      }
   }

   public static Host current(String var0) {
      String var1 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
      String var2 = System.getProperty("os.arch", "").toLowerCase(Locale.ROOT);
      boolean var3 = var2.equals("amd64") || var2.equals("x86_64");
      String var4 = var3 && var1.startsWith("windows") ? "windows-x64" : (var3 && var1.equals("linux") ? "linux-x64" : "other");
      return new Host(var4, Runtime.version().feature(), var0);
   }
}
