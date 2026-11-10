package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class gz {
   public static final int E = 45;
   public static final int j = 32;

   private gz() {
   }

   public static gz.a l(String var0) {
      String var1 = w(var0);
      if (var1.startsWith("orders (page")) {
         return gz.a.f;
      }

      if (!var1.startsWith("orders ->")) {
         return gz.a.b;
      }

      String var2 = var1.substring("orders ->".length()).trim();

      return switch (var2) {
         case "your orders" -> gz.a.i;
         case "edit order" -> gz.a.V;
         case "collect items" -> gz.a.k;
         default -> gz.a.b;
      };
   }

   public static gz.w N(String var0, String var1) {
      String var2 = w(var1);
      if (var2.equals("sell") || var2.contains("sell") || "minecraft:emerald".equals(var0)) {
         return gz.w.h;
      } else if ("minecraft:chest".equals(var0) && var2.equals("your orders")) {
         return gz.w.W;
      } else if ("minecraft:chest".equals(var0) && var2.equals("collect")) {
         return gz.w.c;
      } else if ("minecraft:dropper".equals(var0) && var2.equals("drop")) {
         return gz.w.A;
      } else {
         return "minecraft:arrow".equals(var0) && var2.equals("next page") ? gz.w.t : gz.w.v;
      }
   }

   public static boolean k(String var0, String var1, List<String> var2) {
      if (!var0.equals(var1)) {
         return false;
      }

      for (String var4 : var2) {
         if (w(var4).endsWith("delivered")) {
            return true;
         }
      }

      return false;
   }

   public static String X(String var0) {
      String var1 = var0 == null ? "" : var0.trim().toLowerCase(Locale.ROOT).replace(' ', '_');
      if (var1.isEmpty()) {
         return "";
      } else if (!var1.matches("([a-z0-9_.-]+:)?[a-z0-9_./-]+")) {
         return "";
      } else {
         return var1.contains(":") ? var1 : "minecraft:" + var1;
      }
   }

   public static List<gz.f> J(gz.f var0, gz.f var1) {
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
                  var8.add(new gz.f(var0.x() + var5 * var10, var0.y() + var6 * var9, var0.z() + var7 * var11));
               }
            }
         }

         return List.copyOf(var8);
      } else {
         return List.of();
      }
   }

   public static String w(String var0) {
      return var0 == null ? "" : var0.replaceAll("§.", "").trim().toLowerCase(Locale.ROOT);
   }

   public enum a {
      f,
      i,
      V,
      k,
      b;
   }

   public record f(int x, int y, int z) {
   }

   public enum w {
      W,
      c,
      A,
      t,
      h,
      v;
   }
}
