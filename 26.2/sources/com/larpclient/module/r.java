package com.larpclient.module;

import java.util.function.IntUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

final class r {
   private static final Pattern A = Pattern.compile("\\{player}", 2);
   private static final Pattern a = Pattern.compile("/\\s*(?:[\\w.-]+:)?e?pay(?![\\w])", 2);
   private r.k v;
   private double L;
   private double l = Double.NEGATIVE_INFINITY;

   static boolean M(String var0) {
      return a.matcher(var0).find();
   }

   static r.k p(String var0, String var1) {
      boolean var2 = var0.strip().startsWith("/");
      String var3 = A.matcher(var0).replaceAll(Matcher.quoteReplacement(var1));
      String var4 = StringUtil.trimChatMessage(StringUtils.normalizeSpace(StringUtil.filterText(var3).strip()));
      if (!M(var0) && !M(var4)) {
         if (var2) {
            var4 = var4.substring(1).strip();
         }

         return var4.isEmpty() ? null : new r.k(var4, var2);
      } else {
         return null;
      }
   }

   static int F(int var0, int var1, boolean var2, IntUnaryOperator var3) {
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

   boolean h(double var1) {
      return this.v == null && var1 >= this.l;
   }

   void a(r.k var1, double var2) {
      this.v = var1;
      this.L = var2;
   }

   r.k i(double var1, double var3) {
      if (this.v != null && !(var1 < this.L)) {
         r.k var5 = this.v;
         this.v = null;
         this.l = var1 + var3;
         return var5;
      } else {
         return null;
      }
   }

   void o() {
      this.v = null;
   }

   record k(String text, boolean command) {
   }
}
