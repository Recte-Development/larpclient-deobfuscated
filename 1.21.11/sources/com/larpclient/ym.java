package com.larpclient;

import java.util.Optional;
import net.minecraft.class_1297;
import net.minecraft.class_1676;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3486;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class ym implements sf.k {
   private final class_1937 r;
   private final class_1297 m;

   public ym(class_1937 var1, class_1297 var2) {
      this.r = var1;
      this.m = var2;
   }

   @Override
   public double clip(class_243 var1, class_243 var2) {
      double var3 = var1.method_1022(var2);
      if (var3 < 1.0E-7) {
         return 1.0;
      }

      class_3965 var5 = this.r.method_17742(new class_3959(var1, var2, class_3960.field_17558, class_242.field_1348, this.m));
      double var6 = var5.method_17783() == class_240.field_1333 ? 1.0 : var1.method_1022(var5.method_17784()) / var3;

      for (class_1297 var9 : this.r
         .method_8333(this.m, new class_238(var1, var2).method_1014(1.0), var0 -> var0.method_49108() && !(var0 instanceof class_1676) && !var0.method_7325())) {
         Optional var10 = var9.method_5829().method_1014(0.3).method_992(var1, var2);
         if (var10.isPresent()) {
            var6 = Math.min(var6, var1.method_1022((class_243)var10.get()) / var3);
         }
      }

      return var6;
   }

   @Override
   public boolean water(class_243 var1) {
      return this.r.method_8316(class_2338.method_49638(var1)).method_15767(class_3486.field_15517);
   }
}
