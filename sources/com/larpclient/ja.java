package com.larpclient;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.Collectors;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public final class ja {
   public static void x(ItemStack var0, boolean var1) {
      if (!var0.isEmpty() && var0 instanceof ja.b var2) {
         var2.fakeElytra$setRealPurchase(var1);
      }
   }

   private ja() {
   }

   public static ItemStack T(ItemStack var0) {
      if (!var0.isEmpty() && var0 instanceof ja.b var1) {
         var1.fakeSpawner$setPreview(true);
      }

      return var0;
   }

   public static boolean Q(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0 instanceof ja.b var1 && var1.fakeSpawner$isPreview();
   }

   public static String n(ItemStack var0) {
      String var1 = var0.getComponents()
         .stream()
         .filter(
            var0x -> var0x.type() != DataComponents.LORE
               && var0x.type() != DataComponents.CUSTOM_NAME
               && var0x.type() != DataComponents.ITEM_NAME
               && var0x.type() != DataComponents.DAMAGE
               && var0x.type() != DataComponents.REPAIR_COST
         )
         .map(Object::toString)
         .sorted()
         .collect(Collectors.joining("\n"));
      return k(var0.getItem().builtInRegistryHolder().key().identifier() + "\n" + var1);
   }

   public static String k(String var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0.getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static boolean C(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0 instanceof ja.b var1 && var1.fakeElytra$isRealPurchase();
   }

   public interface b {
      boolean fakeElytra$isRealPurchase();

      void fakeElytra$setRealPurchase(boolean var1);

      boolean fakeSpawner$isPreview();

      void fakeSpawner$setPreview(boolean var1);
   }
}
