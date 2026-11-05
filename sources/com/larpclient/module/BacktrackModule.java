package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.ea;
import com.larpclient.f4;
import com.larpclient.f8;
import com.larpclient.fk;
import com.larpclient.h3;
import com.larpclient.h_;
import com.larpclient.oh;
import com.larpclient.os;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.u2;
import com.larpclient.uv;
import com.larpclient.yc;
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

public final class BacktrackModule extends Module implements u2 {
   private static BacktrackModule j;
   private static final int g = 65536;
   private static final long W = System.nanoTime();
   private final lp rL = this.addSetting(new lp("Version", "Packet delay algorithm", "New", "Old", "New"));
   private final lc rq = this.addSetting(new lc("Range Min", "Lower bound of the box distance to the target", 1.0, 0.0, 10.0, 0.1));
   private final lc a = this.addSetting(new lc("Range Max", "Upper bound of the box distance to the target", 3.0, 0.0, 10.0, 0.1));
   private final lc B = this.addSetting(new lc("Delay Min", "Shortest hold window in milliseconds", 280.0, 0.0, 2000.0, 1.0));
   private final lc rZ = this.addSetting(new lc("Delay Max", "Longest hold window in milliseconds", 480.0, 0.0, 2000.0, 1.0));
   private final lc p = this.addSetting(new lc("Next Backtrack Delay Min", "Shortest wait before the next window in milliseconds", 0.0, 0.0, 2000.0, 1.0));
   private final lc rP = this.addSetting(new lc("Next Backtrack Delay Max", "Longest wait before the next window in milliseconds", 10.0, 0.0, 2000.0, 1.0));
   private final lc rT = this.addSetting(
      new lc("Tracking Buffer", "Keep windows available after the target leaves range, in milliseconds", 500.0, 0.0, 2000.0, 1.0)
   );
   private final lc A = this.addSetting(new lc("Chance", "Chance that an attack allows windows, in percent", 50.0, 0.0, 100.0, 1.0));
   private final ln H = this.addSetting(new ln("Pause On Hurt Time", "Skip windows while the target is hurt", false));
   private final lc o = this.addSetting(new lc("Hurt Time", "Target hurt time that pauses windows", 3.0, 0.0, 10.0, 1.0));
   private final lp h = this.addSetting(new lp("Target Mode", "Use the attacked entity or the nearest one in range", "Attack", "Attack", "Range"));
   private final lc e = this.addSetting(
      new lc("Time Since Last Attack", "How long an attack keeps windows available, in milliseconds", 1000.0, 0.0, 5000.0, 1.0)
   );
   private final ln m = this.addSetting(new ln("Release Out Of Reach", "Release held packets as soon as the target's server position is too far to hit", true));
   private final lc i = this.addSetting(
      new lc("Release Distance", "Server position distance from your eyes that releases held packets. Vanilla ignores hits past 6", 6.0, 1.0, 10.0, 0.1)
   );
   private final lp u = this.addSetting(new lp("ESP", "Draw the target's server position", "Box", "Box", "Wireframe", "Fill", "None"));
   private final lv I = this.addSetting(new lv("Color", "Server position box color", -1));
   private final lv w = this.addSetting(new lv("Gradient", "Second ESP color, blended toward the top", -6862849));
   private final ln K = this.addSetting(new ln("Ghost Model", "Draw a hologram of the target at its server position", true));
   private final ln V = this.addSetting(new ln("Tether", "Link the displayed target to its server position", true));
   private final ln c = this.addSetting(new ln("Bloom", "Glow pass for the ESP", true));
   private final ln T = this.addSetting(new ln("Through Walls", "Keep a faint outline visible behind blocks", true));
   private final lc Z = this.addSetting(new lc("New Range Min", "Lower bound of the distance to the target player", 0.0, 0.0, 10.0, 0.01));
   private final lc Y = this.addSetting(new lc("New Range Max", "Upper bound of the distance to the target player", 10.0, 0.0, 10.0, 0.01));
   private final lc rX = this.addSetting(new lc("New Delay", "How long each held update waits, in milliseconds", 15000.0, 0.0, 15000.0, 1.0));
   private final ln v = this.addSetting(new ln("Only Attack", "Release held updates when the target moves closer", true));
   private Entity y;
   private String b = "Old";
   private final os x = new os(65536);
   private final ArrayDeque<Runnable> G = new ArrayDeque<>();
   private final VecDeltaCodec n = new VecDeltaCodec();
   private boolean P;
   private boolean rf;
   private long S;
   private long D;
   private long rI;
   private int U;
   private final ArrayDeque<Runnable> r6 = new ArrayDeque<>();
   private final ArrayDeque<Long> k = new ArrayDeque<>();
   private VecDeltaCodec ro;
   private boolean E;
   private ClientPacketListener L;
   private ClientLevel d;
   private volatile int rj;
   private static final double rd = 0.55;
   private static final int[][] q = new int[][]{{0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
   private final h3.b X = new h3.b(25);
   private boolean rY;
   private boolean N;
   private Object f;
   private int rx = -1;
   private int t;
   private int C;
   private Vec3 M;
   private double J;
   private double F;
   private double s;
   private double R;
   private double z;
   private double rn = -1.0;
   private EntityRenderState O;
   private Vec3 r;
   private double rr;
   private double Q;
   private double l = -10.0;

   public BacktrackModule() {
      super("Backtrack", "Backtracks player position to a hittable state", Category.COMBAT, -1);
      j = this;
      this.rq.A(this.a).b("Range");
      this.B.A(this.rZ).b("Delay");
      this.p.A(this.rP).b("Next Backtrack Delay");
      this.Z.A(this.Y).b("New Range");
      this.rT.q("ms");
      this.A.q("%");
      this.o.q("");
      this.e.q("ms").b("Last Attack Time To Work");
      this.rX.q("ms");

      for (l6 var4 : new l6[]{this.rq, this.a, this.B, this.rZ, this.p, this.rP, this.rT, this.A, this.H, this.h, this.e}) {
         var4.x(() -> this.rL.i("Old"));
      }

      this.o.x(() -> this.rL.i("Old") && this.H.getValue());
      this.i.x(this.m::getValue);

      for (l6 var11 : new l6[]{this.I, this.w, this.K, this.V, this.c, this.T}) {
         var11.x(() -> !this.u.i("None"));
      }

      for (l6 var12 : new l6[]{this.Z, this.Y, this.rX, this.v}) {
         var12.x(() -> this.rL.i("New"));
      }

      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.D());
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      }

      if (this.y == null) {
         return "Waiting for target";
      }

      int var1 = this.x.I() + this.r6.size();
      return "Tracking " + this.y.getName().getString() + (var1 > 0 ? " · " + var1 + " held" : "");
   }

   @Override
   public void onEnable() {
      this.b = this.rL.getValue();
      this.U = ThreadLocalRandom.current().nextInt(0, 101);
      this.e(false, false, false, null);
      this.t(false, false, null);
      this.clear();
      this.r();
   }

   @Override
   public void onDisable() {
      this.rj = 0;
      this.e(true, false, true, "Stopped");
      this.t(true, false, "Stopped");
      this.clear();
   }

   private void r() {
      this.rj = !this.isRuntimeActive() ? 0 : (this.rL.i("Old") ? (this.y == null && this.x.I() <= 0 ? 0 : 1) : (!this.r6.isEmpty() ? 2 : 0));
   }

   public static void C(Packet<?> var0, PacketListener var1) {
      if (j != null && j.isEnabled()) {
         BacktrackModule var2 = j;
         if (var2 != null && var2.rj != 0 && var1 instanceof ClientPacketListener var3) {
            Minecraft var4 = getMc();
            if (var4 != null && !var4.isSameThread()) {
               if (var2.rj == 1 ? B(var0) : var0 instanceof ClientboundDisconnectPacket) {
                  PacketUtils.ensureRunningOnSameThread(var0, var3, var4.packetProcessor());
               }
            }
         }
      }
   }

   static boolean B(Packet<?> var0) {
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

   public static boolean h() {
      BacktrackModule var0 = j;
      return var0 != null && var0.x.I() > 0;
   }

   private static long C() {
      return (System.nanoTime() - W) / 1000000L;
   }

   private void a() {
      String var1 = this.rL.getValue();
      if (!var1.equals(this.b)) {
         this.e(true, false, false, "Version changed");
         this.t(true, false, "Version changed");
         this.b = var1;
      }
   }

   private boolean s() {
      Minecraft var1 = getMc();
      return this.L != null && var1.getConnection() == this.L && var1.level == this.d && this.L.getConnection().isConnected();
   }

   private void O() {
      if (this.x.I() <= 0 && this.G.isEmpty() && this.r6.isEmpty()) {
         this.L = mc.getConnection();
         this.d = mc.level;
      }
   }

   public static boolean G() {
      BacktrackModule var0 = j;
      return var0 != null && (var0.x.g() || var0.E || var0.rf);
   }

   public static boolean a(Packet<?> var0, PacketListener var1, Runnable var2) {
      if (j != null && j.isEnabled()) {
         BacktrackModule var3 = j;
         Minecraft var4 = getMc();
         if (var3 != null
            && !G()
            && var4.isSameThread()
            && !(var0 instanceof ClientboundBundlePacket)
            && var1 instanceof ClientPacketListener
            && var1 == var4.getConnection()
            && var3.isRuntimeActive()) {
            try {
               var3.a();
               return var3.rL.i("New") ? var3.m(var0, var2) : var3.I(var0, var2);
            } finally {
               var3.r();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean P(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (j != null && j.isEnabled()) {
         BacktrackModule var4 = j;
         Minecraft var5 = getMc();
         if (var4 != null
            && !G()
            && var5 != null
            && var5.isSameThread()
            && var4.isRuntimeActive()
            && var5.player != null
            && var5.level != null
            && var5.getConnection() != null
            && !var5.isLocalServer()
            && var0 == var5.getConnection().getConnection()) {
            var4.a();
            if (!var4.rL.i("New") && var1 instanceof ServerboundPongPacket && var4.x.I() != 0) {
               var4.G.addLast(() -> var0.send(var1, var2, var3));
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

   public static boolean u() {
      BacktrackModule var0 = j;
      return var0 != null && var0.rf;
   }

   public static void F(Entity var0) {
      if (j != null && j.isEnabled()) {
         BacktrackModule var1 = j;
         if (var1 != null && var0 != null && var1.isRuntimeActive() && var1.isInGame()) {
            try {
               var1.a();
               if (!var1.rL.i("New")) {
                  var1.D = C();
                  var1.U = ThreadLocalRandom.current().nextInt(0, 101);
                  if (var1.h.i("Attack")) {
                     var1.M(var0);
                  }

                  return;
               }

               var1.E(var0);
            } finally {
               var1.r();
            }
         }
      }
   }

   @Override
   public void onTick() {
      try {
         this.y();
      } finally {
         this.r();
      }
   }

   private void y() {
      this.a();
      if (this.rL.i("New")) {
         this.p();
      } else if (mc.player != null && mc.level != null) {
         if (this.h.i("Range")) {
            Entity var1 = this.E();
            if (var1 == null) {
               this.e(true, false, true, "No target in range");
            } else {
               this.M(var1);
            }
         }

         int var2 = this.x.I() > 0 ? 1 : 0;
         if (var2 == 0 || !this.E(this.Y())) {
            if (this.E()) {
               if (var2 != 0 && this.x.f(C())) {
                  this.E("Window ended");
                  this.e(false, false, true, null);
               }
            } else if (var2 != 0) {
               this.E("Target no longer eligible");
               this.e(false, false, true, null);
            }
         }
      } else {
         this.e(false, true, false, "World unavailable");
      }
   }

   private boolean I(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.level == null || mc.getConnection() == null || mc.isLocalServer()) {
         return false;
      }

      if (J(var1)) {
         return false;
      }

      if (this.I(var1)) {
         this.e(true, false, true, "Server boundary packet");
         return false;
      }

      boolean var3 = this.E();
      int var4 = this.x.I() > 0 ? 1 : 0;
      if (var4 == 0 && !var3) {
         return false;
      }

      Entity var5 = this.y;
      if (var4 == 0 && !(var1 instanceof ClientboundPingPacket)) {
         this.b(var1, var5);
         return false;
      }

      if (var5 != null) {
         Vec3 var6 = this.b(var1, var5);
         if (var6 != null && this.E(var5.getType().getDimensions().makeBoundingBox(var6))) {
            return false;
         }

         if (var6 != null && this.d(var5, var6)) {
            this.E("Target moved closer");
            return false;
         }
      }

      if (var4 == 0) {
         if (var5 != null && this.c(this.Y())) {
            return false;
         }

         this.O();
         long var8 = b(this.B, this.rZ);
         if (this.x.D()) {
            this.x.Q();
         }

         this.x.e(C(), var8);
         r4.g("Backtrack", "Old window opened for {} ms", var8);
      }

      if (this.x.I(var2)) {
         return true;
      }

      this.E("Packet capacity reached");
      return false;
   }

   private static boolean J(Packet<?> var0) {
      return var0 instanceof ClientboundPlayerChatPacket
         || var0 instanceof ClientboundSystemChatPacket
         || var0 instanceof ClientboundDisguisedChatPacket
         || var0 instanceof ClientboundSetActionBarTextPacket
         || var0 instanceof ClientboundSoundPacket var1 && var1.getSound().value() == SoundEvents.PLAYER_HURT;
   }

   private boolean I(Packet<?> var1) {
      return var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F
         || var1 instanceof ClientboundRemoveEntitiesPacket var2 && this.y != null && M(var2, this.y.getId());
   }

   private Vec3 b(Packet<?> var1, Entity var2) {
      return var2 != null && mc.level != null ? X(this.n, var1, mc.level, var2) : null;
   }

   static Vec3 X(VecDeltaCodec var0, Packet<?> var1, ClientLevel var2, Entity var3) {
      Vec3 var4 = null;
      if (var1 instanceof ClientboundMoveEntityPacket var5 && var5.hasPosition() && var5.getEntity(var2) == var3) {
         var4 = X(var0, var5);
      } else if (var1 instanceof ClientboundTeleportEntityPacket var6 && var6.id() == var3.getId()) {
         var4 = var6.relatives().isEmpty()
            ? var6.change().position()
            : PositionMoveRotation.calculateAbsolute(
                  new PositionMoveRotation(var0.getBase(), var3.getDeltaMovement(), var3.getYRot(), var3.getXRot()), var6.change(), var6.relatives()
               )
               .position();
      } else if (var1 instanceof ClientboundEntityPositionSyncPacket var7 && var7.id() == var3.getId()) {
         var4 = C(var7);
      }

      if (var4 != null) {
         var0.setBase(var4);
      }

      return var4;
   }

   private boolean d(Entity var1, Vec3 var2) {
      AABB var3 = var1.getType().getDimensions().makeBoundingBox(var2);
      AABB var4 = mc.player.getBoundingBox();
      return s(var4, var3) < s(var4, var1.getBoundingBox());
   }

   private boolean E() {
      return this.y != null && this.y.isAlive() && this.J(this.y);
   }

   private boolean J(Entity var1) {
      if (mc.player != null && var1 != null) {
         boolean var2 = this.X(var1);
         long var3 = C();
         if (var2) {
            this.S = var3;
         }

         return (var2 || this.S != 0L && var3 - this.S <= this.rT.getValue().longValue())
            && this.x(var1)
            && mc.player.tickCount > 10
            && this.U < this.A.getValue()
            && var3 >= this.rI
            && (!this.H.getValue() || !this.P)
            && this.D != 0L
            && var3 - this.D <= this.e.getValue().longValue();
      } else {
         return false;
      }
   }

   private boolean x(Entity var1) {
      if (!(var1 instanceof LivingEntity var2 && mc.player != null && var1 != mc.player)) {
         return false;
      } else {
         if (!var2.isAlive() || var2.isRemoved() || var2.isSpectator() || !var2.isPickable()) {
            return false;
         }

         if (!(var1 instanceof Player)) {
            return true;
         }

         MCFriendModule var3 = LarpClient.y().e().y(MCFriendModule.class);
         return var3 == null || !var3.e(var1.getName().getString());
      }
   }

   private boolean X(Entity var1) {
      double var2 = Math.sqrt(s(mc.player.getBoundingBox(), var1.getBoundingBox()));
      return var2 >= Math.min(this.rq.getValue(), this.a.getValue()) && var2 <= Math.max(this.rq.getValue(), this.a.getValue());
   }

   private Entity E() {
      double var1 = Math.min(this.rq.getValue(), this.a.getValue());
      double var3 = Math.max(this.rq.getValue(), this.a.getValue());
      AABB var5 = mc.player.getBoundingBox();
      Entity var6 = null;
      double var7 = Double.MAX_VALUE;

      for (Entity var10 : mc.level.getEntities(mc.player, var5.inflate(var3 + 1.0), this::x)) {
         double var11 = Math.sqrt(s(var5, var10.getBoundingBox()));
         if (var11 >= var1 && var11 <= var3 && var11 < var7) {
            var6 = var10;
            var7 = var11;
         }
      }

      return var6;
   }

   private void M(Entity var1) {
      this.P = (boolean)(var1 instanceof LivingEntity var2 && var2.hurtTime >= this.o.getValue() ? 1 : 0);
      if (this.J(var1)) {
         if (var1 != this.y) {
            this.e(true, false, false, "Target changed");
            this.n.setBase(var1.getPositionCodec().getBase());
         }

         this.y = var1;
      }
   }

   private void e(boolean var1, boolean var2, boolean var3, String var4) {
      if (var1 && !var2) {
         this.E(var4);
      } else if (var2) {
         this.a(var4);
      }

      if (this.y != null && var3) {
         this.rI = C() + b(this.p, this.rP);
      }

      this.y = null;
      this.P = (boolean)0;
      this.n.setBase(Vec3.ZERO);
   }

   private void E(String var1) {
      int var2 = this.x.I() + this.G.size();
      if (var2 == 0) {
         this.x.Q();
      } else if (mc.getConnection() != null && this.s()) {
         this.rf = (boolean)1;

         try {
            while (!this.G.isEmpty() && this.s()) {
               this.G.removeFirst().run();
            }
         } finally {
            this.rf = (boolean)0;
            this.G.clear();
         }

         this.x.Q(this::s);
         if (var1 != null) {
            r4.g("Backtrack", "Released {} held packets: {}", var2, var1);
         }
      } else {
         this.a(var1 + "; session changed");
      }
   }

   private void a(String var1) {
      int var2 = this.x.I() + this.G.size();
      this.x.Q();
      this.G.clear();
      if (var2 > 0 && var1 != null) {
         r4.g("Backtrack", "Discarded {} held packets: {}", var2, var1);
      }
   }

   private void p() {
      if (mc.player != null && mc.level != null) {
         if (!this.w()) {
            this.t(true, false, "Target left New range");
         } else if (!this.E(this.Y())) {
            this.k(false, null);
         }
      } else {
         this.t(true, true, "World unavailable");
      }
   }

   private boolean m(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.level == null) {
         this.t(true, true, "World unavailable");
         return false;
      }

      if (var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F) {
         this.t(true, false, "Server boundary packet");
         return false;
      }

      if (this.r6.isEmpty() && !this.w()) {
         return false;
      }

      this.C(var1);
      if (this.y == null || o(var1) || !H(var1)) {
         return false;
      }

      if (this.r6.size() >= 65536) {
         this.k(true, "Packet capacity reached");
         return false;
      }

      if (this.r6.isEmpty()) {
         this.O();
         r4.g("Backtrack", "New queue opened with {} ms delay", this.rX.getValue().longValue());
      }

      this.r6.addLast(var2);
      this.k.addLast(C());
      return true;
   }

   private static boolean o(Packet<?> var0) {
      return var0 instanceof ClientboundSoundPacket || var0 instanceof ClientboundSetHealthPacket || var0 instanceof ClientboundSetEntityDataPacket;
   }

   private static boolean H(Packet<?> var0) {
      return var0 instanceof ClientboundPingPacket
         || var0 instanceof ClientboundRotateHeadPacket
         || var0 instanceof ClientboundTeleportEntityPacket
         || var0 instanceof ClientboundEntityPositionSyncPacket
         || var0 instanceof ClientboundSetEntityMotionPacket
         || var0 instanceof ClientboundMoveEntityPacket
         || var0 instanceof ClientboundLevelParticlesPacket
         || var0 instanceof ClientboundExplodePacket;
   }

   private void C(Packet<?> var1) {
      if (mc.player != null && mc.level != null && this.y != null) {
         Vec3 var2 = null;
         if (var1 instanceof ClientboundMoveEntityPacket var3 && var3.hasPosition() && var3.getEntity(mc.level) == this.y && this.ro != null) {
            var2 = X(this.ro, var3);
         } else if (var1 instanceof ClientboundTeleportEntityPacket var4 && var4.id() == this.y.getId()) {
            var2 = var4.change().position();
         } else if (var1 instanceof ClientboundEntityPositionSyncPacket var5 && var5.id() == this.y.getId()) {
            var2 = C(var5);
         }

         if (var2 != null) {
            if (this.ro == null) {
               this.ro = new VecDeltaCodec();
            }

            this.ro.setBase(var2);
            AABB var6 = this.y.getBoundingBox().move(var2.subtract(this.y.position()));
            if (!this.E(var6) && this.v.getValue()) {
               Vec3 var7 = mc.player.position();
               if (B(var7, var6) < B(var7, this.y.getBoundingBox())) {
                  this.k(true, "Target moved closer");
               }
            }
         }
      }
   }

   private boolean w() {
      return this.y != null && this.y.isAlive() && this.H(this.y);
   }

   private boolean H(Entity var1) {
      if (mc.player != null && var1 instanceof Player && var1 != mc.player) {
         double var2 = B(mc.player.position(), var1.getBoundingBox());
         double var4 = Math.min(this.Z.getValue(), this.Y.getValue());
         double var6 = Math.max(this.Z.getValue(), this.Y.getValue());
         return var2 >= var4 * var4 && var2 <= var6 * var6;
      } else {
         return false;
      }
   }

   private void E(Entity var1) {
      if (this.H(var1)) {
         if (var1 != this.y) {
            this.t(true, false, "Target changed");
            this.ro = new VecDeltaCodec();
            this.ro.setBase(var1.position());
            r4.g("Backtrack", "New target {}", var1.getName().getString());
         }

         this.y = var1;
      }
   }

   private void t(boolean var1, boolean var2, String var3) {
      if (var2) {
         this.Q(var3);
      } else if (var1) {
         this.k(true, var3);
      }

      this.y = null;
      this.ro = null;
   }

   private void k(boolean var1, String var2) {
      if (!this.E && !this.r6.isEmpty()) {
         if (mc.getConnection() != null && this.s()) {
            long var3 = C();
            long var5 = this.rX.getValue().longValue();
            int var7 = 0;
            this.E = (boolean)1;

            try {
               while (!this.r6.isEmpty() && (var1 || var3 - this.k.peekFirst() >= var5) && this.s()) {
                  this.k.removeFirst();
                  this.r6.removeFirst().run();
                  var7++;
               }

               if (!this.s()) {
                  this.Q("Session changed during release");
               }
            } catch (RuntimeException | Error var12) {
               this.r6.clear();
               this.k.clear();
               throw var12;
            } finally {
               this.E = (boolean)0;
            }

            if (var7 > 0 && var2 != null) {
               r4.g("Backtrack", "Released {} held updates: {}", var7, var2);
            }
         } else {
            this.Q(var2 == null ? "Session changed" : var2 + "; session changed");
         }
      }
   }

   private void Q(String var1) {
      int var2 = this.r6.size();
      this.r6.clear();
      this.k.clear();
      if (var2 > 0 && var1 != null) {
         r4.g("Backtrack", "Discarded {} held updates: {}", var2, var1);
      }
   }

   static long b(lc var0, lc var1) {
      int var2 = Math.round((float)Math.min(var0.getValue(), var1.getValue()));
      int var3 = Math.round((float)Math.max(var0.getValue(), var1.getValue()));
      return var3 > var2 ? ThreadLocalRandom.current().nextInt(var2, var3 + 1) : Math.max(0, var2);
   }

   static double s(AABB var0, AABB var1) {
      double var2 = Math.max(0.0, Math.max(var0.minX - var1.maxX, var1.minX - var0.maxX));
      double var4 = Math.max(0.0, Math.max(var0.minY - var1.maxY, var1.minY - var0.maxY));
      double var6 = Math.max(0.0, Math.max(var0.minZ - var1.maxZ, var1.minZ - var0.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   static double B(Vec3 var0, AABB var1) {
      return var0.distanceToSqr(
         new Vec3(Mth.clamp(var0.x, var1.minX, var1.maxX), Mth.clamp(var0.y, var1.minY, var1.maxY), Mth.clamp(var0.z, var1.minZ, var1.maxZ))
      );
   }

   private boolean c(AABB var1) {
      if (var1 != null && mc.player != null && this.m.getValue()) {
         double var2 = this.i.getValue();
         return B(mc.player.getEyePosition(), var1) > var2 * var2;
      } else {
         return false;
      }
   }

   private boolean E(AABB var1) {
      if (!this.c(var1)) {
         return false;
      }

      String var2 = String.format(Locale.ROOT, "Target out of reach (%.1f blocks)", Math.sqrt(B(mc.player.getEyePosition(), var1)));
      if (this.x.I() + this.r6.size() > 0) {
         this.x(var1);
      }

      if (this.rL.i("New")) {
         this.t(true, false, var2);
      } else {
         this.E(var2);
         this.e(false, false, true, null);
      }

      return true;
   }

   private AABB Y() {
      if (this.y == null) {
         return null;
      } else if (this.rL.i("New")) {
         return this.ro == null ? null : this.y.getBoundingBox().move(this.ro.getBase().subtract(this.y.position()));
      } else {
         return this.y.isAlive() && !this.n.getBase().equals(Vec3.ZERO) ? this.y.getType().getDimensions().makeBoundingBox(this.n.getBase()) : null;
      }
   }

   private void x(AABB var1) {
      this.r = new Vec3((var1.minX + var1.maxX) / 2.0, var1.minY, (var1.minZ + var1.maxZ) / 2.0);
      this.rr = Math.max(var1.getXsize(), var1.getZsize()) / 2.0;
      this.Q = var1.getYsize();
      this.l = ea.y();
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
      return !this.isEnabled() ? false : this.rY && this.N;
   }

   @Override
   public void clear() {
      this.rY = (boolean)0;
      this.M = this.r = null;
      this.O = null;
      this.f = null;
      this.rx = -1;
      this.z = this.R = 0.0;
      this.rn = -1.0;
      this.l = -10.0;
      this.X.H();
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.rY = (boolean)0;
         this.O = null;
         this.X.H();
         if (this.isInGame() && !this.u.i("None")) {
            double var3 = ea.y();
            double var5 = ea.X();
            this.N = this.c.getValue();
            this.t = this.I.getValue();
            this.C = this.w.getValue();
            this.rn = this.r != null && var3 - this.l < 0.55 ? Math.max(0.0, var3 - this.l) / 0.55 : -1.0;
            AABB var7 = this.Y();
            if (var7 == null) {
               this.M = null;
               this.rx = -1;
               this.z = 0.0;
               this.rY = (boolean)(this.rn >= 0.0 ? 1 : 0);
            } else {
               Vec3 var8 = new Vec3((var7.minX + var7.maxX) / 2.0, var7.minY, (var7.minZ + var7.maxZ) / 2.0);
               int var9 = this.M != null && this.rx == this.y.getId() && this.f == mc.level && this.M.distanceToSqr(var8) < 16.0 ? 1 : 0;
               this.M = var9 != 0 ? this.M.lerp(var8, 1.0 - Math.exp(-var5 * 24.0)) : var8;
               this.rx = this.y.getId();
               this.f = mc.level;
               this.J = var7.getXsize() / 2.0;
               this.F = var7.getZsize() / 2.0;
               this.s = var7.getYsize();
               this.z = this.z + ((this.x.I() + this.r6.size() > 0 ? 1 : 0) - this.z) * (1.0 - Math.exp(-var5 * 10.0));
               Vec3 var10 = this.y.getPosition(var1);
               this.R = yc.H((var10.distanceTo(this.M) - 0.05) / 0.45);
               if (this.K.getValue() && this.R > 0.01) {
                  EntityRenderState var11 = uv.v(this.y, var1);
                  var11.x = this.M.x;
                  var11.y = this.M.y;
                  var11.z = this.M.z;
                  var11.nameTag = null;
                  var11.scoreText = null;
                  var11.leashStates = null;
                  var11.displayFireAnimation = (boolean)0;
                  var11.shadowPieces.clear();
                  var11.outlineColor = 0;
                  this.O = var11;
               }

               if (this.V.getValue() && this.R > 0.01) {
                  Vec3 var22 = var10.add(0.0, this.s * 0.55, 0.0);
                  Vec3 var12 = this.M.add(0.0, this.s * 0.55, 0.0);
                  double var13 = Math.min(0.3, var22.distanceTo(var12) * 0.12);

                  for (int var15 = 0; var15 <= 24; var15++) {
                     double var16 = var15 / 24.0;
                     double var18 = Math.pow(0.5 + 0.5 * Math.cos((var16 * 2.5 - var3 * 1.6) * Math.PI * 2.0), 6.0);
                     double var20 = yc.H(var16 / 0.12) * yc.H((1.0 - var16) / 0.12);
                     this.X
                        .P(
                           var22.lerp(var12, var16).add(0.0, Math.sin(Math.PI * var16) * var13, 0.0),
                           0.03 * (1.0 + 0.7 * var18) * (0.35 + 0.65 * var20),
                           oh.S(oh.j(this.t, this.C, var16), this.R * (0.3 + 0.7 * var18))
                        );
                  }

                  this.X.n(var2.pos);
               }

               this.rY = (boolean)1;
            }
         } else {
            this.clear();
         }
      }
   }

   @Override
   public void render(r6 var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.rY && (!var2 || this.N)) {
            long var3 = h_.U();
            double var5 = ea.y();
            if (this.M != null) {
               double var7 = 0.65 + 0.35 * this.z;
               AABB var9 = new AABB(this.M.x - this.J, this.M.y, this.M.z - this.F, this.M.x + this.J, this.M.y + this.s, this.M.z + this.F).inflate(0.03);
               if (this.O != null) {
                  var1.v(this.O, uv.w.I, oh.j(this.t, this.C, 0.4), 0.55 * this.R * (var2 ? 0.5 : 1.0));
               }

               var1.blend(fk.h.u);
               if (!this.u.i("Fill")) {
                  this.v(var1, var9, var5, var7);
               }

               if (!this.u.i("Wireframe")) {
                  this.Q(var1, var9, var5, var7);
               }

               this.m(var1, var9, var5, var7);
               this.y(var1, var9, var5, var7, var2);
               if (this.X.d() > 1) {
                  var1.y(this.X, true);
               }

               var1.blend(fk.h.z);
               if (!var2 && !this.u.i("Wireframe")) {
                  this.g(var1, var9, var7);
               }
            }

            if (this.rn >= 0.0) {
               var1.blend(fk.h.u);
               this.e(var1, this.rn);
               var1.blend(fk.h.z);
            }

            h_.n("Backtrack ESP", var3);
         }
      }
   }

   private static Vec3 b(AABB var0, int var1) {
      return new Vec3((var1 & 1) == 0 ? var0.minX : var0.maxX, (var1 & 2) == 0 ? var0.minY : var0.maxY, (var1 & 4) == 0 ? var0.minZ : var0.maxZ);
   }

   private void v(r6 var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();

      for (int[] var11 : q) {
         Vec3 var12 = b(var2, var11[0]);
         Vec3 var13 = b(var2, var11[1]);

         for (int var14 = 0; var14 < 6; var14++) {
            Vec3 var15 = var12.lerp(var13, var14 / 6.0);
            Vec3 var16 = var12.lerp(var13, (var14 + 1) / 6.0);
            var1.n(
               var15.x, var15.y, var15.z, var16.x, var16.y, var16.z, 0.045, 0.045, this.S(var2, var7, var15, var3, var5), this.S(var2, var7, var16, var3, var5)
            );
         }
      }
   }

   private int S(AABB var1, Vec3 var2, Vec3 var3, double var4, double var6) {
      double var8 = Math.clamp((var3.y - var1.minY) / Math.max(1.0E-6, var1.getYsize()), 0.0, 1.0);
      double var10 = Math.atan2(var3.z - var2.z, var3.x - var2.x);
      double var12 = Math.pow(0.5 + 0.5 * Math.cos(var10 * 2.0 + var8 * 4.0 - var4 * 3.2), 10.0);
      int var14 = oh.j(this.t, this.C, var8);
      return oh.S(oh.j(var14, var14 | 16777215, var12 * 0.6), var6 * (0.6 + 0.4 * var12));
   }

   private void Q(r6 var1, AABB var2, double var3, double var5) {
      double var7 = var3 * 1.6;
      double var9 = 0.5 - 0.5 * Math.cos(var7);
      double var11 = Math.sin(var7);

      for (int var13 = 0; var13 < 3; var13++) {
         double var14 = Math.clamp(var9 - Math.signum(var11) * var13 * 0.07, 0.0, 1.0);
         double var16 = var13 == 0 ? 0.85 : 0.4 / var13 * Math.abs(var11);
         int var18 = oh.S(oh.j(this.t, this.C, var14), var16 * var5);
         double var19 = var2.minY + var2.getYsize() * var14;
         double var21 = var13 == 0 ? 0.04 : 0.025;
         var1.n(var2.minX, var19, var2.minZ, var2.maxX, var19, var2.minZ, var21, var21, var18, var18);
         var1.n(var2.maxX, var19, var2.minZ, var2.maxX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.n(var2.maxX, var19, var2.maxZ, var2.minX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.n(var2.minX, var19, var2.maxZ, var2.minX, var19, var2.minZ, var21, var21, var18, var18);
      }
   }

   private void m(r6 var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();
      double var8 = Math.max(var2.getXsize(), var2.getZsize()) * 0.75;
      double var10 = var2.minY + 0.02;
      double var12 = var3 / 1.3 - Math.floor(var3 / 1.3);
      var1.C(var7.x, var10, var7.z, var8 * (1.0 + 0.04 * Math.sin(var3 * 3.0)), 0.14, oh.S(this.C, 0.7 * var5));
      var1.C(var7.x, var10, var7.z, var8 * (1.0 + var12 * 1.1), 0.1, oh.S(this.t, 0.5 * var5 * (1.0 - var12) * (1.0 - var12)));
   }

   private void y(r6 var1, AABB var2, double var3, double var5, boolean var7) {
      for (int var8 = 0; var8 < 8; var8++) {
         Vec3 var9 = b(var2, var8);
         double var10 = 0.5 + 0.5 * Math.sin(var3 * 4.5 + var8 * 1.9);
         int var12 = oh.j((var8 & 2) == 0 ? this.t : this.C, -1, 0.45);
         var1.j(var9.x, var9.y, var9.z, var7 ? 0.22 : 0.16, 0.0, oh.S(var12, (var7 ? 0.35 : 0.2) * var5), f4.a.u, 0.0, 0.0, 0.0);
         if (!var7) {
            var1.m(var9.x, var9.y, var9.z, 0.05 + 0.035 * var10, var3 * 0.8 + var8, oh.S(var12, var5 * (0.55 + 0.45 * var10)));
         }
      }
   }

   private void g(r6 var1, AABB var2, double var3) {
      double var5 = this.u.i("Fill") ? 1.8 : 1.0;
      int var7 = oh.S(this.t, 0.16 * var5 * var3);
      int var8 = oh.S(this.C, 0.035 * var5 * var3);
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

   private void e(r6 var1, double var2) {
      double var4 = 1.0 - Math.pow(1.0 - var2, 3.0);
      double var6 = Math.pow(1.0 - var2, 1.5);
      Vec3 var8 = this.r;
      double var9 = this.rr * 1.5 * (1.0 + var4 * 2.2);
      double var11 = var8.y + 0.03;
      var1.C(var8.x, var11, var8.z, var9, 0.05 + 0.22 * (1.0 - var2), oh.S(this.t, 0.9 * var6));
      var1.C(var8.x, var11, var8.z, var9 * 0.7, 0.12, oh.S(this.C, 0.6 * var6));
      AABB var13 = new AABB(var8.x - this.rr, var8.y, var8.z - this.rr, var8.x + this.rr, var8.y + this.Q, var8.z + this.rr).inflate(0.35 * var4);
      int var14 = oh.S(oh.j(this.t, this.C, 0.5), var6 * var6);

      for (int[] var18 : q) {
         Vec3 var19 = b(var13, var18[0]);
         Vec3 var20 = b(var13, var18[1]);
         var1.n(var19.x, var19.y, var19.z, var20.x, var20.y, var20.z, 0.05, 0.05, var14, var14);
      }

      int var23 = oh.S(oh.j(this.C, -1, 0.5), var6);

      for (int var24 = 0; var24 < 12; var24++) {
         double var25 = var24 * Math.PI / 6.0 + var2;
         double var26 = Math.cos(var25);
         double var21 = Math.sin(var25);
         var1.j(
            var8.x + var26 * var9,
            var11 + 0.1 + var24 % 2 * var4 * 0.4,
            var8.z + var21 * var9,
            0.02 + 0.06 * (1.0 - var2),
            var25,
            var23,
            f4.a.d,
            var26,
            0.0,
            var21
         );
      }
   }

   private void D() {
      if (this.isRuntimeActive() && this.isInGame() && this.y != null && !this.u.i("None")) {
         int var1 = f8.c() && f8.p(this.effectName()) ? 1 : 0;
         AABB var2 = var1 != 0 && this.M != null && this.rx == this.y.getId()
            ? new AABB(this.M.x - this.J, this.M.y, this.M.z - this.F, this.M.x + this.J, this.M.y + this.s, this.M.z + this.F)
            : this.Y();
         if (var2 != null) {
            int var3 = this.I.getValue() & 16777215;
            if (var1 == 0) {
               int var4 = this.u.i("Fill") ? 0 : -520093696 | var3;
               int var5 = this.u.i("Wireframe") ? 0 : 754974720 | var3;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var4, 1.0F, var5)).setAlwaysOnTop();
            } else if (this.T.getValue()) {
               int var6 = this.u.i("Fill") ? 0 : 1174405120 | var3;
               int var7 = this.u.i("Fill") ? 503316480 | var3 : 0;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var6, 1.0F, var7)).setAlwaysOnTop();
            }
         }
      }
   }

   private static boolean M(ClientboundRemoveEntitiesPacket var0, int var1) {
      return var0.entityIds().contains(var1);
   }

   private static Vec3 X(VecDeltaCodec var0, ClientboundMoveEntityPacket var1) {
      return var1.getPositionDelta().decode(var0).endPosition();
   }

   private static Vec3 C(ClientboundEntityPositionSyncPacket var0) {
      return var0.position().endPosition();
   }
}
