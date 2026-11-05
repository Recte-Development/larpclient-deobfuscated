package com.larpclient.module;

import com.larpclient.ea;
import com.larpclient.ob;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.y2;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class LineGlyphsModule extends Module {
   private static LineGlyphsModule C;
   private final lc y = this.addSetting(new lc("Count", "Active line glyphs", 70.0, 10.0, 200.0, 5.0));
   private final ln L = this.addSetting(new ln("Slow Speed", "Slow the segment growth", false));
   private final lp n = this.addSetting(new lp("Color", "Glyph palette", "Theme", "Theme", "Rainbow", "Single"));
   private final ArrayList<LineGlyphsModule.a> e = new ArrayList<>();
   private final Random i = new Random(40506L);
   private Object u;
   private Object I;
   private Vec3 s;

   public LineGlyphsModule() {
      super("Line Glyphs", "Show animated angular lines around you", Category.VISUALS, -1);
      C = this;
   }

   public static LineGlyphsModule G() {
      return C;
   }

   public void Q() {
      this.e.clear();
      this.u = this.I = null;
      this.s = null;
   }

   @Override
   public void onEnable() {
      this.Q();
      r4.V("LineGlyphs", "Enabled; bounded world-fixed glyphs, count={}", this.y.getValue());
   }

   @Override
   public void onDisable() {
      this.Q();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label50: {
            var1 = this.getPlayer().position();
            if (this.u == getMc().level && this.I == this.getPlayer()) {
               if (this.s == null) {
                  break label50;
               }

               double var16;
               int var10000 = (var16 = var1.distanceToSqr(this.s) - 256.0) == 0.0 ? 0 : (var16 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label50;
               }
            }

            this.Q();
         }

         this.u = getMc().level;
         this.I = this.getPlayer();
         this.s = var1;
         double var2 = ea.t();
         this.e.removeIf(var2x -> var2x.X(var2));
         int var4 = this.y.getValue().intValue();

         while (this.e.size() > var4) {
            this.e.removeFirst();
         }

         for (int var5 = 0; var5 < 8 && this.e.size() < var4; var5++) {
            double var6 = ((Integer)getMc().options.fov().get()).intValue();
            double var8 = Math.toRadians(this.getPlayer().getYRot() + (this.i.nextDouble() - 0.5) * var6 * 1.5);
            int var10 = this.i.nextInt(6, 24);
            Vec3 var11 = new Vec3((int)var1.x - (int)(Math.sin(var8) * var10), (int)var1.y + this.i.nextInt(12), (int)var1.z + (int)(Math.cos(var8) * var10));
            this.e.add(new LineGlyphsModule.a(var11, this.i.nextInt(7, 13), var2, this.i));
         }

         for (LineGlyphsModule.a var15 : this.e) {
            var15.K(var2, this.L.getValue(), this.i);
         }
      } else {
         this.Q();
      }
   }

   public boolean Z(double var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && this.isInGame() && this.u == getMc().level) {
         int var3 = 0;

         for (LineGlyphsModule.a var5 : this.e) {
            double var6 = var5.t(var1);

            for (int var8 = 1; var8 < var5.G.size() && var6 > 0.0; var8++) {
               int var9 = this.s(var3 + var8 * 180, var1, var6 * (0.25 + (var8 - 1.0) / var5.G.size() / 1.75));
               int var10 = this.s(var3 + (var8 + 1) * 180, var1, var6 * (0.25 + (double)var8 / var5.G.size() / 1.75));
               if ((var9 | var10) >>> 24 != 0 && var5.G.get(var8 - 1).distanceToSqr(this.B(var5, var8)) >= 1.0E-10) {
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

   private Vec3 B(LineGlyphsModule.a var1, int var2) {
      Vec3 var3 = var1.G.get(var2);
      return var2 == var1.G.size() - 1
         ? var1.G.get(var2 - 1).lerp(var3, Math.clamp(1.0 - (var1.s - ea.Z() * (this.L.getValue() ? 1 : 2)) / Math.max(1.0, var1.D), 0.0, 1.0))
         : var3;
   }

   public void b(PoseStack var1, y2 var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.u == getMc().level) {
            ob var7 = new ob(var1, var2, var3);
            int var8 = 0;

            for (LineGlyphsModule.a var10 : this.e) {
               double var11 = var10.t(var4);
               double var13 = (0.15 + 2.85 * Math.clamp(1.0 - var10.n.distanceTo(var3.pos) / 20.0, 0.0, 1.0)) * 0.008;

               for (int var15 = 1; var15 < var10.G.size(); var15++) {
                  Vec3 var16 = var10.G.get(var15 - 1);
                  Vec3 var17 = this.B(var10, var15);
                  int var18 = this.s(var8 + var15 * 180, var4, var11 * (0.25 + (var15 - 1.0) / var10.G.size() / 1.75));
                  int var19 = this.s(var8 + (var15 + 1) * 180, var4, var11 * (0.25 + (double)var15 / var10.G.size() / 1.75));
                  if (!var6) {
                     var7.a(var16, var17, var13 * 3.0, oh.S(var18, 0.35), oh.S(var19, 0.35));
                  }

                  var7.a(var16, var17, var13, var18, var19);
                  int var20 = oh.j(var18, var18 & 0xFF000000 | 16777215, 0.28);
                  int var21 = oh.j(var19, var19 & 0xFF000000 | 16777215, var15 == var10.G.size() - 1 ? 0.6 : 0.35);
                  var7.a(var16, var17, var13 * 0.42, var20, var21);
               }

               var8++;
            }
         }
      }
   }

   private int s(int var1, double var2, double var4) {
      int var6 = this.n.i("Rainbow")
         ? oh.O((float)((var2 * 0.1 + var1 / 360.0) % 1.0), 1.0F, 1.0F)
         : (this.n.i("Single") ? rv.e : oh.j(rv.e, rv.GQ, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1 * 0.02)));
      return oh.i(var6, var4);
   }

   private static final class a {
      final Vec3 n;
      final ArrayList<Vec3> G = new ArrayList<>();
      final double F;
      int R;
      int s;
      int D;
      int W;
      int L;
      double h = Double.NaN;

      a(Vec3 var1, int var2, double var3, Random var5) {
         this.n = var1;
         this.R = var2;
         this.F = var3;
         this.G.add(var1);
         this.W = var5.nextInt(4) * 90;
         this.L = var5.nextInt(-1, 2) * 90;
      }

      void K(double var1, boolean var3, Random var4) {
         if (this.R == 0) {
            if (Double.isNaN(this.h)) {
               this.h = var1;
            }
         } else if (this.s > 0) {
            this.s = Math.max(0, this.s - (var3 ? 1 : 2));
         } else {
            int var5 = this.W;
            int var6 = this.L;

            for (int var7 = 0; var7 < 150 && Math.abs(this.L - var6) != 90; var7++) {
               this.L = var4.nextInt(-2, 2) * 90;
            }

            for (int var9 = 0; var9 < 5 && Math.abs(this.W - var5) != 90; var9++) {
               this.W = var4.nextInt(4) * 90;
            }

            this.s = this.D = var4.nextInt(3);
            int var10 = Math.max(1, this.s);
            Vec3 var8 = this.G.getLast();
            this.G
               .add(
                  var8.add(
                     this.L == 0 ? Math.round(-Math.sin(Math.toRadians(this.W)) * var10) : 0.0,
                     Math.round(Math.sin(Math.toRadians(this.L)) * var10),
                     this.L == 0 ? Math.round(Math.cos(Math.toRadians(this.W)) * var10) : 0.0
                  )
               );
            this.R--;
         }
      }

      double t(double var1) {
         return Double.isNaN(this.h) ? Math.clamp((var1 - this.F) / 0.6, 0.0, 1.0) : Math.clamp(1.0 - (var1 - this.h) / 0.4, 0.0, 1.0);
      }

      boolean X(double var1) {
         return !Double.isNaN(this.h) && var1 - this.h >= 0.4 || var1 - this.F > 5.0;
      }
   }
}
