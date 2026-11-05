package com.larpclient;

import java.util.Objects;
import net.minecraft.core.BlockPos;

public final class ee {
   private BlockPos C;
   private Object M;
   private Object a;
   private int o;
   private int K;
   private int R;
   private boolean U;

   public BlockPos K() {
      return this.C;
   }

   public void v() {
      this.C = null;
      this.M = this.a = null;
      this.U = (boolean)0;
   }

   public void Y(BlockPos var1, Object var2, Object var3, int var4) {
      if (!Objects.equals(this.C, var1) || this.M != var2 || this.a != var3 || var4 - this.K != 1) {
         this.C = var1 == null ? null : var1.immutable();
         this.M = var2;
         this.a = var3;
         this.o = var4;
         this.U = (boolean)0;
      }

      this.K = var4;
   }

   public boolean a(int var1, int var2, boolean var3) {
      if (this.C == null || var1 < this.o) {
         return false;
      }

      if (!var3) {
         this.U = (boolean)0;
         return false;
      }

      if (!this.U) {
         this.U = (boolean)1;
         this.R = var1;
      }

      return var1 - this.o >= Math.max(2, var2) && var1 - this.R >= 1;
   }
}
