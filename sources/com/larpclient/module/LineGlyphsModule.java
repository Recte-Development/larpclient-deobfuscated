package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.mt;
import com.larpclient.ty;
import com.larpclient.xh;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class LineGlyphsModule extends Module {
   private static LineGlyphsModule N;
   private final d9 T = this.addSetting(new d9("Count", "Active line glyphs", 70.0, 10.0, 200.0, 5.0));
   private final dh Y = this.addSetting(new dh("Slow Speed", "Slow the segment growth", false));
   private final d6 Q = this.addSetting(new d6("Color", "Glyph palette", "Theme", "Theme", "Rainbow", "Single"));
   private final ArrayList<LineGlyphsModule.f> m = new ArrayList<>();
   private final Random u = new Random(40506L);
   private Object F;
   private Object I;
   private Vec3 b;

   public LineGlyphsModule() {
      super("Line Glyphs", "Show animated angular lines around you", Category.VISUALS, -1);
      N = this;
   }

   public static LineGlyphsModule I() {
      return N;
   }

   public void h() {
      this.m.clear();
      this.F = this.I = null;
      this.b = null;
   }

   @Override
   public void onEnable() {
      this.h();
      mt.K("LineGlyphs", "Enabled; bounded world-fixed glyphs, count={}", this.T.P());
   }

   @Override
   public void onDisable() {
      this.h();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label50: {
            var1 = this.getPlayer().position();
            if (this.F == getMc().level && this.I == this.getPlayer()) {
               if (this.b == null) {
                  break label50;
               }

               double var16;
               int var10000 = (var16 = var1.distanceToSqr(this.b) - 256.0) == 0.0 ? 0 : (var16 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label50;
               }
            }

            this.h();
         }

         this.F = getMc().level;
         this.I = this.getPlayer();
         this.b = var1;
         double var2 = ty.P();
         this.m.removeIf(var2x -> var2x.o(var2));
         int var4 = this.T.P().intValue();

         while (this.m.size() > var4) {
            this.m.removeFirst();
         }

         for (int var5 = 0; var5 < 8 && this.m.size() < var4; var5++) {
            double var6 = ((Integer)getMc().options.fov().get()).intValue();
            double var8 = Math.toRadians(this.getPlayer().getYRot() + (this.u.nextDouble() - 0.5) * var6 * 1.5);
            int var10 = this.u.nextInt(6, 24);
            Vec3 var11 = new Vec3((int)var1.x - (int)(Math.sin(var8) * var10), (int)var1.y + this.u.nextInt(12), (int)var1.z + (int)(Math.cos(var8) * var10));
            this.m.add(new LineGlyphsModule.f(var11, this.u.nextInt(7, 13), var2, this.u));
         }

         for (LineGlyphsModule.f var15 : this.m) {
            var15.u(var2, this.Y.P(), this.u);
         }
      } else {
         this.h();
      }
   }

   public boolean p(double var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && this.isInGame() && this.F == getMc().level) {
         int var3 = 0;

         for (LineGlyphsModule.f var5 : this.m) {
            double var6 = var5.d(var1);

            for (int var8 = 1; var8 < var5.U.size() && var6 > 0.0; var8++) {
               int var9 = this.K(var3 + var8 * 180, var1, var6 * (0.25 + (var8 - 1.0) / var5.U.size() / 1.75));
               int var10 = this.K(var3 + (var8 + 1) * 180, var1, var6 * (0.25 + (double)var8 / var5.U.size() / 1.75));
               if ((var9 | var10) >>> 24 != 0 && var5.U.get(var8 - 1).distanceToSqr(this.g(var5, var8)) >= 1.0E-10) {
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

   private Vec3 g(LineGlyphsModule.f var1, int var2) {
      Vec3 var3 = var1.U.get(var2);
      return var2 == var1.U.size() - 1
         ? var1.U.get(var2 - 1).lerp(var3, Math.clamp(1.0 - (var1.s - ty.g() * (this.Y.P() ? 1 : 2)) / Math.max(1.0, var1.E), 0.0, 1.0))
         : var3;
   }

   public void M(PoseStack var1, xh var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.F == getMc().level) {
            com.larpclient.j var7 = new com.larpclient.j(var1, var2, var3);
            int var8 = 0;

            for (LineGlyphsModule.f var10 : this.m) {
               double var11 = var10.d(var4);
               double var13 = (0.15 + 2.85 * Math.clamp(1.0 - var10.P.distanceTo(var3.pos) / 20.0, 0.0, 1.0)) * 0.008;

               for (int var15 = 1; var15 < var10.U.size(); var15++) {
                  Vec3 var16 = var10.U.get(var15 - 1);
                  Vec3 var17 = this.g(var10, var15);
                  int var18 = this.K(var8 + var15 * 180, var4, var11 * (0.25 + (var15 - 1.0) / var10.U.size() / 1.75));
                  int var19 = this.K(var8 + (var15 + 1) * 180, var4, var11 * (0.25 + (double)var15 / var10.U.size() / 1.75));
                  if (!var6) {
                     var7.H(var16, var17, var13 * 3.0, _3.U(var18, 0.35), _3.U(var19, 0.35));
                  }

                  var7.H(var16, var17, var13, var18, var19);
                  int var20 = _3.D(var18, var18 & 0xFF000000 | 16777215, 0.28);
                  int var21 = _3.D(var19, var19 & 0xFF000000 | 16777215, var15 == var10.U.size() - 1 ? 0.6 : 0.35);
                  var7.H(var16, var17, var13 * 0.42, var20, var21);
               }

               var8++;
            }
         }
      }
   }

   private int K(int var1, double var2, double var4) {
      int var6 = this.Q.S("Rainbow")
         ? _3.Z((float)((var2 * 0.1 + var1 / 360.0) % 1.0), 1.0F, 1.0F)
         : (this.Q.S("Single") ? com.larpclient.f.C : _3.D(com.larpclient.f.C, com.larpclient.f.O, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1 * 0.02)));
      return _3.A(var6, var4);
   }

   private static final class f {
      final Vec3 P;
      final ArrayList<Vec3> U = new ArrayList<>();
      final double l;
      int D;
      int s;
      int E;
      int b;
      int A;
      double x = Double.NaN;

      f(Vec3 var1, int var2, double var3, Random var5) {
         this.P = var1;
         this.D = var2;
         this.l = var3;
         this.U.add(var1);
         this.b = var5.nextInt(4) * 90;
         this.A = var5.nextInt(-1, 2) * 90;
      }

      void u(double var1, boolean var3, Random var4) {
         if (this.D == 0) {
            if (Double.isNaN(this.x)) {
               this.x = var1;
            }
         } else if (this.s > 0) {
            this.s = Math.max(0, this.s - (var3 ? 1 : 2));
         } else {
            int var5 = this.b;
            int var6 = this.A;

            for (int var7 = 0; var7 < 150 && Math.abs(this.A - var6) != 90; var7++) {
               this.A = var4.nextInt(-2, 2) * 90;
            }

            for (int var9 = 0; var9 < 5 && Math.abs(this.b - var5) != 90; var9++) {
               this.b = var4.nextInt(4) * 90;
            }

            this.s = this.E = var4.nextInt(3);
            int var10 = Math.max(1, this.s);
            Vec3 var8 = this.U.getLast();
            this.U
               .add(
                  var8.add(
                     this.A == 0 ? Math.round(-Math.sin(Math.toRadians(this.b)) * var10) : 0.0,
                     Math.round(Math.sin(Math.toRadians(this.A)) * var10),
                     this.A == 0 ? Math.round(Math.cos(Math.toRadians(this.b)) * var10) : 0.0
                  )
               );
            this.D--;
         }
      }

      double d(double var1) {
         return Double.isNaN(this.x) ? Math.clamp((var1 - this.l) / 0.6, 0.0, 1.0) : Math.clamp(1.0 - (var1 - this.x) / 0.4, 0.0, 1.0);
      }

      boolean o(double var1) {
         return !Double.isNaN(this.x) && var1 - this.x >= 0.4 || var1 - this.l > 5.0;
      }
   }
}
