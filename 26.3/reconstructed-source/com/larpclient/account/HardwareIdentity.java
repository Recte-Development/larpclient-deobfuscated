package com.larpclient.account;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public final class HardwareIdentity {
   static final Path configDir = Path.of(System.getProperty("user.home"), ".config", "larp-client");
   private static String cachedId;

   private HardwareIdentity() {
   }

   public static synchronized String getHardwareId() {
      if (cachedId == null) {
         try {
            if (Platform.isWindows()) {
               cachedId = hashIdentity("smbios", readSmbiosTable());
            } else if (Platform.isLinux()) {
               cachedId = readSmbiosTable(Path.of("/etc/machine-id"), Path.of("/sys/class/net"), HardwareIdentity::readBurnedInMac);
            } else {
               if (!Platform.isMac()) {
                  throw new IOException("Unsupported hardware identity platform");
               }

               cachedId = hashIdentity("macos-platform-uuid", normalizeMacUuid(readMacPlatformUuid()));
            }
         } catch (IOException | RuntimeException | LinkageError var1) {
            throw new IllegalStateException("Cannot read machine hardware identity; check system access and retry", var1);
         }
      }

      return cachedId;
   }

   private static String hashIdentity(String var0, String var1) {
      return "hw2_" + InstallIdentity.sha256Hex("larp-hardware-v2\n" + var0 + "\n" + var1);
   }

   private static String readSmbiosTable() throws IOException {
      Function var0 = NativeLibrary.getInstance("kernel32").getFunction("GetSystemFirmwareTable", 63);
      int var1 = var0.invokeInt(new Object[]{1381190978, 0, null, 0});
      if (var1 >= 8 && var1 <= 1048576) {
         Memory var2 = new Memory(var1);

         String var4;
         try {
            int var3 = var0.invokeInt(new Object[]{1381190978, 0, var2, var1});
            if (var3 != var1) {
               throw new IOException("SMBIOS table read failed");
            }

            var4 = parseSmbiosUuid(var2.getByteArray(0L, var3));
         } catch (Throwable var6) {
            try {
               var2.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         var2.close();
         return var4;
      } else {
         throw new IOException("Invalid SMBIOS table size");
      }
   }

   static String parseSmbiosUuid(byte[] var0) throws IOException {
      if (var0.length < 8) {
         throw new IOException("Missing SMBIOS header");
      }

      int var1 = ByteBuffer.wrap(var0, 4, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
      if (var1 >= 4 && var1 == var0.length - 8) {
         int var2 = var0.length;
         int var3 = 8;

         while (var3 + 4 <= var2) {
            int var4 = Byte.toUnsignedInt(var0[var3]);
            int var5 = Byte.toUnsignedInt(var0[var3 + 1]);
            if (var5 < 4 || var5 > var2 - var3) {
               throw new IOException("Invalid SMBIOS structure");
            }

            if (var4 == 1) {
               if (var5 < 24) {
                  throw new IOException("Missing SMBIOS system UUID");
               }

               String var7 = HexFormat.of().formatHex(var0, var3 + 8, var3 + 24);
               if (!var7.equals("0".repeat(32)) && !var7.equals("f".repeat(32))) {
                  return var7;
               }

               throw new IOException("Unavailable SMBIOS system UUID");
            }

            if (var4 == 127) {
               break;
            }

            int var6 = var3 + var5;

            while (var6 + 1 < var2 && (var0[var6] != 0 || var0[var6 + 1] != 0)) {
               var6++;
            }

            if (var6 + 1 >= var2) {
               throw new IOException("Unterminated SMBIOS structure");
            }

            var3 = var6 + 2;
         }

         throw new IOException("Missing SMBIOS system UUID");
      } else {
         throw new IOException("Invalid SMBIOS data length");
      }
   }

   private static String readMacPlatformUuid() throws IOException {
      NativeLibrary var0 = NativeLibrary.getInstance("/System/Library/Frameworks/IOKit.framework/IOKit");
      NativeLibrary var1 = NativeLibrary.getInstance("/System/Library/Frameworks/CoreFoundation.framework/CoreFoundation");
      Pointer var2 = var0.getFunction("IOServiceMatching").invokePointer(new Object[]{"IOPlatformExpertDevice"});
      if (var2 == null) {
         throw new IOException("IOKit platform matching failed");
      }

      int var3 = var0.getFunction("IOServiceGetMatchingService").invokeInt(new Object[]{0, var2});
      if (var3 == 0) {
         throw new IOException("IOKit platform device is unavailable");
      }

      Pointer var4 = null;
      Pointer var5 = null;

      String var9;
      try {
         var4 = var1.getFunction("CFStringCreateWithCString").invokePointer(new Object[]{null, "IOPlatformUUID", 134217984});
         if (var4 == null) {
            throw new IOException("Platform UUID key allocation failed");
         }

         var5 = var0.getFunction("IORegistryEntryCreateCFProperty").invokePointer(new Object[]{var3, var4, null, 0});
         if (var5 == null) {
            throw new IOException("IOPlatformUUID is unavailable");
         }

         Object var6 = var1.getFunction("CFGetTypeID").invoke(NativeLong.class, new Object[]{var5});
         if (!var6.equals(var1.getFunction("CFStringGetTypeID").invoke(NativeLong.class, new Object[0]))) {
            throw new IOException("IOPlatformUUID is not a string");
         }

         Memory var7 = new Memory(64L);

         try {
            var7.clear();
            int var8 = var1.getFunction("CFStringGetCString").invokeInt(new Object[]{var5, var7, new NativeLong(64L), 134217984});
            if ((var8 & 0xFF) == 0) {
               throw new IOException("IOPlatformUUID read failed");
            }

            var9 = var7.getString(0L, "US-ASCII");
         } catch (Throwable var15) {
            try {
               var7.close();
            } catch (Throwable var14) {
               var15.addSuppressed(var14);
            }

            throw var15;
         }

         var7.close();
      } finally {
         if (var5 != null) {
            var1.getFunction("CFRelease").invokeVoid(new Object[]{var5});
         }

         if (var4 != null) {
            var1.getFunction("CFRelease").invokeVoid(new Object[]{var4});
         }

         var0.getFunction("IOObjectRelease").invokeInt(new Object[]{var3});
      }

      return var9;
   }

   static String normalizeMacUuid(String var0) throws IOException {
      String var1 = var0.trim().toLowerCase(Locale.ROOT);
      if (!var1.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")) {
         throw new IOException("Invalid macOS platform UUID");
      } else {
         String var2 = var1.replace("-", "");
         if (!var2.equals("0".repeat(32)) && !var2.equals("f".repeat(32))) {
            return var2;
         } else {
            throw new IOException("Unavailable macOS platform UUID");
         }
      }
   }

   static String readSmbiosTable(Path var0, Path var1, UnaryOperator<String> var2) throws IOException {
      String var3 = readSmallFile(var0).toLowerCase(Locale.ROOT);
      if (var3.matches("[a-f0-9]{32}") && !var3.equals("0".repeat(32))) {
         TreeSet var4 = new TreeSet();
         TreeSet var5 = new TreeSet();
         int var6 = 0;
         int var7 = 0;
         RuntimeException var8 = null;

         try (Stream var9 = Files.list(var1)) {
            for (Path var11 : var9.toList()) {
               if (Files.isDirectory(var11.resolve("device"))) {
                  var6++;
                  if (readSmallFile(var11.resolve("addr_assign_type")).equals("0")) {
                     String var16 = readSmallFile(var11.resolve("address")).toLowerCase(Locale.ROOT);
                     if (isPermanentMac(var16)) {
                        var4.add(var16);
                     }
                  } else {
                     var7++;

                     try {
                        String var12 = Objects.requireNonNullElse(var2.apply(var11.getFileName().toString()), "").toLowerCase(Locale.ROOT);
                        if (isPermanentMac(var12)) {
                           var5.add(var12);
                        }
                     } catch (RuntimeException | LinkageError var14) {
                        if (var8 == null) {
                           var8 = var14;
                        }
                     }
                  }
               }
            }
         }

         if (var4.isEmpty()) {
            var4 = var5;
         }

         if (var4.isEmpty()) {
            throw new IOException("No permanent physical network identity available (physical adapters " + var6 + ", changed or random " + var7 + ")", var8);
         } else {
            return hashIdentity("linux-machine-nic", var3 + "\n" + String.join("\n", var4));
         }
      } else {
         throw new IOException("Invalid Linux machine ID");
      }
   }

   private static boolean isPermanentMac(String var0) {
      return var0.matches("[a-f0-9]{2}(:[a-f0-9]{2}){5}") && !var0.equals("00:00:00:00:00:00") ? (Integer.parseInt(var0.substring(0, 2), 16) & 3) == 0 : false;
   }

   private static String readBurnedInMac(String var0) {
      byte[] var1 = var0.getBytes(StandardCharsets.UTF_8);
      if (var1.length != 0 && var1.length < 16) {
         NativeLibrary var2 = NativeLibrary.getInstance(Platform.C_LIBRARY_NAME);
         int var3 = var2.getFunction("socket").invokeInt(new Object[]{2, 2, 0});
         if (var3 < 0) {
            throw new IllegalStateException("Network query socket failed, errno " + Native.getLastError());
         }

         String var6;
         try {
            Memory var4 = new Memory(40L);

            try {
               Memory var5 = new Memory(40L);

               try {
                  var4.clear();
                  var5.clear();
                  var4.write(0L, var1, 0, var1.length);
                  var4.setPointer(16L, var5);
                  var5.setInt(0L, 32);
                  var5.setInt(4L, 32);
                  if (var2.getFunction("ioctl").invokeInt(new Object[]{var3, new NativeLong(35142L), var4}) != 0) {
                     throw new IllegalStateException("Burned-in address query failed, errno " + Native.getLastError());
                  }

                  var6 = var5.getInt(4L) == 6 ? HexFormat.ofDelimiter(":").formatHex(var5.getByteArray(8L, 6)) : null;
               } catch (Throwable var16) {
                  try {
                     var5.close();
                  } catch (Throwable var15) {
                     var16.addSuppressed(var15);
                  }

                  throw var16;
               }

               var5.close();
            } catch (Throwable var17) {
               try {
                  var4.close();
               } catch (Throwable var14) {
                  var17.addSuppressed(var14);
               }

               throw var17;
            }

            var4.close();
         } finally {
            var2.getFunction("close").invokeInt(new Object[]{var3});
         }

         return var6;
      } else {
         return null;
      }
   }

   private static String readSmallFile(Path var0) throws IOException {
      try (InputStream var1 = Files.newInputStream(var0)) {
         byte[] var2 = var1.readNBytes(257);
         if (var2.length > 256) {
            throw new IOException("Oversize machine identifier");
         } else {
            return new String(var2, StandardCharsets.US_ASCII).trim();
         }
      }
   }
}
