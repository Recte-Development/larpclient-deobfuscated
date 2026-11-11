package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.util.misc.LarpLog;

public class TimeChangerModule extends Module {
   private final NumberSetting time = this.addSetting(new NumberSetting("Time", "Local clock: 6000 is noon, 18000 is midnight", 6000.0, 0.0, 24000.0, 500.0));
   private static TimeChangerModule instance;

   public TimeChangerModule() {
      super("TimeChanger", "Set the time shown in your world", Category.RENDER, -1);
      instance = this;
   }

   public static TimeChangerModule getInstance() {
      return instance;
   }

   public long getTime() {
      return (long)this.time.getValue().doubleValue();
   }

   public long getAdjustedTime(long var1) {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? applyTimeOfDay(var1, this.getTime()) : var1;
      } else {
         return var1;
      }
   }

   public static long applyTimeOfDay(long var0, long var2) {
      return Math.floorDiv(var0, 24000L) * 24000L + Math.floorMod(var2, 24000L);
   }

   @Override
   public void onEnable() {
      LarpLog.info("TimeChanger", "Local overworld clock override enabled; time={}; server clock retained", this.getTime());
   }

   @Override
   public void onDisable() {
      LarpLog.debug("TimeChanger", "Local clock override released; native time resumes");
   }
}
