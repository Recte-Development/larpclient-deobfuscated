package com.larpclient;

import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class j1 extends jd {
   private final String g;
   private final BooleanSupplier o;
   private final boolean M;
   private final boolean m;

   public j1(String var1) {
      this(var1, () -> true);
   }

   public j1(String var1, BooleanSupplier var2) {
      this(var1, var2, false);
   }

   public j1(String var1, BooleanSupplier var2, boolean var3) {
      this(var1, var2, var3, false);
   }

   private j1(String var1, BooleanSupplier var2, boolean var3, boolean var4) {
      this.g = var1;
      this.o = var2;
      this.M = var3;
      this.m = var4;
      this.J = var3 ? 16 : (var4 ? 26 : 22);
      this.p = (boolean)0;
   }

   public static j1 n(String var0) {
      return new j1(var0, () -> true, false, true);
   }

   public boolean t() {
      return this.o.getAsBoolean();
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.i) {
         if (this.m) {
            bj.u var9 = bj.u.I;
            String var10 = bj.i(this.g.toUpperCase(Locale.ROOT), this.A - 48, var9);
            int var11 = bj.e(var10, var9);
            int var12 = this.S + (this.A - var11) / 2;
            int var8 = this.u + 11;
            bj.k(var1, var10, var12, var8 - var9.n() / 2, ho.j, var9);
            kv.Y(var1, this.S + 4, var8, var12 - 8 - (this.S + 4), false, ho.Y);
            kv.Y(var1, var12 + var11 + 8, var8, this.S + this.A - 4 - (var12 + var11 + 8), false, ho.Y);
         } else if (this.M) {
            bj.k(var1, bj.i(this.g, this.A, bj.u.y), this.S, this.u + 3, ho.u, bj.u.y);
         } else {
            bj.u var4 = bj.u.m;
            String var5 = bj.i(this.g.toUpperCase(Locale.ROOT), this.A - 8, var4);
            int var6 = this.u + this.J - var4.n() - 2;
            bj.k(var1, var5, this.S + 4, var6, ho.j, var4);
            if (ho.S()) {
               int var7 = this.S + 4 + bj.e(var5, var4) + 6;
               kv.Y(var1, var7, var6 + var4.n() / 2, this.S + this.A - 4 - var7, false, ho.Y);
            }
         }
      }
   }
}
