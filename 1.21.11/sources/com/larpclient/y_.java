package com.larpclient;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class y_ {
   private static final Pattern N = Pattern.compile(
      "(?i)(?:\\$\\s*|(?:worth|price|cost)\\s*[:~≈]?\\s*\\$?\\s*)([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*+([kmbt]?)(?![a-z0-9.,])"
   );
   private static final Pattern H = Pattern.compile("^[^\\p{L}\\p{N}]*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmbt])(?:\\s*/\\s*[0-9]+)?\\s*$");

   public static int o(int var0) {
      return var0 <= 0 ? 0 : 1 + (var0 - 1) / 64;
   }

   public static boolean P(Iterable<String> var0) {
      return u(var0) == y_.z.y;
   }

   public static y_.z u(Iterable<String> var0) {
      byte var1 = 0;
      byte var2 = 0;

      for (String var4 : var0) {
         String var5 = Normalizer.normalize(var4, Form.NFKC).replaceAll("§.", "").replaceAll("[\\p{Z}\\u200B\\uFEFF]", " ").toLowerCase(Locale.ROOT);
         Matcher var6 = N.matcher(var5);
         if (!var6.find()) {
            var6 = H.matcher(var5);
         }

         var6.reset();

         while (var6.find()) {
            var1 = 1;

            byte var7 = switch (var6.group(2)) {
               case "k" -> 3;
               case "m" -> 6;
               case "b" -> 9;
               case "t" -> 12;
               default -> 0;
            };
            BigDecimal var10 = new BigDecimal(var6.group(1).replace(",", "")).scaleByPowerOfTen(var7);
            if (var10.compareTo(BigDecimal.valueOf(3000000L)) > 0) {
               return y_.z.j;
            }
         }

         if ((var5.contains("worth") || var5.contains("price") || var5.contains("cost") || var5.contains("$"))
            && !N.matcher(var5).find()
            && !H.matcher(var5).matches()) {
            var2 = 1;
         }
      }

      return var1 != 0 && var2 == 0 ? y_.z.y : y_.z.x;
   }

   public enum z {
      y,
      j,
      x;
   }
}
