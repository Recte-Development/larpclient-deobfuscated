package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.sm;
import java.util.List;
import net.minecraft.class_1944;
import net.minecraft.class_2338;
import net.minecraft.class_2874.class_12326;

public final class FogModule extends Module {
   private static FogModule M;
   public final lo k = this.D("Start Distance", "Clear range before fog begins, in blocks", 0.0, 0.0, 256.0, 4.0);
   public final lo K = this.D("End Distance", "Distance where fog reaches full density, in blocks", 48.0, 16.0, 1024.0, 8.0);
   public final lo z = this.D("Height", "Fog thins above sea level over this many blocks", 256.0, 8.0, 256.0, 4.0);
   public final lo C = this.D("Sun Glow", "Warm light scattered through the fog toward the sun", 0.45, 0.0, 1.0, 0.05);
   public final li X = this.addSetting(new li("Color", "Sky follows the native and sky module fog color", "Sky", "Sky", "Custom"));
   public final l0 R = this.addSetting(new l0("Custom Color", "Fog color when Color is Custom", -4601895));
   public final lo h = this.D("Sky Height", "Degrees above the horizon the fog covers the sky; zero keeps the sky clear", 30.0, 0.0, 30.0, 1.0);
   public final l3 j = this.addSetting(new l3("Clear Sky", "Keep vanilla fog from climbing high up the sky", true));
   public final l3 N = this.addSetting(new l3("Fade Underground", "Fade the fog where the camera cannot see the sky", true));
   public final li p = this.addSetting(new li("Dimension Filter", "Choose where the fog appears", "Overworld", "Overworld", "All"));
   public static final float c = 512.0F;
   public static final float m = 1.0F;
   private double a = 1.0;
   private double x = 1.0;
   private int d;

   private lo D(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   public FogModule() {
      super("Fog", "Soft distance fog that keeps the sky clear", Category.VISUALS, -1);
      M = this;
      this.R.O(() -> this.X.G("Custom"));
   }

   public static FogModule Z() {
      return M;
   }

   public List<lb<?>> f() {
      return List.of(this.k, this.K);
   }

   public List<lb<?>> z() {
      return List.of(this.z, this.C, this.X, this.R);
   }

   public List<lb<?>> t() {
      return List.of(this.h, this.j);
   }

   public List<lb<?>> C() {
      return List.of(this.N, this.p);
   }

   public boolean g() {
      if (!this.isEnabled()) {
         return false;
      } else {
         return this.isRuntimeActive() && mc != null && mc.field_1687 != null
            ? this.p.G("All") || mc.field_1687.method_8597().comp_5220() == class_12326.field_64386
            : false;
      }
   }

   public static boolean V() {
      return M != null && M.isEnabled() ? M != null && M.j.getValue() && M.g() : false;
   }

   public float F() {
      return (float)Math.max(this.K.getValue(), this.k.getValue() + 8.0);
   }

   public float f(float var1) {
      return (float)(this.x + (this.a - this.x) * Math.clamp(var1, 0.0F, 1.0F));
   }

   @Override
   public void onTick() {
      this.x = this.a;
      if (this.isInGame()) {
         double var1 = 1.0;
         int var10000 = this.N.getValue();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            class_2338 var3 = class_2338.method_49638(mc.field_1773.method_19418().method_71156());
            double var14;
            if (mc.field_1687.method_22340(var3)) {
               var10000 = mc.field_1687.method_8314(class_1944.field_9284, var3);
               byte var11 = 3;
               int var10 = var10000;
               var14 = Math.clamp((var10 - var11) / 7.0, 0.0, 1.0);
            } else {
               var14 = this.a;
            }

            var1 = var14;
         }

         this.a = this.a + Math.clamp(var1 - this.a, -0.04, 0.04);
         var10000 = this.d;
         byte var5 = 1;
         int var4 = this.d;
         this.d = var4 + var5;
         var10000 %= 100;
         boolean var7 = false;
         int var6 = var10000;
         var10000 = Integer.signum(var6);
         if ((var10000 | -var10000) >>> 31 == 0 && Math.abs(var1 - this.a) > 0.5) {
            q8.v("Fog", "Underground fade toward {}", var1);
         }
      }
   }

   @Override
   public void onEnable() {
      this.a = this.x = 1.0;
      this.d = 0;
      q8.G(
         "Fog",
         "Enabled; start={}; end={}; height={}; sky height={} degrees; clear sky={}",
         this.k.getValue(),
         this.F(),
         this.z.getValue(),
         this.h.getValue(),
         this.j.getValue()
      );
   }

   @Override
   public void onDisable() {
      sm.X();
   }
}
