package com.larpclient.module;

import java.util.function.IntUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

final class u {
   private static final Pattern K = Pattern.compile("\\{player}", 2);
   private static final Pattern f = Pattern.compile("/\\s*(?:[\\w.-]+:)?e?pay(?![\\w])", 2);
   private u.f W;
   private double F;
   private double i = Double.NEGATIVE_INFINITY;

   static boolean z(String var0) {
      return f.matcher(var0).find();
   }

   static u.f i(String var0, String var1) {
      boolean var2 = var0.strip().startsWith("/");
      String var3 = K.matcher(var0).replaceAll(Matcher.quoteReplacement(var1));
      String var4 = StringUtil.trimChatMessage(StringUtils.normalizeSpace(StringUtil.filterText(var3).strip()));
      if (!z(var0) && !z(var4)) {
         if (var2) {
            var4 = var4.substring(1).strip();
         }

         return var4.isEmpty() ? null : new u.f(var4, var2);
      } else {
         return null;
      }
   }

   static int v(int var0, int var1, boolean var2, IntUnaryOperator var3) {
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

   boolean M(double var1) {
      return this.W == null && var1 >= this.i;
   }

   void G(u.f var1, double var2) {
      this.W = var1;
      this.F = var2;
   }

   u.f U(double var1, double var3) {
      if (this.W != null && !(var1 < this.F)) {
         u.f var5 = this.W;
         this.W = null;
         this.i = var1 + var3;
         return var5;
      } else {
         return null;
      }
   }

   void J() {
      this.W = null;
   }

   record f(String text, boolean command) {
   }
}
