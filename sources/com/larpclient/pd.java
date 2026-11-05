package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class pd extends oq {
   public final String m;
   public final String I;
   private final Runnable r;
   private final u7 s = new u7(0.0);
   private final u7 Y = new u7(0.0);
   private final u7 H = new u7(0.0);
   private final u7 G = new u7(0.0);
   private r3.h d = new r3.h(1.0, 0.0);
   public boolean N;
   public double n;
   public Consumer<GuiGraphicsExtractor> z;
   public boolean p;
   public boolean U;
   public double f = 1.0;
   private long J;

   public pd(String var1, String var2, Runnable var3) {
      this.m = var1;
      this.I = var2;
      this.r = var3;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.F) {
         boolean var4 = LarpClient.y().v().E().o;
         double var5 = var4
            ? this.s.y(!this.contains(var2, var3) && (!this.A || !this.h) ? 0.0 : 1.0, 140)
            : (!this.contains(var2, var3) && (!this.A || !this.h) ? 0.0 : 1.0);
         double var7 = var4 ? this.Y.y(System.nanoTime() < this.J ? 1.0 : 0.0, 90) : 0.0;
         double var9 = var4 ? this.H.y(this.p ? 1.0 : 0.0, 220) : (this.p ? 1.0 : 0.0);
         this.d = r3.g(var5, var7, this.n, var4, this.N);
         int var11 = this.U ? -24915 : rv.P;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.B + this.v / 2.0F, (float)(this.L + this.O / 2.0 + this.d.offsetY()));
            var1.pose().scale((float)this.d.scale(), (float)this.d.scale());
            var1.pose().translate(-this.B - this.v / 2.0F, -this.L - this.O / 2.0F);
            ll.N(
               var1,
               this.B,
               this.L,
               this.v,
               this.O,
               5,
               oh.S(oh.j(-987684056, oh.i(this.U ? -24915 : rv.e, 0.45), Math.max(var9 * 0.55, var5 * 0.6 + var7 * 0.2)), this.f)
            );
            if (this.A && this.h) {
               ll.G(var1, this.B, this.L, this.v, this.O, oh.S(rv.e, this.f));
            }

            if (this.z != null) {
               this.z.accept(var1);
            } else if (!this.I.isEmpty()) {
               ou.u(var1, ou.e(this.I), this.B + (this.v - 14) / 2, this.L + (this.O - 14) / 2, 14, oh.S(this.x ? var11 : rv.r, this.f));
            } else {
               j_.n(var1, j_.x(this.m, this.v - 12), this.B + this.v / 2, this.L + (this.O - 9) / 2, oh.S(this.x ? var11 : rv.r, this.f));
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   @Override
   public boolean contains(double var1, double var3) {
      return super.contains(this.d.localX(var1, this.B + this.v / 2.0), this.d.localY(var3, this.L + this.O / 2.0));
   }

   public void R(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
      if (this.F) {
         boolean var6 = LarpClient.y().v().E().o;
         int var7 = !this.contains(var2, var3) && (!this.A || !this.h) ? 0 : 1;
         double var8 = var6 ? this.G.y(var7 != 0 ? 1.0 : 0.0, var7 != 0 ? 160 : 100) : (var7 != 0 ? 1.0 : 0.0);
         if (!(var8 < 0.01)) {
            String var10 = j_.x(this.m, var4 - 20);
            int var11 = j_.L(var10) + 14;
            int var12 = Math.clamp(this.B + this.v / 2 - var11 / 2, 4, Math.max(4, var4 - var11 - 4));
            int var13 = Math.clamp(this.L - 23 + (int)Math.round(this.d.offsetY() + (1.0 - var8) * 4.0), 4, Math.max(4, var5 - 24));
            ll.N(var1, var12, var13, var11, 19, 4, oh.S(-283567328, var8));
            j_.c(var1, var10, var12 + 7, var13 + 5, oh.S(rv.P, var8));
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.J = System.nanoTime() + 90000000L;
         this.r.run();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.F && this.x && this.A && (var1 == 257 || var1 == 32)) {
         this.J = System.nanoTime() + 90000000L;
         this.r.run();
         return true;
      } else {
         return false;
      }
   }
}
