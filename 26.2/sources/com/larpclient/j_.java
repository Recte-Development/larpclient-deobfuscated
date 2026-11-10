package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class j_ extends jd {
   private final BooleanSupplier s;
   private final Runnable q;
   private final jp y;
   private final jp X = new jp(0.0);
   private final jp I = new jp(0.0);
   private long V;
   private jx N;

   public j_(BooleanSupplier var1, Runnable var2) {
      this.s = var1;
      this.q = var2;
      this.y = new jp(var1.getAsBoolean() ? 1.0 : 0.0);
   }

   public void H(jx var1) {
      this.N = var1;
      this.bounds(var1.S + var1.A - 62, var1.u + (var1.J - 22) / 2, 22);
      this.J = 22;
      this.i = var1.i;
      this.p = var1.p;
   }

   public String H() {
      return this.s.getAsBoolean() ? "Remove from favorites" : "Add to favorites";
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.i) {
         double var4 = this.y.k(this.s.getAsBoolean() ? 1.0 : 0.0, 160);
         double var6 = this.X.k(!this.contains(var2, var3) && (!this.T || !this.W) ? 0.0 : 1.0, 160);
         double var8 = this.I.k(System.nanoTime() < this.V ? 1.0 : 0.0, 100);
         lv var10 = this.N == null ? lv.at(0.0, 0.0, ho.J) : this.N.B();
         float var11 = this.N == null ? 0.0F : this.N.g();
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(0.0F, -var11);
            if (var6 > 0.001 || var8 > 0.001) {
               wp.I(var1, this.S, this.u, this.A, this.J, ho.Q, uh.H(var10.title(), var6 * 0.07 + var8 * 0.07));
            }

            int var12 = this.S + (this.A - 11) / 2;
            int var13 = this.u + (this.J - 11) / 2;
            int var14 = uh.q(var10.subtitle(), var10.title(), var6 * 0.5);
            ur.O(var1, ur.V("star"), var12, var13, 11, var14);
            if (var4 > 0.001 && lv.contrast(ho.J, var10.background()) < 3.0) {
               ur.O(var1, ur.V("star"), var12 - 1, var13 - 1, 13, var14);
            }

            if (var4 > 0.001) {
               ur.O(var1, ur.V("star-filled"), var12, var13, 11, uh.F(ho.J | 0xFF000000, var4));
            }

            if (this.T && this.W) {
               wp.t(var1, this.S, this.u, this.A, this.J, 4);
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   public void r(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = bj.T(this.H(), var2 - 22);
      int var5 = bj.u(var4) + 14;
      int var6 = Math.clamp(this.S + this.A / 2 - var5 / 2, 4, Math.max(4, var2 - var5 - 4));
      int var7 = Math.clamp(this.u - 23, 4, Math.max(4, var3 - 23));
      wp.I(var1, var6, var7, var5, 19, 4, ho.A);
      bj.E(var1, var4, var6 + 7, var7 + 5, ho.u);
   }

   private void d() {
      this.V = System.nanoTime() + 110000000L;
      this.q.run();
      sr.v(sr.e.q);
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.d();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.i && this.p && this.T && (var1 == 257 || var1 == 32)) {
         this.d();
         return true;
      } else {
         return false;
      }
   }
}
