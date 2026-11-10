package com.larp.updater;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

final class ModInventory {
   static final int MAX_DEPTH = 4;
   static final int MAX_NESTED_ARCHIVE_BYTES = 33554432;
   static final long MAX_CAPTURED_BYTES = 67108864L;
   static final int MAX_ARCHIVES = 256;
   private static final String OWNED_ID = "larp-client";

   private ModInventory() {
   }

   static boolean raw(byte[] var0, int var1, ModInventory.Budget var2) throws IOException {
      if (var1 > 4 || ++var2.archives > 256) {
         throw StrictJson.fail("Nested archive depth or count limit");
      }

      if (var0.length != 0 && var0.length <= (var1 == 0 ? 134217728L : 33554432L)) {
         ModInventory.Directory var3 = central(var0);
         Map var4 = var3.entries;
         int var5 = 0;
         HashSet var6 = new HashSet();
         byte[] var7 = null;
         LinkedHashMap var8 = new LinkedHashMap();

         try (ZipInputStream var9 = new ZipInputStream(new ByteArrayInputStream(var0), StandardCharsets.UTF_8)) {
            byte[] var10 = new byte[65536];

            ZipEntry var11;
            while ((var11 = var9.getNextEntry()) != null) {
               var2.entry();
               String var12 = var11.getName();
               ArtifactVerifier.validName(var12);
               ModInventory.Central var13 = (ModInventory.Central)var4.get(var12);
               if (var13 != null && var6.add(var12) && var13.method == var11.getMethod() && var13.localOffset == var5) {
                  var5 = var13.nextLocalOffset;
                  boolean var14 = var12.endsWith(".jar") && !var11.isDirectory();
                  int var15 = var12.equals("fabric.mod.json") ? 131072 : (var14 ? 33554432 : 0);
                  ByteArrayOutputStream var16 = var15 > 0 ? new ByteArrayOutputStream() : null;
                  CRC32 var17 = new CRC32();
                  long var18 = 0L;

                  int var20;
                  while ((var20 = var9.read(var10)) != -1) {
                     var18 += var20;
                     var2.expanded(var20);
                     if (var18 > var13.size || var18 > 134217728L) {
                        throw StrictJson.fail("Nested expanded entry limit");
                     }

                     var17.update(var10, 0, var20);
                     if (var16 != null) {
                        if (var18 > var15) {
                           throw StrictJson.fail("Nested resource byte limit");
                        }

                        if (var14) {
                           var2.capture(var20);
                        }

                        var16.write(var10, 0, var20);
                     }
                  }

                  var9.closeEntry();
                  if (var18 == var13.size && var17.getValue() == var13.crc && var11.getCrc() == var13.crc && var11.getSize() == var13.size) {
                     if (var12.equals("fabric.mod.json")) {
                        var7 = var16.toByteArray();
                     } else if (var14) {
                        var8.put(var12, var16.toByteArray());
                     }
                     continue;
                  }

                  throw StrictJson.fail("Nested archive CRC or size mismatch");
               }

               throw StrictJson.fail("Nested local/central mismatch");
            }
         } catch (IllegalArgumentException var23) {
            throw new IOException("Malformed nested archive", var23);
         }

         if (var5 != var3.offset || !var6.equals(var4.keySet())) {
            throw StrictJson.fail("Missing nested archive local entries");
         } else {
            return var7 == null ? false : declared(StrictJson.metadataObject(var7), var8, var1, var2, true);
         }
      } else {
         throw StrictJson.fail("Nested archive byte limit");
      }
   }

   static boolean declared(JsonObject var0, Map<String, byte[]> var1, int var2, ModInventory.Budget var3, boolean var4) throws IOException {
      long var5 = var0.has("schemaVersion") ? StrictJson.number(var0, "schemaVersion", 0L, 1L) : 0L;
      String var7 = StrictJson.string(var0, "id", 64);
      validId(var7);
      String var8 = StrictJson.string(var0, "version", 128);
      if (var8.isEmpty()) {
         throw StrictJson.fail("Missing mod inventory version");
      }

      boolean var9 = var4 && "larp-client".equals(var7);
      if (var5 != 0L || !var0.has("jars") && !var0.has("provides")) {
         if (var0.has("provides")) {
            JsonElement var10 = var0.get("provides");
            if (!var10.isJsonArray() || var10.getAsJsonArray().size() > 128) {
               throw StrictJson.fail("Invalid provided mod identifiers");
            }

            HashSet var11 = new HashSet();

            for (JsonElement var13 : var10.getAsJsonArray()) {
               if (!var13.isJsonPrimitive() || !var13.getAsJsonPrimitive().isString()) {
                  throw StrictJson.fail("Invalid provided mod identifier");
               }

               String var14 = var13.getAsString();
               validId(var14);
               if (!var11.add(var14)) {
                  throw StrictJson.fail("Duplicate provided mod identifier");
               }

               var9 |= var4 && "larp-client".equals(var14);
            }
         }

         if (!var0.has("jars")) {
            return var9;
         }

         JsonElement var17 = var0.get("jars");
         if (var17.isJsonArray() && var17.getAsJsonArray().size() <= 256) {
            HashSet var18 = new HashSet();

            for (JsonElement var20 : var17.getAsJsonArray()) {
               if (!var20.isJsonObject()) {
                  throw StrictJson.fail("Invalid nested jar declaration");
               }

               JsonObject var21 = var20.getAsJsonObject();
               String var15 = StrictJson.string(var21, "file", 1024);
               ArtifactVerifier.validName(var15);
               if (!var15.endsWith(".jar") || !var18.add(var15)) {
                  throw StrictJson.fail("Invalid or duplicate nested jar path");
               }

               byte[] var16 = (byte[])var1.get(var15);
               if (var16 == null) {
                  throw StrictJson.fail("Missing declared nested jar");
               }

               var9 |= raw(var16, var2 + 1, var3);
            }

            return var9;
         } else {
            throw StrictJson.fail("Invalid nested jar declarations");
         }
      } else {
         throw StrictJson.fail("Ambiguous legacy nested mod declarations");
      }
   }

   private static void validId(String var0) throws IOException {
      if (!var0.matches("[a-z][a-z0-9_-]{1,63}")) {
         throw StrictJson.fail("Invalid Fabric mod identifier");
      }
   }

   private static ModInventory.Directory central(byte[] var0) throws IOException {
      int var1 = -1;

      for (int var2 = var0.length - 22; var2 >= Math.max(0, var0.length - 65557); var2--) {
         if (u32(var0, var2) == 101010256L && var2 + 22L + u16(var0, var2 + 20) == var0.length) {
            var1 = var2;
            break;
         }
      }

      if (var1 >= 0 && u16(var0, var1 + 4) == 0 && u16(var0, var1 + 6) == 0 && u16(var0, var1 + 8) == u16(var0, var1 + 10)) {
         int var34 = u16(var0, var1 + 10);
         long var3 = u32(var0, var1 + 12);
         long var5 = u32(var0, var1 + 16);
         if (var34 != 65535 && var5 + var3 == var1 && var5 <= var0.length && var3 <= var0.length) {
            int var7 = (int)var5;
            LinkedHashMap var8 = new LinkedHashMap();
            HashSet var9 = new HashSet();
            long var10 = 0L;
            int var12 = 0;

            while (var12 < var34) {
               if (var7 <= var1 - 46 && u32(var0, var7) == 33639248L) {
                  int var13 = u16(var0, var7 + 8);
                  int var14 = u16(var0, var7 + 10);
                  int var15 = u16(var0, var7 + 28);
                  long var16 = u32(var0, var7 + 16);
                  long var18 = u32(var0, var7 + 20);
                  long var20 = u32(var0, var7 + 24);
                  long var22 = u32(var0, var7 + 42);
                  int var24 = var7 + 46 + var15 + u16(var0, var7 + 30) + u16(var0, var7 + 32);
                  if (var24 <= var1
                     && var15 != 0
                     && var22 < var5
                     && var9.add(var22)
                     && (var13 & 1) == 0
                     && (var14 == 0 || var14 == 8)
                     && var18 <= var0.length
                     && var20 <= 134217728L
                     && u16(var0, var7 + 34) == 0) {
                     String var25;
                     try {
                        var25 = StandardCharsets.UTF_8
                           .newDecoder()
                           .onMalformedInput(CodingErrorAction.REPORT)
                           .decode(ByteBuffer.wrap(var0, var7 + 46, var15))
                           .toString();
                     } catch (CharacterCodingException var33) {
                        throw StrictJson.fail("Invalid nested ZIP name encoding");
                     }

                     ArtifactVerifier.validName(var25);
                     if (var22 <= var5 - 30L && u32(var0, (int)var22) == 67324752L) {
                        int var26 = (int)var22;
                        int var27 = u16(var0, var26 + 26);
                        int var28 = u16(var0, var26 + 28);
                        long var29 = var22 + 30L + var27 + var28;
                        long var31 = var29 + var18;
                        if (var27 == var15
                           && var29 <= var5
                           && var31 <= var5
                           && u16(var0, var26 + 6) == var13
                           && u16(var0, var26 + 8) == var14
                           && Arrays.equals(var0, var7 + 46, var7 + 46 + var15, var0, var26 + 30, var26 + 30 + var27)) {
                           if ((var13 & 8) != 0) {
                              if (var31 > var5 - 12L) {
                                 throw StrictJson.fail("Truncated nested data descriptor");
                              }

                              var31 += u32(var0, (int)var31) == 134695760L ? 16L : 12L;
                              if (var31 > var5) {
                                 throw StrictJson.fail("Truncated nested data descriptor");
                              }
                           }

                           var10 += var20;
                           if (var10 <= 536870912L && (!var25.endsWith("/") || var20 == 0L)) {
                              if (var8.putIfAbsent(var25, new ModInventory.Central(var20, var16, var14, var26, (int)var31)) != null) {
                                 throw StrictJson.fail("Duplicate nested ZIP entry");
                              }

                              var7 = var24;
                              var12++;
                              continue;
                           }

                           throw StrictJson.fail("Nested declared expanded limit");
                        }

                        throw StrictJson.fail("Ambiguous nested local header");
                     }

                     throw StrictJson.fail("Invalid nested local header offset");
                  }

                  throw StrictJson.fail("Unsafe nested ZIP directory entry");
               }

               throw StrictJson.fail("Malformed nested ZIP directory entry");
            }

            if (var7 != var1) {
               throw StrictJson.fail("Nested ZIP directory size mismatch");
            } else {
               return new ModInventory.Directory(var8, (int)var5);
            }
         } else {
            throw StrictJson.fail("Unsupported nested ZIP directory bounds");
         }
      } else {
         throw StrictJson.fail("Missing or ambiguous nested ZIP directory");
      }
   }

   private static int u16(byte[] var0, int var1) {
      return Short.toUnsignedInt(ByteBuffer.wrap(var0, var1, 2).order(ByteOrder.LITTLE_ENDIAN).getShort());
   }

   private static long u32(byte[] var0, int var1) {
      return Integer.toUnsignedLong(ByteBuffer.wrap(var0, var1, 4).order(ByteOrder.LITTLE_ENDIAN).getInt());
   }

   static final class Budget {
      long expanded;
      long captured;
      int entries;
      int archives;

      void expanded(long var1) throws IOException {
         this.expanded += var1;
         if (this.expanded > 536870912L) {
            throw StrictJson.fail("Nested aggregate expanded limit");
         }
      }

      void capture(long var1) throws IOException {
         this.captured += var1;
         if (this.captured > 67108864L) {
            throw StrictJson.fail("Nested aggregate capture limit");
         }
      }

      void entry() throws IOException {
         if (++this.entries > 65535) {
            throw StrictJson.fail("Nested aggregate entry limit");
         }
      }
   }

   private record Central(long size, long crc, int method, int localOffset, int nextLocalOffset) {
   }

   private record Directory(Map<String, ModInventory.Central> entries, int offset) {
   }
}
