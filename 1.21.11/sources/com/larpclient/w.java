package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.NoFallModule;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.class_1268;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_243;
import net.minecraft.class_2848;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_746;
import net.minecraft.class_9284;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2848.class_2849;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class w {
   public static final double v = 4.0;
   private final Module h;
   private class_304 E;
   private boolean i;
   private boolean u;
   private int z = Integer.MIN_VALUE;
   private w.k W = w.k.h;
   private int R;
   private boolean P;
   private static Module c;
   private static Module A;

   public w(Module var1) {
      this.h = var1;
   }

   public w n(boolean var1) {
      this.P = var1;
      return this;
   }

   public boolean z(int var1) {
      class_746 var2 = class_310.method_1551().field_1724;
      if (var2 == null) {
         return false;
      }

      w.f var3 = S(var2, var1).rocket();
      if (var3 == null) {
         return false;
      }

      if (var3.hand() == class_1268.field_5810 || var3.slot() == var2.method_31548().method_67532()) {
         return true;
      }

      if (!fs.o(this.h)) {
         return false;
      }

      var2.method_31548().method_61496(var3.slot());
      return true;
   }

   public static void y(Module var0) {
      c = var0;
   }

   public static void l(Module var0) {
      if (c == var0) {
         c = null;
      }
   }

   public static boolean o(Module var0) {
      Module var1 = c;
      return var1 != null && var1 != var0 && var1.isRuntimeActive();
   }

   public static void h(Module var0) {
      A = var0;
   }

   public static void N(Module var0) {
      if (A == var0) {
         A = null;
      }
   }

   public static boolean u() {
      Module var0 = A;
      return var0 != null && var0.isRuntimeActive();
   }

   public static boolean P(class_1799 var0) {
      if (var0 != null && var0.method_31574(class_1802.field_8639)) {
         class_9284 var1 = (class_9284)var0.method_58694(class_9334.field_49616);
         return var1 == null || var1.comp_2392().isEmpty();
      } else {
         return false;
      }
   }

   public static int E(class_1799 var0) {
      class_9284 var1 = var0 == null ? null : (class_9284)var0.method_58694(class_9334.field_49616);
      return var1 == null ? 0 : var1.comp_2391();
   }

   public static w.t F(class_1799 var0, List<class_1799> var1, int var2, int var3) {
      int var4 = var0 != null && var0.method_31574(class_1802.field_8639) && !P(var0) ? 1 : 0;
      if (P(var0)) {
         return new w.t(new w.f(class_1268.field_5810, -1, E(var0)), false);
      }

      int var5 = -1;
      int var6 = Integer.MIN_VALUE;

      for (int var7 = 0; var7 < var1.size() && var7 < 9; var7++) {
         class_1799 var8 = (class_1799)var1.get(var7);
         if (var8 != null && var8.method_31574(class_1802.field_8639) && !P(var8)) {
            var4 = 1;
         }

         if (P(var8)) {
            int var9 = E(var8);
            int var10 = (var3 > 0 ? (var9 == var3 ? 1000 : -Math.abs(var9 - var3)) : var9) * 10 + (var7 == var2 ? 1 : 0);
            if (var10 > var6) {
               var6 = var10;
               var5 = var7;
            }
         }
      }

      return var5 < 0 ? new w.t(null, (boolean)var4) : new w.t(new w.f(class_1268.field_5808, var5, E((class_1799)var1.get(var5))), false);
   }

   public static w.t S(class_746 var0, int var1) {
      class_1661 var2 = var0.method_31548();
      ArrayList var3 = new ArrayList(9);

      for (int var4 = 0; var4 < 9; var4++) {
         var3.add(var2.method_5438(var4));
      }

      return F(var0.method_6079(), var3, var2.method_67532(), var1);
   }

   public static int A(class_746 var0) {
      int var1 = P(var0.method_6079()) ? var0.method_6079().method_7947() : 0;

      for (int var2 = 0; var2 < 9; var2++) {
         class_1799 var3 = var0.method_31548().method_5438(var2);
         if (P(var3)) {
            var1 += var3.method_7947();
         }
      }

      return var1;
   }

   public static boolean P(class_746 var0, double var1) {
      if (var1 <= 0.0) {
         return false;
      }

      class_243 var3 = var0.method_33571();
      class_243 var4 = var3.method_1019(var0.method_5720().method_1021(var1));
      class_3965 var5 = var0.method_73183().method_17742(new class_3959(var3, var4, class_3960.field_17558, class_242.field_1348, var0));
      return var5.method_17783() == class_240.field_1332;
   }

   public boolean N() {
      class_746 var1 = class_310.method_1551().field_1724;
      return var1 != null && S(var1, 0).rocket() != null;
   }

   public void q(BooleanSupplier var1, double var2, int var4, Runnable var5) {
      cr.y(this.h, () -> this.Y(var2, var4, var5), var1);
   }

   public w.k Y(double var1, int var3, Runnable var4) {
      class_310 var5 = class_310.method_1551();
      class_746 var6 = var5.field_1724;
      if (var6 != null && var5.field_1761 != null && cr.o(this.h) && !var6.method_6115()) {
         w.t var7 = S(var6, var3);
         w.f var8 = var7.rocket();
         if (var8 == null) {
            return this.S(var7.starsOnly() ? w.k.K : w.k.M);
         }

         if (P(var6, var1)) {
            return this.S(w.k.g);
         }

         class_1661 var9 = var6.method_31548();
         int var10 = var9.method_67532();
         int var11 = var8.hand() == class_1268.field_5808 && var8.slot() != var10 ? 1 : 0;
         if (var11 != 0 && !fs.o(this.h)) {
            return this.S(w.k.u);
         }

         if (var11 != 0) {
            var9.method_61496(var8.slot());
         }

         try {
            if (var5.field_1761.method_2919(var6, var8.hand()).method_23665()) {
               w6.H(var6, var8.hand());
               this.R++;
               var4.run();
            }
         } finally {
            if (var11 != 0 && !this.P && var9.method_67532() == var8.slot()) {
               var9.method_61496(var10);
            }
         }

         if (this.W != w.k.h) {
            q8.v("ElytraControl", "{}: rockets usable again", this.h.getName());
         }

         this.W = w.k.h;
         return w.k.h;
      } else {
         return this.S(w.k.r);
      }
   }

   private w.k S(w.k var1) {
      if (var1 != this.W && var1 != w.k.r) {
         q8.v("ElytraControl", "{}: no boost, {}", this.h.getName(), var1.o);
      }

      this.W = var1;
      return var1;
   }

   public w.k I() {
      return this.W;
   }

   public int E() {
      return this.R;
   }

   public void x() {
      this.l(true);
   }

   public void w() {
      this.l(false);
   }

   public boolean z() {
      class_746 var1 = class_310.method_1551().field_1724;
      return this.u && this.i && var1 != null && var1.field_6012 == this.z;
   }

   public void H() {
      class_746 var1 = class_310.method_1551().field_1724;
      if (this.u && (var1 == null || var1.field_6012 != this.z)) {
         this.L();
      }
   }

   private void l(boolean var1) {
      class_310 var2 = class_310.method_1551();
      this.E = var2.field_1690.field_1903;
      this.E.method_23481(var1);
      this.i = var1;
      this.u = (boolean)1;
      this.z = var2.field_1724 == null ? Integer.MIN_VALUE : var2.field_1724.field_6012;
   }

   public void L() {
      if (this.u && this.E != null && this.E.method_1434() == this.i) {
         this.E.method_23481(j.W(this.E));
      }

      this.u = (boolean)0;
      this.E = null;
   }

   public boolean X() {
      class_746 var1 = class_310.method_1551().field_1724;
      if (var1 != null && var1.method_23668()) {
         class_2848 var2 = new class_2848(var1, class_2849.field_12982);
         if (!NoFallModule.g(var2)) {
            var1.field_3944.method_52787(var2);
         }

         q8.v("ElytraControl", "{}: glide started directly", this.h.getName());
         return true;
      } else {
         return false;
      }
   }

   public void n() {
      this.L();
      this.W = w.k.h;
      this.R = 0;
   }

   public static int Y(double var0) {
      return (int)Math.ceil(Math.max(0.0, var0) / 50.0 - 1.0E-9);
   }

   public record f(class_1268 hand, int slot, int duration) {
   }

   public enum k {
      h("used"),
      M("no rocket in the offhand or hotbar"),
      K("only rockets with firework stars, which hurt when boosting"),
      g("a block is right in front of the view"),
      u("another module is using the hotbar"),
      r("the hands or the world are busy");

      public final String o;

      k(String var3) {
         this.o = var3;
      }
   }

   public record t(w.f rocket, boolean starsOnly) {
   }
}
