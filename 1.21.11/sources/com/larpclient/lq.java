package com.larpclient;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.function.LongConsumer;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_4076;
import net.minecraft.class_8603;

public final class lq {
   public static void w(LongSet var0, class_2338 var1, int var2, int var3, int var4, LongConsumer var5) {
      int var6 = class_4076.method_18675(var1.method_10263());
      int var7 = class_4076.method_18675(var1.method_10260());
      int var8 = Math.clamp(class_4076.method_18675(var1.method_10264()), var3, var4);
      LongIterator var9 = var0.iterator();

      while (var9.hasNext()) {
         long var10 = var9.nextLong();
         int var12 = class_1923.method_8325(var10);
         int var13 = class_1923.method_8332(var10);
         if (class_8603.method_52357(var6, var7, var2, var12, var13)) {
            var5.accept(class_4076.method_18685(var12, var8, var13));
         }
      }
   }

   private lq() {
   }
}
