package com.larpclient;

import java.util.List;
import net.minecraft.class_1799;
import net.minecraft.class_1893;
import net.minecraft.class_2680;
import net.minecraft.class_6880;

public final class e9 {
   public static e9.r s(class_1799 var0, class_2680 var1, boolean var2) {
      double var3 = var0.method_7924(var1);
      if (var3 > 1.0) {
         for (class_6880 var6 : var0.method_58657().method_57534()) {
            if (var6.method_40225(class_1893.field_9131)) {
               int var7 = var0.method_58657().method_57536(var6);
               var3 += (double)var7 * var7 + 1.0;
            }
         }
      }

      int var8 = var2 && var0.method_7963() && var0.method_7936() - var0.method_7919() <= 1 ? 0 : 1;
      return new e9.r(var3, !var1.method_29291() || var0.method_7951(var1), (boolean)var8);
   }

   public static int J(List<e9.r> var0, int var1) {
      int var2 = var1;

      for (int var3 = 0; var3 < var0.size(); var3++) {
         e9.r var4 = (e9.r)var0.get(var3);
         e9.r var5 = (e9.r)var0.get(var2);
         if (var4.usable() && (!var5.usable() || var4.harvest() && !var5.harvest() || var4.harvest() == var5.harvest() && var4.speed() > var5.speed())) {
            var2 = var3;
         }
      }

      return var2;
   }

   private e9() {
   }

   public record r(double speed, boolean harvest, boolean usable) {
   }

   public static final class x {
      private int q = -1;
      private int M = -1;
      private boolean z;

      public void D() {
         this.q = this.M = -1;
         this.z = (boolean)1;
      }

      public int l(int var1, int var2) {
         if (this.M >= 0 && var1 != this.M) {
            this.D();
         }

         if (!this.z && var1 != var2) {
            if (this.q < 0) {
               this.q = var1;
            }

            this.M = var2;
            return var2;
         } else {
            return var1;
         }
      }

      public int G(int var1, boolean var2) {
         int var3 = var2 && var1 == this.M && this.q >= 0 ? this.q : var1;
         this.q = this.M = -1;
         this.z = (boolean)0;
         return var3;
      }
   }
}
