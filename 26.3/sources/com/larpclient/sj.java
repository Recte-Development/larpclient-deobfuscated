package com.larpclient;

import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class sj extends j0 {
   private final String M;
   private final BooleanSupplier i;
   private final boolean s;
   private final boolean K;

   public sj(String var1) {
      this(var1, () -> true);
   }

   public sj(String var1, BooleanSupplier var2) {
      this(var1, var2, false);
   }

   public sj(String var1, BooleanSupplier var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   private sj(String var1, BooleanSupplier var2, boolean var3, boolean var4) {
      this.M = var1;
      this.i = var2;
      this.s = var3;
      this.K = var4;
      this.O = var3 ? 16 : (var4 ? 26 : 22);
      this.V = (boolean)0;
   }

   public static sj B(String var0) {
      return new sj(var0, () -> true, false, true);
   }

   public boolean t() {
      return this.i.getAsBoolean();
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.R) {
         if (this.K) {
            ic.n var9 = ic.n.E;
            String var10 = ic.o(this.M.toUpperCase(Locale.ROOT), this.L - 48, var9);
            int var11 = ic.f(var10, var9);
            int var12 = this.P + (this.L - var11) / 2;
            int var8 = this.T + 11;
            ic.N(var1, var10, var12, var8 - var9.T() / 2, c.f, var9);
            h5.T(var1, this.P + 4, var8, var12 - 8 - (this.P + 4), false, c.Xn);
            h5.T(var1, var12 + var11 + 8, var8, this.P + this.L - 4 - (var12 + var11 + 8), false, c.Xn);
         } else if (this.s) {
            ic.N(var1, ic.o(this.M, this.L, ic.n.w), this.P, this.T + 3, c.H, ic.n.w);
         } else {
            ic.n var4 = ic.n.L;
            String var5 = ic.o(this.M.toUpperCase(Locale.ROOT), this.L - 8, var4);
            int var6 = this.T + this.O - var4.T() - 2;
            ic.N(var1, var5, this.P + 4, var6, c.f, var4);
            if (c.J()) {
               int var7 = this.P + 4 + ic.f(var5, var4) + 6;
               h5.T(var1, var7, var6 + var4.T() / 2, this.P + this.L - 4 - var7, false, c.Xn);
            }
         }
      }
   }
}
