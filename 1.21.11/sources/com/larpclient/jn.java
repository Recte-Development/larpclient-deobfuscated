package com.larpclient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.Collectors;
import net.minecraft.class_1799;
import net.minecraft.class_9334;

public final class jn {
   public static void Y(class_1799 var0, boolean var1) {
      if (!var0.method_7960() && var0 instanceof jn.e var2) {
         var2.fakeElytra$setRealPurchase(var1);
      }
   }

   private jn() {
   }

   public static class_1799 Q(class_1799 var0) {
      if (!var0.method_7960() && var0 instanceof jn.e var1) {
         var1.fakeSpawner$setPreview(true);
      }

      return var0;
   }

   public static boolean Z(class_1799 var0) {
      return var0 != null && !var0.method_7960() && var0 instanceof jn.e var1 && var1.fakeSpawner$isPreview();
   }

   public static String x(class_1799 var0) {
      String var1 = var0.method_57353()
         .method_57833()
         .filter(
            var0x -> var0x.comp_2443() != class_9334.field_49632
               && var0x.comp_2443() != class_9334.field_49631
               && var0x.comp_2443() != class_9334.field_50239
               && var0x.comp_2443() != class_9334.field_49629
               && var0x.comp_2443() != class_9334.field_49639
         )
         .map(Object::toString)
         .sorted()
         .collect(Collectors.joining("\n"));
      return J(var0.method_7909().method_40131().method_40237().method_29177() + "\n" + var1);
   }

   public static String J(String var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0.getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static boolean N(class_1799 var0) {
      return var0 != null && !var0.method_7960() && var0 instanceof jn.e var1 && var1.fakeElytra$isRealPurchase();
   }

   public interface e {
      boolean fakeElytra$isRealPurchase();

      void fakeElytra$setRealPurchase(boolean var1);

      boolean fakeSpawner$isPreview();

      void fakeSpawner$setPreview(boolean var1);
   }
}
