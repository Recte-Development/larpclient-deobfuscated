package com.larpclient.module;

public final class ds extends df<String> {
   public static final int y = 128;
   private String t = "Menu title";

   public ds(String var1, String var2, String var3) {
      super(var1, var2, S(var3));
   }

   public ds C(String var1) {
      this.t = var1;
      return this;
   }

   public String X() {
      return this.t;
   }

   public static String S(String var0) {
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

   public void v(String var1) {
      super.setValue(S(var1));
   }

   @Override
   public String getValueAsString() {
      return this.P();
   }
}
