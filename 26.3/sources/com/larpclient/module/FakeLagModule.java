package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.hd;
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
   private static FakeLagModule K;
   public static final int J = 256;
   private static final int q = 10;
   private static final double t = 1.0E-6;
   private final b y = this.addSetting(new b("Delay", "Delay native movement and its input state in milliseconds", 200.0, 0.0, 1000.0, 10.0));
   private final m I = this.addSetting(new m("Grim Compatibility", "Hold ping and chunk replies with movement. Off flags on Grim", false));
   private final hd a = new hd(256);
   private ClientPacketListener Z;
   private ClientLevel p;
   private LocalPlayer z;
   private int w;
   private boolean A;
   private long x;
   private static LocalPlayer O;
   private static ClientLevel D;
   private static ClientPacketListener g;
   private boolean e;
   private int T;
   private boolean B;
   private boolean F;
   private int j;
   private int v;
   private int i;
   private boolean k;

   public FakeLagModule() {
      super("Fake Lag", "Delay native movement updates", Category.PLAYER, -1);
      K = this;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public String getStatus() {
      return !this.isRuntimeActive()
         ? "Inactive"
         : (
            this.y.getValue() == 0.0
               ? "No delay"
               : (this.F ? "Recovery: stand still" : (this.T < 2 ? "Settling" : (this.e ? "Resyncing" : this.a.C() + " held")))
         );
   }

   public static boolean O() {
      return K != null && K.isEnabled() ? K != null && K.isRuntimeActive() : false;
   }

   public static boolean c(Packet<?> var0) {
      return var0 instanceof ServerboundMovePlayerPacket || var0 instanceof ServerboundPlayerInputPacket || var0 instanceof ServerboundPlayerCommandPacket;
   }

   private static boolean M(Packet<?> var0) {
      return var0 instanceof ServerboundPongPacket || var0 instanceof ServerboundChunkBatchReceivedPacket;
   }

   public static boolean X(Packet<?> var0) {
      return !c(var0) && !(var0 instanceof ServerboundClientTickEndPacket) && !(var0 instanceof ServerboundKeepAlivePacket) && !M(var0);
   }

   @Override
   public void onEnable() {
      this.a.r();
      this.v = 0;
      this.B = (boolean)0;
      this.k = (boolean)0;
      this.i = 0;
      this.F = (boolean)0;
      this.j = 0;
      this.Z = mc.getConnection();
      this.p = mc.level;
      this.z = mc.player;
      this.w = this.y.getValue().intValue();
      this.A = this.I.getValue();
      this.T = 2;
      this.e = (boolean)(O == this.z && D == this.p && g == this.Z ? 1 : 0);
      _q.z("FakeLag", "Enabled; outgoing delay={} ms, compatibility={}, capacity={}; native packet values and FIFO order retained", this.w, this.A, 256);
   }

   private boolean i() {
      return this.Z != null && mc != null && mc.getConnection() == this.Z && this.Z.getConnection().isConnected();
   }

   private boolean p() {
      return this.i() && mc.level == this.p && mc.player == this.z && this.z != null && this.z.isAlive();
   }

   @Override
   public void onDisable() {
      try {
         if (this.p()) {
            int var1 = this.a.C();
            int var2 = this.v;
            this.B("Stopped; preserve live trajectory");
            if (var1 > 0) {
               _q.z("FakeLag", "Stopped; released {} queued packets including {} movement reports in FIFO order; no new position resend", var1, var2);
            }

            if (this.i > 0) {
               _q.z("FakeLag", "Activation summary: {} early in-order releases at interactions, setting changes or capacity; no movement dropped", this.i);
            }
         } else {
            this.f("Stopped after session change");
         }
      } finally {
         if (!this.p() && O == this.z) {
            T();
         }

         this.a.r();
         this.v = 0;
         this.Z = null;
         this.p = null;
         this.z = null;
         this.e = (boolean)0;
         this.T = 0;
         this.F = (boolean)0;
         this.j = 0;
      }
   }

   @Override
   public void onTick() {
      if (!this.p()) {
         this.B("Player or world changed; retain live pong replies");
      } else {
         this.u();
         boolean var9 = false;
         byte var8 = this.F;
         int var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            Vec3 var1 = this.z.getDeltaMovement();
            if (!this.z.onGround() || var1.horizontalDistanceSqr() > 1.0E-6) {
               this.j = 0;
               return;
            }

            byte var3 = 1;
            int var2 = this.j;
            var10000 = this.j = var2 + var3;
            byte var5 = 10;
            int var4 = var10000;
            if ((Integer.compare(var4, var5) >>> 31 ^ 1) == 0) {
               return;
            }

            this.F = (boolean)0;
            this.T = 0;
            _q.z("FakeLag", "Correction recovery complete; resuming {} ms delay after grounded settling", this.w);
         }

         this.T = Math.min(2, this.T + 1);
         this.a.D(System.nanoTime(), false, this::i);
         var10000 = this.a.C();
         boolean var7 = false;
         int var6 = var10000;
         var10000 = Integer.signum(var6);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.v = 0;
         }
      }
   }

   private void u() {
      int var1 = this.y.getValue().intValue();
      boolean var2 = this.I.getValue();
      if (var1 != this.w || var2 != this.A) {
         this.z("Settings changed");
         this.w = var1;
         this.A = var2;
      }
   }

   private void B(String var1) {
      int var2 = this.a.C();
      this.a.D(System.nanoTime(), true, this::i);
      this.v = 0;
      if (var2 > 0) {
         _q.c("FakeLag", "Released or discarded {} held packets: {}", var2, var1);
      }
   }

   private void z(String var1) {
      if (this.a.C() != 0) {
         this.i++;
         if (!this.k) {
            this.k = (boolean)1;
            _q.z("FakeLag", "Released {} held packets in order before continuing ({}); movement is never dropped at these boundaries", this.a.C(), var1);
         }

         this.B(var1);
      }
   }

   private void f(String var1) {
      this.x++;
      if (this.v > 0 && this.p()) {
         O = this.z;
         D = this.p;
         g = this.Z;
         this.e = (boolean)1;
      }

      this.B(var1);
   }

   private static void T() {
      O = null;
      D = null;
      g = null;
   }

   public static void f() {
      T();
   }

   public static boolean W(LocalPlayer var0) {
      if (O == null) {
         return false;
      } else if (O == var0 && getMc() != null && getMc().player == var0 && getMc().level == D && getMc().getConnection() == g) {
         T();
         return true;
      } else {
         T();
         return false;
      }
   }

   public static boolean M(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (K != null && K.isEnabled()) {
         FakeLagModule var4 = K;
         Minecraft var5 = getMc();
         if (var4 == null || var5 == null || !var5.isSameThread() || !var4.isRuntimeActive() || var4.a.Z()) {
            return false;
         }

         if (!var4.p()) {
            var4.B("Player or world changed; retain live pong replies");
            return false;
         }

         if (var0 != var4.Z.getConnection()) {
            return false;
         }

         var4.u();
         long var6 = System.nanoTime();
         if (var4.F && var1 instanceof ServerboundMovePlayerPacket var8 && !var8.isOnGround()) {
            var4.j = 0;
         }

         if (!var4.F && var4.T >= 2 && !var4.e) {
            var4.a.D(var6, false, var4::i);
            if (var4.a.C() == 0) {
               var4.v = 0;
            }

            int var11 = !(var1 instanceof ServerboundClientTickEndPacket) && (!var4.A || !M(var1)) ? 0 : 1;
            if (!c(var1) && var11 == 0) {
               if (X(var1)) {
                  var4.z(var1.getClass().getSimpleName());
               }

               return false;
            } else {
               if (var4.w == 0) {
                  return false;
               }

               long var9 = var4.x;
               if (!var4.a.i(var6, var4.w * 1000000L, () -> {
                  if (var1 instanceof ServerboundMovePlayerPacket) {
                     var4.v = Math.max(0, var4.v - 1);
                  }

                  if (M(var1) || var4.p() && (!(var1 instanceof ServerboundMovePlayerPacket) || var9 == var4.x)) {
                     var0.send(var1, var2, var3);
                  }
               })) {
                  var4.z("Capacity reached");
                  return false;
               }

               if (var1 instanceof ServerboundMovePlayerPacket) {
                  var4.v++;
                  if (!var4.B) {
                     var4.B = (boolean)1;
                     _q.z("FakeLag", "Holding native movement at {} ms; input, sprint changes and airborne reports keep their original order", var4.w);
                  }
               }

               return true;
            }
         } else {
            if (var1 instanceof ServerboundClientTickEndPacket && O == null) {
               var4.e = (boolean)0;
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static void p(LocalPlayer var0) {
      FakeLagModule var1 = K;
      if (var1 != null && var1.isRuntimeActive() && var0 == var1.z) {
         int var2 = !var1.F ? 1 : 0;
         var1.F = (boolean)1;
         var1.j = 0;
         var1.T = 0;
         var1.f("Server position correction; discard rejected movement");
         if (var2 != 0) {
            _q.z(
               "FakeLag",
               "Server position correction at {} ms delay; enabled preference retained, native packets pass until {} stationary grounded ticks",
               var1.w,
               10
            );
         }
      }
   }
}
