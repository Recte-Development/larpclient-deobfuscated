package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class mt {
   private static final Logger y = LoggerFactory.getLogger("Larp");
   private static final Set<String> v = new HashSet<>();

   public static void K(String var0, String var1, Object... var2) {
      wz.H(var0, "Info", null);
      y.info("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void U(String var0, String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         K(var0, var1, var2);
      } else {
         y.debug("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void T(String var0, String var1, Object... var2) {
      wz.H(var0, "Warning", null);
      y.warn("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void P(String var0, String var1, Throwable var2) {
      wz.H(var0, "Error", var2);
      y.error("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void B(String var0, String var1, Throwable var2) {
      if (v.add(var0 + var1)) {
         P(var0, var1, var2);
      }
   }
}
