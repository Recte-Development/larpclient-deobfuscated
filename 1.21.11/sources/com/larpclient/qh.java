package com.larpclient;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.class_10712;
import net.minecraft.class_1299;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_9296;
import net.minecraft.class_9334;

public enum qh {
   x("Skeleton", class_1299.field_6137, class_1802.field_8606, class_1802.field_8107, null),
   v("Iron Golem", class_1299.field_6147, class_1802.field_8620, class_1802.field_8162, "MHF_Golem"),
   B("Zombified Piglin", class_1299.field_6050, class_1802.field_8511, class_1802.field_8397, "MHF_PigZombie");

   public final String g;
   public final class_1299<?> c;
   public final class_1792 E;
   public final class_1792 S;
   private final String y;
   private class_9296 D;

   qh(String var3, class_1299<?> var4, class_1792 var5, class_1792 var6, String var7) {
      this.g = var3;
      this.c = var4;
      this.E = var5;
      this.S = var6;
      this.y = var7;
   }

   public class_1799 u() {
      class_1799 var1 = new class_1799(this.y == null ? class_1802.field_8398 : class_1802.field_8575);
      if (this.y != null) {
         if (this.D == null) {
            JsonObject var2 = new JsonObject();
            var2.addProperty("name", this.y);
            this.D = (class_9296)class_9296.field_49359.parse(JsonOps.INSTANCE, var2).getOrThrow();
         }

         var1.method_57379(class_9334.field_49617, this.D);
         var1.method_57379(class_9334.field_56400, class_10712.field_56318.method_67215(class_9334.field_49617, true));
      }

      return var1;
   }

   public static boolean I(class_1792 var0) {
      for (qh var4 : values()) {
         if (var0 == var4.E || var0 != class_1802.field_8162 && var0 == var4.S) {
            return true;
         }
      }

      return false;
   }
}
