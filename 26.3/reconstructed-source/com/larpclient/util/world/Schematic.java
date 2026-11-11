package com.larpclient.util.world;

import com.larpclient.util.data.NbtReader;
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

public final class Schematic {
   static final Set<String> aIR_BLOCKS = Set.of("minecraft:air", "minecraft:cave_air", "minecraft:void_air", "minecraft:structure_void");
   public static final int mAX_BLOCKS = 4000000;
   private final String name;
   private final String format;
   private final List<String> palette;
   private final int[] xs;
   private final int[] ys;
   private final int[] zs;
   private final int[] paletteIndices;
   private final List<Schematic.Region> regions;
   private final Map<Integer, Map<String, Object>> blockEntities;
   private final int minX;
   private final int minY;
   private final int minZ;
   private final int maxX;
   private final int maxY;
   private final int maxZ;

   private Schematic(
      String var1,
      String var2,
      List<String> var3,
      int[] var4,
      int[] var5,
      int[] var6,
      int[] var7,
      List<Schematic.Region> var8,
      Map<Integer, Map<String, Object>> var9
   ) {
      this.name = var1;
      this.format = var2;
      this.palette = List.copyOf(var3);
      this.xs = var4;
      this.ys = var5;
      this.zs = var6;
      this.paletteIndices = var7;
      this.regions = List.copyOf(var8);
      this.blockEntities = Map.copyOf(var9);
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

      this.minX = var10;
      this.minY = var11;
      this.minZ = var12;
      this.maxX = var13;
      this.maxY = var14;
      this.maxZ = var15;
   }

   public String getName() {
      return this.name;
   }

   public String getFormat() {
      return this.format;
   }

   public List<String> getPalette() {
      return this.palette;
   }

   public List<Schematic.Region> getRegions() {
      return this.regions;
   }

   public int blockCount() {
      return this.xs.length;
   }

   public int blockX(int var1) {
      return this.xs[var1];
   }

   public int getFormat(int var1) {
      return this.ys[var1];
   }

   public int blockZ(int var1) {
      return this.zs[var1];
   }

   public int describe(int var1) {
      return this.paletteIndices[var1];
   }

   public String blockState(int var1) {
      return this.palette.get(this.paletteIndices[var1]);
   }

   public Map<String, Object> blockEntity(int var1) {
      return this.blockEntities.get(var1);
   }

   public int[] disabledSlots(int var1) {
      Map var2 = this.blockEntities.get(var1);
      return var2 == null
         ? new int[0]
         : Arrays.stream(NbtReader.getIntArray(var2, "disabled_slots")).filter(var0 -> var0 >= 0 && var0 < 9).distinct().sorted().toArray();
   }

   public int minX() {
      return this.minX;
   }

   public int minY() {
      return this.minY;
   }

   public int minZ() {
      return this.minZ;
   }

   public int maxX() {
      return this.maxX;
   }

   public int maxY() {
      return this.maxY;
   }

   public int maxZ() {
      return this.maxZ;
   }

   public int getRegions() {
      return this.xs.length == 0 ? 0 : this.maxX - this.minX + 1;
   }

   public int sizeY() {
      return this.xs.length == 0 ? 0 : this.maxY - this.minY + 1;
   }

   public int sizeZ() {
      return this.xs.length == 0 ? 0 : this.maxZ - this.minZ + 1;
   }

   public Map<String, Integer> blockCounts() {
      HashMap var1 = new HashMap();

      for (int var5 : this.paletteIndices) {
         var1.merge(maxX(this.palette.get(var5)), 1, Integer::sum);
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

   public String describe() {
      return String.format(Locale.ROOT, "%s, %d x %d x %d, %,d blocks", this.name, this.getRegions(), this.sizeY(), this.sizeZ(), this.blockCount());
   }

   public static String maxX(String var0) {
      int var1 = var0.indexOf(91);
      return var1 < 0 ? var0 : var0.substring(0, var1);
   }

   public static Map<String, String> parseProperties(String var0) {
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

   public static String sizeY(String var0, Map<String, String> var1) {
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

   public static String normalizeState(String var0) {
      return sizeY(maxX(var0), parseProperties(var0));
   }

   static boolean isAir(String var0) {
      return aIR_BLOCKS.contains(maxX(var0));
   }

   public static Schematic loadFile(Path var0) throws IOException {
      String var1 = var0.getFileName().toString();

      try (InputStream var2 = Files.newInputStream(var0)) {
         return load(var1, var2);
      }
   }

   public static Schematic load(String var0, InputStream var1) throws IOException {
      String var2 = var0.toLowerCase(Locale.ROOT);
      String var3 = var0.replaceFirst("(?i)\\.(litematic|schematic|schem|nbt)$", "");
      if (var2.endsWith(".schematic")) {
         throw new IOException("old .schematic files (MCEdit and WorldEdit before 1.13) are not supported; save it as .litematic or .schem");
      } else {
         NbtReader.NbtFile var4 = NbtReader.read(var1);
         if (var2.endsWith(".litematic")) {
            return readLitematic(var4.tag(), var3);
         } else if (var2.endsWith(".schem")) {
            return readSponge(var4, var3);
         } else {
            throw new IOException("unknown schematic type; use a .litematic or .schem file");
         }
      }
   }

   public static Schematic readLitematic(Map<String, Object> var0, String var1) throws IOException {
      Map var2 = NbtReader.getCompound(var0, "Metadata");
      String var3 = NbtReader.getString(var2, "Name", "");
      if (var3.isBlank()) {
         var3 = var1;
      }

      Map var4 = NbtReader.getCompound(var0, "Regions");
      if (var4.isEmpty()) {
         throw new IOException("this .litematic file has no regions");
      }

      Schematic.Builder var5 = new Schematic.Builder();
      ArrayList var6 = new ArrayList();

      for (Entry var8 : var4.entrySet()) {
         if (var8.getValue() instanceof Map) {
            Map var9 = (Map)var8.getValue();
            Map var10 = NbtReader.getCompound(var9, "Position");
            Map var11 = NbtReader.getCompound(var9, "Size");
            int var12 = NbtReader.getInt(var10, "x", 0);
            int var13 = NbtReader.getInt(var10, "y", 0);
            int var14 = NbtReader.getInt(var10, "z", 0);
            int var15 = NbtReader.getInt(var11, "x", 0);
            int var16 = NbtReader.getInt(var11, "y", 0);
            int var17 = NbtReader.getInt(var11, "z", 0);
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
               List var24 = NbtReader.getList(var9, "BlockStatePalette");
               if (var24.isEmpty()) {
                  throw new IOException("region " + (String)var8.getKey() + " has no block palette");
               }

               int[] var25 = new int[var24.size()];

               for (int var26 = 0; var26 < var24.size(); var26++) {
                  Map var27 = var24.get(var26) instanceof Map var28 ? var28 : Map.of();
                  TreeMap var38 = new TreeMap();

                  for (Entry var30 : NbtReader.getCompound(var27, "Properties").entrySet()) {
                     if (var30.getValue() instanceof String var31) {
                        var38.put((String)var30.getKey(), var31);
                     }
                  }

                  String var41 = sizeY(NbtReader.getString(var27, "Name", "minecraft:air"), var38);
                  var25[var26] = isAir(var41) ? -1 : var5.paletteIndex(var41);
               }

               long[] var36 = NbtReader.getLongArray(var9, "BlockStates");
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
                        int var35 = (int)readPackedValue(var36, var37, var34);
                        if (var35 < 0 || var35 >= var25.length) {
                           throw new IOException("region " + (String)var8.getKey() + " points outside its palette");
                        }

                        if (var25[var35] >= 0) {
                           var5.addBlock(var21 + var33, var22 + var43, var23 + var45, var25[var35]);
                           var42++;
                        }
                     }
                  }
               }

               for (Object var46 : NbtReader.getList(var9, "TileEntities")) {
                  if (var46 instanceof Map var47) {
                     Map var48 = var47;
                     var5.addBlockEntity(
                        var21 + NbtReader.getInt(var48, "x", 0), var22 + NbtReader.getInt(var48, "y", 0), var23 + NbtReader.getInt(var48, "z", 0), var48
                     );
                  }
               }

               var6.add(new Schematic.Region((String)var8.getKey(), var21, var22, var23, var18, var19, var20, var42));
            }
         }
      }

      return var5.build(var3, "Litematica", var6);
   }

   static long readPackedValue(long[] var0, int var1, int var2) {
      long var3 = (long)var2 * var1;
      int var5 = (int)(var3 >> 6);
      int var6 = (int)((long)(var2 + 1) * var1 - 1L >> 6);
      int var7 = (int)(var3 & 63L);
      long var8 = (1L << var1) - 1L;
      return var5 == var6 ? var0[var5] >>> var7 & var8 : (var0[var5] >>> var7 | var0[var6] << 64 - var7) & var8;
   }

   public static Schematic readSponge(NbtReader.NbtFile var0, String var1) throws IOException {
      Map var2 = var0.tag();
      if (NbtReader.has(var2, "Schematic")) {
         var2 = NbtReader.getCompound(var2, "Schematic");
      }

      int var3 = NbtReader.getInt(var2, "Version", 2);
      int var4 = NbtReader.getInt(var2, "Width", 0) & 65535;
      int var5 = NbtReader.getInt(var2, "Height", 0) & 65535;
      int var6 = NbtReader.getInt(var2, "Length", 0) & 65535;
      if (var4 != 0 && var5 != 0 && var6 != 0) {
         if ((long)var4 * var5 * var6 > 4000000L) {
            throw new IOException("this .schem file is too big (" + var4 + " x " + var5 + " x " + var6 + ")");
         }

         Map var7;
         byte[] var8;
         if (var3 >= 3) {
            Map var9 = NbtReader.getCompound(var2, "Blocks");
            var7 = NbtReader.getCompound(var9, "Palette");
            var8 = NbtReader.getByteArray(var9, "Data");
         } else {
            var7 = NbtReader.getCompound(var2, "Palette");
            var8 = NbtReader.getByteArray(var2, "BlockData");
         }

         if (var7.isEmpty()) {
            throw new IOException("this .schem file has no block palette");
         }

         Schematic.Builder var23 = new Schematic.Builder();
         HashMap var10 = new HashMap();

         for (Entry var12 : var7.entrySet()) {
            if (var12.getValue() instanceof Number var13) {
               String var27 = normalizeState((String)var12.getKey());
               var10.put(var13.intValue(), isAir(var27) ? -1 : var23.paletteIndex(var27));
            }
         }

         String var24 = NbtReader.getString(NbtReader.getCompound(var2, "Metadata"), "Name", "");
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
                     var23.addBlock(var22, var19, var21, var32);
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

         for (Object var33 : var3 >= 3 ? NbtReader.getList(NbtReader.getCompound(var2, "Blocks"), "BlockEntities") : NbtReader.getList(var2, "BlockEntities")) {
            if (var33 instanceof Map var34) {
               Map var35 = var34;
               int[] var36 = NbtReader.getIntArray(var35, "Pos");
               if (var36.length == 3) {
                  var23.addBlockEntity(var36[0], var36[1], var36[2], var3 >= 3 && NbtReader.has(var35, "Data") ? NbtReader.getCompound(var35, "Data") : var35);
               }
            }
         }

         Schematic.Region var31 = new Schematic.Region("Main", 0, 0, 0, var4, var5, var6, var28);
         return var23.build(var24, "Sponge v" + var3, List.of(var31));
      } else {
         throw new IOException("this .schem file has no size");
      }
   }

   public static List<String> listSchematicFiles(Path var0) {
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

   private static final class Builder {
      final List<String> palette = new ArrayList<>();
      final Map<String, Integer> paletteIndexByState = new HashMap<>();
      final Map<Long, Integer> indexByPos = new HashMap<>();
      final Map<Long, Map<String, Object>> blockEntitiesByPos = new HashMap<>();
      int[] xs = new int[1024];
      int[] ys = new int[1024];
      int[] zs = new int[1024];
      int[] paletteIndices = new int[1024];
      int count;

      int paletteIndex(String var1) {
         return this.paletteIndexByState.computeIfAbsent(var1, var1x -> {
            this.palette.add(var1x);
            return this.palette.size() - 1;
         });
      }

      void addBlock(int var1, int var2, int var3, int var4) throws IOException {
         long var5 = AStarPathfinder.packPos(var1, var2, var3);
         Integer var7 = this.indexByPos.get(var5);
         if (var7 != null) {
            this.paletteIndices[var7] = var4;
         } else {
            if (this.count >= 4000000) {
               throw new IOException("this schematic has more than 4000000 blocks");
            }

            if (this.count == this.xs.length) {
               int var8 = this.xs.length * 2;
               this.xs = Arrays.copyOf(this.xs, var8);
               this.ys = Arrays.copyOf(this.ys, var8);
               this.zs = Arrays.copyOf(this.zs, var8);
               this.paletteIndices = Arrays.copyOf(this.paletteIndices, var8);
            }

            this.xs[this.count] = var1;
            this.ys[this.count] = var2;
            this.zs[this.count] = var3;
            this.paletteIndices[this.count] = var4;
            this.indexByPos.put(var5, this.count++);
         }
      }

      void addBlockEntity(int var1, int var2, int var3, Map<String, Object> var4) {
         this.blockEntitiesByPos.put(AStarPathfinder.packPos(var1, var2, var3), var4);
      }

      Schematic build(String var1, String var2, List<Schematic.Region> var3) throws IOException {
         if (this.count == 0) {
            throw new IOException("this schematic has no blocks");
         }

         HashMap var4 = new HashMap();

         for (Entry var6 : this.blockEntitiesByPos.entrySet()) {
            Integer var7 = this.indexByPos.get(var6.getKey());
            if (var7 != null) {
               var4.put(var7, (Map)var6.getValue());
            }
         }

         return new Schematic(
            var1,
            var2,
            this.palette,
            Arrays.copyOf(this.xs, this.count),
            Arrays.copyOf(this.ys, this.count),
            Arrays.copyOf(this.zs, this.count),
            Arrays.copyOf(this.paletteIndices, this.count),
            var3,
            var4
         );
      }
   }

   public enum Mirror {
      NONE,
      LEFT_RIGHT,
      FRONT_BACK;
   }

   public record Placement(int originX, int originY, int originZ, int rotation, Schematic.Mirror mirror) {
      public Placement(int originX, int originY, int originZ, int rotation, Schematic.Mirror mirror) {
         rotation = Math.floorMod(rotation, 360) / 90 * 90;
         if (mirror == null) {
            mirror = Schematic.Mirror.NONE;
         }

         this.originX = originX;
         this.originY = originY;
         this.originZ = originZ;
         this.rotation = rotation;
         this.mirror = mirror;
      }

      public int[] world(int var1, int var2, int var3) {
         if (this.mirror == Schematic.Mirror.LEFT_RIGHT) {
            var1 = -var1;
         } else if (this.mirror == Schematic.Mirror.FRONT_BACK) {
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

      public Schematic.Placement moved(int var1, int var2, int var3) {
         return new Schematic.Placement(this.originX + var1, this.originY + var2, this.originZ + var3, this.rotation, this.mirror);
      }

      public int[] bounds(Schematic var1) {
         int[] var2 = this.world(var1.minX(), var1.minY(), var1.minZ());
         int[] var3 = this.world(var1.maxX(), var1.maxY(), var1.maxZ());
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

   public record Region(String name, int x, int y, int z, int sizeX, int sizeY, int sizeZ, int cells) {
      private final int originX;
      private final int originY;
      private final int originZ;

      public Region(String name, int x, int y, int z, int sizeX, int sizeY, int sizeZ, int cells) {
         this.name = name;
         this.originX = x;
         this.originY = y;
         this.originZ = z;
         this.sizeX = sizeX;
         this.sizeY = sizeY;
         this.sizeZ = sizeZ;
         this.cells = cells;
      }
   }
}
