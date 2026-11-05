package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class r4 {
   private static final Logger o = LoggerFactory.getLogger("Larp");
   private static final Set<String> V = new HashSet<>();

   public static void V(String var0, String var1, Object... var2) {
      lf.v(var0, "Info", null);
      o.info("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void g(String var0, String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         V(var0, var1, var2);
      } else {
         o.debug("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void m(String var0, String var1, Object... var2) {
      lf.v(var0, "Warning", null);
      o.warn("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void r(String var0, String var1, Throwable var2) {
      lf.v(var0, "Error", var2);
      o.error("[Larp/" + var0 + "] " + var1, var2);
   }

   public static void T(String var0, String var1, Throwable var2) {
      if (V.add(var0 + var1)) {
         r(var0, var1, var2);
      }
   }
}
