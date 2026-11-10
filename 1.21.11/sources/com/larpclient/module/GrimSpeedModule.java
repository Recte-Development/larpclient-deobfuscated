package com.larpclient.module;

import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.sq;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_10185;
import net.minecraft.class_10255;
import net.minecraft.class_1297;
import net.minecraft.class_1301;
import net.minecraft.class_1309;
import net.minecraft.class_1420;
import net.minecraft.class_1453;
import net.minecraft.class_1531;
import net.minecraft.class_1688;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_638;
import net.minecraft.class_746;

public final class GrimSpeedModule extends Module {
   private static GrimSpeedModule V;
   private final lo p = this.addSetting(new lo("Collision Boost", "Horizontal impulse per colliding entity, up to 0.08 total", 0.02, 0.01, 0.06, 0.01) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.2f", this.getValue());
      }
   });
   private final lo B = this.addSetting(new lo("Collision Limit", "Maximum contributing entities", 1.0, 1.0, 3.0, 1.0));
   private final l3 f = this.addSetting(new l3("Sprint Jump", "Sprint and jump normally while moving forward, including in open space", true));
   private int e;
   private String C = "Waiting for collision";
   private class_746 G;
   private class_638 Q;
   private int H = -1;
   private class_238 D;
   private boolean K;

   public GrimSpeedModule() {
      super("Grim Speed", "Native sprint jumps with an experimental entity collision boost", Category.MOVEMENT, -1);
      V = this;
   }

   public static double U(double var0, int var2) {
      return !Double.isFinite(var0) ? 0.0 : Math.min(0.08, Math.max(0.0, var0) * Math.max(0, var2));
   }

   public static class_238 y(class_238 var0, class_238 var1) {
      return var0.method_991(var1).method_1014(0.19);
   }

   public static class_243 j(float var0, float var1, float var2) {
      if (Float.isFinite(var0) && Float.isFinite(var1) && Float.isFinite(var2)) {
         double var3 = Math.toRadians(var2);
         return new class_243(Math.cos(var3) * var0 - Math.sin(var3) * var1, 0.0, Math.cos(var3) * var1 + Math.sin(var3) * var0).method_1029();
      } else {
         return class_243.field_1353;
      }
   }

   public static boolean e(class_1297 var0) {
      return (var0 instanceof class_1309 || var0 instanceof class_10255 || var0 instanceof class_1688)
         && !(var0 instanceof class_1531)
         && !(var0 instanceof class_1420)
         && !(var0 instanceof class_1453)
         && !var0.field_5960
         && var0.method_5805()
         && !var0.method_7325()
         && !var0.method_5765()
         && var0.method_5810();
   }

   public static void u(class_746 var0) {
      if (V != null && V.isRuntimeActive() && var0 == getMc().field_1724) {
         if (V.e == 0) {
            q8.v("GrimSpeed", "Position correction: suspending sprint assistance and collision boost for 20 ticks");
         }

         V.e = 20;
         V.l();
         V.C = "Recovering after correction";
      }
   }

   @Override
   public void onEnable() {
      this.onDisable();
      q8.v(
         "GrimSpeed",
         "Enabled: sprint jump={}, collision boost={}, limit={}, total collision cap=0.08",
         this.f.getValue(),
         this.p.getValue(),
         this.B.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.e = 0;
      this.K = (boolean)0;
      this.l();
      this.C = "Waiting for collision";
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.C : "Inactive";
   }

   @Override
   public void onTick() {
      this.K = (boolean)0;
      this.l();
      boolean var2 = false;
      int var1 = this.e;
      if ((-Integer.signum(var1) >>> 31 ^ 1) == 0) {
         byte var4 = 1;
         int var3 = this.e;
         int var10 = this.e = var3 - var4;
         boolean var6 = false;
         int var5 = var10;
         var10 = Integer.signum(var5);
         if ((var10 | -var10) >>> 31 == 0) {
            q8.v("GrimSpeed", "Correction recovery complete; checking contact next tick");
         }

         this.C = "Recovering after correction";
      } else {
         int var10000 = this.i();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 != 0) {
            this.C = "Waiting for collision";
            this.G = mc.field_1724;
            this.Q = mc.field_1687;
            this.H = this.G.field_6012;
            this.D = this.G.method_5829();
         }
      }
   }

   private void l() {
      this.G = null;
      this.Q = null;
      this.H = -1;
      this.D = null;
   }

   private boolean i() {
      if (!this.isInGame() || !j.k()) {
         this.C = "Movement unavailable";
         return false;
      } else if (TargetStrafeModule.P()) {
         this.C = "Pause: Target Strafe is orbiting";
         return false;
      } else if (j.F("Speed")) {
         this.C = "Pause: Speed is enabled";
         return false;
      } else {
         FlyModule var1 = FlyModule.W();
         if (var1 != null && var1.P()) {
            this.C = "Pause: Fly is enabled";
            return false;
         } else {
            AirStuckModule var2 = AirStuckModule.U();
            if (var2 != null && var2.E()) {
               this.C = "Pause: AirStuck is enabled";
               return false;
            } else {
               ElytraMotionModule var3 = ElytraMotionModule.N();
               if (var3 != null && var3.k()) {
                  this.C = "Pause: ElytraMotion is holding";
                  return false;
               } else {
                  class_746 var4 = mc.field_1724;
                  if (mc.field_1755 != null
                     || var4.method_5799()
                     || var4.method_5771()
                     || var4.method_6101()
                     || var4.method_5765()
                     || var4.method_7325()
                     || var4.method_31549().field_7479
                     || var4.method_6128()
                     || var4.method_18276()
                     || var4.method_5715()
                     || var4.method_6113()
                     || var4.method_74025()
                     || var4.field_5960
                     || var4.field_6235 > 0) {
                     this.C = "Waiting for normal movement";
                     return false;
                  } else if (var4.method_6115()) {
                     this.C = "Pause: using item";
                     return false;
                  } else {
                     return true;
                  }
               }
            }
         }
      }
   }

   public static class_10185 t(class_746 var0, class_10185 var1) {
      if (V != null && V.isEnabled()) {
         GrimSpeedModule var2 = V;
         if (var0 != null
            && var2 != null
            && var2.isRuntimeActive()
            && var2.f.getValue()
            && var2.G == var0
            && var0 == getMc().field_1724
            && var2.Q == mc.field_1687
            && var2.H == var0.field_6012) {
            try {
               if (!var2.i() || !var1.comp_3159() || var1.comp_3160() || var1.comp_3164() || var0.method_7344().method_7586() <= 6 || sq.U() != null) {
                  return var1;
               }

               if (AuraModule.N()) {
                  return var1;
               }

               int var3 = var0.method_5624()
                     && var0.method_24828()
                     && !var0.field_5976
                     && !var0.method_31549().field_7478
                     && var0.method_18798().field_1351 <= 0.0
                     && mc.field_1687.method_8587(var0, var0.method_5829().method_1012(0.0, 1.3, 0.0))
                  ? 1
                  : 0;
               var2.K = (boolean)1;
               return new class_10185(
                  var1.comp_3159(), var1.comp_3160(), var1.comp_3161(), var1.comp_3162(), var1.comp_3163() || var3 != 0, var1.comp_3164(), true
               );
            } catch (RuntimeException var4) {
               q8.u("GrimSpeed", "Input assistance failed; disabling and keeping native input", var4);
               var2.disable();
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public static void c(class_746 var0) {
      if (V != null && V.isEnabled()) {
         GrimSpeedModule var1 = V;
         if (var0 != null && var1 != null && var1.isRuntimeActive() && var1.G == var0) {
            try {
               var1.L(var0);
            } catch (RuntimeException var3) {
               q8.u("GrimSpeed", "Collision phase failed; disabling and clearing pending boost", var3);
               var1.disable();
            }
         }
      }
   }

   private void L(class_746 var1) {
      class_238 var2 = this.D;
      int var3 = var1 == mc.field_1724 && this.Q == mc.field_1687 && this.H == var1.field_6012 ? 1 : 0;
      this.l();
      if (var3 == 0 || var2 == null) {
         this.C = "Movement unavailable";
      } else if (this.i()) {
         class_243 var4 = j(var1.field_6212, var1.field_6250, var1.method_36454());
         if (var4.method_1027() == 0.0) {
            this.C = "Waiting for movement input";
         } else {
            int var5 = 0;

            for (class_1297 var7 : mc.field_1687.method_8333(var1, y(var2, var1.method_5829()), GrimSpeedModule::e)) {
               if (class_1301.method_5911(var7).test(var1)) {
                  if (++var5 >= this.B.getValue().intValue()) {
                     break;
                  }
               }
            }

            if (var5 == 0) {
               this.C = this.K ? "Sprint assist" : "Waiting for collision";
            } else {
               class_243 var9 = var1.method_18798();
               double var10 = U(this.p.getValue(), var5);
               var1.method_18800(var9.field_1352 + var4.field_1352 * var10, var9.field_1351, var9.field_1350 + var4.field_1350 * var10);
               this.C = String.format(Locale.ROOT, "Boosting: %d %s (+%.2f)", var5, var5 == 1 ? "entity" : "entities", var10);
            }
         }
      }
   }
}
