package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ue extends b {
   public final String u;
   public final String U;
   private final Runnable d;
   private final ef Z = new ef(0.0);
   private final ef B = new ef(0.0);
   private final ef a = new ef(0.0);
   private final ef c = new ef(0.0);
   private m8.w n = new m8.w(1.0, 0.0);
   public boolean K;
   public double E;
   public Consumer<GuiGraphicsExtractor> v;
   public boolean f;
   public boolean j;
   public double g = 1.0;
   private long y;

   public ue(String var1, String var2, Runnable var3) {
      this.u = var1;
      this.U = var2;
      this.d = var3;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.D) {
         boolean var4 = LarpClient.F().f().t().V;
         double var5 = var4
            ? this.Z.m(!this.contains(var2, var3) && (!this.l || !this.R) ? 0.0 : 1.0, 140)
            : (!this.contains(var2, var3) && (!this.l || !this.R) ? 0.0 : 1.0);
         double var7 = var4 ? this.B.m(System.nanoTime() < this.y ? 1.0 : 0.0, 90) : 0.0;
         double var9 = var4 ? this.a.m(this.f ? 1.0 : 0.0, 220) : (this.f ? 1.0 : 0.0);
         this.n = m8.E(var5, var7, this.E, var4, this.K);
         int var11 = this.j ? -24915 : com.larpclient.f.J;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.q + this.A / 2.0F, (float)(this.J + this.W / 2.0 + this.n.offsetY()));
            var1.pose().scale((float)this.n.scale(), (float)this.n.scale());
            var1.pose().translate(-this.q - this.A / 2.0F, -this.J - this.W / 2.0F);
            pn.b(
               var1,
               this.q,
               this.J,
               this.A,
               this.W,
               5,
               _3.U(_3.D(-987684056, _3.A(this.j ? -24915 : com.larpclient.f.C, 0.45), Math.max(var9 * 0.55, var5 * 0.6 + var7 * 0.2)), this.g)
            );
            if (this.l && this.R) {
               pn.Y(var1, this.q, this.J, this.A, this.W, _3.U(com.larpclient.f.C, this.g));
            }

            if (this.v != null) {
               this.v.accept(var1);
            } else if (!this.U.isEmpty()) {
               tp.N(var1, tp.j(this.U), this.q + (this.A - 14) / 2, this.J + (this.W - 14) / 2, 14, _3.U(this.X ? var11 : com.larpclient.f.G, this.g));
            } else {
               k6.M(var1, k6.n(this.u, this.A - 12), this.q + this.A / 2, this.J + (this.W - 9) / 2, _3.U(this.X ? var11 : com.larpclient.f.G, this.g));
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   @Override
   public boolean contains(double var1, double var3) {
      return super.contains(this.n.localX(var1, this.q + this.A / 2.0), this.n.localY(var3, this.J + this.W / 2.0));
   }

   public void u(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
      if (this.D) {
         boolean var6 = LarpClient.F().f().t().V;
         int var7 = !this.contains(var2, var3) && (!this.l || !this.R) ? 0 : 1;
         double var8 = var6 ? this.c.m(var7 != 0 ? 1.0 : 0.0, var7 != 0 ? 160 : 100) : (var7 != 0 ? 1.0 : 0.0);
         if (!(var8 < 0.01)) {
            String var10 = k6.n(this.u, var4 - 20);
            int var11 = k6.x(var10) + 14;
            int var12 = Math.clamp(this.q + this.A / 2 - var11 / 2, 4, Math.max(4, var4 - var11 - 4));
            int var13 = Math.clamp(this.J - 23 + (int)Math.round(this.n.offsetY() + (1.0 - var8) * 4.0), 4, Math.max(4, var5 - 24));
            pn.b(var1, var12, var13, var11, 19, 4, _3.U(-283567328, var8));
            k6.G(var1, var10, var12 + 7, var13 + 5, _3.U(com.larpclient.f.J, var8));
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.y = System.nanoTime() + 90000000L;
         this.d.run();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.D && this.X && this.l && (var1 == 257 || var1 == 32)) {
         this.y = System.nanoTime() + 90000000L;
         this.d.run();
         return true;
      } else {
         return false;
      }
   }
}
