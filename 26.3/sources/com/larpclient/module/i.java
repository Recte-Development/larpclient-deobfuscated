package com.larpclient.module;

import com.larpclient.h8;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;

public final class i {
   private static final long i = 350L;
   private int G;
   private int P = 40;
   private int c = 35;
   private boolean T;
   private long b;
   private int j;
   private float E;
   private float R;

   public void e() {
      this.G++;
   }

   public void G(int var1) {
      this.P = Math.max(10, var1);
      if (this.c == 35 || this.c < 10) {
         this.M();
      }
   }

   private void M() {
      int var1 = Math.max(2, this.P / 5);
      this.c = this.P + (int)((Math.random() - 0.5) * 2.0 * var1);
   }

   public void x() {
      if (!this.T && this.G >= this.c) {
         this.T = (boolean)1;
         this.b = System.currentTimeMillis();
         this.G = 0;
         this.j = 0;
         this.M();
         float var1 = Math.random() < 0.5 ? -1.0F : 1.0F;
         float var2 = Math.random() < 0.5 ? -1.0F : 1.0F;
         this.E = var1 * (2.2F + (float)(Math.random() * 2.3));
         this.R = var2 * (1.2F + (float)(Math.random() * 1.8));
      }

      if (this.T) {
         long var5 = System.currentTimeMillis() - this.b;
         Minecraft var3 = Minecraft.getInstance();
         LocalPlayer var4 = var3 == null ? null : var3.player;
         if (var4 != null && this.j == 0 && var5 >= 60L) {
            h8.Q(var4, InteractionHand.MAIN_HAND);
            this.j++;
         }

         if (var5 >= 350L) {
            this.T = (boolean)0;
            this.E = 0.0F;
            this.R = 0.0F;
         }
      }
   }

   public c W(c var1) {
      if (var1 != null && this.T) {
         long var2 = System.currentTimeMillis() - this.b;
         float var4 = Math.min(1.0F, (float)var2 / 350.0F);
         float var5 = (float)Math.sin(var4 * Math.PI);
         return new c(var1.V + this.E * var5, Mth.clamp(var1.T + this.R * var5, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public void c() {
      this.G = 0;
      this.T = (boolean)0;
      this.j = 0;
      this.E = 0.0F;
      this.R = 0.0F;
      this.M();
   }

   public boolean R() {
      return this.T;
   }
}
