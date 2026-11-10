package com.larpclient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.function.Consumer;

public final class w_ {
   private static final BigDecimal W = new BigDecimal("9000000000000000");
   private static final String c = "Usage: /pay <player> <amount>, e.g. /pay Alex 1.5m";
   public static int y;

   private w_() {
   }

   public static boolean W(String var0) {
      if (var0 == null) {
         return false;
      }

      String var1 = var0.strip();
      if (var1.startsWith("/")) {
         var1 = var1.substring(1);
      }

      int var2 = 0;

      while (var2 < var1.length() && !Character.isWhitespace(var1.charAt(var2))) {
         var2++;
      }

      String var3 = var1.substring(0, var2).toLowerCase(Locale.ROOT);
      return var3.equals("pay") || var3.matches("[a-z0-9_.-]+:pay");
   }

   public static w_.e Y(String var0) {
      if (var0 == null) {
         return new w_.e(null, null, "Usage: /pay <player> <amount>, e.g. /pay Alex 1.5m");
      }

      String var1 = var0.strip();
      if (var1.startsWith("/")) {
         var1 = var1.substring(1);
      }

      String[] var2 = var1.split("(?U)\\s+");
      if (var2.length == 3 && W(var1)) {
         if (!var2[1].matches("[A-Za-z0-9_]{1,16}")) {
            return new w_.e(null, null, "Use a Minecraft player name (1 to 16 letters, digits or underscores).");
         }

         String var3 = var2[2];
         if (var3.length() <= 40 && var3.matches("\\$?(?:[0-9]+|[0-9]{1,3}(?:,[0-9]{3})+)(?:\\.[0-9]{1,6})?[kKmMbBtT]?")) {
            String var4 = var3.replace("$", "").replace(",", "").toLowerCase(Locale.ROOT);

            byte var5 = switch (var4.charAt(var4.length() + -1)) {
               case 'b' -> 9;
               case 'k' -> 3;
               case 'm' -> 6;
               case 't' -> 12;
               default -> 0;
            };
            if (var5 != 0) {
               var4 = var4.substring(0, var4.length() + -1);
            }

            BigDecimal var6 = new BigDecimal(var4).scaleByPowerOfTen(var5).stripTrailingZeros();
            return var6.signum() > 0 && var6.compareTo(W) <= 0 && var6.scale() <= 2
               ? new w_.e(var2[1], var6, null)
               : new w_.e(null, null, "Amount must be positive, at most 9 quadrillion, with at most two decimal places after expansion.");
         } else {
            return new w_.e(null, null, "Use a positive amount with optional commas and k, m, b or t.");
         }
      } else {
         return new w_.e(null, null, "Usage: /pay <player> <amount>, e.g. /pay Alex 1.5m");
      }
   }

   public static boolean m(boolean var0, String var1, Consumer<w_.e> var2, Consumer<RuntimeException> var3) {
      if (var0 && W(var1)) {
         try {
            var2.accept(Y(var1));
         } catch (RuntimeException var7) {
            RuntimeException var4 = var7;

            try {
               var3.accept(var4);
            } catch (RuntimeException var6) {
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static String U(BigDecimal var0) {
      String var2 = "";
      int var1 = y;
      byte var3 = 0;
      if (var0.compareTo(BigDecimal.valueOf(1000000000000L)) >= 0) {
         var3 = 12;
         var2 = "T";
         if (var1 == 0) {
            return var0.scaleByPowerOfTen(-var3).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + var2;
         }

         aq.n = (boolean)(aq.n ? 0 : 1);
      }

      if (var0.compareTo(BigDecimal.valueOf(1000000000L)) >= 0) {
         var3 = 9;
         var2 = "B";
         if (var1 == 0) {
            return var0.scaleByPowerOfTen(-var3).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + var2;
         }
      }

      if (var0.compareTo(BigDecimal.valueOf(1000000L)) >= 0) {
         var3 = 6;
         var2 = "M";
         if (var1 == 0) {
            return var0.scaleByPowerOfTen(-var3).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + var2;
         }
      }

      if (var0.compareTo(BigDecimal.valueOf(1000L)) >= 0) {
         var3 = 3;
         var2 = "k";
      }

      return var0.scaleByPowerOfTen(-var3).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString() + var2;
   }

   public record e(String player, BigDecimal amount, String error) {
      public boolean valid() {
         return this.error == null;
      }
   }
}
