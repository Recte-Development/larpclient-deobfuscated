package com.larpclient.module;

import java.util.function.IntUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_3544;
import org.apache.commons.lang3.StringUtils;

final class l9 {
   private static final Pattern E = Pattern.compile("\\{player}", 2);
   private static final Pattern K = Pattern.compile("/\\s*(?:[\\w.-]+:)?e?pay(?![\\w])", 2);
   private l9.k s;
   private double n;
   private double G = Double.NEGATIVE_INFINITY;

   static boolean Y(String var0) {
      return K.matcher(var0).find();
   }

   static l9.k b(String var0, String var1) {
      boolean var2 = var0.strip().startsWith("/");
      String var3 = E.matcher(var0).replaceAll(Matcher.quoteReplacement(var1));
      String var4 = class_3544.method_43681(StringUtils.normalizeSpace(class_3544.method_57180(var3).strip()));
      if (!Y(var0) && !Y(var4)) {
         if (var2) {
            var4 = var4.substring(1).strip();
         }

         return var4.isEmpty() ? null : new l9.k(var4, var2);
      } else {
         return null;
      }
   }

   static int D(int var0, int var1, boolean var2, IntUnaryOperator var3) {
      if (var0 <= 1) {
         return 0;
      } else if (!var2) {
         return (var1 + 1) % var0;
      } else if (var1 >= 0 && var1 < var0) {
         int var4 = var3.applyAsInt(var0 - 1);
         return var4 >= var1 ? var4 + 1 : var4;
      } else {
         return var3.applyAsInt(var0);
      }
   }

   boolean Z(double var1) {
      return this.s == null && var1 >= this.G;
   }

   void W(l9.k var1, double var2) {
      this.s = var1;
      this.n = var2;
   }

   l9.k S(double var1, double var3) {
      if (this.s != null && !(var1 < this.n)) {
         l9.k var5 = this.s;
         this.s = null;
         this.G = var1 + var3;
         return var5;
      } else {
         return null;
      }
   }

   void v() {
      this.s = null;
   }

   record k(String text, boolean command) {
   }
}
