package com.larpclient.module;

import com.larpclient.q8;
import com.larpclient.wp;
import java.util.Locale;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_4184;
import net.minecraft.class_5498;

public final class SmoothCameraModule extends Module {
   private static SmoothCameraModule t;
   private final li U = this.addSetting(new li("Mode", "How the camera follows movement", "Default", "Default", "Smooth"));
   private final l3 O = this.addSetting(new l3("Enable First POV", "Also smooth camera movement in first person", false));
   private final l3 b = this.addSetting(new l3("Reset On Perspective", "Reset smoothing when switching perspective", true));
   private final lo w = this.i("Horizontal Factor", "Higher values add more horizontal lag", 0.9);
   private final lo P = this.i("Vertical Factor", "Higher values add more vertical lag", 0.93);
   private final lo G = this.addSetting(new lo("Smooth Horizontal Speed", "Horizontal follow speed in blocks per second", 2.2, 0.1, 10.0, 0.1));
   private final lo C = this.addSetting(new lo("Smooth Vertical Speed", "Higher values make vertical movement catch up faster", 1.4, 0.1, 10.0, 0.1));
   private final SmoothCameraModule.o D = new SmoothCameraModule.o();
   private Object a;
   private Object Q;
   private Object A;
   private class_243 N;
   private long R;

   public SmoothCameraModule() {
      super("SmoothCamera", "Smooth camera movement as you move", Category.PLAYER, -1);
      t = this;
      this.w.O(() -> this.U.G("Default"));
      this.P.O(() -> this.U.G("Default"));
      this.G.O(() -> this.U.G("Smooth"));
      this.C.O(() -> this.U.G("Smooth"));

      for (lb var2 : this.getSettings()) {
         var2.a(var1 -> this.Y());
      }
   }

   public static SmoothCameraModule L() {
      return t;
   }

   public static boolean l(wp.z var0) {
      return var0 == wp.z.D
         || var0 == wp.z.L
         || var0 == wp.z.v
         || var0 == wp.z.r
         || var0 == wp.z.J
         || var0 == wp.z.m
         || var0 == wp.z.h
         || var0 == wp.z.V
         || var0 == wp.z.g
         || var0 == wp.z.u;
   }

   private lo i(String var1, String var2, double var3) {
      return this.addSetting(new lo(var1, var2, var3, 0.0, 1.0, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      });
   }

   public class_243 h(class_4184 var1, class_243 var2) {
      if (!this.isEnabled()) {
         return var2;
      }

      class_310 var3 = getMc();
      if (this.isRuntimeActive()
         && var3 != null
         && var3.field_1687 != null
         && var3.field_1724 != null
         && var3.field_1724.method_5805()
         && var1.method_19331() != null
         && !(var1.method_19331() instanceof class_1309 var4 && var4.method_6113())
         && !mc.field_1773.method_35765()
         && l(wp.o())
         && (!var3.field_1690.method_31044().method_31034() || this.O.getValue())) {
         long var10 = System.nanoTime();
         class_5498 var6 = var3.field_1690.method_31044();
         double var7 = (var10 - this.R) * 1.0E-9;
         int var9 = this.a == var3.field_1687
               && this.Q == var1.method_19331()
               && (!this.b.getValue() || this.A == var6)
               && this.N != null
               && !(this.N.method_1025(var2) > 256.0)
               && !(var7 > 0.25)
            ? 0
            : 1;
         this.a = var3.field_1687;
         this.Q = var1.method_19331();
         this.A = var6;
         this.N = var2;
         this.R = var10;
         if (var9 != 0) {
            this.D.Z();
         }

         return this.D.s(var2, var7, this.U.G("Smooth"), this.w.getValue(), this.P.getValue(), this.G.getValue(), this.C.getValue());
      } else {
         this.Y();
         return var2;
      }
   }

   private void Y() {
      this.D.Z();
      this.a = this.Q = this.A = null;
      this.N = null;
      this.R = 0L;
   }

   @Override
   public void onEnable() {
      this.Y();
      q8.v("SmoothCamera", "Position smoothing enabled; mode={}, first POV={}", this.U.getValue(), this.O.getValue());
   }

   @Override
   public void onDisable() {
      this.Y();
   }

   public static final class o {
      public static final double S = 1.0;
      private class_243 J;
      private double g;

      public void Z() {
         this.J = null;
         this.g = 0.0;
      }

      public class_243 s(class_243 var1, double var2, boolean var4, double var5, double var7, double var9, double var11) {
         if (!Double.isFinite(var1.field_1352) || !Double.isFinite(var1.field_1351) || !Double.isFinite(var1.field_1350)) {
            this.Z();
            return var1;
         }

         if (this.J == null) {
            this.J = var1;
            return var1;
         }

         double var13 = Math.clamp(var2, 0.0, 0.05);
         if (var13 <= 0.0) {
            return this.J;
         }

         if (!var4) {
            double var39 = Math.pow(var5, var13 * 60.0);
            double var40 = Math.pow(var7, var13 * 60.0);
            this.J = new class_243(
               var1.field_1352 + (this.J.field_1352 - var1.field_1352) * var39,
               var1.field_1351 + (this.J.field_1351 - var1.field_1351) * var40,
               var1.field_1350 + (this.J.field_1350 - var1.field_1350) * var39
            );
            return this.J;
         }

         double var15 = var1.field_1352 - this.J.field_1352;
         double var17 = var1.field_1350 - this.J.field_1350;
         double var19 = Math.hypot(var15, var17);
         double var21 = Math.max(var9 * var13, var19 - 1.0);
         double var23 = !(var19 <= var21) && !(var19 < 1.0E-5) ? var21 / var19 : 1.0;
         double var25 = this.J.field_1351;
         if (Math.abs(var1.field_1351 - var25) > 6.0) {
            double var27 = var11 * var13 * 4.0;
            var25 += Math.clamp(var1.field_1351 - var25, -var27, var27);
            this.g = 0.0;
         } else {
            double var42 = 2.0 / Math.max(0.06, 0.42 / Math.max(0.1, var11));
            double var29 = var42 * var13;
            double var31 = 1.0 / (1.0 + var29 + 0.48 * var29 * var29 + 0.235 * var29 * var29 * var29);
            double var33 = var25 - var1.field_1351;
            double var35 = (this.g + var42 * var33) * var13;
            this.g = (this.g - var42 * var35) * var31;
            double var37 = var1.field_1351 + (var33 + var35) * var31;
            if ((var1.field_1351 - var25 > 0.0 ? 1 : 0) == (var37 > var1.field_1351 ? 1 : 0)) {
               var37 = var1.field_1351;
               this.g = 0.0;
            }

            var25 = var37;
         }

         this.J = new class_243(this.J.field_1352 + var15 * var23, var25, this.J.field_1350 + var17 * var23);
         return this.J;
      }
   }
}
