package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.class_332;

public final class lv extends lc {
   public final String B;
   public final String z;
   private final Runnable A;
   private final qb h = new qb(0.0);
   private final qb p = new qb(0.0);
   private final qb u = new qb(0.0);
   private final qb j = new qb(0.0);
   private ix.g i = new ix.g(1.0, 0.0);
   public boolean y;
   public double R;
   public Consumer<class_332> J;
   public boolean Y;
   public boolean f;
   public double S = 1.0;
   private long Z;

   public lv(String var1, String var2, Runnable var3) {
      this.B = var1;
      this.z = var2;
      this.A = var3;
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      if (this.I) {
         boolean var4 = LarpClient.b().e().F().k;
         double var5 = var4
            ? this.h.a(!this.contains(var2, var3) && (!this.M || !this.q) ? 0.0 : 1.0, 140)
            : (!this.contains(var2, var3) && (!this.M || !this.q) ? 0.0 : 1.0);
         double var7 = var4 ? this.p.a(System.nanoTime() < this.Z ? 1.0 : 0.0, 90) : 0.0;
         double var9 = var4 ? this.u.a(this.Y ? 1.0 : 0.0, 220) : (this.Y ? 1.0 : 0.0);
         this.i = ix.E(var5, var7, this.R, var4, this.y);
         int var11 = this.f ? -24915 : ez.H;
         var1.method_51448().pushMatrix();

         try {
            var1.method_51448().translate(this.D + this.d / 2.0F, (float)(this.g + this.w / 2.0 + this.i.offsetY()));
            var1.method_51448().scale((float)this.i.scale(), (float)this.i.scale());
            var1.method_51448().translate(-this.D - this.d / 2.0F, -this.g - this.w / 2.0F);
            com.larpclient.p.B(
               var1,
               this.D,
               this.g,
               this.d,
               this.w,
               5,
               ei.R(ei.W(-987684056, ei.D(this.f ? -24915 : ez.a, 0.45), Math.max(var9 * 0.55, var5 * 0.6 + var7 * 0.2)), this.S)
            );
            if (this.M && this.q) {
               com.larpclient.p.l(var1, this.D, this.g, this.d, this.w, ei.R(ez.a, this.S));
            }

            if (this.J != null) {
               this.J.accept(var1);
            } else if (!this.z.isEmpty()) {
               lx.N(var1, lx.n(this.z), this.D + (this.d - 14) / 2, this.g + (this.w - 14) / 2, 14, ei.R(this.N ? var11 : ez.t, this.S));
            } else {
               uw.N(var1, uw.R(this.B, this.d - 12), this.D + this.d / 2, this.g + (this.w - 9) / 2, ei.R(this.N ? var11 : ez.t, this.S));
            }
         } finally {
            var1.method_51448().popMatrix();
         }
      }
   }

   @Override
   public boolean contains(double var1, double var3) {
      return super.contains(this.i.localX(var1, this.D + this.d / 2.0), this.i.localY(var3, this.g + this.w / 2.0));
   }

   public void N(class_332 var1, int var2, int var3, int var4, int var5) {
      if (this.I) {
         boolean var6 = LarpClient.b().e().F().k;
         int var7 = !this.contains(var2, var3) && (!this.M || !this.q) ? 0 : 1;
         double var8 = var6 ? this.j.a(var7 != 0 ? 1.0 : 0.0, var7 != 0 ? 160 : 100) : (var7 != 0 ? 1.0 : 0.0);
         if (!(var8 < 0.01)) {
            String var10 = uw.R(this.B, var4 - 20);
            int var11 = uw.i(var10) + 14;
            int var12 = Math.clamp(this.D + this.d / 2 - var11 / 2, 4, Math.max(4, var4 - var11 - 4));
            int var13 = Math.clamp(this.g - 23 + (int)Math.round(this.i.offsetY() + (1.0 - var8) * 4.0), 4, Math.max(4, var5 - 24));
            com.larpclient.p.B(var1, var12, var13, var11, 19, 4, ei.R(-283567328, var8));
            uw.z(var1, var10, var12 + 7, var13 + 5, ei.R(ez.H, var8));
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.Z = System.nanoTime() + 90000000L;
         this.A.run();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.I && this.N && this.M && (var1 == 257 || var1 == 32)) {
         this.Z = System.nanoTime() + 90000000L;
         this.A.run();
         return true;
      } else {
         return false;
      }
   }
}
