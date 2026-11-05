package com.larpclient.module;

import com.larpclient.r4;
import com.larpclient.rr;
import java.util.Locale;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SmoothCameraModule extends Module {
   private static SmoothCameraModule U;
   private final lp P = this.addSetting(new lp("Mode", "How the camera follows movement", "Default", "Default", "Smooth"));
   private final ln p = this.addSetting(new ln("Enable First POV", "Also smooth camera movement in first person", false));
   private final ln C = this.addSetting(new ln("Reset On Perspective", "Reset smoothing when switching perspective", true));
   private final lc v = this.m("Horizontal Factor", "Higher values add more horizontal lag", 0.9);
   private final lc k = this.m("Vertical Factor", "Higher values add more vertical lag", 0.93);
   private final lc h = this.addSetting(new lc("Smooth Horizontal Speed", "Horizontal follow speed in blocks per second", 2.2, 0.1, 10.0, 0.1));
   private final lc W = this.addSetting(new lc("Smooth Vertical Speed", "Higher values make vertical movement catch up faster", 1.4, 0.1, 10.0, 0.1));
   private final SmoothCameraModule.p w = new SmoothCameraModule.p();
   private Object n;
   private Object N;
   private Object t;
   private Vec3 L;
   private long r;

   public SmoothCameraModule() {
      super("SmoothCamera", "Smooth camera movement as you move", Category.PLAYER, -1);
      U = this;
      this.v.x(() -> this.P.i("Default"));
      this.k.x(() -> this.P.i("Default"));
      this.h.x(() -> this.P.i("Smooth"));
      this.W.x(() -> this.P.i("Smooth"));

      for (l6 var2 : this.getSettings()) {
         var2.W(var1 -> this.e());
      }
   }

   public static SmoothCameraModule G() {
      return U;
   }

   public static boolean M(rr.f var0) {
      return var0 == rr.f.i
         || var0 == rr.f.B
         || var0 == rr.f.e
         || var0 == rr.f.L
         || var0 == rr.f.q
         || var0 == rr.f.T
         || var0 == rr.f.y
         || var0 == rr.f.H
         || var0 == rr.f.m
         || var0 == rr.f.E;
   }

   private lc m(String var1, String var2, double var3) {
      return this.addSetting(new lc(var1, var2, var3, 0.0, 1.0, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      });
   }

   public Vec3 M(Camera var1, Vec3 var2) {
      if (!this.isEnabled()) {
         return var2;
      }

      Minecraft var3 = getMc();
      if (this.isRuntimeActive()
         && var3 != null
         && var3.level != null
         && var3.player != null
         && var3.player.isAlive()
         && var1.entity() != null
         && !(var1.entity() instanceof LivingEntity var4 && var4.isSleeping())
         && !var1.isPanoramicMode()
         && M(rr.u())
         && (!var3.options.getCameraType().isFirstPerson() || this.p.getValue())) {
         long var10 = System.nanoTime();
         CameraType var6 = var3.options.getCameraType();
         double var7 = (var10 - this.r) * 1.0E-9;
         int var9 = this.n == var3.level
               && this.N == var1.entity()
               && (!this.C.getValue() || this.t == var6)
               && this.L != null
               && !(this.L.distanceToSqr(var2) > 256.0)
               && !(var7 > 0.25)
            ? 0
            : 1;
         this.n = var3.level;
         this.N = var1.entity();
         this.t = var6;
         this.L = var2;
         this.r = var10;
         if (var9 != 0) {
            this.w.p();
         }

         return this.w.F(var2, var7, this.P.i("Smooth"), this.v.getValue(), this.k.getValue(), this.h.getValue(), this.W.getValue());
      } else {
         this.e();
         return var2;
      }
   }

   private void e() {
      this.w.p();
      this.n = this.N = this.t = null;
      this.L = null;
      this.r = 0L;
   }

   @Override
   public void onEnable() {
      this.e();
      r4.g("SmoothCamera", "Position smoothing enabled; mode={}, first POV={}", this.P.getValue(), this.p.getValue());
   }

   @Override
   public void onDisable() {
      this.e();
   }

   public static final class p {
      public static final double t = 1.0;
      private Vec3 Z;
      private double d;

      public void p() {
         this.Z = null;
         this.d = 0.0;
      }

      public Vec3 F(Vec3 var1, double var2, boolean var4, double var5, double var7, double var9, double var11) {
         if (!Double.isFinite(var1.x) || !Double.isFinite(var1.y) || !Double.isFinite(var1.z)) {
            this.p();
            return var1;
         }

         if (this.Z == null) {
            this.Z = var1;
            return var1;
         }

         double var13 = Math.clamp(var2, 0.0, 0.05);
         if (var13 <= 0.0) {
            return this.Z;
         }

         if (!var4) {
            double var39 = Math.pow(var5, var13 * 60.0);
            double var40 = Math.pow(var7, var13 * 60.0);
            this.Z = new Vec3(var1.x + (this.Z.x - var1.x) * var39, var1.y + (this.Z.y - var1.y) * var40, var1.z + (this.Z.z - var1.z) * var39);
            return this.Z;
         }

         double var15 = var1.x - this.Z.x;
         double var17 = var1.z - this.Z.z;
         double var19 = Math.hypot(var15, var17);
         double var21 = Math.max(var9 * var13, var19 - 1.0);
         double var23 = !(var19 <= var21) && !(var19 < 1.0E-5) ? var21 / var19 : 1.0;
         double var25 = this.Z.y;
         if (Math.abs(var1.y - var25) > 6.0) {
            double var27 = var11 * var13 * 4.0;
            var25 += Math.clamp(var1.y - var25, -var27, var27);
            this.d = 0.0;
         } else {
            double var42 = 2.0 / Math.max(0.06, 0.42 / Math.max(0.1, var11));
            double var29 = var42 * var13;
            double var31 = 1.0 / (1.0 + var29 + 0.48 * var29 * var29 + 0.235 * var29 * var29 * var29);
            double var33 = var25 - var1.y;
            double var35 = (this.d + var42 * var33) * var13;
            this.d = (this.d - var42 * var35) * var31;
            double var37 = var1.y + (var33 + var35) * var31;
            if ((var1.y - var25 > 0.0 ? 1 : 0) == (var37 > var1.y ? 1 : 0)) {
               var37 = var1.y;
               this.d = 0.0;
            }

            var25 = var37;
         }

         this.Z = new Vec3(this.Z.x + var15 * var23, var25, this.Z.z + var17 * var23);
         return this.Z;
      }
   }
}
