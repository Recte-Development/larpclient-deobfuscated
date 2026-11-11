package com.larpclient.util.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class OrderMenuParser {
   public static final int mAX_SELECTION_SIZE = 45;
   public static final int mAX_AXIS_SPAN = 32;

   private OrderMenuParser() {
   }

   public static OrderMenuParser.ScreenType parseScreen(String var0) {
      String var1 = stripFormatting(var0);
      if (var1.startsWith("orders (page")) {
         return OrderMenuParser.ScreenType.ORDERS_LIST;
      }

      if (!var1.startsWith("orders ->")) {
         return OrderMenuParser.ScreenType.UNKNOWN;
      }

      String var2 = var1.substring("orders ->".length()).trim();

      return switch (var2) {
         case "your orders" -> OrderMenuParser.ScreenType.YOUR_ORDERS;
         case "edit order" -> OrderMenuParser.ScreenType.EDIT_ORDER;
         case "collect items" -> OrderMenuParser.ScreenType.COLLECT_ITEMS;
         default -> OrderMenuParser.ScreenType.UNKNOWN;
      };
   }

   public static OrderMenuParser.ButtonType classifyButton(String var0, String var1) {
      String var2 = stripFormatting(var1);
      if (var2.equals("sell") || var2.contains("sell") || "minecraft:emerald".equals(var0)) {
         return OrderMenuParser.ButtonType.SELL;
      } else if ("minecraft:chest".equals(var0) && var2.equals("your orders")) {
         return OrderMenuParser.ButtonType.YOUR_ORDERS;
      } else if ("minecraft:chest".equals(var0) && var2.equals("collect")) {
         return OrderMenuParser.ButtonType.COLLECT;
      } else if ("minecraft:dropper".equals(var0) && var2.equals("drop")) {
         return OrderMenuParser.ButtonType.DROP;
      } else {
         return "minecraft:arrow".equals(var0) && var2.equals("next page") ? OrderMenuParser.ButtonType.NEXT_PAGE : OrderMenuParser.ButtonType.OTHER;
      }
   }

   public static boolean isDelivered(String var0, String var1, List<String> var2) {
      if (!var0.equals(var1)) {
         return false;
      }

      for (String var4 : var2) {
         if (stripFormatting(var4).endsWith("delivered")) {
            return true;
         }
      }

      return false;
   }

   public static String normalizeItemId(String var0) {
      String var1 = var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
      if (var1.isEmpty()) {
         return "";
      } else if (!var1.matches("([a-z0-9_.-]+:)?[a-z0-9_./-]+")) {
         return "";
      } else {
         return var1.contains(":") ? var1 : "minecraft:" + var1;
      }
   }

   public static List<OrderMenuParser.BlockCoord> expandBox(OrderMenuParser.BlockCoord var0, OrderMenuParser.BlockCoord var1) {
      int var2 = Math.abs(var0.getX() - var1.getX());
      int var3 = Math.abs(var0.getY() - var1.getY());
      int var4 = Math.abs(var0.getZ() - var1.getZ());
      if (var2 < 32 && var3 < 32 && var4 < 32) {
         int var5 = Integer.signum(var1.getX() - var0.getX());
         int var6 = Integer.signum(var1.getY() - var0.getY());
         int var7 = Integer.signum(var1.getZ() - var0.getZ());
         ArrayList var8 = new ArrayList((var2 + 1) * (var3 + 1) * (var4 + 1));

         for (int var9 = 0; var9 <= var3; var9++) {
            for (int var10 = 0; var10 <= var2; var10++) {
               for (int var11 = 0; var11 <= var4; var11++) {
                  var8.add(new OrderMenuParser.BlockCoord(var0.getX() + var5 * var10, var0.getY() + var6 * var9, var0.getZ() + var7 * var11));
               }
            }
         }

         return List.copyOf(var8);
      } else {
         return List.of();
      }
   }

   public static String stripFormatting(String var0) {
      return var0 == null ? "" : var0.replaceAll("§.", "").trim().toLowerCase(Locale.ROOT);
   }

   public record BlockCoord(int x, int y, int z) {
      private final int posX;
      private final int posY;
      private final int posZ;

      public BlockCoord(int x, int y, int z) {
         this.posX = x;
         this.posY = y;
         this.posZ = z;
      }

      public int getX() {
         return this.posX;
      }

      public int getY() {
         return this.posY;
      }

      public int getZ() {
         return this.posZ;
      }
   }

   public enum ButtonType {
      YOUR_ORDERS,
      COLLECT,
      DROP,
      NEXT_PAGE,
      SELL,
      OTHER;
   }

   public enum ScreenType {
      ORDERS_LIST,
      YOUR_ORDERS,
      EDIT_ORDER,
      COLLECT_ITEMS,
      UNKNOWN;
   }
}
