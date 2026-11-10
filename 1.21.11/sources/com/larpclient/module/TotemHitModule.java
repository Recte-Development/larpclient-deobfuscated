package com.larpclient.module;

import com.larpclient.cr;
import com.larpclient.fs;
import com.larpclient.q8;
import java.util.Set;
import net.minecraft.class_1297;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_3489;
import net.minecraft.class_746;

public final class TotemHitModule extends Module {
   private static TotemHitModule d;
   private final lo i = this.addSetting(new lo("Switch Delay", "Milliseconds before returning to your totem after the sword hit", 30.0, 10.0, 100.0, 1.0));
   private final fs Y = new fs(this);
   private long C;
   private String V = "Ready";

   public TotemHitModule() {
      super("Totem Hit", "Hit with your sword while holding a totem, then switch back", Category.COMBAT, -1);
      d = this;
   }

   public static int r(class_746 var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         if (var0.method_31548().method_5438(var1).method_31573(class_3489.field_42611)) {
            return var1;
         }
      }

      return -1;
   }

   public static boolean H(long var0, long var2, double var4) {
      return var2 - var0 >= (long)(var4 * 1000000.0);
   }

   public static void h(class_746 var0, class_1297 var1) {
      if (d != null && d.isEnabled()) {
         TotemHitModule var2 = d;
         if (var2 != null && var2.isRuntimeActive() && var1 != null && !cr.C()) {
            class_310 var3 = getMc();
            if (var0 == var3.field_1724 && !var2.Y.I() && var0.method_6047().method_31574(class_1802.field_8288)) {
               int var4 = r(var0);
               if (var4 < 0) {
                  var2.V = "No sword in hotbar";
               } else if (!var2.Y.m(var0, var4)) {
                  var2.V = "Hotbar busy";
               } else {
                  var2.C = System.nanoTime();
                  var2.V = "Sword hit";
                  q8.v("TotemHit", "Hit {} with hotbar sword {}", var1.method_5628(), var4 + 1);
               }
            }
         }
      }
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.V : "";
   }

   @Override
   public void onEnable() {
      this.V = "Ready";
      q8.G("TotemHit", "Enabled; switch delay {} ms", this.i.getValue().intValue());
   }

   @Override
   public void onDisable() {
      if (this.Y.I()) {
         this.Y.o(getMc(), true);
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.Y.I();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         int var6 = H(this.C, System.nanoTime(), this.i.getValue());
         boolean var4 = false;
         byte var3 = (byte)var6;
         var6 = Integer.signum(var3);
         if ((var6 | -var6) >>> 31 != 0) {
            this.Y.o(mc, true);
            this.V = "Ready";
            return;
         }
      }
   }
}
