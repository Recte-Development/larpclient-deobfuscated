package com.larpclient.gui.misc;

import com.larpclient.util.render.Animation;
import java.util.Arrays;

public final class TextFadeAnimator {
   private String text = "";
   private long[] charTimestamps = new long[0];
   private long nowMillis;

   public void update(String var1, long var2, boolean var4) {
      this.nowMillis = var2;
      if (!var4) {
         if (!var1.equals(this.text)) {
            this.charTimestamps = new long[var1.length()];
         }

         this.text = var1;
         Arrays.fill(this.charTimestamps, Long.MIN_VALUE);
      } else if (!var1.equals(this.text)) {
         int var5 = 0;

         while (var5 < this.text.length() && var5 < var1.length() && this.text.codePointAt(var5) == var1.codePointAt(var5)) {
            var5 += Character.charCount(var1.codePointAt(var5));
         }

         int var6 = this.text.length();

         int var7;
         for (var7 = var1.length();
            var6 > var5 && var7 > var5 && this.text.codePointBefore(var6) == var1.codePointBefore(var7);
            var7 -= Character.charCount(var1.codePointBefore(var7))
         ) {
            var6 -= Character.charCount(this.text.codePointBefore(var6));
         }

         long[] var8 = new long[var1.length()];
         System.arraycopy(this.charTimestamps, 0, var8, 0, var5);
         Arrays.fill(var8, var5, var7, var2);
         System.arraycopy(this.charTimestamps, var6, var8, var7, this.text.length() - var6);
         this.text = var1;
         this.charTimestamps = var8;
      }
   }

   public double progress(int var1) {
      return var1 >= 0 && var1 < this.charTimestamps.length && this.charTimestamps[var1] != Long.MIN_VALUE
         ? Animation.Easing.EASE_OUT_CUBIC.apply((this.nowMillis - this.charTimestamps[var1]) / 1.8E8)
         : 1.0;
   }

   public double offset(int var1) {
      return 3.0 * (1.0 - this.progress(var1));
   }

   public int applyAlpha(int var1, int var2) {
      int var3 = var1 >>> 24;
      if (var3 == 0) {
         var3 = 255;
      }

      return var1 & 16777215 | (int)Math.round(var3 * (0.4 + 0.6 * this.progress(var2))) << 24;
   }

   public boolean update() {
      for (int var1 = 0; var1 < this.charTimestamps.length; var1++) {
         if (this.progress(var1) < 1.0) {
            return true;
         }
      }

      return false;
   }

   public int length() {
      return this.charTimestamps.length;
   }

   public static final class CursorCounter {
      private int index;

      public CursorCounter(int var1) {
         this.index = var1;
      }

      public int advance(int var1) {
         int var2 = this.index;
         this.index = this.index + Character.charCount(var1);
         return var2;
      }
   }
}
