package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.stream.Stream;

public final class n {
   static final Set<String> A = Set.of("minecraft:air", "minecraft:cave_air", "minecraft:void_air", "minecraft:structure_void");
   public static final int W = 4000000;
   private final String q;
   private final String r;
   private final List<String> R;
   private final int[] n;
   private final int[] J;
   private final int[] s;
   private final int[] l;
   private final List<n.q> a;
   private final Map<Integer, Map<String, Object>> v;
   private final int X;
   private final int k;
   private final int U;
   private final int K;
   private final int H;
   private final int u;

   private n(
      String var1, String var2, List<String> var3, int[] var4, int[] var5, int[] var6, int[] var7, List<n.q> var8, Map<Integer, Map<String, Object>> var9
   ) {
      this.q = var1;
      this.r = var2;
      this.R = List.copyOf(var3);
      this.n = var4;
      this.J = var5;
      this.s = var6;
      this.l = var7;
      this.a = List.copyOf(var8);
      this.v = Map.copyOf(var9);
      int var10 = Integer.MAX_VALUE;
      int var11 = Integer.MAX_VALUE;
      int var12 = Integer.MAX_VALUE;
      int var13 = Integer.MIN_VALUE;
      int var14 = Integer.MIN_VALUE;
      int var15 = Integer.MIN_VALUE;

      for (int var16 = 0; var16 < var4.length; var16++) {
         var10 = Math.min(var10, var4[var16]);
         var11 = Math.min(var11, var5[var16]);
         var12 = Math.min(var12, var6[var16]);
         var13 = Math.max(var13, var4[var16]);
         var14 = Math.max(var14, var5[var16]);
         var15 = Math.max(var15, var6[var16]);
      }

      if (var4.length == 0) {
         var15 = 0;
         var14 = 0;
         var13 = 0;
         var12 = 0;
         var11 = 0;
         var10 = 0;
      }

      this.X = var10;
      this.k = var11;
      this.U = var12;
      this.K = var13;
      this.H = var14;
      this.u = var15;
   }

   public String D() {
      return this.q;
   }

   public String U() {
      return this.r;
   }

   public List<String> a() {
      return this.R;
   }

   public List<n.q> Y() {
      return this.a;
   }

   public int I() {
      return this.n.length;
   }

   public int s(int var1) {
      return this.n[var1];
   }

   public int r(int var1) {
      return this.J[var1];
   }

   public int F(int var1) {
      return this.s[var1];
   }

   public int D(int var1) {
      return this.l[var1];
   }

   public String j(int var1) {
      return this.R.get(this.l[var1]);
   }

   public Map<String, Object> k(int var1) {
      return this.v.get(var1);
   }

   public int[] w(int var1) {
      Map var2 = this.v.get(var1);
      return var2 == null ? new int[0] : Arrays.stream(c8.Q(var2, "disabled_slots")).filter(var0 -> var0 >= 0 && var0 < 9).distinct().sorted().toArray();
   }

   public int i() {
      return this.X;
   }

   public int p() {
      return this.k;
   }

   public int Q() {
      return this.U;
   }

   public int N() {
      return this.K;
   }

   public int E() {
      return this.H;
   }

   public int h() {
      return this.u;
   }

   public int r() {
      return this.n.length == 0 ? 0 : this.K - this.X + 1;
   }

   public int C() {
      return this.n.length == 0 ? 0 : this.H - this.k + 1;
   }

   public int X() {
      return this.n.length == 0 ? 0 : this.u - this.U + 1;
   }

   public Map<String, Integer> S() {
      HashMap var1 = new HashMap();

      for (int var5 : this.l) {
         var1.merge(s(this.R.get(var5)), 1, Integer::sum);
      }

      ArrayList var6 = new ArrayList(var1.entrySet());
      var6.sort(
         (var0, var1x) -> ((Integer)var0.getValue()).equals(var1x.getValue())
            ? ((String)var0.getKey()).compareTo((String)var1x.getKey())
            : (Integer)var1x.getValue() - (Integer)var0.getValue()
      );
      LinkedHashMap var7 = new LinkedHashMap();

      for (Entry var9 : var6) {
         var7.put((String)var9.getKey(), (Integer)var9.getValue());
      }

      return var7;
   }

   public String E() {
      return String.format(Locale.ROOT, "%s, %d x %d x %d, %,d blocks", this.q, this.r(), this.C(), this.X(), this.I());
   }

   public static String s(String var0) {
      int var1 = var0.indexOf(91);
      return var1 < 0 ? var0 : var0.substring(0, var1);
   }

   public static Map<String, String> F(String var0) {
      TreeMap var1 = new TreeMap();
      int var2 = var0.indexOf(91);
      int var3 = var0.lastIndexOf(93);
      if (var2 >= 0 && var3 >= var2) {
         for (String var7 : var0.substring(var2 + 1, var3).split(",")) {
            int var8 = var7.indexOf(61);
            if (var8 > 0) {
               var1.put(var7.substring(0, var8).trim(), var7.substring(var8 + 1).trim());
            }
         }

         return var1;
      } else {
         return var1;
      }
   }

   public static String U(String var0, Map<String, String> var1) {
      String var2 = var0.trim().toLowerCase(Locale.ROOT);
      if (!var2.contains(":")) {
         var2 = "minecraft:" + var2;
      }

      if (var1 != null && !var1.isEmpty()) {
         TreeMap var3 = new TreeMap(var1);
         StringBuilder var4 = new StringBuilder(var2).append('[');
         byte var5 = 1;

         for (Entry var7 : var3.entrySet()) {
            if (var5 == 0) {
               var4.append(',');
            }

            var5 = 0;
            var4.append(((String)var7.getKey()).toLowerCase(Locale.ROOT)).append('=').append(((String)var7.getValue()).toLowerCase(Locale.ROOT));
         }

         return var4.append(']').toString();
      } else {
         return var2;
      }
   }

   public static String k(String var0) {
      return U(s(var0), F(var0));
   }

   static boolean y(String var0) {
      return A.contains(s(var0));
   }

   public static n W(Path var0) throws IOException {
      String var1 = var0.getFileName().toString();

      try (InputStream var2 = Files.newInputStream(var0)) {
         return a(var1, var2);
      }
   }

   public static n a(String var0, InputStream var1) throws IOException {
      String var2 = var0.toLowerCase(Locale.ROOT);
      String var3 = var0.replaceFirst("(?i)\\.(litematic|schematic|schem|nbt)$", "");
      if (var2.endsWith(".schematic")) {
         throw new IOException("old .schematic files (MCEdit and WorldEdit before 1.13) are not supported; save it as .litematic or .schem");
      } else {
         c8.x var4 = c8.t(var1);
         if (var2.endsWith(".litematic")) {
            return I(var4.tag(), var3);
         } else if (var2.endsWith(".schem")) {
            return c(var4, var3);
         } else {
            throw new IOException("unknown schematic type; use a .litematic or .schem file");
         }
      }
   }

   public static n I(Map<String, Object> var0, String var1) throws IOException {
      Map var2 = c8.O(var0, "Metadata");
      String var3 = c8.J(var2, "Name", "");
      if (var3.isBlank()) {
         var3 = var1;
      }

      Map var4 = c8.O(var0, "Regions");
      if (var4.isEmpty()) {
         throw new IOException("this .litematic file has no regions");
      }

      n.s var5 = new n.s();
      ArrayList var6 = new ArrayList();

      for (Entry var8 : var4.entrySet()) {
         if (var8.getValue() instanceof Map) {
            Map var9 = (Map)var8.getValue();
            Map var10 = c8.O(var9, "Position");
            Map var11 = c8.O(var9, "Size");
            int var12 = c8.H(var10, "x", 0);
            int var13 = c8.H(var10, "y", 0);
            int var14 = c8.H(var10, "z", 0);
            int var15 = c8.H(var11, "x", 0);
            int var16 = c8.H(var11, "y", 0);
            int var17 = c8.H(var11, "z", 0);
            int var18 = Math.abs(var15);
            int var19 = Math.abs(var16);
            int var20 = Math.abs(var17);
            if (var18 != 0 && var19 != 0 && var20 != 0) {
               if ((long)var18 * var19 * var20 > 4000000L) {
                  throw new IOException("region " + (String)var8.getKey() + " is too big (" + var18 + " x " + var19 + " x " + var20 + ")");
               }

               int var21 = var15 < 0 ? var12 + var15 + 1 : var12;
               int var22 = var16 < 0 ? var13 + var16 + 1 : var13;
               int var23 = var17 < 0 ? var14 + var17 + 1 : var14;
               List var24 = c8.U(var9, "BlockStatePalette");
               if (var24.isEmpty()) {
                  throw new IOException("region " + (String)var8.getKey() + " has no block palette");
               }

               int[] var25 = new int[var24.size()];

               for (int var26 = 0; var26 < var24.size(); var26++) {
                  Map var27 = var24.get(var26) instanceof Map var28 ? var28 : Map.of();
                  TreeMap var38 = new TreeMap();

                  for (Entry var30 : c8.O(var27, "Properties").entrySet()) {
                     if (var30.getValue() instanceof String var31) {
                        var38.put((String)var30.getKey(), var31);
                     }
                  }

                  String var41 = U(c8.J(var27, "Name", "minecraft:air"), var38);
                  var25[var26] = y(var41) ? -1 : var5.C(var41);
               }

               long[] var36 = c8.n(var9, "BlockStates");
               int var37 = Math.max(2, 32 - Integer.numberOfLeadingZeros(Math.max(1, var24.size() - 1)));
               long var39 = (long)var18 * var19 * var20;
               if (var36.length < (var39 * var37 + 63L) / 64L) {
                  throw new IOException("region " + (String)var8.getKey() + " has too little block data");
               }

               int var42 = 0;

               for (int var43 = 0; var43 < var19; var43++) {
                  for (int var45 = 0; var45 < var20; var45++) {
                     for (int var33 = 0; var33 < var18; var33++) {
                        int var34 = (var43 * var20 + var45) * var18 + var33;
                        int var35 = (int)I(var36, var37, var34);
                        if (var35 < 0 || var35 >= var25.length) {
                           throw new IOException("region " + (String)var8.getKey() + " points outside its palette");
                        }

                        if (var25[var35] >= 0) {
                           var5.h(var21 + var33, var22 + var43, var23 + var45, var25[var35]);
                           var42++;
                        }
                     }
                  }
               }

               for (Object var46 : c8.U(var9, "TileEntities")) {
                  if (var46 instanceof Map var47) {
                     Map var48 = var47;
                     var5.H(var21 + c8.H(var48, "x", 0), var22 + c8.H(var48, "y", 0), var23 + c8.H(var48, "z", 0), var48);
                  }
               }

               var6.add(new n.q((String)var8.getKey(), var21, var22, var23, var18, var19, var20, var42));
            }
         }
      }

      return var5.l(var3, "Litematica", var6);
   }

   static long I(long[] var0, int var1, int var2) {
      long var3 = (long)var2 * var1;
      int var5 = (int)(var3 >> 6);
      int var6 = (int)((long)(var2 + 1) * var1 - 1L >> 6);
      int var7 = (int)(var3 & 63L);
      long var8 = (1L << var1) - 1L;
      return var5 == var6 ? var0[var5] >>> var7 & var8 : (var0[var5] >>> var7 | var0[var6] << 64 - var7) & var8;
   }

   public static n c(c8.x var0, String var1) throws IOException {
      Map var2 = var0.tag();
      if (c8.o(var2, "Schematic")) {
         var2 = c8.O(var2, "Schematic");
      }

      int var3 = c8.H(var2, "Version", 2);
      int var4 = c8.H(var2, "Width", 0) & 65535;
      int var5 = c8.H(var2, "Height", 0) & 65535;
      int var6 = c8.H(var2, "Length", 0) & 65535;
      if (var4 != 0 && var5 != 0 && var6 != 0) {
         if ((long)var4 * var5 * var6 > 4000000L) {
            throw new IOException("this .schem file is too big (" + var4 + " x " + var5 + " x " + var6 + ")");
         }

         Map var7;
         byte[] var8;
         if (var3 >= 3) {
            Map var9 = c8.O(var2, "Blocks");
            var7 = c8.O(var9, "Palette");
            var8 = c8.V(var9, "Data");
         } else {
            var7 = c8.O(var2, "Palette");
            var8 = c8.V(var2, "BlockData");
         }

         if (var7.isEmpty()) {
            throw new IOException("this .schem file has no block palette");
         }

         n.s var23 = new n.s();
         HashMap var10 = new HashMap();

         for (Entry var12 : var7.entrySet()) {
            if (var12.getValue() instanceof Number var13) {
               String var27 = k((String)var12.getKey());
               var10.put(var13.intValue(), y(var27) ? -1 : var23.C(var27));
            }
         }

         String var24 = c8.J(c8.O(var2, "Metadata"), "Name", "");
         if (var24.isBlank()) {
            var24 = var1;
         }

         int var25 = 0;
         int var26 = 0;
         int var28 = 0;

         label101:
         for (int var15 = var4 * var5 * var6; var25 < var15; var25++) {
            if (var26 >= var8.length) {
               throw new IOException("this .schem file has too little block data");
            }

            int var16 = 0;
            byte var17 = 0;

            while (var26 < var8.length) {
               byte var18 = var8[var26++];
               var16 |= (var18 & 127) << var17;
               if ((var18 & 128) == 0) {
                  Integer var32 = (Integer)var10.get(var16);
                  if (var32 == null) {
                     throw new IOException("this .schem file uses a block id missing from its palette");
                  }

                  if (var32 >= 0) {
                     int var19 = var25 / (var4 * var6);
                     int var20 = var25 % (var4 * var6);
                     int var21 = var20 / var4;
                     int var22 = var20 % var4;
                     var23.h(var22, var19, var21, var32);
                     var28++;
                  }
                  continue label101;
               }

               var17 += 7;
               if (var17 > 28) {
                  throw new IOException("this .schem file has a broken block id");
               }
            }

            throw new IOException("this .schem file ends inside a block id");
         }

         for (Object var33 : var3 >= 3 ? c8.U(c8.O(var2, "Blocks"), "BlockEntities") : c8.U(var2, "BlockEntities")) {
            if (var33 instanceof Map var34) {
               Map var35 = var34;
               int[] var36 = c8.Q(var35, "Pos");
               if (var36.length == 3) {
                  var23.H(var36[0], var36[1], var36[2], var3 >= 3 && c8.o(var35, "Data") ? c8.O(var35, "Data") : var35);
               }
            }
         }

         n.q var31 = new n.q("Main", 0, 0, 0, var4, var5, var6, var28);
         return var23.l(var24, "Sponge v" + var3, List.of(var31));
      } else {
         throw new IOException("this .schem file has no size");
      }
   }

   public static List<String> R(Path var0) {
      if (!Files.isDirectory(var0)) {
         return List.of();
      }

      ArrayList var1 = new ArrayList();

      try (Stream var2 = Files.list(var0)) {
         var2.filter(var0x -> Files.isRegularFile(var0x)).map(var0x -> var0x.getFileName().toString()).filter(var0x -> {
            String var1x = var0x.toLowerCase(Locale.ROOT);
            return var1x.endsWith(".litematic") || var1x.endsWith(".schem") || var1x.endsWith(".schematic");
         }).forEach(var1::add);
      } catch (IOException var7) {
         return List.of();
      }

      Collections.sort(var1, String.CASE_INSENSITIVE_ORDER);
      return var1;
   }

   public enum k {
      M,
      I,
      J;
   }

   public record q(String name, int x, int y, int z, int sizeX, int sizeY, int sizeZ, int cells) {
   }

   private static final class s {
      final List<String> s = new ArrayList<>();
      final Map<String, Integer> z = new HashMap<>();
      final Map<Long, Integer> Y = new HashMap<>();
      final Map<Long, Map<String, Object>> i = new HashMap<>();
      int[] J = new int[1024];
      int[] S = new int[1024];
      int[] h = new int[1024];
      int[] g = new int[1024];
      int v;

      int C(String var1) {
         return this.z.computeIfAbsent(var1, var1x -> {
            this.s.add(var1x);
            return this.s.size() - 1;
         });
      }

      void h(int var1, int var2, int var3, int var4) throws IOException {
         long var5 = g8.M(var1, var2, var3);
         Integer var7 = this.Y.get(var5);
         if (var7 != null) {
            this.g[var7] = var4;
         } else {
            if (this.v >= 4000000) {
               throw new IOException("this schematic has more than 4000000 blocks");
            }

            if (this.v == this.J.length) {
               int var8 = this.J.length * 2;
               this.J = Arrays.copyOf(this.J, var8);
               this.S = Arrays.copyOf(this.S, var8);
               this.h = Arrays.copyOf(this.h, var8);
               this.g = Arrays.copyOf(this.g, var8);
            }

            this.J[this.v] = var1;
            this.S[this.v] = var2;
            this.h[this.v] = var3;
            this.g[this.v] = var4;
            this.Y.put(var5, this.v++);
         }
      }

      void H(int var1, int var2, int var3, Map<String, Object> var4) {
         this.i.put(g8.M(var1, var2, var3), var4);
      }

      n l(String var1, String var2, List<n.q> var3) throws IOException {
         if (this.v == 0) {
            throw new IOException("this schematic has no blocks");
         }

         HashMap var4 = new HashMap();

         for (Entry var6 : this.i.entrySet()) {
            Integer var7 = this.Y.get(var6.getKey());
            if (var7 != null) {
               var4.put(var7, (Map)var6.getValue());
            }
         }

         return new n(
            var1,
            var2,
            this.s,
            Arrays.copyOf(this.J, this.v),
            Arrays.copyOf(this.S, this.v),
            Arrays.copyOf(this.h, this.v),
            Arrays.copyOf(this.g, this.v),
            var3,
            var4
         );
      }
   }

   public record v(int originX, int originY, int originZ, int rotation, n.k mirror) {
      public v(int originX, int originY, int originZ, int rotation, n.k mirror) {
         rotation = Math.floorMod(rotation, 360) / 90 * 90;
         if (mirror == null) {
            mirror = n.k.M;
         }

         this.originX = originX;
         this.originY = originY;
         this.originZ = originZ;
         this.rotation = rotation;
         this.mirror = mirror;
      }

      public int[] world(int var1, int var2, int var3) {
         if (this.mirror == n.k.I) {
            var1 = -var1;
         } else if (this.mirror == n.k.J) {
            var3 = -var3;
         }

         int var4;
         int var5;
         switch (this.rotation) {
            case 90:
               var4 = -var3;
               var5 = var1;
               break;
            case 180:
               var4 = -var1;
               var5 = -var3;
               break;
            case 270:
               var4 = var3;
               var5 = -var1;
               break;
            default:
               var4 = var1;
               var5 = var3;
         }

         return new int[]{this.originX + var4, this.originY + var2, this.originZ + var5};
      }

      public n.v moved(int var1, int var2, int var3) {
         return new n.v(this.originX + var1, this.originY + var2, this.originZ + var3, this.rotation, this.mirror);
      }

      public int[] bounds(n var1) {
         int[] var2 = this.world(var1.i(), var1.p(), var1.Q());
         int[] var3 = this.world(var1.N(), var1.E(), var1.h());
         return new int[]{
            Math.min(var2[0], var3[0]),
            Math.min(var2[1], var3[1]),
            Math.min(var2[2], var3[2]),
            Math.max(var2[0], var3[0]),
            Math.max(var2[1], var3[1]),
            Math.max(var2[2], var3[2])
         };
      }

      public String origin() {
         return this.originX + " " + this.originY + " " + this.originZ;
      }
   }
}
