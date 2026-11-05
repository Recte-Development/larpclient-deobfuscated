package com.larpclient;

import java.util.Objects;
import net.minecraft.core.BlockPos;

public final class ds {
   private BlockPos l;
   private Object C;
   private Object R;
   private int y;
   private int Z;
   private int L;
   private boolean c;

   public BlockPos X() {
      return this.l;
   }

   public void o() {
      this.l = null;
      this.C = this.R = null;
      this.c = (boolean)0;
   }

   public void S(BlockPos var1, Object var2, Object var3, int var4) {
      if (!Objects.equals(this.l, var1) || this.C != var2 || this.R != var3 || var4 - this.Z != 1) {
         this.l = var1 == null ? null : var1.immutable();
         this.C = var2;
         this.R = var3;
         this.y = var4;
         this.c = (boolean)0;
      }

      this.Z = var4;
   }

   public boolean R(int var1, int var2, boolean var3) {
      if (this.l == null || var1 < this.y) {
         return false;
      }

      if (!var3) {
         this.c = (boolean)0;
         return false;
      }

      if (!this.c) {
         this.c = (boolean)1;
         this.L = var1;
      }

      return var1 - this.y >= Math.max(2, var2) && var1 - this.L >= 1;
   }
}
