package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class sv extends j0 {
   private final BooleanSupplier f;
   private final Runnable D;
   private final fq B;
   private final fq z = new fq(0.0);
   private final fq Z = new fq(0.0);
   private long W;
   private s8 m;

   public sv(BooleanSupplier var1, Runnable var2) {
      this.f = var1;
      this.D = var2;
      this.B = new fq(var1.getAsBoolean() ? 1.0 : 0.0);
   }

   public void o(s8 var1) {
      this.m = var1;
      this.bounds(var1.P + var1.L - 62, var1.T + (var1.O - 22) / 2, 22);
      this.O = 22;
      this.R = var1.R;
      this.V = var1.V;
   }

   public String w() {
      return this.f.getAsBoolean() ? "Remove from favorites" : "Add to favorites";
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.R) {
         double var4 = this.B.n(this.f.getAsBoolean() ? 1.0 : 0.0, 160);
         double var6 = this.z.n(!this.contains(var2, var3) && (!this.F || !this.X) ? 0.0 : 1.0, 160);
         double var8 = this.Z.n(System.nanoTime() < this.W ? 1.0 : 0.0, 100);
         w4 var10 = this.m == null ? w4.at(0.0, 0.0, c.y) : this.m.k();
         float var11 = this.m == null ? 0.0F : this.m.M();
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(0.0F, -var11);
            if (var6 > 0.001 || var8 > 0.001) {
               wc.b(var1, this.P, this.T, this.L, this.O, c.G, vv.N(var10.title(), var6 * 0.07 + var8 * 0.07));
            }

            int var12 = this.P + (this.L - 11) / 2;
            int var13 = this.T + (this.O - 11) / 2;
            int var14 = vv.Q(var10.subtitle(), var10.title(), var6 * 0.5);
            wz.D(var1, wz.M("star"), var12, var13, 11, var14);
            if (var4 > 0.001 && w4.contrast(c.y, var10.background()) < 3.0) {
               wz.D(var1, wz.M("star"), var12 - 1, var13 - 1, 13, var14);
            }

            if (var4 > 0.001) {
               wz.D(var1, wz.M("star-filled"), var12, var13, 11, vv.O(c.y | 0xFF000000, var4));
            }

            if (this.F && this.X) {
               wc.U(var1, this.P, this.T, this.L, this.O, 4);
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   public void x(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = ic.U(this.w(), var2 - 22);
      int var5 = ic.H(var4) + 14;
      int var6 = Math.clamp(this.P + this.L / 2 - var5 / 2, 4, Math.max(4, var2 - var5 - 4));
      int var7 = Math.clamp(this.T - 23, 4, Math.max(4, var3 - 23));
      wc.b(var1, var6, var7, var5, 19, 4, c.D);
      ic.N(var1, var4, var6 + 7, var7 + 5, c.H);
   }

   private void N() {
      this.W = System.nanoTime() + 110000000L;
      this.D.run();
      ft.h(ft.e.K);
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.N();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.R && this.V && this.F && (var1 == 257 || var1 == 32)) {
         this.N();
         return true;
      } else {
         return false;
      }
   }
}
