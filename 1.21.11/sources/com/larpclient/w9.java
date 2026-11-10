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

public final class w9 extends ChannelDuplexHandler {
   public static final String B = "larp_proxy";
   static final int n = 10;
   static final int z = 8192;
   private final id.u c;
   private InetSocketAddress A;
   private ChannelPromise r;
   private PendingWriteQueue O;
   private ByteBuf l = Unpooled.EMPTY_BUFFER;
   private ScheduledFuture<?> R;
   private boolean G;
   private boolean H;
   private boolean K;
   private int C;

   public w9(id.u var1) {
      this.c = var1;
   }

   public void handlerAdded(ChannelHandlerContext var1) {
      this.O = new PendingWriteQueue(var1);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) {
      if (this.A != null) {
         var4.setFailure(new w9.h("Proxy connection was already started"));
      } else if (var2 instanceof InetSocketAddress var5) {
         this.A = var5;
         this.r = var4;
         var4.addListener(var1x -> {
            if (var1x.isCancelled()) {
               var1.close();
            }
         });
         ChannelPromise var6 = var1.newPromise();
         var6.addListener(var2x -> {
            if (!var2x.isSuccess() && !var2x.isCancelled()) {
               this.R(var1, new w9.h(this.K() + "could not reach the proxy", var2x.cause()));
            }
         });
         var1.connect(this.c.address(), var3, var6);
      } else {
         var4.setFailure(new w9.h("Proxy destinations must be network addresses"));
      }
   }

   public void channelActive(ChannelHandlerContext var1) {
      this.R = var1.executor().schedule(() -> this.R(var1, new w9.h(this.K() + "the proxy did not answer in time")), 10L, TimeUnit.SECONDS);

      try {
         this.T(var1);
      } catch (w9.h var3) {
         this.R(var1, var3);
      }
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) {
      if (this.G) {
         var1.fireChannelRead(var2);
      } else if (!(var2 instanceof ByteBuf var3)) {
         ReferenceCountUtil.release(var2);
      } else if (this.H) {
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
               throw new w9.h(this.K() + "the proxy sent an oversized reply");
            }

            while (!this.G && !this.H && this.q(var1)) {
            }
         } catch (w9.h var5) {
            this.R(var1, var5);
         }
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      if (this.G) {
         super.channelInactive(var1);
      } else {
         this.R(var1, new w9.h(this.K() + "the proxy closed the connection"));
      }
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
      if (this.G) {
         var1.fireExceptionCaught(var2);
      } else {
         this.R(var1, var2 instanceof w9.h var3 ? var3 : new w9.h(this.K() + "the proxy connection failed", var2));
      }
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) {
      if (this.G) {
         var1.write(var2, var3);
      } else if (this.H) {
         ReferenceCountUtil.release(var2);
         var3.setFailure(new w9.h(this.K() + "the proxy connection failed"));
      } else {
         this.O.add(var2, var3);
      }
   }

   public void flush(ChannelHandlerContext var1) {
      if (this.G) {
         var1.flush();
      } else {
         this.K = (boolean)1;
      }
   }

   public void handlerRemoved(ChannelHandlerContext var1) {
      if (this.R != null) {
         this.R.cancel(false);
      }

      if (!this.G) {
         if (!this.O.isEmpty()) {
            this.O.removeAndFailAll(new w9.h(this.K() + "the proxy connection closed"));
         }

         if (this.l.refCnt() > 0 && this.l != Unpooled.EMPTY_BUFFER) {
            this.l.release();
         }

         this.l = Unpooled.EMPTY_BUFFER;
      }
   }

   private void T(ChannelHandlerContext var1) throws w9.h {
      id var2 = this.c.settings();
      ByteBuf var3 = var1.alloc().buffer();
      switch (var2.type()) {
         case J:
            if (var2.authenticated()) {
               var3.writeBytes(new byte[]{5, 2, 0, 2});
            } else {
               var3.writeBytes(new byte[]{5, 1, 0});
            }
            break;
         case h:
            var3.writeByte(4).writeByte(1).writeShort(this.A.getPort());
            byte[] var7 = var2.username().getBytes(StandardCharsets.UTF_8);
            if (this.A.isUnresolved()) {
               byte[] var6 = m(this.A.getHostString());
               var3.writeBytes(new byte[]{0, 0, 0, 1}).writeBytes(var7).writeByte(0).writeBytes(var6).writeByte(0);
            } else {
               if (!(this.A.getAddress() instanceof Inet4Address var8)) {
                  var3.release();
                  throw new w9.h(this.K() + "SOCKS4 proxies cannot reach IPv6 servers");
               }

               var3.writeBytes(var8.getAddress()).writeBytes(var7).writeByte(0);
            }
            break;
         case M:
            String var4 = V(this.A);
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
            this.R(var1, new w9.h(this.K() + "could not talk to the proxy", var2x.cause()));
         }
      });
   }

   private boolean q(ChannelHandlerContext var1) throws w9.h {
      id var2 = this.c.settings();
      switch (var2.type()) {
         case J:
            return this.H(var1, var2);
         case h:
            if (this.l.readableBytes() < 8) {
               return false;
            } else {
               short var10 = this.l.readUnsignedByte();
               short var11 = this.l.readUnsignedByte();
               this.l.skipBytes(6);
               if (var10 != 0) {
                  throw new w9.h(this.K() + "the proxy does not speak SOCKS4");
               } else {
                  if (var11 == 90) {
                     this.u(var1);
                     return true;
                  }

                  throw new w9.h(this.K() + (var11 != 92 && var11 != 93 ? "the proxy refused the connection" : "the proxy rejected the user ID"));
               }
            }
         case M:
            int var3 = this.C();
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
                     throw new w9.h(this.K() + "the proxy sent an invalid reply");
                  }

                  if (var7 == 407) {
                     throw new w9.h(
                        this.K() + (var2.authenticated() ? "the proxy rejected the username or password" : "the proxy needs a username and password")
                     );
                  }

                  if (var7 / 100 != 2) {
                     throw new w9.h(this.K() + "the proxy refused the connection (HTTP " + var7 + ")");
                  }

                  this.u(var1);
                  return true;
               }

               throw new w9.h(this.K() + "the proxy does not speak HTTP CONNECT");
            }
         default:
            return false;
      }
   }

   private boolean H(ChannelHandlerContext var1, id var2) throws w9.h {
      if (this.C == 0) {
         if (this.l.readableBytes() < 2) {
            return false;
         }

         short var7 = this.l.readUnsignedByte();
         short var8 = this.l.readUnsignedByte();
         if (var7 != 5) {
            throw new w9.h(this.K() + "the proxy does not speak SOCKS5");
         }

         if (var8 == 2 && var2.authenticated()) {
            byte[] var9 = var2.username().getBytes(StandardCharsets.UTF_8);
            byte[] var10 = var2.password().getBytes(StandardCharsets.UTF_8);
            var1.writeAndFlush(var1.alloc().buffer().writeByte(1).writeByte(var9.length).writeBytes(var9).writeByte(var10.length).writeBytes(var10));
            this.C = 1;
            return true;
         }

         if (var8 != 0) {
            throw new w9.h(
               this.K()
                  + (
                     var8 == 255
                        ? (var2.authenticated() ? "the proxy refused these login methods" : "the proxy needs a username and password")
                        : "the proxy chose an unsupported login method"
                  )
            );
         }

         this.H(var1);
         this.C = 2;
         return true;
      } else if (this.C == 1) {
         if (this.l.readableBytes() < 2) {
            return false;
         }

         this.l.readUnsignedByte();
         if (this.l.readUnsignedByte() != 0) {
            throw new w9.h(this.K() + "the proxy rejected the username or password");
         }

         this.H(var1);
         this.C = 2;
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
            throw new w9.h(this.K() + "the proxy does not speak SOCKS5");
         }

         short var6 = this.l.getUnsignedByte(var3 + 1);
         if (var6 != 0) {
            throw new w9.h(this.K() + m(var6));
         }

         if (var5 < 0) {
            throw new w9.h(this.K() + "the proxy sent an invalid reply");
         }

         if (this.l.readableBytes() < var5) {
            return false;
         }

         this.l.skipBytes(var5);
         this.u(var1);
         return true;
      }
   }

   private void H(ChannelHandlerContext var1) throws w9.h {
      ByteBuf var2 = var1.alloc().buffer().writeBytes(new byte[]{5, 1, 0});
      if (this.A.isUnresolved()) {
         byte[] var4 = m(this.A.getHostString());
         if (var4.length > 255) {
            var2.release();
            throw new w9.h(this.K() + "the server name is too long for SOCKS5");
         }

         var2.writeByte(3).writeByte(var4.length).writeBytes(var4);
      } else if (this.A.getAddress() instanceof Inet6Address var3) {
         var2.writeByte(4).writeBytes(var3.getAddress());
      } else {
         var2.writeByte(1).writeBytes(this.A.getAddress().getAddress());
      }

      var2.writeShort(this.A.getPort());
      var1.writeAndFlush(var2);
   }

   private void u(ChannelHandlerContext var1) {
      if (this.R != null) {
         this.R.cancel(false);
      }

      this.G = (boolean)1;
      ByteBuf var2 = this.l;
      this.l = Unpooled.EMPTY_BUFFER;
      if (!this.r.trySuccess()) {
         var2.release();
         var1.close();
      } else {
         var1.fireChannelActive();
         this.O.removeAndWriteAll();
         if (this.K) {
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

   private void R(ChannelHandlerContext var1, Throwable var2) {
      if (!this.G && !this.H) {
         this.H = (boolean)1;
         if (this.R != null) {
            this.R.cancel(false);
         }

         w9.h var3 = var2 instanceof w9.h var4 ? var4 : new w9.h(this.K() + "the proxy connection failed", var2);
         if (this.r != null) {
            this.r.tryFailure(var3);
         }

         if (!this.O.isEmpty()) {
            this.O.removeAndFailAll(var3);
         }

         var1.close();
      }
   }

   private int C() {
      int var1 = this.l.readerIndex();
      int var2 = this.l.writerIndex() - 3;

      for (int var3 = var1; var3 < var2; var3++) {
         if (this.l.getByte(var3) == 13 && this.l.getByte(var3 + 1) == 10 && this.l.getByte(var3 + 2) == 13 && this.l.getByte(var3 + 3) == 10) {
            return var3;
         }
      }

      return -1;
   }

   private String K() {
      return "Proxy " + this.c.settings().label() + ": ";
   }

   private static byte[] m(String var0) throws w9.h {
      if (!var0.matches("[A-Za-z0-9.-]{1,253}")) {
         throw new w9.h("Proxy destinations must be ASCII host names");
      } else {
         return var0.getBytes(StandardCharsets.US_ASCII);
      }
   }

   static String V(InetSocketAddress var0) {
      String var1 = var0.isUnresolved() ? var0.getHostString() : var0.getAddress().getHostAddress();
      return (var1.indexOf(58) >= 0 ? "[" + var1 + "]" : var1) + ":" + var0.getPort();
   }

   static String m(int var0) {
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

   public static final class h extends IOException {
      public h(String var1) {
         super(var1);
      }

      public h(String var1, Throwable var2) {
         super(var1, var2);
      }
   }
}
