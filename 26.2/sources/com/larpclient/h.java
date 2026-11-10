package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public final class h {
   private static final Logger S = LoggerFactory.getLogger("Larp");
   private static final Set<String> N = new HashSet<>();
   private static final boolean F = x();
   private static final Pattern o = Pattern.compile("\\b\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?\\b|\\[[0-9A-Fa-f:]{2,}\\](?::\\d{1,5})?|\\bhttps?://\\S+");

   private static boolean x() {
      if (Boolean.getBoolean("larp.debug")) {
         return false;
      }

      try {
         return "protected".equals(z0.Y);
      } catch (Throwable var1) {
         return false;
      }
   }

   public static boolean z() {
      return F;
   }

   public static void A(String var0, String var1, Object... var2) {
      lo.B(var0, "Info", null);
      if (F) {
         S.debug("[Larp/" + var0 + "] " + var1, var2);
      } else {
         S.info("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void U(String var0, String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         A(var0, var1, var2);
      } else {
         S.debug("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void g(String var0, String var1, Object... var2) {
      lo.B(var0, "Warning", null);
      if (F) {
         S.warn("[Larp/" + var0 + "] " + B(var1, var2, null));
      } else {
         S.warn("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void m(String var0, String var1, Throwable var2) {
      lo.B(var0, "Error", var2);
      if (F) {
         S.error("[Larp/" + var0 + "] " + B(var1, new Object[0], var2));
      } else {
         S.error("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void w(String var0, String var1, Throwable var2) {
      if (N.add(var0 + var1)) {
         m(var0, var1, var2);
      }
   }

   public static String B(String var0, Object[] var1, Throwable var2) {
      FormattingTuple var3 = MessageFormatter.arrayFormat(var0, var1);
      Throwable var4 = var2 != null ? var2 : var3.getThrowable();
      String var5 = var3.getMessage();
      if (var4 != null) {
         var5 = var5 + " (" + var4.getClass().getSimpleName() + ")";
      }

      return lo.l(o.matcher(var5).replaceAll("[address]"));
   }
}
