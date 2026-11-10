package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class j5 extends jd {
   public final String b;
   public final String O;
   private final Runnable z;
   private final jp a = new jp(0.0);
   private final jp t = new jp(0.0);
   private final jp w = new jp(0.0);
   private final jp q = new jp(0.0);
   private mb.d L = new mb.d(1.0, 0.0);
   public boolean I;
   public double d;
   public Consumer<GuiGraphicsExtractor> c;
   public boolean v;
   public boolean j;
   public double G = 1.0;
   private long C;

   public j5(String var1, String var2, Runnable var3) {
      this.b = var1;
      this.O = var2;
      this.z = var3;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.i) {
         boolean var4 = LarpClient.R().o().g().L;
         double var5 = var4
            ? this.a.k(!this.contains(var2, var3) && (!this.T || !this.W) ? 0.0 : 1.0, 140)
            : (!this.contains(var2, var3) && (!this.T || !this.W) ? 0.0 : 1.0);
         double var7 = var4 ? this.t.k(System.nanoTime() < this.C ? 1.0 : 0.0, 90) : 0.0;
         double var9 = var4 ? this.w.k(this.v ? 1.0 : 0.0, 220) : (this.v ? 1.0 : 0.0);
         this.L = mb.L(var5, var7, this.d, var4, this.I);
         int var11 = this.j ? -24915 : ho.u;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(this.S + this.A / 2.0F, (float)(this.u + this.J / 2.0 + this.L.offsetY()));
            var1.pose().scale((float)this.L.scale(), (float)this.L.scale());
            var1.pose().translate(-this.S - this.A / 2.0F, -this.u - this.J / 2.0F);
            wp.I(
               var1,
               this.S,
               this.u,
               this.A,
               this.J,
               5,
               uh.F(uh.q(-987684056, uh.H(this.j ? -24915 : ho.J, 0.45), Math.max(var9 * 0.55, var5 * 0.6 + var7 * 0.2)), this.G)
            );
            if (this.T && this.W) {
               wp.L(var1, this.S, this.u, this.A, this.J, uh.F(ho.J, this.G));
            }

            if (this.c != null) {
               this.c.accept(var1);
            } else if (!this.O.isEmpty()) {
               ur.O(var1, ur.V(this.O), this.S + (this.A - 14) / 2, this.u + (this.J - 14) / 2, 14, uh.F(this.p ? var11 : ho.j, this.G));
            } else {
               bj.n(var1, bj.G(this.b, this.A - 12), this.S + this.A / 2, this.u + (this.J - 9) / 2, uh.F(this.p ? var11 : ho.j, this.G));
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   @Override
   public boolean contains(double var1, double var3) {
      return super.contains(this.L.localX(var1, this.S + this.A / 2.0), this.L.localY(var3, this.u + this.J / 2.0));
   }

   public void A(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5) {
      if (this.i) {
         boolean var6 = LarpClient.R().o().g().L;
         int var7 = !this.contains(var2, var3) && (!this.T || !this.W) ? 0 : 1;
         double var8 = var6 ? this.q.k(var7 != 0 ? 1.0 : 0.0, var7 != 0 ? 160 : 100) : (var7 != 0 ? 1.0 : 0.0);
         if (!(var8 < 0.01)) {
            String var10 = bj.G(this.b, var4 - 20);
            int var11 = bj.q(var10) + 14;
            int var12 = Math.clamp(this.S + this.A / 2 - var11 / 2, 4, Math.max(4, var4 - var11 - 4));
            int var13 = Math.clamp(this.u - 23 + (int)Math.round(this.L.offsetY() + (1.0 - var8) * 4.0), 4, Math.max(4, var5 - 24));
            wp.I(var1, var12, var13, var11, 19, 4, uh.F(-283567328, var8));
            bj.h(var1, var10, var12 + 7, var13 + 5, uh.F(ho.u, var8));
         }
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (this.contains(var1, var3) && var5 == 0) {
         this.C = System.nanoTime() + 90000000L;
         this.z.run();
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean key(int var1) {
      if (this.i && this.p && this.T && (var1 == 257 || var1 == 32)) {
         this.C = System.nanoTime() + 90000000L;
         this.z.run();
         return true;
      } else {
         return false;
      }
   }
}
