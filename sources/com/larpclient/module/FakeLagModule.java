package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.pu;
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
   private static FakeLagModule P;
   public static final int t = 256;
   private static final int F = 10;
   private static final double k = 1.0E-6;
   private final d9 e = this.addSetting(new d9("Delay", "Delay native movement and its input state in milliseconds", 200.0, 0.0, 1000.0, 10.0));
   private final dh X = this.addSetting(new dh("Grim Compatibility", "Hold ping and chunk replies with movement. Off flags on Grim", false));
   private final pu A = new pu(256);
   private ClientPacketListener H;
   private ClientLevel T;
   private LocalPlayer V;
   private int v;
   private boolean u;
   private long d;
   private static LocalPlayer x;
   private static ClientLevel o;
   private static ClientPacketListener W;
   private boolean Y;
   private int K;
   private boolean O;
   private boolean p;
   private int n;
   private int L;
   private int N;
   private boolean c;

   public FakeLagModule() {
      super("Fake Lag", "Delay native movement updates", Category.PLAYER, -1);
      P = this;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public String getStatus() {
      return !this.isRuntimeActive()
         ? "Inactive"
         : (this.e.P() == 0.0 ? "No delay" : (this.p ? "Recovery: stand still" : (this.K < 2 ? "Settling" : (this.Y ? "Resyncing" : this.A.Y() + " held"))));
   }

   public static boolean V() {
      return P != null && P.isEnabled() ? P != null && P.isRuntimeActive() : false;
   }

   public static boolean W(Packet<?> var0) {
      return var0 instanceof ServerboundMovePlayerPacket || var0 instanceof ServerboundPlayerInputPacket || var0 instanceof ServerboundPlayerCommandPacket;
   }

   private static boolean M(Packet<?> var0) {
      return var0 instanceof ServerboundPongPacket || var0 instanceof ServerboundChunkBatchReceivedPacket;
   }

   public static boolean V(Packet<?> var0) {
      return !W(var0) && !(var0 instanceof ServerboundClientTickEndPacket) && !(var0 instanceof ServerboundKeepAlivePacket) && !M(var0);
   }

   @Override
   public void onEnable() {
      this.A.H();
      this.L = 0;
      this.O = (boolean)0;
      this.c = (boolean)0;
      this.N = 0;
      this.p = (boolean)0;
      this.n = 0;
      this.H = mc.getConnection();
      this.T = mc.level;
      this.V = mc.player;
      this.v = this.e.P().intValue();
      this.u = this.X.P();
      this.K = 2;
      this.Y = (boolean)(x == this.V && o == this.T && W == this.H ? 1 : 0);
      mt.K("FakeLag", "Enabled; outgoing delay={} ms, compatibility={}, capacity={}; native packet values and FIFO order retained", this.v, this.u, 256);
   }

   private boolean U() {
      return this.H != null && mc != null && mc.getConnection() == this.H && this.H.getConnection().isConnected();
   }

   private boolean x() {
      return this.U() && mc.level == this.T && mc.player == this.V && this.V != null && this.V.isAlive();
   }

   @Override
   public void onDisable() {
      try {
         if (this.x()) {
            int var1 = this.A.Y();
            int var2 = this.L;
            this.X("Stopped; preserve live trajectory");
            if (var1 > 0) {
               mt.K("FakeLag", "Stopped; released {} queued packets including {} movement reports in FIFO order; no new position resend", var1, var2);
            }

            if (this.N > 0) {
               mt.K("FakeLag", "Activation summary: {} early in-order releases at interactions, setting changes or capacity; no movement dropped", this.N);
            }
         } else {
            this.g("Stopped after session change");
         }
      } finally {
         if (!this.x() && x == this.V) {
            j();
         }

         this.A.H();
         this.L = 0;
         this.H = null;
         this.T = null;
         this.V = null;
         this.Y = (boolean)0;
         this.K = 0;
         this.p = (boolean)0;
         this.n = 0;
      }
   }

   @Override
   public void onTick() {
      if (!this.x()) {
         this.X("Player or world changed; retain live pong replies");
      } else {
         this.u();
         boolean var5 = false;
         byte var4 = this.p;
         int var10000 = Integer.signum(var4);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            Vec3 var1 = this.V.getDeltaMovement();
            if (!this.V.onGround() || var1.horizontalDistanceSqr() > 1.0E-6) {
               this.n = 0;
               return;
            }

            byte var7 = 1;
            int var6 = this.n;
            var10000 = this.n = var6 + var7;
            byte var9 = 10;
            int var8 = var10000;
            if ((Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
               return;
            }

            this.p = (boolean)0;
            this.K = 0;
            mt.K("FakeLag", "Correction recovery complete; resuming {} ms delay after grounded settling", this.v);
         }

         byte var3 = 1;
         int var2 = this.K;
         this.K = Math.min(2, var2 + var3);
         this.A.z(System.nanoTime(), false, this::U);
         if (this.A.Y() == 0) {
            this.L = 0;
         }
      }
   }

   private void u() {
      int var1 = this.e.P().intValue();
      boolean var2 = this.X.P();
      if (var1 != this.v || var2 != this.u) {
         this.C("Settings changed");
         this.v = var1;
         this.u = var2;
      }
   }

   private void X(String var1) {
      int var2 = this.A.Y();
      this.A.z(System.nanoTime(), true, this::U);
      this.L = 0;
      if (var2 > 0) {
         mt.U("FakeLag", "Released or discarded {} held packets: {}", var2, var1);
      }
   }

   private void C(String var1) {
      if (this.A.Y() != 0) {
         this.N++;
         if (!this.c) {
            this.c = (boolean)1;
            mt.K("FakeLag", "Released {} held packets in order before continuing ({}); movement is never dropped at these boundaries", this.A.Y(), var1);
         }

         this.X(var1);
      }
   }

   private void g(String var1) {
      this.d++;
      if (this.L > 0 && this.x()) {
         x = this.V;
         o = this.T;
         W = this.H;
         this.Y = (boolean)1;
      }

      this.X(var1);
   }

   private static void j() {
      x = null;
      o = null;
      W = null;
   }

   public static void w() {
      j();
   }

   public static boolean X(LocalPlayer var0) {
      if (x == null) {
         return false;
      } else if (x == var0 && getMc() != null && getMc().player == var0 && getMc().level == o && getMc().getConnection() == W) {
         j();
         return true;
      } else {
         j();
         return false;
      }
   }

   public static boolean L(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (P != null && P.isEnabled()) {
         FakeLagModule var4 = P;
         Minecraft var5 = getMc();
         if (var4 == null || var5 == null || !var5.isSameThread() || !var4.isRuntimeActive() || var4.A.C()) {
            return false;
         }

         if (!var4.x()) {
            var4.X("Player or world changed; retain live pong replies");
            return false;
         }

         if (var0 != var4.H.getConnection()) {
            return false;
         }

         var4.u();
         long var6 = System.nanoTime();
         if (var4.p && var1 instanceof ServerboundMovePlayerPacket var8 && !var8.isOnGround()) {
            var4.n = 0;
         }

         if (!var4.p && var4.K >= 2 && !var4.Y) {
            var4.A.z(var6, false, var4::U);
            if (var4.A.Y() == 0) {
               var4.L = 0;
            }

            int var11 = var1 instanceof ServerboundClientTickEndPacket || var4.u && M(var1) ? 1 : 0;
            if (!W(var1) && var11 == 0) {
               if (V(var1)) {
                  var4.C(var1.getClass().getSimpleName());
               }

               return false;
            } else {
               if (var4.v == 0) {
                  return false;
               }

               long var9 = var4.d;
               if (!var4.A.D(var6, var4.v * 1000000L, () -> {
                  if (var1 instanceof ServerboundMovePlayerPacket) {
                     var4.L = Math.max(0, var4.L - 1);
                  }

                  if (M(var1) || var4.x() && (!(var1 instanceof ServerboundMovePlayerPacket) || var9 == var4.d)) {
                     var0.send(var1, var2, var3);
                  }
               })) {
                  var4.C("Capacity reached");
                  return false;
               }

               if (var1 instanceof ServerboundMovePlayerPacket) {
                  var4.L++;
                  if (!var4.O) {
                     var4.O = (boolean)1;
                     mt.K("FakeLag", "Holding native movement at {} ms; input, sprint changes and airborne reports keep their original order", var4.v);
                  }
               }

               return true;
            }
         } else {
            if (var1 instanceof ServerboundClientTickEndPacket && x == null) {
               var4.Y = (boolean)0;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static void q(LocalPlayer var0) {
      FakeLagModule var1 = P;
      if (var1 != null && var1.isRuntimeActive() && var0 == var1.V) {
         int var2 = !var1.p ? 1 : 0;
         var1.p = (boolean)1;
         var1.n = 0;
         var1.K = 0;
         var1.g("Server position correction; discard rejected movement");
         if (var2 != 0) {
            mt.K(
               "FakeLag",
               "Server position correction at {} ms delay; enabled preference retained, native packets pass until {} stationary grounded ticks",
               var1.v,
               10
            );
         }
      }
   }
}
