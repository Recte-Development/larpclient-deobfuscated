package com.larpclient.module;

import com.larpclient.hp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;

public final class l9 {
   private static final long B = 350L;
   private int L;
   private int M = 40;
   private int N = 35;
   private boolean I;
   private long q;
   private int a;
   private float n;
   private float V;

   public void p() {
      this.L++;
   }

   public void x(int var1) {
      this.M = Math.max(10, var1);
      if (this.N == 35 || this.N < 10) {
         this.Q();
      }
   }

   private void Q() {
      int var1 = Math.max(2, this.M / 5);
      this.N = this.M + (int)((Math.random() - 0.5) * 2.0 * var1);
   }

   public void c() {
      if (!this.I && this.L >= this.N) {
         this.I = (boolean)1;
         this.q = System.currentTimeMillis();
         this.L = 0;
         this.a = 0;
         this.Q();
         float var1 = Math.random() < 0.5 ? -1.0F : 1.0F;
         float var2 = Math.random() < 0.5 ? -1.0F : 1.0F;
         this.n = var1 * (2.2F + (float)(Math.random() * 2.3));
         this.V = var2 * (1.2F + (float)(Math.random() * 1.8));
      }

      if (this.I) {
         long var5 = System.currentTimeMillis() - this.q;
         Minecraft var3 = Minecraft.getInstance();
         LocalPlayer var4 = var3 == null ? null : var3.player;
         if (var4 != null && this.a == 0 && var5 >= 60L) {
            hp.o(var4, InteractionHand.MAIN_HAND);
            this.a++;
         }

         if (var5 >= 350L) {
            this.I = (boolean)0;
            this.n = 0.0F;
            this.V = 0.0F;
         }
      }
   }

   public lx i(lx var1) {
      if (var1 != null && this.I) {
         long var2 = System.currentTimeMillis() - this.q;
         float var4 = Math.min(1.0F, (float)var2 / 350.0F);
         float var5 = (float)Math.sin(var4 * Math.PI);
         return new lx(var1.z + this.n * var5, Mth.clamp(var1.I + this.V * var5, -90.0F, 90.0F));
      } else {
         return var1;
      }
   }

   public void z() {
      this.L = 0;
      this.I = (boolean)0;
      this.a = 0;
      this.n = 0.0F;
      this.V = 0.0F;
      this.Q();
   }

   public boolean w() {
      return this.I;
   }
}
