package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class ck {
   public static final int t = 45;
   public static final int z = 32;

   private ck() {
   }

   public static ck.n s(String var0) {
      String var1 = d(var0);
      if (var1.startsWith("orders (page")) {
         return ck.n.v;
      }

      if (!var1.startsWith("orders ->")) {
         return ck.n.A;
      }

      String var2 = var1.substring("orders ->".length()).trim();

      return switch (var2) {
         case "your orders" -> ck.n.u;
         case "edit order" -> ck.n.e;
         case "collect items" -> ck.n.z;
         default -> ck.n.A;
      };
   }

   public static ck.p o(String var0, String var1) {
      String var2 = d(var1);
      if (var2.equals("sell") || var2.contains("sell") || "minecraft:emerald".equals(var0)) {
         return ck.p.S;
      } else if ("minecraft:chest".equals(var0) && var2.equals("your orders")) {
         return ck.p.j;
      } else if ("minecraft:chest".equals(var0) && var2.equals("collect")) {
         return ck.p.L;
      } else if ("minecraft:dropper".equals(var0) && var2.equals("drop")) {
         return ck.p.l;
      } else {
         return "minecraft:arrow".equals(var0) && var2.equals("next page") ? ck.p.W : ck.p.C;
      }
   }

   public static boolean E(String var0, String var1, List<String> var2) {
      if (!var0.equals(var1)) {
         return false;
      }

      for (String var4 : var2) {
         if (d(var4).endsWith("delivered")) {
            return true;
         }
      }

      return false;
   }

   public static String g(String var0) {
      String var1 = var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
      if (var1.isEmpty()) {
         return "";
      } else if (!var1.matches("([a-z0-9_.-]+:)?[a-z0-9_./-]+")) {
         return "";
      } else {
         return var1.contains(":") ? var1 : "minecraft:" + var1;
      }
   }

   public static List<ck.s> Q(ck.s var0, ck.s var1) {
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
                  var8.add(new ck.s(var0.x() + var5 * var10, var0.y() + var6 * var9, var0.z() + var7 * var11));
               }
            }
         }

         return List.copyOf(var8);
      } else {
         return List.of();
      }
   }

   public static String d(String var0) {
      return var0 == null ? "" : var0.replaceAll("§.", "").trim().toLowerCase(Locale.ROOT);
   }

   public enum n {
      v,
      u,
      e,
      z,
      A;
   }

   public enum p {
      j,
      L,
      l,
      W,
      S,
      C;
   }

   public record s(int x, int y, int z) {
   }
}
