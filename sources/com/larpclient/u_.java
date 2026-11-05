package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class u_ extends b {
   private final BooleanSupplier n;
   private final Runnable O;
   private final ef T;
   private final ef c = new ef(0.0);
   private final ef e = new ef(0.0);
   private long m;
   private u7 L;

   public u_(BooleanSupplier var1, Runnable var2) {
      this.n = var1;
      this.O = var2;
      this.T = new ef(var1.getAsBoolean() ? 1.0 : 0.0);
   }

   public void r(u7 var1) {
      this.L = var1;
      this.bounds(var1.q + var1.A - 62, var1.J + (var1.W - 22) / 2, 22);
      this.W = 22;
      this.D = var1.D;
      this.X = var1.X;
   }

   public String B() {
      return this.n.getAsBoolean() ? "Remove from favorites" : "Add to favorites";
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.D) {
         double var4 = this.T.m(this.n.getAsBoolean() ? 1.0 : 0.0, 160);
         double var6 = this.c.m(!this.contains(var2, var3) && (!this.l || !this.R) ? 0.0 : 1.0, 160);
         double var8 = this.e.m(System.nanoTime() < this.m ? 1.0 : 0.0, 100);
         n var10 = this.L == null ? com.larpclient.n.at(0.0, 0.0, f.C) : this.L.g();
         float var11 = this.L == null ? 0.0F : this.L.i();
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(0.0F, -var11);
            if (var6 > 0.001 || var8 > 0.001) {
               pn.b(var1, this.q, this.J, this.A, this.W, f.aj, _3.A(var10.title(), var6 * 0.07 + var8 * 0.07));
            }

            int var12 = this.q + (this.A - 11) / 2;
            int var13 = this.J + (this.W - 11) / 2;
            int var14 = _3.D(var10.subtitle(), var10.title(), var6 * 0.5);
            tp.N(var1, tp.j("star"), var12, var13, 11, var14);
            if (var4 > 0.001 && com.larpclient.n.contrast(f.C, var10.background()) < 3.0) {
               tp.N(var1, tp.j("star"), var12 - 1, var13 - 1, 13, var14);
            }

            if (var4 > 0.001) {
               tp.N(var1, tp.j("star-filled"), var12, var13, 11, _3.U(f.C | 0xFF000000, var4));
            }

            if (this.l && this.R) {
               pn.y(var1, this.q, this.J, this.A, this.W, 4);
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   public void J(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = k6.j(this.B(), var2 - 22);
      int var5 = k6.P(var4) + 14;
      int var6 = Math.clamp(this.q + this.A / 2 - var5 / 2, 4, Math.max(4, var2 - var5 - 4));
      int var7 = Math.clamp(this.J - 23, 4, Math.max(4, var3 - 23));
      pn.b(var1, var6, var7, var5, 19, 4, f.u);
      k6.Z(var1, var4, var6 + 7, var7 + 5, f.J);
   }

   private void j() {
      this.m = System.nanoTime() + 110000000L;
      this.O.run();
      kb.d(kb.n.w);
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.j();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.D && this.X && this.l && (var1 == 257 || var1 == 32)) {
         this.j();
         return true;
      } else {
         return false;
      }
   }
}
