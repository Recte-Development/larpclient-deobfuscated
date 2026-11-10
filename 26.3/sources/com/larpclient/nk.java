package com.larpclient;

import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class nk {
   public static boolean k(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, boolean var7) {
      return var0 && var1 && var2 && var3 && !var4 && !var5 && Float.isFinite(var6) && var6 > 0.9F && var7;
   }

   public static Input S(Input var0) {
      return new Input(false, var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), false);
   }

   public static Vec2 K(Input var0) {
      return new Vec2((var0.left() ? 1 : 0) - (var0.right() ? 1 : 0), (var0.forward() ? 1 : 0) - (var0.backward() ? 1 : 0)).normalized();
   }

   private nk() {
   }

   public static final class b {
      private int V;
      private int c = Integer.MIN_VALUE;
      private boolean Z;

      public void R(int var1) {
         if (this.V == 0 && !this.Z) {
            this.V = Math.clamp(var1, 1, 3);
         }
      }

      public boolean H(int var1, boolean var2) {
         if (!var2) {
            this.J();
            return false;
         }

         if (var1 != this.c) {
            this.c = var1;
            this.Z = (boolean)(this.V > 0 ? 1 : 0);
            if (this.Z) {
               this.V--;
            }
         }

         return this.Z;
      }

      public boolean T(int var1) {
         return this.Z && this.c == var1;
      }

      public boolean w() {
         return this.V > 0 || this.Z;
      }

      public void J() {
         this.V = 0;
         this.Z = (boolean)0;
         this.c = Integer.MIN_VALUE;
      }
   }

   public static final class l {
      private Object T;
      private Object i;
      private boolean u;
      private boolean J;

      public void d(Object var1, Object var2, boolean var3) {
         this.T = var1;
         this.i = var2;
         this.u = var3;
         this.J = (boolean)0;
      }

      public void V(Object var1, Object var2) {
         if (this.T == var1 && this.i == var2) {
            this.J = (boolean)1;
         }
      }

      public boolean t(Object var1, Object var2) {
         int var3 = this.u && this.J && this.T == var1 && this.i == var2 ? 1 : 0;
         this.Q();
         return (boolean)var3;
      }

      public void Q() {
         this.T = this.i = null;
         this.u = this.J = (boolean)0;
      }
   }
}
