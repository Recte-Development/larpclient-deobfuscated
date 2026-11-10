package com.larpclient.module;

public class TimeChangerModule extends Module {
   private final e G = this.addSetting(new e("Time", "Local clock: 6000 is noon, 18000 is midnight", 6000.0, 0.0, 24000.0, 500.0));
   private static TimeChangerModule D;

   public TimeChangerModule() {
      super("TimeChanger", "Set the time shown in your world", Category.RENDER, -1);
      D = this;
   }

   public static TimeChangerModule H() {
      return D;
   }

   public long e() {
      return (long)this.G.getValue().doubleValue();
   }

   public long k(long var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? m(var1, this.e()) : var1;
      } else {
         return var1;
      }
   }

   public static long m(long var0, long var2) {
      return Math.floorDiv(var0, 24000L) * 24000L + Math.floorMod(var2, 24000L);
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A("TimeChanger", "Local overworld clock override enabled; time={}; server clock retained", this.e());
   }

   @Override
   public void onDisable() {
      com.larpclient.h.U("TimeChanger", "Local clock override released; native time resumes");
   }
}
