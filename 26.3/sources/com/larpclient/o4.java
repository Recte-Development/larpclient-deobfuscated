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

public final class o4 {
   static final Set<String> W = Set.of("minecraft:air", "minecraft:cave_air", "minecraft:void_air", "minecraft:structure_void");
   public static final int M = 4000000;
   private final String K;
   private final String S;
   private final List<String> p;
   private final int[] q;
   private final int[] r;
   private final int[] d;
   private final int[] y;
   private final List<o4.s> N;
   private final Map<Integer, Map<String, Object>> A;
   private final int Y;
   private final int l;
   private final int X;
   private final int G;
   private final int n;
   private final int J;

   private o4(
      String var1, String var2, List<String> var3, int[] var4, int[] var5, int[] var6, int[] var7, List<o4.s> var8, Map<Integer, Map<String, Object>> var9
   ) {
      this.K = var1;
      this.S = var2;
      this.p = List.copyOf(var3);
      this.q = var4;
      this.r = var5;
      this.d = var6;
      this.y = var7;
      this.N = List.copyOf(var8);
      this.A = Map.copyOf(var9);
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

      this.Y = var10;
      this.l = var11;
      this.X = var12;
      this.G = var13;
      this.n = var14;
      this.J = var15;
   }

   public String e() {
      return this.K;
   }

   public String v() {
      return this.S;
   }

   public List<String> u() {
      return this.p;
   }

   public List<o4.s> g() {
      return this.N;
   }

   public int B() {
      return this.q.length;
   }

   public int j(int var1) {
      return this.q[var1];
   }

   public int v(int var1) {
      return this.r[var1];
   }

   public int P(int var1) {
      return this.d[var1];
   }

   public int Y(int var1) {
      return this.y[var1];
   }

   public String Q(int var1) {
      return this.p.get(this.y[var1]);
   }

   public Map<String, Object> E(int var1) {
      return this.A.get(var1);
   }

   public int[] I(int var1) {
      Map var2 = this.A.get(var1);
      return var2 == null ? new int[0] : Arrays.stream(hp.l(var2, "disabled_slots")).filter(var0 -> var0 >= 0 && var0 < 9).distinct().sorted().toArray();
   }

   public int O() {
      return this.Y;
   }

   public int K() {
      return this.l;
   }

   public int c() {
      return this.X;
   }

   public int D() {
      return this.G;
   }

   public int A() {
      return this.n;
   }

   public int z() {
      return this.J;
   }

   public int g() {
      return this.q.length == 0 ? 0 : this.G - this.Y + 1;
   }

   public int C() {
      return this.q.length == 0 ? 0 : this.n - this.l + 1;
   }

   public int L() {
      return this.q.length == 0 ? 0 : this.J - this.X + 1;
   }

   public Map<String, Integer> o() {
      HashMap var1 = new HashMap();

      for (int var5 : this.y) {
         var1.merge(D(this.p.get(var5)), 1, Integer::sum);
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

   public String Y() {
      return String.format(Locale.ROOT, "%s, %d x %d x %d, %,d blocks", this.K, this.g(), this.C(), this.L(), this.B());
   }

   public static String D(String var0) {
      int var1 = var0.indexOf(91);
      return var1 < 0 ? var0 : var0.substring(0, var1);
   }

   public static Map<String, String> y(String var0) {
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

   public static String C(String var0, Map<String, String> var1) {
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

   public static String f(String var0) {
      return C(D(var0), y(var0));
   }

   static boolean R(String var0) {
      return W.contains(D(var0));
   }

   public static o4 n(Path var0) throws IOException {
      String var1 = var0.getFileName().toString();

      try (InputStream var2 = Files.newInputStream(var0)) {
         return s(var1, var2);
      }
   }

   public static o4 s(String var0, InputStream var1) throws IOException {
      String var2 = var0.toLowerCase(Locale.ROOT);
      String var3 = var0.replaceFirst("(?i)\\.(litematic|schematic|schem|nbt)$", "");
      if (var2.endsWith(".schematic")) {
         throw new IOException("old .schematic files (MCEdit and WorldEdit before 1.13) are not supported; save it as .litematic or .schem");
      } else {
         hp.m var4 = hp.A(var1);
         if (var2.endsWith(".litematic")) {
            return l(var4.tag(), var3);
         } else if (var2.endsWith(".schem")) {
            return J(var4, var3);
         } else {
            throw new IOException("unknown schematic type; use a .litematic or .schem file");
         }
      }
   }

   public static o4 l(Map<String, Object> var0, String var1) throws IOException {
      Map var2 = hp.G(var0, "Metadata");
      String var3 = hp.N(var2, "Name", "");
      if (var3.isBlank()) {
         var3 = var1;
      }

      Map var4 = hp.G(var0, "Regions");
      if (var4.isEmpty()) {
         throw new IOException("this .litematic file has no regions");
      }

      o4.i var5 = new o4.i();
      ArrayList var6 = new ArrayList();

      for (Entry var8 : var4.entrySet()) {
         if (var8.getValue() instanceof Map) {
            Map var9 = (Map)var8.getValue();
            Map var10 = hp.G(var9, "Position");
            Map var11 = hp.G(var9, "Size");
            int var12 = hp.Z(var10, "x", 0);
            int var13 = hp.Z(var10, "y", 0);
            int var14 = hp.Z(var10, "z", 0);
            int var15 = hp.Z(var11, "x", 0);
            int var16 = hp.Z(var11, "y", 0);
            int var17 = hp.Z(var11, "z", 0);
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
               List var24 = hp.M(var9, "BlockStatePalette");
               if (var24.isEmpty()) {
                  throw new IOException("region " + (String)var8.getKey() + " has no block palette");
               }

               int[] var25 = new int[var24.size()];

               for (int var26 = 0; var26 < var24.size(); var26++) {
                  Map var27 = var24.get(var26) instanceof Map var28 ? var28 : Map.of();
                  TreeMap var38 = new TreeMap();

                  for (Entry var30 : hp.G(var27, "Properties").entrySet()) {
                     if (var30.getValue() instanceof String var31) {
                        var38.put((String)var30.getKey(), var31);
                     }
                  }

                  String var41 = C(hp.N(var27, "Name", "minecraft:air"), var38);
                  var25[var26] = R(var41) ? -1 : var5.R(var41);
               }

               long[] var36 = hp.H(var9, "BlockStates");
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
                        int var35 = (int)h(var36, var37, var34);
                        if (var35 < 0 || var35 >= var25.length) {
                           throw new IOException("region " + (String)var8.getKey() + " points outside its palette");
                        }

                        if (var25[var35] >= 0) {
                           var5.r(var21 + var33, var22 + var43, var23 + var45, var25[var35]);
                           var42++;
                        }
                     }
                  }
               }

               for (Object var46 : hp.M(var9, "TileEntities")) {
                  if (var46 instanceof Map var47) {
                     Map var48 = var47;
                     var5.Y(var21 + hp.Z(var48, "x", 0), var22 + hp.Z(var48, "y", 0), var23 + hp.Z(var48, "z", 0), var48);
                  }
               }

               var6.add(new o4.s((String)var8.getKey(), var21, var22, var23, var18, var19, var20, var42));
            }
         }
      }

      return var5.P(var3, "Litematica", var6);
   }

   static long h(long[] var0, int var1, int var2) {
      long var3 = (long)var2 * var1;
      int var5 = (int)(var3 >> 6);
      int var6 = (int)((long)(var2 + 1) * var1 - 1L >> 6);
      int var7 = (int)(var3 & 63L);
      long var8 = (1L << var1) - 1L;
      return var5 == var6 ? var0[var5] >>> var7 & var8 : (var0[var5] >>> var7 | var0[var6] << 64 - var7) & var8;
   }

   public static o4 J(hp.m var0, String var1) throws IOException {
      Map var2 = var0.tag();
      if (hp.f(var2, "Schematic")) {
         var2 = hp.G(var2, "Schematic");
      }

      int var3 = hp.Z(var2, "Version", 2);
      int var4 = hp.Z(var2, "Width", 0) & 65535;
      int var5 = hp.Z(var2, "Height", 0) & 65535;
      int var6 = hp.Z(var2, "Length", 0) & 65535;
      if (var4 != 0 && var5 != 0 && var6 != 0) {
         if ((long)var4 * var5 * var6 > 4000000L) {
            throw new IOException("this .schem file is too big (" + var4 + " x " + var5 + " x " + var6 + ")");
         }

         Map var7;
         byte[] var8;
         if (var3 >= 3) {
            Map var9 = hp.G(var2, "Blocks");
            var7 = hp.G(var9, "Palette");
            var8 = hp.b(var9, "Data");
         } else {
            var7 = hp.G(var2, "Palette");
            var8 = hp.b(var2, "BlockData");
         }

         if (var7.isEmpty()) {
            throw new IOException("this .schem file has no block palette");
         }

         o4.i var23 = new o4.i();
         HashMap var10 = new HashMap();

         for (Entry var12 : var7.entrySet()) {
            if (var12.getValue() instanceof Number var13) {
               String var27 = f((String)var12.getKey());
               var10.put(var13.intValue(), R(var27) ? -1 : var23.R(var27));
            }
         }

         String var24 = hp.N(hp.G(var2, "Metadata"), "Name", "");
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
                     var23.r(var22, var19, var21, var32);
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

         for (Object var33 : var3 >= 3 ? hp.M(hp.G(var2, "Blocks"), "BlockEntities") : hp.M(var2, "BlockEntities")) {
            if (var33 instanceof Map var34) {
               Map var35 = var34;
               int[] var36 = hp.l(var35, "Pos");
               if (var36.length == 3) {
                  var23.Y(var36[0], var36[1], var36[2], var3 >= 3 && hp.f(var35, "Data") ? hp.G(var35, "Data") : var35);
               }
            }
         }

         o4.s var31 = new o4.s("Main", 0, 0, 0, var4, var5, var6, var28);
         return var23.P(var24, "Sponge v" + var3, List.of(var31));
      } else {
         throw new IOException("this .schem file has no size");
      }
   }

   public static List<String> X(Path var0) {
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

   public record h(int originX, int originY, int originZ, int rotation, o4.p mirror) {
      public h(int originX, int originY, int originZ, int rotation, o4.p mirror) {
         rotation = Math.floorMod(rotation, 360) / 90 * 90;
         if (mirror == null) {
            mirror = o4.p.W;
         }

         this.originX = originX;
         this.originY = originY;
         this.originZ = originZ;
         this.rotation = rotation;
         this.mirror = mirror;
      }

      public int[] world(int var1, int var2, int var3) {
         if (this.mirror == o4.p.e) {
            var1 = -var1;
         } else if (this.mirror == o4.p.n) {
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

      public o4.h moved(int var1, int var2, int var3) {
         return new o4.h(this.originX + var1, this.originY + var2, this.originZ + var3, this.rotation, this.mirror);
      }

      public int[] bounds(o4 var1) {
         int[] var2 = this.world(var1.O(), var1.K(), var1.c());
         int[] var3 = this.world(var1.D(), var1.A(), var1.z());
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

   private static final class i {
      final List<String> O = new ArrayList<>();
      final Map<String, Integer> U = new HashMap<>();
      final Map<Long, Integer> V = new HashMap<>();
      final Map<Long, Map<String, Object>> g = new HashMap<>();
      int[] M = new int[1024];
      int[] R = new int[1024];
      int[] N = new int[1024];
      int[] p = new int[1024];
      int j;

      int R(String var1) {
         return this.U.computeIfAbsent(var1, var1x -> {
            this.O.add(var1x);
            return this.O.size() - 1;
         });
      }

      void r(int var1, int var2, int var3, int var4) throws IOException {
         long var5 = hn.f(var1, var2, var3);
         Integer var7 = this.V.get(var5);
         if (var7 != null) {
            this.p[var7] = var4;
         } else {
            if (this.j >= 4000000) {
               throw new IOException("this schematic has more than 4000000 blocks");
            }

            if (this.j == this.M.length) {
               int var8 = this.M.length * 2;
               this.M = Arrays.copyOf(this.M, var8);
               this.R = Arrays.copyOf(this.R, var8);
               this.N = Arrays.copyOf(this.N, var8);
               this.p = Arrays.copyOf(this.p, var8);
            }

            this.M[this.j] = var1;
            this.R[this.j] = var2;
            this.N[this.j] = var3;
            this.p[this.j] = var4;
            this.V.put(var5, this.j++);
         }
      }

      void Y(int var1, int var2, int var3, Map<String, Object> var4) {
         this.g.put(hn.f(var1, var2, var3), var4);
      }

      o4 P(String var1, String var2, List<o4.s> var3) throws IOException {
         if (this.j == 0) {
            throw new IOException("this schematic has no blocks");
         }

         HashMap var4 = new HashMap();

         for (Entry var6 : this.g.entrySet()) {
            Integer var7 = this.V.get(var6.getKey());
            if (var7 != null) {
               var4.put(var7, (Map)var6.getValue());
            }
         }

         return new o4(
            var1,
            var2,
            this.O,
            Arrays.copyOf(this.M, this.j),
            Arrays.copyOf(this.R, this.j),
            Arrays.copyOf(this.N, this.j),
            Arrays.copyOf(this.p, this.j),
            var3,
            var4
         );
      }
   }

   public enum p {
      W,
      e,
      n;
   }

   public record s(String name, int x, int y, int z, int sizeX, int sizeY, int sizeZ, int cells) {
   }
}
