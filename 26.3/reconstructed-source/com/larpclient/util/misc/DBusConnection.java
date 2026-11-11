package com.larpclient.util.misc;

import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedSelectorException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

final class DBusConnection implements AutoCloseable {
   static final int mAX_MESSAGE_SIZE = 1048576;
   private static final int mAX_NESTING_DEPTH = 32;
   private static final int mAX_PARSED_VALUES = 65536;
   private final SocketChannel channel;
   private final Selector selector;
   private int serial;
   private volatile boolean closed;

   DBusConnection(SocketChannel var1, String var2, long var3) throws IOException, InterruptedException {
      this.channel = var1;
      var1.configureBlocking(false);
      this.selector = Selector.open();

      try {
         var1.register(this.selector, 0);
         this.authenticate(var2, var3);
         if (this.callMethod("org.freedesktop.DBus", "/org/freedesktop/DBus", "org.freedesktop.DBus", "Hello", var3) == null) {
            throw new IOException("D-Bus session refused Hello");
         }
      } catch (IOException | InterruptedException | RuntimeException var6) {
         this.selector.close();
         throw var6;
      }
   }

   static DBusConnection connectSession(long var0) throws IOException, InterruptedException {
      String var2 = readUid();
      IOException var3 = null;

      for (String var5 : candidateSocketPaths(System.getenv("DBUS_SESSION_BUS_ADDRESS"), System.getenv("XDG_RUNTIME_DIR"), var2)) {
         if (Files.exists(Path.of(var5))) {
            SocketChannel var6 = null;

            try {
               var6 = SocketChannel.open(UnixDomainSocketAddress.of(var5));
               DBusConnection var7 = new DBusConnection(var6, var2, var0);
               var6 = null;
               return var7;
            } catch (IOException var12) {
               var3 = var12;
            } finally {
               if (var6 != null) {
                  var6.close();
               }
            }
         }
      }

      throw var3 != null ? var3 : new IOException("No D-Bus session socket found");
   }

   static List<String> candidateSocketPaths(String var0, String var1, String var2) {
      LinkedHashSet var3 = new LinkedHashSet();
      if (var0 != null) {
         for (String var7 : var0.split(";")) {
            int var8 = var7.indexOf(58);
            if (var8 >= 0 && var7.substring(0, var8).equals("unix")) {
               for (String var12 : var7.substring(var8 + 1).split(",")) {
                  if (var12.startsWith("path=")) {
                     String var13 = percentDecode(var12.substring(5));
                     if (var13 != null && !var13.isEmpty()) {
                        var3.add(var13);
                     }
                  }
               }
            }
         }
      }

      if (var1 != null && var1.startsWith("/")) {
         var3.add(var1.replaceAll("/+$", "") + "/bus");
      }

      if (var2 != null && var2.matches("\\d{1,10}")) {
         var3.add("/run/user/" + var2 + "/bus");
      }

      return List.copyOf(var3);
   }

   private static String percentDecode(String var0) {
      ByteArrayOutputStream var1 = new ByteArrayOutputStream();

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (var3 != '%') {
            var1.write(var3);
         } else {
            if (var2 + 2 >= var0.length()) {
               return null;
            }

            int var4 = Character.digit(var0.charAt(var2 + 1), 16);
            int var5 = Character.digit(var0.charAt(var2 + 2), 16);
            if (var4 < 0 || var5 < 0) {
               return null;
            }

            var1.write(var4 << 4 | var5);
            var2 += 2;
         }
      }

      return var1.toString(StandardCharsets.UTF_8);
   }

   private static String readUid() throws IOException {
      try {
         return String.valueOf(Files.getAttribute(Path.of("/proc/self"), "unix:uid"));
      } catch (UnsupportedOperationException | IllegalArgumentException var1) {
         throw new IOException("Cannot determine the Linux user for D-Bus", var1);
      }
   }

   private void authenticate(String var1, long var2) throws IOException, InterruptedException {
      StringBuilder var4 = new StringBuilder();

      for (byte var8 : var1.getBytes(StandardCharsets.US_ASCII)) {
         var4.append(String.format(Locale.ROOT, "%02x", var8));
      }

      this.readMessage(ByteBuffer.wrap(("\u0000AUTH EXTERNAL " + var4 + "\r\n").getBytes(StandardCharsets.US_ASCII)), var2);
      if (!this.authenticate(var2).startsWith("OK ")) {
         throw new IOException("D-Bus session rejected authentication");
      }

      this.readMessage(ByteBuffer.wrap("BEGIN\r\n".getBytes(StandardCharsets.US_ASCII)), var2);
   }

   private String authenticate(long var1) throws IOException, InterruptedException {
      StringBuilder var3 = new StringBuilder();
      ByteBuffer var4 = ByteBuffer.allocate(1);

      while (var3.length() < 512) {
         this.readFully(var4.clear(), var1);
         char var5 = (char)(var4.get(0) & 0xFF);
         if (var5 == '\n') {
            return var3.toString().strip();
         }

         var3.append(var5);
      }

      throw new IOException("Oversized D-Bus authentication reply");
   }

   List<Object> callMethod(String var1, String var2, String var3, String var4, long var5, String... var7) throws IOException, InterruptedException {
      this.serial = this.serial == Integer.MAX_VALUE ? 1 : this.serial + 1;
      int var8 = this.serial;
      this.readMessage(buildMethodCall(var8, var1, var2, var3, var4, var7), var5);

      while (System.nanoTime() - var5 < 0L) {
         DBusConnection.Message var9 = new DBusConnection.Message(this.readMessage(var5));
         if (var9.replySerial == var8 && (var9.messageType == 2 || var9.messageType == 3)) {
            return var9.messageType == 3 ? null : var9.readBody();
         }
      }

      throw new IOException("D-Bus request timed out");
   }

   static ByteBuffer buildMethodCall(int var0, String var1, String var2, String var3, String var4, String... var5) {
      ByteBuffer var6 = ByteBuffer.allocate(4096).order(ByteOrder.LITTLE_ENDIAN);
      var6.put((byte)108).put((byte)1).put((byte)0).put((byte)1).putInt(0).putInt(var0).putInt(0);
      writeHeaderField(var6, 1, "o", var2);
      writeHeaderField(var6, 6, "s", var1);
      writeHeaderField(var6, 2, "s", var3);
      writeHeaderField(var6, 3, "s", var4);
      if (var5.length > 0) {
         writeHeaderField(var6, 8, "g", "s".repeat(var5.length));
      }

      var6.putInt(12, var6.position() - 16);
      align(var6, 8);
      int var7 = var6.position();

      for (String var11 : var5) {
         writeString(var6, var11);
      }

      var6.putInt(4, var6.position() - var7);
      return var6.flip();
   }

   private static void writeHeaderField(ByteBuffer var0, int var1, String var2, String var3) {
      align(var0, 8);
      var0.put((byte)var1);
      writeSignature(var0, var2);
      if (var2.equals("g")) {
         writeSignature(var0, var3);
      } else {
         writeString(var0, var3);
      }
   }

   private static void align(ByteBuffer var0, int var1) {
      while (var0.position() % var1 != 0) {
         var0.put((byte)0);
      }
   }

   private static void writeString(ByteBuffer var0, String var1) {
      byte[] var2 = var1.getBytes(StandardCharsets.UTF_8);
      align(var0, 4);
      var0.putInt(var2.length).put(var2).put((byte)0);
   }

   private static void writeSignature(ByteBuffer var0, String var1) {
      byte[] var2 = var1.getBytes(StandardCharsets.US_ASCII);
      var0.put((byte)var2.length).put(var2).put((byte)0);
   }

   private ByteBuffer readMessage(long var1) throws IOException, InterruptedException {
      ByteBuffer var3 = ByteBuffer.allocate(16);
      this.readFully(var3, var1);
      byte var4 = var3.get(0);
      if (var4 != 108 && var4 != 66) {
         throw new IOException("Invalid D-Bus message");
      } else {
         var3.order(var4 == 108 ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN);
         long var5 = var3.getInt(12) & 4294967295L;
         long var7 = var3.getInt(4) & 4294967295L;
         long var9 = (16L + var5 + 7L & -8L) + var7;
         if (var3.get(3) == 1 && var9 <= 1048576L) {
            ByteBuffer var11 = ByteBuffer.allocate((int)var9).order(var3.order());
            var11.put(var3.flip());
            this.readFully(var11, var1);
            return var11.flip();
         } else {
            throw new IOException("Unsupported or oversized D-Bus message");
         }
      }
   }

   private void readFully(ByteBuffer var1, long var2) throws IOException, InterruptedException {
      while (var1.hasRemaining()) {
         int var4 = this.channel.read(var1);
         if (var4 < 0) {
            throw new EOFException("D-Bus session closed the connection");
         }

         if (var4 == 0) {
            this.awaitReady(1, var2);
         }
      }
   }

   private void readMessage(ByteBuffer var1, long var2) throws IOException, InterruptedException {
      while (var1.hasRemaining()) {
         if (this.channel.write(var1) == 0) {
            this.awaitReady(4, var2);
         }
      }
   }

   private void awaitReady(int var1, long var2) throws IOException, InterruptedException {
      if (this.closed) {
         throw new IOException("D-Bus session closed");
      }

      long var4 = var2 - System.nanoTime();
      if (var4 <= 0L) {
         throw new IOException("D-Bus request timed out");
      }

      try {
         SelectionKey var6 = this.channel.keyFor(this.selector);
         if (var6 == null) {
            throw new IOException("D-Bus session closed");
         }

         var6.interestOps(var1);
         this.selector.select(Math.max(1L, TimeUnit.NANOSECONDS.toMillis(var4)));
         this.selector.selectedKeys().clear();
      } catch (ClosedSelectorException | CancelledKeyException var7) {
         throw new IOException("D-Bus session closed", var7);
      }

      if (Thread.interrupted()) {
         throw new InterruptedException("D-Bus request interrupted");
      }

      if (this.closed) {
         throw new IOException("D-Bus session closed");
      }
   }

   @Override
   public void close() {
      this.closed = (boolean)1;

      try {
         this.channel.close();
      } catch (IOException var3) {
      }

      try {
         this.selector.close();
      } catch (IOException var2) {
      }
   }

   static final class Message {
      private final ByteBuffer buffer;
      private final int bodyStart;
      private int position;
      private int valueCount;
      final int messageType;
      long replySerial;
      private String signature = "";

      Message(ByteBuffer var1) throws IOException {
         this.buffer = var1;
         if (var1.limit() < 16) {
            throw invalidMessage();
         }

         this.messageType = var1.get(1);
         this.bodyStart = (int)(16L + (var1.getInt(12) & 4294967295L) + 7L & -8L);
         this.position = 12;
         Object var3 = this.readValue("a(yv)", new int[1], 0);
         if (var3 instanceof List) {
            for (Object var4 : (List)var3) {
               List var5 = (List)var4;
               long var6 = (Long)var5.get(0);
               if (var6 == 5L && var5.get(1) instanceof Long var8) {
                  this.replySerial = var8;
               }

               if (var6 == 8L && var5.get(1) instanceof String var11) {
                  this.signature = var11;
               }

               if (var6 == 9L && !Long.valueOf(0L).equals(var5.get(1))) {
                  throw new IOException("Unexpected D-Bus file descriptors");
               }
            }

            if (this.position > this.bodyStart || this.bodyStart > var1.limit()) {
               throw invalidMessage();
            }
         } else {
            throw invalidMessage();
         }
      }

      List<Object> readBody() throws IOException {
         this.position = this.bodyStart;
         ArrayList var1 = new ArrayList();
         int[] var2 = new int[]{0};

         while (var2[0] < this.signature.length()) {
            var1.add(this.readValue(this.signature, var2, 0));
         }

         if (this.position != this.buffer.limit()) {
            throw invalidMessage();
         } else {
            return var1;
         }
      }

      private Object readValue(String var1, int[] var2, int var3) throws IOException {
         if (var3 <= 32 && var2[0] < var1.length() && ++this.valueCount <= 65536) {
            char var4 = var1.charAt((int)(var2[0]++));
            switch (var4) {
               case '(':
                  this.align(8, 0);
                  ArrayList var15 = new ArrayList();

                  while (var2[0] < var1.length() && var1.charAt(var2[0]) != ')') {
                     var15.add(this.readValue(var1, var2, var3 + 1));
                  }

                  if (var2[0]++ < var1.length() && !var15.isEmpty()) {
                     return var15;
                  } else {
                     throw invalidMessage();
                  }
               case 'a':
                  long var14 = this.buffer.getInt(this.align(4, 4)) & 4294967295L;
                  int var16 = findTypeEnd(var1, var2[0], var3 + 1);
                  String var8 = var1.substring(var2[0], var16);
                  var2[0] = var16;
                  this.align(alignmentOf(var8.charAt(0)), 0);
                  if (var14 > this.buffer.limit() - this.position) {
                     throw invalidMessage();
                  } else {
                     int var9 = this.position + (int)var14;
                     if (var8.charAt(0) == '{') {
                        LinkedHashMap var17 = new LinkedHashMap();

                        while (this.position < var9) {
                           this.align(8, 0);
                           int[] var11 = new int[]{1};
                           Object var12 = this.readValue(var8, var11, var3 + 1);
                           var17.put(var12, this.readValue(var8, var11, var3 + 1));
                           if (var11[0] != var8.length() - 1) {
                              throw invalidMessage();
                           }
                        }

                        if (this.position != var9) {
                           throw invalidMessage();
                        }

                        return var17;
                     } else {
                        ArrayList var10 = new ArrayList();

                        while (this.position < var9) {
                           var10.add(this.readValue(var8, new int[1], var3 + 1));
                        }

                        if (this.position != var9) {
                           throw invalidMessage();
                        }

                        return var10;
                     }
                  }
               case 'b':
                  int var13 = this.buffer.getInt(this.align(4, 4));
                  if (var13 != 0 && var13 != 1) {
                     throw invalidMessage();
                  }

                  return var13 == 1;
               case 'd':
                  return this.buffer.getDouble(this.align(8, 8));
               case 'g':
                  return this.readString(this.buffer.get(this.align(1, 1)) & 0xFF);
               case 'h':
               case 'u':
                  return this.buffer.getInt(this.align(4, 4)) & 4294967295L;
               case 'i':
                  return (long)this.buffer.getInt(this.align(4, 4));
               case 'n':
                  return (long)this.buffer.getShort(this.align(2, 2));
               case 'o':
               case 's':
                  return this.readString(this.buffer.getInt(this.align(4, 4)) & 4294967295L);
               case 'q':
                  return (long)(this.buffer.getShort(this.align(2, 2)) & 65535);
               case 't':
               case 'x':
                  return this.buffer.getLong(this.align(8, 8));
               case 'v':
                  String var5 = this.readString(this.buffer.get(this.align(1, 1)) & 0xFF);
                  int[] var6 = new int[]{0};
                  Object var7 = this.readValue(var5, var6, var3 + 1);
                  if (var6[0] != var5.length()) {
                     throw invalidMessage();
                  }

                  return var7;
               case 'y':
                  return (long)(this.buffer.get(this.align(1, 1)) & 0xFF);
               default:
                  throw invalidMessage();
            }
         } else {
            throw invalidMessage();
         }
      }

      private String readString(long var1) throws IOException {
         if (var1 > this.buffer.limit()) {
            throw invalidMessage();
         }

         int var3 = this.align(1, (int)var1 + 1);
         if (this.buffer.get(var3 + (int)var1) != 0) {
            throw invalidMessage();
         }

         byte[] var4 = new byte[(int)var1];
         this.buffer.get(var3, var4);
         return new String(var4, StandardCharsets.UTF_8);
      }

      private int align(int var1, int var2) throws IOException {
         int var3 = this.position + var1 - 1 & -var1;
         if (var3 >= this.position && var3 <= this.buffer.limit() - var2) {
            this.position = var3 + var2;
            return var3;
         } else {
            throw invalidMessage();
         }
      }

      private static int findTypeEnd(String var0, int var1, int var2) throws IOException {
         if (var2 <= 32 && var1 < var0.length()) {
            char var3 = var0.charAt(var1);
            if (var3 == 'a') {
               return findTypeEnd(var0, var1 + 1, var2 + 1);
            }

            if (var3 == '(' || var3 == '{') {
               int var4 = var3 == '(' ? 41 : 125;
               int var5 = var1 + 1;

               while (var5 < var0.length() && var0.charAt(var5) != var4) {
                  var5 = findTypeEnd(var0, var5, var2 + 1);
               }

               if (var5 < var0.length() && var5 != var1 + 1) {
                  return var5 + 1;
               } else {
                  throw invalidMessage();
               }
            } else if ("ybnqiuxtdsogvh".indexOf(var3) < 0) {
               throw invalidMessage();
            } else {
               return var1 + 1;
            }
         } else {
            throw invalidMessage();
         }
      }

      private static int alignmentOf(char var0) {
         return switch (var0) {
            case '(', 'd', 't', 'x', '{' -> 8;
            case 'g', 'v', 'y' -> 1;
            case 'n', 'q' -> 2;
            default -> 4;
         };
      }

      private static IOException invalidMessage() {
         return new IOException("Invalid D-Bus message");
      }
   }
}
