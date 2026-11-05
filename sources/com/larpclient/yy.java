package com.larpclient;

import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class yy {
   public static boolean B(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, boolean var7) {
      return var0 && var1 && var2 && var3 && !var4 && !var5 && Float.isFinite(var6) && var6 > 0.9F && var7;
   }

   public static Input D(Input var0) {
      return new Input(false, var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), false);
   }

   public static Vec2 M(Input var0) {
      return new Vec2((var0.left() ? 1 : 0) - (var0.right() ? 1 : 0), (var0.forward() ? 1 : 0) - (var0.backward() ? 1 : 0)).normalized();
   }

   private yy() {
   }

   public static final class n {
      private int X;
      private int i = Integer.MIN_VALUE;
      private boolean d;

      public void O(int var1) {
         if (this.X == 0 && !this.d) {
            this.X = Math.clamp(var1, 1, 3);
         }
      }

      public boolean X(int var1, boolean var2) {
         if (!var2) {
            this.p();
            return false;
         }

         if (var1 != this.i) {
            this.i = var1;
            this.d = (boolean)(this.X > 0 ? 1 : 0);
            if (this.d) {
               this.X--;
            }
         }

         return this.d;
      }

      public boolean p(int var1) {
         return this.d && this.i == var1;
      }

      public boolean k() {
         return this.X > 0 || this.d;
      }

      public void p() {
         this.X = 0;
         this.d = (boolean)0;
         this.i = Integer.MIN_VALUE;
      }
   }

   public static final class r {
      private Object a;
      private Object F;
      private boolean l;
      private boolean t;

      public void C(Object var1, Object var2, boolean var3) {
         this.a = var1;
         this.F = var2;
         this.l = var3;
         this.t = (boolean)0;
      }

      public void E(Object var1, Object var2) {
         if (this.a == var1 && this.F == var2) {
            this.t = (boolean)1;
         }
      }

      public boolean K(Object var1, Object var2) {
         int var3 = this.l && this.t && this.a == var1 && this.F == var2 ? 1 : 0;
         this.u();
         return (boolean)var3;
      }

      public void u() {
         this.a = this.F = null;
         this.l = this.t = (boolean)0;
      }
   }
}
