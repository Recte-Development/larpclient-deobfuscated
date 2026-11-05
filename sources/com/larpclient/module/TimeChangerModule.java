package com.larpclient.module;

import com.larpclient.r4;

public class TimeChangerModule extends Module {
   private final lc S = this.addSetting(new lc("Time", "Local clock: 6000 is noon, 18000 is midnight", 6000.0, 0.0, 24000.0, 500.0));
   private static TimeChangerModule T;

   public TimeChangerModule() {
      super("TimeChanger", "Set the time shown in your world", Category.RENDER, -1);
      T = this;
   }

   public static TimeChangerModule p() {
      return T;
   }

   public long a() {
      return (long)this.S.getValue().doubleValue();
   }

   public static long f(long var0, long var2) {
      return Math.floorDiv(var0, 24000L) * 24000L + Math.floorMod(var2, 24000L);
   }

   @Override
   public void onEnable() {
      r4.V("TimeChanger", "Local overworld clock override enabled; time={}; server clock retained", this.a());
   }

   @Override
   public void onDisable() {
      r4.g("TimeChanger", "Local clock override released; native time resumes");
   }
}
