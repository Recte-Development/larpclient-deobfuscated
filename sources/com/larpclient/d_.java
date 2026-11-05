package com.larpclient;

import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class d_ {
   public static boolean Q(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, boolean var7) {
      return var0 && var1 && var2 && var3 && !var4 && !var5 && Float.isFinite(var6) && var6 > 0.9F && var7;
   }

   public static Input r(Input var0) {
      return new Input(false, var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), false);
   }

   public static Vec2 Y(Input var0) {
      return new Vec2((var0.left() ? 1 : 0) - (var0.right() ? 1 : 0), (var0.forward() ? 1 : 0) - (var0.backward() ? 1 : 0)).normalized();
   }

   private d_() {
   }

   public static final class k {
      private int T;
      private int n = Integer.MIN_VALUE;
      private boolean j;

      public void l(int var1) {
         if (this.T == 0 && !this.j) {
            this.T = Math.clamp(var1, 1, 3);
         }
      }

      public boolean b(int var1, boolean var2) {
         if (!var2) {
            this.i();
            return false;
         }

         if (var1 != this.n) {
            this.n = var1;
            this.j = (boolean)(this.T > 0 ? 1 : 0);
            if (this.j) {
               this.T--;
            }
         }

         return this.j;
      }

      public boolean L(int var1) {
         return this.j && this.n == var1;
      }

      public boolean F() {
         return this.T > 0 || this.j;
      }

      public void i() {
         this.T = 0;
         this.j = (boolean)0;
         this.n = Integer.MIN_VALUE;
      }
   }

   public static final class l {
      private Object i;
      private Object H;
      private boolean P;
      private boolean R;

      public void l(Object var1, Object var2, boolean var3) {
         this.i = var1;
         this.H = var2;
         this.P = var3;
         this.R = (boolean)0;
      }

      public void y(Object var1, Object var2) {
         if (this.i == var1 && this.H == var2) {
            this.R = (boolean)1;
         }
      }

      public boolean U(Object var1, Object var2) {
         int var3 = this.P && this.R && this.i == var1 && this.H == var2 ? 1 : 0;
         this.X();
         return (boolean)var3;
      }

      public void X() {
         this.i = this.H = null;
         this.P = this.R = (boolean)0;
      }
   }
}
