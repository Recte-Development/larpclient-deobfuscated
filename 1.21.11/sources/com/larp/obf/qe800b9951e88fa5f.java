package com.larp.obf;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.management.HotSpotDiagnosticMXBean;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

final class qe800b9951e88fa5f {
   private static final String f0 = "0b4ed33ee20440042c772ffb37f1a815c2cd7d0ec3e8287dd07209b170f48f42";
   private static final int f1 = 1048576;
   private static final String f2 = "-XX:-EnableDynamicAgentLoading";
   private static volatile Set<Path> f3;

   private qe800b9951e88fa5f() {
   }

   static boolean m0(boolean var0) {
      return m1(var0) == null;
   }

   static String m1(boolean var0) {
      try {
         String var1 = null;
         Path var2 = null;

         for (String var4 : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            String var5 = var4.toLowerCase(Locale.ROOT);
            if (!var5.startsWith("-javaagent:")) {
               if (var5.startsWith("-agentlib:") || var5.startsWith("-agentpath:") || var5.startsWith("-xrun")) {
                  return "Native agent " + m5(var4) + " is not allowed. Remove it from the Java arguments and restart.";
               }
            } else {
               String var6 = var4.substring("-javaagent:".length());
               if (var1 != null) {
                  return "A second Java agent (" + m4(var6) + ") is loaded next to the Modrinth App agent. Remove extra -javaagent arguments and restart.";
               }

               String var7 = mc(var6);
               if (var7 == null) {
                  return "Java agent " + m4(var6) + " is not allowed. Remove -javaagent from the Java arguments and restart.";
               }

               if (!"0b4ed33ee20440042c772ffb37f1a815c2cd7d0ec3e8287dd07209b170f48f42".equals(var7)) {
                  return "This Modrinth App launcher agent (theseus.jar SHA-256 "
                     + var7
                     + ") is not a verified version. Update Modrinth App to 0.23.1 and restart. Other Java agents are not allowed.";
               }

               var1 = var7;
               var2 = Path.of(m6(var6)).toRealPath();
            }
         }

         String var11 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
         if (var1 != null && !ma()) {
            if (var11.equals("linux")) {
               return "Modrinth App on Linux needs -XX:-EnableDynamicAgentLoading in this instance's Java arguments (instance settings, Java and memory, Custom Java arguments). Add it and restart; it stops other agents loading later.";
            }

            String var12 = m8(var2);
            if (var12 != null) {
               return "A Java agent (" + var12 + ") was loaded after startup. Close tools that attach Java agents and restart.";
            }

            if (var11.startsWith("windows") && var0) {
               String var15 = m9();
               if (var15 != null) {
                  return "Native agent " + var15 + " was loaded after startup. Close tools that attach agents and restart.";
               }
            }
         }

         if (var11.startsWith("windows") && var0) {
            Kernel32 var14 = Kernel32.INSTANCE;
            if (var14.GetModuleHandle("jdwp.dll") != null) {
               return m2();
            }

            if (var1 == null && var14.GetModuleHandle("instrument.dll") != null) {
               return m3();
            }
         } else if (var11.equals("linux")) {
            boolean var13 = var1 != null;

            try (Stream var16 = Files.lines(Path.of("/proc/self/maps"))) {
               String var17 = var16.<String>map(var0x -> var0x.toLowerCase(Locale.ROOT))
                  .filter(var1x -> var1x.contains("/libjdwp.so") || !var13 && var1x.contains("/libinstrument.so"))
                  .findFirst()
                  .orElse(null);
               if (var17 != null) {
                  return var17.contains("/libjdwp.so") ? m2() : m3();
               }
            }
         }

         return null;
      } catch (IOException | NoSuchAlgorithmException | RuntimeException | LinkageError var10) {
         return "The Java/native agents check could not run (" + var10.getClass().getSimpleName() + "). Restart, and report this if it repeats.";
      }
   }

   private static String m2() {
      return "A Java debugger is attached. Remove debugger and agents arguments and restart.";
   }

   private static String m3() {
      return "A Java agent was loaded after startup. Close tools that attach Java agents and restart.";
   }

   private static String m4(String var0) {
      int var1 = var0.indexOf(61);
      String var2 = var1 >= 0 ? var0.substring(0, var1) : var0;
      if (var2.length() > 1 && var2.startsWith("\"")) {
         var2 = var2.substring(1);
      }

      if (var2.endsWith("\"")) {
         var2 = var2.substring(0, var2.length() - 1);
      }

      int var3 = Math.max(var2.lastIndexOf(47), var2.lastIndexOf(92));
      return var2.substring(var3 + 1);
   }

   private static String m5(String var0) {
      int var1 = var0.indexOf(58);
      return var1 >= 0 ? m4(var0.substring(var1 + 1)) : var0;
   }

   private static String m6(String var0) {
      return var0.length() > 2 && var0.startsWith("\"") && var0.endsWith("\"") ? var0.substring(1, var0.length() - 1) : var0;
   }

   private static Set<Path> m7() {
      HashSet var0 = f3;
      if (var0 != null) {
         return var0;
      }

      var0 = new HashSet();

      for (String var4 : System.getProperty("java.class.path", "").split(File.pathSeparator)) {
         if (!var4.isEmpty()) {
            try {
               var0.add(Path.of(var4).toRealPath());
            } catch (IOException | RuntimeException var6) {
            }
         }
      }

      return f3 = Set.copyOf(var0);
   }

   static String m8(Path var0) throws IOException {
      Set var1 = m7();
      Enumeration var2 = ClassLoader.getSystemClassLoader().getResources("META-INF/MANIFEST.MF");

      while (var2.hasMoreElements()) {
         URL var3 = (URL)var2.nextElement();
         if ("jar".equals(var3.getProtocol())) {
            String var4 = var3.getPath();
            int var5 = var4.indexOf("!/");
            if (var5 >= 0 && var4.startsWith("file:")) {
               Path var6;
               try {
                  var6 = Path.of(URI.create(var4.substring(0, var5))).toRealPath();
               } catch (IOException | RuntimeException var8) {
                  return var4.substring(0, var5);
               }

               if (!var1.contains(var6) && !var6.equals(var0)) {
                  return String.valueOf(var6.getFileName());
               }
            }
         }
      }

      return null;
   }

   static String m9() throws IOException {
      NativeLibrary var0 = NativeLibrary.getInstance("kernel32");
      Function var1 = var0.getFunction("K32EnumProcessModules");
      Function var2 = var0.getFunction("K32GetModuleFileNameExW");
      Function var3 = var0.getFunction("GetProcAddress");
      Pointer var4 = Pointer.createConstant(-1L);
      int var5 = 512;

      while (true) {
         Memory var7 = new Memory((long)var5 * Native.POINTER_SIZE);
         Memory var8 = new Memory(4L);
         if (var1.invokeInt(new Object[]{var4, var7, var5 * Native.POINTER_SIZE, var8}) == 0) {
            throw new IOException("EnumProcessModules failed: " + Native.getLastError());
         }

         int var9 = var8.getInt(0L) / Native.POINTER_SIZE;
         if (var9 <= var5) {
            Pointer[] var6 = var7.getPointerArray(0L, var9);
            Path var16 = Path.of(System.getProperty("java.home"), "bin", "instrument.dll");

            for (Pointer var11 : var6) {
               if (var11 != null && var3.invokePointer(new Object[]{var11, "Agent_OnAttach"}) != null) {
                  Memory var12 = new Memory(65536L);
                  int var13 = var2.invokeInt(new Object[]{var4, var11, var12, 32768});
                  if (var13 <= 0) {
                     return "an unnamed module";
                  }

                  Path var14 = Path.of(new String(var12.getCharArray(0L, var13)));
                  String var15 = String.valueOf(var14.getFileName()).toLowerCase(Locale.ROOT);
                  if (!var15.equals("jdwp.dll") && (!var15.equals("instrument.dll") || !Files.exists(var16) || !Files.isSameFile(var14, var16))) {
                     return String.valueOf(var14.getFileName());
                  }
               }
            }

            return null;
         }

         var5 = var9 + 64;
      }
   }

   private static boolean ma() {
      HotSpotDiagnosticMXBean var0 = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
      return var0 != null
         && (var0.getVMOption("DisableAttachMechanism").getValue().equals("true") || var0.getVMOption("EnableDynamicAgentLoading").getValue().equals("false"));
   }

   static boolean mb(String var0) throws IOException, NoSuchAlgorithmException {
      return "0b4ed33ee20440042c772ffb37f1a815c2cd7d0ec3e8287dd07209b170f48f42".equals(mc(var0));
   }

   private static String mc(String var0) throws IOException, NoSuchAlgorithmException {
      Path var1 = Path.of(m6(var0));
      if (var1.getFileName() != null && var1.getFileName().toString().equalsIgnoreCase("theseus.jar")) {
         Path var2 = var1.toRealPath();
         Path var3 = var2.getParent();
         if (var2.getFileName().toString().equalsIgnoreCase("theseus.jar")
            && var3 != null
            && var3.getFileName().toString().matches("\\.tmp[A-Za-z0-9]{6}")
            && var3.getParent() != null
            && Files.isSameFile(var3.getParent(), Path.of(System.getProperty("java.io.tmpdir")))
            && Files.isRegularFile(var2, LinkOption.NOFOLLOW_LINKS)) {
            try (InputStream var4 = Files.newInputStream(var2, LinkOption.NOFOLLOW_LINKS)) {
               byte[] var5 = var4.readNBytes(1048577);
               return var5.length > 1048576 ? null : HexFormat.of().formatHex(MessageDigest.getInstance("SHA-256").digest(var5));
            }
         } else {
            return null;
         }
      } else {
         return null;
      }
   }
}
