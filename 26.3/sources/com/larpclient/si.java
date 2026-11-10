package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class si extends j0 {
   public final String s;
   public final String x;
   private final Runnable h;
   private final fq d = new fq(0.0);
   private final fq g = new fq(0.0);
   private final fq z = new fq(0.0);
   private final fq D = new fq(0.0);
   private fr.z Y = new fr.z(1.0, 0.0);
   public boolean C;
   public double p;
   public Consumer<GuiGraphicsExtractor> q;
   public boolean l;
   public boolean y;
   public double n = 1.0;
   private long S;

   public si(String var1, String var2, Runnable var3) {
      this.s = var1;
      this.x = var2;
      this.h = var3;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.R) {
         boolean var4 = LarpClient.N().h().k().w;
         double var5 = var4
            ? this.d.n(!this.contains(var2, var3) && (!this.F || !this.X) ? 0.0 : 1.0, 140)
            : (!this.contains(var2, var3) && (!this.F || !this.X) ? 0.0 : 1.0);
         double var7 = var4 ? this.g.n(System.nanoTime() < this.S ? 1.0 : 0.0, 90) : 0.0;
         double var9 = var4 ? this.z.n(this.l ? 1.0 : 0.0, 220) : (this.l ? 1.0 : 0.0);
         this.Y = fr.t(var5, var7, this.p, var4, this.C);
         int var11 = this.y ? -24915 : c.H;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.P + this.L / 2.0F, (float)(this.T + this.O / 2.0 + this.Y.offsetY()));
            var1.pose().scale((float)this.Y.scale(), (float)this.Y.scale());
            var1.pose().translate(-this.P - this.L / 2.0F, -this.T - this.O / 2.0F);
            wc.b(
               var1,
               this.P,
               this.T,
               this.L,
               this.O,
               5,
               vv.O(vv.Q(-987684056, vv.N(this.y ? -24915 : c.y, 0.45), Math.max(var9 * 0.55, var5 * 0.6 + var7 * 0.2)), this.n)
            );
            if (this.F && this.X) {
               wc.j(var1, this.P, this.T, this.L, this.O, vv.O(c.y, this.n));
            }

            if (this.q != null) {
               this.q.accept(var1);
            } else if (!this.x.isEmpty()) {
               wz.D(var1, wz.M(this.x), this.P + (this.L - 14) / 2, this.T + (this.O - 14) / 2, 14, vv.O(this.V ? var11 : c.f, this.n));
            } else {
               ic.s(var1, ic.W(this.s, this.L - 12), this.P + this.L / 2, this.T + (this.O - 9) / 2, vv.O(this.V ? var11 : c.f, this.n));
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   @Override
   public boolean contains(double var1, double var3) {
      return super.contains(this.Y.localX(var1, this.P + this.L / 2.0), this.Y.localY(var3, this.T + this.O / 2.0));
   }

   public void m(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
      if (this.R) {
         boolean var6 = LarpClient.N().h().k().w;
         int var7 = !this.contains(var2, var3) && (!this.F || !this.X) ? 0 : 1;
         double var8 = var6 ? this.D.n(var7 != 0 ? 1.0 : 0.0, var7 != 0 ? 160 : 100) : (var7 != 0 ? 1.0 : 0.0);
         if (!(var8 < 0.01)) {
            String var10 = ic.W(this.s, var4 - 20);
            int var11 = ic.E(var10) + 14;
            int var12 = Math.clamp(this.P + this.L / 2 - var11 / 2, 4, Math.max(4, var4 - var11 - 4));
            int var13 = Math.clamp(this.T - 23 + (int)Math.round(this.Y.offsetY() + (1.0 - var8) * 4.0), 4, Math.max(4, var5 - 24));
            wc.b(var1, var12, var13, var11, 19, 4, vv.O(-283567328, var8));
            ic.H(var1, var10, var12 + 7, var13 + 5, vv.O(c.H, var8));
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.S = System.nanoTime() + 90000000L;
         this.h.run();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.R && this.V && this.F && (var1 == 257 || var1 == 32)) {
         this.S = System.nanoTime() + 90000000L;
         this.h.run();
         return true;
      } else {
         return false;
      }
   }
}
