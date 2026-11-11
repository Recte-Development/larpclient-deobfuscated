package com.larpclient.social.presence;

import com.larpclient.network.DiscordPipeChannel;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

final class DiscordIpc {
   static final int maxFrameSize = 65536;

   static List<String> candidatePaths(String var0, Map<String, String> var1) {
      ArrayList var2 = new ArrayList();
      if (var0.toLowerCase(Locale.ROOT).startsWith("windows")) {
         for (int var3 = 0; var3 < 10; var3++) {
            var2.add("\\\\?\\pipe\\discord-ipc-" + var3);
         }
      } else {
         LinkedHashSet var9 = new LinkedHashSet();

         for (String var5 : List.of("XDG_RUNTIME_DIR", "TMPDIR", "TMP", "TEMP")) {
            String var6 = (String)var1.get(var5);
            if (var6 != null && !var6.isBlank()) {
               var9.add(var6);
            }
         }

         var9.add("/tmp");

         for (String var11 : var9) {
            for (String var7 : List.of("", "app/com.discordapp.Discord", "snap.discord")) {
               for (int var8 = 0; var8 < 10; var8++) {
                  var2.add(Path.of(var11).resolve(var7).resolve("discord-ipc-" + var8).toString());
               }
            }
         }
      }

      return List.copyOf(var2);
   }

   static ByteChannel connect(String var0) throws IOException {
      if (var0.startsWith("\\\\?\\pipe\\")) {
         return new DiscordPipeChannel(var0);
      }

      SocketChannel var1 = SocketChannel.open(StandardProtocolFamily.UNIX);

      try {
         var1.configureBlocking(false);
         var1.connect(UnixDomainSocketAddress.of(var0));
         long var2 = System.nanoTime() + TimeUnit.SECONDS.toNanos(1L);

         while (!var1.finishConnect()) {
            if (System.nanoTime() >= var2) {
               throw new IOException("Discord socket connect timed out");
            }

            pause();
         }

         var1.configureBlocking(true);
         return var1;
      } catch (IOException | RuntimeException var4) {
         var1.close();
         throw var4;
      }
   }

   static void writeFrame(ByteChannel var0, int var1, byte[] var2) throws IOException {
      if (var2.length > 65536) {
         throw new IOException("Oversized Discord IPC frame");
      }

      ByteBuffer var3 = ByteBuffer.allocate(8 + var2.length).order(ByteOrder.LITTLE_ENDIAN);
      var3.putInt(var1).putInt(var2.length).put(var2).flip();

      while (var3.hasRemaining()) {
         if (var0.write(var3) == 0) {
            pause();
         }
      }
   }

   static DiscordIpc.Frame readFrame(ByteChannel var0) throws IOException {
      ByteBuffer var1 = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
      readFully(var0, var1);
      var1.flip();
      int var2 = var1.getInt();
      int var3 = var1.getInt();
      if (var2 >= 0 && var2 <= 4 && var3 >= 0 && var3 <= 65536) {
         ByteBuffer var4 = ByteBuffer.allocate(var3);
         readFully(var0, var4);
         return new DiscordIpc.Frame(var2, var4.array());
      } else {
         throw new IOException("Invalid Discord IPC frame header");
      }
   }

   private static void readFully(ByteChannel var0, ByteBuffer var1) throws IOException {
      while (var1.hasRemaining()) {
         int var2 = var0.read(var1);
         if (var2 < 0) {
            throw new EOFException("Discord IPC closed");
         }

         if (var2 == 0) {
            pause();
         }
      }
   }

   private static void pause() throws InterruptedIOException {
      try {
         Thread.sleep(25L);
      } catch (InterruptedException var1) {
         Thread.currentThread().interrupt();
         throw new InterruptedIOException("Discord IPC interrupted");
      }
   }

   private DiscordIpc() {
   }

   record Frame(int opcode, byte[] data) {
   }
}
