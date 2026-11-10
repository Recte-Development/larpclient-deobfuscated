package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.c3;
import com.larpclient.cf;
import com.larpclient.gl;
import com.larpclient.j4;
import com.larpclient.kj;
import com.larpclient.kz;
import com.larpclient.st;
import com.larpclient.tk;
import com.larpclient.u_;
import com.larpclient.uh;
import com.larpclient.zj;
import io.netty.channel.ChannelFutureListener;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
import net.minecraft.network.protocol.common.ClientboundPingPacket;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundBundlePacket;
import net.minecraft.network.protocol.game.ClientboundChunkBatchFinishedPacket;
import net.minecraft.network.protocol.game.ClientboundChunkBatchStartPacket;
import net.minecraft.network.protocol.game.ClientboundDebugSamplePacket;
import net.minecraft.network.protocol.game.ClientboundDisguisedChatPacket;
import net.minecraft.network.protocol.game.ClientboundEntityPositionSyncPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundLowDiskSpaceWarningPacket;
import net.minecraft.network.protocol.game.ClientboundMoveEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerChatPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatEndPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatEnterPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundRotateHeadPacket;
import net.minecraft.network.protocol.game.ClientboundSetActionBarTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.network.protocol.game.ClientboundSystemChatPacket;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.protocol.game.VecDeltaCodec;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class BacktrackModule extends Module implements tk {
   private static BacktrackModule W;
   private static final int T = 65536;
   private static final long n0 = System.nanoTime();
   private final l G = this.addSetting(new l("Version", "Packet delay algorithm", "New", "Old", "New"));
   private final e nc = this.addSetting(new e("Range Min", "Lower bound of the box distance to the target", 1.0, 0.0, 10.0, 0.1));
   private final e na = this.addSetting(new e("Range Max", "Upper bound of the box distance to the target", 3.0, 0.0, 10.0, 0.1));
   private final e n_ = this.addSetting(new e("Delay Min", "Shortest hold window in milliseconds", 280.0, 0.0, 2000.0, 1.0));
   private final e h = this.addSetting(new e("Delay Max", "Longest hold window in milliseconds", 480.0, 0.0, 2000.0, 1.0));
   private final e B = this.addSetting(new e("Next Backtrack Delay Min", "Shortest wait before the next window in milliseconds", 0.0, 0.0, 2000.0, 1.0));
   private final e V = this.addSetting(new e("Next Backtrack Delay Max", "Longest wait before the next window in milliseconds", 10.0, 0.0, 2000.0, 1.0));
   private final e p = this.addSetting(
      new e("Tracking Buffer", "Keep windows available after the target leaves range, in milliseconds", 500.0, 0.0, 2000.0, 1.0)
   );
   private final e Y = this.addSetting(new e("Chance", "Chance that an attack allows windows, in percent", 50.0, 0.0, 100.0, 1.0));
   private final s n7 = this.addSetting(new s("Pause On Hurt Time", "Skip windows while the target is hurt", false));
   private final e U = this.addSetting(new e("Hurt Time", "Target hurt time that pauses windows", 3.0, 0.0, 10.0, 1.0));
   private final l x = this.addSetting(new l("Target Mode", "Use the attacked entity or the nearest one in range", "Attack", "Attack", "Range"));
   private final e I = this.addSetting(new e("Time Since Last Attack", "How long an attack keeps windows available, in milliseconds", 1000.0, 0.0, 5000.0, 1.0));
   private final s nJ = this.addSetting(new s("Release Out Of Reach", "Release held packets as soon as the target's server position is too far to hit", true));
   private final e nU = this.addSetting(
      new e("Release Distance", "Server position distance from your eyes that releases held packets. Vanilla ignores hits past 6", 6.0, 1.0, 10.0, 0.1)
   );
   private final l u = this.addSetting(new l("ESP", "Draw the target's server position", "Box", "Box", "Wireframe", "Fill", "None"));
   private final i L = this.addSetting(new i("Color", "Server position box color", -1));
   private final i D = this.addSetting(new i("Gradient", "Second ESP color, blended toward the top", -6862849));
   private final s nL = this.addSetting(new s("Ghost Model", "Draw a hologram of the target at its server position", true));
   private final s nw = this.addSetting(new s("Tether", "Link the displayed target to its server position", true));
   private final s ne = this.addSetting(new s("Bloom", "Glow pass for the ESP", true));
   private final s f = this.addSetting(new s("Through Walls", "Keep a faint outline visible behind blocks", true));
   private final e o = this.addSetting(new e("New Range Min", "Lower bound of the distance to the target player", 0.0, 0.0, 10.0, 0.01));
   private final e Q = this.addSetting(new e("New Range Max", "Upper bound of the distance to the target player", 10.0, 0.0, 10.0, 0.01));
   private final e M = this.addSetting(new e("New Delay", "How long each held update waits, in milliseconds", 15000.0, 0.0, 15000.0, 1.0));
   private final s S = this.addSetting(new s("Only Attack", "Release held updates when the target moves closer", true));
   private Entity t;
   private String H = "Old";
   private final com.larpclient.s r = new com.larpclient.s(65536);
   private final ArrayDeque<Runnable> C = new ArrayDeque<>();
   private final VecDeltaCodec O = new VecDeltaCodec();
   private boolean P;
   private boolean n8;
   private long w;
   private long K;
   private long m;
   private int n1;
   private final ArrayDeque<Runnable> E = new ArrayDeque<>();
   private final ArrayDeque<Long> z = new ArrayDeque<>();
   private VecDeltaCodec n3;
   private boolean e;
   private ClientPacketListener A;
   private ClientLevel F;
   private volatile int b;
   private static final double c = 0.55;
   private static final int[][] X = new int[][]{{0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
   private final u_.p nQ = new u_.p(25);
   private boolean l;
   private boolean g;
   private Object nP;
   private int N = -1;
   private int y;
   private int n;
   private Vec3 Z;
   private double nl;
   private double j;
   private double i;
   private double v;
   private double k;
   private double d = -1.0;
   private EntityRenderState R;
   private Vec3 s;
   private double q;
   private double J;
   private double a = -10.0;

   public BacktrackModule() {
      super("Backtrack", "Backtracks player position to a hittable state", Category.COMBAT, -1);
      W = this;
      this.nc.C(this.na).h("Range");
      this.n_.C(this.h).h("Delay");
      this.B.C(this.V).h("Next Backtrack Delay");
      this.o.C(this.Q).h("New Range");
      this.p.V("ms");
      this.Y.V("%");
      this.U.V("");
      this.I.V("ms").h("Last Attack Time To Work");
      this.M.V("ms");

      for (q var4 : new q[]{this.nc, this.na, this.n_, this.h, this.B, this.V, this.p, this.Y, this.n7, this.x, this.I}) {
         var4.P(() -> this.G.O("Old"));
      }

      this.U.P(() -> this.G.O("Old") && this.n7.getValue());
      this.nU.P(this.nJ::getValue);

      for (q var11 : new q[]{this.L, this.D, this.nL, this.nw, this.ne, this.f}) {
         var11.P(() -> !this.u.O("None"));
      }

      for (q var12 : new q[]{this.o, this.Q, this.M, this.S}) {
         var12.P(() -> this.G.O("New"));
      }

      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.T());
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      }

      if (this.t == null) {
         return "Waiting for target";
      }

      int var1 = this.r.w() + this.E.size();
      return "Tracking " + this.t.getName().getString() + (var1 > 0 ? " · " + var1 + " held" : "");
   }

   @Override
   public void onEnable() {
      this.H = this.G.getValue();
      this.n1 = ThreadLocalRandom.current().nextInt(0, 101);
      this.W(false, false, false, null);
      this.f(false, false, null);
      this.clear();
      this.S();
   }

   @Override
   public void onDisable() {
      this.b = 0;
      this.W(true, false, true, "Stopped");
      this.f(true, false, "Stopped");
      this.clear();
   }

   private void S() {
      this.b = !this.isRuntimeActive() ? 0 : (this.G.O("Old") ? (this.t == null && this.r.w() <= 0 ? 0 : 1) : (!this.E.isEmpty() ? 2 : 0));
   }

   public static void k(Packet<?> var0, PacketListener var1) {
      if (W != null && W.isEnabled()) {
         BacktrackModule var2 = W;
         if (var2 != null && var2.b != 0 && var1 instanceof ClientPacketListener var3) {
            Minecraft var4 = getMc();
            if (var4 != null && !var4.isSameThread()) {
               if (var2.b == 1 ? h(var0) : var0 instanceof ClientboundDisconnectPacket) {
                  PacketUtils.ensureRunningOnSameThread(var0, var3, var4.packetProcessor());
               }
            }
         }
      }
   }

   static boolean h(Packet<?> var0) {
      return var0 instanceof ClientboundKeepAlivePacket
         || var0 instanceof ClientboundDisconnectPacket
         || var0 instanceof ClientboundCustomPayloadPacket
         || var0 instanceof ClientboundChunkBatchStartPacket
         || var0 instanceof ClientboundChunkBatchFinishedPacket
         || var0 instanceof ClientboundDebugSamplePacket
         || var0 instanceof ClientboundLowDiskSpaceWarningPacket
         || var0 instanceof ClientboundPlayerCombatEnterPacket
         || var0 instanceof ClientboundPlayerCombatEndPacket
         || var0 instanceof ClientboundPongResponsePacket;
   }

   public static boolean E() {
      BacktrackModule var0 = W;
      return var0 != null && var0.r.w() > 0;
   }

   private static long L() {
      return (System.nanoTime() - n0) / 1000000L;
   }

   private void f() {
      String var1 = this.G.getValue();
      if (!var1.equals(this.H)) {
         this.W(true, false, false, "Version changed");
         this.f(true, false, "Version changed");
         this.H = var1;
      }
   }

   private boolean n() {
      Minecraft var1 = getMc();
      return this.A != null && var1.getConnection() == this.A && var1.level == this.F && this.A.getConnection().isConnected();
   }

   private void H() {
      if (this.r.w() <= 0 && this.C.isEmpty() && this.E.isEmpty()) {
         this.A = mc.getConnection();
         this.F = mc.level;
      }
   }

   public static boolean X() {
      BacktrackModule var0 = W;
      return var0 != null && (var0.r.B() || var0.e || var0.n8);
   }

   public static boolean d(Packet<?> var0, PacketListener var1, Runnable var2) {
      if (W != null && W.isEnabled()) {
         BacktrackModule var3 = W;
         Minecraft var4 = getMc();
         if (var3 != null
            && !X()
            && var4.isSameThread()
            && !(var0 instanceof ClientboundBundlePacket)
            && var1 instanceof ClientPacketListener
            && var1 == var4.getConnection()
            && var3.isRuntimeActive()) {
            try {
               var3.f();
               return var3.G.O("New") ? var3.p(var0, var2) : var3.X(var0, var2);
            } finally {
               var3.S();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean g(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (W != null && W.isEnabled()) {
         BacktrackModule var4 = W;
         Minecraft var5 = getMc();
         if (var4 != null
            && !X()
            && var5 != null
            && var5.isSameThread()
            && var4.isRuntimeActive()
            && var5.player != null
            && var5.level != null
            && var5.getConnection() != null
            && !var5.isLocalServer()
            && var0 == var5.getConnection().getConnection()) {
            var4.f();
            if (!var4.G.O("New") && var1 instanceof ServerboundPongPacket && var4.r.w() != 0) {
               var4.C.addLast(() -> var0.send(var1, var2, var3));
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean z() {
      BacktrackModule var0 = W;
      return var0 != null && var0.n8;
   }

   public static void V(Entity var0) {
      if (W != null && W.isEnabled()) {
         BacktrackModule var1 = W;
         if (var1 != null && var0 != null && var1.isRuntimeActive() && var1.isInGame()) {
            try {
               var1.f();
               if (!var1.G.O("New")) {
                  var1.K = L();
                  var1.n1 = ThreadLocalRandom.current().nextInt(0, 101);
                  if (var1.x.O("Attack")) {
                     var1.l(var0);
                  }

                  return;
               }

               var1.x(var0);
            } finally {
               var1.S();
            }
         }
      }
   }

   @Override
   public void onTick() {
      try {
         this.K();
      } finally {
         this.S();
      }
   }

   private void K() {
      this.f();
      if (this.G.O("New")) {
         this.C();
      } else if (mc.player != null && mc.level != null) {
         if (this.x.O("Range")) {
            Entity var1 = this.p();
            if (var1 == null) {
               this.W(true, false, true, "No target in range");
            } else {
               this.l(var1);
            }
         }

         int var2 = this.r.w() > 0 ? 1 : 0;
         if (var2 == 0 || !this.I(this.d())) {
            if (this.m()) {
               if (var2 != 0 && this.r.s(L())) {
                  this.d("Window ended");
                  this.W(false, false, true, null);
               }
            } else if (var2 != 0) {
               this.d("Target no longer eligible");
               this.W(false, false, true, null);
            }
         }
      } else {
         this.W(false, true, false, "World unavailable");
      }
   }

   private boolean X(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.level == null || mc.getConnection() == null || mc.isLocalServer()) {
         return false;
      }

      if (H(var1)) {
         return false;
      }

      if (this.n(var1)) {
         this.W(true, false, true, "Server boundary packet");
         return false;
      }

      boolean var3 = this.m();
      int var4 = this.r.w() > 0 ? 1 : 0;
      if (var4 == 0 && !var3) {
         return false;
      }

      Entity var5 = this.t;
      if (var4 == 0 && !(var1 instanceof ClientboundPingPacket)) {
         this.d(var1, var5);
         return false;
      }

      if (var5 != null) {
         Vec3 var6 = this.d(var1, var5);
         if (var6 != null && this.I(var5.getType().getDimensions().makeBoundingBox(var6))) {
            return false;
         }

         if (var6 != null && this.A(var5, var6)) {
            this.d("Target moved closer");
            return false;
         }
      }

      if (var4 == 0) {
         if (var5 != null && this.p(this.d())) {
            return false;
         }

         this.H();
         long var8 = n(this.n_, this.h);
         if (this.r.s()) {
            this.r.q();
         }

         this.r.o(L(), var8);
         com.larpclient.h.U("Backtrack", "Old window opened for {} ms", var8);
      }

      if (this.r.n(var2)) {
         return true;
      }

      this.d("Packet capacity reached");
      return false;
   }

   private static boolean H(Packet<?> var0) {
      return var0 instanceof ClientboundPlayerChatPacket
         || var0 instanceof ClientboundSystemChatPacket
         || var0 instanceof ClientboundDisguisedChatPacket
         || var0 instanceof ClientboundSetActionBarTextPacket
         || var0 instanceof ClientboundSoundPacket var1 && var1.getSound().value() == SoundEvents.PLAYER_HURT;
   }

   private boolean n(Packet<?> var1) {
      return var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F
         || var1 instanceof ClientboundRemoveEntitiesPacket var2 && this.t != null && X(var2, this.t.getId());
   }

   private Vec3 d(Packet<?> var1, Entity var2) {
      return var2 != null && mc.level != null ? O(this.O, var1, mc.level, var2) : null;
   }

   static Vec3 O(VecDeltaCodec var0, Packet<?> var1, ClientLevel var2, Entity var3) {
      Vec3 var4 = null;
      if (var1 instanceof ClientboundMoveEntityPacket var5 && var5.hasPosition() && var5.getEntity(var2) == var3) {
         var4 = V(var0, var5);
      } else if (var1 instanceof ClientboundTeleportEntityPacket var6 && var6.id() == var3.getId()) {
         var4 = var6.relatives().isEmpty()
            ? var6.change().position()
            : PositionMoveRotation.calculateAbsolute(
                  new PositionMoveRotation(var0.getBase(), var3.getDeltaMovement(), var3.getYRot(), var3.getXRot()), var6.change(), var6.relatives()
               )
               .position();
      } else if (var1 instanceof ClientboundEntityPositionSyncPacket var7 && var7.id() == var3.getId()) {
         var4 = T(var7);
      }

      if (var4 != null) {
         var0.setBase(var4);
      }

      return var4;
   }

   private boolean A(Entity var1, Vec3 var2) {
      AABB var3 = var1.getType().getDimensions().makeBoundingBox(var2);
      AABB var4 = mc.player.getBoundingBox();
      return r(var4, var3) < r(var4, var1.getBoundingBox());
   }

   private boolean m() {
      return this.t != null && this.t.isAlive() && this.D(this.t);
   }

   private boolean D(Entity var1) {
      if (mc.player != null && var1 != null) {
         boolean var2 = this.i(var1);
         long var3 = L();
         if (var2) {
            this.w = var3;
         }

         return (var2 || this.w != 0L && var3 - this.w <= this.p.getValue().longValue())
            && this.G(var1)
            && mc.player.tickCount > 10
            && this.n1 < this.Y.getValue()
            && var3 >= this.m
            && (!this.n7.getValue() || !this.P)
            && this.K != 0L
            && var3 - this.K <= this.I.getValue().longValue();
      } else {
         return false;
      }
   }

   private boolean G(Entity var1) {
      if (!(var1 instanceof LivingEntity var2 && mc.player != null && var1 != mc.player)) {
         return false;
      } else {
         if (!var2.isAlive() || var2.isRemoved() || var2.isSpectator() || !var2.isPickable()) {
            return false;
         }

         if (!(var1 instanceof Player)) {
            return true;
         }

         MCFriendModule var3 = LarpClient.R().z().p(MCFriendModule.class);
         return var3 == null || !var3.S(var1.getName().getString());
      }
   }

   private boolean i(Entity var1) {
      double var2 = Math.sqrt(r(mc.player.getBoundingBox(), var1.getBoundingBox()));
      return var2 >= Math.min(this.nc.getValue(), this.na.getValue()) && var2 <= Math.max(this.nc.getValue(), this.na.getValue());
   }

   private Entity p() {
      double var1 = Math.min(this.nc.getValue(), this.na.getValue());
      double var3 = Math.max(this.nc.getValue(), this.na.getValue());
      AABB var5 = mc.player.getBoundingBox();
      Entity var6 = null;
      double var7 = Double.MAX_VALUE;

      for (Entity var10 : mc.level.getEntities(mc.player, var5.inflate(var3 + 1.0), this::G)) {
         double var11 = Math.sqrt(r(var5, var10.getBoundingBox()));
         if (var11 >= var1 && var11 <= var3 && var11 < var7) {
            var6 = var10;
            var7 = var11;
         }
      }

      return var6;
   }

   private void l(Entity var1) {
      this.P = (boolean)(var1 instanceof LivingEntity var2 && var2.hurtTime >= this.U.getValue() ? 1 : 0);
      if (this.D(var1)) {
         if (var1 != this.t) {
            this.W(true, false, false, "Target changed");
            this.O.setBase(var1.getPositionCodec().getBase());
         }

         this.t = var1;
      }
   }

   private void W(boolean var1, boolean var2, boolean var3, String var4) {
      if (var1 && !var2) {
         this.d(var4);
      } else if (var2) {
         this.T(var4);
      }

      if (this.t != null && var3) {
         this.m = L() + n(this.B, this.V);
      }

      this.t = null;
      this.P = (boolean)0;
      this.O.setBase(Vec3.ZERO);
   }

   private void d(String var1) {
      int var2 = this.r.w() + this.C.size();
      if (var2 == 0) {
         this.r.q();
      } else if (mc.getConnection() != null && this.n()) {
         this.n8 = (boolean)1;

         try {
            while (!this.C.isEmpty() && this.n()) {
               this.C.removeFirst().run();
            }
         } finally {
            this.n8 = (boolean)0;
            this.C.clear();
         }

         this.r.b(this::n);
         if (var1 != null) {
            com.larpclient.h.U("Backtrack", "Released {} held packets: {}", var2, var1);
         }
      } else {
         this.T(var1 + "; session changed");
      }
   }

   private void T(String var1) {
      int var2 = this.r.w() + this.C.size();
      this.r.q();
      this.C.clear();
      if (var2 > 0 && var1 != null) {
         com.larpclient.h.U("Backtrack", "Discarded {} held packets: {}", var2, var1);
      }
   }

   private void C() {
      if (mc.player != null && mc.level != null) {
         if (!this.R()) {
            this.f(true, false, "Target left New range");
         } else if (!this.I(this.d())) {
            this.j(false, null);
         }
      } else {
         this.f(true, true, "World unavailable");
      }
   }

   private boolean p(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.level == null) {
         this.f(true, true, "World unavailable");
         return false;
      }

      if (var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F) {
         this.f(true, false, "Server boundary packet");
         return false;
      }

      if (this.E.isEmpty() && !this.R()) {
         return false;
      }

      this.b(var1);
      if (this.t == null || o(var1) || !F(var1)) {
         return false;
      }

      if (this.E.size() >= 65536) {
         this.j(true, "Packet capacity reached");
         return false;
      }

      if (this.E.isEmpty()) {
         this.H();
         com.larpclient.h.U("Backtrack", "New queue opened with {} ms delay", this.M.getValue().longValue());
      }

      this.E.addLast(var2);
      this.z.addLast(L());
      return true;
   }

   private static boolean o(Packet<?> var0) {
      return var0 instanceof ClientboundSoundPacket || var0 instanceof ClientboundSetHealthPacket || var0 instanceof ClientboundSetEntityDataPacket;
   }

   private static boolean F(Packet<?> var0) {
      return var0 instanceof ClientboundPingPacket
         || var0 instanceof ClientboundRotateHeadPacket
         || var0 instanceof ClientboundTeleportEntityPacket
         || var0 instanceof ClientboundEntityPositionSyncPacket
         || var0 instanceof ClientboundSetEntityMotionPacket
         || var0 instanceof ClientboundMoveEntityPacket
         || var0 instanceof ClientboundLevelParticlesPacket
         || var0 instanceof ClientboundExplodePacket;
   }

   private void b(Packet<?> var1) {
      if (mc.player != null && mc.level != null && this.t != null) {
         Vec3 var2 = null;
         if (var1 instanceof ClientboundMoveEntityPacket var3 && var3.hasPosition() && var3.getEntity(mc.level) == this.t && this.n3 != null) {
            var2 = V(this.n3, var3);
         } else if (var1 instanceof ClientboundTeleportEntityPacket var4 && var4.id() == this.t.getId()) {
            var2 = var4.change().position();
         } else if (var1 instanceof ClientboundEntityPositionSyncPacket var5 && var5.id() == this.t.getId()) {
            var2 = T(var5);
         }

         if (var2 != null) {
            if (this.n3 == null) {
               this.n3 = new VecDeltaCodec();
            }

            this.n3.setBase(var2);
            AABB var6 = this.t.getBoundingBox().move(var2.subtract(this.t.position()));
            if (!this.I(var6) && this.S.getValue()) {
               Vec3 var7 = mc.player.position();
               if (A(var7, var6) < A(var7, this.t.getBoundingBox())) {
                  this.j(true, "Target moved closer");
               }
            }
         }
      }
   }

   private boolean R() {
      return this.t != null && this.t.isAlive() && this.t(this.t);
   }

   private boolean t(Entity var1) {
      if (mc.player != null && var1 instanceof Player && var1 != mc.player) {
         double var2 = A(mc.player.position(), var1.getBoundingBox());
         double var4 = Math.min(this.o.getValue(), this.Q.getValue());
         double var6 = Math.max(this.o.getValue(), this.Q.getValue());
         return var2 >= var4 * var4 && var2 <= var6 * var6;
      } else {
         return false;
      }
   }

   private void x(Entity var1) {
      if (this.t(var1)) {
         if (var1 != this.t) {
            this.f(true, false, "Target changed");
            this.n3 = new VecDeltaCodec();
            this.n3.setBase(var1.position());
            com.larpclient.h.U("Backtrack", "New target {}", var1.getName().getString());
         }

         this.t = var1;
      }
   }

   private void f(boolean var1, boolean var2, String var3) {
      if (var2) {
         this.L(var3);
      } else if (var1) {
         this.j(true, var3);
      }

      this.t = null;
      this.n3 = null;
   }

   private void j(boolean var1, String var2) {
      if (!this.e && !this.E.isEmpty()) {
         if (mc.getConnection() != null && this.n()) {
            long var3 = L();
            long var5 = this.M.getValue().longValue();
            int var7 = 0;
            this.e = (boolean)1;

            try {
               while (!this.E.isEmpty() && (var1 || var3 - this.z.peekFirst() >= var5) && this.n()) {
                  this.z.removeFirst();
                  this.E.removeFirst().run();
                  var7++;
               }

               if (!this.n()) {
                  this.L("Session changed during release");
               }
            } catch (RuntimeException | Error var12) {
               this.E.clear();
               this.z.clear();
               throw var12;
            } finally {
               this.e = (boolean)0;
            }

            if (var7 > 0 && var2 != null) {
               com.larpclient.h.U("Backtrack", "Released {} held updates: {}", var7, var2);
            }
         } else {
            this.L(var2 == null ? "Session changed" : var2 + "; session changed");
         }
      }
   }

   private void L(String var1) {
      int var2 = this.E.size();
      this.E.clear();
      this.z.clear();
      if (var2 > 0 && var1 != null) {
         com.larpclient.h.U("Backtrack", "Discarded {} held updates: {}", var2, var1);
      }
   }

   static long n(e var0, e var1) {
      int var2 = Math.round((float)Math.min(var0.getValue(), var1.getValue()));
      int var3 = Math.round((float)Math.max(var0.getValue(), var1.getValue()));
      return var3 > var2 ? ThreadLocalRandom.current().nextInt(var2, var3 + 1) : Math.max(0, var2);
   }

   static double r(AABB var0, AABB var1) {
      double var2 = Math.max(0.0, Math.max(var0.minX - var1.maxX, var1.minX - var0.maxX));
      double var4 = Math.max(0.0, Math.max(var0.minY - var1.maxY, var1.minY - var0.maxY));
      double var6 = Math.max(0.0, Math.max(var0.minZ - var1.maxZ, var1.minZ - var0.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   static double A(Vec3 var0, AABB var1) {
      return var0.distanceToSqr(
         new Vec3(Mth.clamp(var0.x, var1.minX, var1.maxX), Mth.clamp(var0.y, var1.minY, var1.maxY), Mth.clamp(var0.z, var1.minZ, var1.maxZ))
      );
   }

   private boolean p(AABB var1) {
      if (var1 != null && mc.player != null && this.nJ.getValue()) {
         double var2 = this.nU.getValue();
         return A(mc.player.getEyePosition(), var1) > var2 * var2;
      } else {
         return false;
      }
   }

   private boolean I(AABB var1) {
      if (!this.p(var1)) {
         return false;
      }

      String var2 = String.format(Locale.ROOT, "Target out of reach (%.1f blocks)", Math.sqrt(A(mc.player.getEyePosition(), var1)));
      if (this.r.w() + this.E.size() > 0) {
         this.Z(var1);
      }

      if (this.G.O("New")) {
         this.f(true, false, var2);
      } else {
         this.d(var2);
         this.W(false, false, true, null);
      }

      return true;
   }

   private AABB d() {
      if (this.t == null) {
         return null;
      } else if (this.G.O("New")) {
         return this.n3 == null ? null : this.t.getBoundingBox().move(this.n3.getBase().subtract(this.t.position()));
      } else {
         return this.t.isAlive() && !this.O.getBase().equals(Vec3.ZERO) ? this.t.getType().getDimensions().makeBoundingBox(this.O.getBase()) : null;
      }
   }

   private void Z(AABB var1) {
      this.s = new Vec3((var1.minX + var1.maxX) / 2.0, var1.minY, (var1.minZ + var1.maxZ) / 2.0);
      this.q = Math.max(var1.getXsize(), var1.getZsize()) / 2.0;
      this.J = var1.getYsize();
      this.a = cf.F();
   }

   @Override
   public String effectName() {
      return "Backtrack";
   }

   @Override
   public void tick(Minecraft var1) {
      if (!this.isEnabled()) {
         ;
      }
   }

   @Override
   public boolean glowing() {
      return !this.isEnabled() ? false : this.l && this.g;
   }

   @Override
   public void clear() {
      this.l = (boolean)0;
      this.Z = this.s = null;
      this.R = null;
      this.nP = null;
      this.N = -1;
      this.k = this.v = 0.0;
      this.d = -1.0;
      this.a = -10.0;
      this.nQ.q();
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.l = (boolean)0;
         this.R = null;
         this.nQ.q();
         if (this.isInGame() && !this.u.O("None")) {
            double var3 = cf.F();
            double var5 = cf.v();
            this.g = this.ne.getValue();
            this.y = this.L.getValue();
            this.n = this.D.getValue();
            this.d = this.s != null && var3 - this.a < 0.55 ? Math.max(0.0, var3 - this.a) / 0.55 : -1.0;
            AABB var7 = this.d();
            if (var7 == null) {
               this.Z = null;
               this.N = -1;
               this.k = 0.0;
               this.l = (boolean)(this.d >= 0.0 ? 1 : 0);
            } else {
               Vec3 var8 = new Vec3((var7.minX + var7.maxX) / 2.0, var7.minY, (var7.minZ + var7.maxZ) / 2.0);
               int var9 = this.Z != null && this.N == this.t.getId() && this.nP == mc.level && this.Z.distanceToSqr(var8) < 16.0 ? 1 : 0;
               this.Z = var9 != 0 ? this.Z.lerp(var8, 1.0 - Math.exp(-var5 * 24.0)) : var8;
               this.N = this.t.getId();
               this.nP = mc.level;
               this.nl = var7.getXsize() / 2.0;
               this.j = var7.getZsize() / 2.0;
               this.i = var7.getYsize();
               this.k = this.k + ((this.r.w() + this.E.size() > 0 ? 1 : 0) - this.k) * (1.0 - Math.exp(-var5 * 10.0));
               Vec3 var10 = this.t.getPosition(var1);
               this.v = st.h((var10.distanceTo(this.Z) - 0.05) / 0.45);
               if (this.nL.getValue() && this.v > 0.01) {
                  EntityRenderState var11 = j4.d(this.t, var1);
                  var11.x = this.Z.x;
                  var11.y = this.Z.y;
                  var11.z = this.Z.z;
                  var11.nameTag = null;
                  var11.scoreText = null;
                  var11.leashStates = null;
                  var11.displayFireAnimation = (boolean)0;
                  var11.shadowPieces.clear();
                  var11.outlineColor = 0;
                  this.R = var11;
               }

               if (this.nw.getValue() && this.v > 0.01) {
                  Vec3 var22 = var10.add(0.0, this.i * 0.55, 0.0);
                  Vec3 var12 = this.Z.add(0.0, this.i * 0.55, 0.0);
                  double var13 = Math.min(0.3, var22.distanceTo(var12) * 0.12);

                  for (int var15 = 0; var15 <= 24; var15++) {
                     double var16 = var15 / 24.0;
                     double var18 = Math.pow(0.5 + 0.5 * Math.cos((var16 * 2.5 - var3 * 1.6) * Math.PI * 2.0), 6.0);
                     double var20 = st.h(var16 / 0.12) * st.h((1.0 - var16) / 0.12);
                     this.nQ
                        .n(
                           var22.lerp(var12, var16).add(0.0, Math.sin(Math.PI * var16) * var13, 0.0),
                           0.03 * (1.0 + 0.7 * var18) * (0.35 + 0.65 * var20),
                           uh.F(uh.q(this.y, this.n, var16), this.v * (0.3 + 0.7 * var18))
                        );
                  }

                  this.nQ.x(var2.pos);
               }

               this.l = (boolean)1;
            }
         } else {
            this.clear();
         }
      }
   }

   @Override
   public void render(kj var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.l && (!var2 || this.g)) {
            long var3 = zj.m();
            double var5 = cf.F();
            if (this.Z != null) {
               double var7 = 0.65 + 0.35 * this.k;
               AABB var9 = new AABB(this.Z.x - this.nl, this.Z.y, this.Z.z - this.j, this.Z.x + this.nl, this.Z.y + this.i, this.Z.z + this.j).inflate(0.03);
               if (this.R != null) {
                  var1.u(this.R, j4.j.k, uh.q(this.y, this.n, 0.4), 0.55 * this.v * (var2 ? 0.5 : 1.0));
               }

               var1.blend(gl.c.E);
               if (!this.u.O("Fill")) {
                  this.s(var1, var9, var5, var7);
               }

               if (!this.u.O("Wireframe")) {
                  this.o(var1, var9, var5, var7);
               }

               this.R(var1, var9, var5, var7);
               this.G(var1, var9, var5, var7, var2);
               if (this.nQ.F() > 1) {
                  var1.b(this.nQ, true);
               }

               var1.blend(gl.c.K);
               if (!var2 && !this.u.O("Wireframe")) {
                  this.s(var1, var9, var7);
               }
            }

            if (this.d >= 0.0) {
               var1.blend(gl.c.E);
               this.C(var1, this.d);
               var1.blend(gl.c.K);
            }

            zj.l("Backtrack ESP", var3);
         }
      }
   }

   private static Vec3 o(AABB var0, int var1) {
      return new Vec3((var1 & 1) == 0 ? var0.minX : var0.maxX, (var1 & 2) == 0 ? var0.minY : var0.maxY, (var1 & 4) == 0 ? var0.minZ : var0.maxZ);
   }

   private void s(kj var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();

      for (int[] var11 : X) {
         Vec3 var12 = o(var2, var11[0]);
         Vec3 var13 = o(var2, var11[1]);

         for (int var14 = 0; var14 < 6; var14++) {
            Vec3 var15 = var12.lerp(var13, var14 / 6.0);
            Vec3 var16 = var12.lerp(var13, (var14 + 1) / 6.0);
            var1.W(
               var15.x, var15.y, var15.z, var16.x, var16.y, var16.z, 0.045, 0.045, this.I(var2, var7, var15, var3, var5), this.I(var2, var7, var16, var3, var5)
            );
         }
      }
   }

   private int I(AABB var1, Vec3 var2, Vec3 var3, double var4, double var6) {
      double var8 = Math.clamp((var3.y - var1.minY) / Math.max(1.0E-6, var1.getYsize()), 0.0, 1.0);
      double var10 = Math.atan2(var3.z - var2.z, var3.x - var2.x);
      double var12 = Math.pow(0.5 + 0.5 * Math.cos(var10 * 2.0 + var8 * 4.0 - var4 * 3.2), 10.0);
      int var14 = uh.q(this.y, this.n, var8);
      return uh.F(uh.q(var14, var14 | 16777215, var12 * 0.6), var6 * (0.6 + 0.4 * var12));
   }

   private void o(kj var1, AABB var2, double var3, double var5) {
      double var7 = var3 * 1.6;
      double var9 = 0.5 - 0.5 * Math.cos(var7);
      double var11 = Math.sin(var7);

      for (int var13 = 0; var13 < 3; var13++) {
         double var14 = Math.clamp(var9 - Math.signum(var11) * var13 * 0.07, 0.0, 1.0);
         double var16 = var13 == 0 ? 0.85 : 0.4 / var13 * Math.abs(var11);
         int var18 = uh.F(uh.q(this.y, this.n, var14), var16 * var5);
         double var19 = var2.minY + var2.getYsize() * var14;
         double var21 = var13 == 0 ? 0.04 : 0.025;
         var1.W(var2.minX, var19, var2.minZ, var2.maxX, var19, var2.minZ, var21, var21, var18, var18);
         var1.W(var2.maxX, var19, var2.minZ, var2.maxX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.W(var2.maxX, var19, var2.maxZ, var2.minX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.W(var2.minX, var19, var2.maxZ, var2.minX, var19, var2.minZ, var21, var21, var18, var18);
      }
   }

   private void R(kj var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();
      double var8 = Math.max(var2.getXsize(), var2.getZsize()) * 0.75;
      double var10 = var2.minY + 0.02;
      double var12 = var3 / 1.3 - Math.floor(var3 / 1.3);
      var1.F(var7.x, var10, var7.z, var8 * (1.0 + 0.04 * Math.sin(var3 * 3.0)), 0.14, uh.F(this.n, 0.7 * var5));
      var1.F(var7.x, var10, var7.z, var8 * (1.0 + var12 * 1.1), 0.1, uh.F(this.y, 0.5 * var5 * (1.0 - var12) * (1.0 - var12)));
   }

   private void G(kj var1, AABB var2, double var3, double var5, boolean var7) {
      for (int var8 = 0; var8 < 8; var8++) {
         Vec3 var9 = o(var2, var8);
         double var10 = 0.5 + 0.5 * Math.sin(var3 * 4.5 + var8 * 1.9);
         int var12 = uh.q((var8 & 2) == 0 ? this.y : this.n, -1, 0.45);
         var1.I(var9.x, var9.y, var9.z, var7 ? 0.22 : 0.16, 0.0, uh.F(var12, (var7 ? 0.35 : 0.2) * var5), c3.a.Z, 0.0, 0.0, 0.0);
         if (!var7) {
            var1.P(var9.x, var9.y, var9.z, 0.05 + 0.035 * var10, var3 * 0.8 + var8, uh.F(var12, var5 * (0.55 + 0.45 * var10)));
         }
      }
   }

   private void s(kj var1, AABB var2, double var3) {
      double var5 = this.u.O("Fill") ? 1.8 : 1.0;
      int var7 = uh.F(this.y, 0.16 * var5 * var3);
      int var8 = uh.F(this.n, 0.035 * var5 * var3);
      double[][] var9 = new double[][]{
         {var2.minX, var2.minZ, var2.maxX, var2.minZ},
         {var2.maxX, var2.minZ, var2.maxX, var2.maxZ},
         {var2.maxX, var2.maxZ, var2.minX, var2.maxZ},
         {var2.minX, var2.maxZ, var2.minX, var2.minZ}
      };

      for (double[] var13 : var9) {
         var1.vertex(var13[0], var2.minY, var13[1], var7);
         var1.vertex(var13[2], var2.minY, var13[3], var7);
         var1.vertex(var13[2], var2.maxY, var13[3], var8);
         var1.vertex(var13[0], var2.maxY, var13[1], var8);
      }

      var1.vertex(var2.minX, var2.maxY, var2.minZ, var8);
      var1.vertex(var2.maxX, var2.maxY, var2.minZ, var8);
      var1.vertex(var2.maxX, var2.maxY, var2.maxZ, var8);
      var1.vertex(var2.minX, var2.maxY, var2.maxZ, var8);
   }

   private void C(kj var1, double var2) {
      double var4 = 1.0 - Math.pow(1.0 - var2, 3.0);
      double var6 = Math.pow(1.0 - var2, 1.5);
      Vec3 var8 = this.s;
      double var9 = this.q * 1.5 * (1.0 + var4 * 2.2);
      double var11 = var8.y + 0.03;
      var1.F(var8.x, var11, var8.z, var9, 0.05 + 0.22 * (1.0 - var2), uh.F(this.y, 0.9 * var6));
      var1.F(var8.x, var11, var8.z, var9 * 0.7, 0.12, uh.F(this.n, 0.6 * var6));
      AABB var13 = new AABB(var8.x - this.q, var8.y, var8.z - this.q, var8.x + this.q, var8.y + this.J, var8.z + this.q).inflate(0.35 * var4);
      int var14 = uh.F(uh.q(this.y, this.n, 0.5), var6 * var6);

      for (int[] var18 : X) {
         Vec3 var19 = o(var13, var18[0]);
         Vec3 var20 = o(var13, var18[1]);
         var1.W(var19.x, var19.y, var19.z, var20.x, var20.y, var20.z, 0.05, 0.05, var14, var14);
      }

      int var23 = uh.F(uh.q(this.n, -1, 0.5), var6);

      for (int var24 = 0; var24 < 12; var24++) {
         double var25 = var24 * Math.PI / 6.0 + var2;
         double var26 = Math.cos(var25);
         double var21 = Math.sin(var25);
         var1.I(
            var8.x + var26 * var9,
            var11 + 0.1 + var24 % 2 * var4 * 0.4,
            var8.z + var21 * var9,
            0.02 + 0.06 * (1.0 - var2),
            var25,
            var23,
            c3.a.P,
            var26,
            0.0,
            var21
         );
      }
   }

   private void T() {
      if (this.isRuntimeActive() && this.isInGame() && this.t != null && !this.u.O("None")) {
         int var1 = kz.x() && kz.u(this.effectName()) ? 1 : 0;
         AABB var2 = var1 != 0 && this.Z != null && this.N == this.t.getId()
            ? new AABB(this.Z.x - this.nl, this.Z.y, this.Z.z - this.j, this.Z.x + this.nl, this.Z.y + this.i, this.Z.z + this.j)
            : this.d();
         if (var2 != null) {
            int var3 = this.L.getValue() & 16777215;
            if (var1 == 0) {
               int var4 = this.u.O("Fill") ? 0 : -520093696 | var3;
               int var5 = this.u.O("Wireframe") ? 0 : 754974720 | var3;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var4, 1.0F, var5)).setAlwaysOnTop();
            } else if (this.f.getValue()) {
               int var6 = this.u.O("Fill") ? 0 : 1174405120 | var3;
               int var7 = this.u.O("Fill") ? 503316480 | var3 : 0;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var6, 1.0F, var7)).setAlwaysOnTop();
            }
         }
      }
   }

   private static boolean X(ClientboundRemoveEntitiesPacket var0, int var1) {
      return var0.getEntityIds().contains(var1);
   }

   private static Vec3 V(VecDeltaCodec var0, ClientboundMoveEntityPacket var1) {
      return var0.decode(var1.getXa(), var1.getYa(), var1.getZa());
   }

   private static Vec3 T(ClientboundEntityPositionSyncPacket var0) {
      return var0.values().position();
   }
}
