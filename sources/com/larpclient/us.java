package com.larpclient;

import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class us extends b {
   private final String E;
   private final BooleanSupplier S;
   private final boolean O;
   private final boolean d;

   public us(String var1) {
      this(var1, () -> true);
   }

   public us(String var1, BooleanSupplier var2) {
      this(var1, var2, false);
   }

   public us(String var1, BooleanSupplier var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   private us(String var1, BooleanSupplier var2, boolean var3, boolean var4) {
      this.E = var1;
      this.S = var2;
      this.O = var3;
      this.d = var4;
      this.W = var3 ? 16 : (var4 ? 26 : 22);
      this.X = (boolean)0;
   }

   public static us R(String var0) {
      return new us(var0, () -> true, false, true);
   }

   public boolean D() {
      return this.S.getAsBoolean();
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.D) {
         if (this.d) {
            k6.d var9 = k6.d.F;
            String var10 = k6.o(this.E.toUpperCase(Locale.ROOT), this.A - 48, var9);
            int var11 = k6.a(var10, var9);
            int var12 = this.q + (this.A - var11) / 2;
            int var8 = this.J + 11;
            k6.T(var1, var10, var12, var8 - var9.q() / 2, f.G, var9);
            c7.Q(var1, this.q + 4, var8, var12 - 8 - (this.q + 4), false, f.H);
            c7.Q(var1, var12 + var11 + 8, var8, this.q + this.A - 4 - (var12 + var11 + 8), false, f.H);
         } else if (this.O) {
            k6.T(var1, k6.o(this.E, this.A, k6.d.X), this.q, this.J + 3, f.J, k6.d.X);
         } else {
            k6.d var4 = k6.d.q;
            String var5 = k6.o(this.E.toUpperCase(Locale.ROOT), this.A - 8, var4);
            int var6 = this.J + this.W - var4.q() - 2;
            k6.T(var1, var5, this.q + 4, var6, f.G, var4);
            if (f.Y()) {
               int var7 = this.q + 4 + k6.a(var5, var4) + 6;
               c7.Q(var1, var7, var6 + var4.q() / 2, this.q + this.A - 4 - var7, false, f.H);
            }
         }
      }
   }
}
