package com.larpclient.util.misc;

import com.larpclient.core.BuildInfo;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.FormattingTuple;
import org.slf4j.helpers.MessageFormatter;

public final class LarpLog {
   private static final Logger logger = LoggerFactory.getLogger("Larp");
   private static final Set<String> loggedOnce = new HashSet<>();
   private static final boolean protectedBuild = detectProtectedBuild();
   private static final Pattern addressPattern = Pattern.compile(
      "\\b\\d{1,3}(?:\\.\\d{1,3}){3}(?::\\d{1,5})?\\b|\\[[0-9A-Fa-f:]{2,}\\](?::\\d{1,5})?|\\bhttps?://\\S+"
   );

   private static boolean detectProtectedBuild() {
      if (Boolean.getBoolean("larp.debug")) {
         return false;
      }

      try {
         return "protected".equals(BuildInfo.flavor);
      } catch (Throwable var1) {
         return false;
      }
   }

   public static boolean isProtectedBuild() {
      return protectedBuild;
   }

   public static void info(String var0, String var1, Object... var2) {
      BugReportBuilder.logError(var0, "Info", null);
      if (protectedBuild) {
         logger.debug("[Larp/" + var0 + "] " + var1, var2);
      } else {
         logger.info("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void debug(String var0, String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         info(var0, var1, var2);
      } else {
         logger.debug("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void warn(String var0, String var1, Object... var2) {
      BugReportBuilder.logError(var0, "Warning", null);
      if (protectedBuild) {
         logger.warn("[Larp/" + var0 + "] " + sanitize(var1, var2, null));
      } else {
         logger.warn("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void error(String var0, String var1, Throwable var2) {
      BugReportBuilder.logError(var0, "Error", var2);
      if (protectedBuild) {
         logger.error("[Larp/" + var0 + "] " + sanitize(var1, new Object[0], var2));
      } else {
         logger.error("[Larp/" + var0 + "] " + var1, var2);
      }
   }

   public static void errorOnce(String var0, String var1, Throwable var2) {
      if (loggedOnce.add(var0 + var1)) {
         error(var0, var1, var2);
      }
   }

   public static String sanitize(String var0, Object[] var1, Throwable var2) {
      FormattingTuple var3 = MessageFormatter.arrayFormat(var0, var1);
      Throwable var4 = var2 != null ? var2 : var3.getThrowable();
      String var5 = var3.getMessage();
      if (var4 != null) {
         var5 = var5 + " (" + var4.getClass().getSimpleName() + ")";
      }

      return BugReportBuilder.redact(addressPattern.matcher(var5).replaceAll("[address]"));
   }
}
