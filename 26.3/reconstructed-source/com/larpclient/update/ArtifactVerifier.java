package com.larpclient.update;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;

public final class ArtifactVerifier {
   public static final int MAX_ENTRIES = 65535;
   public static final long MAX_EXPANDED_BYTES = 536870912L;
   private static final int MAX_PROPERTIES_BYTES = 16384;
   private static final Set<String> IDENTITY_FIELDS = Set.of("larpVersion", "minecraftVersion", "product", "edition", "loader", "channel", "flavor", "buildId");

   private ArtifactVerifier() {
   }

   public static Identity verify(Path var0, UpdateManifest.Release var1, Host var2) throws IOException {
      Objects.requireNonNull(var1);
      Objects.requireNonNull(var2);
      if (!var1.supports(var2)) {
         throw StrictJson.fail("Unsupported artifact host");
      }

      ArtifactVerifier.Archive var3 = inspect(var0, var1);
      if (!var3.identity.equals(var1.identity())) {
         throw StrictJson.fail("Embedded release identity mismatch");
      }

      dependencies(var3.metadata, var3.identity, var2);
      if (!var1.flavor().equals("protected")) {
         if (!var3.nativeHeaders.isEmpty()) {
            throw StrictJson.fail("Native libraries in plain artifact");
         }
      } else {
         for (String var5 : var1.platforms()) {
            String var6 = nativePath(var5);
            byte[] var7 = var3.nativeHeaders.get(var6);
            if (var7 == null || !nativeHeader(var5, var7)) {
               throw StrictJson.fail("Missing or invalid protected native platform: " + var5);
            }
         }

         if (!var3.nativeHeaders.containsKey(nativePath(var2.platform()))) {
            throw StrictJson.fail("Missing host native library");
         }
      }

      return var3.identity;
   }

   public static Identity readIdentity(Path var0) throws IOException {
      return inspect(var0, null).identity;
   }

   public static boolean containsOwnedMod(Path var0) throws IOException {
      BasicFileAttributes var1 = attributes(var0);
      if (var1.size() > 0L && var1.size() <= 134217728L) {
         byte[] var2 = new byte[(int)var1.size()];

         try (FileChannel var3 = FileChannel.open(var0, StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS)) {
            ByteBuffer var4 = ByteBuffer.wrap(var2);

            while (var4.hasRemaining()) {
               if (var3.read(var4) < 0) {
                  throw StrictJson.fail("Mod inventory truncated during read");
               }
            }

            if (var3.read(ByteBuffer.allocate(1)) >= 0) {
               throw StrictJson.fail("Mod inventory grew during read");
            }
         }

         BasicFileAttributes var8 = attributes(var0);
         if (var1.size() == var8.size() && var1.lastModifiedTime().equals(var8.lastModifiedTime()) && Objects.equals(var1.fileKey(), var8.fileKey())) {
            return ModInventory.raw(var2, 0, new ModInventory.Budget());
         } else {
            throw StrictJson.fail("Mod inventory changed during read");
         }
      } else {
         throw StrictJson.fail("Mod inventory file size limit");
      }
   }

   private static ArtifactVerifier.Archive inspect(Path var0, UpdateManifest.Release var1) throws IOException {
      BasicFileAttributes var2 = attributes(var0);
      if (var2.size() > 0L && var2.size() <= 134217728L && (var1 == null || var2.size() == var1.size())) {
         LinkedHashMap var3 = new LinkedHashMap();
         long var4 = 0L;

         try (ZipFile var6 = new ZipFile(var0.toFile(), StandardCharsets.UTF_8)) {
            Enumeration var7 = var6.entries();

            while (var7.hasMoreElements()) {
               ZipEntry var8 = (ZipEntry)var7.nextElement();
               validName(var8.getName());
               if (var3.size() < 65535
                  && var8.getSize() >= 0L
                  && var8.getSize() <= 134217728L
                  && var8.getCompressedSize() >= 0L
                  && var8.getCompressedSize() <= var2.size()
                  && var8.getCrc() >= 0L
                  && (var8.getMethod() == 0 || var8.getMethod() == 8)) {
                  var4 += var8.getSize();
                  if (var4 <= 536870912L && (!var8.isDirectory() || var8.getSize() == 0L)) {
                     if (var3.putIfAbsent(var8.getName(), new ArtifactVerifier.Central(var8.getSize(), var8.getCrc(), var8.getMethod())) != null) {
                        throw StrictJson.fail("Duplicate archive entry");
                     }
                     continue;
                  }

                  throw StrictJson.fail("Declared expanded archive limit");
               }

               throw StrictJson.fail("Archive entry limit or unsupported method");
            }
         }

         byte[] var39 = null;
         byte[] var40 = null;
         HashMap var41 = new HashMap();
         LinkedHashMap var9 = new LinkedHashMap();
         ModInventory.Budget var10 = new ModInventory.Budget();
         var10.entries = var3.size();
         var10.archives = 1;

         try (FileChannel var11 = FileChannel.open(var0, StandardOpenOption.READ, LinkOption.NOFOLLOW_LINKS)) {
            MessageDigest var12;
            try {
               var12 = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException var34) {
               throw new IOException(var34);
            }

            final long[] var13 = new long[]{0L};
            FilterInputStream var14 = new FilterInputStream(Channels.newInputStream(var11)) {
               @Override
               public int read() throws IOException {
                  int var1x = this.in.read();
                  if (var1x >= 0) {
                     this.add(1);
                  }

                  return var1x;
               }

               @Override
               public int read(byte[] var1, int var2x, int var3x) throws IOException {
                  int var4x = this.in.read(var1, var2x, var3x);
                  if (var4x > 0) {
                     this.add(var4x);
                  }

                  return var4x;
               }

               private void add(int var1) throws IOException {
                  var13[0] = var13[0] + var1;
                  if (var13[0] > 134217728L) {
                     throw StrictJson.fail("Artifact grew during verification");
                  }
               }
            };
            DigestInputStream var15 = new DigestInputStream(var14, var12);
            HashSet var16 = new HashSet();
            long var17 = 0L;
            byte[] var19 = new byte[65536];
            ZipInputStream var20 = new ZipInputStream(var15, StandardCharsets.UTF_8);

            try {
               ZipEntry var21;
               while ((var21 = var20.getNextEntry()) != null) {
                  String var22 = var21.getName();
                  validName(var22);
                  ArtifactVerifier.Central var23 = (ArtifactVerifier.Central)var3.get(var22);
                  if (var23 != null && var16.add(var22) && var16.size() <= 65535 && var21.getMethod() == var23.method) {
                     boolean var24 = var22.endsWith(".jar") && !var21.isDirectory();
                     int var25 = var22.equals("larp-version.properties")
                        ? 16384
                        : (var22.equals("fabric.mod.json") ? 131072 : (var22.startsWith("larp/native/") ? 4096 : (var24 ? 33554432 : 0)));
                     ByteArrayOutputStream var26 = var25 > 0 ? new ByteArrayOutputStream() : null;
                     CRC32 var27 = new CRC32();
                     long var28 = 0L;

                     int var30;
                     while ((var30 = var20.read(var19)) != -1) {
                        var28 += var30;
                        var17 += var30;
                        if (var28 > 134217728L || var28 > var23.size || var17 > 536870912L) {
                           throw StrictJson.fail("Expanded archive limit");
                        }

                        var27.update(var19, 0, var30);
                        if (var24) {
                           if (var28 > var25) {
                              throw StrictJson.fail("Nested resource byte limit");
                           }

                           var10.capture(var30);
                        }

                        if (var26 != null && var26.size() < var25) {
                           var26.write(var19, 0, Math.min(var30, var25 - var26.size()));
                        }
                     }

                     var20.closeEntry();
                     if (var28 == var23.size && var27.getValue() == var23.crc && var21.getCrc() == var23.crc && var21.getSize() == var23.size) {
                        if (var22.equals("larp-version.properties")) {
                           if (var28 > 16384L) {
                              throw StrictJson.fail("Identity properties size limit");
                           }

                           var39 = var26.toByteArray();
                        } else if (var22.equals("fabric.mod.json")) {
                           if (var28 > 131072L) {
                              throw StrictJson.fail("Fabric metadata size limit");
                           }

                           var40 = var26.toByteArray();
                        } else if (var22.startsWith("larp/native/")) {
                           if (!var22.equals("larp/native/linux-x64.so") && !var22.equals("larp/native/windows-x64.dll") && !var21.isDirectory()) {
                              throw StrictJson.fail("Unknown protected native resource");
                           }

                           if (!var21.isDirectory()) {
                              var41.put(var22, var26.toByteArray());
                           }
                        } else if (var24) {
                           var9.put(var22, var26.toByteArray());
                        }
                        continue;
                     }

                     throw StrictJson.fail("Archive CRC or length mismatch");
                  }

                  throw StrictJson.fail("Local/central archive entry mismatch");
               }

               var10.expanded = var17;
               var15.transferTo(OutputStream.nullOutputStream());
               if (var13[0] != var2.size() || var1 != null && !MessageDigest.isEqual(var12.digest(), HexFormat.of().parseHex(var1.sha256()))) {
                  throw StrictJson.fail("Artifact digest mismatch");
               }
            } catch (Throwable var35) {
               try {
                  var20.close();
               } catch (Throwable var32) {
                  var35.addSuppressed(var32);
               }

               throw var35;
            }

            var20.close();
            if (!var16.equals(var3.keySet())) {
               throw StrictJson.fail("Missing local archive entries");
            }
         } catch (IllegalArgumentException var37) {
            throw new IOException("Malformed artifact archive", var37);
         }

         BasicFileAttributes var42 = attributes(var0);
         if (var2.size() != var42.size() || !var2.lastModifiedTime().equals(var42.lastModifiedTime()) || !Objects.equals(var2.fileKey(), var42.fileKey())) {
            throw StrictJson.fail("Artifact changed during verification");
         }

         if (var39 != null && var40 != null) {
            Identity var43 = properties(var39);
            JsonObject var44 = StrictJson.metadataObject(var40);
            if (!StrictJson.string(var44, "id", 128).equals("larp-client") || !StrictJson.string(var44, "version", 128).equals(var43.version())) {
               throw StrictJson.fail("Fabric mod identity mismatch");
            } else if (ModInventory.declared(var44, var9, 0, var10, false)) {
               throw StrictJson.fail("Artifact declares a duplicate owned nested mod");
            } else {
               return new ArtifactVerifier.Archive(var43, var44, Map.copyOf(var41));
            }
         } else {
            throw StrictJson.fail("Missing embedded release metadata");
         }
      } else {
         throw StrictJson.fail("Artifact size mismatch or limit");
      }
   }

   private static BasicFileAttributes attributes(Path var0) throws IOException {
      BasicFileAttributes var1 = Files.readAttributes(var0, BasicFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
      if (var1.isRegularFile() && !var1.isSymbolicLink()) {
         return var1;
      } else {
         throw StrictJson.fail("Artifact must be a regular file without a symbolic link");
      }
   }

   static void validName(String var0) throws IOException {
      if (!var0.isEmpty()
         && var0.length() <= 1024
         && !var0.startsWith("/")
         && var0.indexOf(92) < 0
         && var0.indexOf(58) < 0
         && !var0.chars().anyMatch(var0x -> var0x < 32 || var0x == 127)) {
         String var1 = var0.endsWith("/") ? var0.substring(0, var0.length() - 1) : var0;

         for (String var5 : var1.split("/", -1)) {
            if (var5.isEmpty() || var5.equals(".") || var5.equals("..")) {
               throw StrictJson.fail("Unsafe archive component");
            }
         }
      } else {
         throw StrictJson.fail("Unsafe archive path");
      }
   }

   private static Identity properties(byte[] var0) throws IOException {
      Properties var1 = new Properties() {
         @Override
         public synchronized Object put(Object var1, Object var2) {
            if (this.containsKey(var1)) {
               throw new IllegalArgumentException("Duplicate identity property");
            } else {
               return super.put(var1, var2);
            }
         }
      };

      try {
         var1.load(new ByteArrayInputStream(var0));
      } catch (IllegalArgumentException var3) {
         throw new IOException("Malformed identity properties", var3);
      }

      if (!var1.stringPropertyNames().equals(IDENTITY_FIELDS)) {
         throw StrictJson.fail("Incomplete or unknown build identity fields");
      } else {
         return Identity.checked(
            var1.getProperty("product"),
            var1.getProperty("edition"),
            var1.getProperty("minecraftVersion"),
            var1.getProperty("loader"),
            var1.getProperty("channel"),
            var1.getProperty("flavor"),
            var1.getProperty("larpVersion"),
            var1.getProperty("buildId")
         );
      }
   }

   private static void dependencies(JsonObject var0, Identity var1, Host var2) throws IOException {
      StrictJson.number(var0, "schemaVersion", 1L, 1L);
      String var3 = StrictJson.string(var0, "environment", 16);
      if (!var3.equals("client")) {
         throw StrictJson.fail("Update is not a client mod");
      }

      JsonObject var4 = StrictJson.child(var0, "depends");
      if (!var4.keySet().containsAll(Set.of("minecraft", "java", "fabricloader"))) {
         throw StrictJson.fail("Missing required Fabric dependencies");
      }

      for (String var6 : var4.keySet()) {
         String var7 = switch (var6) {
            case "minecraft" -> var1.minecraft();
            case "java" -> Integer.toString(var2.javaVersion());
            case "fabricloader" -> var2.loaderVersion();
            default -> null;
         };
         ArrayList var15 = new ArrayList();
         JsonElement var16 = var4.get(var6);
         if (var16.isJsonPrimitive() && var16.getAsJsonPrimitive().isString()) {
            var15.add(var16.getAsString());
         } else {
            if (!var16.isJsonArray() || var16.getAsJsonArray().size() <= 0 || var16.getAsJsonArray().size() > 16) {
               throw StrictJson.fail("Invalid Fabric dependency predicate");
            }

            for (JsonElement var11 : var16.getAsJsonArray()) {
               if (!var11.isJsonPrimitive() || !var11.getAsJsonPrimitive().isString()) {
                  throw StrictJson.fail("Invalid Fabric dependency predicate");
               }

               var15.add(var11.getAsString());
            }
         }

         boolean var17 = false;

         for (String var12 : var15) {
            if (var12.length() > 256) {
               throw StrictJson.fail("Fabric dependency predicate limit");
            }

            if (var7 == null) {
               if (!var12.equals("*")) {
                  throw StrictJson.fail("Cannot verify required dependency: " + var6);
               }

               var17 = true;
            } else {
               try {
                  var17 |= VersionPredicate.parse(var12).test(Version.parse(var7));
               } catch (VersionParsingException var14) {
                  throw new IOException("Malformed Fabric dependency: " + var6, var14);
               }
            }
         }

         if (!var17) {
            throw StrictJson.fail("Incompatible Fabric dependency: " + var6);
         }
      }

      if (var0.has("breaks") && !StrictJson.child(var0, "breaks").isEmpty()) {
         throw StrictJson.fail("Artifact has unverified mod conflicts");
      }
   }

   private static String nativePath(String var0) throws IOException {
      return switch (var0) {
         case "linux-x64" -> "larp/native/linux-x64.so";
         case "windows-x64" -> "larp/native/windows-x64.dll";
         default -> throw StrictJson.fail("Unsupported native platform");
      };
   }

   private static boolean nativeHeader(String var0, byte[] var1) {
      if (var1.length < 64) {
         return false;
      }

      if (var0.equals("linux-x64")) {
         return var1[0] == 127
            && var1[1] == 69
            && var1[2] == 76
            && var1[3] == 70
            && var1[4] == 2
            && var1[5] == 1
            && var1[6] == 1
            && var1[16] == 3
            && var1[17] == 0
            && var1[18] == 62
            && var1[19] == 0
            && var1[20] == 1
            && var1[21] == 0
            && var1[22] == 0
            && var1[23] == 0;
      }

      if (var0.equals("windows-x64") && var1[0] == 77 && var1[1] == 90) {
         long var2 = Integer.toUnsignedLong(ByteBuffer.wrap(var1, 60, 4).order(ByteOrder.LITTLE_ENDIAN).getInt());
         if (var2 >= 64L && var2 <= var1.length - 26) {
            int var4 = (int)var2;
            return var1[var4] == 80
               && var1[var4 + 1] == 69
               && var1[var4 + 2] == 0
               && var1[var4 + 3] == 0
               && (var1[var4 + 4] & 255) == 100
               && (var1[var4 + 5] & 255) == 134
               && (var1[var4 + 24] & 255) == 11
               && var1[var4 + 25] == 2;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private record Archive(Identity identity, JsonObject metadata, Map<String, byte[]> nativeHeaders) {
   }

   private record Central(long size, long crc, int method) {
   }
}
