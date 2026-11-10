package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.qc90ed4dd4cb15de4;
import com.larp.obf.q41a5851bcf8e876a.qee5f93289f869c83;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.AutoPearlCatchModule;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.WindChargeHopModule;
import com.larpclient.module.kb;
import com.larpclient.module.kv;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

public final class ij {
   private static final ij.v J = new ij.v();
   private static CameraType I;
   private static CameraType G;
   private static float y;
   private static float q;
   private static Vec3 r = Vec3.ZERO;
   private static Vec3 B = Vec3.ZERO;
   public static final int C = 40;
   private static _b E;
   private static double g;
   private static boolean s;
   private static ij.r Q = ij.r.X;
   private static int m;

   public static boolean a(ij.r var0) {
      L();
      return J.p(var0);
   }

   public static void V(ij.r var0) {
      Minecraft var1 = Minecraft.getInstance();
      L();
      if (J.p(var0)) {
         if (J.i == var0) {
            if (!l(var0)) {
               H(var0);
            }
         } else if (J.i == ij.r.o && l(var0)) {
            l("taken over by " + var0);
            v();
            J.f(var0, var1.player, var1.level);
            H("acquired");
         } else {
            if (l(J.i)) {
               W(J.i);
            }

            if (J.i == ij.r.X && !l(var0)) {
               I = var1.options.getCameraType();
            }

            if (J.f(var0, var1.player, var1.level)) {
               y = var1.player.getYRot();
               q = var1.player.getXRot();
               G = var1.options.getCameraType();
               if (l(var0) && G.isMirrored()) {
                  y += 180.0F;
                  q = -q;
               }

               B = r = var0 == ij.r.f ? var1.player.position().add(0.0, var1.player.getEyeHeight(Pose.STANDING), 0.0) : var1.player.getEyePosition();
               if (!l(var0)) {
                  H(var0);
               } else {
                  H("acquired");
               }
            }
         }
      }
   }

   public static void W(ij.r var0) {
      if (J.i == var0) {
         Minecraft var1 = Minecraft.getInstance();
         if (l(var0) && J.Q(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
            s();
            float[] var2 = p(y, q, var1.options.getCameraType().isMirrored());
            var2[0] = var1.player.getYRot() + Mth.wrapDegrees(var2[0] - var1.player.getYRot());
            var1.player.setYRot(var2[0]);
            var1.player.setXRot(var2[1]);
            if (var0 == ij.r.B) {
               var1.player.setYHeadRot(var2[0]);
            }

            var1.player.yRotO = var2[0];
            var1.player.xRotO = var2[1];
         }

         F();
         if (var0 == ij.r.i || var0 == ij.r.f) {
            ScaffoldModule.x();
         }
      }
   }

   public static void P(ij.r var0, _b var1, double var2, boolean var4) {
      if (J.i == var0 && var0 != ij.r.o) {
         if (var1 != null && !(var2 >= 180.0) && l(var0) && b()) {
            Minecraft var5 = Minecraft.getInstance();
            s();
            float[] var6 = p(y, q, var5.options.getCameraType().isMirrored());
            if (_b.Z(var5.player.getYRot(), var5.player.getXRot(), var6[0], var6[1], com.larpclient.module.w.d())) {
               F();
            } else {
               J.u(var0);
               E = var1;
               g = var2;
               s = var4;
               Q = var0;
               m = 0;
               var1.T();
            }
         } else {
            W(var0);
         }
      }
   }

   public static void i() {
      if (L() == ij.r.o) {
         Minecraft var0 = Minecraft.getInstance();
         LocalPlayer var1 = var0.player;
         float[] var2 = p(y, q, var0.options.getCameraType().isMirrored());
         float var3 = com.larpclient.module.w.d();
         if (_b.Z(var1.getYRot(), var1.getXRot(), var2[0], var2[1], var3)) {
            l("done, " + E.m());
            F();
         } else if (++m > 40) {
            l("not on your look after 40 ticks, so handed back at once; " + E.m());
            W(ij.r.o);
         } else {
            float[] var4 = E.e(var1.getYRot(), var1.getXRot(), var2[0], var2[1], g, s, var3);
            b6.E(b6.w.b, var4[0], var4[1], 1.0, var1.getYRot(), var1.getXRot());
         }
      }
   }

   public static void z() {
      if (J.i == ij.r.o && b6.x() && !b6.c(b6.w.b)) {
         l("ended by another module's turn; " + E.m());
         F();
      }
   }

   public static void S() {
      if (J.i == ij.r.o) {
         Minecraft var0 = Minecraft.getInstance();
         if (r(var0.options.keyAttack) || r(var0.options.keyUse)) {
            B("your click");
            if (var0.player != null && var0.level != null) {
               ((qc90ed4dd4cb15de4)var0).larpPick(1.0F);
            }
         }
      }
   }

   public static void B(String var0) {
      if (J.i == ij.r.o) {
         l("finished at once for " + var0 + "; " + E.m());
         W(ij.r.o);
      }
   }

   private static boolean r(KeyMapping var0) {
      return var0.isDown() || ((qee5f93289f869c83)var0).larpClickCount() > 0;
   }

   public static boolean Y() {
      return J.i == ij.r.o;
   }

   private static void v() {
      E = null;
      Q = ij.r.X;
      m = 0;
   }

   private static void l(String var0) {
      String var1 = switch (Q) {
         case z -> "Auto Builder";
         default -> "Camera";
         case w -> "Mace";
         case q -> "WindHop";
         case J -> "PearlCatch";
      };
      _q.z(var1, "Return to your look {}", var0);
   }

   private static boolean b() {
      Minecraft var0 = Minecraft.getInstance();
      return J.Q(var0.player, var0.level, var0.player != null && var0.player.isAlive())
         && var0.gui.screen() == null
         && var0.isWindowActive()
         && !var0.player.isSpectator();
   }

   public static void F() {
      if (l(J.i)) {
         H("released");
         X();
      }

      if (I != null) {
         Minecraft.getInstance().options.setCameraType(I);
      }

      I = null;
      G = null;
      J.W(J.i);
      v();
      B = r = Vec3.ZERO;
      q = 0.0F;
      y = 0.0F;
   }

   public static ij.r L() {
      if (J.i == ij.r.B) {
         ScaffoldModule var0 = ScaffoldModule.Z();
         if (!b() || var0 == null || !var0.a()) {
            W(ij.r.B);
         }
      }

      if (J.i == ij.r.D) {
         kv var1 = kv.m();
         if (!b() || var1 == null || !var1.Y()) {
            W(ij.r.D);
         }
      }

      if (J.i == ij.r.s) {
         kb var2 = kb.u();
         if (!b() || var2 == null || !var2.w()) {
            W(ij.r.s);
         }
      }

      if (J.i == ij.r.I) {
         AutoAnchorModule var3 = AutoAnchorModule.q();
         if (!b() || var3 == null || !var3.K()) {
            W(ij.r.I);
         }
      }

      if (J.i == ij.r.N) {
         AuraModule var4 = AuraModule.t();
         if (!b() || var4 == null || !var4.I()) {
            W(ij.r.N);
         }
      }

      if (J.i == ij.r.w) {
         MaceModule var5 = MaceModule.U();
         if (!b() || var5 == null) {
            W(ij.r.w);
         } else if (!var5.X()) {
            P(ij.r.w, var5.f(), var5.y(), true);
         }
      }

      if (J.i == ij.r.p) {
         AutoCrystalModule var6 = AutoCrystalModule.d();
         if (!b() || var6 == null || !var6.n()) {
            W(ij.r.p);
         }
      }

      if (J.i == ij.r.J) {
         AutoPearlCatchModule var7 = AutoPearlCatchModule.l();
         if (!b() || var7 == null) {
            W(ij.r.J);
         } else if (!var7.j()) {
            P(ij.r.J, var7.Z(), var7.d(), var7.G());
         }
      }

      if (J.i == ij.r.q) {
         WindChargeHopModule var8 = WindChargeHopModule.Q();
         if (!b() || var8 == null) {
            W(ij.r.q);
         } else if (!var8.D()) {
            P(ij.r.q, var8.x(), var8.t(), var8.c());
         }
      }

      if (J.i == ij.r.z) {
         _d var9 = _d.Z();
         if (!b() || var9 == null) {
            W(ij.r.z);
         } else if (!var9.m()) {
            P(ij.r.z, var9.N(), var9.g(), true);
         }
      }

      if (J.i == ij.r.o && !b()) {
         W(ij.r.o);
      }

      Minecraft var10 = Minecraft.getInstance();
      if (J.i != ij.r.X && !J.Q(var10.player, var10.level, var10.player != null && var10.player.isAlive())) {
         F();
      }

      if (l(J.i)) {
         s();
      }

      return J.i;
   }

   private static boolean l(ij.r var0) {
      return var0 == ij.r.N
         || var0 == ij.r.w
         || var0 == ij.r.D
         || var0 == ij.r.s
         || var0 == ij.r.I
         || var0 == ij.r.p
         || var0 == ij.r.B
         || var0 == ij.r.J
         || var0 == ij.r.q
         || var0 == ij.r.o
         || var0 == ij.r.z;
   }

   public static boolean g() {
      return l(L());
   }

   private static void s() {
      CameraType var0 = Minecraft.getInstance().options.getCameraType();
      if (G != null && G.isMirrored() != var0.isMirrored()) {
         y = y + (var0.isMirrored() ? 180.0F : -180.0F);
         q = -q;
      }

      G = var0;
   }

   public static boolean y() {
      return L() == ij.r.f;
   }

   public static float G(float var0) {
      return !g() ? Float.NaN : x(p(y, q, Minecraft.getInstance().options.getCameraType().isMirrored())[0], var0);
   }

   public static float M() {
      return !g() ? Float.NaN : p(y, q, Minecraft.getInstance().options.getCameraType().isMirrored())[1];
   }

   public static float x(float var0, float var1) {
      return var1 + Mth.wrapDegrees(var0 - var1);
   }

   public static float B(float var0, float var1) {
      return Math.round((var1 - var0) / 360.0F) * 360.0F;
   }

   private static void X() {
      LocalPlayer var0 = Minecraft.getInstance().player;
      if (var0 != null) {
         float var1 = B(var0.yBob, var0.getYRot());
         if (var1 != 0.0F) {
            var0.yBob += var1;
            var0.yBobO += var1;
            _q.c("Camera", "Hand sway moved {} degrees back onto the player's rotation", var1);
         }
      }
   }

   public static void c(double var0, double var2) {
      y += (float)var0 * 0.15F;
      float var4 = y() ? 90.0F : 89.0F;
      q = Mth.clamp(q + (float)var2 * 0.15F, -var4, var4);
   }

   public static float D() {
      return y;
   }

   public static float f() {
      return Minecraft.getInstance().options.getCameraType().isMirrored() ? y - 180.0F : y;
   }

   public static float J() {
      return q;
   }

   public static Vec3 J(float var0) {
      return B.lerp(r, var0);
   }

   public static void y(double var0, double var2, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      B = r;
      if (y() && var5.gui.screen() == null && var5.isWindowActive()) {
         r = r.add(b7.d(y, FreecamModule.C(), var0, var2, var4));
      }
   }

   private static void H(ij.r var0) {
      Minecraft var1 = Minecraft.getInstance();
      CameraType var2 = I == null ? var1.options.getCameraType() : I;
      CameraType var3 = var0 == ij.r.f ? (FreecamModule.T() && var2.isFirstPerson() ? CameraType.THIRD_PERSON_BACK : var2) : CameraType.THIRD_PERSON_BACK;
      if (var1.options.getCameraType() != var3) {
         var1.options.setCameraType(var3);
      }
   }

   public static float[] p(float var0, float var1, boolean var2) {
      return new float[]{var2 ? var0 - 180.0F : var0, var2 ? -var1 : var1};
   }

   private static void H(String var0) {
      if (J.i != ij.r.o) {
         if (J.i == ij.r.B) {
            _q.c("Scaffold", "Independent view {}", var0);
         } else if (J.i == ij.r.D) {
            _q.z("AutoTrap", "Independent view {}", var0);
         } else if (J.i == ij.r.s) {
            _q.z("AutoWeb", "Independent view {}", var0);
         } else if (J.i == ij.r.I) {
            _q.z("AutoAnchor", "Independent view {}", var0);
         } else if (J.i == ij.r.p) {
            _q.c("AutoCrystal", "Independent view {}", var0);
         } else if (J.i == ij.r.J) {
            _q.z("PearlCatch", "Silent view {}", var0);
         } else if (J.i == ij.r.q) {
            _q.z("WindHop", "Silent view {}", var0);
         } else {
            if (J.i == ij.r.N) {
               _q.c("Aura", "Independent view {}", var0);
            }

            if (J.i == ij.r.w) {
               _q.c("Mace", "Independent view {}", var0);
            }
         }
      }
   }

   private ij() {
   }

   public enum r {
      X,
      o,
      z,
      B,
      D,
      s,
      I,
      N,
      w,
      p,
      q,
      J,
      i,
      f;
   }

   public static final class v {
      private ij.r i = ij.r.X;
      private Object P;
      private Object X;

      public ij.r Z() {
         return this.i;
      }

      public boolean p(ij.r var1) {
         if (var1 == ij.r.o) {
            return false;
         } else if (this.i == ij.r.o) {
            return true;
         } else if (var1 == ij.r.z) {
            return this.i == ij.r.X || this.i == var1;
         } else if (var1 == ij.r.B) {
            return this.i == ij.r.X || this.i == ij.r.z || this.i == var1;
         } else if (f(var1)) {
            return this.i == ij.r.X || this.i == ij.r.B || this.i == ij.r.z || f(this.i);
         } else if (var1 == ij.r.N) {
            return this.i == ij.r.X || this.i == ij.r.B || this.i == ij.r.z || f(this.i) || this.i == var1;
         } else if (var1 == ij.r.w) {
            return this.i == ij.r.X || this.i == ij.r.B || this.i == ij.r.z || f(this.i) || this.i == ij.r.N || this.i == var1;
         } else if (var1 == ij.r.p) {
            return this.i == ij.r.X || this.i == ij.r.B || this.i == ij.r.z || f(this.i) || this.i == ij.r.N || this.i == var1;
         } else if (var1 == ij.r.q) {
            return this.i != ij.r.i && this.i != ij.r.f && this.i != ij.r.J;
         } else if (var1 != ij.r.J) {
            return var1 == ij.r.i ? this.i != ij.r.f : true;
         } else {
            return this.i != ij.r.i && this.i != ij.r.f;
         }
      }

      private static boolean f(ij.r var0) {
         return var0 == ij.r.D || var0 == ij.r.s || var0 == ij.r.I;
      }

      public boolean f(ij.r var1, Object var2, Object var3) {
         if (!this.p(var1)) {
            return false;
         }

         this.i = var1;
         this.P = var2;
         this.X = var3;
         return true;
      }

      public boolean u(ij.r var1) {
         if (this.i == var1 && var1 != ij.r.X && var1 != ij.r.o) {
            this.i = ij.r.o;
            return true;
         } else {
            return false;
         }
      }

      public boolean Q(Object var1, Object var2, boolean var3) {
         return var1 != null && var3 && this.P == var1 && this.X == var2;
      }

      public boolean W(ij.r var1) {
         if (this.i != var1) {
            return false;
         }

         this.i = ij.r.X;
         this.P = this.X = null;
         return true;
      }
   }
}
