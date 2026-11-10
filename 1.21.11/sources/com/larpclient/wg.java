package com.larpclient;

import net.minecraft.class_10185;
import net.minecraft.class_241;

public final class wg {
   public static boolean n(boolean var0, boolean var1, boolean var2, boolean var3, boolean var4, boolean var5, float var6, boolean var7) {
      return var0 && var1 && var2 && var3 && !var4 && !var5 && Float.isFinite(var6) && var6 > 0.9F && var7;
   }

   public static class_10185 g(class_10185 var0) {
      return new class_10185(false, var0.comp_3160(), var0.comp_3161(), var0.comp_3162(), var0.comp_3163(), var0.comp_3164(), false);
   }

   public static class_241 F(class_10185 var0) {
      return new class_241((var0.comp_3161() ? 1 : 0) - (var0.comp_3162() ? 1 : 0), (var0.comp_3159() ? 1 : 0) - (var0.comp_3160() ? 1 : 0)).method_35581();
   }

   private wg() {
   }

   public static final class h {
      private int A;
      private int Y = Integer.MIN_VALUE;
      private boolean N;

      public void O(int var1) {
         if (this.A == 0 && !this.N) {
            this.A = Math.clamp(var1, 1, 3);
         }
      }

      public boolean O(int var1, boolean var2) {
         if (!var2) {
            this.I();
            return false;
         }

         if (var1 != this.Y) {
            this.Y = var1;
            this.N = (boolean)(this.A > 0 ? 1 : 0);
            if (this.N) {
               this.A--;
            }
         }

         return this.N;
      }

      public boolean k(int var1) {
         return this.N && this.Y == var1;
      }

      public boolean c() {
         return this.A > 0 || this.N;
      }

      public void I() {
         this.A = 0;
         this.N = (boolean)0;
         this.Y = Integer.MIN_VALUE;
      }
   }

   public static final class q {
      private Object c;
      private Object P;
      private boolean T;
      private boolean V;

      public void X(Object var1, Object var2, boolean var3) {
         this.c = var1;
         this.P = var2;
         this.T = var3;
         this.V = (boolean)0;
      }

      public void D(Object var1, Object var2) {
         if (this.c == var1 && this.P == var2) {
            this.V = (boolean)1;
         }
      }

      public boolean e(Object var1, Object var2) {
         int var3 = this.T && this.V && this.c == var1 && this.P == var2 ? 1 : 0;
         this.Z();
         return (boolean)var3;
      }

      public void Z() {
         this.c = this.P = null;
         this.T = this.V = (boolean)0;
      }
   }
}
