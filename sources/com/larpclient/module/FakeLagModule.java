package com.larpclient.module;

import com.larpclient.me;
import com.larpclient.r4;
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
   private static FakeLagModule d;
   public static final int k = 256;
   private static final int w = 10;
   private static final double y = 1.0E-6;
   private final lc S = this.addSetting(new lc("Delay", "Delay native movement and its input state in milliseconds", 200.0, 0.0, 1000.0, 10.0));
   private final ln r = this.addSetting(new ln("Grim Compatibility", "Hold ping and chunk replies with movement. Off flags on Grim", false));
   private final me u = new me(256);
   private ClientPacketListener A;
   private ClientLevel f;
   private LocalPlayer X;
   private int Q;
   private boolean c;
   private long M;
   private static LocalPlayer v;
   private static ClientLevel j;
   private static ClientPacketListener a;
   private boolean l;
   private int I;
   private boolean Y;
   private boolean t;
   private int H;
   private int e;
   private int x;
   private boolean C;

   public FakeLagModule() {
      super("Fake Lag", "Delay native movement updates", Category.PLAYER, -1);
      d = this;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public String getStatus() {
      return !this.isRuntimeActive()
         ? "Inactive"
         : (
            this.S.getValue() == 0.0
               ? "No delay"
               : (this.t ? "Recovery: stand still" : (this.I < 2 ? "Settling" : (this.l ? "Resyncing" : this.u.p() + " held")))
         );
   }

   public static boolean z() {
      return d != null && d.isEnabled() ? d != null && d.isRuntimeActive() : false;
   }

   public static boolean O(Packet<?> var0) {
      return var0 instanceof ServerboundMovePlayerPacket || var0 instanceof ServerboundPlayerInputPacket || var0 instanceof ServerboundPlayerCommandPacket;
   }

   private static boolean K(Packet<?> var0) {
      return var0 instanceof ServerboundPongPacket || var0 instanceof ServerboundChunkBatchReceivedPacket;
   }

   public static boolean a(Packet<?> var0) {
      return !O(var0) && !(var0 instanceof ServerboundClientTickEndPacket) && !(var0 instanceof ServerboundKeepAlivePacket) && !K(var0);
   }

   @Override
   public void onEnable() {
      this.u.h();
      this.e = 0;
      this.Y = (boolean)0;
      this.C = (boolean)0;
      this.x = 0;
      this.t = (boolean)0;
      this.H = 0;
      this.A = mc.getConnection();
      this.f = mc.level;
      this.X = mc.player;
      this.Q = this.S.getValue().intValue();
      this.c = this.r.getValue();
      this.I = 2;
      this.l = (boolean)(v == this.X && j == this.f && a == this.A ? 1 : 0);
      r4.V("FakeLag", "Enabled; outgoing delay={} ms, compatibility={}, capacity={}; native packet values and FIFO order retained", this.Q, this.c, 256);
   }

   private boolean l() {
      return this.A != null && mc != null && mc.getConnection() == this.A && this.A.getConnection().isConnected();
   }

   private boolean X() {
      return this.l() && mc.level == this.f && mc.player == this.X && this.X != null && this.X.isAlive();
   }

   @Override
   public void onDisable() {
      try {
         if (this.X()) {
            int var1 = this.u.p();
            int var2 = this.e;
            this.L("Stopped; preserve live trajectory");
            if (var1 > 0) {
               r4.V("FakeLag", "Stopped; released {} queued packets including {} movement reports in FIFO order; no new position resend", var1, var2);
            }

            if (this.x > 0) {
               r4.V("FakeLag", "Activation summary: {} early in-order releases at interactions, setting changes or capacity; no movement dropped", this.x);
            }
         } else {
            this.d("Stopped after session change");
         }
      } finally {
         if (!this.X() && v == this.X) {
            j();
         }

         this.u.h();
         this.e = 0;
         this.A = null;
         this.f = null;
         this.X = null;
         this.l = (boolean)0;
         this.I = 0;
         this.t = (boolean)0;
         this.H = 0;
      }
   }

   @Override
   public void onTick() {
      if (!this.X()) {
         this.L("Player or world changed; retain live pong replies");
      } else {
         this.u();
         boolean var9 = false;
         byte var8 = this.t;
         int var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            Vec3 var1 = this.X.getDeltaMovement();
            if (!this.X.onGround() || var1.horizontalDistanceSqr() > 1.0E-6) {
               this.H = 0;
               return;
            }

            byte var5 = 1;
            int var4 = this.H;
            var10000 = this.H = var4 + var5;
            byte var7 = 10;
            int var6 = var10000;
            if ((Integer.compare(var6, var7) >>> 31 ^ 1) == 0) {
               return;
            }

            this.t = (boolean)0;
            this.I = 0;
            r4.V("FakeLag", "Correction recovery complete; resuming {} ms delay after grounded settling", this.Q);
         }

         byte var3 = 1;
         int var2 = this.I;
         this.I = Math.min(2, var2 + var3);
         this.u.z(System.nanoTime(), false, this::l);
         if (this.u.p() == 0) {
            this.e = 0;
         }
      }
   }

   private void u() {
      int var1 = this.S.getValue().intValue();
      boolean var2 = this.r.getValue();
      if (var1 != this.Q || var2 != this.c) {
         this.g("Settings changed");
         this.Q = var1;
         this.c = var2;
      }
   }

   private void L(String var1) {
      int var2 = this.u.p();
      this.u.z(System.nanoTime(), true, this::l);
      this.e = 0;
      if (var2 > 0) {
         r4.g("FakeLag", "Released or discarded {} held packets: {}", var2, var1);
      }
   }

   private void g(String var1) {
      if (this.u.p() != 0) {
         this.x++;
         if (!this.C) {
            this.C = (boolean)1;
            r4.V("FakeLag", "Released {} held packets in order before continuing ({}); movement is never dropped at these boundaries", this.u.p(), var1);
         }

         this.L(var1);
      }
   }

   private void d(String var1) {
      this.M++;
      if (this.e > 0 && this.X()) {
         v = this.X;
         j = this.f;
         a = this.A;
         this.l = (boolean)1;
      }

      this.L(var1);
   }

   private static void j() {
      v = null;
      j = null;
      a = null;
   }

   public static void b() {
      j();
   }

   public static boolean W(LocalPlayer var0) {
      if (v == null) {
         return false;
      } else if (v == var0 && getMc() != null && getMc().player == var0 && getMc().level == j && getMc().getConnection() == a) {
         j();
         return true;
      } else {
         j();
         return false;
      }
   }

   public static boolean Y(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (d != null && d.isEnabled()) {
         FakeLagModule var4 = d;
         Minecraft var5 = getMc();
         if (var4 == null || var5 == null || !var5.isSameThread() || !var4.isRuntimeActive() || var4.u.D()) {
            return false;
         }

         if (!var4.X()) {
            var4.L("Player or world changed; retain live pong replies");
            return false;
         }

         if (var0 != var4.A.getConnection()) {
            return false;
         }

         var4.u();
         long var6 = System.nanoTime();
         if (var4.t && var1 instanceof ServerboundMovePlayerPacket var8 && !var8.isOnGround()) {
            var4.H = 0;
         }

         if (!var4.t && var4.I >= 2 && !var4.l) {
            var4.u.z(var6, false, var4::l);
            if (var4.u.p() == 0) {
               var4.e = 0;
            }

            int var11 = !(var1 instanceof ServerboundClientTickEndPacket) && (!var4.c || !K(var1)) ? 0 : 1;
            if (!O(var1) && var11 == 0) {
               if (a(var1)) {
                  var4.g(var1.getClass().getSimpleName());
               }

               return false;
            } else {
               if (var4.Q == 0) {
                  return false;
               }

               long var9 = var4.M;
               if (!var4.u.m(var6, var4.Q * 1000000L, () -> {
                  if (var1 instanceof ServerboundMovePlayerPacket) {
                     var4.e = Math.max(0, var4.e - 1);
                  }

                  if (K(var1) || var4.X() && (!(var1 instanceof ServerboundMovePlayerPacket) || var9 == var4.M)) {
                     var0.send(var1, var2, var3);
                  }
               })) {
                  var4.g("Capacity reached");
                  return false;
               }

               if (var1 instanceof ServerboundMovePlayerPacket) {
                  var4.e++;
                  if (!var4.Y) {
                     var4.Y = (boolean)1;
                     r4.V("FakeLag", "Holding native movement at {} ms; input, sprint changes and airborne reports keep their original order", var4.Q);
                  }
               }

               return true;
            }
         } else {
            if (var1 instanceof ServerboundClientTickEndPacket && v == null) {
               var4.l = (boolean)0;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static void z(LocalPlayer var0) {
      FakeLagModule var1 = d;
      if (var1 != null && var1.isRuntimeActive() && var0 == var1.X) {
         int var2 = !var1.t ? 1 : 0;
         var1.t = (boolean)1;
         var1.H = 0;
         var1.I = 0;
         var1.d("Server position correction; discard rejected movement");
         if (var2 != 0) {
            r4.V(
               "FakeLag",
               "Server position correction at {} ms delay; enabled preference retained, native packets pass until {} stationary grounded ticks",
               var1.Q,
               10
            );
         }
      }
   }
}
