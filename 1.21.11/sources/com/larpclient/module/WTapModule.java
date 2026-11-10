package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cr;
import com.larpclient.g9;
import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.sq;
import com.larpclient.wg;
import com.larpclient.wp;
import java.util.Set;
import net.minecraft.class_10185;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_3489;
import net.minecraft.class_3966;
import net.minecraft.class_746;

public final class WTapModule extends Module {
   private static WTapModule t;
   private final l3 L = this.addSetting(new l3("Players Only", "Only release forward after attacking a player", true));
   private final l3 X = this.addSetting(new l3("Weapons Only", "Require a held melee weapon", true));
   private final lo U = this.addSetting(new lo("Release Duration", "Forward release in native input ticks", 1.0, 1.0, 3.0, 1.0));
   private final wg.h M = new wg.h();
   private final wg.q F = new wg.q();
   private class_746 v;
   private Object a;
   private String q = "Waiting for a sprint attack";

   public WTapModule() {
      super("W Tap", "Briefly release forward after a charged sprint attack", Category.COMBAT, -1);
      t = this;
   }

   public static void w(class_1657 var0, class_1297 var1) {
      if (t != null && t.isEnabled()) {
         WTapModule var2 = t;
         if (var2 != null && var0 == getMc().field_1724) {
            var2.F.Z();
            if (var2.isRuntimeActive() && var0 == getMc().field_1724 && var0 instanceof class_746 var3) {
               int var4 = !cr.C() && mc.field_1690.field_1886.method_1434() && mc.field_1765 instanceof class_3966 var5 && var5.method_17782() == var1 ? 1 : 0;
               boolean var7 = var2.l(var1);
               boolean var8 = wg.n(
                  (boolean)var4,
                  var2.G(var3),
                  var3.method_5624(),
                  mc.field_1690.field_1894.method_1434(),
                  mc.field_1690.field_1881.method_1434(),
                  mc.field_1690.field_1832.method_1434(),
                  var3.method_7261(0.5F),
                  var7
               );
               var2.F.X(var0, var1, var8);
               if (var8) {
                  var2.v = var3;
                  var2.a = mc.field_1687;
               }
            }
         }
      }
   }

   public static void x(class_1657 var0, class_1297 var1) {
      if (t != null && t.isEnabled()) {
         WTapModule var2 = t;
         if (var2 != null) {
            var2.F.D(var0, var1);
         }
      }
   }

   public static void K(class_1657 var0, class_1297 var1) {
      if (t != null && t.isEnabled()) {
         WTapModule var2 = t;
         if (var2 != null && var0 == getMc().field_1724) {
            if (var2.F.e(var0, var1) && var2.isRuntimeActive() && var0 == var2.v && var2.G(var2.v)) {
               var2.M.O(var2.U.getValue().intValue());
               var2.q = "Forward release queued";
               q8.v("WTap", "Executed manual sprint attack; release queued for {} input ticks", var2.U.getValue().intValue());
            }
         }
      }
   }

   public static class_10185 B(class_746 var0, class_10185 var1) {
      if (t != null && t.isEnabled()) {
         WTapModule var2 = t;
         if (var2 == null || !var2.isRuntimeActive()) {
            return var1;
         } else if (var2.v != var0 || var2.a != getMc().field_1687 || !var2.G(var0) || !var1.comp_3159() || var1.comp_3160() || var1.comp_3164()) {
            var2.r("Input or movement context changed");
            return var1;
         } else if (!var2.M.O(var0.field_6012, true)) {
            var2.q = "Waiting for a sprint attack";
            return var1;
         } else {
            var2.q = "Releasing forward";
            g9.B();
            var0.method_5728(false);
            class_10185 var3 = wg.g(var1);
            var0.field_3913.field_55868 = wg.F(var3);
            return var3;
         }
      } else {
         return var1;
      }
   }

   public static boolean C() {
      if (t != null && t.isEnabled()) {
         WTapModule var0 = t;
         class_310 var1 = getMc();
         if (var0 == null || !var0.isRuntimeActive() || var1.field_1724 == null) {
            return false;
         }

         if (var0.v == var1.field_1724 && var0.a == var1.field_1687 && var0.G(var1.field_1724)) {
            return var0.M.k(var1.field_1724.field_6012);
         }

         var0.r("Sprint ownership changed");
         return false;
      } else {
         return false;
      }
   }

   private boolean G(class_746 var1) {
      if (var1 != null
         && var1 == getMc().field_1724
         && j.k()
         && cr.u()
         && mc.method_1569()
         && wp.o() == wp.z.D
         && !var1.method_6115()
         && !var1.method_5799()
         && !var1.method_5771()
         && !var1.method_6101()
         && !var1.method_5765()
         && !var1.method_31549().field_7479
         && !var1.method_6128()
         && !var1.method_6113()
         && !var1.method_74025()
         && !var1.field_5960
         && !var1.method_18276()
         && !var1.method_5715()
         && sq.U() == null) {
         AirStuckModule var2 = AirStuckModule.U();
         ElytraMotionModule var3 = ElytraMotionModule.N();
         FlyModule var4 = FlyModule.W();
         return (var2 == null || !var2.E()) && (var3 == null || !var3.k()) && (var4 == null || !var4.P()) && !AuraModule.N();
      } else {
         return false;
      }
   }

   private boolean l(class_1297 var1) {
      if (!(
         var1 instanceof class_1309 var2
            && var1 != mc.field_1724
            && var2.method_5805()
            && !var1.method_31481()
            && !var1.method_7325()
            && var1.method_73183() == mc.field_1687
            && !var1.method_5767()
            && !mc.field_1724.method_5722(var1)
            && (!this.L.getValue() || var1 instanceof class_1657)
            && mc.field_1724.method_6057(var1)
            && !(cr.M(var1) > mc.field_1724.method_55755() + 1.0E-7)
      )) {
         return false;
      } else {
         if (this.X.getValue() && !t(mc.field_1724)) {
            return false;
         }

         LarpClient var3 = LarpClient.b();
         MCFriendModule var4 = var3 != null && var3.G() != null ? var3.G().b(MCFriendModule.class) : null;
         return !(var1 instanceof class_1657) || var4 == null || !var4.M(var1.method_5477().getString());
      }
   }

   public static boolean t(class_1657 var0) {
      class_1799 var1 = var0.method_6047();
      return !var1.method_7960()
         && (var1.method_31573(class_3489.field_42611) || var1.method_31573(class_3489.field_42612) || var1.method_31573(class_3489.field_63258));
   }

   private void r(String var1) {
      if (this.M.c()) {
         q8.v("WTap", "Release cancelled: {}; native input resumes", var1);
      }

      this.M.I();
      this.F.Z();
      this.q = "Waiting for a sprint attack";
   }

   public static void i(class_746 var0) {
      WTapModule var1 = t;
      if (var1 != null && var1.v == var0) {
         var1.r("Position correction");
      }
   }

   @Override
   public void onEnable() {
      this.r("Activated");
   }

   @Override
   public void onDisable() {
      this.r("Disabled or disconnected");
      this.v = null;
      this.a = null;
   }

   public static void K() {
      WTapModule var0 = t;
      if (var0 != null) {
         if (!var0.isRuntimeActive()) {
            var0.r("Runtime unavailable");
         } else {
            if (var0.v != null && (var0.v != getMc().field_1724 || var0.a != mc.field_1687 || !var0.G(mc.field_1724))) {
               var0.r("Context unavailable");
            }
         }
      }
   }

   @Override
   public void onTick() {
      K();
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }

   @Override
   public String getStatus() {
      return this.q;
   }
}
