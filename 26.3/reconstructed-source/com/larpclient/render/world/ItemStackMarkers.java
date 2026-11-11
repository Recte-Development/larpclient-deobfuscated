package com.larpclient.render.world;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.Collectors;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;

public final class ItemStackMarkers {
   public static void setRealPurchase(ItemStack var0, boolean var1) {
      if (!var0.isEmpty() && var0 instanceof ItemStackMarkers.StackMarkerAccess var2) {
         var2.fakeElytra$setRealPurchase(var1);
      }
   }

   private ItemStackMarkers() {
   }

   public static ItemStack markPreview(ItemStack var0) {
      if (!var0.isEmpty() && var0 instanceof ItemStackMarkers.StackMarkerAccess var1) {
         var1.fakeSpawner$setPreview(true);
      }

      return var0;
   }

   public static boolean isPreview(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0 instanceof ItemStackMarkers.StackMarkerAccess var1 && var1.fakeSpawner$isPreview();
   }

   public static String fingerprint(ItemStack var0) {
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
      return sha256(var0.getItem().builtInRegistryHolder().key().identifier() + "\n" + var1);
   }

   public static String sha256(String var0) {
      try {
         return HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var0.getBytes(StandardCharsets.UTF_8)));
      } catch (NoSuchAlgorithmException var2) {
         throw new IllegalStateException(var2);
      }
   }

   public static boolean isRealPurchase(ItemStack var0) {
      return var0 != null && !var0.isEmpty() && var0 instanceof ItemStackMarkers.StackMarkerAccess var1 && var1.fakeElytra$isRealPurchase();
   }

   public interface StackMarkerAccess {
      boolean fakeElytra$isRealPurchase();

      void fakeElytra$setRealPurchase(boolean var1);

      boolean fakeSpawner$isPreview();

      void fakeSpawner$setPreview(boolean var1);
   }
}
