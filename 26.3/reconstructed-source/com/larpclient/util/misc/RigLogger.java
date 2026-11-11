package com.larpclient.util.misc;

public final class RigLogger {
   private final String tag;

   private RigLogger(String var1) {
      this.tag = var1 == null ? "Rig" : "Rig/" + var1;
   }

   public static RigLogger forModule(String var0) {
      return new RigLogger(var0);
   }

   public void info(String var1, Object... var2) {
      LarpLog.debug(this.tag, var1, nonNullArgs(var2));
   }

   public void debug(String var1, Object... var2) {
      LarpLog.info(this.tag, var1, nonNullArgs(var2));
   }

   public void warn(String var1, Object... var2) {
      LarpLog.warn(this.tag, var1, nonNullArgs(var2));
   }

   public void warnRaw(String var1, Object... var2) {
      LarpLog.warn(this.tag, var1, var2);
   }

   private static Object[] nonNullArgs(Object[] var0) {
      return var0 == null ? new Object[0] : var0;
   }
}
