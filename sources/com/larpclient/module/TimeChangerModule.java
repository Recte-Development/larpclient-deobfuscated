package com.larpclient.module;

import com.larpclient.mt;

public class TimeChangerModule extends Module {
   private final d9 H = this.addSetting(new d9("Time", "Local clock: 6000 is noon, 18000 is midnight", 6000.0, 0.0, 24000.0, 500.0));
   private static TimeChangerModule a;

   public TimeChangerModule() {
      super("TimeChanger", "Set the time shown in your world", Category.RENDER, -1);
      a = this;
   }

   public static TimeChangerModule u() {
      return a;
   }

   public long D() {
      return (long)this.H.P().doubleValue();
   }

   public static long g(long var0, long var2) {
      return Math.floorDiv(var0, 24000L) * 24000L + Math.floorMod(var2, 24000L);
   }

   @Override
   public void onEnable() {
      mt.K("TimeChanger", "Local overworld clock override enabled; time={}; server clock retained", this.D());
   }

   @Override
   public void onDisable() {
      mt.U("TimeChanger", "Local clock override released; native time resumes");
   }
}
