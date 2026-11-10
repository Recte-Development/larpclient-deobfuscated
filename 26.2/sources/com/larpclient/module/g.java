package com.larpclient.module;

import java.util.function.Predicate;

public final class g extends q<String> {
   public static final int F = 128;
   private String U = "Menu title";
   private Predicate<String> k = var0 -> false;
   private String l = "";

   public g(String var1, String var2, String var3) {
      super(var1, var2, D(var3));
   }

   public g a(Predicate<String> var1, String var2) {
      this.k = var1;
      this.l = var2;
      return this;
   }

   public String L(String var1) {
      D(var1);
      if (this.k.test(var1)) {
         throw new IllegalArgumentException(this.l);
      } else {
         return var1;
      }
   }

   public g F(String var1) {
      this.U = var1;
      return this;
   }

   public String a() {
      return this.U;
   }

   public static String D(String var0) {
      if (var0 != null && var0.length() <= 128 && !var0.codePoints().anyMatch(Character::isISOControl)) {
         for (int var1 = 0; var1 < var0.length(); var1++) {
            char var2 = var0.charAt(var1);
            if (Character.isHighSurrogate(var2)) {
               var1++;
               if (var1 >= var0.length() || !Character.isLowSurrogate(var0.charAt(var1))) {
                  throw new IllegalArgumentException("Incomplete text character");
               }
            } else if (Character.isLowSurrogate(var2)) {
               throw new IllegalArgumentException("Incomplete text character");
            }
         }

         return var0;
      } else {
         throw new IllegalArgumentException("Expected plain text of at most 128 characters");
      }
   }

   public void k(String var1) {
      super.setValue(this.L(var1));
   }

   @Override
   public String getValueAsString() {
      return this.getValue();
   }
}
