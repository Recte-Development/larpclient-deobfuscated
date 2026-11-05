package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class zc {
   public static final int Z = 45;
   public static final int w = 32;

   private zc() {
   }

   public static zc.q p(String var0) {
      String var1 = c(var0);
      if (var1.startsWith("orders (page")) {
         return zc.q.y;
      }

      if (!var1.startsWith("orders ->")) {
         return zc.q.b;
      }

      String var2 = var1.substring("orders ->".length()).trim();

      return switch (var2) {
         case "your orders" -> zc.q.F;
         case "edit order" -> zc.q.J;
         case "collect items" -> zc.q.U;
         default -> zc.q.b;
      };
   }

   public static zc.o P(String var0, String var1) {
      String var2 = c(var1);
      if (var2.equals("sell") || var2.contains("sell") || "minecraft:emerald".equals(var0)) {
         return zc.o.P;
      } else if ("minecraft:chest".equals(var0) && var2.equals("your orders")) {
         return zc.o.H;
      } else if ("minecraft:chest".equals(var0) && var2.equals("collect")) {
         return zc.o.j;
      } else if ("minecraft:dropper".equals(var0) && var2.equals("drop")) {
         return zc.o.R;
      } else {
         return "minecraft:arrow".equals(var0) && var2.equals("next page") ? zc.o.T : zc.o.B;
      }
   }

   public static boolean l(String var0, String var1, List<String> var2) {
      if (!var0.equals(var1)) {
         return false;
      }

      for (String var4 : var2) {
         if (c(var4).endsWith("delivered")) {
            return true;
         }
      }

      return false;
   }

   public static String t(String var0) {
      String var1 = var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
      if (var1.isEmpty()) {
         return "";
      } else if (!var1.matches("([a-z0-9_.-]+:)?[a-z0-9_./-]+")) {
         return "";
      } else {
         return var1.contains(":") ? var1 : "minecraft:" + var1;
      }
   }

   public static List<zc.t> A(zc.t var0, zc.t var1) {
      int var2 = Math.abs(var0.x() - var1.x());
      int var3 = Math.abs(var0.y() - var1.y());
      int var4 = Math.abs(var0.z() - var1.z());
      if (var2 < 32 && var3 < 32 && var4 < 32) {
         int var5 = Integer.signum(var1.x() - var0.x());
         int var6 = Integer.signum(var1.y() - var0.y());
         int var7 = Integer.signum(var1.z() - var0.z());
         ArrayList var8 = new ArrayList((var2 + 1) * (var3 + 1) * (var4 + 1));

         for (int var9 = 0; var9 <= var3; var9++) {
            for (int var10 = 0; var10 <= var2; var10++) {
               for (int var11 = 0; var11 <= var4; var11++) {
                  var8.add(new zc.t(var0.x() + var5 * var10, var0.y() + var6 * var9, var0.z() + var7 * var11));
               }
            }
         }

         return List.copyOf(var8);
      } else {
         return List.of();
      }
   }

   public static String c(String var0) {
      return var0 == null ? "" : var0.replaceAll("§.", "").trim().toLowerCase(Locale.ROOT);
   }

   public enum o {
      H,
      j,
      R,
      T,
      P,
      B;
   }

   public enum q {
      y,
      F,
      J,
      U,
      b;
   }

   public record t(int x, int y, int z) {
   }
}
