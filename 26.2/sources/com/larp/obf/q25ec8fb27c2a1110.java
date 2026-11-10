package com.larp.obf;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.management.HotSpotDiagnosticMXBean;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Locale;
import java.util.stream.Stream;

final class q25ec8fb27c2a1110 {
   private static final String f0 = "0b4ed33ee20440042c772ffb37f1a815c2cd7d0ec3e8287dd07209b170f48f42";
   private static final int f1 = 1048576;
   private static final String f2 = "-XX:-EnableDynamicAgentLoading";

   private q25ec8fb27c2a1110() {
   }

   static boolean m0(boolean var0) {
      return m1(var0) == null;
   }

   static String m1(boolean var0) {
      try {
         String var1 = null;

         for (String var3 : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            String var4 = var3.toLowerCase(Locale.ROOT);
            if (!var4.startsWith("-javaagent:")) {
               if (var4.startsWith("-agentlib:") || var4.startsWith("-agentpath:") || var4.startsWith("-xrun")) {
                  return "Native agent " + m5(var3) + " is not allowed. Remove it from the Java arguments and restart.";
               }
            } else {
               String var5 = var3.substring("-javaagent:".length());
               if (var1 != null) {
                  return "A second Java agent (" + m4(var5) + ") is loaded next to the Modrinth App agent. Remove extra -javaagent arguments and restart.";
               }

               String var6 = m8(var5);
               if (var6 == null) {
                  return "Java agent " + m4(var5) + " is not allowed. Remove -javaagent from the Java arguments and restart.";
               }

               if (!"0b4ed33ee20440042c772ffb37f1a815c2cd7d0ec3e8287dd07209b170f48f42".equals(var6)) {
                  return "This Modrinth App launcher agent (theseus.jar SHA-256 "
                     + var6
                     + ") is not a verified version. Update Modrinth App to 0.23.1 and restart. Other Java agents are not allowed.";
               }

               var1 = var6;
            }
         }

         if (var1 != null && !m6()) {
            return "Modrinth App needs -XX:-EnableDynamicAgentLoading in this instance's Java arguments (instance settings, Java and memory, Custom Java arguments). Add it and restart; it stops other agents loading later.";
         }

         String var10 = System.getProperty("os.name", "").toLowerCase(Locale.ROOT);
         if (var10.startsWith("windows") && var0) {
            Kernel32 var12 = Kernel32.INSTANCE;
            if (var12.GetModuleHandle("jdwp.dll") != null) {
               return m2();
            }

            if (var1 == null && var12.GetModuleHandle("instrument.dll") != null) {
               return m3();
            }
         } else if (var10.equals("linux")) {
            boolean var11 = var1 != null;

            try (Stream var13 = Files.lines(Path.of("/proc/self/maps"))) {
               String var14 = var13.<String>map(var0x -> var0x.toLowerCase(Locale.ROOT))
                  .filter(var1x -> var1x.contains("/libjdwp.so") || !var11 && var1x.contains("/libinstrument.so"))
                  .findFirst()
                  .orElse(null);
               if (var14 != null) {
                  return var14.contains("/libjdwp.so") ? m2() : m3();
               }
            }
         }

         return null;
      } catch (IOException | NoSuchAlgorithmException | RuntimeException | LinkageError var9) {
         return "The Java/native agents check could not run (" + var9.getClass().getSimpleName() + "). Restart, and report this if it repeats.";
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

   private static boolean m6() {
      HotSpotDiagnosticMXBean var0 = ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);
      return var0 != null
         && (var0.getVMOption("DisableAttachMechanism").getValue().equals("true") || var0.getVMOption("EnableDynamicAgentLoading").getValue().equals("false"));
   }

   static boolean m7(String var0) throws IOException, NoSuchAlgorithmException {
      return "0b4ed33ee20440042c772ffb37f1a815c2cd7d0ec3e8287dd07209b170f48f42".equals(m8(var0));
   }

   private static String m8(String var0) throws IOException, NoSuchAlgorithmException {
      if (var0.length() > 2 && var0.startsWith("\"") && var0.endsWith("\"")) {
         var0 = var0.substring(1, var0.length() - 1);
      }

      Path var1 = Path.of(var0);
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
