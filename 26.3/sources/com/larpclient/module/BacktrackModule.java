package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._0;
import com.larpclient._q;
import com.larpclient.fz;
import com.larpclient.he;
import com.larpclient.hz;
import com.larpclient.pr;
import com.larpclient.qe;
import com.larpclient.vv;
import com.larpclient.vx;
import com.larpclient.w7;
import com.larpclient.wp;
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

public final class BacktrackModule extends Module implements qe {
   private static BacktrackModule Ya;
   private static final int G = 65536;
   private static final long e = System.nanoTime();
   private final o f = this.addSetting(new o("Version", "Packet delay algorithm", "New", "Old", "New"));
   private final b t = this.addSetting(new b("Range Min", "Lower bound of the box distance to the target", 1.0, 0.0, 10.0, 0.1));
   private final b c = this.addSetting(new b("Range Max", "Upper bound of the box distance to the target", 3.0, 0.0, 10.0, 0.1));
   private final b x = this.addSetting(new b("Delay Min", "Shortest hold window in milliseconds", 280.0, 0.0, 2000.0, 1.0));
   private final b YK = this.addSetting(new b("Delay Max", "Longest hold window in milliseconds", 480.0, 0.0, 2000.0, 1.0));
   private final b K = this.addSetting(new b("Next Backtrack Delay Min", "Shortest wait before the next window in milliseconds", 0.0, 0.0, 2000.0, 1.0));
   private final b d = this.addSetting(new b("Next Backtrack Delay Max", "Longest wait before the next window in milliseconds", 10.0, 0.0, 2000.0, 1.0));
   private final b A = this.addSetting(
      new b("Tracking Buffer", "Keep windows available after the target leaves range, in milliseconds", 500.0, 0.0, 2000.0, 1.0)
   );
   private final b o = this.addSetting(new b("Chance", "Chance that an attack allows windows, in percent", 50.0, 0.0, 100.0, 1.0));
   private final m Y4 = this.addSetting(new m("Pause On Hurt Time", "Skip windows while the target is hurt", false));
   private final b N = this.addSetting(new b("Hurt Time", "Target hurt time that pauses windows", 3.0, 0.0, 10.0, 1.0));
   private final o F = this.addSetting(new o("Target Mode", "Use the attacked entity or the nearest one in range", "Attack", "Attack", "Range"));
   private final b P = this.addSetting(new b("Time Since Last Attack", "How long an attack keeps windows available, in milliseconds", 1000.0, 0.0, 5000.0, 1.0));
   private final m q = this.addSetting(new m("Release Out Of Reach", "Release held packets as soon as the target's server position is too far to hit", true));
   private final b k = this.addSetting(
      new b("Release Distance", "Server position distance from your eyes that releases held packets. Vanilla ignores hits past 6", 6.0, 1.0, 10.0, 0.1)
   );
   private final o Yg = this.addSetting(new o("ESP", "Draw the target's server position", "Box", "Box", "Wireframe", "Fill", "None"));
   private final h R = this.addSetting(new h("Color", "Server position box color", -1));
   private final h O = this.addSetting(new h("Gradient", "Second ESP color, blended toward the top", -6862849));
   private final m YU = this.addSetting(new m("Ghost Model", "Draw a hologram of the target at its server position", true));
   private final m j = this.addSetting(new m("Tether", "Link the displayed target to its server position", true));
   private final m r = this.addSetting(new m("Bloom", "Glow pass for the ESP", true));
   private final m J = this.addSetting(new m("Through Walls", "Keep a faint outline visible behind blocks", true));
   private final b L = this.addSetting(new b("New Range Min", "Lower bound of the distance to the target player", 0.0, 0.0, 10.0, 0.01));
   private final b YL = this.addSetting(new b("New Range Max", "Upper bound of the distance to the target player", 10.0, 0.0, 10.0, 0.01));
   private final b a = this.addSetting(new b("New Delay", "How long each held update waits, in milliseconds", 15000.0, 0.0, 15000.0, 1.0));
   private final m s = this.addSetting(new m("Only Attack", "Release held updates when the target moves closer", true));
   private Entity v;
   private String n = "Old";
   private final vx Yj = new vx(65536);
   private final ArrayDeque<Runnable> YS = new ArrayDeque<>();
   private final VecDeltaCodec h = new VecDeltaCodec();
   private boolean U;
   private boolean Y7;
   private long Y;
   private long p;
   private long I;
   private int Yb;
   private final ArrayDeque<Runnable> D = new ArrayDeque<>();
   private final ArrayDeque<Long> l = new ArrayDeque<>();
   private VecDeltaCodec M;
   private boolean b;
   private ClientPacketListener Z;
   private ClientLevel Y8;
   private volatile int S;
   private static final double B = 0.55;
   private static final int[][] V = new int[][]{{0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
   private final com.larpclient.x.c E = new com.larpclient.x.c(25);
   private boolean u;
   private boolean C;
   private Object Yy;
   private int W = -1;
   private int X;
   private int H;
   private Vec3 m;
   private double g;
   private double YG;
   private double Y3;
   private double Yz;
   private double y;
   private double z = -1.0;
   private EntityRenderState w;
   private Vec3 Q;
   private double i;
   private double T;
   private double YI = -10.0;

   public BacktrackModule() {
      super("Backtrack", "Backtracks player position to a hittable state", Category.COMBAT, -1);
      Ya = this;
      this.t.R(this.c).B("Range");
      this.x.R(this.YK).B("Delay");
      this.K.R(this.d).B("Next Backtrack Delay");
      this.L.R(this.YL).B("New Range");
      this.A.A("ms");
      this.o.A("%");
      this.N.A("");
      this.P.A("ms").B("Last Attack Time To Work");
      this.a.A("ms");

      for (r var4 : new r[]{this.t, this.c, this.x, this.YK, this.K, this.d, this.A, this.o, this.Y4, this.F, this.P}) {
         var4.a(() -> this.f.w("Old"));
      }

      this.N.a(() -> this.f.w("Old") && this.Y4.getValue());
      this.k.a(this.q::getValue);

      for (r var11 : new r[]{this.R, this.O, this.YU, this.j, this.r, this.J}) {
         var11.a(() -> !this.Yg.w("None"));
      }

      for (r var12 : new r[]{this.L, this.YL, this.a, this.s}) {
         var12.a(() -> this.f.w("New"));
      }

      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.K());
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.k);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      }

      if (this.v == null) {
         return "Waiting for target";
      }

      int var1 = this.Yj.b() + this.D.size();
      return "Tracking " + this.v.getName().getString() + (var1 > 0 ? " · " + var1 + " held" : "");
   }

   @Override
   public void onEnable() {
      this.n = this.f.getValue();
      this.Yb = ThreadLocalRandom.current().nextInt(0, 101);
      this.U(false, false, false, null);
      this.P(false, false, null);
      this.clear();
      this.h();
   }

   @Override
   public void onDisable() {
      this.S = 0;
      this.U(true, false, true, "Stopped");
      this.P(true, false, "Stopped");
      this.clear();
   }

   private void h() {
      this.S = !this.isRuntimeActive() ? 0 : (this.f.w("Old") ? (this.v == null && this.Yj.b() <= 0 ? 0 : 1) : (!this.D.isEmpty() ? 2 : 0));
   }

   public static void a(Packet<?> var0, PacketListener var1) {
      if (Ya != null && Ya.isEnabled()) {
         BacktrackModule var2 = Ya;
         if (var2 != null && var2.S != 0 && var1 instanceof ClientPacketListener var3) {
            Minecraft var4 = getMc();
            if (var4 != null && !var4.isSameThread()) {
               if (var2.S == 1 ? E(var0) : var0 instanceof ClientboundDisconnectPacket) {
                  PacketUtils.ensureRunningOnSameThread(var0, var3, var4.packetProcessor());
               }
            }
         }
      }
   }

   static boolean E(Packet<?> var0) {
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

   public static boolean F() {
      BacktrackModule var0 = Ya;
      return var0 != null && var0.Yj.b() > 0;
   }

   private static long W() {
      return (System.nanoTime() - e) / 1000000L;
   }

   private void d() {
      String var1 = this.f.getValue();
      if (!var1.equals(this.n)) {
         this.U(true, false, false, "Version changed");
         this.P(true, false, "Version changed");
         this.n = var1;
      }
   }

   private boolean b() {
      Minecraft var1 = getMc();
      return this.Z != null && var1.getConnection() == this.Z && var1.level == this.Y8 && this.Z.getConnection().isConnected();
   }

   private void x() {
      if (this.Yj.b() <= 0 && this.YS.isEmpty() && this.D.isEmpty()) {
         this.Z = mc.getConnection();
         this.Y8 = mc.level;
      }
   }

   public static boolean E() {
      BacktrackModule var0 = Ya;
      return var0 != null && (var0.Yj.h() || var0.b || var0.Y7);
   }

   public static boolean I(Packet<?> var0, PacketListener var1, Runnable var2) {
      if (Ya != null && Ya.isEnabled()) {
         BacktrackModule var3 = Ya;
         Minecraft var4 = getMc();
         if (var3 != null
            && !E()
            && var4.isSameThread()
            && !(var0 instanceof ClientboundBundlePacket)
            && var1 instanceof ClientPacketListener
            && var1 == var4.getConnection()
            && var3.isRuntimeActive()) {
            try {
               var3.d();
               return var3.f.w("New") ? var3.q(var0, var2) : var3.D(var0, var2);
            } finally {
               var3.h();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean H(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (Ya != null && Ya.isEnabled()) {
         BacktrackModule var4 = Ya;
         Minecraft var5 = getMc();
         if (var4 != null
            && !E()
            && var5 != null
            && var5.isSameThread()
            && var4.isRuntimeActive()
            && var5.player != null
            && var5.level != null
            && var5.getConnection() != null
            && !var5.isLocalServer()
            && var0 == var5.getConnection().getConnection()) {
            var4.d();
            if (!var4.f.w("New") && var1 instanceof ServerboundPongPacket && var4.Yj.b() != 0) {
               var4.YS.addLast(() -> var0.send(var1, var2, var3));
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

   public static boolean Z() {
      BacktrackModule var0 = Ya;
      return var0 != null && var0.Y7;
   }

   public static void c(Entity var0) {
      if (Ya != null && Ya.isEnabled()) {
         BacktrackModule var1 = Ya;
         if (var1 != null && var0 != null && var1.isRuntimeActive() && var1.isInGame()) {
            try {
               var1.d();
               if (!var1.f.w("New")) {
                  var1.p = W();
                  var1.Yb = ThreadLocalRandom.current().nextInt(0, 101);
                  if (var1.F.w("Attack")) {
                     var1.b(var0);
                  }

                  return;
               }

               var1.r(var0);
            } finally {
               var1.h();
            }
         }
      }
   }

   @Override
   public void onTick() {
      try {
         this.c();
      } finally {
         this.h();
      }
   }

   private void c() {
      this.d();
      if (this.f.w("New")) {
         this.u();
      } else if (mc.player != null && mc.level != null) {
         if (this.F.w("Range")) {
            Entity var1 = this.U();
            if (var1 == null) {
               this.U(true, false, true, "No target in range");
            } else {
               this.b(var1);
            }
         }

         int var2 = this.Yj.b() > 0 ? 1 : 0;
         if (var2 == 0 || !this.U(this.G())) {
            if (this.g()) {
               if (var2 != 0 && this.Yj.c(W())) {
                  this.I("Window ended");
                  this.U(false, false, true, null);
               }
            } else if (var2 != 0) {
               this.I("Target no longer eligible");
               this.U(false, false, true, null);
            }
         }
      } else {
         this.U(false, true, false, "World unavailable");
      }
   }

   private boolean D(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.level == null || mc.getConnection() == null || mc.isLocalServer()) {
         return false;
      }

      if (R(var1)) {
         return false;
      }

      if (this.V(var1)) {
         this.U(true, false, true, "Server boundary packet");
         return false;
      }

      boolean var3 = this.g();
      int var4 = this.Yj.b() > 0 ? 1 : 0;
      if (var4 == 0 && !var3) {
         return false;
      }

      Entity var5 = this.v;
      if (var4 == 0 && !(var1 instanceof ClientboundPingPacket)) {
         this.M(var1, var5);
         return false;
      }

      if (var5 != null) {
         Vec3 var6 = this.M(var1, var5);
         if (var6 != null && this.U(var5.getType().getDimensions().makeBoundingBox(var6))) {
            return false;
         }

         if (var6 != null && this.i(var5, var6)) {
            this.I("Target moved closer");
            return false;
         }
      }

      if (var4 == 0) {
         if (var5 != null && this.r(this.G())) {
            return false;
         }

         this.x();
         long var8 = Q(this.x, this.YK);
         if (this.Yj.k()) {
            this.Yj.v();
         }

         this.Yj.y(W(), var8);
         _q.c("Backtrack", "Old window opened for {} ms", var8);
      }

      if (this.Yj.Y(var2)) {
         return true;
      }

      this.I("Packet capacity reached");
      return false;
   }

   private static boolean R(Packet<?> var0) {
      return var0 instanceof ClientboundPlayerChatPacket
         || var0 instanceof ClientboundSystemChatPacket
         || var0 instanceof ClientboundDisguisedChatPacket
         || var0 instanceof ClientboundSetActionBarTextPacket
         || var0 instanceof ClientboundSoundPacket var1 && var1.getSound().value() == SoundEvents.PLAYER_HURT;
   }

   private boolean V(Packet<?> var1) {
      return var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F
         || var1 instanceof ClientboundRemoveEntitiesPacket var2 && this.v != null && Y(var2, this.v.getId());
   }

   private Vec3 M(Packet<?> var1, Entity var2) {
      return var2 != null && mc.level != null ? y(this.h, var1, mc.level, var2) : null;
   }

   static Vec3 y(VecDeltaCodec var0, Packet<?> var1, ClientLevel var2, Entity var3) {
      Vec3 var4 = null;
      if (var1 instanceof ClientboundMoveEntityPacket var5 && var5.hasPosition() && var5.getEntity(var2) == var3) {
         var4 = E(var0, var5);
      } else if (var1 instanceof ClientboundTeleportEntityPacket var6 && var6.id() == var3.getId()) {
         var4 = var6.relatives().isEmpty()
            ? var6.change().position()
            : PositionMoveRotation.calculateAbsolute(
                  new PositionMoveRotation(var0.getBase(), var3.getDeltaMovement(), var3.getYRot(), var3.getXRot()), var6.change(), var6.relatives()
               )
               .position();
      } else if (var1 instanceof ClientboundEntityPositionSyncPacket var7 && var7.id() == var3.getId()) {
         var4 = y(var7);
      }

      if (var4 != null) {
         var0.setBase(var4);
      }

      return var4;
   }

   private boolean i(Entity var1, Vec3 var2) {
      AABB var3 = var1.getType().getDimensions().makeBoundingBox(var2);
      AABB var4 = mc.player.getBoundingBox();
      return E(var4, var3) < E(var4, var1.getBoundingBox());
   }

   private boolean g() {
      return this.v != null && this.v.isAlive() && this.A(this.v);
   }

   private boolean A(Entity var1) {
      if (mc.player != null && var1 != null) {
         boolean var2 = this.S(var1);
         long var3 = W();
         if (var2) {
            this.Y = var3;
         }

         return (var2 || this.Y != 0L && var3 - this.Y <= this.A.getValue().longValue())
            && this.L(var1)
            && mc.player.tickCount > 10
            && this.Yb < this.o.getValue()
            && var3 >= this.I
            && (!this.Y4.getValue() || !this.U)
            && this.p != 0L
            && var3 - this.p <= this.P.getValue().longValue();
      } else {
         return false;
      }
   }

   private boolean L(Entity var1) {
      if (!(var1 instanceof LivingEntity var2 && mc.player != null && var1 != mc.player)) {
         return false;
      } else {
         if (!var2.isAlive() || var2.isRemoved() || var2.isSpectator() || !var2.isPickable()) {
            return false;
         }

         if (!(var1 instanceof Player)) {
            return true;
         }

         MCFriendModule var3 = LarpClient.N().j().y(MCFriendModule.class);
         return var3 == null || !var3.M(var1.getName().getString());
      }
   }

   private boolean S(Entity var1) {
      double var2 = Math.sqrt(E(mc.player.getBoundingBox(), var1.getBoundingBox()));
      return var2 >= Math.min(this.t.getValue(), this.c.getValue()) && var2 <= Math.max(this.t.getValue(), this.c.getValue());
   }

   private Entity U() {
      double var1 = Math.min(this.t.getValue(), this.c.getValue());
      double var3 = Math.max(this.t.getValue(), this.c.getValue());
      AABB var5 = mc.player.getBoundingBox();
      Entity var6 = null;
      double var7 = Double.MAX_VALUE;

      for (Entity var10 : mc.level.getEntities(mc.player, var5.inflate(var3 + 1.0), this::L)) {
         double var11 = Math.sqrt(E(var5, var10.getBoundingBox()));
         if (var11 >= var1 && var11 <= var3 && var11 < var7) {
            var6 = var10;
            var7 = var11;
         }
      }

      return var6;
   }

   private void b(Entity var1) {
      this.U = (boolean)(var1 instanceof LivingEntity var2 && var2.hurtTime >= this.N.getValue() ? 1 : 0);
      if (this.A(var1)) {
         if (var1 != this.v) {
            this.U(true, false, false, "Target changed");
            this.h.setBase(var1.getPositionCodec().getBase());
         }

         this.v = var1;
      }
   }

   private void U(boolean var1, boolean var2, boolean var3, String var4) {
      if (var1 && !var2) {
         this.I(var4);
      } else if (var2) {
         this.n(var4);
      }

      if (this.v != null && var3) {
         this.I = W() + Q(this.K, this.d);
      }

      this.v = null;
      this.U = (boolean)0;
      this.h.setBase(Vec3.ZERO);
   }

   private void I(String var1) {
      int var2 = this.Yj.b() + this.YS.size();
      if (var2 == 0) {
         this.Yj.v();
      } else if (mc.getConnection() != null && this.b()) {
         this.Y7 = (boolean)1;

         try {
            while (!this.YS.isEmpty() && this.b()) {
               this.YS.removeFirst().run();
            }
         } finally {
            this.Y7 = (boolean)0;
            this.YS.clear();
         }

         this.Yj.L(this::b);
         if (var1 != null) {
            _q.c("Backtrack", "Released {} held packets: {}", var2, var1);
         }
      } else {
         this.n(var1 + "; session changed");
      }
   }

   private void n(String var1) {
      int var2 = this.Yj.b() + this.YS.size();
      this.Yj.v();
      this.YS.clear();
      if (var2 > 0 && var1 != null) {
         _q.c("Backtrack", "Discarded {} held packets: {}", var2, var1);
      }
   }

   private void u() {
      if (mc.player != null && mc.level != null) {
         if (!this.k()) {
            this.P(true, false, "Target left New range");
         } else if (!this.U(this.G())) {
            this.m(false, null);
         }
      } else {
         this.P(true, true, "World unavailable");
      }
   }

   private boolean q(Packet<?> var1, Runnable var2) {
      if (mc.player == null || mc.level == null) {
         this.P(true, true, "World unavailable");
         return false;
      }

      if (var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F) {
         this.P(true, false, "Server boundary packet");
         return false;
      }

      if (this.D.isEmpty() && !this.k()) {
         return false;
      }

      this.l(var1);
      if (this.v == null || n(var1) || !o(var1)) {
         return false;
      }

      if (this.D.size() >= 65536) {
         this.m(true, "Packet capacity reached");
         return false;
      }

      if (this.D.isEmpty()) {
         this.x();
         _q.c("Backtrack", "New queue opened with {} ms delay", this.a.getValue().longValue());
      }

      this.D.addLast(var2);
      this.l.addLast(W());
      return true;
   }

   private static boolean n(Packet<?> var0) {
      return var0 instanceof ClientboundSoundPacket || var0 instanceof ClientboundSetHealthPacket || var0 instanceof ClientboundSetEntityDataPacket;
   }

   private static boolean o(Packet<?> var0) {
      return var0 instanceof ClientboundPingPacket
         || var0 instanceof ClientboundRotateHeadPacket
         || var0 instanceof ClientboundTeleportEntityPacket
         || var0 instanceof ClientboundEntityPositionSyncPacket
         || var0 instanceof ClientboundSetEntityMotionPacket
         || var0 instanceof ClientboundMoveEntityPacket
         || var0 instanceof ClientboundLevelParticlesPacket
         || var0 instanceof ClientboundExplodePacket;
   }

   private void l(Packet<?> var1) {
      if (mc.player != null && mc.level != null && this.v != null) {
         Vec3 var2 = null;
         if (var1 instanceof ClientboundMoveEntityPacket var3 && var3.hasPosition() && var3.getEntity(mc.level) == this.v && this.M != null) {
            var2 = E(this.M, var3);
         } else if (var1 instanceof ClientboundTeleportEntityPacket var4 && var4.id() == this.v.getId()) {
            var2 = var4.change().position();
         } else if (var1 instanceof ClientboundEntityPositionSyncPacket var5 && var5.id() == this.v.getId()) {
            var2 = y(var5);
         }

         if (var2 != null) {
            if (this.M == null) {
               this.M = new VecDeltaCodec();
            }

            this.M.setBase(var2);
            AABB var6 = this.v.getBoundingBox().move(var2.subtract(this.v.position()));
            if (!this.U(var6) && this.s.getValue()) {
               Vec3 var7 = mc.player.position();
               if (c(var7, var6) < c(var7, this.v.getBoundingBox())) {
                  this.m(true, "Target moved closer");
               }
            }
         }
      }
   }

   private boolean k() {
      return this.v != null && this.v.isAlive() && this.G(this.v);
   }

   private boolean G(Entity var1) {
      if (mc.player != null && var1 instanceof Player && var1 != mc.player) {
         double var2 = c(mc.player.position(), var1.getBoundingBox());
         double var4 = Math.min(this.L.getValue(), this.YL.getValue());
         double var6 = Math.max(this.L.getValue(), this.YL.getValue());
         return var2 >= var4 * var4 && var2 <= var6 * var6;
      } else {
         return false;
      }
   }

   private void r(Entity var1) {
      if (this.G(var1)) {
         if (var1 != this.v) {
            this.P(true, false, "Target changed");
            this.M = new VecDeltaCodec();
            this.M.setBase(var1.position());
            _q.c("Backtrack", "New target {}", var1.getName().getString());
         }

         this.v = var1;
      }
   }

   private void P(boolean var1, boolean var2, String var3) {
      if (var2) {
         this.W(var3);
      } else if (var1) {
         this.m(true, var3);
      }

      this.v = null;
      this.M = null;
   }

   private void m(boolean var1, String var2) {
      if (!this.b && !this.D.isEmpty()) {
         if (mc.getConnection() != null && this.b()) {
            long var3 = W();
            long var5 = this.a.getValue().longValue();
            int var7 = 0;
            this.b = (boolean)1;

            try {
               while (!this.D.isEmpty() && (var1 || var3 - this.l.peekFirst() >= var5) && this.b()) {
                  this.l.removeFirst();
                  this.D.removeFirst().run();
                  var7++;
               }

               if (!this.b()) {
                  this.W("Session changed during release");
               }
            } catch (RuntimeException | Error var12) {
               this.D.clear();
               this.l.clear();
               throw var12;
            } finally {
               this.b = (boolean)0;
            }

            if (var7 > 0 && var2 != null) {
               _q.c("Backtrack", "Released {} held updates: {}", var7, var2);
            }
         } else {
            this.W(var2 == null ? "Session changed" : var2 + "; session changed");
         }
      }
   }

   private void W(String var1) {
      int var2 = this.D.size();
      this.D.clear();
      this.l.clear();
      if (var2 > 0 && var1 != null) {
         _q.c("Backtrack", "Discarded {} held updates: {}", var2, var1);
      }
   }

   static long Q(b var0, b var1) {
      int var2 = Math.round((float)Math.min(var0.getValue(), var1.getValue()));
      int var3 = Math.round((float)Math.max(var0.getValue(), var1.getValue()));
      return var3 > var2 ? ThreadLocalRandom.current().nextInt(var2, var3 + 1) : Math.max(0, var2);
   }

   static double E(AABB var0, AABB var1) {
      double var2 = Math.max(0.0, Math.max(var0.minX - var1.maxX, var1.minX - var0.maxX));
      double var4 = Math.max(0.0, Math.max(var0.minY - var1.maxY, var1.minY - var0.maxY));
      double var6 = Math.max(0.0, Math.max(var0.minZ - var1.maxZ, var1.minZ - var0.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   static double c(Vec3 var0, AABB var1) {
      return var0.distanceToSqr(
         new Vec3(Mth.clamp(var0.x, var1.minX, var1.maxX), Mth.clamp(var0.y, var1.minY, var1.maxY), Mth.clamp(var0.z, var1.minZ, var1.maxZ))
      );
   }

   private boolean r(AABB var1) {
      if (var1 != null && mc.player != null && this.q.getValue()) {
         double var2 = this.k.getValue();
         return c(mc.player.getEyePosition(), var1) > var2 * var2;
      } else {
         return false;
      }
   }

   private boolean U(AABB var1) {
      if (!this.r(var1)) {
         return false;
      }

      String var2 = String.format(Locale.ROOT, "Target out of reach (%.1f blocks)", Math.sqrt(c(mc.player.getEyePosition(), var1)));
      if (this.Yj.b() + this.D.size() > 0) {
         this.Q(var1);
      }

      if (this.f.w("New")) {
         this.P(true, false, var2);
      } else {
         this.I(var2);
         this.U(false, false, true, null);
      }

      return true;
   }

   private AABB G() {
      if (this.v == null) {
         return null;
      } else if (this.f.w("New")) {
         return this.M == null ? null : this.v.getBoundingBox().move(this.M.getBase().subtract(this.v.position()));
      } else {
         return this.v.isAlive() && !this.h.getBase().equals(Vec3.ZERO) ? this.v.getType().getDimensions().makeBoundingBox(this.h.getBase()) : null;
      }
   }

   private void Q(AABB var1) {
      this.Q = new Vec3((var1.minX + var1.maxX) / 2.0, var1.minY, (var1.minZ + var1.maxZ) / 2.0);
      this.i = Math.max(var1.getXsize(), var1.getZsize()) / 2.0;
      this.T = var1.getYsize();
      this.YI = hz.Q();
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
      return !this.isEnabled() ? false : this.u && this.C;
   }

   @Override
   public void clear() {
      this.u = (boolean)0;
      this.m = this.Q = null;
      this.w = null;
      this.Yy = null;
      this.W = -1;
      this.y = this.Yz = 0.0;
      this.z = -1.0;
      this.YI = -10.0;
      this.E.w();
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.u = (boolean)0;
         this.w = null;
         this.E.w();
         if (this.isInGame() && !this.Yg.w("None")) {
            double var3 = hz.Q();
            double var5 = hz.V();
            this.C = this.r.getValue();
            this.X = this.R.getValue();
            this.H = this.O.getValue();
            this.z = this.Q != null && var3 - this.YI < 0.55 ? Math.max(0.0, var3 - this.YI) / 0.55 : -1.0;
            AABB var7 = this.G();
            if (var7 == null) {
               this.m = null;
               this.W = -1;
               this.y = 0.0;
               this.u = (boolean)(this.z >= 0.0 ? 1 : 0);
            } else {
               Vec3 var8 = new Vec3((var7.minX + var7.maxX) / 2.0, var7.minY, (var7.minZ + var7.maxZ) / 2.0);
               int var9 = this.m != null && this.W == this.v.getId() && this.Yy == mc.level && this.m.distanceToSqr(var8) < 16.0 ? 1 : 0;
               this.m = var9 != 0 ? this.m.lerp(var8, 1.0 - Math.exp(-var5 * 24.0)) : var8;
               this.W = this.v.getId();
               this.Yy = mc.level;
               this.g = var7.getXsize() / 2.0;
               this.YG = var7.getZsize() / 2.0;
               this.Y3 = var7.getYsize();
               this.y = this.y + ((this.Yj.b() + this.D.size() > 0 ? 1 : 0) - this.y) * (1.0 - Math.exp(-var5 * 10.0));
               Vec3 var10 = this.v.getPosition(var1);
               this.Yz = pr.k((var10.distanceTo(this.m) - 0.05) / 0.45);
               if (this.YU.getValue() && this.Yz > 0.01) {
                  EntityRenderState var11 = w7.u(this.v, var1);
                  var11.x = this.m.x;
                  var11.y = this.m.y;
                  var11.z = this.m.z;
                  var11.nameTag = null;
                  var11.scoreText = null;
                  var11.leashStates = null;
                  var11.displayFireAnimation = (boolean)0;
                  var11.shadowPieces.clear();
                  var11.outlineColor = 0;
                  this.w = var11;
               }

               if (this.j.getValue() && this.Yz > 0.01) {
                  Vec3 var22 = var10.add(0.0, this.Y3 * 0.55, 0.0);
                  Vec3 var12 = this.m.add(0.0, this.Y3 * 0.55, 0.0);
                  double var13 = Math.min(0.3, var22.distanceTo(var12) * 0.12);

                  for (int var15 = 0; var15 <= 24; var15++) {
                     double var16 = var15 / 24.0;
                     double var18 = Math.pow(0.5 + 0.5 * Math.cos((var16 * 2.5 - var3 * 1.6) * Math.PI * 2.0), 6.0);
                     double var20 = pr.k(var16 / 0.12) * pr.k((1.0 - var16) / 0.12);
                     this.E
                        .J(
                           var22.lerp(var12, var16).add(0.0, Math.sin(Math.PI * var16) * var13, 0.0),
                           0.03 * (1.0 + 0.7 * var18) * (0.35 + 0.65 * var20),
                           vv.O(vv.Q(this.X, this.H, var16), this.Yz * (0.3 + 0.7 * var18))
                        );
                  }

                  this.E.T(var2.pos);
               }

               this.u = (boolean)1;
            }
         } else {
            this.clear();
         }
      }
   }

   @Override
   public void render(fz var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.u && (!var2 || this.C)) {
            long var3 = he.s();
            double var5 = hz.Q();
            if (this.m != null) {
               double var7 = 0.65 + 0.35 * this.y;
               AABB var9 = new AABB(this.m.x - this.g, this.m.y, this.m.z - this.YG, this.m.x + this.g, this.m.y + this.Y3, this.m.z + this.YG).inflate(0.03);
               if (this.w != null) {
                  var1.m(this.w, w7.b.Q, vv.Q(this.X, this.H, 0.4), 0.55 * this.Yz * (var2 ? 0.5 : 1.0));
               }

               var1.blend(_0.v.c);
               if (!this.Yg.w("Fill")) {
                  this.A(var1, var9, var5, var7);
               }

               if (!this.Yg.w("Wireframe")) {
                  this.R(var1, var9, var5, var7);
               }

               this.Z(var1, var9, var5, var7);
               this.Z(var1, var9, var5, var7, var2);
               if (this.E.m() > 1) {
                  var1.n(this.E, true);
               }

               var1.blend(_0.v.z);
               if (!var2 && !this.Yg.w("Wireframe")) {
                  this.V(var1, var9, var7);
               }
            }

            if (this.z >= 0.0) {
               var1.blend(_0.v.c);
               this.U(var1, this.z);
               var1.blend(_0.v.z);
            }

            he.T("Backtrack ESP", var3);
         }
      }
   }

   private static Vec3 x(AABB var0, int var1) {
      return new Vec3((var1 & 1) == 0 ? var0.minX : var0.maxX, (var1 & 2) == 0 ? var0.minY : var0.maxY, (var1 & 4) == 0 ? var0.minZ : var0.maxZ);
   }

   private void A(fz var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();

      for (int[] var11 : V) {
         Vec3 var12 = x(var2, var11[0]);
         Vec3 var13 = x(var2, var11[1]);

         for (int var14 = 0; var14 < 6; var14++) {
            Vec3 var15 = var12.lerp(var13, var14 / 6.0);
            Vec3 var16 = var12.lerp(var13, (var14 + 1) / 6.0);
            var1.h(
               var15.x, var15.y, var15.z, var16.x, var16.y, var16.z, 0.045, 0.045, this.A(var2, var7, var15, var3, var5), this.A(var2, var7, var16, var3, var5)
            );
         }
      }
   }

   private int A(AABB var1, Vec3 var2, Vec3 var3, double var4, double var6) {
      double var8 = Math.clamp((var3.y - var1.minY) / Math.max(1.0E-6, var1.getYsize()), 0.0, 1.0);
      double var10 = Math.atan2(var3.z - var2.z, var3.x - var2.x);
      double var12 = Math.pow(0.5 + 0.5 * Math.cos(var10 * 2.0 + var8 * 4.0 - var4 * 3.2), 10.0);
      int var14 = vv.Q(this.X, this.H, var8);
      return vv.O(vv.Q(var14, var14 | 16777215, var12 * 0.6), var6 * (0.6 + 0.4 * var12));
   }

   private void R(fz var1, AABB var2, double var3, double var5) {
      double var7 = var3 * 1.6;
      double var9 = 0.5 - 0.5 * Math.cos(var7);
      double var11 = Math.sin(var7);

      for (int var13 = 0; var13 < 3; var13++) {
         double var14 = Math.clamp(var9 - Math.signum(var11) * var13 * 0.07, 0.0, 1.0);
         double var16 = var13 == 0 ? 0.85 : 0.4 / var13 * Math.abs(var11);
         int var18 = vv.O(vv.Q(this.X, this.H, var14), var16 * var5);
         double var19 = var2.minY + var2.getYsize() * var14;
         double var21 = var13 == 0 ? 0.04 : 0.025;
         var1.h(var2.minX, var19, var2.minZ, var2.maxX, var19, var2.minZ, var21, var21, var18, var18);
         var1.h(var2.maxX, var19, var2.minZ, var2.maxX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.h(var2.maxX, var19, var2.maxZ, var2.minX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.h(var2.minX, var19, var2.maxZ, var2.minX, var19, var2.minZ, var21, var21, var18, var18);
      }
   }

   private void Z(fz var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();
      double var8 = Math.max(var2.getXsize(), var2.getZsize()) * 0.75;
      double var10 = var2.minY + 0.02;
      double var12 = var3 / 1.3 - Math.floor(var3 / 1.3);
      var1.l(var7.x, var10, var7.z, var8 * (1.0 + 0.04 * Math.sin(var3 * 3.0)), 0.14, vv.O(this.H, 0.7 * var5));
      var1.l(var7.x, var10, var7.z, var8 * (1.0 + var12 * 1.1), 0.1, vv.O(this.X, 0.5 * var5 * (1.0 - var12) * (1.0 - var12)));
   }

   private void Z(fz var1, AABB var2, double var3, double var5, boolean var7) {
      for (int var8 = 0; var8 < 8; var8++) {
         Vec3 var9 = x(var2, var8);
         double var10 = 0.5 + 0.5 * Math.sin(var3 * 4.5 + var8 * 1.9);
         int var12 = vv.Q((var8 & 2) == 0 ? this.X : this.H, -1, 0.45);
         var1.a(var9.x, var9.y, var9.z, var7 ? 0.22 : 0.16, 0.0, vv.O(var12, (var7 ? 0.35 : 0.2) * var5), com.larpclient.l.b.P, 0.0, 0.0, 0.0);
         if (!var7) {
            var1.K(var9.x, var9.y, var9.z, 0.05 + 0.035 * var10, var3 * 0.8 + var8, vv.O(var12, var5 * (0.55 + 0.45 * var10)));
         }
      }
   }

   private void V(fz var1, AABB var2, double var3) {
      double var5 = this.Yg.w("Fill") ? 1.8 : 1.0;
      int var7 = vv.O(this.X, 0.16 * var5 * var3);
      int var8 = vv.O(this.H, 0.035 * var5 * var3);
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

   private void U(fz var1, double var2) {
      double var4 = 1.0 - Math.pow(1.0 - var2, 3.0);
      double var6 = Math.pow(1.0 - var2, 1.5);
      Vec3 var8 = this.Q;
      double var9 = this.i * 1.5 * (1.0 + var4 * 2.2);
      double var11 = var8.y + 0.03;
      var1.l(var8.x, var11, var8.z, var9, 0.05 + 0.22 * (1.0 - var2), vv.O(this.X, 0.9 * var6));
      var1.l(var8.x, var11, var8.z, var9 * 0.7, 0.12, vv.O(this.H, 0.6 * var6));
      AABB var13 = new AABB(var8.x - this.i, var8.y, var8.z - this.i, var8.x + this.i, var8.y + this.T, var8.z + this.i).inflate(0.35 * var4);
      int var14 = vv.O(vv.Q(this.X, this.H, 0.5), var6 * var6);

      for (int[] var18 : V) {
         Vec3 var19 = x(var13, var18[0]);
         Vec3 var20 = x(var13, var18[1]);
         var1.h(var19.x, var19.y, var19.z, var20.x, var20.y, var20.z, 0.05, 0.05, var14, var14);
      }

      int var23 = vv.O(vv.Q(this.H, -1, 0.5), var6);

      for (int var24 = 0; var24 < 12; var24++) {
         double var25 = var24 * Math.PI / 6.0 + var2;
         double var26 = Math.cos(var25);
         double var21 = Math.sin(var25);
         var1.a(
            var8.x + var26 * var9,
            var11 + 0.1 + var24 % 2 * var4 * 0.4,
            var8.z + var21 * var9,
            0.02 + 0.06 * (1.0 - var2),
            var25,
            var23,
            com.larpclient.l.b.i,
            var26,
            0.0,
            var21
         );
      }
   }

   private void K() {
      if (this.isRuntimeActive() && this.isInGame() && this.v != null && !this.Yg.w("None")) {
         int var1 = wp.Q() && wp.Z(this.effectName()) ? 1 : 0;
         AABB var2 = var1 != 0 && this.m != null && this.W == this.v.getId()
            ? new AABB(this.m.x - this.g, this.m.y, this.m.z - this.YG, this.m.x + this.g, this.m.y + this.Y3, this.m.z + this.YG)
            : this.G();
         if (var2 != null) {
            int var3 = this.R.getValue() & 16777215;
            if (var1 == 0) {
               int var4 = this.Yg.w("Fill") ? 0 : -520093696 | var3;
               int var5 = this.Yg.w("Wireframe") ? 0 : 754974720 | var3;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var4, 1.0F, var5)).setAlwaysOnTop();
            } else if (this.J.getValue()) {
               int var6 = this.Yg.w("Fill") ? 0 : 1174405120 | var3;
               int var7 = this.Yg.w("Fill") ? 503316480 | var3 : 0;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var6, 1.0F, var7)).setAlwaysOnTop();
            }
         }
      }
   }

   private static boolean Y(ClientboundRemoveEntitiesPacket var0, int var1) {
      return var0.entityIds().contains(var1);
   }

   private static Vec3 E(VecDeltaCodec var0, ClientboundMoveEntityPacket var1) {
      return var1.getPositionDelta().decode(var0).endPosition();
   }

   private static Vec3 y(ClientboundEntityPositionSyncPacket var0) {
      return var0.position().endPosition();
   }
}
