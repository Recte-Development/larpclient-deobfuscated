package com.larpclient;

import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.class_332;

public final class l5 extends lc {
   private final String U;
   private final BooleanSupplier J;
   private final boolean p;
   private final boolean S;

   public l5(String var1) {
      this(var1, () -> true);
   }

   public l5(String var1, BooleanSupplier var2) {
      this(var1, var2, false);
   }

   public l5(String var1, BooleanSupplier var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   private l5(String var1, BooleanSupplier var2, boolean var3, boolean var4) {
      this.U = var1;
      this.J = var2;
      this.p = var3;
      this.S = var4;
      this.w = var3 ? 16 : (var4 ? 26 : 22);
      this.N = (boolean)0;
   }

   public static l5 q(String var0) {
      return new l5(var0, () -> true, false, true);
   }

   public boolean H() {
      return this.J.getAsBoolean();
   }

   @Override
   public void render(class_332 var1, int var2, int var3) {
      if (this.I) {
         if (this.S) {
            uw.c var9 = uw.c.t;
            String var10 = uw.X(this.U.toUpperCase(Locale.ROOT), this.d - 48, var9);
            int var11 = uw.C(var10, var9);
            int var12 = this.D + (this.d - var11) / 2;
            int var8 = this.g + 11;
            uw.X(var1, var10, var12, var8 - var9.b() / 2, ez.t, var9);
            gu.q(var1, this.D + 4, var8, var12 - 8 - (this.D + 4), false, ez.o);
            gu.q(var1, var12 + var11 + 8, var8, this.D + this.d - 4 - (var12 + var11 + 8), false, ez.o);
         } else if (this.p) {
            uw.X(var1, uw.X(this.U, this.d, uw.c.E), this.D, this.g + 3, ez.H, uw.c.E);
         } else {
            uw.c var4 = uw.c.H;
            String var5 = uw.X(this.U.toUpperCase(Locale.ROOT), this.d - 8, var4);
            int var6 = this.g + this.w - var4.b() - 2;
            uw.X(var1, var5, this.D + 4, var6, ez.t, var4);
            if (ez.d()) {
               int var7 = this.D + 4 + uw.C(var5, var4) + 6;
               gu.q(var1, var7, var6 + var4.b() / 2, this.D + this.d - 4 - var7, false, ez.o);
            }
         }
      }
   }
}
