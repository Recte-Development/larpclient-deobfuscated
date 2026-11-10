package com.larpclient;

import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1661;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_2378;
import net.minecraft.class_310;
import net.minecraft.class_7924;
import net.minecraft.class_9334;

@Environment(EnvType.CLIENT)
public final class bk {
   private static final int Y = 3;
   private static final bk I = new bk();
   public boolean R;
   public boolean U;
   private boolean q;
   private boolean T;
   private long y;
   private int E = -1;
   private class_1799 k = class_1799.field_8037;

   private bk() {
   }

   public static bk W() {
      return I;
   }

   public void m() {
      this.R = (boolean)(!this.R ? 1 : 0);
   }

   public void U(boolean var1) {
      this.U = (boolean)(var1 && !hu.f().N() ? 1 : 0);
   }

   public void e() {
      this.U(!this.U);
   }

   public boolean l() {
      return this.q;
   }

   public void i() {
      this.q = (boolean)1;
   }

   public void R() {
      this.q = (boolean)0;
   }

   public boolean g() {
      class_310 var1 = class_310.method_1551();
      if (this.U && var1 != null && var1.field_1724 != null && var1.field_1687 != null && var1.field_1761 != null && var1.method_1562() != null) {
         int var2 = this.n();
         return var2 >= 0 && var2 < 9;
      } else {
         return false;
      }
   }

   public boolean E() {
      if (!this.R && !this.U && !this.T) {
         return false;
      }

      int var1 = this.n();
      return var1 >= 0 && var1 < 9;
   }

   public void V(int var1) {
      this.E = var1;
   }

   public int i() {
      class_310 var1 = class_310.method_1551();
      if (var1 == null || var1.field_1724 == null) {
         return 0;
      } else {
         return this.E() && this.E >= 0 && this.E < 9 ? this.E : var1.field_1724.method_31548().method_67532();
      }
   }

   public int n() {
      class_310 var1 = class_310.method_1551();
      if (var1 != null && var1.field_1724 != null) {
         class_1661 var2 = var1.field_1724.method_31548();

         for (int var3 = 0; var3 < var2.method_5439(); var3++) {
            if (A(var2.method_5438(var3))) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private static boolean A(class_1799 var0) {
      if (var0 != null && !var0.method_7960() && var0.method_31574(class_1802.field_8378)) {
         class_310 var1 = class_310.method_1551();
         if (var1 != null && var1.field_1687 != null) {
            class_2378 var2 = var1.field_1687.method_30349().method_30530(class_7924.field_41265);
            Optional var3 = var2.method_31189(class_1893.field_9100);
            return var3.isEmpty() ? false : class_1890.method_8225(var2.method_47983((class_1887)var3.get()), var0) >= 3;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static boolean E(class_1799 var0) {
      return var0 != null && !var0.method_7960() && var0.method_31574(class_1802.field_8378);
   }

   private static class_1799 F(class_1799 var0) {
      if (!E(var0)) {
         return var0;
      }

      class_1799 var1 = var0.method_7972();
      var1.method_57381(class_9334.field_49633);
      var1.method_57381(class_9334.field_49643);
      var1.method_57381(class_9334.field_50239);
      var1.method_57381(class_9334.field_49631);
      var1.method_57381(class_9334.field_49632);
      return var1;
   }

   public class_1799 c(int var1, class_1799 var2) {
      if (var1 < 0) {
         return var2;
      } else if ((this.R || this.U || this.T) && var1 == this.n()) {
         return !this.k.method_7960() ? this.k : class_1799.field_8037;
      } else {
         return var2;
      }
   }

   public class_1799 i() {
      return this.k;
   }

   public void h() {
      this.k = class_1799.field_8037;
   }

   public void u(class_1799 var1) {
      if (var1 != null && !var1.method_7960()) {
         if (this.k.method_7960()) {
            this.k = var1.method_7972();
         } else if (class_1799.method_7973(this.k, var1)) {
            this.k.method_7933(var1.method_7947());
            if (this.k.method_7947() > this.k.method_7914()) {
               this.k.method_7939(this.k.method_7914());
            }
         } else {
            this.k = var1.method_7972();
         }
      }
   }

   public class_1799 f(int var1, class_1799 var2) {
      return !this.R && !this.U ? var2 : F(var2);
   }

   public boolean m() {
      class_310 var1 = class_310.method_1551();
      if (var1 != null && var1.field_1724 != null) {
         return var1.field_1724.method_31548().method_67532() == this.n() ? false : E(var1.field_1724.method_6047());
      } else {
         return false;
      }
   }

   public void I(class_310 var1) {
      if (this.U && var1 != null && var1.field_1724 != null && var1.field_1761 != null) {
         if (var1.field_1755 == null) {
            int var2 = this.n();
            if (var2 >= 9 && var2 < 36) {
               long var3 = System.currentTimeMillis();
               if (var3 - this.y >= 250L) {
                  this.y = var3;
                  this.N(var1, var2);
               }
            }
         }
      }
   }

   private void N(class_310 var1, int var2) {
      class_1661 var3 = var1.field_1724.method_31548();
      int var4 = -1;

      for (int var5 = 0; var5 < 9; var5++) {
         if (var3.method_5438(var5).method_7960()) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         int var11 = var3.method_67532();
         if (var3.method_5438(var11).method_7960()) {
            var4 = var11;
         }
      }

      if (var4 >= 0) {
         int var12 = -1;

         for (class_1735 var7 : var1.field_1724.field_7512.field_7761) {
            if (var7.field_7871 == var3 && var7.method_34266() == var2) {
               var12 = var7.field_7874;
               break;
            }
         }

         if (var12 >= 0) {
            this.T = (boolean)1;

            try {
               var1.field_1761.method_2906(var1.field_1724.field_7512.field_7763, var12, var4, class_1713.field_7791, var1.field_1724);
            } finally {
               this.T = (boolean)0;
            }
         }
      }
   }

   public String j() {
      return this.U ? "On" : "Off";
   }
}
