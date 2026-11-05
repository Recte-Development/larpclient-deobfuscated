package com.larpclient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.PendingWriteQueue;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.ScheduledFuture;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

public final class u2 extends ChannelDuplexHandler {
   public static final String C = "larp_proxy";
   static final int U = 10;
   static final int Z = 8192;
   private final oe.u e;
   private InetSocketAddress t;
   private ChannelPromise X;
   private PendingWriteQueue n;
   private ByteBuf l = Unpooled.EMPTY_BUFFER;
   private ScheduledFuture<?> i;
   private boolean S;
   private boolean g;
   private boolean W;
   private int R;

   public u2(oe.u var1) {
      this.e = var1;
   }

   public void handlerAdded(ChannelHandlerContext var1) {
      this.n = new PendingWriteQueue(var1);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) {
      if (this.t != null) {
         var4.setFailure(new u2.i("Proxy connection was already started"));
      } else if (var2 instanceof InetSocketAddress var5) {
         this.t = var5;
         this.X = var4;
         var4.addListener(var1x -> {
            if (var1x.isCancelled()) {
               var1.close();
            }
         });
         ChannelPromise var6 = var1.newPromise();
         var6.addListener(var2x -> {
            if (!var2x.isSuccess() && !var2x.isCancelled()) {
               this.y(var1, new u2.i(this.M() + "could not reach the proxy", var2x.cause()));
            }
         });
         var1.connect(this.e.address(), var3, var6);
      } else {
         var4.setFailure(new u2.i("Proxy destinations must be network addresses"));
      }
   }

   public void channelActive(ChannelHandlerContext var1) {
      this.i = var1.executor().schedule(() -> this.y(var1, new u2.i(this.M() + "the proxy did not answer in time")), 10L, TimeUnit.SECONDS);

      try {
         this.w(var1);
      } catch (u2.i var3) {
         this.y(var1, var3);
      }
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) {
      if (this.S) {
         var1.fireChannelRead(var2);
      } else if (!(var2 instanceof ByteBuf var3)) {
         ReferenceCountUtil.release(var2);
      } else if (this.g) {
         var3.release();
      } else {
         if (this.l.isReadable()) {
            this.l = Unpooled.wrappedBuffer(new ByteBuf[]{this.l, var3});
         } else {
            this.l.release();
            this.l = var3;
         }

         try {
            if (this.l.readableBytes() > 8192) {
               throw new u2.i(this.M() + "the proxy sent an oversized reply");
            }

            while (!this.S && !this.g && this.W(var1)) {
            }
         } catch (u2.i var5) {
            this.y(var1, var5);
         }
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      if (this.S) {
         super.channelInactive(var1);
      } else {
         this.y(var1, new u2.i(this.M() + "the proxy closed the connection"));
      }
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
      if (this.S) {
         var1.fireExceptionCaught(var2);
      } else {
         this.y(var1, var2 instanceof u2.i var3 ? var3 : new u2.i(this.M() + "the proxy connection failed", var2));
      }
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) {
      if (this.S) {
         var1.write(var2, var3);
      } else if (this.g) {
         ReferenceCountUtil.release(var2);
         var3.setFailure(new u2.i(this.M() + "the proxy connection failed"));
      } else {
         this.n.add(var2, var3);
      }
   }

   public void flush(ChannelHandlerContext var1) {
      if (this.S) {
         var1.flush();
      } else {
         this.W = (boolean)1;
      }
   }

   public void handlerRemoved(ChannelHandlerContext var1) {
      if (this.i != null) {
         this.i.cancel(false);
      }

      if (!this.S) {
         if (!this.n.isEmpty()) {
            this.n.removeAndFailAll(new u2.i(this.M() + "the proxy connection closed"));
         }

         if (this.l.refCnt() > 0 && this.l != Unpooled.EMPTY_BUFFER) {
            this.l.release();
         }

         this.l = Unpooled.EMPTY_BUFFER;
      }
   }

   private void w(ChannelHandlerContext var1) throws u2.i {
      oe var2 = this.e.settings();
      ByteBuf var3 = var1.alloc().buffer();
      switch (var2.type()) {
         case x:
            if (var2.authenticated()) {
               var3.writeBytes(new byte[]{5, 2, 0, 2});
            } else {
               var3.writeBytes(new byte[]{5, 1, 0});
            }
            break;
         case q:
            var3.writeByte(4).writeByte(1).writeShort(this.t.getPort());
            byte[] var7 = var2.username().getBytes(StandardCharsets.UTF_8);
            if (this.t.isUnresolved()) {
               byte[] var6 = w(this.t.getHostString());
               var3.writeBytes(new byte[]{0, 0, 0, 1}).writeBytes(var7).writeByte(0).writeBytes(var6).writeByte(0);
            } else {
               if (!(this.t.getAddress() instanceof Inet4Address var8)) {
                  var3.release();
                  throw new u2.i(this.M() + "SOCKS4 proxies cannot reach IPv6 servers");
               }

               var3.writeBytes(var8.getAddress()).writeBytes(var7).writeByte(0);
            }
            break;
         case u:
            String var4 = Y(this.t);
            StringBuilder var5 = new StringBuilder("CONNECT ").append(var4).append(" HTTP/1.1\r\nHost: ").append(var4).append("\r\n");
            if (var2.authenticated()) {
               var5.append("Proxy-Authorization: Basic ")
                  .append(Base64.getEncoder().encodeToString((var2.username() + ":" + var2.password()).getBytes(StandardCharsets.UTF_8)))
                  .append("\r\n");
            }

            var3.writeBytes(var5.append("\r\n").toString().getBytes(StandardCharsets.ISO_8859_1));
      }

      var1.writeAndFlush(var3).addListener(var2x -> {
         if (!var2x.isSuccess()) {
            this.y(var1, new u2.i(this.M() + "could not talk to the proxy", var2x.cause()));
         }
      });
   }

   private boolean W(ChannelHandlerContext var1) throws u2.i {
      oe var2 = this.e.settings();
      switch (var2.type()) {
         case x:
            return this.r(var1, var2);
         case q:
            if (this.l.readableBytes() < 8) {
               return false;
            } else {
               short var10 = this.l.readUnsignedByte();
               short var11 = this.l.readUnsignedByte();
               this.l.skipBytes(6);
               if (var10 != 0) {
                  throw new u2.i(this.M() + "the proxy does not speak SOCKS4");
               } else {
                  if (var11 == 90) {
                     this.d(var1);
                     return true;
                  }

                  throw new u2.i(this.M() + (var11 != 92 && var11 != 93 ? "the proxy refused the connection" : "the proxy rejected the user ID"));
               }
            }
         case u:
            int var3 = this.s();
            if (var3 < 0) {
               return false;
            } else {
               String var4 = this.l.readCharSequence(var3 + 4 - this.l.readerIndex(), StandardCharsets.ISO_8859_1).toString();
               String var5 = var4.substring(0, Math.max(0, var4.indexOf("\r\n")));
               String[] var6 = var5.split(" ", 3);
               if (var6.length >= 2 && var6[0].startsWith("HTTP/1.")) {
                  int var7;
                  try {
                     var7 = Integer.parseInt(var6[1]);
                  } catch (NumberFormatException var9) {
                     throw new u2.i(this.M() + "the proxy sent an invalid reply");
                  }

                  if (var7 == 407) {
                     throw new u2.i(
                        this.M() + (var2.authenticated() ? "the proxy rejected the username or password" : "the proxy needs a username and password")
                     );
                  }

                  if (var7 / 100 != 2) {
                     throw new u2.i(this.M() + "the proxy refused the connection (HTTP " + var7 + ")");
                  }

                  this.d(var1);
                  return true;
               }

               throw new u2.i(this.M() + "the proxy does not speak HTTP CONNECT");
            }
         default:
            return false;
      }
   }

   private boolean r(ChannelHandlerContext var1, oe var2) throws u2.i {
      if (this.R == 0) {
         if (this.l.readableBytes() < 2) {
            return false;
         }

         short var7 = this.l.readUnsignedByte();
         short var8 = this.l.readUnsignedByte();
         if (var7 != 5) {
            throw new u2.i(this.M() + "the proxy does not speak SOCKS5");
         }

         if (var8 == 2 && var2.authenticated()) {
            byte[] var9 = var2.username().getBytes(StandardCharsets.UTF_8);
            byte[] var10 = var2.password().getBytes(StandardCharsets.UTF_8);
            var1.writeAndFlush(var1.alloc().buffer().writeByte(1).writeByte(var9.length).writeBytes(var9).writeByte(var10.length).writeBytes(var10));
            this.R = 1;
            return true;
         }

         if (var8 != 0) {
            throw new u2.i(
               this.M()
                  + (
                     var8 == 255
                        ? (var2.authenticated() ? "the proxy refused these login methods" : "the proxy needs a username and password")
                        : "the proxy chose an unsupported login method"
                  )
            );
         }

         this.c(var1);
         this.R = 2;
         return true;
      } else if (this.R == 1) {
         if (this.l.readableBytes() < 2) {
            return false;
         }

         this.l.readUnsignedByte();
         if (this.l.readUnsignedByte() != 0) {
            throw new u2.i(this.M() + "the proxy rejected the username or password");
         }

         this.c(var1);
         this.R = 2;
         return true;
      } else {
         if (this.l.readableBytes() < 5) {
            return false;
         }

         int var3 = this.l.readerIndex();
         short var4 = this.l.getUnsignedByte(var3 + 3);

         int var5 = switch (var4) {
            case 1 -> 10;
            default -> -1;
            case 3 -> 7 + this.l.getUnsignedByte(var3 + 4);
            case 4 -> 22;
         };
         if (this.l.getUnsignedByte(var3) != 5) {
            throw new u2.i(this.M() + "the proxy does not speak SOCKS5");
         }

         short var6 = this.l.getUnsignedByte(var3 + 1);
         if (var6 != 0) {
            throw new u2.i(this.M() + d(var6));
         }

         if (var5 < 0) {
            throw new u2.i(this.M() + "the proxy sent an invalid reply");
         }

         if (this.l.readableBytes() < var5) {
            return false;
         }

         this.l.skipBytes(var5);
         this.d(var1);
         return true;
      }
   }

   private void c(ChannelHandlerContext var1) throws u2.i {
      ByteBuf var2 = var1.alloc().buffer().writeBytes(new byte[]{5, 1, 0});
      if (this.t.isUnresolved()) {
         byte[] var4 = w(this.t.getHostString());
         if (var4.length > 255) {
            var2.release();
            throw new u2.i(this.M() + "the server name is too long for SOCKS5");
         }

         var2.writeByte(3).writeByte(var4.length).writeBytes(var4);
      } else if (this.t.getAddress() instanceof Inet6Address var3) {
         var2.writeByte(4).writeBytes(var3.getAddress());
      } else {
         var2.writeByte(1).writeBytes(this.t.getAddress().getAddress());
      }

      var2.writeShort(this.t.getPort());
      var1.writeAndFlush(var2);
   }

   private void d(ChannelHandlerContext var1) {
      if (this.i != null) {
         this.i.cancel(false);
      }

      this.S = (boolean)1;
      ByteBuf var2 = this.l;
      this.l = Unpooled.EMPTY_BUFFER;
      if (!this.X.trySuccess()) {
         var2.release();
         var1.close();
      } else {
         var1.fireChannelActive();
         this.n.removeAndWriteAll();
         if (this.W) {
            var1.flush();
         }

         if (var2.isReadable()) {
            var1.fireChannelRead(var2);
         } else {
            var2.release();
         }

         var1.pipeline().remove(this);
      }
   }

   private void y(ChannelHandlerContext var1, Throwable var2) {
      if (!this.S && !this.g) {
         this.g = (boolean)1;
         if (this.i != null) {
            this.i.cancel(false);
         }

         u2.i var3 = var2 instanceof u2.i var4 ? var4 : new u2.i(this.M() + "the proxy connection failed", var2);
         if (this.X != null) {
            this.X.tryFailure(var3);
         }

         if (!this.n.isEmpty()) {
            this.n.removeAndFailAll(var3);
         }

         var1.close();
      }
   }

   private int s() {
      int var1 = this.l.readerIndex();
      int var2 = this.l.writerIndex() - 3;

      for (int var3 = var1; var3 < var2; var3++) {
         if (this.l.getByte(var3) == 13 && this.l.getByte(var3 + 1) == 10 && this.l.getByte(var3 + 2) == 13 && this.l.getByte(var3 + 3) == 10) {
            return var3;
         }
      }

      return -1;
   }

   private String M() {
      return "Proxy " + this.e.settings().label() + ": ";
   }

   private static byte[] w(String var0) throws u2.i {
      if (!var0.matches("[A-Za-z0-9.-]{1,253}")) {
         throw new u2.i("Proxy destinations must be ASCII host names");
      } else {
         return var0.getBytes(StandardCharsets.US_ASCII);
      }
   }

   static String Y(InetSocketAddress var0) {
      String var1 = var0.isUnresolved() ? var0.getHostString() : var0.getAddress().getHostAddress();
      return (var1.indexOf(58) >= 0 ? "[" + var1 + "]" : var1) + ":" + var0.getPort();
   }

   static String d(int var0) {
      return switch (var0) {
         case 1 -> "the proxy failed";
         case 2 -> "the proxy does not allow this connection";
         case 3 -> "the proxy cannot reach that network";
         case 4 -> "the proxy cannot reach that server";
         case 5 -> "the server refused the connection through the proxy";
         case 6 -> "the proxy connection expired";
         case 7 -> "the proxy does not support connect requests";
         case 8 -> "the proxy does not support this address type";
         default -> "the proxy refused the connection (SOCKS5 reply " + var0 + ")";
      };
   }

   public static final class i extends IOException {
      public i(String var1) {
         super(var1);
      }

      public i(String var1, Throwable var2) {
         super(var1, var2);
      }
   }
}
