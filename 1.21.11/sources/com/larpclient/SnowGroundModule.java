package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import com.larpclient.module.NoWeatherModule;
import com.larpclient.module.RemovalsModule;
import java.nio.ByteBuffer;
import net.minecraft.class_12075;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_2902.class_2903;

public final class SnowGroundModule extends Module {
   private static SnowGroundModule i;
   public final com.larpclient.module.lo K = this.k("Coverage", "Snow coverage on exposed ground and roofs, percent", 100.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.lo G = this.k("Depth", "Soft drift relief and snow along upper edges, percent", 75.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.lo l = this.k("Sparkle", "Small sunlit ice crystals, percent", 30.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.l3 E = this.addSetting(new com.larpclient.module.l3("Snowfall", "Falling snow even in clear server weather", true));
   public final com.larpclient.module.lo q = this.k("Density", "Falling snow density, percent", 85.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.lo H = this.k("Wind", "Sideways drift and rolling gusts, percent", 35.0, 0.0, 100.0, 5.0);
   public final com.larpclient.module.lo r = this.k("Radius", "Snowfall radius around the camera in blocks", 24.0, 12.0, 40.0, 2.0);
   public final com.larpclient.module.li Q = this.addSetting(
      new com.larpclient.module.li("Quality", "Snow detail and bounded flake count", "High", "Low", "Medium", "High")
   );
   public final com.larpclient.module.l3 v = this.addSetting(
      new com.larpclient.module.l3("Footsteps", "Clear powder beneath you and leave a soft walking trail", true)
   );
   public final com.larpclient.module.lo p = this.k("Trail Time", "Seconds before walking trails fill back in", 12.0, 2.0, 30.0, 1.0);
   private final cs y = new cs();
   private class_243 d = class_243.field_1353;
   private final be J = new be();
   private Object I;
   private Object S;
   private int D = -1;
   private int f = -1;

   private com.larpclient.module.lo k(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new com.larpclient.module.lo(var1, var2, var3, var5, var7, var9));
   }

   public SnowGroundModule() {
      super("Snow Ground", "Deep powder, wind-shaped snow drifts and dense falling snow", Category.RENDER, -1);
      i = this;
      this.q.O(this.E::getValue);
      this.H.O(this.E::getValue);
      this.r.O(this.E::getValue);
      this.p.O(this.v::getValue);
      this.v.a(var1 -> this.y.V());
      this.E.a(var1 -> this.J.l());
      this.Q.a(var1 -> {
         this.J.l();
         qs.O();
      });
      this.r.a(var1 -> this.J.l());
   }

   public static boolean K() {
      return i != null && i.isEnabled() ? i != null && i.isRuntimeActive() : false;
   }

   public static SnowGroundModule R() {
      return i;
   }

   public int w() {
      return this.Q.G("High") ? 2 : (this.Q.G("Medium") ? 1 : 0);
   }

   public static int J(int var0) {
      return var0 >= 2 ? 3200 : (var0 == 1 ? 1600 : 800);
   }

   public static boolean p() {
      if (i != null && i.isEnabled()) {
         SnowGroundModule var0 = i;
         return var0 != null && K() && var0.D != t_.g && var0.E.getValue() && var0.q.getValue() > 0.0 && !NoWeatherModule.b() && !RemovalsModule.o("weather");
      } else {
         return false;
      }
   }

   public be b() {
      return this.J;
   }

   public void L() {
      this.J.l();
      this.y.V();
      this.d = class_243.field_1353;
      this.I = this.S = null;
   }

   public cs M() {
      return this.y;
   }

   public void D(ByteBuffer var1, class_243 var2, double var3, boolean var5) {
      if (this.isEnabled()) {
         this.y.G(var1, var2, this.d, var3, this.p.getValue(), var5 && K() && this.v.getValue() && this.I == mc.field_1687 && this.S == mc.field_1724);
      }
   }

   public void K() {
      this.L();
      this.D = t_.g;
   }

   public static boolean e() {
      return i != null && i.isEnabled() ? p() && i.J.X() > 0 : false;
   }

   @Override
   public void onEnable() {
      this.L();
      qs.w();
      qs.O();
      q8.G(
         "SnowGround",
         "Enabled powder and snowfall; quality={}, flake cap={}, radius={} blocks, footsteps={}, trail={} seconds",
         this.Q.getValue(),
         J(this.w()),
         this.r.getValue(),
         this.v.getValue(),
         this.p.getValue()
      );
      qs.j(this.w());
      boolean var1 = NoWeatherModule.b();
      if (this.E.getValue() && (var1 || RemovalsModule.o("weather"))) {
         q8.G("SnowGround", "Snowfall hidden while {} removes weather; powder and footsteps still draw", var1 ? "NoWeather" : "Removals");
      }
   }

   @Override
   public void onDisable() {
      this.L();
      qs.O();
      qs.B();
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() && qs.c() ? "Material fallback" : "";
   }

   @Override
   public void onTick() {
      int var3 = t_.g;
      int var2 = this.f;
      int var10000 = Integer.compare(var2, var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.L();
         this.f = t_.g;
      }

      if (!this.isInGame()) {
         this.L();
      } else {
         if (this.I != mc.field_1687 || this.S != mc.field_1724) {
            this.L();
            this.I = mc.field_1687;
            this.S = mc.field_1724;
         }

         if (!mc.method_1493()) {
            class_243 var1 = new class_243(mc.field_1724.method_23317(), mc.field_1724.method_5829().field_1322, mc.field_1724.method_23321());
            this.d = var1;
            if (this.v.getValue()) {
               int var15;
               label37: {
                  var10 = this.y;
                  if (mc.field_1724.method_24828()) {
                     var15 = mc.field_1724.method_5765();
                     boolean var5 = false;
                     byte var4 = var15;
                     var15 = Integer.signum(var4);
                     if ((var15 | -var15) >>> 31 == 0) {
                        int var12 = mc.field_1724.method_7325();
                        boolean var7 = false;
                        byte var6 = (byte)var12;
                        var12 = Integer.signum(var6);
                        if ((var12 | -var12) >>> 31 == 0 && !mc.field_1724.method_5799()) {
                           boolean var9 = false;
                           byte var8 = mc.field_1724.method_31549().field_7479;
                           var12 = Integer.signum(var8);
                           if ((var12 | -var12) >>> 31 == 0) {
                              var15 = 1;
                              break label37;
                           }
                        }
                     }
                  }

                  var15 = 0;
               }

               var10.F(var1, (boolean)var15, qo.K(), this.p.getValue());
            } else {
               this.y.V();
            }

            if (!p()) {
               this.J.l();
            } else {
               this.J
                  .X(
                     mc.field_1773.method_19418().method_71156(),
                     new be.o(this.r.getValue(), this.q.getValue() / 100.0, this.H.getValue() / 100.0, J(this.w())),
                     (var0, var1x) -> {
                        if (!mc.field_1687.method_8393(var0 >> 4, var1x >> 4)) {
                           return be.v.UNAVAILABLE;
                        }

                        int var2x = mc.field_1687.method_8624(class_2903.field_13197, var0, var1x) - 1;
                        class_2338 var3x = new class_2338(var0, var2x, var1x);
                        class_2680 var4x = mc.field_1687.method_8320(var3x);
                        class_265 var5x = var4x.method_26220(mc.field_1687, var3x);
                        double var6x = var2x + (!var5x.method_1110() && var4x.method_26227().method_15769() ? var5x.method_1105(class_2351.field_11052) : 1.0);
                        int var8x = mc.field_1687.method_22339(var3x.method_10084());
                        return new be.v(var6x, var8x / 15.0);
                     }
                  );
            }
         }
      }
   }

   public void J(float var1, class_12075 var2) {
      if (this.isEnabled()) {
         if (K() && this.I == mc.field_1687 && this.S == mc.field_1724) {
            this.d = mc.field_1724.method_5836(var1).method_1023(0.0, mc.field_1724.method_5751(), 0.0);
            if (!p()) {
               this.J.l();
            } else {
               this.J.u(var2.field_63078, var1);
            }
         } else {
            this.L();
         }
      }
   }

   public void E(f1 var1) {
      if (this.isEnabled()) {
         if (K() && p() && this.I == mc.field_1687 && this.S == mc.field_1724) {
            this.J.N(var1);
         }
      }
   }
}
