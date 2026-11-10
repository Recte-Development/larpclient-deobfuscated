package com.larpclient.module;

import com.larpclient.p_;
import java.util.Locale;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SmoothCameraModule extends Module {
   private static SmoothCameraModule S;
   private final l N = this.addSetting(new l("Mode", "How the camera follows movement", "Default", "Default", "Smooth"));
   private final s s = this.addSetting(new s("Enable First POV", "Also smooth camera movement in first person", false));
   private final s o = this.addSetting(new s("Reset On Perspective", "Reset smoothing when switching perspective", true));
   private final e Q = this.o("Horizontal Factor", "Higher values add more horizontal lag", 0.9);
   private final e z = this.o("Vertical Factor", "Higher values add more vertical lag", 0.93);
   private final e g = this.addSetting(new e("Smooth Horizontal Speed", "Horizontal follow speed in blocks per second", 2.2, 0.1, 10.0, 0.1));
   private final e e = this.addSetting(new e("Smooth Vertical Speed", "Higher values make vertical movement catch up faster", 1.4, 0.1, 10.0, 0.1));
   private final SmoothCameraModule.p K = new SmoothCameraModule.p();
   private Object q;
   private Object n;
   private Object M;
   private Vec3 R;
   private long b;

   public SmoothCameraModule() {
      super("SmoothCamera", "Smooth camera movement as you move", Category.PLAYER, -1);
      S = this;
      this.Q.P(() -> this.N.O("Default"));
      this.z.P(() -> this.N.O("Default"));
      this.g.P(() -> this.N.O("Smooth"));
      this.e.P(() -> this.N.O("Smooth"));

      for (q var2 : this.getSettings()) {
         var2.o(var1 -> this.a());
      }
   }

   public static SmoothCameraModule P() {
      return S;
   }

   public static boolean u(p_.o var0) {
      return var0 == p_.o.T
         || var0 == p_.o.b
         || var0 == p_.o.G
         || var0 == p_.o.h
         || var0 == p_.o.d
         || var0 == p_.o.K
         || var0 == p_.o.D
         || var0 == p_.o.Y
         || var0 == p_.o.y
         || var0 == p_.o.R;
   }

   private e o(String var1, String var2, double var3) {
      return this.addSetting(new e(var1, var2, var3, 0.0, 1.0, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      });
   }

   public Vec3 y(Camera var1, Vec3 var2) {
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
         && u(p_.A())
         && (!var3.options.getCameraType().isFirstPerson() || this.s.getValue())) {
         long var10 = System.nanoTime();
         CameraType var6 = var3.options.getCameraType();
         double var7 = (var10 - this.b) * 1.0E-9;
         int var9 = this.q == var3.level
               && this.n == var1.entity()
               && (!this.o.getValue() || this.M == var6)
               && this.R != null
               && !(this.R.distanceToSqr(var2) > 256.0)
               && !(var7 > 0.25)
            ? 0
            : 1;
         this.q = var3.level;
         this.n = var1.entity();
         this.M = var6;
         this.R = var2;
         this.b = var10;
         if (var9 != 0) {
            this.K.u();
         }

         return this.K.u(var2, var7, this.N.O("Smooth"), this.Q.getValue(), this.z.getValue(), this.g.getValue(), this.e.getValue());
      } else {
         this.a();
         return var2;
      }
   }

   private void a() {
      this.K.u();
      this.q = this.n = this.M = null;
      this.R = null;
      this.b = 0L;
   }

   @Override
   public void onEnable() {
      this.a();
      com.larpclient.h.U("SmoothCamera", "Position smoothing enabled; mode={}, first POV={}", this.N.getValue(), this.s.getValue());
   }

   @Override
   public void onDisable() {
      this.a();
   }

   public static final class p {
      public static final double C = 1.0;
      private Vec3 o;
      private double S;

      public void u() {
         this.o = null;
         this.S = 0.0;
      }

      public Vec3 u(Vec3 var1, double var2, boolean var4, double var5, double var7, double var9, double var11) {
         if (!Double.isFinite(var1.x) || !Double.isFinite(var1.y) || !Double.isFinite(var1.z)) {
            this.u();
            return var1;
         }

         if (this.o == null) {
            this.o = var1;
            return var1;
         }

         double var13 = Math.clamp(var2, 0.0, 0.05);
         if (var13 <= 0.0) {
            return this.o;
         }

         if (!var4) {
            double var39 = Math.pow(var5, var13 * 60.0);
            double var40 = Math.pow(var7, var13 * 60.0);
            this.o = new Vec3(var1.x + (this.o.x - var1.x) * var39, var1.y + (this.o.y - var1.y) * var40, var1.z + (this.o.z - var1.z) * var39);
            return this.o;
         }

         double var15 = var1.x - this.o.x;
         double var17 = var1.z - this.o.z;
         double var19 = Math.hypot(var15, var17);
         double var21 = Math.max(var9 * var13, var19 - 1.0);
         double var23 = !(var19 <= var21) && !(var19 < 1.0E-5) ? var21 / var19 : 1.0;
         double var25 = this.o.y;
         if (Math.abs(var1.y - var25) > 6.0) {
            double var27 = var11 * var13 * 4.0;
            var25 += Math.clamp(var1.y - var25, -var27, var27);
            this.S = 0.0;
         } else {
            double var42 = 2.0 / Math.max(0.06, 0.42 / Math.max(0.1, var11));
            double var29 = var42 * var13;
            double var31 = 1.0 / (1.0 + var29 + 0.48 * var29 * var29 + 0.235 * var29 * var29 * var29);
            double var33 = var25 - var1.y;
            double var35 = (this.S + var42 * var33) * var13;
            this.S = (this.S - var42 * var35) * var31;
            double var37 = var1.y + (var33 + var35) * var31;
            if ((var1.y - var25 > 0.0 ? 1 : 0) == (var37 > var1.y ? 1 : 0)) {
               var37 = var1.y;
               this.S = 0.0;
            }

            var25 = var37;
         }

         this.o = new Vec3(this.o.x + var15 * var23, var25, this.o.z + var17 * var23);
         return this.o;
      }
   }
}
