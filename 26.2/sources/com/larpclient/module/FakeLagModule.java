package com.larpclient.module;

import com.larpclient.w1;
import io.netty.channel.ChannelFutureListener;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ServerboundChunkBatchReceivedPacket;
import net.minecraft.network.protocol.game.ServerboundClientTickEndPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;
import net.minecraft.world.phys.Vec3;

public final class FakeLagModule extends Module {
   private static FakeLagModule I;
   public static final int V = 256;
   private static final int F = 10;
   private static final double n = 1.0E-6;
   private final e U = this.addSetting(new e("Delay", "Delay native movement and its input state in milliseconds", 200.0, 0.0, 1000.0, 10.0));
   private final s x = this.addSetting(new s("Grim Compatibility", "Hold ping and chunk replies with movement. Off flags on Grim", false));
   private final w1 E = new w1(256);
   private ClientPacketListener t;
   private ClientLevel X;
   private LocalPlayer a;
   private int L;
   private boolean e;
   private long Q;
   private static LocalPlayer i;
   private static ClientLevel T;
   private static ClientPacketListener W;
   private boolean r;
   private int h;
   private boolean j;
   private boolean J;
   private int m;
   private int G;
   private int Y;
   private boolean K;

   public FakeLagModule() {
      super("Fake Lag", "Delay native movement updates", Category.PLAYER, -1);
      I = this;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      return !this.isRuntimeActive()
         ? "Inactive"
         : (
            this.U.getValue() == 0.0
               ? "No delay"
               : (this.J ? "Recovery: stand still" : (this.h < 2 ? "Settling" : (this.r ? "Resyncing" : this.E.r() + " held")))
         );
   }

   public static boolean M() {
      return I != null && I.isEnabled() ? I != null && I.isRuntimeActive() : false;
   }

   public static boolean b(Packet<?> var0) {
      return var0 instanceof ServerboundMovePlayerPacket || var0 instanceof ServerboundPlayerInputPacket || var0 instanceof ServerboundPlayerCommandPacket;
   }

   private static boolean z(Packet<?> var0) {
      return var0 instanceof ServerboundPongPacket || var0 instanceof ServerboundChunkBatchReceivedPacket;
   }

   public static boolean a(Packet<?> var0) {
      return !b(var0) && !(var0 instanceof ServerboundClientTickEndPacket) && !(var0 instanceof ServerboundKeepAlivePacket) && !z(var0);
   }

   @Override
   public void onEnable() {
      this.E.v();
      this.G = 0;
      this.j = (boolean)0;
      this.K = (boolean)0;
      this.Y = 0;
      this.J = (boolean)0;
      this.m = 0;
      this.t = mc.getConnection();
      this.X = mc.level;
      this.a = mc.player;
      this.L = this.U.getValue().intValue();
      this.e = this.x.getValue();
      this.h = 2;
      this.r = (boolean)(i == this.a && T == this.X && W == this.t ? 1 : 0);
      com.larpclient.h.A(
         "FakeLag", "Enabled; outgoing delay={} ms, compatibility={}, capacity={}; native packet values and FIFO order retained", this.L, this.e, 256
      );
   }

   private boolean E() {
      return this.t != null && mc != null && mc.getConnection() == this.t && this.t.getConnection().isConnected();
   }

   private boolean g() {
      return this.E() && mc.level == this.X && mc.player == this.a && this.a != null && this.a.isAlive();
   }

   @Override
   public void onDisable() {
      try {
         if (this.g()) {
            int var1 = this.E.r();
            int var2 = this.G;
            this.n("Stopped; preserve live trajectory");
            if (var1 > 0) {
               com.larpclient.h.A(
                  "FakeLag", "Stopped; released {} queued packets including {} movement reports in FIFO order; no new position resend", var1, var2
               );
            }

            if (this.Y > 0) {
               com.larpclient.h.A(
                  "FakeLag", "Activation summary: {} early in-order releases at interactions, setting changes or capacity; no movement dropped", this.Y
               );
            }
         } else {
            this.y("Stopped after session change");
         }
      } finally {
         if (!this.g() && i == this.a) {
            V();
         }

         this.E.v();
         this.G = 0;
         this.t = null;
         this.X = null;
         this.a = null;
         this.r = (boolean)0;
         this.h = 0;
         this.J = (boolean)0;
         this.m = 0;
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.g();
      boolean var3 = false;
      byte var2 = (byte)var10000;
      var10000 = Integer.signum(var2);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.n("Player or world changed; retain live pong replies");
      } else {
         this.K();
         boolean var7 = false;
         byte var6 = this.J;
         var10000 = Integer.signum(var6);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            Vec3 var1 = this.a.getDeltaMovement();
            if (!this.a.onGround() || var1.horizontalDistanceSqr() > 1.0E-6) {
               this.m = 0;
               return;
            }

            var10000 = ++this.m;
            byte var5 = 10;
            int var4 = var10000;
            if ((Integer.compare(var4, var5) >>> 31 ^ 1) == 0) {
               return;
            }

            this.J = (boolean)0;
            this.h = 0;
            com.larpclient.h.A("FakeLag", "Correction recovery complete; resuming {} ms delay after grounded settling", this.L);
         }

         byte var9 = 1;
         int var8 = this.h;
         this.h = Math.min(2, var8 + var9);
         this.E.o(System.nanoTime(), false, this::E);
         if (this.E.r() == 0) {
            this.G = 0;
         }
      }
   }

   private void K() {
      int var1 = this.U.getValue().intValue();
      boolean var2 = this.x.getValue();
      if (var1 != this.L || var2 != this.e) {
         this.M("Settings changed");
         this.L = var1;
         this.e = var2;
      }
   }

   private void n(String var1) {
      int var2 = this.E.r();
      this.E.o(System.nanoTime(), true, this::E);
      this.G = 0;
      if (var2 > 0) {
         com.larpclient.h.U("FakeLag", "Released or discarded {} held packets: {}", var2, var1);
      }
   }

   private void M(String var1) {
      if (this.E.r() != 0) {
         this.Y++;
         if (!this.K) {
            this.K = (boolean)1;
            com.larpclient.h.A(
               "FakeLag", "Released {} held packets in order before continuing ({}); movement is never dropped at these boundaries", this.E.r(), var1
            );
         }

         this.n(var1);
      }
   }

   private void y(String var1) {
      this.Q++;
      if (this.G > 0 && this.g()) {
         i = this.a;
         T = this.X;
         W = this.t;
         this.r = (boolean)1;
      }

      this.n(var1);
   }

   private static void V() {
      i = null;
      T = null;
      W = null;
   }

   public static void h() {
      V();
   }

   public static boolean B(LocalPlayer var0) {
      if (i == null) {
         return false;
      } else if (i == var0 && getMc() != null && getMc().player == var0 && getMc().level == T && getMc().getConnection() == W) {
         V();
         return true;
      } else {
         V();
         return false;
      }
   }

   public static boolean C(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (I != null && I.isEnabled()) {
         FakeLagModule var4 = I;
         Minecraft var5 = getMc();
         if (var4 == null || var5 == null || !var5.isSameThread() || !var4.isRuntimeActive() || var4.E.c()) {
            return false;
         }

         if (!var4.g()) {
            var4.n("Player or world changed; retain live pong replies");
            return false;
         }

         if (var0 != var4.t.getConnection()) {
            return false;
         }

         var4.K();
         long var6 = System.nanoTime();
         if (var4.J && var1 instanceof ServerboundMovePlayerPacket var8 && !var8.isOnGround()) {
            var4.m = 0;
         }

         if (!var4.J && var4.h >= 2 && !var4.r) {
            var4.E.o(var6, false, var4::E);
            if (var4.E.r() == 0) {
               var4.G = 0;
            }

            int var11 = var1 instanceof ServerboundClientTickEndPacket || var4.e && z(var1) ? 1 : 0;
            if (!b(var1) && var11 == 0) {
               if (a(var1)) {
                  var4.M(var1.getClass().getSimpleName());
               }

               return false;
            } else {
               if (var4.L == 0) {
                  return false;
               }

               long var9 = var4.Q;
               if (!var4.E.A(var6, var4.L * 1000000L, () -> {
                  if (var1 instanceof ServerboundMovePlayerPacket) {
                     var4.G = Math.max(0, var4.G - 1);
                  }

                  if (z(var1) || var4.g() && (!(var1 instanceof ServerboundMovePlayerPacket) || var9 == var4.Q)) {
                     var0.send(var1, var2, var3);
                  }
               })) {
                  var4.M("Capacity reached");
                  return false;
               }

               if (var1 instanceof ServerboundMovePlayerPacket) {
                  var4.G++;
                  if (!var4.j) {
                     var4.j = (boolean)1;
                     com.larpclient.h.A(
                        "FakeLag", "Holding native movement at {} ms; input, sprint changes and airborne reports keep their original order", var4.L
                     );
                  }
               }

               return true;
            }
         } else {
            if (var1 instanceof ServerboundClientTickEndPacket && i == null) {
               var4.r = (boolean)0;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static void r(LocalPlayer var0) {
      FakeLagModule var1 = I;
      if (var1 != null && var1.isRuntimeActive() && var0 == var1.a) {
         int var2 = !var1.J ? 1 : 0;
         var1.J = (boolean)1;
         var1.m = 0;
         var1.h = 0;
         var1.y("Server position correction; discard rejected movement");
         if (var2 != 0) {
            com.larpclient.h.A(
               "FakeLag",
               "Server position correction at {} ms delay; enabled preference retained, native packets pass until {} stationary grounded ticks",
               var1.L,
               10
            );
         }
      }
   }
}
