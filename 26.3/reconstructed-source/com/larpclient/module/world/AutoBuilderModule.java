package com.larpclient.module.world;

import com.larpclient.LarpClient;
import com.larpclient.config.BuildOrderFile;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.gui.clickgui.ModulePanel;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.widget.SchematicPreviewWidget;
import com.larpclient.gui.widget.Widget;
import com.larpclient.hud.NotificationManager;
import com.larpclient.mixin.BlockStatePredictionHandlerMixin;
import com.larpclient.mixin.ClientLevelMixin3;
import com.larpclient.mixin.LocalPlayerMixin_4;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.StaffListModule;
import com.larpclient.module.misc.TextSetting;
import com.larpclient.module.player.AuctionBuyer;
import com.larpclient.module.player.AutoSustain;
import com.larpclient.render.world.WorldPathView;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.entity.MovementCorrection;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.entity.RotationController;
import com.larpclient.util.math.RotationSmoother2;
import com.larpclient.util.misc.AuctionParser;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.AStarPathfinder;
import com.larpclient.util.world.BlockBreaker;
import com.larpclient.util.world.BlockPlacementUtil;
import com.larpclient.util.world.BuildPlanner;
import com.larpclient.util.world.LayerScanPlan;
import com.larpclient.util.world.NativePlacement;
import com.larpclient.util.world.PathFollower;
import com.larpclient.util.world.Schematic;
import com.mojang.blaze3d.Blaze3D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.BooleanSupplier;
import java.util.regex.Pattern;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractSignEditScreen;
import net.minecraft.client.gui.screens.inventory.CrafterScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Plane;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.CrafterMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.BarrelBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.CrafterBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoBuilderModule extends WorldAutomationModule implements ModulePanel, NativePlacement.PlacementAimer {
   private static AutoBuilderModule instance;
   private static boolean hooksRegistered;
   static final int scanBatchSize = 4096;
   static final int maxPreviewCells = 2048;
   static final int placeSearchRange = 24;
   static final int settleTicks = 12;
   static final double reachMargin = 0.3;
   private static final int pillarSearchBudget = 600;
   static final int maxAttemptsPerBlock = 3;
   static final int placeTimeoutTicks = 40;
   static final int confirmTimeoutTicks = 40;
   static final int setAsideTicks = 400;
   static final int maxServerRemovals = 3;
   static final int serverRemovedSkipTicks = 2400;
   static final int placementCheckTicks = 20;
   static final int relogGhostThreshold = 8;
   static final int relogWindowTicks = 1200;
   static final long relogCooldownMs = 300000L;
   static final int maxRelogsPerBuild = 3;
   static final long logoffDelayMs = 5000L;
   static final long rejoinRetryMs = 10000L;
   static final long relogTimeoutMs = 120000L;
   static final int maxRejoinAttempts = 3;
   static final long rejoinResumeDelayMs = 3000L;
   private static final int relogIdle = 0;
   private static final int relogLoggingOff = 1;
   private static final int relogRejoining = 2;
   private static final int relogRejoined = 3;
   static final int pathSearchLimit = 16000;
   static final int buyStillTicks = 4;
   static final double maxStartDistance = 32.0;
   static final int unsetTick = -1000000000;
   static final int eatHungerThreshold = 10;
   static final int maxScannedCells = 200000;
   static final float aimToleranceDegrees = 2.0F;
   static final int maxProgress = 100;
   static final int containerClickDelayTicks = 6;
   static final int restockWalkTimeoutTicks = 900;
   static final int containerOpenTimeoutTicks = 60;
   static final int containerClicksTimeoutTicks = 400;
   static final int pillarBlockReserve = 32;
   static final int searchMargin = 10;
   static final double maxTurnSpeed = 35.0;
   static final int recordRadius = 8;
   static final int walkTimeoutTicks = 600;
   static final int facingProbeLength = 3;
   static final float aimSnapDegrees = 1.6F;
   static final float reversalDegrees = 6.0F;
   static final float sharpTurnDegrees = 26.0F;
   static final int towerReturnTimeoutTicks = 600;
   static final int pillarBlockTimeoutTicks = 80;
   static final int digTimeoutTicks = 200;
   static final int maxWalkwayBlocks = 24;
   static final int maxWalkwayAttempts = 3;
   static final int walkwayRemoveTimeoutTicks = 80;
   static final int sweepTimeoutTicks = 160;
   static final int minLeftoverTowerHeight = 3;
   static final int walkwayTimeoutTicks = 120;
   static final double walkwayPlaceDistance = 0.66;
   static final double walkwayPlaceThreshold = 0.54;
   static final double walkwayAimEpsilon = 0.08;
   static final double arriveDistance = 0.25;
   static final int layerLookahead = 64;
   static final int layerRetryLimit = 3;
   static final int maxSupportAttempts = 3;
   static final int towerSearchLimit = 120;
   static final int pillarLookaheadLayers = 2;
   static final int stallTimeoutTicks = 1200;
   static final int supportDigTimeoutTicks = 200;
   static final int supportWalkTimeoutTicks = 400;
   static final int wrongFacingTimeoutTicks = 400;
   static final int maxSupportFailures = 2;
   static final int maxFacingRetries = 2;
   static final int routeWalkTimeoutTicks = 400;
   static final int minPillarSupportSides = 3;
   static final double topPlaceMinRatio = 0.6;
   private final TextSetting schematicSetting = this.addSetting(
      new TextSetting("Schematic", "File name in the schematics folder (a .litematic or .schem file). The list at the top picks one", "")
         .setTitle("house.litematic")
   );
   private final TextSetting originSetting = this.addSetting(
      new TextSetting("Origin", "Where the schematic's origin goes, as x y z. The buttons under Set where it goes set it", "").setTitle("x y z")
   );
   private final ModeSetting rotationSetting = this.addSetting(
      new ModeSetting("Rotation", "Turn the schematic clockwise around its origin, seen from above", "0", "0", "90", "180", "270")
   );
   private final ModeSetting mirrorSetting = this.addSetting(
      new ModeSetting("Mirror", "Mirror the schematic first: X flips east and west, Z flips north and south", "None", "None", "X", "Z")
   );
   private final BooleanSetting autoMove = this.addToggleSetting(
      "Auto Move", "Walk to blocks that are out of reach. Off: only blocks within reach of where you stand", true
   );
   private final BooleanSetting sprint = this.addToggleSetting("Sprint", "Sprint on longer walks", true);
   private final BooleanSetting safeSneak = this.addToggleSetting("Safe Sneak", "Crouch next to drops so the bot never walks off an edge while it places", true);
   private final BooleanSetting refillHotbar = this.addToggleSetting(
      "Refill Hotbar",
      "Move the block it needs from your inventory into the hotbar, standing still. Only empty slots and blocks the build no longer needs are replaced",
      true
   );
   private final NumberSetting reach = this.addSliderSetting("Reach", "How far blocks are placed from, capped by your real reach", 4.5, 3.0, 6.0, 0.5);
   private final NumberSetting placeDelay = this.addSliderSetting("Place Delay", "Ticks between two placements (20 ticks are one second)", 3.0, 1.0, 20.0, 1.0);
   private final BooleanSetting autoEat = this.addToggleSetting("Auto Eat", "Eat food from your hotbar at 10 hunger or less", true);
   private final BooleanSetting showPreview = this.addToggleSetting(
      "Show Preview", "Draw the schematic in the world: missing blocks in the theme color, wrong blocks red, wrongly turned blocks orange", true
   );
   private final BooleanSetting showCorrect = this.addToggleSetting("Show Correct", "Also draw the blocks that are already right, in green", false);
   private final NumberSetting previewRange = this.addSliderSetting("Preview Range", "How far from you the preview draws blocks", 48.0, 8.0, 160.0, 8.0);
   private final BooleanSetting showPath = this.addToggleSetting("Show Path", "Draw the route the builder walks and the block it places next", true);
   private final BooleanSetting statusCard = this.addToggleSetting(
      "Status Card", "Show a card at the top of the screen with the progress and what the builder is doing", true
   );
   private final BooleanSetting notifyWhenDone = this.addToggleSetting("Notify When Done", "Show a notification when the build is finished", true);
   private final BooleanSetting pauseInGui = this.addToggleSetting(
      "Pause In GUI",
      "Stop while a menu other than chat or the pause menu is open, such as the ClickGUI. Chat and the pause menu never stop it; inventories always make it hold still",
      true
   );
   private final BooleanSetting temporaryPillars = this.addToggleSetting(
      "Temporary Pillars", "Pillar up on throwaway blocks to place blocks that are too high to reach from the ground, then dig the pillar away", true
   );
   private final NumberSetting maxPillarHeight = this.addSliderSetting("Max Pillar Height", "Tallest temporary pillar, in blocks", 6.0, 1.0, 64.0, 1.0);
   private final ModeSetting pillarBlock = this.addSetting(
      new ModeSetting(
         "Pillar Block",
         "Throwaway block the pillars are built of. Never one the schematic uses",
         "Dirt",
         "Dirt",
         "Cobblestone",
         "Netherrack",
         "Cobbled Deepslate"
      )
   );
   private final BooleanSetting restockFromContainers = this.addToggleSetting(
      "Restock From Containers", "When a block runs out, walk to a chest, barrel or shulker box you marked and take more from it", true
   );
   private final BooleanSetting putBackSpares = this.addToggleSetting(
      "Put Back Spares", "On a restock trip, put blocks the build no longer needs into the container, so the marked containers are the build's stockpile", true
   );
   private final KeybindSetting markRestockKey = this.addSetting(
      new KeybindSetting("Mark Restock Key", "Look at a chest, barrel or shulker box and press this to mark it as a restock container, or to unmark it", -1)
   );
   private final TextSetting restockContainers = this.addSetting(
      new TextSetting(
            "Restock Containers", "Marked containers, as dimension x y z, separated by commas. The Mark Restock Key and the Restock buttons set them", ""
         )
         .setTitle("minecraft:overworld x y z")
   );
   private final NumberSetting restockDistance = this.addSliderSetting(
      "Restock Distance", "Farthest a marked container may be for the builder to walk to it", 64.0, 8.0, 256.0, 8.0
   );
   private final BooleanSetting showRestockContainers = this.addToggleSetting("Show Restock Containers", "Outline the marked containers in the world", true);
   private final BooleanSetting stopOnStaffNearby = this.addToggleSetting(
      "Stop On Staff Nearby", "Stop building and log off when someone Staff List counts as staff comes within range, invisible ones included", true
   );
   private final NumberSetting staffRange = this.addSliderSetting(
      "Staff Range", "How close staff may come before the builder stops, in blocks", 48.0, 8.0, 128.0, 4.0
   );
   private final NumberSetting logOffDelay = this.addSliderSetting("Log Off Delay", "Seconds between stopping and logging off", 5.0, 0.0, 30.0, 0.5);
   private final TextSetting logOffMessage = this.addSetting(
      new TextSetting("Log Off Message", "Shown on the disconnect screen when staff nearby makes the builder log off", "Auto Builder stopped: staff nearby")
   );
   private final BooleanSetting buyMissingBlocks = this.addToggleSetting(
      "Buy Missing Blocks",
      "When a block runs out and no marked container has it, buy it from /ah on DonutSMP, then go on building. Spends your in-game money",
      false
   );
   private final NumberSetting maxPricePerItem = this.addSliderSetting(
         "Max Price Per Item", "Most it pays for one block, whatever the size of the listing. Type amounts like 50, 2K or 1.5M", 100.0, 1.0, 1.0E7, 1.0
      )
      .abbreviated();
   private final NumberSetting maxPricePerBuy = this.addSliderSetting(
         "Max Price Per Buy", "Most it pays for one listing. Type amounts like 10K, 5M or 1B", 10000.0, 100.0, 1.0E10, 100.0
      )
      .abbreviated();
   private final NumberSetting maxTotalSpend = this.addSliderSetting(
         "Max Total Spend", "Most it spends in one run, all purchases together. Type amounts like 100K, 10M or 1.5B", 100000.0, 1000.0, 1.0E12, 1000.0
      )
      .abbreviated();
   private final ModeSetting placementMode = this.addSetting(
      new ModeSetting(
         "Placement Mode",
         "Normal: your camera turns to every block with the builder, like a player's. Aggressive: while it places, your camera stays where you look and only the look sent to the server turns, easing back to your look afterwards. Walking still turns your camera",
         "Normal",
         "Normal",
         "Aggressive"
      )
   );
   private final BooleanSetting followRecording = this.addToggleSetting(
      "Follow Recording",
      "Build in the order you placed the blocks when you recorded this schematic with the Build Recorder. Ignored when it has no recording",
      true
   );
   private final BooleanSetting relogOnGhostBlocks = this.addToggleSetting(
      "Relog On Ghost Blocks",
      "When the server keeps taking placed blocks back (8 in a minute), log off, rejoin the same server and carry on building. At most once every 5 minutes and 3 times a build, never while temporary blocks stand",
      false
   );
   private static final String noSchematicLabel = "No schematic";
   private int fileListRefreshTicks;
   private final ModeSetting schematicFileSetting = new ModeSetting(
      "File", "A .litematic or .schem file from the schematics folder. New files appear here by themselves", "No schematic", "No schematic"
   ) {
      public String getValue() {
         String var1 = AutoBuilderModule.this.schematicSetting.getValue().trim();
         return !var1.isEmpty() && !AutoBuilderModule.this.unloaded ? var1 : "No schematic";
      }

      @Override
      public boolean acceptsValue(String var1) {
         return true;
      }

      @Override
      public void setValue(String var1) {
         AutoBuilderModule.this.getSchematic(var1);
      }

      @Override
      public boolean isSearchable() {
         return this.getModes().size() > 8;
      }

      @Override
      public String getModeLabel(String var1) {
         return var1.replaceFirst("(?i)\\.(litematic|schem|schematic)$", "");
      }
   };
   private volatile AutoBuilderModule.LoadResult pendingLoad;
   private volatile boolean loading;
   private int loadGeneration;
   private String loadedFileName = "";
   private Schematic schematic;
   private String loadError;
   private AutoBuilderModule.SchematicLayout layout;
   private boolean layoutDirty = (boolean)1;
   private boolean unloaded;
   private boolean running;
   private Object startLevel;
   private Object startPlayer;
   private String pendingStopReason;
   private String lastRunSummary;
   private final RotationSmoother2 movement = new RotationSmoother2(RotationSmoother2.Profile.QUICK);
   private final AutoSustain playerActions = new AutoSustain(this);
   private final PathFollower pathFollower = new PathFollower(this, this.movement, this::aimWhileWalking);
   private final Map<Integer, Integer> setAsideUntil = new HashMap<>();
   private final Map<Integer, Integer> attemptCounts = new HashMap<>();
   private final Map<Integer, Integer> serverRemovalCounts = new HashMap<>();
   private final Set<Integer> givenUpCells = new HashSet<>();
   private final Set<Integer> towerCells = new HashSet<>();
   private final Map<Integer, String> giveUpReasons = new HashMap<>();
   private final Map<Item, Integer> missingItems = new LinkedHashMap<>();
   private BuildPlanner.BlockTarget currentTarget;
   private BuildPlanner.BlockTarget lastPlaced;
   private int unconfirmedSinceTick = -1000000000;
   private AutoBuilderModule.CellAction currentAction = AutoBuilderModule.CellAction.PLACE;
   private BlockState stateBeforePlace;
   private final Map<Integer, AutoBuilderModule.CellAction> actionByCell = new HashMap<>();
   private final Set<Integer> configuredCrafters = new HashSet<>();
   private Integer activeCrafterCell;
   private int crafterOpenTick;
   private int containerOpenedTick = -1000000000;
   private int lastCrafterClickTick = -1000000000;
   private int rightClickCount;
   private int unsupportedCount;
   private final Map<Integer, BlockState> expectedStates = new HashMap<>();
   private BuildPlanner.Pillar activePillar;
   private AutoBuilderModule.TowerPhase towerPhase;
   private final List<BlockPos> pillarBlocks = new ArrayList<>();
   private final Set<Long> failedPillarColumns = new HashSet<>();
   private int pillarWalkStartTick;
   private int pillarPhaseTick;
   private int towerPlacedMark;
   private int pillarCount;
   private BuildPlanner.Column activeSupport;
   private AutoBuilderModule.SupportPhase supportPhase;
   private final List<BlockPos> supportBlocks = new ArrayList<>();
   private final Set<Long> skippedSupportSeeds = new HashSet<>();
   private final Map<Integer, Integer> supportFailures = new HashMap<>();
   private int supportFailureMark;
   private int supportLayerY;
   private int standingOnTempSinceTick = -1000000000;
   private int lastProgressCount;
   private int progressSinceTick;
   private int lastProgressTick;
   private final Set<Long> refusedBreaks = new HashSet<>();
   private BlockPos wrongFacingPos;
   private int wrongFacingCell;
   private int wrongFacingTick;
   private final Map<Integer, Integer> facingRetryCounts = new HashMap<>();
   private final Set<Integer> diagnosedCells = new HashSet<>();
   private int supportRetryMark;
   private long lastPillarKey = Long.MIN_VALUE;
   private long lastSupportKey = Long.MIN_VALUE;
   private boolean pillarForLayer;
   private int supportWalkStartTick;
   private int supportActionTick;
   private final List<BlockPos> walkwayBlocks = new ArrayList<>();
   private final Set<Long> recentPillarColumns = new HashSet<>();
   private boolean leftoverChecked;
   private int tickCounter;
   private int lastAimTick = -1000000000;
   private final SplittableRandom random = new SplittableRandom();
   private int extraPlaceDelay;
   private int sprintJumpUntil = -1000000000;
   private int nextSprintJumpTick = -1000000000;
   private double aimHeightOffset = 0.6;
   private double aimPhase;
   private boolean stopPending;
   private boolean settling;
   private float lastYaw = Float.NaN;
   private float lastYawDelta;
   private float maxYawDelta;
   private int reversalCount;
   private int sprintJumpCount;
   private int sharpTurnLogs;
   private final List<BlockPos> sweepBlocks = new ArrayList<>();
   private boolean sweepFromTower;
   private final Set<BlockPos> towerTried = new HashSet<>();
   private final Set<Integer> abandonedCells = new HashSet<>();
   private List<BuildPlanner.Breakout> doorways;
   private final Set<Integer> keptOpenCells = new HashSet<>();
   private final Set<Integer> outOfHeightCells = new HashSet<>();
   private BuildPlanner.Breakout activeDoorway;
   private BuildPlanner.Breakout returningDoorway;
   private BuildPlanner.Pillar savedPillar;
   private final List<BlockPos> towerBlocks = new ArrayList<>();
   private int towerReturnTick;
   private int towerExitPlacedCount;
   private int lastTowerDonePlaced = -1;
   private BlockPos sweepTarget;
   private int sweepTargetTick;
   private int lastSneakTick = -1000000000;
   private int walkwayAttemptMark;
   private int walkwayAttempts;
   private int towerStartPlaced;
   private int lastTowerEndPlaced;
   private Direction walkwayDirection;
   private BlockPos walkwayTarget;
   private int walkwayTick;
   private final List<List<BuildPlanner.BlockTarget>> layerGroups = new ArrayList<>();
   private int neededPillarBlocks;
   private int giveUpRetryMark;
   private AutoBuilderModule.RestockPhase restockPhase;
   private AutoBuilderModule.ContainerPos restockTarget;
   private final Set<AutoBuilderModule.ContainerPos> emptyContainers = new HashSet<>();
   private final Map<Item, Integer> restockWanted = new HashMap<>();
   private final Set<Integer> restockMovedSlots = new HashSet<>();
   private int restockTick;
   private int restockTaken;
   private int restockPutBack;
   private int restockTrips;
   private int lastQuickMoveTick = -1000000000;
   private int lowestMissingLayer = Integer.MAX_VALUE;
   private int currentLayer;
   private boolean markKeyDown;
   private String staffNearby;
   private final AuctionBuyer buyer = new AuctionBuyer("Auto Builder");
   private Item buyingItem;
   private long totalSpent;
   private int totalBought;
   private int buyStillCount;
   private boolean outOfMoney;
   private final Set<Item> unbuyableItems = new HashSet<>();
   static final long stockConfirmWindowMs = 10000L;
   static final long stockStartWindowMs = 2000L;
   private boolean stocking;
   private boolean stockingTouchedChest;
   private boolean stockingInterrupted;
   private long stockConfirmTime;
   private long stockStartTime;
   private AutoBuilderModule.StockingPhase stockingPhase;
   private final List<AutoBuilderModule.ContainerPos> chestsToRead = new ArrayList<>();
   private final Map<AutoBuilderModule.ContainerPos, Map<Item, Integer>> chestContents = new HashMap<>();
   private final Map<AutoBuilderModule.ContainerPos, Integer> chestFreeSlots = new HashMap<>();
   private final Set<AutoBuilderModule.ContainerPos> fullChests = new HashSet<>();
   private final Set<AutoBuilderModule.ContainerPos> failedChests = new HashSet<>();
   private final Set<AutoBuilderModule.ContainerPos> doubleChestHalves = new HashSet<>();
   private final Map<Item, Integer> toBuy = new LinkedHashMap<>();
   private final Map<Item, Integer> toPutAway = new LinkedHashMap<>();
   private final Map<Item, Integer> boughtTotals = new LinkedHashMap<>();
   private final Map<Item, Integer> putAwayTotals = new LinkedHashMap<>();
   private final Map<Item, Long> buyPrices = new LinkedHashMap<>();
   private int freeChestSlots;
   private final RotationController rotation = new RotationController();
   private boolean recording;
   private final List<Integer> recordedCells = new ArrayList<>();
   private final Set<Integer> recordedCellSet = new HashSet<>();
   private BitSet standingAtRecordStart;
   private int[] recordedOrder;
   private AutoBuilderModule.SchematicLayout recordedLayout;
   private String recordingError;
   private int logoffAtTick = -1000000000;
   private final ArrayDeque<Integer> ghostBlockTicks = new ArrayDeque<>();
   private int relogState = 0;
   private int rejoinAttempts;
   private int relogCount;
   private ServerData relogServer;
   private long relogStartMs;
   private long nextRejoinMs;
   private long lastRelogMs;
   private long resumeAtMs;
   private boolean relogLimitNotified;
   private boolean sawNoWorld;
   private BlockHitResult currentHit;
   private int targetStartTick;
   private int lastActionTick = -1000000000;
   private int lastSelectedSlot = -1;
   private int slotSelectedTick;
   private int lastInventoryMoveTick = -1000000000;
   private int inventoryMoveStillTick = -1000000000;
   private int placedCount;
   private int stuckCount;
   private int lastRouteTick = -1000000000;
   private int initialDoneCount;
   private int settleCountdown;
   private final BuildPlanner.SkippedStands skippedStands = new BuildPlanner.SkippedStands();
   private BlockPos routeEnd;
   private final Map<Integer, Integer> pendingPlacements = new HashMap<>();
   private List<BuildPlanner.BlockTarget> currentGroup = List.of();
   private BlockPos routeTarget;
   private long startTimeMs;
   private int initialMissingCount;
   private String lastNotice = "";
   private Vec3 moveDirection = Vec3.ZERO;
   private boolean jumping;
   private boolean sneaking;
   private boolean sprinting;
   private int lastInputTick = Integer.MIN_VALUE;
   private String hotbarError = "";
   private boolean buyingDone;
   private final Map<Item, String> buyFailureReasons = new LinkedHashMap<>();
   private static final Pattern schematicExtensionPattern = Pattern.compile("(?i)\\.(litematic|schematic|schem)$");

   static List<AutoBuilderModule.ContainerPos> getInstance(String var0) {
      ArrayList var1 = new ArrayList();
      if (var0 == null) {
         return var1;
      }

      for (String var5 : var0.split(",")) {
         String[] var6 = var5.trim().split(" +");
         if (var6.length == 4 && !var6[0].isEmpty()) {
            try {
               AutoBuilderModule.ContainerPos var7 = new AutoBuilderModule.ContainerPos(
                  var6[0], Integer.parseInt(var6[1]), Integer.parseInt(var6[2]), Integer.parseInt(var6[3])
               );
               if (!var1.contains(var7)) {
                  var1.add(var7);
               }
            } catch (NumberFormatException var8) {
            }
         }
      }

      return var1;
   }

   static String formatContainers(List<AutoBuilderModule.ContainerPos> var0) {
      ArrayList var1 = new ArrayList();

      for (AutoBuilderModule.ContainerPos var3 : var0) {
         var1.add(var3.toString());
      }

      return String.join(", ", var1);
   }

   public AutoBuilderModule() {
      super("Auto Builder", "Build a .litematic or .schem schematic block by block, walking to what is out of reach; or mine it out");
      instance = this;
      this.placeDelay.setDisplayName("Ticks Between Blocks");
      this.pauseInGui.setDisplayName("Pause In Other Menus");
      this.schematicSetting.setDisplayName("Schematic (file name)");
      this.originSetting.setDisplayName("Origin (x y z)");
      this.rotationSetting.setDisplayName("Rotation");
      this.restockContainers.setDisplayName("Restock Containers");
      this.staffRange.visibleWhen(this.stopOnStaffNearby::getValue);
      this.logOffDelay.visibleWhen(this.stopOnStaffNearby::getValue);
      this.logOffMessage.visibleWhen(this.stopOnStaffNearby::getValue);
      this.putBackSpares.visibleWhen(this.restockFromContainers::getValue);
      this.restockDistance.visibleWhen(this.restockFromContainers::getValue);
      this.maxPillarHeight.visibleWhen(this.temporaryPillars::getValue);
      this.pillarBlock.visibleWhen(this.temporaryPillars::getValue);
      this.showCorrect.visibleWhen(this.showPreview::getValue);
      this.previewRange.visibleWhen(this.showPreview::getValue);
      this.schematicSetting.onChange(var1 -> this.loadSchematic());
      this.originSetting.onChange(var1 -> this.layoutDirty = (boolean)1);
      this.rotationSetting.onChange(var1 -> this.layoutDirty = (boolean)1);
      this.mirrorSetting.onChange(var1 -> this.layoutDirty = (boolean)1);
      if (!hooksRegistered) {
         hooksRegistered = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            AutoBuilderModule var1 = instance;
            if (var1 != null) {
               var1.renderPreview();
            }
         });
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> {
            AutoBuilderModule var2 = instance;
            if (!var1 && var2 != null && var2.buyer.isRunning()) {
               AuctionBuyer.onChatMessage(var0);
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            AutoBuilderModule var1 = instance;
            if (var1 != null) {
               var1.shouldRelog();
               var1.tickRelog();
            }
         });
      }
   }

   public static AutoBuilderModule getInstance() {
      return instance;
   }

   public static Path getSchematicsDir() {
      return FabricLoader.getInstance().getGameDir().resolve("schematics");
   }

   @Override
   public void disable() {
      this.pendingStopReason = null;
      super.disable();
   }

   @Override
   public boolean allowsScreenInteractions() {
      Screen var1 = getMc() == null ? null : minecraft.gui.screen();
      return var1 == null || var1 instanceof ChatScreen || var1 instanceof PauseScreen || !this.pauseInGui.getValue();
   }

   @Override
   public List<ModulePanel.Command> commands() {
      return List.of(
         new ModulePanel.Command("Schematic", "Next schematic", () -> this.cycleSchematic(1)),
         new ModulePanel.Command("Schematic", "Previous schematic", () -> this.cycleSchematic(-1)),
         new ModulePanel.Command("Schematic", "Reload it", this::isAggressiveActive),
         new ModulePanel.Command("Schematic", "Unload schematic", this::unloadSchematic),
         new ModulePanel.Command("Schematic", "Open the schematics folder", this::renderPreview),
         new ModulePanel.Command("Where it goes", "Origin at my feet", this::loadSchematic),
         new ModulePanel.Command("Where it goes", "Origin on the block I look at", this::walkToStand),
         new ModulePanel.Command("Where it goes", "Turn it 90 degrees", this::turnSchematic),
         new ModulePanel.Command("Move it", "East (+X)", () -> this.getReach(1, 0, 0)),
         new ModulePanel.Command("Move it", "West (-X)", () -> this.getReach(-1, 0, 0)),
         new ModulePanel.Command("Move it", "Up", () -> this.getReach(0, 1, 0)),
         new ModulePanel.Command("Move it", "Down", () -> this.getReach(0, -1, 0)),
         new ModulePanel.Command("Move it", "South (+Z)", () -> this.getReach(0, 0, 1)),
         new ModulePanel.Command("Move it", "North (-Z)", () -> this.getReach(0, 0, -1)),
         new ModulePanel.Command("Restock", "Mark the container I look at", this::getFeetPos),
         new ModulePanel.Command("Restock", "Clear the restock marks", this::getSchematicsDir),
         new ModulePanel.Command("Restock", "Stock the materials", this::getRotationController),
         new ModulePanel.Command("Build Recorder", "Start recording", this::isStaffNearby),
         new ModulePanel.Command("Build Recorder", "Stop and save the recording", this::stopRecording),
         new ModulePanel.Command("Build Recorder", "Delete the recording", this::blockOf),
         new ModulePanel.Command("Build", "List the materials", this::canPlaceFrom),
         new ModulePanel.Command("Build", "Mine it out instead", this::getPlacedCount)
      );
   }

   @Override
   public String summary() {
      String var1 = this.schematicSetting.getValue().trim();
      if (var1.isEmpty()) {
         return "Put .litematic or .schem files in the schematics folder and press Next schematic.";
      }

      if (this.unloaded) {
         return "No schematic loaded. Next schematic or Reload it loads " + var1 + " or another one again.";
      }

      if (this.loading) {
         return "Loading " + var1 + "...";
      }

      if (this.loadError != null) {
         return var1 + ": " + this.loadError + ".";
      }

      if (this.schematic == null) {
         return "Loading " + var1 + "...";
      }

      String var2 = this.schematic.getName()
         + ": "
         + this.schematic.getRegions()
         + " x "
         + this.schematic.sizeY()
         + " x "
         + this.schematic.sizeZ()
         + ", "
         + this.schematic.blockCount()
         + " blocks";
      Schematic.Placement var3 = this.getReach();
      if (var3 == null) {
         return var2 + ". Set the origin to place it.";
      }

      String var4 = " at " + var3.origin() + this.describeTransform();
      if (this.running && this.isRuntimeActive()) {
         return var2 + var4 + ". " + this.followPath() + "." + this.describeRecording();
      }

      AutoBuilderModule.SchematicLayout var5 = this.layout;
      return var5 != null && var5.scanComplete && this.isInGame()
         ? var2
            + var4
            + ". "
            + var5.stateCount(BlockPlacementUtil.PlacementFailure.PLACEABLE)
            + " missing, "
            + (var5.stateCount(BlockPlacementUtil.PlacementFailure.BLOCKED) + var5.stateCount(BlockPlacementUtil.PlacementFailure.STATE_MISMATCH))
            + " wrong, "
            + var5.stateCount(BlockPlacementUtil.PlacementFailure.SECOND_HALF)
            + " done"
            + (this.lastRunSummary != null ? ". " + this.lastRunSummary : "")
            + "."
            + this.describeRecording()
         : var2 + var4 + "." + this.describeRecording();
   }

   private String describeTransform() {
      String var1 = this.rotationSetting.getValue().equals("0") ? "" : ", turned " + this.rotationSetting.getValue();
      return this.mirrorSetting.getValue().equals("None") ? var1 : var1 + ", mirrored " + this.mirrorSetting.getValue();
   }

   @Override
   public String detail() {
      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 != null && var1.scanComplete && this.isInGame()) {
         Map var2 = this.getMaterialNeeds(var1);
         if (var2.isEmpty()) {
            return var1.stateCount(BlockPlacementUtil.PlacementFailure.PLACEABLE) > 0
               ? "Missing blocks cannot be placed yet; List the materials says why."
               : "";
         }

         StringBuilder var3 = new StringBuilder("Needs: ");
         int var4 = 0;

         for (Entry var6 : var2.entrySet()) {
            if (var4 == 3) {
               var3.append(", ").append(var2.size() - 3).append(" more");
               break;
            }

            if (var4++ > 0) {
               var3.append(", ");
            }

            int var7 = this.countItem((Item)var6.getKey());
            var3.append(var6.getValue())
               .append(' ')
               .append(shouldRelog((Item)var6.getKey()))
               .append(var7 >= var6.getValue() ? "" : " (" + (var7 == 0 ? "none" : var7) + " on you)");
         }

         return var3.append('.').toString();
      } else {
         return "";
      }
   }

   @Override
   public List<ModulePanel.SettingGroup> settingGroups() {
      BooleanSupplier var1 = () -> this.getReach() != null;
      return List.of(
         new ModulePanel.SettingGroup("Schematic", List.of(this.schematicFileSetting))
            .with(
               new ModulePanel.Setting(
                  this::getStateCounts,
                  List.of(
                     List.of(
                        this.findAction("Reload", "Reload it").availableWhen(() -> !this.schematicSetting.getValue().isBlank()),
                        this.findAction("Unload", "Unload schematic"),
                        this.findAction("Open folder", "Open the schematics folder")
                     )
                  ),
                  this::preview,
                  true
               )
            ),
         new ModulePanel.SettingGroup("Placement", List.of(this.rotationSetting, this.mirrorSetting))
            .with(
               new ModulePanel.Setting(
                  this::setAside,
                  List.of(
                     List.of(this.findAction("At my feet", "Origin at my feet"), this.findAction("Block I look at", "Origin on the block I look at")),
                     List.of(
                        this.findAction("West", "West (-X)").availableWhen(var1),
                        this.findAction("East", "East (+X)").availableWhen(var1),
                        this.findAction("North", "North (-Z)").availableWhen(var1),
                        this.findAction("South", "South (+Z)").availableWhen(var1)
                     ),
                     List.of(this.findAction("Up", "Up").availableWhen(var1), this.findAction("Down", "Down").availableWhen(var1))
                  )
               )
            ),
         new ModulePanel.SettingGroup("Blocks", List.of(this.refillHotbar))
            .with(new ModulePanel.Setting(this::findPlaceHit, List.of(List.of(this.findAction("List in chat", "List the materials"))))),
         new ModulePanel.SettingGroup(
            "While building",
            List.of(this.placementMode, this.followRecording, this.autoMove, this.sprint, this.safeSneak, this.autoEat, this.temporaryPillars)
         ),
         new ModulePanel.SettingGroup("Material chests", List.of(this.restockFromContainers, this.putBackSpares, this.markRestockKey))
            .with(
               new ModulePanel.Setting(
                  this::logLeftovers,
                  List.of(
                     List.of(this.findAction("Select chest", "Mark the container I look at"), this.findAction("Clear chests", "Clear the restock marks")),
                     List.of(this.findAction("Stock materials", "Stock the materials"))
                  )
               )
            ),
         new ModulePanel.SettingGroup("Buying on DonutSMP", List.of(this.buyMissingBlocks, this.maxPricePerItem, this.maxPricePerBuy, this.maxTotalSpend)),
         new ModulePanel.SettingGroup("Safety", List.of(this.stopOnStaffNearby, this.staffRange, this.relogOnGhostBlocks)),
         new ModulePanel.SettingGroup(
            "Display", List.of(this.showPreview, this.showCorrect, this.showPath, this.statusCard, this.notifyWhenDone, this.showRestockContainers)
         ),
         ModulePanel.SettingGroup.advanced(
               "Advanced",
               List.of(
                  this.schematicSetting,
                  this.originSetting,
                  this.reach,
                  this.placeDelay,
                  this.previewRange,
                  this.pauseInGui,
                  this.maxPillarHeight,
                  this.pillarBlock,
                  this.restockContainers,
                  this.restockDistance,
                  this.logOffDelay,
                  this.logOffMessage
               )
            )
            .with(
               new ModulePanel.Setting(
                  () -> "",
                  List.of(
                     List.of(
                        this.findAction("Record build", "Start recording"),
                        this.findAction("Save recording", "Stop and save the recording"),
                        this.findAction("Delete recording", "Delete the recording"),
                        this.findAction("Mine it out", "Mine it out instead")
                     )
                  )
               )
            )
      );
   }

   private String getStateCounts() {
      String var1 = this.schematicSetting.getValue().trim();
      if (this.schematicFileSetting.getModes().size() <= 1 && var1.isEmpty()) {
         return "The schematics folder is empty.";
      }

      if (!var1.isEmpty() && !this.unloaded) {
         if (!this.loading && (this.schematic != null || this.loadError != null)) {
            return this.loadError != null
               ? "Could not load " + var1 + ": " + this.loadError + "."
               : this.schematic.getRegions()
                  + " x "
                  + this.schematic.sizeY()
                  + " x "
                  + this.schematic.sizeZ()
                  + ", "
                  + String.format(Locale.ROOT, "%,d", this.schematic.blockCount())
                  + " blocks";
         } else {
            return "Loading.";
         }
      } else {
         return "";
      }
   }

   private String setAside() {
      Schematic.Placement var1 = this.getReach();
      return var1 == null ? "Not set." : "Origin at " + var1.origin() + this.describeTransform();
   }

   private int getStateCounts(AutoBuilderModule.SchematicLayout var1) {
      int var2 = 0;

      for (Entry var4 : this.getMaterialNeeds(var1).entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.countItem((Item)var4.getKey()));
      }

      return var2;
   }

   private String findPlaceHit() {
      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 != null && this.isInGame() && var1.scanComplete) {
         String var2 = this.detail();
         return var2.isEmpty() ? "Nothing more is needed." : var2;
      } else {
         return "";
      }
   }

   @Override
   public void panelTick() {
      if (this.fileListRefreshTicks-- <= 0) {
         this.fileListRefreshTicks = 40;
         ArrayList var1 = new ArrayList();
         var1.add("No schematic");
         var1.addAll(Schematic.listSchematicFiles(getSchematicsDir()));
         this.schematicFileSetting.setModes(var1);
      }
   }

   @Override
   public ModulePanel.Status status() {
      if (this.running && this.isRuntimeActive()) {
         return new ModulePanel.Status(this.followPath() + ".", false);
      }

      String var1 = this.schematicSetting.getValue().trim();
      if (var1.isEmpty() || this.unloaded) {
         return new ModulePanel.Status("Choose a schematic, then set where it goes.", false);
      }

      if (this.loadError != null) {
         return new ModulePanel.Status("Could not load " + var1 + ": " + this.loadError + ".", true);
      }

      if (this.loading || this.schematic == null) {
         return new ModulePanel.Status("Loading " + var1 + ".", false);
      }

      if (this.getReach() == null) {
         return new ModulePanel.Status("Set where it goes, then turn the module on.", false);
      }

      if (!this.isInGame()) {
         return new ModulePanel.Status("Schematic set. Join the world it goes in.", false);
      }

      AutoBuilderModule.SchematicLayout var2 = this.layout;
      if (var2 != null && var2.scanComplete) {
         String var3 = ModulePanel.findConflictingModule(this);
         if (var3 != null) {
            return new ModulePanel.Status(var3 + " is running. One bot runs at a time.", true);
         }

         if (this.recording) {
            return new ModulePanel.Status("Build Recorder is recording. Stop and save it first.", true);
         }

         BlockPos var4 = minecraft.player.blockPosition();
         int var5 = Math.max(Math.max(var2.minX - var4.getX(), var4.getX() - var2.maxX), Math.max(var2.minZ - var4.getZ(), var4.getZ() - var2.maxZ));
         if (var5 > 32.0) {
            return new ModulePanel.Status("You are " + var5 + " blocks from the build. Walk next to it.", true);
         }

         int var6 = var2.stateCount(BlockPlacementUtil.PlacementFailure.PLACEABLE);
         int var7 = var2.stateCount(BlockPlacementUtil.PlacementFailure.BLOCKED) + var2.stateCount(BlockPlacementUtil.PlacementFailure.STATE_MISMATCH);
         if (var6 == 0 && var7 == 0) {
            return new ModulePanel.Status("Every block already matches the schematic.", false);
         }

         String var8 = String.format(Locale.ROOT, "Will place %,d blocks", var6) + (var7 > 0 ? ", " + var7 + " wrong ones are left alone" : "") + ".";
         int var9 = this.getStateCounts(var2);
         if (var9 == 0) {
            return new ModulePanel.Status(var8, false);
         }

         int var10 = 0;

         for (AutoBuilderModule.ContainerPos var12 : getInstance(this.restockContainers.getValue())) {
            if (var12.dimension().equals(this.setStatus())) {
               var10++;
            }
         }

         int var13 = (!this.restockFromContainers.getValue() || var10 <= 0) && !this.buyMissingBlocks.getValue() ? 0 : 1;
         return new ModulePanel.Status(var8 + String.format(Locale.ROOT, " You are short of %,d.", var9), var13 == 0);
      } else {
         return new ModulePanel.Status("Checking the world.", false);
      }
   }

   private String cycleSchematic(int var1) {
      List var2 = Schematic.listSchematicFiles(getSchematicsDir());
      if (var2.isEmpty()) {
         return "No schematics in " + getSchematicsDir() + ". Put .litematic or .schem files there.";
      }

      int var3 = var2.indexOf(this.schematicSetting.getValue().trim());
      int var4 = var3 < 0 ? (var1 > 0 ? 0 : var2.size() - 1) : Math.floorMod(var3 + var1, var2.size());
      if (this.running) {
         return "Turn the builder off before picking another schematic.";
      }

      this.setAside((String)var2.get(var4));
      return "Picked " + (String)var2.get(var4) + " (" + (var4 + 1) + " of " + var2.size() + ").";
   }

   private void setAside(String var1) {
      boolean var2 = var1.equals(this.schematicSetting.getValue().trim());
      this.schematicSetting.setText(var1);
      if (var2 && this.unloaded) {
         this.loadSchematic();
      }

      this.getStateCounts();
   }

   private void getSchematic(String var1) {
      if ("No schematic".equals(var1)) {
         this.unloadSchematic();
      } else if (this.running) {
         this.sendChat("turn the builder off before picking another schematic.");
      } else {
         this.setAside(var1);
      }
   }

   private String isAggressiveActive() {
      if (this.schematicSetting.getValue().isBlank()) {
         return "Pick a schematic first.";
      }

      if (this.running) {
         return "Turn the builder off before reloading.";
      }

      this.loadSchematic();
      return "Reloading " + this.schematicSetting.getValue().trim() + ".";
   }

   private String unloadSchematic() {
      if (this.running) {
         this.sendChat("turn the builder off before unloading the schematic.");
         return "Turn the builder off before unloading the schematic.";
      }

      boolean var1 = AutoAreaMiner.countDrillBlocks("the schematic was unloaded");
      if (this.unloaded || this.schematic == null && !this.loading && this.loadError == null) {
         if (!var1) {
            this.sendChat("no schematic is loaded.");
         }

         return var1 ? "Stopped Mine Out; no schematic was loaded." : "No schematic is loaded.";
      } else {
         String var2 = this.schematic != null ? this.schematic.getName() : this.schematicSetting.getValue().trim();
         if (this.recording) {
            this.getSchematicsDir("the schematic was unloaded");
         }

         this.loadGeneration++;
         this.pendingLoad = null;
         this.loading = (boolean)0;
         this.schematic = null;
         this.loadError = null;
         this.layout = null;
         this.layoutDirty = (boolean)0;
         this.recordedOrder = null;
         this.recordedLayout = null;
         this.lastRunSummary = null;
         this.unloaded = (boolean)1;
         LarpLog.info(this.getName(), "Unloaded {}{}; settings kept", var2, var1 ? " after stopping its Mine Out" : "");
         this.sendChat(
            "unloaded "
               + var2
               + (var1 ? " and stopped mining it out" : "")
               + ". Your settings are kept; pick one in the list or press Reload to load one again."
         );
         return "Unloaded " + var2 + (var1 ? ", Mine Out stopped." : ".");
      }
   }

   private String renderPreview() {
      Path var1 = getSchematicsDir();

      try {
         Files.createDirectories(var1);
      } catch (IOException var3) {
         return "Could not create " + var1 + ": " + var3.getMessage();
      }

      Blaze3D.openPath(var1);
      return "Opened " + var1 + ". Put .litematic or .schem files there, then choose one in the list.";
   }

   private String loadSchematic() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.running) {
         return "Turn the builder off before moving the schematic.";
      }

      BlockPos var1 = minecraft.player.blockPosition();
      return this.progressPercent(var1.getX(), var1.getY(), var1.getZ());
   }

   private String walkToStand() {
      if (!this.isInGame()) {
         return "Join a world first.";
      } else if (this.running) {
         return "Turn the builder off before moving the schematic.";
      } else if (minecraft.hitResult instanceof BlockHitResult var1 && var1.getType() == Type.BLOCK) {
         BlockPos var3 = var1.getBlockPos().relative(var1.getDirection());
         return this.progressPercent(var3.getX(), var3.getY(), var3.getZ());
      } else {
         return "Look at a block first.";
      }
   }

   private String progressPercent(int var1, int var2, int var3) {
      this.originSetting.setText(LayerScanPlan.formatCoords(var1, var2, var3));
      this.getStateCounts();
      return "Origin set to " + var1 + " " + var2 + " " + var3 + ".";
   }

   private String turnSchematic() {
      if (this.running) {
         return "Turn the builder off before turning the schematic.";
      }

      this.rotationSetting.cycleNext();
      this.getStateCounts();
      return "Turned to " + this.rotationSetting.getValue() + " degrees.";
   }

   private String getReach(int var1, int var2, int var3) {
      if (this.running) {
         return "Turn the builder off before moving the schematic.";
      }

      int[] var4 = LayerScanPlan.parseCoords(this.originSetting.getValue());
      return var4 == null ? "Set the origin first." : this.progressPercent(var4[0] + var1, var4[1] + var2, var4[2] + var3);
   }

   private String canPlaceFrom() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 == null) {
         return this.schematic == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (!var1.scanComplete) {
         return "Still checking the world; try again in a moment.";
      }

      Map var2 = this.getMaterialNeeds(var1);
      this.sendChat(
         "materials for "
            + this.schematic.getName()
            + " ("
            + var1.stateCount(BlockPlacementUtil.PlacementFailure.PLACEABLE)
            + " missing, "
            + var1.stateCount(BlockPlacementUtil.PlacementFailure.BLOCKED)
            + " wrong blocks, "
            + var1.stateCount(BlockPlacementUtil.PlacementFailure.STATE_MISMATCH)
            + " turned wrong):"
      );

      for (Entry var4 : var2.entrySet()) {
         int var5 = this.countItem((Item)var4.getKey());
         this.sendChat(
            "  "
               + var4.getValue()
               + " "
               + shouldRelog((Item)var4.getKey())
               + ", you carry "
               + var5
               + (var5 >= var4.getValue() ? "" : ", short by " + ((Integer)var4.getValue() - var5))
         );
      }

      TreeMap var6 = new TreeMap();

      for (int var7 = 0; var7 < var1.blockCount; var7++) {
         String var9 = var1.unplaceableReasons[var1.paletteIndices[var7]];
         if (var9 != null && var1.cellState(var7) != BlockPlacementUtil.PlacementFailure.SECOND_HALF) {
            var6.merge(
               Schematic.maxX(this.schematic.getPalette().get(var1.paletteIndices[var7])).replace("minecraft:", "") + " (" + var9 + ")", 1, Integer::sum
            );
         }
      }

      for (Entry var10 : var6.entrySet()) {
         this.sendChat("  left out: " + var10.getValue() + " " + (String)var10.getKey());
      }

      return var2.isEmpty() && var6.isEmpty() ? "Nothing is missing." : "Listed " + (var2.size() + var6.size()) + " materials in chat.";
   }

   private String getPlacedCount() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (!this.running && !this.isRuntimeActive()) {
         AutoBuilderModule.SchematicLayout var1 = this.layout;
         if (var1 == null) {
            return "Load a schematic and set its origin first.";
         }

         ArrayList var2 = new ArrayList(var1.blockCount);

         for (int var3 = 0; var3 < var1.blockCount; var3++) {
            var2.add(var1.posAt(var3));
         }

         String var4 = AutoAreaMiner.setTarget(var2, "the " + this.schematic.getName() + " schematic");
         if (var4 != null) {
            return var4;
         }

         LarpLog.info(
            this.getName(), "Mine Out: handed {} cells of {} at {} to Auto Area Miner", var2.size(), this.schematic.getName(), var1.transform.origin()
         );
         return "Auto Area Miner is mining out " + this.schematic.getName() + ".";
      } else {
         return "Turn the builder off first; one bot runs at a time.";
      }
   }

   private Map<Item, Integer> getMaterialNeeds(AutoBuilderModule.SchematicLayout var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.blockCount; var3++) {
         if (var1.cellState(var3) == BlockPlacementUtil.PlacementFailure.PLACEABLE
            && var1.unplaceableReasons[var1.paletteIndices[var3]] == null
            && !var1.isSecondHalf(var3)) {
            BlockState var4 = var1.stateAt(var3);
            BlockItem var5 = BlockPlacementUtil.getPlacingItem(var4);
            int var6 = BlockPlacementUtil.isStackedState(var4) && this.isInGame()
               ? BlockPlacementUtil.countPlacementsNeeded(var4, minecraft.level.getBlockState(var1.posAt(var3)))
               : 1;
            if (var5 != null && var6 > 0) {
               var2.merge(var5, var6, Integer::sum);
            }
         }
      }

      ArrayList var7 = new ArrayList(var2.entrySet());
      var7.sort((var0, var1x) -> (Integer)var1x.getValue() - (Integer)var0.getValue());
      LinkedHashMap var8 = new LinkedHashMap();

      for (Entry var10 : var7) {
         var8.put((Item)var10.getKey(), (Integer)var10.getValue());
      }

      return var8;
   }

   private static String shouldRelog(Item var0) {
      return new ItemStack(var0).getHoverName().getString();
   }

   private int countItem(Item var1) {
      if (!this.isInGame()) {
         return 0;
      }

      Inventory var2 = minecraft.player.getInventory();
      int var3 = 0;

      for (int var4 = 0; var4 < 36; var4++) {
         if (var2.getItem(var4).is(var1)) {
            var3 += var2.getItem(var4).getCount();
         }
      }

      if (minecraft.player.getOffhandItem().is(var1)) {
         var3 += minecraft.player.getOffhandItem().getCount();
      }

      return var3;
   }

   private void getStateCounts() {
      LarpClient var1 = LarpClient.getInstance();
      if (var1 != null && var1.getConfigManager() != null) {
         var1.getConfigManager().saveActiveProfile();
      }
   }

   private void loadSchematic() {
      this.unloaded = (boolean)0;
      int var1 = ++this.loadGeneration;
      String var2 = this.schematicSetting.getValue().trim();
      this.schematic = null;
      this.loadError = null;
      this.layout = null;
      this.layoutDirty = (boolean)1;
      this.loadedFileName = var2;
      if (var2.isEmpty()) {
         this.loading = (boolean)0;
      } else if (!var2.contains("/") && !var2.contains("\\") && !var2.contains("..")) {
         this.loading = (boolean)1;
         Path var3 = getSchematicsDir().resolve(var2);
         Thread var4 = new Thread(() -> {
            AutoBuilderModule.LoadResult var4x;
            try {
               if (!Files.isRegularFile(var3)) {
                  var4x = new AutoBuilderModule.LoadResult(var2, null, "no such file in the schematics folder");
               } else {
                  var4x = new AutoBuilderModule.LoadResult(var2, Schematic.loadFile(var3), null);
               }
            } catch (IOException | RuntimeException var6) {
               var4x = new AutoBuilderModule.LoadResult(var2, null, var6.getMessage() == null ? var6.getClass().getSimpleName() : var6.getMessage());
            }

            if (var1 == this.loadGeneration) {
               this.pendingLoad = var4x;
            }
         }, "Larp-SchematicLoad");
         var4.setDaemon(true);
         var4.start();
      } else {
         this.loadError = "only a file name inside the schematics folder works";
      }
   }

   private void shouldRelog() {
      this.getInstance();
      if (this.unloaded) {
         if (this.recording) {
            this.getSchematicsDir("the schematic was unloaded");
         }
      } else {
         AutoBuilderModule.LoadResult var1 = this.pendingLoad;
         if (var1 != null) {
            this.pendingLoad = null;
            this.loading = (boolean)0;
            if (var1.file().equals(this.loadedFileName)) {
               this.schematic = var1.schematic();
               this.loadError = var1.error();
               this.layoutDirty = (boolean)1;
               if (this.schematic != null) {
                  LarpLog.info(
                     this.getName(),
                     "Loaded {} ({}): {}, {} regions, {} palette entries",
                     var1.file(),
                     this.schematic.getFormat(),
                     this.schematic.describe(),
                     this.schematic.getRegions().size(),
                     this.schematic.getPalette().size()
                  );
               } else {
                  LarpLog.info(this.getName(), "Could not load {}: {}", var1.file(), this.loadError);
               }
            }
         }

         if (this.schematic == null && !this.loading && this.loadError == null && !this.schematicSetting.getValue().isBlank()) {
            this.loadSchematic();
         } else {
            if (this.layoutDirty) {
               this.layoutDirty = (boolean)0;
               Schematic.Placement var2 = this.getReach();
               this.layout = this.schematic != null && var2 != null ? new AutoBuilderModule.SchematicLayout(this.schematic, var2) : null;
               if (this.layout != null) {
                  LarpLog.debug(
                     this.getName(),
                     "Layout at {}{}: box {} to {}",
                     var2.origin(),
                     this.describeTransform(),
                     this.layout.minX + " " + this.layout.minY + " " + this.layout.minZ,
                     this.layout.maxX + " " + this.layout.maxY + " " + this.layout.maxZ
                  );
               }
            }

            AutoBuilderModule.SchematicLayout var3 = this.layout;
            if (var3 != this.recordedLayout) {
               if (this.recording) {
                  this.getSchematicsDir("the schematic or its origin changed");
               }

               this.getSchematic(var3);
            }

            if (var3 != null && this.isInGame()) {
               this.clearTarget(var3);
               if (var3.scanLevel != minecraft.level) {
                  var3.scanLevel = minecraft.level;
                  var3.scanCursor = 0;
                  var3.scanComplete = (boolean)0;
                  Arrays.fill(var3.pendingStateCounts, 0);
                  var3.nearestQueue.clear();
               }

               this.stop(var3, 4096);
            } else {
               if (this.recording) {
                  this.getSchematicsDir("the schematic or the world changed");
               }
            }
         }
      }
   }

   private Schematic.Placement getReach() {
      int[] var1 = LayerScanPlan.parseCoords(this.originSetting.getValue());
      if (var1 == null) {
         return null;
      }

      int var2;
      try {
         var2 = Integer.parseInt(this.rotationSetting.getValue());
      } catch (NumberFormatException var6) {
         var2 = 0;
      }
      Schematic.Mirror var3 = switch ((String)this.mirrorSetting.getValue()) {
         case "X" -> Schematic.Mirror.LEFT_RIGHT;
         case "Z" -> Schematic.Mirror.FRONT_BACK;
         default -> Schematic.Mirror.NONE;
      };
      return new Schematic.Placement(var1[0], var1[1], var1[2], var2, var3);
   }

   private void stop(AutoBuilderModule.SchematicLayout var1, int var2) {
      if (var1.blockCount != 0) {
         MutableBlockPos var3 = new MutableBlockPos();
         double var4 = this.previewRange.getValue();
         double var6 = minecraft.player.getX();
         double var8 = minecraft.player.getY();
         double var10 = minecraft.player.getZ();

         for (int var12 = 0; var12 < var2; var12++) {
            int var13 = var1.scanCursor;
            BlockPlacementUtil.PlacementFailure var14 = this.getCellState(var1, var13, var3);
            var1.pendingStateCounts[var14.ordinal()]++;
            if (var14 != BlockPlacementUtil.PlacementFailure.UNKNOWN_STATE && var1.stateAt(var13) != null) {
               double var15 = var3.getX() + 0.5 - var6;
               double var17 = var3.getY() + 0.5 - var8;
               double var19 = var3.getZ() + 0.5 - var10;
               double var21 = var15 * var15 + var17 * var17 + var19 * var19;
               if (var21 <= var4 * var4 && (var14 != BlockPlacementUtil.PlacementFailure.SECOND_HALF || this.showCorrect.getValue())) {
                  var1.nearestQueue.add(new long[]{(long)var21, var13});
                  if (var1.nearestQueue.size() > 2048) {
                     var1.nearestQueue.poll();
                  }
               }
            }

            if (++var1.scanCursor >= var1.blockCount) {
               var1.scanCursor = 0;
               System.arraycopy(var1.pendingStateCounts, 0, var1.stateCounts, 0, var1.stateCounts.length);
               Arrays.fill(var1.pendingStateCounts, 0);
               var1.scanComplete = (boolean)1;
               int[] var23 = new int[var1.nearestQueue.size()];
               int var16 = 0;

               for (long[] var18 : var1.nearestQueue) {
                  var23[var16++] = (int)var18[1];
               }

               var1.previewIndices = var23;
               var1.nearestQueue.clear();
               if (var1.blockCount < var2) {
                  break;
               }
            }
         }
      }
   }

   private BlockPlacementUtil.PlacementFailure getCellState(AutoBuilderModule.SchematicLayout var1, int var2, MutableBlockPos var3) {
      var3.set(var1.packedPositions[var2]);
      BlockState var4 = var1.stateAt(var2);
      BlockPlacementUtil.PlacementFailure var5;
      if (!minecraft.level.hasChunkAt(var3)) {
         var5 = BlockPlacementUtil.PlacementFailure.UNKNOWN_STATE;
      } else if (var4 == null) {
         var5 = BlockPlacementUtil.PlacementFailure.SECOND_HALF;
      } else {
         var5 = BlockPlacementUtil.getPlacementFailureReason(var4, minecraft.level.getBlockState(var3));
      }

      var1.cellStates[var2] = (byte)var5.ordinal();
      return var5;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.reset();
      this.placedCount = 0;
      this.lastRunSummary = null;
      if (this.pendingStopReason != null) {
         String var4 = this.pendingStopReason;
         this.pendingStopReason = null;
         if (this.stockingInterrupted) {
            this.stockingInterrupted = (boolean)0;
            this.stockStartTime = 0L;
            this.stop("stocking stopped earlier because " + var4 + ". Press Stock materials to go on");
         } else {
            this.stop("it stopped earlier because " + var4 + ". Turn it on again to carry on");
         }
      } else if (this.relogState != 3 && this.relogState != 2) {
         this.relogCount = 0;
         this.relogLimitNotified = (boolean)0;
         int var3 = System.currentTimeMillis() - this.stockStartTime < 2000L ? 1 : 0;
         this.stockStartTime = 0L;
         String var5 = ModulePanel.findConflictingModule(this);
         if (var5 != null) {
            this.stop("turn off " + var5 + " first; one bot runs at a time");
         } else if (var3 != 0) {
            this.startStocking();
         } else {
            this.getSchematicsDir();
         }
      } else {
         ServerData var1 = minecraft.getCurrentServer();
         int var2 = var1 != null && this.relogServer != null && var1.ip.equalsIgnoreCase(this.relogServer.ip) ? 1 : 0;
         this.relogState = 0;
         this.relogServer = null;
         if (var2 == 0) {
            this.stop("it logged off to resync blocks and you joined another server. Turn it on again to carry on");
         } else {
            this.resumeAtMs = System.currentTimeMillis() + 3000L;
            LarpLog.info(this.getName(), "Rejoined after the relog; carrying on in {} ms", 3000L);
            this.setStatus("Rejoined; waiting for the world to load");
         }
      }
   }

   private void getSchematicsDir() {
      if (this.recording) {
         this.stop("the Build Recorder is recording; stop and save it first");
      } else if (AutoAreaMiner.stopWithReason()) {
         this.stop("Auto Area Miner is mining a schematic out; turn it off first");
      } else if (this.unloaded) {
         this.stop("the schematic is unloaded; pick it in the list or press Reload first");
      } else if (this.schematic == null) {
         this.stop(this.loadError != null ? "the schematic did not load: " + this.loadError : "pick a schematic first");
      } else {
         if (this.layoutDirty) {
            this.shouldRelog();
         }

         AutoBuilderModule.SchematicLayout var1 = this.layout;
         if (var1 == null) {
            this.stop("set the origin first");
         } else {
            BlockPos var2 = minecraft.player.blockPosition();
            double var3 = Math.max(Math.max(var1.minX - var2.getX(), var2.getX() - var1.maxX), Math.max(var1.minZ - var2.getZ(), var2.getZ() - var1.maxZ));
            if (var3 > 32.0) {
               this.stop("stand next to the build to start (you are " + (int)var3 + " blocks away)");
            } else {
               if (!var1.scanComplete || var1.blockCount <= 200000) {
                  this.stop(var1, var1.blockCount);
               }

               this.running = (boolean)1;
               this.startLevel = minecraft.level;
               this.startPlayer = minecraft.player;
               this.playerActions.start();
               this.startTimeMs = System.currentTimeMillis();
               this.initialMissingCount = var1.stateCount(BlockPlacementUtil.PlacementFailure.PLACEABLE);
               this.initialDoneCount = var1.stateCount(BlockPlacementUtil.PlacementFailure.SECOND_HALF);
               this.sendChat(
                  "building "
                     + this.schematic.getName()
                     + " at "
                     + var1.transform.origin()
                     + this.describeTransform()
                     + ": "
                     + this.initialMissingCount
                     + " blocks to place, "
                     + var1.stateCount(BlockPlacementUtil.PlacementFailure.SECOND_HALF)
                     + " already done."
               );
               ArrayList var5 = new ArrayList();
               Map var6 = this.getMaterialNeeds(var1);
               ArrayList var7 = new ArrayList();

               for (Entry var9 : var6.entrySet()) {
                  int var10 = this.countItem((Item)var9.getKey());
                  if (var10 < (Integer)var9.getValue()) {
                     var7.add((Integer)var9.getValue() - var10 + " " + shouldRelog((Item)var9.getKey()));
                  }
               }

               int var12 = 0;

               for (AutoBuilderModule.ContainerPos var15 : getInstance(this.restockContainers.getValue())) {
                  if (var15.dimension().equals(this.setStatus())) {
                     var12++;
                  }
               }

               if (!var7.isEmpty()) {
                  var5.add(
                     "you are short of "
                        + String.join(", ", var7.subList(0, Math.min(4, var7.size())))
                        + (var7.size() > 4 ? " and " + (var7.size() - 4) + " more" : "")
                        + (
                           this.restockFromContainers.getValue() && var12 > 0
                              ? " (it restocks from " + var12 + " marked " + (var12 == 1 ? "container)" : "containers)")
                              : ""
                        )
                  );
               }

               int var14 = var1.stateCount(BlockPlacementUtil.PlacementFailure.BLOCKED) + var1.stateCount(BlockPlacementUtil.PlacementFailure.STATE_MISMATCH);
               int var16 = 0;

               for (int var11 = 0; var11 < var1.blockCount; var11++) {
                  if (var1.cellState(var11) == BlockPlacementUtil.PlacementFailure.STATE_MISMATCH
                     && BlockPlacementUtil.countInteractionsNeeded(var1.stateAt(var11), minecraft.level.getBlockState(var1.posAt(var11))) > 0) {
                     var16++;
                  }
               }

               if (var14 > var16) {
                  var5.add(var14 - var16 + " cells hold another block or face the wrong way; they are left as they are");
               }

               if (var16 > 0) {
                  this.sendChat(var16 + " blocks are set by right clicks (repeater delays, comparator modes, notes, open doors or levers).");
               }

               if (this.autoEat.getValue() && AutoSustain.findHotbarSlot(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                  var5.add("no food in the hotbar");
               }

               if (!var5.isEmpty()) {
                  this.sendChat("heads up: " + String.join("; ", var5) + ".");
               }

               LarpLog.info(
                  this.getName(),
                  "Started: {} at {}{}, {} to place, {} done, {} wrong, box {} {} {} to {} {} {}",
                  this.schematic.getName(),
                  var1.transform.origin(),
                  this.describeTransform(),
                  this.initialMissingCount,
                  var1.stateCount(BlockPlacementUtil.PlacementFailure.SECOND_HALF),
                  var14,
                  var1.minX,
                  var1.minY,
                  var1.minZ,
                  var1.maxX,
                  var1.maxY,
                  var1.maxZ
               );
               LarpLog.info(this.getName(), "Player at {} when the run started", this.getFeetPos().toShortString());
               if (this.initialMissingCount == 0) {
                  this.finishBuild();
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.running && this.relogState != 0) {
         LarpLog.info(this.getName(), "Logged off for the relog after {} blocks", this.placedCount);
      } else if (this.isEnabled() && this.running) {
         this.pendingStopReason = "the world, the player or the connection changed";
         this.stockingInterrupted = this.stocking;
         LarpLog.info(this.getName(), "Interrupted after {} blocks: {}", this.placedCount, this.pendingStopReason);
      }

      super.onDisable();
      NativePlacement.cancel(this);
      BlockBreaker.release(this);
      if (!this.pillarBlocks.isEmpty()) {
         BlockPos var1 = this.pillarBlocks.getFirst();
         this.sendChat(
            "left "
               + this.pillarBlocks.size()
               + " temporary "
               + (this.pillarBlocks.size() == 1 ? "block" : "blocks")
               + " in the pillar at "
               + var1.toShortString()
               + "."
         );
         LarpLog.info(this.getName(), "Stopped with {} temporary blocks standing from {}", this.pillarBlocks.size(), var1.toShortString());
      }

      if (!this.towerBlocks.isEmpty()) {
         this.sendChat(
            "left "
               + this.towerBlocks.size()
               + " temporary "
               + (this.towerBlocks.size() == 1 ? "block" : "blocks")
               + " in the tower at "
               + this.towerBlocks.getFirst().toShortString()
               + "."
         );
         LarpLog.info(
            this.getName(),
            "Stopped with {} tower blocks standing beside a doorway from {}",
            this.towerBlocks.size(),
            this.towerBlocks.getFirst().toShortString()
         );
      }

      if (!this.sweepBlocks.isEmpty()) {
         this.sendChat(
            "left "
               + this.sweepBlocks.size()
               + " temporary "
               + (this.sweepBlocks.size() == 1 ? "block" : "blocks")
               + " starting at "
               + this.sweepBlocks.getFirst().toShortString()
               + "."
         );
         LarpLog.info(
            this.getName(), "Stopped with {} temporary blocks not swept up, from {}", this.sweepBlocks.size(), this.sweepBlocks.getFirst().toShortString()
         );
      }

      if (!this.walkwayBlocks.isEmpty()) {
         this.sendChat(
            "left "
               + this.walkwayBlocks.size()
               + " temporary walkway "
               + (this.walkwayBlocks.size() == 1 ? "block" : "blocks")
               + " at "
               + this.walkwayBlocks.getFirst().toShortString()
               + "."
         );
         LarpLog.info(this.getName(), "Stopped with {} walkway blocks from {}", this.walkwayBlocks.size(), this.walkwayBlocks.getFirst().toShortString());
      }

      if (!this.supportBlocks.isEmpty()) {
         this.sendChat(
            "left "
               + this.supportBlocks.size()
               + " temporary "
               + (this.supportBlocks.size() == 1 ? "block" : "blocks")
               + " at "
               + this.supportBlocks.getFirst().toShortString()
               + "."
         );
         LarpLog.info(
            this.getName(), "Stopped with {} temporary support blocks at {}", this.supportBlocks.size(), this.supportBlocks.getFirst().toShortString()
         );
      }

      this.playerActions.reset();
      this.reset();
   }

   private void reset() {
      this.running = (boolean)0;
      this.startLevel = this.startPlayer = null;
      this.setAsideUntil.clear();
      this.attemptCounts.clear();
      this.serverRemovalCounts.clear();
      this.givenUpCells.clear();
      this.giveUpReasons.clear();
      this.towerCells.clear();
      this.diagnosedCells.clear();
      this.facingRetryCounts.clear();
      this.wrongFacingPos = null;
      this.refusedBreaks.clear();
      this.standingOnTempSinceTick = -1000000000;
      this.missingItems.clear();
      this.pendingPlacements.clear();
      this.actionByCell.clear();
      this.configuredCrafters.clear();
      this.activeCrafterCell = null;
      this.containerOpenedTick = this.lastCrafterClickTick = -1000000000;
      this.rightClickCount = this.unsupportedCount = 0;
      this.expectedStates.clear();
      this.currentAction = AutoBuilderModule.CellAction.PLACE;
      this.stateBeforePlace = null;
      this.activePillar = null;
      this.towerPhase = null;
      this.pillarBlocks.clear();
      this.failedPillarColumns.clear();
      this.pillarWalkStartTick = this.pillarPhaseTick = this.towerPlacedMark = this.pillarCount = 0;
      this.walkwayBlocks.clear();
      this.walkwayDirection = null;
      this.walkwayTarget = null;
      this.walkwayTick = 0;
      this.lastYaw = Float.NaN;
      this.lastYawDelta = this.maxYawDelta = 0.0F;
      this.reversalCount = this.sprintJumpCount = this.extraPlaceDelay = this.sharpTurnLogs = 0;
      this.sprintJumpUntil = this.nextSprintJumpTick = -1000000000;
      this.stopPending = this.settling = (boolean)0;
      this.doorways = null;
      this.keptOpenCells.clear();
      this.outOfHeightCells.clear();
      this.abandonedCells.clear();
      this.sweepFromTower = (boolean)0;
      this.towerTried.clear();
      this.towerExitPlacedCount = 0;
      this.lastTowerDonePlaced = -1;
      this.activeDoorway = this.returningDoorway = null;
      this.savedPillar = null;
      this.towerBlocks.clear();
      this.towerReturnTick = 0;
      this.recentPillarColumns.clear();
      this.lastTowerEndPlaced = 0;
      this.leftoverChecked = (boolean)0;
      this.walkwayAttemptMark = this.walkwayAttempts = 0;
      this.sweepBlocks.clear();
      this.sweepTarget = null;
      this.sweepTargetTick = 0;
      this.towerStartPlaced = 0;
      this.activeSupport = null;
      this.supportPhase = null;
      this.supportBlocks.clear();
      this.skippedSupportSeeds.clear();
      this.supportWalkStartTick = this.supportActionTick = this.neededPillarBlocks = this.giveUpRetryMark = this.supportFailureMark = this.supportRetryMark = 0;
      this.supportFailures.clear();
      this.lastPillarKey = Long.MIN_VALUE;
      this.lastSupportKey = Long.MIN_VALUE;
      this.pillarForLayer = (boolean)0;
      this.layerGroups.clear();
      this.restockPhase = null;
      this.restockTarget = null;
      this.emptyContainers.clear();
      this.restockWanted.clear();
      this.restockMovedSlots.clear();
      this.restockTick = this.restockTaken = this.restockPutBack = this.restockTrips = 0;
      this.lastQuickMoveTick = -1000000000;
      this.lowestMissingLayer = Integer.MAX_VALUE;
      this.currentLayer = 0;
      this.staffNearby = null;
      this.logoffAtTick = -1000000000;
      if (this.buyer.isRunning()) {
         this.buyer.abort("the builder stopped");
      }

      this.buyingItem = null;
      this.totalSpent = 0L;
      this.totalBought = this.buyStillCount = 0;
      this.outOfMoney = (boolean)0;
      this.unbuyableItems.clear();
      this.stocking = this.stockingTouchedChest = (boolean)0;
      this.stockingPhase = null;
      this.chestsToRead.clear();
      this.chestContents.clear();
      this.chestFreeSlots.clear();
      this.fullChests.clear();
      this.failedChests.clear();
      this.doubleChestHalves.clear();
      this.toBuy.clear();
      this.toPutAway.clear();
      this.boughtTotals.clear();
      this.putAwayTotals.clear();
      this.buyPrices.clear();
      this.buyFailureReasons.clear();
      this.freeChestSlots = 0;
      this.lastPlaced = null;
      this.unconfirmedSinceTick = -1000000000;
      this.ghostBlockTicks.clear();
      this.resumeAtMs = 0L;
      this.currentTarget = null;
      this.currentHit = null;
      this.targetStartTick = 0;
      this.lastActionTick = -1000000000;
      this.lastSelectedSlot = -1;
      this.slotSelectedTick = 0;
      this.lastInventoryMoveTick = -1000000000;
      this.inventoryMoveStillTick = -1000000000;
      this.stuckCount = this.initialDoneCount = this.settleCountdown = 0;
      this.skippedStands.reset();
      this.routeEnd = null;
      this.lastRouteTick = -1000000000;
      this.currentGroup = List.of();
      this.routeTarget = null;
      this.pathFollower.reset();
      this.movement.reset();
      this.moveDirection = Vec3.ZERO;
      this.jumping = this.sneaking = this.sprinting = (boolean)0;
      this.lastInputTick = Integer.MIN_VALUE;
      this.lastNotice = "";
      this.initialMissingCount = 0;
   }

   @Override
   public void onTick() {
      byte var6 = 1;
      int var5 = this.tickCounter;
      this.tickCounter = var5 + var6;
      this.moveDirection = Vec3.ZERO;
      this.jumping = this.sneaking = this.sprinting = (boolean)0;
      if (!this.running) {
         if (this.resumeAtMs != 0L) {
            this.sendChat();
         }
      } else if (minecraft.level == this.startLevel && minecraft.player == this.startPlayer) {
         AutoBuilderModule.SchematicLayout var1 = this.layout;
         if (var1 == null) {
            this.stop("the schematic or its origin changed");
         } else {
            this.lastInputTick = minecraft.player.tickCount;
            int var10001;
            if (!minecraft.player.isShiftKeyDown()) {
               var10001 = -1000000000;
            } else {
               int var8 = -1000000000;
               int var7 = this.lastSneakTick;
               var10001 = Integer.compare(var7, var8);
               var10001 = (var10001 | -var10001) >>> 31 == 0 ? minecraft.player.tickCount : this.lastSneakTick;
            }

            this.lastSneakTick = var10001;
            this.canPlaceFrom();
            if (this.staffNearby == null && (!this.stopOnStaffNearby.getValue() || !this.isStaffNearby())) {
               String var2 = AutoSustain.getDangerReason();
               if (var2 != null) {
                  this.clearTarget();
                  this.playerActions.stopEating();
                  this.pathFollower.reset();
                  this.jumping = (boolean)1;
                  this.setStatus(var2 + ": swimming up");
               } else if (this.autoEat.getValue() && this.playerActions.tryEat(10)) {
                  this.clearTarget();
                  this.setStatus("Eating");
               } else if (minecraft.gui.screen() instanceof AbstractSignEditScreen var3 && minecraft.player.tickCount - this.lastActionTick < 40) {
                  var3.onClose();
               } else if (this.activeCrafterCell == null || !this.getMovementInput(var1)) {
                  if (this.restockPhase != AutoBuilderModule.RestockPhase.TRANSFER || (this.stocking ? !this.reset() : !this.logLeftovers(var1))) {
                     if (this.buyingItem != null) {
                        this.countItem();
                     } else if (!this.isReady()) {
                        this.holdStill();
                        this.setStatus(minecraft.gui.screen() != null ? "Paused while a screen is open" : "Waiting for world or interaction control");
                     } else if (this.isManualInput()) {
                        this.holdStill();
                        this.pathFollower.reset();
                        this.movement.reset();
                        this.setStatus("Paused for manual movement");
                     } else if (minecraft.gui.screen() instanceof AbstractContainerScreen) {
                        this.holdStill();
                        this.setStatus("Holding still while an inventory menu is open");
                     } else {
                        this.loadSchematic(var1);
                        if (this.followPath()) {
                           this.getPlacedCount();
                        } else if (this.towerPhase == AutoBuilderModule.TowerPhase.ASCEND) {
                           this.cycleSchematic(var1);
                        } else if (this.towerPhase == AutoBuilderModule.TowerPhase.DESCEND) {
                           this.stopRecording();
                        } else if (this.towerPhase == AutoBuilderModule.TowerPhase.WALKWAY_PLACE) {
                           this.isRunning();
                        } else if (this.towerPhase == AutoBuilderModule.TowerPhase.WALKWAY_STEP) {
                           this.stop();
                        } else if (this.towerPhase == AutoBuilderModule.TowerPhase.WALKWAY_BREAK) {
                           this.isManualInput();
                        } else if (!minecraft.player.onGround() && !minecraft.player.isInWater()) {
                           if (this.pathFollower.hasRemainingNodes()) {
                              this.followPath();
                           } else {
                              this.clearTarget();
                              this.setStatus("Waiting to land");
                           }
                        } else if (this.stocking) {
                           this.describeRecording(var1);
                        } else if (this.currentTarget != null) {
                           if (this.isAggressiveActive()) {
                              ViewController.acquire(ViewController.Owner.AUTO_BUILDER);
                           }

                           if (this.currentAction == AutoBuilderModule.CellAction.PLACE) {
                              this.reset(var1);
                           } else {
                              this.subtractItems(var1);
                           }
                        } else if (this.countItem() && ViewController.update() == ViewController.Owner.RETURN) {
                           this.holdStill();
                           this.setStatus("Turning back to your look");
                        } else if (this.settleCountdown > 0) {
                           this.getReach();
                        } else {
                           int var10000 = minecraft.player.tickCount - this.lastActionTick;
                           int var10 = this.placeDelay.getValue().intValue();
                           int var9 = var10000;
                           if ((Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
                              this.getSchematic();
                           } else {
                              var10000 = minecraft.player.tickCount - this.lastActionTick;
                              int var12 = this.placeDelay.getValue().intValue() + this.extraPlaceDelay;
                              int var11 = var10000;
                              if ((Integer.compare(var11, var12) >>> 31 ^ 1) == 0) {
                                 this.getSchematic();
                              } else if (this.setStatus()) {
                                 if (this.pathFollower.hasRemainingNodes()) {
                                    this.followPath();
                                 } else {
                                    this.getSchematic();
                                 }
                              } else {
                                 this.isManualInput(var1);
                              }
                           }
                        }
                     }
                  }
               }
            } else {
               this.describeTransform();
            }
         }
      } else {
         this.stop("the world or player changed");
      }
   }

   private boolean setStatus() {
      BlockStatePredictionHandler var1 = ((ClientLevelMixin3)minecraft.level).LarpRig$getPendingUpdateManager();
      if (((BlockStatePredictionHandlerMixin)var1).larp$unconfirmed().isEmpty()) {
         this.unconfirmedSinceTick = -1000000000;
         return false;
      }

      int var2 = minecraft.player.tickCount;
      if (this.unconfirmedSinceTick == -1000000000) {
         this.unconfirmedSinceTick = var2;
      }

      if (var2 - this.unconfirmedSinceTick == 40) {
         LarpLog.info(this.getName(), "The server has not confirmed a block change for {} ticks; going on", 40);
      }

      if (var2 - this.unconfirmedSinceTick >= 40) {
         return false;
      }

      this.setStatus("Waiting for the server to confirm the last block");
      return true;
   }

   private void holdStill() {
      this.clearTarget();
      this.moveDirection = Vec3.ZERO;
   }

   private void clearTarget() {
      NativePlacement.cancel(this);
      this.currentTarget = null;
      this.currentHit = null;
   }

   private boolean isManualInput() {
      return minecraft.isWindowActive()
         && minecraft.gui.screen() == null
         && (
            PlayerInputUtil.isKeyDown(minecraft.options.keyUp)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyDown)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyLeft)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyRight)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyJump)
               || PlayerInputUtil.isKeyDown(minecraft.options.keyShift)
         );
   }

   private double getReach() {
      return Math.min(this.reach.getValue(), minecraft.player.blockInteractionRange());
   }

   private BlockPos getFeetPos() {
      return BlockPos.containing(minecraft.player.getX(), minecraft.player.getY() + 0.2, minecraft.player.getZ());
   }

   private void loadSchematic(AutoBuilderModule.SchematicLayout var1) {
      if (!this.pendingPlacements.isEmpty()) {
         MutableBlockPos var2 = new MutableBlockPos();
         Iterator var3 = this.pendingPlacements.entrySet().iterator();

         while (var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            int var5 = (Integer)var4.getKey();
            BlockPlacementUtil.PlacementFailure var6 = this.getCellState(var1, var5, var2);
            BlockState var7 = this.expectedStates.get(var5);
            int var8 = var6 != BlockPlacementUtil.PlacementFailure.PLACEABLE || var7 != null && !minecraft.level.getBlockState(var2).equals(var7) ? 0 : 1;
            if (var6 == BlockPlacementUtil.PlacementFailure.PLACEABLE && var8 == 0) {
               if (minecraft.player.tickCount - (Integer)var4.getValue() > 20) {
                  var3.remove();
                  this.expectedStates.remove(var5);
               }
            } else if (var8 != 0) {
               var3.remove();
               this.expectedStates.remove(var5);
               int var9 = this.serverRemovalCounts.merge(var5, 1, Integer::sum);
               this.ghostBlockTicks.addLast(minecraft.player.tickCount);
               LarpLog.debug(this.getName(), "The server took back {} ({} times)", var2.toShortString(), var9);
               if (var9 >= 3) {
                  this.serverRemovalCounts.remove(var5);
                  this.setAsideUntil.put(var5, minecraft.player.tickCount + 2400);
                  this.sendChat(var2.toShortString() + ": the server keeps removing it, skipping it for now.");
                  LarpLog.info(this.getName(), "Server removed {} {} times; skipping for {} ticks", var2.toShortString(), 3, 2400);
               }
            } else if (var6 == BlockPlacementUtil.PlacementFailure.STATE_MISMATCH) {
               var3.remove();
               this.expectedStates.remove(var5);
               if (BlockPlacementUtil.countInteractionsNeeded(var1.stateAt(var5), minecraft.level.getBlockState(var2)) < 0) {
                  LarpLog.info(
                     this.getName(),
                     "Placed {} facing the wrong way: {} instead of {}",
                     var2.toShortString(),
                     minecraft.level.getBlockState(var2),
                     var1.stateAt(var5)
                  );
                  if (this.facingRetryCounts.merge(var5, 1, Integer::sum) <= 2) {
                     this.wrongFacingCell = var5;
                     this.wrongFacingPos = var2.immutable();
                     this.wrongFacingTick = minecraft.player.tickCount;
                  }
               }
            } else if (minecraft.player.tickCount - (Integer)var4.getValue() > 20) {
               var3.remove();
               this.expectedStates.remove(var5);
            }
         }
      }
   }

   private void isManualInput(AutoBuilderModule.SchematicLayout var1) {
      BlockPos var2 = this.getFeetPos();
      if (!this.leftoverChecked) {
         this.leftoverChecked = (boolean)1;
         if (this.shouldRelog(var1)) {
            return;
         }
      }

      if (this.wrongFacingPos == null || !this.isManualInput(var1)) {
         if (this.towerPhase == null && this.supportPhase == null && this.sweepBlocks.contains(var2.below())) {
            if (this.standingOnTempSinceTick == -1000000000) {
               this.standingOnTempSinceTick = minecraft.player.tickCount;
            }

            if (minecraft.player.tickCount - this.standingOnTempSinceTick <= 200) {
               this.setStatus("Coming off a temporary block");
               if (!this.isAggressiveActive(var2.below())) {
                  this.startChestTrip(var2.below());
               }

               return;
            }

            LarpLog.info(this.getName(), "Could not dig {} from on top of it; going on without it", var2.below().toShortString());
            BlockBreaker.dumpTrace(this.getName(), var2.below());
            this.sweepBlocks.remove(var2.below());
         }

         this.standingOnTempSinceTick = -1000000000;
         if (this.supportPhase == null || !this.getInstance(var1)) {
            if (this.towerPhase == AutoBuilderModule.TowerPhase.TOP && this.sweepFromTower) {
               this.blockOf();
            } else if (this.towerPhase == AutoBuilderModule.TowerPhase.WALK_TO_PILLAR) {
               this.isAggressiveActive(var1);
            } else if (this.restockPhase == AutoBuilderModule.RestockPhase.WALK_TO_CONTAINER) {
               this.getPlacedCount(var1);
            } else if (this.restockPhase == AutoBuilderModule.RestockPhase.OPEN_CONTAINER) {
               this.getMaterialNeeds();
            } else {
               List var3 = this.isRunning(var1);
               if (!this.tickRelog(var3)) {
                  if (this.returningDoorway != null) {
                     this.renderPreview(var1);
                  } else if (var3 == null && this.finishBuild()) {
                     this.startChestTrip();
                  } else {
                     if (this.towerPhase == AutoBuilderModule.TowerPhase.TOP
                        && this.activeDoorway != null
                        && this.towerBlocks.isEmpty()
                        && this.activePillar != null) {
                        this.getFeetPos();
                     }

                     if (this.neededPillarBlocks <= 0 || this.towerPhase != null || this.supportPhase != null || !this.turnSchematic(var1)) {
                        if (this.missingItems.isEmpty()
                           || var3 != null && this.lowestMissingLayer >= this.currentLayer
                           || !this.restockFromContainers.getValue()
                           || this.towerPhase != null
                           || !this.isRunning(var1)) {
                           if (this.missingItems.isEmpty()
                              || var3 != null && this.lowestMissingLayer >= this.currentLayer
                              || !this.buyMissingBlocks.getValue()
                              || this.towerPhase != null
                              || !this.cycleSchematic(var1)) {
                              long var4 = this.placedCount * 1000003L
                                 + this.layerGroups.size() * 1009L
                                 + this.givenUpCells.size() * 31L
                                 + this.setAsideUntil.size();
                              long var6 = var2.asLong() * 31L + var4;
                              if (this.temporaryPillars.getValue()
                                 && this.autoMove.getValue()
                                 && this.supportPhase == null
                                 && (this.towerPhase == null || this.towerPhase == AutoBuilderModule.TowerPhase.TOP)
                                 && !this.finishBuild()
                                 && var6 != this.lastSupportKey) {
                                 for (List var9 : this.layerGroups) {
                                    if (this.getCellState(var1, var9)) {
                                       return;
                                    }
                                 }

                                 this.lastSupportKey = var6;
                              }

                              if (this.temporaryPillars.getValue()
                                 && this.autoMove.getValue()
                                 && this.towerPhase == null
                                 && this.supportPhase == null
                                 && !this.finishBuild()
                                 && var4 != this.lastPillarKey) {
                                 for (List var17 : this.layerGroups) {
                                    if (this.renderPreview(var1, var17)) {
                                       return;
                                    }
                                 }

                                 this.lastPillarKey = var4;
                              }

                              if (var3 == null) {
                                 if (this.towerPhase == AutoBuilderModule.TowerPhase.TOP) {
                                    this.finishBuild("the build is done");
                                 } else {
                                    this.getSchematicsDir(var1);
                                 }
                              } else {
                                 WorldPathView var16 = new WorldPathView(var0 -> -1, false);
                                 double var18 = this.getReach();
                                 int var11 = this.pathFollower.hasRemainingNodes() && minecraft.player.tickCount - this.lastRouteTick <= 100 ? 0 : 1;
                                 BuildPlanner.PlacementPlan var12 = BuildPlanner.planPlacement(
                                    var16,
                                    var3,
                                    this.towerCells,
                                    var2.getX(),
                                    var2.getY(),
                                    var2.getZ(),
                                    var18,
                                    var1.bounds(),
                                    var2x -> this.isPlaceable(var1, var2x),
                                    (var2x, var3x, var4x, var5) -> this.findPlaceHit(var1, var2x) != null,
                                    (var2x, var3x, var4x, var5) -> this.canPlaceFrom(var1, var2x, var3x, var4x, var5),
                                    this.towerPhase == AutoBuilderModule.TowerPhase.TOP ? 120 : 24,
                                    (boolean)var11,
                                    this.skippedStands.getSkipped(),
                                    this.lastPlaced,
                                    this.autoMove.getValue() && this.towerPhase != AutoBuilderModule.TowerPhase.TOP
                                 );
                                 if ("keep walking".equals(var12.reason())) {
                                    this.followPath();
                                 } else if (var12.place() != null) {
                                    this.pathFollower.reset();
                                    this.currentGroup = List.of();
                                    this.skippedStands.clear();
                                    this.clearTarget(var1, var12.place());
                                 } else if (this.towerPhase != AutoBuilderModule.TowerPhase.TOP || !this.getInstance(var1, var16, var2, var18)) {
                                    if (this.towerPhase != AutoBuilderModule.TowerPhase.TOP || !var12.walk() || !this.describeTransform(var1, var12)) {
                                       if (this.towerPhase != AutoBuilderModule.TowerPhase.TOP
                                          || !this.walkwayBlocks.isEmpty()
                                          || !this.isRunning(var1, var12.walkingFor())) {
                                          if (this.towerPhase != AutoBuilderModule.TowerPhase.TOP || !this.getPlacedCount(var1, var12.walkingFor())) {
                                             if (this.towerPhase == AutoBuilderModule.TowerPhase.TOP) {
                                                this.finishBuild("nothing more to place from the top");
                                             } else if (var12.walk() && this.stopPending) {
                                                this.stopPending = (boolean)0;
                                                this.settling = (boolean)1;
                                                this.settleCountdown = 12;
                                             } else {
                                                if (var12.walk() && this.skippedStands.commitPending()) {
                                                   LarpLog.debug(
                                                      this.getName(),
                                                      "Nothing placeable from {}; leaving that spot out until a block goes down ({} left out)",
                                                      var2.toShortString(),
                                                      this.skippedStands.getSkipped().size()
                                                   );
                                                }

                                                if (var12.walk()) {
                                                   if (!this.autoMove.getValue()) {
                                                      this.getSchematic();
                                                      this.setStatus("Nothing in reach; walk closer (Auto Move is off)");
                                                      this.notifyOnce("nothing is in reach; walk closer or turn on Auto Move");
                                                   } else {
                                                      this.walkToStand(var1, var12);
                                                   }
                                                } else {
                                                   this.pathFollower.reset();
                                                   if (var12.reason() == null) {
                                                      this.getSchematicsDir(var1);
                                                   } else if ("no standing spot within reach".equals(var12.reason()) && this.describeTransform()) {
                                                      this.renderRestockOutlines(this.activeDoorway);
                                                   } else if (!"no standing spot within reach".equals(var12.reason())
                                                      || !this.temporaryPillars.getValue()
                                                      || !this.getReach(var1, var12.walkingFor())) {
                                                      for (BuildPlanner.BlockTarget var14 : var12.walkingFor()) {
                                                         this.setAside(var14.stateId(), var12.reason());
                                                      }

                                                      this.setStatus("Setting aside " + var12.walkingFor().size() + " blocks: " + var12.reason());
                                                   }
                                                }
                                             }
                                          }
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private List<BuildPlanner.BlockTarget> isRunning(AutoBuilderModule.SchematicLayout var1) {
      MutableBlockPos var2 = new MutableBlockPos();
      int var3 = minecraft.player.tickCount;
      this.setAsideUntil.values().removeIf(var1x -> var1x <= var3);
      this.missingItems.clear();
      this.actionByCell.clear();
      this.unsupportedCount = 0;
      this.lowestMissingLayer = Integer.MAX_VALUE;
      this.layerGroups.clear();
      this.findPlaceHit(var1);
      if (this.followRecording.getValue() && this.recordedOrder != null && this.recordedLayout == var1) {
         for (int var7 : this.recordedOrder) {
            if (var1.stateAt(var7) != null
               && var1.unplaceableReasons[var1.paletteIndices[var7]] == null
               && !var1.isSecondHalf(var7)
               && !this.givenUpCells.contains(var7)
               && !this.setAsideUntil.containsKey(var7)
               && !this.outOfHeightCells.contains(var7)
               && this.getCellState(var1, var7, var2) == BlockPlacementUtil.PlacementFailure.PLACEABLE) {
               BlockState var8 = var1.stateAt(var7);
               int var9 = var2.getY() - var1.minY;
               BlockItem var10 = BlockPlacementUtil.getPlacingItem(var8);
               if (var10 != null && this.countItem(var10) != 0) {
                  if (BlockPlacementUtil.countInteractionsNeeded(var8) && minecraft.level.getBlockState(var2.below()).canBeReplaced()) {
                     this.unsupportedCount++;
                  } else {
                     BuildPlanner.BlockTarget var11 = new BuildPlanner.BlockTarget(var2.getX(), var2.getY(), var2.getZ(), var7);
                     this.actionByCell.put(var7, AutoBuilderModule.CellAction.PLACE);
                     if (this.isPlaceable(var1, var11)) {
                        this.currentLayer = var9;
                        return List.of(var11);
                     }
                  }
               } else {
                  this.missingItems.merge(var10, 1, Integer::sum);
                  if (var10 != null) {
                     this.lowestMissingLayer = Math.min(this.lowestMissingLayer, var9);
                  }
               }
            }
         }

         this.actionByCell.clear();
      }

      HashSet var14 = this.finishBuild() && !this.activeDoorway.aims().isEmpty()
         ? new HashSet<>(this.activeDoorway.aims().stream().map(BuildPlanner.BlockTarget::stateId).toList())
         : null;

      for (int var15 = 0; var15 + 1 < var1.layerOffsets.length; var15++) {
         ArrayList var16 = null;
         byte var17 = 0;

         for (int var18 = var1.layerOffsets[var15]; var18 < var1.layerOffsets[var15 + 1]; var18++) {
            int var19 = var1.indicesByY[var18];
            if (var1.stateAt(var19) != null
               && var1.unplaceableReasons[var1.paletteIndices[var19]] == null
               && !var1.isSecondHalf(var19)
               && !this.givenUpCells.contains(var19)
               && !this.setAsideUntil.containsKey(var19)
               && !this.keptOpenCells.contains(var19)
               && !this.outOfHeightCells.contains(var19)
               && (var14 == null || var14.contains(var19))) {
               BlockPlacementUtil.PlacementFailure var20 = this.getCellState(var1, var19, var2);
               BlockState var21 = var1.stateAt(var19);
               AutoBuilderModule.CellAction var12;
               if (var20 == BlockPlacementUtil.PlacementFailure.PLACEABLE) {
                  var12 = AutoBuilderModule.CellAction.PLACE;
               } else if (var20 == BlockPlacementUtil.PlacementFailure.STATE_MISMATCH
                  && BlockPlacementUtil.countInteractionsNeeded(var21, minecraft.level.getBlockState(var2)) > 0) {
                  var12 = AutoBuilderModule.CellAction.ADJUST;
               } else {
                  if (var20 != BlockPlacementUtil.PlacementFailure.SECOND_HALF
                     || !var1.disabledSlots.containsKey(var19)
                     || this.configuredCrafters.contains(var19)) {
                     continue;
                  }

                  var12 = AutoBuilderModule.CellAction.OPEN_CRAFTER;
               }

               if (var12 == AutoBuilderModule.CellAction.PLACE) {
                  BlockItem var13 = BlockPlacementUtil.getPlacingItem(var21);
                  if (var13 == null || this.countItem(var13) == 0) {
                     this.missingItems.merge(var13, 1, Integer::sum);
                     if (var13 != null) {
                        this.lowestMissingLayer = Math.min(this.lowestMissingLayer, var15);
                     }
                     continue;
                  }

                  if (BlockPlacementUtil.countInteractionsNeeded(var21) && minecraft.level.getBlockState(var2.below()).canBeReplaced()) {
                     this.unsupportedCount++;
                     continue;
                  }
               }

               this.actionByCell.put(var19, var12);
               if (var16 == null) {
                  var16 = new ArrayList();
               }

               BuildPlanner.BlockTarget var22 = new BuildPlanner.BlockTarget(var2.getX(), var2.getY(), var2.getZ(), var19);
               var16.add(var22);
               if (var17 == 0 && this.isPlaceable(var1, var22)) {
                  var17 = 1;
               }
            }
         }

         if (var16 != null && var17 != 0) {
            this.currentLayer = var15;
            return this.stopRecording(var1, var16);
         }

         if (var16 != null) {
            this.layerGroups.add(var16);
         }
      }

      return null;
   }

   private void findPlaceHit(AutoBuilderModule.SchematicLayout var1) {
      if (this.doorways == null) {
         this.stopRecording(var1);
         this.setAside(var1);
      }

      if (!this.doorways.isEmpty() && this.returningDoorway == null) {
         MutableBlockPos var2 = new MutableBlockPos();

         for (BuildPlanner.Breakout var4 : List.copyOf(this.doorways)) {
            byte var5 = 0;
            if (!var4.aims().isEmpty()) {
               for (BuildPlanner.BlockTarget var7 : var4.aims()) {
                  if (this.getCellState(var1, var7.stateId(), var2) == BlockPlacementUtil.PlacementFailure.PLACEABLE) {
                     var5 = 1;
                     break;
                  }
               }

               if (var5 == 0) {
                  this.renderRestockOutlines(var4);
               }
            }
         }
      }
   }

   private void stopRecording(AutoBuilderModule.SchematicLayout var1) {
      this.outOfHeightCells.clear();

      for (int var2 = 0; var2 < var1.blockCount; var2++) {
         if (var1.stateAt(var2) != null && minecraft.level.isOutsideBuildHeight(BlockPos.getY(var1.packedPositions[var2]))) {
            this.outOfHeightCells.add(var2);
         }
      }

      if (!this.outOfHeightCells.isEmpty()) {
         String var3 = "Y " + minecraft.level.getMinY() + " to " + minecraft.level.getMaxY();
         this.sendChat(
            this.outOfHeightCells.size()
               + " blocks of the schematic are outside this world's height limits ("
               + var3
               + ") and are left out; move the origin to build them."
         );
         LarpLog.info(this.getName(), "{} cells are outside the height limits ({}) and are left out", this.outOfHeightCells.size(), var3);
      }
   }

   private void setAside(AutoBuilderModule.SchematicLayout var1) {
      this.doorways = new ArrayList<>();
      this.keptOpenCells.clear();
      if (this.temporaryPillars.getValue() && this.autoMove.getValue() && (!this.followRecording.getValue() || this.recordedOrder == null)) {
         ArrayList var2 = new ArrayList();
         MutableBlockPos var3 = new MutableBlockPos();

         for (int var4 = 0; var4 < var1.blockCount; var4++) {
            BlockState var5 = var1.stateAt(var4);
            if (var5 != null
               && var1.unplaceableReasons[var1.paletteIndices[var4]] == null
               && !var1.isSecondHalf(var4)
               && BlockPlacementUtil.getInteractableProperties(var5)
               && this.getCellState(var1, var4, var3) == BlockPlacementUtil.PlacementFailure.PLACEABLE) {
               var2.add(new BuildPlanner.BlockTarget(var3.getX(), var3.getY(), var3.getZ(), var4));
            }
         }

         if (!var2.isEmpty()) {
            HashSet var10 = new HashSet();

            for (BuildPlanner.BlockTarget var6 : var2) {
               var10.add(var6.stateId());
            }

            BuildPlanner.CellFilter var12 = (var3x, var4x, var5x) -> {
               int var6x = var1.indexAt(var3x, var4x, var5x);
               return var6x >= 0
                  && !var10.contains(var6x)
                  && var1.stateAt(var6x) != null
                  && var1.unplaceableReasons[var1.paletteIndices[var6x]] == null
                  && !var1.isSecondHalf(var6x)
                  && this.getCellState(var1, var6x, new MutableBlockPos()) == BlockPlacementUtil.PlacementFailure.PLACEABLE;
            };
            this.doorways
               .addAll(
                  BuildPlanner.planPlacement(
                     (var2x, var3x, var4x) -> this.isPlaceable(var1, var2x, var3x, var4x), var12, var2, var1.bounds(), this.getReach() - 0.3
                  )
               );

            for (BuildPlanner.Breakout var7 : this.doorways) {
               for (int[] var9 : var7.cells()) {
                  this.keptOpenCells.add(var1.indexAt(var9[0], var9[1], var9[2]));
               }

               LarpLog.info(
                  this.getName(),
                  "Keeping {} blocks open beside {} {} {} until the {} blocks that need a look up from inside stand",
                  var7.cells().size(),
                  var7.outX(),
                  var7.outY(),
                  var7.outZ(),
                  var7.aims().size()
               );
            }
         }
      }
   }

   private boolean isPlaceable(AutoBuilderModule.SchematicLayout var1, int var2, int var3, int var4) {
      BlockPos var5 = new BlockPos(var2, var3, var4);
      int var6 = var1.indexAt(var2, var3, var4);
      int var7 = var6 >= 0 && var1.stateAt(var6) != null && var1.unplaceableReasons[var1.paletteIndices[var6]] == null ? 1 : 0;
      return (var7 != 0 ? var1.stateAt(var6) : minecraft.level.getBlockState(var5)).getCollisionShape(minecraft.level, var5).isEmpty();
   }

   private boolean isManualInput(AutoBuilderModule.SchematicLayout var1, BuildPlanner.PlacementPlan var2) {
      if (this.supportPhase == null && this.doorways != null) {
         for (BuildPlanner.Breakout var4 : this.doorways) {
            if (var4 != this.activeDoorway && this.cycleSchematic(var1, var4, var2.walkingFor())) {
               Item var5 = this.getMaterialNeeds();
               if (var5 == null) {
                  return false;
               }

               BuildPlanner.Pillar var6 = BuildPlanner.pillarAfterBreakout(
                  new WorldPathView(var0 -> -1, false), var4, this.maxPillarHeight.getValue().intValue(), var1::stateAt, var2.walkingFor()
               );
               if (var6 == null) {
                  return false;
               }

               if (this.countItem(var5) < var6.height()) {
                  this.neededPillarBlocks = var6.height();
                  return true;
               }

               this.activePillar = var6;
               this.towerPhase = AutoBuilderModule.TowerPhase.WALK_TO_PILLAR;
               this.pillarWalkStartTick = this.pillarPhaseTick = minecraft.player.tickCount;
               this.pathFollower.reset();
               this.currentGroup = List.of();
               this.activeDoorway = var4;
               LarpLog.info(
                  this.getName(),
                  "Tower of {} at {} {} {} leads to the doorway for {} blocks that need a look up",
                  var6.height(),
                  var6.pillarX(),
                  var6.ground(),
                  var6.pillarZ(),
                  var4.aims().size()
               );
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private boolean describeTransform(AutoBuilderModule.SchematicLayout var1, BuildPlanner.PlacementPlan var2) {
      if (this.activeDoorway == null
         && this.activePillar != null
         && !var2.stands().isEmpty()
         && this.walkwayBlocks.isEmpty()
         && this.placedCount != this.lastTowerDonePlaced) {
         BlockPos var3 = this.getFeetPos();
         HashSet var4 = new HashSet();

         for (long var6 : var2.stands()) {
            if (AStarPathfinder.unpackY(var6) >= var3.getY() - 1) {
               var4.add(var6);
            }
         }

         if (!var4.isEmpty() && this.reset(var1, var4, new BlockPos(var2.toward().blockX(), var2.toward().blockY(), var2.toward().blockZ()))) {
            this.towerExitPlacedCount = this.placedCount;
            this.activeDoorway = new BuildPlanner.Breakout(List.of(), var3.getX(), var3.getY(), var3.getZ(), List.of(), Set.of());
            this.doorways.add(this.activeDoorway);
            this.currentGroup = var2.walkingFor();
            this.getFeetPos();
            this.followPath();
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean cycleSchematic(AutoBuilderModule.SchematicLayout var1, BuildPlanner.Breakout var2, List<BuildPlanner.BlockTarget> var3) {
      for (BuildPlanner.BlockTarget var5 : var3) {
         if (!var2.aims().stream().noneMatch(var1x -> var1x.stateId() == var5.stateId())) {
            for (long var7 : var2.space()) {
               if (this.canPlaceFrom(var1, var5, AStarPathfinder.unpackX(var7), AStarPathfinder.unpackY(var7), AStarPathfinder.unpackZ(var7))) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean describeTransform() {
      return this.finishBuild() && this.activeDoorway.aims().isEmpty();
   }

   private boolean finishBuild() {
      return this.activeDoorway != null && !this.towerBlocks.isEmpty() && this.returningDoorway == null;
   }

   private void getFeetPos() {
      this.savedPillar = this.activePillar;
      this.towerBlocks.addAll(this.pillarBlocks);
      this.pillarBlocks.clear();
      this.activePillar = null;
      this.towerPhase = null;
      this.skippedStands.reset();
      if (this.activeDoorway.aims().isEmpty()) {
         LarpLog.info(
            this.getName(),
            "Walking off the tower at {} {} {} along the build",
            this.activeDoorway.outX(),
            this.activeDoorway.outY(),
            this.activeDoorway.outZ()
         );
      } else {
         LarpLog.info(
            this.getName(),
            "Walking off the tower at {} {} {} for {} blocks",
            this.activeDoorway.outX(),
            this.activeDoorway.outY(),
            this.activeDoorway.outZ(),
            this.activeDoorway.aims().size()
         );
      }
   }

   private void startChestTrip() {
      byte var1 = 0;

      for (BuildPlanner.BlockTarget var3 : this.activeDoorway.aims()) {
         if (this.setAsideUntil.remove(var3.stateId()) != null) {
            var1 = 1;
         }
      }

      if (var1 == 0) {
         this.renderRestockOutlines(this.activeDoorway);
      }
   }

   private void renderRestockOutlines(BuildPlanner.Breakout var1) {
      if (var1 == this.activeDoorway && !this.towerBlocks.isEmpty()) {
         this.returningDoorway = var1;
         this.towerReturnTick = minecraft.player.tickCount;
         this.pathFollower.reset();
         LarpLog.info(this.getName(), "Going back to the tower at {} {} {}", var1.outX(), var1.outY(), var1.outZ());
      } else {
         this.getFeetPos(var1);
      }
   }

   private void getFeetPos(BuildPlanner.Breakout var1) {
      this.doorways.remove(var1);
      if (this.layout != null) {
         for (int[] var3 : var1.cells()) {
            int var4 = this.layout.indexAt(var3[0], var3[1], var3[2]);
            this.keptOpenCells.remove(var4);
            this.towerCells.remove(var4);
         }
      }

      this.skippedStands.reset();
   }

   private void renderPreview(AutoBuilderModule.SchematicLayout var1) {
      BuildPlanner.Breakout var2 = this.returningDoorway;
      BlockPos var3 = this.getFeetPos();
      BlockPos var4 = new BlockPos(var2.outX(), var2.outY(), var2.outZ());
      if (var3.equals(var4)) {
         this.pathFollower.reset();
         if (var2.aims().isEmpty() && this.placedCount == this.towerExitPlacedCount) {
            this.lastTowerDonePlaced = this.placedCount;
         }

         Vec3 var6 = Vec3.atBottomCenterOf(var4).subtract(minecraft.player.position()).multiply(1.0, 0.0, 1.0);
         if (var6.length() >= 0.25 && minecraft.player.tickCount - this.towerReturnTick <= 600) {
            this.sneaking = (boolean)1;
            this.moveDirection = var6;
            this.setStatus("Stepping to the middle of the tower");
         } else {
            this.pillarBlocks.addAll(this.towerBlocks);
            this.towerBlocks.clear();
            this.activePillar = this.savedPillar;
            this.savedPillar = null;
            this.towerPhase = AutoBuilderModule.TowerPhase.TOP;
            this.towerPlacedMark = this.placedCount;
            this.returningDoorway = null;
            this.activeDoorway = null;
            this.getFeetPos(var2);
         }
      } else {
         this.setStatus("Going back to the tower");
         int var5 = minecraft.player.tickCount - this.towerReturnTick > 600 ? 1 : 0;
         if (var5 != 0
            || !this.pathFollower.hasRemainingNodes() && !this.reset(var1, Set.of(AStarPathfinder.packPos(var4.getX(), var4.getY(), var4.getZ())), var4)) {
            LarpLog.info(
               this.getName(),
               "No way back to the tower from {} ({}); it is swept up at the end",
               var3.toShortString(),
               var5 != 0 ? "took too long" : "no route"
            );
            if (var2.aims().isEmpty() && this.placedCount == this.towerExitPlacedCount) {
               this.lastTowerDonePlaced = this.placedCount;
            }

            this.pathFollower.reset();
            this.sweepBlocks.addAll(this.towerBlocks);
            this.towerBlocks.clear();
            this.savedPillar = null;
            this.returningDoorway = null;
            this.activeDoorway = null;
            this.getFeetPos(var2);
         } else {
            this.followPath();
         }
      }
   }

   private List<BuildPlanner.BlockTarget> stopRecording(AutoBuilderModule.SchematicLayout var1, List<BuildPlanner.BlockTarget> var2) {
      HashSet var3 = new HashSet();

      for (BuildPlanner.BlockTarget var5 : var2) {
         BlockState var6 = var1.stateAt(var5.stateId());
         if (BlockPlacementUtil.isDirectional(var6)
            && this.actionByCell.getOrDefault(var5.stateId(), AutoBuilderModule.CellAction.PLACE) == AutoBuilderModule.CellAction.PLACE) {
            Direction var7 = (Direction)var6.getValue(BlockStateProperties.FACING);
            if (!var7.getAxis().isVertical()) {
               for (int var8 = 1; var8 <= 3; var8++) {
                  var3.add(AStarPathfinder.packPos(var5.blockX() + var7.getStepX() * var8, var5.blockY(), var5.blockZ() + var7.getStepZ() * var8));
               }
            }
         }
      }

      if (var3.isEmpty()) {
         return var2;
      }

      List var9 = var2.stream()
         .filter(
            var2x -> BlockPlacementUtil.isDirectional(var1.stateAt(var2x.stateId()))
               || !var3.contains(AStarPathfinder.packPos(var2x.blockX(), var2x.blockY(), var2x.blockZ()))
         )
         .toList();
      return var9.stream().anyMatch(var2x -> this.isPlaceable(var1, var2x)) ? var9 : var2;
   }

   private boolean isPlaceable(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2) {
      BlockPos var3 = new BlockPos(var2.blockX(), var2.blockY(), var2.blockZ());
      return this.actionByCell.getOrDefault(var2.stateId(), AutoBuilderModule.CellAction.PLACE) != AutoBuilderModule.CellAction.PLACE
         ? true
         : BlockPlacementUtil.hasSupportNeighbor(var3) || minecraft.level.getBlockState(var3).is(var1.stateAt(var2.stateId()).getBlock());
   }

   private BlockHitResult findPlaceHit(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2) {
      BlockState var3 = var1.stateAt(var2.stateId());
      BlockPos var4 = new BlockPos(var2.blockX(), var2.blockY(), var2.blockZ());
      if (this.actionByCell.getOrDefault(var2.stateId(), AutoBuilderModule.CellAction.PLACE) != AutoBuilderModule.CellAction.PLACE) {
         return BlockPlacementUtil.findInteractHit(this, var4, minecraft.player.getEyePosition(), this.getReach(), var3);
      }

      ItemStack var5 = this.getSchematicsDir(BlockPlacementUtil.getPlacingItem(var3));
      if (var5.isEmpty()) {
         return null;
      }

      if (minecraft.player.getBoundingBox().intersects(new AABB(var4))) {
         return null;
      }

      BlockPos var6 = BlockPlacementUtil.getOtherHalfPos(var3, var4);
      return var6 != null && minecraft.player.getBoundingBox().intersects(new AABB(var6))
         ? null
         : BlockPlacementUtil.findPlacementHit(this, minecraft.player, var5, var4, var3, minecraft.player.getEyePosition(), this.getReach(), true);
   }

   private boolean canPlaceFrom(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2, int var3, int var4, int var5) {
      BlockState var6 = var1.stateAt(var2.stateId());
      BlockPos var7 = new BlockPos(var2.blockX(), var2.blockY(), var2.blockZ());
      double var8 = this.getReach() - 0.3;
      if (this.actionByCell.getOrDefault(var2.stateId(), AutoBuilderModule.CellAction.PLACE) != AutoBuilderModule.CellAction.PLACE) {
         return BlockPlacementUtil.findInteractHit(this, var7, new Vec3(var3 + 0.5, var4 + 1.62, var5 + 0.5), var8, var6) != null;
      }

      ItemStack var10 = this.getSchematicsDir(BlockPlacementUtil.getPlacingItem(var6));
      if (var10.isEmpty()) {
         return false;
      }

      BlockPos var11 = BlockPlacementUtil.getOtherHalfPos(var6, var7);
      if (var11 == null || var11.getX() != var3 || var11.getZ() != var5 || var11.getY() != var4 && var11.getY() != var4 + 1) {
         for (double var15 : new double[]{1.62, minecraft.player.getEyeHeight(Pose.CROUCHING)}) {
            if (BlockPlacementUtil.findPlacementHitInternal(
                  this, minecraft.player, var10, var7, var6, new Vec3(var3 + 0.5, var4 + var15, var5 + 0.5), var8, true, true
               )
               == null) {
               return false;
            }

            if (!this.safeSneak.getValue()) {
               break;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private ItemStack getSchematicsDir(Item var1) {
      if (var1 == null) {
         return ItemStack.EMPTY;
      }

      Inventory var2 = minecraft.player.getInventory();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var2.getItem(var3).is(var1)) {
            return var2.getItem(var3);
         }
      }

      return ItemStack.EMPTY;
   }

   private void setAside(int var1, String var2) {
      int var3 = this.attemptCounts.merge(var1, 1, Integer::sum);
      String var4 = this.layout == null ? "?" : this.layout.posAt(var1).toShortString();
      if (var3 >= 3) {
         this.givenUpCells.add(var1);
         this.giveUpReasons.put(var1, var2);
         LarpLog.info(this.getName(), "Giving up on {}: {}", var4, var2);
      } else {
         this.setAsideUntil.put(var1, minecraft.player.tickCount + 400);
         LarpLog.debug(this.getName(), "Setting {} aside for now: {}", var4, var2);
      }
   }

   private void getSchematicsDir(AutoBuilderModule.SchematicLayout var1) {
      this.pathFollower.reset();
      this.skippedStands.reset();
      if (!this.setAsideUntil.isEmpty()) {
         byte var2 = 0;

         for (Integer var4 : new ArrayList<>(this.setAsideUntil.keySet())) {
            if (!this.givenUpCells.contains(var4)) {
               this.setAsideUntil.remove(var4);
               var2 = 1;
            }
         }

         if (var2 != 0) {
            LarpLog.debug(this.getName(), "Retrying blocks set aside");
            this.setStatus("Trying the blocks set aside again");
            return;
         }
      }

      if (!this.sweepBlocks.isEmpty()) {
         this.setStatus(var1);
      } else if (!this.givenUpCells.isEmpty() && this.towerPhase == null && this.towerBlocks.isEmpty() && this.progressPercent()) {
         BlockPos var6 = this.getFeetPos();
         this.stop("no way to walk from where you stand at " + var6.toShortString() + "; stand on the floor beside the build and start again");
      } else if (!this.givenUpCells.isEmpty() && this.placedCount > this.giveUpRetryMark) {
         this.giveUpRetryMark = this.placedCount;
         LarpLog.info(this.getName(), "Trying the {} blocks given up on again, now that more of the build stands", this.givenUpCells.size());
         this.abandonedCells.clear();
         this.givenUpCells.clear();
         this.giveUpReasons.clear();
         this.attemptCounts.clear();
         this.setStatus("Trying the blocks given up on again");
      } else if (this.supportFailures.values().stream().anyMatch(var0 -> var0 >= 2) && this.placedCount > this.supportRetryMark) {
         this.supportRetryMark = this.placedCount;
         this.supportFailures.clear();
         this.supportFailureMark = this.placedCount;
         this.lastPillarKey = Long.MIN_VALUE;
         this.lastSupportKey = Long.MIN_VALUE;
         LarpLog.info(this.getName(), "Trying temporary supports and towers once more, {} blocks placed so far", this.placedCount);
         this.setStatus("Trying towers again");
      } else if (this.doorways != null && !this.doorways.isEmpty()) {
         this.renderRestockOutlines(this.doorways.getFirst());
      } else if (!this.missingItems.isEmpty()) {
         ArrayList var5 = new ArrayList();

         for (Entry var8 : this.missingItems.entrySet()) {
            var5.add(var8.getValue() + " " + (var8.getKey() == null ? "unplaceable" : shouldRelog((Item)var8.getKey())));
         }

         this.stop(
            "out of blocks: "
               + String.join(", ", var5.subList(0, Math.min(5, var5.size())))
               + (var5.size() > 5 ? " and more" : "")
               + (this.emptyContainers.isEmpty() ? "" : " (the marked containers have none)")
         );
      } else {
         this.finishBuild();
      }
   }

   private void clearTarget(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2) {
      this.currentTarget = var2;
      this.currentAction = this.actionByCell.getOrDefault(var2.stateId(), AutoBuilderModule.CellAction.PLACE);
      this.targetStartTick = minecraft.player.tickCount;
      this.currentHit = null;
      if (this.isAggressiveActive()) {
         ViewController.acquire(ViewController.Owner.AUTO_BUILDER);
      }

      if (this.currentAction == AutoBuilderModule.CellAction.PLACE) {
         this.reset(var1);
      } else {
         this.subtractItems(var1);
      }
   }

   private void reset(AutoBuilderModule.SchematicLayout var1) {
      BuildPlanner.BlockTarget var2 = this.currentTarget;
      BlockPos var3 = new BlockPos(var2.blockX(), var2.blockY(), var2.blockZ());
      MutableBlockPos var4 = new MutableBlockPos();
      if (this.getCellState(var1, var2.stateId(), var4) != BlockPlacementUtil.PlacementFailure.PLACEABLE) {
         this.clearTarget();
      } else if (minecraft.player.tickCount - this.targetStartTick > 40) {
         LarpLog.debug(this.getName(), "Placing {} took too long: {}", var3.toShortString(), NativePlacement.getFailureReason(this));
         this.clearTarget();
         this.setAside(var2.stateId(), "could not place it");
      } else {
         BlockState var5 = var1.stateAt(var2.stateId());
         BlockItem var6 = BlockPlacementUtil.getPlacingItem(var5);
         int var7 = this.setStatus(var6);
         if (var7 < 0) {
            this.setStatus(var6);
         } else {
            this.getSchematic();
            if (!this.describeTransform(var7)) {
               this.setStatus("Taking out " + minecraft.player.getInventory().getItem(var7).getHoverName().getString());
            } else {
               BlockHitResult var8 = this.findPlaceHit(var1, var2);
               if (var8 == null) {
                  this.clearTarget();
               } else {
                  this.currentHit = var8;
                  this.stateBeforePlace = minecraft.level.getBlockState(var3);
                  BlockPos var9 = var8.getBlockPos();
                  this.findPlaceHit(var9);
                  double var10 = this.getReach();
                  boolean var12 = NativePlacement.matchesItem(
                     this,
                     var3,
                     var8,
                     var6,
                     var10,
                     true,
                     () -> NativePlacement.facePoints(var3, var6) && !this.isManualInput(),
                     () -> NativePlacement.matchesItem(this, var9, var10)
                        && BlockPlacementUtil.isFacingHitFace(minecraft.player, var8)
                        && BlockPlacementUtil.isSupportBlock(minecraft.player, var9)
                        && this.getSchematicsDir(var3, var9, var8.getDirection(), var5),
                     var3x -> this.startStocking(var1, var2, var3x)
                  );
                  this.setStatus(
                     var12
                        ? "Placing " + var5.getBlock().getName().getString() + " at " + var3.toShortString()
                        : "Cannot place at " + var3.toShortString() + ": " + NativePlacement.getFailureReason(this)
                  );
               }
            }
         }
      }
   }

   private void findPlaceHit(BlockPos var1) {
      if (BlockPlacementUtil.isSpecialInteractable(minecraft.level.getBlockState(var1))) {
         this.sneaking = (boolean)1;
      }
   }

   private boolean finishBuild(BlockPos var1) {
      return !BlockPlacementUtil.isSpecialInteractable(minecraft.level.getBlockState(var1))
         || this.lastSneakTick != -1000000000 && minecraft.player.tickCount - this.lastSneakTick >= 3;
   }

   private boolean getSchematicsDir(BlockPos var1, BlockPos var2, Direction var3, BlockState var4) {
      if (!this.finishBuild(var2)) {
         return false;
      }

      if (!BlockPlacementUtil.hasOrientationProperty(var4)) {
         return true;
      }

      Vec3 var5 = minecraft.player.getLookAngle();
      Vec3 var6 = minecraft.player.getEyePosition();
      BlockHitResult var7 = NativePlacement.raycastFromOrigin(this, var6, var2, var5, this.getReach());
      if (var7 != null && var7.getDirection() == var3) {
         ItemStack var8 = this.getSchematicsDir(BlockPlacementUtil.getPlacingItem(var4));
         BlockState var9 = minecraft.level.getBlockState(var1);
         LocalPlayerMixin_4 var10 = (LocalPlayerMixin_4)minecraft.player;
         if (!(Math.abs(Mth.wrapDegrees(minecraft.player.getYRot() - var10.larpYRotLast())) > 2.0F)
            && !(Math.abs(minecraft.player.getXRot() - var10.larpXRotLast()) > 2.0F)) {
            for (float[] var14 : new float[][]{{minecraft.player.getYRot(), minecraft.player.getXRot()}, {var10.larpYRotLast(), var10.larpXRotLast()}}) {
               BlockState var15 = BlockPlacementUtil.toRotation(minecraft.player, var8, var7, var14[0], var14[1]);
               if (!BlockPlacementUtil.isNextPlacementStep(var4, var9, var15)) {
                  return false;
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void startStocking(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2, boolean var3) {
      if (var3 && this.currentHit != null) {
         LarpLog.debug(
            this.getName(),
            "Click for {} {} {}: {} face of {}, held {}, player at {}",
            var2.blockX(),
            var2.blockY(),
            var2.blockZ(),
            this.currentHit.getDirection().getSerializedName(),
            this.currentHit.getBlockPos().toShortString(),
            minecraft.player.getMainHandItem().getHoverName().getString(),
            minecraft.player.blockPosition().toShortString()
         );
      }

      this.lastActionTick = minecraft.player.tickCount;
      this.currentTarget = null;
      this.currentHit = null;
      if (!var3) {
         LarpLog.debug(this.getName(), "Placement at {} {} {} was not accepted", var2.blockX(), var2.blockY(), var2.blockZ());
      } else {
         this.lastPlaced = var2;
         this.placedCount++;
         this.stopPending = (boolean)0;
         this.extraPlaceDelay = this.random.nextInt(5) + (this.random.nextInt(100) < 6 ? 8 + this.random.nextInt(16) : 0);
         this.pendingPlacements.put(var2.stateId(), minecraft.player.tickCount);
         if (this.stateBeforePlace != null) {
            this.expectedStates.put(var2.stateId(), this.stateBeforePlace);
         }
      }
   }

   private void subtractItems(AutoBuilderModule.SchematicLayout var1) {
      BuildPlanner.BlockTarget var2 = this.currentTarget;
      BlockPos var3 = new BlockPos(var2.blockX(), var2.blockY(), var2.blockZ());
      MutableBlockPos var4 = new MutableBlockPos();
      BlockPlacementUtil.PlacementFailure var5 = this.getCellState(var1, var2.stateId(), var4);
      BlockState var6 = var1.stateAt(var2.stateId());
      int var7 = this.currentAction == AutoBuilderModule.CellAction.ADJUST
         ? (
            var5 == BlockPlacementUtil.PlacementFailure.STATE_MISMATCH
                  && BlockPlacementUtil.countInteractionsNeeded(var6, minecraft.level.getBlockState(var3)) > 0
               ? 1
               : 0
         )
         : (
            var5 == BlockPlacementUtil.PlacementFailure.SECOND_HALF && !this.configuredCrafters.contains(var2.stateId()) && this.activeCrafterCell == null
               ? 1
               : 0
         );
      if (var7 == 0) {
         this.clearTarget();
      } else if (minecraft.player.tickCount - this.targetStartTick > 40) {
         LarpLog.debug(this.getName(), "Clicking {} took too long: {}", var3.toShortString(), NativePlacement.getFailureReason(this));
         this.clearTarget();
         this.setAside(var2.stateId(), this.currentAction == AutoBuilderModule.CellAction.ADJUST ? "could not adjust it" : "could not open the crafter");
      } else {
         this.moveDirection = Vec3.ZERO;
         this.sneaking = (boolean)0;
         int var8 = this.getMaterialNeeds();
         if (var8 >= 0 && !this.describeTransform(var8)) {
            this.setStatus("Putting the block away");
         } else {
            BlockHitResult var9 = this.findPlaceHit(var1, var2);
            if (var9 == null) {
               this.clearTarget();
            } else {
               this.currentHit = var9;
               ItemStack var10 = minecraft.player.getMainHandItem();
               Item var11 = var10.isEmpty() ? Items.AIR : var10.getItem();
               double var12 = this.getReach();
               boolean var14 = NativePlacement.isReplaceable(
                  this,
                  var3,
                  var11,
                  var12,
                  true,
                  false,
                  () -> !this.isManualInput(),
                  () -> !minecraft.player.isShiftKeyDown()
                     && minecraft.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4
                     && NativePlacement.matchesItem(this, var3, var12)
                     && BlockPlacementUtil.getPitchJitter(var6, minecraft.player.getYRot())
                     && BlockPlacementUtil.getPitchJitter(var6, ((LocalPlayerMixin_4)minecraft.player).larpYRotLast()),
                  var2x -> this.unloadSchematic(var2, var2x)
               );
               String var15 = this.currentAction == AutoBuilderModule.CellAction.OPEN_CRAFTER
                  ? "Opening the crafter at "
                  : "Adjusting " + var6.getBlock().getName().getString() + " at ";
               this.setStatus(var14 ? var15 + var3.toShortString() : "Cannot click " + var3.toShortString() + ": " + NativePlacement.getFailureReason(this));
            }
         }
      }
   }

   private int getMaterialNeeds() {
      Inventory var1 = minecraft.player.getInventory();
      if (!(var1.getSelectedItem().getItem() instanceof BlockItem)) {
         return var1.getSelectedSlot();
      }

      for (int var2 = 0; var2 < 9; var2++) {
         if (var1.getItem(var2).isEmpty()) {
            return var2;
         }
      }

      for (int var3 = 0; var3 < 9; var3++) {
         if (!(var1.getItem(var3).getItem() instanceof BlockItem)) {
            return var3;
         }
      }

      return -1;
   }

   private void unloadSchematic(BuildPlanner.BlockTarget var1, boolean var2) {
      this.lastActionTick = minecraft.player.tickCount;
      AutoBuilderModule.CellAction var3 = this.currentAction;
      this.currentTarget = null;
      this.currentHit = null;
      this.currentAction = AutoBuilderModule.CellAction.PLACE;
      if (!var2) {
         LarpLog.debug(this.getName(), "Click on {} {} {} was not accepted", var1.blockX(), var1.blockY(), var1.blockZ());
      } else {
         this.lastPlaced = var1;
         if (var3 == AutoBuilderModule.CellAction.OPEN_CRAFTER) {
            this.activeCrafterCell = var1.stateId();
            this.crafterOpenTick = minecraft.player.tickCount;
            this.containerOpenedTick = -1000000000;
         } else {
            this.rightClickCount++;
         }
      }
   }

   private boolean getMovementInput(AutoBuilderModule.SchematicLayout var1) {
      int var2 = minecraft.player.tickCount;
      int var3 = this.activeCrafterCell;
      BlockPos var4 = var1.posAt(var3);
      this.holdStill();
      if (minecraft.gui.screen() instanceof CrafterScreen var5) {
         if (this.containerOpenedTick == -1000000000) {
            this.containerOpenedTick = var2;
         }

         if (var2 - this.crafterOpenTick > 100) {
            this.activeCrafterCell = null;
            minecraft.player.closeContainer();
            this.setAside(var3, "could not set the crafter's slots");
            return true;
         }

         this.setStatus("Setting the crafter's slots at " + var4.toShortString());
         if (var2 - this.containerOpenedTick >= 6 && var2 - this.lastCrafterClickTick >= 4) {
            CrafterMenu var10 = (CrafterMenu)var5.getMenu();
            int[] var7 = var1.disabledSlots.get(var3);

            for (int var8 = 0; var8 < 9; var8++) {
               int var9 = Arrays.binarySearch(var7, var8) >= 0 ? 1 : 0;
               if (var10.isSlotDisabled(var8) != var9) {
                  if (var9 != 0 && !var10.getSlot(var8).getItem().isEmpty()) {
                     this.activeCrafterCell = null;
                     minecraft.player.closeContainer();
                     this.setAside(var3, "a crafter slot to disable holds an item");
                     return true;
                  }

                  var10.setSlotState(var8, var9 == 0);
                  minecraft.gameMode.handleSlotStateChanged(var8, var10.containerId, var9 == 0);
                  this.lastCrafterClickTick = var2;
                  return true;
               }
            }

            this.configuredCrafters.add(var3);
            this.activeCrafterCell = null;
            minecraft.player.closeContainer();
            this.lastActionTick = var2;
            LarpLog.info(this.getName(), "Crafter at {}: disabled slots {}", var4.toShortString(), Arrays.toString(var7));
            return true;
         } else {
            return true;
         }
      } else if (var2 - this.crafterOpenTick <= 50) {
         this.setStatus("Waiting for the crafter at " + var4.toShortString() + " to open");
         return true;
      } else {
         this.activeCrafterCell = null;
         this.setAside(var3, "the crafter did not open");
         return false;
      }
   }

   private int setStatus(Item var1) {
      Inventory var2 = minecraft.player.getInventory();

      for (int var3 = 0; var3 < 9; var3++) {
         if (var2.getItem(var3).is(var1)) {
            return var3;
         }
      }

      return -1;
   }

   private boolean describeTransform(int var1) {
      Inventory var2 = minecraft.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         var2.setSelectedSlot(var1);
         this.lastSelectedSlot = var1;
         this.slotSelectedTick = minecraft.player.tickCount;
         return false;
      }

      if (this.lastSelectedSlot != var1) {
         this.lastSelectedSlot = var1;
         this.slotSelectedTick = minecraft.player.tickCount;
      }

      return minecraft.player.tickCount - this.slotSelectedTick >= 3;
   }

   private void setStatus(Item var1) {
      BuildPlanner.BlockTarget var2 = this.currentTarget;
      if (!this.refillHotbar.getValue()) {
         this.clearTarget();
         this.notifyOnce("put " + shouldRelog(var1) + " in your hotbar (Refill Hotbar is off)");
         this.setAside(var2.stateId(), "not in the hotbar");
      } else if (this.formatContainers(var1) == -2) {
         this.clearTarget();
         if (this.hotbarError.startsWith("make room")) {
            this.notifyOnce(this.hotbarError);
         }

         this.setAside(var2.stateId(), this.hotbarError.startsWith("make room") ? "no hotbar room" : this.hotbarError);
      }
   }

   private int formatContainers(Item var1) {
      int var2 = this.setStatus(var1);
      if (var2 >= 0) {
         return var2;
      }

      Inventory var3 = minecraft.player.getInventory();
      int var4 = -1;

      for (int var5 = 9; var5 < 36; var5++) {
         if (var3.getItem(var5).is(var1)) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         this.hotbarError = "no " + shouldRelog(var1) + " left";
         return -2;
      }

      int var11 = -1;

      for (int var6 = 0; var6 < 9 && var11 < 0; var6++) {
         if (var3.getItem(var6).isEmpty()) {
            var11 = var6;
         }
      }

      Map var12 = this.getPlacedCount();

      for (int var7 = 0; var7 < 9 && var11 < 0; var7++) {
         ItemStack var8 = var3.getItem(var7);
         if (var8.getItem() instanceof BlockItem && !var12.containsKey(var8.getItem()) && var7 != var3.getSelectedSlot()) {
            var11 = var7;
         }
      }

      int var13 = Integer.MAX_VALUE;
      if (var11 < 0) {
         for (int var14 = 0; var14 < 9; var14++) {
            ItemStack var9 = var3.getItem(var14);
            if (var9.getItem() instanceof BlockItem && !var9.is(var1) && var14 != var3.getSelectedSlot()) {
               int var10 = var12.getOrDefault(var9.getItem(), 0);
               if (var10 < var13) {
                  var13 = var10;
                  var11 = var14;
               }
            }
         }
      }

      if (var11 < 0) {
         this.hotbarError = "make room in your hotbar for " + shouldRelog(var1) + " (an empty slot, or a block)";
         return -2;
      }

      this.moveDirection = Vec3.ZERO;
      this.sneaking = (boolean)0;
      if (minecraft.player.getDeltaMovement().horizontalDistanceSqr() > 1.0E-4) {
         this.inventoryMoveStillTick = -1000000000;
         this.setStatus("Stopping to move a block into the hotbar");
         return -1;
      }

      if (this.inventoryMoveStillTick == -1000000000) {
         this.inventoryMoveStillTick = minecraft.player.tickCount;
      }

      if (minecraft.player.tickCount - this.inventoryMoveStillTick >= 4 && minecraft.player.tickCount - this.lastInventoryMoveTick >= 4) {
         InventoryMenu var15 = minecraft.player.inventoryMenu;
         minecraft.gameMode.handleContainerInput(var15.containerId, var4, var11, ContainerInput.SWAP, minecraft.player);
         this.lastInventoryMoveTick = minecraft.player.tickCount;
         this.inventoryMoveStillTick = -1000000000;
         LarpLog.debug(this.getName(), "Moved {} from inventory slot {} to hotbar slot {}", shouldRelog(var1), var4, var11 + 1);
         this.setStatus("Moved " + shouldRelog(var1) + " into hotbar slot " + (var11 + 1));
         return -1;
      } else {
         this.setStatus("Moving " + shouldRelog(var1) + " into the hotbar");
         return -1;
      }
   }

   private Map<Item, Integer> getPlacedCount() {
      AutoBuilderModule.SchematicLayout var1 = this.layout;
      HashMap var2 = new HashMap();
      if (var1 == null) {
         return var2;
      }

      for (int var3 = 0; var3 < var1.blockCount; var3++) {
         if (var1.cellState(var3) == BlockPlacementUtil.PlacementFailure.PLACEABLE && var1.stateAt(var3) != null && !var1.isSecondHalf(var3)) {
            BlockItem var4 = BlockPlacementUtil.getPlacingItem(var1.stateAt(var3));
            if (var4 != null) {
               var2.merge(var4, 1, Integer::sum);
            }
         }
      }

      return var2;
   }

   private void walkToStand(AutoBuilderModule.SchematicLayout var1, BuildPlanner.PlacementPlan var2) {
      BlockPos var3 = this.getFeetPos();
      BuildPlanner.BlockTarget var4 = var2.toward();
      BlockPos var5 = new BlockPos(var4.blockX(), var4.blockY(), var4.blockZ());
      if (!this.pathFollower.hasRemainingNodes()
         || !var5.equals(this.routeTarget) && minecraft.player.tickCount - this.lastRouteTick > 20
         || minecraft.player.tickCount - this.lastRouteTick > 100) {
         this.lastRouteTick = minecraft.player.tickCount;
         this.routeTarget = var5;
         this.currentGroup = var2.walkingFor();
         AStarPathfinder.Bounds var6 = new AStarPathfinder.Bounds(
            Math.min(var1.minX, var3.getX()) - 10,
            Math.min(var1.minY, var3.getY()) - 6,
            Math.min(var1.minZ, var3.getZ()) - 10,
            Math.max(var1.maxX, var3.getX()) + 10,
            Math.max(var1.maxY, var3.getY()) + 6,
            Math.max(var1.maxZ, var3.getZ()) + 10
         );
         AStarPathfinder.PathResult var7 = AStarPathfinder.findPath(
            new WorldPathView(var0 -> -1, false),
            var3.getX(),
            var3.getY(),
            var3.getZ(),
            AStarPathfinder.Goal.anyOf(var2.stands(), var4.blockX(), var4.blockY() - 1, var4.blockZ(), this.getReach()),
            var6,
            AStarPathfinder.Options.walking(16000)
         );
         if (!var7.found() || var7.steps().isEmpty()) {
            this.pathFollower.reset();
            if (this.describeTransform()) {
               this.renderRestockOutlines(this.activeDoorway);
               return;
            }

            LarpLog.debug(this.getName(), "No route to stand within reach of {} ({} cells searched)", var5.toShortString(), var7.explored());
            if (this.temporaryPillars.getValue() && this.towerPhase == null && this.isManualInput(var1, var2)) {
               return;
            }

            if (this.temporaryPillars.getValue() && this.towerPhase == null && this.getReach(var1, var2.walkingFor())) {
               return;
            }

            if (this.temporaryPillars.getValue() && this.towerPhase == null && this.logLeftovers(var1, var2)) {
               return;
            }

            for (BuildPlanner.BlockTarget var13 : var2.walkingFor()) {
               this.setAside(var13.stateId(), "no way to walk within reach");
            }

            return;
         }

         if (this.finishBuild()
            && this.activeDoorway.cells().isEmpty()
            && this.savedPillar != null
            && var7.steps().stream().anyMatch(var1x -> var1x.blockY() < this.activeDoorway.outY() - 1)) {
            AStarPathfinder.PathResult var8 = AStarPathfinder.findPath(
               new WorldPathView(var0 -> -1, false),
               this.savedPillar.pillarX(),
               this.savedPillar.ground(),
               this.savedPillar.pillarZ(),
               AStarPathfinder.Goal.anyOf(var2.stands(), var4.blockX(), var4.blockY() - 1, var4.blockZ(), this.getReach()),
               var6,
               AStarPathfinder.Options.walking(16000)
            );
            if (var8.found()) {
               this.pathFollower.reset();
               LarpLog.debug(this.getName(), "The spot for {} needs a drop off the build; the tower comes down first", var5.toShortString());
               this.renderRestockOutlines(this.activeDoorway);
               return;
            }
         }

         this.pathFollower.setPath(var7.steps());
         this.pathFollower.randomizeWobble();
         this.aimHeightOffset = 0.3 + this.random.nextDouble() * 0.7;
         this.aimPhase = this.random.nextDouble() * Math.PI * 2.0;
         BlockItem var11 = this.actionByCell.getOrDefault(var4.stateId(), AutoBuilderModule.CellAction.PLACE) == AutoBuilderModule.CellAction.PLACE
            ? BlockPlacementUtil.getPlacingItem(var1.stateAt(var4.stateId()))
            : null;
         int var9 = var11 == null ? -1 : this.setStatus(var11);
         if (var9 >= 0) {
            minecraft.player.getInventory().setSelectedSlot(var9);
         }

         AStarPathfinder.Step var10 = var7.steps().getLast();
         this.routeEnd = new BlockPos(var10.blockX(), var10.blockY(), var10.blockZ());
         LarpLog.debug(this.getName(), "Route of {} steps to {} for {}", var7.steps().size(), this.routeEnd.toShortString(), var5.toShortString());
      }

      this.followPath();
   }

   private void followPath() {
      PathFollower.Result var1 = this.pathFollower.tick(this.getReach(), var0 -> -1, this.sprint.getValue());
      this.moveDirection = this.pathFollower.getMoveDirection();
      this.jumping = (boolean)(!this.pathFollower.shouldJump() && !this.shouldSprintJump() ? 0 : 1);
      this.sneaking = (boolean)(!this.pathFollower.isBraking()
            && (
               !this.safeSneak.getValue()
                  || !this.tickRelog()
                  || this.pathFollower.getRemainingPath().isEmpty()
                  || this.pathFollower.getRemainingPath().getFirst().move() != AStarPathfinder.MoveType.WALK
            )
         ? 0
         : 1);
      this.sprinting = (boolean)(this.pathFollower.shouldSprint() && !this.sneaking ? 1 : 0);
      this.setStatus("Walking to " + (this.routeTarget == null ? "the next block" : this.routeTarget.toShortString()));
      if (var1 == PathFollower.Result.ARRIVED) {
         this.pathFollower.reset();
         this.stuckCount = 0;
         this.settleCountdown = 12;
         this.stopPending = (boolean)1;
         BlockPos var2 = this.getFeetPos();
         this.skippedStands.setPending(var2.getX(), var2.getY(), var2.getZ());
      } else if (var1 == PathFollower.Result.FAILED) {
         LarpLog.debug(this.getName(), "Route stuck: {}", this.pathFollower.getRemainingPath());
         this.pathFollower.reset();
         if (this.towerPhase == AutoBuilderModule.TowerPhase.WALK_TO_PILLAR) {
            this.getMaterialNeeds("stuck on the way to it");
            return;
         }

         if (this.supportPhase == AutoBuilderModule.SupportPhase.APPROACH) {
            this.getInstance("stuck on the way to it");
            return;
         }

         if (this.supportPhase == AutoBuilderModule.SupportPhase.DISMANTLE) {
            return;
         }

         if (this.routeEnd != null) {
            this.skippedStands.add(this.routeEnd.getX(), this.routeEnd.getY(), this.routeEnd.getZ());
         }

         if (++this.stuckCount >= 3) {
            this.stuckCount = 0;

            for (BuildPlanner.BlockTarget var3 : this.currentGroup) {
               this.setAside(var3.stateId(), "stuck on the way");
            }
         }
      }
   }

   private void getReach() {
      this.settleCountdown--;
      if (!this.settling) {
         if (minecraft.player.getDeltaMovement().horizontalDistance() < 0.03) {
            this.settleCountdown = 0;
         }

         this.setStatus("Stopping");
      } else {
         if (this.settleCountdown <= 0) {
            this.settling = (boolean)0;
         }

         BlockPos var1 = this.getFeetPos();
         Vec3 var2 = Vec3.atBottomCenterOf(var1);
         Vec3 var3 = var2.subtract(minecraft.player.position()).multiply(1.0, 0.0, 1.0);
         double var4 = minecraft.player.getDeltaMovement().horizontalDistance();
         if (var3.length() < 0.12 && var4 < 0.01) {
            this.settleCountdown = 0;
            this.settling = (boolean)0;
         } else {
            double var6 = var3.lengthSqr() < 1.0E-6 ? 0.0 : minecraft.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var3.normalize());
            this.sneaking = (boolean)1;
            this.moveDirection = this.movement.updateLatch(var3.length(), var6, InteractionArbiter.ownsInteraction(this)) ? var3 : Vec3.ZERO;
            this.setStatus("Stopping on the spot");
         }
      }
   }

   private boolean shouldSprintJump() {
      int var1 = minecraft.player.tickCount;
      if (this.pathFollower.isSprintJump() && this.pathFollower.shouldSprint() && minecraft.player.isSprinting() && minecraft.player.onGround()) {
         if (!minecraft.level.noCollision(minecraft.player, minecraft.player.getBoundingBox().move(0.0, 1.3, 0.0))) {
            return false;
         }

         if (this.sprintJumpUntil != -1000000000 && var1 < this.sprintJumpUntil) {
            this.sprintJumpCount++;
            return true;
         }

         if (this.nextSprintJumpTick != -1000000000 && var1 < this.nextSprintJumpTick) {
            return false;
         }

         this.nextSprintJumpTick = var1 + 30 + this.random.nextInt(110);
         if (this.random.nextInt(100) >= 60) {
            return false;
         }

         this.sprintJumpUntil = var1 + 12 + this.random.nextInt(45);
         this.sprintJumpCount++;
         return true;
      } else {
         return false;
      }
   }

   private void canPlaceFrom() {
      float var1 = minecraft.player.getYRot();
      float var2 = Float.isNaN(this.lastYaw) ? 0.0F : Mth.wrapDegrees(var1 - this.lastYaw);
      this.lastYaw = var1;
      this.maxYawDelta = Math.max(this.maxYawDelta, Math.abs(var2));
      if (Math.abs(var2) > 26.0F && this.sharpTurnLogs < 12) {
         this.sharpTurnLogs++;
         LarpLog.info(
            this.getName(),
            "Sharp turn of {} degrees in one tick while: {} (rotation owner {})",
            String.format(Locale.ROOT, "%.1f", var2),
            this.getStatus(),
            RotationManager.hasRequest() && RotationManager.isActive(RotationManager.Priority.BOT) ? "bot" : "other"
         );
      }

      if (Math.abs(var2) > 6.0F && Math.abs(this.lastYawDelta) > 6.0F && Math.signum(var2) != Math.signum(this.lastYawDelta)) {
         this.reversalCount++;
      }

      this.lastYawDelta = var2;
   }

   private void getSchematic() {
      if (this.safeSneak.getValue() && minecraft.player.onGround() && this.tickRelog()) {
         this.sneaking = (boolean)1;
      }
   }

   private boolean tickRelog() {
      BlockPos var1 = this.getFeetPos();

      for (Direction var3 : Plane.HORIZONTAL) {
         BlockPos var4 = var1.relative(var3);
         if (this.startStocking(var4) && this.startStocking(var4.below()) && this.startStocking(var4.below(2))) {
            return true;
         }
      }

      for (int[] var5 : new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}) {
         BlockPos var6 = var1.offset(var5[0], 0, var5[1]);
         if (this.startStocking(var6) && this.startStocking(var6.below()) && this.startStocking(var6.below(2))) {
            return true;
         }
      }

      return false;
   }

   private boolean startStocking(BlockPos var1) {
      BlockState var2 = minecraft.level.getBlockState(var1);
      return var2.getCollisionShape(minecraft.level, var1).isEmpty();
   }

   private Item getMaterialNeeds() {
      Item var1 = switch ((String)this.pillarBlock.getValue()) {
         case "Cobblestone" -> Items.COBBLESTONE;
         case "Netherrack" -> Items.NETHERRACK;
         case "Cobbled Deepslate" -> Items.COBBLED_DEEPSLATE;
         default -> Items.DIRT;
      };
      return this.layout != null && getStateCounts(this.layout, var1) ? null : var1;
   }

   private static boolean getStateCounts(AutoBuilderModule.SchematicLayout var0, Item var1) {
      for (BlockState var5 : var0.paletteStates) {
         if (var5 != null && BlockPlacementUtil.getPlacingItem(var5) == var1) {
            return true;
         }
      }

      return false;
   }

   private boolean getReach(AutoBuilderModule.SchematicLayout var1, List<BuildPlanner.BlockTarget> var2) {
      if (this.supportPhase != null) {
         return false;
      }

      Item var3 = this.getMaterialNeeds();
      if (var3 == null) {
         this.notifyOnce("the build uses " + this.pillarBlock.getValue().toLowerCase(Locale.ROOT) + "; pick another Pillar Block for temporary pillars");
         return false;
      }

      BlockPos var4 = this.getFeetPos();
      if (this.placedCount > this.lastTowerEndPlaced) {
         this.recentPillarColumns.clear();
      }

      HashSet var5 = new HashSet<>(this.failedPillarColumns);
      var5.addAll(this.recentPillarColumns);
      var2 = var2.stream().filter(var1x -> !this.abandonedCells.contains(var1x.stateId())).toList();
      if (var2.isEmpty()) {
         return false;
      } else {
         BuildPlanner.Pillar var6 = BuildPlanner.findPillarRelaxed(
            new WorldPathView(var0 -> -1, false),
            var2,
            this.clearTarget(var1, 2),
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.getReach(),
            this.maxPillarHeight.getValue().intValue(),
            var1::stateAt,
            (var2x, var3x, var4x, var5x) -> this.stopRecording(var1, var2x, var3x, var4x, var5x),
            var5,
            600
         );
         if (var6 == null) {
            LarpLog.debug(this.getName(), "No pillar reaches {} blocks", var2.size());
            this.getSchematic(var1, (BuildPlanner.BlockTarget)var2.getFirst());
            return false;
         } else if (this.countItem(var3) < var6.height()) {
            this.neededPillarBlocks = var6.height();
            return true;
         } else {
            this.activePillar = var6;
            this.towerPhase = AutoBuilderModule.TowerPhase.WALK_TO_PILLAR;
            this.pillarWalkStartTick = this.pillarPhaseTick = minecraft.player.tickCount;
            this.pathFollower.reset();
            this.currentGroup = List.of();
            LarpLog.debug(this.getName(), "Planned a pillar of {} at {} {} {}", var6.height(), var6.pillarX(), var6.ground(), var6.pillarZ());
            return true;
         }
      }
   }

   private void getSchematic(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2) {
      if (this.diagnosedCells.add(var2.stateId())) {
         BlockState var3 = var1.stateAt(var2.stateId());
         ItemStack var4 = this.getSchematicsDir(BlockPlacementUtil.getPlacingItem(var3));
         if (!var4.isEmpty()) {
            WorldPathView var5 = new WorldPathView(var0 -> -1, false);
            BlockPos var6 = new BlockPos(var2.blockX(), var2.blockY(), var2.blockZ());
            int var7 = (int)Math.floor(this.getReach());
            int var8 = this.maxPillarHeight.getValue().intValue();
            TreeMap var9 = new TreeMap();
            String var10 = null;

            for (int var11 = -var7; var11 <= var7; var11++) {
               for (int var12 = -var7; var12 <= var7; var12++) {
                  if ((var11 != 0 || var12 != 0) && !(var11 * var11 + var12 * var12 > this.getReach() * this.getReach())) {
                     int var13 = var2.blockX() + var11;
                     int var14 = var2.blockZ() + var12;
                     int var15 = Integer.MIN_VALUE;

                     for (int var16 = var2.blockY(); var16 >= var2.blockY() - var8 - 3; var16--) {
                        if (var5.loaded(var13, var16, var14)
                           && var5.floor(var13, var16 - 1, var14)
                           && var5.open(var13, var16, var14)
                           && var5.open(var13, var16 + 1, var14)) {
                           var15 = var16;
                           break;
                        }
                     }

                     if (var15 == Integer.MIN_VALUE) {
                        var9.merge("no ground", 1, Integer::sum);
                     } else {
                        for (int var24 = 1; var24 <= Math.min(var8, var2.blockY() - var15 + 1); var24++) {
                           if (!BuildPlanner.isShaftOpen(var5, var13, var15, var14, var24, var1::stateAt, var2.blockY())) {
                              var9.merge("column taken", 1, Integer::sum);
                              if (var10 == null) {
                                 for (int var26 = var15; var26 <= var15 + var24 + 1 && var10 == null; var26++) {
                                    if (!var5.open(var13, var26, var14) || var1.stateAt(var13, var26, var14)) {
                                       var10 = var13
                                          + " "
                                          + var26
                                          + " "
                                          + var14
                                          + (var5.open(var13, var26, var14) ? " (a cell of the build)" : " (solid)")
                                          + " from ground "
                                          + var15;
                                    }
                                 }
                              }
                              break;
                           }

                           String var17 = BlockPlacementUtil.getPlacementFailureReason(
                              this, minecraft.player, var4, var6, var3, new Vec3(var13 + 0.5, var15 + var24 + 1.62, var14 + 0.5), this.getReach() - 0.3
                           );
                           var9.merge(var17 == null ? "would work" : var17, 1, Integer::sum);
                        }
                     }
                  }
               }
            }

            StringBuilder var19 = new StringBuilder();
            if (BlockPlacementUtil.isDirectional(var3)) {
               Direction var20 = (Direction)var3.getValue(BlockStateProperties.FACING);
               var19.append("; below ").append(minecraft.level.getBlockState(var6.below()).getBlock().getName().getString());

               for (int var21 = 1; var21 <= 4; var21++) {
                  int var22 = var2.blockX() + var20.getStepX() * var21;
                  int var23 = var2.blockZ() + var20.getStepZ() * var21;
                  var19.append("; ")
                     .append(var21)
                     .append(" out: ")
                     .append(minecraft.level.getBlockState(new BlockPos(var22, var2.blockY(), var23)).getBlock().getName().getString());

                  for (int var25 = var2.blockY(); var25 >= var2.blockY() - var8 - 3; var25--) {
                     if (var5.loaded(var22, var25, var23)
                        && var5.floor(var22, var25 - 1, var23)
                        && var5.open(var22, var25, var23)
                        && var5.open(var22, var25 + 1, var23)) {
                        for (int var27 = 0; var27 <= Math.min(var8, var2.blockY() - var25 + 1); var27++) {
                           String var18 = BlockPlacementUtil.getPlacementFailureReason(
                              this, minecraft.player, var4, var6, var3, new Vec3(var22 + 0.5, var25 + var27 + 1.62, var23 + 0.5), this.getReach() - 0.3
                           );
                           var19.append(" h")
                              .append(var27)
                              .append('=')
                              .append(var18 == null ? "ok" : var18)
                              .append(BuildPlanner.isShaftOpen(var5, var22, var25, var23, Math.max(1, var27), var1::stateAt, var2.blockY()) ? "" : "(taken)");
                        }
                        break;
                     }
                  }
               }
            }

            if (var10 != null) {
               var19.append("; first column taken at ").append(var10);
            }

            LarpLog.debug(this.getName(), "Why no tower for {} at {}: {}{}", var3.getBlock().getName().getString(), var6.toShortString(), var9, var19);
         }
      }
   }

   private boolean stopRecording(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2, int var3, int var4, int var5) {
      return (!this.towerCells.contains(var2.stateId()) || !var1.bounds().containsColumn(var3, var5)) && this.canPlaceFrom(var1, var2, var3, var4, var5);
   }

   private boolean logLeftovers(AutoBuilderModule.SchematicLayout var1, BuildPlanner.PlacementPlan var2) {
      if (this.supportPhase == null && this.activeDoorway == null) {
         Item var3 = this.getMaterialNeeds();
         if (var3 == null) {
            return false;
         }

         if (this.placedCount > this.lastTowerEndPlaced) {
            this.recentPillarColumns.clear();
         }

         HashSet var4 = new HashSet<>(this.failedPillarColumns);
         var4.addAll(this.recentPillarColumns);
         BuildPlanner.Pillar var5 = BuildPlanner.findPillarFromCells(
            new WorldPathView(var0 -> -1, false), var2.stands(), var2.walkingFor(), this.maxPillarHeight.getValue().intValue(), var1::stateAt, var4
         );
         if (var5 == null) {
            return false;
         } else if (this.countItem(var3) < var5.height()) {
            this.neededPillarBlocks = var5.height();
            return true;
         } else {
            this.activePillar = var5;
            this.towerPhase = AutoBuilderModule.TowerPhase.WALK_TO_PILLAR;
            this.pillarWalkStartTick = this.pillarPhaseTick = minecraft.player.tickCount;
            this.pathFollower.reset();
            this.currentGroup = List.of();
            this.activeDoorway = new BuildPlanner.Breakout(List.of(), var5.pillarX(), var5.topY(), var5.pillarZ(), List.copyOf(var2.walkingFor()), Set.of());
            this.doorways.add(this.activeDoorway);
            LarpLog.info(
               this.getName(),
               "Tower of {} at {} {} {} leads across the build to spots the ground cannot reach",
               var5.height(),
               var5.pillarX(),
               var5.ground(),
               var5.pillarZ()
            );
            return true;
         }
      } else {
         return false;
      }
   }

   private List<BuildPlanner.BlockTarget> clearTarget(AutoBuilderModule.SchematicLayout var1, int var2) {
      ArrayList var3 = new ArrayList();
      MutableBlockPos var4 = new MutableBlockPos();
      int var5 = Math.min(this.currentLayer + var2, var1.layerOffsets.length - 1);

      for (int var6 = var1.layerOffsets[this.currentLayer]; var6 < var1.layerOffsets[var5]; var6++) {
         int var7 = var1.indicesByY[var6];
         if (var1.stateAt(var7) != null
            && var1.unplaceableReasons[var1.paletteIndices[var7]] == null
            && !var1.isSecondHalf(var7)
            && !this.givenUpCells.contains(var7)
            && !this.keptOpenCells.contains(var7)
            && this.getCellState(var1, var7, var4) == BlockPlacementUtil.PlacementFailure.PLACEABLE) {
            var3.add(new BuildPlanner.BlockTarget(var4.getX(), var4.getY(), var4.getZ(), var7));
         }
      }

      return var3;
   }

   private boolean getInstance(AutoBuilderModule.SchematicLayout var1, WorldPathView var2, BlockPos var3, double var4) {
      ArrayList var6 = new ArrayList();
      int var7 = var1.minY + this.currentLayer;

      for (BuildPlanner.BlockTarget var9 : this.clearTarget(var1, 65)) {
         if (var9.blockY() > var7) {
            var6.add(var9);
         }
      }

      for (int var12 = var7 + 1; var12 <= var7 + 64; var12++) {
         int var13 = var12;
         List var10 = this.stopRecording(var1, var6.stream().filter(var1x -> var1x.blockY() == var13).toList());
         if (!var10.isEmpty()) {
            BuildPlanner.PlacementPlan var11 = BuildPlanner.planPlacement(
               var2,
               var10,
               this.towerCells,
               var3.getX(),
               var3.getY(),
               var3.getZ(),
               var4,
               var1.bounds(),
               var2x -> this.isPlaceable(var1, var2x),
               (var2x, var3x, var4x, var5) -> this.findPlaceHit(var1, var2x) != null,
               (var2x, var3x, var4x, var5) -> this.canPlaceFrom(var1, var2x, var3x, var4x, var5),
               120,
               false,
               this.skippedStands.getSkipped(),
               this.lastPlaced,
               false
            );
            if (var11.place() != null) {
               this.actionByCell.put(var11.place().stateId(), AutoBuilderModule.CellAction.PLACE);
               this.clearTarget(var1, var11.place());
               return true;
            }
         }
      }

      return false;
   }

   private boolean shouldRelog(AutoBuilderModule.SchematicLayout var1) {
      Item var2 = this.getMaterialNeeds();
      if (var2 != null && this.temporaryPillars.getValue()) {
         Block var3 = blockOf(var2);
         BlockPos var4 = this.getFeetPos();
         ArrayList var5 = new ArrayList();

         BlockPos var6;
         for (var6 = var4.below();
            var5.size() < 16
               && minecraft.level.getBlockState(var6).is(var3)
               && !var1.stateAt(var6.getX(), var6.getY(), var6.getZ())
               && this.getReach(var6) >= 2;
            var6 = var6.below()
         ) {
            var5.add(var6);
         }

         if (var5.size() >= 3 && !this.startStocking(var6)) {
            Collections.reverse(var5);
            this.pillarBlocks.clear();
            this.pillarBlocks.addAll(var5);
            this.activePillar = new BuildPlanner.Pillar(var4.getX(), var6.getY() + 1, var4.getZ(), var5.size(), List.of());
            this.towerPhase = AutoBuilderModule.TowerPhase.TOP;
            this.towerPlacedMark = this.placedCount;
            this.towerStartPlaced = this.placedCount;
            this.sendChat("you are standing on a tower left from an earlier run; it is used and then dug away.");
            LarpLog.info(this.getName(), "Took over the leftover tower of {} at {}", var5.size(), ((BlockPos)var5.getFirst()).toShortString());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int getReach(BlockPos var1) {
      int var2 = 0;

      for (Direction var4 : Plane.HORIZONTAL) {
         if (this.startStocking(var1.relative(var4))) {
            var2++;
         }
      }

      return var2;
   }

   private boolean progressPercent() {
      BlockPos var1 = this.getFeetPos();
      return !BuildPlanner.findColumn(
         new WorldPathView(var0 -> -1, false),
         var1.getX(),
         var1.getY(),
         var1.getZ(),
         new LayerScanPlan.Region(var1.getX() - 3, var1.getY() - 3, var1.getZ() - 3, var1.getX() + 3, var1.getY() + 3, var1.getZ() + 3)
      );
   }

   private boolean isRunning(AutoBuilderModule.SchematicLayout var1, List<BuildPlanner.BlockTarget> var2) {
      Item var3 = this.getMaterialNeeds();
      if (var3 != null && this.activePillar != null && !var2.isEmpty()) {
         int var4 = BuildPlanner.extendPillarHeight(
            new WorldPathView(var0 -> -1, false),
            var2,
            this.activePillar,
            this.maxPillarHeight.getValue().intValue(),
            this.countItem(var3),
            var1::stateAt,
            (var2x, var3x, var4x, var5) -> this.stopRecording(var1, var2x, var3x, var4x, var5)
         );
         if (var4 == 0) {
            return false;
         }

         LarpLog.info(
            this.getName(),
            "Raising the pillar at {} {} from {} to {} for the next blocks",
            this.activePillar.pillarX(),
            this.activePillar.pillarZ(),
            this.activePillar.height(),
            var4
         );
         this.activePillar = new BuildPlanner.Pillar(
            this.activePillar.pillarX(), this.activePillar.ground(), this.activePillar.pillarZ(), var4, this.activePillar.unlocks()
         );
         this.towerPhase = AutoBuilderModule.TowerPhase.ASCEND;
         this.pillarPhaseTick = minecraft.player.tickCount;
         return true;
      } else {
         return false;
      }
   }

   private boolean getPlacedCount(AutoBuilderModule.SchematicLayout var1, List<BuildPlanner.BlockTarget> var2) {
      Item var3 = this.getMaterialNeeds();
      if (var3 != null && this.walkwayBlocks.size() < 24 && this.countItem(var3) != 0) {
         if (this.placedCount != this.walkwayAttemptMark) {
            this.walkwayAttemptMark = this.placedCount;
            this.walkwayAttempts = 0;
         }

         if (this.walkwayAttempts >= 3) {
            return false;
         }

         this.walkwayAttempts++;
         BlockPos var4 = this.getFeetPos();
         ArrayList var5 = new ArrayList();

         for (BuildPlanner.BlockTarget var7 : this.clearTarget(var1, 2)) {
            if (this.isPlaceable(var1, var7)) {
               var5.add(var7);
            }
         }

         HashSet var10 = new HashSet();

         for (BlockPos var8 : this.walkwayBlocks) {
            var10.add(AStarPathfinder.packPos(var8.getX(), 0, var8.getZ()));
         }

         if (this.activePillar != null) {
            var10.add(AStarPathfinder.packPos(this.activePillar.pillarX(), 0, this.activePillar.pillarZ()));
         }

         int[] var12 = BuildPlanner.withBlockPlaced(
            new WorldPathView(var0 -> -1, false),
            var5,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            3,
            this.getReach(),
            var1::stateAt,
            (var2x, var3x, var4x, var5x) -> this.stopRecording(var1, var2x, var3x, var4x, var5x),
            var10
         );
         if (var12 == null) {
            return false;
         }

         for (Direction var9 : Plane.HORIZONTAL) {
            if (var9.getStepX() == var12[0] && var9.getStepZ() == var12[1]) {
               this.walkwayDirection = var9;
            }
         }

         this.walkwayTarget = var4.below().relative(this.walkwayDirection);
         this.towerPhase = AutoBuilderModule.TowerPhase.WALKWAY_PLACE;
         this.walkwayTick = minecraft.player.tickCount;
         LarpLog.info(
            this.getName(),
            "Stepping {} along the top instead of digging down, {} blocks still to place up here ({} walkway blocks so far)",
            this.walkwayDirection.getName(),
            var5.size(),
            this.walkwayBlocks.size()
         );
         return true;
      } else {
         return false;
      }
   }

   private void getCellState(String var1) {
      LarpLog.info(this.getName(), "Walkway stopped: {}", var1);
      NativePlacement.cancel(this);
      this.walkwayTarget = null;
      this.towerPhase = AutoBuilderModule.TowerPhase.TOP;
      this.finishBuild(var1);
   }

   private void isRunning() {
      Item var1 = this.getMaterialNeeds();
      if (var1 == null || minecraft.player.tickCount - this.walkwayTick > 120) {
         this.getCellState("a walkway block did not go down");
      } else if (!NativePlacement.isReplaceable(this.walkwayTarget)) {
         this.walkwayBlocks.add(this.walkwayTarget);
         this.towerPhase = AutoBuilderModule.TowerPhase.WALKWAY_STEP;
         this.walkwayTick = minecraft.player.tickCount;
      } else {
         this.sneaking = (boolean)1;
         if (minecraft.player.onGround()) {
            int var2 = this.formatContainers(var1);
            if (var2 == -2) {
               this.getCellState("no " + shouldRelog(var1) + " left");
            } else if (var2 >= 0 && this.describeTransform(var2)) {
               BlockPos var3 = this.walkwayTarget.relative(this.walkwayDirection.getOpposite());
               Vec3 var4 = Vec3.atCenterOf(var3);
               double var5 = this.walkwayDirection.getStepX();
               double var7 = this.walkwayDirection.getStepZ();
               double var9 = (minecraft.player.getX() - var4.x) * var5 + (minecraft.player.getZ() - var4.z) * var7;
               double var11 = var7 != 0.0 ? var4.x - minecraft.player.getX() : 0.0;
               double var13 = var5 != 0.0 ? var4.z - minecraft.player.getZ() : 0.0;
               this.moveDirection = var9 < 0.66 ? new Vec3(var5 + var11, 0.0, var7 + var13) : new Vec3(var11, 0.0, var13);
               Vec3 var15 = minecraft.player.getEyePosition();
               Vec3 var16 = var4.add(var5 * 0.5, 0.3, var7 * 0.5);
               float var17 = this.walkwayDirection.getOpposite().toYRot();
               float var18 = (float)Math.toDegrees(Math.atan2(var15.y - var16.y, Math.max(var9 - 0.5, 0.08)));
               this.aimPlacement(var17, var18);
               this.setStatus("Laying a walkway block");
               if (!(var9 < 0.54)) {
                  double var19 = this.getReach();
                  BlockHitResult var21 = NativePlacement.raycastFromOrigin(this, var15, var3, Vec3.directionFromRotation(var18, var17), var19);
                  if (var21 != null && var21.getDirection() == this.walkwayDirection) {
                     BlockPos var22 = this.walkwayTarget;
                     NativePlacement.matchesItem(
                        this,
                        var22,
                        var21,
                        var1,
                        var19,
                        true,
                        () -> NativePlacement.isReplaceable(var22) && !this.isManualInput(),
                        () -> NativePlacement.matchesItem(this, var3, var19)
                           && BlockPlacementUtil.isFacingHitFace(minecraft.player, var21)
                           && BlockPlacementUtil.isSupportBlock(minecraft.player, var3),
                        var1x -> this.lastActionTick = minecraft.player.tickCount
                     );
                  }
               }
            } else {
               this.setStatus("Taking out " + shouldRelog(var1) + " for the walkway");
            }
         }
      }
   }

   private void stop() {
      if (minecraft.player.tickCount - this.walkwayTick > 120) {
         this.getCellState("could not step onto the walkway");
      } else {
         this.sneaking = (boolean)1;
         this.setStatus("Stepping along the walkway");
         Vec3 var1 = Vec3.atBottomCenterOf(this.walkwayTarget.above()).subtract(minecraft.player.position()).multiply(1.0, 0.0, 1.0);
         if (var1.length() < 0.25) {
            this.walkwayTarget = null;
            this.towerPhase = AutoBuilderModule.TowerPhase.TOP;
            this.towerPlacedMark = this.placedCount;
            this.skippedStands.reset();
         } else {
            this.moveDirection = var1;
         }
      }
   }

   private void isManualInput() {
      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 == null) {
         this.walkwayBlocks.clear();
         this.towerPhase = AutoBuilderModule.TowerPhase.DESCEND;
      } else if (this.walkwayBlocks.isEmpty()) {
         BlockBreaker.release(this);
         this.towerPhase = AutoBuilderModule.TowerPhase.DESCEND;
         this.pillarPhaseTick = minecraft.player.tickCount;
      } else if (minecraft.player.tickCount - this.pillarPhaseTick <= 80) {
         this.sneaking = (boolean)1;
         Item var2 = this.getMaterialNeeds();
         Block var3 = blockOf(var2 == null ? Items.DIRT : var2);
         BlockPos var4 = this.walkwayBlocks.getLast();
         BlockPos var5 = this.walkwayBlocks.size() >= 2
            ? this.walkwayBlocks.get(this.walkwayBlocks.size() - 2)
            : (this.pillarBlocks.isEmpty() ? null : this.pillarBlocks.getLast());
         if (var5 == null) {
            this.walkwayBlocks.clear();
         } else {
            Vec3 var6 = Vec3.atBottomCenterOf(var5.above()).subtract(minecraft.player.position()).multiply(1.0, 0.0, 1.0);
            if (minecraft.player.onGround() && minecraft.player.getY() < var5.getY() + 0.5) {
               BlockBreaker.release(this);
               LarpLog.info(
                  this.getName(),
                  "Fell off the walkway at {} with {} blocks left; sweeping them and the tower up instead",
                  this.getFeetPos().toShortString(),
                  this.walkwayBlocks.size()
               );
               this.isManualInput(this.walkwayBlocks);
               this.isManualInput(this.pillarBlocks);
               this.activePillar = null;
               this.towerPhase = null;
            } else if (var6.length() > 0.25) {
               BlockBreaker.release(this);
               this.setStatus("Walking back along the walkway");
               this.moveDirection = var6;
            } else if (minecraft.level.getBlockState(var4).is(var3) && !var1.stateAt(var4.getX(), var4.getY(), var4.getZ())) {
               if (!this.isAggressiveActive(var4)) {
                  this.setStatus("Cannot see the walkway block at " + var4.toShortString());
               }
            } else {
               this.walkwayBlocks.removeLast();
               this.pillarPhaseTick = minecraft.player.tickCount;
            }
         }
      } else {
         BlockBreaker.release(this);
         LarpLog.info(
            this.getName(),
            "Walkway could not be taken up from the top with {} blocks left at {}; sweeping them and the tower up instead",
            this.walkwayBlocks.size(),
            this.walkwayBlocks.getFirst().toShortString()
         );
         LarpLog.info(
            this.getName(),
            "Walkway stall: player {}, next block {}, on ground {}, speed {}, rotation owner other than the bot {}, keys tick {}, screen {}",
            minecraft.player.position(),
            this.walkwayBlocks.getLast().toShortString(),
            minecraft.player.onGround(),
            minecraft.player.getDeltaMovement().horizontalDistance(),
            RotationManager.hasRequest() && !RotationManager.isActive(RotationManager.Priority.BOT),
            this.lastInputTick == minecraft.player.tickCount,
            minecraft.gui.screen() != null
         );
         this.isManualInput(this.walkwayBlocks);
         this.isManualInput(this.pillarBlocks);
         this.activePillar = null;
         this.towerPhase = null;
      }
   }

   private boolean turnSchematic(AutoBuilderModule.SchematicLayout var1) {
      Item var2 = this.getMaterialNeeds();
      if (var2 == null || this.countItem(var2) >= this.neededPillarBlocks) {
         this.neededPillarBlocks = 0;
         return false;
      }

      if (this.restockFromContainers.getValue() && this.isRunning(var1)) {
         return true;
      }

      if (this.buyMissingBlocks.getValue() && !this.outOfMoney && !this.unbuyableItems.contains(var2) && this.cycleSchematic(var1)) {
         return true;
      }

      this.stop(
         "out of "
            + shouldRelog(var2)
            + " for temporary blocks; carry at least "
            + this.neededPillarBlocks
            + (this.buyMissingBlocks.getValue() ? "" : " or turn on Buy Missing Blocks")
      );
      return true;
   }

   private boolean walkToStand(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2) {
      BlockState var3 = var1.stateAt(var2.stateId());
      BlockPos var4 = new BlockPos(var2.blockX(), var2.blockY(), var2.blockZ());
      return var3.getBlock() instanceof FenceGateBlock
         ? true
         : !var3.hasBlockEntity()
            && !BlockPlacementUtil.isUsableBlock(var3)
            && !BlockPlacementUtil.hasOrientationProperty(var3)
            && !BlockPlacementUtil.countInteractionsNeeded(var3)
            && var3.isCollisionShapeFullBlock(minecraft.level, var4);
   }

   private boolean getCellState(AutoBuilderModule.SchematicLayout var1, List<BuildPlanner.BlockTarget> var2) {
      Item var3 = this.getMaterialNeeds();
      if (var3 != null && !this.isStaffNearby(((BuildPlanner.BlockTarget)var2.getFirst()).blockY())) {
         BlockPos var4 = this.getFeetPos();
         BuildPlanner.Column var5 = BuildPlanner.findColumn(
            new WorldPathView(var0 -> -1, false),
            var2,
            var1::stateAt,
            (var0, var1x, var2x) -> BlockPlacementUtil.hasSupportNeighbor(new BlockPos(var0, var1x, var2x)),
            var2x -> this.walkToStand(var1, var2x),
            this.skippedSupportSeeds,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            var2x -> this.getMaterialNeeds(var1, var2x)
         );
         if (var5 == null) {
            return false;
         } else if (this.countItem(var3) < var5.height()) {
            this.neededPillarBlocks = var5.height();
            return true;
         } else {
            this.activeSupport = var5;
            this.supportPhase = AutoBuilderModule.SupportPhase.APPROACH;
            this.supportWalkStartTick = this.supportActionTick = minecraft.player.tickCount;
            this.supportBlocks.clear();
            this.pathFollower.reset();
            this.currentGroup = List.of();
            LarpLog.info(
               this.getName(),
               "Nothing holds layer Y {} up: {} temporary {} at {} {} {} to start it",
               var5.seed().blockY(),
               var5.height(),
               var5.height() == 1 ? "block" : "blocks",
               var5.columnX(),
               var5.ground(),
               var5.columnZ()
            );
            return true;
         }
      } else {
         return false;
      }
   }

   private boolean tickRelog(List<BuildPlanner.BlockTarget> var1) {
      int var2 = minecraft.player.tickCount;
      if (this.placedCount != this.lastProgressCount || var2 - this.lastProgressTick > 5) {
         this.lastProgressCount = this.placedCount;
         this.progressSinceTick = var2;
      }

      this.lastProgressTick = var2;
      if (var2 - this.progressSinceTick <= 1200) {
         return false;
      }

      this.progressSinceTick = var2;
      LarpLog.info(this.getName(), "No block went down for {} ticks ({}); setting aside what it was trying", 1200, this.towerPhase);
      if (var1 != null) {
         for (BuildPlanner.BlockTarget var4 : var1) {
            this.setAside(var4.stateId(), "no progress");
         }
      }

      this.clearTarget();
      this.pathFollower.reset();
      if (this.activePillar != null
         && this.towerPhase != null
         && this.towerPhase != AutoBuilderModule.TowerPhase.DESCEND
         && this.towerPhase != AutoBuilderModule.TowerPhase.WALKWAY_BREAK
         && this.towerPhase != AutoBuilderModule.TowerPhase.WALK_TO_PILLAR) {
         this.finishBuild("no block went down for a minute");
      }

      return true;
   }

   private boolean isManualInput(AutoBuilderModule.SchematicLayout var1) {
      if (this.getCellState(var1, this.wrongFacingCell, new MutableBlockPos()) == BlockPlacementUtil.PlacementFailure.STATE_MISMATCH
         && minecraft.player.tickCount - this.wrongFacingTick <= 400) {
         this.setStatus("Breaking a block that faces the wrong way");
         return this.isAggressiveActive(this.wrongFacingPos);
      } else {
         BlockBreaker.release(this);
         this.wrongFacingPos = null;
         return false;
      }
   }

   private boolean isStaffNearby(int var1) {
      if (this.placedCount > this.supportFailureMark) {
         this.supportFailures.clear();
      }

      return this.supportFailures.getOrDefault(var1, 0) >= 2;
   }

   private void followPath(int var1) {
      if (this.placedCount > this.supportFailureMark) {
         this.supportFailures.clear();
      }

      this.supportFailureMark = this.placedCount;
      if (this.supportFailures.merge(var1, 1, Integer::sum) == 2) {
         LarpLog.info(this.getName(), "Two temporary supports in a row gave nothing at Y {}; no more there until a block of the build goes down", var1);
      }
   }

   private boolean getMaterialNeeds(AutoBuilderModule.SchematicLayout var1, BuildPlanner.Column var2) {
      BlockPos var3 = this.getFeetPos();
      BuildPlanner.BlockTarget var4 = var2.seed();
      if (this.renderPreview(var2, var3.getX(), var3.getY(), var3.getZ(), this.towerPhase == AutoBuilderModule.TowerPhase.TOP)) {
         return true;
      }

      if (this.towerPhase == AutoBuilderModule.TowerPhase.TOP) {
         return false;
      }

      WorldPathView var5 = new WorldPathView(var0 -> -1, false);
      Set var6 = BuildPlanner.findStandingSpots(
         var5, var4, this.getReach(), var1.bounds(), false, (var2x, var3x, var4x, var5x) -> this.renderPreview(var2, var3x, var4x, var5x, false), Set.of()
      );
      if (var6.isEmpty()) {
         return false;
      }

      AStarPathfinder.Bounds var7 = new AStarPathfinder.Bounds(
         Math.min(var1.minX, var3.getX()) - 10,
         Math.min(var1.minY, var3.getY()) - 6,
         Math.min(var1.minZ, var3.getZ()) - 10,
         Math.max(var1.maxX, var3.getX()) + 10,
         Math.max(var1.maxY, var3.getY()) + 6,
         Math.max(var1.maxZ, var3.getZ()) + 10
      );
      return AStarPathfinder.findPath(
            var5,
            var3.getX(),
            var3.getY(),
            var3.getZ(),
            AStarPathfinder.Goal.anyOf(var6, var4.blockX(), var4.blockY() - 1, var4.blockZ(), this.getReach()),
            var7,
            AStarPathfinder.Options.walking(16000)
         )
         .found();
   }

   private boolean renderPreview(BuildPlanner.Column var1, int var2, int var3, int var4, boolean var5) {
      Item var6 = this.getMaterialNeeds();
      if (var6 != null && BuildPlanner.canPlaceFromColumn(var1, var2, var3, var4)) {
         BlockState var7 = this.layout == null ? null : this.layout.stateAt(var1.seed().stateId());
         if (var7 != null && var7.getBlock() instanceof FenceGateBlock) {
            Direction var8 = (Direction)var7.getValue(BlockStateProperties.HORIZONTAL_FACING);
            if (var1.columnX() - var1.seed().blockX() != var8.getStepX() || var1.columnZ() - var1.seed().blockZ() != var8.getStepZ()) {
               return false;
            }
         }

         double var14 = this.getReach() - 0.3;
         if (BuildPlanner.reachDistanceSq(var2, var3, var4, var1.seed()) > var14 * var14) {
            return false;
         }

         if (var5 && Math.abs(var1.columnX() - var2) + Math.abs(var1.columnZ() - var4) == 1) {
            return false;
         }

         if (var5 && var1.ground() < var3) {
            double var10 = Math.hypot(var1.columnX() - var2, var1.columnZ() - var4);
            double var12 = var3 + 1.62 - var1.ground();
            if (var10 * 1.62 / var12 < 0.6) {
               return false;
            }
         }

         return this.setAside(var6, new BlockPos(var1.columnX(), var1.ground(), var1.columnZ()), new Vec3(var2 + 0.5, var3 + 1.62, var4 + 0.5), var14, true)
            != null;
      } else {
         return false;
      }
   }

   private boolean renderPreview(AutoBuilderModule.SchematicLayout var1, List<BuildPlanner.BlockTarget> var2) {
      Item var3 = this.getMaterialNeeds();
      if (var3 != null && !this.isStaffNearby(((BuildPlanner.BlockTarget)var2.getFirst()).blockY())) {
         BlockPos var4 = this.getFeetPos();
         if (this.placedCount > this.lastTowerEndPlaced) {
            this.recentPillarColumns.clear();
         }

         HashSet var5 = new HashSet<>(this.failedPillarColumns);
         var5.addAll(this.recentPillarColumns);
         BuildPlanner.Pillar var6 = BuildPlanner.findPillarForColumns(
            new WorldPathView(var0 -> -1, false),
            var2,
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.getReach(),
            this.maxPillarHeight.getValue().intValue(),
            var1::stateAt,
            (var0, var1x, var2x) -> BlockPlacementUtil.hasSupportNeighbor(new BlockPos(var0, var1x, var2x)),
            var2x -> this.walkToStand(var1, var2x),
            (var1x, var2x, var3x, var4x) -> this.renderPreview(var1x, var2x, var3x, var4x, true),
            this.skippedSupportSeeds,
            var5,
            600
         );
         if (var6 == null) {
            return false;
         } else if (this.countItem(var3) < var6.height() + 3) {
            this.neededPillarBlocks = var6.height() + 3;
            return true;
         } else {
            this.activePillar = var6;
            this.pillarForLayer = (boolean)1;
            this.supportLayerY = ((BuildPlanner.BlockTarget)var2.getFirst()).blockY();
            this.towerPhase = AutoBuilderModule.TowerPhase.WALK_TO_PILLAR;
            this.pillarWalkStartTick = this.pillarPhaseTick = minecraft.player.tickCount;
            this.pathFollower.reset();
            this.currentGroup = List.of();
            LarpLog.info(
               this.getName(),
               "Layer Y {} is too high to start from the floor: tower of {} at {} {} {} to stand on",
               ((BuildPlanner.BlockTarget)var2.getFirst()).blockY(),
               var6.height(),
               var6.pillarX(),
               var6.ground(),
               var6.pillarZ()
            );
            return true;
         }
      } else {
         return false;
      }
   }

   private boolean getInstance(AutoBuilderModule.SchematicLayout var1) {
      switch (this.supportPhase) {
         case APPROACH:
            this.describeStocking(var1);
            break;
         case BUILD:
            this.finishBuild(var1);
            break;
         case ASCEND:
            BuildPlanner.BlockTarget var2 = this.activeSupport.seed();
            int var3 = this.getCellState(var1, var2.stateId(), new MutableBlockPos()) != BlockPlacementUtil.PlacementFailure.PLACEABLE ? 1 : 0;
            if (var3 == 0
               && !this.setAsideUntil.containsKey(var2.stateId())
               && !this.givenUpCells.contains(var2.stateId())
               && minecraft.player.tickCount - this.supportWalkStartTick <= 400) {
               return false;
            }

            if (var3 == 0) {
               this.skippedSupportSeeds.add(AStarPathfinder.packPos(var2.blockX(), var2.blockY(), var2.blockZ()));
               this.followPath(var2.blockY());
            }

            this.supportPhase = AutoBuilderModule.SupportPhase.DISMANTLE;
            this.supportActionTick = minecraft.player.tickCount;
            this.shouldSprintJump(var1);
            break;
         case DISMANTLE:
            this.shouldSprintJump(var1);
      }

      return true;
   }

   private void getInstance(String var1) {
      BuildPlanner.BlockTarget var2 = this.activeSupport.seed();
      LarpLog.info(
         this.getName(),
         "Temporary support at {} {} {} left out: {}",
         this.activeSupport.columnX(),
         this.activeSupport.ground(),
         this.activeSupport.columnZ(),
         var1
      );
      this.skippedSupportSeeds.add(AStarPathfinder.packPos(var2.blockX(), var2.blockY(), var2.blockZ()));
      this.followPath(var2.blockY());
      this.pathFollower.reset();
      NativePlacement.cancel(this);
      if (this.supportBlocks.isEmpty()) {
         this.activeSupport = null;
         this.supportPhase = null;
      } else {
         this.supportPhase = AutoBuilderModule.SupportPhase.DISMANTLE;
         this.supportActionTick = minecraft.player.tickCount;
      }
   }

   private BlockPos tickRelog() {
      return new BlockPos(this.activeSupport.columnX(), this.activeSupport.ground() + this.supportBlocks.size(), this.activeSupport.columnZ());
   }

   private BlockHitResult setAside(Item var1, BlockPos var2, Vec3 var3, double var4, boolean var6) {
      ItemStack var7 = this.getSchematicsDir(var1);
      return var7.isEmpty()
         ? null
         : BlockPlacementUtil.findPlacementHitInternal(this, minecraft.player, var7, var2, blockOf(var1).defaultBlockState(), var3, var4, true, var6);
   }

   private boolean logLeftovers() {
      return minecraft.player
         .getBoundingBox()
         .intersects(
            new AABB(
               this.activeSupport.columnX(),
               this.activeSupport.ground(),
               this.activeSupport.columnZ(),
               this.activeSupport.columnX() + 1,
               this.activeSupport.top() + 1,
               this.activeSupport.columnZ() + 1
            )
         );
   }

   private void describeStocking(AutoBuilderModule.SchematicLayout var1) {
      if (minecraft.player.tickCount - this.supportWalkStartTick > 400) {
         this.getInstance("the walk took too long");
      } else {
         Item var2 = this.getMaterialNeeds();
         if (var2 == null) {
            this.getInstance("the block is used by the build");
         } else {
            BlockPos var3 = this.tickRelog();
            if (this.pathFollower.hasRemainingNodes()) {
               this.followPath();
            } else if (!this.logLeftovers() && this.setAside(var2, var3, minecraft.player.getEyePosition(), this.getReach(), false) != null) {
               this.supportPhase = AutoBuilderModule.SupportPhase.BUILD;
               this.supportActionTick = minecraft.player.tickCount;
            } else if (this.towerPhase == AutoBuilderModule.TowerPhase.TOP) {
               this.getInstance("no click from the tower's top");
            } else {
               BlockPos var4 = this.getFeetPos();
               BuildPlanner.BlockTarget var5 = this.activeSupport.seed();
               double var6 = this.getReach() - 0.3;
               Set var8 = BuildPlanner.findStandingSpots(
                  new WorldPathView(var0 -> -1, false),
                  new BuildPlanner.BlockTarget(var3.getX(), var3.getY(), var3.getZ(), -1),
                  this.getReach(),
                  var1.bounds(),
                  false,
                  (var6x, var7, var8x, var9) -> (var7 != this.activeSupport.columnX() || var9 != this.activeSupport.columnZ())
                     && BuildPlanner.reachDistanceSq(var7, var8x, var9, var5) <= var6 * var6
                     && this.setAside(var2, var3, new Vec3(var7 + 0.5, var8x + 1.62, var9 + 0.5), var6, true) != null,
                  Set.of()
               );
               var8.remove(AStarPathfinder.packPos(var4.getX(), var4.getY(), var4.getZ()));
               if (var8.isEmpty()) {
                  this.getInstance("no spot to place it from");
               } else if (!this.reset(var1, var8, var3)) {
                  this.getInstance("no way to walk to it");
               } else {
                  this.followPath();
               }
            }
         }
      }
   }

   private boolean reset(AutoBuilderModule.SchematicLayout var1, Set<Long> var2, BlockPos var3) {
      BlockPos var4 = this.getFeetPos();
      AStarPathfinder.Bounds var5 = new AStarPathfinder.Bounds(
         Math.min(var1.minX, var4.getX()) - 10,
         Math.min(var1.minY, var4.getY()) - 6,
         Math.min(var1.minZ, var4.getZ()) - 10,
         Math.max(var1.maxX, var4.getX()) + 10,
         Math.max(var1.maxY, var4.getY()) + 6,
         Math.max(var1.maxZ, var4.getZ()) + 10
      );
      AStarPathfinder.PathResult var6 = AStarPathfinder.findPath(
         new WorldPathView(var0 -> -1, false),
         var4.getX(),
         var4.getY(),
         var4.getZ(),
         AStarPathfinder.Goal.anyOf(var2, var3.getX(), var3.getY() - 1, var3.getZ(), this.getReach()),
         var5,
         AStarPathfinder.Options.walking(16000)
      );
      if (var6.found() && !var6.steps().isEmpty()) {
         this.pathFollower.setPath(var6.steps());
         this.routeTarget = var3;
         AStarPathfinder.Step var7 = var6.steps().getLast();
         this.routeEnd = new BlockPos(var7.blockX(), var7.blockY(), var7.blockZ());
         return true;
      } else {
         return false;
      }
   }

   private void finishBuild(AutoBuilderModule.SchematicLayout var1) {
      Item var2 = this.getMaterialNeeds();
      if (var2 == null) {
         this.getInstance("the block is used by the build");
      } else {
         Block var3 = blockOf(var2);

         while (!this.supportBlocks.isEmpty() && !minecraft.level.getBlockState(this.supportBlocks.getLast()).is(var3)) {
            this.supportBlocks.removeLast();
         }

         if (this.supportBlocks.size() >= this.activeSupport.height()) {
            this.supportPhase = AutoBuilderModule.SupportPhase.ASCEND;
            this.supportWalkStartTick = minecraft.player.tickCount;
            this.skippedStands.reset();
         } else if (minecraft.player.tickCount - this.supportActionTick > 40) {
            this.getInstance("it did not go down");
         } else {
            int var4 = this.formatContainers(var2);
            if (var4 == -2) {
               this.getInstance("no " + shouldRelog(var2) + " left");
            } else if (var4 >= 0 && this.describeTransform(var4)) {
               this.getSchematic();
               BlockPos var5 = this.tickRelog();
               BlockHitResult var6 = this.logLeftovers() ? null : this.setAside(var2, var5, minecraft.player.getEyePosition(), this.getReach(), false);
               if (var6 == null) {
                  this.supportPhase = AutoBuilderModule.SupportPhase.APPROACH;
               } else {
                  BlockPos var7 = var6.getBlockPos();
                  this.findPlaceHit(var7);
                  double var8 = this.getReach();
                  NativePlacement.matchesItem(
                     this,
                     var5,
                     var6,
                     var2,
                     var8,
                     true,
                     () -> NativePlacement.isReplaceable(var5) && !this.isManualInput(),
                     () -> NativePlacement.matchesItem(this, var7, var8)
                        && BlockPlacementUtil.isFacingHitFace(minecraft.player, var6)
                        && BlockPlacementUtil.isSupportBlock(minecraft.player, var7)
                        && this.finishBuild(var7),
                     var2x -> {
                        this.lastActionTick = minecraft.player.tickCount;
                        if (var2x && !this.supportBlocks.contains(var5)) {
                           this.supportBlocks.add(var5);
                           this.supportActionTick = minecraft.player.tickCount;
                        }
                     }
                  );
                  this.setStatus("Placing a temporary block at " + var5.toShortString());
               }
            } else {
               this.setStatus("Taking out " + shouldRelog(var2) + " for a temporary block");
            }
         }
      }
   }

   private void shouldSprintJump(AutoBuilderModule.SchematicLayout var1) {
      Item var2 = this.getMaterialNeeds();

      for (Block var3 = blockOf(var2 == null ? Items.DIRT : var2);
         !this.supportBlocks.isEmpty() && !minecraft.level.getBlockState(this.supportBlocks.getLast()).is(var3);
         this.supportActionTick = minecraft.player.tickCount
      ) {
         this.supportBlocks.removeLast();
      }

      if (this.supportBlocks.isEmpty()) {
         BlockBreaker.release(this);
         this.activeSupport = null;
         this.supportPhase = null;
         this.skippedStands.reset();
         this.setStatus("Temporary block removed");
      } else if (this.pathFollower.hasRemainingNodes()) {
         this.followPath();
      } else if (minecraft.player.tickCount - this.supportActionTick > 200) {
         BlockBreaker.release(this);
         LarpLog.info(
            this.getName(), "Could not dig the temporary block at {} right away; sweeping it up instead", this.supportBlocks.getFirst().toShortString()
         );
         this.isManualInput(this.supportBlocks);
         this.activeSupport = null;
         this.supportPhase = null;
      } else {
         this.getSchematic();
         BlockPos var4 = this.supportBlocks.getLast();
         if (!this.isAggressiveActive(var4)) {
            if (this.towerPhase != AutoBuilderModule.TowerPhase.TOP) {
               BlockPos var5 = this.getFeetPos();
               Set var6 = BuildPlanner.findStandingSpots(
                  new WorldPathView(var0 -> -1, false),
                  new BuildPlanner.BlockTarget(var4.getX(), var4.getY(), var4.getZ(), -1),
                  this.getReach(),
                  var1.bounds(),
                  false,
                  (var1x, var2x, var3x, var4x) -> var2x != var4.getX() || var4x != var4.getZ(),
                  Set.of()
               );
               var6.remove(AStarPathfinder.packPos(var5.getX(), var5.getY(), var5.getZ()));
               if (!var6.isEmpty() && this.reset(var1, var6, var4)) {
                  this.followPath();
               }
            }
         }
      }
   }

   private String progressPercent(AutoBuilderModule.SchematicLayout var1, BuildPlanner.BlockTarget var2) {
      BlockState var3 = var1.stateAt(var2.stateId());
      ItemStack var4 = this.getSchematicsDir(BlockPlacementUtil.getPlacingItem(var3));
      if (var4.isEmpty()) {
         return "none carried";
      }

      BlockPos var5 = new BlockPos(var2.blockX(), var2.blockY(), var2.blockZ());
      if (minecraft.player.getBoundingBox().intersects(new AABB(var5))) {
         return "the body is in the cell";
      }

      String var6 = BlockPlacementUtil.getPlacementFailureReason(this, minecraft.player, var4, var5, var3, minecraft.player.getEyePosition(), this.getReach());
      return var6 == null ? "steady look missed" : var6;
   }

   private String notifyOnce() {
      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 != null && this.activePillar != null) {
         StringBuilder var2 = new StringBuilder();
         MutableBlockPos var3 = new MutableBlockPos();

         for (BuildPlanner.BlockTarget var5 : this.activePillar.unlocks()) {
            if (this.getCellState(var1, var5.stateId(), var3) == BlockPlacementUtil.PlacementFailure.PLACEABLE) {
               String var6 = !this.setAsideUntil.containsKey(var5.stateId()) && !this.givenUpCells.contains(var5.stateId())
                  ? (this.findPlaceHit(var1, var5) == null ? "no click from here: " + this.progressPercent(var1, var5) : "click was possible")
                  : "set aside";
               var2.append(var2.isEmpty() ? "; planned but not placed: " : ", ")
                  .append(var1.stateAt(var5.stateId()).getBlock().getName().getString())
                  .append(" at ")
                  .append(var5.blockX())
                  .append(' ')
                  .append(var5.blockY())
                  .append(' ')
                  .append(var5.blockZ())
                  .append(" (")
                  .append(var6)
                  .append(')');
            }
         }

         return var2.toString();
      } else {
         return "";
      }
   }

   private void isManualInput(List<BlockPos> var1) {
      Item var2 = this.getMaterialNeeds();
      Block var3 = blockOf(var2 == null ? Items.DIRT : var2);

      for (BlockPos var5 : var1) {
         if (minecraft.level.getBlockState(var5).is(var3)
            && (this.layout == null || !this.layout.stateAt(var5.getX(), var5.getY(), var5.getZ()))
            && !this.sweepBlocks.contains(var5)) {
            this.sweepBlocks.add(var5);
         }
      }

      var1.clear();
   }

   private void setStatus(AutoBuilderModule.SchematicLayout var1) {
      Item var2 = this.getMaterialNeeds();
      Block var3 = blockOf(var2 == null ? Items.DIRT : var2);
      this.sweepBlocks.removeIf(var1x -> !minecraft.level.getBlockState(var1x).is(var3));
      if (this.sweepBlocks.isEmpty()) {
         BlockBreaker.release(this);
         this.sweepTarget = null;
      } else if (this.pathFollower.hasRemainingNodes()) {
         this.followPath();
      } else {
         if (this.sweepTarget == null || !this.sweepBlocks.contains(this.sweepTarget)) {
            this.sweepTarget = this.sweepBlocks.stream().max(Comparator.comparingInt(Vec3i::getY)).orElseThrow();
            this.sweepTargetTick = minecraft.player.tickCount;
         }

         if (minecraft.player.tickCount - this.sweepTargetTick > 160) {
            BlockBreaker.release(this);
            this.sendChat("could not reach the temporary block at " + this.sweepTarget.toShortString() + "; break it by hand.");
            LarpLog.info(this.getName(), "Sweep gave up on the temporary block at {}", this.sweepTarget.toShortString());
            BlockBreaker.dumpTrace(this.getName(), this.sweepTarget);
            this.sweepBlocks.remove(this.sweepTarget);
            this.sweepTarget = null;
         } else {
            this.setStatus("Removing the temporary blocks left standing");
            if (!this.isAggressiveActive(this.sweepTarget)) {
               BlockPos var4 = this.getFeetPos();
               BlockPos var5 = this.sweepTarget;
               Set var6 = BuildPlanner.findStandingSpots(
                  new WorldPathView(var0 -> -1, false),
                  new BuildPlanner.BlockTarget(var5.getX(), var5.getY(), var5.getZ(), -1),
                  this.getReach(),
                  var1.bounds(),
                  false,
                  (var2x, var3x, var4x, var5x) -> (var3x != var5.getX() || var5x != var5.getZ()) && this.getStateCounts(var3x, var4x, var5x, var5),
                  Set.of()
               );
               var6.remove(AStarPathfinder.packPos(var4.getX(), var4.getY(), var4.getZ()));
               if (!var6.isEmpty() && this.reset(var1, var6, var5) || !this.tickRelog(var1, var5)) {
                  if (!var6.isEmpty() && (!this.pathFollower.getRemainingPath().isEmpty() || this.reset(var1, var6, var5))) {
                     this.followPath();
                  } else {
                     this.sweepTargetTick = minecraft.player.tickCount - 160 - 1;
                  }
               }
            }
         }
      }
   }

   private boolean tickRelog(AutoBuilderModule.SchematicLayout var1, BlockPos var2) {
      Item var3 = this.getMaterialNeeds();
      if (var3 != null && this.temporaryPillars.getValue() && this.countItem(var3) != 0 && !this.towerTried.contains(var2)) {
         this.towerTried.add(var2);
         BlockPos var4 = this.getFeetPos();
         BuildPlanner.BlockTarget var5 = new BuildPlanner.BlockTarget(var2.getX(), var2.getY(), var2.getZ(), -1);
         BuildPlanner.Pillar var6 = BuildPlanner.findPillar(
            new WorldPathView(var0 -> -1, false),
            List.of(var5),
            var4.getX(),
            var4.getY(),
            var4.getZ(),
            this.getReach(),
            this.maxPillarHeight.getValue().intValue(),
            var1::stateAt,
            (var2x, var3x, var4x, var5x) -> (var3x != var2.getX() || var5x != var2.getZ()) && this.getStateCounts(var3x, var4x, var5x, var2),
            this.failedPillarColumns,
            600
         );
         if (var6 != null && this.countItem(var3) >= var6.height()) {
            this.pathFollower.reset();
            this.activePillar = new BuildPlanner.Pillar(var6.pillarX(), var6.ground(), var6.pillarZ(), var6.height(), List.of());
            this.towerPhase = AutoBuilderModule.TowerPhase.WALK_TO_PILLAR;
            this.pillarWalkStartTick = this.pillarPhaseTick = minecraft.player.tickCount;
            this.sweepFromTower = (boolean)1;
            LarpLog.info(
               this.getName(),
               "Tower of {} at {} {} {} to dig the temporary block at {} left out of reach",
               var6.height(),
               var6.pillarX(),
               var6.ground(),
               var6.pillarZ(),
               var2.toShortString()
            );
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void blockOf() {
      Item var1 = this.getMaterialNeeds();
      Block var2 = blockOf(var1 == null ? Items.DIRT : var1);
      this.sweepBlocks.removeIf(var1x -> !minecraft.level.getBlockState(var1x).is(var2));
      BlockPos var3 = this.getFeetPos();
      ArrayList var4 = new ArrayList<>(this.sweepBlocks);
      var4.sort(Comparator.<BlockPos>comparingInt(var0 -> var0.getY()).reversed());

      for (BlockPos var6 : var4) {
         if ((var6.getX() != var3.getX() || var6.getZ() != var3.getZ()) && this.getStateCounts(var3.getX(), var3.getY(), var3.getZ(), var6)) {
            if (!var6.equals(this.sweepTarget)) {
               this.sweepTarget = var6;
               this.sweepTargetTick = minecraft.player.tickCount;
            }

            if (minecraft.player.tickCount - this.sweepTargetTick > 160) {
               BlockBreaker.release(this);
               LarpLog.info(this.getName(), "Sweep gave up on the temporary block at {} from a tower", var6.toShortString());
               BlockBreaker.dumpTrace(this.getName(), var6);
               this.sendChat("could not break the temporary block at " + var6.toShortString() + "; break it by hand.");
               this.sweepBlocks.remove(var6);
               this.sweepTarget = null;
               return;
            }

            this.setStatus("Removing the temporary blocks left standing");
            if (this.isAggressiveActive(var6)) {
               return;
            }
         }
      }

      this.sweepFromTower = (boolean)0;
      this.finishBuild("the temporary blocks in reach are gone");
   }

   private boolean getStateCounts(int var1, int var2, int var3, BlockPos var4) {
      Vec3 var5 = new Vec3(var1 + 0.5, var2 + 1.62, var3 + 0.5);
      double var6 = this.getReach() - 0.3;

      for (Direction var11 : Direction.values()) {
         Vec3 var12 = Vec3.atCenterOf(var4).add(var11.getStepX() * 0.5, var11.getStepY() * 0.5, var11.getStepZ() * 0.5);
         Vec3 var13 = new Vec3(var11.getStepX(), var11.getStepY(), var11.getStepZ());
         if (!(var5.subtract(var12).dot(var13) <= 0.02)
            && !(var5.distanceToSqr(var12) > var6 * var6)
            && NativePlacement.raycastFromOrigin(this, var5, var4, var12.subtract(var5), var6) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean isAggressiveActive(BlockPos var1) {
      return BlockBreaker.breakBlock(this, var1, this.getReach(), true, 12.0F, BlockBreaker::bestToolSlot, () -> !this.isManualInput());
   }

   private void getMaterialNeeds(String var1) {
      LarpLog.debug(this.getName(), "Pillar at {} {}: {}", this.activePillar.pillarX(), this.activePillar.pillarZ(), var1);
      this.sweepFromTower = (boolean)0;
      if (this.activeDoorway != null && this.towerBlocks.isEmpty()) {
         if (this.activeDoorway.cells().isEmpty()) {
            this.doorways.remove(this.activeDoorway);
         }

         this.activeDoorway = null;
      }

      this.failedPillarColumns.add(AStarPathfinder.packPos(this.activePillar.pillarX(), 0, this.activePillar.pillarZ()));
      this.pathFollower.reset();
      NativePlacement.cancel(this);
      if (this.pillarBlocks.isEmpty()) {
         if (this.pillarForLayer) {
            this.followPath(this.supportLayerY);
            this.pillarForLayer = (boolean)0;
         }

         this.activePillar = null;
         this.towerPhase = null;
      } else {
         this.finishBuild(var1);
      }
   }

   private void isAggressiveActive(AutoBuilderModule.SchematicLayout var1) {
      BlockPos var2 = this.getFeetPos();
      BlockPos var3 = new BlockPos(this.activePillar.pillarX(), this.activePillar.ground(), this.activePillar.pillarZ());
      if (minecraft.player.tickCount - this.pillarWalkStartTick > 600) {
         this.getMaterialNeeds("the walk took too long");
      } else if (var2.equals(var3) && !this.pathFollower.hasRemainingNodes()) {
         this.towerPhase = AutoBuilderModule.TowerPhase.ASCEND;
         this.pillarPhaseTick = minecraft.player.tickCount;
         this.pillarCount++;
         this.towerStartPlaced = this.placedCount;
         LarpLog.info(
            this.getName(),
            "Pillar of {} at {} {} {} for {} blocks now, in reach of more above",
            this.activePillar.height(),
            this.activePillar.pillarX(),
            this.activePillar.ground(),
            this.activePillar.pillarZ(),
            this.activePillar.unlocks().size()
         );
         this.setStatus("Building a pillar of " + this.activePillar.height());
      } else {
         if (!this.pathFollower.hasRemainingNodes()) {
            AStarPathfinder.Bounds var4 = new AStarPathfinder.Bounds(
               Math.min(var1.minX, var2.getX()) - 10,
               Math.min(var1.minY, var2.getY()) - 6,
               Math.min(var1.minZ, var2.getZ()) - 10,
               Math.max(var1.maxX, var2.getX()) + 10,
               Math.max(var1.maxY, var2.getY()) + 6,
               Math.max(var1.maxZ, var2.getZ()) + 10
            );
            AStarPathfinder.PathResult var5 = AStarPathfinder.findPath(
               new WorldPathView(var0 -> -1, false),
               var2.getX(),
               var2.getY(),
               var2.getZ(),
               AStarPathfinder.Goal.anyOf(Set.of(AStarPathfinder.packPos(var3.getX(), var3.getY(), var3.getZ())), var3.getX(), var3.getY(), var3.getZ(), 0.0),
               var4,
               AStarPathfinder.Options.walking(16000)
            );
            if (!var5.found() || var5.steps().isEmpty()) {
               this.getMaterialNeeds("no way to walk to it");
               return;
            }

            this.pathFollower.setPath(var5.steps());
            this.routeTarget = var3;
            this.routeEnd = var3;
         }

         this.followPath();
      }
   }

   private void cycleSchematic(AutoBuilderModule.SchematicLayout var1) {
      Item var2 = this.getMaterialNeeds();
      int var3 = this.formatContainers();
      if (var2 == null) {
         this.getMaterialNeeds("the pillar block is used by the build");
      } else if (var3 >= this.activePillar.height()) {
         if (!minecraft.player.onGround()) {
            this.setStatus("Landing on the pillar");
         } else {
            this.towerPhase = AutoBuilderModule.TowerPhase.TOP;
            this.towerPlacedMark = this.placedCount;
            this.skippedStands.reset();
            LarpLog.debug(this.getName(), "On a pillar of {} at {} {}", var3, this.activePillar.pillarX(), this.activePillar.pillarZ());
         }
      } else if (minecraft.player.tickCount - this.pillarPhaseTick > 80) {
         this.getMaterialNeeds("a pillar block did not go down");
      } else {
         BlockPos var4 = new BlockPos(this.activePillar.pillarX(), this.activePillar.ground() + var3, this.activePillar.pillarZ());
         BlockPos var5 = var4.below();
         int var6 = this.formatContainers(var2);
         if (var6 == -2) {
            this.getMaterialNeeds("no " + shouldRelog(var2) + " left");
         } else if (var6 >= 0 && this.describeTransform(var6)) {
            this.sneaking = (boolean)(var3 > 0 ? 1 : 0);
            Vec3 var7 = new Vec3(var4.getX() + 0.5, var4.getY(), var4.getZ() + 0.5);
            float[] var8 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var7);
            this.aimPlacement(var8[0], var8[1]);
            double var9 = this.getReach();
            if (!minecraft.player.onGround()) {
               if (!(minecraft.player.getY() < var4.getY() + 1.02)) {
                  BlockHitResult var11 = new BlockHitResult(var7, Direction.UP, var5, false);
                  NativePlacement.matchesItem(
                     this,
                     var4,
                     var11,
                     var2,
                     var9,
                     true,
                     () -> NativePlacement.isReplaceable(var4) && !this.isManualInput(),
                     () -> NativePlacement.matchesItem(this, var5, var9) && minecraft.player.getY() >= var4.getY() + 1.02,
                     var2x -> {
                        this.lastActionTick = minecraft.player.tickCount;
                        if (var2x) {
                           this.pillarBlocks.add(var4);
                           this.pillarPhaseTick = minecraft.player.tickCount;
                        }
                     }
                  );
               }
            } else {
               if (NativePlacement.matchesItem(this, var5, var9) && (var3 == 0 || minecraft.level.getBlockState(var5).is(blockOf(var2)))) {
                  this.jumping = (boolean)1;
               }

               this.setStatus("Building a pillar: " + var3 + " of " + this.activePillar.height());
            }
         } else {
            this.setStatus("Taking out " + shouldRelog(var2) + " for a pillar");
         }
      }
   }

   private static Block blockOf(Item var0) {
      return var0 instanceof BlockItem var1 ? var1.getBlock() : null;
   }

   private int formatContainers() {
      Block var1 = blockOf(this.getMaterialNeeds() == null ? Items.DIRT : this.getMaterialNeeds());

      while (!this.pillarBlocks.isEmpty() && !minecraft.level.getBlockState(this.pillarBlocks.getLast()).is(var1)) {
         this.pillarBlocks.removeLast();
      }

      return this.pillarBlocks.size();
   }

   private void finishBuild(String var1) {
      NativePlacement.cancel(this);
      this.pathFollower.reset();
      this.towerPhase = this.walkwayBlocks.isEmpty() ? AutoBuilderModule.TowerPhase.DESCEND : AutoBuilderModule.TowerPhase.WALKWAY_BREAK;
      this.pillarPhaseTick = minecraft.player.tickCount;
      if (this.activePillar != null) {
         this.recentPillarColumns.add(AStarPathfinder.packPos(this.activePillar.pillarX(), 0, this.activePillar.pillarZ()));
         this.lastTowerEndPlaced = this.placedCount;
      }

      if (this.placedCount == this.towerPlacedMark && this.activePillar != null) {
         this.failedPillarColumns.add(AStarPathfinder.packPos(this.activePillar.pillarX(), 0, this.activePillar.pillarZ()));
      }

      if (this.activePillar != null && this.layout != null && this.placedCount == this.towerPlacedMark) {
         MutableBlockPos var2 = new MutableBlockPos();

         for (BuildPlanner.BlockTarget var4 : this.activePillar.unlocks()) {
            if (var4.stateId() >= 0 && this.getCellState(this.layout, var4.stateId(), var2) == BlockPlacementUtil.PlacementFailure.PLACEABLE) {
               this.abandonedCells.add(var4.stateId());
            }
         }
      }

      if (this.pillarForLayer && this.placedCount == this.towerStartPlaced) {
         this.followPath(this.supportLayerY);
      }

      this.pillarForLayer = (boolean)0;
      if (this.activePillar != null) {
         LarpLog.info(
            this.getName(),
            "Tower at {} {} comes down after {} blocks, {} walkway blocks: {}{}",
            this.activePillar.pillarX(),
            this.activePillar.pillarZ(),
            this.placedCount - this.towerStartPlaced,
            this.walkwayBlocks.size(),
            var1,
            this.notifyOnce()
         );
      }
   }

   private void stopRecording() {
      Item var1 = this.getMaterialNeeds();

      for (Block var2 = blockOf(var1 == null ? Items.DIRT : var1);
         !this.pillarBlocks.isEmpty() && !minecraft.level.getBlockState(this.pillarBlocks.getLast()).is(var2);
         this.pillarPhaseTick = minecraft.player.tickCount
      ) {
         this.pillarBlocks.removeLast();
      }

      if (this.pillarBlocks.isEmpty()) {
         BlockBreaker.release(this);
         this.activePillar = null;
         this.towerPhase = null;
         this.skippedStands.reset();
         this.settleCountdown = 12;
         this.setStatus("Pillar removed");
      } else if (!minecraft.player.onGround() && !minecraft.player.isInWater()) {
         this.setStatus("Coming down");
      } else if (minecraft.player.tickCount - this.pillarPhaseTick > 200) {
         BlockBreaker.release(this);
         BlockPos var4 = this.pillarBlocks.getFirst();
         LarpLog.info(
            this.getName(),
            "Could not dig the pillar at {} from its top with {} blocks left; sweeping them up instead",
            var4.toShortString(),
            this.pillarBlocks.size()
         );
         this.isManualInput(this.pillarBlocks);
         this.activePillar = null;
         this.towerPhase = null;
      } else {
         this.sneaking = (boolean)1;
         BlockPos var3 = this.pillarBlocks.getLast();
         if (!this.isAggressiveActive(var3)) {
            this.startChestTrip(var3);
         }
      }
   }

   private void startChestTrip(BlockPos var1) {
      if (this.refusedBreaks.add(var1.asLong())) {
         LarpLog.info(
            this.getName(),
            "Miner refused {}: wait {}, allowed {}, breakable {}, seen {}, state {}, player {} eye {}, block above feet {}, at head {}",
            var1.toShortString(),
            this.getPauseReason(),
            InteractionArbiter.canModuleInteract(this),
            BlockBreaker.canBreak(var1),
            BlockBreaker.canBreak(this, var1, this.getReach()) != null,
            minecraft.level.getBlockState(var1).getBlock().getName().getString(),
            minecraft.player.position(),
            minecraft.player.getEyePosition(),
            minecraft.level.getBlockState(minecraft.player.blockPosition()).getBlock().getName().getString(),
            minecraft.level.getBlockState(minecraft.player.blockPosition().above()).getBlock().getName().getString()
         );
      }
   }

   private static boolean stopRecording(BlockState var0) {
      Block var1 = var0.getBlock();
      return var1 instanceof ChestBlock || var1 instanceof BarrelBlock || var1 instanceof ShulkerBoxBlock;
   }

   private String setStatus() {
      return minecraft.level.dimension().identifier().toString();
   }

   private String getFeetPos() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (minecraft.hitResult instanceof BlockHitResult var1
         && var1.getType() == Type.BLOCK
         && stopRecording(minecraft.level.getBlockState(var1.getBlockPos()))) {
         BlockPos var7 = var1.getBlockPos();
         AutoBuilderModule.ContainerPos var3 = new AutoBuilderModule.ContainerPos(this.setStatus(), var7.getX(), var7.getY(), var7.getZ());
         List var4 = getInstance(this.restockContainers.getValue());
         boolean var5 = var4.remove(var3);
         if (!var5) {
            var4.add(var3);
         }

         this.restockContainers.setText(formatContainers(var4));
         this.getStateCounts();
         String var6 = minecraft.level.getBlockState(var7).getBlock().getName().getString();
         LarpLog.info(this.getName(), "{} restock container {} at {} ({} marked)", var5 ? "Unmarked" : "Marked", var6, var7.toShortString(), var4.size());
         return (var5 ? "Unmarked the " : "Marked the ")
            + var6.toLowerCase(Locale.ROOT)
            + " at "
            + var7.toShortString()
            + " as a restock container ("
            + var4.size()
            + " marked).";
      } else {
         return "Look at a chest, barrel or shulker box first.";
      }
   }

   private String getSchematicsDir() {
      int var1 = getInstance(this.restockContainers.getValue()).size();
      this.restockContainers.setText("");
      this.getStateCounts();
      return var1 == 0 ? "No containers were marked." : "Cleared " + var1 + " restock " + (var1 == 1 ? "mark." : "marks.");
   }

   private void getInstance() {
      int var1 = this.markRestockKey.getValue() != -1
            && this.isInGame()
            && minecraft.gui.screen() == null
            && minecraft.isWindowActive()
            && BindCodes.isPressed(minecraft.getWindow().handle(), this.markRestockKey.getValue())
         ? 1
         : 0;
      if (var1 != 0 && !this.markKeyDown) {
         this.sendChat(this.getFeetPos());
      }

      this.markKeyDown = (boolean)var1;
   }

   private boolean isRunning(AutoBuilderModule.SchematicLayout var1) {
      this.restockWanted.clear();

      for (Entry var3 : this.getMaterialNeeds(var1).entrySet()) {
         int var4 = (Integer)var3.getValue() - this.countItem((Item)var3.getKey());
         if (var4 > 0) {
            this.restockWanted.put((Item)var3.getKey(), var4);
         }
      }

      Item var11 = this.temporaryPillars.getValue() ? this.getMaterialNeeds() : null;
      if (this.neededPillarBlocks > 0 && var11 != null && this.countItem(var11) < 32) {
         this.restockWanted.put(var11, 32 - this.countItem(var11));
      }

      if (this.restockWanted.isEmpty()) {
         return false;
      }

      if (var11 != null && this.countItem(var11) < 32) {
         this.restockWanted.put(var11, 32 - this.countItem(var11));
      }

      AutoBuilderModule.ContainerPos var12 = null;
      double var13 = this.restockDistance.getValue() * this.restockDistance.getValue();
      String var6 = this.setStatus();

      for (AutoBuilderModule.ContainerPos var8 : getInstance(this.restockContainers.getValue())) {
         if (var8.dimension().equals(var6) && !this.emptyContainers.contains(var8)) {
            double var9 = minecraft.player.distanceToSqr(Vec3.atCenterOf(var8.pos()));
            if (!(var9 > var13)) {
               if (minecraft.level.hasChunkAt(var8.pos()) && !stopRecording(minecraft.level.getBlockState(var8.pos()))) {
                  this.emptyContainers.add(var8);
               } else {
                  var12 = var8;
                  var13 = var9;
               }
            }
         }
      }

      if (var12 == null) {
         return false;
      }

      this.restockTarget = var12;
      this.restockPhase = AutoBuilderModule.RestockPhase.WALK_TO_CONTAINER;
      this.restockTick = minecraft.player.tickCount;
      this.restockTaken = this.restockPutBack = 0;
      this.restockMovedSlots.clear();
      this.pathFollower.reset();
      this.currentGroup = List.of();
      NativePlacement.cancel(this);
      ArrayList var14 = new ArrayList();

      for (Entry var16 : this.restockWanted.entrySet()) {
         var14.add(var16.getValue() + " " + shouldRelog((Item)var16.getKey()));
      }

      LarpLog.info(this.getName(), "Restock trip to {} for {}", var12.pos().toShortString(), String.join(", ", var14));
      this.setStatus("Going to the restock container at " + var12.pos().toShortString());
      return true;
   }

   private void isManualInput(String var1) {
      if (this.stocking) {
         this.followPath(var1);
      } else {
         if (this.restockTaken == 0 && this.restockTarget != null) {
            this.emptyContainers.add(this.restockTarget);
         } else if (this.restockTaken > 0) {
            this.emptyContainers.clear();
         }

         LarpLog.info(
            this.getName(),
            "Restock at {}: took {}, put back {} ({})",
            this.restockTarget == null ? "?" : this.restockTarget.pos().toShortString(),
            this.restockTaken,
            this.restockPutBack,
            var1
         );
         if (this.restockTaken > 0 || this.restockPutBack > 0) {
            this.restockTrips++;
            this.sendChat(
               "restocked at "
                  + this.restockTarget.pos().toShortString()
                  + ": took "
                  + this.restockTaken
                  + (this.restockPutBack > 0 ? ", put back " + this.restockPutBack : "")
                  + "."
            );
         }

         this.restockPhase = null;
         this.restockTarget = null;
         this.pathFollower.reset();
         NativePlacement.cancel(this);
         this.settleCountdown = 12;
      }
   }

   private Set<Long> getInstance(BlockPos var1) {
      WorldPathView var2 = new WorldPathView(var0 -> -1, false);
      HashSet var3 = new HashSet();
      double var4 = (this.getReach() - 0.3) * (this.getReach() - 0.3);

      for (int var6 = -4; var6 <= 4; var6++) {
         for (int var7 = -4; var7 <= 4; var7++) {
            for (int var8 = -3; var8 <= 1; var8++) {
               int var9 = var1.getX() + var6;
               int var10 = var1.getY() + var8;
               int var11 = var1.getZ() + var7;
               if (var6 != 0 || var7 != 0) {
                  double var12 = var9 + 0.5 - (var1.getX() + 0.5);
                  double var14 = var10 + 1.62 - (var1.getY() + 0.5);
                  double var16 = var11 + 0.5 - (var1.getZ() + 0.5);
                  if (!(var12 * var12 + var14 * var14 + var16 * var16 > var4)
                     && var2.loaded(var9, var10, var11)
                     && var2.floor(var9, var10 - 1, var11)
                     && var2.open(var9, var10, var11)
                     && var2.open(var9, var10 + 1, var11)
                     && BlockPlacementUtil.findInteractHit(this, var1, new Vec3(var9 + 0.5, var10 + 1.62, var11 + 0.5), this.getReach() - 0.3, null) != null) {
                     var3.add(AStarPathfinder.packPos(var9, var10, var11));
                  }
               }
            }
         }
      }

      return var3;
   }

   private void getPlacedCount(AutoBuilderModule.SchematicLayout var1) {
      BlockPos var2 = this.restockTarget.pos();
      if (minecraft.player.tickCount - this.restockTick > 900) {
         this.isManualInput("the walk took too long");
      } else if (minecraft.level.hasChunkAt(var2) && stopRecording(minecraft.level.getBlockState(var2))) {
         if (!this.pathFollower.hasRemainingNodes()
            && BlockPlacementUtil.findInteractHit(this, var2, minecraft.player.getEyePosition(), this.getReach(), null) != null) {
            this.restockPhase = AutoBuilderModule.RestockPhase.OPEN_CONTAINER;
            this.restockTick = minecraft.player.tickCount;
         } else {
            if (!this.pathFollower.hasRemainingNodes()) {
               BlockPos var3 = this.getFeetPos();
               Set var4 = this.getInstance(var2);
               if (var4.isEmpty()) {
                  this.isManualInput("no spot to stand at it");
                  return;
               }

               AStarPathfinder.Bounds var5 = new AStarPathfinder.Bounds(
                  Math.min(var2.getX(), var3.getX()) - 12,
                  Math.min(var2.getY(), var3.getY()) - 6,
                  Math.min(var2.getZ(), var3.getZ()) - 12,
                  Math.max(var2.getX(), var3.getX()) + 12,
                  Math.max(var2.getY(), var3.getY()) + 6,
                  Math.max(var2.getZ(), var3.getZ()) + 12
               );
               AStarPathfinder.PathResult var6 = AStarPathfinder.findPath(
                  new WorldPathView(var0 -> -1, false),
                  var3.getX(),
                  var3.getY(),
                  var3.getZ(),
                  AStarPathfinder.Goal.anyOf(var4, var2.getX(), var2.getY(), var2.getZ(), this.getReach()),
                  var5,
                  AStarPathfinder.Options.walking(32000)
               );
               if (!var6.found()) {
                  this.isManualInput("no way to walk to it");
                  return;
               }

               if (var6.steps().isEmpty()) {
                  this.restockPhase = AutoBuilderModule.RestockPhase.OPEN_CONTAINER;
                  this.restockTick = minecraft.player.tickCount;
                  return;
               }

               this.pathFollower.setPath(var6.steps());
               this.routeTarget = var2;
               AStarPathfinder.Step var7 = var6.steps().getLast();
               this.routeEnd = new BlockPos(var7.blockX(), var7.blockY(), var7.blockZ());
            }

            this.followPath();
         }
      } else {
         this.isManualInput("the container is gone");
      }
   }

   private void getMaterialNeeds() {
      BlockPos var1 = this.restockTarget.pos();
      if (minecraft.player.tickCount - this.restockTick > 60) {
         this.isManualInput("it did not open");
      } else {
         this.moveDirection = Vec3.ZERO;
         this.sneaking = (boolean)0;
         int var2 = this.getMaterialNeeds();
         if (var2 >= 0 && !this.describeTransform(var2)) {
            this.setStatus("Putting the block away");
         } else {
            ItemStack var3 = minecraft.player.getMainHandItem();
            Item var4 = var3.isEmpty() ? Items.AIR : var3.getItem();
            double var5 = this.getReach();
            NativePlacement.isReplaceable(
               this,
               var1,
               var4,
               var5,
               true,
               false,
               () -> !this.isManualInput(),
               () -> !minecraft.player.isShiftKeyDown()
                  && minecraft.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4
                  && NativePlacement.matchesItem(this, var1, var5),
               var1x -> {
                  this.lastActionTick = minecraft.player.tickCount;
                  if (var1x) {
                     this.restockPhase = AutoBuilderModule.RestockPhase.TRANSFER;
                     this.restockTick = minecraft.player.tickCount;
                     this.containerOpenedTick = -1000000000;
                  }
               }
            );
            this.setStatus("Opening the restock container at " + var1.toShortString());
         }
      }
   }

   private boolean logLeftovers(AutoBuilderModule.SchematicLayout var1) {
      int var2 = minecraft.player.tickCount;
      this.holdStill();
      Screen var3 = minecraft.gui.screen();
      AbstractContainerMenu var4 = minecraft.player.containerMenu;
      if (var3 instanceof AbstractContainerScreen && var4 != minecraft.player.inventoryMenu) {
         if (this.containerOpenedTick == -1000000000) {
            this.containerOpenedTick = var2;
         }

         if (var2 - this.restockTick > 400) {
            minecraft.player.closeContainer();
            this.isManualInput("the clicks took too long");
            return true;
         }

         this.setStatus("Restocking at " + this.restockTarget.pos().toShortString());
         if (var2 - this.containerOpenedTick >= 6 && var2 - this.lastQuickMoveTick >= 4 && var4.getCarried().isEmpty()) {
            int var5 = var4.slots.size() - 36;
            if (var5 <= 0) {
               minecraft.player.closeContainer();
               this.isManualInput("not a container menu");
               return true;
            }

            if (this.putBackSpares.getValue()) {
               Map var6 = this.getPlacedCount();
               Item var7 = this.temporaryPillars.getValue() ? this.getMaterialNeeds() : null;

               for (int var8 = var5; var8 < var4.slots.size(); var8++) {
                  ItemStack var9 = ((Slot)var4.slots.get(var8)).getItem();
                  if (!var9.isEmpty()
                     && var9.getItem() instanceof BlockItem
                     && !var6.containsKey(var9.getItem())
                     && !var9.is(var7)
                     && !this.restockMovedSlots.contains(var8)
                     && sendChat(var4, 0, var5, var9)) {
                     int var10 = var9.getCount();
                     this.renderRestockOutlines(var4, var8);
                     this.restockPutBack += var10;
                     this.restockMovedSlots.add(var8);
                     return true;
                  }
               }
            }

            for (int var11 = 0; var11 < var5; var11++) {
               ItemStack var12 = ((Slot)var4.slots.get(var11)).getItem();
               int var13 = var12.isEmpty() ? 0 : this.restockWanted.getOrDefault(var12.getItem(), 0);
               if (var13 > 0 && sendChat(var4, var5, var4.slots.size(), var12)) {
                  int var14 = var12.getCount();
                  Item var15 = var12.getItem();
                  this.renderRestockOutlines(var4, var11);
                  this.restockTaken += var14;
                  this.restockWanted.merge(var15, -var14, Integer::sum);
                  return true;
               }
            }

            minecraft.player.closeContainer();
            this.isManualInput(this.restockWanted.values().stream().anyMatch(var0 -> var0 > 0) ? "took what it had" : "took everything needed");
            return true;
         } else {
            return true;
         }
      } else if (var2 - this.restockTick <= 60) {
         this.setStatus("Waiting for the container to open");
         return true;
      } else {
         this.isManualInput("it did not open");
         return false;
      }
   }

   private void renderRestockOutlines(AbstractContainerMenu var1, int var2) {
      minecraft.gameMode.handleContainerInput(var1.containerId, var2, 0, ContainerInput.QUICK_MOVE, minecraft.player);
      this.lastQuickMoveTick = minecraft.player.tickCount;
   }

   private static boolean sendChat(AbstractContainerMenu var0, int var1, int var2, ItemStack var3) {
      for (int var4 = var1; var4 < var2; var4++) {
         ItemStack var5 = ((Slot)var0.slots.get(var4)).getItem();
         if (var5.isEmpty() || ItemStack.isSameItemSameComponents(var5, var3) && var5.getCount() < var5.getMaxStackSize()) {
            return true;
         }
      }

      return false;
   }

   private String logLeftovers() {
      if (!this.isInGame()) {
         return "";
      }

      int var1 = 0;

      for (AutoBuilderModule.ContainerPos var3 : getInstance(this.restockContainers.getValue())) {
         if (var3.dimension().equals(this.setStatus())) {
            var1++;
         }
      }

      String var4 = var1 == 0 ? "No chests selected. Look at one and press Select chest." : var1 + (var1 == 1 ? " chest selected." : " chests selected.");
      return this.stocking && this.running ? var4 + " " + this.describeStocking() + "." : var4;
   }

   private String getRotationController() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.running) {
         return this.stocking ? "Already stocking." : "Turn the builder off first; it stocks between builds.";
      }

      String var1 = ModulePanel.findConflictingModule(this);
      if (var1 != null) {
         return "Turn off " + var1 + " first; one bot runs at a time.";
      }

      if (this.recording) {
         return "Stop and save the Build Recorder first.";
      }

      AutoBuilderModule.SchematicLayout var2 = this.layout;
      if (var2 == null) {
         return this.schematic == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (!var2.scanComplete) {
         return "Still checking the world; try again in a moment.";
      }

      if (this.getMaterialNeeds(var2).isEmpty()) {
         return "The build needs no more blocks.";
      }

      int var3 = this.sendChat().size();
      if (var3 == 0) {
         return "Select the material chests first: look at a chest, barrel or shulker box within "
            + this.restockDistance.getValue().intValue()
            + " blocks and press Select chest.";
      }

      long var4 = System.currentTimeMillis();
      if (var4 - this.stockConfirmTime > 10000L) {
         this.stockConfirmTime = var4;
         return "Press Stock materials again within 10 seconds to start. It checks the "
            + var3
            + (var3 == 1 ? " chest" : " chests")
            + " for what they already hold, buys only what is still short from /ah, cheapest first, at most "
            + AuctionParser.formatPrice(this.maxPricePerItem.getValue().longValue())
            + " a block and "
            + AuctionParser.formatPrice(this.maxTotalSpend.getValue().longValue())
            + " in all, and puts it into them.";
      }

      this.stockConfirmTime = 0L;
      this.stockStartTime = var4;
      this.enable();
      if (this.stocking && this.running) {
         return "Stocking: checking " + var3 + (var3 == 1 ? " chest" : " chests") + " first.";
      }

      this.stockStartTime = 0L;
      return this.lastRunSummary != null ? this.lastRunSummary + "." : "Stocking did not start.";
   }

   private List<AutoBuilderModule.ContainerPos> sendChat() {
      ArrayList var1 = new ArrayList();
      double var2 = this.restockDistance.getValue() * this.restockDistance.getValue();
      String var4 = this.setStatus();

      for (AutoBuilderModule.ContainerPos var6 : getInstance(this.restockContainers.getValue())) {
         if (var6.dimension().equals(var4) && minecraft.player.distanceToSqr(Vec3.atCenterOf(var6.pos())) <= var2) {
            var1.add(var6);
         }
      }

      return var1;
   }

   private void startStocking() {
      if (this.layoutDirty) {
         this.shouldRelog();
      }

      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 != null && var1.scanComplete) {
         List var2 = this.sendChat();
         if (var2.isEmpty()) {
            this.stop("no material chests are selected within Restock Distance");
         } else {
            if (var1.blockCount <= 200000) {
               this.stop(var1, var1.blockCount);
            }

            this.running = (boolean)1;
            this.stocking = (boolean)1;
            this.buyingDone = (boolean)0;
            this.startLevel = minecraft.level;
            this.startPlayer = minecraft.player;
            this.playerActions.start();
            this.startTimeMs = System.currentTimeMillis();
            this.stockingPhase = AutoBuilderModule.StockingPhase.READING_CHESTS;
            this.chestsToRead.addAll(var2);
            LarpLog.info(
               this.getName(),
               "Stocking started for {}: reading {} containers within {} blocks",
               this.schematic.getName(),
               var2.size(),
               this.restockDistance.getValue()
            );
            this.sendChat(
               "stocking " + this.schematic.getName() + ": checking " + var2.size() + (var2.size() == 1 ? " chest" : " chests") + " for what they hold first."
            );
         }
      } else {
         this.stop("set the schematic and its origin first");
      }
   }

   private void describeRecording(AutoBuilderModule.SchematicLayout var1) {
      if (this.restockPhase == AutoBuilderModule.RestockPhase.WALK_TO_CONTAINER) {
         this.getPlacedCount(var1);
      } else if (this.restockPhase == AutoBuilderModule.RestockPhase.OPEN_CONTAINER) {
         this.getMaterialNeeds();
      } else if (this.stockingPhase == AutoBuilderModule.StockingPhase.READING_CHESTS) {
         AutoBuilderModule.ContainerPos var6 = this.getRotationController(this.chestsToRead);
         if (var6 != null) {
            this.startChestTrip(var6);
         } else {
            this.turnSchematic(var1);
         }
      } else {
         if (this.stockingPhase == AutoBuilderModule.StockingPhase.BUYING) {
            AutoBuilderModule.BuyResult var2 = this.setAside();
            if (var2 == AutoBuilderModule.BuyResult.BUYING) {
               return;
            }

            if (var2 == AutoBuilderModule.BuyResult.DONE) {
               this.buyingDone = (boolean)1;
            }

            this.stockingPhase = AutoBuilderModule.StockingPhase.PUTTING_AWAY;
         }

         if (this.toPutAway.isEmpty()) {
            if (!this.buyingDone) {
               this.stockingPhase = AutoBuilderModule.StockingPhase.BUYING;
            } else {
               this.getStateCounts((String)null);
            }
         } else {
            ArrayList var5 = new ArrayList();

            for (AutoBuilderModule.ContainerPos var4 : this.chestContents.keySet()) {
               if (!this.fullChests.contains(var4)
                  && (this.chestFreeSlots.getOrDefault(var4, 0) > 0 || !Collections.disjoint(this.chestContents.get(var4).keySet(), this.toPutAway.keySet()))) {
                  var5.add(var4);
               }
            }

            AutoBuilderModule.ContainerPos var7 = this.getRotationController(var5);
            if (var7 == null) {
               this.getStateCounts("the selected chests are full");
            } else {
               this.startChestTrip(var7);
            }
         }
      }
   }

   private AutoBuilderModule.ContainerPos getRotationController(Collection<AutoBuilderModule.ContainerPos> var1) {
      AutoBuilderModule.ContainerPos var2 = null;
      double var3 = Double.MAX_VALUE;

      for (AutoBuilderModule.ContainerPos var6 : var1) {
         if (!this.failedChests.contains(var6) && !this.doubleChestHalves.contains(var6)) {
            double var7 = minecraft.player.distanceToSqr(Vec3.atCenterOf(var6.pos()));
            if (var7 < var3) {
               var2 = var6;
               var3 = var7;
            }
         }
      }

      return var2;
   }

   private void startChestTrip(AutoBuilderModule.ContainerPos var1) {
      this.restockTarget = var1;
      this.restockPhase = AutoBuilderModule.RestockPhase.WALK_TO_CONTAINER;
      this.restockTick = minecraft.player.tickCount;
      this.restockTaken = this.restockPutBack = 0;
      this.restockMovedSlots.clear();
      this.stockingTouchedChest = (boolean)0;
      this.pathFollower.reset();
      this.currentGroup = List.of();
      NativePlacement.cancel(this);
      LarpLog.info(
         this.getName(),
         "Stocking: going to {} to {}",
         var1.pos().toShortString(),
         this.stockingPhase == AutoBuilderModule.StockingPhase.READING_CHESTS ? "read it" : "put " + followPath(this.toPutAway) + " blocks in"
      );
      this.setStatus(
         (this.stockingPhase == AutoBuilderModule.StockingPhase.READING_CHESTS ? "Checking the chest at " : "Taking the materials to the chest at ")
            + var1.pos().toShortString()
      );
   }

   private void followPath(String var1) {
      AutoBuilderModule.ContainerPos var2 = this.restockTarget;
      LarpLog.info(this.getName(), "Stocking at {}: {}", var2 == null ? "?" : var2.pos().toShortString(), var1);
      if (var2 != null) {
         if (this.stockingPhase == AutoBuilderModule.StockingPhase.READING_CHESTS) {
            this.chestsToRead.remove(var2);
         }

         if (!this.stockingTouchedChest && this.failedChests.add(var2)) {
            this.sendChat("could not use the chest at " + var2.pos().toShortString() + ": " + var1 + "; going on without it.");
         }
      }

      this.restockPhase = null;
      this.restockTarget = null;
      this.pathFollower.reset();
      NativePlacement.cancel(this);
   }

   private boolean reset() {
      int var1 = minecraft.player.tickCount;
      this.holdStill();
      Screen var2 = minecraft.gui.screen();
      AbstractContainerMenu var3 = minecraft.player.containerMenu;
      if (var2 instanceof AbstractContainerScreen && var3 != minecraft.player.inventoryMenu) {
         if (this.containerOpenedTick == -1000000000) {
            this.containerOpenedTick = var1;
         }

         if (var1 - this.restockTick > 400) {
            minecraft.player.closeContainer();
            this.isManualInput("the clicks took too long");
            return true;
         }

         if (var1 - this.containerOpenedTick >= 6 && var1 - this.lastQuickMoveTick >= 4 && var3.getCarried().isEmpty()) {
            int var4 = var3.slots.size() - 36;
            if (var4 <= 0) {
               minecraft.player.closeContainer();
               this.isManualInput("not a container menu");
               return true;
            }

            AutoBuilderModule.ContainerPos var5 = this.restockTarget;
            if (this.stockingPhase == AutoBuilderModule.StockingPhase.PUTTING_AWAY) {
               this.setStatus("Putting materials into the chest at " + var5.pos().toShortString());

               for (int var6 = var4; var6 < var3.slots.size(); var6++) {
                  ItemStack var7 = ((Slot)var3.slots.get(var6)).getItem();
                  if (!var7.isEmpty()
                     && this.toPutAway.getOrDefault(var7.getItem(), 0) > 0
                     && var7.getComponentsPatch().isEmpty()
                     && sendChat(var3, 0, var4, var7)) {
                     Item var8 = var7.getItem();
                     int var9 = var7.getCount();
                     this.renderRestockOutlines(var3, var6);
                     int var10 = this.toPutAway.get(var8) - var9;
                     if (var10 > 0) {
                        this.toPutAway.put(var8, var10);
                     } else {
                        this.toPutAway.remove(var8);
                     }

                     this.putAwayTotals.merge(var8, var9, Integer::sum);
                     this.stockingTouchedChest = (boolean)1;
                     return true;
                  }
               }

               if (!this.toPutAway.isEmpty()) {
                  this.fullChests.add(var5);
               }
            }

            HashMap var11 = new HashMap();
            int var12 = 0;

            for (int var13 = 0; var13 < var4; var13++) {
               ItemStack var15 = ((Slot)var3.slots.get(var13)).getItem();
               if (var15.isEmpty()) {
                  var12++;
               } else if (var15.getComponentsPatch().isEmpty()) {
                  var11.merge(var15.getItem(), var15.getCount(), Integer::sum);
               }
            }

            if (!this.chestContents.containsKey(var5)) {
               LarpLog.info(
                  this.getName(),
                  "Stocking: the container at {} holds {} kinds of plain items, {} of {} slots free",
                  var5.pos().toShortString(),
                  var11.size(),
                  var12,
                  var4
               );
            }

            this.chestContents.put(var5, var11);
            this.chestFreeSlots.put(var5, var12);
            AutoBuilderModule.ContainerPos var14 = this.renderPreview(var5);
            if (var14 != null) {
               this.doubleChestHalves.add(var14);
               this.chestsToRead.remove(var14);
               this.chestContents.remove(var14);
               this.chestFreeSlots.remove(var14);
            }

            this.stockingTouchedChest = (boolean)1;
            minecraft.player.closeContainer();
            this.isManualInput(
               this.stockingPhase == AutoBuilderModule.StockingPhase.READING_CHESTS
                  ? "read it"
                  : (this.toPutAway.isEmpty() ? "put everything in" : "it is full")
            );
            return true;
         } else {
            return true;
         }
      } else if (var1 - this.restockTick <= 60) {
         this.setStatus("Waiting for the chest to open");
         return true;
      } else {
         this.isManualInput("it did not open");
         return false;
      }
   }

   private AutoBuilderModule.ContainerPos renderPreview(AutoBuilderModule.ContainerPos var1) {
      BlockState var2 = minecraft.level.getBlockState(var1.pos());
      if (var2.getBlock() instanceof ChestBlock && var2.getValue(ChestBlock.TYPE) != ChestType.SINGLE) {
         BlockPos var3 = var1.pos().relative(ChestBlock.getConnectedDirection(var2));
         return new AutoBuilderModule.ContainerPos(var1.dimension(), var3.getX(), var3.getY(), var3.getZ());
      } else {
         return null;
      }
   }

   private void turnSchematic(AutoBuilderModule.SchematicLayout var1) {
      int var2 = 0;

      for (AutoBuilderModule.ContainerPos var4 : this.chestContents.keySet()) {
         if (!this.doubleChestHalves.contains(var4)) {
            var2++;
         }
      }

      if (var2 == 0) {
         this.stop("none of the selected chests could be opened");
      } else {
         LinkedHashMap var10 = new LinkedHashMap<>(this.getMaterialNeeds(var1));
         Item var11 = this.temporaryPillars.getValue() ? this.getMaterialNeeds() : null;
         if (var11 != null) {
            var10.merge(var11, 32, Integer::sum);
         }

         HashMap var5 = new HashMap();

         for (Map var7 : this.chestContents.values()) {
            var7.forEach((var1x, var2x) -> var5.merge(var1x, var2x, Integer::sum));
         }

         for (Item var15 : var10.keySet()) {
            var5.merge(var15, this.countItem(var15), Integer::sum);
         }

         this.toBuy.putAll(subtractItems(var10, var5));
         this.freeChestSlots = 0;

         for (int var16 : this.chestFreeSlots.values()) {
            this.freeChestSlots += var16;
         }

         int var14 = followPath(var10);
         int var17 = var14 - followPath(this.toBuy);
         LarpLog.info(
            this.getName(),
            "Stocking plan: {} chests read, {} of {} blocks at hand, {} free chest slots; to buy {}",
            var2,
            var17,
            var14,
            this.freeChestSlots,
            this.toBuy
         );
         this.sendChat(
            "checked "
               + var2
               + (var2 == 1 ? " chest" : " chests")
               + ": with your inventory they hold "
               + var17
               + " of the "
               + var14
               + " blocks the build still needs."
         );
         if (this.toBuy.isEmpty()) {
            this.getStateCounts("nothing needs buying");
         } else {
            for (Entry var9 : this.toBuy.entrySet()) {
               this.sendChat("  to buy: " + var9.getValue() + " " + shouldRelog((Item)var9.getKey()));
            }

            this.stockingPhase = AutoBuilderModule.StockingPhase.BUYING;
         }
      }
   }

   static Map<Item, Integer> subtractItems(Map<Item, Integer> var0, Map<Item, Integer> var1) {
      LinkedHashMap var2 = new LinkedHashMap();

      for (Entry var4 : var0.entrySet()) {
         int var5 = (Integer)var4.getValue() - var1.getOrDefault(var4.getKey(), 0);
         if (var5 > 0) {
            var2.put((Item)var4.getKey(), var5);
         }
      }

      return var2;
   }

   static int followPath(Map<Item, Integer> var0) {
      int var1 = 0;

      for (int var3 : var0.values()) {
         var1 += var3;
      }

      return var1;
   }

   private AutoBuilderModule.BuyResult setAside() {
      if (this.outOfMoney) {
         return AutoBuilderModule.BuyResult.DONE;
      }

      long var1 = this.maxTotalSpend.getValue().longValue() - this.totalSpent;
      if (var1 <= 0L) {
         this.notifyOnce("Max Total Spend is used up; nothing more is bought");
         return AutoBuilderModule.BuyResult.DONE;
      }

      for (Entry var4 : this.toBuy.entrySet()) {
         Item var5 = (Item)var4.getKey();
         if ((Integer)var4.getValue() > 0 && !this.unbuyableItems.contains(var5)) {
            if (this.freeChestSlots <= 0) {
               this.notifyOnce("the selected chests have no free slots left; select more chests");
               return AutoBuilderModule.BuyResult.DONE;
            }

            int var6 = new ItemStack(var5).getMaxStackSize();
            int var7 = Math.min((Integer)var4.getValue(), this.freeChestSlots * var6);
            int var8 = this.stopRecording(var5);
            if (var8 < Math.min(var7, var6)) {
               if (!this.toPutAway.isEmpty()) {
                  return AutoBuilderModule.BuyResult.NEEDS_PUT_AWAY;
               }

               this.notifyOnce("no free inventory room to buy " + shouldRelog(var5) + " into; empty a few slots");
               return AutoBuilderModule.BuyResult.DONE;
            }

            var7 = Math.min(var7, var8);
            this.buyingItem = var5;
            this.buyStillCount = 0;
            this.pathFollower.reset();
            this.currentGroup = List.of();
            NativePlacement.cancel(this);
            this.buyer.startItem(var5, var7, this.maxPricePerItem.getValue().longValue(), this.maxPricePerBuy.getValue().longValue(), var1);
            this.sendChat(
               "buying "
                  + var7
                  + " "
                  + shouldRelog(var5)
                  + " from /ah, cheapest first: at most "
                  + AuctionParser.formatPrice(this.maxPricePerItem.getValue().longValue())
                  + " a block, "
                  + AuctionParser.formatPrice(this.maxPricePerBuy.getValue().longValue())
                  + " a listing, "
                  + AuctionParser.formatPrice(var1)
                  + " left to spend."
            );
            return AutoBuilderModule.BuyResult.BUYING;
         }
      }

      return AutoBuilderModule.BuyResult.DONE;
   }

   private int stopRecording(Item var1) {
      int var2 = new ItemStack(var1).getMaxStackSize();
      int var3 = 0;
      Inventory var4 = minecraft.player.getInventory();

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var4.getItem(var5);
         if (var6.isEmpty()) {
            var3 += var2;
         } else if (var6.is(var1) && var6.getComponentsPatch().isEmpty()) {
            var3 += Math.max(0, var2 - var6.getCount());
         }
      }

      return var3;
   }

   private void blockOf(Item var1, int var2, long var3, String var5) {
      if (var1 != null) {
         if (var2 > 0) {
            int var6 = new ItemStack(var1).getMaxStackSize();
            int var7 = this.toBuy.getOrDefault(var1, 0) - var2;
            if (var7 > 0) {
               this.toBuy.put(var1, var7);
            } else {
               this.toBuy.remove(var1);
            }

            this.toPutAway.merge(var1, var2, Integer::sum);
            this.boughtTotals.merge(var1, var2, Integer::sum);
            this.freeChestSlots -= (var2 + var6 - 1) / var6;
         } else {
            this.buyPrices.put(var1, var3);
            this.buyFailureReasons.put(var1, var5 != null && !var5.isEmpty() ? var5 : "nothing bought");
         }
      }
   }

   private void getStateCounts(String var1) {
      String var2 = AutoAreaMiner.clearRoute(System.currentTimeMillis() - this.startTimeMs);
      StringBuilder var3 = new StringBuilder("stocking done in ").append(var2);
      if (var1 != null) {
         var3.append(" (").append(var1).append(')');
      }

      var3.append(": bought ")
         .append(followPath(this.boughtTotals))
         .append(" blocks for ")
         .append(AuctionParser.formatPrice(this.totalSpent))
         .append(", put ")
         .append(followPath(this.putAwayTotals))
         .append(" into the chests");
      this.sendChat(var3 + ".");

      for (Entry var5 : this.boughtTotals.entrySet()) {
         this.sendChat(
            "  bought " + var5.getValue() + " " + shouldRelog((Item)var5.getKey()) + ", put away " + this.putAwayTotals.getOrDefault(var5.getKey(), 0)
         );
      }

      if (!this.toPutAway.isEmpty()) {
         ArrayList var7 = new ArrayList();

         for (Entry var6 : this.toPutAway.entrySet()) {
            var7.add(var6.getValue() + " " + shouldRelog((Item)var6.getKey()));
         }

         this.sendChat("  still in your inventory, no chest had room: " + String.join(", ", var7));
      }

      for (Entry var10 : this.toBuy.entrySet()) {
         String var11 = this.buyFailureReasons.containsKey(var10.getKey())
            ? this.buyFailureReasons.get(var10.getKey())
            : (
               this.outOfMoney
                  ? "out of money"
                  : (
                     this.totalSpent >= this.maxTotalSpend.getValue().longValue()
                        ? "Max Total Spend reached"
                        : (this.freeChestSlots <= 0 ? "no chest room left" : "not bought")
                  )
            );
         this.sendChat("  still short: " + var10.getValue() + " " + shouldRelog((Item)var10.getKey()) + " (" + var11 + ")");
      }

      LarpLog.info(this.getName(), "Stocking done: {}; still short {}; reasons {}", var3, this.toBuy, this.buyFailureReasons);
      this.lastRunSummary = "Last stocking: bought "
         + followPath(this.boughtTotals)
         + ", put away "
         + followPath(this.putAwayTotals)
         + (this.toBuy.isEmpty() ? "" : ", " + followPath(this.toBuy) + " still short");
      NotificationManager.getInstance()
         .notify(
            "Auto Builder",
            "Stocking done: "
               + followPath(this.putAwayTotals)
               + " blocks put away"
               + (this.toBuy.isEmpty() ? "" : ", " + followPath(this.toBuy) + " still short"),
            this.toBuy.isEmpty() ? NotificationManager.Type.SUCCESS : NotificationManager.Type.WARNING,
            8000L
         );
      this.running = (boolean)0;
      this.disable();
   }

   private String describeStocking() {
      String var1 = this.stockingPhase == AutoBuilderModule.StockingPhase.READING_CHESTS
         ? "checking the chests"
         : (this.stockingPhase == AutoBuilderModule.StockingPhase.BUYING ? "buying" : "putting materials away");
      return "Stocking, "
         + var1
         + ": bought "
         + followPath(this.boughtTotals)
         + " for "
         + AuctionParser.formatPrice(this.totalSpent)
         + ", "
         + followPath(this.putAwayTotals)
         + " put away, "
         + AutoAreaMiner.clearRoute(System.currentTimeMillis() - this.startTimeMs);
   }

   private Path stop() {
      return getSchematicsDir().resolve(BuildOrderFile.orderFileName(this.schematicSetting.getValue().trim()));
   }

   private void getSchematic(AutoBuilderModule.SchematicLayout var1) {
      this.recordedLayout = var1;
      this.recordedOrder = null;
      this.recordingError = null;
      if (var1 != null && !this.schematicSetting.getValue().isBlank()) {
         try {
            List var2 = BuildOrderFile.read(this.stop());
            if (var2.isEmpty()) {
               return;
            }

            ArrayList var3 = new ArrayList();

            for (int[] var5 : var2) {
               int var6 = var1.indexAtSchematicPos(var5[0], var5[1], var5[2]);
               if (var6 >= 0) {
                  var3.add(var6);
               }
            }

            this.recordedOrder = var3.stream().mapToInt(Integer::intValue).toArray();
            LarpLog.info(
               this.getName(),
               "Recorded order of {}: {} cells ({} not in the schematic)",
               this.schematicSetting.getValue().trim(),
               this.recordedOrder.length,
               var2.size() - this.recordedOrder.length
            );
         } catch (IOException var7) {
            this.recordingError = var7.getMessage();
            LarpLog.info(this.getName(), "Could not read the recorded order: {}", var7.toString());
         }
      }
   }

   private String isStaffNearby() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.running) {
         return "Turn the builder off before recording.";
      }

      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 == null) {
         return this.schematic == null ? "Load a schematic and set its origin first." : "Set the origin first.";
      }

      if (this.recording) {
         return "Already recording: " + this.recordedCells.size() + " blocks so far.";
      }

      if (var1.blockCount > 200000) {
         return "This schematic is too big to record (" + var1.blockCount + " blocks).";
      }

      this.standingAtRecordStart = new BitSet(var1.blockCount);

      for (int var2 = 0; var2 < var1.blockCount; var2++) {
         BlockState var3 = var1.stateAt(var2);
         if (var3 != null && minecraft.level.getBlockState(var1.posAt(var2)).is(var3.getBlock())) {
            this.standingAtRecordStart.set(var2);
         }
      }

      this.recordedCells.clear();
      this.recordedCellSet.clear();
      this.recording = (boolean)1;
      LarpLog.info(
         this.getName(),
         "Build Recorder started on {} ({} of {} blocks already stand)",
         this.schematic.getName(),
         this.standingAtRecordStart.cardinality(),
         var1.blockCount
      );
      return "Recording: place the blocks of " + this.schematic.getName() + " in the order you want them built, then press Stop and save the recording.";
   }

   private void clearTarget(AutoBuilderModule.SchematicLayout var1) {
      if (this.recording) {
         BlockPos var2 = minecraft.player.blockPosition();
         MutableBlockPos var3 = new MutableBlockPos();

         for (int var4 = Math.max(var1.minX, var2.getX() - 8); var4 <= Math.min(var1.maxX, var2.getX() + 8); var4++) {
            for (int var5 = Math.max(var1.minY, var2.getY() - 8); var5 <= Math.min(var1.maxY, var2.getY() + 8); var5++) {
               for (int var6 = Math.max(var1.minZ, var2.getZ() - 8); var6 <= Math.min(var1.maxZ, var2.getZ() + 8); var6++) {
                  int var7 = var1.indexAt(var4, var5, var6);
                  if (var7 >= 0
                     && !this.recordedCellSet.contains(var7)
                     && !this.standingAtRecordStart.get(var7)
                     && var1.stateAt(var7) != null
                     && minecraft.level.getBlockState(var3.set(var4, var5, var6)).is(var1.stateAt(var7).getBlock())) {
                     this.recordedCells.add(var7);
                     this.recordedCellSet.add(var7);
                  }
               }
            }
         }
      }
   }

   private void getSchematicsDir(String var1) {
      this.recording = (boolean)0;
      LarpLog.info(this.getName(), "Build Recorder stopped without saving: {} ({} blocks recorded)", var1, this.recordedCells.size());
      this.sendChat("the Build Recorder stopped without saving: " + var1 + ".");
      this.recordedCells.clear();
      this.recordedCellSet.clear();
   }

   private String stopRecording() {
      if (!this.recording) {
         return "The Build Recorder is not recording.";
      }

      AutoBuilderModule.SchematicLayout var1 = this.layout;
      this.recording = (boolean)0;
      if (var1 != null && !this.recordedCells.isEmpty()) {
         ArrayList var2 = new ArrayList();

         for (int var4 : this.recordedCells) {
            var2.add(new int[]{var1.schematic.blockX(var4), var1.schematic.getFormat(var4), var1.schematic.blockZ(var4)});
         }

         Path var6 = this.stop();

         try {
            BuildOrderFile.write(var6, var1.schematic.getName(), var2);
         } catch (IOException var5) {
            return "Could not save the recording: " + var5.getMessage();
         }

         int var7 = var2.size();
         this.recordedCells.clear();
         this.recordedCellSet.clear();
         this.getSchematic(var1);
         LarpLog.info(this.getName(), "Saved the build order of {} blocks to {}", var7, var6.getFileName());
         return "Saved the order of " + var7 + " blocks to " + var6.getFileName() + ". Follow Recording builds in this order.";
      } else {
         this.recordedCells.clear();
         this.recordedCellSet.clear();
         return "Nothing was placed, so nothing was saved.";
      }
   }

   private String blockOf() {
      if (this.recording) {
         this.getSchematicsDir("you deleted the recording");
      }

      if (this.schematicSetting.getValue().isBlank()) {
         return "Pick a schematic first.";
      }

      try {
         boolean var1 = Files.deleteIfExists(this.stop());
         this.getSchematic(this.layout);
         return var1 ? "Deleted the recorded order of " + this.schematicSetting.getValue().trim() + "." : "This schematic has no recording.";
      } catch (IOException var2) {
         return "Could not delete the recording: " + var2.getMessage();
      }
   }

   private String describeRecording() {
      if (this.recording) {
         return " Recording the build order: " + this.recordedCells.size() + " blocks so far.";
      } else if (this.recordingError != null) {
         return " Its recording could not be read: " + this.recordingError + ".";
      } else {
         return this.recordedOrder != null && this.recordedOrder.length > 0
            ? " Recorded order: " + this.recordedOrder.length + " blocks" + (this.followRecording.getValue() ? ", followed." : ", not followed.")
            : "";
      }
   }

   private boolean cycleSchematic(AutoBuilderModule.SchematicLayout var1) {
      if (this.outOfMoney) {
         return false;
      }

      long var2 = this.maxTotalSpend.getValue().longValue() - this.totalSpent;
      if (var2 <= 0L) {
         this.notifyOnce("Max Total Spend is used up; buying stops for this run");
         return false;
      }

      Item var4 = null;
      int var5 = 0;
      Item var6 = this.neededPillarBlocks > 0 ? this.getMaterialNeeds() : null;
      if (var6 != null && !this.unbuyableItems.contains(var6) && this.countItem(var6) < 32) {
         var4 = var6;
         var5 = 32 - this.countItem(var6);
      } else {
         for (Entry var8 : this.getMaterialNeeds(var1).entrySet()) {
            Item var9 = (Item)var8.getKey();
            if (!this.unbuyableItems.contains(var9) && this.missingItems.containsKey(var9)) {
               int var10 = (Integer)var8.getValue() - this.countItem(var9);
               if (var10 > 0) {
                  var4 = var9;
                  var5 = var10;
                  break;
               }
            }
         }
      }

      if (var4 == null) {
         return false;
      }

      int var12 = 0;

      for (int var13 = 0; var13 < 36; var13++) {
         if (minecraft.player.getInventory().getItem(var13).isEmpty()) {
            var12++;
         }
      }

      var5 = Math.min(var5, var12 * new ItemStack(var4).getMaxStackSize());
      if (var5 <= 0) {
         this.notifyOnce("no free inventory slot to buy " + shouldRelog(var4) + " into");
         return false;
      } else {
         this.buyingItem = var4;
         this.buyStillCount = 0;
         this.pathFollower.reset();
         this.currentGroup = List.of();
         NativePlacement.cancel(this);
         this.buyer.startItem(var4, var5, this.maxPricePerItem.getValue().longValue(), this.maxPricePerBuy.getValue().longValue(), var2);
         this.sendChat(
            "buying "
               + var5
               + " "
               + shouldRelog(var4)
               + " from /ah: at most "
               + AuctionParser.formatPrice(this.maxPricePerItem.getValue().longValue())
               + " a block, "
               + AuctionParser.formatPrice(this.maxPricePerBuy.getValue().longValue())
               + " a listing, "
               + AuctionParser.formatPrice(var2)
               + " left to spend."
         );
         return true;
      }
   }

   private void countItem() {
      this.holdStill();
      this.sneaking = (boolean)0;
      if (this.isManualInput() && this.buyer.isRunning()) {
         this.buyer.abort("you moved");
      }

      if (this.buyer.isRunning() && this.buyStillCount < 4) {
         this.buyStillCount = minecraft.player.onGround() && minecraft.player.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 ? this.buyStillCount + 1 : 0;
         this.setStatus("Stopping to buy " + shouldRelog(this.buyingItem));
      } else if (this.buyer.isRunning() && this.buyer.tick() == AuctionBuyer.State.RUNNING) {
         this.setStatus("Buying " + shouldRelog(this.buyingItem) + " from /ah");
      } else {
         this.totalSpent = this.totalSpent + this.buyer.getSpent();
         this.totalBought = this.totalBought + this.buyer.getBoughtCount();
         String var1 = shouldRelog(this.buyingItem);
         if (this.buyer.getBoughtCount() > 0) {
            this.sendChat(
               "bought "
                  + this.buyer.getBoughtCount()
                  + " "
                  + var1
                  + " for "
                  + AuctionParser.formatPrice(this.buyer.getSpent())
                  + (this.buyer.onChatMessage().isEmpty() ? "" : " (" + this.buyer.onChatMessage() + ")")
                  + "."
            );
            NotificationManager.getInstance()
               .notifyDefault(
                  "Auto Builder",
                  "Bought " + this.buyer.getBoughtCount() + " " + var1 + " for " + AuctionParser.formatPrice(this.buyer.getSpent()),
                  NotificationManager.Type.SUCCESS
               );
         }

         if (this.buyer.getBoughtCount() == 0) {
            this.unbuyableItems.add(this.buyingItem);
            this.sendChat("could not buy " + var1 + ": " + (this.buyer.onChatMessage().isEmpty() ? "nothing bought" : this.buyer.onChatMessage()) + ".");
         }

         if (this.buyer.isOutOfMoney()) {
            this.outOfMoney = (boolean)1;
         }

         if (this.stocking) {
            this.blockOf(this.buyingItem, this.buyer.getBoughtCount(), this.buyer.openAuction(), this.buyer.onChatMessage());
         }

         LarpLog.info(
            this.getName(),
            "Buying {} ended {}: bought {} for {}; this run {} for {}",
            var1,
            this.buyer.getState(),
            this.buyer.getBoughtCount(),
            AuctionParser.formatPrice(this.buyer.getSpent()),
            this.totalBought,
            AuctionParser.formatPrice(this.totalSpent)
         );
         this.buyingItem = null;
         this.lastActionTick = minecraft.player.tickCount;
      }
   }

   private boolean isStaffNearby() {
      double var1 = this.staffRange.getValue();
      double var3 = var1 * var1;
      String var5 = null;

      for (StaffListModule.StaffEntry var7 : StaffListModule.getStaffEntries()) {
         if (var7.info() != null) {
            Player var8 = minecraft.level.getPlayerByUUID(var7.info().getProfile().id());
            if (var8 != null && var8 != minecraft.player) {
               double var9 = var8.distanceToSqr(minecraft.player);
               if (var9 <= var3) {
                  var3 = var9;
                  var5 = var7.name() + " (" + (int)Math.sqrt(var9) + " blocks away)";
               }
            }
         }
      }

      if (var5 == null) {
         return false;
      }

      this.staffNearby = var5;
      return true;
   }

   private void describeTransform() {
      int var1 = minecraft.player.tickCount;
      if (this.logoffAtTick == -1000000000) {
         this.holdStill();
         this.pathFollower.reset();
         this.playerActions.stopEating();
         BlockBreaker.release(this);
         if (minecraft.gui.screen() instanceof AbstractContainerScreen) {
            minecraft.player.closeContainer();
         }

         this.activeCrafterCell = null;
         this.restockPhase = null;
         if (this.buyer.isRunning()) {
            this.buyer.abort("staff nearby");
         }

         this.buyingItem = null;
         this.logoffAtTick = var1 + (int)Math.round(this.logOffDelay.getValue() * 20.0);
         LarpLog.info(this.getName(), "Staff nearby: {}; logging off in {} s", this.staffNearby, this.logOffDelay.getValue());
         this.sendChat("staff nearby: " + this.staffNearby + ". Stopping and logging off.");
      }

      this.holdStill();
      this.setStatus("Staff nearby: " + this.staffNearby);
      if (var1 >= this.logoffAtTick) {
         ClientPacketListener var2 = minecraft.getConnection();
         String var3 = "staff came near: " + this.staffNearby;
         if (!this.pillarBlocks.isEmpty()) {
            var3 = var3 + ", " + this.pillarBlocks.size() + " temporary blocks left standing";
         }

         if (var2 != null && !minecraft.isLocalServer()) {
            LarpLog.info(this.getName(), "Logging off: {}", var3);
            this.stop("logged off, " + var3);
            var2.getConnection()
               .disconnect(Component.literal(this.logOffMessage.getValue().isBlank() ? "Auto Builder stopped: staff nearby" : this.logOffMessage.getValue()));
         } else {
            LarpLog.info(this.getName(), "Staff stop in singleplayer: stopped without logging off ({})", var3);
            this.stop(var3 + " (singleplayer: not logged off)");
         }
      }
   }

   private boolean followPath() {
      int var1 = minecraft.player.tickCount;

      while (!this.ghostBlockTicks.isEmpty() && var1 - this.ghostBlockTicks.peekFirst() > 1200) {
         this.ghostBlockTicks.removeFirst();
      }

      if (this.relogOnGhostBlocks.getValue() && this.ghostBlockTicks.size() >= 8) {
         long var2 = this.lastRelogMs == 0L ? Long.MAX_VALUE : System.currentTimeMillis() - this.lastRelogMs;
         if (!shouldRelog(this.ghostBlockTicks.size(), var2, this.relogCount)) {
            if (this.relogCount >= 3 && !this.relogLimitNotified) {
               this.relogLimitNotified = (boolean)1;
               LarpLog.info(this.getName(), "Relog On Ghost Blocks: {} relogs this build already; not relogging again", 3);
               this.sendChat("the server keeps taking placed blocks back, but it already relogged 3 times this build, so it builds on without relogging.");
            }

            return false;
         } else {
            return !minecraft.isLocalServer() && minecraft.getCurrentServer() != null && minecraft.getConnection() != null
               ? this.pillarBlocks.isEmpty()
                  && this.towerBlocks.isEmpty()
                  && this.sweepBlocks.isEmpty()
                  && this.walkwayBlocks.isEmpty()
                  && this.supportBlocks.isEmpty()
                  && this.towerPhase == null
                  && this.activeSupport == null
                  && this.restockPhase == null
                  && this.buyingItem == null
                  && !this.buyer.isRunning()
                  && this.activeCrafterCell == null
                  && !this.stocking
                  && !(minecraft.gui.screen() instanceof AbstractContainerScreen)
                  && minecraft.player.onGround()
               : false;
         }
      } else {
         return false;
      }
   }

   static boolean shouldRelog(int var0, long var1, int var3) {
      return var0 >= 8 && var1 >= 300000L && var3 < 3;
   }

   private void getPlacedCount() {
      int var1 = this.ghostBlockTicks.size();
      this.relogServer = minecraft.getCurrentServer();
      this.relogState = 1;
      this.relogCount++;
      this.lastRelogMs = System.currentTimeMillis();
      this.rejoinAttempts = 0;
      this.sawNoWorld = (boolean)0;
      this.holdStill();
      this.pathFollower.reset();
      this.movement.reset();
      this.playerActions.stopEating();
      LarpLog.info(
         this.getName(),
         "Relog On Ghost Blocks: the server took back {} placed blocks in {} ticks; logging off and rejoining {} (relog {} of {}, {} blocks placed)",
         var1,
         1200,
         this.relogServer.ip,
         this.relogCount,
         3,
         this.placedCount
      );
      this.sendChat("the server took back " + var1 + " placed blocks in the last minute. Logging off and rejoining to sync up, then building on.");
      this.setStatus("Logging off to resync blocks");
   }

   private void tickRelog() {
      if (this.relogState != 0) {
         Minecraft var1 = Minecraft.getInstance();
         if (!this.isEnabled()) {
            this.finishBuild("the builder was turned off", false);
         } else {
            long var2 = System.currentTimeMillis();
            if (this.relogState == 1) {
               this.relogStartMs = var2;
               this.nextRejoinMs = var2 + 5000L;
               this.relogState = 2;
               if (var1.level != null && var1.getConnection() != null) {
                  LarpLog.info(this.getName(), "Logging off {} to resync blocks", this.relogServer.ip);
                  var1.disconnectFromWorld(Component.literal("Larp Auto Builder: rejoining to resync blocks"));
               }
            } else {
               if (var1.level == null) {
                  this.sawNoWorld = (boolean)1;
               }

               if (this.relogState == 3 || this.sawNoWorld && var1.level != null) {
                  this.relogState = 3;
                  if (var2 - this.relogStartMs > 120000L) {
                     this.finishBuild("it rejoined but the build did not start again", true);
                  }
               } else if (var1.level != null) {
                  if (var2 - this.relogStartMs > 120000L) {
                     this.finishBuild("the log off did not go through", true);
                  }
               } else if (var2 - this.relogStartMs > 120000L) {
                  this.finishBuild("it could not rejoin within 120 seconds", true);
               } else {
                  Screen var4 = var1.gui.screen();
                  if (!(var4 instanceof ConnectScreen) && var2 >= this.nextRejoinMs) {
                     if (this.rejoinAttempts >= 3) {
                        this.finishBuild("the server did not take 3 joins", true);
                     } else {
                        this.rejoinAttempts++;
                        this.nextRejoinMs = var2 + 10000L;
                        LarpLog.info(this.getName(), "Rejoining {} to resync blocks (try {} of {})", this.relogServer.ip, this.rejoinAttempts, 3);
                        Object var5 = var4 != null ? var4 : new JoinMultiplayerScreen(new TitleScreen());
                        ConnectScreen.startConnecting((Screen)var5, var1, ServerAddress.parseString(this.relogServer.ip), this.relogServer, false, null);
                     }
                  }
               }
            }
         }
      }
   }

   private void finishBuild(String var1, boolean var2) {
      LarpLog.info(this.getName(), "Relog On Ghost Blocks gave up: {}", var1);
      this.relogState = 0;
      this.relogServer = null;
      if (this.isEnabled()) {
         this.pendingStopReason = "it logged off to resync blocks and " + var1;
      }

      if (var2) {
         NotificationManager.getInstance()
            .notifyDefault("Auto Builder relog", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), NotificationManager.Type.WARNING);
      }
   }

   private void sendChat() {
      if (System.currentTimeMillis() >= this.resumeAtMs && minecraft.level.hasChunkAt(minecraft.player.blockPosition())) {
         this.resumeAtMs = 0L;
         String var1 = ModulePanel.findConflictingModule(this);
         if (var1 != null) {
            this.stop("turn off " + var1 + " first; one bot runs at a time");
         } else {
            LarpLog.info(this.getName(), "Carrying on after the relog ({} of {} this build)", this.relogCount, 3);
            this.sendChat("rejoined; building on (relog " + this.relogCount + " of 3 this build).");
            this.getSchematicsDir();
         }
      } else {
         this.setStatus("Rejoined; waiting for the world to load");
      }
   }

   private void logLeftovers(AutoBuilderModule.SchematicLayout var1) {
      TreeMap var2 = new TreeMap();
      TreeMap var3 = new TreeMap();
      MutableBlockPos var4 = new MutableBlockPos();
      int var5 = 0;
      int var6 = 0;

      for (int var7 = 0; var7 < var1.blockCount; var7++) {
         if (var1.stateAt(var7) != null && var1.cellState(var7) == BlockPlacementUtil.PlacementFailure.PLACEABLE) {
            if (var1.unplaceableReasons[var1.paletteIndices[var7]] != null) {
               var5++;
            } else if (this.outOfHeightCells.contains(var7)) {
               var6++;
            } else {
               this.getCellState(var1, var7, var4);
               var2.merge(var4.getY(), 1, Integer::sum);
               var3.merge(var1.stateAt(var7).getBlock().getName().getString(), 1, Integer::sum);
            }
         }
      }

      LarpLog.info(
         this.getName(),
         "Left over: {} that are never placed (fluids and the like), {} outside the height limits; placeable, by Y: {}; by block: {}",
         var5,
         var6,
         var2,
         var3
      );
      int var9 = 0;

      for (int var8 = 0; var8 < var1.blockCount && var9 < 12; var8++) {
         if (var1.stateAt(var8) != null
            && (
               var1.cellState(var8) == BlockPlacementUtil.PlacementFailure.STATE_MISMATCH
                  || var1.cellState(var8) == BlockPlacementUtil.PlacementFailure.BLOCKED
            )) {
            this.getCellState(var1, var8, var4);
            LarpLog.info(this.getName(), "Wrong at {}: {} instead of {}", var4.toShortString(), minecraft.level.getBlockState(var4), var1.stateAt(var8));
            var9++;
         }
      }
   }

   private void finishBuild() {
      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 != null && var1.blockCount <= 200000) {
         this.stop(var1, var1.blockCount);
      }

      int var2 = var1 == null ? 0 : var1.stateCount(BlockPlacementUtil.PlacementFailure.PLACEABLE);
      int var3 = var1 == null
         ? 0
         : var1.stateCount(BlockPlacementUtil.PlacementFailure.BLOCKED) + var1.stateCount(BlockPlacementUtil.PlacementFailure.STATE_MISMATCH);
      if (var1 != null) {
         this.logLeftovers(var1);
      }

      LarpLog.info(
         this.getName(),
         "Movement: sharpest one-tick turn {} degrees, {} sudden reversals, {} sprint-jumps",
         String.format(Locale.ROOT, "%.1f", this.maxYawDelta),
         this.reversalCount,
         this.sprintJumpCount
      );
      String var4 = AutoAreaMiner.clearRoute(System.currentTimeMillis() - this.startTimeMs);
      TreeMap var5 = new TreeMap();

      for (String var7 : this.giveUpReasons.values()) {
         var5.merge(var7, 1, Integer::sum);
      }

      StringBuilder var10 = new StringBuilder(this.schematic == null ? "build" : this.schematic.getName())
         .append(" done in ")
         .append(var4)
         .append(", ")
         .append(this.placedCount)
         .append(" blocks placed");
      if (this.rightClickCount > 0) {
         var10.append(", ").append(this.rightClickCount).append(" right clicks to set blocks");
      }

      if (!this.configuredCrafters.isEmpty()) {
         var10.append(", ").append(this.configuredCrafters.size()).append(this.configuredCrafters.size() == 1 ? " crafter" : " crafters").append(" set");
      }

      if (this.pillarCount > 0) {
         var10.append(", ").append(this.pillarCount).append(this.pillarCount == 1 ? " pillar" : " pillars");
      }

      if (this.totalBought > 0) {
         var10.append(", bought ").append(this.totalBought).append(" blocks for ").append(AuctionParser.formatPrice(this.totalSpent));
      }

      if (this.restockTrips > 0) {
         var10.append(", ").append(this.restockTrips).append(this.restockTrips == 1 ? " restock trip" : " restock trips");
      }

      if (var2 > 0) {
         var10.append(", ").append(var2).append(" still missing");
      }

      if (this.unsupportedCount > 0) {
         var10.append(", ").append(this.unsupportedCount).append(" would fall with nothing below them");
      }

      if (!var5.isEmpty()) {
         ArrayList var11 = new ArrayList();

         for (Entry var9 : var5.entrySet()) {
            var11.add(var9.getValue() + " " + (String)var9.getKey());
         }

         var10.append(" (").append(String.join(", ", var11)).append(')');
      }

      if (var3 > 0) {
         var10.append(", ").append(var3).append(" wrong blocks left as they were");
      }

      this.lastRunSummary = "Last run: " + this.placedCount + " placed" + (var2 > 0 ? ", " + var2 + " missing" : "");
      this.sendChat(var10 + ".");
      LarpLog.info(this.getName(), "Done: {}", var10);
      if (this.notifyWhenDone.getValue()) {
         NotificationManager.getInstance()
            .notify(
               "Auto Builder",
               (this.schematic == null ? "Build" : this.schematic.getName()) + " done in " + var4 + (var2 > 0 ? ", " + var2 + " missing" : ""),
               var2 > 0 ? NotificationManager.Type.WARNING : NotificationManager.Type.SUCCESS,
               8000L
            );
      }

      this.running = (boolean)0;
      this.disable();
   }

   private boolean getCellState() {
      if (this.lastAimTick == this.tickCounter) {
         return true;
      }

      this.lastAimTick = this.tickCounter;
      return false;
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (InteractionArbiter.ownsInteraction(this) && !this.stop()) {
            if (!this.getCellState()) {
               float[] var3 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var1);
               float[] var4 = this.movement.rotate(minecraft.player.getYRot(), minecraft.player.getXRot(), var3[0], var3[1], false);
               RotationManager.requestForModule(this, RotationManager.Priority.BOT, var4[0], var4[1], 1.0);
            }
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.stop()) {
            if (!this.getCellState()) {
               int var3 = Math.hypot(Mth.wrapDegrees(var1 - minecraft.player.getYRot()), var2 - minecraft.player.getXRot()) < 1.6F ? 1 : 0;
               float[] var4 = this.movement.rotateWithMode(minecraft.player.getYRot(), minecraft.player.getXRot(), var1, var2, (boolean)var3, true);
               RotationManager.requestForModule(this, RotationManager.Priority.BOT, var4[0], var4[1], 1.0);
            }
         }
      }
   }

   private void aimWhileWalking(Vec3 var1) {
      if (InteractionArbiter.ownsInteraction(this) && !this.stop()) {
         if (!this.getCellState()) {
            Vec3 var2 = minecraft.player.getEyePosition();
            Vec3 var3 = var1.add(0.0, 0.6 - this.aimHeightOffset + 0.12 * Math.sin(minecraft.player.tickCount * 0.09 + this.aimPhase), 0.0);
            if (this.routeTarget != null
               && this.pathFollower.getRemainingPath().size() <= 2
               && var2.distanceToSqr(Vec3.atCenterOf(this.routeTarget)) <= 64.0
               && AutoAreaMiner.getRoutePoint(this.pathFollower.getMoveDirection(), Vec3.atCenterOf(this.routeTarget).subtract(var2))) {
               var3 = Vec3.atCenterOf(this.routeTarget);
            }

            float[] var4 = RotationManager.calculateAngles(var2, var3);
            float[] var5 = this.movement.rotate(minecraft.player.getYRot(), minecraft.player.getXRot(), var4[0], var4[1], false);
            RotationManager.requestForModule(this, RotationManager.Priority.BOT, var5[0], var5[1], 1.0);
         }
      }
   }

   private boolean countItem() {
      return this.placementMode.getValue().equals("Aggressive");
   }

   public boolean isAggressiveActive() {
      return !this.isEnabled()
         ? false
         : this.countItem()
            && this.running
            && this.isRuntimeActive()
            && minecraft.player != null
            && minecraft.player == this.startPlayer
            && !this.pathFollower.hasRemainingNodes()
            && this.towerPhase == null
            && this.restockPhase == null
            && this.buyingItem == null
            && this.activeCrafterCell == null
            && this.staffNearby == null
            && (this.currentTarget != null || minecraft.player.tickCount - this.lastActionTick < 10)
            && !this.isManualInput();
   }

   private boolean stop() {
      if (!this.countItem()) {
         return false;
      }

      ViewController.Owner var1 = ViewController.update();
      return var1 == ViewController.Owner.RETURN || var1 == ViewController.Owner.AUTO_BUILDER && !this.isAggressiveActive();
   }

   public RotationController getRotationController() {
      return this.rotation;
   }

   public double getMaterialNeeds() {
      return 35.0;
   }

   public static Input getMovementInput() {
      if (instance != null && instance.isEnabled()) {
         AutoBuilderModule var0 = instance;
         Minecraft var1 = Minecraft.getInstance();
         int var2 = ViewController.update() == ViewController.Owner.AUTO_BUILDER ? 1 : 0;
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.running
            && var1.player != null
            && var1.player == var0.startPlayer
            && var1.player.tickCount == var0.lastInputTick
            && !ViewController.isFreecam()
            && (!MovementCorrection.isRotationActive() || var2 != 0)
            && !(var1.gui.screen() instanceof AbstractContainerScreen)
            && (!RotationManager.hasRequest() || RotationManager.isActive(RotationManager.Priority.BOT))) {
            int var3 = var0.moveDirection.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 != 0 && var3 != 0) {
               return null;
            }

            if (var3 == 0 && !var0.jumping && !var0.sneaking) {
               return null;
            }

            Input var4 = var3 != 0
               ? var0.movement.directionToInput(var1.player.getYRot(), var0.moveDirection)
               : new Input(false, false, false, false, false, false, false);
            return new Input(var4.forward(), var4.backward(), var4.left(), var4.right(), var0.jumping, var0.sneaking, var0.sprinting);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void sendChat(String var1) {
      if (minecraft != null && minecraft.player != null) {
         minecraft.player.sendSystemMessage(Component.literal("Larp Auto Builder: " + var1));
      }
   }

   private void notifyOnce(String var1) {
      if (!var1.equals(this.lastNotice)) {
         this.lastNotice = var1;
         this.sendChat(var1 + ".");
      }
   }

   private void stop(String var1) {
      LarpLog.info(this.getName(), "Stopped: {}", var1);
      if (this.layout != null && minecraft.level != null && this.layout.blockCount <= 200000) {
         this.stop(this.layout, this.layout.blockCount);
         this.logLeftovers(this.layout);
      }

      this.sendChat("stopped, " + var1 + ".");
      NotificationManager.getInstance()
         .notifyDefault("Auto Builder stopped", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), NotificationManager.Type.WARNING);
      this.setStatus("Stopped: " + var1);
      this.lastRunSummary = "Stopped: " + var1;
      this.running = (boolean)0;
      this.disable();
   }

   private int progressPercent() {
      AutoBuilderModule.SchematicLayout var1 = this.layout;
      if (var1 != null && this.initialMissingCount > 0) {
         int var2 = var1.stateCount(BlockPlacementUtil.PlacementFailure.PLACEABLE);
         return Math.clamp(Math.round(100.0 * (this.initialMissingCount - Math.min(this.initialMissingCount, var2)) / this.initialMissingCount), 0, 100);
      } else {
         return this.running ? 0 : 100;
      }
   }

   private String followPath() {
      return this.stocking
         ? this.describeStocking()
         : this.progressPercent() + "% done, " + this.placedCount + " placed, " + AutoAreaMiner.clearRoute(System.currentTimeMillis() - this.startTimeMs);
   }

   private void renderPreview() {
      if (this.isInGame()) {
         if (this.showRestockContainers.getValue()) {
            this.renderRestockOutlines();
         }

         if (this.showPreview.getValue()) {
            AutoBuilderModule.SchematicLayout var1 = this.layout;
            if (var1 != null && var1.scanLevel == minecraft.level) {
               try {
                  int var2 = Theme.scaleRounding(0.25);
                  Gizmos.cuboid(
                     new AABB(var1.minX, var1.minY, var1.minZ, var1.maxX + 1, var1.maxY + 1, var1.maxZ + 1),
                     GizmoStyle.stroke(ColorUtil.withAlpha(var2, 0.8), 1.5F)
                  );
                  Schematic.Placement var3 = var1.transform;
                  Gizmos.cuboid(
                        new AABB(var3.originX(), var3.originY(), var3.originZ(), var3.originX() + 1, var3.originY() + 1, var3.originZ() + 1).inflate(0.04),
                        GizmoStyle.strokeAndFill(-14249, 2.0F, 1090504791)
                     )
                     .setAlwaysOnTop();
                  boolean var4 = this.showCorrect.getValue();

                  for (int var8 : var1.previewIndices) {
                     if (var8 < var1.blockCount) {
                        BlockPlacementUtil.PlacementFailure var9 = var1.cellState(var8);

                        int var10 = switch (var9) {
                           case PLACEABLE -> var2;
                           case BLOCKED -> -45747;
                           case STATE_MISMATCH -> -24765;
                           case SECOND_HALF -> var4 ? -11870592 : 0;
                           case UNKNOWN_STATE -> 0;
                        };
                        if (var10 != 0) {
                           BlockPos var11 = var1.posAt(var8);
                           float var12 = var9 == BlockPlacementUtil.PlacementFailure.PLACEABLE
                              ? 0.12F
                              : (var9 == BlockPlacementUtil.PlacementFailure.SECOND_HALF ? 0.08F : 0.3F);
                           Gizmos.cuboid(
                              new AABB(var11).deflate(0.03),
                              GizmoStyle.strokeAndFill(ColorUtil.withAlpha(var10, 0.55), 1.0F, ColorUtil.withAlpha(var10, var12))
                           );
                        }
                     }
                  }

                  if (this.running && this.showPath.getValue()) {
                     BuildPlanner.BlockTarget var14 = this.currentTarget;
                     if (var14 != null) {
                        Gizmos.cuboid(
                              new AABB(new BlockPos(var14.blockX(), var14.blockY(), var14.blockZ())).inflate(0.01),
                              GizmoStyle.strokeAndFill(ColorUtil.withAlpha(var2, 1.0), 2.0F, ColorUtil.withAlpha(var2, 0.25))
                           )
                           .setAlwaysOnTop();
                     }

                     if (this.pathFollower.hasRemainingNodes()) {
                        Vec3 var15 = minecraft.player.position().add(0.0, 0.05, 0.0);

                        for (AStarPathfinder.Step var17 : this.pathFollower.getRemainingPath()) {
                           Vec3 var18 = new Vec3(var17.blockX() + 0.5, var17.blockY() + 0.05, var17.blockZ() + 0.5);
                           Gizmos.line(var15, var18, ColorUtil.withAlpha(var2, 0.85), 2.0F).setAlwaysOnTop();
                           var15 = var18;
                        }
                     }
                  }
               } catch (RuntimeException var13) {
                  LarpLog.errorOnce(this.getName(), "Preview failed", var13);
               }
            }
         }
      }
   }

   private void renderRestockOutlines() {
      List var1 = getInstance(this.restockContainers.getValue());
      if (!var1.isEmpty()) {
         try {
            String var2 = this.setStatus();
            double var3 = Math.max(32.0, this.restockDistance.getValue());

            for (AutoBuilderModule.ContainerPos var6 : var1) {
               if (var6.dimension().equals(var2) && !(minecraft.player.distanceToSqr(Vec3.atCenterOf(var6.pos())) > var3 * var3)) {
                  int var7 = this.restockTarget != null && this.restockTarget.equals(var6) ? 1 : 0;
                  int var8 = var7 != 0 ? -11870592 : -10443270;
                  Gizmos.cuboid(
                        new AABB(var6.pos()).inflate(0.02), GizmoStyle.strokeAndFill(ColorUtil.withAlpha(var8, 0.9), 2.0F, ColorUtil.withAlpha(var8, 0.12))
                     )
                     .setAlwaysOnTop();
               }
            }
         } catch (RuntimeException var9) {
            LarpLog.errorOnce(this.getName(), "Restock outlines failed", var9);
         }
      }
   }

   @Override
   public ModulePanel.HudCard hudCard() {
      if (this.isInGame() && this.statusCard.getValue() && this.running && this.isRuntimeActive() && minecraft.level == this.startLevel) {
         AutoBuilderModule.SchematicLayout var1 = this.layout;
         int var2 = var1 != null && var1.scanComplete ? 1 : 0;
         String var3 = schematicExtensionPattern.matcher(this.schematicSetting.getValue().trim()).replaceFirst("");
         String var4 = this.getStatus();
         return this.stocking
            ? new ModulePanel.HudCard(
               "Stocking " + (var3.isEmpty() ? "materials" : var3),
               "",
               var4 != null && !var4.isBlank() ? var4 : "Starting",
               followPath(this.putAwayTotals) + " put away",
               -1.0
            )
            : new ModulePanel.HudCard(
               var3.isEmpty() ? "Schematic" : var3,
               var2 != 0 ? this.progressPercent() + "%" : "",
               var4 != null && !var4.isBlank() ? var4 : "Starting",
               this.placedCount + " placed" + (var2 != 0 ? ", " + var1.stateCount(BlockPlacementUtil.PlacementFailure.PLACEABLE) + " missing" : ""),
               var2 != 0 ? this.progressPercent() / 100.0 : -1.0
            );
      } else {
         return null;
      }
   }

   @Override
   public Widget preview() {
      return new SchematicPreviewWidget(this);
   }

   Schematic countItem() {
      return this.unloaded ? null : this.schematic;
   }

   String getSchematic() {
      String var1 = this.schematicSetting.getValue().trim();
      if (this.loading) {
         return "Loading " + var1;
      } else {
         return this.loadError != null ? "Could not load " + (var1.isEmpty() ? "the schematic" : var1) + ": " + this.loadError : "No schematic loaded";
      }
   }

   public boolean isRunning() {
      return this.running;
   }

   public int getPlacedCount() {
      return this.placedCount;
   }

   public Schematic getSchematic() {
      return this.schematic;
   }

   public String clearTarget() {
      return this.loadError;
   }

   public boolean renderPreview() {
      return this.loading;
   }

   public int[] getStateCounts() {
      AutoBuilderModule.SchematicLayout var1 = this.layout;
      return var1 != null && var1.scanComplete ? (int[])var1.stateCounts.clone() : null;
   }

   private enum BuyResult {
      BUYING,
      NEEDS_PUT_AWAY,
      DONE;
   }

   enum CellAction {
      PLACE,
      ADJUST,
      OPEN_CRAFTER;
   }

   record ContainerPos(String dimension, int x, int y, int z) {
      private final int blockX;
      private final int blockY;
      private final int blockZ;

      ContainerPos(String dimension, int x, int y, int z) {
         this.dimension = dimension;
         this.blockX = x;
         this.blockY = y;
         this.blockZ = z;
      }

      BlockPos pos() {
         return new BlockPos(this.blockX, this.blockY, this.blockZ);
      }

      @Override
      public String toString() {
         return this.dimension + " " + this.blockX + " " + this.blockY + " " + this.blockZ;
      }
   }

   private record LoadResult(String file, Schematic schematic, String error) {
   }

   enum RestockPhase {
      WALK_TO_CONTAINER,
      OPEN_CONTAINER,
      TRANSFER;
   }

   static final class SchematicLayout {
      final Schematic schematic;
      final Schematic.Placement transform;
      final int blockCount;
      final long[] packedPositions;
      final int[] paletteIndices;
      final int[] indicesByY;
      final int[] layerOffsets;
      final BlockState[] paletteStates;
      final String[] unplaceableReasons;
      final boolean[] secondHalfFlags;
      final Map<Integer, int[]> disabledSlots = new HashMap<>();
      final byte[] cellStates;
      final int minX;
      final int minY;
      final int minZ;
      final int maxX;
      final int maxY;
      final int maxZ;
      int scanCursor;
      final int[] stateCounts = new int[BlockPlacementUtil.PlacementFailure.values().length];
      final int[] pendingStateCounts = new int[BlockPlacementUtil.PlacementFailure.values().length];
      boolean scanComplete;
      int[] previewIndices = new int[0];
      final PriorityQueue<long[]> nearestQueue = new PriorityQueue<>((var0, var1x) -> Long.compare(var1x[0], var0[0]));
      Object scanLevel;
      private Map<Long, Integer> positionIndex;
      private Map<Long, Integer> schematicPositionIndex;

      SchematicLayout(Schematic var1, Schematic.Placement var2) {
         this.schematic = var1;
         this.transform = var2;
         this.blockCount = var1.blockCount();
         this.packedPositions = new long[this.blockCount];
         this.paletteIndices = new int[this.blockCount];
         this.cellStates = new byte[this.blockCount];
         BlockState[] var3 = new BlockState[var1.getPalette().size()];
         this.unplaceableReasons = new String[var3.length];
         this.secondHalfFlags = new boolean[var3.length];

         for (int var4 = 0; var4 < var3.length; var4++) {
            BlockState var5 = BlockPlacementUtil.parseBlockState(var1.getPalette().get(var4));
            var3[var4] = var5 == null ? null : BlockPlacementUtil.transformState(var5, var2);
            this.unplaceableReasons[var4] = var5 == null ? "unknown block" : BlockPlacementUtil.getUnplaceableReason(var3[var4]);
            this.secondHalfFlags[var4] = (boolean)(var5 != null && BlockPlacementUtil.isSecondHalf(var3[var4]) ? 1 : 0);
         }

         this.paletteStates = var3;
         int[] var10 = var2.bounds(var1);
         this.minX = var10[0];
         this.minY = var10[1];
         this.minZ = var10[2];
         this.maxX = var10[3];
         this.maxY = var10[4];
         this.maxZ = var10[5];
         int var11 = this.maxY - this.minY + 1;
         this.layerOffsets = new int[var11 + 1];

         for (int var6 = 0; var6 < this.blockCount; var6++) {
            int[] var7 = var2.world(var1.blockX(var6), var1.getFormat(var6), var1.blockZ(var6));
            this.packedPositions[var6] = BlockPos.asLong(var7[0], var7[1], var7[2]);
            this.paletteIndices[var6] = var1.describe(var6);
            this.cellStates[var6] = (byte)BlockPlacementUtil.PlacementFailure.UNKNOWN_STATE.ordinal();
            this.layerOffsets[var7[1] - this.minY + 1]++;
            BlockState var8 = var3[this.paletteIndices[var6]];
            if (var8 != null && var8.getBlock() instanceof CrafterBlock) {
               int[] var9 = var1.disabledSlots(var6);
               if (var9.length > 0) {
                  this.disabledSlots.put(var6, var9);
               }
            }
         }

         for (int var12 = 1; var12 <= var11; var12++) {
            this.layerOffsets[var12] = this.layerOffsets[var12] + this.layerOffsets[var12 - 1];
         }

         this.indicesByY = new int[this.blockCount];
         int[] var13 = Arrays.copyOf(this.layerOffsets, var11);
         int var14 = 0;

         while (var14 < this.blockCount) {
            this.indicesByY[var13[BlockPos.getY(this.packedPositions[var14]) - this.minY]++] = var14++;
         }
      }

      boolean stateAt(int var1, int var2, int var3) {
         if (var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && var3 >= this.minZ && var3 <= this.maxZ) {
            if (this.positionIndex == null) {
               HashMap var4 = new HashMap(Math.max(16, this.blockCount * 2));

               for (int var5 = 0; var5 < this.blockCount; var5++) {
                  var4.put(this.packedPositions[var5], var5);
               }

               this.positionIndex = var4;
            }

            return this.positionIndex.containsKey(BlockPos.asLong(var1, var2, var3));
         } else {
            return false;
         }
      }

      boolean isBuildable(int var1, int var2, int var3) {
         int var4 = this.indexAt(var1, var2, var3);
         return var4 >= 0 && this.stateAt(var4) != null && this.unplaceableReasons[this.paletteIndices[var4]] == null;
      }

      int indexAt(int var1, int var2, int var3) {
         return !this.stateAt(var1, var2, var3) ? -1 : this.positionIndex.get(BlockPos.asLong(var1, var2, var3));
      }

      int indexAtSchematicPos(int var1, int var2, int var3) {
         if (this.schematicPositionIndex == null) {
            HashMap var4 = new HashMap(Math.max(16, this.blockCount * 2));

            for (int var5 = 0; var5 < this.blockCount; var5++) {
               var4.put(AStarPathfinder.packPos(this.schematic.blockX(var5), this.schematic.getFormat(var5), this.schematic.blockZ(var5)), var5);
            }

            this.schematicPositionIndex = var4;
         }

         return this.schematicPositionIndex.getOrDefault(AStarPathfinder.packPos(var1, var2, var3), -1);
      }

      BlockState stateAt(int var1) {
         return this.paletteStates[this.paletteIndices[var1]];
      }

      boolean isSecondHalf(int var1) {
         return this.secondHalfFlags[this.paletteIndices[var1]];
      }

      BlockPos posAt(int var1) {
         return BlockPos.of(this.packedPositions[var1]);
      }

      BlockPlacementUtil.PlacementFailure cellState(int var1) {
         return BlockPlacementUtil.PlacementFailure.values()[this.cellStates[var1]];
      }

      int stateCount(BlockPlacementUtil.PlacementFailure var1) {
         return this.stateCounts[var1.ordinal()];
      }

      LayerScanPlan.Region bounds() {
         return new LayerScanPlan.Region(this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ);
      }
   }

   enum StockingPhase {
      READING_CHESTS,
      BUYING,
      PUTTING_AWAY;
   }

   enum SupportPhase {
      APPROACH,
      BUILD,
      ASCEND,
      DISMANTLE;
   }

   enum TowerPhase {
      WALK_TO_PILLAR,
      ASCEND,
      TOP,
      DESCEND,
      WALKWAY_PLACE,
      WALKWAY_STEP,
      WALKWAY_BREAK;
   }
}
