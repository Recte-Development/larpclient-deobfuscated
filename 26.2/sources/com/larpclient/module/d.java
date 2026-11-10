package com.larpclient.module;

import com.larpclient.ks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;

public final class d {
   private static final long Y = 350L;
   private int O;
   private int K = 40;
   private int c = 35;
   private boolean J;
   private long B;
   private int G;
   private float z;
   private float r;

   public void L() {
      this.O++;
   }

   public void p(int var1) {
      this.K = Math.max(10, var1);
      if (this.c == 35 || this.c < 10) {
         this.N();
      }
   }

   private void N() {
      int var1 = Math.max(2, this.K / 5);
      this.c = this.K + (int)((Math.random() - 0.5) * 2.0 * var1);
   }

   public void h() {
      if (!this.J && this.O >= this.c) {
         this.J = (boolean)1;
         this.B = System.currentTimeMillis();
         this.O = 0;
         this.G = 0;
         this.N();
         float var1 = Math.random() < 0.5 ? -1.0F : 1.0F;
         float var2 = Math.random() < 0.5 ? -1.0F : 1.0F;
         this.z = var1 * (2.2F + (float)(Math.random() * 2.3));
         this.r = var2 * (1.2F + (float)(Math.random() * 1.8));
      }

      if (this.J) {
         long var5 = System.currentTimeMillis() - this.B;
         Minecraft var3 = Minecraft.getInstance();
         LocalPlayer var4 = var3 == null ? null : var3.player;
         if (var4 != null && this.G == 0 && var5 >= 60L) {
            ks.X(var4, InteractionHand.MAIN_HAND);
            this.G++;
         }

         if (var5 >= 350L) {
            this.J = (boolean)0;
            this.z = 0.0F;
            this.r = 0.0F;
         }
      }
   }

   public u u(u var1) {
      if (var1 != null && this.J) {
         long var2 = System.currentTimeMillis() - this.B;
         float var4 = Math.min(1.0F, (float)var2 / 350.0F);
         float var5 = (float)Math.sin(var4 * Math.PI);
         return new u(var1.R + this.z * var5, Mth.clamp(var1.x + this.r * var5, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public void g() {
      this.O = 0;
      this.J = (boolean)0;
      this.G = 0;
      this.z = 0.0F;
      this.r = 0.0F;
      this.N();
   }

   public boolean N() {
      return this.J;
   }
}
