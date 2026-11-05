package com.larpclient.module;

import com.larpclient._8;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;

public final class dd {
   private static final long Y = 350L;
   private int L;
   private int e = 40;
   private int d = 35;
   private boolean D;
   private long X;
   private int i;
   private float r;
   private float g;

   public void Y() {
      this.L++;
   }

   public void I(int var1) {
      this.e = Math.max(10, var1);
      if (this.d == 35 || this.d < 10) {
         this.Q();
      }
   }

   private void Q() {
      int var1 = Math.max(2, this.e / 5);
      this.d = this.e + (int)((Math.random() - 0.5) * 2.0 * var1);
   }

   public void F() {
      if (!this.D && this.L >= this.d) {
         this.D = (boolean)1;
         this.X = System.currentTimeMillis();
         this.L = 0;
         this.i = 0;
         this.Q();
         float var1 = Math.random() < 0.5 ? -1.0F : 1.0F;
         float var2 = Math.random() < 0.5 ? -1.0F : 1.0F;
         this.r = var1 * (2.2F + (float)(Math.random() * 2.3));
         this.g = var2 * (1.2F + (float)(Math.random() * 1.8));
      }

      if (this.D) {
         long var5 = System.currentTimeMillis() - this.X;
         Minecraft var3 = Minecraft.getInstance();
         LocalPlayer var4 = var3 == null ? null : var3.player;
         if (var4 != null && this.i == 0 && var5 >= 60L) {
            _8.f(var4, InteractionHand.MAIN_HAND);
            this.i++;
         }

         if (var5 >= 350L) {
            this.D = (boolean)0;
            this.r = 0.0F;
            this.g = 0.0F;
         }
      }
   }

   public d5 G(d5 var1) {
      if (var1 != null && this.D) {
         long var2 = System.currentTimeMillis() - this.X;
         float var4 = Math.min(1.0F, (float)var2 / 350.0F);
         float var5 = (float)Math.sin(var4 * Math.PI);
         return new d5(var1.M + this.r * var5, Mth.clamp(var1.G + this.g * var5, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public void i() {
      this.L = 0;
      this.D = (boolean)0;
      this.i = 0;
      this.r = 0.0F;
      this.g = 0.0F;
      this.Q();
   }

   public boolean E() {
      return this.D;
   }
}
