package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.config.HitEffectConfig;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.render.misc.VisualsRenderer;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.render.overlay.HighlightEffect;
import com.larpclient.render.particle.ParticleSystem;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.data.HeldActionQueue;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.math.CatmullRomSpline;
import com.larpclient.util.math.Easing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
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

public final class BacktrackModule extends Module implements HighlightEffect {
   private static BacktrackModule instance;
   private static final int maxQueueSize = 65536;
   private static final long startNanos = System.nanoTime();
   private final ModeSetting version = this.addSetting(new ModeSetting("Version", "Packet delay algorithm", "New", "Old", "New"));
   private final NumberSetting rangeMin = this.addSetting(new NumberSetting("Range Min", "Lower bound of the box distance to the target", 1.0, 0.0, 10.0, 0.1));
   private final NumberSetting rangeMax = this.addSetting(new NumberSetting("Range Max", "Upper bound of the box distance to the target", 3.0, 0.0, 10.0, 0.1));
   private final NumberSetting delayMin = this.addSetting(new NumberSetting("Delay Min", "Shortest hold window in milliseconds", 280.0, 0.0, 2000.0, 1.0));
   private final NumberSetting delayMax = this.addSetting(new NumberSetting("Delay Max", "Longest hold window in milliseconds", 480.0, 0.0, 2000.0, 1.0));
   private final NumberSetting nextDelayMin = this.addSetting(
      new NumberSetting("Next Backtrack Delay Min", "Shortest wait before the next window in milliseconds", 0.0, 0.0, 2000.0, 1.0)
   );
   private final NumberSetting nextDelayMax = this.addSetting(
      new NumberSetting("Next Backtrack Delay Max", "Longest wait before the next window in milliseconds", 10.0, 0.0, 2000.0, 1.0)
   );
   private final NumberSetting trackingBuffer = this.addSetting(
      new NumberSetting("Tracking Buffer", "Keep windows available after the target leaves range, in milliseconds", 500.0, 0.0, 2000.0, 1.0)
   );
   private final NumberSetting chance = this.addSetting(new NumberSetting("Chance", "Chance that an attack allows windows, in percent", 50.0, 0.0, 100.0, 1.0));
   private final BooleanSetting pauseOnHurtTime = this.addSetting(new BooleanSetting("Pause On Hurt Time", "Skip windows while the target is hurt", false));
   private final NumberSetting hurtTime = this.addSetting(new NumberSetting("Hurt Time", "Target hurt time that pauses windows", 3.0, 0.0, 10.0, 1.0));
   private final ModeSetting targetMode = this.addSetting(
      new ModeSetting("Target Mode", "Use the attacked entity or the nearest one in range", "Attack", "Attack", "Range")
   );
   private final NumberSetting timeSinceLastAttack = this.addSetting(
      new NumberSetting("Time Since Last Attack", "How long an attack keeps windows available, in milliseconds", 1000.0, 0.0, 5000.0, 1.0)
   );
   private final BooleanSetting releaseOutOfReach = this.addSetting(
      new BooleanSetting("Release Out Of Reach", "Release held packets as soon as the target's server position is too far to hit", true)
   );
   private final NumberSetting releaseDistance = this.addSetting(
      new NumberSetting(
         "Release Distance", "Server position distance from your eyes that releases held packets. Vanilla ignores hits past 6", 6.0, 1.0, 10.0, 0.1
      )
   );
   private final ModeSetting esp = this.addSetting(new ModeSetting("ESP", "Draw the target's server position", "Box", "Box", "Wireframe", "Fill", "None"));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Server position box color", -1));
   private final ColorSetting gradient = this.addSetting(new ColorSetting("Gradient", "Second ESP color, blended toward the top", -6862849));
   private final BooleanSetting ghostModel = this.addSetting(new BooleanSetting("Ghost Model", "Draw a hologram of the target at its server position", true));
   private final BooleanSetting tether = this.addSetting(new BooleanSetting("Tether", "Link the displayed target to its server position", true));
   private final BooleanSetting bloom = this.addSetting(new BooleanSetting("Bloom", "Glow pass for the ESP", true));
   private final BooleanSetting throughWalls = this.addSetting(new BooleanSetting("Through Walls", "Keep a faint outline visible behind blocks", true));
   private final NumberSetting newRangeMin = this.addSetting(
      new NumberSetting("New Range Min", "Lower bound of the distance to the target player", 0.0, 0.0, 10.0, 0.01)
   );
   private final NumberSetting newRangeMax = this.addSetting(
      new NumberSetting("New Range Max", "Upper bound of the distance to the target player", 10.0, 0.0, 10.0, 0.01)
   );
   private final NumberSetting newDelay = this.addSetting(
      new NumberSetting("New Delay", "How long each held update waits, in milliseconds", 15000.0, 0.0, 15000.0, 1.0)
   );
   private final BooleanSetting onlyAttack = this.addSetting(new BooleanSetting("Only Attack", "Release held updates when the target moves closer", true));
   private Entity target;
   private String activeVersion = "Old";
   private final HeldActionQueue heldPackets = new HeldActionQueue(65536);
   private final ArrayDeque<Runnable> heldPongs = new ArrayDeque<>();
   private final VecDeltaCodec targetCodec = new VecDeltaCodec();
   private boolean targetHurt;
   private boolean releasingPongs;
   private long lastInRangeTime;
   private long lastAttackTime;
   private long nextWindowTime;
   private int chanceRoll;
   private final ArrayDeque<Runnable> newQueue = new ArrayDeque<>();
   private final ArrayDeque<Long> newQueueTimes = new ArrayDeque<>();
   private VecDeltaCodec newTargetCodec;
   private boolean releasingNew;
   private ClientPacketListener packetListener;
   private ClientLevel sessionLevel;
   private volatile int holdState;
   private static final double ghostAlpha = 0.55;
   private static final int[][] boxEdges = new int[][]{{0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
   private final CatmullRomSpline.Ribbon tetherRibbon = new CatmullRomSpline.Ribbon(25);
   private boolean hasRenderState;
   private boolean bloomEnabled;
   private Object renderLevel;
   private int renderTargetId = -1;
   private int colorStart;
   private int colorEnd;
   private Vec3 displayPos;
   private double halfWidthX;
   private double halfWidthZ;
   private double boxHeight;
   private double ghostSeparation;
   private double heldAmount;
   private double releasePulse = -1.0;
   private EntityRenderState ghostState;
   private Vec3 releasePos;
   private double releaseRadius;
   private double releaseHeight;
   private double releaseTime = -10.0;

   public BacktrackModule() {
      super("Backtrack", "Backtracks player position to a hittable state", Category.COMBAT, -1);
      instance = this;
      this.rangeMin.linkUpperBound(this.rangeMax).setDisplayName("Range");
      this.delayMin.linkUpperBound(this.delayMax).setDisplayName("Delay");
      this.nextDelayMin.linkUpperBound(this.nextDelayMax).setDisplayName("Next Backtrack Delay");
      this.newRangeMin.linkUpperBound(this.newRangeMax).setDisplayName("New Range");
      this.trackingBuffer.setSuffix("ms");
      this.chance.setSuffix("%");
      this.hurtTime.setSuffix("");
      this.timeSinceLastAttack.setSuffix("ms").setDisplayName("Last Attack Time To Work");
      this.newDelay.setSuffix("ms");

      for (Setting var4 : new Setting[]{
         this.rangeMin,
         this.rangeMax,
         this.delayMin,
         this.delayMax,
         this.nextDelayMin,
         this.nextDelayMax,
         this.trackingBuffer,
         this.chance,
         this.pauseOnHurtTime,
         this.targetMode,
         this.timeSinceLastAttack
      }) {
         var4.visibleWhen(() -> this.version.isMode("Old"));
      }

      this.hurtTime.visibleWhen(() -> this.version.isMode("Old") && this.pauseOnHurtTime.getValue());
      this.releaseDistance.visibleWhen(this.releaseOutOfReach::getValue);

      for (Setting var11 : new Setting[]{this.color, this.gradient, this.ghostModel, this.tether, this.bloom, this.throughWalls}) {
         var11.visibleWhen(() -> !this.esp.isMode("None"));
      }

      for (Setting var12 : new Setting[]{this.newRangeMin, this.newRangeMax, this.newDelay, this.onlyAttack}) {
         var12.visibleWhen(() -> this.version.isMode("New"));
      }

      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.renderGizmos());
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      }

      if (this.target == null) {
         return "Waiting for target";
      }

      int var1 = this.heldPackets.size() + this.newQueue.size();
      return "Tracking " + this.target.getName().getString() + (var1 > 0 ? " · " + var1 + " held" : "");
   }

   @Override
   public void onEnable() {
      this.activeVersion = this.version.getValue();
      this.chanceRoll = ThreadLocalRandom.current().nextInt(0, 101);
      this.releaseIfOutOfReach(false, false, false, null);
      this.resetNew(false, false, null);
      this.clear();
      this.updateHoldState();
   }

   @Override
   public void onDisable() {
      this.holdState = 0;
      this.releaseIfOutOfReach(true, false, true, "Stopped");
      this.resetNew(true, false, "Stopped");
      this.clear();
   }

   private void updateHoldState() {
      this.holdState = !this.isRuntimeActive()
         ? 0
         : (this.version.isMode("Old") ? (this.target == null && this.heldPackets.size() <= 0 ? 0 : 1) : (!this.newQueue.isEmpty() ? 2 : 0));
   }

   public static void onReceive(Packet<?> var0, PacketListener var1) {
      if (instance != null && instance.isEnabled()) {
         BacktrackModule var2 = instance;
         if (var2 != null && var2.holdState != 0 && var1 instanceof ClientPacketListener var3) {
            Minecraft var4 = getMc();
            if (var4 != null && !var4.isSameThread()) {
               if (var2.holdState == 1 ? boxDistanceSq(var0) : var0 instanceof ClientboundDisconnectPacket) {
                  PacketUtils.ensureRunningOnSameThread(var0, var3, var4.packetProcessor());
               }
            }
         }
      }
   }

   static boolean boxDistanceSq(Packet<?> var0) {
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

   public static boolean hasHeldPackets() {
      BacktrackModule var0 = instance;
      return var0 != null && var0.heldPackets.size() > 0;
   }

   private static long now() {
      return (System.nanoTime() - startNanos) / 1000000L;
   }

   private void checkVersionChange() {
      String var1 = this.version.getValue();
      if (!var1.equals(this.activeVersion)) {
         this.releaseIfOutOfReach(true, false, false, "Version changed");
         this.resetNew(true, false, "Version changed");
         this.activeVersion = var1;
      }
   }

   private boolean updateTarget() {
      Minecraft var1 = getMc();
      return this.packetListener != null
         && var1.getConnection() == this.packetListener
         && var1.level == this.sessionLevel
         && this.packetListener.getConnection().isConnected();
   }

   private void captureSession() {
      if (this.heldPackets.size() <= 0 && this.heldPongs.isEmpty() && this.newQueue.isEmpty()) {
         this.packetListener = minecraft.getConnection();
         this.sessionLevel = minecraft.level;
      }
   }

   public static boolean boxDistanceSq() {
      BacktrackModule var0 = instance;
      return var0 != null && (var0.heldPackets.isFlushing() || var0.releasingNew || var0.releasingPongs);
   }

   public static boolean releaseHeld(Packet<?> var0, PacketListener var1, Runnable var2) {
      if (instance != null && instance.isEnabled()) {
         BacktrackModule var3 = instance;
         Minecraft var4 = getMc();
         if (var3 != null
            && !boxDistanceSq()
            && var4.isSameThread()
            && !(var0 instanceof ClientboundBundlePacket)
            && var1 instanceof ClientPacketListener
            && var1 == var4.getConnection()
            && var3.isRuntimeActive()) {
            try {
               var3.checkVersionChange();
               return var3.version.isMode("New") ? var3.holdNewPacket(var0, var2) : var3.holdOldPacket(var0, var2);
            } finally {
               var3.updateHoldState();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean interceptSend(Connection var0, Packet<?> var1, ChannelFutureListener var2, boolean var3) {
      if (instance != null && instance.isEnabled()) {
         BacktrackModule var4 = instance;
         Minecraft var5 = getMc();
         if (var4 != null
            && !boxDistanceSq()
            && var5 != null
            && var5.isSameThread()
            && var4.isRuntimeActive()
            && var5.player != null
            && var5.level != null
            && var5.getConnection() != null
            && !var5.isLocalServer()
            && var0 == var5.getConnection().getConnection()) {
            var4.checkVersionChange();
            if (!var4.version.isMode("New") && var1 instanceof ServerboundPongPacket && var4.heldPackets.size() != 0) {
               var4.heldPongs.addLast(() -> var0.send(var1, var2, var3));
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

   public static boolean isReleasingPongs() {
      BacktrackModule var0 = instance;
      return var0 != null && var0.releasingPongs;
   }

   public static void onAttack(Entity var0) {
      if (instance != null && instance.isEnabled()) {
         BacktrackModule var1 = instance;
         if (var1 != null && var0 != null && var1.isRuntimeActive() && var1.isInGame()) {
            try {
               var1.checkVersionChange();
               if (!var1.version.isMode("New")) {
                  var1.lastAttackTime = now();
                  var1.chanceRoll = ThreadLocalRandom.current().nextInt(0, 101);
                  if (var1.targetMode.isMode("Attack")) {
                     var1.updateTarget(var0);
                  }

                  return;
               }

               var1.setNewTarget(var0);
            } finally {
               var1.updateHoldState();
            }
         }
      }
   }

   @Override
   public void onTick() {
      try {
         this.onAttack();
      } finally {
         this.updateHoldState();
      }
   }

   private void onAttack() {
      this.checkVersionChange();
      if (this.version.isMode("New")) {
         this.tickNew();
      } else if (minecraft.player != null && minecraft.level != null) {
         if (this.targetMode.isMode("Range")) {
            Entity var1 = this.releaseIfOutOfReach();
            if (var1 == null) {
               this.releaseIfOutOfReach(true, false, true, "No target in range");
            } else {
               this.updateTarget(var1);
            }
         }

         int var2 = this.heldPackets.size() > 0 ? 1 : 0;
         if (var2 == 0 || !this.releaseIfOutOfReach(this.serverBox())) {
            if (this.hasValidTarget()) {
               if (var2 != 0 && this.heldPackets.isHoldExpired(now())) {
                  this.releaseHeld("Window ended");
                  this.releaseIfOutOfReach(false, false, true, null);
               }
            } else if (var2 != 0) {
               this.releaseHeld("Target no longer eligible");
               this.releaseIfOutOfReach(false, false, true, null);
            }
         }
      } else {
         this.releaseIfOutOfReach(false, true, false, "World unavailable");
      }
   }

   private boolean holdOldPacket(Packet<?> var1, Runnable var2) {
      if (minecraft.player == null || minecraft.level == null || minecraft.getConnection() == null || minecraft.isLocalServer()) {
         return false;
      }

      if (isChatPacket(var1)) {
         return false;
      }

      if (this.drawFill(var1)) {
         this.releaseIfOutOfReach(true, false, true, "Server boundary packet");
         return false;
      }

      boolean var3 = this.hasValidTarget();
      int var4 = this.heldPackets.size() > 0 ? 1 : 0;
      if (var4 == 0 && !var3) {
         return false;
      }

      Entity var5 = this.target;
      if (var4 == 0 && !(var1 instanceof ClientboundPingPacket)) {
         this.trackTargetPosition(var1, var5);
         return false;
      }

      if (var5 != null) {
         Vec3 var6 = this.trackTargetPosition(var1, var5);
         if (var6 != null && this.releaseIfOutOfReach(var5.getType().getDimensions().makeBoundingBox(var6))) {
            return false;
         }

         if (var6 != null && this.hasMovedCloser(var5, var6)) {
            this.releaseHeld("Target moved closer");
            return false;
         }
      }

      if (var4 == 0) {
         if (var5 != null && this.setNewTarget(this.serverBox())) {
            return false;
         }

         this.captureSession();
         long var8 = randomDelay(this.delayMin, this.delayMax);
         if (this.heldPackets.isHolding()) {
            this.heldPackets.cancel();
         }

         this.heldPackets.beginHold(now(), var8);
         LarpLog.debug("Backtrack", "Old window opened for {} ms", var8);
      }

      if (this.heldPackets.enqueue(var2)) {
         return true;
      }

      this.releaseHeld("Packet capacity reached");
      return false;
   }

   private static boolean isChatPacket(Packet<?> var0) {
      return var0 instanceof ClientboundPlayerChatPacket
         || var0 instanceof ClientboundSystemChatPacket
         || var0 instanceof ClientboundDisguisedChatPacket
         || var0 instanceof ClientboundSetActionBarTextPacket
         || var0 instanceof ClientboundSoundPacket var1 && var1.getSound().value() == SoundEvents.PLAYER_HURT;
   }

   private boolean drawFill(Packet<?> var1) {
      return var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F
         || var1 instanceof ClientboundRemoveEntitiesPacket var2 && this.target != null && removesEntity(var2, this.target.getId());
   }

   private Vec3 trackTargetPosition(Packet<?> var1, Entity var2) {
      return var2 != null && minecraft.level != null ? decodePosition(this.targetCodec, var1, minecraft.level, var2) : null;
   }

   static Vec3 decodePosition(VecDeltaCodec var0, Packet<?> var1, ClientLevel var2, Entity var3) {
      Vec3 var4 = null;
      if (var1 instanceof ClientboundMoveEntityPacket var5 && var5.hasPosition() && var5.getEntity(var2) == var3) {
         var4 = boxDistanceSq(var0, var5);
      } else if (var1 instanceof ClientboundTeleportEntityPacket var6 && var6.id() == var3.getId()) {
         var4 = var6.relatives().isEmpty()
            ? var6.change().position()
            : PositionMoveRotation.calculateAbsolute(
                  new PositionMoveRotation(var0.getBase(), var3.getDeltaMovement(), var3.getYRot(), var3.getXRot()), var6.change(), var6.relatives()
               )
               .position();
      } else if (var1 instanceof ClientboundEntityPositionSyncPacket var7 && var7.id() == var3.getId()) {
         var4 = decodePosition(var7);
      }

      if (var4 != null) {
         var0.setBase(var4);
      }

      return var4;
   }

   private boolean hasMovedCloser(Entity var1, Vec3 var2) {
      AABB var3 = var1.getType().getDimensions().makeBoundingBox(var2);
      AABB var4 = minecraft.player.getBoundingBox();
      return boxDistanceSq(var4, var3) < boxDistanceSq(var4, var1.getBoundingBox());
   }

   private boolean hasValidTarget() {
      return this.target != null && this.target.isAlive() && this.drawEdgeGlow(this.target);
   }

   private boolean drawEdgeGlow(Entity var1) {
      if (minecraft.player != null && var1 != null) {
         boolean var2 = this.isInRange(var1);
         long var3 = now();
         if (var2) {
            this.lastInRangeTime = var3;
         }

         return (var2 || this.lastInRangeTime != 0L && var3 - this.lastInRangeTime <= this.trackingBuffer.getValue().longValue())
            && this.isValidTarget(var1)
            && minecraft.player.tickCount > 10
            && this.chanceRoll < this.chance.getValue()
            && var3 >= this.nextWindowTime
            && (!this.pauseOnHurtTime.getValue() || !this.targetHurt)
            && this.lastAttackTime != 0L
            && var3 - this.lastAttackTime <= this.timeSinceLastAttack.getValue().longValue();
      } else {
         return false;
      }
   }

   private boolean isValidTarget(Entity var1) {
      if (!(var1 instanceof LivingEntity var2 && minecraft.player != null && var1 != minecraft.player)) {
         return false;
      } else {
         if (!var2.isAlive() || var2.isRemoved() || var2.isSpectator() || !var2.isPickable()) {
            return false;
         }

         if (!(var1 instanceof Player)) {
            return true;
         }

         MCFriendModule var3 = LarpClient.getInstance().getModuleManager().getModule(MCFriendModule.class);
         return var3 == null || !var3.isFriend(var1.getName().getString());
      }
   }

   private boolean isInRange(Entity var1) {
      double var2 = Math.sqrt(boxDistanceSq(minecraft.player.getBoundingBox(), var1.getBoundingBox()));
      return var2 >= Math.min(this.rangeMin.getValue(), this.rangeMax.getValue()) && var2 <= Math.max(this.rangeMin.getValue(), this.rangeMax.getValue());
   }

   private Entity releaseIfOutOfReach() {
      double var1 = Math.min(this.rangeMin.getValue(), this.rangeMax.getValue());
      double var3 = Math.max(this.rangeMin.getValue(), this.rangeMax.getValue());
      AABB var5 = minecraft.player.getBoundingBox();
      Entity var6 = null;
      double var7 = Double.MAX_VALUE;

      for (Entity var10 : minecraft.level.getEntities(minecraft.player, var5.inflate(var3 + 1.0), this::isValidTarget)) {
         double var11 = Math.sqrt(boxDistanceSq(var5, var10.getBoundingBox()));
         if (var11 >= var1 && var11 <= var3 && var11 < var7) {
            var6 = var10;
            var7 = var11;
         }
      }

      return var6;
   }

   private void updateTarget(Entity var1) {
      this.targetHurt = (boolean)(var1 instanceof LivingEntity var2 && var2.hurtTime >= this.hurtTime.getValue() ? 1 : 0);
      if (this.drawEdgeGlow(var1)) {
         if (var1 != this.target) {
            this.releaseIfOutOfReach(true, false, false, "Target changed");
            this.targetCodec.setBase(var1.getPositionCodec().getBase());
         }

         this.target = var1;
      }
   }

   private void releaseIfOutOfReach(boolean var1, boolean var2, boolean var3, String var4) {
      if (var1 && !var2) {
         this.releaseHeld(var4);
      } else if (var2) {
         this.discardHeld(var4);
      }

      if (this.target != null && var3) {
         this.nextWindowTime = now() + randomDelay(this.nextDelayMin, this.nextDelayMax);
      }

      this.target = null;
      this.targetHurt = (boolean)0;
      this.targetCodec.setBase(Vec3.ZERO);
   }

   private void releaseHeld(String var1) {
      int var2 = this.heldPackets.size() + this.heldPongs.size();
      if (var2 == 0) {
         this.heldPackets.cancel();
      } else if (minecraft.getConnection() != null && this.updateTarget()) {
         this.releasingPongs = (boolean)1;

         try {
            while (!this.heldPongs.isEmpty() && this.updateTarget()) {
               this.heldPongs.removeFirst().run();
            }
         } finally {
            this.releasingPongs = (boolean)0;
            this.heldPongs.clear();
         }

         this.heldPackets.flush(this::updateTarget);
         if (var1 != null) {
            LarpLog.debug("Backtrack", "Released {} held packets: {}", var2, var1);
         }
      } else {
         this.discardHeld(var1 + "; session changed");
      }
   }

   private void discardHeld(String var1) {
      int var2 = this.heldPackets.size() + this.heldPongs.size();
      this.heldPackets.cancel();
      this.heldPongs.clear();
      if (var2 > 0 && var1 != null) {
         LarpLog.debug("Backtrack", "Discarded {} held packets: {}", var2, var1);
      }
   }

   private void tickNew() {
      if (minecraft.player != null && minecraft.level != null) {
         if (!this.isTargetInNewRange()) {
            this.resetNew(true, false, "Target left New range");
         } else if (!this.releaseIfOutOfReach(this.serverBox())) {
            this.releaseNew(false, null);
         }
      } else {
         this.resetNew(true, true, "World unavailable");
      }
   }

   private boolean holdNewPacket(Packet<?> var1, Runnable var2) {
      if (minecraft.player == null || minecraft.level == null) {
         this.resetNew(true, true, "World unavailable");
         return false;
      }

      if (var1 instanceof ClientboundPlayerPositionPacket
         || var1 instanceof ClientboundDisconnectPacket
         || var1 instanceof ClientboundSetHealthPacket var3 && var3.getHealth() <= 0.0F) {
         this.resetNew(true, false, "Server boundary packet");
         return false;
      }

      if (this.newQueue.isEmpty() && !this.isTargetInNewRange()) {
         return false;
      }

      this.trackNewPosition(var1);
      if (this.target == null || discardHeld(var1) || !isDelayablePacket(var1)) {
         return false;
      }

      if (this.newQueue.size() >= 65536) {
         this.releaseNew(true, "Packet capacity reached");
         return false;
      }

      if (this.newQueue.isEmpty()) {
         this.captureSession();
         LarpLog.debug("Backtrack", "New queue opened with {} ms delay", this.newDelay.getValue().longValue());
      }

      this.newQueue.addLast(var2);
      this.newQueueTimes.addLast(now());
      return true;
   }

   private static boolean discardHeld(Packet<?> var0) {
      return var0 instanceof ClientboundSoundPacket || var0 instanceof ClientboundSetHealthPacket || var0 instanceof ClientboundSetEntityDataPacket;
   }

   private static boolean isDelayablePacket(Packet<?> var0) {
      return var0 instanceof ClientboundPingPacket
         || var0 instanceof ClientboundRotateHeadPacket
         || var0 instanceof ClientboundTeleportEntityPacket
         || var0 instanceof ClientboundEntityPositionSyncPacket
         || var0 instanceof ClientboundSetEntityMotionPacket
         || var0 instanceof ClientboundMoveEntityPacket
         || var0 instanceof ClientboundLevelParticlesPacket
         || var0 instanceof ClientboundExplodePacket;
   }

   private void trackNewPosition(Packet<?> var1) {
      if (minecraft.player != null && minecraft.level != null && this.target != null) {
         Vec3 var2 = null;
         if (var1 instanceof ClientboundMoveEntityPacket var3
            && var3.hasPosition()
            && var3.getEntity(minecraft.level) == this.target
            && this.newTargetCodec != null) {
            var2 = boxDistanceSq(this.newTargetCodec, var3);
         } else if (var1 instanceof ClientboundTeleportEntityPacket var4 && var4.id() == this.target.getId()) {
            var2 = var4.change().position();
         } else if (var1 instanceof ClientboundEntityPositionSyncPacket var5 && var5.id() == this.target.getId()) {
            var2 = decodePosition(var5);
         }

         if (var2 != null) {
            if (this.newTargetCodec == null) {
               this.newTargetCodec = new VecDeltaCodec();
            }

            this.newTargetCodec.setBase(var2);
            AABB var6 = this.target.getBoundingBox().move(var2.subtract(this.target.position()));
            if (!this.releaseIfOutOfReach(var6) && this.onlyAttack.getValue()) {
               Vec3 var7 = minecraft.player.position();
               if (onAttack(var7, var6) < onAttack(var7, this.target.getBoundingBox())) {
                  this.releaseNew(true, "Target moved closer");
               }
            }
         }
      }
   }

   private boolean isTargetInNewRange() {
      return this.target != null && this.target.isAlive() && this.serverBox(this.target);
   }

   private boolean serverBox(Entity var1) {
      if (minecraft.player != null && var1 instanceof Player && var1 != minecraft.player) {
         double var2 = onAttack(minecraft.player.position(), var1.getBoundingBox());
         double var4 = Math.min(this.newRangeMin.getValue(), this.newRangeMax.getValue());
         double var6 = Math.max(this.newRangeMin.getValue(), this.newRangeMax.getValue());
         return var2 >= var4 * var4 && var2 <= var6 * var6;
      } else {
         return false;
      }
   }

   private void setNewTarget(Entity var1) {
      if (this.serverBox(var1)) {
         if (var1 != this.target) {
            this.resetNew(true, false, "Target changed");
            this.newTargetCodec = new VecDeltaCodec();
            this.newTargetCodec.setBase(var1.position());
            LarpLog.debug("Backtrack", "New target {}", var1.getName().getString());
         }

         this.target = var1;
      }
   }

   private void resetNew(boolean var1, boolean var2, String var3) {
      if (var2) {
         this.now(var3);
      } else if (var1) {
         this.releaseNew(true, var3);
      }

      this.target = null;
      this.newTargetCodec = null;
   }

   private void releaseNew(boolean var1, String var2) {
      if (!this.releasingNew && !this.newQueue.isEmpty()) {
         if (minecraft.getConnection() != null && this.updateTarget()) {
            long var3 = now();
            long var5 = this.newDelay.getValue().longValue();
            int var7 = 0;
            this.releasingNew = (boolean)1;

            try {
               while (!this.newQueue.isEmpty() && (var1 || var3 - this.newQueueTimes.peekFirst() >= var5) && this.updateTarget()) {
                  this.newQueueTimes.removeFirst();
                  this.newQueue.removeFirst().run();
                  var7++;
               }

               if (!this.updateTarget()) {
                  this.now("Session changed during release");
               }
            } catch (RuntimeException | Error var12) {
               this.newQueue.clear();
               this.newQueueTimes.clear();
               throw var12;
            } finally {
               this.releasingNew = (boolean)0;
            }

            if (var7 > 0 && var2 != null) {
               LarpLog.debug("Backtrack", "Released {} held updates: {}", var7, var2);
            }
         } else {
            this.now(var2 == null ? "Session changed" : var2 + "; session changed");
         }
      }
   }

   private void now(String var1) {
      int var2 = this.newQueue.size();
      this.newQueue.clear();
      this.newQueueTimes.clear();
      if (var2 > 0 && var1 != null) {
         LarpLog.debug("Backtrack", "Discarded {} held updates: {}", var2, var1);
      }
   }

   static long randomDelay(NumberSetting var0, NumberSetting var1) {
      int var2 = Math.round((float)Math.min(var0.getValue(), var1.getValue()));
      int var3 = Math.round((float)Math.max(var0.getValue(), var1.getValue()));
      return var3 > var2 ? ThreadLocalRandom.current().nextInt(var2, var3 + 1) : Math.max(0, var2);
   }

   static double boxDistanceSq(AABB var0, AABB var1) {
      double var2 = Math.max(0.0, Math.max(var0.minX - var1.maxX, var1.minX - var0.maxX));
      double var4 = Math.max(0.0, Math.max(var0.minY - var1.maxY, var1.minY - var0.maxY));
      double var6 = Math.max(0.0, Math.max(var0.minZ - var1.maxZ, var1.minZ - var0.maxZ));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   static double onAttack(Vec3 var0, AABB var1) {
      return var0.distanceToSqr(
         new Vec3(Mth.clamp(var0.x, var1.minX, var1.maxX), Mth.clamp(var0.y, var1.minY, var1.maxY), Mth.clamp(var0.z, var1.minZ, var1.maxZ))
      );
   }

   private boolean setNewTarget(AABB var1) {
      if (var1 != null && minecraft.player != null && this.releaseOutOfReach.getValue()) {
         double var2 = this.releaseDistance.getValue();
         return onAttack(minecraft.player.getEyePosition(), var1) > var2 * var2;
      } else {
         return false;
      }
   }

   private boolean releaseIfOutOfReach(AABB var1) {
      if (!this.setNewTarget(var1)) {
         return false;
      }

      String var2 = String.format(Locale.ROOT, "Target out of reach (%.1f blocks)", Math.sqrt(onAttack(minecraft.player.getEyePosition(), var1)));
      if (this.heldPackets.size() + this.newQueue.size() > 0) {
         this.randomDelay(var1);
      }

      if (this.version.isMode("New")) {
         this.resetNew(true, false, var2);
      } else {
         this.releaseHeld(var2);
         this.releaseIfOutOfReach(false, false, true, null);
      }

      return true;
   }

   private AABB serverBox() {
      if (this.target == null) {
         return null;
      } else if (this.version.isMode("New")) {
         return this.newTargetCodec == null ? null : this.target.getBoundingBox().move(this.newTargetCodec.getBase().subtract(this.target.position()));
      } else {
         return this.target.isAlive() && !this.targetCodec.getBase().equals(Vec3.ZERO)
            ? this.target.getType().getDimensions().makeBoundingBox(this.targetCodec.getBase())
            : null;
      }
   }

   private void randomDelay(AABB var1) {
      this.releasePos = new Vec3((var1.minX + var1.maxX) / 2.0, var1.minY, (var1.minZ + var1.maxZ) / 2.0);
      this.releaseRadius = Math.max(var1.getXsize(), var1.getZsize()) / 2.0;
      this.releaseHeight = var1.getYsize();
      this.releaseTime = TickTimer.getTime();
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
      return !this.isEnabled() ? false : this.hasRenderState && this.bloomEnabled;
   }

   @Override
   public void clear() {
      this.hasRenderState = (boolean)0;
      this.displayPos = this.releasePos = null;
      this.ghostState = null;
      this.renderLevel = null;
      this.renderTargetId = -1;
      this.heldAmount = this.ghostSeparation = 0.0;
      this.releasePulse = -1.0;
      this.releaseTime = -10.0;
      this.tetherRibbon.clear();
   }

   @Override
   public void prepare(float var1, CameraRenderState var2) {
      if (this.isEnabled()) {
         this.hasRenderState = (boolean)0;
         this.ghostState = null;
         this.tetherRibbon.clear();
         if (this.isInGame() && !this.esp.isMode("None")) {
            double var3 = TickTimer.getTime();
            double var5 = TickTimer.getDeltaTime();
            this.bloomEnabled = this.bloom.getValue();
            this.colorStart = this.color.getValue();
            this.colorEnd = this.gradient.getValue();
            this.releasePulse = this.releasePos != null && var3 - this.releaseTime < 0.55 ? Math.max(0.0, var3 - this.releaseTime) / 0.55 : -1.0;
            AABB var7 = this.serverBox();
            if (var7 == null) {
               this.displayPos = null;
               this.renderTargetId = -1;
               this.heldAmount = 0.0;
               this.hasRenderState = (boolean)(this.releasePulse >= 0.0 ? 1 : 0);
            } else {
               Vec3 var8 = new Vec3((var7.minX + var7.maxX) / 2.0, var7.minY, (var7.minZ + var7.maxZ) / 2.0);
               int var9 = this.displayPos != null
                     && this.renderTargetId == this.target.getId()
                     && this.renderLevel == minecraft.level
                     && this.displayPos.distanceToSqr(var8) < 16.0
                  ? 1
                  : 0;
               this.displayPos = var9 != 0 ? this.displayPos.lerp(var8, 1.0 - Math.exp(-var5 * 24.0)) : var8;
               this.renderTargetId = this.target.getId();
               this.renderLevel = minecraft.level;
               this.halfWidthX = var7.getXsize() / 2.0;
               this.halfWidthZ = var7.getZsize() / 2.0;
               this.boxHeight = var7.getYsize();
               this.heldAmount = this.heldAmount
                  + ((this.heldPackets.size() + this.newQueue.size() > 0 ? 1 : 0) - this.heldAmount) * (1.0 - Math.exp(-var5 * 10.0));
               Vec3 var10 = this.target.getPosition(var1);
               this.ghostSeparation = Easing.smoothstep((var10.distanceTo(this.displayPos) - 0.05) / 0.45);
               if (this.ghostModel.getValue() && this.ghostSeparation > 0.01) {
                  EntityRenderState var11 = ModelVisualRenderer.extractEntity(this.target, var1);
                  var11.x = this.displayPos.x;
                  var11.y = this.displayPos.y;
                  var11.z = this.displayPos.z;
                  var11.nameTag = null;
                  var11.scoreText = null;
                  var11.leashStates = null;
                  var11.displayFireAnimation = (boolean)0;
                  var11.shadowPieces.clear();
                  var11.outlineColor = 0;
                  this.ghostState = var11;
               }

               if (this.tether.getValue() && this.ghostSeparation > 0.01) {
                  Vec3 var22 = var10.add(0.0, this.boxHeight * 0.55, 0.0);
                  Vec3 var12 = this.displayPos.add(0.0, this.boxHeight * 0.55, 0.0);
                  double var13 = Math.min(0.3, var22.distanceTo(var12) * 0.12);

                  for (int var15 = 0; var15 <= 24; var15++) {
                     double var16 = var15 / 24.0;
                     double var18 = Math.pow(0.5 + 0.5 * Math.cos((var16 * 2.5 - var3 * 1.6) * Math.PI * 2.0), 6.0);
                     double var20 = Easing.smoothstep(var16 / 0.12) * Easing.smoothstep((1.0 - var16) / 0.12);
                     this.tetherRibbon
                        .addPoint(
                           var22.lerp(var12, var16).add(0.0, Math.sin(Math.PI * var16) * var13, 0.0),
                           0.03 * (1.0 + 0.7 * var18) * (0.35 + 0.65 * var20),
                           ColorUtil.multiplyAlpha(ColorUtil.lerpColor(this.colorStart, this.colorEnd, var16), this.ghostSeparation * (0.3 + 0.7 * var18))
                        );
                  }

                  this.tetherRibbon.computeOffsets(var2.pos);
               }

               this.hasRenderState = (boolean)1;
            }
         } else {
            this.clear();
         }
      }
   }

   @Override
   public void render(EffectsRenderer var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.hasRenderState && (!var2 || this.bloomEnabled)) {
            long var3 = PerfProfiler.startTimer();
            double var5 = TickTimer.getTime();
            if (this.displayPos != null) {
               double var7 = 0.65 + 0.35 * this.heldAmount;
               AABB var9 = new AABB(
                     this.displayPos.x - this.halfWidthX,
                     this.displayPos.y,
                     this.displayPos.z - this.halfWidthZ,
                     this.displayPos.x + this.halfWidthX,
                     this.displayPos.y + this.boxHeight,
                     this.displayPos.z + this.halfWidthZ
                  )
                  .inflate(0.03);
               if (this.ghostState != null) {
                  var1.drawEntityModel(
                     this.ghostState,
                     ModelVisualRenderer.Material.HOLOGRAM,
                     ColorUtil.lerpColor(this.colorStart, this.colorEnd, 0.4),
                     0.55 * this.ghostSeparation * (var2 ? 0.5 : 1.0)
                  );
               }

               var1.blend(HitEffectConfig.BlendMode.ADDITIVE);
               if (!this.esp.isMode("Fill")) {
                  this.drawEdgeGlow(var1, var9, var5, var7);
               }

               if (!this.esp.isMode("Wireframe")) {
                  this.isChatPacket(var1, var9, var5, var7);
               }

               this.isReleasingPongs(var1, var9, var5, var7);
               this.isReleasingPongs(var1, var9, var5, var7, var2);
               if (this.tetherRibbon.getSize() > 1) {
                  var1.drawTrail(this.tetherRibbon, true);
               }

               var1.blend(HitEffectConfig.BlendMode.NORMAL);
               if (!var2 && !this.esp.isMode("Wireframe")) {
                  this.drawFill(var1, var9, var7);
               }
            }

            if (this.releasePulse >= 0.0) {
               var1.blend(HitEffectConfig.BlendMode.ADDITIVE);
               this.releaseIfOutOfReach(var1, this.releasePulse);
               var1.blend(HitEffectConfig.BlendMode.NORMAL);
            }

            PerfProfiler.endTimer("Backtrack ESP", var3);
         }
      }
   }

   private static Vec3 captureSession(AABB var0, int var1) {
      return new Vec3((var1 & 1) == 0 ? var0.minX : var0.maxX, (var1 & 2) == 0 ? var0.minY : var0.maxY, (var1 & 4) == 0 ? var0.minZ : var0.maxZ);
   }

   private void drawEdgeGlow(EffectsRenderer var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();

      for (int[] var11 : boxEdges) {
         Vec3 var12 = captureSession(var2, var11[0]);
         Vec3 var13 = captureSession(var2, var11[1]);

         for (int var14 = 0; var14 < 6; var14++) {
            Vec3 var15 = var12.lerp(var13, var14 / 6.0);
            Vec3 var16 = var12.lerp(var13, (var14 + 1) / 6.0);
            var1.drawGlowRibbon(
               var15.x,
               var15.y,
               var15.z,
               var16.x,
               var16.y,
               var16.z,
               0.045,
               0.045,
               this.drawEdgeGlow(var2, var7, var15, var3, var5),
               this.drawEdgeGlow(var2, var7, var16, var3, var5)
            );
         }
      }
   }

   private int drawEdgeGlow(AABB var1, Vec3 var2, Vec3 var3, double var4, double var6) {
      double var8 = Math.clamp((var3.y - var1.minY) / Math.max(1.0E-6, var1.getYsize()), 0.0, 1.0);
      double var10 = Math.atan2(var3.z - var2.z, var3.x - var2.x);
      double var12 = Math.pow(0.5 + 0.5 * Math.cos(var10 * 2.0 + var8 * 4.0 - var4 * 3.2), 10.0);
      int var14 = ColorUtil.lerpColor(this.colorStart, this.colorEnd, var8);
      return ColorUtil.multiplyAlpha(ColorUtil.lerpColor(var14, var14 | 16777215, var12 * 0.6), var6 * (0.6 + 0.4 * var12));
   }

   private void isChatPacket(EffectsRenderer var1, AABB var2, double var3, double var5) {
      double var7 = var3 * 1.6;
      double var9 = 0.5 - 0.5 * Math.cos(var7);
      double var11 = Math.sin(var7);

      for (int var13 = 0; var13 < 3; var13++) {
         double var14 = Math.clamp(var9 - Math.signum(var11) * var13 * 0.07, 0.0, 1.0);
         double var16 = var13 == 0 ? 0.85 : 0.4 / var13 * Math.abs(var11);
         int var18 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(this.colorStart, this.colorEnd, var14), var16 * var5);
         double var19 = var2.minY + var2.getYsize() * var14;
         double var21 = var13 == 0 ? 0.04 : 0.025;
         var1.drawGlowRibbon(var2.minX, var19, var2.minZ, var2.maxX, var19, var2.minZ, var21, var21, var18, var18);
         var1.drawGlowRibbon(var2.maxX, var19, var2.minZ, var2.maxX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.drawGlowRibbon(var2.maxX, var19, var2.maxZ, var2.minX, var19, var2.maxZ, var21, var21, var18, var18);
         var1.drawGlowRibbon(var2.minX, var19, var2.maxZ, var2.minX, var19, var2.minZ, var21, var21, var18, var18);
      }
   }

   private void isReleasingPongs(EffectsRenderer var1, AABB var2, double var3, double var5) {
      Vec3 var7 = var2.getCenter();
      double var8 = Math.max(var2.getXsize(), var2.getZsize()) * 0.75;
      double var10 = var2.minY + 0.02;
      double var12 = var3 / 1.3 - Math.floor(var3 / 1.3);
      var1.drawRingFlat(var7.x, var10, var7.z, var8 * (1.0 + 0.04 * Math.sin(var3 * 3.0)), 0.14, ColorUtil.multiplyAlpha(this.colorEnd, 0.7 * var5));
      var1.drawRingFlat(
         var7.x, var10, var7.z, var8 * (1.0 + var12 * 1.1), 0.1, ColorUtil.multiplyAlpha(this.colorStart, 0.5 * var5 * (1.0 - var12) * (1.0 - var12))
      );
   }

   private void isReleasingPongs(EffectsRenderer var1, AABB var2, double var3, double var5, boolean var7) {
      for (int var8 = 0; var8 < 8; var8++) {
         Vec3 var9 = captureSession(var2, var8);
         double var10 = 0.5 + 0.5 * Math.sin(var3 * 4.5 + var8 * 1.9);
         int var12 = ColorUtil.lerpColor((var8 & 2) == 0 ? this.colorStart : this.colorEnd, -1, 0.45);
         var1.drawSpriteWithType(
            var9.x,
            var9.y,
            var9.z,
            var7 ? 0.22 : 0.16,
            0.0,
            ColorUtil.multiplyAlpha(var12, (var7 ? 0.35 : 0.2) * var5),
            ParticleSystem.Style.GLOW,
            0.0,
            0.0,
            0.0
         );
         if (!var7) {
            var1.drawSprite(var9.x, var9.y, var9.z, 0.05 + 0.035 * var10, var3 * 0.8 + var8, ColorUtil.multiplyAlpha(var12, var5 * (0.55 + 0.45 * var10)));
         }
      }
   }

   private void drawFill(EffectsRenderer var1, AABB var2, double var3) {
      double var5 = this.esp.isMode("Fill") ? 1.8 : 1.0;
      int var7 = ColorUtil.multiplyAlpha(this.colorStart, 0.16 * var5 * var3);
      int var8 = ColorUtil.multiplyAlpha(this.colorEnd, 0.035 * var5 * var3);
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

   private void releaseIfOutOfReach(EffectsRenderer var1, double var2) {
      double var4 = 1.0 - Math.pow(1.0 - var2, 3.0);
      double var6 = Math.pow(1.0 - var2, 1.5);
      Vec3 var8 = this.releasePos;
      double var9 = this.releaseRadius * 1.5 * (1.0 + var4 * 2.2);
      double var11 = var8.y + 0.03;
      var1.drawRingFlat(var8.x, var11, var8.z, var9, 0.05 + 0.22 * (1.0 - var2), ColorUtil.multiplyAlpha(this.colorStart, 0.9 * var6));
      var1.drawRingFlat(var8.x, var11, var8.z, var9 * 0.7, 0.12, ColorUtil.multiplyAlpha(this.colorEnd, 0.6 * var6));
      AABB var13 = new AABB(
            var8.x - this.releaseRadius,
            var8.y,
            var8.z - this.releaseRadius,
            var8.x + this.releaseRadius,
            var8.y + this.releaseHeight,
            var8.z + this.releaseRadius
         )
         .inflate(0.35 * var4);
      int var14 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(this.colorStart, this.colorEnd, 0.5), var6 * var6);

      for (int[] var18 : boxEdges) {
         Vec3 var19 = captureSession(var13, var18[0]);
         Vec3 var20 = captureSession(var13, var18[1]);
         var1.drawGlowRibbon(var19.x, var19.y, var19.z, var20.x, var20.y, var20.z, 0.05, 0.05, var14, var14);
      }

      int var23 = ColorUtil.multiplyAlpha(ColorUtil.lerpColor(this.colorEnd, -1, 0.5), var6);

      for (int var24 = 0; var24 < 12; var24++) {
         double var25 = var24 * Math.PI / 6.0 + var2;
         double var26 = Math.cos(var25);
         double var21 = Math.sin(var25);
         var1.drawSpriteWithType(
            var8.x + var26 * var9,
            var11 + 0.1 + var24 % 2 * var4 * 0.4,
            var8.z + var21 * var9,
            0.02 + 0.06 * (1.0 - var2),
            var25,
            var23,
            ParticleSystem.Style.SPARK,
            var26,
            0.0,
            var21
         );
      }
   }

   private void renderGizmos() {
      if (this.isRuntimeActive() && this.isInGame() && this.target != null && !this.esp.isMode("None")) {
         int var1 = VisualsRenderer.isRenderingEnabled() && VisualsRenderer.isStageActive(this.effectName()) ? 1 : 0;
         AABB var2 = var1 != 0 && this.displayPos != null && this.renderTargetId == this.target.getId()
            ? new AABB(
               this.displayPos.x - this.halfWidthX,
               this.displayPos.y,
               this.displayPos.z - this.halfWidthZ,
               this.displayPos.x + this.halfWidthX,
               this.displayPos.y + this.boxHeight,
               this.displayPos.z + this.halfWidthZ
            )
            : this.serverBox();
         if (var2 != null) {
            int var3 = this.color.getValue() & 16777215;
            if (var1 == 0) {
               int var4 = this.esp.isMode("Fill") ? 0 : -520093696 | var3;
               int var5 = this.esp.isMode("Wireframe") ? 0 : 754974720 | var3;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var4, 1.0F, var5)).setAlwaysOnTop();
            } else if (this.throughWalls.getValue()) {
               int var6 = this.esp.isMode("Fill") ? 0 : 1174405120 | var3;
               int var7 = this.esp.isMode("Fill") ? 503316480 | var3 : 0;
               Gizmos.cuboid(var2, GizmoStyle.strokeAndFill(var6, 1.0F, var7)).setAlwaysOnTop();
            }
         }
      }
   }

   private static boolean removesEntity(ClientboundRemoveEntitiesPacket var0, int var1) {
      return var0.entityIds().contains(var1);
   }

   private static Vec3 boxDistanceSq(VecDeltaCodec var0, ClientboundMoveEntityPacket var1) {
      return var1.getPositionDelta().decode(var0).endPosition();
   }

   private static Vec3 decodePosition(ClientboundEntityPositionSyncPacket var0) {
      return var0.position().endPosition();
   }
}
