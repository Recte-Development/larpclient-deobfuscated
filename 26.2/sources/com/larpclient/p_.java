package com.larpclient;

import com.larp.obf.qa300483f54d804a3.q7ae1dbb86ec789d8;
import com.larp.obf.qa300483f54d804a3.qa7574265f54f83cc;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.AutoPearlCatchModule;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.WindChargeHopModule;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

public final class p_ {
   private static final p_.x a = new p_.x();
   private static CameraType f;
   private static CameraType c;
   private static float d;
   private static float N;
   private static Vec3 p = Vec3.ZERO;
   private static Vec3 t = Vec3.ZERO;
   public static final int Z = 40;
   private static lm j;
   private static double g;
   private static boolean h;
   private static p_.o D = p_.o.T;
   private static int w;

   public static boolean I(p_.o var0) {
      A();
      return a.N(var0);
   }

   public static void p(p_.o var0) {
      Minecraft var1 = Minecraft.getInstance();
      A();
      if (a.N(var0)) {
         if (a.m == var0) {
            if (!q(var0)) {
               f(var0);
            }
         } else if (a.m == p_.o.R && q(var0)) {
            M("taken over by " + var0);
            D();
            a.B(var0, var1.player, var1.level);
            A("acquired");
         } else {
            if (q(a.m)) {
               Y(a.m);
            }

            if (a.m == p_.o.T && !q(var0)) {
               f = var1.options.getCameraType();
            }

            if (a.B(var0, var1.player, var1.level)) {
               d = var1.player.getYRot();
               N = var1.player.getXRot();
               c = var1.options.getCameraType();
               if (q(var0) && c.isMirrored()) {
                  d += 180.0F;
                  N = -N;
               }

               t = p = var0 == p_.o.p ? var1.player.position().add(0.0, var1.player.getEyeHeight(Pose.STANDING), 0.0) : var1.player.getEyePosition();
               if (!q(var0)) {
                  f(var0);
               } else {
                  A("acquired");
               }
            }
         }
      }
   }

   public static void Y(p_.o var0) {
      if (a.m == var0) {
         Minecraft var1 = Minecraft.getInstance();
         if (q(var0) && a.D(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
            K();
            float[] var2 = e(d, N, var1.options.getCameraType().isMirrored());
            var2[0] = var1.player.getYRot() + Mth.wrapDegrees(var2[0] - var1.player.getYRot());
            var1.player.setYRot(var2[0]);
            var1.player.setXRot(var2[1]);
            if (var0 == p_.o.k) {
               var1.player.setYHeadRot(var2[0]);
            }

            var1.player.yRotO = var2[0];
            var1.player.xRotO = var2[1];
         }

         j();
         if (var0 == p_.o.t || var0 == p_.o.p) {
            ScaffoldModule.g();
         }
      }
   }

   public static void t(p_.o var0, lm var1, double var2, boolean var4) {
      if (a.m == var0 && var0 != p_.o.R) {
         if (var1 != null && !(var2 >= 180.0) && q(var0) && N()) {
            Minecraft var5 = Minecraft.getInstance();
            K();
            float[] var6 = e(d, N, var5.options.getCameraType().isMirrored());
            if (lm.S(var5.player.getYRot(), var5.player.getXRot(), var6[0], var6[1], com.larpclient.module.tv.g())) {
               j();
            } else {
               a.z(var0);
               j = var1;
               g = var2;
               h = var4;
               D = var0;
               w = 0;
               var1.q();
            }
         } else {
            Y(var0);
         }
      }
   }

   public static void W() {
      if (A() == p_.o.R) {
         Minecraft var0 = Minecraft.getInstance();
         LocalPlayer var1 = var0.player;
         float[] var2 = e(d, N, var0.options.getCameraType().isMirrored());
         float var3 = com.larpclient.module.tv.g();
         if (lm.S(var1.getYRot(), var1.getXRot(), var2[0], var2[1], var3)) {
            M("done, " + j.u());
            j();
         } else if (++w > 40) {
            M("not on your look after 40 ticks, so handed back at once; " + j.u());
            Y(p_.o.R);
         } else {
            float[] var4 = j.T(var1.getYRot(), var1.getXRot(), var2[0], var2[1], g, h, var3);
            j6.k(j6.l.F, var4[0], var4[1], 1.0, var1.getYRot(), var1.getXRot());
         }
      }
   }

   public static void R() {
      if (a.m == p_.o.R && j6.j() && !j6.g(j6.l.F)) {
         M("ended by another module's turn; " + j.u());
         j();
      }
   }

   public static void P() {
      if (a.m == p_.o.R) {
         Minecraft var0 = Minecraft.getInstance();
         if (x(var0.options.keyAttack) || x(var0.options.keyUse)) {
            g("your click");
            if (var0.player != null && var0.level != null) {
               ((q7ae1dbb86ec789d8)var0).larpPick(1.0F);
            }
         }
      }
   }

   public static void g(String var0) {
      if (a.m == p_.o.R) {
         M("finished at once for " + var0 + "; " + j.u());
         Y(p_.o.R);
      }
   }

   private static boolean x(KeyMapping var0) {
      return var0.isDown() || ((qa7574265f54f83cc)var0).larpClickCount() > 0;
   }

   public static boolean R() {
      return a.m == p_.o.R;
   }

   private static void D() {
      j = null;
      D = p_.o.T;
      w = 0;
   }

   private static void M(String var0) {
      String var1 = switch (D) {
         case i -> "Auto Builder";
         default -> "Camera";
         case K -> "Mace";
         case y -> "WindHop";
         case Y -> "PearlCatch";
      };
      com.larpclient.h.A(var1, "Return to your look {}", var0);
   }

   private static boolean N() {
      Minecraft var0 = Minecraft.getInstance();
      return a.D(var0.player, var0.level, var0.player != null && var0.player.isAlive())
         && var0.gui.screen() == null
         && var0.isWindowActive()
         && !var0.player.isSpectator();
   }

   public static void j() {
      if (q(a.m)) {
         A("released");
         h();
      }

      if (f != null) {
         Minecraft.getInstance().options.setCameraType(f);
      }

      f = null;
      c = null;
      a.u(a.m);
      D();
      t = p = Vec3.ZERO;
      N = 0.0F;
      d = 0.0F;
   }

   public static p_.o A() {
      if (a.m == p_.o.k) {
         ScaffoldModule var0 = ScaffoldModule.l();
         if (!N() || var0 == null || !var0.n()) {
            Y(p_.o.k);
         }
      }

      if (a.m == p_.o.G) {
         com.larpclient.module.tb var1 = com.larpclient.module.tb.R();
         if (!N() || var1 == null || !var1.e()) {
            Y(p_.o.G);
         }
      }

      if (a.m == p_.o.h) {
         com.larpclient.module.th var2 = com.larpclient.module.th.B();
         if (!N() || var2 == null || !var2.V()) {
            Y(p_.o.h);
         }
      }

      if (a.m == p_.o.d) {
         AutoAnchorModule var3 = AutoAnchorModule.y();
         if (!N() || var3 == null || !var3.O()) {
            Y(p_.o.d);
         }
      }

      if (a.m == p_.o.b) {
         AuraModule var4 = AuraModule.V();
         if (!N() || var4 == null || !var4.B()) {
            Y(p_.o.b);
         }
      }

      if (a.m == p_.o.K) {
         MaceModule var5 = MaceModule.W();
         if (!N() || var5 == null) {
            Y(p_.o.K);
         } else if (!var5.B()) {
            t(p_.o.K, var5.S(), var5.R(), true);
         }
      }

      if (a.m == p_.o.D) {
         AutoCrystalModule var6 = AutoCrystalModule.S();
         if (!N() || var6 == null || !var6.L()) {
            Y(p_.o.D);
         }
      }

      if (a.m == p_.o.Y) {
         AutoPearlCatchModule var7 = AutoPearlCatchModule.W();
         if (!N() || var7 == null) {
            Y(p_.o.Y);
         } else if (!var7.D()) {
            t(p_.o.Y, var7.P(), var7.B(), var7.f());
         }
      }

      if (a.m == p_.o.y) {
         WindChargeHopModule var8 = WindChargeHopModule.h();
         if (!N() || var8 == null) {
            Y(p_.o.y);
         } else if (!var8.h()) {
            t(p_.o.y, var8.t(), var8.r(), var8.N());
         }
      }

      if (a.m == p_.o.i) {
         ak var9 = ak.g();
         if (!N() || var9 == null) {
            Y(p_.o.i);
         } else if (!var9.B()) {
            t(p_.o.i, var9.f(), var9.e(), true);
         }
      }

      if (a.m == p_.o.R && !N()) {
         Y(p_.o.R);
      }

      Minecraft var10 = Minecraft.getInstance();
      if (a.m != p_.o.T && !a.D(var10.player, var10.level, var10.player != null && var10.player.isAlive())) {
         j();
      }

      if (q(a.m)) {
         K();
      }

      return a.m;
   }

   private static boolean q(p_.o var0) {
      return var0 == p_.o.b
         || var0 == p_.o.K
         || var0 == p_.o.G
         || var0 == p_.o.h
         || var0 == p_.o.d
         || var0 == p_.o.D
         || var0 == p_.o.k
         || var0 == p_.o.Y
         || var0 == p_.o.y
         || var0 == p_.o.R
         || var0 == p_.o.i;
   }

   public static boolean j() {
      return q(A());
   }

   private static void K() {
      CameraType var0 = Minecraft.getInstance().options.getCameraType();
      if (c != null && c.isMirrored() != var0.isMirrored()) {
         d = d + (var0.isMirrored() ? 180.0F : -180.0F);
         N = -N;
      }

      c = var0;
   }

   public static boolean V() {
      return A() == p_.o.p;
   }

   public static float M(float var0) {
      return !j() ? Float.NaN : W(e(d, N, Minecraft.getInstance().options.getCameraType().isMirrored())[0], var0);
   }

   public static float f() {
      return !j() ? Float.NaN : e(d, N, Minecraft.getInstance().options.getCameraType().isMirrored())[1];
   }

   public static float W(float var0, float var1) {
      return var1 + Mth.wrapDegrees(var0 - var1);
   }

   public static float Y(float var0, float var1) {
      return Math.round((var1 - var0) / 360.0F) * 360.0F;
   }

   private static void h() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null) {
         float var1 = Y(var0.yBob, var0.getYRot());
         if (var1 != 0.0F) {
            var0.yBob += var1;
            var0.yBobO += var1;
            com.larpclient.h.U("Camera", "Hand sway moved {} degrees back onto the player's rotation", var1);
         }
      }
   }

   public static void M(double var0, double var2) {
      d += (float)var0 * 0.15F;
      float var4 = V() ? 90.0F : 89.0F;
      N = Mth.clamp(N + (float)var2 * 0.15F, -var4, var4);
   }

   public static float V() {
      return d;
   }

   public static float z() {
      return Minecraft.getInstance().options.getCameraType().isMirrored() ? d - 180.0F : d;
   }

   public static float s() {
      return N;
   }

   public static Vec3 S(float var0) {
      return t.lerp(p, var0);
   }

   public static void e(double var0, double var2, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      t = p;
      if (V() && var5.gui.screen() == null && var5.isWindowActive()) {
         p = p.add(cr.N(d, FreecamModule.o(), var0, var2, var4));
      }
   }

   private static void f(p_.o var0) {
      Minecraft var1 = Minecraft.getInstance();
      CameraType var2 = f == null ? var1.options.getCameraType() : f;
      CameraType var3 = var0 == p_.o.p ? (FreecamModule.Z() && var2.isFirstPerson() ? CameraType.THIRD_PERSON_BACK : var2) : CameraType.THIRD_PERSON_BACK;
      if (var1.options.getCameraType() != var3) {
         var1.options.setCameraType(var3);
      }
   }

   public static float[] e(float var0, float var1, boolean var2) {
      return new float[]{var2 ? var0 - 180.0F : var0, var2 ? -var1 : var1};
   }

   private static void A(String var0) {
      if (a.m != p_.o.R) {
         if (a.m == p_.o.k) {
            com.larpclient.h.U("Scaffold", "Independent view {}", var0);
         } else if (a.m == p_.o.G) {
            com.larpclient.h.A("AutoTrap", "Independent view {}", var0);
         } else if (a.m == p_.o.h) {
            com.larpclient.h.A("AutoWeb", "Independent view {}", var0);
         } else if (a.m == p_.o.d) {
            com.larpclient.h.A("AutoAnchor", "Independent view {}", var0);
         } else if (a.m == p_.o.D) {
            com.larpclient.h.U("AutoCrystal", "Independent view {}", var0);
         } else if (a.m == p_.o.Y) {
            com.larpclient.h.A("PearlCatch", "Silent view {}", var0);
         } else if (a.m == p_.o.y) {
            com.larpclient.h.A("WindHop", "Silent view {}", var0);
         } else {
            if (a.m == p_.o.b) {
               com.larpclient.h.U("Aura", "Independent view {}", var0);
            }

            if (a.m == p_.o.K) {
               com.larpclient.h.U("Mace", "Independent view {}", var0);
            }
         }
      }
   }

   private p_() {
   }

   public enum o {
      T,
      R,
      i,
      k,
      G,
      h,
      d,
      b,
      K,
      D,
      y,
      Y,
      t,
      p;
   }

   public static final class x {
      private p_.o m = p_.o.T;
      private Object y;
      private Object P;

      public p_.o W() {
         return this.m;
      }

      public boolean N(p_.o var1) {
         if (var1 == p_.o.R) {
            return false;
         } else if (this.m == p_.o.R) {
            return true;
         } else if (var1 == p_.o.i) {
            return this.m == p_.o.T || this.m == var1;
         } else if (var1 == p_.o.k) {
            return this.m == p_.o.T || this.m == p_.o.i || this.m == var1;
         } else if (e(var1)) {
            return this.m == p_.o.T || this.m == p_.o.k || this.m == p_.o.i || e(this.m);
         } else if (var1 == p_.o.b) {
            return this.m == p_.o.T || this.m == p_.o.k || this.m == p_.o.i || e(this.m) || this.m == var1;
         } else if (var1 == p_.o.K) {
            return this.m == p_.o.T || this.m == p_.o.k || this.m == p_.o.i || e(this.m) || this.m == p_.o.b || this.m == var1;
         } else if (var1 == p_.o.D) {
            return this.m == p_.o.T || this.m == p_.o.k || this.m == p_.o.i || e(this.m) || this.m == p_.o.b || this.m == var1;
         } else if (var1 == p_.o.y) {
            return this.m != p_.o.t && this.m != p_.o.p && this.m != p_.o.Y;
         } else if (var1 != p_.o.Y) {
            return var1 == p_.o.t ? this.m != p_.o.p : true;
         } else {
            return this.m != p_.o.t && this.m != p_.o.p;
         }
      }

      private static boolean e(p_.o var0) {
         return var0 == p_.o.G || var0 == p_.o.h || var0 == p_.o.d;
      }

      public boolean B(p_.o var1, Object var2, Object var3) {
         if (!this.N(var1)) {
            return false;
         }

         this.m = var1;
         this.y = var2;
         this.P = var3;
         return true;
      }

      public boolean z(p_.o var1) {
         if (this.m == var1 && var1 != p_.o.T && var1 != p_.o.R) {
            this.m = p_.o.R;
            return true;
         } else {
            return false;
         }
      }

      public boolean D(Object var1, Object var2, boolean var3) {
         return var1 != null && var3 && this.y == var1 && this.P == var2;
      }

      public boolean u(p_.o var1) {
         if (this.m != var1) {
            return false;
         }

         this.m = p_.o.T;
         this.y = this.P = null;
         return true;
      }
   }
}
