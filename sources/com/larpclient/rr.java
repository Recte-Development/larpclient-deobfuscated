package com.larpclient;

import com.larp.obf.q5682c6a8aca9b6ec.q19f6d37a64cd71d5;
import com.larp.obf.q5682c6a8aca9b6ec.qa1ad45ac3ead4599;
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

public final class rr {
   private static final rr.l D = new rr.l();
   private static CameraType A;
   private static CameraType H;
   private static float q;
   private static float w;
   private static Vec3 l = Vec3.ZERO;
   private static Vec3 S = Vec3.ZERO;
   public static final int n = 40;
   private static l0 i;
   private static double C;
   private static boolean k;
   private static rr.f Q = rr.f.i;
   private static int t;

   public static boolean j(rr.f var0) {
      u();
      return D.Q(var0);
   }

   public static void W(rr.f var0) {
      Minecraft var1 = Minecraft.getInstance();
      u();
      if (D.Q(var0)) {
         if (D.n == var0) {
            if (!p(var0)) {
               C(var0);
            }
         } else if (D.n == rr.f.E && p(var0)) {
            Q("taken over by " + var0);
            E();
            D.H(var0, var1.player, var1.level);
            B("acquired");
         } else {
            if (p(D.n)) {
               N(D.n);
            }

            if (D.n == rr.f.i && !p(var0)) {
               A = var1.options.getCameraType();
            }

            if (D.H(var0, var1.player, var1.level)) {
               q = var1.player.getYRot();
               w = var1.player.getXRot();
               H = var1.options.getCameraType();
               if (p(var0) && H.isMirrored()) {
                  q += 180.0F;
                  w = -w;
               }

               S = l = var0 == rr.f.u ? var1.player.position().add(0.0, var1.player.getEyeHeight(Pose.STANDING), 0.0) : var1.player.getEyePosition();
               if (!p(var0)) {
                  C(var0);
               } else {
                  B("acquired");
               }
            }
         }
      }
   }

   public static void N(rr.f var0) {
      if (D.n == var0) {
         Minecraft var1 = Minecraft.getInstance();
         if (p(var0) && D.B(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
            D();
            float[] var2 = l(q, w, var1.options.getCameraType().isMirrored());
            var2[0] = var1.player.getYRot() + Mth.wrapDegrees(var2[0] - var1.player.getYRot());
            var1.player.setYRot(var2[0]);
            var1.player.setXRot(var2[1]);
            if (var0 == rr.f.x) {
               var1.player.setYHeadRot(var2[0]);
            }

            var1.player.yRotO = var2[0];
            var1.player.xRotO = var2[1];
         }

         g();
      }
   }

   public static void L(rr.f var0, l0 var1, double var2, boolean var4) {
      if (D.n == var0 && var0 != rr.f.E) {
         if (var1 != null && !(var2 >= 180.0) && p(var0) && C()) {
            Minecraft var5 = Minecraft.getInstance();
            D();
            float[] var6 = l(q, w, var5.options.getCameraType().isMirrored());
            if (l0.g(var5.player.getYRot(), var5.player.getXRot(), var6[0], var6[1], com.larpclient.module.le.e())) {
               g();
            } else {
               D.h(var0);
               i = var1;
               C = var2;
               k = var4;
               Q = var0;
               t = 0;
               var1.O();
            }
         } else {
            N(var0);
         }
      }
   }

   public static void X() {
      if (u() == rr.f.E) {
         Minecraft var0 = Minecraft.getInstance();
         LocalPlayer var1 = var0.player;
         float[] var2 = l(q, w, var0.options.getCameraType().isMirrored());
         float var3 = com.larpclient.module.le.e();
         if (l0.g(var1.getYRot(), var1.getXRot(), var2[0], var2[1], var3)) {
            Q("done, " + i.F());
            g();
         } else if (++t > 40) {
            Q("not on your look after 40 ticks, so handed back at once; " + i.F());
            N(rr.f.E);
         } else {
            float[] var4 = i.B(var1.getYRot(), var1.getXRot(), var2[0], var2[1], C, k, var3);
            ep.q(ep.z.Y, var4[0], var4[1], 1.0, var1.getYRot(), var1.getXRot());
         }
      }
   }

   public static void t() {
      if (D.n == rr.f.E && ep.f() && !ep.W(ep.z.Y)) {
         Q("ended by another module's turn; " + i.F());
         g();
      }
   }

   public static void C() {
      if (D.n == rr.f.E) {
         Minecraft var0 = Minecraft.getInstance();
         if (H(var0.options.keyAttack) || H(var0.options.keyUse)) {
            T("your click");
            if (var0.player != null && var0.level != null) {
               ((qa1ad45ac3ead4599)var0).larpPick(1.0F);
            }
         }
      }
   }

   public static void T(String var0) {
      if (D.n == rr.f.E) {
         Q("finished at once for " + var0 + "; " + i.F());
         N(rr.f.E);
      }
   }

   private static boolean H(KeyMapping var0) {
      return var0.isDown() || ((q19f6d37a64cd71d5)var0).larpClickCount() > 0;
   }

   public static boolean n() {
      return D.n == rr.f.E;
   }

   private static void E() {
      i = null;
      Q = rr.f.i;
      t = 0;
   }

   private static void Q(String var0) {
      String var1 = switch (Q) {
         case T -> "Mace";
         default -> "Camera";
         case m -> "WindHop";
         case H -> "PearlCatch";
      };
      r4.V(var1, "Return to your look {}", var0);
   }

   private static boolean C() {
      Minecraft var0 = Minecraft.getInstance();
      return D.B(var0.player, var0.level, var0.player != null && var0.player.isAlive())
         && var0.gui.screen() == null
         && var0.isWindowActive()
         && !var0.player.isSpectator();
   }

   public static void g() {
      if (p(D.n)) {
         B("released");
      }

      if (A != null) {
         Minecraft.getInstance().options.setCameraType(A);
      }

      A = null;
      H = null;
      D.l(D.n);
      E();
      S = l = Vec3.ZERO;
      w = 0.0F;
      q = 0.0F;
   }

   public static rr.f u() {
      if (D.n == rr.f.x) {
         ScaffoldModule var0 = ScaffoldModule.v();
         if (!C() || var0 == null || !var0.v()) {
            N(rr.f.x);
         }
      }

      if (D.n == rr.f.e) {
         com.larpclient.module.t var1 = com.larpclient.module.t.d();
         if (!C() || var1 == null || !var1.n()) {
            N(rr.f.e);
         }
      }

      if (D.n == rr.f.L) {
         com.larpclient.module.k var2 = com.larpclient.module.k.v();
         if (!C() || var2 == null || !var2.g()) {
            N(rr.f.L);
         }
      }

      if (D.n == rr.f.q) {
         AutoAnchorModule var3 = AutoAnchorModule.y();
         if (!C() || var3 == null || !var3.J()) {
            N(rr.f.q);
         }
      }

      if (D.n == rr.f.B) {
         AuraModule var4 = AuraModule.G();
         if (!C() || var4 == null || !var4.Y()) {
            N(rr.f.B);
         }
      }

      if (D.n == rr.f.T) {
         MaceModule var5 = MaceModule.Q();
         if (!C() || var5 == null) {
            N(rr.f.T);
         } else if (!var5.E()) {
            L(rr.f.T, var5.W(), var5.z(), true);
         }
      }

      if (D.n == rr.f.y) {
         AutoCrystalModule var6 = AutoCrystalModule.T();
         if (!C() || var6 == null || !var6.b()) {
            N(rr.f.y);
         }
      }

      if (D.n == rr.f.H) {
         AutoPearlCatchModule var7 = AutoPearlCatchModule.g();
         if (!C() || var7 == null) {
            N(rr.f.H);
         } else if (!var7.n()) {
            L(rr.f.H, var7.g(), var7.G(), var7.v());
         }
      }

      if (D.n == rr.f.m) {
         WindChargeHopModule var8 = WindChargeHopModule.Q();
         if (!C() || var8 == null) {
            N(rr.f.m);
         } else if (!var8.d()) {
            L(rr.f.m, var8.i(), var8.L(), var8.M());
         }
      }

      if (D.n == rr.f.E && !C()) {
         N(rr.f.E);
      }

      Minecraft var9 = Minecraft.getInstance();
      if (D.n != rr.f.i && !D.B(var9.player, var9.level, var9.player != null && var9.player.isAlive())) {
         g();
      }

      if (p(D.n)) {
         D();
      }

      return D.n;
   }

   private static boolean p(rr.f var0) {
      return var0 == rr.f.B
         || var0 == rr.f.T
         || var0 == rr.f.e
         || var0 == rr.f.L
         || var0 == rr.f.q
         || var0 == rr.f.y
         || var0 == rr.f.x
         || var0 == rr.f.H
         || var0 == rr.f.m
         || var0 == rr.f.E;
   }

   public static boolean e() {
      return p(u());
   }

   private static void D() {
      CameraType var0 = Minecraft.getInstance().options.getCameraType();
      if (H != null && H.isMirrored() != var0.isMirrored()) {
         q = q + (var0.isMirrored() ? 180.0F : -180.0F);
         w = -w;
      }

      H = var0;
   }

   public static boolean o() {
      return u() == rr.f.u;
   }

   public static void H(double var0, double var2) {
      q += (float)var0 * 0.15F;
      float var4 = o() ? 90.0F : 89.0F;
      w = Mth.clamp(w + (float)var2 * 0.15F, -var4, var4);
   }

   public static float j() {
      return q;
   }

   public static float h() {
      return Minecraft.getInstance().options.getCameraType().isMirrored() ? q - 180.0F : q;
   }

   public static float T() {
      return w;
   }

   public static Vec3 i(float var0) {
      return S.lerp(l, var0);
   }

   public static void S(double var0, double var2, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      S = l;
      if (o() && var5.gui.screen() == null && var5.isWindowActive()) {
         l = l.add(yw.b(q, FreecamModule.w(), var0, var2, var4));
      }
   }

   private static void C(rr.f var0) {
      Minecraft var1 = Minecraft.getInstance();
      CameraType var2 = A == null ? var1.options.getCameraType() : A;
      CameraType var3 = var0 == rr.f.u ? (FreecamModule.j() && var2.isFirstPerson() ? CameraType.THIRD_PERSON_BACK : var2) : CameraType.THIRD_PERSON_BACK;
      if (var1.options.getCameraType() != var3) {
         var1.options.setCameraType(var3);
      }
   }

   public static float[] l(float var0, float var1, boolean var2) {
      return new float[]{var2 ? var0 - 180.0F : var0, var2 ? -var1 : var1};
   }

   private static void B(String var0) {
      if (D.n != rr.f.E) {
         if (D.n == rr.f.x) {
            r4.g("Scaffold", "Independent view {}", var0);
         } else if (D.n == rr.f.e) {
            r4.V("AutoTrap", "Independent view {}", var0);
         } else if (D.n == rr.f.L) {
            r4.V("AutoWeb", "Independent view {}", var0);
         } else if (D.n == rr.f.q) {
            r4.V("AutoAnchor", "Independent view {}", var0);
         } else if (D.n == rr.f.y) {
            r4.g("AutoCrystal", "Independent view {}", var0);
         } else if (D.n == rr.f.H) {
            r4.V("PearlCatch", "Silent view {}", var0);
         } else if (D.n == rr.f.m) {
            r4.V("WindHop", "Silent view {}", var0);
         } else {
            if (D.n == rr.f.B) {
               r4.g("Aura", "Independent view {}", var0);
            }

            if (D.n == rr.f.T) {
               r4.g("Mace", "Independent view {}", var0);
            }
         }
      }
   }

   private rr() {
   }

   public enum f {
      i,
      E,
      x,
      e,
      L,
      q,
      B,
      T,
      y,
      m,
      H,
      R,
      u;
   }

   public static final class l {
      private rr.f n = rr.f.i;
      private Object P;
      private Object U;

      public rr.f F() {
         return this.n;
      }

      public boolean Q(rr.f var1) {
         if (var1 == rr.f.E) {
            return false;
         } else if (this.n == rr.f.E) {
            return true;
         } else if (var1 == rr.f.x) {
            return this.n == rr.f.i || this.n == var1;
         } else if (k(var1)) {
            return this.n == rr.f.i || this.n == rr.f.x || k(this.n);
         } else if (var1 == rr.f.B) {
            return this.n == rr.f.i || this.n == rr.f.x || k(this.n) || this.n == var1;
         } else if (var1 == rr.f.T) {
            return this.n == rr.f.i || this.n == rr.f.x || k(this.n) || this.n == rr.f.B || this.n == var1;
         } else if (var1 == rr.f.y) {
            return this.n == rr.f.i || this.n == rr.f.x || k(this.n) || this.n == rr.f.B || this.n == var1;
         } else if (var1 == rr.f.m) {
            return this.n != rr.f.R && this.n != rr.f.u && this.n != rr.f.H;
         } else if (var1 != rr.f.H) {
            return var1 == rr.f.R ? this.n != rr.f.u : true;
         } else {
            return this.n != rr.f.R && this.n != rr.f.u;
         }
      }

      private static boolean k(rr.f var0) {
         return var0 == rr.f.e || var0 == rr.f.L || var0 == rr.f.q;
      }

      public boolean H(rr.f var1, Object var2, Object var3) {
         if (!this.Q(var1)) {
            return false;
         }

         this.n = var1;
         this.P = var2;
         this.U = var3;
         return true;
      }

      public boolean h(rr.f var1) {
         if (this.n == var1 && var1 != rr.f.i && var1 != rr.f.E) {
            this.n = rr.f.E;
            return true;
         } else {
            return false;
         }
      }

      public boolean B(Object var1, Object var2, boolean var3) {
         return var1 != null && var3 && this.P == var1 && this.U == var2;
      }

      public boolean l(rr.f var1) {
         if (this.n != var1) {
            return false;
         }

         this.n = rr.f.i;
         this.P = this.U = null;
         return true;
      }
   }
}
