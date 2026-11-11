package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.gui.clickgui.ModulePanel;
import com.larpclient.gui.theme.Theme;
import com.larpclient.hud.NotificationManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.MCFriendModule;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.ModuleManager;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.player.AutoSustain;
import com.larpclient.module.player.SellMenuLogic;
import com.larpclient.module.world.WorldAutomationModule;
import com.larpclient.render.world.PearlTrajectory;
import com.larpclient.render.world.WorldPathView;
import com.larpclient.social.misc.ChatMessageClassifier;
import com.larpclient.util.entity.MovementCorrection;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.math.RotationSmoother2;
import com.larpclient.util.misc.ClickStepTimer;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.RegionAttemptScheduler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.AStarPathfinder;
import com.larpclient.util.world.AnchorPlacement;
import com.larpclient.util.world.BlockBreaker;
import com.larpclient.util.world.NativePlacement;
import com.larpclient.util.world.PathFollower;
import com.larpclient.util.world.WorldRaycaster;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AnchorHunter extends WorldAutomationModule implements ModulePanel, NativePlacement.PlacementAimer {
   private static AnchorHunter instance;
   private static boolean hooksRegistered;
   static final List<String> conflictingModules = List.of("AutoAnchor", "AnchorTap", "Anchor Macro", "AutoCrystal", "Freecam");
   static final double pearlTravelDistance = 28.0;
   static final int maxMineTicks = 900;
   static final long routeTimeoutMs = 120000L;
   static final long spotTimeoutMs = 360000L;
   static final long teleportTimeoutMs = 60000L;
   static final int maxRouteFailures = 3;
   static final int pathSearchLimit = 16000;
   private final ModeSetting regionSetting = this.addSetting(
      new ModeSetting(
         "Region",
         "Which /rtp region to use. Random takes the overworld regions in turn; the Nether is never used",
         "Random",
         RegionAttemptScheduler.regionLabels.toArray(new String[0])
      )
   );
   private final NumberSetting playerRangeSetting = this.addSliderSetting(
      "Player Range",
      "Any player closer than this makes the bot leave by the next /rtp. Servers only show players up to about 48 to 128 blocks away, so the default means anyone you can see",
      96.0,
      8.0,
      160.0,
      1.0
   );
   private final BooleanSetting ignoreFriendsSetting = this.addToggleSetting(
      "Ignore Friends", "Players on your friends list (MCFriend) do not make the bot leave", true
   );
   private final BooleanSetting logOutIfCaughtSetting = this.addToggleSetting(
      "Log Out If Caught",
      "If a player gets within 5 blocks while /rtp is still cooling down, log out instead of running. Never logs out while a player has hit you in the last 15 seconds",
      false
   );
   private final BooleanSetting digToHiddenSetting = this.addToggleSetting(
      "Dig To Hidden Anchors", "Dig a tunnel to anchors inside walls or underground. Off: only anchors you can walk up to", true
   );
   private final BooleanSetting pearlTravelSetting = this.addToggleSetting(
      "Pearl Travel", "Throw ender pearls from your hotbar to reach far anchors fast. Only while you wear armour and have enough health", true
   );
   private final BooleanSetting enderChestStashSetting = this.addToggleSetting(
      "Ender Chest Stash",
      "When your inventory is full, put the anchors in your ender chest. Needs an ender chest and a Silk Touch pickaxe in your hotbar",
      true
   );
   private final NumberSetting stopAfterSetting = this.addSliderSetting("Stop After", "Stop after this many anchors (0 never stops)", 0.0, 0.0, 2304.0, 1.0);
   private final ModeSetting notificationsSetting = this.addSetting(
      new ModeSetting("Notifications", "How the bot tells you about finds and problems", "Toast", "Toast", "Chat", "Off")
   );
   private final BooleanSetting statusCardSetting = this.addToggleSetting(
      "Status Card", "Show a card at the top of the screen with anchors found, the /rtp timer and what the bot is doing", true
   );
   private final BooleanSetting showPathSetting = this.addToggleSetting(
      "Show Path", "Draw the route, the blocks it digs, the anchor it is going to and its pearl throws", true
   );
   private final BooleanSetting fightMobsSetting = this.addToggleSetting(
      "Fight Mobs",
      "Hit zombies, spiders and other melee mobs that come for you, with the best sword or axe in your hotbar. Creepers and skeletons are still run from, and it runs at 8 health or less",
      false
   );
   private final BooleanSetting autoSellSetting = this.addToggleSetting(
      "Auto Sell Anchors",
      "After picking up an anchor, sell your anchors with /sell, standing still. Only anchors go into the Sell menu; if the menu looks wrong it stops selling for five minutes",
      false
   );
   private final RotationSmoother2 motion = new RotationSmoother2();
   private final AutoSustain autoEat = new AutoSustain(this);
   private final PathFollower pathFollower = new PathFollower(this, this.motion, this::getOnlinePlayerNames);
   private final RegionAttemptScheduler rtpTimer = new RegionAttemptScheduler();
   private final RegionAttemptScheduler.Point regionPicker = new RegionAttemptScheduler.Point();
   private final ConcurrentLinkedQueue<String> chatQueue = new ConcurrentLinkedQueue<>();
   private AnchorHunter.State state = AnchorHunter.State.INITIALIZING;
   private AnchorHunter.State resumeState = AnchorHunter.State.SCANNING;
   private boolean running;
   private Object runLevel;
   private Object runPlayer;
   private Object runConnection;
   private long teleportStartedAt = -1L;
   private String interruptReason;
   private String lastResult;
   private final List<String> disabledModules = new ArrayList<>();
   private boolean singleplayer;
   private int runTicks;
   private int stateStartTick;
   private long stateStartedAt;
   private long runStartedAt;
   private long spotStartedAt;
   private long lastRtpSentAt = -2305843009213693952L;
   private long countdownSeenAt = -1L;
   private String currentRegion;
   private boolean countdownSeen;
   private Vec3 lastPosition;
   private int anchorsCollected;
   private int teleportCount;
   private int spotCount;
   private int emptySpotCount;
   private final Map<String, Integer> skipReasons = new TreeMap<>();
   private final Map<Long, Integer> foundAnchors = new HashMap<>();
   private final Set<Long> skippedAnchors = new HashSet<>();
   private boolean areaProtected;
   private boolean playerNearby;
   private boolean fleeingMobs;
   private LivingEntity fightTarget;
   private int mobFleeCount;
   private String nearbyPlayerName = "";
   private double nearestPlayerDistance = Double.MAX_VALUE;
   private final List<double[]> dangerPoints = new ArrayList<>();
   private long lastPlayerHitAt = -1L;
   private float lastHealth = 20.0F;
   private double[] fleeTarget;
   private int fleeRepathTick;
   private BlockPos targetAnchor;
   private int targetCharges;
   private int routeFailures;
   private int mineTicks;
   private int brokenTicks;
   private int anchorReturnCount;
   private int anchorsBeforeMine;
   private int dropWaitStartTick;
   private int noSightTicks;
   private boolean anchorBroken;
   private boolean farLeg;
   private long targetSelectedAt;
   private long standingSpotReachedAt = -2305843009213693952L;
   private int digsAtTargetStart;
   private int lastRouteTick = -1000;
   private final Set<Long> standingSpots = new HashSet<>();
   private final Set<Long> rejectedSpots = new HashSet<>();
   private Boolean toolCutsThreeByThree;
   private final Set<BlockPos> probeBlocks = new HashSet<>();
   private int stashStep;
   private int stashStepTick;
   private int stashFailures;
   private int anchorsBeforeStash;
   private int enderChestsBeforePickup;
   private int pendingHotbarSlot = -1;
   private int hotbarSwitchTick;
   private int lastQuickMoveTick;
   private BlockPos enderChestPos;
   private String stashStopReason;
   private int sellStep;
   private int sellStepTick;
   private int anchorsBeforeSell;
   private int sellButtonSlot = -1;
   private int lastSellClickTick;
   private int soldCount;
   private long sellRetryAt;
   static final long sellRetryDelayMs = 300000L;
   private AnchorHunter.PearlState pearlState = AnchorHunter.PearlState.IDLE;
   private float pearlYaw;
   private float pearlPitch;
   private Vec3 pearlLanding;
   private int pearlStateTick;
   private int nextPearlTick = -1000;
   private Vec3 pearlThrowOrigin;
   private Vec3 moveDirection = Vec3.ZERO;
   private boolean jumping;
   private boolean sneaking;
   private boolean sprinting;
   private int lastTickCount = Integer.MIN_VALUE;
   private String lastNotice = "";
   private long lastNoticeAt;
   private final List<BlockPos> scanHighlights = new ArrayList<>();
   private long scanHighlightUntil;

   public AnchorHunter() {
      super("Anchor Hunter", "DonutSMP: /rtp around, find respawn anchors in loaded chunks, walk or dig to them, mine and collect them");
      instance = this;
      this.stopAfterSetting.setDisplayName("Stop After (anchors, 0 never)");
      if (!hooksRegistered) {
         hooksRegistered = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            AnchorHunter var1 = instance;
            if (var1 != null) {
               var1.setState();
            }
         });
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> getInstance(var0.getString()));
      }
   }

   public static AnchorHunter getInstance() {
      return instance;
   }

   public static void getInstance(String var0) {
      if (instance != null && instance.isEnabled()) {
         AnchorHunter var1 = instance;
         if (var1 != null && var1.running && var0 != null && !var0.isBlank() && var1.chatQueue.size() < 64) {
            var1.chatQueue.add(var0);
         }
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return minecraft == null || !(WorldPathView.currentScreen() instanceof AbstractContainerScreen) || this.isStashChestOpen();
   }

   private boolean isStashChestOpen() {
      return this.running
         && (this.state == AnchorHunter.State.STORING || this.state == AnchorHunter.State.SELLING)
         && minecraft != null
         && minecraft.player != null
         && minecraft.player.containerMenu instanceof ChestMenu;
   }

   @Override
   public void disable() {
      this.interruptReason = null;
      int var1 = this.teleportStartedAt >= 0L ? 1 : 0;
      this.teleportStartedAt = -1L;
      super.disable();
      if (var1 != 0) {
         this.endRun("turned off while teleporting");
      }
   }

   @Override
   public List<ModulePanel.Command> commands() {
      return List.of(
         new ModulePanel.Command("Before you start", "Check my setup", this::checkSetup),
         new ModulePanel.Command("Before you start", "Find anchors near me", this::scanNearbyAnchors)
      );
   }

   @Override
   public String summary() {
      if (this.isRuntimeActive() && this.running) {
         return setState(this.anchorsCollected, "anchor")
            + " in "
            + ModulePanel.formatDuration(System.currentTimeMillis() - this.runStartedAt)
            + ", "
            + setState(this.teleportCount, "teleport")
            + ". "
            + this.state.LABEL
            + ".";
      } else {
         return this.lastResult != null
            ? this.lastResult + "."
            : "Stand anywhere on DonutSMP and turn it on: it uses /rtp and mines the respawn anchors it finds.";
      }
   }

   @Override
   public String detail() {
      if (!this.isInGame()) {
         return "Needs a diamond or netherite pickaxe in your hotbar.";
      }

      int var1 = this.findPickaxeSlot(Blocks.RESPAWN_ANCHOR.defaultBlockState());
      String var2 = var1 >= 0 ? minecraft.player.getInventory().getItem(var1).getHoverName().getString() : "no diamond or netherite pickaxe";
      return "Pickaxe: "
         + var2
         + ". Ender chest "
         + (AutoSustain.findHotbarSlot(var0 -> var0.is(Items.ENDER_CHEST)) >= 0 ? "yes" : "no")
         + ", Silk Touch pickaxe "
         + (this.findSilkPickaxeSlot() >= 0 ? "yes" : "no")
         + ", pearls "
         + this.countPearls()
         + ".";
   }

   @Override
   public List<ModulePanel.SettingGroup> settingGroups() {
      return List.of(
         new ModulePanel.SettingGroup("Where to go", List.of(this.regionSetting)),
         new ModulePanel.SettingGroup("Other players", List.of(this.ignoreFriendsSetting, this.logOutIfCaughtSetting)),
         new ModulePanel.SettingGroup(
            "While hunting",
            List.of(
               this.autoSellSetting,
               this.enderChestStashSetting,
               this.stopAfterSetting,
               this.digToHiddenSetting,
               this.pearlTravelSetting,
               this.fightMobsSetting
            )
         ),
         new ModulePanel.SettingGroup("Display", List.of(this.statusCardSetting, this.showPathSetting, this.notificationsSetting)),
         ModulePanel.SettingGroup.advanced("Advanced", List.of(this.playerRangeSetting))
      );
   }

   private String checkSetup() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      if (minecraft.level.dimension() != Level.OVERWORLD) {
         var1.add("go to the overworld");
      }

      if (this.findPickaxeSlot(Blocks.RESPAWN_ANCHOR.defaultBlockState()) < 0) {
         var1.add("put a diamond or netherite pickaxe in your hotbar");
      }

      if (AutoSustain.findHotbarSlot(AnchorHunter::getPauseReason) < 0) {
         var2.add("no food in the hotbar");
      }

      if (!this.clearMovement()) {
         if (this.enderChestStashSetting.getValue() && this.getPauseReason()) {
            var2.add("your inventory is full, so it stashes in the ender chest first");
         } else {
            var1.add("make room in your inventory");
         }
      }

      if (this.enderChestStashSetting.getValue() && !this.getPauseReason()) {
         var2.add("no ender chest and Silk Touch pickaxe in the hotbar, so a full inventory stops the run");
      }

      if (this.pearlTravelSetting.getValue() && this.countPearls() == 0) {
         var2.add("no ender pearls in the hotbar, so it walks everywhere");
      } else if (this.pearlTravelSetting.getValue() && minecraft.player.getArmorValue() < 12) {
         var2.add("wear armour (12 points or more) for pearl travel");
      }

      String var3 = var1.isEmpty() ? "Ready." : "Not ready: " + String.join("; ", var1) + ".";
      return var2.isEmpty() ? var3 : var3 + " Note: " + String.join("; ", var2) + ".";
   }

   private String scanNearbyAnchors() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      HashMap var1 = new HashMap();
      int var2 = this.scanLoadedChunks(var1);
      this.scanHighlights.clear();
      Vec3 var3 = minecraft.player.getEyePosition();
      List var4 = var1.keySet()
         .stream()
         .map(var0 -> new BlockPos(AStarPathfinder.unpackX(var0), AStarPathfinder.unpackY(var0), AStarPathfinder.unpackZ(var0)))
         .sorted(Comparator.comparingDouble(var1x -> var3.distanceToSqr(Vec3.atCenterOf(var1x))))
         .toList();
      this.scanHighlights.addAll(var4.subList(0, Math.min(64, var4.size())));
      this.scanHighlightUntil = System.currentTimeMillis() + 15000L;
      LarpLog.info(this.getName(), "Scan around the player: {} anchors in {} loaded chunks", var1.size(), var2);
      if (var4.isEmpty()) {
         return "No respawn anchors in the " + var2 + " loaded chunks around you.";
      }

      BlockPos var5 = (BlockPos)var4.getFirst();
      return var1.size()
         + " anchors in the "
         + var2
         + " loaded chunks around you, shown for 15 seconds. Nearest at "
         + var5.toShortString()
         + ", "
         + (int)Math.round(Math.sqrt(var3.distanceToSqr(Vec3.atCenterOf(var5))))
         + " blocks away ("
         + var1.get(AStarPathfinder.packPos(var5.getX(), var5.getY(), var5.getZ()))
         + " charges).";
   }

   @Override
   public void onEnable() {
      super.onEnable();
      if (this.teleportStartedAt >= 0L) {
         long var1 = System.currentTimeMillis() - this.teleportStartedAt;
         this.teleportStartedAt = -1L;
         if (this.getConnection() == this.runConnection && this.runConnection != null && var1 <= 60000L && minecraft.player.isAlive()) {
            this.running = (boolean)1;
            this.runLevel = minecraft.level;
            this.runPlayer = minecraft.player;
            LarpLog.info(this.getName(), "Teleport landed in a new world after {} ms; carrying on", var1);
            this.abandonAnchor(System.currentTimeMillis(), "a new world");
            return;
         }

         this.interruptReason = var1 > 60000L ? "the teleport did not land within a minute" : "the connection changed during the teleport";
         this.endRun(this.interruptReason);
      }

      if (this.interruptReason != null) {
         String var4 = this.interruptReason;
         this.interruptReason = null;
         this.resetTarget("it stopped earlier because " + var4 + ". Turn it on again to start a new run");
      } else {
         String var3 = ModulePanel.findConflictingModule(this);
         if (var3 != null) {
            this.resetTarget("turn off " + var3 + " first; one bot runs at a time");
         } else if (minecraft.level.dimension() != Level.OVERWORLD) {
            this.resetTarget("go to the overworld first; it only hunts there");
         } else if (this.findPickaxeSlot(Blocks.RESPAWN_ANCHOR.defaultBlockState()) < 0) {
            this.resetTarget("put a diamond or netherite pickaxe with uses left in your hotbar");
         } else if (minecraft.player.getFoodData().getFoodLevel() < 10 && AutoSustain.findHotbarSlot(AnchorHunter::getPauseReason) < 0) {
            this.resetTarget("bring food in your hotbar; you are hungry");
         } else if (this.clearMovement() || this.enderChestStashSetting.getValue() && this.getPauseReason() && this.countItem(Items.RESPAWN_ANCHOR) > 0) {
            this.resetRun();
            this.running = (boolean)1;
            this.lastResult = null;
            this.runLevel = minecraft.level;
            this.runPlayer = minecraft.player;
            this.runConnection = this.getConnection();
            this.singleplayer = minecraft.isLocalServer();
            this.runStartedAt = System.currentTimeMillis();
            this.lastHealth = minecraft.player.getHealth();
            this.autoEat.start();
            this.disableConflictingModules();
            ArrayList var2 = new ArrayList();
            if (!this.disabledModules.isEmpty()) {
               var2.add(String.join(", ", this.disabledModules) + (this.disabledModules.size() == 1 ? " is" : " are") + " off while hunting and back on after");
            }

            if (AutoSustain.findHotbarSlot(AnchorHunter::getPauseReason) < 0) {
               var2.add("no food in the hotbar");
            }

            if (this.enderChestStashSetting.getValue() && !this.getPauseReason()) {
               var2.add("no ender chest and Silk Touch pickaxe in the hotbar, so a full inventory ends the run");
            }

            if (this.pearlTravelSetting.getValue() && this.countPearls() == 0) {
               var2.add("no pearls in the hotbar, so it walks to far anchors");
            }

            if (this.singleplayer) {
               var2.add("singleplayer has no /rtp, so it only hunts around you");
            }

            this.sendChatMessage(
               this.singleplayer ? "hunting anchors around you." : "hunting anchors with /rtp " + this.regionSetting.getValue().toLowerCase(Locale.ROOT) + "."
            );
            if (!var2.isEmpty()) {
               this.sendChatMessage("heads up: " + String.join("; ", var2) + ".");
            }

            LarpLog.info(
               this.getName(),
               "Started at {} ({}), region {}, range {}, dig {}, pearls {}, stash {}, stop after {}",
               minecraft.player.blockPosition().toShortString(),
               this.singleplayer ? "singleplayer" : "server",
               this.regionSetting.getValue(),
               this.playerRangeSetting.getValue().intValue(),
               this.digToHiddenSetting.getValue(),
               this.pearlTravelSetting.getValue(),
               this.enderChestStashSetting.getValue(),
               this.stopAfterSetting.getValue().intValue()
            );
            if (this.singleplayer) {
               this.spotStartedAt = this.runStartedAt;
               this.setState(AnchorHunter.State.LANDING, "start");
            } else {
               this.setState(AnchorHunter.State.SENDING_RTP, "start");
            }
         } else {
            this.resetTarget("your inventory is full; make room first");
         }
      }
   }

   @Override
   public void onDisable() {
      int var1 = this.isEnabled() && this.running ? 1 : 0;
      this.clearMovement();
      super.onDisable();
      this.autoEat.reset();
      NativePlacement.cancel(this);
      if (var1 == 0) {
         if (this.running) {
            this.endRun((String)null);
         }

         if (this.lastResult != null) {
            this.setStatus(this.lastResult);
         }
      } else {
         int var2 = minecraft != null && minecraft.player != null && minecraft.player.isAlive() ? 1 : 0;
         if (var2 == 0
            || this.getConnection() != this.runConnection
            || this.runConnection == null
            || !this.rtpTimer.isInFlight() && this.state != AnchorHunter.State.AWAITING_TELEPORT && this.state != AnchorHunter.State.STANDING_STILL) {
            this.interruptReason = var2 != 0 ? "the world, the player or the connection changed" : "you died";
            LarpLog.info(this.getName(), "Interrupted in {} after {} anchors: {}", this.state, this.anchorsCollected, this.interruptReason);
            this.endRun(this.interruptReason);
         } else {
            this.teleportStartedAt = System.currentTimeMillis();
            this.running = (boolean)0;
            LarpLog.info(this.getName(), "World changed while waiting for /rtp; waiting for the new world");
         }
      }
   }

   private void endRun(String var1) {
      this.sendRtp();
      if (this.running || var1 != null) {
         LarpLog.info(
            this.getName(),
            "Run ended{}: {} anchors, {} teleports, {} spots{}",
            var1 == null ? "" : " (" + var1 + ")",
            this.anchorsCollected,
            this.teleportCount,
            this.spotCount,
            this.skipReasons.isEmpty() ? "" : ", skipped " + this.skipReasons
         );
      }

      this.running = (boolean)0;
      this.runLevel = this.runPlayer = this.runConnection = null;
      this.chatQueue.clear();
   }

   private void resetRun() {
      this.rtpTimer.reset();
      this.regionPicker.getX();
      this.chatQueue.clear();
      this.skipReasons.clear();
      this.toolCutsThreeByThree = null;
      this.probeBlocks.clear();
      this.soldCount = 0;
      this.sellRetryAt = 0L;
      this.sellStep = 0;
      this.sellButtonSlot = -1;
      this.state = AnchorHunter.State.INITIALIZING;
      this.resumeState = AnchorHunter.State.SCANNING;
      this.runTicks = this.stateStartTick = 0;
      this.countdownSeen = (boolean)0;
      this.countdownSeenAt = -1L;
      this.currentRegion = null;
      this.anchorsCollected = this.teleportCount = this.spotCount = this.emptySpotCount = 0;
      this.lastRtpSentAt = -2305843009213693952L;
      this.lastPosition = null;
      this.teleportStartedAt = -1L;
      this.setStatus();
      this.motion.reset();
   }

   private void setStatus() {
      this.foundAnchors.clear();
      this.skippedAnchors.clear();
      this.areaProtected = this.playerNearby = this.fleeingMobs = (boolean)0;
      this.fightTarget = null;
      this.nearbyPlayerName = "";
      this.nearestPlayerDistance = Double.MAX_VALUE;
      this.dangerPoints.clear();
      this.fleeTarget = null;
      this.resetTarget();
      this.pearlState = AnchorHunter.PearlState.IDLE;
      this.stashStep = 0;
      this.enderChestPos = null;
   }

   private void resetTarget() {
      this.targetAnchor = null;
      this.targetCharges = this.routeFailures = this.mineTicks = this.brokenTicks = this.anchorReturnCount = this.noSightTicks = this.mobFleeCount = 0;
      this.anchorBroken = this.farLeg = (boolean)0;
      this.standingSpots.clear();
      this.rejectedSpots.clear();
      this.pathFollower.reset();
      this.lastRouteTick = -1000;
   }

   private void clearMovement() {
      this.pathFollower.reset();
      BlockBreaker.release(this);
      this.moveDirection = Vec3.ZERO;
      this.jumping = this.sneaking = this.sprinting = (boolean)0;
   }

   private Object getConnection() {
      return minecraft != null && minecraft.getConnection() != null ? minecraft.getConnection().getConnection() : null;
   }

   private void resetTarget(String var1) {
      LarpLog.info(this.getName(), "Not started: {}", var1);
      this.sendChatMessage("not started, " + var1 + ".");
      NotificationManager.getInstance().notifyDefault("Anchor Hunter", checkSetup(var1), NotificationManager.Type.WARNING);
      this.lastResult = "Not started: " + var1;
      this.setStatus(this.lastResult);
      this.disable();
   }

   private void stopRun(String var1) {
      String var2 = this.anchorsCollected
         + " anchors in "
         + ModulePanel.formatDuration(System.currentTimeMillis() - this.runStartedAt)
         + ", "
         + this.teleportCount
         + " teleports";
      LarpLog.info(this.getName(), "Stopped: {} ({})", var1, var2);
      this.notify(AnchorHunter.NoticeLevel.ERROR, checkSetup(var1) + ". " + checkSetup(var2) + ".");
      this.lastResult = "Stopped: " + var1;
      this.setStatus(this.lastResult);
      this.state = AnchorHunter.State.STOPPED;
      this.disable();
   }

   private void disableConflictingModules() {
      this.disabledModules.clear();
      ModuleManager var1 = LarpClient.getInstance().getModuleManager();

      for (String var3 : conflictingModules) {
         Module var4 = var1.getByName(var3);
         if (var4 != null && var4 != this && var4.isEnabled()) {
            var4.setEnabled(false);
            if (!var4.isEnabled()) {
               this.disabledModules.add(var3);
            }
         }
      }

      if (!this.disabledModules.isEmpty()) {
         LarpLog.info(this.getName(), "Turned off for the run: {}", String.join(", ", this.disabledModules));
      }
   }

   private void sendRtp() {
      if (!this.disabledModules.isEmpty()) {
         ModuleManager var1 = LarpClient.getInstance().getModuleManager();
         ArrayList var2 = new ArrayList();

         for (String var4 : this.disabledModules) {
            Module var5 = var1.getByName(var4);
            if (var5 != null && !var5.isEnabled()) {
               var5.setEnabled(true);
               var2.add(var4);
            }
         }

         this.disabledModules.clear();
         if (!var2.isEmpty()) {
            LarpLog.info(this.getName(), "Turned back on: {}", String.join(", ", var2));
         }
      }
   }

   private String sendRtp() {
      ModuleManager var1 = LarpClient.getInstance().getModuleManager();

      for (String var3 : conflictingModules) {
         Module var4 = var1.getByName(var3);
         if (var4 != null && var4 != this && var4.isEnabled()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public void onTick() {
      this.moveDirection = Vec3.ZERO;
      this.jumping = this.sneaking = this.sprinting = (boolean)0;
      boolean var8 = false;
      byte var7 = this.running;
      int var10000 = Integer.signum(var7);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (minecraft.level == this.runLevel && minecraft.player == this.runPlayer) {
            byte var10 = 1;
            int var9 = this.runTicks;
            this.runTicks = var9 + var10;
            this.lastTickCount = minecraft.player.tickCount;
            long var1 = System.currentTimeMillis();
            if (!minecraft.player.isAlive()) {
               this.stopRun("you died");
            } else {
               String var3 = this.sendRtp();
               if (var3 != null) {
                  this.stopRun(var3 + " was turned on");
               } else {
                  this.processReplies(var1);
                  boolean var12 = false;
                  byte var11 = this.running;
                  var10000 = Integer.signum(var11);
                  if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.state != AnchorHunter.State.STOPPED) {
                     this.stopRun(var1);
                     this.collectAnchorDrop(var1);
                     this.notify();
                     String var4 = AutoSustain.getDangerReason();
                     if (var4 != null) {
                        this.clearMovement();
                        this.autoEat.stopEating();
                        this.jumping = (boolean)1;
                        this.setStatus(var4 + ": swimming up");
                     } else if (!this.avoidMobs(var1)) {
                        if (this.playerNearby) {
                           this.isStashChestOpen(var1);
                        } else if (this.getPauseReason() instanceof String var5) {
                           if (this.state == AnchorHunter.State.STORING && minecraft.player.containerMenu instanceof ChestMenu) {
                              minecraft.player.closeContainer();
                           }

                           if (this.state != AnchorHunter.State.PAUSED) {
                              this.resumeState = this.state != AnchorHunter.State.STORING && this.state != AnchorHunter.State.MINING
                                 ? this.state
                                 : AnchorHunter.State.TRAVELING;
                              this.setState(AnchorHunter.State.PAUSED, var5);
                           }

                           this.clearMovement();
                           if (!this.isStashChestOpen() && WorldPathView.currentScreen() instanceof AbstractContainerScreen) {
                              NativePlacement.cancel(this);
                           }

                           this.setStatus("Paused: " + var5);
                        } else {
                           if (this.state == AnchorHunter.State.PAUSED) {
                              this.motion.reset();
                              this.setState(this.resumeState, "resumed");
                           }

                           if (this.shouldEat() && this.autoEat.tryEat(4)) {
                              this.clearMovement();
                              this.setStatus("Eating");
                           } else {
                              if (this.distanceTo()) {
                                 boolean var14 = false;
                                 byte var13 = this.singleplayer;
                                 var10000 = Integer.signum(var13);
                                 if ((var10000 | -var10000) >>> 31 == 0 && var1 - this.spotStartedAt > 360000L) {
                                    this.clearMovement("six minutes on this spot");
                                    return;
                                 }
                              }

                              switch (this.state) {
                                 case INITIALIZING:
                                 case SENDING_RTP:
                                    this.sendRtp(var1);
                                    break;
                                 case AWAITING_TELEPORT:
                                 case STANDING_STILL:
                                    this.countItem(var1);
                                    break;
                                 case AWAITING_RTP:
                                    if (this.rtpTimer.canAttempt(var1)) {
                                       this.sendRtp(var1);
                                    }
                                    break;
                                 case LANDING:
                                    this.setStatus(var1);
                                    break;
                                 case SCANNING:
                                    this.endRun();
                                    break;
                                 case SELECTING_ANCHOR:
                                    this.chooseAnchor(var1);
                                    break;
                                 case TRAVELING:
                                    this.getConnection(var1);
                                    break;
                                 case MINING:
                                    this.mineAnchor(var1);
                                    break;
                                 case COLLECTING:
                                    this.collectAnchorDrop();
                                    break;
                                 case STORING:
                                    this.stopRun();
                                    break;
                                 case SELLING:
                                    this.chooseAnchor();
                                    break;
                                 case PROTECTED_AREA:
                                    this.areaProtected = (boolean)1;
                                    this.clearMovement("the area is protected");
                                    break;
                                 case PLAYER_NEARBY:
                                    this.setState(
                                       this.rtpTimer.isInFlight() ? AnchorHunter.State.AWAITING_TELEPORT : AnchorHunter.State.SENDING_RTP, "nobody near now"
                                    );
                                 case PAUSED:
                                 case STOPPED:
                              }

                              if (this.running && this.state != AnchorHunter.State.STOPPED) {
                                 this.setStatus(this.describeState(var1));
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.stopRun("the world or the player changed");
         }
      }
   }

   private String describeState(long var1) {
      return switch (this.state) {
         case AWAITING_RTP -> "Waiting for /rtp (" + chooseAnchor(this.rtpTimer.millisUntilNext(var1)) + ")";
         default -> this.state.LABEL;
         case TRAVELING -> this.pearlState == AnchorHunter.PearlState.AIMING
            ? "Aiming a pearl at the anchor"
            : (
               this.pearlState == AnchorHunter.PearlState.THROWN
                  ? "Pearling to an anchor"
                  : (
                     this.pathFollower.getRemainingPath() != null
                        ? "Digging toward the anchor at " + this.targetAnchor.toShortString()
                        : (
                           this.targetAnchor == null
                              ? this.state.LABEL
                              : "Going to the anchor at " + this.targetAnchor.toShortString() + ", " + this.distanceTo(this.targetAnchor) + " blocks"
                        )
                  )
            );
         case MINING -> this.targetAnchor == null ? this.state.LABEL : "Mining the anchor at " + this.targetAnchor.toShortString();
         case STORING -> "Stashing anchors in the ender chest (" + (this.stashStep + 1) + "/6)";
      };
   }

   private int distanceTo(BlockPos var1) {
      return (int)Math.sqrt(minecraft.player.distanceToSqr(Vec3.atCenterOf(var1)));
   }

   private String getPauseReason() {
      if (ViewController.isFreecam()) {
         return "Freecam is on";
      } else if (this.isPlayerSteering()) {
         return "you are steering";
      } else {
         return WorldPathView.currentScreen() instanceof AbstractContainerScreen && !this.isStashChestOpen() ? "an inventory is open" : null;
      }
   }

   private boolean isPlayerSteering() {
      return minecraft.isWindowActive()
         && WorldPathView.currentScreen() == null
         && (
            PlayerInputUtil.isKeyDown(minecraft.options.keyUp)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyDown)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyLeft)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyRight)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyJump)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyShift)
         );
   }

   private boolean shouldEat() {
      return this.state == AnchorHunter.State.AWAITING_RTP
         || this.state == AnchorHunter.State.LANDING
         || this.state == AnchorHunter.State.SCANNING
         || this.state == AnchorHunter.State.SELECTING_ANCHOR
         || this.state == AnchorHunter.State.TRAVELING && !this.pathFollower.hasRemainingNodes()
         || this.state == AnchorHunter.State.MINING && minecraft.player.getFoodData().getFoodLevel() <= 6;
   }

   private boolean distanceTo() {
      return this.state == AnchorHunter.State.SCANNING
         || this.state == AnchorHunter.State.SELECTING_ANCHOR
         || this.state == AnchorHunter.State.TRAVELING
         || this.state == AnchorHunter.State.MINING
         || this.state == AnchorHunter.State.COLLECTING;
   }

   private void setState(AnchorHunter.State var1, String var2) {
      if (var1 != this.state) {
         LarpLog.debug(this.getName(), "{} -> {} ({})", this.state, var1, var2);
         this.state = var1;
         this.stateStartTick = this.runTicks;
         this.stateStartedAt = System.currentTimeMillis();
      }
   }

   private void sendRtp(long var1) {
      if (this.singleplayer) {
         this.setState("mined every anchor it can reach around you (singleplayer has no /rtp)");
      } else if (!this.rtpTimer.canAttempt(var1)) {
         this.setState(AnchorHunter.State.AWAITING_RTP, "rtp not ready");
      } else {
         this.clearMovement();
         if (!minecraft.player.onGround() && !minecraft.player.isInWater()) {
            this.setStatus("Landing before /rtp");
         } else {
            this.currentRegion = this.regionPicker.getZ(this.regionSetting.getValue());
            minecraft.player.connection.sendCommand("rtp " + this.currentRegion);
            this.rtpTimer.recordAttempt(var1);
            this.lastRtpSentAt = var1;
            this.countdownSeen = (boolean)0;
            this.teleportCount++;
            this.lastPosition = minecraft.player.position();
            LarpLog.info(this.getName(), "Sent /rtp {} ({} so far)", this.currentRegion, this.teleportCount);
            this.setState(AnchorHunter.State.AWAITING_TELEPORT, "rtp " + this.currentRegion);
         }
      }
   }

   private void countItem(long var1) {
      if (this.rtpTimer.hasTimedOut(var1)) {
         this.regionPicker.distanceTo(this.currentRegion);
         if (this.rtpTimer.recordFailure(var1)) {
            this.stopRun("/rtp did not answer 5 times in a row");
         } else {
            LarpLog.info(this.getName(), "/rtp {} got no answer; trying again in {}", this.currentRegion, chooseAnchor(this.rtpTimer.millisUntilNext(var1)));
            this.setState(AnchorHunter.State.AWAITING_RTP, "rtp silent");
         }
      } else {
         if (!this.rtpTimer.isInFlight()) {
            this.setState(AnchorHunter.State.AWAITING_RTP, "rtp answered");
         }
      }
   }

   private void processReplies(long var1) {
      Set var4 = null;

      String var3;
      while ((var3 = this.chatQueue.poll()) != null) {
         if (var4 == null) {
            var4 = this.getOnlinePlayerNames();
         }

         Set var5 = var4;
         ChatMessageClassifier.Result var6 = ChatMessageClassifier.classify(var3, var1x -> var5.contains(var1x.toLowerCase(Locale.ROOT)));
         if (this.state == AnchorHunter.State.SELLING && ChatMessageClassifier.normalize(var3).contains("sold")) {
            LarpLog.info(this.getName(), "Sell reply: {}", ChatMessageClassifier.normalize(var3));
         }

         if (var6.kind() != ChatMessageClassifier.Kind.IGNORED) {
            LarpLog.info(this.getName(), "Server reply {} ({} ms wait): {}", var6.kind(), var6.waitMs(), ChatMessageClassifier.normalize(var3));
            this.avoidMobs(var6, var1);
            if (!this.running || this.state == AnchorHunter.State.STOPPED) {
               return;
            }
         }
      }
   }

   private Set<String> getOnlinePlayerNames() {
      HashSet var1 = new HashSet();
      if (minecraft.getConnection() != null) {
         for (PlayerInfo var3 : minecraft.getConnection().getOnlinePlayers()) {
            var1.add(var3.getProfile().name().toLowerCase(Locale.ROOT));
         }
      }

      return var1;
   }

   private void avoidMobs(ChatMessageClassifier.Result var1, long var2) {
      boolean var4 = this.rtpTimer.isInFlight();
      int var5 = var2 - this.lastRtpSentAt < 2000L ? 1 : 0;
      switch (var1.kind()) {
         case SEARCHING:
         case ALREADY_TELEPORTING:
            this.rtpTimer.extendDeadline(var2);
            break;
         case COUNTDOWN:
            if (var4) {
               this.countdownSeen = (boolean)1;
               this.countdownSeenAt = var2;
               if (this.state == AnchorHunter.State.AWAITING_TELEPORT) {
                  this.setState(AnchorHunter.State.STANDING_STILL, "countdown");
               }
            }
            break;
         case COOLDOWN:
            if (!var4) {
               return;
            }

            this.rtpTimer.recordCooldown(var2, var1.waitMs());
            LarpLog.info(this.getName(), "/rtp is cooling down; next in {}", chooseAnchor(this.rtpTimer.millisUntilNext(var2)));
            this.disableConflictingModules("rtp cooldown");
            break;
         case IN_COMBAT:
            if (var4) {
               this.rtpTimer.blockFor(var2, 10000L);
               this.disableConflictingModules("combat tagged");
            }
            break;
         case RATE_LIMITED:
            if (var4 && var5 != 0) {
               this.rtpTimer.blockFor(var2, 1500L);
               this.disableConflictingModules("chat limit");
            }
            break;
         case UNKNOWN_COMMAND:
            if (var4 && var5 != 0) {
               this.stopRun("this server has no /rtp");
            }
            break;
         case CANCELLED:
         case UNSAFE_LOCATION:
            if (var4 || this.countdownSeen) {
               this.rtpTimer.blockFor(var2, 3000L);
               this.countdownSeen = (boolean)0;
               this.disableConflictingModules(var1.kind() == ChatMessageClassifier.Kind.UNSAFE_LOCATION ? "unsafe spot" : "teleport cancelled");
            }
            break;
         case MAINTENANCE:
            if (var4) {
               this.regionPicker.distanceTo(this.currentRegion);
               this.rtpTimer.blockFor(var2, 3000L);
               this.disableConflictingModules("region down");
            }
            break;
         case PROTECTED_AREA:
            if (this.state == AnchorHunter.State.STORING) {
               this.stashStopReason = "the area is protected";
            } else if ((this.state == AnchorHunter.State.MINING || this.state == AnchorHunter.State.TRAVELING) && var2 - this.standingSpotReachedAt <= 3000L) {
               this.clearMovement();
               LarpLog.info(this.getName(), "The server says {} is protected", this.targetAnchor == null ? "this spot" : this.targetAnchor.toShortString());
               this.setState(AnchorHunter.State.PROTECTED_AREA, "protected area");
            }
         case TELEPORTED:
         case IGNORED:
      }
   }

   private void disableConflictingModules(String var1) {
      if (this.state == AnchorHunter.State.AWAITING_TELEPORT
         || this.state == AnchorHunter.State.STANDING_STILL
         || this.state == AnchorHunter.State.PLAYER_NEARBY) {
         this.setState(AnchorHunter.State.AWAITING_RTP, var1);
      }
   }

   private void stopRun(long var1) {
      Vec3 var3 = minecraft.player.position();
      Vec3 var4 = this.lastPosition;
      this.lastPosition = var3;
      if (var4 != null && (this.rtpTimer.isInFlight() || this.countdownSeen)) {
         if (var3.distanceTo(var4) > 64.0) {
            this.abandonAnchor(var1, String.format(Locale.ROOT, "%.0f blocks", var3.distanceTo(var4)));
         }
      }
   }

   private void abandonAnchor(long var1, String var3) {
      this.rtpTimer.recordSuccess(var1);
      this.regionPicker.directionTo(this.currentRegion);
      this.spotCount++;
      this.countdownSeen = (boolean)0;
      this.setStatus();
      this.clearMovement();
      this.spotStartedAt = var1;
      this.lastPosition = minecraft.player.position();
      LarpLog.info(this.getName(), "Teleported ({}) to {}; spot {}", var3, minecraft.player.blockPosition().toShortString(), this.spotCount);
      this.setState(AnchorHunter.State.LANDING, "teleported");
   }

   private void clearMovement(String var1) {
      this.clearMovement();
      this.resetTarget();
      LarpLog.info(this.getName(), "Leaving this spot: {}", var1);
      if (this.singleplayer) {
         this.setState("mined every anchor it can reach around you (singleplayer has no /rtp)");
      } else {
         this.setState(this.rtpTimer.canAttempt(System.currentTimeMillis()) ? AnchorHunter.State.SENDING_RTP : AnchorHunter.State.AWAITING_RTP, var1);
      }
   }

   private int findPickaxeSlot() {
      return Math.clamp(minecraft.options.getEffectiveRenderDistance(), 2, 12);
   }

   private void setStatus(long var1) {
      if (minecraft.level.dimension() != Level.OVERWORLD) {
         if (var1 - this.stateStartedAt > 10000L) {
            this.stopRun("the /rtp sent you out of the overworld");
         }
      } else {
         int var3 = !minecraft.player.onGround() && !minecraft.player.isInWater() ? 0 : 1;
         int var4 = this.findPickaxeSlot();
         int var5 = 0;
         int var6 = 0;
         int var7 = minecraft.player.getBlockX() >> 4;
         int var8 = minecraft.player.getBlockZ() >> 4;

         for (int var9 = -var4; var9 <= var4; var9++) {
            for (int var10 = -var4; var10 <= var4; var10++) {
               var6++;
               if (minecraft.level.getChunkSource().getChunk(var7 + var9, var8 + var10, ChunkStatus.FULL, false) != null) {
                  var5++;
               }
            }
         }

         double var11 = var6 == 0 ? 0.0 : (double)var5 / var6;
         if (var3 != 0 && var1 - this.stateStartedAt >= 1000L && var11 >= 0.9 || var1 - this.stateStartedAt > 8000L) {
            LarpLog.debug(this.getName(), "Landed; {}% of {} chunks loaded", Math.round(var11 * 100.0), var6);
            this.setState(AnchorHunter.State.SCANNING, "landed");
         }
      }
   }

   private int scanLoadedChunks(Map<Long, Integer> var1) {
      int var2 = this.findPickaxeSlot();
      int var3 = 0;
      int var4 = minecraft.player.getBlockX() >> 4;
      int var5 = minecraft.player.getBlockZ() >> 4;

      for (int var6 = -var2; var6 <= var2; var6++) {
         for (int var7 = -var2; var7 <= var2; var7++) {
            LevelChunk var8 = minecraft.level.getChunkSource().getChunk(var4 + var6, var5 + var7, ChunkStatus.FULL, false);
            if (var8 != null) {
               var3++;
               LevelChunkSection[] var9 = var8.getSections();

               for (int var10 = 0; var10 < var9.length; var10++) {
                  final LevelChunkSection var11 = var9[var10];
                  if (var11 != null && !var11.hasOnlyAir()) {
                     AnchorPlacement.collectAnchors(new AnchorPlacement.ChunkSection() {
                        @Override
                        public boolean maybeHasAnchor() {
                           return var11.maybeHas(var0 -> var0.is(Blocks.RESPAWN_ANCHOR));
                        }

                        @Override
                        public BlockState state(int var1, int var2x, int var3x) {
                           return var11.getBlockState(var1, var2x, var3x);
                        }
                     }, var4 + var6 << 4, var8.getSectionYFromSectionIndex(var10) << 4, var5 + var7 << 4, var1);
                  }
               }
            }
         }
      }

      return var3;
   }

   private void endRun() {
      this.foundAnchors.clear();
      int var1 = this.scanLoadedChunks(this.foundAnchors);
      this.foundAnchors.keySet().removeAll(this.skippedAnchors);
      LarpLog.info(this.getName(), "{} anchors in {} loaded chunks", this.foundAnchors.size(), var1);
      if (this.foundAnchors.isEmpty()) {
         this.emptySpotCount++;
         this.clearMovement("no anchors here");
      } else {
         this.notify(
            AnchorHunter.NoticeLevel.DEBUG,
            this.foundAnchors.size() + (this.foundAnchors.size() == 1 ? " anchor" : " anchors") + " in sight of the chunks here"
         );
         this.setState(AnchorHunter.State.SELECTING_ANCHOR, this.foundAnchors.size() + " anchors");
      }
   }

   private void chooseAnchor(long var1) {
      this.scanLoadedChunks(this.foundAnchors);
      this.foundAnchors.keySet().removeAll(this.skippedAnchors);
      Vec3 var3 = minecraft.player.getEyePosition();
      List var4 = this.foundAnchors
         .keySet()
         .stream()
         .sorted(
            Comparator.comparingDouble(
               var1x -> var3.distanceToSqr(AStarPathfinder.unpackX(var1x) + 0.5, AStarPathfinder.unpackY(var1x) + 0.5, AStarPathfinder.unpackZ(var1x) + 0.5)
            )
         )
         .limit(48L)
         .toList();
      BlockPos var5 = null;
      double var6 = Double.MAX_VALUE;
      int var8 = minecraft.player.getBlockY();
      WorldPathView var9 = this.buildWorldView(null);

      for (long var11 : var4) {
         var9.setDigBounds(new BlockPos(AStarPathfinder.unpackX(var11), AStarPathfinder.unpackY(var11), AStarPathfinder.unpackZ(var11)));
      }

      AnchorPlacement.LineOfSight var21 = this.resetRun();

      for (long var12 : var4) {
         BlockPos var14 = new BlockPos(AStarPathfinder.unpackX(var12), AStarPathfinder.unpackY(var12), AStarPathfinder.unpackZ(var12));
         if (!minecraft.level.getBlockState(var14).is(Blocks.RESPAWN_ANCHOR)) {
            this.foundAnchors.remove(var12);
         } else {
            double var15 = Math.hypot(var14.getX() + 0.5 - minecraft.player.getX(), var14.getZ() + 0.5 - minecraft.player.getZ());
            AnchorPlacement.StandSearchResult var17 = var15 <= 48.0
               ? AnchorPlacement.findStandPositions(var9, var14.getX(), var14.getY(), var14.getZ(), var21)
               : null;
            String var18 = var17 == null
               ? (this.areaProtected ? "protected" : null)
               : AnchorPlacement.rejectionReason(
                  var9, var14.getX(), var14.getY(), var14.getZ(), var8, var17, this.digToHiddenSetting.getValue(), this.areaProtected
               );
            if (var18 != null) {
               this.sendRtp(var12, var18);
            } else {
               double var19 = Math.sqrt(var3.distanceToSqr(Vec3.atCenterOf(var14)))
                  + (var17 != null && var17.exposed().isEmpty() ? 6 + 3 * var17.dig().getFirst().digs() : 0);
               if (var19 < var6) {
                  var6 = var19;
                  var5 = var14;
               }
            }
         }
      }

      if (var5 == null) {
         this.clearMovement(this.foundAnchors.isEmpty() ? "no anchors left here" : "no safe anchors left here");
      } else {
         this.selectTarget(var5, var1);
      }
   }

   private void sendRtp(long var1, String var3) {
      this.skippedAnchors.add(var1);
      this.foundAnchors.remove(var1);
      this.skipReasons.merge(var3, 1, Integer::sum);
      LarpLog.debug(
         this.getName(),
         "Skipping the anchor at {} {} {}: {}",
         AStarPathfinder.unpackX(var1),
         AStarPathfinder.unpackY(var1),
         AStarPathfinder.unpackZ(var1),
         var3
      );
   }

   private void selectTarget(BlockPos var1, long var2) {
      this.resetTarget();
      this.targetAnchor = var1.immutable();
      this.targetCharges = this.foundAnchors.getOrDefault(AStarPathfinder.packPos(var1.getX(), var1.getY(), var1.getZ()), 0);
      this.targetSelectedAt = var2;
      this.digsAtTargetStart = this.pathFollower.getBlocksBroken();
      LarpLog.info(
         this.getName(),
         "Going for the anchor at {} ({} charges, {} blocks away)",
         this.targetAnchor.toShortString(),
         this.targetCharges,
         (int)Math.sqrt(minecraft.player.distanceToSqr(Vec3.atCenterOf(this.targetAnchor)))
      );
      this.setState(AnchorHunter.State.TRAVELING, "target " + this.targetAnchor.toShortString());
   }

   private void abandonAnchor(String var1) {
      if (this.targetAnchor != null) {
         this.sendRtp(AStarPathfinder.packPos(this.targetAnchor.getX(), this.targetAnchor.getY(), this.targetAnchor.getZ()), var1);
         LarpLog.info(this.getName(), "Gave up on the anchor at {}: {}", this.targetAnchor.toShortString(), var1);
      }

      this.clearMovement();
      this.resetTarget();
      this.setState(AnchorHunter.State.SELECTING_ANCHOR, var1);
   }

   private void getConnection(long var1) {
      if (this.targetAnchor == null) {
         this.setState(AnchorHunter.State.SELECTING_ANCHOR, "no target");
      } else if (!minecraft.level.getBlockState(this.targetAnchor).is(Blocks.RESPAWN_ANCHOR)) {
         this.foundAnchors.remove(AStarPathfinder.packPos(this.targetAnchor.getX(), this.targetAnchor.getY(), this.targetAnchor.getZ()));
         LarpLog.debug(this.getName(), "The anchor at {} is gone", this.targetAnchor.toShortString());
         this.resetTarget();
         this.setState(AnchorHunter.State.SELECTING_ANCHOR, "anchor gone");
      } else if (!this.clearMovement()) {
         this.handleInventoryFull();
      } else if (this.routeFailures < 3 && this.pathFollower.getBlocksBroken() - this.digsAtTargetStart <= 40 && var1 - this.targetSelectedAt <= 120000L) {
         double var3 = Math.hypot(this.targetAnchor.getX() + 0.5 - minecraft.player.getX(), this.targetAnchor.getZ() + 0.5 - minecraft.player.getZ());
         if (!this.farLeg && var3 > 28.0) {
            this.farLeg = (boolean)1;
         } else if (this.farLeg && var3 <= 22.0) {
            this.farLeg = (boolean)0;
            this.pathFollower.reset();
         }

         if (this.pearlState == AnchorHunter.PearlState.IDLE && (!this.farLeg || !this.pearlTravelSetting.getValue()) || !this.sendChatMessage()) {
            if (this.pathFollower.hasRemainingNodes()
                  && (!this.farLeg || this.runTicks - this.lastRouteTick <= 100)
                  && (!this.pathFollower.randomizeWobble() || this.pathFollower.getRemainingPath().size() > 4 || this.runTicks - this.lastRouteTick <= 10)
               || this.planRoute()
               || this.pathFollower.hasRemainingNodes()) {
               PathFollower.Result var5 = this.pathFollower.tick(this.chooseAnchor(), this::isPlayerSteering, true);
               this.applyPathInput();
               switch (var5) {
                  case ARRIVED:
                     this.pathFollower.reset();
                     if (!this.farLeg && this.standingSpots.contains(this.getFeetPosKey())) {
                        this.getInstance(var1);
                     }
                     break;
                  case FAILED:
                     this.routeFailures++;
                     LarpLog.info(
                        this.getName(),
                        "Route to {} stuck ({}): {}",
                        this.targetAnchor.toShortString(),
                        this.routeFailures,
                        this.pathFollower.getRemainingPath()
                     );
                     this.pathFollower.reset();
               }
            }
         }
      } else {
         this.abandonAnchor(
            this.routeFailures >= 3
               ? "no way there"
               : (this.pathFollower.getBlocksBroken() - this.digsAtTargetStart > 40 ? "too much digging" : "took too long to reach")
         );
      }
   }

   private boolean planRoute() {
      this.lastRouteTick = this.runTicks;
      WorldPathView var1 = this.buildWorldView(this.targetAnchor);
      BlockPos var2 = this.resetTarget();
      AStarPathfinder.PathResult var3;
      if (this.farLeg) {
         AStarPathfinder.Bounds var4 = AStarPathfinder.Bounds.around(
            var2.getX(), var2.getY(), var2.getZ(), this.targetAnchor.getX(), this.targetAnchor.getY(), this.targetAnchor.getZ(), 16, 24, 24
         );
         AStarPathfinder.Options var5 = new AStarPathfinder.Options(16000, this.digToHiddenSetting.getValue(), 3, true, 3.0);
         var3 = AStarPathfinder.findPath(
            var1,
            var2.getX(),
            var2.getY(),
            var2.getZ(),
            AStarPathfinder.Goal.near(this.targetAnchor.getX() + 0.5, this.targetAnchor.getY(), this.targetAnchor.getZ() + 0.5, 18.0, 24.0),
            var4,
            var5
         );
      } else {
         AnchorPlacement.StandSearchResult var10 = AnchorPlacement.findStandPositions(
            var1, this.targetAnchor.getX(), this.targetAnchor.getY(), this.targetAnchor.getZ(), this.resetRun()
         );
         String var11 = AnchorPlacement.rejectionReason(
            var1,
            this.targetAnchor.getX(),
            this.targetAnchor.getY(),
            this.targetAnchor.getZ(),
            var2.getY(),
            var10,
            this.digToHiddenSetting.getValue(),
            this.areaProtected
         );
         if (var11 != null) {
            this.abandonAnchor(var11);
            return false;
         }

         this.standingSpots.clear();

         for (AnchorPlacement.StandPosition var7 : var10.usable(this.digToHiddenSetting.getValue())) {
            long var8 = AStarPathfinder.packPos(var7.standX(), var7.standY(), var7.standZ());
            if (!this.rejectedSpots.contains(var8)) {
               this.standingSpots.add(var8);
            }
         }

         if (this.standingSpots.isEmpty()) {
            this.abandonAnchor("no clear view from any standing spot");
            return false;
         }

         if (this.standingSpots.contains(this.getFeetPosKey())) {
            this.getInstance(System.currentTimeMillis());
            return false;
         }

         AStarPathfinder.Bounds var12 = AStarPathfinder.Bounds.around(
            var2.getX(), var2.getY(), var2.getZ(), this.targetAnchor.getX(), this.targetAnchor.getY(), this.targetAnchor.getZ(), 10, 12, 12
         );
         AStarPathfinder.Options var13 = new AStarPathfinder.Options(16000, this.digToHiddenSetting.getValue(), 3, false, 1.0);
         var3 = AStarPathfinder.findPath(
            var1,
            var2.getX(),
            var2.getY(),
            var2.getZ(),
            AStarPathfinder.Goal.anyOf(this.standingSpots, this.targetAnchor.getX(), this.targetAnchor.getY() - 1, this.targetAnchor.getZ(), 1.0),
            var12,
            var13
         );
      }

      if (!var3.found()) {
         this.routeFailures++;
         if (var3.explored() <= 1) {
            this.logRouteFailure(var2);
         }

         LarpLog.info(this.getName(), "No route toward {} ({} cells searched, try {})", this.targetAnchor.toShortString(), var3.explored(), this.routeFailures);
         return false;
      } else if (var3.steps().isEmpty()) {
         if (this.farLeg) {
            this.farLeg = (boolean)0;
         }

         return false;
      } else {
         this.pathFollower.setPathWithEnd(var3.steps(), var3.complete());
         LarpLog.debug(
            this.getName(),
            "Route of {} steps ({} to dig{}) toward {}",
            var3.steps().size(),
            var3.digs(),
            var3.complete() ? "" : ", partial",
            this.targetAnchor.toShortString()
         );
         return true;
      }
   }

   private BlockPos resetTarget() {
      return BlockPos.containing(minecraft.player.getX(), minecraft.player.getY() + 0.2, minecraft.player.getZ());
   }

   private void logRouteFailure(BlockPos var1) {
      Function var2 = var0 -> BuiltInRegistries.BLOCK.getKey(minecraft.level.getBlockState(var0).getBlock()).getPath()
         + (minecraft.level.getFluidState(var0).isEmpty() ? "" : "+fluid");
      LarpLog.info(
         this.getName(),
         "Route could not start at {} (y {}): feet {}, head {}, below {}, in water {}, on ground {}",
         var1.toShortString(),
         String.format(Locale.ROOT, "%.3f", minecraft.player.getY()),
         var2.apply(var1),
         var2.apply(var1.above()),
         var2.apply(var1.below()),
         minecraft.player.isInWater(),
         minecraft.player.onGround()
      );
   }

   private long getFeetPosKey() {
      BlockPos var1 = this.resetTarget();
      return AStarPathfinder.packPos(var1.getX(), var1.getY(), var1.getZ());
   }

   private WorldPathView buildWorldView(BlockPos var1) {
      WorldPathView var2 = new WorldPathView(this::isPlayerSteering, this.handleInventoryFull());
      if (var1 != null) {
         var2.setDigBounds(var1);
      }

      for (LivingEntity var4 : minecraft.level
         .getEntitiesOfClass(
            LivingEntity.class,
            minecraft.player.getBoundingBox().inflate(24.0),
            var0 -> var0 instanceof Enemy && var0.isAlive() && minecraft.player.hasLineOfSight(var0)
         )) {
         var2.addAvoidZone(var4.getX(), var4.getY(), var4.getZ(), var4 instanceof Creeper ? 7.0 : 3.0);
      }

      return var2;
   }

   private AnchorPlacement.LineOfSight resetRun() {
      return (var0, var2, var4, var6, var7, var8) -> {
         Vec3 var9 = new Vec3(var0, var2, var4);
         BlockPos var10 = new BlockPos(var6, var7, var8);
         Vec3 var11 = Vec3.atCenterOf(var10);

         for (int var12 = -1; var12 < 6; var12++) {
            Vec3 var13 = var12 < 0 ? var11 : var11.add(Vec3.atLowerCornerOf(Direction.values()[var12].getUnitVec3i()).scale(0.49));
            BlockHitResult var14 = minecraft.level.clip(new ClipContext(var9, var13, Block.OUTLINE, Fluid.NONE, minecraft.player));
            if (var14.getType() != Type.BLOCK || var14.getBlockPos().equals(var10)) {
               return true;
            }
         }

         return false;
      };
   }

   private void getInstance(long var1) {
      this.pathFollower.reset();
      this.mineTicks = 0;
      this.noSightTicks = 0;
      this.anchorBroken = (boolean)0;
      this.anchorsBeforeMine = this.countItem(Items.RESPAWN_ANCHOR);
      this.standingSpotReachedAt = var1;
      this.setState(AnchorHunter.State.MINING, "at a standing spot");
   }

   private void mineAnchor(long var1) {
      if (this.targetAnchor == null) {
         this.setState(AnchorHunter.State.SELECTING_ANCHOR, "no target");
      } else {
         BlockState var3 = minecraft.level.getBlockState(this.targetAnchor);
         if (!var3.is(Blocks.RESPAWN_ANCHOR)) {
            BlockBreaker.release(this);
            if (!this.anchorBroken) {
               this.anchorBroken = (boolean)1;
               this.brokenTicks = 0;
            }

            if (++this.brokenTicks >= 3) {
               this.dropWaitStartTick = this.runTicks;
               this.foundAnchors.remove(AStarPathfinder.packPos(this.targetAnchor.getX(), this.targetAnchor.getY(), this.targetAnchor.getZ()));
               this.setState(AnchorHunter.State.COLLECTING, "anchor broken");
            }
         } else {
            if (this.anchorBroken) {
               this.anchorBroken = (boolean)0;
               if (++this.anchorReturnCount >= 2) {
                  LarpLog.info(this.getName(), "The anchor at {} came back twice", this.targetAnchor.toShortString());
                  this.setState(AnchorHunter.State.PROTECTED_AREA, "anchor came back twice");
                  return;
               }
            }

            if (!this.clearMovement()) {
               this.handleInventoryFull();
            } else if (++this.mineTicks > 900) {
               this.abandonAnchor("took too long to mine");
            } else {
               double var4 = this.chooseAnchor();
               if (minecraft.player.getEyePosition().distanceTo(Vec3.atCenterOf(this.targetAnchor)) > var4 + 0.2) {
                  this.setState(AnchorHunter.State.TRAVELING, "out of reach");
               } else if (this.handleInventoryFull() && this.hasFluidNearby(this.targetAnchor)) {
                  this.abandonAnchor("water or lava next to it and your pickaxe may cut 3x3");
               } else {
                  BlockPos var6 = this.targetAnchor;
                  this.probeToolCut();
                  boolean var7 = BlockBreaker.breakBlock(this, var6, var4, true, 12.0F, this::findPickaxeSlot, () -> !this.isPlayerSteering());
                  if (var7) {
                     this.noSightTicks = 0;
                  } else {
                     if (++this.noSightTicks > 20) {
                        this.rejectedSpots.add(this.getFeetPosKey());
                        this.noSightTicks = 0;
                        if (this.rejectedSpots.size() >= 3) {
                           this.abandonAnchor("no clear view");
                           return;
                        }

                        LarpLog.debug(
                           this.getName(),
                           "No clear view of {} from {}; trying another spot",
                           this.targetAnchor.toShortString(),
                           minecraft.player.blockPosition().toShortString()
                        );
                        this.pathFollower.reset();
                        this.setState(AnchorHunter.State.TRAVELING, "lost sight");
                     }
                  }
               }
            }
         }
      }
   }

   private void collectAnchorDrop() {
      this.findSilkPickaxeSlot();
      int var1 = this.countItem(Items.RESPAWN_ANCHOR);
      if (var1 > this.anchorsBeforeMine) {
         int var4 = var1 - this.anchorsBeforeMine;
         this.anchorsCollected += var4;
         LarpLog.info(
            this.getName(),
            "Picked up {} anchor{} ({} total){}",
            var4,
            var4 == 1 ? "" : "s",
            this.anchorsCollected,
            this.targetCharges > 0 ? ", it had " + this.targetCharges + " charges" : ""
         );
         this.notify(AnchorHunter.NoticeLevel.SUCCESS, "+" + var4 + " anchor" + (var4 == 1 ? "" : "s") + " (" + this.anchorsCollected + " total)");
         this.clearMovement();
         this.resetTarget();
         if (this.autoSellSetting.getValue() && !this.singleplayer && System.currentTimeMillis() >= this.sellRetryAt) {
            this.checkSetup();
         } else {
            int var5 = this.stopAfterSetting.getValue().intValue();
            if (var5 > 0 && this.anchorsCollected >= var5) {
               this.setState("collected " + this.anchorsCollected + " anchors");
            } else {
               this.setState(AnchorHunter.State.SELECTING_ANCHOR, "picked up");
            }
         }
      } else {
         int var2 = this.runTicks - this.dropWaitStartTick;
         if (var2 > 200) {
            LarpLog.info(this.getName(), "Lost the drop of the anchor at {}", this.targetAnchor == null ? "?" : this.targetAnchor.toShortString());
            this.clearMovement();
            this.resetTarget();
            this.setState(AnchorHunter.State.SELECTING_ANCHOR, "drop lost");
         } else if (var2 >= 10) {
            ItemEntity var3 = this.notify(Items.RESPAWN_ANCHOR, 8.0);
            if (var3 != null) {
               this.planRoute(var3);
            }
         }
      }
   }

   private void setState(String var1) {
      LarpLog.info(this.getName(), "Done: {}", var1);
      this.notify(AnchorHunter.NoticeLevel.SUCCESS, "Done, " + var1 + ".");
      this.lastResult = "Done: " + var1;
      this.setStatus(this.lastResult);
      this.state = AnchorHunter.State.STOPPED;
      this.disable();
   }

   private ItemEntity notify(Item var1, double var2) {
      List var4 = minecraft.level
         .getEntitiesOfClass(ItemEntity.class, minecraft.player.getBoundingBox().inflate(var2), var1x -> var1x.isAlive() && var1x.getItem().is(var1));
      return var4.stream().min(Comparator.comparingDouble(var0 -> var0.distanceToSqr(minecraft.player))).orElse(null);
   }

   private void planRoute(ItemEntity var1) {
      BlockPos var2 = BlockPos.containing(var1.position());
      BlockPos var3 = this.resetTarget();
      if (!(Math.hypot(var1.getX() - minecraft.player.getX(), var1.getZ() - minecraft.player.getZ()) < 0.6)) {
         if (!this.pathFollower.hasRemainingNodes() || this.runTicks - this.lastRouteTick > 20) {
            this.lastRouteTick = this.runTicks;
            AStarPathfinder.Bounds var4 = AStarPathfinder.Bounds.around(var3.getX(), var3.getY(), var3.getZ(), var2.getX(), var2.getY(), var2.getZ(), 6, 6, 4);
            AStarPathfinder.PathResult var5 = AStarPathfinder.findPath(
               this.buildWorldView(null),
               var3.getX(),
               var3.getY(),
               var3.getZ(),
               AStarPathfinder.Goal.near(var1.getX(), var2.getY(), var1.getZ(), 0.5, 1.0),
               var4,
               AStarPathfinder.Options.walking(4000)
            );
            if (!var5.found() || var5.steps().isEmpty()) {
               this.notify(var1.position());
               return;
            }

            this.pathFollower.setPath(var5.steps());
         }

         PathFollower.Result var6 = this.pathFollower.tick(this.chooseAnchor(), this::isPlayerSteering, false);
         this.applyPathInput();
         if (var6 == PathFollower.Result.ARRIVED || var6 == PathFollower.Result.FAILED) {
            this.pathFollower.reset();
            this.notify(var1.position());
         }
      }
   }

   private void notify(Vec3 var1) {
      Vec3 var2 = var1.subtract(minecraft.player.position()).multiply(1.0, 0.0, 1.0);
      double var3 = var2.length();
      double var5 = var3 < 1.0E-6 ? 0.0 : minecraft.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var2.normalize());
      this.sneaking = (boolean)1;
      boolean var7 = this.motion.updateLatch(var3, var5, InteractionArbiter.ownsInteraction(this));
      this.moveDirection = var7 ? var2 : Vec3.ZERO;
   }

   private void applyPathInput() {
      this.moveDirection = this.pathFollower.getMoveDirection();
      this.jumping = this.pathFollower.shouldJump();
      this.sneaking = this.pathFollower.isBraking();
      this.sprinting = this.pathFollower.shouldSprint();
   }

   private void handleInventoryFull() {
      this.clearMovement();
      if (this.enderChestStashSetting.getValue() && this.getPauseReason() && this.countItem(Items.RESPAWN_ANCHOR) > 0) {
         this.resumeState = this.targetAnchor != null ? AnchorHunter.State.TRAVELING : AnchorHunter.State.SELECTING_ANCHOR;
         this.stashStep = 0;
         this.stashStepTick = this.runTicks;
         this.stashFailures = 0;
         this.stashStopReason = null;
         if (this.enderChestPos != null && !minecraft.level.getBlockState(this.enderChestPos).is(Blocks.ENDER_CHEST)) {
            this.enderChestPos = null;
         }

         this.anchorsBeforeStash = this.countItem(Items.RESPAWN_ANCHOR);
         LarpLog.info(this.getName(), "Inventory full with {} anchors; stashing them in the ender chest", this.anchorsBeforeStash);
         this.setState(AnchorHunter.State.STORING, "inventory full");
      } else {
         this.stopRun(this.countItem(Items.RESPAWN_ANCHOR) > 0 ? "your inventory is full" : "your inventory is full of other things; make room for anchors");
      }
   }

   private void stopRun() {
      if (this.stashStopReason != null && this.stashStep <= 1) {
         this.failStash(this.stashStopReason);
      } else if (this.runTicks - this.stashStepTick > 300) {
         this.failStash("step " + this.stashStep + " took too long");
      } else if (minecraft.player.onGround()) {
         switch (this.stashStep) {
            case 0:
               this.findPickaxeSlot();
               break;
            case 1:
               this.isStashChestOpen();
               break;
            case 2:
               this.countItem();
               break;
            case 3:
               this.describeState();
               break;
            case 4:
               this.hasFluidNearby();
               break;
            case 5:
               this.scanLoadedChunks();
               break;
            default:
               this.failStash("unknown step");
         }
      }
   }

   private void selectTarget(int var1) {
      this.stashStep = var1;
      this.stashStepTick = this.runTicks;
   }

   private void findPickaxeSlot() {
      if (this.enderChestPos != null && minecraft.level.getBlockState(this.enderChestPos).is(Blocks.ENDER_CHEST)) {
         this.selectTarget(1);
      } else {
         if (this.enderChestPos == null) {
            BlockPos var1 = this.resetTarget();
            int[] var2 = AnchorPlacement.findSafeSpot(this.buildWorldView(this.targetAnchor), var1.getX(), var1.getY(), var1.getZ());
            if (var2 == null) {
               this.failStash("no room to place the ender chest");
               return;
            }

            this.enderChestPos = new BlockPos(var2[0], var2[1], var2[2]);
         }

         int var4 = AutoSustain.findHotbarSlot(var0 -> var0.is(Items.ENDER_CHEST));
         if (var4 < 0) {
            this.failStash("no ender chest in the hotbar");
         } else if (!this.selectHotbarSlot(var4)) {
            this.setStatus("Taking out the ender chest");
         } else {
            BlockPos var5 = this.enderChestPos;
            boolean var3 = NativePlacement.requestPlacement(
               this,
               var5,
               Items.ENDER_CHEST,
               this.chooseAnchor(),
               true,
               true,
               () -> NativePlacement.isReplaceable(var5),
               var2x -> LarpLog.debug(this.getName(), "Ender chest at {} {}", var5.toShortString(), var2x ? "placed" : "rejected")
            );
            if (!var3 && this.runTicks - this.stashStepTick > 60) {
               this.enderChestPos = null;
               this.stashStepTick = this.runTicks;
               if (++this.stashFailures >= 3) {
                  this.failStash("could not place the ender chest");
               }
            }
         }
      }
   }

   private void isStashChestOpen() {
      if (minecraft.player.containerMenu instanceof ChestMenu) {
         this.selectTarget(2);
      } else if (!minecraft.level.getBlockState(this.enderChestPos).is(Blocks.ENDER_CHEST)) {
         if (++this.stashFailures >= 3) {
            this.failStash("the ender chest disappeared");
         } else {
            this.enderChestPos = null;
            this.selectTarget(0);
         }
      } else if (WorldPathView.currentScreen() == null) {
         ItemStack var1 = minecraft.player.getMainHandItem();
         if (this.runTicks % 10 == 0) {
            NativePlacement.pause(
               this,
               this.enderChestPos,
               var1.getItem(),
               this.chooseAnchor(),
               true,
               false,
               () -> WorldPathView.currentScreen() == null,
               var1x -> LarpLog.debug(this.getName(), "Opening the ender chest {}", var1x ? "sent" : "refused")
            );
         }
      }
   }

   private void countItem() {
      if (!(minecraft.player.containerMenu instanceof ChestMenu var1)) {
         this.selectTarget(1);
      } else if (!(minecraft.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4)) {
         int var7 = var1.getRowCount() * 9;
         int var3 = -1;
         byte var4 = 0;

         for (int var5 = 0; var5 < var1.slots.size(); var5++) {
            ItemStack var6 = ((Slot)var1.slots.get(var5)).getItem();
            if (var5 < var7) {
               if (var6.isEmpty() || var6.is(Items.RESPAWN_ANCHOR) && var6.getCount() < var6.getMaxStackSize()) {
                  var4 = 1;
               }
            } else if (var3 < 0 && var6.is(Items.RESPAWN_ANCHOR)) {
               var3 = var5;
            }
         }

         if (var3 >= 0 && var4 != 0) {
            if (this.runTicks - this.lastQuickMoveTick >= 4) {
               this.lastQuickMoveTick = this.runTicks;
               minecraft.gameMode.handleContainerInput(var1.containerId, var3, 0, ContainerInput.QUICK_MOVE, minecraft.player);
            }
         } else {
            int var8 = Math.max(0, this.anchorsBeforeStash - this.countItem(Items.RESPAWN_ANCHOR));
            if (var8 == 0) {
               this.stashStopReason = var3 < 0 ? "no anchors to store" : "the ender chest is full";
            }

            LarpLog.info(this.getName(), "Stored {} anchors in the ender chest{}", var8, this.stashStopReason == null ? "" : " (" + this.stashStopReason + ")");
            this.selectTarget(3);
         }
      }
   }

   private void describeState() {
      if (minecraft.player.containerMenu instanceof ChestMenu) {
         minecraft.player.closeContainer();
      } else {
         this.enderChestsBeforePickup = this.countItem(Items.ENDER_CHEST);
         this.selectTarget(4);
      }
   }

   private void hasFluidNearby() {
      if (!minecraft.level.getBlockState(this.enderChestPos).is(Blocks.ENDER_CHEST)) {
         this.dropWaitStartTick = this.runTicks;
         this.selectTarget(5);
      } else if (!this.endRun(new ItemStack(Items.ENDER_CHEST))) {
         this.failStash("no room to pick the ender chest back up");
      } else if (this.findSilkPickaxeSlot() < 0) {
         this.failStash("no Silk Touch pickaxe in the hotbar");
      } else {
         BlockPos var1 = this.enderChestPos;
         if (!BlockBreaker.breakBlock(this, var1, this.chooseAnchor(), true, 12.0F, var1x -> this.findSilkPickaxeSlot(), () -> true)
            && this.runTicks - this.stashStepTick > 60) {
            this.failStash("cannot reach the ender chest to take it back");
         }
      }
   }

   private void scanLoadedChunks() {
      if (this.countItem(Items.ENDER_CHEST) > this.enderChestsBeforePickup) {
         BlockBreaker.release(this);
         int var2 = Math.max(0, this.anchorsBeforeStash - this.countItem(Items.RESPAWN_ANCHOR));
         if (this.stashStopReason != null && var2 == 0) {
            this.stopRun("your inventory is full and " + this.stashStopReason);
         } else {
            this.notify(AnchorHunter.NoticeLevel.SUCCESS, var2 + " anchors stored in your ender chest");
            this.enderChestPos = null;
            this.setState(this.resumeState, "stashed");
         }
      } else {
         ItemEntity var1 = this.notify(Items.ENDER_CHEST, 8.0);
         if (var1 != null && this.runTicks - this.dropWaitStartTick < 200) {
            this.planRoute(var1);
         } else {
            if (this.runTicks - this.dropWaitStartTick > 60) {
               this.failStash("lost the ender chest");
            }
         }
      }
   }

   private void failStash(String var1) {
      this.clearMovement();
      if (minecraft.player.containerMenu instanceof ChestMenu) {
         minecraft.player.closeContainer();
      }

      LarpLog.info(this.getName(), "Ender chest stash failed: {}", var1);
      this.stopRun("your inventory is full (" + var1 + ")");
   }

   private boolean selectHotbarSlot(int var1) {
      Inventory var2 = minecraft.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         BlockBreaker.release(this);
         var2.setSelectedSlot(var1);
         this.pendingHotbarSlot = var1;
         this.hotbarSwitchTick = this.runTicks;
         return false;
      }

      if (this.pendingHotbarSlot != var1) {
         this.pendingHotbarSlot = var1;
         this.hotbarSwitchTick = this.runTicks;
      }

      return this.runTicks - this.hotbarSwitchTick >= 3;
   }

   private void checkSetup() {
      this.sellStep = 0;
      this.sellStepTick = this.runTicks;
      this.sellButtonSlot = -1;
      this.anchorsBeforeSell = this.countItem(Items.RESPAWN_ANCHOR);
      LarpLog.info(this.getName(), "Selling {} anchors with /sell", this.anchorsBeforeSell);
      this.setState(AnchorHunter.State.SELLING, "selling");
   }

   private void stopRun(int var1) {
      this.sellStep = var1;
      this.sellStepTick = this.runTicks;
   }

   private void chooseAnchor() {
      if (this.runTicks - this.sellStepTick > 200) {
         this.scanNearbyAnchors("the Sell menu took too long (step " + (this.sellStep + 1) + ")");
      } else {
         switch (this.sellStep) {
            case 0:
               if (!minecraft.player.onGround()
                  || minecraft.player.containerMenu != minecraft.player.inventoryMenu
                  || WorldPathView.currentScreen() instanceof AbstractContainerScreen) {
                  return;
               }

               if (this.countItem(Items.RESPAWN_ANCHOR) == 0) {
                  this.scanNearbyAnchors();
                  return;
               }

               minecraft.player.connection.sendCommand("sell");
               this.stopRun(1);
               break;
            case 1:
               ChestMenu var12 = this.scanLoadedChunks();
               if (var12 == null) {
                  return;
               }

               int var13 = var12.getRowCount() * 9;
               this.sellButtonSlot = -1;

               for (int var14 = var13 - 9; var14 < var13; var14++) {
                  ItemStack var4 = var12.getSlot(var14).getItem();
                  if (!var4.isEmpty() && (var4.has(DataComponents.CUSTOM_NAME) || var4.has(DataComponents.ITEM_NAME)) && var4.getCount() == 1) {
                     String var5 = BuiltInRegistries.ITEM.getKey(var4.getItem()).toString();
                     ItemLore var6 = (ItemLore)var4.get(DataComponents.LORE);
                     ArrayList var7 = new ArrayList();
                     if (var6 != null) {
                        for (Component var9 : var6.lines()) {
                           var7.add(var9.getString());
                        }
                     }

                     if (SellMenuLogic.isSellConfirmButtonWithLore(var5, var4.getHoverName().getString(), var7, var14, var13)) {
                        if (this.sellButtonSlot >= 0) {
                           this.scanNearbyAnchors("the Sell menu has more than one Sell button");
                           return;
                        }

                        this.sellButtonSlot = var14;
                     }
                  }
               }

               if (this.sellButtonSlot < 0) {
                  this.scanNearbyAnchors("the Sell menu has no Sell button in its bottom row");
                  return;
               }

               for (int var15 = 0; var15 < var13 - 9; var15++) {
                  if (!var12.getSlot(var15).getItem().isEmpty()) {
                     this.scanNearbyAnchors("the Sell menu already holds items");
                     return;
                  }
               }

               this.stopRun(2);
               break;
            case 2:
               ChestMenu var11 = this.scanLoadedChunks();
               if (var11 == null) {
                  this.scanNearbyAnchors("the Sell menu closed");
                  return;
               }

               if (minecraft.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4 || !var11.getCarried().isEmpty()) {
                  return;
               }

               int var2 = var11.getRowCount() * 9;

               for (int var3 = var2; var3 < var11.slots.size(); var3++) {
                  if (((Slot)var11.slots.get(var3)).getItem().is(Items.RESPAWN_ANCHOR)) {
                     if (this.runTicks - this.lastSellClickTick >= 4) {
                        this.lastSellClickTick = this.runTicks;
                        minecraft.gameMode.handleContainerInput(var11.containerId, var3, 0, ContainerInput.QUICK_MOVE, minecraft.player);
                     }

                     return;
                  }
               }

               if (this.selectTarget(var11) == 0) {
                  this.scanNearbyAnchors("no anchors went into the Sell menu");
                  return;
               }

               this.stopRun(3);
               break;
            case 3:
               ChestMenu var10 = this.scanLoadedChunks();
               if (var10 == null) {
                  this.scanNearbyAnchors("the Sell menu closed before selling");
                  return;
               }

               if (this.runTicks - this.lastSellClickTick < 4 || !var10.getCarried().isEmpty()) {
                  return;
               }

               this.lastSellClickTick = this.runTicks;
               minecraft.gameMode.handleContainerInput(var10.containerId, this.sellButtonSlot, 0, ContainerInput.PICKUP, minecraft.player);
               this.stopRun(4);
               break;
            default:
               ChestMenu var1 = this.scanLoadedChunks();
               if (var1 != null && this.selectTarget(var1) > 0) {
                  if (this.runTicks - this.sellStepTick > 100) {
                     this.scanNearbyAnchors("the server did not take the anchors");
                  }

                  return;
               }

               if (var1 != null) {
                  minecraft.player.closeContainer();
               }

               this.scanNearbyAnchors();
         }
      }
   }

   private int selectTarget(ChestMenu var1) {
      int var2 = 0;
      int var3 = var1.getRowCount() * 9;

      for (int var4 = 0; var4 < var3 - 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (var5.is(Items.RESPAWN_ANCHOR)) {
            var2 += var5.getCount();
         }
      }

      return var2;
   }

   private ChestMenu scanLoadedChunks() {
      return WorldPathView.currentScreen() instanceof ContainerScreen var1
            && minecraft.player.containerMenu instanceof ChestMenu var2
            && var1.getMenu() == var2
            && ClickStepTimer.namesMatch(var1.getTitle().getString(), "Sell")
         ? var2
         : null;
   }

   private void scanNearbyAnchors() {
      int var1 = Math.max(0, this.anchorsBeforeSell - this.countItem(Items.RESPAWN_ANCHOR));
      this.soldCount += var1;
      LarpLog.info(this.getName(), "Sold {} anchors ({} this run)", var1, this.soldCount);
      if (var1 > 0) {
         this.notify(AnchorHunter.NoticeLevel.SUCCESS, "Sold " + var1 + " anchor" + (var1 == 1 ? "" : "s") + " (" + this.soldCount + " this run)");
      }

      int var2 = this.stopAfterSetting.getValue().intValue();
      if (var2 > 0 && this.anchorsCollected >= var2) {
         this.setState("collected " + this.anchorsCollected + " anchors");
      } else {
         this.setState(AnchorHunter.State.SELECTING_ANCHOR, "sold");
      }
   }

   private void scanNearbyAnchors(String var1) {
      if (minecraft.player.containerMenu instanceof ChestMenu) {
         minecraft.player.closeContainer();
      }

      this.sellRetryAt = System.currentTimeMillis() + 300000L;
      LarpLog.info(this.getName(), "Selling stopped: {}; trying again in five minutes", var1);
      this.notify(AnchorHunter.NoticeLevel.WARNING, "Could not sell anchors: " + var1);
      int var2 = this.stopAfterSetting.getValue().intValue();
      if (var2 > 0 && this.anchorsCollected >= var2) {
         this.setState("collected " + this.anchorsCollected + " anchors");
      } else {
         this.setState(AnchorHunter.State.SELECTING_ANCHOR, "sell failed");
      }
   }

   private boolean sendChatMessage() {
      LocalPlayer var1 = minecraft.player;
      switch (this.pearlState) {
         case AIMING:
            if (this.runTicks - this.pearlStateTick > 40 || !this.disableConflictingModules()) {
               this.pearlState = AnchorHunter.PearlState.IDLE;
               this.nextPearlTick = this.runTicks + 60;
               return false;
            } else if (!InteractionArbiter.ownsInteraction(this)) {
               return true;
            } else {
               float[] var4 = this.motion.rotate(var1.getYRot(), var1.getXRot(), this.pearlYaw, this.pearlPitch, false);
               RotationManager.requestForModule(this, RotationManager.Priority.BOT, var4[0], var4[1], 1.0);
               int var3 = Math.abs(Mth.wrapDegrees(this.pearlYaw - var1.getYRot())) <= 0.75F && Math.abs(this.pearlPitch - var1.getXRot()) <= 0.75F ? 1 : 0;
               if (var3 != 0 && var1.getDeltaMovement().horizontalDistanceSqr() < 1.0E-5) {
                  this.buildWorldView();
               }

               return true;
            }
         case THROWN:
            int var2 = var1.position().distanceTo(this.pearlThrowOrigin) > 4.0 && var1.onGround() ? 1 : 0;
            if (var2 != 0 || this.runTicks - this.pearlStateTick > 100) {
               LarpLog.info(this.getName(), var2 != 0 ? "Pearl landed at {}" : "Pearl throw from {} did not arrive", var1.blockPosition().toShortString());
               this.pearlState = AnchorHunter.PearlState.IDLE;
               this.nextPearlTick = this.runTicks + 20;
               this.pathFollower.reset();
            }

            return true;
         default:
            if (this.runTicks < this.nextPearlTick || !this.disableConflictingModules() || !var1.onGround()) {
               return false;
            } else if (!this.scanLoadedChunks()) {
               this.nextPearlTick = this.runTicks + 100;
               return false;
            } else {
               this.clearMovement();
               this.pearlState = AnchorHunter.PearlState.AIMING;
               this.pearlStateTick = this.runTicks;
               return true;
            }
      }
   }

   private boolean disableConflictingModules() {
      LocalPlayer var1 = minecraft.player;
      int var2 = AutoSustain.findHotbarSlot(var0 -> var0.is(Items.ENDER_PEARL));
      return var2 >= 0
         && !var1.getCooldowns().isOnCooldown(var1.getInventory().getItem(var2))
         && var1.getArmorValue() >= 12
         && var1.getHealth() + var1.getAbsorptionAmount() >= 12.0F;
   }

   private boolean scanLoadedChunks() {
      WorldPathView var1 = this.buildWorldView(this.targetAnchor);
      AnchorPlacement.StandSearchResult var2 = AnchorPlacement.findStandPositions(
         var1, this.targetAnchor.getX(), this.targetAnchor.getY(), this.targetAnchor.getZ(), this.resetRun()
      );
      ArrayList var3 = new ArrayList();

      for (AnchorPlacement.StandPosition var5 : var2.exposed()) {
         if (var3.size() >= 3) {
            break;
         }

         var3.add(new Vec3(var5.standX() + 0.5, var5.standY(), var5.standZ() + 0.5));
      }

      if (var3.isEmpty()) {
         for (byte var15 = -4; var15 <= 4 && var3.size() < 3; var15 += 4) {
            for (byte var17 = -4; var17 <= 4 && var3.size() < 3; var17 += 4) {
               int var6 = this.targetAnchor.getX() + var15;
               int var7 = this.targetAnchor.getZ() + var17;
               int var8 = minecraft.level.getHeight(Types.MOTION_BLOCKING_NO_LEAVES, var6, var7);
               if (var1.floor(var6, var8 - 1, var7) && var1.open(var6, var8, var7) && var1.open(var6, var8 + 1, var7)) {
                  var3.add(new Vec3(var6 + 0.5, var8, var7 + 0.5));
               }
            }
         }
      }

      WorldRaycaster var16 = new WorldRaycaster(minecraft.level, minecraft.player);
      Vec3 var18 = minecraft.player.getEyePosition();

      for (Vec3 var20 : var3) {
         float var21 = (float)(Math.toDegrees(Math.atan2(var20.z - var18.z, var20.x - var18.x)) - 90.0);
         Vec3 var9 = null;
         float var10 = 0.0F;
         int var11 = Integer.MAX_VALUE;

         for (int var12 = -55; var12 <= 45; var12++) {
            PearlTrajectory.Simulation var13 = PearlTrajectory.simulate(
               PearlTrajectory.PearlState.thrown(var18, Vec3.directionFromRotation(var12, var21), Vec3.ZERO), var16
            );
            Vec3 var14 = var13.impact();
            if (var14 != null && !(var14.distanceTo(var20) > 2.0) && var13.ticks() < var11) {
               var9 = var14;
               var10 = var12;
               var11 = var13.ticks();
            }
         }

         if (var9 != null) {
            this.pearlYaw = var21;
            this.pearlPitch = var10;
            this.pearlLanding = var9;
            LarpLog.info(this.getName(), "Pearl throw toward {} (pitch {}, {} ticks of flight)", BlockPos.containing(var9).toShortString(), var10, var11);
            return true;
         }
      }

      LarpLog.debug(this.getName(), "No pearl throw lands near the anchor at {}", this.targetAnchor.toShortString());
      return false;
   }

   private void buildWorldView() {
      LocalPlayer var1 = minecraft.player;
      this.pearlThrowOrigin = var1.position();
      InteractionArbiter.queueAction(this, () -> {
         int var2 = AutoSustain.findHotbarSlot(var0 -> var0.is(Items.ENDER_PEARL));
         if (var2 >= 0) {
            Inventory var3 = var1.getInventory();
            int var4 = var3.getSelectedSlot();
            var3.setSelectedSlot(var2);

            try {
               if (minecraft.gameMode.useItem(var1, InteractionHand.MAIN_HAND).consumesAction()) {
                  SwingHelper.swingInteract(var1, InteractionHand.MAIN_HAND);
                  this.pearlState = AnchorHunter.PearlState.THROWN;
                  this.pearlStateTick = this.runTicks;
               }
            } finally {
               if (var3.getSelectedSlot() == var2) {
                  var3.setSelectedSlot(var4);
               }
            }
         }
      }, () -> minecraft.player == var1 && this.isRuntimeActive() && this.pearlState == AnchorHunter.PearlState.AIMING);
   }

   private void notify() {
      this.dangerPoints.clear();
      this.nearestPlayerDistance = Double.MAX_VALUE;
      double var1 = this.playerRangeSetting.getValue();
      MCFriendModule var3 = LarpClient.getInstance().getModuleManager().getModule(MCFriendModule.class);
      String var4 = null;

      for (AbstractClientPlayer var6 : minecraft.level.players()) {
         if (var6 != minecraft.player && !var6.isSpectator()) {
            String var7 = var6.getGameProfile().name();
            if (!this.ignoreFriendsSetting.getValue() || var3 == null || !var3.isFriend(var7)) {
               double var8 = var6.distanceTo(minecraft.player);
               if (!(var8 > var1)) {
                  this.dangerPoints.add(new double[]{var6.getX(), var6.getZ()});
                  if (var8 < this.nearestPlayerDistance) {
                     this.nearestPlayerDistance = var8;
                     var4 = var7;
                  }
               }
            }
         }
      }

      if (var4 != null) {
         this.nearbyPlayerName = var4;
         if (!this.playerNearby) {
            this.playerNearby = (boolean)1;
            LarpLog.info(this.getName(), "{} is {} blocks away; leaving by the next /rtp", var4, (int)this.nearestPlayerDistance);
            this.notify(AnchorHunter.NoticeLevel.WARNING, "Player " + var4 + " is " + (int)Math.round(this.nearestPlayerDistance) + " blocks away, leaving");
         }
      }
   }

   private void collectAnchorDrop(long var1) {
      float var3 = minecraft.player.getHealth();
      if (var3 < this.lastHealth && minecraft.player.hurtTime > 0) {
         for (AbstractClientPlayer var5 : minecraft.level.players()) {
            if (var5 != minecraft.player && var5.distanceTo(minecraft.player) < 6.0F) {
               this.lastPlayerHitAt = var1;
               break;
            }
         }
      }

      this.lastHealth = var3;
   }

   private void isStashChestOpen(long var1) {
      BlockBreaker.release(this);
      if (minecraft.player.containerMenu instanceof ChestMenu) {
         minecraft.player.closeContainer();
      }

      if (this.state != AnchorHunter.State.PLAYER_NEARBY
         && this.state != AnchorHunter.State.AWAITING_TELEPORT
         && this.state != AnchorHunter.State.STANDING_STILL) {
         this.setState(AnchorHunter.State.PLAYER_NEARBY, "player " + this.nearbyPlayerName);
      }

      if (this.rtpTimer.hasTimedOut(var1)) {
         this.regionPicker.distanceTo(this.currentRegion);
         if (this.rtpTimer.recordFailure(var1)) {
            this.stopRun("/rtp did not answer 5 times in a row");
            return;
         }
      }

      double var3 = this.dangerPoints.isEmpty() ? 64.0 : this.nearestPlayerDistance;
      RegionAttemptScheduler.Action var5 = RegionAttemptScheduler.chooseAction(
         true,
         this.rtpTimer.canAttempt(var1),
         this.rtpTimer.isInFlight(),
         this.countdownSeen,
         var3,
         this.rtpTimer.millisUntilNext(var1),
         this.logOutIfCaughtSetting.getValue(),
         this.lastPlayerHitAt < 0L ? -1L : var1 - this.lastPlayerHitAt
      );
      switch (var5) {
         case FLEE:
            this.sendRtp(var1);
            break;
         case ENGAGE:
            this.checkSetup("a player came within " + (int)Math.round(var3) + " blocks during the /rtp cooldown");
            break;
         case RETREAT:
            this.setStatus("Player nearby, standing still for the teleport");
            break;
         case HOLD:
            this.logRouteFailure();
            this.setStatus(
               "Player nearby, running (/rtp " + (this.rtpTimer.isInFlight() ? "pending" : "in " + chooseAnchor(this.rtpTimer.millisUntilNext(var1))) + ")"
            );
         case NONE:
      }
   }

   private void logRouteFailure() {
      this.findPickaxeSlot(24.0, 40);
   }

   private void findPickaxeSlot(double var1, int var3) {
      if (!this.dangerPoints.isEmpty() || this.fleeTarget != null) {
         if (this.fleeTarget == null || this.runTicks - this.fleeRepathTick > var3 || !this.pathFollower.hasRemainingNodes()) {
            this.fleeRepathTick = this.runTicks;
            if (this.dangerPoints.isEmpty()) {
               return;
            }

            this.fleeTarget = RegionAttemptScheduler.computeEscapePoint(minecraft.player.getX(), minecraft.player.getZ(), this.dangerPoints, var1);
            BlockPos var4 = this.resetTarget();
            AStarPathfinder.Bounds var5 = AStarPathfinder.Bounds.around(
               var4.getX(), var4.getY(), var4.getZ(), var4.getX(), var4.getY(), var4.getZ(), (int)var1 + 8, 10, 10
            );
            AStarPathfinder.PathResult var6 = AStarPathfinder.findPath(
               this.buildWorldView(null),
               var4.getX(),
               var4.getY(),
               var4.getZ(),
               AStarPathfinder.Goal.awayFrom(List.copyOf(this.dangerPoints), var1),
               var5,
               AStarPathfinder.Options.walking(8000).withPartial(true)
            );
            if (var6.found() && !var6.steps().isEmpty()) {
               this.pathFollower.setPathWithEnd(var6.steps(), var6.complete());
            }
         }

         PathFollower.Result var7 = this.pathFollower.tick(this.chooseAnchor(), this::isPlayerSteering, true);
         this.applyPathInput();
         if (var7 == PathFollower.Result.ARRIVED || var7 == PathFollower.Result.FAILED) {
            this.pathFollower.reset();
         }
      }
   }

   private boolean avoidMobs(long var1) {
      LocalPlayer var3 = minecraft.player;
      ArrayList var4 = new ArrayList();
      byte var5 = 0;
      LivingEntity var6 = null;
      double var7 = Math.min(3.0, var3.entityInteractionRange());

      for (LivingEntity var10 : minecraft.level
         .getEntitiesOfClass(LivingEntity.class, var3.getBoundingBox().inflate(10.0), var0 -> var0 instanceof Enemy && var0.isAlive())) {
         byte var11 = var10 instanceof Creeper;
         int var12 = !(var10 instanceof RangedAttackMob) && !(var10 instanceof CrossbowAttackMob) ? 0 : 1;
         double var13 = var10.distanceTo(var3);
         if (this.fightMobsSetting.getValue() && RegionAttemptScheduler.shouldRetreat((boolean)var11, (boolean)var12, var3.getHealth())) {
            if (RegionAttemptScheduler.millisUntilNext((boolean)var11, (boolean)var12, InteractionArbiter.claimManualInteraction(var10), var7, var3.getHealth())
               && var3.hasLineOfSight(var10)
               && (var6 == null || var13 < var6.distanceTo(var3))) {
               var6 = var10;
            }
         } else if (RegionAttemptScheduler.isInRange(var13, (boolean)var11, var3.hasLineOfSight(var10), var3.getHealth(), this.fleeingMobs)) {
            var4.add(var10);
            var5 |= var11;
         }
      }

      if (var4.isEmpty() && var6 != null) {
         this.abandonAnchor(var6, var7);
         return true;
      }

      if (this.fightTarget != null) {
         LarpLog.info(this.getName(), "Done fighting; carrying on");
         this.fightTarget = null;
      }

      if (var4.isEmpty()) {
         if (this.fleeingMobs) {
            this.fleeingMobs = (boolean)0;
            this.pathFollower.reset();
            this.fleeTarget = null;
            LarpLog.info(this.getName(), "Away from the mobs; carrying on");
         }

         return false;
      } else {
         if (!this.fleeingMobs) {
            this.fleeingMobs = (boolean)1;
            if (this.targetAnchor != null && ++this.mobFleeCount >= 3) {
               LarpLog.info(this.getName(), "Ran from mobs three times on the way to {}; leaving it", this.targetAnchor.toShortString());
               this.abandonAnchor("mobs keep guarding it");
            }

            this.pathFollower.reset();
            this.fleeTarget = null;
            LivingEntity var15 = var4.stream().min(Comparator.comparingDouble(var1x -> var1x.distanceTo(var3))).orElseThrow();
            LarpLog.info(
               this.getName(),
               "{} {} blocks away at {} health; running",
               var15.getType().getDescription().getString(),
               String.format(Locale.ROOT, "%.1f", var15.distanceTo(var3)),
               (int)var3.getHealth()
            );
         }

         BlockBreaker.release(this);
         NativePlacement.cancel(this);
         this.autoEat.stopEating();
         if (var3.containerMenu instanceof ChestMenu) {
            var3.closeContainer();
         }

         if (!this.singleplayer && this.rtpTimer.canAttempt(var1)) {
            int var16 = this.teleportCount;
            this.sendRtp(var1);
            if (this.teleportCount > var16) {
               LarpLog.info(this.getName(), "{} close; left by /rtp", var5 != 0 ? "Creeper" : "Hostile mob");
            }
         }

         this.dangerPoints.clear();

         for (LivingEntity var18 : var4) {
            this.dangerPoints.add(new double[]{var18.getX(), var18.getZ()});
         }

         this.findPickaxeSlot(var5 != 0 ? 18.0 : 12.0, 20);
         if (!this.pathFollower.hasRemainingNodes()) {
            this.fleeToSafeSpot();
         }

         this.setStatus(var5 != 0 ? "Running from a creeper" : "Running from hostile mobs");
         return true;
      }
   }

   private void abandonAnchor(LivingEntity var1, double var2) {
      LocalPlayer var4 = minecraft.player;
      if (this.fightTarget != var1) {
         this.fightTarget = var1;
         LarpLog.info(
            this.getName(),
            "Fighting a {} {} blocks away at {} health",
            var1.getType().getDescription().getString(),
            String.format(Locale.ROOT, "%.1f", var1.distanceTo(var4)),
            (int)var4.getHealth()
         );
      }

      BlockBreaker.release(this);
      NativePlacement.cancel(this);
      this.autoEat.stopEating();
      this.pathFollower.reset();
      int var5 = this.buildWorldView();
      if (var5 >= 0 && var4.getInventory().getSelectedSlot() != var5) {
         var4.getInventory().setSelectedSlot(var5);
      }

      if (InteractionArbiter.ownsInteraction(this)) {
         Vec3 var6 = var1.getBoundingBox().getCenter().add(0.0, var1.getBbHeight() * 0.2, 0.0);
         float[] var7 = RotationManager.calculateAngles(var4.getEyePosition(), var6);
         float[] var8 = this.motion.rotate(var4.getYRot(), var4.getXRot(), var7[0], var7[1], false);
         RotationManager.requestForModule(this, RotationManager.Priority.BOT, var8[0], var8[1], 1.0);
      }

      LivingEntity var9 = var1;
      InteractionArbiter.claimManualInteraction(this, var9, var2, true, () -> this.running && var9.isAlive() && minecraft.player == var4);
      this.setStatus("Fighting a " + var1.getType().getDescription().getString());
   }

   private int buildWorldView() {
      int var1 = -1;
      double var2 = -1.0;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = minecraft.player.getInventory().getItem(var4);
         boolean var6 = var5.is(ItemTags.SWORDS);
         boolean var7 = var5.is(ItemTags.AXES);
         if (var6 || var7) {
            double var8 = (var6 ? 10000 : 0) + var5.getMaxDamage();
            if (var8 > var2) {
               var2 = var8;
               var1 = var4;
            }
         }
      }

      return var1;
   }

   private void fleeToSafeSpot() {
      double[] var1 = RegionAttemptScheduler.computeEscapePoint(minecraft.player.getX(), minecraft.player.getZ(), this.dangerPoints, 3.0);
      if (var1 != null) {
         WorldPathView var2 = this.buildWorldView(null);
         BlockPos var3 = this.resetTarget();
         int var4 = (int)Math.floor(var1[0]);
         int var5 = (int)Math.floor(var1[1]);
         byte var6 = 0;

         for (int var7 = 1; var7 >= -3 && var6 == 0; var7--) {
            int var8 = var3.getY() + var7;
            var6 = (byte)(var2.open(var4, var8, var5)
                  && var2.open(var4, var8 + 1, var5)
                  && var2.floor(var4, var8 - 1, var5)
                  && var2.penalty(var4, var8, var5) < 8.0
               ? 1
               : 0);
         }

         if (var6 != 0) {
            this.moveDirection = new Vec3(var1[0] - minecraft.player.getX(), 0.0, var1[1] - minecraft.player.getZ());
            this.sprinting = (boolean)(minecraft.player.getFoodData().getFoodLevel() > 6 ? 1 : 0);
         }
      }
   }

   private void checkSetup(String var1) {
      LarpLog.info(this.getName(), "Logging out: {}", var1);
      String var2 = "Larp Anchor Hunter: " + var1 + ". " + this.anchorsCollected + " anchors collected.";
      ClientPacketListener var3 = minecraft.getConnection();
      this.stopRun("logged out, " + var1);
      if (var3 != null && !minecraft.isLocalServer()) {
         var3.getConnection().disconnect(Component.literal(var2));
      }
   }

   private static boolean getPauseReason(ItemStack var0) {
      return var0.get(DataComponents.FOOD) != null
         && !var0.is(Items.GOLDEN_APPLE)
         && !var0.is(Items.ENCHANTED_GOLDEN_APPLE)
         && !var0.is(Items.ROTTEN_FLESH)
         && !var0.is(Items.SPIDER_EYE)
         && !var0.is(Items.POISONOUS_POTATO)
         && !var0.is(Items.PUFFERFISH)
         && !var0.is(Items.CHORUS_FRUIT)
         && !var0.is(Items.SUSPICIOUS_STEW);
   }

   private int countItem(Item var1) {
      Inventory var2 = minecraft.player.getInventory();
      int var3 = 0;

      for (int var4 = 0; var4 < 36; var4++) {
         ItemStack var5 = var2.getItem(var4);
         if (var5.is(var1)) {
            var3 += var5.getCount();
         }
      }

      return var3;
   }

   private boolean clearMovement() {
      return this.endRun(new ItemStack(Items.RESPAWN_ANCHOR));
   }

   private boolean endRun(ItemStack var1) {
      Inventory var2 = minecraft.player.getInventory();

      for (int var3 = 0; var3 < 36; var3++) {
         ItemStack var4 = var2.getItem(var3);
         if (var4.isEmpty() || ItemStack.isSameItemSameComponents(var4, var1) && var4.getCount() < var4.getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }

   private int countPearls() {
      return this.isInGame() ? this.disableConflictingModules(Items.ENDER_PEARL) : 0;
   }

   private int disableConflictingModules(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 9; var3++) {
         ItemStack var4 = minecraft.player.getInventory().getItem(var3);
         if (var4.is(var1)) {
            var2 += var4.getCount();
         }
      }

      return var2;
   }

   private boolean getPauseReason() {
      return AutoSustain.findHotbarSlot(var0 -> var0.is(Items.ENDER_CHEST)) >= 0 && this.findSilkPickaxeSlot() >= 0;
   }

   static boolean chooseAnchor(ItemStack var0) {
      for (Holder var2 : var0.getEnchantments().keySet()) {
         if (var2.is(Enchantments.SILK_TOUCH)) {
            return true;
         }
      }

      return false;
   }

   private int findSilkPickaxeSlot() {
      if (!this.isInGame()) {
         return -1;
      }

      BlockState var1 = Blocks.ENDER_CHEST.defaultBlockState();

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = minecraft.player.getInventory().getItem(var2);
         if (chooseAnchor(var3) && var3.isCorrectToolForDrops(var1) && AutoSustain.getRemainingDurability(var3) > 5) {
            return var2;
         }
      }

      return -1;
   }

   private int findPickaxeSlot(BlockState var1) {
      if (!this.isInGame()) {
         return -1;
      }

      int var2 = -1;
      float var3 = -1.0F;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = minecraft.player.getInventory().getItem(var4);
         if (var5.isCorrectToolForDrops(var1) && AutoSustain.getRemainingDurability(var5) > 20) {
            float var6 = var5.getDestroySpeed(var1) + (var5.has(DataComponents.CUSTOM_NAME) ? 0 : 1000);
            if (var6 > var3) {
               var3 = var6;
               var2 = var4;
            }
         }
      }

      return var2;
   }

   private int isPlayerSteering(BlockState var1) {
      Inventory var2 = minecraft.player.getInventory();
      int var3 = var2.getSelectedSlot();
      float var4 = -1.0F;

      for (int var5 = 0; var5 < 9; var5++) {
         ItemStack var6 = var2.getItem(var5);
         if (!var6.isDamageableItem() || AutoSustain.getRemainingDurability(var6) > 5) {
            float var7 = var6.getDestroySpeed(var1)
               + (var6.isCorrectToolForDrops(var1) ? 100 : 0)
               + (var6.has(DataComponents.CUSTOM_NAME) ? 0 : 1000)
               + (var5 == var2.getSelectedSlot() ? 0.5F : 0.0F);
            if (var7 > var4) {
               var4 = var7;
               var3 = var5;
            }
         }
      }

      return var3;
   }

   private boolean handleInventoryFull() {
      int var1 = this.findPickaxeSlot(Blocks.RESPAWN_ANCHOR.defaultBlockState());
      return var1 >= 0 && minecraft.player.getInventory().getItem(var1).has(DataComponents.CUSTOM_NAME) && !Boolean.FALSE.equals(this.toolCutsThreeByThree);
   }

   private void probeToolCut() {
      if (this.toolCutsThreeByThree == null && this.probeBlocks.isEmpty() && this.handleInventoryFull()) {
         BlockHitResult var1 = BlockBreaker.raycastLook(this.targetAnchor, this.chooseAnchor());
         if (var1 != null) {
            Axis var2 = var1.getDirection().getAxis();

            for (int var3 = -1; var3 <= 1; var3++) {
               for (int var4 = -1; var4 <= 1; var4++) {
                  if (var3 != 0 || var4 != 0) {
                     BlockPos var5 = var2 == Axis.X
                        ? this.targetAnchor.offset(0, var3, var4)
                        : (var2 == Axis.Y ? this.targetAnchor.offset(var3, 0, var4) : this.targetAnchor.offset(var3, var4, 0));
                     if (BlockBreaker.canBreak(var5)) {
                        this.probeBlocks.add(var5.immutable());
                     }
                  }
               }
            }
         }
      }
   }

   private void findSilkPickaxeSlot() {
      if (!this.probeBlocks.isEmpty() && this.runTicks - this.dropWaitStartTick >= 10) {
         long var1 = this.probeBlocks.stream().filter(var0 -> minecraft.level.getBlockState(var0).isAir()).count();
         this.toolCutsThreeByThree = var1 * 4L >= this.probeBlocks.size() * 3L;
         LarpLog.info(
            this.getName(),
            "Tool check: {} of {} blocks around the anchor broke, so the renamed pickaxe {}",
            var1,
            this.probeBlocks.size(),
            this.toolCutsThreeByThree ? "cuts 3x3" : "breaks one block at a time"
         );
         this.sendChatMessage(
            this.toolCutsThreeByThree
               ? "your renamed pickaxe cuts 3x3, so it keeps away from lava and water."
               : "your renamed pickaxe breaks one block at a time here."
         );
         this.probeBlocks.clear();
      }
   }

   private boolean hasFluidNearby(BlockPos var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            for (int var4 = -1; var4 <= 1; var4++) {
               if (!minecraft.level.getFluidState(var1.offset(var2, var3, var4)).isEmpty()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private double chooseAnchor() {
      return Math.min(4.5, minecraft.player.blockInteractionRange());
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (InteractionArbiter.ownsInteraction(this)) {
            float[] var3 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var1);
            BlockPos var4 = this.pathFollower.getRemainingPath() != null
               ? this.pathFollower.getRemainingPath()
               : (this.state == AnchorHunter.State.MINING ? this.targetAnchor : null);
            int var5 = var4 != null && BlockBreaker.raycastLook(var4, this.chooseAnchor()) != null ? 1 : 0;
            float[] var6 = this.motion.rotate(minecraft.player.getYRot(), minecraft.player.getXRot(), var3[0], var3[1], (boolean)var5);
            RotationManager.requestForModule(this, RotationManager.Priority.BOT, var6[0], var6[1], 1.0);
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = this.motion.rotate(minecraft.player.getYRot(), minecraft.player.getXRot(), var1, var2, false);
         RotationManager.requestForModule(this, RotationManager.Priority.BOT, var3[0], var3[1], 1.0);
      }
   }

   private void getOnlinePlayerNames(Vec3 var1) {
      if (InteractionArbiter.ownsInteraction(this)) {
         float[] var2 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var1);
         float[] var3 = this.motion.rotate(minecraft.player.getYRot(), minecraft.player.getXRot(), var2[0], var2[1], false);
         RotationManager.requestForModule(this, RotationManager.Priority.BOT, var3[0], var3[1], 1.0);
      }
   }

   public static Input collectAnchorDrop() {
      if (instance != null && instance.isEnabled()) {
         AnchorHunter var0 = instance;
         Minecraft var1 = Minecraft.getInstance();
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.running
            && var1.player != null
            && var1.player == var0.runPlayer
            && var1.player.tickCount == var0.lastTickCount
            && !ViewController.isFreecam()
            && !MovementCorrection.isRotationActive()
            && !(WorldPathView.currentScreen() instanceof AbstractContainerScreen)
            && (!RotationManager.hasRequest() || RotationManager.isActive(RotationManager.Priority.BOT))) {
            int var2 = var0.moveDirection.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 == 0 && !var0.jumping && !var0.sneaking) {
               return null;
            }

            Input var3 = var2 != 0
               ? var0.motion.directionToInput(var1.player.getYRot(), var0.moveDirection)
               : new Input(false, false, false, false, false, false, false);
            return new Input(var3.forward(), var3.backward(), var3.left(), var3.right(), var0.jumping, var0.sneaking, var0.sprinting);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void sendChatMessage(String var1) {
      if (minecraft != null && minecraft.player != null) {
         minecraft.player.sendSystemMessage(Component.literal("Larp Anchor Hunter: " + var1));
      }
   }

   private void notify(AnchorHunter.NoticeLevel var1, String var2) {
      LarpLog.debug(this.getName(), "Notice {}: {}", var1, var2);
      if (var1 != AnchorHunter.NoticeLevel.DEBUG) {
         long var3 = System.currentTimeMillis();
         if (var1 == AnchorHunter.NoticeLevel.ERROR || !var2.equals(this.lastNotice) || var3 - this.lastNoticeAt >= 30000L) {
            this.lastNotice = var2;
            this.lastNoticeAt = var3;
            String var5 = this.notificationsSetting.getValue();
            if (!var5.equals("Off") || var1 == AnchorHunter.NoticeLevel.ERROR) {
               if (!var5.equals("Chat") && !var5.equals("Off")) {
                  NotificationManager.Type var6 = switch (var1) {
                     case SUCCESS -> NotificationManager.Type.SUCCESS;
                     case WARNING, ERROR -> NotificationManager.Type.WARNING;
                     default -> NotificationManager.Type.INFO;
                  };
                  NotificationManager.getInstance().notify("Anchor Hunter", var2, var6, 5000L);
                  if (var1 == AnchorHunter.NoticeLevel.ERROR) {
                     this.sendChatMessage(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
                  }
               } else {
                  this.sendChatMessage(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
               }
            }
         }
      }
   }

   private static String checkSetup(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1);
   }

   static String setState(int var0, String var1) {
      return var0 + " " + var1 + (var0 == 1 ? "" : "s");
   }

   static String chooseAnchor(long var0) {
      return (var0 + 999L) / 1000L + "s";
   }

   private void setState() {
      if (this.isInGame()) {
         try {
            int var1 = Theme.scaleRounding(0.25);
            if (System.currentTimeMillis() < this.scanHighlightUntil) {
               for (BlockPos var3 : this.scanHighlights) {
                  Gizmos.cuboid(new AABB(var3).inflate(0.02), GizmoStyle.strokeAndFill(-14249, 2.0F, 587188311)).setAlwaysOnTop();
               }
            }

            if (!this.running || !this.showPathSetting.getValue() || minecraft.level != this.runLevel) {
               return;
            }

            if (this.targetAnchor != null) {
               Gizmos.cuboid(new AABB(this.targetAnchor).inflate(0.03), GizmoStyle.strokeAndFill(-14249, 2.5F, 872400983)).setAlwaysOnTop();
            }

            List var12 = this.pathFollower.getRemainingPath();
            double var13 = 0.05;
            Vec3 var5 = minecraft.player.position().add(0.0, var13, 0.0);

            for (AStarPathfinder.Step var7 : var12) {
               Vec3 var8 = new Vec3(var7.blockX() + 0.5, var7.blockY() + var13, var7.blockZ() + 0.5);
               Gizmos.line(var5, var8, ColorUtil.withAlpha(var1, 0.85), 2.0F).setAlwaysOnTop();
               var5 = var8;

               for (AStarPathfinder.DigTarget var10 : var7.digs()) {
                  Gizmos.cuboid(
                     new AABB(var10.blockX(), var10.blockY(), var10.blockZ(), var10.blockX() + 1, var10.blockY() + 1, var10.blockZ() + 1).inflate(0.01),
                     GizmoStyle.strokeAndFill(ColorUtil.withAlpha(-38037, 0.9), 1.5F, ColorUtil.withAlpha(-38037, 0.12))
                  );
               }
            }

            if (this.pearlState == AnchorHunter.PearlState.AIMING && this.pearlLanding != null) {
               Gizmos.cuboid(
                     new AABB(
                        this.pearlLanding.x - 0.5,
                        this.pearlLanding.y,
                        this.pearlLanding.z - 0.5,
                        this.pearlLanding.x + 0.5,
                        this.pearlLanding.y + 0.1,
                        this.pearlLanding.z + 0.5
                     ),
                     GizmoStyle.strokeAndFill(-8530948, 2.0F, 863884284)
                  )
                  .setAlwaysOnTop();
            }
         } catch (RuntimeException var11) {
            LarpLog.errorOnce(this.getName(), "Path overlay failed", var11);
         }
      }
   }

   @Override
   public ModulePanel.HudCard hudCard() {
      if (this.isInGame() && this.statusCardSetting.getValue() && this.isRuntimeActive() && this.running && minecraft.level == this.runLevel) {
         long var1 = System.currentTimeMillis();
         double var3 = Math.max(1.0E-6, (var1 - this.runStartedAt) / 3600000.0);
         int var5 = this.stopAfterSetting.getValue().intValue();
         String var6 = this.playerNearby
            ? "Player " + this.nearbyPlayerName + " nearby"
            : setState(this.anchorsCollected, "anchor") + (var1 - this.runStartedAt > 60000L ? ", " + Math.round(this.anchorsCollected / var3) + "/h" : "");
         String var7 = this.singleplayer
            ? "Singleplayer"
            : (
               this.rtpTimer.isInFlight()
                  ? "/rtp waiting"
                  : (this.rtpTimer.canAttempt(var1) ? "/rtp ready" : "/rtp in " + chooseAnchor(this.rtpTimer.millisUntilNext(var1)))
            );
         String var8 = this.getStatus();
         return new ModulePanel.HudCard(
            var6,
            var5 > 0 ? this.anchorsCollected + " of " + var5 : "",
            var8 != null && !var8.isBlank() ? var8 : "Starting",
            var7,
            var5 > 0 ? Math.clamp((double)this.anchorsCollected / var5, 0.0, 1.0) : -1.0,
            this.playerNearby
         );
      } else {
         return null;
      }
   }

   public AnchorHunter.State getInstance() {
      return this.state;
   }

   public boolean avoidMobs() {
      return this.running;
   }

   public int setState() {
      return this.anchorsCollected;
   }

   public BlockPos getConnection() {
      return this.targetAnchor;
   }

   public int endRun() {
      return this.pathFollower.getBlocksBroken();
   }

   public double distanceTo() {
      return this.pathFollower.reset();
   }

   enum NoticeLevel {
      DEBUG,
      SUCCESS,
      WARNING,
      ERROR;
   }

   private enum PearlState {
      IDLE,
      AIMING,
      THROWN;
   }

   public enum State {
      INITIALIZING("Getting ready"),
      SENDING_RTP("Sending /rtp"),
      AWAITING_TELEPORT("Waiting for the teleport"),
      STANDING_STILL("Standing still for the teleport"),
      AWAITING_RTP("Waiting for /rtp"),
      LANDING("Landing"),
      SCANNING("Looking for anchors"),
      SELECTING_ANCHOR("Choosing an anchor"),
      TRAVELING("Going to an anchor"),
      MINING("Mining an anchor"),
      COLLECTING("Picking up the anchor"),
      STORING("Putting anchors in the ender chest"),
      SELLING("Selling anchors"),
      PROTECTED_AREA("Protected area, leaving"),
      PLAYER_NEARBY("Player nearby, leaving"),
      PAUSED("Paused"),
      STOPPED("Stopped");

      final String LABEL;

      State(String var3) {
         this.LABEL = var3;
      }
   }
}
