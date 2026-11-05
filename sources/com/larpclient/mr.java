package com.larpclient;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public final class mr {
   public static Map<mr.h, Integer> x(Map<mr.h, Integer> var0, mr.h var1, int var2) {
      HashMap var3 = new HashMap(var0);
      int var4 = var3.getOrDefault(var1, 0) + var2;
      if (var4 < 0) {
         return Map.of();
      }

      if (var4 == 0) {
         var3.remove(var1);
      } else {
         var3.put(var1, var4);
      }

      return Map.copyOf(var3);
   }

   public static boolean b(int var0, int var1, Map<mr.h, Integer> var2, Map<mr.h, Integer> var3, Map<mr.h, Integer> var4) {
      return var0 != var1 && var2.isEmpty() && var3.equals(var4);
   }

   public static mr.a F(boolean var0, int var1, boolean var2, Map<mr.h, Integer> var3, Map<mr.h, Integer> var4) {
      if (var0 && var3.equals(var4) && var1 < 100) {
         return var2 && var1 >= 5 ? mr.a.L : mr.a.g;
      } else {
         return mr.a.r;
      }
   }

   public static boolean x(boolean var0, boolean var1, boolean var2, int var3, int var4) {
      return var2 && var4 > 0 && (var0 || var1 && var3 < var4);
   }

   public static boolean G(String var0, String var1, int var2, int var3) {
      if (var2 >= var3 - 9 && var2 < var3 && Set.of("minecraft:lime_stained_glass_pane", "minecraft:green_stained_glass_pane").contains(var0)) {
         String var4 = var1.strip().toLowerCase(Locale.ROOT);
         return var4.equals("sell") || var4.equals("confirm") || var4.equals("confirm sale") || var4.startsWith("sell ") || var4.startsWith("confirm ");
      } else {
         return false;
      }
   }

   public static boolean D(String var0, String var1, int var2, int var3) {
      if (var2 >= var3 - 9 && var2 < var3) {
         String var4 = var1.strip().toLowerCase(Locale.ROOT);
         return Set.of("minecraft:black_stained_glass_pane", "minecraft:gray_stained_glass_pane").contains(var0)
            ? var4.isEmpty()
            : var0.equals("minecraft:red_stained_glass_pane")
               && (var4.equals("cancel") || var4.equals("close") || var4.equals("back") || var4.startsWith("cancel ") || var4.startsWith("close "));
      } else {
         return false;
      }
   }

   private mr() {
   }

   public enum a {
      g,
      L,
      r;
   }

   public record h(String id, Object components) {
   }

   public record l(int revision, mr.h key, int sourceCount, Map<mr.h, Integer> input, Map<mr.h, Integer> inventory) {
      public l(int revision, mr.h key, int sourceCount, Map<mr.h, Integer> input, Map<mr.h, Integer> inventory) {
         input = Map.copyOf(input);
         inventory = Map.copyOf(inventory);
         this.revision = revision;
         this.key = key;
         this.sourceCount = sourceCount;
         this.input = input;
         this.inventory = inventory;
      }

      public int accepted(int var1, int var2, Map<mr.h, Integer> var3, Map<mr.h, Integer> var4) {
         if (this.revision != var1 && var2 >= 0 && var2 < this.sourceCount) {
            int var5 = this.sourceCount - var2;
            return mr.x(this.input, this.key, var5).equals(var3) && mr.x(this.inventory, this.key, -var5).equals(var4) ? var5 : 0;
         } else {
            return 0;
         }
      }
   }
}
