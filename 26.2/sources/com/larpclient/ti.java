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

public final class ti extends ChannelDuplexHandler {
   public static final String B = "larp_proxy";
   static final int p = 10;
   static final int f = 8192;
   private final p8.v x;
   private InetSocketAddress N;
   private ChannelPromise I;
   private PendingWriteQueue Q;
   private ByteBuf F = Unpooled.EMPTY_BUFFER;
   private ScheduledFuture<?> L;
   private boolean o;
   private boolean d;
   private boolean g;
   private int n;

   public ti(p8.v var1) {
      this.x = var1;
   }

   public void handlerAdded(ChannelHandlerContext var1) {
      this.Q = new PendingWriteQueue(var1);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) {
      if (this.N != null) {
         var4.setFailure(new ti.r("Proxy connection was already started"));
      } else if (var2 instanceof InetSocketAddress var5) {
         this.N = var5;
         this.I = var4;
         var4.addListener(var1x -> {
            if (var1x.isCancelled()) {
               var1.close();
            }
         });
         ChannelPromise var6 = var1.newPromise();
         var6.addListener(var2x -> {
            if (!var2x.isSuccess() && !var2x.isCancelled()) {
               this.B(var1, new ti.r(this.a() + "could not reach the proxy", var2x.cause()));
            }
         });
         var1.connect(this.x.address(), var3, var6);
      } else {
         var4.setFailure(new ti.r("Proxy destinations must be network addresses"));
      }
   }

   public void channelActive(ChannelHandlerContext var1) {
      this.L = var1.executor().schedule(() -> this.B(var1, new ti.r(this.a() + "the proxy did not answer in time")), 10L, TimeUnit.SECONDS);

      try {
         this.M(var1);
      } catch (ti.r var3) {
         this.B(var1, var3);
      }
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) {
      if (this.o) {
         var1.fireChannelRead(var2);
      } else if (!(var2 instanceof ByteBuf var3)) {
         ReferenceCountUtil.release(var2);
      } else if (this.d) {
         var3.release();
      } else {
         if (this.F.isReadable()) {
            this.F = Unpooled.wrappedBuffer(new ByteBuf[]{this.F, var3});
         } else {
            this.F.release();
            this.F = var3;
         }

         try {
            if (this.F.readableBytes() > 8192) {
               throw new ti.r(this.a() + "the proxy sent an oversized reply");
            }

            while (!this.o && !this.d && this.e(var1)) {
            }
         } catch (ti.r var5) {
            this.B(var1, var5);
         }
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      if (this.o) {
         super.channelInactive(var1);
      } else {
         this.B(var1, new ti.r(this.a() + "the proxy closed the connection"));
      }
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
      if (this.o) {
         var1.fireExceptionCaught(var2);
      } else {
         this.B(var1, var2 instanceof ti.r var3 ? var3 : new ti.r(this.a() + "the proxy connection failed", var2));
      }
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) {
      if (this.o) {
         var1.write(var2, var3);
      } else if (this.d) {
         ReferenceCountUtil.release(var2);
         var3.setFailure(new ti.r(this.a() + "the proxy connection failed"));
      } else {
         this.Q.add(var2, var3);
      }
   }

   public void flush(ChannelHandlerContext var1) {
      if (this.o) {
         var1.flush();
      } else {
         this.g = (boolean)1;
      }
   }

   public void handlerRemoved(ChannelHandlerContext var1) {
      if (this.L != null) {
         this.L.cancel(false);
      }

      if (!this.o) {
         if (!this.Q.isEmpty()) {
            this.Q.removeAndFailAll(new ti.r(this.a() + "the proxy connection closed"));
         }

         if (this.F.refCnt() > 0 && this.F != Unpooled.EMPTY_BUFFER) {
            this.F.release();
         }

         this.F = Unpooled.EMPTY_BUFFER;
      }
   }

   private void M(ChannelHandlerContext var1) throws ti.r {
      p8 var2 = this.x.settings();
      ByteBuf var3 = var1.alloc().buffer();
      switch (var2.type()) {
         case B:
            if (var2.authenticated()) {
               var3.writeBytes(new byte[]{5, 2, 0, 2});
            } else {
               var3.writeBytes(new byte[]{5, 1, 0});
            }
            break;
         case e:
            var3.writeByte(4).writeByte(1).writeShort(this.N.getPort());
            byte[] var7 = var2.username().getBytes(StandardCharsets.UTF_8);
            if (this.N.isUnresolved()) {
               byte[] var6 = X(this.N.getHostString());
               var3.writeBytes(new byte[]{0, 0, 0, 1}).writeBytes(var7).writeByte(0).writeBytes(var6).writeByte(0);
            } else {
               if (!(this.N.getAddress() instanceof Inet4Address var8)) {
                  var3.release();
                  throw new ti.r(this.a() + "SOCKS4 proxies cannot reach IPv6 servers");
               }

               var3.writeBytes(var8.getAddress()).writeBytes(var7).writeByte(0);
            }
            break;
         case L:
            String var4 = X(this.N);
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
            this.B(var1, new ti.r(this.a() + "could not talk to the proxy", var2x.cause()));
         }
      });
   }

   private boolean e(ChannelHandlerContext var1) throws ti.r {
      p8 var2 = this.x.settings();
      switch (var2.type()) {
         case B:
            return this.R(var1, var2);
         case e:
            if (this.F.readableBytes() < 8) {
               return false;
            } else {
               short var10 = this.F.readUnsignedByte();
               short var11 = this.F.readUnsignedByte();
               this.F.skipBytes(6);
               if (var10 != 0) {
                  throw new ti.r(this.a() + "the proxy does not speak SOCKS4");
               } else {
                  if (var11 == 90) {
                     this.N(var1);
                     return true;
                  }

                  throw new ti.r(this.a() + (var11 != 92 && var11 != 93 ? "the proxy refused the connection" : "the proxy rejected the user ID"));
               }
            }
         case L:
            int var3 = this.K();
            if (var3 < 0) {
               return false;
            } else {
               String var4 = this.F.readCharSequence(var3 + 4 - this.F.readerIndex(), StandardCharsets.ISO_8859_1).toString();
               String var5 = var4.substring(0, Math.max(0, var4.indexOf("\r\n")));
               String[] var6 = var5.split(" ", 3);
               if (var6.length >= 2 && var6[0].startsWith("HTTP/1.")) {
                  int var7;
                  try {
                     var7 = Integer.parseInt(var6[1]);
                  } catch (NumberFormatException var9) {
                     throw new ti.r(this.a() + "the proxy sent an invalid reply");
                  }

                  if (var7 == 407) {
                     throw new ti.r(
                        this.a() + (var2.authenticated() ? "the proxy rejected the username or password" : "the proxy needs a username and password")
                     );
                  }

                  if (var7 / 100 != 2) {
                     throw new ti.r(this.a() + "the proxy refused the connection (HTTP " + var7 + ")");
                  }

                  this.N(var1);
                  return true;
               }

               throw new ti.r(this.a() + "the proxy does not speak HTTP CONNECT");
            }
         default:
            return false;
      }
   }

   private boolean R(ChannelHandlerContext var1, p8 var2) throws ti.r {
      if (this.n == 0) {
         if (this.F.readableBytes() < 2) {
            return false;
         }

         short var7 = this.F.readUnsignedByte();
         short var8 = this.F.readUnsignedByte();
         if (var7 != 5) {
            throw new ti.r(this.a() + "the proxy does not speak SOCKS5");
         }

         if (var8 == 2 && var2.authenticated()) {
            byte[] var9 = var2.username().getBytes(StandardCharsets.UTF_8);
            byte[] var10 = var2.password().getBytes(StandardCharsets.UTF_8);
            var1.writeAndFlush(var1.alloc().buffer().writeByte(1).writeByte(var9.length).writeBytes(var9).writeByte(var10.length).writeBytes(var10));
            this.n = 1;
            return true;
         }

         if (var8 != 0) {
            throw new ti.r(
               this.a()
                  + (
                     var8 == 255
                        ? (var2.authenticated() ? "the proxy refused these login methods" : "the proxy needs a username and password")
                        : "the proxy chose an unsupported login method"
                  )
            );
         }

         this.U(var1);
         this.n = 2;
         return true;
      } else if (this.n == 1) {
         if (this.F.readableBytes() < 2) {
            return false;
         }

         this.F.readUnsignedByte();
         if (this.F.readUnsignedByte() != 0) {
            throw new ti.r(this.a() + "the proxy rejected the username or password");
         }

         this.U(var1);
         this.n = 2;
         return true;
      } else {
         if (this.F.readableBytes() < 5) {
            return false;
         }

         int var3 = this.F.readerIndex();
         short var4 = this.F.getUnsignedByte(var3 + 3);

         int var5 = switch (var4) {
            case 1 -> 10;
            default -> -1;
            case 3 -> 7 + this.F.getUnsignedByte(var3 + 4);
            case 4 -> 22;
         };
         if (this.F.getUnsignedByte(var3) != 5) {
            throw new ti.r(this.a() + "the proxy does not speak SOCKS5");
         }

         short var6 = this.F.getUnsignedByte(var3 + 1);
         if (var6 != 0) {
            throw new ti.r(this.a() + g(var6));
         }

         if (var5 < 0) {
            throw new ti.r(this.a() + "the proxy sent an invalid reply");
         }

         if (this.F.readableBytes() < var5) {
            return false;
         }

         this.F.skipBytes(var5);
         this.N(var1);
         return true;
      }
   }

   private void U(ChannelHandlerContext var1) throws ti.r {
      ByteBuf var2 = var1.alloc().buffer().writeBytes(new byte[]{5, 1, 0});
      if (this.N.isUnresolved()) {
         byte[] var4 = X(this.N.getHostString());
         if (var4.length > 255) {
            var2.release();
            throw new ti.r(this.a() + "the server name is too long for SOCKS5");
         }

         var2.writeByte(3).writeByte(var4.length).writeBytes(var4);
      } else if (this.N.getAddress() instanceof Inet6Address var3) {
         var2.writeByte(4).writeBytes(var3.getAddress());
      } else {
         var2.writeByte(1).writeBytes(this.N.getAddress().getAddress());
      }

      var2.writeShort(this.N.getPort());
      var1.writeAndFlush(var2);
   }

   private void N(ChannelHandlerContext var1) {
      if (this.L != null) {
         this.L.cancel(false);
      }

      this.o = (boolean)1;
      ByteBuf var2 = this.F;
      this.F = Unpooled.EMPTY_BUFFER;
      if (!this.I.trySuccess()) {
         var2.release();
         var1.close();
      } else {
         var1.fireChannelActive();
         this.Q.removeAndWriteAll();
         if (this.g) {
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

   private void B(ChannelHandlerContext var1, Throwable var2) {
      if (!this.o && !this.d) {
         this.d = (boolean)1;
         if (this.L != null) {
            this.L.cancel(false);
         }

         ti.r var3 = var2 instanceof ti.r var4 ? var4 : new ti.r(this.a() + "the proxy connection failed", var2);
         if (this.I != null) {
            this.I.tryFailure(var3);
         }

         if (!this.Q.isEmpty()) {
            this.Q.removeAndFailAll(var3);
         }

         var1.close();
      }
   }

   private int K() {
      int var1 = this.F.readerIndex();
      int var2 = this.F.writerIndex() - 3;

      for (int var3 = var1; var3 < var2; var3++) {
         if (this.F.getByte(var3) == 13 && this.F.getByte(var3 + 1) == 10 && this.F.getByte(var3 + 2) == 13 && this.F.getByte(var3 + 3) == 10) {
            return var3;
         }
      }

      return -1;
   }

   private String a() {
      return "Proxy " + this.x.settings().label() + ": ";
   }

   private static byte[] X(String var0) throws ti.r {
      if (!var0.matches("[A-Za-z0-9.-]{1,253}")) {
         throw new ti.r("Proxy destinations must be ASCII host names");
      } else {
         return var0.getBytes(StandardCharsets.US_ASCII);
      }
   }

   static String X(InetSocketAddress var0) {
      String var1 = var0.isUnresolved() ? var0.getHostString() : var0.getAddress().getHostAddress();
      return (var1.indexOf(58) >= 0 ? "[" + var1 + "]" : var1) + ":" + var0.getPort();
   }

   static String g(int var0) {
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

   public static final class r extends IOException {
      public r(String var1) {
         super(var1);
      }

      public r(String var1, Throwable var2) {
         super(var1, var2);
      }
   }
}
