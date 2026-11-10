package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.ij;
import java.util.Locale;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public final class SmoothCameraModule extends Module {
   private static SmoothCameraModule k;
   private final o y = this.addSetting(new o("Mode", "How the camera follows movement", "Default", "Default", "Smooth"));
   private final m V = this.addSetting(new m("Enable First POV", "Also smooth camera movement in first person", false));
   private final m M = this.addSetting(new m("Reset On Perspective", "Reset smoothing when switching perspective", true));
   private final b j = this.A("Horizontal Factor", "Higher values add more horizontal lag", 0.9);
   private final b A = this.A("Vertical Factor", "Higher values add more vertical lag", 0.93);
   private final b i = this.addSetting(new b("Smooth Horizontal Speed", "Horizontal follow speed in blocks per second", 2.2, 0.1, 10.0, 0.1));
   private final b P = this.addSetting(new b("Smooth Vertical Speed", "Higher values make vertical movement catch up faster", 1.4, 0.1, 10.0, 0.1));
   private final SmoothCameraModule.p m = new SmoothCameraModule.p();
   private Object Q;
   private Object W;
   private Object w;
   private Vec3 o;
   private long l;

   public SmoothCameraModule() {
      super("SmoothCamera", "Smooth camera movement as you move", Category.PLAYER, -1);
      k = this;
      this.j.a(() -> this.y.w("Default"));
      this.A.a(() -> this.y.w("Default"));
      this.i.a(() -> this.y.w("Smooth"));
      this.P.a(() -> this.y.w("Smooth"));

      for (r var2 : this.getSettings()) {
         var2.w(var1 -> this.u());
      }
   }

   public static SmoothCameraModule D() {
      return k;
   }

   public static boolean F(ij.r var0) {
      return var0 == ij.r.X
         || var0 == ij.r.N
         || var0 == ij.r.D
         || var0 == ij.r.s
         || var0 == ij.r.I
         || var0 == ij.r.w
         || var0 == ij.r.p
         || var0 == ij.r.J
         || var0 == ij.r.q
         || var0 == ij.r.o;
   }

   private b A(String var1, String var2, double var3) {
      return this.addSetting(new b(var1, var2, var3, 0.0, 1.0, 0.01) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      });
   }

   public Vec3 R(Camera var1, Vec3 var2) {
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
         && F(ij.L())
         && (!var3.options.getCameraType().isFirstPerson() || this.V.getValue())) {
         long var10 = System.nanoTime();
         CameraType var6 = var3.options.getCameraType();
         double var7 = (var10 - this.l) * 1.0E-9;
         int var9 = this.Q == var3.level
               && this.W == var1.entity()
               && (!this.M.getValue() || this.w == var6)
               && this.o != null
               && !(this.o.distanceToSqr(var2) > 256.0)
               && !(var7 > 0.25)
            ? 0
            : 1;
         this.Q = var3.level;
         this.W = var1.entity();
         this.w = var6;
         this.o = var2;
         this.l = var10;
         if (var9 != 0) {
            this.m.a();
         }

         return this.m.Y(var2, var7, this.y.w("Smooth"), this.j.getValue(), this.A.getValue(), this.i.getValue(), this.P.getValue());
      } else {
         this.u();
         return var2;
      }
   }

   private void u() {
      this.m.a();
      this.Q = this.W = this.w = null;
      this.o = null;
      this.l = 0L;
   }

   @Override
   public void onEnable() {
      this.u();
      _q.c("SmoothCamera", "Position smoothing enabled; mode={}, first POV={}", this.y.getValue(), this.V.getValue());
   }

   @Override
   public void onDisable() {
      this.u();
   }

   public static final class p {
      public static final double i = 1.0;
      private Vec3 O;
      private double a;

      public void a() {
         this.O = null;
         this.a = 0.0;
      }

      public Vec3 Y(Vec3 var1, double var2, boolean var4, double var5, double var7, double var9, double var11) {
         if (!Double.isFinite(var1.x) || !Double.isFinite(var1.y) || !Double.isFinite(var1.z)) {
            this.a();
            return var1;
         }

         if (this.O == null) {
            this.O = var1;
            return var1;
         }

         double var13 = Math.clamp(var2, 0.0, 0.05);
         if (var13 <= 0.0) {
            return this.O;
         }

         if (!var4) {
            double var39 = Math.pow(var5, var13 * 60.0);
            double var40 = Math.pow(var7, var13 * 60.0);
            this.O = new Vec3(var1.x + (this.O.x - var1.x) * var39, var1.y + (this.O.y - var1.y) * var40, var1.z + (this.O.z - var1.z) * var39);
            return this.O;
         }

         double var15 = var1.x - this.O.x;
         double var17 = var1.z - this.O.z;
         double var19 = Math.hypot(var15, var17);
         double var21 = Math.max(var9 * var13, var19 - 1.0);
         double var23 = !(var19 <= var21) && !(var19 < 1.0E-5) ? var21 / var19 : 1.0;
         double var25 = this.O.y;
         if (Math.abs(var1.y - var25) > 6.0) {
            double var27 = var11 * var13 * 4.0;
            var25 += Math.clamp(var1.y - var25, -var27, var27);
            this.a = 0.0;
         } else {
            double var42 = 2.0 / Math.max(0.06, 0.42 / Math.max(0.1, var11));
            double var29 = var42 * var13;
            double var31 = 1.0 / (1.0 + var29 + 0.48 * var29 * var29 + 0.235 * var29 * var29 * var29);
            double var33 = var25 - var1.y;
            double var35 = (this.a + var42 * var33) * var13;
            this.a = (this.a - var42 * var35) * var31;
            double var37 = var1.y + (var33 + var35) * var31;
            if ((var1.y - var25 > 0.0 ? 1 : 0) == (var37 > var1.y ? 1 : 0)) {
               var37 = var1.y;
               this.a = 0.0;
            }

            var25 = var37;
         }

         this.O = new Vec3(this.O.x + var15 * var23, var25, this.O.z + var17 * var23);
         return this.O;
      }
   }
}
