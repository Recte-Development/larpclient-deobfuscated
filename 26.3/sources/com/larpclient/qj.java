package com.larpclient;

import java.util.BitSet;

public final class qj {
   public static final int p = 15;
   public static final int C = 7;
   private static final int n = 225;
   private static final int y = 57;
   public static final qj U = new qj(new BitSet(225));
   public static final qj K;
   private final BitSet w;

   private qj(BitSet var1) {
      this.w = var1;
   }

   private static int Z(int var0, int var1) {
      if (var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15) {
         return var1 * 15 + var0;
      } else {
         throw new IndexOutOfBoundsException("Cell outside the drawing");
      }
   }

   public boolean k(int var1, int var2) {
      return this.w.get(Z(var1, var2));
   }

   public boolean r() {
      return this.w.isEmpty();
   }

   public int n() {
      return this.w.cardinality();
   }

   public qj E(int var1, int var2, boolean var3) {
      int var4 = Z(var1, var2);
      if (this.w.get(var4) == var3) {
         return this;
      }

      BitSet var5 = (BitSet)this.w.clone();
      var5.set(var4, var3);
      return new qj(var5);
   }

   public String G() {
      StringBuilder var1 = new StringBuilder(57);

      for (int var2 = 0; var2 < 57; var2++) {
         int var3 = 0;

         for (int var4 = 0; var4 < 4; var4++) {
            if (this.w.get(var2 * 4 + var4)) {
               var3 |= 8 >> var4;
            }
         }

         var1.append(Character.forDigit(var3, 16));
      }

      return var1.toString();
   }

   public static qj p(String var0) {
      if (var0 != null && var0.length() == 57) {
         BitSet var1 = new BitSet(225);

         for (int var2 = 0; var2 < 57; var2++) {
            int var3 = Character.digit(var0.charAt(var2), 16);
            if (var3 < 0) {
               throw new IllegalArgumentException("Expected hex digits");
            }

            for (int var4 = 0; var4 < 4; var4++) {
               if ((var3 & 8 >> var4) != 0) {
                  if (var2 * 4 + var4 >= 225) {
                     throw new IllegalArgumentException("Cell outside the drawing");
                  }

                  var1.set(var2 * 4 + var4);
               }
            }
         }

         return new qj(var1);
      } else {
         throw new IllegalArgumentException("Expected 57 hex digits");
      }
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof qj var2 && this.w.equals(var2.w);
   }

   @Override
   public int hashCode() {
      return this.w.hashCode();
   }

   static {
      BitSet var0 = new BitSet(225);
      var0.set(Z(7, 7));

      for (int var1 = 2; var1 <= 4; var1++) {
         var0.set(Z(7 - var1, 7));
         var0.set(Z(7 + var1, 7));
         var0.set(Z(7, 7 - var1));
         var0.set(Z(7, 7 + var1));
      }

      K = new qj(var0);
   }
}
