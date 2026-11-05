package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._3;
import com.larpclient.__;
import com.larpclient.cg;
import com.larpclient.h;
import com.larpclient.hp;
import com.larpclient.k8;
import com.larpclient.ki;
import com.larpclient.kx;
import com.larpclient.mt;
import com.larpclient.pf;
import com.larpclient.tj;
import com.larpclient.ty;
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

public final class BacktrackModule extends Module implements com.larpclient.c {
   private static BacktrackModule W;
   private static final int E = 65536;
   private static final long cN = System.nanoTime();
   private final d6 m = this.addSetting(new d6("Version", "Packet delay algorithm", "New", "Old", "New"));
   private final d9 M = this.addSetting(new d9("Range Min", "Lower bound of the box distance to the target", 1.0, 0.0, 10.0, 0.1));
   private final d9 S = this.addSetting(new d9("Range Max", "Upper bound of the box distance to the target", 3.0, 0.0, 10.0, 0.1));
   private final d9 t = this.addSetting(new d9("Delay Min", "Shortest hold window in milliseconds", 280.0, 0.0, 2000.0, 1.0));
   private final d9 c7 = this.addSetting(new d9("Delay Max", "Longest hold window in milliseconds", 480.0, 0.0, 2000.0, 1.0));
   private final d9 c = this.addSetting(new d9("Next Backtrack Delay Min", "Shortest wait before the next window in milliseconds", 0.0, 0.0, 2000.0, 1.0));
   private final d9 B = this.addSetting(new d9("Next Backtrack Delay Max", "Longest wait before the next window in milliseconds", 10.0, 0.0, 2000.0, 1.0));
   private final d9 K = this.addSetting(
      new d9("Tracking Buffer", "Keep windows available after the target leaves range, in milliseconds", 500.0, 0.0, 2000.0, 1.0)
   );
   private final d9 j = this.addSetting(new d9("Chance", "Chance that an attack allows windows, in percent", 50.0, 0.0, 100.0, 1.0));
   private final dh k = this.addSetting(new dh("Pause On Hurt Time", "Skip windows while the target is hurt", false));
   private final d9 g = this.addSetting(new d9("Hurt Time", "Target hurt time that pauses windows", 3.0, 0.0, 10.0, 1.0));
   private final d6 H = this.addSetting(new d6("Target Mode", "Use the attacked entity or the nearest one in range", "Attack", "Attack", "Range"));
   private final d9 N = this.addSetting(
      new d9("Time Since Last Attack", "How long an attack keeps windows available, in milliseconds", 1000.0, 0.0, 5000.0, 1.0)
   );
   private final dh cq = this.addSetting(new dh("Release Out Of Reach", "Release held packets as soon as the target's server position is too far to hit", true));
   private final d9 ch = this.addSetting(
      new d9("Release Distance", "Server position distance from your eyes that releases held packets. Vanilla ignores hits past 6", 6.0, 1.0, 10.0, 0.1)
   );
   private final d6 R = this.addSetting(new d6("ESP", "Draw the target's server position", "Box", "Box", "Wireframe", "Fill", "None"));
   private final dk cO = this.addSetting(new dk("Color", "Server position box color", -1));
   private final dk F = this.addSetting(new dk("Gradient", "Second ESP color, blended toward the top", -6862849));
   private final dh i = this.addSetting(new dh("Ghost Model", "Draw a hologram of the target at its server position", true));
   private final dh cx = this.addSetting(new dh("Tether", "Link the displayed target to its server position", true));
   private final dh T = this.addSetting(new dh("Bloom", "Glow pass for the ESP", true));
   private final dh Y = this.addSetting(new dh("Through Walls", "Keep a faint outline visible behind blocks", true));
   private final d9 q = this.addSetting(new d9("New Range Min", "Lower bound of the distance to the target player", 0.0, 0.0, 10.0, 0.01));
   private final d9 r = this.addSetting(new d9("New Range Max", "Upper bound of the distance to the target player", 10.0, 0.0, 10.0, 0.01));
   private final d9 C = this.addSetting(new d9("New Delay", "How long each held update waits, in milliseconds", 15000.0, 0.0, 15000.0, 1.0));
   private final dh X = this.addSetting(new dh("Only Attack", "Release held updates when the target moves closer", true));
   private Entity o;
   private String G = "Old";
   private final k8 U = new k8(65536);
   private final ArrayDeque<Runnable> J = new ArrayDeque<>();
   private final VecDeltaCodec d = new VecDeltaCodec();
   private boolean D;
   private boolean ct;
   private long I;
   private long e;
   private long v;
   private int b;
   private final ArrayDeque<Runnable> cE = new ArrayDeque<>();
   private final ArrayDeque<Long> w = new ArrayDeque<>();
   private VecDeltaCodec co;
   private boolean ca;
   private ClientPacketListener Q;
   private ClientLevel z;
   private volatile int n;
   private static final double l = 0.55;
   private static final int[][] O = new int[][]{{0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
   private final __.l cz = new __.l(25);
   private boolean f;
   private boolean u;
   private Object cV;
   private int Z = -1;
   private int s;
   private int h;
   private Vec3 c5;
   private double cj;
   private double y;
   private double P;
   private double V;
   private double x;
   private double p = -1.0;
   private EntityRenderState A;
   private Vec3 c4;
   private double cY;
   private double L;
   private double a = -10.0;

   public BacktrackModule() {
      super("Backtrack", "Backtracks player position to a hittable state", Category.COMBAT, -1);
      W = this;
      this.M.V(this.S).n("Range");
      this.t.V(this.c7).n("Delay");
      this.c.V(this.B).n("Next Backtrack Delay");
      this.q.V(this.r).n("New Range");
      this.K.T("ms");
      this.j.T("%");
      this.g.T("");
      this.N.T("ms").n("Last Attack Time To Work");
      this.C.T("ms");

      for (df var4 : new df[]{this.M, this.S, this.t, this.c7, this.c, this.B, this.K, this.j, this.k, this.H, this.N}) {
         var4.H(() -> this.m.S("Old"));
      }

      this.g.H(() -> this.m.S("Old") && this.k.P());
      this.ch.H(this.cq::P);

      for (df var11 : new df[]{this.cO, this.F, this.i, this.cx, this.T, this.Y}) {
         var11.H(() -> !this.R.S("None"));
      }

      for (df var12 : new df[]{this.q, this.r, this.C, this.X}) {
         var12.H(() -> this.m.S("New"));
      }

      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.Z());
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      }

      if (this.o == null) {
         return "Waiting for target";
      }

      int var1 = this.U.u() + this.cE.size();
      return "Tracking " + this.o.getName().getString() + (var1 > 0 ? " · " + var1 + " held" : "");
   }

   @Override
   public void onEnable() {
      this.G = this.m.P();
      this.b = ThreadLocalRandom.current().nextInt(0, 101);
      this.L(false, false, false, null);
      this.H(false, false, null);
      this.clear();
      this.s();
   }

   @Override
   public void onDisable() {
      this.n = 0;
      this.L(true, false, true, "Stopped");
      this.H(true, false, "Stopped");
      this.clear();
   }

   private void s() {
      this.n = !this.isRuntimeActive() ? 0 : (this.m.S("Old") ? (this.o == null && this.U.u() <= 0 ? 0 : 1) : (!this.cE.isEmpty() ? 2 : 0));
   }

   public static void I(Packet<?> var0, PacketListener var1) {
      if (W != null && W.isEnabled()) {
         BacktrackModule var2 = W;
         if (var2 != null && var2.n != 0 && var1 instanceof ClientPacketListener var3) {
            Minecraft var4 = getMc();
            if (var4 != null && !var4.isSameThread()) {
               if (var2.n == 1 ? c(var0) : var0 instanceof ClientboundDisconnectPacket) {
                  PacketUtils.ensureRunningOnSameThread(var0, var3, var4.packetProcessor());
               }
            }
         }
      }
   }

   static boolean c(Packet<?> var0) {
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

   public static boolean Z() {
      BacktrackModule var0 = W;
      return var0 != null && var0.U.u() > 0;
   }

   private static long y() {
      return (System.nanoTime() - cN) / 1000000L;
   }

   private void X() {
      String var1 = this.m.P();
      if (!var1.equals(this.G)) {
         this.L(true, false, false, "Version changed");
         this.H(true, false, "Version changed");
         this.G = var1;
      }
   }

   private boolean E() {
      Minecraft var1 = getMc();
      return this.Q != null && var1.getConnection() == this.Q && var1.level == this.z && this.Q.getConnection().isConnected();
   }

   private void x() {
      if (this.U.u() <= 0 && this.J.isEmpty() && this.cE.isEmpty()) {
         this.Q = mc.getConnection();
         this.z = mc.level;
      }
   }

   public static boolean w() {
      BacktrackModule var0 = W;
      return var0 != null && (var0.U.F() || var0.ca || var0.ct);
   }

   public static boolean C(Packet<?> var0, PacketListener var1, Runnable var2) {
      if (W != null && W.isEnabled()) {
         BacktrackModule var3 = W;
         Minecraft var4 = getMc();
         if (var3 != null
            && !w()
            && var4.isSameThread()
            && !(var0 instanceof ClientboundBundlePacket)
            && var1 instanceof ClientPacketListener
            && var1 == var4.getConnection()
            && var3.isRuntimeActive()) {
            try {
               var3.X();
               return var3.m.S("New") ? var3.b(var0, var2) : var3.s(var0, var2);
            } finally {
               var3.s();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean o(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (W != null && W.isEnabled()) {
         BacktrackModule var4 = W;
         Minecraft var5 = getMc();
         if (var4 != null
            && !w()
            && var5 != null
            && var5.isSameThread()
            && var4.isRuntimeActive()
            && var5.player != null
            && var5.level != null
            && var5.getConnection() != null
            && !var5.isLocalServer()
            && var0 == var5.getConnection().getConnection()) {
            var4.X();
            if (!var4.m.S("New") && var1 instanceof ServerboundPongPacket && var4.U.u() != 0) {
               var4.J.addLast(() -> var0.send(var1, var2, var3));
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

   public static boolean C() {
      BacktrackModule var0 = W;
      return var0 != null && var0.ct;
   }

   public static void o(Entity var0) {
      if (W != null && W.isEnabled()) {
         BacktrackModule var1 = W;
         if (var1 != null && var0 != null && var1.isRuntimeActive() && var1.isInGame()) {
            try {
               var1.X();
               if (!var1.m.S("New")) {
                  var1.e = y();
                  var1.b = ThreadLocalRandom.current().nextInt(0, 101);
                  if (var1.H.S("Attack")) {
                     var1.m(var0);
                  }

                  return;
               }

               var1.N(var0);
            } finally {
               var1.s();
            }
         }
      }
   }

   @Override
   public void onTick() {
      try {
         this.O();
      } finally {
         this.s();
      }
   }

   private void O() {
      this.X();
      if (this.m.S("New")) {
         this.z();
      } else if (mc.player != null && mc.level != null) {
         if (this.H.S("Range")) {
            Entity var1 = this.L();
            if (var1 == null) {
               this.L(true, false, true, "No target in range");
            } else {
               this.m(var1);
            }
         }

         int var2 = this.U.u() > 0 ? 1 : 0;
         if (var2 == 0 || !this.L(this.M())) {
            if (this.H()) {
               if (var2 != 0 && this.U.T(y())) {
                  this.v("Window ended");
                  this.L(false, false, true, null);
               }
            } else if (var2 != 0) {
               this.v("Target no longer eligible");
               this.L(false, false, true, null);
            }
         }
      } else {
         this.L(false, true, false, "World unavailable");
      }
   }

   private boolean s(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.level == null || mc.getConnection() == null || mc.isLocalServer()) {
         return false;
      }

      if (k(var1)) {
         return false;
      }

      if (this.e(var1)) {
         this.L(true, false, true, "Server boundary packet");
         return false;
      }

      boolean var3 = this.H();
      int var4 = this.U.u() > 0 ? 1 : 0;
      if (var4 == 0 && !var3) {
         return false;
      }

      Entity var5 = this.o;
      if (var4 == 0 && !(var1 instanceof ClientboundPingPacket)) {
         this.v(var1, var5);
         return false;
      }

      if (var5 != null) {
         Vec3 var6 = this.v(var1, var5);
         if (var6 != null && this.L(var5.getType().getDimensions().makeBoundingBox(var6))) {
            return false;
         }

         if (var6 != null && this.Z(var5, var6)) {
            this.v("Target moved closer");
            return false;
         }
      }

      if (var4 == 0) {
         if (var5 != null && this.Y(this.M())) {
            return false;
         }

         this.x();
         long var8 = d(this.t, this.c7);
         if (this.U.W()) {
            this.U.C();
         }

         this.U.b(y(), var8);
         mt.U("Backtrack", "Old window opened for {} ms", var8);
      }

      if (this.U.J(var2)) {
         return true;
      }

      this.v("Packet capacity reached");
      return false;
   }

   private static boolean k(Packet<?> var0) {
      return var0 instanceof ClientboundPlayerChatPacket
         || var0 instanceof ClientboundSystemChatPacket
         || var0 instanceof ClientboundDisguisedChatPacket
         || var0 instanceof ClientboundSetActionBarTextPacket
         || var0 instanceof ClientboundSoundPacket var1 && var1.getSound().value() == SoundEvents.PLAYER_HURT;
   }

   private boolean e(Packet<?> var1) {
      return var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F
         || var1 instanceof ClientboundRemoveEntitiesPacket var2 && this.o != null && B(var2, this.o.getId());
   }

   private Vec3 v(Packet<?> var1, Entity var2) {
      return var2 != null && mc.level != null ? L(this.d, var1, mc.level, var2) : null;
   }

   static Vec3 L(VecDeltaCodec var0, Packet<?> var1, ClientLevel var2, Entity var3) {
      Vec3 var4 = null;
      if (var1 instanceof ClientboundMoveEntityPacket var5 && var5.hasPosition() && var5.getEntity(var2) == var3) {
         var4 = N(var0, var5);
      } else if (var1 instanceof ClientboundTeleportEntityPacket var6 && var6.id() == var3.getId()) {
         var4 = var6.relatives().isEmpty()
            ? var6.change().position()
            : PositionMoveRotation.calculateAbsolute(
                  new PositionMoveRotation(var0.getBase(), var3.getDeltaMovement(), var3.getYRot(), var3.getXRot()), var6.change(), var6.relatives()
               )
               .position();
      } else if (var1 instanceof ClientboundEntityPositionSyncPacket var7 && var7.id() == var3.getId()) {
         var4 = W(var7);
      }

      if (var4 != null) {
         var0.setBase(var4);
      }

      return var4;
   }

   private boolean Z(Entity var1, Vec3 var2) {
      AABB var3 = var1.getType().getDimensions().makeBoundingBox(var2);
      AABB var4 = mc.player.getBoundingBox();
      return t(var4, var3) < t(var4, var1.getBoundingBox());
   }

   private boolean H() {
      return this.o != null && this.o.isAlive() && this.u(this.o);
   }

   private boolean u(Entity var1) {
      if (mc.player != null && var1 != null) {
         boolean var2 = this.P(var1);
         long var3 = y();
         if (var2) {
            this.I = var3;
         }

         return (var2 || this.I != 0L && var3 - this.I <= this.K.P().longValue())
            && this.d(var1)
            && mc.player.tickCount > 10
            && this.b < this.j.P()
            && var3 >= this.v
            && (!this.k.P() || !this.D)
            && this.e != 0L
            && var3 - this.e <= this.N.P().longValue();
      } else {
         return false;
      }
   }

   private boolean d(Entity var1) {
      if (!(var1 instanceof LivingEntity var2 && mc.player != null && var1 != mc.player)) {
         return false;
      } else {
         if (!var2.isAlive() || var2.isRemoved() || var2.isSpectator() || !var2.isPickable()) {
            return false;
         }

         if (!(var1 instanceof Player)) {
            return true;
         }

         MCFriendModule var3 = LarpClient.F().x().V(MCFriendModule.class);
         return var3 == null || !var3.V(var1.getName().getString());
      }
   }

   private boolean P(Entity var1) {
      double var2 = Math.sqrt(t(mc.player.getBoundingBox(), var1.getBoundingBox()));
      return var2 >= Math.min(this.M.P(), this.S.P()) && var2 <= Math.max(this.M.P(), this.S.P());
   }

   private Entity L() {
      double var1 = Math.min(this.M.P(), this.S.P());
      double var3 = Math.max(this.M.P(), this.S.P());
      AABB var5 = mc.player.getBoundingBox();
      Entity var6 = null;
      double var7 = Double.MAX_VALUE;

      for (Entity var10 : mc.level.getEntities(mc.player, var5.inflate(var3 + 1.0), this::d)) {
         double var11 = Math.sqrt(t(var5, var10.getBoundingBox()));
         if (var11 >= var1 && var11 <= var3 && var11 < var7) {
            var6 = var10;
            var7 = var11;
         }
      }

      return var6;
   }

   private void m(Entity var1) {
      this.D = (boolean)(var1 instanceof LivingEntity var2 && var2.hurtTime >= this.g.P() ? 1 : 0);
      if (this.u(var1)) {
         if (var1 != this.o) {
            this.L(true, false, false, "Target changed");
            this.d.setBase(var1.getPositionCodec().getBase());
         }

         this.o = var1;
      }
   }

   private void L(boolean var1, boolean var2, boolean var3, String var4) {
      if (var1 && !var2) {
         this.v(var4);
      } else if (var2) {
         this.V(var4);
      }

      if (this.o != null && var3) {
         this.v = y() + d(this.c, this.B);
      }

      this.o = null;
      this.D = (boolean)0;
      this.d.setBase(Vec3.ZERO);
   }

   private void v(String var1) {
      int var2 = this.U.u() + this.J.size();
      if (var2 == 0) {
         this.U.C();
      } else if (mc.getConnection() != null && this.E()) {
         this.ct = (boolean)1;

         try {
            while (!this.J.isEmpty() && this.E()) {
               this.J.removeFirst().run();
            }
         } finally {
            this.ct = (boolean)0;
            this.J.clear();
         }

         this.U.M(this::E);
         if (var1 != null) {
            mt.U("Backtrack", "Released {} held packets: {}", var2, var1);
         }
      } else {
         this.V(var1 + "; session changed");
      }
   }

   private void V(String var1) {
      int var2 = this.U.u() + this.J.size();
      this.U.C();
      this.J.clear();
      if (var2 > 0 && var1 != null) {
         mt.U("Backtrack", "Discarded {} held packets: {}", var2, var1);
      }
   }

   private void z() {
      if (mc.player != null && mc.level != null) {
         if (!this.z()) {
            this.H(true, false, "Target left New range");
         } else if (!this.L(this.M())) {
            this.n(false, null);
         }
      } else {
         this.H(true, true, "World unavailable");
      }
   }

   private boolean b(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.level == null) {
         this.H(true, true, "World unavailable");
         return false;
      }

      if (var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F) {
         this.H(true, false, "Server boundary packet");
         return false;
      }

      if (this.cE.isEmpty() && !this.z()) {
         return false;
      }

      this.z(var1);
      if (this.o == null || u(var1) || !f(var1)) {
         return false;
      }

      if (this.cE.size() >= 65536) {
         this.n(true, "Packet capacity reached");
         return false;
      }

      if (this.cE.isEmpty()) {
         this.x();
         mt.U("Backtrack", "New queue opened with {} ms delay", this.C.P().longValue());
      }

      this.cE.addLast(var2);
      this.w.addLast(y());
      return true;
   }

   private static boolean u(Packet<?> var0) {
      return var0 instanceof ClientboundSoundPacket || var0 instanceof ClientboundSetHealthPacket || var0 instanceof ClientboundSetEntityDataPacket;
   }

   private static boolean f(Packet<?> var0) {
      return var0 instanceof ClientboundPingPacket
         || var0 instanceof ClientboundRotateHeadPacket
         || var0 instanceof ClientboundTeleportEntityPacket
         || var0 instanceof ClientboundEntityPositionSyncPacket
         || var0 instanceof ClientboundSetEntityMotionPacket
         || var0 instanceof ClientboundMoveEntityPacket
         || var0 instanceof ClientboundLevelParticlesPacket
         || var0 instanceof ClientboundExplodePacket;
   }

   private void z(Packet<?> var1) {
      if (mc.player != null && mc.level != null && this.o != null) {
         Vec3 var2 = null;
         if (var1 instanceof ClientboundMoveEntityPacket var3 && var3.hasPosition() && var3.getEntity(mc.level) == this.o && this.co != null) {
            var2 = N(this.co, var3);
         } else if (var1 instanceof ClientboundTeleportEntityPacket var4 && var4.id() == this.o.getId()) {
            var2 = var4.change().position();
         } else if (var1 instanceof ClientboundEntityPositionSyncPacket var5 && var5.id() == this.o.getId()) {
            var2 = W(var5);
         }

         if (var2 != null) {
            if (this.co == null) {
               this.co = new VecDeltaCodec();
            }

            this.co.setBase(var2);
            AABB var6 = this.o.getBoundingBox().move(var2.subtract(this.o.position()));
            if (!this.L(var6) && this.X.P()) {
               Vec3 var7 = mc.player.position();
               if (D(var7, var6) < D(var7, this.o.getBoundingBox())) {
                  this.n(true, "Target moved closer");
               }
            }
         }
      }
   }

   private boolean z() {
      return this.o != null && this.o.isAlive() && this.o(this.o);
   }

   private boolean o(Entity var1) {
      if (mc.player != null && var1 instanceof Player && var1 != mc.player) {
         double var2 = D(mc.player.position(), var1.getBoundingBox());
         double var4 = Math.min(this.q.P(), this.r.P());
         double var6 = Math.max(this.q.P(), this.r.P());
         return var2 >= var4 * var4 && var2 <= var6 * var6;
      } else {
         return false;
      }
   }

   private void N(Entity var1) {
      if (this.o(var1)) {
         if (var1 != this.o) {
            this.H(true, false, "Target changed");
            this.co = new VecDeltaCodec();
            this.co.setBase(var1.position());
            mt.U("Backtrack", "New target {}", var1.getName().getString());
         }

         this.o = var1;
      }
   }

   private void H(boolean var1, boolean var2, String var3) {
      if (var2) {
         this.L(var3);
      } else if (var1) {
         this.n(true, var3);
      }

      this.o = null;
      this.co = null;
   }

   private void n(boolean var1, String var2) {
      if (!this.ca && !this.cE.isEmpty()) {
         if (mc.getConnection() != null && this.E()) {
            long var3 = y();
            long var5 = this.C.P().longValue();
            int var7 = 0;
            this.ca = (boolean)1;

            try {
               while (!this.cE.isEmpty() && (var1 || var3 - this.w.peekFirst() >= var5) && this.E()) {
                  this.w.removeFirst();
                  this.cE.removeFirst().run();
                  var7++;
               }

               if (!this.E()) {
                  this.L("Session changed during release");
               }
            } catch (RuntimeException | Error var12) {
               this.cE.clear();
               this.w.clear();
               throw var12;
            } finally {
               this.ca = (boolean)0;
            }

            if (var7 > 0 && var2 != null) {
               mt.U("Backtrack", "Released {} held updates: {}", var7, var2);
            }
         } else {
            this.L(var2 == null ? "Session changed" : var2 + "; session changed");
         }
      }
   }

   private void L(String var1) {
      int var2 = this.cE.size();
      this.cE.clear();
      this.w.clear();
      if (var2 > 0 && var1 != null) {
         mt.U("Backtrack", "Discarded {} held updates: {}", var2, var1);
      }
   }

   static long d(d9 var0, d9 var1) {
      int var2 = Math.round((float)Math.min(var0.P(), var1.P()));
      int var3 = Math.round((float)Math.max(var0.P(), var1.P()));
      return var3 > var2 ? ThreadLocalRandom.current().nextInt(var2, var3 + 1) : Math.max(0, var2);
   }

   static double t(AABB var0, AABB var1) {
      double var2 = Math.max(0.0, Math.max(var0.minX - var1.maxX, var1.minX - var0.maxX));
      double var4 = Math.max(0.0, Math.max(var0.minY - var1.maxY, var1.minY - var0.maxY));
      double var6 = Math.max(0.0, Math.max(var0.minZ - var1.maxZ, var1.minZ - var0.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   static double D(Vec3 var0, AABB var1) {
      return var0.distanceToSqr(
         new Vec3(Mth.clamp(var0.x, var1.minX, var1.maxX), Mth.clamp(var0.y, var1.minY, var1.maxY), Mth.clamp(var0.z, var1.minZ, var1.maxZ))
      );
   }

   private boolean Y(AABB var1) {
      if (var1 != null && mc.player != null && this.cq.P()) {
         double var2 = this.ch.P();
         return D(mc.player.getEyePosition(), var1) > var2 * var2;
      } else {
         return false;
      }
   }

   private boolean L(AABB var1) {
      if (!this.Y(var1)) {
         return false;
      }

      String var2 = String.format(Locale.ROOT, "Target out of reach (%.1f blocks)", Math.sqrt(D(mc.player.getEyePosition(), var1)));
      if (this.U.u() + this.cE.size() > 0) {
         this.H(var1);
      }

      if (this.m.S("New")) {
         this.H(true, false, var2);
      } else {
         this.v(var2);
         this.L(false, false, true, null);
      }

      return true;
   }

   private AABB M() {
      if (this.o == null) {
         return null;
      } else if (this.m.S("New")) {
         return this.co == null ? null : this.o.getBoundingBox().move(this.co.getBase().subtract(this.o.position()));
      } else {
         return this.o.isAlive() && !this.d.getBase().equals(Vec3.ZERO) ? this.o.getType().getDimensions().makeBoundingBox(this.d.getBase()) : null;
      }
   }

   private void H(AABB var1) {
      this.c4 = new Vec3((var1.minX + var1.maxX) / 2.0, var1.minY, (var1.minZ + var1.maxZ) / 2.0);
      this.cY = Math.max(var1.getXsize(), var1.getZsize()) / 2.0;
      this.L = var1.getYsize();
      this.a = ty.K();
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
      return !this.isEnabled() ? false : this.f && this.u;
   }

   @Override
   public void clear() {
      this.f = (boolean)0;
      this.c5 = this.c4 = null;
      this.A = null;
      this.cV = null;
      this.Z = -1;
      this.x = this.V = 0.0;
      this.p = -1.0;
      this.a = -10.0;
      this.cz.y();
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.f = (boolean)0;
         this.A = null;
         this.cz.y();
         if (this.isInGame() && !this.R.S("None")) {
            double var3 = ty.K();
            double var5 = ty.N();
            this.u = this.T.P();
            this.s = this.cO.P();
            this.h = this.F.P();
            this.p = this.c4 != null && var3 - this.a < 0.55 ? Math.max(0.0, var3 - this.a) / 0.55 : -1.0;
            AABB var7 = this.M();
            if (var7 == null) {
               this.c5 = null;
               this.Z = -1;
               this.x = 0.0;
               this.f = (boolean)(this.p >= 0.0 ? 1 : 0);
            } else {
               Vec3 var8 = new Vec3((var7.minX + var7.maxX) / 2.0, var7.minY, (var7.minZ + var7.maxZ) / 2.0);
               int var9 = this.c5 != null && this.Z == this.o.getId() && this.cV == mc.level && this.c5.distanceToSqr(var8) < 16.0 ? 1 : 0;
               this.c5 = var9 != 0 ? this.c5.lerp(var8, 1.0 - Math.exp(-var5 * 24.0)) : var8;
               this.Z = this.o.getId();
               this.cV = mc.level;
               this.cj = var7.getXsize() / 2.0;
               this.y = var7.getZsize() / 2.0;
               this.P = var7.getYsize();
               this.x = this.x + ((this.U.u() + this.cE.size() > 0 ? 1 : 0) - this.x) * (1.0 - Math.exp(-var5 * 10.0));
               Vec3 var10 = this.o.getPosition(var1);
               this.V = hp.R((var10.distanceTo(this.c5) - 0.05) / 0.45);
               if (this.i.P() && this.V > 0.01) {
                  EntityRenderState var11 = tj.s(this.o, var1);
                  var11.x = this.c5.x;
                  var11.y = this.c5.y;
                  var11.z = this.c5.z;
                  var11.nameTag = null;
                  var11.scoreText = null;
                  var11.leashStates = null;
                  var11.displayFireAnimation = (boolean)0;
                  var11.shadowPieces.clear();
                  var11.outlineColor = 0;
                  this.A = var11;
               }

               if (this.cx.P() && this.V > 0.01) {
                  Vec3 var22 = var10.add(0.0, this.P * 0.55, 0.0);
                  Vec3 var12 = this.c5.add(0.0, this.P * 0.55, 0.0);
                  double var13 = Math.min(0.3, var22.distanceTo(var12) * 0.12);

                  for (int var15 = 0; var15 <= 24; var15++) {
                     double var16 = var15 / 24.0;
                     double var18 = Math.pow(0.5 + 0.5 * Math.cos((var16 * 2.5 - var3 * 1.6) * Math.PI * 2.0), 6.0);
                     double var20 = hp.R(var16 / 0.12) * hp.R((1.0 - var16) / 0.12);
                     this.cz
                        .y(
                           var22.lerp(var12, var16).add(0.0, Math.sin(Math.PI * var16) * var13, 0.0),
                           0.03 * (1.0 + 0.7 * var18) * (0.35 + 0.65 * var20),
                           _3.U(_3.D(this.s, this.h, var16), this.V * (0.3 + 0.7 * var18))
                        );
                  }

                  this.cz.a(var2.pos);
               }

               this.f = (boolean)1;
            }
         } else {
            this.clear();
         }
      }
   }

   @Override
   public void render(ki var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.f && (!var2 || this.u)) {
            long var3 = kx.G();
            double var5 = ty.K();
            if (this.c5 != null) {
               double var7 = 0.65 + 0.35 * this.x;
               AABB var9 = new AABB(this.c5.x - this.cj, this.c5.y, this.c5.z - this.y, this.c5.x + this.cj, this.c5.y + this.P, this.c5.z + this.y)
                  .inflate(0.03);
               if (this.A != null) {
                  var1.Z(this.A, tj.b.g, _3.D(this.s, this.h, 0.4), 0.55 * this.V * (var2 ? 0.5 : 1.0));
               }

               var1.blend(pf.x.E);
               if (!this.R.S("Fill")) {
                  this.E(var1, var9, var5, var7);
               }

               if (!this.R.S("Wireframe")) {
                  this.K(var1, var9, var5, var7);
               }

               this.j(var1, var9, var5, var7);
               this.r(var1, var9, var5, var7, var2);
               if (this.cz.h() > 1) {
                  var1.M(this.cz, true);
               }

               var1.blend(pf.x.S);
               if (!var2 && !this.R.S("Wireframe")) {
                  this.k(var1, var9, var7);
               }
            }

            if (this.p >= 0.0) {
               var1.blend(pf.x.E);
               this.x(var1, this.p);
               var1.blend(pf.x.S);
            }

            kx.u("Backtrack ESP", var3);
         }
      }
   }

   private static Vec3 o(AABB var0, int var1) {
      return new Vec3((var1 & 1) == 0 ? var0.minX : var0.maxX, (var1 & 2) == 0 ? var0.minY : var0.maxY, (var1 & 4) == 0 ? var0.minZ : var0.maxZ);
   }

   private void E(ki var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();

      for (int[] var11 : O) {
         Vec3 var12 = o(var2, var11[0]);
         Vec3 var13 = o(var2, var11[1]);

         for (int var14 = 0; var14 < 6; var14++) {
            Vec3 var15 = var12.lerp(var13, var14 / 6.0);
            Vec3 var16 = var12.lerp(var13, (var14 + 1) / 6.0);
            var1.R(
               var15.x, var15.y, var15.z, var16.x, var16.y, var16.z, 0.045, 0.045, this.p(var2, var7, var15, var3, var5), this.p(var2, var7, var16, var3, var5)
            );
         }
      }
   }

   private int p(AABB var1, Vec3 var2, Vec3 var3, double var4, double var6) {
      double var8 = Math.clamp((var3.y - var1.minY) / Math.max(1.0E-6, var1.getYsize()), 0.0, 1.0);
      double var10 = Math.atan2(var3.z - var2.z, var3.x - var2.x);
      double var12 = Math.pow(0.5 + 0.5 * Math.cos(var10 * 2.0 + var8 * 4.0 - var4 * 3.2), 10.0);
      int var14 = _3.D(this.s, this.h, var8);
      return _3.U(_3.D(var14, var14 | 16777215, var12 * 0.6), var6 * (0.6 + 0.4 * var12));
   }

   private void K(ki var1, AABB var2, double var3, double var5) {
      double var7 = var3 * 1.6;
      double var9 = 0.5 - 0.5 * Math.cos(var7);
      double var11 = Math.sin(var7);

      for (int var13 = 0; var13 < 3; var13++) {
         double var14 = Math.clamp(var9 - Math.signum(var11) * var13 * 0.07, 0.0, 1.0);
         double var16 = var13 == 0 ? 0.85 : 0.4 / var13 * Math.abs(var11);
         int var18 = _3.U(_3.D(this.s, this.h, var14), var16 * var5);
         double var19 = var2.minY + var2.getYsize() * var14;
         double var21 = var13 == 0 ? 0.04 : 0.025;
         var1.R(var2.minX, var19, var2.minZ, var2.maxX, var19, var2.minZ, var21, var21, var18, var18);
         var1.R(var2.maxX, var19, var2.minZ, var2.maxX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.R(var2.maxX, var19, var2.maxZ, var2.minX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.R(var2.minX, var19, var2.maxZ, var2.minX, var19, var2.minZ, var21, var21, var18, var18);
      }
   }

   private void j(ki var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();
      double var8 = Math.max(var2.getXsize(), var2.getZsize()) * 0.75;
      double var10 = var2.minY + 0.02;
      double var12 = var3 / 1.3 - Math.floor(var3 / 1.3);
      var1.H(var7.x, var10, var7.z, var8 * (1.0 + 0.04 * Math.sin(var3 * 3.0)), 0.14, _3.U(this.h, 0.7 * var5));
      var1.H(var7.x, var10, var7.z, var8 * (1.0 + var12 * 1.1), 0.1, _3.U(this.s, 0.5 * var5 * (1.0 - var12) * (1.0 - var12)));
   }

   private void r(ki var1, AABB var2, double var3, double var5, boolean var7) {
      for (int var8 = 0; var8 < 8; var8++) {
         Vec3 var9 = o(var2, var8);
         double var10 = 0.5 + 0.5 * Math.sin(var3 * 4.5 + var8 * 1.9);
         int var12 = _3.D((var8 & 2) == 0 ? this.s : this.h, -1, 0.45);
         var1.W(var9.x, var9.y, var9.z, var7 ? 0.22 : 0.16, 0.0, _3.U(var12, (var7 ? 0.35 : 0.2) * var5), cg.u.a, 0.0, 0.0, 0.0);
         if (!var7) {
            var1.t(var9.x, var9.y, var9.z, 0.05 + 0.035 * var10, var3 * 0.8 + var8, _3.U(var12, var5 * (0.55 + 0.45 * var10)));
         }
      }
   }

   private void k(ki var1, AABB var2, double var3) {
      double var5 = this.R.S("Fill") ? 1.8 : 1.0;
      int var7 = _3.U(this.s, 0.16 * var5 * var3);
      int var8 = _3.U(this.h, 0.035 * var5 * var3);
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

   private void x(ki var1, double var2) {
      double var4 = 1.0 - Math.pow(1.0 - var2, 3.0);
      double var6 = Math.pow(1.0 - var2, 1.5);
      Vec3 var8 = this.c4;
      double var9 = this.cY * 1.5 * (1.0 + var4 * 2.2);
      double var11 = var8.y + 0.03;
      var1.H(var8.x, var11, var8.z, var9, 0.05 + 0.22 * (1.0 - var2), _3.U(this.s, 0.9 * var6));
      var1.H(var8.x, var11, var8.z, var9 * 0.7, 0.12, _3.U(this.h, 0.6 * var6));
      AABB var13 = new AABB(var8.x - this.cY, var8.y, var8.z - this.cY, var8.x + this.cY, var8.y + this.L, var8.z + this.cY).inflate(0.35 * var4);
      int var14 = _3.U(_3.D(this.s, this.h, 0.5), var6 * var6);

      for (int[] var18 : O) {
         Vec3 var19 = o(var13, var18[0]);
         Vec3 var20 = o(var13, var18[1]);
         var1.R(var19.x, var19.y, var19.z, var20.x, var20.y, var20.z, 0.05, 0.05, var14, var14);
      }

      int var23 = _3.U(_3.D(this.h, -1, 0.5), var6);

      for (int var24 = 0; var24 < 12; var24++) {
         double var25 = var24 * Math.PI / 6.0 + var2;
         double var26 = Math.cos(var25);
         double var21 = Math.sin(var25);
         var1.W(
            var8.x + var26 * var9,
            var11 + 0.1 + var24 % 2 * var4 * 0.4,
            var8.z + var21 * var9,
            0.02 + 0.06 * (1.0 - var2),
            var25,
            var23,
            cg.u.M,
            var26,
            0.0,
            var21
         );
      }
   }

   private void Z() {
      if (this.isRuntimeActive() && this.isInGame() && this.o != null && !this.R.S("None")) {
         int var1 = com.larpclient.h.V() && com.larpclient.h.R(this.effectName()) ? 1 : 0;
         AABB var2 = var1 != 0 && this.c5 != null && this.Z == this.o.getId()
            ? new AABB(this.c5.x - this.cj, this.c5.y, this.c5.z - this.y, this.c5.x + this.cj, this.c5.y + this.P, this.c5.z + this.y)
            : this.M();
         if (var2 != null) {
            int var3 = this.cO.P() & 16777215;
            if (var1 == 0) {
               int var4 = this.R.S("Fill") ? 0 : -520093696 | var3;
               int var5 = this.R.S("Wireframe") ? 0 : 754974720 | var3;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var4, 1.0F, var5)).setAlwaysOnTop();
            } else if (this.Y.P()) {
               int var6 = this.R.S("Fill") ? 0 : 1174405120 | var3;
               int var7 = this.R.S("Fill") ? 503316480 | var3 : 0;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var6, 1.0F, var7)).setAlwaysOnTop();
            }
         }
      }
   }

   private static boolean B(ClientboundRemoveEntitiesPacket var0, int var1) {
      return var0.entityIds().contains(var1);
   }

   private static Vec3 N(VecDeltaCodec var0, ClientboundMoveEntityPacket var1) {
      return var1.getPositionDelta().decode(var0).endPosition();
   }

   private static Vec3 W(ClientboundEntityPositionSyncPacket var0) {
      return var0.position().endPosition();
   }
}
