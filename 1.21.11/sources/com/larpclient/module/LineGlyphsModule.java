package com.larpclient.module;

import com.larpclient.cw;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.jq;
import com.larpclient.q8;
import com.larpclient.qo;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.class_12075;
import net.minecraft.class_243;
import net.minecraft.class_4587;

public final class LineGlyphsModule extends Module {
   private static LineGlyphsModule l;
   private final lo H = this.addSetting(new lo("Count", "Active line glyphs", 70.0, 10.0, 200.0, 5.0));
   private final l3 A = this.addSetting(new l3("Slow Speed", "Slow the segment growth", false));
   private final li K = this.addSetting(new li("Color", "Glyph palette", "Theme", "Theme", "Rainbow", "Single"));
   private final ArrayList<LineGlyphsModule.v> S = new ArrayList<>();
   private final Random b = new Random(40506L);
   private Object t;
   private Object W;
   private class_243 X;

   public LineGlyphsModule() {
      super("Line Glyphs", "Show animated angular lines around you", Category.VISUALS, -1);
      l = this;
   }

   public static LineGlyphsModule a() {
      return l;
   }

   public void s() {
      this.S.clear();
      this.t = this.W = null;
      this.X = null;
   }

   @Override
   public void onEnable() {
      this.s();
      q8.G("LineGlyphs", "Enabled; bounded world-fixed glyphs, count={}", this.H.getValue());
   }

   @Override
   public void onDisable() {
      this.s();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         class_243 var1;
         label50: {
            var1 = this.getPlayer().method_73189();
            if (this.t == getMc().field_1687 && this.W == this.getPlayer()) {
               if (this.X == null) {
                  break label50;
               }

               double var16;
               int var10000 = (var16 = var1.method_1025(this.X) - 256.0) == 0.0 ? 0 : (var16 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label50;
               }
            }

            this.s();
         }

         this.t = getMc().field_1687;
         this.W = this.getPlayer();
         this.X = var1;
         double var2 = qo.K();
         this.S.removeIf(var2x -> var2x.R(var2));
         int var4 = this.H.getValue().intValue();

         while (this.S.size() > var4) {
            this.S.removeFirst();
         }

         for (int var5 = 0; var5 < 8 && this.S.size() < var4; var5++) {
            double var6 = ((Integer)getMc().field_1690.method_41808().method_41753()).intValue();
            double var8 = Math.toRadians(this.getPlayer().method_36454() + (this.b.nextDouble() - 0.5) * var6 * 1.5);
            int var10 = this.b.nextInt(6, 24);
            class_243 var11 = new class_243(
               (int)var1.field_1352 - (int)(Math.sin(var8) * var10),
               (int)var1.field_1351 + this.b.nextInt(12),
               (int)var1.field_1350 + (int)(Math.cos(var8) * var10)
            );
            this.S.add(new LineGlyphsModule.v(var11, this.b.nextInt(7, 13), var2, this.b));
         }

         for (LineGlyphsModule.v var15 : this.S) {
            var15.q(var2, this.A.getValue(), this.b);
         }
      } else {
         this.s();
      }
   }

   public boolean D(double var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && this.isInGame() && this.t == getMc().field_1687) {
         int var3 = 0;

         for (LineGlyphsModule.v var5 : this.S) {
            double var6 = var5.C(var1);

            for (int var8 = 1; var8 < var5.Y.size() && var6 > 0.0; var8++) {
               int var9 = this.k(var3 + var8 * 180, var1, var6 * (0.25 + (var8 - 1.0) / var5.Y.size() / 1.75));
               int var10 = this.k(var3 + (var8 + 1) * 180, var1, var6 * (0.25 + (double)var8 / var5.Y.size() / 1.75));
               if ((var9 | var10) >>> 24 != 0 && var5.Y.get(var8 - 1).method_1025(this.j(var5, var8)) >= 1.0E-10) {
                  return true;
               }
            }

            var3++;
         }

         return false;
      } else {
         return false;
      }
   }

   private class_243 j(LineGlyphsModule.v var1, int var2) {
      class_243 var3 = var1.Y.get(var2);
      return var2 == var1.Y.size() - 1
         ? var1.Y.get(var2 - 1).method_35590(var3, Math.clamp(1.0 - (var1.F - qo.C() * (this.A.getValue() ? 1 : 2)) / Math.max(1.0, var1.p), 0.0, 1.0))
         : var3;
   }

   public void d(class_4587 var1, cw var2, class_12075 var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.t == getMc().field_1687) {
            jq var7 = new jq(var1, var2, var3);
            int var8 = 0;

            for (LineGlyphsModule.v var10 : this.S) {
               double var11 = var10.C(var4);
               double var13 = (0.15 + 2.85 * Math.clamp(1.0 - var10.l.method_1022(var3.field_63078) / 20.0, 0.0, 1.0)) * 0.008;

               for (int var15 = 1; var15 < var10.Y.size(); var15++) {
                  class_243 var16 = var10.Y.get(var15 - 1);
                  class_243 var17 = this.j(var10, var15);
                  int var18 = this.k(var8 + var15 * 180, var4, var11 * (0.25 + (var15 - 1.0) / var10.Y.size() / 1.75));
                  int var19 = this.k(var8 + (var15 + 1) * 180, var4, var11 * (0.25 + (double)var15 / var10.Y.size() / 1.75));
                  if (!var6) {
                     var7.n(var16, var17, var13 * 3.0, ei.R(var18, 0.35), ei.R(var19, 0.35));
                  }

                  var7.n(var16, var17, var13, var18, var19);
                  int var20 = ei.W(var18, var18 & 0xFF000000 | 16777215, 0.28);
                  int var21 = ei.W(var19, var19 & 0xFF000000 | 16777215, var15 == var10.Y.size() - 1 ? 0.6 : 0.35);
                  var7.n(var16, var17, var13 * 0.42, var20, var21);
               }

               var8++;
            }
         }
      }
   }

   private int k(int var1, double var2, double var4) {
      int var6 = this.K.G("Rainbow")
         ? ei.X((float)((var2 * 0.1 + var1 / 360.0) % 1.0), 1.0F, 1.0F)
         : (this.K.G("Single") ? ez.a : ei.W(ez.a, ez.f, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1 * 0.02)));
      return ei.D(var6, var4);
   }

   private static final class v {
      final class_243 l;
      final ArrayList<class_243> Y = new ArrayList<>();
      final double z;
      int W;
      int F;
      int p;
      int w;
      int G;
      double D = Double.NaN;

      v(class_243 var1, int var2, double var3, Random var5) {
         this.l = var1;
         this.W = var2;
         this.z = var3;
         this.Y.add(var1);
         this.w = var5.nextInt(4) * 90;
         this.G = var5.nextInt(-1, 2) * 90;
      }

      void q(double var1, boolean var3, Random var4) {
         if (this.W == 0) {
            if (Double.isNaN(this.D)) {
               this.D = var1;
            }
         } else if (this.F > 0) {
            this.F = Math.max(0, this.F - (var3 ? 1 : 2));
         } else {
            int var5 = this.w;
            int var6 = this.G;

            for (int var7 = 0; var7 < 150 && Math.abs(this.G - var6) != 90; var7++) {
               this.G = var4.nextInt(-2, 2) * 90;
            }

            for (int var9 = 0; var9 < 5 && Math.abs(this.w - var5) != 90; var9++) {
               this.w = var4.nextInt(4) * 90;
            }

            this.F = this.p = var4.nextInt(3);
            int var10 = Math.max(1, this.F);
            class_243 var8 = this.Y.getLast();
            this.Y
               .add(
                  var8.method_1031(
                     this.G == 0 ? Math.round(-Math.sin(Math.toRadians(this.w)) * var10) : 0.0,
                     Math.round(Math.sin(Math.toRadians(this.G)) * var10),
                     this.G == 0 ? Math.round(Math.cos(Math.toRadians(this.w)) * var10) : 0.0
                  )
               );
            this.W--;
         }
      }

      double C(double var1) {
         return Double.isNaN(this.D) ? Math.clamp((var1 - this.z) / 0.6, 0.0, 1.0) : Math.clamp(1.0 - (var1 - this.D) / 0.4, 0.0, 1.0);
      }

      boolean R(double var1) {
         return !Double.isNaN(this.D) && var1 - this.D >= 0.4 || var1 - this.z > 5.0;
      }
   }
}
