package com.larpclient.module;

import com.larpclient.q8;

public class TimeChangerModule extends Module {
   private final lo C = this.addSetting(new lo("Time", "Local clock: 6000 is noon, 18000 is midnight", 6000.0, 0.0, 24000.0, 500.0));
   private static TimeChangerModule s;

   public TimeChangerModule() {
      super("TimeChanger", "Set the time shown in your world", Category.RENDER, -1);
      s = this;
   }

   public static TimeChangerModule e() {
      return s;
   }

   public long P() {
      return (long)this.C.getValue().doubleValue();
   }

   public long A(long var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? L(var1, this.P()) : var1;
      } else {
         return var1;
      }
   }

   public static long L(long var0, long var2) {
      return Math.floorDiv(var0, 24000L) * 24000L + Math.floorMod(var2, 24000L);
   }

   @Override
   public void onEnable() {
      q8.G("TimeChanger", "Local overworld clock override enabled; time={}; server clock retained", this.P());
   }

   @Override
   public void onDisable() {
      q8.v("TimeChanger", "Local clock override released; native time resumes");
   }
}
