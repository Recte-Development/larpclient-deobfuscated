package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.iw;
import com.larpclient.q8;
import com.larpclient.t9;
import com.larpclient.w4;
import com.larpclient.wp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3489;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class AimAssistModule extends Module {
   private static AimAssistModule J;
   private final lo z = this.addSetting(new lo("Anubis Range", "Maximum eye-to-hitbox acquisition distance", 4.0, 3.0, 6.0, 0.1) {
      @Override
      public String getDisplayName() {
         return "Range";
      }
   });
   private final lo T = this.addSetting(new lo("Anubis FOV", "Total acquisition width around the crosshair", 60.0, 10.0, 180.0, 5.0) {
      @Override
      public String getDisplayName() {
         return "FOV";
      }
   });
   private final lo Q = this.addSetting(
      new lo("Anubis Speed", "Maximum correction in degrees per second, varied slightly for each target", 60.0, 10.0, 180.0, 5.0) {
         @Override
         public String getDisplayName() {
            return "Speed";
         }
      }
   );
   private final l3 k = this.addSetting(new l3("Anubis Weapons Only", "Require a sword, axe, spear, mace or trident", true) {
      @Override
      public String getDisplayName() {
         return "Weapons Only";
      }
   });
   private final l3 V = this.addSetting(new l3("Anubis Vertical", "Also ease pitch toward the target's core", true) {
      @Override
      public String getDisplayName() {
         return "Vertical";
      }
   });
   private final iw.c q = new iw.c();
   private class_1657 Z;
   private class_1657 Y;
   private long v;
   private long R;
   private double o = 1.0;
   private class_746 X;
   private Object h;
   private String g = "Waiting for input";

   public AimAssistModule() {
      super("Aim Assist", "Smooth visible aim correction while you actively aim", Category.COMBAT, -1);
      J = this;

      for (lb var2 : this.getSettings()) {
         var2.a(var1 -> this.y("Settings changed"));
      }

      this.z();
   }

   private void z() {
      for (lb var2 : List.of(
         new l3("Require Attack", "Retired profile value", true),
         new l3("Weapons Only", "Retired profile value", true),
         new l3("Players Only", "Retired profile value", true),
         new l3("Vertical Assistance", "Retired profile value", false),
         new lo("Range", "Retired profile value", 3.0, 0.5, 6.0, 0.25),
         new lo("Aim Cone", "Retired profile value", 60.0, 10.0, 180.0, 5.0),
         new lo("Correction Speed", "Retired profile value", 60.0, 5.0, 180.0, 5.0)
      )) {
         this.addSetting(var2).O(() -> false);
      }
   }

   private String t() {
      class_310 var1 = getMc();
      if (var1.field_1724 == null || var1.field_1687 == null || var1.field_1761 == null) {
         return "Waiting for world";
      }

      if (!var1.field_1724.method_5805() || var1.field_1724.method_7325() || var1.field_1724.method_6113()) {
         return "Paused: player unavailable";
      }

      if (var1.field_1755 != null || var1.method_18506() != null) {
         return "Paused: menu open";
      }

      if (var1.method_1569() && var1.field_1729.method_1613()) {
         if (var1.method_1560() != var1.field_1724 || wp.o() != wp.z.D || t9.I()) {
            return "Paused: rotation owned";
         } else if (var1.field_1724.method_6115() || var1.field_1690.field_1904.method_1434()) {
            return "Paused: using an item";
         } else {
            return this.k.getValue() && !F(var1.field_1724) ? "Waiting for a melee weapon" : null;
         }
      } else {
         return "Paused: mouse not captured";
      }
   }

   private static boolean F(class_1657 var0) {
      class_1799 var1 = var0.method_6047();
      return var1.method_31573(class_3489.field_42611)
         || var1.method_31573(class_3489.field_42612)
         || var1.method_31573(class_3489.field_63257)
         || var1.method_31574(class_1802.field_49814)
         || var1.method_31574(class_1802.field_8547);
   }

   public static void Z(class_1657 var0, class_1297 var1) {
      if (J != null && J.isEnabled()) {
         AimAssistModule var2 = J;
         class_310 var3 = getMc();
         if (var2 != null
            && var0 == var3.field_1724
            && var1 instanceof class_1657 var4
            && var4.method_5805()
            && !var4.method_31481()
            && var4.method_73183() == var3.field_1687) {
            var2.Y = var4;
            var2.v = System.nanoTime() / 1000000L;
         }
      }
   }

   public static iw.u Q(class_746 var0, double var1, double var3, double var5) {
      if (J != null && J.isEnabled()) {
         AimAssistModule var7 = J;
         if (var7 != null && var7.isRuntimeActive()) {
            try {
               return var7.t(var0, var1, var3, var5);
            } catch (RuntimeException var9) {
               var7.y("Paused: aim correction failed");
               q8.u("AimAssist", "Mouse correction failed; disabling and retaining native input", var9);
               var7.disable();
               return iw.u.STILL;
            }
         } else {
            return iw.u.STILL;
         }
      } else {
         return iw.u.STILL;
      }
   }

   private iw.u t(class_746 var1, double var2, double var4, double var6) {
      String var8 = this.t();
      if (var8 == null && var1 == mc.field_1724) {
         this.F(var1);
         long var9 = System.nanoTime() / 1000000L;
         int var11 = var2 == 0.0 && var4 == 0.0 && !this.q.T(var9) ? 0 : 1;
         if (var11 == 0) {
            this.q.u();
            this.P("Waiting for mouse movement");
            return iw.u.STILL;
         }

         float[] var12 = iw.Q(var1.method_36454(), var1.method_36455(), var2, var4);
         class_243 var13 = class_243.method_1030(var12[1], var12[0]);
         this.R(this.l(var1, var9, var12[0], var12[1], var13));
         float[] var14 = this.Z == null ? null : iw.x(var1.method_33571(), var13, iw.C(this.Z.method_5829()), this.z.getValue());
         double var15 = var14 == null ? 0.0 : w4.X(var14[0] - var12[0]);
         double var17 = var14 == null ? 0.0 : var14[1] - var12[1];
         double var19 = iw.M((Double)mc.field_1690.method_42495().method_41753(), mc.field_1690.method_31044().method_31034(), var1.method_31550());
         iw.u var21 = this.q.Q(var2, var4, var15, var17, var14 != null, this.V.getValue(), var6, this.Q.getValue(), this.o, var19, var9);
         if (this.Z == null) {
            this.P("No eligible target");
         } else if (var14 == null) {
            this.P("On target");
         } else if (this.q.r(var15, var17, this.V.getValue())) {
            this.P("Following your mouse direction");
         } else {
            this.P("Assisting " + this.Z.method_5477().getString());
         }

         return var21;
      } else {
         this.y(var8 == null ? "Waiting for input" : var8);
         return iw.u.STILL;
      }
   }

   private void F(class_746 var1) {
      if (this.X != var1 || this.h != mc.field_1687) {
         if (this.X != null || this.Y != null && this.Y.method_73183() != mc.field_1687) {
            this.Y = null;
         }

         this.y("Waiting for input");
         this.X = var1;
         this.h = mc.field_1687;
      }
   }

   private class_1657 l(class_746 var1, long var2, float var4, float var5, class_243 var6) {
      ArrayList var7 = new ArrayList(2);
      long var8 = var2 - this.v;
      if (var8 >= 0L && var8 <= 1500L) {
         iw.k var10 = this.e(this.Y, var4, var5, var6);
         if (var10 != null) {
            var7.add(var10);
         }
      }

      iw.k var16 = this.e(this.Z, var4, var5, var6);
      if (var16 != null) {
         var7.add(var16);
      }

      class_1657 var11 = iw.L(var7, this.Y, var8, this.Z, this.z.getValue(), this.T.getValue());
      if (var11 != null) {
         return var11;
      }

      ArrayList var12 = new ArrayList();

      for (class_1657 var14 : mc.field_1687.method_18467(class_1657.class, var1.method_5829().method_1014(this.z.getValue() + 0.5))) {
         iw.k var15 = this.e(var14, var4, var5, var6);
         if (var15 != null) {
            var12.add(var15);
         }
      }

      return iw.L(var12, this.Y, var2 - this.v, this.Z, this.z.getValue(), this.T.getValue());
   }

   private void R(class_1657 var1) {
      if (this.Z != var1) {
         this.Z = var1;
         this.o = ThreadLocalRandom.current().nextDouble(0.85, 1.15);
         this.q.V();
      }
   }

   private iw.k<class_1657> e(class_1657 var1, float var2, float var3, class_243 var4) {
      if (var1 != null
         && var1 != mc.field_1724
         && var1.method_5805()
         && !var1.method_31481()
         && var1.method_73183() == mc.field_1687
         && !var1.method_7325()
         && !var1.method_68878()
         && var1.method_5732()
         && !var1.method_5767()
         && mc.field_1724.method_5854() != var1
         && mc.field_1724.method_7256(var1)
         && mc.field_1724.field_3944.method_2871(var1.method_5667()) != null) {
         LarpClient var5 = LarpClient.b();
         MCFriendModule var6 = var5 != null && var5.G() != null ? var5.G().b(MCFriendModule.class) : null;
         if (var6 != null && var6.M(var1.method_5477().getString())) {
            return null;
         }

         class_243 var7 = mc.field_1724.method_33571();
         double var8 = var7.method_1022(iw.y(var7, var1.method_5829()));
         if (var8 > this.z.getValue() + 0.5) {
            return null;
         }

         class_238 var10 = iw.C(var1.method_5829());
         float[] var11 = iw.x(var7, var4, var10, this.z.getValue());
         double var12 = iw.b(var2, var3, var11);
         if (var12 > this.T.getValue() / 2.0 * 1.3) {
            return null;
         }

         class_243 var14 = iw.t(var7, var4, var10, this.z.getValue());
         class_3965 var15 = mc.field_1687.method_17742(new class_3959(var7, var14, class_3960.field_17558, class_242.field_1348, mc.field_1724));
         return var15.method_17783() != class_240.field_1333 && var15.method_17784().method_1025(var7) < var14.method_1025(var7) - 0.001
            ? null
            : new iw.k<>(var1, var8, var12);
      } else {
         return null;
      }
   }

   private void P(String var1) {
      if (!this.g.equals(var1)) {
         this.g = var1;
         long var2 = System.nanoTime() / 1000000L;
         if (var2 - this.R >= 1000L) {
            this.R = var2;
            q8.v("AimAssist", "{}", var1);
         }
      }
   }

   private void y(String var1) {
      this.q.u();
      this.Z = null;
      this.P(var1);
   }

   public static void n(class_746 var0) {
      AimAssistModule var1 = J;
      if (var1 != null && var1.X == var0) {
         var1.y("Waiting for input");
      }
   }

   public static void t() {
      AimAssistModule var0 = J;
      if (var0 != null) {
         class_310 var1 = getMc();
         if (var0.Y != null
            && (!var0.Y.method_5805() || var0.Y.method_31481() || var0.Y.method_73183() != var1.field_1687 || System.nanoTime() / 1000000L - var0.v > 30000L)) {
            var0.Y = null;
         }

         if (!var0.isRuntimeActive()) {
            var0.y("Waiting for input");
         } else {
            String var2 = var0.t();
            if (var2 != null || var0.X != var1.field_1724 || var0.h != var1.field_1687) {
               var0.y(var2 == null ? "Waiting for input" : var2);
            }
         }
      }
   }

   @Override
   public void onTick() {
      t();
   }

   @Override
   public void onEnable() {
      this.y("Waiting for input");
      q8.v(
         "AimAssist",
         "Aim assistance active; range={}, FOV={}, speed={}, weapons={}, vertical={}",
         this.z.getValue(),
         this.T.getValue(),
         this.Q.getValue(),
         this.k.getValue(),
         this.V.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.y("Waiting for input");
      this.X = null;
      this.h = null;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }

   @Override
   public String getStatus() {
      return this.g;
   }
}
