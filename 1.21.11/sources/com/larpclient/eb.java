package com.larpclient;

import com.larpclient.module.EmotesModule;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;

public final class eb extends class_437 implements tp {
   private static final int g = 66;
   private static final int H = 42;
   private static final int G = 6;
   private static final int E = 240;
   private static final double O = 0.11;
   private static final double b = 12.0;
   private static final int J = 12;
   private static final int t = 11;
   private final EmotesModule K;
   private final qb F = new qb(0.0);
   private final qb[] d = new qb[]{new qb(0.0), new qb(0.0), new qb(0.0), new qb(0.0)};
   private final qb[] P = new qb[]{new qb(0.0), new qb(0.0), new qb(0.0), new qb(0.0)};
   private int A = -1;
   private double M = Double.NaN;
   private float Y = 1.0F;
   private boolean S;

   public eb(EmotesModule var1) {
      super(class_2561.method_43470("Emotes"));
      this.K = var1;
      this.F.a(1.0, 240);
   }

   public static int s(double var0, double var2, double var4) {
      if (var0 * var0 + var2 * var2 < var4 * var4) {
         return -1;
      } else if (Math.abs(var0) > Math.abs(var2)) {
         return var0 > 0.0 ? 1 : 3;
      } else {
         return var2 > 0.0 ? 2 : 0;
      }
   }

   private void n(double var1, double var3) {
      double var5 = var1 - this.field_22789 / 2.0;
      double var7 = var3 - this.field_22790 / 2.0;
      this.A = s(var5, var7, 48.0F * this.Y);
      this.M = this.A < 0 ? Double.NaN : Math.atan2(var7, var5);
   }

   private void k() {
      if (!this.S) {
         this.S = (boolean)1;
         List var1 = this.K.f();
         if (this.A >= 0 && !"Empty".equals(var1.get(this.A))) {
            this.K.Y(this.A);
         } else if (this.A < 0 && this.K.n()) {
            this.K.M("stopped from the wheel");
         } else {
            q8.v("Emotes", "Wheel closed without an emote (slot {})", this.A < 0 ? "hub" : this.A + 1);
         }

         this.method_25419();
      }
   }

   public void method_25393() {
      int var1 = this.K.N();
      boolean var2 = s5.m(this.field_22787.method_22683().method_4490(), var1);
      if (!this.K.isRuntimeActive() || this.field_22787.field_1724 == null) {
         this.method_25419();
      } else if (!var2) {
         this.k();
      }
   }

   public boolean method_16803(class_11908 var1) {
      if (s2.i(var1) == this.K.N()) {
         this.k();
         return true;
      } else {
         return super.method_16803(var1);
      }
   }

   public void method_16014(double var1, double var3) {
      this.n(var1, var3);
   }

   public boolean method_25402(class_11909 var1, boolean var2) {
      this.n(var1.comp_4798(), var1.comp_4799());
      if (s2.m(var1) == 0) {
         this.k();
      } else {
         this.method_25419();
      }

      return true;
   }

   public boolean method_25421() {
      return false;
   }

   public void method_25420(class_332 var1, int var2, int var3, float var4) {
      gu.Z(var1, 0, 0, this.field_22789, this.field_22790, ei.D(-16777216, 0.25 * this.F.u()));
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      super.method_25394(var1, var2, var3, var4);
      List var5 = this.K.f();
      double var6 = this.F.a(1.0, 240);
      int var8 = this.field_22789 / 2;
      int var9 = this.field_22790 / 2;
      float var10 = (float)Math.min(1.0, (Math.min(this.field_22789, this.field_22790) - 24) / 276.0);
      this.Y = (float)((0.9 + 0.1 * var6) * var10);
      this.n(var2, var3);

      try (fp var11 = fp.O(var6)) {
         var1.method_51448().pushMatrix();

         try {
            var1.method_51448().translate(var8, var9);
            var1.method_51448().scale(this.Y, this.Y);

            for (int var12 = 0; var12 < 4; var12++) {
               this.L(var1, var12, (String)var5.get(var12), var6);
            }

            this.Z(var1, var5);
         } finally {
            var1.method_51448().popMatrix();
         }

         this.F(var1, var8, var9 + Math.round(128.0F * this.Y));
      }
   }

   private void L(class_332 var1, int var2, String var3, double var4) {
      boolean var6 = "Empty".equals(var3);
      fj.g var7 = fj.e(var3);
      int var8 = this.K.n() && var3.equals(this.K.S()) ? 1 : 0;
      double var9 = this.d[var2].a(var2 == this.A ? 1.0 : 0.0, 160);
      double var11 = this.P[var2].a(this.A >= 0 && var2 != this.A ? 1.0 : 0.0, 160);
      double var13 = Math.clamp((var4 - var2 * 0.11) / 0.6699999999999999, 0.0, 1.0);
      double var15 = var9 * 6.0 - (1.0 - var13) * 12.0;
      double var17 = var6 ? var9 * 0.4 : var9;
      int var19 = ei.D(ez.d, var6 ? 0.7 : 0.92 - 0.08 * var11);
      int var20 = ei.W(var19, ei.D(ei.W(ez.d, ez.a, 0.3), 0.96), var17);
      int var21 = ei.W(ei.D(-1381654, 0.09), var6 ? ei.D(-1381654, 0.3) : ez.a, Math.max(var9, var8 != 0 ? 0.7 : 0.0));
      int var22 = ei.D(ez.a, var17);

      try (fp var23 = fp.O(var13)) {
         qe.S(var1, var2, var15, var20, var21, var22);
         double var24 = (var2 - 1) * Math.PI / 2.0;
         double var26 = 82.0 + var15;
         int var28 = (int)Math.round(Math.cos(var24) * var26);
         int var29 = (int)Math.round(Math.sin(var24) * var26);
         uw.c var30 = uw.c.o;
         if (!var6 && var7 != null) {
            int var42 = ei.W(ei.W(-1184275, -1, var9), -5855578, var11);
            double var32 = 1.0 + 0.08 * var9;
            var1.method_51448().pushMatrix();

            try {
               var1.method_51448().translate(var28, var29);
               var1.method_51448().scale((float)var32, (float)var32);
               short var34 = 192;
               var1.method_25293(class_10799.field_56883, var7.icon(), -33, -33, 0.0F, 0.0F, 66, 66, var34, var34, var34, var34, fp.R(var42));
               return;
            } finally {
               var1.method_51448().popMatrix();
            }
         }

         int var31 = ei.W(ei.W(ez.t, ez.PH, var9), ez.d, 0.25 * var11);
         gu.Z(var1, var28 - 5, var29 - 9, 11, 1, var31);
         gu.Z(var1, var28, var29 - 14, 1, 11, var31);
         uw.X(var1, "Empty", var28 - uw.C("Empty", var30) / 2, var29 + 4, var31, var30);
      }
   }

   private void Z(class_332 var1, List<String> var2) {
      gu.Q(var1, 0, 0, 42, ei.D(ez.d, 0.9));
      p.a(var1, -42, -42, 85, 85, 42, ei.D(-1381654, 0.12));
      int var5 = -1381654;
      String var3;
      String var4;
      if (this.A >= 0) {
         var3 = (String)var2.get(this.A);
         boolean var6 = "Empty".equals(var3);
         var4 = var6 ? "No emote" : (this.K.n() && var3.equals(this.K.S()) ? "Replay" : "Play");
         if (var6) {
            var5 = ez.t;
         }
      } else {
         var3 = this.K.n() ? "Stop" : "Cancel";
         var4 = this.K.n() ? this.K.S() : "Emotes";
         var5 = ez.PH;
      }

      uw.c var13 = uw.c.e;
      List var7 = J(var3, var13, 72);
      int var8 = -(var7.size() * 12 + 11) / 2;

      for (String var10 : var7) {
         uw.X(var1, var10, -uw.C(var10, var13) / 2, var8, var5, var13);
         var8 += 12;
      }

      uw.c var14 = uw.c.H;
      var4 = m(var4, var14, 66);
      uw.X(var1, var4, -uw.C(var4, var14) / 2, var8 + 2, ez.PH, var14);
      if (!Double.isNaN(this.M)) {
         int var15 = (int)Math.round(Math.cos(this.M) * 45.0);
         int var11 = (int)Math.round(Math.sin(this.M) * 45.0);
         gu.Q(var1, var15, var11, 2, ez.a);
      }
   }

   private void F(class_332 var1, int var2, int var3) {
      String var4 = s5.S(this.K.N());
      String var5 = "Release " + var4 + " to play  ·  Esc to cancel";
      uw.c var6 = uw.c.M;
      uw.X(var1, var5, var2 - uw.C(var5, var6) / 2, var3, ez.PH, var6);
   }

   private static List<String> J(String var0, uw.c var1, int var2) {
      if (uw.C(var0, var1) <= var2) {
         return List.of(var0);
      }

      for (int var3 = var0.indexOf(32); var3 >= 0; var3 = var0.indexOf(32, var3 + 1)) {
         String var4 = var0.substring(0, var3);
         String var5 = var0.substring(var3 + 1);
         if (uw.C(var4, var1) <= var2 && uw.C(var5, var1) <= var2) {
            return List.of(var4, var5);
         }
      }

      return List.of(m(var0, var1, var2));
   }

   private static String m(String var0, uw.c var1, int var2) {
      if (uw.C(var0, var1) <= var2) {
         return var0;
      }

      String var3 = var0;

      while (var3.length() > 1 && uw.C(var3 + "...", var1) > var2) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3.strip() + "...";
   }
}
