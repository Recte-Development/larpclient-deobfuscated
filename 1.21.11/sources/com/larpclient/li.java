package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.class_332;

public final class li extends lc {
   private final BooleanSupplier W;
   private final Runnable x;
   private final qb f;
   private final qb S = new qb(0.0);
   private final qb h = new qb(0.0);
   private long t;
   private ls c;

   public li(BooleanSupplier var1, Runnable var2) {
      this.W = var1;
      this.x = var2;
      this.f = new qb(var1.getAsBoolean() ? 1.0 : 0.0);
   }

   public void V(ls var1) {
      this.c = var1;
      this.bounds(var1.D + var1.d - 62, var1.g + (var1.w - 22) / 2, 22);
      this.w = 22;
      this.I = var1.I;
      this.N = var1.N;
   }

   public String P() {
      return this.W.getAsBoolean() ? "Remove from favorites" : "Add to favorites";
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      if (this.I) {
         double var4 = this.f.a(this.W.getAsBoolean() ? 1.0 : 0.0, 160);
         double var6 = this.S.a(!this.contains(var2, var3) && (!this.M || !this.q) ? 0.0 : 1.0, 160);
         double var8 = this.h.a(System.nanoTime() < this.t ? 1.0 : 0.0, 100);
         jx var10 = this.c == null ? jx.at(0.0, 0.0, ez.a) : this.c.C();
         float var11 = this.c == null ? 0.0F : this.c.P();
         var1.method_51448().pushMatrix();

         try {
            var1.method_51448().translate(0.0F, -var11);
            if (var6 > 0.001 || var8 > 0.001) {
               p.B(var1, this.D, this.g, this.d, this.w, ez.m, ei.D(var10.title(), var6 * 0.07 + var8 * 0.07));
            }

            int var12 = this.D + (this.d - 11) / 2;
            int var13 = this.g + (this.w - 11) / 2;
            int var14 = ei.W(var10.subtitle(), var10.title(), var6 * 0.5);
            lx.N(var1, lx.n("star"), var12, var13, 11, var14);
            if (var4 > 0.001 && jx.contrast(ez.a, var10.background()) < 3.0) {
               lx.N(var1, lx.n("star"), var12 - 1, var13 - 1, 13, var14);
            }

            if (var4 > 0.001) {
               lx.N(var1, lx.n("star-filled"), var12, var13, 11, ei.R(ez.a | 0xFF000000, var4));
            }

            if (this.M && this.q) {
               p.C(var1, this.D, this.g, this.d, this.w, 4);
            }
         } finally {
            var1.method_51448().popMatrix();
         }
      }
   }

   public void n(class_332 var1, int var2, int var3) {
      String var4 = uw.J(this.P(), var2 - 22);
      int var5 = uw.r(var4) + 14;
      int var6 = Math.clamp(this.D + this.d / 2 - var5 / 2, 4, Math.max(4, var2 - var5 - 4));
      int var7 = Math.clamp(this.g - 23, 4, Math.max(4, var3 - 23));
      p.B(var1, var6, var7, var5, 19, 4, ez.d);
      uw.G(var1, var4, var6 + 7, var7 + 5, ez.H);
   }

   private void P() {
      this.t = System.nanoTime() + 110000000L;
      this.x.run();
      c_.b(c_.d.q);
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.P();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.I && this.N && this.M && (var1 == 257 || var1 == 32)) {
         this.P();
         return true;
      } else {
         return false;
      }
   }
}
