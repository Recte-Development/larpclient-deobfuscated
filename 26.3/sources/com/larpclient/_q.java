package com.larpclient;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public final class _q {
   private static final Logger g = LoggerFactory.getLogger("Larp");
   private static final Set<String> l = new HashSet<>();
   private static final boolean c = V();
   private static final Pattern b = Pattern.compile("\\b\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?\\b|\\[[0-9A-Fa-f:]{2,}\\](?::\\d{1,5})?|\\bhttps?://\\S+");

   private static boolean V() {
      if (Boolean.getBoolean("larp.debug")) {
         return false;
      }

      try {
         return "protected".equals(a0.B);
      } catch (Throwable var1) {
         return false;
      }
   }

   public static boolean q() {
      return c;
   }

   public static void z(String var0, String var1, Object... var2) {
      i0.m(var0, "Info", null);
      if (c) {
         g.debug("[Larp/" + var0 + "] " + var1, var2);
      } else {
         g.info("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void c(String var0, String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         z(var0, var1, var2);
      } else {
         g.debug("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void R(String var0, String var1, Object... var2) {
      i0.m(var0, "Warning", null);
      if (c) {
         g.warn("[Larp/" + var0 + "] " + w(var1, var2, null));
      } else {
         g.warn("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void o(String var0, String var1, Throwable var2) {
      i0.m(var0, "Error", var2);
      if (c) {
         g.error("[Larp/" + var0 + "] " + w(var1, new Object[0], var2));
      } else {
         g.error("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void g(String var0, String var1, Throwable var2) {
      if (l.add(var0 + var1)) {
         o(var0, var1, var2);
      }
   }

   public static String w(String var0, Object[] var1, Throwable var2) {
      FormattingTuple var3 = MessageFormatter.arrayFormat(var0, var1);
      Throwable var4 = var2 != null ? var2 : var3.getThrowable();
      String var5 = var3.getMessage();
      if (var4 != null) {
         var5 = var5 + " (" + var4.getClass().getSimpleName() + ")";
      }

      return i0.d(b.matcher(var5).replaceAll("[address]"));
   }
}
