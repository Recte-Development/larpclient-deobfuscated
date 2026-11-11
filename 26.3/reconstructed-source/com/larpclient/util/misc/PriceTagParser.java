package com.larpclient.util.misc;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PriceTagParser {
   private static final Pattern pricePattern = Pattern.compile(
      "(?i)(?:\\$\\s*|(?:worth|price|cost)\\s*[:~≈]?\\s*\\$?\\s*)([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*+([kmbt]?)(?![a-z0-9.,])"
   );
   private static final Pattern shorthandPattern = Pattern.compile("^[^\\p{L}\\p{N}]*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmbt])(?:\\s*/\\s*[0-9]+)?\\s*$");

   public static int stackCount(int var0) {
      return var0 <= 0 ? 0 : 1 + (var0 - 1) / 64;
   }

   public static boolean isPlausiblePrice(Iterable<String> var0) {
      return classifyPrice(var0) == PriceTagParser.PriceVerdict.PLAUSIBLE;
   }

   public static PriceTagParser.PriceVerdict classifyPrice(Iterable<String> var0) {
      byte var1 = 0;
      byte var2 = 0;

      for (String var4 : var0) {
         String var5 = Normalizer.normalize(var4, Form.NFKC).replaceAll("§.", "").replaceAll("[\\p{Z}\\u200B\\uFEFF]", " ").toLowerCase(Locale.ROOT);
         Matcher var6 = pricePattern.matcher(var5);
         if (!var6.find()) {
            var6 = shorthandPattern.matcher(var5);
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
               return PriceTagParser.PriceVerdict.TOO_HIGH;
            }
         }

         if ((var5.contains("worth") || var5.contains("price") || var5.contains("cost") || var5.contains("$"))
            && !pricePattern.matcher(var5).find()
            && !shorthandPattern.matcher(var5).matches()) {
            var2 = 1;
         }
      }

      return var1 != 0 && var2 == 0 ? PriceTagParser.PriceVerdict.PLAUSIBLE : PriceTagParser.PriceVerdict.UNCLEAR;
   }

   public enum PriceVerdict {
      PLAUSIBLE,
      TOO_HIGH,
      UNCLEAR;
   }
}
