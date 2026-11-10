package com.larpclient;

import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class a {
   public static boolean Z(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, boolean var7) {
      return var0 && var1 && var2 && var3 && !var4 && !var5 && Float.isFinite(var6) && var6 > 0.9F && var7;
   }

   public static Input y(Input var0) {
      return new Input(false, var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), false);
   }

   public static Vec2 G(Input var0) {
      return new Vec2((var0.left() ? 1 : 0) - (var0.right() ? 1 : 0), (var0.forward() ? 1 : 0) - (var0.backward() ? 1 : 0)).normalized();
   }

   private a() {
   }

   public static final class b {
      private Object T;
      private Object U;
      private boolean Y;
      private boolean W;

      public void F(Object var1, Object var2, boolean var3) {
         this.T = var1;
         this.U = var2;
         this.Y = var3;
         this.W = (boolean)0;
      }

      public void F(Object var1, Object var2) {
         if (this.T == var1 && this.U == var2) {
            this.W = (boolean)1;
         }
      }

      public boolean u(Object var1, Object var2) {
         int var3 = this.Y && this.W && this.T == var1 && this.U == var2 ? 1 : 0;
         this.a();
         return (boolean)var3;
      }

      public void a() {
         this.T = this.U = null;
         this.Y = this.W = (boolean)0;
      }
   }

   public static final class u {
      private int T;
      private int q = Integer.MIN_VALUE;
      private boolean I;

      public void v(int var1) {
         if (this.T == 0 && !this.I) {
            this.T = Math.clamp(var1, 1, 3);
         }
      }

      public boolean l(int var1, boolean var2) {
         if (!var2) {
            this.l();
            return false;
         }

         if (var1 != this.q) {
            this.q = var1;
            this.I = (boolean)(this.T > 0 ? 1 : 0);
            if (this.I) {
               this.T--;
            }
         }

         return this.I;
      }

      public boolean X(int var1) {
         return this.I && this.q == var1;
      }

      public boolean u() {
         return this.T > 0 || this.I;
      }

      public void l() {
         this.T = 0;
         this.I = (boolean)0;
         this.q = Integer.MIN_VALUE;
      }
   }
}
