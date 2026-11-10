package com.larpclient;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class j8 {
   private static final Pattern K = Pattern.compile("(?<![\\p{N}.])([1-9])(?:\\s*\\.\\s*(1))?(?![\\p{N}.])");

   private j8() {
   }

   private static String w(String var0) {
      return Normalizer.normalize(var0, Form.NFKC).replaceAll("[§&][0-9a-fk-orxA-FK-ORX]", "").replaceAll("\\p{Cf}", "").strip();
   }

   public static int i(String var0) {
      Matcher var1 = K.matcher(w(var0));
      if (!var1.find()) {
         return -1;
      }

      int var2 = Integer.parseInt(var1.group(1));
      return var1.find() ? -1 : var2;
   }

   public static boolean P(String var0) {
      Matcher var1 = K.matcher(w(var0));
      return i(var0) > 0 && var1.find() && var1.group(2) != null;
   }

   public static j8.u x(int var0, int var1, boolean var2, int var3) {
      if (var0 >= 1 && var0 <= 9 && var1 >= 1 && var1 <= 9) {
         if (var2 ? var0 <= var1 : var1 <= var0) {
            ArrayList var4 = new ArrayList();

            for (int var5 = 1; var5 <= 9; var5++) {
               for (int var6 = 1; var6 <= 9; var6++) {
                  if ((var2 ? var5 > var6 : var6 > var5) && (var5 == var0 || var6 == var1)) {
                     var4.add(new j8.u(var5, var6));
                  }
               }
            }

            if (var4.isEmpty()) {
               for (int var7 = 1; var7 <= 9; var7++) {
                  for (int var8 = 1; var8 <= 9; var8++) {
                     if (var2 ? var7 > var8 : var8 > var7) {
                        var4.add(new j8.u(var7, var8));
                     }
                  }
               }
            }

            return (j8.u)var4.get(Math.floorMod(var3, var4.size()));
         } else {
            return new j8.u(var0, var1);
         }
      } else {
         throw new IllegalArgumentException("Ranks must be 1-9");
      }
   }

   public record u(int host, int viewer) {
   }
}
