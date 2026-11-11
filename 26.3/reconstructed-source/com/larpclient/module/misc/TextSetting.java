package com.larpclient.module.misc;

import java.util.function.Predicate;

public final class TextSetting extends Setting<String> {
   public static final int mAX_LENGTH = 128;
   private String title = "Menu title";
   private Predicate<String> rejector = var0 -> false;
   private String rejectMessage = "";

   public TextSetting(String var1, String var2, String var3) {
      super(var1, var2, setTitle(var3));
   }

   public TextSetting setRejector(Predicate<String> var1, String var2) {
      this.rejector = var1;
      this.rejectMessage = var2;
      return this;
   }

   public String validate(String var1) {
      setTitle(var1);
      if (this.rejector.test(var1)) {
         throw new IllegalArgumentException(this.rejectMessage);
      } else {
         return var1;
      }
   }

   public TextSetting setTitle(String var1) {
      this.title = var1;
      return this;
   }

   public String getTitle() {
      return this.title;
   }

   public static String setTitle(String var0) {
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

   public void setText(String var1) {
      super.setValue(this.validate(var1));
   }

   @Override
   public String getValueAsString() {
      return this.getValue();
   }
}
