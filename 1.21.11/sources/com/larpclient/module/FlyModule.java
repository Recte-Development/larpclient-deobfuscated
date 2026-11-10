package com.larpclient.module;

import com.larpclient.j;
import java.util.Set;
import net.minecraft.class_1656;
import net.minecraft.class_243;
import net.minecraft.class_746;

public class FlyModule extends Module {
   private final lo f = this.addSetting(new lo("Speed", "Flight speed", 2.0, 0.5, 10.0, 0.1));
   private final li G = this.addSetting(new li("Mode", "Vanilla flight or gradual glide descent", "Vanilla", "Vanilla", "Glide"));
   private final lo w = this.addSetting(new lo("Glide Speed", "Target descent speed", 0.02, 0.0, 0.1, 0.005));
   private static FlyModule s;
   private class_746 g;
   private boolean i;
   private boolean L;
   private boolean t;
   private float D;
   private float H;

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   public FlyModule() {
      super("Fly", "Fly or glide slowly", Category.MOVEMENT, -1);
      s = this;
      this.w.O(() -> this.G.G("Glide"));
   }

   public static FlyModule W() {
      return s;
   }

   public boolean P() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && getMc().field_1724 != null && getMc().field_1724.method_5805();
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "Vanilla flight or gradual glide descent";
      } else {
         return this.P() ? "Flight active" : "Waiting for player";
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame() && this.P() && j.k()) {
         if (this.G.G("Vanilla")) {
            class_1656 var1 = mc.field_1724.method_31549();
            if (this.g != mc.field_1724) {
               this.O();
               this.g = mc.field_1724;
               this.t = var1.field_7478;
               this.i = var1.field_7479;
               this.D = var1.field_7481;
            }

            byte var5 = this.L;
            byte var4 = var1.field_7479;
            int var10000 = Integer.compare(var4, var5);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.i = var1.field_7479;
            }

            if (var1.field_7481 != this.H) {
               this.D = var1.field_7481;
            }

            boolean var7 = false;
            byte var6 = var1.field_7478;
            var10000 = Integer.signum(var6);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.t = (boolean)0;
            }

            boolean var9 = false;
            byte var8 = var1.field_7479;
            var10000 = Integer.signum(var8);
            int var2 = (var10000 | -var10000) >>> 31 == 0 ? 1 : 0;
            var1.field_7478 = (boolean)1;
            var1.field_7479 = this.L = (boolean)1;
            var1.field_7481 = this.H = this.f.getValue().floatValue() * 0.05F;
            if (var2 != 0) {
               mc.field_1724.method_7355();
            }
         } else {
            this.O();
            class_243 var12 = mc.field_1724.method_18798();
            int var16 = mc.field_1724.method_24828();
            boolean var11 = false;
            byte var10 = (byte)var16;
            var16 = Integer.signum(var10);
            if ((var16 | -var16) >>> 31 == 0 && var12.field_1351 < 0.0 && !mc.field_1724.method_5799() && !mc.field_1724.method_5771()) {
               class_243 var13 = j.i().method_1021(this.f.getValue() * 0.15);
               class_243 var3 = j.N(new class_243(var12.field_1352, 0.0, var12.field_1350), var13, 0.04);
               mc.field_1724
                  .method_18800(var3.field_1352, Math.min(0.0, var12.field_1351 + Math.min(0.08, -this.w.getValue() - var12.field_1351)), var3.field_1350);
            }
         }
      } else {
         this.O();
      }
   }

   private void O() {
      if (this.g != null) {
         class_1656 var1 = this.g.method_31549();
         boolean var2 = var1.field_7479;
         if (var1.field_7478) {
            var1.field_7478 = this.t;
         }

         if (var1.field_7479 == this.L) {
            var1.field_7479 = (boolean)(var1.field_7478 && this.i ? 1 : 0);
         }

         if (var1.field_7481 == this.H) {
            var1.field_7481 = this.D;
         }

         if (var2 != var1.field_7479 && this.g == getMc().field_1724 && this.g.method_5805()) {
            this.g.method_7355();
         }

         this.g = null;
      }
   }

   public void q() {
      this.g = null;
   }

   @Override
   public void onDisable() {
      this.O();
   }
}
