package com.larpclient;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public final class hp {
   static final int K = 256;
   static final int z = 67108864;

   private hp() {
   }

   public static hp.m A(InputStream var0) throws IOException {
      BufferedInputStream var1 = new BufferedInputStream(var0);
      var1.mark(2);
      int var2 = var1.read();
      int var3 = var1.read();
      var1.reset();
      FilterInputStream var4 = var2 == 31 && var3 == 139 ? new GZIPInputStream(var1) : var1;
      DataInputStream var5 = new DataInputStream(var4);
      int var6 = var5.readUnsignedByte();
      if (var6 != 10) {
         throw new IOException("not an NBT file (the first tag is type " + var6 + ", not a compound)");
      }

      String var7 = var5.readUTF();
      Map var8 = (Map)B(var5, 10, 0);
      return new hp.m(var7, var8);
   }

   private static Object B(DataInputStream var0, int var1, int var2) throws IOException {
      if (var2 > 256) {
         throw new IOException("NBT nested too deeply");
      }

      return switch (var1) {
         case 1 -> var0.readByte();
         case 2 -> var0.readShort();
         case 3 -> var0.readInt();
         case 4 -> var0.readLong();
         case 5 -> var0.readFloat();
         case 6 -> var0.readDouble();
         case 7 -> {
            byte[] var10 = new byte[W(var0)];
            var0.readFully(var10);
            yield var10;
         }
         case 8 -> var0.readUTF();
         case 9 -> {
            int var9 = var0.readUnsignedByte();
            int var13 = W(var0);
            ArrayList var14 = new ArrayList(Math.min(var13, 4096));

            for (int var6 = 0; var6 < var13; var6++) {
               var14.add(B(var0, var9, var2 + 1));
            }

            yield var14;
         }
         case 10 -> {
            LinkedHashMap var8 = new LinkedHashMap();

            while (true) {
               int var12 = var0.readUnsignedByte();
               if (var12 == 0) {
                  yield var8;
               }

               String var5 = var0.readUTF();
               var8.put(var5, B(var0, var12, var2 + 1));
            }
         }
         case 11 -> {
            int[] var7 = new int[W(var0)];

            for (int var11 = 0; var11 < var7.length; var11++) {
               var7[var11] = var0.readInt();
            }

            yield var7;
         }
         case 12 -> {
            long[] var3 = new long[W(var0)];

            for (int var4 = 0; var4 < var3.length; var4++) {
               var3[var4] = var0.readLong();
            }

            yield var3;
         }
         default -> throw new IOException("unknown NBT tag type " + var1);
      };
   }

   private static int W(DataInputStream var0) throws IOException {
      int var1 = var0.readInt();
      if (var1 >= 0 && var1 <= 67108864) {
         return var1;
      } else {
         throw new IOException("NBT array length " + var1 + " is out of range");
      }
   }

   public static Map<String, Object> G(Map<String, Object> var0, String var1) {
      return (var0 == null ? null : var0.get(var1)) instanceof Map var3 ? var3 : Map.of();
   }

   public static boolean f(Map<String, Object> var0, String var1) {
      return var0 != null && var0.containsKey(var1);
   }

   public static int Z(Map<String, Object> var0, String var1, int var2) {
      return (var0 == null ? null : var0.get(var1)) instanceof Number var4 ? var4.intValue() : var2;
   }

   public static String N(Map<String, Object> var0, String var1, String var2) {
      return (var0 == null ? null : var0.get(var1)) instanceof String var4 ? var4 : var2;
   }

   public static List<Object> M(Map<String, Object> var0, String var1) {
      return (var0 == null ? null : var0.get(var1)) instanceof List var3 ? var3 : List.of();
   }

   public static long[] H(Map<String, Object> var0, String var1) {
      return (var0 == null ? null : var0.get(var1)) instanceof long[] var3 ? var3 : new long[0];
   }

   public static byte[] b(Map<String, Object> var0, String var1) {
      return (var0 == null ? null : var0.get(var1)) instanceof byte[] var3 ? var3 : new byte[0];
   }

   public static int[] l(Map<String, Object> var0, String var1) {
      return (var0 == null ? null : var0.get(var1)) instanceof int[] var3 ? var3 : new int[0];
   }

   public record m(String name, Map<String, Object> tag) {
   }
}
