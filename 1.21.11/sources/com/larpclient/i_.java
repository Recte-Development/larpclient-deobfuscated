package com.larpclient;

import java.util.BitSet;

public final class i_ {
   public static final int i = 15;
   public static final int B = 7;
   private static final int J = 225;
   private static final int H = 57;
   public static final i_ p = new i_(new BitSet(225));
   public static final i_ y;
   private final BitSet P;

   private i_(BitSet var1) {
      this.P = var1;
   }

   private static int S(int var0, int var1) {
      if (var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15) {
         return var1 * 15 + var0;
      } else {
         throw new IndexOutOfBoundsException("Cell outside the drawing");
      }
   }

   public boolean J(int var1, int var2) {
      return this.P.get(S(var1, var2));
   }

   public boolean u() {
      return this.P.isEmpty();
   }

   public int n() {
      return this.P.cardinality();
   }

   public i_ u(int var1, int var2, boolean var3) {
      int var4 = S(var1, var2);
      if (this.P.get(var4) == var3) {
         return this;
      }

      BitSet var5 = (BitSet)this.P.clone();
      var5.set(var4, var3);
      return new i_(var5);
   }

   public String q() {
      StringBuilder var1 = new StringBuilder(57);

      for (int var2 = 0; var2 < 57; var2++) {
         int var3 = 0;

         for (int var4 = 0; var4 < 4; var4++) {
            if (this.P.get(var2 * 4 + var4)) {
               var3 |= 8 >> var4;
            }
         }

         var1.append(Character.forDigit(var3, 16));
      }

      return var1.toString();
   }

   public static i_ E(String var0) {
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

         return new i_(var1);
      } else {
         throw new IllegalArgumentException("Expected 57 hex digits");
      }
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof i_ var2 && this.P.equals(var2.P);
   }

   @Override
   public int hashCode() {
      return this.P.hashCode();
   }

   static {
      BitSet var0 = new BitSet(225);
      var0.set(S(7, 7));

      for (int var1 = 2; var1 <= 4; var1++) {
         var0.set(S(7 - var1, 7));
         var0.set(S(7 + var1, 7));
         var0.set(S(7, 7 - var1));
         var0.set(S(7, 7 + var1));
      }

      y = new i_(var0);
   }
}
