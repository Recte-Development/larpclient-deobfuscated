package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public final class q8 {
   private static final Logger Z = LoggerFactory.getLogger("Larp");
   private static final Set<String> y = new HashSet<>();
   private static final boolean i = T();
   private static final Pattern S = Pattern.compile("\\b\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?\\b|\\[[0-9A-Fa-f:]{2,}\\](?::\\d{1,5})?|\\bhttps?://\\S+");

   private static boolean T() {
      if (Boolean.getBoolean("larp.debug")) {
         return false;
      }

      try {
         return "protected".equals(sc.K);
      } catch (Throwable var1) {
         return false;
      }
   }

   public static boolean S() {
      return i;
   }

   public static void G(String var0, String var1, Object... var2) {
      f.w(var0, "Info", null);
      if (i) {
         Z.debug("[Larp/" + var0 + "] " + var1, var2);
      } else {
         Z.info("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void v(String var0, String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         G(var0, var1, var2);
      } else {
         Z.debug("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void L(String var0, String var1, Object... var2) {
      f.w(var0, "Warning", null);
      if (i) {
         Z.warn("[Larp/" + var0 + "] " + z(var1, var2, null));
      } else {
         Z.warn("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void u(String var0, String var1, Throwable var2) {
      f.w(var0, "Error", var2);
      if (i) {
         Z.error("[Larp/" + var0 + "] " + z(var1, new Object[0], var2));
      } else {
         Z.error("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void O(String var0, String var1, Throwable var2) {
      if (y.add(var0 + var1)) {
         u(var0, var1, var2);
      }
   }

   public static String z(String var0, Object[] var1, Throwable var2) {
      FormattingTuple var3 = MessageFormatter.arrayFormat(var0, var1);
      Throwable var4 = var2 != null ? var2 : var3.getThrowable();
      String var5 = var3.getMessage();
      if (var4 != null) {
         var5 = var5 + " (" + var4.getClass().getSimpleName() + ")";
      }

      return f.Z(S.matcher(var5).replaceAll("[address]"));
   }
}
