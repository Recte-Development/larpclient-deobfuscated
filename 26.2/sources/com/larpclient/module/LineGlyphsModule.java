package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.gv;
import com.larpclient.hh;
import com.larpclient.ho;
import com.larpclient.uh;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.phys.Vec3;

public final class LineGlyphsModule extends Module {
   private static LineGlyphsModule o;
   private final e r = this.addSetting(new e("Count", "Active line glyphs", 70.0, 10.0, 200.0, 5.0));
   private final s F = this.addSetting(new s("Slow Speed", "Slow the segment growth", false));
   private final l s = this.addSetting(new l("Color", "Glyph palette", "Theme", "Theme", "Rainbow", "Single"));
   private final ArrayList<LineGlyphsModule.a> U = new ArrayList<>();
   private final Random V = new Random(40506L);
   private Object B;
   private Object i;
   private Vec3 l;

   public LineGlyphsModule() {
      super("Line Glyphs", "Show animated angular lines around you", Category.VISUALS, -1);
      o = this;
   }

   public static LineGlyphsModule w() {
      return o;
   }

   public void i() {
      this.U.clear();
      this.B = this.i = null;
      this.l = null;
   }

   @Override
   public void onEnable() {
      this.i();
      com.larpclient.h.A("LineGlyphs", "Enabled; bounded world-fixed glyphs, count={}", this.r.getValue());
   }

   @Override
   public void onDisable() {
      this.i();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         Vec3 var1;
         label50: {
            var1 = this.getPlayer().position();
            if (this.B == getMc().level && this.i == this.getPlayer()) {
               if (this.l == null) {
                  break label50;
               }

               double var16;
               int var10000 = (var16 = var1.distanceToSqr(this.l) - 256.0) == 0.0 ? 0 : (var16 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label50;
               }
            }

            this.i();
         }

         this.B = getMc().level;
         this.i = this.getPlayer();
         this.l = var1;
         double var2 = cf.I();
         this.U.removeIf(var2x -> var2x.X(var2));
         int var4 = this.r.getValue().intValue();

         while (this.U.size() > var4) {
            this.U.removeFirst();
         }

         for (int var5 = 0; var5 < 8 && this.U.size() < var4; var5++) {
            double var6 = ((Integer)getMc().options.fov().get()).intValue();
            double var8 = Math.toRadians(this.getPlayer().getYRot() + (this.V.nextDouble() - 0.5) * var6 * 1.5);
            int var10 = this.V.nextInt(6, 24);
            Vec3 var11 = new Vec3((int)var1.x - (int)(Math.sin(var8) * var10), (int)var1.y + this.V.nextInt(12), (int)var1.z + (int)(Math.cos(var8) * var10));
            this.U.add(new LineGlyphsModule.a(var11, this.V.nextInt(7, 13), var2, this.V));
         }

         for (LineGlyphsModule.a var15 : this.U) {
            var15.R(var2, this.F.getValue(), this.V);
         }
      } else {
         this.i();
      }
   }

   public boolean O(double var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && this.isInGame() && this.B == getMc().level) {
         int var3 = 0;

         for (LineGlyphsModule.a var5 : this.U) {
            double var6 = var5.g(var1);

            for (int var8 = 1; var8 < var5.d.size() && var6 > 0.0; var8++) {
               int var9 = this.H(var3 + var8 * 180, var1, var6 * (0.25 + (var8 - 1.0) / var5.d.size() / 1.75));
               int var10 = this.H(var3 + (var8 + 1) * 180, var1, var6 * (0.25 + (double)var8 / var5.d.size() / 1.75));
               if ((var9 | var10) >>> 24 != 0 && var5.d.get(var8 - 1).distanceToSqr(this.N(var5, var8)) >= 1.0E-10) {
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

   private Vec3 N(LineGlyphsModule.a var1, int var2) {
      Vec3 var3 = var1.d.get(var2);
      return var2 == var1.d.size() - 1
         ? var1.d.get(var2 - 1).lerp(var3, Math.clamp(1.0 - (var1.t - cf.l() * (this.F.getValue() ? 1 : 2)) / Math.max(1.0, var1.b), 0.0, 1.0))
         : var3;
   }

   public void S(PoseStack var1, hh var2, CameraRenderState var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.B == getMc().level) {
            gv var7 = new gv(var1, var2, var3);
            int var8 = 0;

            for (LineGlyphsModule.a var10 : this.U) {
               double var11 = var10.g(var4);
               double var13 = (0.15 + 2.85 * Math.clamp(1.0 - var10.s.distanceTo(var3.pos) / 20.0, 0.0, 1.0)) * 0.008;

               for (int var15 = 1; var15 < var10.d.size(); var15++) {
                  Vec3 var16 = var10.d.get(var15 - 1);
                  Vec3 var17 = this.N(var10, var15);
                  int var18 = this.H(var8 + var15 * 180, var4, var11 * (0.25 + (var15 - 1.0) / var10.d.size() / 1.75));
                  int var19 = this.H(var8 + (var15 + 1) * 180, var4, var11 * (0.25 + (double)var15 / var10.d.size() / 1.75));
                  if (!var6) {
                     var7.h(var16, var17, var13 * 3.0, uh.F(var18, 0.35), uh.F(var19, 0.35));
                  }

                  var7.h(var16, var17, var13, var18, var19);
                  int var20 = uh.q(var18, var18 & 0xFF000000 | 16777215, 0.28);
                  int var21 = uh.q(var19, var19 & 0xFF000000 | 16777215, var15 == var10.d.size() - 1 ? 0.6 : 0.35);
                  var7.h(var16, var17, var13 * 0.42, var20, var21);
               }

               var8++;
            }
         }
      }
   }

   private int H(int var1, double var2, double var4) {
      int var6 = this.s.O("Rainbow")
         ? uh.n((float)((var2 * 0.1 + var1 / 360.0) % 1.0), 1.0F, 1.0F)
         : (this.s.O("Single") ? ho.J : uh.q(ho.J, ho.ds, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1 * 0.02)));
      return uh.H(var6, var4);
   }

   private static final class a {
      final Vec3 s;
      final ArrayList<Vec3> d = new ArrayList<>();
      final double T;
      int P;
      int t;
      int b;
      int K;
      int E;
      double F = Double.NaN;

      a(Vec3 var1, int var2, double var3, Random var5) {
         this.s = var1;
         this.P = var2;
         this.T = var3;
         this.d.add(var1);
         this.K = var5.nextInt(4) * 90;
         this.E = var5.nextInt(-1, 2) * 90;
      }

      void R(double var1, boolean var3, Random var4) {
         if (this.P == 0) {
            if (Double.isNaN(this.F)) {
               this.F = var1;
            }
         } else if (this.t > 0) {
            this.t = Math.max(0, this.t - (var3 ? 1 : 2));
         } else {
            int var5 = this.K;
            int var6 = this.E;

            for (int var7 = 0; var7 < 150 && Math.abs(this.E - var6) != 90; var7++) {
               this.E = var4.nextInt(-2, 2) * 90;
            }

            for (int var9 = 0; var9 < 5 && Math.abs(this.K - var5) != 90; var9++) {
               this.K = var4.nextInt(4) * 90;
            }

            this.t = this.b = var4.nextInt(3);
            int var10 = Math.max(1, this.t);
            Vec3 var8 = this.d.getLast();
            this.d
               .add(
                  var8.add(
                     this.E == 0 ? Math.round(-Math.sin(Math.toRadians(this.K)) * var10) : 0.0,
                     Math.round(Math.sin(Math.toRadians(this.E)) * var10),
                     this.E == 0 ? Math.round(Math.cos(Math.toRadians(this.K)) * var10) : 0.0
                  )
               );
            this.P--;
         }
      }

      double g(double var1) {
         return Double.isNaN(this.F) ? Math.clamp((var1 - this.T) / 0.6, 0.0, 1.0) : Math.clamp(1.0 - (var1 - this.F) / 0.4, 0.0, 1.0);
      }

      boolean X(double var1) {
         return !Double.isNaN(this.F) && var1 - this.F >= 0.4 || var1 - this.T > 5.0;
      }
   }
}
