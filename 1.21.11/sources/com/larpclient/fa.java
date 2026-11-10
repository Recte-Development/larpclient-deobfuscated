package com.larpclient;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.LongPredicate;
import net.minecraft.class_1923;

public final class fa {
   public static final int u = -4;
   public static final int E = 8;
   public static final int F = 625;
   private final Map<Long, byte[][]> Y = new HashMap<>();
   private final Map<Long, BitSet> Z = new HashMap<>();
   private final Set<Long> K = new HashSet<>();
   private static final byte[] p = new byte[2048];

   public void U() {
      this.Y.clear();
      this.Z.clear();
      this.K.clear();
   }

   public void r(long var1) {
      this.Y.remove(var1);
      this.Z.remove(var1);
      this.K.remove(var1);
   }

   public void o(LongPredicate var1) {
      this.Y.keySet().removeIf(var1x -> !var1.test(var1x));
      this.Z.keySet().retainAll(this.Y.keySet());
      this.K.retainAll(this.Y.keySet());
   }

   public int F() {
      return this.Y.size();
   }

   public Set<Long> T() {
      Set var1 = Set.copyOf(this.K);
      this.K.clear();
      return var1;
   }

   public void p(int var1, int var2, int var3, BitSet var4, BitSet var5, List<byte[]> var6) {
      long var7 = class_1923.method_8331(var1, var2);
      if (this.Y.containsKey(var7) || this.Y.size() < 625) {
         byte[][] var9 = this.Y.computeIfAbsent(var7, var0 -> new byte[13][]);
         BitSet var10 = this.Z.computeIfAbsent(var7, var0 -> new BitSet());
         byte var11 = 0;
         int var12 = 0;

         for (int var13 = var4.nextSetBit(0); var13 >= 0; var13 = var4.nextSetBit(var13 + 1)) {
            byte[] var14 = var12 < var6.size() ? (byte[])var6.get(var12) : null;
            var12++;
            int var15 = var3 + var13;
            if (var15 >= -4 && var15 <= 8 && var14 != null && var14.length == 2048) {
               int var16 = var15 - -4;
               var10.set(var16, f(var14));
               if (!Arrays.equals(var9[var16], var14)) {
                  var9[var16] = (byte[])var14.clone();
                  var11 = 1;
               }
            }
         }

         for (int var17 = var5.nextSetBit(0); var17 >= 0; var17 = var5.nextSetBit(var17 + 1)) {
            int var18 = var3 + var17;
            if (var18 >= -4 && var18 <= 8) {
               int var19 = var18 - -4;
               var10.clear(var19);
               if (var9[var19] == null || !Arrays.equals(var9[var19], p)) {
                  var9[var19] = p;
                  var11 = 1;
               }
            }
         }

         if (var11 != 0) {
            this.K.add(var7);
         }
      }
   }

   private static boolean f(byte[] var0) {
      for (byte var4 : var0) {
         if (N(var4 & 15) || N((var4 & 255) >> 4)) {
            return true;
         }
      }

      return false;
   }

   private static boolean N(int var0) {
      return var0 >= 3 && var0 <= 5;
   }

   public boolean w(int var1, int var2, int var3) {
      BitSet var4 = this.Z.get(class_1923.method_8331(var1, var3));
      return var4 != null && var2 >= -4 && var2 <= 8 && var4.get(var2 - -4);
   }

   public int z(int var1, int var2, int var3) {
      byte[][] var4 = this.Y.get(class_1923.method_8331(var1 >> 4, var3 >> 4));
      int var5 = var2 >> 4;
      if (var4 != null && var5 >= -4 && var5 <= 8 && var4[var5 - -4] != null) {
         int var6 = (var2 & 15) << 8 | (var3 & 15) << 4 | var1 & 15;
         int var7 = var4[var5 - -4][var6 >> 1] & 255;
         return var7 >> (var6 & 1) * 4 & 15;
      } else {
         return -1;
      }
   }
}
