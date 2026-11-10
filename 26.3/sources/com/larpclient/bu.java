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

public final class bu extends ChannelDuplexHandler {
   public static final String z = "larp_proxy";
   static final int q = 10;
   static final int S = 8192;
   private final ou.o d;
   private InetSocketAddress D;
   private ChannelPromise a;
   private PendingWriteQueue K;
   private ByteBuf C = Unpooled.EMPTY_BUFFER;
   private ScheduledFuture<?> f;
   private boolean n;
   private boolean X;
   private boolean s;
   private int m;

   public bu(ou.o var1) {
      this.d = var1;
   }

   public void handlerAdded(ChannelHandlerContext var1) {
      this.K = new PendingWriteQueue(var1);
   }

   public void connect(ChannelHandlerContext var1, SocketAddress var2, SocketAddress var3, ChannelPromise var4) {
      if (this.D != null) {
         var4.setFailure(new bu.j("Proxy connection was already started"));
      } else if (var2 instanceof InetSocketAddress var5) {
         this.D = var5;
         this.a = var4;
         var4.addListener(var1x -> {
            if (var1x.isCancelled()) {
               var1.close();
            }
         });
         ChannelPromise var6 = var1.newPromise();
         var6.addListener(var2x -> {
            if (!var2x.isSuccess() && !var2x.isCancelled()) {
               this.B(var1, new bu.j(this.Y() + "could not reach the proxy", var2x.cause()));
            }
         });
         var1.connect(this.d.address(), var3, var6);
      } else {
         var4.setFailure(new bu.j("Proxy destinations must be network addresses"));
      }
   }

   public void channelActive(ChannelHandlerContext var1) {
      this.f = var1.executor().schedule(() -> this.B(var1, new bu.j(this.Y() + "the proxy did not answer in time")), 10L, TimeUnit.SECONDS);

      try {
         this.e(var1);
      } catch (bu.j var3) {
         this.B(var1, var3);
      }
   }

   public void channelRead(ChannelHandlerContext var1, Object var2) {
      if (this.n) {
         var1.fireChannelRead(var2);
      } else if (!(var2 instanceof ByteBuf var3)) {
         ReferenceCountUtil.release(var2);
      } else if (this.X) {
         var3.release();
      } else {
         if (this.C.isReadable()) {
            this.C = Unpooled.wrappedBuffer(new ByteBuf[]{this.C, var3});
         } else {
            this.C.release();
            this.C = var3;
         }

         try {
            if (this.C.readableBytes() > 8192) {
               throw new bu.j(this.Y() + "the proxy sent an oversized reply");
            }

            while (!this.n && !this.X && this.b(var1)) {
            }
         } catch (bu.j var5) {
            this.B(var1, var5);
         }
      }
   }

   public void channelInactive(ChannelHandlerContext var1) throws Exception {
      if (this.n) {
         super.channelInactive(var1);
      } else {
         this.B(var1, new bu.j(this.Y() + "the proxy closed the connection"));
      }
   }

   public void exceptionCaught(ChannelHandlerContext var1, Throwable var2) {
      if (this.n) {
         var1.fireExceptionCaught(var2);
      } else {
         this.B(var1, var2 instanceof bu.j var3 ? var3 : new bu.j(this.Y() + "the proxy connection failed", var2));
      }
   }

   public void write(ChannelHandlerContext var1, Object var2, ChannelPromise var3) {
      if (this.n) {
         var1.write(var2, var3);
      } else if (this.X) {
         ReferenceCountUtil.release(var2);
         var3.setFailure(new bu.j(this.Y() + "the proxy connection failed"));
      } else {
         this.K.add(var2, var3);
      }
   }

   public void flush(ChannelHandlerContext var1) {
      if (this.n) {
         var1.flush();
      } else {
         this.s = (boolean)1;
      }
   }

   public void handlerRemoved(ChannelHandlerContext var1) {
      if (this.f != null) {
         this.f.cancel(false);
      }

      if (!this.n) {
         if (!this.K.isEmpty()) {
            this.K.removeAndFailAll(new bu.j(this.Y() + "the proxy connection closed"));
         }

         if (this.C.refCnt() > 0 && this.C != Unpooled.EMPTY_BUFFER) {
            this.C.release();
         }

         this.C = Unpooled.EMPTY_BUFFER;
      }
   }

   private void e(ChannelHandlerContext var1) throws bu.j {
      ou var2 = this.d.settings();
      ByteBuf var3 = var1.alloc().buffer();
      switch (var2.type()) {
         case r:
            if (var2.authenticated()) {
               var3.writeBytes(new byte[]{5, 2, 0, 2});
            } else {
               var3.writeBytes(new byte[]{5, 1, 0});
            }
            break;
         case C:
            var3.writeByte(4).writeByte(1).writeShort(this.D.getPort());
            byte[] var7 = var2.username().getBytes(StandardCharsets.UTF_8);
            if (this.D.isUnresolved()) {
               byte[] var6 = H(this.D.getHostString());
               var3.writeBytes(new byte[]{0, 0, 0, 1}).writeBytes(var7).writeByte(0).writeBytes(var6).writeByte(0);
            } else {
               if (!(this.D.getAddress() instanceof Inet4Address var8)) {
                  var3.release();
                  throw new bu.j(this.Y() + "SOCKS4 proxies cannot reach IPv6 servers");
               }

               var3.writeBytes(var8.getAddress()).writeBytes(var7).writeByte(0);
            }
            break;
         case p:
            String var4 = s(this.D);
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
            this.B(var1, new bu.j(this.Y() + "could not talk to the proxy", var2x.cause()));
         }
      });
   }

   private boolean b(ChannelHandlerContext var1) throws bu.j {
      ou var2 = this.d.settings();
      switch (var2.type()) {
         case r:
            return this.i(var1, var2);
         case C:
            if (this.C.readableBytes() < 8) {
               return false;
            } else {
               short var10 = this.C.readUnsignedByte();
               short var11 = this.C.readUnsignedByte();
               this.C.skipBytes(6);
               if (var10 != 0) {
                  throw new bu.j(this.Y() + "the proxy does not speak SOCKS4");
               } else {
                  if (var11 == 90) {
                     this.d(var1);
                     return true;
                  }

                  throw new bu.j(this.Y() + (var11 != 92 && var11 != 93 ? "the proxy refused the connection" : "the proxy rejected the user ID"));
               }
            }
         case p:
            int var3 = this.A();
            if (var3 < 0) {
               return false;
            } else {
               String var4 = this.C.readCharSequence(var3 + 4 - this.C.readerIndex(), StandardCharsets.ISO_8859_1).toString();
               String var5 = var4.substring(0, Math.max(0, var4.indexOf("\r\n")));
               String[] var6 = var5.split(" ", 3);
               if (var6.length >= 2 && var6[0].startsWith("HTTP/1.")) {
                  int var7;
                  try {
                     var7 = Integer.parseInt(var6[1]);
                  } catch (NumberFormatException var9) {
                     throw new bu.j(this.Y() + "the proxy sent an invalid reply");
                  }

                  if (var7 == 407) {
                     throw new bu.j(
                        this.Y() + (var2.authenticated() ? "the proxy rejected the username or password" : "the proxy needs a username and password")
                     );
                  }

                  if (var7 / 100 != 2) {
                     throw new bu.j(this.Y() + "the proxy refused the connection (HTTP " + var7 + ")");
                  }

                  this.d(var1);
                  return true;
               }

               throw new bu.j(this.Y() + "the proxy does not speak HTTP CONNECT");
            }
         default:
            return false;
      }
   }

   private boolean i(ChannelHandlerContext var1, ou var2) throws bu.j {
      if (this.m == 0) {
         if (this.C.readableBytes() < 2) {
            return false;
         }

         short var7 = this.C.readUnsignedByte();
         short var8 = this.C.readUnsignedByte();
         if (var7 != 5) {
            throw new bu.j(this.Y() + "the proxy does not speak SOCKS5");
         }

         if (var8 == 2 && var2.authenticated()) {
            byte[] var9 = var2.username().getBytes(StandardCharsets.UTF_8);
            byte[] var10 = var2.password().getBytes(StandardCharsets.UTF_8);
            var1.writeAndFlush(var1.alloc().buffer().writeByte(1).writeByte(var9.length).writeBytes(var9).writeByte(var10.length).writeBytes(var10));
            this.m = 1;
            return true;
         }

         if (var8 != 0) {
            throw new bu.j(
               this.Y()
                  + (
                     var8 == 255
                        ? (var2.authenticated() ? "the proxy refused these login methods" : "the proxy needs a username and password")
                        : "the proxy chose an unsupported login method"
                  )
            );
         }

         this.C(var1);
         this.m = 2;
         return true;
      } else if (this.m == 1) {
         if (this.C.readableBytes() < 2) {
            return false;
         }

         this.C.readUnsignedByte();
         if (this.C.readUnsignedByte() != 0) {
            throw new bu.j(this.Y() + "the proxy rejected the username or password");
         }

         this.C(var1);
         this.m = 2;
         return true;
      } else {
         if (this.C.readableBytes() < 5) {
            return false;
         }

         int var3 = this.C.readerIndex();
         short var4 = this.C.getUnsignedByte(var3 + 3);

         int var5 = switch (var4) {
            case 1 -> 10;
            default -> -1;
            case 3 -> 7 + this.C.getUnsignedByte(var3 + 4);
            case 4 -> 22;
         };
         if (this.C.getUnsignedByte(var3) != 5) {
            throw new bu.j(this.Y() + "the proxy does not speak SOCKS5");
         }

         short var6 = this.C.getUnsignedByte(var3 + 1);
         if (var6 != 0) {
            throw new bu.j(this.Y() + d(var6));
         }

         if (var5 < 0) {
            throw new bu.j(this.Y() + "the proxy sent an invalid reply");
         }

         if (this.C.readableBytes() < var5) {
            return false;
         }

         this.C.skipBytes(var5);
         this.d(var1);
         return true;
      }
   }

   private void C(ChannelHandlerContext var1) throws bu.j {
      ByteBuf var2 = var1.alloc().buffer().writeBytes(new byte[]{5, 1, 0});
      if (this.D.isUnresolved()) {
         byte[] var4 = H(this.D.getHostString());
         if (var4.length > 255) {
            var2.release();
            throw new bu.j(this.Y() + "the server name is too long for SOCKS5");
         }

         var2.writeByte(3).writeByte(var4.length).writeBytes(var4);
      } else if (this.D.getAddress() instanceof Inet6Address var3) {
         var2.writeByte(4).writeBytes(var3.getAddress());
      } else {
         var2.writeByte(1).writeBytes(this.D.getAddress().getAddress());
      }

      var2.writeShort(this.D.getPort());
      var1.writeAndFlush(var2);
   }

   private void d(ChannelHandlerContext var1) {
      if (this.f != null) {
         this.f.cancel(false);
      }

      this.n = (boolean)1;
      ByteBuf var2 = this.C;
      this.C = Unpooled.EMPTY_BUFFER;
      if (!this.a.trySuccess()) {
         var2.release();
         var1.close();
      } else {
         var1.fireChannelActive();
         this.K.removeAndWriteAll();
         if (this.s) {
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
      if (!this.n && !this.X) {
         this.X = (boolean)1;
         if (this.f != null) {
            this.f.cancel(false);
         }

         bu.j var3 = var2 instanceof bu.j var4 ? var4 : new bu.j(this.Y() + "the proxy connection failed", var2);
         if (this.a != null) {
            this.a.tryFailure(var3);
         }

         if (!this.K.isEmpty()) {
            this.K.removeAndFailAll(var3);
         }

         var1.close();
      }
   }

   private int A() {
      int var1 = this.C.readerIndex();
      int var2 = this.C.writerIndex() - 3;

      for (int var3 = var1; var3 < var2; var3++) {
         if (this.C.getByte(var3) == 13 && this.C.getByte(var3 + 1) == 10 && this.C.getByte(var3 + 2) == 13 && this.C.getByte(var3 + 3) == 10) {
            return var3;
         }
      }

      return -1;
   }

   private String Y() {
      return "Proxy " + this.d.settings().label() + ": ";
   }

   private static byte[] H(String var0) throws bu.j {
      if (!var0.matches("[A-Za-z0-9.-]{1,253}")) {
         throw new bu.j("Proxy destinations must be ASCII host names");
      } else {
         return var0.getBytes(StandardCharsets.US_ASCII);
      }
   }

   static String s(InetSocketAddress var0) {
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

   public static final class j extends IOException {
      public j(String var1) {
         super(var1);
      }

      public j(String var1, Throwable var2) {
         super(var1, var2);
      }
   }
}
