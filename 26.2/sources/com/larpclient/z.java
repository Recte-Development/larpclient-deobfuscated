package com.larpclient;

import java.util.Objects;
import net.minecraft.core.BlockPos;

public final class z {
   private BlockPos O;
   private Object P;
   private Object J;
   private int S;
   private int A;
   private int Z;
   private boolean F;

   public BlockPos b() {
      return this.O;
   }

   public void T() {
      this.O = null;
      this.P = this.J = null;
      this.F = (boolean)0;
   }

   public void Z(BlockPos var1, Object var2, Object var3, int var4) {
      if (!Objects.equals(this.O, var1) || this.P != var2 || this.J != var3 || var4 - this.A != 1) {
         this.O = var1 == null ? null : var1.immutable();
         this.P = var2;
         this.J = var3;
         this.S = var4;
         this.F = (boolean)0;
      }

      this.A = var4;
   }

   public boolean W(int var1, int var2, boolean var3) {
      if (this.O == null || var1 < this.S) {
         return false;
      }

      if (!var3) {
         this.F = (boolean)0;
         return false;
      }

      if (!this.F) {
         this.F = (boolean)1;
         this.Z = var1;
      }

      return var1 - this.S >= Math.max(2, var2) && var1 - this.Z >= 1;
   }
}
