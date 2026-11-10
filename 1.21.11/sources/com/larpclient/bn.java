package com.larpclient;

import com.larp.obf.q4031f93fe72647cb.q219e0ba2e314b7b5;
import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larp.obf.q4031f93fe72647cb.qf65e76ba6afee893;
import com.larpclient.module.MCFriendModule;
import com.larpclient.module.MaceModule;
import net.minecraft.class_10185;
import net.minecraft.class_10255;
import net.minecraft.class_10590;
import net.minecraft.class_10707;
import net.minecraft.class_1282;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import net.minecraft.class_5134;
import net.minecraft.class_640;
import net.minecraft.class_746;
import net.minecraft.class_9334;
import net.minecraft.class_1322.class_1323;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class bn {
   private static class_1657 k;
   private static Object b;
   private static int H = -1;
   private static class_1309 v;
   private static int T;
   private static final double d = Math.toRadians(10.0);

   public static void b(class_1657 var0) {
      class_310 var1 = class_310.method_1551();
      if (var0 == var1.field_1724) {
         k = var0;
         b = var1.field_1687;
         H = var0.field_6012;
      }
   }

   public static boolean w(class_746 var0) {
      return var0 != null && k == var0 && b == var0.method_73183() && H == var0.field_6012;
   }

   public static void L() {
      k = null;
      b = null;
      H = -1;
      v = null;
   }

   public static boolean q(class_310 var0) {
      return x(var0, wp.z.D);
   }

   public static boolean x(class_310 var0, wp.z var1) {
      wp.z var2 = wp.o();
      return cr.f()
         && var0.method_18506() == null
         && var0.method_1569()
         && var0.method_1560() == var0.field_1724
         && (var2 == wp.z.D || var2 == var1 || var2 == wp.z.u && var1 != wp.z.D);
   }

   public static boolean R(class_310 var0, class_746 var1) {
      if (!var1.method_6115() && !var1.method_3144() && !var0.field_1761.method_2923() && !M(var0.field_1690.field_1831) && !M(var0.field_1690.field_1869)) {
         class_10185 var2 = var1.method_71091();
         return !(var1.method_5854() instanceof class_10255) || var2.comp_3159() == var2.comp_3160() && var2.comp_3161() == var2.comp_3162();
      } else {
         return false;
      }
   }

   public static boolean M(class_304 var0) {
      return ((q219e0ba2e314b7b5)var0).larpClickCount() > 0;
   }

   public static boolean x(class_310 var0) {
      return M(var0.field_1690.field_1886);
   }

   public static boolean l(class_310 var0) {
      return var0.field_1690.field_1904.method_1434() || M(var0.field_1690.field_1904);
   }

   public static class_10185 I(class_10185 var0) {
      return !var0.comp_3165()
         ? var0
         : new class_10185(var0.comp_3159(), var0.comp_3160(), var0.comp_3161(), var0.comp_3162(), var0.comp_3163(), var0.comp_3164(), false);
   }

   public static int v(class_310 var0) {
      return ((qf65e76ba6afee893)var0).larpAttackDelay();
   }

   public static void i(class_310 var0, int var1) {
      ((qf65e76ba6afee893)var0).larpAttackDelay(var1);
   }

   public static void E(class_310 var0) {
      while (var0.field_1690.field_1886.method_1436()) {
      }

      while (var0.field_1690.field_1904.method_1436()) {
      }

      qf65e76ba6afee893 var1 = (qf65e76ba6afee893)var0;
      var1.larpAttackDelay(Math.max(var1.larpAttackDelay(), 1));
      var1.larpUseDelay(Math.max(var1.larpUseDelay(), 2));
   }

   public static boolean K(class_746 var0, class_1297 var1) {
      if (var1 instanceof class_1309 var2
         && var1 != var0
         && !(var1 instanceof class_1531)
         && var1 != var0.method_5854()
         && var2.method_5805()
         && var1.method_5732()
         && !var1.method_31481()
         && var1.method_73183() == var0.method_73183()) {
         if (var1 instanceof class_1657 var3) {
            if (var3.method_7325() || var3.method_68878() || !var0.method_7256(var3) || var0.field_3944.method_2871(var3.method_5667()) == null) {
               return false;
            }

            LarpClient var4 = LarpClient.b();
            MCFriendModule var5 = var4 != null && var4.G() != null ? var4.G().b(MCFriendModule.class) : null;
            if (var5 != null && var5.M(var3.method_5477().getString())) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean I(class_746 var0, class_243 var1, class_243 var2) {
      class_3965 var3 = var0.method_73183().method_17742(new class_3959(var1, var2, class_3960.field_17558, class_242.field_1348, var0));
      return var3.method_17783() == class_240.field_1333 || var1.method_1025(var3.method_17784()) >= var1.method_1025(var2) - 0.001;
   }

   public static class_3966 U(class_310 var0) {
      if (var0.field_1724 != null && var0.field_1687 != null) {
         class_239 var1 = var0.field_1724.method_76762(1.0F, var0.field_1724);
         return var1 instanceof class_3966 var2 && I(var0.field_1724, var0.field_1724.method_33571(), var1.method_17784()) ? var2 : null;
      } else {
         return null;
      }
   }

   public static float M(class_746 var0) {
      return var0.method_7261(0.5F);
   }

   public static double i(class_1799 var0) {
      double[] var1 = new double[]{0.0};
      var0.method_57354(class_1304.field_6173, (var1x, var2) -> {
         if (var1x.equals(class_5134.field_23721) && var2.comp_2450() == class_1323.field_6328) {
            var1[0] += var2.comp_2449();
         }
      });
      return var1[0];
   }

   public static boolean A(class_746 var0, class_1309 var1) {
      return M(var0) > 0.9F
         && var0.field_6017 > 0.0
         && !var0.method_24828()
         && !var0.method_6101()
         && !var0.method_5799()
         && !var0.method_74025()
         && !var0.method_5765()
         && !var0.method_5624();
   }

   public static boolean B(class_1309 var0, class_746 var1) {
      if (!var0.method_6115()) {
         return false;
      }

      class_1799 var2 = var0.method_6030();
      class_10707 var3 = (class_10707)var2.method_58694(class_9334.field_56396);
      if (var3 != null && var0.method_6048() + Z(var1) >= var3.method_67197()) {
         class_1282 var4 = var1.method_48923().method_48802(var1);
         if (var3.comp_3637().map(var1x -> var4.method_48789(var1x)).orElse(false)) {
            return false;
         }

         class_243 var5 = var1.method_73189().method_1020(var0.method_73189()).method_18805(1.0, 0.0, 1.0).method_1029();
         double var6 = Math.acos(Math.clamp(var5.method_1026(class_243.method_1030(0.0F, var0.method_5791())), -1.0, 1.0));
         return var3.method_67202(var4, 1.0F, Math.max(0.0, var6 - d)) > 0.0F;
      } else {
         return false;
      }
   }

   public static int Z(class_746 var0) {
      class_640 var1 = var0.field_3944.method_2871(var0.method_5667());
      return var1 == null ? 1 : Math.max(0, (var1.method_2959() + 49) / 50) + 1;
   }

   public static void f(class_1309 var0) {
      class_746 var1 = class_310.method_1551().field_1724;
      v = var0;
      T = var0.field_6012 + Math.clamp(var1 == null ? 3L : Z(var1) + 2, 3, 20);
   }

   public static boolean G(class_1309 var0) {
      return var0 == v && var0.field_6012 < T;
   }

   public static int d(class_746 var0) {
      int var1 = -1;
      double var2 = Double.NEGATIVE_INFINITY;

      for (int var4 = 0; var4 < class_1661.method_7368(); var4++) {
         class_1799 var5 = var0.method_31548().method_5438(var4);
         class_10590 var6 = (class_10590)var5.method_58694(class_9334.field_55878);
         if (!var5.method_63692() && var6 != null && !(var6.comp_3602() <= 0.0F)) {
            double var7 = i(var5);
            if (var7 > var2) {
               var1 = var4;
               var2 = var7;
            }
         }
      }

      return var1;
   }

   public static boolean u(class_310 var0, class_3966 var1) {
      class_746 var2 = var0.field_1724;
      if (var2 != null
         && v(var0) <= 0
         && !w(var2)
         && R(var0, var2)
         && !var2.method_7325()
         && var2.method_5624() == ((q586814c6d8f3640b)var2).larpWasSprinting()) {
         class_1799 var3 = var2.method_6047();
         return !var3.method_57826(class_9334.field_63631) && var3.method_45435(var0.field_1687.method_45162()) && !var2.method_75202(var3, 0)
            ? cr.Z(MaceModule.c(), var1.method_17782(), var2.method_55755())
            : false;
      } else {
         return false;
      }
   }

   private bn() {
   }
}
