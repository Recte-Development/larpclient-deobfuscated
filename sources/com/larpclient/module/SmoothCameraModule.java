package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.tq;
import java.util.Locale;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SmoothCameraModule extends Module {
   private static SmoothCameraModule N;
   private final d6 Z = this.addSetting(new d6("Mode", "How the camera follows movement", "Default", "Default", "Smooth"));
   private final dh I = this.addSetting(new dh("Enable First POV", "Also smooth camera movement in first person", false));
   private final dh y = this.addSetting(new dh("Reset On Perspective", "Reset smoothing when switching perspective", true));
   private final d9 S = this.F("Horizontal Factor", "Higher values add more horizontal lag", 0.9);
   private final d9 O = this.F("Vertical Factor", "Higher values add more vertical lag", 0.93);
   private final d9 x = this.addSetting(new d9("Smooth Horizontal Speed", "Horizontal follow speed in blocks per second", 2.2, 0.1, 10.0, 0.1));
   private final d9 p = this.addSetting(new d9("Smooth Vertical Speed", "Higher values make vertical movement catch up faster", 1.4, 0.1, 10.0, 0.1));
   private final SmoothCameraModule.a M = new SmoothCameraModule.a();
   private Object R;
   private Object Q;
   private Object l;
   private Vec3 k;
   private long o;

   public SmoothCameraModule() {
      super("SmoothCamera", "Smooth camera movement as you move", Category.PLAYER, -1);
      N = this;
      this.S.H(() -> this.Z.S("Default"));
      this.O.H(() -> this.Z.S("Default"));
      this.x.H(() -> this.Z.S("Smooth"));
      this.p.H(() -> this.Z.S("Smooth"));

      for (df var2 : this.getSettings()) {
         var2.h(var1 -> this.w());
      }
   }

   public static SmoothCameraModule d() {
      return N;
   }

   public static boolean G(tq.y var0) {
      return var0 == tq.y.A
         || var0 == tq.y.u
         || var0 == tq.y.J
         || var0 == tq.y.a
         || var0 == tq.y.g
         || var0 == tq.y.e
         || var0 == tq.y.K
         || var0 == tq.y.N
         || var0 == tq.y.B
         || var0 == tq.y.P;
   }

   private d9 F(String var1, String var2, double var3) {
      return this.addSetting(new d9(var1, var2, var3, 0.0, 1.0, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.P());
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
         && G(tq.l())
         && (!var3.options.getCameraType().isFirstPerson() || this.I.P())) {
         long var10 = System.nanoTime();
         CameraType var6 = var3.options.getCameraType();
         double var7 = (var10 - this.o) * 1.0E-9;
         int var9 = this.R == var3.level
               && this.Q == var1.entity()
               && (!this.y.P() || this.l == var6)
               && this.k != null
               && !(this.k.distanceToSqr(var2) > 256.0)
               && !(var7 > 0.25)
            ? 0
            : 1;
         this.R = var3.level;
         this.Q = var1.entity();
         this.l = var6;
         this.k = var2;
         this.o = var10;
         if (var9 != 0) {
            this.M.R();
         }

         return this.M.d(var2, var7, this.Z.S("Smooth"), this.S.P(), this.O.P(), this.x.P(), this.p.P());
      } else {
         this.w();
         return var2;
      }
   }

   private void w() {
      this.M.R();
      this.R = this.Q = this.l = null;
      this.k = null;
      this.o = 0L;
   }

   @Override
   public void onEnable() {
      this.w();
      mt.U("SmoothCamera", "Position smoothing enabled; mode={}, first POV={}", this.Z.P(), this.I.P());
   }

   @Override
   public void onDisable() {
      this.w();
   }

   public static final class a {
      public static final double L = 1.0;
      private Vec3 s;
      private double r;

      public void R() {
         this.s = null;
         this.r = 0.0;
      }

      public Vec3 d(Vec3 var1, double var2, boolean var4, double var5, double var7, double var9, double var11) {
         if (!Double.isFinite(var1.x) || !Double.isFinite(var1.y) || !Double.isFinite(var1.z)) {
            this.R();
            return var1;
         }

         if (this.s == null) {
            this.s = var1;
            return var1;
         }

         double var13 = Math.clamp(var2, 0.0, 0.05);
         if (var13 <= 0.0) {
            return this.s;
         }

         if (!var4) {
            double var39 = Math.pow(var5, var13 * 60.0);
            double var40 = Math.pow(var7, var13 * 60.0);
            this.s = new Vec3(var1.x + (this.s.x - var1.x) * var39, var1.y + (this.s.y - var1.y) * var40, var1.z + (this.s.z - var1.z) * var39);
            return this.s;
         }

         double var15 = var1.x - this.s.x;
         double var17 = var1.z - this.s.z;
         double var19 = Math.hypot(var15, var17);
         double var21 = Math.max(var9 * var13, var19 - 1.0);
         double var23 = !(var19 <= var21) && !(var19 < 1.0E-5) ? var21 / var19 : 1.0;
         double var25 = this.s.y;
         if (Math.abs(var1.y - var25) > 6.0) {
            double var27 = var11 * var13 * 4.0;
            var25 += Math.clamp(var1.y - var25, -var27, var27);
            this.r = 0.0;
         } else {
            double var42 = 2.0 / Math.max(0.06, 0.42 / Math.max(0.1, var11));
            double var29 = var42 * var13;
            double var31 = 1.0 / (1.0 + var29 + 0.48 * var29 * var29 + 0.235 * var29 * var29 * var29);
            double var33 = var25 - var1.y;
            double var35 = (this.r + var42 * var33) * var13;
            this.r = (this.r - var42 * var35) * var31;
            double var37 = var1.y + (var33 + var35) * var31;
            if ((var1.y - var25 > 0.0 ? 1 : 0) == (var37 > var1.y ? 1 : 0)) {
               var37 = var1.y;
               this.r = 0.0;
            }

            var25 = var37;
         }

         this.s = new Vec3(this.s.x + var15 * var23, var25, this.s.z + var17 * var23);
         return this.s;
      }
   }
}
