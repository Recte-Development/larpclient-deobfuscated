package com.larpclient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.LongPredicate;
import net.minecraft.class_1923;
import net.minecraft.class_2338;

public final class sj {
   public static final int H = 625;
   public static final int q = 4096;
   private static final byte[] A = new byte[2048];
   private final LinkedHashMap<Long, byte[][]> h = new LinkedHashMap<>();
   private final LinkedHashMap<Long, sj.l> k = new LinkedHashMap<>();

   public void t() {
      this.h.clear();
      this.k.clear();
   }

   public int f() {
      return this.h.size();
   }

   public int n() {
      return this.k.size();
   }

   public boolean c(long var1) {
      return this.h.containsKey(var1);
   }

   public void N(long var1) {
      this.h.remove(var1);
      this.k.values().removeIf(var2 -> class_1923.method_8331(var2.pos().method_10263() >> 4, var2.pos().method_10260() >> 4) == var1);
   }

   public void B(LongPredicate var1) {
      this.h.keySet().removeIf(var1x -> !var1.test(var1x));
      this.k.values().removeIf(var1x -> !var1.test(class_1923.method_8331(var1x.pos().method_10263() >> 4, var1x.pos().method_10260() >> 4)));
   }

   public void m(int var1, int var2, int var3, BitSet var4, BitSet var5, List<byte[]> var6, boolean var7) {
      long var8 = class_1923.method_8331(var1, var2);
      if (var7) {
         this.N(var8);
      }

      byte[][] var10 = this.h.computeIfAbsent(var8, var0 -> new byte[4][]);

      while (this.h.size() > 625) {
         this.N(this.h.keySet().iterator().next());
      }

      int var11 = 0;

      for (int var12 = var4.nextSetBit(0); var12 >= 0; var12 = var4.nextSetBit(var12 + 1)) {
         byte[] var13 = var11 < var6.size() ? (byte[])var6.get(var11) : null;
         var11++;
         int var14 = var3 + var12;
         if (var14 >= -4 && var14 < 0 && var13 != null && var13.length == 2048) {
            this.O(var10, var1, var2, var14, var13, !var7);
         }
      }

      for (int var15 = var5.nextSetBit(0); var15 >= 0; var15 = var5.nextSetBit(var15 + 1)) {
         int var16 = var3 + var15;
         if (var16 >= -4 && var16 < 0) {
            this.O(var10, var1, var2, var16, A, !var7);
         }
      }
   }

   private void O(byte[][] var1, int var2, int var3, int var4, byte[] var5, boolean var6) {
      byte[] var7 = var1[var4 + 4];
      if (var6 && var7 != null && !Arrays.equals(var7, var5)) {
         for (int var8 = 0; var8 < 4096; var8++) {
            int var9 = (var8 & 1) * 4;
            int var10 = var7[var8 >> 1] >> var9 & 15;
            int var11 = var5[var8 >> 1] >> var9 & 15;
            if (var10 != var11) {
               class_2338 var12 = new class_2338((var2 << 4) + (var8 & 15), (var4 << 4) + (var8 >> 8), (var3 << 4) + (var8 >> 4 & 15));
               this.k.put(var12.method_10063(), new sj.l(var12, var10, var11));
               if (this.k.size() > 4096) {
                  this.k.remove(this.k.keySet().iterator().next());
               }
            }
         }
      }

      var1[var4 + 4] = var5 == A ? A : (byte[])var5.clone();
   }

   public List<sj.l> W(int var1) {
      ArrayList var2 = new ArrayList();
      Iterator var3 = this.k.values().iterator();

      while (var2.size() < var1 && var3.hasNext()) {
         var2.add((sj.l)var3.next());
         var3.remove();
      }

      return List.copyOf(var2);
   }

   public record l(class_2338 pos, int before, int after) {
   }
}
