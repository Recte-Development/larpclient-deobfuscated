package com.larpclient.module;

import java.util.function.Predicate;

public final class aj extends lb<String> {
   public static final int E = 128;
   private String i = "Menu title";
   private Predicate<String> p = var0 -> false;
   private String I = "";

   public aj(String var1, String var2, String var3) {
      super(var1, var2, N(var3));
   }

   public aj Y(Predicate<String> var1, String var2) {
      this.p = var1;
      this.I = var2;
      return this;
   }

   public String I(String var1) {
      N(var1);
      if (this.p.test(var1)) {
         throw new IllegalArgumentException(this.I);
      } else {
         return var1;
      }
   }

   public aj A(String var1) {
      this.i = var1;
      return this;
   }

   public String m() {
      return this.i;
   }

   public static String N(String var0) {
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

   public void Y(String var1) {
      super.setValue(this.I(var1));
   }

   @Override
   public String getValueAsString() {
      return this.getValue();
   }
}
