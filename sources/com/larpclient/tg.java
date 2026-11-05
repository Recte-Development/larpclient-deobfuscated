package com.larpclient;

import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.LongPredicate;
import net.minecraft.world.level.ChunkPos;

public final class tg {
   public static final int c = -4;
   public static final int M = 8;
   public static final int V = 625;
   private final Map<Long, byte[][]> R = new HashMap<>();
   private final Set<Long> m = new HashSet<>();

   public void w() {
      this.R.clear();
      this.m.clear();
   }

   public void y(long var1) {
      this.R.remove(var1);
      this.m.remove(var1);
   }

   public void i(LongPredicate var1) {
      this.R.keySet().removeIf(var1x -> !var1.test(var1x));
      this.m.retainAll(this.R.keySet());
   }

   public int V() {
      return this.R.size();
   }

   public Set<Long> Q() {
      Set var1 = Set.copyOf(this.m);
      this.m.clear();
      return var1;
   }

   public void r(int var1, int var2, int var3, BitSet var4, BitSet var5, List<byte[]> var6) {
      long var7 = ChunkPos.pack(var1, var2);
      if (this.R.containsKey(var7) || this.R.size() < 625) {
         byte[][] var9 = this.R.computeIfAbsent(var7, var0 -> new byte[13][]);
         byte var10 = 0;
         int var11 = 0;

         for (int var12 = var4.nextSetBit(0); var12 >= 0; var12 = var4.nextSetBit(var12 + 1)) {
            byte[] var13 = var11 < var6.size() ? (byte[])var6.get(var11) : null;
            var11++;
            int var14 = var3 + var12;
            if (var14 >= -4 && var14 <= 8 && var13 != null && var13.length == 2048) {
               int var15 = var14 - -4;
               byte[] var16 = M(var13) ? var13 : null;
               if (!Arrays.equals(var9[var15], var16)) {
                  var9[var15] = var16 == null ? null : (byte[])var16.clone();
                  var10 = 1;
               }
            }
         }

         for (int var17 = var5.nextSetBit(0); var17 >= 0; var17 = var5.nextSetBit(var17 + 1)) {
            int var18 = var3 + var17;
            if (var18 >= -4 && var18 <= 8) {
               int var19 = var18 - -4;
               if (var9[var19] != null) {
                  var9[var19] = null;
                  var10 = 1;
               }
            }
         }

         if (var10 != 0) {
            this.m.add(var7);
         }
      }
   }

   private static boolean M(byte[] var0) {
      for (byte var4 : var0) {
         if ((var4 & 15) == 5 || (var4 & 255) >> 4 == 5) {
            return true;
         }
      }

      return false;
   }

   public boolean i(int var1, int var2, int var3) {
      byte[][] var4 = this.R.get(ChunkPos.pack(var1, var3));
      return var4 != null && var2 >= -4 && var2 <= 8 && var4[var2 - -4] != null;
   }

   public int s(int var1, int var2, int var3) {
      byte[][] var4 = this.R.get(ChunkPos.pack(var1 >> 4, var3 >> 4));
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
