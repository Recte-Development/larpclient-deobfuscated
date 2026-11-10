package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q58db36eeaf095e51;
import com.larpclient.q8;
import com.larpclient.qb;
import com.larpclient.wp;
import java.util.Locale;
import net.minecraft.class_10042;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_481;
import net.minecraft.class_490;
import net.minecraft.class_5498;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class Feature3DUIModule extends Module {
   private static Feature3DUIModule m;
   private final li O = this.addSetting(new li("Screens", "Which menus open in 3D", "Inventory", "Inventory", "All Containers"));
   private final li k = this.addSetting(new li("Side", "Screen side for the menu; the camera moves over the opposite shoulder", "Right", "Right", "Left"));
   private final lo G = this.addSetting(new lo("Menu Angle", "Degrees the menu turns toward the center of the view", 14.0, 0.0, 45.0, 1.0));
   private final lo Y = this.addSetting(new lo("Menu Screen Height", "On-screen menu height, tabs included, as a percent of the screen", 76.0, 30.0, 90.0, 1.0));
   private final lo J = this.addSetting(new lo("Camera Distance", "Camera distance behind the player in blocks", 1.1, 0.6, 4.0, 0.1));
   private final lo K = this.addSetting(new lo("Camera Offset", "Sideways camera offset in blocks", 0.5, 0.0, 1.5, 0.05));
   private final lo g = this.addSetting(new lo("Camera Height", "Camera height relative to the torso in blocks", 0.1, -0.6, 0.6, 0.05));
   private final lo A = this.addSetting(new lo("Camera Pitch", "Camera pitch in degrees; positive looks down", 3.0, -15.0, 40.0, 1.0));
   private final lo X = this.addSetting(new lo("Transition", "Camera and menu transition in milliseconds", 320.0, 0.0, 800.0, 10.0));
   private final l3 L = this.addSetting(new l3("Dim Background", "Keep the vanilla darkened backdrop behind the menu", false));
   private final l3 t = this.addSetting(new l3("Show HUD", "Keep the hotbar and HUD visible while a 3D menu is open", true));
   private final li R = this.addSetting(
      new li(
         "Player Facing",
         "Where your player turns while a 3D menu is open: its own way, around to look at you, or by Player Turn. Only you see it",
         "Forward",
         "Forward",
         "Camera",
         "Custom"
      )
   );
   private final lo e = this.addSetting(new lo("Player Turn", "Degrees your player turns from its own facing; 180 faces the camera", 180.0, -180.0, 180.0, 5.0));
   private final qb u = new qb(0.0);
   private class_5498 o;
   private boolean d;
   private static final double z = 3.5;

   public Feature3DUIModule() {
      super("3D UI", "Third person shoulder view with your inventory in 3D beside you", Category.RENDER, -1);
      m = this;
      this.e.O(() -> this.R.G("Custom"));
   }

   public static Feature3DUIModule f() {
      return m;
   }

   private boolean F() {
      class_310 var1 = getMc();
      class_746 var2 = var1.field_1724;
      return this.isRuntimeActive()
         && var2 != null
         && var1.field_1687 != null
         && var2.method_5805()
         && !var2.method_6113()
         && !var2.method_7325()
         && !wp.d()
         && !FreelookModule.l();
   }

   public boolean d(class_437 var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return var1 instanceof class_465 && this.F() ? this.O.G("All Containers") || var1 instanceof class_490 || var1 instanceof class_481 : false;
      }
   }

   private double X() {
      boolean var1 = this.d(getMc().field_1755);
      double var2 = this.u.G(var1 ? 1.0 : 0.0, this.X.getValue().intValue(), qb.y.E);
      return Math.clamp(var2, 0.0, 1.0);
   }

   public static void h() {
      Feature3DUIModule var0 = m;
      if (var0 != null) {
         var0.a();
      }
   }

   private void a() {
      if (!this.F()) {
         this.j("view unavailable");
         this.u.w(0.0);
      } else {
         double var1 = this.X();
         if (var1 <= 0.001 && !this.d(getMc().field_1755)) {
            this.j("closed");
         } else {
            class_315 var3 = getMc().field_1690;
            if (this.o == null) {
               this.o = var3.method_31044();
               q8.v("Camera", "3D UI view engaged from {}", this.o);
            } else if (var3.method_31044() != class_5498.field_26665) {
               q8.v("Camera", "3D UI view yielded to perspective {}", var3.method_31044());
               this.o = null;
               this.u.w(0.0);
               return;
            }

            if (var3.method_31044() != class_5498.field_26665) {
               var3.method_31043(class_5498.field_26665);
            }
         }
      }
   }

   private void j(String var1) {
      if (this.o != null) {
         class_315 var2 = getMc().field_1690;
         if (var2.method_31044() == class_5498.field_26665) {
            var2.method_31043(this.o);
         }

         q8.v("Camera", "3D UI view released ({}), perspective {}", var1, var2.method_31044());
         this.o = null;
      }
   }

   public Feature3DUIModule.k T(class_243 var1, float var2, float var3, class_1297 var4, float var5, float var6) {
      if (this.isEnabled()) {
         if (this.o != null && var4 != null) {
            double var7 = this.X();
            if (var7 <= 0.001) {
               return null;
            }

            class_243 var9 = var4.method_30950(var5);
            class_243 var10 = this.o.method_31034() ? var9.method_1031(0.0, var6, 0.0) : var1;
            float var11 = var4 instanceof class_1309 var12 ? var12.method_55693() : 1.0F;
            float var28 = var4.method_5705(var5);
            float var13 = this.A.getValue().floatValue();
            double var14 = Math.toRadians(var28);
            double var16 = Math.toRadians(var13);
            class_243 var18 = new class_243(-Math.sin(var14) * Math.cos(var16), -Math.sin(var16), Math.cos(var14) * Math.cos(var16));
            class_243 var19 = new class_243(-Math.cos(var14), 0.0, -Math.sin(var14));
            class_243 var20 = var9.method_1031(0.0, var4.method_17682() * 0.6 + this.g.getValue() * var11, 0.0);
            double var21 = (this.k.G("Right") ? 1 : -1) * this.K.getValue() * var11;
            class_243 var23 = var20.method_1020(var18.method_1021(this.J.getValue() * var11)).method_1019(var19.method_1021(var21));
            class_3965 var24 = var4.method_73183().method_17742(new class_3959(var20, var23, class_3960.field_23142, class_242.field_1348, var4));
            if (var24.method_17783() != class_240.field_1333) {
               class_243 var25 = var24.method_17784().method_1020(var20);
               double var26 = var25.method_1033();
               var23 = var26 < 1.0E-4 ? var20 : var20.method_1019(var25.method_1021(Math.max(0.0, var26 - 0.2) / var26));
            }

            float var29 = (float)var7;
            class_243 var30 = var10.method_35590(var23, var29);
            return new Feature3DUIModule.k(
               var30.field_1352,
               var30.field_1351,
               var30.field_1350,
               var2 + class_3532.method_15393(var28 - var2) * var29,
               class_3532.method_16439(var29, var3, var13)
            );
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private Feature3DUIModule.c s(class_1297 var1, float var2) {
      if (this.o != null && !this.R.G("Forward") && var1 != null && var1 == getMc().field_1724) {
         float var3 = (float)this.X();
         if (var3 <= 0.001) {
            return null;
         }

         if (this.R.G("Custom")) {
            return new Feature3DUIModule.c(var1.method_5705(var2) + this.e.getValue().floatValue(), Float.NaN, var3);
         }

         class_243 var4 = getMc().field_1773.method_19418().method_71156();
         class_243 var5 = var1.method_5836(var2);
         double var6 = var4.field_1352 - var5.field_1352;
         double var8 = var4.field_1351 - var5.field_1351;
         double var10 = var4.field_1350 - var5.field_1350;
         return new Feature3DUIModule.c(
            (float)Math.toDegrees(Math.atan2(-var6, var10)), (float)(-Math.toDegrees(Math.atan2(var8, Math.hypot(var6, var10)))), var3
         );
      } else {
         return null;
      }
   }

   public static void G(class_1297 var0, class_10042 var1, float var2) {
      if (m != null && m.isEnabled()) {
         Feature3DUIModule var3 = m;
         Feature3DUIModule.c var4 = var3 == null ? null : var3.s(var0, var2);
         if (var4 != null) {
            float var5 = var1.field_53446;
            float var6 = var5 + var1.field_53447;
            float var7 = var4.weight();
            float var8 = var5 + class_3532.method_15393(var4.yaw() - var5) * var7;
            float var9 = var6 + class_3532.method_15393(var4.yaw() - var6) * var7;
            var1.field_53446 = var8;
            var1.field_53447 = class_3532.method_15393(var9 - var8);
            if (!Float.isNaN(var4.pitch())) {
               var1.field_53448 = class_3532.method_16439(var7, var1.field_53448, var4.pitch());
            }
         }
      }
   }

   public static float n(class_1297 var0, float var1, float var2) {
      if (m != null && m.isEnabled()) {
         Feature3DUIModule var3 = m;
         Feature3DUIModule.c var4 = var3 == null ? null : var3.s(var0, var2);
         return var4 == null ? var1 : var1 + class_3532.method_15393(var4.yaw() - var1) * var4.weight();
      } else {
         return var1;
      }
   }

   public static com.larpclient.lb.z q(class_437 var0) {
      Feature3DUIModule var1 = m;
      com.larpclient.lb.z var2 = var1 == null ? null : var1.n(var0);
      com.larpclient.lb.K(var0, var2);
      return var2;
   }

   private com.larpclient.lb.z n(class_437 var1) {
      if (this.o != null && this.d(var1)) {
         double var2 = this.X();
         if (var2 <= 0.001) {
            return null;
         }

         double[] var4 = com.larpclient.lb.Q(var1);
         if (var4 == null) {
            q58db36eeaf095e51 var5 = (q58db36eeaf095e51)var1;
            var4 = new double[]{var5.larpLeftPos(), var5.larpTopPos(), var5.larpLeftPos() + var5.larpImageWidth(), var5.larpTopPos() + var5.larpImageHeight()};
         }

         double var41 = var4[0];
         double var7 = var4[1];
         double var9 = var4[2];
         double var11 = var4[3];
         double var13 = (var41 + var9) / 2.0;
         double var15 = (var7 + var11) / 2.0;
         int var17 = this.k.G("Right") ? 1 : -1;
         double var18 = -var17 * Math.toRadians(this.G.getValue());
         double var20 = 3.5 * Math.max(1.0, (var9 - var41) / 2.0);
         double[] var22 = com.larpclient.lb.z.of(var13, var15, 0.0, 0.0, 1.0, var18, var20).bounds(var41, var7, var9, var11);
         double var23 = 8.0;
         double var25 = var1.field_22789;
         double var27 = var1.field_22790;
         double var29 = Math.min(
            Math.min(this.Y.getValue() / 100.0 * var27, var27 - 2.0 * var23) / (var22[3] - var22[1]), (var25 * 0.5 - var23) / (var22[2] - var22[0])
         );
         double var31 = var17 > 0 ? var25 * 0.71 : var25 * 0.29;
         double var33 = var31 - var29 * (var22[0] + var22[2]) / 2.0;
         var33 = var17 > 0 ? Math.min(var33, var25 - var23 - var29 * var22[2]) : Math.max(var33, var23 - var29 * var22[0]);
         double var35 = var27 / 2.0 - var29 * (var22[1] + var22[3]) / 2.0;
         if (var2 >= 0.999 && !this.d) {
            this.d = (boolean)1;
            q8.v(
               "GUI",
               "3D UI menu settled: {} content {}..{} x {}..{} (measured={}), scale {}, target {} {} in {}x{}",
               var1.getClass().getSimpleName(),
               Math.round(var41),
               Math.round(var9),
               Math.round(var7),
               Math.round(var11),
               com.larpclient.lb.Q(var1) != null,
               String.format(Locale.ROOT, "%.2f", var29),
               Math.round(var33),
               Math.round(var35),
               var1.field_22789,
               var1.field_22790
            );
         } else if (var2 < 0.5) {
            this.d = (boolean)0;
         }

         double var37 = var2;
         double var39 = class_3532.method_16436(var37, 1.0, var29);
         return com.larpclient.lb.z.of(
            var13, var15, class_3532.method_16436(var37, var13, var33), class_3532.method_16436(var37, var15, var35), var39, var18 * var37, var20 * var39
         );
      } else {
         return null;
      }
   }

   public static void E(class_332 var0, int var1, int var2) {
      Feature3DUIModule var3 = m;
      if (var3 != null) {
         float var4 = var3.L.getValue() ? 1.0F : (float)(1.0 - var3.X());
         if (!(var4 <= 0.01F)) {
            var0.method_25296(0, 0, var1, var2, A(-1072689136, var4), A(-804253680, var4));
         }
      }
   }

   private static int A(int var0, float var1) {
      return Math.round((var0 >>> 24) * var1) << 24 | var0 & 16777215;
   }

   public static boolean e() {
      if (m != null && m.isEnabled()) {
         Feature3DUIModule var0 = m;
         return var0 != null && !var0.t.getValue() && var0.o != null && var0.d(getMc().field_1755);
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      q8.v(
         "Camera",
         "3D UI active: screens={}, side={}, player facing={}{}",
         this.O.getValue(),
         this.k.getValue(),
         this.R.getValue(),
         this.R.G("Custom") ? " " + this.e.getValue().intValue() + " degrees" : ""
      );
   }

   @Override
   public void onDisable() {
      this.j("module inactive");
      this.u.w(0.0);
      com.larpclient.lb.K(null, null);
   }

   private record c(float yaw, float pitch, float weight) {
   }

   public record k(double x, double y, double z, float yaw, float pitch) {
   }
}
