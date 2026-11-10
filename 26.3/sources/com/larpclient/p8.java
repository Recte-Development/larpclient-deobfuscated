package com.larpclient;

import java.util.Objects;
import net.minecraft.core.BlockPos;

public final class p8 {
   private BlockPos r;
   private Object a;
   private Object x;
   private int i;
   private int M;
   private int n;
   private boolean b;

   public BlockPos e() {
      return this.r;
   }

   public void J() {
      this.r = null;
      this.a = this.x = null;
      this.b = (boolean)0;
   }

   public void q(BlockPos var1, Object var2, Object var3, int var4) {
      if (!Objects.equals(this.r, var1) || this.a != var2 || this.x != var3 || var4 - this.M != 1) {
         this.r = var1 == null ? null : var1.immutable();
         this.a = var2;
         this.x = var3;
         this.i = var4;
         this.b = (boolean)0;
      }

      this.M = var4;
   }

   public boolean m(int var1, int var2, boolean var3) {
      if (this.r == null || var1 < this.i) {
         return false;
      }

      if (!var3) {
         this.b = (boolean)0;
         return false;
      }

      if (!this.b) {
         this.b = (boolean)1;
         this.n = var1;
      }

      return var1 - this.i >= Math.max(2, var2) && var1 - this.n >= 1;
   }
}
