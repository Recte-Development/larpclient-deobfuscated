package com.larpclient.module;

import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.sq;
import com.larpclient.wp;
import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_10185;
import net.minecraft.class_1309;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_638;
import net.minecraft.class_746;

public final class TargetStrafeModule extends Module {
   private static final int k = 3;
   private static final int j = 10;
   private static final float l = 25.0F;
   private static final double h = Math.PI / 3;
   private static final double A = 0.7;
   private static final double q = 1.0;
   private static final float[] d = new float[]{0.0F, 0.0F};
   private static TargetStrafeModule T;
   private final li W = this.addSetting(
      new li(
         "Mode",
         "Native presses real movement keys at normal speed for anti-cheat servers; Fast overrides your speed and will flag on them",
         "Native",
         "Native",
         "Fast"
      )
   );
   private final lo g = this.addSetting(new lo("Distance", "Distance to hold from the target center, capped inside actual attack reach", 2.4, 1.5, 3.0, 0.1));
   private final lo b = this.addSetting(new lo("Speed", "Horizontal orbit speed in blocks per tick", 1.0, 0.1, 1.2, 0.1));
   private final lo w = this.addSetting(new lo("Radius", "Distance from the target center, capped inside actual attack reach", 1.0, 1.0, 3.0, 0.1));
   private final li U = this.addSetting(
      new li("Direction", "Starting orbit direction; reverse when the path is blocked", "Clockwise", "Clockwise", "Counterclockwise")
   );
   private final l3 P = this.addSetting(new l3("Auto Jump", "Jump while orbiting when grounded with clear headroom", false));
   private final l3 t = this.addSetting(new l3("Avoid Edges", "Require ground below the orbit path; sneak pauses automatic movement", true));
   private class_746 R;
   private class_746 B;
   private class_638 v;
   private class_1309 n;
   private long p;
   private boolean Q;
   private int z = 1;
   private int i;
   private int y;
   private int O;
   private float[] V;
   private float a;
   private long G;
   private String e = "";
   private String r = "Waiting for Kill Aura";
   private class_10185 H;
   private class_10185 s;
   private class_241 N;
   private class_241 E;

   public TargetStrafeModule() {
      super("Target Strafe", "Automatically circle Kill Aura's target", Category.MOVEMENT, -1);
      T = this;
      this.g.O(() -> !this.B());
      this.b.O(this::B);
      this.w.O(this::B);
   }

   private boolean B() {
      return this.W.G("Fast");
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.r : "Inactive";
   }

   @Override
   public void onEnable() {
      this.V();
      this.i = 0;
      this.m("Waiting for Kill Aura");
      q8.v(
         "TargetStrafe",
         "Enabled: mode={}, distance={}, fast speed={} blocks/tick, fast radius={}, auto jump={}, edge checks={}",
         this.W.getValue(),
         this.g.getValue(),
         this.b.getValue(),
         this.w.getValue(),
         this.P.getValue(),
         this.t.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.V();
      this.i = 0;
      this.m("Inactive");
   }

   private void V() {
      this.x();
      this.R = null;
      this.v = null;
      this.n = null;
      this.Q = (boolean)0;
      this.e = "";
      this.V = null;
      this.y = this.O = 0;
   }

   private void m(String var1) {
      if (!this.r.equals(var1)) {
         this.B(var1);
      }

      this.r = var1;
   }

   private void B(String var1) {
      long var2 = System.nanoTime();
      if (var2 - this.G >= 1000000000L) {
         q8.v("TargetStrafe", "{}", var1);
         this.G = var2;
      }
   }

   @Override
   public void onTick() {
      this.x();
      this.Q = (boolean)0;
      boolean var3 = false;
      int var2 = this.i;
      if ((-Integer.signum(var2) >>> 31 ^ 1) == 0) {
         byte var5 = 1;
         int var4 = this.i;
         this.i = var4 - var5;
         this.V();
         this.m("Recovering after position correction");
      } else {
         class_1309 var1 = this.u();
         if (var1 == null) {
            this.V();
         } else {
            label28: {
               if (var1 == this.n) {
                  int var10000 = this.U.getValue().equals(this.e);
                  boolean var7 = false;
                  byte var6 = (byte)var10000;
                  var10000 = Integer.signum(var6);
                  if ((var10000 | -var10000) >>> 31 != 0) {
                     break label28;
                  }
               }

               int var10001 = this.U.G("Clockwise");
               boolean var9 = false;
               byte var8 = (byte)var10001;
               var10001 = Integer.signum(var8);
               this.z = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? 1 : -1;
               this.e = this.U.getValue();
               this.V = null;
               this.O = 0;
               this.B("Orbit target acquired: entity=" + var1.method_5628());
            }

            this.R = mc.field_1724;
            this.v = mc.field_1687;
            this.p = mc.field_1687.method_75260();
            this.n = var1;
         }
      }
   }

   private class_1309 u() {
      if (this.isInGame() && com.larpclient.j.k() && mc.method_1569() && mc.field_1755 == null && !mc.method_1493()) {
         class_746 var1 = mc.field_1724;
         FlyModule var2 = FlyModule.W();
         AirStuckModule var3 = AirStuckModule.U();
         ElytraMotionModule var4 = ElytraMotionModule.N();
         if ((var2 == null || !var2.P())
            && (var3 == null || !var3.E())
            && (var4 == null || !var4.k())
            && sq.U() == null
            && wp.o() != wp.z.v
            && wp.o() != wp.z.r
            && wp.o() != wp.z.J) {
            if (var1.method_5765()
               || var1.method_7325()
               || var1.method_31549().field_7479
               || var1.method_6128()
               || var1.method_5799()
               || var1.method_5771()
               || var1.method_6101()
               || var1.method_6113()
               || var1.method_74025()
               || var1.field_5960
               || var1.method_6115()
               || var1.method_18276()
               || var1.method_5715()
               || mc.field_1690.field_1832.method_1434()) {
               this.m("Paused for player movement or item use");
               return null;
            } else if (this.B() && var1.field_6235 > 0) {
               this.m("Paused for knockback");
               return null;
            } else {
               AuraModule var5 = AuraModule.R();
               class_1309 var6 = var5 == null ? null : var5.B();
               if (var6 == null) {
                  this.m("Waiting for Kill Aura target");
                  return null;
               } else if (l(var1.method_33571(), var6.method_5829(), this.A(), var5.P()) <= 0.0) {
                  this.m("Target is outside vertical attack reach");
                  return null;
               } else {
                  return var6;
               }
            }
         } else {
            this.m("Paused for another movement controller");
            return null;
         }
      } else {
         this.m("Waiting for gameplay");
         return null;
      }
   }

   private boolean Z(class_746 var1) {
      return this.isRuntimeActive()
         && this.i == 0
         && var1 != null
         && var1 == this.R
         && var1 == getMc().field_1724
         && this.v == mc.field_1687
         && this.p == mc.field_1687.method_75260()
         && this.n != null
         && this.u() == this.n;
   }

   private double A() {
      return this.B() ? this.w.getValue() : this.g.getValue();
   }

   public static boolean Y() {
      if (T != null && T.isEnabled()) {
         TargetStrafeModule var0 = T;
         if (var0 == null) {
            return false;
         }

         try {
            return var0.Z(getMc().field_1724);
         } catch (RuntimeException var2) {
            var0.g("movement ownership", var2);
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean P() {
      if (T != null && T.isEnabled()) {
         TargetStrafeModule var0 = T;
         return var0 != null && var0.B() && Y();
      } else {
         return false;
      }
   }

   public static class_10185 P(class_746 var0, class_10185 var1) {
      if (T != null && T.isEnabled()) {
         TargetStrafeModule var2 = T;
         if (var2 == null) {
            return var1;
         }

         try {
            if (!var2.Z(var0)) {
               return var1;
            }

            if (var1.comp_3164()) {
               var2.V();
               var2.m("Paused for sneak input");
               return var1;
            }

            if (!var2.B()) {
               return var2.d(var0, var1);
            }

            class_243 var3 = var2.a();
            class_241 var4 = K(var3, var0.method_36454());
            return var2.A(
               var0,
               var1,
               var4,
               new class_10185(
                  var4.field_1342 > 0.1F,
                  var4.field_1342 < -0.1F,
                  var4.field_1343 > 0.1F,
                  var4.field_1343 < -0.1F,
                  var1.comp_3163() || var2.G(var0, var3.method_1027() > 0.0),
                  var1.comp_3164(),
                  var1.comp_3165()
               )
            );
         } catch (RuntimeException var5) {
            var2.g("input", var5);
            return var1;
         }
      } else {
         return var1;
      }
   }

   private class_10185 d(class_746 var1, class_10185 var2) {
      float[] var3 = this.r();
      int var4 = var3[0] > 0.0F ? 1 : 0;
      int var5 = var3[0] == 0.0F && var3[1] == 0.0F ? 0 : 1;
      return this.A(
         var1,
         var2,
         new class_241(var3[1], var3[0]).method_35581(),
         new class_10185(
            (boolean)var4,
            var3[0] < 0.0F,
            var3[1] > 0.0F,
            var3[1] < 0.0F,
            var2.comp_3163() || this.G(var1, (boolean)var5),
            var2.comp_3164(),
            var4 != 0 && !var1.field_5976
         )
      );
   }

   private boolean G(class_746 var1, boolean var2) {
      return var2
         && this.P.getValue()
         && var1.method_24828()
         && var1.method_18798().field_1351 <= 0.0
         && !var1.method_31549().field_7478
         && mc.field_1687.method_8587(var1, var1.method_5829().method_1012(0.0, 1.3, 0.0));
   }

   private class_10185 A(class_746 var1, class_10185 var2, class_241 var3, class_10185 var4) {
      this.B = var1;
      this.H = var2;
      this.N = var1.field_3913.field_55868;
      this.E = var3;
      this.s = var4;
      var1.field_3913.field_55868 = var3;
      return var4;
   }

   public static boolean b() {
      if (T != null && T.isEnabled()) {
         TargetStrafeModule var0 = T;
         return var0 != null
            && var0.B != null
            && var0.B == getMc().field_1724
            && var0.v == mc.field_1687
            && var0.p == mc.field_1687.method_75260()
            && var0.B.field_3913.field_55868 == var0.E;
      } else {
         return false;
      }
   }

   private void x() {
      if (this.B != null && this.B == getMc().field_1724 && this.v == mc.field_1687 && this.p == mc.field_1687.method_75260()) {
         if (this.B.field_3913.field_54155 == this.s) {
            this.B.field_3913.field_54155 = this.H;
         }

         if (this.B.field_3913.field_55868 == this.E) {
            this.B.field_3913.field_55868 = this.N;
         }
      }

      this.B = null;
      this.H = this.s = null;
      this.N = this.E = null;
   }

   public static class_243 N(class_746 var0, class_243 var1) {
      if (T != null && T.isEnabled()) {
         TargetStrafeModule var2 = T;
         if (var2 != null && !var2.Q) {
            try {
               if (!var2.Z(var0)) {
                  var2.x();
                  return var1;
               }

               var2.Q = (boolean)1;
               if (!var2.B()) {
                  return var1;
               }

               class_243 var3 = var2.a();
               class_243 var4 = new class_243(var3.field_1352, var1.field_1351, var3.field_1350);
               var0.method_18799(var4);
               return var4;
            } catch (RuntimeException var5) {
               var2.g("movement", var5);
               return var1;
            }
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void g(String var1, RuntimeException var2) {
      q8.u("TargetStrafe", "Failed during " + var1 + "; disabling and releasing orbit input", var2);
      this.disable();
   }

   public static void r(class_746 var0) {
      TargetStrafeModule var1 = T;
      if (var1 != null && var1.isRuntimeActive() && var0 == getMc().field_1724) {
         if (var1.i == 0) {
            q8.G("TargetStrafe", "Position correction while active: mode=" + var1.W.getValue() + ", status=" + var1.r + "; pausing for 20 ticks");
         }

         var1.V();
         var1.i = 20;
         var1.m("Recovering after position correction");
      }
   }

   private float[] r() {
      class_746 var1 = mc.field_1724;
      float var2 = var1.method_36454();
      if (this.O > 0) {
         this.O--;
      }

      double var3 = l(var1.method_33571(), this.n.method_5829(), this.g.getValue(), AuraModule.R().P());
      if (var3 <= 0.0) {
         this.V = null;
         return d;
      }

      if (this.V != null && this.y-- > 0 && Math.abs(class_3532.method_15393(var2 - this.a)) <= 25.0F && this.o(this.V, var2)) {
         return this.V;
      }

      class_243 var5 = this.n.method_5829().method_1005();
      float[] var6 = I(var1.method_73189(), var5, var3, this.z, var2);
      if (!this.o(var6, var2)) {
         float[] var7 = this.O == 0 ? I(var1.method_73189(), var5, var3, -this.z, var2) : null;
         if (var7 == null || !this.o(var7, var2)) {
            this.V = null;
            this.m("Orbit blocked by terrain or edge");
            return d;
         }

         this.z = -this.z;
         this.O = 10;
         var6 = var7;
         this.B("Reversed orbit at blocked path or edge");
      }

      if (!Arrays.equals(var6, this.V)) {
         this.B(
            String.format(
               Locale.ROOT,
               "Keys forward=%.0f strafe=%.0f distance=%.2f ring=%.2f",
               var6[0],
               var6[1],
               var1.method_73189().method_1020(var5).method_37267(),
               var3
            )
         );
      }

      this.V = var6;
      this.y = 2;
      this.a = var2;
      this.m("Orbiting");
      return var6;
   }

   private boolean o(float[] var1, float var2) {
      class_243 var3 = GrimSpeedModule.j(var1[1], var1[0], var2);
      return this.p(var3.method_1021(mc.field_1724.method_24828() ? 0.7 : 1.0));
   }

   private class_243 a() {
      class_746 var1 = mc.field_1724;
      double var2 = l(var1.method_33571(), this.n.method_5829(), this.w.getValue(), AuraModule.R().P());
      if (var2 <= 0.0) {
         return class_243.field_1353;
      } else {
         class_243 var4 = this.n.method_5829().method_1005();
         class_243 var5 = M(var1.method_73189(), var4, var2, this.b.getValue(), this.z, var1.method_36454());
         if (this.p(var5)) {
            this.m("Orbiting");
            return var5;
         } else {
            class_243 var6 = M(var1.method_73189(), var4, var2, this.b.getValue(), -this.z, var1.method_36454());
            if (this.p(var6)) {
               this.z = -this.z;
               this.B("Reversed orbit at blocked path or edge");
               this.m("Orbiting");
               return var6;
            } else {
               this.m("Orbit blocked by terrain or edge");
               return class_243.field_1353;
            }
         }
      }
   }

   private boolean p(class_243 var1) {
      class_746 var2 = mc.field_1724;
      class_238 var3 = var2.method_5829().method_1011(0.001);
      if (!mc.field_1687.method_8587(var2, var3.method_1012(var1.field_1352, Math.max(0.0, var2.method_18798().field_1351), var1.field_1350))) {
         return false;
      }

      int var4 = Math.max(1, (int)Math.ceil(var1.method_37267() / 0.25));
      double var5 = var2.method_24828() ? 0.6 : 2.0;

      for (int var7 = 1; var7 <= var4; var7++) {
         double var8 = var2.method_23317() + var1.field_1352 * var7 / var4;
         double var10 = var2.method_23321() + var1.field_1350 * var7 / var4;
         if (!mc.field_1687.method_22340(class_2338.method_49637(var8, var2.method_23318(), var10))) {
            return false;
         }

         if (this.t.getValue()
            && mc.field_1687.method_52569(var2, new class_238(var8 - 0.1, var3.field_1322 - var5, var10 - 0.1, var8 + 0.1, var3.field_1322, var10 + 0.1))) {
            return false;
         }
      }

      return true;
   }

   public static float[] I(class_243 var0, class_243 var1, double var2, int var4, float var5) {
      return Double.isFinite(var2)
            && !(var2 <= 0.0)
            && Float.isFinite(var5)
            && Double.isFinite(var0.field_1352)
            && Double.isFinite(var0.field_1350)
            && Double.isFinite(var1.field_1352)
            && Double.isFinite(var1.field_1350)
         ? AuraModule.V(P(var0, var1, var2, var4, var5), var5)
         : d;
   }

   public static double P(class_243 var0, class_243 var1, double var2, int var4, float var5) {
      double var6 = var0.field_1352 - var1.field_1352;
      double var8 = var0.field_1350 - var1.field_1350;
      double var10 = Math.hypot(var6, var8);
      if (var10 < 1.0E-4) {
         return var5;
      }

      double var12 = var6 / var10;
      double var14 = var8 / var10;
      double var16 = var4 >= 0 ? 1.0 : -1.0;
      double var18 = Math.clamp((var10 - var2) * (Math.PI / 3), -Math.PI / 2, Math.PI / 2);
      double var20 = -var14 * var16 * Math.cos(var18) - var12 * Math.sin(var18);
      double var22 = var12 * var16 * Math.cos(var18) - var14 * Math.sin(var18);
      return class_3532.method_15338(Math.toDegrees(Math.atan2(var22, var20)) - 90.0);
   }

   public static class_243 M(class_243 var0, class_243 var1, double var2, double var4, int var6, float var7) {
      if (Double.isFinite(var2)
         && Double.isFinite(var4)
         && !(var2 <= 0.0)
         && !(var4 <= 0.0)
         && Double.isFinite(var0.field_1352)
         && Double.isFinite(var0.field_1350)
         && Double.isFinite(var1.field_1352)
         && Double.isFinite(var1.field_1350)
         && Float.isFinite(var7)) {
         double var8 = var0.field_1352 - var1.field_1352;
         double var10 = var0.field_1350 - var1.field_1350;
         double var12 = var8 * var8 + var10 * var10 < 1.0E-8 ? Math.toRadians(var7) : Math.atan2(var10, var8);
         var12 += (var6 >= 0 ? 1 : -1) * 2 * Math.asin(Math.min(1.0, var4 / (2.0 * var2)));
         class_243 var14 = new class_243(
            var1.field_1352 + Math.cos(var12) * var2 - var0.field_1352, 0.0, var1.field_1350 + Math.sin(var12) * var2 - var0.field_1350
         );
         double var15 = var14.method_1033();
         return var15 > var4 ? var14.method_1021(var4 / var15) : var14;
      } else {
         return class_243.field_1353;
      }
   }

   public static double l(class_243 var0, class_238 var1, double var2, double var4) {
      double var6 = Math.max(var1.field_1322 - var0.field_1351, Math.max(0.0, var0.field_1351 - var1.field_1325));
      double var8 = Math.max(0.0, var4 - 0.25);
      double var10 = var8 * var8 - var6 * var6;
      return Double.isFinite(var10) && !(var10 <= 0.0)
         ? Math.max(0.0, Math.min(var2, Math.sqrt(var10) + Math.min(var1.method_17939(), var1.method_17941()) / 2.0))
         : 0.0;
   }

   public static class_241 K(class_243 var0, float var1) {
      if (var0.method_37268() < 1.0E-8) {
         return class_241.field_1340;
      }

      class_243 var2 = var0.method_1029();
      double var3 = Math.toRadians(var1);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new class_241((float)(var5 * var2.field_1352 + var7 * var2.field_1350), (float)(-var7 * var2.field_1352 + var5 * var2.field_1350));
   }
}
