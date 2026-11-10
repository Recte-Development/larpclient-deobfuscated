package com.larpclient;

import java.util.stream.IntStream;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1713;
import net.minecraft.class_1716;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_9334;

public class ux {
   private static final ux L = new ux();
   public class_1792 Q = class_1802.field_20391;
   public class_1792 D = class_1802.field_20412;
   public class_1792 S = class_1802.field_8477;
   public ux.k z = ux.k.L;
   public boolean n;
   private final en e = new en(true);
   private final ux.k[] H = new ux.k[9];
   private boolean Z;
   private boolean m;

   public ux() {
      this.x();
   }

   public static ux o() {
      return L;
   }

   public void K() {
      this.z = this.z.h();
   }

   public class_1792 D() {
      return switch (this.z) {
         case L -> this.Q;
         case l -> this.D;
         case V -> this.S;
      };
   }

   public boolean k(class_310 var1, class_2338 var2, class_1716 var3) {
      this.e.J = this.z.ordinal() + 1;
      if (var2 != null && var1 != null) {
         if (this.e.j() && var2.equals(this.e.P(0)) && this.e.S(var1)) {
            this.e.N(var3, 0, true);
            return true;
         }

         class_1792 var4 = null;
         class_1792 var5 = null;
         class_1792 var6 = null;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            class_1799 var9 = var3.method_7611(var8).method_7677();
            if (!var9.method_7960()) {
               var7++;
               if (var8 == 4) {
                  var6 = var9.method_7909();
               } else if (var4 == null) {
                  var4 = var9.method_7909();
               } else if (var9.method_7909() != var4 && var5 == null) {
                  var5 = var9.method_7909();
               }
            }
         }

         if (var7 == 0) {
            return false;
         }

         if (!this.e.j() || !var2.equals(this.e.P(0)) || var7 == 9) {
            if (var4 != null) {
               this.Q = var4;
            }

            if (var5 != null) {
               this.D = var5;
            }

            if (var6 != null) {
               this.S = var6;
            }
         }

         for (int var10 = 0; var10 < 9; var10++) {
            class_1799 var11 = var3.method_7611(var10).method_7677();
            if (!var11.method_7960()) {
               this.m(var10, var10 == 4 ? ux.k.V : (var11.method_31574(this.D) ? ux.k.l : ux.k.L));
            }
         }

         this.e.f(var1, var2, var3, this.Q, this.D, this.S, this.o());
         return true;
      } else {
         return false;
      }
   }

   public void x() {
      this.g(new int[]{1, 1, 2, 1, 3, 2, 1, 2, 2});
   }

   public ux.k K(int var1) {
      return var1 >= 0 && var1 < 9 ? this.H[var1] : null;
   }

   public void m(int var1, ux.k var2) {
      if (var1 >= 0 && var1 < 9) {
         this.H[var1] = var2;
      }
   }

   public void Z(int var1) {
      this.m(var1, null);
   }

   public int[] o() {
      int[] var1 = new int[9];

      for (int var2 = 0; var2 < 9; var2++) {
         var1[var2] = this.H[var2] == null ? 0 : this.H[var2].ordinal() + 1;
      }

      return var1;
   }

   public void g(int[] var1) {
      if (var1 != null && var1.length == 9) {
         for (int var2 = 0; var2 < 9; var2++) {
            this.H[var2] = var1[var2] >= 1 && var1[var2] <= 3 ? ux.k.values()[var1[var2] - 1] : null;
         }
      } else {
         this.x();
      }
   }

   public int[] C(ux.k var1) {
      return IntStream.range(0, 9).filter(var2 -> var1 != null && this.H[var2] == var1).toArray();
   }

   public void w() {
   }

   public class_1799 b(class_1799 var1, boolean var2) {
      return this.X(var1);
   }

   public String M(class_1799 var1, String var2) {
      return var2;
   }

   public static class_1799 l(class_1792 var0, class_1799 var1, boolean var2) {
      class_1799 var3 = new class_1799(var0, var1.method_7947());

      for (int var4 = 1; var4 <= 3; var4++) {
         class_1799 var5 = L.e.F(var4);
         if (var5.method_31574(var0)) {
            var3 = var5.method_46651(var1.method_7947());
            break;
         }
      }

      if (var2 && var1.method_57826(class_9334.field_49631)) {
         var3.method_57379(class_9334.field_49631, (class_2561)var1.method_58694(class_9334.field_49631));
      }

      return var3;
   }

   private boolean n(class_310 var1, class_1716 var2) {
      hu var3 = hu.f();
      class_2338 var4 = this.e.P(0);
      return this.n
         && var4 != null
         && var1 != null
         && var1.field_1724 != null
         && var2 == var1.field_1724.field_7512
         && var2.field_7763 == var3.z()
         && var3.N(var1, var4).equals(var3.W());
   }

   public void h(class_310 var1) {
      if (var1 != null && var1.field_1724 != null && var1.field_1687 != null) {
         if (!this.n) {
            if (this.m) {
               this.e.N(var1);
            }

            this.m = (boolean)0;
         } else {
            this.m = (boolean)1;
            this.e.J = this.z.ordinal() + 1;
            this.e.I(var1);
         }
      } else {
         if (this.m && var1 != null) {
            this.e.N(var1);
         }

         this.m = (boolean)0;
      }
   }

   public class_1799 X(class_1799 var1) {
      return this.n && var1 != null && !var1.method_7960() ? this.e.l(var1) : var1;
   }

   public class_1799 V(class_1799 var1) {
      return this.X(var1);
   }

   public boolean c(class_1799 var1) {
      return (var1.method_31574(class_1802.field_8705) || var1.method_31574(class_1802.field_8187)) && this.X(var1).method_31574(class_1802.field_8288);
   }

   public class_1799 z(class_1309 var1, class_1799 var2) {
      class_310 var3 = class_310.method_1551();
      return var3 != null && var1 == var3.field_1724 ? this.X(var2) : var2;
   }

   public class_1799 V(class_1542 var1, class_1799 var2) {
      class_310 var3 = class_310.method_1551();
      if (this.n && var1 != null && var3 != null && var3.field_1687 != null) {
         class_1799 var4 = this.e.Y(var3, var1);
         return var4 == var1.method_6983() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public class_1799 g(class_310 var1, class_1735 var2) {
      return var1 != null
            && var1.field_1724 != null
            && var2.field_7871 != var1.field_1724.method_31548()
            && var1.field_1724.field_7512 instanceof class_1716 var3
            && var2.method_34266() >= 0
            && var2.method_34266() < 9
            && this.n(var1, var3)
         ? this.e.i(var1, var3, 0, var2.method_34266())
         : null;
   }

   public boolean L(class_310 var1, int var2, int var3, int var4, class_1713 var5) {
      if (!this.Z
         && var1 != null
         && var1.field_1724 != null
         && var1.field_1761 != null
         && var1.field_1724.field_7512 instanceof class_1716 var6
         && var6.field_7763 == var2
         && var3 >= 0
         && var3 < 9
         && this.n(var1, var6)) {
         int var12 = this.e.U(var1, var6, 0, var3);
         int var8 = var5 == class_1713.field_7791 && var4 >= 0 && var4 < 9 ? var4 : (var5 == class_1713.field_7790 && var4 == 0 ? 36 : -1);
         if (var8 >= 0 && this.e.g(var1, var6, var12, var8)) {
            return true;
         }

         if (var12 == var3) {
            return false;
         }

         this.Z = (boolean)1;

         try {
            var1.field_1761.method_2906(var2, var12, var4, var5, var1.field_1724);
         } finally {
            this.Z = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public enum k {
      L("1st"),
      l("2nd"),
      V("Middle");

      public final String g;

      k(String var3) {
         this.g = var3;
      }

      public ux.k h() {
         return switch (this) {
            case L -> V;
            case l -> L;
            case V -> l;
         };
      }
   }
}
