package com.larpclient.module;

import com.larpclient._q;

public class TimeChangerModule extends Module {
   private final b v = this.addSetting(new b("Time", "Local clock: 6000 is noon, 18000 is midnight", 6000.0, 0.0, 24000.0, 500.0));
   private static TimeChangerModule L;

   public TimeChangerModule() {
      super("TimeChanger", "Set the time shown in your world", Category.RENDER, -1);
      L = this;
   }

   public static TimeChangerModule p() {
      return L;
   }

   public long P() {
      return (long)this.v.getValue().doubleValue();
   }

   public long c(long var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? Z(var1, this.P()) : var1;
      } else {
         return var1;
      }
   }

   public static long Z(long var0, long var2) {
      return Math.floorDiv(var0, 24000L) * 24000L + Math.floorMod(var2, 24000L);
   }

   @Override
   public void onEnable() {
      _q.z("TimeChanger", "Local overworld clock override enabled; time={}; server clock retained", this.P());
   }

   @Override
   public void onDisable() {
      _q.c("TimeChanger", "Local clock override released; native time resumes");
   }
}
