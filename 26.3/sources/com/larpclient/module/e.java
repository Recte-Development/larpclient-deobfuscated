package com.larpclient.module;

import java.util.function.Predicate;

public final class e extends r<String> {
   public static final int D = 128;
   private String T = "Menu title";
   private Predicate<String> b = var0 -> false;
   private String z = "";

   public e(String var1, String var2, String var3) {
      super(var1, var2, L(var3));
   }

   public e F(Predicate<String> var1, String var2) {
      this.b = var1;
      this.z = var2;
      return this;
   }

   public String a(String var1) {
      L(var1);
      if (this.b.test(var1)) {
         throw new IllegalArgumentException(this.z);
      } else {
         return var1;
      }
   }

   public e L(String var1) {
      this.T = var1;
      return this;
   }

   public String N() {
      return this.T;
   }

   public static String L(String var0) {
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

   public void T(String var1) {
      super.setValue(this.a(var1));
   }

   @Override
   public String getValueAsString() {
      return this.getValue();
   }
}
