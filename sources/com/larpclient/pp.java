package com.larpclient;

import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class pp extends oq {
   private final BooleanSupplier Z;
   private final Runnable C;
   private final u7 X;
   private final u7 G = new u7(0.0);
   private final u7 S = new u7(0.0);
   private long s;
   private p7 V;

   public pp(BooleanSupplier var1, Runnable var2) {
      this.Z = var1;
      this.C = var2;
      this.X = new u7(var1.getAsBoolean() ? 1.0 : 0.0);
   }

   public void j(p7 var1) {
      this.V = var1;
      this.bounds(var1.B + var1.v - 62, var1.L + (var1.O - 22) / 2, 22);
      this.O = 22;
      this.F = var1.F;
      this.x = var1.x;
   }

   public String C() {
      return this.Z.getAsBoolean() ? "Remove from favorites" : "Add to favorites";
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.F) {
         double var4 = this.X.y(this.Z.getAsBoolean() ? 1.0 : 0.0, 160);
         double var6 = this.G.y(!this.contains(var2, var3) && (!this.A || !this.h) ? 0.0 : 1.0, 160);
         double var8 = this.S.y(System.nanoTime() < this.s ? 1.0 : 0.0, 100);
         jd var10 = this.V == null ? jd.at(0.0, 0.0, rv.e) : this.V.Y();
         float var11 = this.V == null ? 0.0F : this.V.D();
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(0.0F, -var11);
            if (var6 > 0.001 || var8 > 0.001) {
               ll.N(var1, this.B, this.L, this.v, this.O, rv.g, oh.i(var10.title(), var6 * 0.07 + var8 * 0.07));
            }

            int var12 = this.B + (this.v - 11) / 2;
            int var13 = this.L + (this.O - 11) / 2;
            int var14 = oh.j(var10.subtitle(), var10.title(), var6 * 0.5);
            ou.u(var1, ou.e("star"), var12, var13, 11, var14);
            if (var4 > 0.001 && jd.contrast(rv.e, var10.background()) < 3.0) {
               ou.u(var1, ou.e("star"), var12 - 1, var13 - 1, 13, var14);
            }

            if (var4 > 0.001) {
               ou.u(var1, ou.e("star-filled"), var12, var13, 11, oh.S(rv.e | 0xFF000000, var4));
            }

            if (this.A && this.h) {
               ll.w(var1, this.B, this.L, this.v, this.O, 4);
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   public void A(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = j_.S(this.C(), var2 - 22);
      int var5 = j_.a(var4) + 14;
      int var6 = Math.clamp(this.B + this.v / 2 - var5 / 2, 4, Math.max(4, var2 - var5 - 4));
      int var7 = Math.clamp(this.L - 23, 4, Math.max(4, var3 - 23));
      ll.N(var1, var6, var7, var5, 19, 4, rv.w);
      j_.E(var1, var4, var6 + 7, var7 + 5, rv.P);
   }

   private void i() {
      this.s = System.nanoTime() + 110000000L;
      this.C.run();
      hx.w(hx.t.X);
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.i();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.F && this.x && this.A && (var1 == 257 || var1 == 32)) {
         this.i();
         return true;
      } else {
         return false;
      }
   }
}
