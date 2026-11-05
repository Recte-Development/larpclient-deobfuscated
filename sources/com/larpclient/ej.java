package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ej {
   public static final int E = 32;
   public static final String I = "Selection Version";
   public static final String t = "Selection Error";
   public static final String f = "Item Mode";
   public static final String X = "Protect Hotbar";
   public static final String G = "Protect Named Items";
   public static final String O = "Protect Enchanted Items";
   public static final String g = "Protect Filled Containers";
   public static final List<String> R = List.of("Selected items", "All except excluded", "All inventory items");

   public static String W(int var0) {
      return "Selected Item " + (var0 + 1);
   }

   public static String Y(int var0) {
      return "Excluded Item " + (var0 + 1);
   }

   public static boolean d(String var0) {
      return var0 != null && var0.length() <= 128 && var0.matches("[a-z0-9_.-]+:[a-z0-9/._-]+");
   }

   public static ej.q e(Map<String, ?> var0) {
      try {
         if (!"1".equals(var0.get("Selection Version"))) {
            throw new IllegalArgumentException("Unsupported item selection version");
         } else {
            String var1 = d(var0.get("Selection Error"));
            String var2 = d(var0.get("Item Mode"));
            if (!R.contains(var2)) {
               throw new IllegalArgumentException("Invalid item selection mode");
            } else {
               return new ej.q(
                  var2,
                  h(var0, false),
                  h(var0, true),
                  J(var0.get("Protect Hotbar")),
                  J(var0.get("Protect Named Items")),
                  J(var0.get("Protect Enchanted Items")),
                  J(var0.get("Protect Filled Containers")),
                  var1
               );
            }
         }
      } catch (RuntimeException var3) {
         return new ej.q(R.getFirst(), Set.of(), Set.of(), true, true, true, true, "Invalid item selection. Open Choose items and save to repair.");
      }
   }

   private static Set<String> h(Map<String, ?> var0, boolean var1) {
      LinkedHashSet var2 = new LinkedHashSet();

      for (int var3 = 0; var3 < 32; var3++) {
         String var4 = d(var0.get(var1 ? Y(var3) : W(var3)));
         if (!var4.isEmpty() && !d(var4)) {
            throw new IllegalArgumentException("Invalid item ID");
         }

         if (!var4.isEmpty()) {
            var2.add(var4);
         }
      }

      return var2;
   }

   private static String d(Object var0) {
      if (var0 instanceof String var1) {
         return var1;
      } else {
         throw new IllegalArgumentException("Expected selection text");
      }
   }

   private static boolean J(Object var0) {
      if (var0 instanceof Boolean var1) {
         return var1;
      } else {
         throw new IllegalArgumentException("Expected selection boolean");
      }
   }

   public static List<String> V(Collection<String> var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (String var3 : var0) {
         if (!d(var3)) {
            throw new IllegalArgumentException("Use an exact namespaced item ID: " + var3);
         }

         var1.add(var3);
      }

      if (var1.size() > 32) {
         throw new IllegalArgumentException("Each item list supports up to 32 items.");
      } else {
         return List.copyOf(var1);
      }
   }

   public static Set<String> b() {
      LinkedHashSet var0 = new LinkedHashSet<>(
         List.of(
            "Selection Version",
            "Selection Error",
            "Item Mode",
            "Protect Hotbar",
            "Protect Named Items",
            "Protect Enchanted Items",
            "Protect Filled Containers"
         )
      );

      for (int var1 = 0; var1 < 32; var1++) {
         var0.add(W(var1));
         var0.add(Y(var1));
      }

      return Set.copyOf(var0);
   }

   public static void Y(JsonObject var0) {
      boolean var1 = var0.keySet()
         .stream()
         .noneMatch(var0x -> b().contains(var0x) || var0x.startsWith("Selected Item ") || var0x.startsWith("Excluded Item ") || var0x.startsWith("Selection "));
      if (var1) {
         W(var0, true, "");
      } else {
         try {
            if (var0.keySet().stream().anyMatch(var0x -> (var0x.startsWith("Selected Item ") || var0x.startsWith("Excluded Item ")) && !b().contains(var0x))) {
               throw new IllegalArgumentException("Unsupported item selection slot");
            }

            LinkedHashMap var2 = new LinkedHashMap();

            for (String var4 : b()) {
               JsonElement var5 = var0.get(var4);
               if (var5 == null || !var5.isJsonPrimitive()) {
                  throw new IllegalArgumentException("Missing selection field");
               }

               JsonPrimitive var6 = var5.getAsJsonPrimitive();
               if (List.of("Protect Hotbar", "Protect Named Items", "Protect Enchanted Items", "Protect Filled Containers").contains(var4)) {
                  if (!var6.isBoolean()) {
                     throw new IllegalArgumentException("Invalid protection");
                  }

                  var2.put(var4, var6.getAsBoolean());
               } else {
                  if (!var6.isString()) {
                     throw new IllegalArgumentException("Invalid item ID field");
                  }

                  var2.put(var4, var6.getAsString());
               }
            }

            ej.q var8 = e(var2);
            if (!var8.error.isEmpty()) {
               throw new IllegalArgumentException(var8.error);
            }
         } catch (RuntimeException var7) {
            W(var0, false, "Invalid item selection. Open Choose items and save to repair.");
         }
      }
   }

   private static void W(JsonObject var0, boolean var1, String var2) {
      var0.addProperty("Selection Version", "1");
      var0.addProperty("Selection Error", var2);
      var0.addProperty("Item Mode", var1 ? R.get(2) : R.getFirst());

      for (String var4 : List.of("Protect Hotbar", "Protect Named Items", "Protect Enchanted Items", "Protect Filled Containers")) {
         var0.addProperty(var4, !var1);
      }

      for (int var5 = 0; var5 < 32; var5++) {
         var0.addProperty(W(var5), "");
         var0.addProperty(Y(var5), "");
      }
   }

   private ej() {
   }

   public record q(String mode, Set<String> selected, Set<String> excluded, boolean hotbar, boolean named, boolean enchanted, boolean filled, String error) {
      public q(String mode, Set<String> selected, Set<String> excluded, boolean hotbar, boolean named, boolean enchanted, boolean filled, String error) {
         selected = Set.copyOf(selected);
         excluded = Set.copyOf(excluded);
         this.mode = mode;
         this.selected = selected;
         this.excluded = excluded;
         this.hotbar = hotbar;
         this.named = named;
         this.enchanted = enchanted;
         this.filled = filled;
         this.error = error;
      }

      public String rejection(ej.v var1) {
         if (!this.error.isEmpty()) {
            return this.error;
         } else if (var1.count <= 0 || var1.id.equals("minecraft:air")) {
            return "Empty stack";
         } else if (this.excluded.contains(var1.id)) {
            return "Excluded item";
         } else if (this.mode.equals(ej.R.getFirst()) && !this.selected.contains(var1.id)) {
            return "Item is not selected";
         } else if (this.hotbar && var1.inventorySlot >= 0 && var1.inventorySlot < 9) {
            return "Protected hotbar";
         } else if (this.named && var1.named) {
            return "Protected custom name";
         } else if (this.enchanted && var1.enchanted) {
            return "Protected enchantments";
         } else {
            return this.filled && var1.filled ? "Protected container contents" : "";
         }
      }

      public boolean eligible(ej.v var1) {
         return this.rejection(var1).isEmpty();
      }

      public boolean needsSelection() {
         return this.error.isEmpty() && this.mode.equals(ej.R.getFirst()) && this.selected.isEmpty();
      }
   }

   public record v(String id, int count, int inventorySlot, boolean named, boolean enchanted, boolean filled) {
   }
}
