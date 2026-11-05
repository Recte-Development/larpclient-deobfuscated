package com.larpclient;

import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class p8 extends oq {
   private final String C;
   private final BooleanSupplier W;
   private final boolean U;
   private final boolean q;

   public p8(String var1) {
      this(var1, () -> true);
   }

   public p8(String var1, BooleanSupplier var2) {
      this(var1, var2, false);
   }

   public p8(String var1, BooleanSupplier var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   private p8(String var1, BooleanSupplier var2, boolean var3, boolean var4) {
      this.C = var1;
      this.W = var2;
      this.U = var3;
      this.q = var4;
      this.O = var3 ? 16 : (var4 ? 26 : 22);
      this.x = (boolean)0;
   }

   public static p8 Y(String var0) {
      return new p8(var0, () -> true, false, true);
   }

   public boolean v() {
      return this.W.getAsBoolean();
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.F) {
         if (this.q) {
            j_.m var9 = j_.m.Q;
            String var10 = j_.d(this.C.toUpperCase(Locale.ROOT), this.v - 48, var9);
            int var11 = j_.o(var10, var9);
            int var12 = this.B + (this.v - var11) / 2;
            int var8 = this.L + 11;
            j_.L(var1, var10, var12, var8 - var9.C() / 2, rv.r, var9);
            fr.W(var1, this.B + 4, var8, var12 - 8 - (this.B + 4), false, rv.h);
            fr.W(var1, var12 + var11 + 8, var8, this.B + this.v - 4 - (var12 + var11 + 8), false, rv.h);
         } else if (this.U) {
            j_.L(var1, j_.d(this.C, this.v, j_.m.n), this.B, this.L + 3, rv.P, j_.m.n);
         } else {
            j_.m var4 = j_.m.u;
            String var5 = j_.d(this.C.toUpperCase(Locale.ROOT), this.v - 8, var4);
            int var6 = this.L + this.O - var4.C() - 2;
            j_.L(var1, var5, this.B + 4, var6, rv.r, var4);
            if (rv.u()) {
               int var7 = this.B + 4 + j_.o(var5, var4) + 6;
               fr.W(var1, var7, var6 + var4.C() / 2, this.B + this.v - 4 - var7, false, rv.h);
            }
         }
      }
   }
}
