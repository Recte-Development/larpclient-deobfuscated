package com.larpclient.module;

import com.larpclient.b9;
import com.larpclient.bu;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.fp;
import com.larpclient.gu;
import com.larpclient.lx;
import com.larpclient.q8;
import com.larpclient.qb;
import com.larpclient.s2;
import com.larpclient.tj;
import com.larpclient.tk;
import com.larpclient.tv;
import java.util.Locale;
import net.minecraft.class_1041;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_11910;
import net.minecraft.class_156;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4184;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.joml.Matrix4f;

public final class WorldMenuModule extends Module {
   private static WorldMenuModule A;
   private final li T = this.addSetting(
      new li("Placement", "Pinned stays where you leave it and closes once you walk away; Follow moves along with you", "Pinned", "Pinned", "Follow")
   );
   private final lo n = this.addSetting(new lo("Close Distance", "A pinned menu closes once you are this many blocks from it", 15.0, 4.0, 48.0, 1.0));
   private final lo N = this.addSetting(
      new lo("Menu Height", "Share of your view the menu fills when it opens in front of you, in percent", 50.0, 30.0, 90.0, 1.0)
   );
   private final lo t = this.addSetting(new lo("Panel Size", "Height of the menu panel in blocks", 1.5, 0.5, 4.0, 0.1));
   private static final double a = 0.5;
   private static final double f = 16.0;
   private static final long D = 50000000L;
   private static final long M = 250L;
   private static final int j = 76;
   private static final int H = 6;
   private static final int u = 9;
   private static final int k = 16;
   private static final int s = 9;
   private static final int J = 16;
   private static final int l = 22;
   private tv.h o;
   private Object C;
   private tk R;
   private tk V;
   private class_243 b;
   private boolean X;
   private double Q;
   private double q;
   private int h;
   private double y;
   private double G;
   private long P;
   private int i = -1;
   private WorldMenuModule.h m;
   private final qb Y = new qb(0.0);
   private final qb I = new qb(0.0);
   private double L = 1.0;
   private long O;

   public WorldMenuModule() {
      super("World Menu", "Opens the ClickGUI on a panel in the world while you keep playing", Category.RENDER, -1);
      A = this;
   }

   public static WorldMenuModule v() {
      return A;
   }

   @Override
   public String getDisplayName() {
      return "3D Click UI";
   }

   private boolean Z() {
      class_310 var1 = getMc();
      return this.isRuntimeActive() && var1.field_1724 != null && var1.field_1687 != null && var1.field_1773.method_19418().method_19332();
   }

   private boolean i() {
      class_310 var1 = getMc();
      return var1.field_1755 == null && var1.method_18506() == null && var1.field_1729.method_1613();
   }

   private double x(tk var1) {
      double[] var2 = var1.h();
      return this.t.getValue() / Math.max(1.0, var2[3] - var2[1]);
   }

   private com.larpclient.lb.n u(tk var1, tv.h var2) {
      class_4184 var3 = getMc().field_1773.method_19418();
      class_243 var4 = var3.method_71156();
      return tv.N(
         var2,
         this.x(var1),
         var1.field_22789 / 2.0,
         var1.field_22790 / 2.0,
         var4.field_1352,
         var4.field_1351,
         var4.field_1350,
         b9.n(new Matrix4f()),
         var1.field_22789,
         var1.field_22790
      );
   }

   private static double x(tk var0, com.larpclient.lb.n var1) {
      double[] var2 = var0.h();
      double[] var3 = var1.bounds(var2[0], var2[1], var2[2], var2[3]);
      if (var3[0] == -10000.0) {
         return 0.0;
      }

      double var4 = b9.U();
      double var6 = ((Integer)getMc().field_1690.method_41808().method_41753()).intValue();
      double var8 = var4 > 1.0 && var4 < 179.0 && var6 > 1.0 && var6 < 179.0
         ? Math.tan(Math.toRadians(var4) / 2.0) / Math.tan(Math.toRadians(var6) / 2.0)
         : 1.0;
      return (var3[3] - var3[1]) / Math.max(1, var0.field_22790) * var8;
   }

   private double x() {
      return this.T.G("Pinned") ? Math.clamp(this.n.getValue() - 1.0, 0.5, 16.0) : 16.0;
   }

   public static boolean a(tk var0) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var1 = A;
         return var1 != null && var1.Y(var0);
      } else {
         return false;
      }
   }

   private boolean Y(tk var1) {
      if (!this.Z()) {
         return false;
      }

      if (var1 == this.R) {
         return true;
      }

      if (var1.n() != null) {
         return false;
      }

      if (this.R != null) {
         this.r("a new window replaced it");
      }

      this.V = null;
      this.R = var1;
      this.L();
      class_1041 var2 = getMc().method_22683();
      var1.method_25423(var2.method_4486(), var2.method_4502());
      this.s(var1);
      return true;
   }

   private void s(tk var1) {
      class_4184 var2 = getMc().field_1773.method_19418();
      class_243 var3 = var2.method_71156();
      tv.h var4 = tv.I(var3.field_1352, var3.field_1351, var3.field_1350, var2.method_19330(), var2.method_19329(), 1.0);
      double var5 = x(var1, this.u(var1, var4));
      double var7 = var5 > 0.0 ? Math.clamp(var5 / (this.N.getValue() / 100.0), 0.5, 16.0) : 2.0;
      this.o = tv.I(var3.field_1352, var3.field_1351, var3.field_1350, var2.method_19330(), var2.method_19329(), var7);
      this.C = getMc().field_1687;
      this.b = var3;
      this.Y.w(1.0);
      this.L = 1.0;
      this.O = 0L;
      q8.v(
         "GUI",
         "World Menu opened in the world, {} blocks ahead, yaw {}, pitch {}, {}",
         O(var7),
         Math.round(this.o.yaw()),
         Math.round(this.o.pitch()),
         this.T.G("Pinned") ? "closes beyond " + this.n.getValue().intValue() + " blocks" : "following"
      );
   }

   private double p() {
      if (this.o == null) {
         return 0.0;
      }

      class_243 var1 = getMc().field_1773.method_19418().method_71156();
      return this.o.distance(var1.field_1352, var1.field_1351, var1.field_1350);
   }

   private static String O(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   public static boolean O() {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var0 = A;
         return var0 != null && var0.R != null && var0.Z();
      } else {
         return false;
      }
   }

   public static boolean H() {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var0 = A;
         if (var0 != null && var0.R != null && var0.Z()) {
            q8.v("GUI", "World Menu closed with the menu key");
            var0.R.method_25419();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean G(tk var0) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var1 = A;
         return var1 != null && var1.A(var0);
      } else {
         return false;
      }
   }

   private boolean A(tk var1) {
      if (var1 != null && var1 == this.R) {
         this.R = null;
         this.V = var1;
         this.L();
         var1.method_25432();
         q8.v("GUI", "World Menu closing {} blocks away", O(this.p()));
         return true;
      } else {
         return false;
      }
   }

   private void r(String var1) {
      tk var2 = this.R;
      this.R = this.V = null;
      this.L();
      this.o = null;
      this.C = null;
      this.b = null;
      if (var2 != null) {
         q8.v("GUI", "World Menu removed its panel ({})", var1);

         try {
            var2.method_25432();
         } catch (RuntimeException var4) {
            q8.L("GUI", "World Menu window cleanup failed after {}: {}", var1, var4.toString());
         }
      }
   }

   private void L() {
      this.h = 0;
      this.m = null;
      this.X = (boolean)0;
   }

   @Override
   public void onTick() {
      tk var1 = this.R;
      if (var1 != null) {
         int var10000 = this.Z();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.C == getMc().field_1687) {
            var1.method_25393();
            if (this.R == var1) {
               int var11 = this.T.G("Pinned");
               boolean var5 = false;
               byte var4 = (byte)var11;
               var11 = Integer.signum(var4);
               if (((var11 | -var11) >>> 31 ^ 1) == 0 && this.m == null) {
                  double var2 = this.p();
                  double var14;
                  var11 = (var14 = var2 - this.n.getValue()) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
                  boolean var7 = false;
                  int var6 = var11;
                  if (-Integer.signum(var6) >>> 31 == 0) {
                     return;
                  }

                  q8.v("GUI", "World Menu closed: you are {} blocks from it, past Close Distance {}", O(var2), this.n.getValue().intValue());
                  var1.method_25419();
                  return;
               }
            }
         } else {
            this.r("left the level");
         }
      }
   }

   public static void a(class_332 var0, float var1) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var2 = A;
         if (var2 != null) {
            var2.o(var0, var1);
         }
      }
   }

   private void o(class_332 var1, float var2) {
      this.X = (boolean)0;
      tk var3 = this.R != null ? this.R : this.V;
      if (var3 != null) {
         class_310 var4 = getMc();
         if (!this.Z() || this.o == null || this.C != var4.field_1687) {
            this.r(this.Z() ? "left the level" : "unavailable");
         } else if (var3 == this.V && var3.C()) {
            this.V = null;
         } else {
            if (var3.field_22789 != var1.method_51421() || var3.field_22790 != var1.method_51443()) {
               if (var3 != this.R) {
                  this.V = null;
                  return;
               }

               var3.method_25423(var1.method_51421(), var1.method_51443());
               q8.v("GUI", "World Menu window laid out again at {}x{}", var3.field_22789, var3.field_22790);
            }

            if (!var4.field_1690.field_1842 && !(var4.field_1755 instanceof tj)) {
               class_243 var5 = var4.field_1773.method_19418().method_71156();
               if (this.T.G("Follow") && this.b != null && this.m == null) {
                  this.o = new tv.h(
                     this.o.x() + var5.field_1352 - this.b.field_1352,
                     this.o.y() + var5.field_1351 - this.b.field_1351,
                     this.o.z() + var5.field_1350 - this.b.field_1350,
                     this.o.yaw(),
                     this.o.pitch()
                  );
               }

               this.b = var5;
               if (this.m != null) {
                  this.G(var3);
               }

               if (!this.o.faces(var5.field_1352, var5.field_1351, var5.field_1350)) {
                  this.Y.w(0.0);
               } else {
                  com.larpclient.lb.n var6 = this.u(var3, this.o);
                  double[] var7 = var3.h();
                  double[] var8 = var6.bounds(0.0, 0.0, var3.field_22789, Math.max(var3.field_22790, var7[3] + 24.0));
                  if (!(var8[2] < 0.0) && !(var8[3] < 0.0) && !(var8[0] > var3.field_22789) && !(var8[1] > var3.field_22790)) {
                     long var9 = System.nanoTime();
                     if (var9 - this.O >= 50000000L) {
                        this.O = var9;
                        this.L = this.D(var3, var5);
                     }

                     double var11 = this.Y.a(this.L, 160);
                     if (!(var11 < 0.01)) {
                        double[] var13 = var6.inverse(var3.field_22789 / 2.0, var3.field_22790 / 2.0);
                        int var14 = var3 == this.R && var13[0] != -10000.0 ? 1 : 0;
                        this.X = (boolean)(var14 != 0 && var11 > 0.5 && this.i() && var3.V(var13[0], var13[1]) && !this.x(var3, var13, var5) ? 1 : 0);
                        if (this.X) {
                           this.Q = var13[0];
                           this.q = var13[1];
                        }

                        if (var14 != 0 && this.h != 0 && this.m == null && (var13[0] != this.y || var13[1] != this.G)) {
                           class_11910 var15 = new class_11910(Integer.numberOfTrailingZeros(this.h), 0);
                           var3.method_25403(new class_11909(var13[0], var13[1], var15), var13[0] - this.y, var13[1] - this.G);
                           this.y = var13[0];
                           this.G = var13[1];
                        }

                        int var18 = !this.X && (var14 == 0 || this.h == 0) ? 0 : 1;
                        int var16 = var18 != 0 ? (int)Math.floor(var13[0]) : -10000;
                        int var17 = var18 != 0 ? (int)Math.floor(var13[1]) : -10000;
                        var1.method_71048();
                        com.larpclient.lb.y(var1, var6, false, () -> {
                           var1.method_71048();

                           try (fp var7x = fp.O(var11)) {
                              var3.method_25394(var1, var16, var17, var2);
                           }
                        });
                        var1.method_71048();
                     }
                  } else {
                     this.Y.w(0.0);
                  }
               }
            }
         }
      }
   }

   private boolean x(tk var1, double[] var2, class_243 var3) {
      class_239 var4 = getMc().field_1765;
      if (var4 != null && var4.method_17783() != class_240.field_1333) {
         double var5 = this.x(var1);
         double[] var7 = this.o.point((var2[0] - var1.field_22789 / 2.0) * var5, (var1.field_22790 / 2.0 - var2[1]) * var5);
         return var4.method_17784().method_1022(var3) < new class_243(var7[0], var7[1], var7[2]).method_1022(var3);
      } else {
         return false;
      }
   }

   private void G(tk var1) {
      class_4184 var2 = getMc().field_1773.method_19418();
      double[] var3 = tv.o(b9.n(new Matrix4f()), var1.field_22789 / 2.0, var1.field_22790 / 2.0, var1.field_22789, var1.field_22790);
      if (var3 != null) {
         class_243 var4 = var2.method_71156();
         this.o = tv.D(var4.field_1352, var4.field_1351, var4.field_1350, var3, this.m.distance(), this.m.right(), this.m.up());
      }
   }

   private double D(tk var1, class_243 var2) {
      class_310 var3 = getMc();
      double[] var4 = var1.h();
      double var5 = this.x(var1);
      double var7 = var1.field_22789 / 2.0;
      double var9 = var1.field_22790 / 2.0;
      double var11 = (var4[0] + var4[2]) / 2.0;
      double var13 = (var4[1] + var4[3]) / 2.0;
      double var15 = (var4[2] - var4[0]) * 0.4;
      double var17 = (var4[3] - var4[1]) * 0.4;
      double[][] var19 = new double[][]{
         {var11, var13}, {var11 - var15, var13 - var17}, {var11 + var15, var13 - var17}, {var11 - var15, var13 + var17}, {var11 + var15, var13 + var17}
      };
      int var20 = 0;

      for (double[] var24 : var19) {
         double[] var25 = this.o.point((var24[0] - var7) * var5, (var9 - var24[1]) * var5);
         class_243 var26 = new class_243(var25[0], var25[1], var25[2]);
         class_3965 var27 = var3.field_1687.method_17742(new class_3959(var2, var26, class_3960.field_23142, class_242.field_1348, var3.field_1724));
         if (var27.method_17783() == class_240.field_1333 || var27.method_17784().method_1022(var2) >= var26.method_1022(var2) - 0.05) {
            var20++;
         }
      }

      return (double)var20 / var19.length;
   }

   public static boolean W(class_11910 var0, int var1) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var2 = A;
         return var2 != null && var2.e(var0, var1);
      } else {
         return false;
      }
   }

   private boolean e(class_11910 var1, int var2) {
      tk var3 = this.R;
      int var4 = 1 << Math.clamp(s2.E(var1), 0, 30);
      if (var2 == 0) {
         if ((this.h & var4) == 0) {
            return false;
         }

         this.h &= ~var4;
         if (s2.E(var1) == 0) {
            this.O();
         }

         if (var3 != null) {
            var3.method_25406(new class_11909(this.y, this.G, var1));
         }

         return getMc().field_1755 == null;
      } else {
         if (var2 != 1 || var3 == null || !this.Z() || !this.i()) {
            return false;
         }

         if (!this.X) {
            if (var3.R()) {
               var3.z();
               q8.v("GUI", "World Menu typing ended by a click in the world");
            }

            return false;
         } else {
            long var5 = class_156.method_658();
            int var7 = s2.E(var1) == this.i && var5 - this.P < 250L ? 1 : 0;
            this.P = var5;
            this.i = s2.E(var1);
            this.h |= var4;
            this.y = this.Q;
            this.G = this.q;
            var3.method_25402(new class_11909(this.Q, this.q, var1), (boolean)var7);
            return true;
         }
      }
   }

   public static boolean A() {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var0 = A;
         return var0 != null && var0.R != null && (var0.X || var0.h != 0) && var0.i();
      } else {
         return false;
      }
   }

   public static boolean U(double var0) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var2 = A;
         return var2 != null && var2.s(var0);
      } else {
         return false;
      }
   }

   private boolean s(double var1) {
      tk var3 = this.R;
      if (var3 == null || !this.Z() || !this.i()) {
         return false;
      }

      if (this.m != null) {
         this.m = new WorldMenuModule.h(Math.clamp(this.m.distance() * Math.pow(1.12, var1), 0.5, this.x()), this.m.right(), this.m.up());
         return true;
      }

      if (!this.X) {
         return false;
      }

      var3.method_25401(this.Q, this.q, 0.0, var1);
      return true;
   }

   private boolean y(tk var1) {
      return var1 != null && var1 == this.R && this.Z() && this.i() && var1.R();
   }

   public static boolean V() {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var0 = A;
         return var0 != null && var0.y(var0.R);
      } else {
         return false;
      }
   }

   public static boolean G(int var0, class_11908 var1) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var2 = A;
         if (var2 != null && var0 != 0 && var2.y(var2.R)) {
            if (s2.i(var1) == 256) {
               var2.R.Q();
               q8.v("GUI", "World Menu typing ended with Escape");
            } else {
               var2.R.method_25404(var1);
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean a(class_11905 var0) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var1 = A;
         if (var1 != null && var1.y(var1.R)) {
            var1.R.method_25400(var0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean X(tk var0) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var1 = A;
         return var1 != null && var0 != null && (var0 == var1.R || var0 == var1.V) && var1.Z();
      } else {
         return false;
      }
   }

   private static int[] v(bu var0) {
      return new int[]{var0.left() + (var0.width() - 76) / 2, var0.top() + var0.height() + 9, 76, 6};
   }

   private static boolean J(bu var0, double var1, double var3) {
      int[] var5 = v(var0);
      return var1 >= var5[0] - 16 && var1 < var5[0] + var5[2] + 16 && var3 >= var5[1] - 9 && var3 < var5[1] + var5[3] + 9;
   }

   private static int[] W(bu var0) {
      int[] var1 = v(var0);
      return new int[]{var1[0] + var1[2] + 22, var1[1] + var1[3] / 2 - 8, 16, 16};
   }

   private static boolean v(bu var0, double var1, double var3) {
      int[] var5 = W(var0);
      return var1 >= var5[0] - 4 && var1 < var5[0] + var5[2] + 4 && var3 >= var5[1] - 4 && var3 < var5[1] + var5[3] + 4;
   }

   public static boolean C(bu var0, double var1, double var3) {
      return J(var0, var1, var3) || v(var0, var1, var3);
   }

   public static void P(class_332 var0, tk var1, bu var2, int var3, int var4) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var5 = A;
         if (var5 != null && X(var1)) {
            var5.I(var0, var1, var2, var3, var4);
         }
      }
   }

   private void I(class_332 var1, tk var2, bu var3, int var4, int var5) {
      int var6 = var2 == this.R ? 1 : 0;
      int var7 = var6 != 0 && this.m != null ? 1 : 0;
      int var8 = var7 != 0 || var6 != 0 && J(var3, var4, var5) ? 1 : 0;
      double var9 = this.I.a(var8 != 0 ? 1.0 : 0.0, 180);
      int[] var11 = v(var3);
      int var12 = var11[2] + (int)Math.round(18.0 * var9);
      int var13 = var11[0] - (var12 - var11[2]) / 2;
      int var14 = var7 != 0 ? ez.X(var13, ez.d) : ei.D(ez.H, 0.34 + 0.42 * var9);
      gu.N(var1, var13, var11[1], var12, var11[3], var11[3] / 2, var14);
      int[] var15 = W(var3);
      int var16 = var6 != 0 && this.m == null && v(var3, var4, var5) ? 1 : 0;
      gu.N(var1, var15[0], var15[1], var15[2], var15[3], var15[2] / 2, var16 != 0 ? -1750963 : ei.D(ez.H, 0.16));
      lx.N(var1, lx.n("x"), var15[0] + 3, var15[1] + 3, var15[2] - 6, var16 != 0 ? -1 : ei.D(ez.H, 0.8));
   }

   public static boolean M(tk var0, double var1, double var3, int var5, bu var6) {
      if (A != null && A.isEnabled()) {
         WorldMenuModule var7 = A;
         return var7 != null && var7.O(var0, var1, var3, var5, var6);
      } else {
         return false;
      }
   }

   private boolean O(tk var1, double var2, double var4, int var6, bu var7) {
      if (var6 != 0 || var1 != this.R || !this.Z()) {
         return false;
      } else if (v(var7, var2, var4)) {
         q8.v("GUI", "World Menu closed with its X");
         var1.method_25419();
         return true;
      } else if (J(var7, var2, var4) && this.X) {
         double var8 = this.x(var1);
         double var10 = (this.Q - var1.field_22789 / 2.0) * var8;
         double var12 = (var1.field_22790 / 2.0 - this.q) * var8;
         double[] var14 = this.o.point(var10, var12);
         class_243 var15 = getMc().field_1773.method_19418().method_71156();
         double var16 = new class_243(var14[0], var14[1], var14[2]).method_1022(var15);
         this.m = new WorldMenuModule.h(Math.clamp(var16, 0.5, this.x()), var10, var12);
         q8.v("GUI", "World Menu grabbed {} blocks away", O(var16));
         return true;
      } else {
         return false;
      }
   }

   private void O() {
      if (this.m != null) {
         this.m = null;
         q8.v(
            "GUI",
            "World Menu set down {} blocks away, yaw {}, pitch {}",
            O(this.p()),
            this.o == null ? 0L : Math.round(this.o.yaw()),
            this.o == null ? 0L : Math.round(this.o.pitch())
         );
      }
   }

   @Override
   public void onEnable() {
      q8.v(
         "GUI",
         "World Menu active: placement={}, close distance={}, height={}%, size={}",
         this.T.getValue(),
         this.n.getValue().intValue(),
         this.N.getValue().intValue(),
         this.t.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.r("turned off");
      this.Y.w(0.0);
   }

   private record h(double distance, double right, double up) {
   }
}
