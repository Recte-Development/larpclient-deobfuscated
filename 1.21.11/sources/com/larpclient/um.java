package com.larpclient;

import java.util.Objects;
import net.minecraft.class_2338;

public final class um {
   private class_2338 W;
   private Object L;
   private Object B;
   private int i;
   private int a;
   private int N;
   private boolean k;

   public class_2338 O() {
      return this.W;
   }

   public void W() {
      this.W = null;
      this.L = this.B = null;
      this.k = (boolean)0;
   }

   public void K(class_2338 var1, Object var2, Object var3, int var4) {
      if (!Objects.equals(this.W, var1) || this.L != var2 || this.B != var3 || var4 - this.a != 1) {
         this.W = var1 == null ? null : var1.method_10062();
         this.L = var2;
         this.B = var3;
         this.i = var4;
         this.k = (boolean)0;
      }

      this.a = var4;
   }

   public boolean H(int var1, int var2, boolean var3) {
      if (this.W == null || var1 < this.i) {
         return false;
      }

      if (!var3) {
         this.k = (boolean)0;
         return false;
      }

      if (!this.k) {
         this.k = (boolean)1;
         this.N = var1;
      }

      return var1 - this.i >= Math.max(2, var2) && var1 - this.N >= 1;
   }
}
