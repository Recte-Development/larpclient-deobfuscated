package com.larpclient;

import com.larp.obf.qbac0b262827a15ac.qb131e08b200c8a57;
import com.larp.obf.qbac0b262827a15ac.qd9e304165aebf039;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.AutoPearlCatchModule;
import com.larpclient.module.FreecamModule;
import com.larpclient.module.MaceModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.WindChargeHopModule;
import com.larpclient.module.dj;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.phys.Vec3;

public final class tq {
   private static final tq.b H = new tq.b();
   private static CameraType F;
   private static CameraType k;
   private static float Q;
   private static float q;
   private static Vec3 I = Vec3.ZERO;
   private static Vec3 a = Vec3.ZERO;
   public static final int n = 40;
   private static h_ A;
   private static double N;
   private static boolean x;
   private static tq.y y = tq.y.A;
   private static int w;

   public static boolean y(tq.y var0) {
      l();
      return H.I(var0);
   }

   public static void i(tq.y var0) {
      Minecraft var1 = Minecraft.getInstance();
      l();
      if (H.I(var0)) {
         if (H.w == var0) {
            if (!u(var0)) {
               L(var0);
            }
         } else if (H.w == tq.y.P && u(var0)) {
            R("taken over by " + var0);
            k();
            H.Y(var0, var1.player, var1.level);
            W("acquired");
         } else {
            if (u(H.w)) {
               A(H.w);
            }

            if (H.w == tq.y.A && !u(var0)) {
               F = var1.options.getCameraType();
            }

            if (H.Y(var0, var1.player, var1.level)) {
               Q = var1.player.getYRot();
               q = var1.player.getXRot();
               k = var1.options.getCameraType();
               if (u(var0) && k.isMirrored()) {
                  Q += 180.0F;
                  q = -q;
               }

               a = I = var0 == tq.y.F ? var1.player.position().add(0.0, var1.player.getEyeHeight(Pose.STANDING), 0.0) : var1.player.getEyePosition();
               if (!u(var0)) {
                  L(var0);
               } else {
                  W("acquired");
               }
            }
         }
      }
   }

   public static void A(tq.y var0) {
      if (H.w == var0) {
         Minecraft var1 = Minecraft.getInstance();
         if (u(var0) && H.S(var1.player, var1.level, var1.player != null && var1.player.isAlive())) {
            S();
            float[] var2 = i(Q, q, var1.options.getCameraType().isMirrored());
            var2[0] = var1.player.getYRot() + Mth.wrapDegrees(var2[0] - var1.player.getYRot());
            var1.player.setYRot(var2[0]);
            var1.player.setXRot(var2[1]);
            if (var0 == tq.y.Y) {
               var1.player.setYHeadRot(var2[0]);
            }

            var1.player.yRotO = var2[0];
            var1.player.xRotO = var2[1];
         }

         l();
      }
   }

   public static void c(tq.y var0, h_ var1, double var2, boolean var4) {
      if (H.w == var0 && var0 != tq.y.P) {
         if (var1 != null && !(var2 >= 180.0) && u(var0) && R()) {
            Minecraft var5 = Minecraft.getInstance();
            S();
            float[] var6 = i(Q, q, var5.options.getCameraType().isMirrored());
            if (h_.k(var5.player.getYRot(), var5.player.getXRot(), var6[0], var6[1], dj.t())) {
               l();
            } else {
               H.p(var0);
               A = var1;
               N = var2;
               x = var4;
               y = var0;
               w = 0;
               var1.b();
            }
         } else {
            A(var0);
         }
      }
   }

   public static void U() {
      if (l() == tq.y.P) {
         Minecraft var0 = Minecraft.getInstance();
         LocalPlayer var1 = var0.player;
         float[] var2 = i(Q, q, var0.options.getCameraType().isMirrored());
         float var3 = dj.t();
         if (h_.k(var1.getYRot(), var1.getXRot(), var2[0], var2[1], var3)) {
            R("done, " + A.H());
            l();
         } else if (++w > 40) {
            R("not on your look after 40 ticks, so handed back at once; " + A.H());
            A(tq.y.P);
         } else {
            float[] var4 = A.j(var1.getYRot(), var1.getXRot(), var2[0], var2[1], N, x, var3);
            m9.t(m9.s.D, var4[0], var4[1], 1.0, var1.getYRot(), var1.getXRot());
         }
      }
   }

   public static void o() {
      if (H.w == tq.y.P && m9.R() && !m9.F(m9.s.D)) {
         R("ended by another module's turn; " + A.H());
         l();
      }
   }

   public static void x() {
      if (H.w == tq.y.P) {
         Minecraft var0 = Minecraft.getInstance();
         if (N(var0.options.keyAttack) || N(var0.options.keyUse)) {
            j("your click");
            if (var0.player != null && var0.level != null) {
               ((qb131e08b200c8a57)var0).larpPick(1.0F);
            }
         }
      }
   }

   public static void j(String var0) {
      if (H.w == tq.y.P) {
         R("finished at once for " + var0 + "; " + A.H());
         A(tq.y.P);
      }
   }

   private static boolean N(KeyMapping var0) {
      return var0.isDown() || ((qd9e304165aebf039)var0).larpClickCount() > 0;
   }

   public static boolean Q() {
      return H.w == tq.y.P;
   }

   private static void k() {
      A = null;
      y = tq.y.A;
      w = 0;
   }

   private static void R(String var0) {
      String var1 = switch (y) {
         case e -> "Mace";
         default -> "Camera";
         case B -> "WindHop";
         case N -> "PearlCatch";
      };
      mt.K(var1, "Return to your look {}", var0);
   }

   private static boolean R() {
      Minecraft var0 = Minecraft.getInstance();
      return H.S(var0.player, var0.level, var0.player != null && var0.player.isAlive())
         && var0.gui.screen() == null
         && var0.isWindowActive()
         && !var0.player.isSpectator();
   }

   public static void l() {
      if (u(H.w)) {
         W("released");
      }

      if (F != null) {
         Minecraft.getInstance().options.setCameraType(F);
      }

      F = null;
      k = null;
      H.C(H.w);
      k();
      a = I = Vec3.ZERO;
      q = 0.0F;
      Q = 0.0F;
   }

   public static tq.y l() {
      if (H.w == tq.y.Y) {
         ScaffoldModule var0 = ScaffoldModule.i();
         if (!R() || var0 == null || !var0.n()) {
            A(tq.y.Y);
         }
      }

      if (H.w == tq.y.J) {
         com.larpclient.module.f var1 = com.larpclient.module.f.X();
         if (!R() || var1 == null || !var1.w()) {
            A(tq.y.J);
         }
      }

      if (H.w == tq.y.a) {
         com.larpclient.module.w var2 = com.larpclient.module.w.Y();
         if (!R() || var2 == null || !var2.f()) {
            A(tq.y.a);
         }
      }

      if (H.w == tq.y.g) {
         AutoAnchorModule var3 = AutoAnchorModule.S();
         if (!R() || var3 == null || !var3.B()) {
            A(tq.y.g);
         }
      }

      if (H.w == tq.y.u) {
         AuraModule var4 = AuraModule.S();
         if (!R() || var4 == null || !var4.D()) {
            A(tq.y.u);
         }
      }

      if (H.w == tq.y.e) {
         MaceModule var5 = MaceModule.c();
         if (!R() || var5 == null) {
            A(tq.y.e);
         } else if (!var5.k()) {
            c(tq.y.e, var5.W(), var5.A(), true);
         }
      }

      if (H.w == tq.y.K) {
         AutoCrystalModule var6 = AutoCrystalModule.S();
         if (!R() || var6 == null || !var6.S()) {
            A(tq.y.K);
         }
      }

      if (H.w == tq.y.N) {
         AutoPearlCatchModule var7 = AutoPearlCatchModule.D();
         if (!R() || var7 == null) {
            A(tq.y.N);
         } else if (!var7.a()) {
            c(tq.y.N, var7.c(), var7.o(), var7.m());
         }
      }

      if (H.w == tq.y.B) {
         WindChargeHopModule var8 = WindChargeHopModule.C();
         if (!R() || var8 == null) {
            A(tq.y.B);
         } else if (!var8.g()) {
            c(tq.y.B, var8.P(), var8.e(), var8.y());
         }
      }

      if (H.w == tq.y.P && !R()) {
         A(tq.y.P);
      }

      Minecraft var9 = Minecraft.getInstance();
      if (H.w != tq.y.A && !H.S(var9.player, var9.level, var9.player != null && var9.player.isAlive())) {
         l();
      }

      if (u(H.w)) {
         S();
      }

      return H.w;
   }

   private static boolean u(tq.y var0) {
      return var0 == tq.y.u
         || var0 == tq.y.e
         || var0 == tq.y.J
         || var0 == tq.y.a
         || var0 == tq.y.g
         || var0 == tq.y.K
         || var0 == tq.y.Y
         || var0 == tq.y.N
         || var0 == tq.y.B
         || var0 == tq.y.P;
   }

   public static boolean q() {
      return u(l());
   }

   private static void S() {
      CameraType var0 = Minecraft.getInstance().options.getCameraType();
      if (k != null && k.isMirrored() != var0.isMirrored()) {
         Q = Q + (var0.isMirrored() ? 180.0F : -180.0F);
         q = -q;
      }

      k = var0;
   }

   public static boolean f() {
      return l() == tq.y.F;
   }

   public static void Q(double var0, double var2) {
      Q += (float)var0 * 0.15F;
      float var4 = f() ? 90.0F : 89.0F;
      q = Mth.clamp(q + (float)var2 * 0.15F, -var4, var4);
   }

   public static float J() {
      return Q;
   }

   public static float z() {
      return Minecraft.getInstance().options.getCameraType().isMirrored() ? Q - 180.0F : Q;
   }

   public static float F() {
      return q;
   }

   public static Vec3 N(float var0) {
      return a.lerp(I, var0);
   }

   public static void C(double var0, double var2, boolean var4) {
      Minecraft var5 = Minecraft.getInstance();
      a = I;
      if (f() && var5.gui.screen() == null && var5.isWindowActive()) {
         I = I.add(hs.E(Q, FreecamModule.z(), var0, var2, var4));
      }
   }

   private static void L(tq.y var0) {
      Minecraft var1 = Minecraft.getInstance();
      CameraType var2 = F == null ? var1.options.getCameraType() : F;
      CameraType var3 = var0 == tq.y.F ? (FreecamModule.p() && var2.isFirstPerson() ? CameraType.THIRD_PERSON_BACK : var2) : CameraType.THIRD_PERSON_BACK;
      if (var1.options.getCameraType() != var3) {
         var1.options.setCameraType(var3);
      }
   }

   public static float[] i(float var0, float var1, boolean var2) {
      return new float[]{var2 ? var0 - 180.0F : var0, var2 ? -var1 : var1};
   }

   private static void W(String var0) {
      if (H.w != tq.y.P) {
         if (H.w == tq.y.Y) {
            mt.U("Scaffold", "Independent view {}", var0);
         } else if (H.w == tq.y.J) {
            mt.K("AutoTrap", "Independent view {}", var0);
         } else if (H.w == tq.y.a) {
            mt.K("AutoWeb", "Independent view {}", var0);
         } else if (H.w == tq.y.g) {
            mt.K("AutoAnchor", "Independent view {}", var0);
         } else if (H.w == tq.y.K) {
            mt.U("AutoCrystal", "Independent view {}", var0);
         } else if (H.w == tq.y.N) {
            mt.K("PearlCatch", "Silent view {}", var0);
         } else if (H.w == tq.y.B) {
            mt.K("WindHop", "Silent view {}", var0);
         } else {
            if (H.w == tq.y.u) {
               mt.U("Aura", "Independent view {}", var0);
            }

            if (H.w == tq.y.e) {
               mt.U("Mace", "Independent view {}", var0);
            }
         }
      }
   }

   private tq() {
   }

   public static final class b {
      private tq.y w = tq.y.A;
      private Object W;
      private Object R;

      public tq.y e() {
         return this.w;
      }

      public boolean I(tq.y var1) {
         if (var1 == tq.y.P) {
            return false;
         } else if (this.w == tq.y.P) {
            return true;
         } else if (var1 == tq.y.Y) {
            return this.w == tq.y.A || this.w == var1;
         } else if (u(var1)) {
            return this.w == tq.y.A || this.w == tq.y.Y || u(this.w);
         } else if (var1 == tq.y.u) {
            return this.w == tq.y.A || this.w == tq.y.Y || u(this.w) || this.w == var1;
         } else if (var1 == tq.y.e) {
            return this.w == tq.y.A || this.w == tq.y.Y || u(this.w) || this.w == tq.y.u || this.w == var1;
         } else if (var1 == tq.y.K) {
            return this.w == tq.y.A || this.w == tq.y.Y || u(this.w) || this.w == tq.y.u || this.w == var1;
         } else if (var1 == tq.y.B) {
            return this.w != tq.y.O && this.w != tq.y.F && this.w != tq.y.N;
         } else if (var1 != tq.y.N) {
            return var1 == tq.y.O ? this.w != tq.y.F : true;
         } else {
            return this.w != tq.y.O && this.w != tq.y.F;
         }
      }

      private static boolean u(tq.y var0) {
         return var0 == tq.y.J || var0 == tq.y.a || var0 == tq.y.g;
      }

      public boolean Y(tq.y var1, Object var2, Object var3) {
         if (!this.I(var1)) {
            return false;
         }

         this.w = var1;
         this.W = var2;
         this.R = var3;
         return true;
      }

      public boolean p(tq.y var1) {
         if (this.w == var1 && var1 != tq.y.A && var1 != tq.y.P) {
            this.w = tq.y.P;
            return true;
         } else {
            return false;
         }
      }

      public boolean S(Object var1, Object var2, boolean var3) {
         return var1 != null && var3 && this.W == var1 && this.R == var2;
      }

      public boolean C(tq.y var1) {
         if (this.w != var1) {
            return false;
         }

         this.w = tq.y.A;
         this.W = this.R = null;
         return true;
      }
   }

   public enum y {
      A,
      P,
      Y,
      J,
      a,
      g,
      u,
      e,
      K,
      B,
      N,
      O,
      F;
   }
}
