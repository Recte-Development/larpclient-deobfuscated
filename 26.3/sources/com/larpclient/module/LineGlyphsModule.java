package com.larpclient.module;

import com.larpclient._g;
import com.larpclient._q;
import com.larpclient.hz;
import com.larpclient.vv;
import com.larpclient.wh;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class LineGlyphsModule extends Module {
   private static LineGlyphsModule r;
   private final b U = this.addSetting(new b("Count", "Active line glyphs", 70.0, 10.0, 200.0, 5.0));
   private final m m = this.addSetting(new m("Slow Speed", "Slow the segment growth", false));
   private final o t = this.addSetting(new o("Color", "Glyph palette", "Theme", "Theme", "Rainbow", "Single"));
   private final ArrayList<LineGlyphsModule.y> R = new ArrayList<>();
   private final Random j = new Random(40506L);
   private Object P;
   private Object x;
   private Vec3 I;

   public LineGlyphsModule() {
      super("Line Glyphs", "Show animated angular lines around you", Category.VISUALS, -1);
      r = this;
   }

   public static LineGlyphsModule d() {
      return r;
   }

   public void z() {
      this.R.clear();
      this.P = this.x = null;
      this.I = null;
   }

   @Override
   public void onEnable() {
      this.z();
      _q.z("LineGlyphs", "Enabled; bounded world-fixed glyphs, count={}", this.U.getValue());
   }

   @Override
   public void onDisable() {
      this.z();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label50: {
            var1 = this.getPlayer().position();
            if (this.P == getMc().level && this.x == this.getPlayer()) {
               if (this.I == null) {
                  break label50;
               }

               double var16;
               int var10000 = (var16 = var1.distanceToSqr(this.I) - 256.0) == 0.0 ? 0 : (var16 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label50;
               }
            }

            this.z();
         }

         this.P = getMc().level;
         this.x = this.getPlayer();
         this.I = var1;
         double var2 = hz.S();
         this.R.removeIf(var2x -> var2x.N(var2));
         int var4 = this.U.getValue().intValue();

         while (this.R.size() > var4) {
            this.R.removeFirst();
         }

         for (int var5 = 0; var5 < 8 && this.R.size() < var4; var5++) {
            double var6 = ((Integer)getMc().options.fov().get()).intValue();
            double var8 = Math.toRadians(this.getPlayer().getYRot() + (this.j.nextDouble() - 0.5) * var6 * 1.5);
            int var10 = this.j.nextInt(6, 24);
            Vec3 var11 = new Vec3((int)var1.x - (int)(Math.sin(var8) * var10), (int)var1.y + this.j.nextInt(12), (int)var1.z + (int)(Math.cos(var8) * var10));
            this.R.add(new LineGlyphsModule.y(var11, this.j.nextInt(7, 13), var2, this.j));
         }

         for (LineGlyphsModule.y var15 : this.R) {
            var15.o(var2, this.m.getValue(), this.j);
         }
      } else {
         this.z();
      }
   }

   public boolean K(double var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && this.isInGame() && this.P == getMc().level) {
         int var3 = 0;

         for (LineGlyphsModule.y var5 : this.R) {
            double var6 = var5.u(var1);

            for (int var8 = 1; var8 < var5.D.size() && var6 > 0.0; var8++) {
               int var9 = this.R(var3 + var8 * 180, var1, var6 * (0.25 + (var8 - 1.0) / var5.D.size() / 1.75));
               int var10 = this.R(var3 + (var8 + 1) * 180, var1, var6 * (0.25 + (double)var8 / var5.D.size() / 1.75));
               if ((var9 | var10) >>> 24 != 0 && var5.D.get(var8 - 1).distanceToSqr(this.T(var5, var8)) >= 1.0E-10) {
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

   private Vec3 T(LineGlyphsModule.y var1, int var2) {
      Vec3 var3 = var1.D.get(var2);
      return var2 == var1.D.size() - 1
         ? var1.D.get(var2 - 1).lerp(var3, Math.clamp(1.0 - (var1.g - hz.C() * (this.m.getValue() ? 1 : 2)) / Math.max(1.0, var1.m), 0.0, 1.0))
         : var3;
   }

   public void R(PoseStack var1, wh var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.P == getMc().level) {
            _g var7 = new _g(var1, var2, var3);
            int var8 = 0;

            for (LineGlyphsModule.y var10 : this.R) {
               double var11 = var10.u(var4);
               double var13 = (0.15 + 2.85 * Math.clamp(1.0 - var10.z.distanceTo(var3.pos) / 20.0, 0.0, 1.0)) * 0.008;

               for (int var15 = 1; var15 < var10.D.size(); var15++) {
                  Vec3 var16 = var10.D.get(var15 - 1);
                  Vec3 var17 = this.T(var10, var15);
                  int var18 = this.R(var8 + var15 * 180, var4, var11 * (0.25 + (var15 - 1.0) / var10.D.size() / 1.75));
                  int var19 = this.R(var8 + (var15 + 1) * 180, var4, var11 * (0.25 + (double)var15 / var10.D.size() / 1.75));
                  if (!var6) {
                     var7.M(var16, var17, var13 * 3.0, vv.O(var18, 0.35), vv.O(var19, 0.35));
                  }

                  var7.M(var16, var17, var13, var18, var19);
                  int var20 = vv.Q(var18, var18 & 0xFF000000 | 16777215, 0.28);
                  int var21 = vv.Q(var19, var19 & 0xFF000000 | 16777215, var15 == var10.D.size() - 1 ? 0.6 : 0.35);
                  var7.M(var16, var17, var13 * 0.42, var20, var21);
               }

               var8++;
            }
         }
      }
   }

   private int R(int var1, double var2, double var4) {
      int var6 = this.t.w("Rainbow")
         ? vv.b((float)((var2 * 0.1 + var1 / 360.0) % 1.0), 1.0F, 1.0F)
         : (this.t.w("Single") ? com.larpclient.c.y : vv.Q(com.larpclient.c.y, com.larpclient.c.t, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1 * 0.02)));
      return vv.N(var6, var4);
   }

   private static final class y {
      final Vec3 z;
      final ArrayList<Vec3> D = new ArrayList<>();
      final double O;
      int B;
      int g;
      int m;
      int Q;
      int X;
      double P = Double.NaN;

      y(Vec3 var1, int var2, double var3, Random var5) {
         this.z = var1;
         this.B = var2;
         this.O = var3;
         this.D.add(var1);
         this.Q = var5.nextInt(4) * 90;
         this.X = var5.nextInt(-1, 2) * 90;
      }

      void o(double var1, boolean var3, Random var4) {
         if (this.B == 0) {
            if (Double.isNaN(this.P)) {
               this.P = var1;
            }
         } else if (this.g > 0) {
            this.g = Math.max(0, this.g - (var3 ? 1 : 2));
         } else {
            int var5 = this.Q;
            int var6 = this.X;

            for (int var7 = 0; var7 < 150 && Math.abs(this.X - var6) != 90; var7++) {
               this.X = var4.nextInt(-2, 2) * 90;
            }

            for (int var9 = 0; var9 < 5 && Math.abs(this.Q - var5) != 90; var9++) {
               this.Q = var4.nextInt(4) * 90;
            }

            this.g = this.m = var4.nextInt(3);
            int var10 = Math.max(1, this.g);
            Vec3 var8 = this.D.getLast();
            this.D
               .add(
                  var8.add(
                     this.X == 0 ? Math.round(-Math.sin(Math.toRadians(this.Q)) * var10) : 0.0,
                     Math.round(Math.sin(Math.toRadians(this.X)) * var10),
                     this.X == 0 ? Math.round(Math.cos(Math.toRadians(this.Q)) * var10) : 0.0
                  )
               );
            this.B--;
         }
      }

      double u(double var1) {
         return Double.isNaN(this.P) ? Math.clamp((var1 - this.O) / 0.6, 0.0, 1.0) : Math.clamp(1.0 - (var1 - this.P) / 0.4, 0.0, 1.0);
      }

      boolean N(double var1) {
         return !Double.isNaN(this.P) && var1 - this.P >= 0.4 || var1 - this.O > 5.0;
      }
   }
}
