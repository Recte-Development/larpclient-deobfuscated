package com.larpclient;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class _a {
   private static final Pattern O = Pattern.compile("(?<![\\p{N}.])([1-9])(?:\\s*\\.\\s*(1))?(?![\\p{N}.])");

   private _a() {
   }

   private static String U(String var0) {
      return Normalizer.normalize(var0, Form.NFKC).replaceAll("[§&][0-9a-fk-orxA-FK-ORX]", "").replaceAll("\\p{Cf}", "").strip();
   }

   public static int p(String var0) {
      Matcher var1 = O.matcher(U(var0));
      if (!var1.find()) {
         return -1;
      }

      int var2 = Integer.parseInt(var1.group(1));
      return var1.find() ? -1 : var2;
   }

   public static boolean R(String var0) {
      Matcher var1 = O.matcher(U(var0));
      return p(var0) > 0 && var1.find() && var1.group(2) != null;
   }

   public static _a.t P(int var0, int var1, boolean var2, int var3) {
      if (var0 >= 1 && var0 <= 9 && var1 >= 1 && var1 <= 9) {
         if (var2 ? var0 <= var1 : var1 <= var0) {
            ArrayList var4 = new ArrayList();

            for (int var5 = 1; var5 <= 9; var5++) {
               for (int var6 = 1; var6 <= 9; var6++) {
                  if ((var2 ? var5 > var6 : var6 > var5) && (var5 == var0 || var6 == var1)) {
                     var4.add(new _a.t(var5, var6));
                  }
               }
            }

            if (var4.isEmpty()) {
               for (int var7 = 1; var7 <= 9; var7++) {
                  for (int var8 = 1; var8 <= 9; var8++) {
                     if (var2 ? var7 > var8 : var8 > var7) {
                        var4.add(new _a.t(var7, var8));
                     }
                  }
               }
            }

            return (_a.t)var4.get(Math.floorMod(var3, var4.size()));
         } else {
            return new _a.t(var0, var1);
         }
      } else {
         throw new IllegalArgumentException("Ranks must be 1-9");
      }
   }

   public record t(int host, int viewer) {
   }
}
