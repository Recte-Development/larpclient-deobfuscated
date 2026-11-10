package com.larpclient.module;

import com.larpclient.w6;
import net.minecraft.class_1268;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_746;

public final class ax {
   private static final long J = 350L;
   private int c;
   private int o = 40;
   private int z = 35;
   private boolean v;
   private long E;
   private int V;
   private float X;
   private float k;

   public void G() {
      this.c++;
   }

   public void F(int var1) {
      this.o = Math.max(10, var1);
      if (this.z == 35 || this.z < 10) {
         this.U();
      }
   }

   private void U() {
      int var1 = Math.max(2, this.o / 5);
      this.z = this.o + (int)((Math.random() - 0.5) * 2.0 * var1);
   }

   public void X() {
      if (!this.v && this.c >= this.z) {
         this.v = (boolean)1;
         this.E = System.currentTimeMillis();
         this.c = 0;
         this.V = 0;
         this.U();
         float var1 = Math.random() < 0.5 ? -1.0F : 1.0F;
         float var2 = Math.random() < 0.5 ? -1.0F : 1.0F;
         this.X = var1 * (2.2F + (float)(Math.random() * 2.3));
         this.k = var2 * (1.2F + (float)(Math.random() * 1.8));
      }

      if (this.v) {
         long var5 = System.currentTimeMillis() - this.E;
         class_310 var3 = class_310.method_1551();
         class_746 var4 = var3 == null ? null : var3.field_1724;
         if (var4 != null && this.V == 0 && var5 >= 60L) {
            w6.M(var4, class_1268.field_5808);
            this.V++;
         }

         if (var5 >= 350L) {
            this.v = (boolean)0;
            this.X = 0.0F;
            this.k = 0.0F;
         }
      }
   }

   public l4 u(l4 var1) {
      if (var1 != null && this.v) {
         long var2 = System.currentTimeMillis() - this.E;
         float var4 = Math.min(1.0F, (float)var2 / 350.0F);
         float var5 = (float)Math.sin(var4 * Math.PI);
         return new l4(var1.j + this.X * var5, class_3532.method_15363(var1.l + this.k * var5, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public void o() {
      this.c = 0;
      this.v = (boolean)0;
      this.V = 0;
      this.X = 0.0F;
      this.k = 0.0F;
      this.U();
   }

   public boolean X() {
      return this.v;
   }
}
