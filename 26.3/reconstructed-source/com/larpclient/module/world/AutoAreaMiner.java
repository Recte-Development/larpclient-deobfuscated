package com.larpclient.module.world;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.gui.clickgui.ModulePanel;
import com.larpclient.gui.theme.Theme;
import com.larpclient.gui.widget.ConfirmationWindow;
import com.larpclient.hud.NotificationManager;
import com.larpclient.mixin.LocalPlayerMixin_4;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.TextSetting;
import com.larpclient.module.player.AutoSustain;
import com.larpclient.module.render.FreecamModule;
import com.larpclient.render.world.WorldPathView;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.entity.MovementCorrection;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.inventory.ToolSelector;
import com.larpclient.util.math.RotationSmoother2;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.AStarPathfinder;
import com.larpclient.util.world.BlockBreaker;
import com.larpclient.util.world.GridPathfinder;
import com.larpclient.util.world.LayerScanPlan;
import com.larpclient.util.world.NativePlacement;
import com.larpclient.util.world.PathFollower;
import com.larpclient.util.world.TunnelPlanner;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SplittableRandom;
import java.util.Map.Entry;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class AutoAreaMiner extends WorldAutomationModule implements ModulePanel, NativePlacement.PlacementAimer {
   private static AutoAreaMiner instance;
   private static boolean hooksRegistered;
   static final int sET_ASIDE_TICKS = 400;
   static final int mAX_FAILURES = 3;
   static final int rESTORE_SKIP_TICKS = 2400;
   static final int rESTORE_RESET_TICKS = 600;
   static final int mAX_RESTORES = 3;
   static final int sCAN_INTERVAL_TICKS = 40;
   private static final int mAX_CANDIDATES = 8;
   private static final int fLOOD_FILL_LIMIT = 20000;
   private static final int mIN_DRILL_BLOCKS = 2;
   static final int nEVER = -1000000000;
   static final int bREAK_TIMEOUT_TICKS = 200;
   static final int mAX_PLUG_ATTEMPTS = 60;
   private final TextSetting corner1Setting = this.addSetting(
      new TextSetting("Corner 1", "First corner as x y z, or use Pick with camera", "").setTitle("x y z")
   );
   private final TextSetting corner2Setting = this.addSetting(
      new TextSetting("Corner 2", "Second corner as x y z, or use Pick with camera", "").setTitle("x y z")
   );
   private final TextSetting drillNameSetting = this.addSetting(
      new TextSetting(
            "Drill Name", "Part of your 3x3 drill's name, not case sensitive. The drill cuts 3x3 faces and a plain pickaxe cleans up the rest", "Drill"
         )
         .setTitle("Drill")
   );
   private final NumberSetting layerHeightSetting = this.addSliderSetting(
      "Layer Height", "How tall each layer is. The area is cleared from the top down, one layer at a time", 3.0, 1.0, 6.0, 1.0
   );
   private final NumberSetting passWidthSetting = this.addSliderSetting("Pass Width", "How wide each row of a layer is", 3.0, 1.0, 5.0, 1.0);
   private final NumberSetting reachSetting = this.addSliderSetting("Reach", "How far the miner reaches, capped by your real reach", 4.5, 3.0, 6.0, 0.5);
   private final BooleanSetting sweepLeftoversSetting = this.addToggleSetting(
      "Sweep Leftovers", "After the drill finishes a layer, mine every block still standing before moving down", true
   );
   private final BooleanSetting sprintSetting = this.addToggleSetting("Sprint", "Sprint while walking", true);
   private final BooleanSetting placeBlocksSetting = this.addToggleSetting(
      "Place Blocks", "Pillar up, bridge gaps and plug lava or water with solid blocks from your hotbar", true
   );
   private final BooleanSetting autoEatSetting = this.addToggleSetting(
      "Auto Eat", "Eat food at 10 hunger or less, and a golden apple after losing the amount below", true
   );
   private final NumberSetting gappleAfterLosingSetting = this.addSliderSetting(
      "Gapple After Losing", "Hunger bars lost before a golden apple from your hotbar is eaten", 2.0, 1.0, 10.0, 1.0
   );
   private final BooleanSetting xpRepairSetting = this.addToggleSetting(
      "XP Repair", "When your tool runs low, throw XP bottles from your hotbar to mend it", true
   );
   private final NumberSetting repairAtSetting = this.addSliderSetting(
      "Repair At", "Uses left on the tool that starts XP repair, or stops the run if it cannot be mended", 50.0, 1.0, 1000.0, 1.0
   );
   private final BooleanSetting showAreaSetting = this.addToggleSetting("Show Area", "Draw the selected area and the block being mined", true);
   private final BooleanSetting showPathSetting = this.addToggleSetting("Show Path", "Draw the route the miner is walking", true);
   private final BooleanSetting pauseInGuiSetting = this.addToggleSetting(
      "Pause In GUI",
      "Stop while a menu other than chat or the pause menu is open, such as the ClickGUI. Chat and the pause menu never stop it; inventories always make it hold still",
      true
   );
   private final BooleanSetting notifyWhenDoneSetting = this.addToggleSetting("Notify When Done", "Show a notification when the area is cleared", true);
   private final BooleanSetting showProgressSetting = this.addToggleSetting(
      "Show Progress", "Show a small card at the top of the screen with the layer, progress and what the miner is doing", true
   );
   private final BooleanSetting collectDropsSetting = this.addToggleSetting(
      "Collect Drops", "After the last layer, walk over the items lying in the area to pick them up", true
   );
   private final KeybindSetting selectAreaKeySetting = this.addSetting(
      new KeybindSetting("Select Area Key", "Press to pick the corners with a free camera: left click sets one corner, right click the other, Esc finishes", -1)
   );
   private final BooleanSetting autoToolSetting = this.addToggleSetting(
      "Auto Tool",
      "Take the right hotbar tool for each block: a shovel for dirt, sand and gravel, an axe for wood, shears or a hoe for leaves. The drill still cuts its 3x3 faces. Off mines everything with your pickaxe",
      true
   );
   private final ModeSetting drillCutSetting = this.addSetting(
      new ModeSetting(
         "Drill Cut",
         "Auto watches what each drill hit breaks and uses 3x3 faces once it sees one. 3x3 trusts your drill from the first hit; One Block uses it like a normal pickaxe",
         "Auto",
         "Auto",
         "3x3",
         "One Block"
      )
   );
   private final BooleanSetting crouchSometimesSetting = this.addToggleSetting(
      "Crouch Sometimes",
      "Crouch where players do: while cutting the step down to the next layer and beside a drop, and now and then while mining with the pickaxe. Never for drill hits, which need you standing",
      true
   );
   private final NumberSetting cameraSpeedSetting = this.addSliderSetting(
      "Camera Speed",
      "How fast the free camera flies while you pick corners, in blocks per second. The mouse wheel changes it until you finish. Your normal Freecam speed stays as it is",
      6.0,
      1.0,
      40.0,
      1.0
   );
   private LayerScanPlan plan;
   private Object level;
   private Object player;
   private int currentLayer;
   private boolean pickaxeOnly;
   private boolean layerAnnounced;
   private AutoAreaMiner.Phase phase = AutoAreaMiner.Phase.MINE_LAYER;
   private final List<BlockPos> minableBlocks = new ArrayList<>();
   private final List<BlockPos> fluidBlocks = new ArrayList<>();
   private int lastScanTick = -1000000000;
   private int lastRestoreResetTick;
   private final Map<BlockPos, Integer> setAsideUntil = new HashMap<>();
   private final Map<BlockPos, Integer> failureCounts = new HashMap<>();
   private final Map<BlockPos, Integer> restoreCounts = new HashMap<>();
   private final Map<BlockPos, Integer> plugCounts = new HashMap<>();
   private final Map<BlockPos, BlockState> brokenStates = new HashMap<>();
   private final Map<BlockPos, String> breakNotes = new HashMap<>();
   private int breakStartTick;
   private int lastBreakTick;
   private final Map<BlockPos, Integer> recentBreaks = new HashMap<>();
   private static final int pUT_BACK_WINDOW_TICKS = 200;
   private final int[] breaksByDistance = new int[5];
   private final int[] putBacksByDistance = new int[5];
   private final Map<BlockPos, Integer> breakDistanceBuckets = new HashMap<>();
   private int lastReportTick;
   private final Set<BlockPos> abandonedBlocks = new HashSet<>();
   private final Set<Long> completedRows = new HashSet<>();
   private int blocksBroken;
   private int startLayer;
   private BlockPos targetBlock;
   private BlockPos walkTarget;
   private BlockState targetState;
   private final Map<BlockPos, BlockState> drillFaceStates = new HashMap<>();
   private boolean drillFaceActive;
   private BlockPos lastBreakTarget;
   private int ticksOnTarget;
   private Boolean drillCuts3x3;
   private BlockPos drillProbeBlock;
   private final Map<BlockPos, BlockState> drillProbeNeighbours = new HashMap<>();
   private int drillProbeDeadline;
   private final Set<BlockPos> outsideCutBlocks = new HashSet<>();
   static final int rESTORE_SAMPLE_SIZE = 10;
   private List<GridPathfinder.GridPos> route = List.of();
   private int routeIndex;
   private int routePlanTick;
   private int replanCount;
   private int stuckTicks;
   private Vec3 lastRoutePosition;
   private List<GridPathfinder.GridPos> previousRoute = List.of();
   private BlockPos previousRouteGoal;
   private double routeOffsetX;
   private double routeOffsetZ;
   private double arrivalRadius = 0.245;
   private double aimDrop = 0.6;
   private final SplittableRandom random = new SplittableRandom();
   private BlockPos lastMinedBlock;
   private int crouchUntilTick;
   private BlockPos crouchBlock;
   static final double cROUCH_CHANCE = 0.015;
   static final int cROUCH_MIN_TICKS = 20;
   static final int cROUCH_MAX_TICKS = 50;
   private List<TunnelPlanner.Step> standOrder = List.of();
   private List<TunnelPlanner.Step> plannedStands = List.of();
   private List<TunnelPlanner.Step> reversedStands = List.of();
   private List<TunnelPlanner.Step> lastStandOrder;
   private final Set<TunnelPlanner.Step> visitedStands = new HashSet<>();
   private List<TunnelPlanner.Step> pendingStands = List.of();
   private int pendingStandsStamp = -1;
   private int pendingStandsTick = -1000000000;
   private int reroutedStandIndex = -1;
   private int rerouteTick = -1000000000;
   private int nearestFaceCount;
   private final Map<Long, Integer> cellOrder = new HashMap<>();
   private int plannedLayer = -1;
   private int standIndex;
   private int standsDone;
   private int walkStandIndex = -1;
   private int stairRouteY = -1000000000;
   private int stairY = -1000000000;
   private int stairAttempts;
   private int failedWalkStandIndex = -1;
   private int failedWalkTick = -1000000000;
   private boolean cleanupPhase;
   private boolean stairDisabled;
   private boolean standHadWork;
   private final Set<BlockPos> skippedCells = new HashSet<>();
   private int skippedCellsStamp = -1;
   private BlockPos drillTarget;
   private Direction drillFace;
   private boolean jitterAim;
   private long jitterKey = Long.MIN_VALUE;
   private float yawJitter;
   private float pitchJitter;
   static final float yAW_JITTER_RANGE = 2.5F;
   static final float pITCH_JITTER_MIN = -2.0F;
   static final float lOOK_AROUND_DISTANCE = 6.0F;
   static final float dRILL_YAW_TOLERANCE = 25.0F;
   static final double fACE_MARGIN = 0.12;
   static final int sTAIR_LENGTH = 3;
   static final int dISTANCE_BUCKETS = 5;
   private final ConfirmationWindow restoreMonitor = new ConfirmationWindow();
   private final Set<BlockPos> rejectedDrillTargets = new HashSet<>();
   static final int dRILL_FACE_TIMEOUT_TICKS = 60;
   private boolean forceCrouch;
   private int drillWaitUntilTick;
   private int lookAroundUntilTick;
   private float lookAroundYaw;
   private float lookAroundPitch;
   private BlockPos reactionBlock;
   private int reactionStartTick = -1000000000;
   private int reactionDelayTicks;
   private double swayPhase;
   static final double lOOK_AROUND_CHANCE = 0.035;
   static final int mAX_STEP_ATTEMPTS = 80;
   static final double lOOK_AROUND_REACH = 6.0;
   private final Set<BlockPos> unreachableBlocks = new HashSet<>();
   private BlockPos fallbackTarget;
   private int unreachableStamp = -1;
   private boolean drillHintSent;
   private int correctionCount;
   private Vec3 positionBeforeTick;
   private double speedBeforeTick;
   static final int sTUCK_TICKS_LIMIT = 30;
   private Vec3 moveDirection = Vec3.ZERO;
   private boolean swimUp;
   private boolean sneaking;
   private boolean sprinting;
   private int lastActiveTick = Integer.MIN_VALUE;
   private int lastToolSlot = -1;
   private final Set<Item> announcedTools = new HashSet<>();
   private BlockPos pillarTarget;
   private BlockPos pillarBase;
   private boolean bridgingAllowed;
   private int selectedSlot = -1;
   private int selectSlotTick;
   static final int mAX_BRIDGE_ATTEMPTS = 3;
   private final Set<BlockPos> unbridgeableBlocks = new HashSet<>();
   private boolean pickingCorners;
   private boolean freecamStartedByMiner;
   private boolean pickKeyWasDown;
   private boolean pickingDrill;
   private BlockPos aimedBlock;
   private final boolean[] cornerPicked = new boolean[2];
   private Screen previousScreen;
   static final double pICK_RAY_LENGTH = 128.0;
   private boolean collectingDrops;
   private int dropCollectTicks;
   private int dropStallTicks;
   private final Set<Integer> ignoredDropIds = new HashSet<>();
   static final int mAX_DROP_COLLECT_TICKS = 1200;
   private int pillarJumpTick;
   private int pillarStallCount;
   private boolean pillarBlockPlaced;
   private final RotationSmoother2 rotator = new RotationSmoother2(RotationSmoother2.Profile.QUICK);
   private final AutoSustain consumables = new AutoSustain(this);
   private String pendingStopReason;
   private String lastHoldReason;
   private String lastAreaKey;
   private int lastLayerReached = -1;
   private boolean areaCleared;
   private boolean started;
   private final Set<BlockPos> restoredBlocks = new HashSet<>();
   private int retryStamp = -1;
   private boolean finalSweep;
   private LayerScanPlan originalPlan;
   private int originalBlockTarget;
   static final int mAX_FINAL_SWEEP_BLOCKS = 20000;
   private int noCutHitCount;
   private final Set<BlockPos> ignoredProbeBlocks = new HashSet<>();
   static final int dRILL_CHECK_HITS = 3;
   private int totalBlocks = -1;
   private int remainingBlocks = -1;
   private int lastCountTick = -1000000000;
   private long startMillis;
   private long previewUntilMillis;
   static final int mAX_AREA_SIDE = 256;
   static final int mAX_AREA_VOLUME = 200000;
   static final long pREVIEW_MILLIS = 15000L;
   private Set<BlockPos> blockSet;
   private Set<BlockPos> pendingBlockSet;
   private String blockSetName = "";
   private String pendingBlockSetName = "";
   private final PathFollower walker = new PathFollower(this, this.rotator, var1 -> this.aimAt(var1, 12.0F));
   private int lastBlockSetPlanTick = -1000000000;
   private int blockSetFailures;
   private final List<BlockPos> blockSetTargets = new ArrayList<>();
   static final int mAX_BLOCK_SET_TARGETS = 8;
   static final int pATH_NODE_BUDGET = 12000;
   private int slowTickCount;
   private int lastSlowReportTick;
   private long longestTickNanos;
   private String longestTickStatus = "";
   static final long sLOW_TICK_NANOS = 5000000L;
   static final int mAX_NEAREST_FACE_LOGS = 6;
   private TunnelPlanner.Step tunnelStand;
   private int tunnelStandIndex = -1;
   private int tunnelStandStamp = -1;
   private int standLogCount;
   private int passedOverCount;
   private long tunnelRouteKey = Long.MIN_VALUE;
   private final Set<Long> failedTunnelCells = new HashSet<>();
   static final int mAX_TUNNEL_SEARCH = 32;
   static final int dRILL_TUNNEL_MIN = 3;
   private BlockPos cleanupTarget;
   private int cleanupStamp = -1;
   static final int mIN_CUT_BLOCKS = 2;
   private int asideBlockCount;
   private int asidePlaceCount;
   private String asideFirstPos;
   private String asideFirstReason;
   private int giveUpCount;
   private String giveUpFirstPos;
   private String giveUpFirstReason;
   private int standLoopCounter;
   static final int sTAND_LOOP_LIMIT = 16;
   private Set<Long> reachableFloor = Set.of();
   private long reachableFloorStart = Long.MIN_VALUE;
   private int reachableFloorY = Integer.MIN_VALUE;
   private int reachableFloorStamp = -1;
   private int reachableFloorTick = -1000000000;
   private int unreachableCount;
   private int unreachableCountStamp = -1;
   static final int mAX_NEAREST_FACE_RETRIES = 6;
   static final int rOUTE_LOOKAHEAD = 8;
   static final int mAX_STAND_DISTANCE = 16;
   static final int aRRIVAL_MARGIN = 2;
   static final int mAX_STAIR_SEARCH = 6;
   static final float pLACE_LOOK_TOLERANCE = 1.5F;
   static final int bRIDGE_REACH = 5;
   static final int pLACE_PATIENCE = 3;
   private BlockPos placeBlock;
   private BlockPos lastPlacedBlock;
   private int placeSteadyTicks;
   private int placeRequiredTicks;
   private int lastPlaceCheckTick = -1000000000;
   private int nextPlaceTick;
   private int placeSettleTick;
   private final Map<BlockPos, Integer> placeCounts = new HashMap<>();
   private float pillarPitch = 88.0F;
   private String lastHint;
   static final double lANE_JITTER = 0.18;
   static final double mAX_TURN_COS = Math.cos(Math.toRadians(70.0));
   static final double cLOSE_REACH_FACTOR = 0.75;
   private final Set<BlockPos> drillSupportBlocks = new HashSet<>();
   private int drillSupportUntilTick;
   static final int rESTORE_WINDOW_BREAKS = 10;

   private static int getPlan(double var0) {
      return var0 < 2.0 ? 0 : (var0 < 3.0 ? 1 : (var0 < 3.5 ? 2 : (var0 < 4.0 ? 3 : 4)));
   }

   public AutoAreaMiner() {
      super("Auto Area Miner", "Clear the box between two corners, top layer first, with your 3x3 drill and a pickaxe");
      instance = this;
      this.gappleAfterLosingSetting.visibleWhen(this.autoEatSetting::getValue);
      this.repairAtSetting.visibleWhen(this.xpRepairSetting::getValue);
      this.gappleAfterLosingSetting.setDisplayName("Golden Apple After");
      this.repairAtSetting.setDisplayName("Mend Tool Below");
      this.pauseInGuiSetting.setDisplayName("Pause In Other Menus");
      this.corner1Setting.setDisplayName("Corner 1 (x y z)");
      this.corner2Setting.setDisplayName("Corner 2 (x y z)");
      this.corner1Setting.onChange(var1 -> this.previewUntilMillis = System.currentTimeMillis() + 15000L);
      this.corner2Setting.onChange(var1 -> this.previewUntilMillis = System.currentTimeMillis() + 15000L);
      if (!hooksRegistered) {
         hooksRegistered = (boolean)1;
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            AutoAreaMiner var1 = instance;
            if (var1 != null) {
               var1.mineLayer();
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            AutoAreaMiner var1 = instance;
            if (var1 != null) {
               var1.onEndTick();
            }
         });
      }
   }

   public static AutoAreaMiner findDrillSlot() {
      return instance;
   }

   public static String setTarget(Collection<BlockPos> var0, String var1) {
      AutoAreaMiner var2 = instance;
      if (var2 == null) {
         return "Auto Area Miner is not available in this edition.";
      }

      if (var2.isEnabled()) {
         return "Auto Area Miner is already on; turn it off first.";
      }

      String var3 = ModulePanel.findConflictingModule(var2);
      if (var3 != null) {
         return "Turn off " + var3 + " first; one bot runs at a time.";
      }

      if (var0 != null && !var0.isEmpty()) {
         HashSet var4 = new HashSet(var0.size() * 2);

         for (BlockPos var6 : var0) {
            var4.add(var6.immutable());
         }

         var2.pendingStopReason = null;
         var2.pendingBlockSet = var4;
         var2.pendingBlockSetName = var1 == null ? "" : var1;
         var2.enable();
         return null;
      } else {
         return "There is nothing to mine.";
      }
   }

   public static boolean countDrillBlocks(String var0) {
      AutoAreaMiner var1 = instance;
      if (var1 != null && var1.isRuntimeActive() && var1.blockSet != null && !var1.finalSweep) {
         LarpLog.info(var1.getName(), "Mine Out stopped: {}", var0);
         var1.sendMessage("stopped mining out " + var1.blockSetName + ": " + var0 + ".");
         var1.disable();
         return true;
      } else {
         return false;
      }
   }

   public static boolean stopWithReason() {
      AutoAreaMiner var0 = instance;
      return var0 != null && var0.isRuntimeActive() && var0.blockSet != null && !var0.finalSweep;
   }

   public static int getReach() {
      AutoAreaMiner var0 = instance;
      return var0 != null && var0.blockSet != null && !var0.finalSweep ? Math.max(0, var0.remainingBlocks) : -1;
   }

   @Override
   public void disable() {
      this.pendingStopReason = null;
      super.disable();
   }

   @Override
   public boolean allowsScreenInteractions() {
      Screen var1 = getMc() == null ? null : minecraft.gui.screen();
      return var1 == null || var1 instanceof ChatScreen || var1 instanceof PauseScreen || !this.pauseInGuiSetting.getValue();
   }

   @Override
   public List<ModulePanel.Command> commands() {
      return List.of(
         new ModulePanel.Command("Area", "Select the area with a free camera", this::isInReach),
         new ModulePanel.Command("Area", "Show the area", this::showArea),
         new ModulePanel.Command("Area", "Clear corners", this::clearCorners),
         new ModulePanel.Command("Drill", "Use the item in your hand", this::validateArea),
         new ModulePanel.Command("Drill", "Pick it from your inventory", this::findTunnelStand),
         new ModulePanel.Command("Drill", "No drill, pickaxe only", this::setDrill)
      );
   }

   @Override
   public String detail() {
      String var1 = this.drillNameSetting.getValue().trim();
      if (var1.isEmpty()) {
         return "Drill: none, your best pickaxe mines everything.";
      }

      if (!this.isInGame()) {
         return "Drill: any item named like \"" + var1 + "\".";
      }

      int var2 = this.findDrillSlot();
      return var2 >= 0
         ? "Drill: " + minecraft.player.getInventory().getItem(var2).getHoverName().getString() + " (hotbar slot " + (var2 + 1) + ")."
         : "Drill: \"" + var1 + "\" is not in your hotbar, so the pickaxe mines everything.";
   }

   private String showArea() {
      LayerScanPlan.Region var1 = this.getArea();
      if (var1 == null) {
         return "Set both corners first.";
      }

      if (!this.isInGame()) {
         return "Join a world first.";
      }

      this.previewUntilMillis = System.currentTimeMillis() + 15000L;
      return "Showing " + var1.describe() + " for 15 seconds.";
   }

   private String isInReach() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      String var1 = this.runTick();
      return var1 != null ? var1 : "Fly around and aim: left click sets corner 1, right click sets corner 2, Esc finishes.";
   }

   private String validateArea() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      ItemStack var1 = minecraft.player.getMainHandItem();
      return var1.isEmpty() ? "Hold your drill first." : this.setDrill(var1);
   }

   private String findTunnelStand() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the miner off before changing the drill.";
      }

      this.pickingDrill = (boolean)1;
      this.previousScreen = minecraft.gui.screen();
      minecraft.gui.setScreen(new InventoryScreen(minecraft.player));
      LarpLog.debug(this.getName(), "Picking the drill from the inventory");
      this.sendMessage("click your drill in your inventory. Nothing moves; Esc cancels.");
      return "Click your drill in your inventory. Nothing moves; Esc cancels.";
   }

   private String setDrill() {
      this.drillNameSetting.setText("");
      this.getPlan();
      return "No drill: your best pickaxe mines everything.";
   }

   private String setDrill(ItemStack var1) {
      String var2 = var1.getHoverName().getString().trim();
      if (var2.length() > 128) {
         var2 = var2.substring(0, 128);
      }

      this.drillNameSetting.setText(var2);
      this.getPlan();
      LarpLog.info(this.getName(), "Drill set to {}", var2);
      return "Drill set to " + var2 + ".";
   }

   public static boolean setAside(ItemStack var0) {
      AutoAreaMiner var1 = instance;
      if (var1 != null && var1.pickingDrill && var0 != null && !var0.isEmpty()) {
         var1.pickingDrill = (boolean)0;
         String var2 = var1.setDrill(var0);
         var1.sendMessage(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
         Screen var3 = var1.previousScreen;
         var1.previousScreen = null;
         getMc().gui.setScreen(var3);
         return true;
      } else {
         return false;
      }
   }

   public static boolean findPickaxeSlot() {
      AutoAreaMiner var0 = instance;
      return var0 != null && var0.pickingDrill;
   }

   private String clearCorners() {
      if (this.isRuntimeActive()) {
         return "Turn the miner off before clearing the corners.";
      }

      this.corner1Setting.setText("");
      this.corner2Setting.setText("");
      this.previewUntilMillis = 0L;
      this.getPlan();
      return "Corners cleared.";
   }

   private void getPlan() {
      LarpClient var1 = LarpClient.getInstance();
      if (var1 != null && var1.getConfigManager() != null) {
         var1.getConfigManager().saveActiveProfile();
      }
   }

   @Override
   public String summary() {
      if (this.blockSet != null && this.plan != null && this.isRuntimeActive()) {
         return "Mining out " + this.blockSetName + ": " + this.plan.getRegion().describe() + ". " + this.endCornerPicking() + ".";
      }

      LayerScanPlan.Region var1 = this.getArea();
      if (var1 == null) {
         return "Set both corners to choose the area.";
      }

      String var2 = "Area: " + var1.describe();
      return this.isRuntimeActive() && this.plan != null ? var2 + ". " + this.endCornerPicking() + "." : var2 + ". Stand on top of it and turn the miner on.";
   }

   @Override
   public List<ModulePanel.SettingGroup> settingGroups() {
      return List.of(
         new ModulePanel.SettingGroup("Area", List.of(this.cameraSpeedSetting, this.selectAreaKeySetting))
            .with(
               new ModulePanel.Setting(
                  this::countRemainingBlockSet,
                  List.of(
                     List.of(
                        this.findAction("Pick with camera", "Select the area with a free camera"),
                        this.findAction("Show", "Show the area").availableWhen(() -> this.getArea() != null),
                        this.findAction("Clear", "Clear corners").availableWhen(() -> this.getArea() != null)
                     )
                  )
               )
            ),
         new ModulePanel.SettingGroup("Drill", List.of(this.drillNameSetting, this.drillCutSetting))
            .with(
               new ModulePanel.Setting(
                  this::resetRestoreCounts,
                  List.of(
                     List.of(
                        this.findAction("Held item", "Use the item in your hand"),
                        this.findAction("From inventory", "Pick it from your inventory"),
                        this.findAction("No drill", "No drill, pickaxe only")
                     )
                  )
               )
            ),
         new ModulePanel.SettingGroup(
            "While mining", List.of(this.autoToolSetting, this.placeBlocksSetting, this.collectDropsSetting, this.sprintSetting, this.crouchSometimesSetting)
         ),
         new ModulePanel.SettingGroup(
            "Food and repair", List.of(this.autoEatSetting, this.gappleAfterLosingSetting, this.xpRepairSetting, this.repairAtSetting)
         ),
         new ModulePanel.SettingGroup("Display", List.of(this.showAreaSetting, this.showPathSetting, this.showProgressSetting, this.notifyWhenDoneSetting)),
         ModulePanel.SettingGroup.advanced(
            "Advanced",
            List.of(
               this.corner1Setting,
               this.corner2Setting,
               this.layerHeightSetting,
               this.passWidthSetting,
               this.reachSetting,
               this.sweepLeftoversSetting,
               this.pauseInGuiSetting
            )
         )
      );
   }

   private String countRemainingBlockSet() {
      LayerScanPlan.Region var1 = this.getArea();
      return var1 == null ? "Not set. Pick two opposite corners." : var1.describe() + (validateArea(var1) != null ? ", too wide to mine" : "");
   }

   private String resetRestoreCounts() {
      if (!this.isInGame()) {
         return this.detail();
      }

      int var1 = this.findPickaxeSlot();
      return this.detail()
         + (var1 >= 0 ? " Pickaxe: " + minecraft.player.getInventory().getItem(var1).getHoverName().getString() + "." : " No pickaxe in your hotbar.");
   }

   @Override
   public ModulePanel.Status status() {
      if (this.isRuntimeActive()) {
         return new ModulePanel.Status(
            this.plan == null ? "Starting." : (this.blockSet != null ? "Mining out " + this.blockSetName + ". " : "") + this.endCornerPicking() + ".", false
         );
      }

      LayerScanPlan.Region var1 = this.getArea();
      if (var1 == null) {
         return new ModulePanel.Status("Set the area, then turn the module on.", false);
      }

      if (validateArea(var1) != null) {
         return new ModulePanel.Status("Each side across can be at most 256 blocks. Pick the corners again.", true);
      }

      if (!this.isInGame()) {
         return new ModulePanel.Status("Area set. Join the world it is in.", false);
      }

      String var2 = ModulePanel.findConflictingModule(this);
      if (var2 != null) {
         return new ModulePanel.Status(var2 + " is running. One bot runs at a time.", true);
      }

      BlockPos var3 = this.findBlockSlot();
      int var4 = Math.max(Math.max(var1.minX() - var3.getX(), var3.getX() - var1.maxX()), Math.max(var1.minZ() - var3.getZ(), var3.getZ() - var1.maxZ()));
      if (var4 > 16) {
         return new ModulePanel.Status("You are " + var4 + " blocks from the area. Stand on it or next to it.", true);
      }

      if (var3.getY() < var1.minY()) {
         return new ModulePanel.Status("You are below the area. Stand on top of it or inside it.", true);
      }

      int var5 = this.findPickaxeSlot();
      int var6 = this.findDrillSlot();
      if (var5 < 0 && var6 < 0) {
         return new ModulePanel.Status("Put a pickaxe or your drill in your hotbar.", true);
      }

      int var7 = (int)Math.ceil(var1.sizeY() / this.layerHeightSetting.getValue());
      ArrayList var8 = new ArrayList();

      for (int var12 : new int[]{var6, var5}) {
         if (var12 >= 0) {
            var8.add(minecraft.player.getInventory().getItem(var12).getHoverName().getString());
         }
      }

      StringBuilder var13 = new StringBuilder(
         "Will clear " + var1.describe() + " in " + var7 + (var7 == 1 ? " layer" : " layers") + " with " + String.join(" and ", var8) + "."
      );
      if (this.placeBlocksSetting.getValue() && this.findBlockSlot() < 0) {
         var13.append(" No blocks in your hotbar for bridging or plugging lava.");
      }

      if (this.autoEatSetting.getValue() && AutoSustain.findHotbarSlot(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
         var13.append(" No food in your hotbar.");
      }

      return new ModulePanel.Status(var13.toString(), false);
   }

   private LayerScanPlan.Region getArea() {
      int[] var1 = LayerScanPlan.parseCoords(this.corner1Setting.getValue());
      int[] var2 = LayerScanPlan.parseCoords(this.corner2Setting.getValue());
      return var1 != null && var2 != null ? LayerScanPlan.Region.fromCorners(var1, var2) : null;
   }

   private String runTick() {
      if (this.isRuntimeActive()) {
         return "Turn the miner off before choosing a new area.";
      }

      if (this.pickingCorners) {
         return null;
      }

      this.pickingCorners = (boolean)1;
      this.cornerPicked[0] = this.cornerPicked[1] = (boolean)0;
      this.aimedBlock = null;
      minecraft.gui.setScreen(null);
      FreecamModule var1 = this.finishLayer();
      if (var1 != null && (var1.isEnabled() || !var1.isHoldMode())) {
         var1.setGuidedSpeed(this.cameraSpeedSetting.getValue());
         if (!var1.isEnabled()) {
            var1.setEnabled(true);
            this.freecamStartedByMiner = var1.isEnabled();
         }

         if (!var1.isEnabled()) {
            var1.clearGuidedSpeed();
         }
      }

      int var2 = var1 != null && var1.isEnabled() ? 1 : 0;
      LarpLog.info(this.getName(), "Picking corners{}", var2 != 0 ? " with Freecam at " + var1.getGuidedSpeed() + " blocks/s" : " from your own view");
      if (var2 != 0) {
         this.sendMessage(
            "fly with the movement keys at " + this.cameraSpeedSetting.getValue().intValue() + " blocks a second; the mouse wheel changes it. Esc finishes."
         );
      }

      return null;
   }

   private FreecamModule finishLayer() {
      return LarpClient.getInstance().getModuleManager().getModule(FreecamModule.class);
   }

   static String validateArea(LayerScanPlan.Region var0) {
      return var0.sizeX() <= 256 && var0.sizeZ() <= 256
         ? null
         : String.format(
            Locale.ROOT,
            "the area is %,d x %,d x %,d blocks; each side across can be at most %d. One corner is probably left from another world or server, so pick both corners again",
            var0.sizeX(),
            var0.sizeY(),
            var0.sizeZ(),
            256
         );
   }

   private void endCornerPicking(String var1) {
      if (this.pickingCorners) {
         this.pickingCorners = (boolean)0;
         this.aimedBlock = null;
         FreecamModule var2 = this.finishLayer();
         if (var2 != null) {
            var2.clearGuidedSpeed();
            if (this.freecamStartedByMiner && var2.isEnabled()) {
               var2.setEnabled(false);
            }
         }

         this.freecamStartedByMiner = (boolean)0;
         this.getPlan();
         LayerScanPlan.Region var3 = this.getArea();
         if (var3 != null) {
            this.previewUntilMillis = System.currentTimeMillis() + 15000L;
         }

         LarpLog.info(
            this.getName(),
            "Corner picking ended ({}); area {}, picked corners {}{}",
            var1,
            var3 == null ? "incomplete" : var3.describe(),
            this.cornerPicked[0] ? "1" : "",
            this.cornerPicked[1] ? "2" : ""
         );
         if (this.isInGame()) {
            if (var3 == null) {
               this.sendMessage("corner picking ended; set both corners to choose the area.");
            } else if (validateArea(var3) != null) {
               int var4 = this.cornerPicked[0] && !this.cornerPicked[1] ? 2 : (this.cornerPicked[1] && !this.cornerPicked[0] ? 1 : 0);
               this.sendMessage(
                  var4 > 0
                     ? "corner "
                        + var4
                        + " is still "
                        + (var4 == 1 ? this.corner1Setting : this.corner2Setting).getValue()
                        + " from before, so the area is "
                        + var3.describe()
                        + ". Pick corner "
                        + var4
                        + " too ("
                        + (var4 == 1 ? "left" : "right")
                        + " click)."
                     : "the area is " + var3.describe() + "; each side across can be at most 256 blocks. Pick the corners again."
               );
            } else {
               this.sendMessage("area set: " + var3.describe() + ". Stand on top of it and turn the miner on.");
            }
         }
      }
   }

   public static boolean isPickingCorners() {
      AutoAreaMiner var0 = instance;
      return var0 != null && var0.pickingCorners;
   }

   public static void onCornerClick(int var0) {
      AutoAreaMiner var1 = instance;
      if (var1 != null && var1.pickingCorners && var1.isInGame()) {
         BlockPos var2 = var1.findDrillSlot();
         if (var2 == null) {
            var1.sendMessage("aim at a block to set a corner.");
         } else {
            TextSetting var3 = var0 == 0 ? var1.corner1Setting : var1.corner2Setting;
            var3.setText(LayerScanPlan.formatCoords(var2.getX(), var2.getY(), var2.getZ()));
            var1.cornerPicked[var0 == 0 ? 0 : 1] = (boolean)1;
            LarpLog.debug(var1.getName(), "Corner {} picked at {}", var0 + 1, var2.toShortString());
         }
      }
   }

   private BlockPos findDrillSlot() {
      boolean var1 = ViewController.isFreecam();
      Vec3 var2 = var1 ? ViewController.getPosition(1.0F) : minecraft.player.getEyePosition();
      float var3 = var1 ? ViewController.getRawYaw() : minecraft.player.getYRot();
      float var4 = var1 ? ViewController.getPosition() : minecraft.player.getXRot();
      Vec3 var5 = var2.add(Vec3.directionFromRotation(var4, var3).scale(128.0));
      BlockHitResult var6 = minecraft.level.clip(new ClipContext(var2, var5, Block.OUTLINE, Fluid.NONE, minecraft.player));
      return var6.getType() == Type.BLOCK ? var6.getBlockPos().immutable() : null;
   }

   private void onEndTick() {
      if (!this.isInGame()) {
         if (this.pickingCorners) {
            this.pickingCorners = (boolean)0;
            this.freecamStartedByMiner = (boolean)0;
            FreecamModule var3 = this.finishLayer();
            if (var3 != null) {
               var3.clearGuidedSpeed();
            }
         }

         this.pickingDrill = (boolean)0;
      } else {
         if (this.pickingDrill && !(minecraft.gui.screen() instanceof InventoryScreen)) {
            this.pickingDrill = (boolean)0;
            this.previousScreen = null;
         }

         int var1 = this.selectAreaKeySetting.getValue() != -1
               && minecraft.gui.screen() == null
               && minecraft.isWindowActive()
               && BindCodes.isPressed(minecraft.getWindow().handle(), this.selectAreaKeySetting.getValue())
            ? 1
            : 0;
         if (var1 != 0 && !this.pickKeyWasDown) {
            if (this.pickingCorners) {
               this.endCornerPicking("key pressed");
            } else {
               String var2 = this.runTick();
               if (var2 != null) {
                  this.sendMessage(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
               }
            }
         }

         this.pickKeyWasDown = (boolean)var1;
         if (this.pickingCorners) {
            if (minecraft.gui.screen() != null) {
               this.endCornerPicking("menu opened");
            } else {
               this.aimedBlock = this.findDrillSlot();
            }
         }
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.resetState();
      if (this.pickingCorners) {
         this.endCornerPicking("miner turned on");
      }

      if (this.pendingStopReason != null) {
         String var20 = this.pendingStopReason;
         this.pendingStopReason = null;
         this.stopWithReason("it stopped earlier because " + var20 + ". Turn it on again to carry on");
      } else if (this.pendingBlockSet != null) {
         Set var19 = this.pendingBlockSet;
         this.pendingBlockSet = null;
         this.setStatus(var19, this.pendingBlockSetName);
      } else {
         LayerScanPlan.Region var1 = this.getArea();
         if (var1 == null) {
            this.stopWithReason("set both corners first");
         } else {
            String var2 = validateArea(var1);
            if (var2 != null) {
               this.stopWithReason(var2);
            } else {
               String var3 = ModulePanel.findConflictingModule(this);
               if (var3 != null) {
                  this.stopWithReason("turn off " + var3 + " first; one bot runs at a time");
               } else {
                  BlockPos var4 = this.findBlockSlot();
                  double var5 = Math.max(
                     Math.max(var1.minX() - var4.getX(), var4.getX() - var1.maxX()), Math.max(var1.minZ() - var4.getZ(), var4.getZ() - var1.maxZ())
                  );
                  if (var5 > 16.0) {
                     this.stopWithReason("stand inside or next to the area to start (you are " + (int)var5 + " blocks away)");
                  } else if (var4.getY() < var1.minY()) {
                     this.stopWithReason("you are below the area; stand on top of it or inside it");
                  } else {
                     int var7 = this.findPickaxeSlot();
                     int var8 = var7 < 0 && this.findDrillSlot() >= 0 ? 1 : 0;
                     if (var7 < 0 && var8 == 0) {
                        this.stopWithReason("put a pickaxe or your drill in your hotbar");
                     } else {
                        ItemStack var9 = minecraft.player.getInventory().getItem(var8 != 0 ? this.findDrillSlot() : var7);
                        this.plan = new LayerScanPlan(
                           var1, this.layerHeightSetting.getValue().intValue(), this.passWidthSetting.getValue().intValue(), var4.getX(), var4.getZ()
                        );
                        this.level = minecraft.level;
                        this.player = minecraft.player;
                        this.consumables.start();
                        this.pickaxeOnly = (boolean)(this.findDrillSlot() < 0 ? 1 : 0);
                        int var10 = var4.getY() > var1.maxY() ? 0 : Math.max(0, this.plan.layerIndexOf(var4.getY()));
                        int var11 = var10 <= 0 || (!this.getCurrentLayer().equals(this.lastAreaKey) || var10 < this.lastLayerReached) && !this.clearRoute(var4)
                           ? 0
                           : 1;
                        int var12 = var11 != 0 ? var10 : this.startFinalSweep();
                        if (var12 == -1) {
                           LarpLog.info(this.getName(), "Nothing to mine in {}", var1.describe());
                           this.sendMessage("there is nothing to mine in " + var1.describe() + ": every block in it is gone already.");
                           this.areaCleared = (boolean)1;
                           this.disable();
                        } else {
                           int var13 = var12 >= 0 && var12 < var10 ? 1 : 0;
                           int var14 = var13 != 0 ? this.getCurrentLayer(this.plan.layerAt(var10).top()) : 0;
                           if (var13 == 0 || this.placeBlocksSetting.getValue() && this.findBlockSlot() >= 0) {
                              if (var13 != 0 && var5 > 2.0) {
                                 this.stopWithReason(
                                    "the layers above you still have "
                                       + var14
                                       + " blocks to mine. Stand on top of the area, or right next to it (you are "
                                       + (int)var5
                                       + " blocks away) so the miner can pillar up beside it"
                                 );
                              } else {
                                 this.currentLayer = var13 != 0 ? var12 : var10;
                                 this.startLayer = this.currentLayer;
                                 this.startMillis = System.currentTimeMillis();
                                 this.totalBlocks = this.remainingBlocks = this.setAside();
                                 this.lastCountTick = minecraft.player.tickCount;
                                 this.previewUntilMillis = 0L;
                                 StringBuilder var15 = new StringBuilder(var9.getHoverName().getString());
                                 int var16 = this.findDrillSlot();
                                 if (var16 >= 0 && var8 == 0) {
                                    var15.insert(0, minecraft.player.getInventory().getItem(var16).getHoverName().getString() + " and ");
                                 }

                                 String var17 = var13 != 0
                                    ? ", pillaring up to layer " + (this.currentLayer + 1) + " first: the layers above you still have " + var14 + " blocks"
                                    : (
                                       this.currentLayer > 0
                                          ? ", starting at layer "
                                             + (this.currentLayer + 1)
                                             + " where you stand (the "
                                             + this.getCurrentLayer(this.plan.layerAt(this.currentLayer).top())
                                             + " blocks in the layers above you are mined at the end)"
                                          : ""
                                    );
                                 this.sendMessage("mining " + var1.describe() + " in " + this.plan.layerCount() + " layers with " + var15 + var17 + ".");
                                 ArrayList var18 = new ArrayList();
                                 if (var16 < 0) {
                                    var18.add("no hotbar item matches Drill Name \"" + this.drillNameSetting.getValue() + "\", so the pickaxe mines everything");
                                 }

                                 if (var8 != 0) {
                                    var18.add(
                                       "your drill is your only pickaxe, so it only cuts where its 3x3 stays inside the area; add a plain pickaxe to the hotbar to clear the edges too"
                                    );
                                 }

                                 if (this.placeBlocksSetting.getValue() && this.findBlockSlot() < 0) {
                                    var18.add("no solid blocks in the hotbar to pillar, bridge or plug lava with");
                                 }

                                 if (this.autoEatSetting.getValue() && AutoSustain.findHotbarSlot(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                                    var18.add("no food in the hotbar");
                                 }

                                 if (this.xpRepairSetting.getValue()
                                    && (AutoSustain.hasMending(var9) || var16 >= 0 && AutoSustain.hasMending(minecraft.player.getInventory().getItem(var16)))
                                    && AutoSustain.findHotbarSlot(var0 -> var0.is(Items.EXPERIENCE_BOTTLE)) < 0) {
                                    var18.add("no XP bottles in the hotbar to mend with");
                                 }

                                 if (!var18.isEmpty()) {
                                    this.sendMessage("heads up: " + String.join("; ", var18) + ".");
                                 }

                                 LarpLog.info(
                                    this.getName(),
                                    "Started: {} from {}, {} layers from layer {}, rows along {}, pass width {}, drill slot {}, {} solid blocks{}",
                                    var1.describe(),
                                    var4.toShortString(),
                                    this.plan.layerCount(),
                                    this.currentLayer + 1,
                                    this.plan.rowDirection() ? "X" : "Z",
                                    this.plan.getLaneWidth(),
                                    var16,
                                    this.totalBlocks,
                                    var13 != 0 ? ", pillaring up from layer " + (var10 + 1) : (var11 != 0 ? ", carrying on where you stand" : "")
                                 );
                                 this.started = (boolean)1;
                              }
                           } else {
                              this.stopWithReason(
                                 "the layers above you still have "
                                    + var14
                                    + " blocks to mine. Stand on top of the area to start, or put blocks in your hotbar with Place Blocks on so the miner can pillar up to them"
                              );
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.plan != null) {
         this.pendingStopReason = "the world, the player or the connection changed";
         LarpLog.info(this.getName(), "Interrupted after {} blocks: {}", this.blocksBroken, this.pendingStopReason);
      }

      if (this.plan != null && this.blockSet == null && this.started) {
         this.lastAreaKey = this.areaCleared ? null : this.getCurrentLayer();
         this.lastLayerReached = this.areaCleared ? -1 : this.currentLayer;
      }

      super.onDisable();
      NativePlacement.cancel(this);
      this.consumables.reset();
      this.resetState();
   }

   private void resetState() {
      this.plan = null;
      this.level = this.player = null;
      this.currentLayer = 0;
      this.pickaxeOnly = this.layerAnnounced = (boolean)0;
      this.phase = AutoAreaMiner.Phase.MINE_LAYER;
      this.minableBlocks.clear();
      this.fluidBlocks.clear();
      this.lastScanTick = -1000000000;
      this.lastRestoreResetTick = 0;
      this.setAsideUntil.clear();
      this.failureCounts.clear();
      this.restoreCounts.clear();
      this.plugCounts.clear();
      this.brokenStates.clear();
      this.breakNotes.clear();
      this.recentBreaks.clear();
      this.abandonedBlocks.clear();
      this.completedRows.clear();
      this.restoreMonitor.clear();
      this.jitterAim = (boolean)0;
      this.jitterKey = Long.MIN_VALUE;
      this.blocksBroken = 0;
      this.targetBlock = this.walkTarget = null;
      this.targetState = null;
      this.drillFaceStates.clear();
      this.drillFaceActive = (boolean)0;
      this.clearRoute();
      this.lastRoutePosition = null;
      this.previousRoute = List.of();
      this.previousRouteGoal = null;
      this.correctionCount = 0;
      this.positionBeforeTick = null;
      this.lastMinedBlock = this.crouchBlock = this.fallbackTarget = null;
      this.crouchUntilTick = 0;
      this.unreachableBlocks.clear();
      this.unreachableStamp = -1;
      this.drillHintSent = (boolean)0;
      this.moveDirection = Vec3.ZERO;
      this.swimUp = this.sneaking = this.sprinting = (boolean)0;
      this.lastActiveTick = Integer.MIN_VALUE;
      this.lastToolSlot = -1;
      this.announcedTools.clear();
      this.pillarTarget = this.pillarBase = null;
      this.pillarStallCount = 0;
      this.pillarBlockPlaced = (boolean)0;
      this.rotator.reset();
      this.collectingDrops = (boolean)0;
      this.dropCollectTicks = this.dropStallTicks = 0;
      this.ignoredDropIds.clear();
      this.lastBreakTarget = null;
      this.ticksOnTarget = 0;
      this.startLayer = 0;
      this.drillCuts3x3 = null;
      this.drillProbeBlock = null;
      this.drillProbeNeighbours.clear();
      this.drillProbeDeadline = 0;
      this.outsideCutBlocks.clear();
      this.bridgingAllowed = (boolean)0;
      this.unbridgeableBlocks.clear();
      this.selectedSlot = -1;
      this.selectSlotTick = 0;
      this.totalBlocks = this.remainingBlocks = -1;
      this.lastCountTick = -1000000000;
      this.startMillis = System.currentTimeMillis();
      this.areaCleared = this.started = (boolean)0;
      this.restoredBlocks.clear();
      this.retryStamp = -1;
      this.finalSweep = (boolean)0;
      this.originalPlan = null;
      this.originalBlockTarget = 0;
      this.noCutHitCount = 0;
      this.ignoredProbeBlocks.clear();
      this.blockSet = null;
      this.blockSetName = "";
      this.walker.reset();
      this.lastBlockSetPlanTick = -1000000000;
      this.blockSetFailures = 0;
      this.blockSetTargets.clear();
      this.standOrder = this.plannedStands = this.reversedStands = this.pendingStands = List.of();
      this.lastStandOrder = null;
      this.visitedStands.clear();
      this.cellOrder.clear();
      this.plannedLayer = -1;
      this.standIndex = this.standsDone = this.stairAttempts = 0;
      this.walkStandIndex = this.failedWalkStandIndex = -1;
      this.stairRouteY = this.stairY = this.failedWalkTick = -1000000000;
      this.cleanupPhase = this.stairDisabled = this.forceCrouch = this.standHadWork = (boolean)0;
      this.skippedCells.clear();
      this.skippedCellsStamp = -1;
      this.drillTarget = this.reactionBlock = null;
      this.drillFace = null;
      this.rejectedDrillTargets.clear();
      this.reactionStartTick = -1000000000;
      this.drillWaitUntilTick = this.lookAroundUntilTick = 0;
      this.placeBlock = this.lastPlacedBlock = null;
      this.placeSteadyTicks = this.placeRequiredTicks = this.nextPlaceTick = this.placeSettleTick = 0;
      this.lastPlaceCheckTick = -1000000000;
      this.placeCounts.clear();
      this.pillarPitch = 88.0F;
   }

   private void clearRoute() {
      this.route = List.of();
      this.routeIndex = 0;
      this.replanCount = 0;
      this.stuckTicks = 0;
   }

   @Override
   public void onTick() {
      long var1 = System.nanoTime();

      try {
         this.runTick();
      } finally {
         this.stopMovement(System.nanoTime() - var1);
      }
   }

   private void stopMovement(long var1) {
      if (minecraft.player != null) {
         if (var1 > 5000000L) {
            this.slowTickCount++;
            if (var1 > this.longestTickNanos) {
               this.longestTickNanos = var1;
               this.longestTickStatus = this.getStatus();
            }
         }

         int var3 = minecraft.player.tickCount;
         if (var3 - this.lastSlowReportTick >= 200) {
            this.lastSlowReportTick = var3;
            this.hasAdjacentFluid();
            if (this.slowTickCount != 0) {
               LarpLog.info(
                  this.getName(),
                  "Miner ticks over 5 ms in the last 10 s: {}, longest {} ms while: {}",
                  this.slowTickCount,
                  String.format(Locale.ROOT, "%.1f", this.longestTickNanos / 1000000.0),
                  this.longestTickStatus
               );
               this.slowTickCount = 0;
               this.longestTickNanos = 0L;
            }
         }
      }
   }

   private void runTick() {
      this.moveDirection = Vec3.ZERO;
      this.swimUp = this.sneaking = this.sprinting = this.forceCrouch = (boolean)0;
      this.standLoopCounter = 0;
      if (this.plan != null) {
         if (minecraft.level == this.level && minecraft.player == this.player) {
            this.lastActiveTick = minecraft.player.tickCount;
            String var1 = AutoSustain.getDangerReason();
            if (var1 != null) {
               BlockBreaker.release(this);
               NativePlacement.cancel(this);
               this.consumables.stopEating();
               this.swimUp = (boolean)1;
               this.setStatus(var1 + ": swimming up");
            } else if (this.autoEatSetting.getValue() && this.consumables.tryEat(this.gappleAfterLosingSetting.getValue().intValue())) {
               BlockBreaker.release(this);
               NativePlacement.cancel(this);
               this.setStatus("Eating");
            } else if (this.pillarTarget != null) {
               this.getArea();
            } else {
               String var2 = this.getPauseReason();
               if (var2 != null) {
                  this.stopMovement();
                  this.setStatus(var2);
                  if (!var2.equals(this.lastHoldReason)) {
                     this.lastHoldReason = var2;
                     LarpLog.info(this.getName(), "Held: {}", var2);
                  }
               } else {
                  this.lastHoldReason = null;
                  if (this.isManualMovement()) {
                     this.stopMovement();
                     this.rotator.reset();
                     this.setStatus("Paused for manual movement");
                  } else if (minecraft.gui.screen() instanceof AbstractContainerScreen) {
                     this.stopMovement();
                     this.setStatus("Holding still while an inventory menu is open");
                  } else if (!this.xpRepairSetting.getValue() || !this.getFootY()) {
                     if (!this.hasAdjacentFluid()) {
                        BlockBreaker.release(this);
                        this.moveDirection = Vec3.ZERO;
                        this.setStatus("Waiting for the area to load");
                     } else if (!this.isInReach()) {
                        if (this.blockSet != null) {
                           this.onCornerClick();
                        } else if (!minecraft.player.onGround()) {
                           BlockBreaker.release(this);
                           this.setStatus("Waiting to land");
                        } else {
                           this.resetRestoreCounts();
                           this.isInReach();
                           this.getReach();
                           if (this.totalBlocks >= 0 && minecraft.player.tickCount - this.lastCountTick >= 100) {
                              this.lastCountTick = minecraft.player.tickCount;
                              this.remainingBlocks = this.setAside();
                           }

                           if (this.collectingDrops) {
                              this.collectDrops();
                           } else {
                              LayerScanPlan.Layer var3 = this.plan.layerAt(this.currentLayer);
                              int var4 = this.getFootY();
                              if (var4 >= var3.bottom()) {
                                 this.stopWithReason(var3);
                              }

                              if (var4 > var3.bottom()) {
                                 this.phase = AutoAreaMiner.Phase.ENTER_AREA;
                                 this.isInsideArea(var3);
                              } else if (var4 < var3.bottom()) {
                                 this.phase = AutoAreaMiner.Phase.CLEAR_ROUTE;
                                 this.clearRoute(var3);
                              } else {
                                 this.phase = AutoAreaMiner.Phase.MINE_LAYER;
                                 this.mineLayer(var3);
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.stopWithReason("the world or player changed");
         }
      }
   }

   private void stopMovement() {
      BlockBreaker.release(this);
      NativePlacement.cancel(this);
      this.moveDirection = Vec3.ZERO;
   }

   private boolean isManualMovement() {
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

   private boolean getFootY() {
      int var1 = this.repairAtSetting.getValue().intValue();

      for (int var5 : new int[]{this.lastToolSlot, this.findDrillSlot()}) {
         if (var5 >= 0 && !this.isSpareSlot(var5)) {
            ItemStack var6 = minecraft.player.getInventory().getItem(var5);
            if (AutoSustain.getRemainingDurability(var6) <= var1 || this.consumables.isRepairing()) {
               BlockBreaker.release(this);
               AutoSustain.RepairResult var7 = this.consumables.repairTool(var5, var1);
               if (var7 == AutoSustain.RepairResult.REPAIRING) {
                  this.setStatus("Mending the tool with XP bottles");
                  return true;
               }

               if (var7 == AutoSustain.RepairResult.LOW_DURABILITY) {
                  this.stopWithReason(
                     "your "
                        + var6.getHoverName().getString()
                        + " has "
                        + AutoSustain.getRemainingDurability(var6)
                        + " uses left and cannot be mended (it needs Mending and XP bottles in the hotbar)"
                  );
                  return true;
               }
            }
         }
      }

      return false;
   }

   private void mineLayer(LayerScanPlan.Layer var1) {
      if (!this.layerAnnounced) {
         this.layerAnnounced = (boolean)1;
         BlockPos var2 = this.findBlockSlot();
         if (this.plan.getRegion().containsColumn(var2.getX(), var2.getZ())) {
            this.completedRows.add(this.isInsideArea(var2.getX(), var2.getZ()));
         }

         LarpLog.info(this.getName(), "Layer {} of {}: Y {} to {}", this.currentLayer + 1, this.plan.layerCount(), var1.bottom(), var1.top());
      }

      if (minecraft.player.tickCount - this.lastScanTick >= 40) {
         this.endCornerPicking(var1);
      }

      if (!this.placeBlocksSetting.getValue() || !this.findTunnelStand(var1)) {
         if (!this.clearRoute(var1)) {
            BlockPos var7 = this.mineBlock(var1);
            if (var7 != null) {
               this.clearRoute();
               if (this.drillFaceActive) {
                  this.sendMessage(var7, true);
               } else {
                  this.getFootY(var7, null);
               }
            } else {
               BlockBreaker.release(this);
               BlockPos var3 = this.cleanupPhase ? this.sendMessage(var1) : this.isSetAside(var1);
               if (var3 == null) {
                  if (minecraft.player.tickCount - this.lastScanTick > 2) {
                     this.endCornerPicking(var1);
                  } else {
                     byte var4 = 0;
                     if (this.blocksBroken != this.retryStamp) {
                        for (BlockPos var6 : this.minableBlocks) {
                           if (this.setAsideUntil.containsKey(var6) && !this.abandonedBlocks.contains(var6) && BlockBreaker.canBreak(var6)) {
                              this.setAsideUntil.remove(var6);
                              var4 = 1;
                           }
                        }
                     }

                     if (var4 != 0) {
                        this.retryStamp = this.blocksBroken;
                        LarpLog.debug(this.getName(), "Retrying blocks set aside in layer {}", this.currentLayer + 1);
                        this.clearRoute();
                     } else {
                        this.finishLayer(var1);
                     }
                  }
               } else {
                  this.findPickaxeSlot(var3, var1);
               }
            }
         }
      }
   }

   private void stopWithReason(LayerScanPlan.Layer var1) {
      if (this.plannedLayer != this.currentLayer) {
         this.plannedLayer = this.currentLayer;
         this.standIndex = this.standsDone = this.stairAttempts = 0;
         this.walkStandIndex = this.failedWalkStandIndex = -1;
         this.stairRouteY = this.stairY = this.failedWalkTick = -1000000000;
         this.cleanupPhase = this.stairDisabled = this.standHadWork = (boolean)0;
         this.skippedCells.clear();
         this.skippedCellsStamp = -1;
         this.cellOrder.clear();
         this.tunnelStand = null;
         this.tunnelStandIndex = this.tunnelStandStamp = -1;
         this.tunnelRouteKey = Long.MIN_VALUE;
         this.failedTunnelCells.clear();
         this.standLogCount = 0;
         this.passedOverCount = 0;
         this.cleanupTarget = null;
         this.cleanupStamp = -1;
         this.visitedStands.clear();
         this.pendingStands = List.of();
         this.pendingStandsStamp = -1;
         this.pendingStandsTick = -1000000000;
         this.lastStandOrder = null;
         this.reroutedStandIndex = -1;
         this.rerouteTick = -1000000000;
         this.nearestFaceCount = 0;
         if (this.blockSet != null) {
            this.standOrder = this.plannedStands = this.reversedStands = List.of();
            this.cleanupPhase = (boolean)1;
         } else {
            int var2 = this.getFootY();
            this.standOrder = this.plannedStands = TunnelPlanner.plan(this.plan, this.currentLayer, var2);
            this.reversedStands = TunnelPlanner.planSteps(this.plan, this.currentLayer, var1.bottom(), true);
            this.findTunnelStand();
            long var3 = this.standOrder.stream().filter(var0 -> var0.kind() == TunnelPlanner.StepKind.DESCEND).count();
            String var5 = var3 > 0L
               ? "a " + var3 + " block stair down from the corner"
               : (var2 > var1.bottom() ? "the first lane is too short for a stair, so it digs down at the corner" : "standing on the layer's floor");
            LarpLog.info(
               this.getName(),
               "Layer {} dig order: {} stands, {} lanes {} wide along {}, {}",
               this.currentLayer + 1,
               this.standOrder.size(),
               this.plan.rowDirection(),
               this.plan.getLaneWidth(),
               this.plan.rowDirection() ? "X" : "Z",
               var5
            );
         }
      }
   }

   private void findTunnelStand() {
      this.cellOrder.clear();
      int var1 = 0;

      for (TunnelPlanner.Step var3 : this.standOrder) {
         for (TunnelPlanner.Cell var5 : var3.cells()) {
            this.cellOrder.putIfAbsent(var5.key(), var1++);
         }
      }
   }

   private boolean countRemainingBlockSet() {
      return this.plan != null && this.blockSet == null && !this.cleanupPhase && this.plannedLayer == this.currentLayer;
   }

   private boolean clearRoute(LayerScanPlan.Layer var1) {
      if (!this.countRemainingBlockSet()) {
         return false;
      }

      int var2 = minecraft.player.tickCount;
      if (this.blocksBroken != this.skippedCellsStamp) {
         this.skippedCells.clear();
         this.skippedCellsStamp = this.blocksBroken;
      }

      if (var2 < this.drillWaitUntilTick && !this.findTunnelStand()) {
         BlockBreaker.release(this);
         this.setStatus("Waiting for the drill's cut to land");
         return true;
      }

      if (this.waitForFallingBlocks()) {
         return true;
      }

      if (var2 < this.lookAroundUntilTick) {
         this.sendMessage();
         return true;
      }

      while (this.standIndex < this.standOrder.size() && this.getFootY(this.standOrder.get(this.standIndex), var1)) {
         this.runTick(false);
      }

      if (this.standIndex >= this.standOrder.size() && !this.stopMovement(var1, false)) {
         this.cleanupPhase = (boolean)1;
         this.skippedCells.clear();
         int var7 = 0;

         for (BlockPos var10 : this.minableBlocks) {
            if (this.getReach(var10, var1)) {
               var7++;
            }
         }

         LarpLog.info(
            this.getName(),
            "Layer {}: dig order done after {} of {} stands{}{}{}",
            this.currentLayer + 1,
            this.standsDone,
            this.standOrder.size(),
            this.nearestFaceCount > 0 ? ", moved to the nearest face " + this.nearestFaceCount + " times" : "",
            var7 > 0 ? ", " + var7 + " blocks left for the clean-up" : "",
            this.passedOverCount > 0 ? ", " + this.passedOverCount + " passed over with no way to dig to them" : ""
         );
         return false;
      } else {
         BlockPos var3 = this.findBlockSlot();
         if (var3.getX() != this.standOrder.get(this.standIndex).standX() || var3.getZ() != this.standOrder.get(this.standIndex).standZ()) {
            this.findDrillSlot(var1);
         }

         TunnelPlanner.Step var4 = this.standOrder.get(this.standIndex);
         if (var3.getX() == var4.standX() && var3.getZ() == var4.standZ()) {
            this.clearRoute();
            this.forceCrouch = PathFollower.hasEscapeRoute(var4.standX(), var1.bottom(), var4.standZ());
            if (this.isInsideArea() && this.findBlockSlot(var4, var1)) {
               this.standHadWork = (boolean)1;
               if (this.findTunnelStand(var4)
                  && BlockBreaker.canBreak(new BlockPos(var4.standX() + var4.facingX(), var1.bottom(), var4.standZ() + var4.facingZ()))) {
                  this.moveDirection = new Vec3(var4.facingX(), 0.0, var4.facingZ());
               }

               return true;
            } else {
               BlockPos var9 = this.findPickaxeSlot(var4, var1, true);
               if (var9 != null) {
                  this.standHadWork = (boolean)1;
                  this.mineBlock(var9, null);
                  return true;
               } else {
                  BlockPos var6 = this.onEndTick(var4, var1);
                  if (var6 != null) {
                     this.standHadWork = (boolean)1;
                     this.mineBlock(var6, null);
                     return true;
                  } else {
                     this.runTick(true);
                     return true;
                  }
               }
            }
         } else {
            if (this.finishArea(var4, var3, var1)) {
               return true;
            }

            if (this.finishArea(var4, var1)) {
               return true;
            }

            BlockPos var5 = this.findPickaxeSlot(var4, var1, false);
            if (var5 != null) {
               this.clearRoute();
               this.mineBlock(var5, null);
               return true;
            }

            this.clearRoute(var4, var1);
            if (this.isManualMovement(var4, var1)) {
               return true;
            }

            this.isPickingCorners(var4, var1);
            this.runTick(false);
            return ++this.standLoopCounter % 16 != 0 ? this.clearRoute(var1) : true;
         }
      }
   }

   private boolean finishArea(TunnelPlanner.Step var1, BlockPos var2, LayerScanPlan.Layer var3) {
      if (var1.kind() == TunnelPlanner.StepKind.ROW && var1.drill() && this.isInsideArea() && this.findTunnelStand(var1) && var2.getY() == var3.bottom()) {
         int var4 = (var1.standX() - var2.getX()) * var1.facingX() + (var1.standZ() - var2.getZ()) * var1.facingZ();
         int var5 = var1.facingX() != 0 ? (var2.getZ() == var1.standZ() ? 1 : 0) : (var2.getX() == var1.standX() ? 1 : 0);
         if (var5 != 0 && var4 >= 1 && var4 <= 3) {
            for (int var6 = 1; var6 <= var4; var6++) {
               BlockPos var7 = var2.offset(var1.facingX() * var6, 0, var1.facingZ() * var6);
               if (!this.setStatus(var7)
                  || !this.setStatus(var7.above())
                  || !this.clearCorners(var7.below())
                  || PathFollower.hasEscapeRoute(var7.getX(), var7.getY(), var7.getZ())) {
                  return false;
               }
            }

            TunnelPlanner.Cell var8 = var1.drillCentre();
            BlockPos var9 = new BlockPos(var8.cellX(), var8.cellY(), var8.cellZ());
            if (!BlockBreaker.canBreak(var9.below())) {
               return false;
            }

            if (this.isInReach(var9) && this.findBlockSlot(var1, var3)) {
               this.standHadWork = (boolean)1;
            }

            this.clearRoute();
            this.moveDirection = new Vec3(var1.facingX(), 0.0, var1.facingZ());
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private boolean findTunnelStand(TunnelPlanner.Step var1) {
      return Math.abs(Mth.wrapDegrees(minecraft.player.getYRot() - findTunnelStand(var1).toYRot())) <= 25.0F;
   }

   private void endCornerPicking(TunnelPlanner.Step var1) {
      long var2 = var1.facingX() + 2L << 40 ^ var1.facingZ() + 2L << 36 ^ (var1.facingX() != 0 ? var1.standZ() : var1.standX()) & 4294967295L;
      if (var2 != this.jitterKey) {
         this.jitterKey = var2;
         this.yawJitter = (float)((this.random.nextDouble() * 2.0 - 1.0) * 2.5);
         this.pitchJitter = -2.0F + (float)this.random.nextDouble() * 8.0F;
      }
   }

   static Vec3 findDrillSlot(Vec3 var0, BlockPos var1, Direction var2, float var3, float var4) {
      int var5 = var2.getAxis() == Axis.X ? 1 : 0;
      double var6 = var5 != 0 ? var1.getX() + (var2 == Direction.EAST ? 1 : 0) : var1.getZ() + (var2 == Direction.SOUTH ? 1 : 0);
      double var8 = Math.abs(var6 - (var5 != 0 ? var0.x : var0.z));
      int var10 = var5 != 0 ? var1.getZ() : var1.getX();
      double var11 = Mth.clamp((var5 != 0 ? var0.z : var0.x) + Math.tan(Math.toRadians(var3)) * var8, var10 + 0.12, var10 + 1 - 0.12);
      double var13 = Mth.clamp(var0.y - Math.tan(Math.toRadians(var4)) * var8, var1.getY() + 0.12, var1.getY() + 1 - 0.12);
      return var5 != 0 ? new Vec3(var6, var13, var11) : new Vec3(var11, var13, var6);
   }

   private boolean getFootY(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      return this.visitedStands.contains(var1) || this.onEndTick(var1, var2);
   }

   private void findDrillSlot(LayerScanPlan.Layer var1) {
      TunnelPlanner.Step var2 = this.standOrder.get(this.standIndex);
      BlockPos var3 = this.findBlockSlot();
      int var4 = minecraft.player.tickCount;
      int var5 = this.standIndex == this.reroutedStandIndex && this.standOrder == this.lastStandOrder ? 0 : 1;
      boolean var6 = this.runTick(var1.bottom()).contains(GridPathfinder.reconstructPath(var2.standX(), var2.standZ()));
      if (var5 != 0 || !var6 && var4 - this.rerouteTick >= 40) {
         this.rerouteTick = var4;
         if (!var6 || Math.abs(var2.standX() - var3.getX()) + Math.abs(var2.standZ() - var3.getZ()) > 6) {
            this.stopMovement(var1, var5 == 0);
         }

         this.reroutedStandIndex = this.standIndex;
         this.lastStandOrder = this.standOrder;
      }
   }

   private List<TunnelPlanner.Step> getPendingStands(LayerScanPlan.Layer var1) {
      int var2 = minecraft.player.tickCount;
      if (this.pendingStandsStamp == this.blocksBroken && var2 - this.pendingStandsTick < 40) {
         this.pendingStands.removeIf(this.visitedStands::contains);
      } else {
         this.pendingStandsStamp = this.blocksBroken;
         this.pendingStandsTick = var2;
         ArrayList var3 = new ArrayList();

         for (TunnelPlanner.Step var5 : this.plannedStands) {
            if (!this.getFootY(var5, var1)) {
               var3.add(var5);
            }
         }

         for (TunnelPlanner.Step var7 : this.reversedStands) {
            if (!this.getFootY(var7, var1)) {
               var3.add(var7);
            }
         }

         this.pendingStands = var3;
      }

      return this.pendingStands;
   }

   private boolean stopMovement(LayerScanPlan.Layer var1, boolean var2) {
      BlockPos var3 = this.findBlockSlot();
      Set var4 = this.runTick(var1.bottom());
      TunnelPlanner.Step var5 = TunnelPlanner.pickStep(this.getPendingStands(var1), var4, var3.getX(), var3.getZ());
      if (var5 == null) {
         return false;
      }

      boolean var6 = var4.contains(GridPathfinder.reconstructPath(var5.standX(), var5.standZ()));
      if (var2 && !var6) {
         return false;
      }

      if (this.standIndex < this.standOrder.size() && this.standOrder.get(this.standIndex).equals(var5)) {
         return true;
      }

      int var7 = this.standOrder.indexOf(var5);
      if (var7 < 0) {
         this.standOrder = this.standOrder == this.reversedStands ? this.plannedStands : this.reversedStands;
         var7 = this.standOrder.indexOf(var5);
         this.findTunnelStand();
      }

      this.standIndex = var7;
      this.walkStandIndex = -1;
      this.tunnelStandIndex = -1;
      this.skippedCells.clear();
      if (this.nearestFaceCount++ < 6) {
         LarpLog.info(
            this.getName(),
            "Layer {}: going to the nearest face instead, stand {} of {} ({}) at {}, {}, {}, lanes {}",
            this.currentLayer + 1,
            this.standIndex + 1,
            this.standOrder.size(),
            var5.kind(),
            var5.standX(),
            var5.standZ(),
            var6 ? "on the floor" : "off the floor",
            this.standOrder == this.reversedStands ? "reversed" : "as planned"
         );
      }

      return true;
   }

   private TunnelPlanner.Step findTunnelStand(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      if (this.tunnelStandIndex != this.standIndex) {
         this.failedTunnelCells.clear();
      }

      if (this.tunnelStandIndex == this.standIndex && this.tunnelStandStamp == this.blocksBroken) {
         return this.tunnelStand;
      }

      this.tunnelStandIndex = this.standIndex;
      this.tunnelStandStamp = this.blocksBroken;
      this.tunnelStand = null;
      int var3 = var2.bottom();

      for (TunnelPlanner.ApproachMove var5 : TunnelPlanner.planSteps(this.runTick(var3), var1.standX(), var1.standZ(), 32)) {
         int var6 = var5.fromX() + var5.stepX();
         int var7 = var5.fromZ() + var5.stepZ();
         if (!this.failedTunnelCells.contains(GridPathfinder.reconstructPath(var6, var7)) && this.clearCorners(new BlockPos(var6, var3 - 1, var7))) {
            BlockPos var8 = new BlockPos(var6, var3 + 1, var7);
            BlockPos var9 = new BlockPos(var6, var3, var7);
            boolean var10 = this.setStatus(var8);
            boolean var11 = this.setStatus(var9);
            if ((!var10 || !var11) && (var10 || this.getReach(var8, var2)) && (var11 || this.getReach(var9, var2))) {
               ArrayList var12 = new ArrayList(2);
               if (!var10) {
                  var12.add(new TunnelPlanner.Cell(var6, var3 + 1, var7));
               }

               if (!var11) {
                  var12.add(new TunnelPlanner.Cell(var6, var3, var7));
               }

               this.tunnelStand = new TunnelPlanner.Step(
                  TunnelPlanner.StepKind.LANE_SHIFT, var5.fromX(), var3, var5.fromZ(), var5.stepX(), var5.stepZ(), var12, false
               );
               break;
            }
         }
      }

      return this.tunnelStand;
   }

   private boolean isManualMovement(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      TunnelPlanner.Step var3 = this.findTunnelStand(var1, var2);
      if (var3 == null) {
         return false;
      }

      BlockPos var4 = this.findBlockSlot();
      TunnelPlanner.Cell var5 = var3.cells().getFirst();
      long var6 = GridPathfinder.reconstructPath(var5.cellX(), var5.cellZ());
      if (var4.getX() == var3.standX() && var4.getZ() == var3.standZ()) {
         this.clearRoute();
         if (this.isInsideArea() && this.stopMovement(var3, var2)) {
            this.standHadWork = (boolean)1;
            return true;
         } else {
            BlockPos var13 = this.findPickaxeSlot(var3, var2, true);
            if (var13 == null) {
               this.skippedCells.clear();
               this.failedTunnelCells.add(var6);
               this.tunnelStandStamp = -1;
               return true;
            } else {
               this.standHadWork = (boolean)1;
               this.mineBlock(var13, "Digging a way to the next stand");
               return true;
            }
         }
      } else {
         long var8 = GridPathfinder.reconstructPath(var3.standX(), var3.standZ());
         if (this.tunnelRouteKey != var8 || this.route.isEmpty() && this.routeIndex == 0 || minecraft.player.tickCount - this.routePlanTick > 40) {
            this.tunnelRouteKey = var8;
            this.routePlanTick = minecraft.player.tickCount;
            this.walkStandIndex = -1;
            int var10 = var3.standX();
            int var11 = var3.standZ();
            List var12 = this.findRoute(var2.bottom(), (var2x, var3x) -> var2x == var10 && var3x == var11);
            if (var12 == null) {
               this.clearRoute();
               this.failedTunnelCells.add(var6);
               this.tunnelStandStamp = -1;
               return true;
            }

            if (var12.isEmpty()) {
               return true;
            }

            this.route = var12;
            this.routeIndex = 0;
            this.crouchUntilTick = 0;
            this.getArea(var2, var3);
         }

         BlockBreaker.release(this);
         this.walkTarget = new BlockPos(var5.cellX(), var5.cellY(), var5.cellZ());
         this.getReach(var2);
         return true;
      }
   }

   private boolean stopMovement(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      TunnelPlanner.Cell var3 = var1.drillCentre();
      BlockPos var4 = new BlockPos(var3.cellX(), var3.cellY(), var3.cellZ());
      if (!this.skippedCells.contains(var4) && this.getReach(var4, var2)) {
         Direction var5 = findTunnelStand(var1).getOpposite();
         int var6 = this.countDrillBlocks(var4, var5, var2.bottom());
         return var6 >= 2 && this.collectDrops(var4, var6) && this.setTarget(var4, var5, "Drilling a way to the next stand (" + var6 + " blocks)");
      } else {
         return false;
      }
   }

   private String showArea(BlockPos var1) {
      return BuiltInRegistries.BLOCK.getKey(minecraft.level.getBlockState(var1).getBlock()).getPath();
   }

   private String setStatus(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      BlockPos var3 = new BlockPos(var1.standX(), var2.bottom(), var1.standZ());
      return String.format(
         Locale.ROOT,
         "stand %d of %d (%s) at %d, %d; player at %s, reachable floor %d cells; feet block %s%s, head %s%s, floor %s",
         this.standIndex + 1,
         this.standOrder.size(),
         var1.kind(),
         var1.standX(),
         var1.standZ(),
         this.findBlockSlot().toShortString(),
         this.runTick(var2.bottom()).size(),
         this.showArea(var3),
         this.isSetAside(var3) ? " (set aside)" : "",
         this.showArea(var3.above()),
         this.isSetAside(var3.above()) ? " (set aside)" : "",
         this.showArea(var3.below())
      );
   }

   private void clearRoute(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      if (this.tunnelStandIndex != this.standIndex && this.standLogCount < 3) {
         this.standLogCount++;
         LarpLog.info(this.getName(), "Layer {}: {}; digging a way to it", this.currentLayer + 1, this.setStatus(var1, var2));
      }
   }

   private void isPickingCorners(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      if (this.passedOverCount++ < 3) {
         LarpLog.info(this.getName(), "Layer {}: no way to dig to {}; passed over", this.currentLayer + 1, this.setStatus(var1, var2));
      }
   }

   private BlockPos sendMessage(LayerScanPlan.Layer var1) {
      if (this.cleanupTarget != null && this.cleanupStamp == this.blocksBroken && this.getReach(this.cleanupTarget, var1)) {
         return this.cleanupTarget;
      }

      this.cleanupStamp = this.blocksBroken;
      this.cleanupTarget = null;
      double var2 = minecraft.player.getX();
      double var4 = minecraft.player.getZ();
      ArrayList var6 = new ArrayList();

      for (BlockPos var8 : this.minableBlocks) {
         if (this.getReach(var8, var1) && this.resetRestoreCounts(var8)) {
            var6.add(var8);
         }
      }

      var6.sort(Comparator.comparingDouble(var4x -> Math.pow(var4x.getX() + 0.5 - var2, 2.0) + Math.pow(var4x.getZ() + 0.5 - var4, 2.0)));
      int var10 = 0;

      for (BlockPos var9 : var6) {
         if (var10++ >= 32) {
            break;
         }

         if (this.clearCorners(var9, var1)) {
            this.cleanupTarget = var9;
            break;
         }
      }

      return this.cleanupTarget;
   }

   static Direction findTunnelStand(TunnelPlanner.Step var0) {
      return var0.facingX() > 0 ? Direction.EAST : (var0.facingX() < 0 ? Direction.WEST : (var0.facingZ() > 0 ? Direction.SOUTH : Direction.NORTH));
   }

   private boolean findBlockSlot(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      if (var1.kind() != TunnelPlanner.StepKind.ROW && var1.kind() != TunnelPlanner.StepKind.CLIMB) {
         return false;
      } else {
         TunnelPlanner.Cell var3 = var1.drillCentre();
         BlockPos var4 = new BlockPos(var3.cellX(), var3.cellY(), var3.cellZ());
         if (!this.skippedCells.contains(var4) && this.getReach(var4, var2)) {
            Direction var5 = findTunnelStand(var1).getOpposite();
            int var6 = this.countDrillBlocks(var4, var5, var2.bottom());
            this.endCornerPicking(var1);
            return var6 >= 2
               && this.collectDrops(var4, var6)
               && this.isSetAside(var4, var5, "Cutting a 3x3 face at " + var4.toShortString() + " (" + var6 + " blocks)", true);
         } else {
            return false;
         }
      }
   }

   private boolean isInsideArea() {
      return this.findDrillSlot() >= 0 && this.stopMovement();
   }

   static List<BlockPos> getDrillBlocks(BlockPos var0, Direction var1) {
      ArrayList var2 = new ArrayList(9);
      Axis var3 = var1.getAxis();

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            var2.add(var3 == Axis.X ? var0.offset(0, var4, var5) : (var3 == Axis.Y ? var0.offset(var4, 0, var5) : var0.offset(var4, var5, 0)));
         }
      }

      return var2;
   }

   private int countDrillBlocks(BlockPos var1, Direction var2, int var3) {
      int var4 = 0;

      for (BlockPos var6 : getDrillBlocks(var1, var2)) {
         if (BlockBreaker.canBreak(var6)) {
            if (!this.isInsideArea(var6) || var6.getY() < var3 || this.stopWithReason(var6) || this.hasAdjacentFluid(var6)) {
               return -1;
            }

            var4++;
         }
      }

      return var4;
   }

   private boolean collectDrops(BlockPos var1, int var2) {
      BlockState var3 = minecraft.level.getBlockState(var1);
      Inventory var4 = minecraft.player.getInventory();
      ItemStack var5 = var4.getItem(this.findDrillSlot());
      ItemStack var6 = var4.getItem(Math.max(0, this.getPlan(var3, this.findDrillSlot())));
      return var5.getDestroySpeed(var3) * var2 >= var6.getDestroySpeed(var3) && (!var3.requiresCorrectToolForDrops() || var5.isCorrectToolForDrops(var3));
   }

   private boolean setTarget(BlockPos var1, Direction var2, String var3) {
      return this.isSetAside(var1, var2, var3, false);
   }

   private boolean isSetAside(BlockPos var1, Direction var2, String var3, boolean var4) {
      if (this.rejectedDrillTargets.contains(var1)) {
         return false;
      }

      this.drillTarget = var1.immutable();
      this.drillFace = var2;
      this.jitterAim = var4;
      BlockHitResult var5 = BlockBreaker.canBreak(this, var1, this.getReach());
      if (var5 != null && var5.getDirection() == var2) {
         this.drillFaceActive = (boolean)1;
         this.drillFaceStates.clear();

         for (BlockPos var7 : getDrillBlocks(var1, var2)) {
            if (BlockBreaker.canBreak(var7)) {
               this.drillFaceStates.put(var7.immutable(), minecraft.level.getBlockState(var7));
            }
         }

         this.setTarget(var1);
         this.breakBlock(var1, true, var3);
         return true;
      } else {
         this.drillTarget = null;
         return false;
      }
   }

   private void getFootY(BlockPos var1, String var2) {
      if (this.isInsideArea()) {
         BlockHitResult var3 = BlockBreaker.canBreak(this, var1, this.getReach());
         if (var3 != null) {
            int var4 = this.countDrillBlocks(var1, var3.getDirection(), this.getFootY());
            if (var4 >= 2
               && this.collectDrops(var1, var4)
               && this.setTarget(var1, var3.getDirection(), "Cutting a 3x3 at " + var1.toShortString() + " (" + var4 + " blocks)")) {
               return;
            }
         }
      }

      this.mineBlock(var1, var2);
   }

   private boolean onEndTick(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      if (var1.kind() == TunnelPlanner.StepKind.DESCEND) {
         return true;
      }

      for (TunnelPlanner.Cell var4 : var1.cells()) {
         BlockPos var5 = new BlockPos(var4.cellX(), var4.cellY(), var4.cellZ());
         if (!this.skippedCells.contains(var5) && this.getReach(var5, var2) && !this.clearRoute(var1, var5)) {
            return false;
         }
      }

      return true;
   }

   private boolean clearRoute(TunnelPlanner.Step var1, BlockPos var2) {
      if (!this.sweepLeftoversSetting.getValue() && var1.drill() && !this.pickaxeOnly && this.findDrillSlot() >= 0) {
         TunnelPlanner.Cell var3 = var1.drillCentre();
         return var2.getX() != var3.cellX() || var2.getZ() != var3.cellZ() || var2.getY() > var3.cellY();
      } else {
         return false;
      }
   }

   private void runTick(boolean var1) {
      int var2 = !var1 && !this.standHadWork ? 0 : 1;
      if (var2 != 0) {
         this.reachableFloorTick = -1000000000;
      }

      this.standHadWork = (boolean)0;
      if (this.standIndex < this.standOrder.size()) {
         this.visitedStands.add(this.standOrder.get(this.standIndex));
      }

      this.standIndex++;
      this.walkStandIndex = -1;
      this.skippedCells.clear();
      if (var2 != 0) {
         this.standsDone++;
         if (this.random.nextDouble() < 0.035 && (this.pickaxeOnly || this.findDrillSlot() < 0)) {
            this.lookAroundUntilTick = minecraft.player.tickCount + this.random.nextInt(8, 22);
            this.lookAroundYaw = minecraft.player.getYRot() + (float)((this.random.nextBoolean() ? 1 : -1) * (20.0 + this.random.nextDouble() * 45.0));
            this.lookAroundPitch = Mth.clamp(minecraft.player.getXRot() - 12.0F + (float)this.random.nextDouble() * 24.0F, -30.0F, 60.0F);
         }
      }
   }

   private void sendMessage() {
      BlockBreaker.release(this);
      this.moveDirection = Vec3.ZERO;
      this.aimAt(minecraft.player.getEyePosition().add(Vec3.directionFromRotation(this.lookAroundPitch, this.lookAroundYaw).scale(6.0)), 12.0F);
      this.setStatus("Looking around");
   }

   private boolean waitForFallingBlocks() {
      AABB var1 = minecraft.player.getBoundingBox().inflate(4.0, 6.0, 4.0);
      if (minecraft.level.getEntitiesOfClass(FallingBlockEntity.class, var1).isEmpty()) {
         return false;
      }

      BlockBreaker.release(this);
      this.moveDirection = Vec3.ZERO;
      this.setStatus("Waiting for falling blocks to land");
      return true;
   }

   private BlockPos findPickaxeSlot(TunnelPlanner.Step var1, LayerScanPlan.Layer var2, boolean var3) {
      double var4 = this.getReach();
      int var6 = 0;

      for (TunnelPlanner.Cell var8 : var1.cells()) {
         BlockPos var9 = new BlockPos(var8.cellX(), var8.cellY(), var8.cellZ());
         if (!this.skippedCells.contains(var9) && this.getReach(var9, var2) && !this.clearRoute(var1, var9)) {
            if (!this.isInReach(var9)) {
               if (var3) {
                  this.skippedCells.add(var9);
               }
            } else {
               if (!var3 && var6++ >= 8) {
                  return null;
               }

               BlockHitResult var10 = BlockBreaker.canBreak(this, var9, var4);
               if (var10 != null && this.findBlockSlot(var9, var10)) {
                  return var9;
               }

               if (var3) {
                  this.skippedCells.add(var9);
                  LarpLog.debug(this.getName(), "No open face of {} from the stand at {}, {}; later", var9.toShortString(), var1.standX(), var1.standZ());
               }
            }
         }
      }

      return null;
   }

   private BlockPos onEndTick(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      if (var1.cells().isEmpty()) {
         return null;
      }

      Integer var3 = this.cellOrder.get(var1.cells().getFirst().key());
      if (var3 != null && var3 != 0) {
         double var4 = this.getReach();
         Vec3 var6 = minecraft.player.getEyePosition();
         int var7 = var1.standX();
         int var8 = var1.standZ();
         int var9 = var7 + var1.facingX();
         int var10 = var8 + var1.facingZ();
         ArrayList var11 = new ArrayList();

         for (BlockPos var13 : this.minableBlocks) {
            Integer var14 = this.cellOrder.get(TunnelPlanner.cellKey(var13.getX(), var13.getY(), var13.getZ()));
            if (var14 != null && var14 < var3 && !this.skippedCells.contains(var13)) {
               int var15 = (Math.abs(var13.getX() - var7) > 1 || Math.abs(var13.getZ() - var8) > 1)
                     && (Math.abs(var13.getX() - var9) > 1 || Math.abs(var13.getZ() - var10) > 1)
                  ? 0
                  : 1;
               if (var15 != 0 && !(var6.distanceToSqr(Vec3.atCenterOf(var13)) > var4 * var4) && this.getReach(var13, var2)) {
                  var11.add(var13);
               }
            }
         }

         if (var11.isEmpty()) {
            return null;
         }

         float var19 = minecraft.player.getYRot();
         float var20 = minecraft.player.getXRot();
         BlockPos var21 = this.lastMinedBlock;
         var11.sort(Comparator.comparingDouble(var6x -> setTarget(var6, var6x, var19, var20, var4, var21)));
         int var22 = 0;

         for (BlockPos var17 : var11) {
            if (var22++ >= 8) {
               break;
            }

            BlockHitResult var18 = BlockBreaker.canBreak(this, var17, var4);
            if (var18 != null && this.findBlockSlot(var17, var18)) {
               return var17;
            }

            this.skippedCells.add(var17);
         }

         return null;
      } else {
         return null;
      }
   }

   private void mineBlock(BlockPos var1, String var2) {
      this.drillTarget = null;
      this.drillFaceActive = (boolean)0;
      this.drillFaceStates.clear();
      this.setTarget(var1);
      this.breakBlock(var1, false, var2);
   }

   private boolean finishArea(TunnelPlanner.Step var1, LayerScanPlan.Layer var2) {
      BlockPos var3 = null;

      for (TunnelPlanner.Cell var5 : var1.cells()) {
         BlockPos var6 = new BlockPos(var5.cellX(), var5.cellY(), var5.cellZ());
         if (this.getReach(var6, var2)) {
            var3 = var6;
            break;
         }
      }

      if (this.failedWalkStandIndex == this.standIndex && minecraft.player.tickCount - this.failedWalkTick < 40) {
         return false;
      }

      if (!this.runTick(var2.bottom()).contains(GridPathfinder.reconstructPath(var1.standX(), var1.standZ()))) {
         return false;
      }

      if (this.walkStandIndex != this.standIndex || this.route.isEmpty() && this.routeIndex == 0 || minecraft.player.tickCount - this.routePlanTick > 40) {
         this.walkStandIndex = this.standIndex;
         this.routePlanTick = minecraft.player.tickCount;
         int var7 = var1.standX();
         int var8 = var1.standZ();
         List var9 = this.findRoute(var2.bottom(), (var2x, var3x) -> var2x == var7 && var3x == var8);
         if (var9 == null) {
            this.clearRoute();
            this.walkStandIndex = -1;
            this.failedWalkStandIndex = this.standIndex;
            this.failedWalkTick = minecraft.player.tickCount;
            return false;
         }

         if (var9.isEmpty()) {
            return true;
         }

         this.route = var9;
         this.routeIndex = 0;
         this.crouchUntilTick = 0;
         this.getArea(var2, var1);
         LarpLog.debug(
            this.getName(), "{} {} of {}: walking {} steps to {}, {}", var1.kind(), this.standIndex + 1, this.standOrder.size(), this.route.size(), var7, var8
         );
      }

      BlockBreaker.release(this);
      this.walkTarget = var3;
      this.getReach(var2);
      return true;
   }

   private void getArea(LayerScanPlan.Layer var1, TunnelPlanner.Step var2) {
      this.previousRoute = this.route;
      this.previousRouteGoal = null;
      GridPathfinder.GridPos var3 = this.route.getLast();
      boolean var4 = PathFollower.hasEscapeRoute(var3.blockX(), var1.bottom(), var3.blockZ());
      double var5 = var4 ? 0.0 : -0.18 + this.random.nextDouble() * 0.19999999999999998;
      double var7 = var4 ? 0.0 : (this.random.nextDouble() * 2.0 - 1.0) * 0.12;
      this.routeOffsetX = var2.facingX() * var5 - var2.facingZ() * var7;
      this.routeOffsetZ = var2.facingZ() * var5 + var2.facingX() * var7;
      this.arrivalRadius = var4 ? 0.245 : 0.15 + this.random.nextDouble() * 0.15;
      this.aimDrop = 0.35 + this.random.nextDouble() * 0.65;
      this.swayPhase = this.random.nextDouble() * Math.PI * 2.0;
   }

   private boolean setAside(LayerScanPlan.Layer var1) {
      int var2 = this.getFootY();
      TunnelPlanner.Step var3 = null;

      for (TunnelPlanner.Step var5 : this.standOrder) {
         if (var5.kind() == TunnelPlanner.StepKind.DESCEND && var5.standY() == var2) {
            var3 = var5;
            break;
         }
      }

      if (var3 == null) {
         return this.setAside("no step planned from Y " + var2);
      }

      if (var2 != this.stairY) {
         this.stairY = var2;
         this.stairAttempts = 0;
      }

      if (this.waitForFallingBlocks()) {
         return true;
      }

      BlockPos var13 = this.findBlockSlot();
      int var14 = var3.standX() + var3.facingX();
      int var6 = var3.standZ() + var3.facingZ();
      BlockPos var7 = var3.cells().isEmpty()
         ? new BlockPos(var14, var2 - 1, var6)
         : new BlockPos(var3.cells().getFirst().cellX(), var3.cells().getFirst().cellY(), var3.cells().getFirst().cellZ());
      if (var13.getX() == var14 && var13.getZ() == var6 && this.setStatus(new BlockPos(var14, var2 - 1, var6))) {
         this.resetState(var3, var2);
         return ++this.stairAttempts > 80 ? this.setAside("could not step down to " + new BlockPos(var14, var2 - 1, var6).toShortString()) : true;
      }

      if (var13.getX() == var3.standX() && var13.getZ() == var3.standZ()) {
         this.clearRoute();

         for (int var15 = var2 - 1; var15 <= var2 + 1; var15++) {
            if (!var1.contains(var15) && !this.setStatus(new BlockPos(var14, var15, var6))) {
               return this.setAside(
                  new BlockPos(var14, var15, var6).toShortString()
                     + (this.plan.getRegion().contains(var14, var15, var6) ? " was left by the layer above" : " above the area")
                     + " and is in the way"
               );
            }
         }

         BlockPos var16 = new BlockPos(var14, var2 - 1, var6);

         for (TunnelPlanner.Cell var18 : var3.cells()) {
            BlockPos var11 = new BlockPos(var18.cellX(), var18.cellY(), var18.cellZ());
            if (!this.setStatus(var11)) {
               AABB var12 = minecraft.player.getBoundingBox();
               if (var11.getY() == var2 - 1 && var12.maxX > var14 && var12.minX < var14 + 1 && var12.maxZ > var6 && var12.minZ < var6 + 1) {
                  BlockBreaker.release(this);
                  this.findTunnelStand(
                     new Vec3(var3.standX() + 0.5 - var3.facingX() * 0.1, minecraft.player.getY(), var3.standZ() + 0.5 - var3.facingZ() * 0.1)
                  );
                  this.setStatus("Stepping back from the edge");
                  if (++this.stairAttempts > 80) {
                     return this.setAside("could not get clear of " + var16.toShortString());
                  }

                  return true;
               }

               if (!this.getReach(var11, var1)) {
                  return this.setAside(var11.toShortString() + " cannot be mined (set aside, beside a fluid or unbreakable)");
               }

               if (var11.equals(var16) && !this.finishLayer(var16, var1)) {
                  return this.setAside("the drop at " + var16.toShortString() + " is not safe");
               }

               this.forceCrouch = (boolean)1;
               this.mineBlock(var11, "Cutting a step down to layer " + (this.currentLayer + 1));
               return true;
            }
         }

         if (!this.finishLayer(var16, var1)) {
            return this.setAside("the drop at " + var16.toShortString() + " is not safe");
         }

         this.resetState(var3, var2);
         return ++this.stairAttempts > 80 ? this.setAside("could not step down to " + var16.toShortString()) : true;
      } else {
         if (this.stairRouteY != var2 || this.route.isEmpty() && this.routeIndex == 0) {
            this.stairRouteY = var2;
            int var8 = var3.standX();
            int var9 = var3.standZ();
            List var10 = this.findRoute(var2, (var2x, var3x) -> var2x == var8 && var3x == var9);
            if (var10 == null) {
               return this.setAside("no way to the step at " + var8 + ", " + var2 + ", " + var9);
            }

            this.route = var10;
            this.routeIndex = 0;
            this.previousRoute = List.of();
            this.crouchUntilTick = 0;
         }

         BlockBreaker.release(this);
         this.walkTarget = var7;
         this.breakBlock(var2);
         this.setStatus("Walking to the step down to layer " + (this.currentLayer + 1));
         return true;
      }
   }

   private void resetState(TunnelPlanner.Step var1, int var2) {
      BlockBreaker.release(this);
      this.crouchUntilTick = 0;
      int var3 = var1.standX() + var1.facingX();
      int var4 = var1.standZ() + var1.facingZ();
      Vec3 var5 = minecraft.player.position();
      Vec3 var6 = new Vec3(var3 + 0.5 - var5.x, 0.0, var4 + 0.5 - var5.z);
      double var7 = var6.length();
      double var9 = var7 < 1.0E-6 ? 0.0 : minecraft.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var6.normalize());
      boolean var11 = this.rotator.updateLatch(var7 + 0.2, var9, InteractionArbiter.ownsInteraction(this));
      this.moveDirection = var11 ? var6 : Vec3.ZERO;
      this.aimAt(new Vec3(var3 + 0.5 + var1.facingX() * 0.7, var2 - 1, var4 + 0.5 + var1.facingZ() * 0.7), 12.0F);
      this.setStatus("Stepping down to layer " + (this.currentLayer + 1));
   }

   private boolean setAside(String var1) {
      this.stairDisabled = (boolean)1;
      this.clearRoute();
      this.forceCrouch = (boolean)0;
      LarpLog.info(this.getName(), "Layer {}: the stair down is not used ({}); digging down at the corner instead", this.currentLayer + 1, var1);
      return false;
   }

   private void setStatus(Set<BlockPos> var1, String var2) {
      int[] var3 = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
      int[] var4 = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};

      for (BlockPos var6 : var1) {
         var3[0] = Math.min(var3[0], var6.getX());
         var3[1] = Math.min(var3[1], var6.getY());
         var3[2] = Math.min(var3[2], var6.getZ());
         var4[0] = Math.max(var4[0], var6.getX());
         var4[1] = Math.max(var4[1], var6.getY());
         var4[2] = Math.max(var4[2], var6.getZ());
      }

      LayerScanPlan.Region var12 = LayerScanPlan.Region.fromCorners(var3, var4);
      this.blockSetName = var2.isBlank() ? "the block set" : var2;
      BlockPos var13 = this.findBlockSlot();
      double var7 = Math.max(
         Math.max(var12.minX() - var13.getX(), var13.getX() - var12.maxX()), Math.max(var12.minZ() - var13.getZ(), var13.getZ() - var12.maxZ())
      );
      if (var7 > 24.0) {
         this.stopWithReason("stand next to " + this.blockSetName + " to mine it out (you are " + (int)var7 + " blocks away)");
      } else {
         int var9 = this.findPickaxeSlot();
         int var10 = var9 < 0 && this.findDrillSlot() >= 0 ? 1 : 0;
         if (var9 < 0 && var10 == 0) {
            this.stopWithReason("put a pickaxe in your hotbar");
         } else {
            this.blockSet = var1;
            this.plan = new LayerScanPlan(
               var12, this.layerHeightSetting.getValue().intValue(), this.passWidthSetting.getValue().intValue(), var13.getX(), var13.getZ()
            );
            this.level = minecraft.level;
            this.player = minecraft.player;
            this.consumables.start();
            this.pickaxeOnly = (boolean)1;
            this.currentLayer = this.startLayer = 0;
            this.startMillis = System.currentTimeMillis();
            this.totalBlocks = this.remainingBlocks = this.countRemainingBlockSet();
            this.lastCountTick = minecraft.player.tickCount;
            this.previewUntilMillis = 0L;
            String var11 = minecraft.player.getInventory().getItem(var10 != 0 ? this.findDrillSlot() : var9).getHoverName().getString();
            this.sendMessage(
               "mining out "
                  + this.blockSetName
                  + ": "
                  + this.totalBlocks
                  + " blocks in "
                  + var12.describe()
                  + " with "
                  + var11
                  + (var10 != 0 ? ", only where its cut stays inside the schematic" : "")
                  + ", from the top down."
            );
            if (this.autoEatSetting.getValue() && AutoSustain.findHotbarSlot(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
               this.sendMessage("heads up: no food in the hotbar.");
            }

            LarpLog.info(
               this.getName(),
               "Block set started: {} cells of {} in {}, {} still standing, tool {}",
               var1.size(),
               this.blockSetName,
               var12.describe(),
               this.totalBlocks,
               var11
            );
         }
      }
   }

   private int countRemainingBlockSet() {
      int var1 = 0;

      for (BlockPos var3 : this.blockSet) {
         if (BlockBreaker.canBreak(var3)) {
            var1++;
         }
      }

      return var1;
   }

   private void onCornerClick() {
      if (!minecraft.player.onGround() && !minecraft.player.isInWater() && !this.walker.hasRemainingNodes()) {
         BlockBreaker.release(this);
         this.setStatus("Waiting to land");
      } else {
         this.resetRestoreCounts();
         this.isInReach();
         this.getReach();
         if (minecraft.player.tickCount - this.lastCountTick >= 100) {
            this.lastCountTick = minecraft.player.tickCount;
            this.remainingBlocks = this.countRemainingBlockSet();
         }

         if (this.collectingDrops) {
            this.collectDrops();
         } else {
            if (minecraft.player.tickCount - this.lastScanTick >= 40) {
               this.findRoute();
            }

            int var1 = this.setStatus();
            if (var1 < 0) {
               if (minecraft.player.tickCount - this.lastScanTick > 2) {
                  this.findRoute();
               } else {
                  byte var5 = 0;

                  for (BlockPos var4 : this.minableBlocks) {
                     if (this.setAsideUntil.containsKey(var4) && !this.abandonedBlocks.contains(var4) && BlockBreaker.canBreak(var4)) {
                        this.setAsideUntil.remove(var4);
                        var5 = 1;
                     }
                  }

                  if (var5 != 0) {
                     LarpLog.debug(this.getName(), "Retrying block-set cells set aside");
                     this.walker.reset();
                  } else {
                     this.walker.reset();
                     this.remainingBlocks = this.countRemainingBlockSet();
                     if (this.collectDropsSetting.getValue()) {
                        this.collectingDrops = (boolean)1;
                        this.setStatus("Picking up drops");
                        LarpLog.info(this.getName(), "Picking up drops around {}", this.blockSetName);
                     } else {
                        this.finishArea();
                     }
                  }
               }
            } else {
               this.currentLayer = var1;
               LayerScanPlan.Layer var2 = this.plan.layerAt(var1);
               BlockPos var3 = this.walker.hasRemainingNodes() && this.walker.getRemainingPath() != null ? null : this.mineBlock(var2);
               if (var3 != null) {
                  this.walker.reset();
                  this.getFootY(var3, null);
               } else {
                  BlockBreaker.release(this);
                  this.getPendingStands(var2);
               }
            }
         }
      }
   }

   private void findRoute() {
      this.lastScanTick = minecraft.player.tickCount;
      this.minableBlocks.clear();
      this.fluidBlocks.clear();
      int var1 = minecraft.player.tickCount;
      this.setAsideUntil.values().removeIf(var1x -> var1x <= var1);

      for (BlockPos var3 : this.blockSet) {
         if (minecraft.level.hasChunkAt(var3) && BlockBreaker.canBreak(var3)) {
            this.minableBlocks.add(var3);
         }
      }

      this.minableBlocks
         .sort(
            Comparator.<BlockPos>comparingInt(var1x -> this.plan.layerIndexOf(var1x.getY()))
               .thenComparingLong(var1x -> this.plan.sortKey(var1x.getX(), var1x.getY(), var1x.getZ(), this.plan.layerIndexOf(var1x.getY())))
         );
   }

   private int setStatus() {
      for (BlockPos var2 : this.minableBlocks) {
         int var3 = this.plan.layerIndexOf(var2.getY());
         if (var3 >= 0 && this.getReach(var2, this.plan.layerAt(var3))) {
            return var3;
         }
      }

      return -1;
   }

   private void getPendingStands(LayerScanPlan.Layer var1) {
      BlockPos var2 = BlockPos.containing(minecraft.player.getX(), minecraft.player.getY() + 0.2, minecraft.player.getZ());
      if (!this.walker.hasRemainingNodes() || minecraft.player.tickCount - this.lastBlockSetPlanTick > 100) {
         this.lastBlockSetPlanTick = minecraft.player.tickCount;
         Vec3 var3 = minecraft.player.getEyePosition();
         this.blockSetTargets.clear();
         this.minableBlocks
            .stream()
            .filter(var2x -> this.getReach(var2x, var1))
            .sorted(Comparator.comparingDouble(var1x -> var3.distanceToSqr(Vec3.atCenterOf(var1x))))
            .limit(8L)
            .forEach(this.blockSetTargets::add);
         if (this.blockSetTargets.isEmpty()) {
            this.walker.reset();
            return;
         }

         final double var4 = Math.pow(this.getReach() - 0.4, 2.0);
         final double var6 = minecraft.player.getEyeHeight();
         final ArrayList var8 = new ArrayList<>(this.blockSetTargets);
         AStarPathfinder.Goal var9 = new AStarPathfinder.Goal() {
            @Override
            public boolean reached(int var1, int var2x, int var3x) {
               for (BlockPos var5 : var8) {
                  if (var5.getX() != var1 || var5.getZ() != var3x || var5.getY() > var2x || var5.getY() < var2x - 1) {
                     double var6x = var1 + 0.5 - (var5.getX() + 0.5);
                     double var8x = var2x + var6 - (var5.getY() + 0.5);
                     double var10 = var3x + 0.5 - (var5.getZ() + 0.5);
                     if (var6x * var6x + var8x * var8x + var10 * var10 <= var4) {
                        return true;
                     }
                  }
               }

               return false;
            }

            @Override
            public double estimate(int var1, int var2x, int var3x) {
               double var4x = Double.MAX_VALUE;

               for (BlockPos var7 : var8) {
                  var4x = Math.min(
                     var4x,
                     Math.sqrt(
                        Math.pow(var1 + 0.5 - var7.getX() - 0.5, 2.0)
                           + Math.pow(var2x + var6 - var7.getY() - 0.5, 2.0)
                           + Math.pow(var3x + 0.5 - var7.getZ() - 0.5, 2.0)
                     )
                  );
               }

               return Math.max(0.0, var4x - Math.sqrt(var4));
            }
         };
         LayerScanPlan.Region var10 = this.plan.getRegion();
         AStarPathfinder.Bounds var11 = new AStarPathfinder.Bounds(
            Math.min(var10.minX(), var2.getX()) - 8,
            Math.min(var10.minY(), var2.getY()) - 4,
            Math.min(var10.minZ(), var2.getZ()) - 8,
            Math.max(var10.maxX(), var2.getX()) + 8,
            Math.max(var10.maxY(), var2.getY()) + 4,
            Math.max(var10.maxZ(), var2.getZ()) + 8
         );
         WorldPathView var12 = new WorldPathView(var1x -> this.getPlan(var1x, this.findDrillSlot()), false);
         if (this.finalSweep) {
            var12.setDigBounds(
               this.originalPlan.getRegion().minX(),
               this.originalPlan.getRegion().minY(),
               this.originalPlan.getRegion().minZ(),
               this.originalPlan.getRegion().maxX(),
               this.originalPlan.getRegion().maxY(),
               this.originalPlan.getRegion().maxZ()
            );
         } else {
            var12.setAllowedDigPositions(this.blockSet);
         }

         AStarPathfinder.PathResult var13 = AStarPathfinder.findPath(
            var12, var2.getX(), var2.getY(), var2.getZ(), var9, var11, AStarPathfinder.Options.digging(12000)
         );
         if (!var13.found() || var13.steps().isEmpty()) {
            this.walker.reset();
            String var14 = var13.found() ? "not visible from any standing spot nearby" : "no way to stand within reach";

            for (BlockPos var16 : this.blockSetTargets) {
               this.setAside(var16, var14);
            }

            LarpLog.debug(
               this.getName(), "Block set: {} for {} cells near {}", var14, this.blockSetTargets.size(), this.blockSetTargets.getFirst().toShortString()
            );
            if (this.isPickingCorners() >= 3) {
               this.setAsideLayer(var1, var14);
            }

            this.unreachableCount++;
            return;
         }

         this.walker.setPath(var13.steps());
         LarpLog.debug(this.getName(), "Block set route of {} steps toward {}", var13.steps().size(), this.blockSetTargets.getFirst().toShortString());
      }

      PathFollower.Result var17 = this.walker.tick(this.getReach(), var1x -> this.getPlan(var1x, this.findDrillSlot()), this.sprintSetting.getValue());
      this.moveDirection = this.walker.getMoveDirection();
      this.swimUp = this.walker.shouldJump();
      this.sneaking = this.walker.isBraking();
      this.sprinting = this.walker.shouldSprint();
      this.setStatus("Walking to " + (this.blockSetTargets.isEmpty() ? "the next block" : this.blockSetTargets.getFirst().toShortString()));
      if (var17 == PathFollower.Result.ARRIVED) {
         this.walker.reset();
         this.blockSetFailures = 0;
      } else if (var17 == PathFollower.Result.FAILED) {
         LarpLog.debug(this.getName(), "Block set route stuck: {}", this.walker.getRemainingPath());
         this.walker.reset();
         if (++this.blockSetFailures >= 3) {
            this.blockSetFailures = 0;

            for (BlockPos var5 : this.blockSetTargets) {
               this.setAside(var5, "stuck on the way");
            }
         }
      }
   }

   private void hasAdjacentFluid() {
      if (this.giveUpCount > 0) {
         LarpLog.info(this.getName(), "Gave up on {} blocks in the last 10 s (first {}): {}", this.giveUpCount, this.giveUpFirstPos, this.giveUpFirstReason);
         this.giveUpCount = 0;
         this.giveUpFirstPos = this.giveUpFirstReason = null;
      }

      if (this.asidePlaceCount != 0) {
         LarpLog.info(
            this.getName(),
            "Set aside {} blocks in {} places in the last 10 s (first around {}): {}",
            this.asideBlockCount,
            this.asidePlaceCount,
            this.asideFirstPos,
            this.asideFirstReason
         );
         this.asideBlockCount = this.asidePlaceCount = 0;
         this.asideFirstPos = this.asideFirstReason = null;
      }
   }

   private Set<Long> runTick(int var1) {
      BlockPos var2 = this.findBlockSlot();
      long var3 = GridPathfinder.reconstructPath(var2.getX(), var2.getZ());
      int var5 = minecraft.player.tickCount;
      if (var1 != this.reachableFloorY
         || this.blocksBroken != this.reachableFloorStamp
         || var5 - this.reachableFloorTick >= 40
         || var3 != this.reachableFloorStart && !this.reachableFloor.contains(var3)) {
         this.reachableFloorStart = var3;
         this.reachableFloorY = var1;
         this.reachableFloorStamp = this.blocksBroken;
         this.reachableFloorTick = var5;
         LayerScanPlan.Region var6 = this.plan.getRegion();
         GridPathfinder.SearchBounds var7 = new GridPathfinder.SearchBounds(
            Math.min(var6.minX(), var2.getX()) - 2,
            Math.min(var6.minZ(), var2.getZ()) - 2,
            Math.max(var6.maxX(), var2.getX()) + 2,
            Math.max(var6.maxZ(), var2.getZ()) + 2
         );
         HashSet var8 = new HashSet();
         ArrayDeque var9 = new ArrayDeque();
         var8.add(var3);
         var9.add(new long[]{var2.getX(), var2.getZ()});
         this.bridgingAllowed = (boolean)(this.placeBlocksSetting.getValue() && this.blockSet == null ? 1 : 0);

         try {
            while (!var9.isEmpty() && var8.size() < 20000) {
               long[] var10 = (long[])var9.poll();

               for (int[] var14 : new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}) {
                  int var15 = (int)var10[0] + var14[0];
                  int var16 = (int)var10[1] + var14[1];
                  long var17 = GridPathfinder.reconstructPath(var15, var16);
                  if (!var8.contains(var17) && var7.contains(var15, var16) && this.findPickaxeSlot(var15, var1, var16)) {
                     var8.add(var17);
                     var9.add(new long[]{var15, var16});
                  }
               }
            }
         } finally {
            this.bridgingAllowed = (boolean)0;
         }

         this.reachableFloor = var8;
         return this.reachableFloor;
      } else {
         return this.reachableFloor;
      }
   }

   private boolean clearCorners(BlockPos var1, LayerScanPlan.Layer var2) {
      Set var3 = this.runTick(var2.bottom());
      if (var3.size() > 4000) {
         return true;
      }

      double var4 = this.getReach() - 0.4;
      double var6 = var2.bottom() + minecraft.player.getEyeHeight();
      Vec3 var8 = Vec3.atCenterOf(var1);

      for (long var10 : var3) {
         double var12 = GridPathfinder.unpackX(var10) + 0.5 - var8.x;
         double var14 = var6 - var8.y;
         double var16 = GridPathfinder.unpackZ(var10) + 0.5 - var8.z;
         if (var12 * var12 + var14 * var14 + var16 * var16 <= var4 * var4) {
            return true;
         }
      }

      return false;
   }

   private int isPickingCorners() {
      if (this.blocksBroken != this.unreachableCountStamp) {
         this.unreachableCountStamp = this.blocksBroken;
         this.unreachableCount = 0;
      }

      return this.unreachableCount;
   }

   private void findTunnelStand(BlockPos var1, LayerScanPlan.Layer var2, String var3) {
      int var4 = minecraft.player.tickCount;
      int var5 = 0;

      for (BlockPos var7 : this.minableBlocks) {
         if (!var7.equals(var1) && var7.distChessboard(var1) <= 2 && this.getReach(var7, var2)) {
            this.setAsideUntil.put(var7, var4 + 400);
            var5++;
         }
      }

      if (var5 != 0) {
         this.asideBlockCount += var5;
         this.asidePlaceCount++;
         if (this.asideFirstPos == null) {
            this.asideFirstPos = var1.toShortString();
            this.asideFirstReason = var3;
         }
      }
   }

   private void setAsideLayer(LayerScanPlan.Layer var1, String var2) {
      int var3 = 0;

      for (BlockPos var5 : this.minableBlocks) {
         if (this.getReach(var5, var1)) {
            this.abandonedBlocks.add(var5.immutable());
            var3++;
         }
      }

      if (var3 != 0) {
         int var6 = this.placeBlocksSetting.getValue() && this.findBlockSlot() >= 0 ? 1 : 0;
         String var7 = var2 + (var6 != 0 ? "" : ", and there are no blocks in the hotbar to pillar up with");
         LarpLog.info(this.getName(), "Leaving {} blocks of {} at Y {} to {}: {}", var3, this.blockSetName, var1.bottom(), var1.top(), var7);
         this.sendMessage("left " + var3 + " blocks at Y " + var1.bottom() + " to " + var1.top() + ": " + var7 + ".");
         this.setStatus("Left " + var3 + " blocks out of reach");
      }
   }

   private void getReach() {
      if (this.targetBlock != null && !BlockBreaker.canBreak(this.targetBlock)) {
         if (minecraft.level.getBlockState(this.targetBlock).isAir()) {
            this.blocksBroken++;
            if (this.targetState != null) {
               this.brokenStates.put(this.targetBlock, this.targetState);
               int var1 = minecraft.player.tickCount;
               if (this.recentBreaks.size() < 256) {
                  this.recentBreaks.put(this.targetBlock, var1);
                  int var2 = getPlan(Math.sqrt(minecraft.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(this.targetBlock))));
                  this.breakDistanceBuckets.put(this.targetBlock, var2);
                  this.breaksByDistance[var2]++;
               }

               this.breakNotes
                  .put(
                     this.targetBlock,
                     String.format(
                        Locale.ROOT,
                        "broken with %s%s after %d ticks on it, %d ticks after the break before, %s",
                        minecraft.player.getMainHandItem().getHoverName().getString(),
                        this.drillFaceActive ? " as a drill face" : "",
                        var1 - this.breakStartTick,
                        var1 - this.lastBreakTick,
                        minecraft.player.onGround() ? "on the ground" : "in the air"
                     )
                  );
               this.lastBreakTick = var1;
               if (this.drillFaceActive && this.drillFaceStates.size() > 1 && this.stopMovement()) {
                  this.drillWaitUntilTick = var1 + 2 + this.random.nextInt(3);
                  this.drillSupportBlocks.clear();
                  this.drillSupportBlocks.addAll(this.drillFaceStates.keySet());
                  this.drillSupportUntilTick = var1 + 10;
               }
            }
         }

         for (Entry var4 : this.drillFaceStates.entrySet()) {
            if (!((BlockPos)var4.getKey()).equals(this.targetBlock) && minecraft.level.getBlockState((BlockPos)var4.getKey()).isAir()) {
               this.blocksBroken++;
               this.brokenStates.put((BlockPos)var4.getKey(), (BlockState)var4.getValue());
            }
         }

         this.drillFaceStates.clear();
         this.targetBlock = null;
         this.targetState = null;
      }
   }

   private BlockPos setTarget(BlockPos var1) {
      if (this.drillProbeBlock != null && !var1.equals(this.drillProbeBlock)) {
         this.ignoredProbeBlocks.add(var1.immutable());
      }

      BlockState var2 = minecraft.level.getBlockState(var1);
      BlockState var3 = this.brokenStates.remove(var1);
      String var4 = this.breakNotes.remove(var1);
      if (var3 != null && var3.getBlock() == var2.getBlock() && !(var2.getBlock() instanceof FallingBlock)) {
         LarpLog.info(this.getName(), "Server put back {} ({}): {}", var1.toShortString(), var2.getBlock().getName().getString(), var4);
         this.setStatus(var1);
      }

      if (!var1.equals(this.targetBlock)) {
         this.breakStartTick = minecraft.player.tickCount;
      }

      this.targetBlock = var1;
      this.targetState = var2;
      this.lastMinedBlock = var1.immutable();
      return var1;
   }

   private void endCornerPicking(LayerScanPlan.Layer var1) {
      this.lastScanTick = minecraft.player.tickCount;
      this.minableBlocks.clear();
      this.fluidBlocks.clear();
      LayerScanPlan.Region var2 = this.plan.getRegion();
      int var3 = minecraft.player.tickCount;
      this.setAsideUntil.values().removeIf(var1x -> var1x <= var3);

      for (int var4 = var1.top(); var4 >= var1.bottom(); var4--) {
         for (int var5 = var2.minX(); var5 <= var2.maxX(); var5++) {
            for (int var6 = var2.minZ(); var6 <= var2.maxZ(); var6++) {
               BlockPos var7 = new BlockPos(var5, var4, var6);
               if (minecraft.level.hasChunkAt(var7)) {
                  if (!minecraft.level.getFluidState(var7).isEmpty()) {
                     this.fluidBlocks.add(var7);
                  } else if (BlockBreaker.canBreak(var7)) {
                     this.minableBlocks.add(var7);
                  }
               }
            }
         }
      }

      int var8 = this.currentLayer;
      this.minableBlocks.sort(Comparator.comparingLong(var2x -> {
         Integer var3x = this.plannedLayer == var8 ? this.cellOrder.get(TunnelPlanner.cellKey(var2x.getX(), var2x.getY(), var2x.getZ())) : null;
         return var3x != null ? var3x.intValue() : 1125899906842624L + this.plan.sortKey(var2x.getX(), var2x.getY(), var2x.getZ(), var8);
      }));
   }

   private void setStatus(BlockPos var1) {
      int var2 = this.restoreCounts.merge(var1, 1, Integer::sum);
      if (var2 >= 3) {
         this.restoreCounts.remove(var1);
         this.restoredBlocks.add(var1.immutable());
         this.setAsideUntil.put(var1, minecraft.player.tickCount + 2400);
         this.sendMessage(var1.toShortString() + ": the server keeps putting it back, skipping it for now.");
         LarpLog.info(this.getName(), "Server restored {} {} times; skipping for {} ticks", var1.toShortString(), 3, 2400);
      }
   }

   private boolean isInReach() {
      int var1 = minecraft.player.tickCount;
      if (var1 - this.lastReportTick >= 600) {
         this.lastReportTick = var1;
         int var2 = 0;

         for (int var6 : this.breaksByDistance) {
            var2 += var6;
         }

         if (var2 > 0) {
            LarpLog.info(
               this.getName(),
               "Breaks put back by eye distance: under 2 {}/{}, 2-3 {}/{}, 3-3.5 {}/{}, 3.5-4 {}/{}, 4+ {}/{}",
               this.putBacksByDistance[0],
               this.breaksByDistance[0],
               this.putBacksByDistance[1],
               this.breaksByDistance[1],
               this.putBacksByDistance[2],
               this.breaksByDistance[2],
               this.putBacksByDistance[3],
               this.breaksByDistance[3],
               this.putBacksByDistance[4],
               this.breaksByDistance[4]
            );
         }
      }

      Vec3 var7 = minecraft.player.getEyePosition();
      this.recentBreaks
         .entrySet()
         .removeIf(
            var3 -> {
               int var4 = var1 - var3.getValue();
               if (var4 <= 200 && var4 >= 0) {
                  BlockState var5 = minecraft.level.getBlockState(var3.getKey());
                  if (var5.isAir()) {
                     return false;
                  }

                  Integer var6x = this.breakDistanceBuckets.remove(var3.getKey());
                  if (var6x != null) {
                     this.putBacksByDistance[var6x]++;
                  }

                  BlockBreaker.dumpTrace(this.getName(), var3.getKey());
                  LarpLog.info(
                     this.getName(),
                     "{} came back as {} {} ticks after the break, {} blocks from the eye",
                     var3.getKey().toShortString(),
                     var5.getBlock().getName().getString(),
                     var4,
                     String.format(Locale.ROOT, "%.1f", Math.sqrt(var7.distanceToSqr(Vec3.atCenterOf((Vec3i)var3.getKey()))))
                  );
                  if (var4 <= 5 && !(var5.getBlock() instanceof FallingBlock)) {
                     this.restoreMonitor.confirm(var3.getKey().asLong());
                  }

                  return true;
               } else {
                  this.breakDistanceBuckets.remove(var3.getKey());
                  return true;
               }
            }
         );
      if (!this.restoreMonitor.isSatisfied()) {
         return false;
      }

      LarpLog.info(this.getName(), "Stopping: the server took back {} of the last {} breaks", this.restoreMonitor.confirmedCount(), 10);
      BlockBreaker.release(this);
      this.stopWithReason(
         "the server put back " + this.restoreMonitor.confirmedCount() + " of your last 10 breaks, so it is refusing them and digging on only draws attention"
      );
      return true;
   }

   private void resetRestoreCounts() {
      if (minecraft.player.tickCount - this.lastRestoreResetTick >= 600) {
         this.lastRestoreResetTick = minecraft.player.tickCount;
         this.restoreCounts.clear();
      }
   }

   private boolean isSetAside(BlockPos var1) {
      Integer var2 = this.setAsideUntil.get(var1);
      return var2 != null && var2 > minecraft.player.tickCount || this.abandonedBlocks.contains(var1);
   }

   private void setAside(BlockPos var1, String var2) {
      int var3 = this.failureCounts.merge(var1, 1, Integer::sum);
      if (var3 >= 3) {
         this.abandonedBlocks.add(var1);
         if (this.giveUpCount++ == 0) {
            this.giveUpFirstPos = var1.toShortString();
            this.giveUpFirstReason = var2;
         }
      } else {
         this.setAsideUntil.put(var1, minecraft.player.tickCount + 400);
         LarpLog.debug(this.getName(), "Skipping {} for now: {}", var1.toShortString(), var2);
      }

      this.setStatus("Set aside " + var1.toShortString() + ": " + var2);
      if (var1.equals(this.targetBlock)) {
         this.targetBlock = null;
      }

      if (var1.equals(this.walkTarget)) {
         this.walkTarget = null;
      }
   }

   private boolean hasAdjacentFluid(BlockPos var1) {
      for (Direction var5 : Direction.values()) {
         if (var5 != Direction.DOWN && !minecraft.level.getFluidState(var1.relative(var5)).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private boolean setTarget(BlockPos var1) {
      if (this.pickaxeOnly) {
         return true;
      }

      int var2 = this.plan.laneIndexOf(var1.getX(), var1.getZ(), this.currentLayer);
      return !this.plan.isThinLane(var2, this.currentLayer) ? true : this.completedRows.contains(this.isInsideArea(var1.getX(), var1.getZ()));
   }

   private BlockPos mineBlock(LayerScanPlan.Layer var1) {
      double var2 = this.getReach();
      Vec3 var4 = minecraft.player.getEyePosition();
      if (this.targetBlock == null
         || !this.getReach(this.targetBlock, var1)
         || !(var4.distanceToSqr(Vec3.atCenterOf(this.targetBlock)) <= var2 * var2)
         || (
            this.drillFaceActive
               ? !this.findRoute(this.targetBlock)
               : !this.findBlockSlot(this.targetBlock, BlockBreaker.canBreak(this, this.targetBlock, var2))
         )) {
         this.targetBlock = null;
         this.targetState = null;
         this.drillFaceActive = (boolean)0;
         this.drillFaceStates.clear();
         if (!this.pickaxeOnly && this.findDrillSlot() >= 0) {
            BlockPos var5 = this.mineLayer(var1, var2, var4);
            if (var5 != null) {
               this.drillFaceActive = (boolean)1;
               this.drillFaceStates.clear();
               int var15 = this.plan.laneIndexOf(var5.getX(), var5.getZ(), this.currentLayer);
               boolean var17 = this.plan.rowDirection();

               for (int var18 = -1; var18 <= 1; var18++) {
                  for (int var19 = -1; var19 <= 1; var19++) {
                     BlockPos var21 = var17 ? var5.offset(0, var18, var19) : var5.offset(var19, var18, 0);
                     if (BlockBreaker.canBreak(var21)) {
                        this.drillFaceStates.put(var21, minecraft.level.getBlockState(var21));
                     }
                  }
               }

               LarpLog.debug(this.getName(), "Drill face at {} in row {} ({} blocks)", var5.toShortString(), var15 + 1, this.drillFaceStates.size());
               return this.setTarget(var5);
            }
         }

         ArrayList var13 = new ArrayList();

         for (BlockPos var7 : this.minableBlocks) {
            if (this.getReach(var7, var1) && this.setTarget(var7) && !(var4.distanceToSqr(Vec3.atCenterOf(var7)) > var2 * var2)) {
               var13.add(var7);
            }
         }

         float var14 = minecraft.player.getYRot();
         float var16 = minecraft.player.getXRot();
         BlockPos var8 = this.lastMinedBlock;
         var13.sort(Comparator.comparingDouble(var6 -> setTarget(var4, var6, var14, var16, var2, var8)));
         int var9 = 0;

         for (BlockPos var11 : var13) {
            if (var9++ >= 16) {
               break;
            }

            if (this.findBlockSlot(var11, BlockBreaker.canBreak(this, var11, var2))) {
               return this.setTarget(var11);
            }
         }

         for (BlockPos var22 : var13.stream()
            .sorted(Comparator.comparingLong(var1x -> this.plan.sortKey(var1x.getX(), var1x.getY(), var1x.getZ(), this.currentLayer)))
            .limit(4L)
            .toList()) {
            BlockPos var12 = this.isPickingCorners(var4, var22, var2);
            if (var12 != null && this.getReach(var12, var1) && this.isInReach(var12) && this.findBlockSlot(var12, BlockBreaker.canBreak(this, var12, var2))) {
               LarpLog.debug(this.getName(), "Digging through {} toward {}", var12.toShortString(), var22.toShortString());
               return this.setTarget(var12);
            }
         }

         return null;
      } else {
         return this.targetBlock;
      }
   }

   static double setTarget(Vec3 var0, BlockPos var1, float var2, float var3, double var4, BlockPos var6) {
      double var7 = Math.sqrt(var0.distanceToSqr(Vec3.atCenterOf(var1)));
      double var9 = isPickingCorners(var0, var1, var2, var3) + 10.0 * var7;
      if (var7 > var4 - 0.6) {
         var9 += 40.0;
      }

      if (var6 != null && var1.distManhattan(var6) == 1) {
         var9 -= 15.0;
      }

      return var9;
   }

   static double isPickingCorners(Vec3 var0, BlockPos var1, float var2, float var3) {
      float[] var4 = RotationManager.calculateAngles(var0, Vec3.atCenterOf(var1));
      return Math.hypot(Mth.wrapDegrees(var4[0] - var2), var4[1] - var3);
   }

   private BlockPos isPickingCorners(Vec3 var1, BlockPos var2, double var3) {
      Vec3 var5 = Vec3.atCenterOf(var2);
      if (var1.distanceToSqr(var5) > var3 * var3) {
         return null;
      }

      BlockHitResult var6 = minecraft.level.clip(new ClipContext(var1, var5, Block.OUTLINE, Fluid.NONE, minecraft.player));
      return var6.getType() == Type.BLOCK && !var6.getBlockPos().equals(var2) ? var6.getBlockPos() : null;
   }

   private boolean getReach(BlockPos var1, LayerScanPlan.Layer var2) {
      return var2.contains(var1.getY())
         && this.isInsideArea(var1)
         && BlockBreaker.canBreak(var1)
         && !this.isSetAside(var1)
         && !this.hasAdjacentFluid(var1)
         && !this.stopWithReason(var1);
   }

   private boolean stopWithReason(BlockPos var1) {
      AABB var2 = minecraft.player.getBoundingBox();
      return var1.getY() == this.getFootY() - 1
         && var2.maxX > var1.getX()
         && var2.minX < var1.getX() + 1
         && var2.maxZ > var1.getZ()
         && var2.minZ < var1.getZ() + 1;
   }

   private int findDrillSlot() {
      String var1 = this.drillNameSetting.getValue().trim().toLowerCase(Locale.ROOT);
      if (!var1.isEmpty() && this.isInGame()) {
         Inventory var2 = minecraft.player.getInventory();

         for (int var3 = 0; var3 < 9; var3++) {
            ItemStack var4 = var2.getItem(var3);
            if (!var4.isEmpty() && var4.getHoverName().getString().toLowerCase(Locale.ROOT).contains(var1)) {
               return var3;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private long isInsideArea(int var1, int var2) {
      int var3 = this.plan.laneIndexOf(var1, var2, this.currentLayer);
      int var4 = this.plan.rowDirection() ? var1 : var2;
      return (long)var3 << 32 ^ var4 & 4294967295L;
   }

   private BlockPos getPendingStands(int var1, int var2, LayerScanPlan.Layer var3) {
      int var4 = this.plan.laneCenter(var1, this.currentLayer);
      return this.plan.rowDirection() ? new BlockPos(var2, var3.bottom() + 1, var4) : new BlockPos(var4, var3.bottom() + 1, var2);
   }

   private Direction getFootY(int var1) {
      int var2 = this.plan.rowDirection(var1, this.currentLayer);
      return this.plan.rowDirection() ? (var2 > 0 ? Direction.WEST : Direction.EAST) : (var2 > 0 ? Direction.NORTH : Direction.SOUTH);
   }

   private boolean findRoute(BlockPos var1) {
      if (!var1.equals(this.drillTarget)) {
         return this.resetState(var1);
      }

      BlockHitResult var2 = BlockBreaker.canBreak(this, var1, this.getReach());
      return var2 != null && var2.getDirection() == this.drillFace;
   }

   private boolean resetState(BlockPos var1) {
      BlockHitResult var2 = BlockBreaker.canBreak(this, var1, this.getReach());
      int var3 = this.plan.laneIndexOf(var1.getX(), var1.getZ(), this.currentLayer);
      return var2 != null && var2.getDirection() == this.getFootY(var3);
   }

   private BlockPos mineLayer(LayerScanPlan.Layer var1, double var2, Vec3 var4) {
      HashSet var5 = new HashSet();

      for (BlockPos var7 : this.minableBlocks) {
         if (this.getReach(var7, var1)) {
            int var8 = this.plan.laneIndexOf(var7.getX(), var7.getZ(), this.currentLayer);
            if (this.plan.isThinLane(var8, this.currentLayer) && !this.completedRows.contains(this.isInsideArea(var7.getX(), var7.getZ()))) {
               long var9 = this.isInsideArea(var7.getX(), var7.getZ());
               if (var5.add(var9)) {
                  BlockPos var11 = this.getPendingStands(var8, this.plan.rowDirection() ? var7.getX() : var7.getZ(), var1);
                  if (this.abandonedBlocks.contains(var11) || !this.isSetAside(var11) || !BlockBreaker.canBreak(var11)) {
                     if (this.getReach(var11, var1)) {
                        if (!(var4.distanceToSqr(Vec3.atCenterOf(var11)) > var2 * var2) && this.resetState(var11)) {
                           return var11;
                        }

                        return null;
                     }

                     this.completedRows.add(var9);
                  }
               }
            }
         }
      }

      return null;
   }

   private double getReach() {
      return Math.min(this.reachSetting.getValue(), minecraft.player.blockInteractionRange());
   }

   private void sendMessage(BlockPos var1, boolean var2) {
      this.breakBlock(var1, var2, null);
   }

   private void breakBlock(BlockPos var1, boolean var2, String var3) {
      int var4 = this.findDrillSlot();
      BlockPos var5 = var1.immutable();
      if (!this.isInReach(var5)) {
         BlockBreaker.release(this);
         this.lastBreakTarget = null;
         this.setAside(var5, "out of reach from here");
      } else {
         if (var5.equals(this.lastBreakTarget)) {
            if (var5.equals(this.drillTarget) && this.ticksOnTarget >= 60 && !this.findTunnelStand(var5)) {
               LarpLog.info(
                  this.getName(), "Drill hit on {} dropped: the crosshair stayed off its {} face for {} ticks", var5.toShortString(), this.drillFace, 60
               );
               this.rejectedDrillTargets.add(var5);
               this.drillTarget = null;
               this.drillFaceActive = (boolean)0;
               this.drillFaceStates.clear();
               this.breakBlock(var5, false, var3);
               return;
            }

            if (++this.ticksOnTarget > 200) {
               BlockHitResult var6 = BlockBreaker.raycastLook(var5, this.getReach());
               LarpLog.info(
                  this.getName(),
                  "Could not break {} ({}) in {} ticks; crosshair {}, distance {}, phase {}",
                  var5.toShortString(),
                  BuiltInRegistries.BLOCK.getKey(minecraft.level.getBlockState(var5).getBlock()).getPath(),
                  200,
                  var6 == null ? "off the block" : "on " + var6.getDirection(),
                  String.format(Locale.ROOT, "%.2f", Math.sqrt(minecraft.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var5)))),
                  this.phase
               );
               this.lastBreakTarget = null;
               this.ticksOnTarget = 0;
               BlockBreaker.release(this);
               this.pillarBase = null;
               this.setAside(var5, "could not break it");
               return;
            }
         } else {
            this.lastBreakTarget = var5;
            this.ticksOnTarget = 0;
         }

         if (var4 < 0
            || !minecraft.player.isShiftKeyDown() && !minecraft.player.isCrouching()
            || !var2 && this.getPlan(minecraft.level.getBlockState(var5), var4) != var4) {
            this.countDrillBlocks(var5, var2 || var4 >= 0 && this.getPlan(minecraft.level.getBlockState(var5), var4) == var4);
            boolean var7 = BlockBreaker.breakBlock(
               this,
               var5,
               this.getReach(),
               true,
               12.0F,
               var4x -> {
                  int var5x = var2 && var4 >= 0 ? var4 : this.getPlan(var4x, var4);
                  this.lastToolSlot = var5x;
                  if (var5x >= 0 && this.isSpareSlot(var5x) && this.announcedTools.add(minecraft.player.getInventory().getItem(var5x).getItem())) {
                     LarpLog.info(
                        this.getName(),
                        "Auto Tool: {} for {}",
                        minecraft.player.getInventory().getItem(var5x).getHoverName().getString(),
                        BuiltInRegistries.BLOCK.getKey(var4x.getBlock()).getPath()
                     );
                  }

                  if (var5x == var4 && this.drillCutSetting.isMode("Auto") && this.drillCuts3x3 == null && this.drillProbeBlock == null) {
                     this.isInsideArea(var5);
                  }

                  return var5x;
               },
               () -> !this.isManualMovement() && !this.hasAdjacentFluid(var5) && this.endCornerPicking(var5) && this.findTunnelStand(var5)
            );
            this.setStatus(
               !var7 ? "Waiting for a reachable face" : (var3 != null ? var3 : (var2 ? "Cutting a 3x3 face at " : "Mining ") + var5.toShortString())
            );
            if (!var7) {
               this.setAside(var5, "no reachable face");
            }
         } else {
            this.crouchUntilTick = 0;
            BlockBreaker.release(this);
            this.setStatus("Standing up before the drill hit");
         }
      }
   }

   private void countDrillBlocks(BlockPos var1, boolean var2) {
      if (var2 || !this.crouchSometimesSetting.getValue()) {
         this.crouchUntilTick = 0;
      } else if (this.forceCrouch) {
         this.sneaking = (boolean)1;
      } else {
         int var3 = minecraft.player.tickCount;
         if (!var1.equals(this.crouchBlock)) {
            this.crouchBlock = var1;
            if (var3 >= this.crouchUntilTick && this.random.nextDouble() < 0.015) {
               this.crouchUntilTick = var3 + this.random.nextInt(20, 51);
            }
         }

         if (var3 < this.crouchUntilTick) {
            this.sneaking = (boolean)1;
         }
      }
   }

   private boolean endCornerPicking(BlockPos var1) {
      int var2 = minecraft.player.tickCount;
      if (!var1.equals(this.reactionBlock)) {
         this.reactionBlock = var1.immutable();
         this.reactionStartTick = -1000000000;
         this.reactionDelayTicks = countDrillBlocks(this.random.nextDouble());
      }

      if (this.reactionStartTick == -1000000000) {
         if (BlockBreaker.raycastLook(var1, this.getReach()) == null) {
            return false;
         }

         this.reactionStartTick = var2;
      }

      return var2 - this.reactionStartTick >= this.reactionDelayTicks;
   }

   static int countDrillBlocks(double var0) {
      return (int)Math.min(6.0, Math.floor(-Math.log(1.0 - Math.min(var0, 0.999999)) * 1.3));
   }

   private boolean findTunnelStand(BlockPos var1) {
      if (this.drillTarget != null && var1.equals(this.drillTarget)) {
         BlockHitResult var2 = BlockBreaker.raycastLook(var1, this.getReach());
         return var2 != null && var2.getDirection() == this.drillFace;
      } else {
         return true;
      }
   }

   private int findPickaxeSlot() {
      BlockState var1 = Blocks.STONE.defaultBlockState();
      int var2 = this.mineLayer(var1, this.findDrillSlot());
      return var2 >= 0 && minecraft.player.getInventory().getItem(var2).isCorrectToolForDrops(var1) ? var2 : -1;
   }

   private int getPlan(BlockState var1, int var2) {
      int var3 = this.findPickaxeSlot();
      Inventory var4 = minecraft.player.getInventory();
      int var5 = var3 >= 0 ? var3 : (var2 >= 0 ? var2 : var4.getSelectedSlot());
      if (!this.autoToolSetting.getValue()) {
         return var5;
      }

      ArrayList var6 = new ArrayList(9);

      for (int var7 = 0; var7 < 9; var7++) {
         var6.add(var4.getItem(var7));
      }

      int var9 = hasAdjacentFluid(var6, var1, var2, var5, var3 < 0, this.mineLayer());
      int var8 = var4.getSelectedSlot();
      return var8 != var9 && var8 >= 0 && var8 < 9 && this.isInReach((ItemStack)var6.get(var8), (ItemStack)var6.get(var9), var1, var8 == var2 && var3 >= 0)
         ? var8
         : var9;
   }

   private boolean isInReach(ItemStack var1, ItemStack var2, BlockState var3, boolean var4) {
      if (!var1.isEmpty() && var1.isDamageableItem() && !var4 && !var1.is(ItemTags.SWORDS) && AutoSustain.getRemainingDurability(var1) > this.mineLayer()) {
         ToolSelector.ToolScore var5 = ToolSelector.evaluateTool(var1, var3, true);
         ToolSelector.ToolScore var6 = ToolSelector.evaluateTool(var2, var3, true);
         return var5.usable() && var5.harvest() == var6.harvest() && var5.speed() >= var6.speed();
      } else {
         return false;
      }
   }

   static int hasAdjacentFluid(List<ItemStack> var0, BlockState var1, int var2, int var3, boolean var4, int var5) {
      ArrayList var6 = new ArrayList(var0.size());

      for (int var7 = 0; var7 < var0.size(); var7++) {
         ItemStack var8 = (ItemStack)var0.get(var7);
         ToolSelector.ToolScore var9 = ToolSelector.evaluateTool(var8, var1, true);
         int var10 = var7 != var3 && (var8.isEmpty() || var8.is(ItemTags.SWORDS) || var7 == var2 && !var4 || AutoSustain.getRemainingDurability(var8) <= var5)
            ? 0
            : 1;
         var6.add(var10 != 0 ? var9 : new ToolSelector.ToolScore(var9.speed(), var9.harvest(), false));
      }

      return ToolSelector.pickBestTool(var6, var3);
   }

   private int mineLayer() {
      return this.xpRepairSetting.getValue() ? this.repairAtSetting.getValue().intValue() : 1;
   }

   private boolean isSpareSlot(int var1) {
      return var1 != this.findDrillSlot() && var1 != this.findPickaxeSlot();
   }

   private boolean getPhaseName() {
      return this.findDrillSlot() >= 0 && this.findPickaxeSlot() < 0;
   }

   private boolean stopMovement() {
      if (this.drillCutSetting.isMode("3x3")) {
         return true;
      } else {
         return this.drillCutSetting.isMode("One Block") ? false : !Boolean.FALSE.equals(this.drillCuts3x3);
      }
   }

   private void setStatus() {
      if (!this.drillHintSent && this.drillCutSetting.isMode("Auto") && this.drillCuts3x3 == null) {
         this.drillHintSent = (boolean)1;
         this.sendMessage(
            "blocks are left because a 3x3 drill cut would reach outside the area, and no drill hit has shown yet how your drill cuts. If it is a normal pickaxe, set Drill Cut to One Block, or add a plain pickaxe to the hotbar."
         );
         LarpLog.info(this.getName(), "Drill Cut hint sent: drill-only, cut unknown, blocks left for cutting outside the area");
      }
   }

   private void isInsideArea(BlockPos var1) {
      BlockHitResult var2 = BlockBreaker.raycastLook(var1, this.getReach());
      Direction var3 = var2 != null ? var2.getDirection() : Direction.UP;
      this.drillProbeBlock = var1;
      this.drillProbeNeighbours.clear();
      Axis var4 = var3.getAxis();

      for (int var5 = -1; var5 <= 1; var5++) {
         for (int var6 = -1; var6 <= 1; var6++) {
            if (var5 != 0 || var6 != 0) {
               BlockPos var7 = var4 == Axis.X ? var1.offset(0, var5, var6) : (var4 == Axis.Y ? var1.offset(var5, 0, var6) : var1.offset(var5, var6, 0));
               if (BlockBreaker.canBreak(var7)) {
                  this.drillProbeNeighbours.put(var7.immutable(), minecraft.level.getBlockState(var7));
               }
            }
         }
      }

      this.drillProbeDeadline = 0;
      this.ignoredProbeBlocks.clear();
      LarpLog.debug(
         this.getName(), "Watching the first drill hit at {} on {} ({} blocks around it)", var1.toShortString(), var3, this.drillProbeNeighbours.size()
      );
   }

   private void isInReach() {
      if (this.drillProbeBlock != null) {
         if (this.drillProbeNeighbours.isEmpty()) {
            this.drillProbeBlock = null;
         } else {
            if (this.drillProbeDeadline == 0) {
               if (BlockBreaker.canBreak(this.drillProbeBlock)) {
                  return;
               }

               this.drillProbeDeadline = minecraft.player.tickCount + 10;
            }

            int var1 = 0;
            int var2 = 0;

            for (BlockPos var4 : this.drillProbeNeighbours.keySet()) {
               if (!this.ignoredProbeBlocks.contains(var4)) {
                  var2++;
                  if (minecraft.level.getBlockState(var4).isAir()) {
                     var1++;
                  }
               }
            }

            int var5 = var2 > 0 && var1 * 2 >= var2 ? 1 : 0;
            if (var5 != 0 || var2 == 0 || minecraft.player.tickCount >= this.drillProbeDeadline) {
               if (var5 != 0) {
                  this.drillCuts3x3 = true;
                  this.noCutHitCount = 0;
                  LarpLog.info(this.getName(), "Drill check: {} of {} blocks around the hit broke, so the drill cuts 3x3", var1, var2);
                  this.sendMessage("your drill cuts 3x3 here, so it mines full 3x3 faces.");
               } else if (var2 > 0 && ++this.noCutHitCount >= 3) {
                  this.drillCuts3x3 = false;
                  LarpLog.info(
                     this.getName(),
                     "Drill check: {} hits in a row broke one block ({} of {} around the last), so the drill breaks one block at a time",
                     3,
                     var1,
                     var2
                  );
                  this.sendMessage(
                     "your drill broke one block on 3 hits in a row here, so it is used like a normal pickaxe. Set Drill Cut to 3x3 if it really cuts 3x3."
                  );
                  this.pickaxeOnly = (boolean)1;
                  this.abandonedBlocks.removeAll(this.outsideCutBlocks);
                  this.failureCounts.keySet().removeAll(this.outsideCutBlocks);
                  this.outsideCutBlocks.clear();
                  this.setAsideUntil.clear();
               } else {
                  LarpLog.info(this.getName(), "Drill check: {} of {} blocks around the hit broke; checking the next drill hit too", var1, var2);
               }

               this.drillProbeBlock = null;
               this.drillProbeNeighbours.clear();
               this.ignoredProbeBlocks.clear();
            }
         }
      }
   }

   private boolean stopWithReason(BlockPos var1, Direction var2) {
      Axis var3 = var2.getAxis();

      for (int var4 = -1; var4 <= 1; var4++) {
         for (int var5 = -1; var5 <= 1; var5++) {
            BlockPos var6 = var3 == Axis.X ? var1.offset(0, var4, var5) : (var3 == Axis.Y ? var1.offset(var4, 0, var5) : var1.offset(var4, var5, 0));
            if (!this.isInsideArea(var6) && BlockBreaker.canBreak(var6)) {
               return false;
            }
         }
      }

      return true;
   }

   private boolean isInsideArea(BlockPos var1) {
      return this.plan.getRegion().contains(var1.getX(), var1.getY(), var1.getZ()) && (this.blockSet == null || this.blockSet.contains(var1));
   }

   private boolean findBlockSlot(BlockPos var1, BlockHitResult var2) {
      if (var2 == null) {
         return false;
      }

      if (this.getPhaseName() && this.stopMovement() && !this.stopWithReason(var1, var2.getDirection())) {
         if (this.getPlan(minecraft.level.getBlockState(var1), this.findDrillSlot()) != this.findDrillSlot()) {
            return true;
         }

         this.outsideCutBlocks.add(var1.immutable());
         return false;
      } else {
         return true;
      }
   }

   private int mineLayer(BlockState var1, int var2) {
      Inventory var3 = minecraft.player.getInventory();
      int var4 = -1;
      float var5 = -1.0F;

      for (int var6 = 0; var6 < 9; var6++) {
         if (var6 != var2) {
            ItemStack var7 = var3.getItem(var6);
            float var8 = var7.getDestroySpeed(var1) + (var7.isCorrectToolForDrops(var1) ? 1000 : 0);
            if (var8 > var5) {
               var5 = var8;
               var4 = var6;
            }
         }
      }

      return var4;
   }

   private boolean findTunnelStand(LayerScanPlan.Layer var1) {
      if (this.fluidBlocks.isEmpty()) {
         return false;
      }

      double var2 = this.getReach();
      Vec3 var4 = minecraft.player.getEyePosition();
      int var5 = this.findBlockSlot();

      for (BlockPos var7 : this.fluidBlocks) {
         if (!minecraft.level.getFluidState(var7).isEmpty()
            && this.plugCounts.getOrDefault(var7, 0) < 60
            && !(var4.distanceToSqr(Vec3.atCenterOf(var7)) > var2 * var2)) {
            if (var5 < 0) {
               this.mineBlock("no solid block in your hotbar to plug lava or water with");
               return false;
            }

            Item var8 = minecraft.player.getInventory().getItem(var5).getItem();
            BlockPos var9 = var7.immutable();
            if (NativePlacement.findPlacementFor(this, var9, var8, var2, true, true) != null) {
               if (this.waitForMenuToClose("plug " + (minecraft.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water"))) {
                  return true;
               }

               if (!this.findBlockSlot(var5)) {
                  this.setStatus("Taking out " + minecraft.player.getInventory().getItem(var5).getHoverName().getString() + " to plug a fluid");
                  return true;
               }

               if (minecraft.player.tickCount < this.nextPlaceTick) {
                  BlockBreaker.release(this);
                  this.setStatus("Steadying before the next block");
                  return true;
               }

               boolean var10 = NativePlacement.pruneInactive(
                  this,
                  var9,
                  var8,
                  var2,
                  true,
                  true,
                  () -> !minecraft.level.getFluidState(var9).isEmpty() && !this.isManualMovement(),
                  () -> this.stopWithReason(var9, var8, true),
                  var2x -> {
                     this.resetRestoreCounts(var9);
                     LarpLog.debug(this.getName(), "Plug at {} {}", var9.toShortString(), var2x ? "placed" : "rejected");
                  }
               );
               if (var10) {
                  if (this.plugCounts.merge(var9, 1, Integer::sum) == 1) {
                     LarpLog.info(
                        this.getName(), "Plugging {} at {}", minecraft.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water", var9.toShortString()
                     );
                  }

                  BlockBreaker.release(this);
                  this.setStatus("Plugging " + (minecraft.level.getFluidState(var9).is(FluidTags.LAVA) ? "lava" : "water") + " at " + var9.toShortString());
                  return true;
               }
            }
         }
      }

      return false;
   }

   private boolean findBlockSlot(int var1) {
      Inventory var2 = minecraft.player.getInventory();
      if (var2.getSelectedSlot() != var1) {
         BlockBreaker.release(this);
         var2.setSelectedSlot(var1);
         this.selectedSlot = var1;
         this.selectSlotTick = minecraft.player.tickCount;
         return false;
      }

      if (this.selectedSlot != var1) {
         this.selectedSlot = var1;
         this.selectSlotTick = minecraft.player.tickCount;
      }

      return minecraft.player.tickCount - this.selectSlotTick >= 3;
   }

   private float hasAdjacentFluid() {
      LocalPlayerMixin_4 var1 = (LocalPlayerMixin_4)minecraft.player;
      return (float)Math.hypot(Mth.wrapDegrees(minecraft.player.getYRot() - var1.larpYRotLast()), minecraft.player.getXRot() - var1.larpXRotLast());
   }

   private boolean stopWithReason(BlockPos var1, Item var2, boolean var3) {
      int var4 = minecraft.player.tickCount;
      if (!var1.equals(this.placeBlock)) {
         this.placeBlock = var1.immutable();
         this.placeSteadyTicks = 0;
         this.placeRequiredTicks = getArea(this.random.nextDouble());
         this.lastPlaceCheckTick = -1000000000;
      }

      if (this.lastPlaceCheckTick != var4) {
         this.lastPlaceCheckTick = var4;
         int var5 = var3 && !(minecraft.player.getDeltaMovement().horizontalDistanceSqr() < 4.0E-4) ? 0 : 1;
         int var6 = var5 != 0 && this.hasAdjacentFluid() <= 1.5F && NativePlacement.findPlacementFor(this, var1, var2, this.getReach(), false, true) != null
            ? 1
            : 0;
         this.placeSteadyTicks = var6 != 0 ? this.placeSteadyTicks + 1 : 0;
      }

      return this.placeSteadyTicks >= this.placeRequiredTicks && var4 >= this.nextPlaceTick;
   }

   static int getArea(double var0) {
      return 2 + (int)Math.round(4.0 * var0 * var0);
   }

   static int waitForMenuToClose(double var0) {
      return 8 + (int)Math.round(8.0 * var0 * var0);
   }

   private void resetRestoreCounts(BlockPos var1) {
      int var2 = minecraft.player.tickCount;
      this.nextPlaceTick = var2 + waitForMenuToClose(this.random.nextDouble());
      this.lastPlacedBlock = var1.immutable();
      this.placeSettleTick = var2 + 5;
      this.placeCounts.merge(var1.immutable(), 1, Integer::sum);
      this.placeBlock = null;
   }

   private boolean waitForMenuToClose(String var1) {
      Screen var2 = minecraft.gui.screen();
      if (var2 != null && (!this.allowsScreenInteractions() || var2 instanceof AbstractContainerScreen)) {
         this.setStatus("Waiting for the menu to close to " + var1);
         return true;
      } else {
         return false;
      }
   }

   private int findBlockSlot() {
      Inventory var1 = minecraft.player.getInventory();
      int var2 = this.findDrillSlot();

      for (int var3 = 0; var3 < 9; var3++) {
         if (var3 != var2) {
            ItemStack var4 = var1.getItem(var3);
            if (var4.getItem() instanceof BlockItem var5 && waitForFallingBlocks(var5)) {
               return var3;
            }
         }
      }

      return -1;
   }

   static boolean waitForFallingBlocks(BlockItem var0) {
      net.minecraft.world.level.block.Block var1 = var0.getBlock();
      BlockState var2 = var1.defaultBlockState();
      return !(var1 instanceof FallingBlock)
         && !var2.hasBlockEntity()
         && !var2.ignitedByLava()
         && var2.isCollisionShapeFullBlock(EmptyBlockGetter.INSTANCE, BlockPos.ZERO)
         && var1 != Blocks.ICE
         && var1 != Blocks.TNT
         && var1 != Blocks.MAGMA_BLOCK;
   }

   private void mineBlock(String var1) {
      if (!var1.equals(this.lastHint)) {
         this.lastHint = var1;
         this.sendMessage(var1 + ".");
      }
   }

   private BlockPos isSetAside(LayerScanPlan.Layer var1) {
      if (this.blocksBroken != this.unreachableStamp) {
         this.unreachableBlocks.clear();
         this.unreachableStamp = this.blocksBroken;
      }

      BlockPos var2 = null;
      BlockPos var3 = null;
      double var4 = 36.0;
      double var6 = minecraft.player.getX();
      double var8 = minecraft.player.getZ();
      int var10 = !this.pickaxeOnly && this.findDrillSlot() >= 0 ? 1 : 0;

      for (BlockPos var12 : this.minableBlocks) {
         double var13 = var12.getX() + 0.5 - var6;
         double var15 = var12.getZ() + 0.5 - var8;
         double var17 = var13 * var13 + var15 * var15;
         if ((var2 == null || !(var17 >= var4)) && this.getReach(var12, var1)) {
            boolean var19 = this.setTarget(var12);
            if (var19 || var10 != 0) {
               if (var2 == null) {
                  var2 = var12;
               }

               if (var19 && var17 < var4 && !this.unreachableBlocks.contains(var12) && this.resetRestoreCounts(var12)) {
                  var4 = var17;
                  var3 = var12;
               }
            }
         }
      }

      this.fallbackTarget = var3 != null && !var3.equals(var2) ? var3 : null;
      return var3 != null ? var3 : var2;
   }

   private boolean resetRestoreCounts(BlockPos var1) {
      for (Direction var5 : Direction.values()) {
         BlockPos var6 = var1.relative(var5);
         if (minecraft.level.getBlockState(var6).getCollisionShape(minecraft.level, var6).isEmpty()) {
            return true;
         }
      }

      return false;
   }

   private void findPickaxeSlot(BlockPos var1, LayerScanPlan.Layer var2) {
      if (!this.clearCorners(var1, var2)) {
         this.setAside(var1, "no way to walk within reach");
         this.findTunnelStand(var1, var2, "no way to walk within reach");
         this.clearRoute();
      } else {
         int var3 = !this.pickaxeOnly && this.findDrillSlot() >= 0 && !this.setTarget(var1) ? 1 : 0;
         BlockPos var4 = var3 != 0
            ? this.getPendingStands(
               this.plan.laneIndexOf(var1.getX(), var1.getZ(), this.currentLayer), this.plan.rowDirection() ? var1.getX() : var1.getZ(), var2
            )
            : var1;
         if (!var4.equals(this.walkTarget) || minecraft.player.tickCount - this.routePlanTick > 40 || this.route.isEmpty() && this.routeIndex == 0) {
            this.walkTarget = var4;
            this.routePlanTick = minecraft.player.tickCount;
            List var5 = this.findRoute(var2.bottom(), var3 != 0 ? this.setDrill(var4) : this.isPickingCorners(var4, var2));
            if ((var5 == null || var5.isEmpty()) && var1.equals(this.fallbackTarget)) {
               this.unreachableBlocks.add(var1.immutable());
               this.fallbackTarget = null;
               this.clearRoute();
               return;
            }

            if (var5 == null || var5.isEmpty()) {
               if (var3 != 0) {
                  this.completedRows.add(this.isInsideArea(var1.getX(), var1.getZ()));
                  LarpLog.debug(
                     this.getName(),
                     "Opening slice at {} with the pickaxe: {}",
                     var4.toShortString(),
                     var5 == null ? "no route to the row line" : "not in line"
                  );
               } else {
                  if (var5 != null
                     && this.isPickingCorners(minecraft.player.getEyePosition(), var1, this.getReach()) instanceof BlockPos var6
                     && this.getReach(var6, var2)
                     && this.findBlockSlot(var6, BlockBreaker.canBreak(this, var6, this.getReach()))) {
                     this.clearRoute();
                     this.setTarget(var6);
                     this.drillFaceActive = (boolean)0;
                     this.sendMessage(var6, false);
                     return;
                  }

                  if (var5 != null && this.outsideCutBlocks.contains(var1)) {
                     this.setStatus();
                  }

                  String var8 = var5 == null
                     ? "no way to walk within reach"
                     : (this.outsideCutBlocks.contains(var1) ? "the drill would cut outside the area" : "not visible from any standing spot nearby");
                  this.setAside(var1, var8);
                  if (this.isPickingCorners() >= 2) {
                     this.findTunnelStand(var1, var2, var8);
                  }

                  this.unreachableCount++;
               }

               this.clearRoute();
               return;
            }

            this.route = var5;
            this.routeIndex = 0;
            this.crouchUntilTick = 0;
            this.resetState(var2, var4);
            LarpLog.debug(this.getName(), "Route of {} steps toward {}", this.route.size(), var4.toShortString());
         }

         this.getReach(var2);
      }
   }

   private void resetState(LayerScanPlan.Layer var1, BlockPos var2) {
      GridPathfinder.GridPos var3 = this.route.getLast();
      GridPathfinder.GridPos var4 = this.previousRoute.isEmpty() ? null : this.previousRoute.getLast();
      int var5 = var2.equals(this.previousRouteGoal) && var3.equals(var4) ? 1 : 0;
      this.previousRoute = this.route;
      this.previousRouteGoal = var2;
      if (var5 == 0) {
         boolean var6 = PathFollower.hasEscapeRoute(var3.blockX(), var1.bottom(), var3.blockZ());
         this.routeOffsetX = var6 ? 0.0 : (this.random.nextDouble() * 2.0 - 1.0) * 0.18;
         this.routeOffsetZ = var6 ? 0.0 : (this.random.nextDouble() * 2.0 - 1.0) * 0.18;
         this.arrivalRadius = var6 ? 0.245 : 0.15 + this.random.nextDouble() * 0.15;
         this.aimDrop = 0.35 + this.random.nextDouble() * 0.65;
         this.swayPhase = this.random.nextDouble() * Math.PI * 2.0;
      }
   }

   private Vec3 getRoutePoint(int var1, double var2) {
      GridPathfinder.GridPos var4 = this.route.get(var1);
      int var5 = this.route == this.previousRoute && var1 == this.route.size() - 1 ? 1 : 0;
      return new Vec3(var4.blockX() + 0.5 + (var5 != 0 ? this.routeOffsetX : 0.0), var2, var4.blockZ() + 0.5 + (var5 != 0 ? this.routeOffsetZ : 0.0));
   }

   static boolean getRoutePoint(Vec3 var0, Vec3 var1) {
      double var2 = Math.hypot(var0.x, var0.z);
      double var4 = Math.hypot(var1.x, var1.z);
      return !(var2 < 1.0E-6) && !(var4 < 1.0E-6) ? (var0.x * var1.x + var0.z * var1.z) / (var2 * var4) >= mAX_TURN_COS : true;
   }

   public static void getReach(LocalPlayer var0) {
      if (instance != null && instance.isEnabled()) {
         AutoAreaMiner var1 = instance;
         if (var1 != null && var1.plan != null && var0 == var1.player) {
            var1.positionBeforeTick = var0.position();
            var1.speedBeforeTick = var0.getDeltaMovement().horizontalDistance();
         }
      }
   }

   public static void getCurrentLayer(LocalPlayer var0) {
      if (instance != null && instance.isEnabled()) {
         AutoAreaMiner var1 = instance;
         if (var1 != null && var1.plan != null && var0 == var1.player && var1.isRuntimeActive()) {
            Vec3 var2 = var1.positionBeforeTick;
            var1.positionBeforeTick = null;
            double var3 = var2 == null ? -1.0 : var2.distanceTo(var0.position());
            if (!(var3 >= 0.0) || !(var3 < 0.001)) {
               int var5 = ++var1.correctionCount;
               if (var5 <= 30 || var5 % 10 == 0) {
                  LarpLog.info(
                     var1.getName(),
                     "Server {} you {} blocks (correction {} this run) while: {}; walking {}, sprint {}, sneak {}, on ground {}, speed {}",
                     var3 > 16.0 ? "teleported" : "moved",
                     var3 < 0.0 ? "?" : String.format(Locale.ROOT, "%.2f", var3),
                     var5,
                     var1.getStatus(),
                     var1.moveDirection.lengthSqr() > 1.0E-6,
                     var0.isSprinting(),
                     var0.isShiftKeyDown(),
                     var0.onGround(),
                     String.format(Locale.ROOT, "%.3f", var1.speedBeforeTick)
                  );
               }
            }
         }
      }
   }

   private GridPathfinder.GoalTest isPickingCorners(BlockPos var1, LayerScanPlan.Layer var2) {
      double var3 = this.getReach() - 0.4;
      Vec3 var5 = Vec3.atCenterOf(var1);
      double var6 = var2.bottom() + minecraft.player.getEyeHeight();
      return (var6x, var7) -> {
         double var8 = var6x + 0.5 - var5.x;
         double var10 = var7 + 0.5 - var5.z;
         double var12 = var6 - var5.y;
         return var8 * var8 + var12 * var12 + var10 * var10 <= var3 * var3
            && (var6x != var1.getX() || var7 != var1.getZ())
            && BlockBreaker.describeDigState(new Vec3(var6x + 0.5, var6, var7 + 0.5), var1, var3);
      };
   }

   private GridPathfinder.GoalTest setDrill(BlockPos var1) {
      int var2 = this.plan.laneIndexOf(var1.getX(), var1.getZ(), this.currentLayer);
      int var3 = this.plan.rowDirection(var2, this.currentLayer);
      int var4 = this.plan.laneCenter(var2, this.currentLayer);
      int var5 = this.plan.rowDirection() ? var1.getX() : var1.getZ();
      int var6 = Math.max(1, (int)Math.floor(this.getReach() - 0.5));
      Direction var7 = this.getFootY(var2);
      double var8 = var1.getY() - 1 + minecraft.player.getEyeHeight();
      double var10 = this.getReach() - 0.4;
      Vec3 var12 = Vec3.atCenterOf(var1);
      return (var12x, var13) -> {
         int var14 = this.plan.rowDirection() ? var13 : var12x;
         int var15 = this.plan.rowDirection() ? var12x : var13;
         int var16 = (var5 - var15) * var3;
         if (var16 < 1 || var16 > var6) {
            return false;
         }

         if (var14 == var4) {
            return true;
         }

         if (Math.abs(var14 - var4) == 1 && var16 >= 2) {
            Vec3 var17 = new Vec3(var12x + 0.5, var8, var13 + 0.5);
            if (var17.distanceToSqr(var12) > var10 * var10) {
               return false;
            }

            BlockHitResult var18 = minecraft.level.clip(new ClipContext(var17, var12, Block.OUTLINE, Fluid.NONE, minecraft.player));
            return var18.getType() == Type.BLOCK && var18.getBlockPos().equals(var1) && var18.getDirection() == var7;
         } else {
            return false;
         }
      };
   }

   private boolean hasAdjacentFluid() {
      LayerScanPlan.Region var1 = this.plan.getRegion();
      BlockPos var2 = this.findBlockSlot();
      byte var3 = 3;
      int var4 = var2.getX() >> 4;
      int var5 = var2.getZ() >> 4;
      int var6 = Math.max(var1.minX() - 2 >> 4, var4 - var3);
      int var7 = Math.min(var1.maxX() + 2 >> 4, var4 + var3);
      int var8 = Math.max(var1.minZ() - 2 >> 4, var5 - var3);
      int var9 = Math.min(var1.maxZ() + 2 >> 4, var5 + var3);

      for (int var10 = var6; var10 <= var7; var10++) {
         for (int var11 = var8; var11 <= var9; var11++) {
            if (!minecraft.level.getChunkSource().hasChunk(var10, var11)) {
               return false;
            }
         }
      }

      return true;
   }

   private List<GridPathfinder.GridPos> findRoute(int var1, GridPathfinder.GoalTest var2) {
      LayerScanPlan.Region var3 = this.plan.getRegion();
      BlockPos var4 = this.findBlockSlot();
      GridPathfinder.SearchBounds var5 = new GridPathfinder.SearchBounds(
         Math.min(var3.minX(), var4.getX()) - 2,
         Math.min(var3.minZ(), var4.getZ()) - 2,
         Math.max(var3.maxX(), var4.getX()) + 2,
         Math.max(var3.maxZ(), var4.getZ()) + 2
      );
      this.bridgingAllowed = (boolean)0;
      List var6 = GridPathfinder.findPath(
         (var3x, var4x) -> this.findPickaxeSlot(var3x, var1, var4x) || var3x == var4.getX() && var4x == var4.getZ(),
         var4.getX(),
         var4.getZ(),
         var2,
         var5,
         20000
      );
      if (var6 == null && this.placeBlocksSetting.getValue()) {
         this.bridgingAllowed = (boolean)1;

         try {
            return GridPathfinder.findPath(
               (var3x, var4x) -> this.findPickaxeSlot(var3x, var1, var4x) || var3x == var4.getX() && var4x == var4.getZ(),
               var4.getX(),
               var4.getZ(),
               var2,
               var5,
               20000
            );
         } finally {
            this.bridgingAllowed = (boolean)0;
         }
      } else {
         return var6;
      }
   }

   private boolean setStatus(BlockPos var1) {
      BlockState var2 = minecraft.level.getBlockState(var1);
      return var2.getCollisionShape(minecraft.level, var1).isEmpty()
         && minecraft.level.getFluidState(var1).isEmpty()
         && !var2.is(Blocks.COBWEB)
         && !var2.is(Blocks.FIRE)
         && !var2.is(Blocks.SOUL_FIRE)
         && !var2.is(Blocks.SWEET_BERRY_BUSH)
         && !var2.is(Blocks.POWDER_SNOW);
   }

   private boolean clearCorners(BlockPos var1) {
      BlockState var2 = minecraft.level.getBlockState(var1);
      return (var2.isFaceSturdy(minecraft.level, var1, Direction.UP) || this.getReach(var2, var1))
         && !var2.is(Blocks.MAGMA_BLOCK)
         && minecraft.level.getFluidState(var1).isEmpty();
   }

   private boolean getReach(BlockState var1, BlockPos var2) {
      VoxelShape var3 = var1.getCollisionShape(minecraft.level, var2);
      return findDrillSlot(var3);
   }

   static boolean findDrillSlot(VoxelShape var0) {
      if (var0.isEmpty()) {
         return false;
      }

      AABB var1 = var0.bounds();
      return var1.maxY >= 0.75 && var1.maxY <= 1.0 && var1.minX <= 0.0 && var1.maxX >= 1.0 && var1.minZ <= 0.0 && var1.maxZ >= 1.0;
   }

   static int setDrill(double var0) {
      return Mth.floor(var0 + 0.25);
   }

   private int getFootY() {
      return setDrill(minecraft.player.getY());
   }

   private BlockPos findBlockSlot() {
      return new BlockPos(minecraft.player.getBlockX(), this.getFootY(), minecraft.player.getBlockZ());
   }

   private boolean getPendingStands(BlockPos var1) {
      if (this.blockSet == null
         && this.bridgingAllowed
         && !this.unbridgeableBlocks.contains(var1)
         && this.placeBlocksSetting.getValue()
         && NativePlacement.isReplaceable(var1)
         && minecraft.level.getFluidState(var1).isEmpty()) {
         for (Direction var5 : Direction.values()) {
            if (var5 != Direction.UP && NativePlacement.getFailureReason(var1.relative(var5))) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   boolean findPickaxeSlot(int var1, int var2, int var3) {
      BlockPos var4 = new BlockPos(var1, var2, var3);
      if (!minecraft.level.hasChunkAt(var4)) {
         return false;
      }

      BlockPos var5 = var4.below();
      return this.setStatus(var4) && this.setStatus(var4.above()) && (this.clearCorners(var5) || this.getPendingStands(var5) && this.findBlockSlot() >= 0);
   }

   private void getReach(LayerScanPlan.Layer var1) {
      if (this.routeIndex >= this.route.size()) {
         this.clearRoute();
      } else {
         Vec3 var2 = minecraft.player.position();
         int var3 = this.stopMovement(var2, var1.bottom());

         while (this.routeIndex < var3 && this.clearRoute(var2, this.routeIndex, var3)) {
            this.routeIndex++;
         }

         int var4 = var3 > this.routeIndex ? 1 : 0;
         GridPathfinder.GridPos var5 = this.route.get(this.routeIndex);
         Vec3 var6 = this.getRoutePoint(this.routeIndex, var2.y);
         int var7 = this.routeIndex == this.route.size() - 1 ? 1 : 0;
         double var8 = this.route == this.previousRoute ? this.arrivalRadius : 0.245;
         if (var2.distanceToSqr(var6) < (var7 != 0 ? var8 * var8 : (var4 != 0 ? 0.49 : 0.12))) {
            this.routeIndex++;
            if (this.routeIndex >= this.route.size()) {
               this.clearRoute();
               this.setStatus("Arrived; looking for the next block");
               return;
            }

            var5 = this.route.get(this.routeIndex);
            var6 = this.getRoutePoint(this.routeIndex, var2.y);
            var7 = this.routeIndex == this.route.size() - 1 ? 1 : 0;
         }

         BlockPos var10 = new BlockPos(var5.blockX(), var1.bottom() - 1, var5.blockZ());
         if (var10.equals(this.lastPlacedBlock) && minecraft.player.tickCount < this.placeSettleTick) {
            this.setStatus("Waiting for the placed block to land");
         } else if (!this.clearCorners(var10)) {
            if (!this.findDrillSlot(var10)) {
               this.unbridgeableBlocks.add(var10.immutable());
               this.getCurrentLayer("could not bridge the gap at " + var10.toShortString());
            }
         } else {
            double var11 = Math.sqrt(var2.distanceToSqr(var6));

            for (int var13 = this.routeIndex + 1; var13 < this.route.size(); var13++) {
               GridPathfinder.GridPos var14 = this.route.get(var13 - 1);
               GridPathfinder.GridPos var15 = this.route.get(var13);
               var11 += Math.hypot(var15.blockX() - var14.blockX(), var15.blockZ() - var14.blockZ());
            }

            Vec3 var24 = (var4 != 0 && var3 < this.route.size() ? this.getRoutePoint(var3, var2.y) : var6).subtract(var2).multiply(1.0, 0.0, 1.0);
            Vec3 var25 = minecraft.player.getDeltaMovement();
            double var26 = var24.lengthSqr() < 1.0E-6 ? 0.0 : var25.multiply(1.0, 0.0, 1.0).dot(var24.normalize());
            GridPathfinder.GridPos var17 = this.route.get(this.route.size() - 1);
            this.sneaking = (boolean)(var11 < 0.9 && PathFollower.hasEscapeRoute(var17.blockX(), var1.bottom(), var17.blockZ()) ? 1 : 0);
            boolean var18 = this.rotator.updateLatch(this.sneaking ? var11 : var11 - 0.1, var26, InteractionArbiter.ownsInteraction(this));
            this.moveDirection = var18 ? var24 : Vec3.ZERO;
            this.sprinting = (boolean)(var18
                  && this.sprintSetting.getValue()
                  && !this.sneaking
                  && var11 > 4.0
                  && minecraft.player.getFoodData().getFoodLevel() > 6
               ? 1
               : 0);
            if (this.walkTarget == null
               || this.collectingDrops
               || !(minecraft.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(this.walkTarget)) <= 64.0)
               || !(var11 < 1.5) && !getRoutePoint(var24, Vec3.atCenterOf(this.walkTarget).subtract(var2))) {
               double var19 = minecraft.player.getEyeY()
                  - (this.route == this.previousRoute ? this.aimDrop : 0.6)
                  + 0.12 * Math.sin(minecraft.player.tickCount * 0.09 + this.swayPhase);
               ArrayList var21 = new ArrayList(this.route.size() - this.routeIndex);

               for (int var22 = this.routeIndex; var22 < this.route.size(); var22++) {
                  var21.add(new Vec3(this.route.get(var22).blockX() + 0.5, var19, this.route.get(var22).blockZ() + 0.5));
               }

               this.aimAt(PathFollower.lookAheadPoint(new Vec3(var2.x, var19, var2.z), var21, 3.0), 12.0F);
            } else {
               this.aimAt(Vec3.atCenterOf(this.walkTarget), 12.0F);
            }

            this.trackMovement(var18);
            this.setStatus("Walking to " + (this.walkTarget == null ? "the next block" : this.walkTarget.toShortString()));
         }
      }
   }

   private int stopMovement(Vec3 var1, int var2) {
      if (Math.abs(var1.y - var2) > 0.6 && this.getFootY() != var2) {
         return this.routeIndex;
      }

      int var3 = this.routeIndex;

      for (int var4 = this.routeIndex + 1; var4 < Math.min(this.route.size(), this.routeIndex + 1 + 8); var3 = var4++) {
         GridPathfinder.GridPos var5 = this.route.get(var4);
         if (!PathFollower.isWalkClear(minecraft.level, var1, var5.blockX() + 0.5, var5.blockZ() + 0.5, var2)) {
            break;
         }
      }

      return var3;
   }

   private boolean clearRoute(Vec3 var1, int var2, int var3) {
      GridPathfinder.GridPos var4 = this.route.get(var2);
      GridPathfinder.GridPos var5 = this.route.get(var3);
      return Math.hypot(var5.blockX() + 0.5 - var1.x, var5.blockZ() + 0.5 - var1.z)
         <= Math.hypot(var5.blockX() - var4.blockX(), var5.blockZ() - var4.blockZ()) + 0.05;
   }

   private void trackMovement(boolean var1) {
      Vec3 var2 = minecraft.player.position();
      if (var1 && this.lastRoutePosition != null && var2.distanceToSqr(this.lastRoutePosition) < 1.0E-4) {
         this.stuckTicks++;
      } else {
         this.stuckTicks = 0;
      }

      this.lastRoutePosition = var2;
      if (this.stuckTicks > 40) {
         this.getCurrentLayer("movement blocked");
      }
   }

   private void getCurrentLayer(String var1) {
      if (++this.replanCount >= 3 && this.walkTarget != null) {
         this.setAside(this.walkTarget, var1);
      }

      LarpLog.debug(this.getName(), "Replanning route: {}", var1);
      this.route = List.of();
      this.routeIndex = 0;
      this.stuckTicks = 0;
      this.routePlanTick = -1000000000;
   }

   private boolean findDrillSlot(BlockPos var1) {
      int var2 = this.findBlockSlot();
      if (var2 >= 0 && this.placeBlocksSetting.getValue()) {
         this.sneaking = (boolean)1;
         if (this.waitForMenuToClose("bridge a gap")) {
            return true;
         } else if (!this.findBlockSlot(var2)) {
            this.setStatus("Taking out " + minecraft.player.getInventory().getItem(var2).getHoverName().getString() + " to bridge a gap");
            return true;
         } else {
            Item var3 = minecraft.player.getInventory().getItem(var2).getItem();
            BlockPos var4 = var1.immutable();
            if (this.placeCounts.getOrDefault(var4, 0) >= 3) {
               return false;
            } else if (minecraft.player.tickCount < this.nextPlaceTick) {
               this.setStatus("Steadying before the next block");
               return true;
            } else {
               boolean var5 = NativePlacement.pruneInactive(
                  this,
                  var4,
                  var3,
                  this.getReach(),
                  true,
                  true,
                  () -> NativePlacement.isReplaceable(var4) && !this.isManualMovement(),
                  () -> this.stopWithReason(var4, var3, true),
                  var2x -> {
                     this.resetRestoreCounts(var4);
                     LarpLog.debug(this.getName(), "Bridge block at {} {}", var4.toShortString(), var2x ? "placed" : "rejected");
                  }
               );
               this.setStatus(var5 ? "Bridging a gap at " + var4.toShortString() : "Cannot reach the gap at " + var4.toShortString());
               return var5;
            }
         }
      } else {
         return false;
      }
   }

   private void finishLayer(LayerScanPlan.Layer var1) {
      if (!this.pickaxeOnly && this.findDrillSlot() >= 0) {
         this.pickaxeOnly = (boolean)1;
         if (this.sweepLeftoversSetting.getValue()) {
            LarpLog.info(this.getName(), "Layer {}: drill faces done, sweeping leftovers", this.currentLayer + 1);
            this.lastScanTick = -1000000000;
            return;
         }
      }

      int var2 = 0;

      for (BlockPos var4 : this.minableBlocks) {
         if (BlockBreaker.canBreak(var4)) {
            var2++;
         }
      }

      LarpLog.info(
         this.getName(),
         "Layer {} of {} done; {} blocks left (held back fluid, unreachable, set aside or not swept)",
         this.currentLayer + 1,
         this.plan.layerCount(),
         var2
      );
      this.currentLayer++;
      this.pickaxeOnly = (boolean)(this.findDrillSlot() < 0 ? 1 : 0);
      this.layerAnnounced = (boolean)0;
      this.retryStamp = -1;
      this.pillarBase = null;
      this.minableBlocks.clear();
      this.fluidBlocks.clear();
      this.lastScanTick = -1000000000;
      this.targetBlock = this.walkTarget = null;
      this.clearRoute();
      if (this.currentLayer >= this.plan.layerCount()) {
         this.currentLayer = this.plan.layerCount() - 1;
         if (this.collectDropsSetting.getValue()) {
            this.collectingDrops = (boolean)1;
            this.setStatus("Picking up drops");
            LarpLog.info(this.getName(), "Picking up drops in the area");
         } else {
            this.finishArea();
         }
      } else {
         this.setStatus("Moving down to layer " + (this.currentLayer + 1));
      }
   }

   private void finishArea() {
      if (this.finalSweep) {
         this.blockSet = null;
         this.plan = this.originalPlan;
         this.startLayer = 0;
         this.totalBlocks = this.originalBlockTarget;
      } else if (this.blockSet == null && this.startFinalSweep()) {
         return;
      }

      LayerScanPlan.Region var1 = this.plan.getRegion();
      int var2 = 0;
      if (this.blockSet != null) {
         var2 = this.countRemainingBlockSet();
      } else {
         for (int var3 = var1.minY(); var3 <= this.plan.layerAt(this.startLayer).top(); var3++) {
            for (int var4 = var1.minX(); var4 <= var1.maxX(); var4++) {
               for (int var5 = var1.minZ(); var5 <= var1.maxZ(); var5++) {
                  if (BlockBreaker.canBreak(new BlockPos(var4, var3, var5))) {
                     var2++;
                  }
               }
            }
         }
      }

      String var15 = clearRoute(System.currentTimeMillis() - this.startMillis);
      if (this.blockSet != null) {
         String var17 = this.blockSetName
            + " mined out in "
            + var15
            + ", "
            + Math.max(0, this.totalBlocks - var2)
            + " blocks mined"
            + (var2 > 0 ? ", " + var2 + " left standing (next to lava or water, unreachable or put back by the server)" : "");
         this.sendMessage(var17 + ".");
         LarpLog.info(this.getName(), "Block set done: {}", var17);
         if (this.notifyWhenDoneSetting.getValue()) {
            NotificationManager.getInstance()
               .notify(
                  "Auto Area Miner",
                  this.blockSetName + " mined out in " + var15 + (var2 > 0 ? ", " + var2 + " left" : ""),
                  NotificationManager.Type.SUCCESS,
                  8000L
               );
         }

         this.disable();
      } else {
         int var16 = 0;
         int var18 = 0;
         int var6 = 0;
         int var7 = 0;
         BlockPos var8 = null;
         ArrayList var9 = new ArrayList();

         for (int var10 = this.plan.layerAt(this.startLayer).top(); var10 >= var1.minY(); var10--) {
            for (int var11 = var1.minX(); var11 <= var1.maxX(); var11++) {
               for (int var12 = var1.minZ(); var12 <= var1.maxZ(); var12++) {
                  BlockPos var13 = new BlockPos(var11, var10, var12);
                  if (BlockBreaker.canBreak(var13)) {
                     String var14;
                     if (this.hasAdjacentFluid(var13)) {
                        var16++;
                        var14 = "holds back a fluid";
                     } else if (this.restoredBlocks.contains(var13)) {
                        var18++;
                        var14 = "put back by the server";
                     } else if (this.outsideCutBlocks.contains(var13)) {
                        var6++;
                        var14 = "drill would cut outside";
                     } else {
                        var7++;
                        var14 = "not reached";
                        if (var8 == null) {
                           var8 = var13;
                        }
                     }

                     if (var9.size() < 24) {
                        var9.add(
                           var13.toShortString()
                              + " "
                              + BuiltInRegistries.BLOCK.getKey(minecraft.level.getBlockState(var13).getBlock()).getPath()
                              + " ("
                              + var14
                              + ")"
                        );
                     }
                  }
               }
            }
         }

         int var19 = Math.max(0, this.totalBlocks - var2);
         int var20 = this.startLayer > 0 ? this.getCurrentLayer(this.plan.layerAt(this.startLayer).top()) : 0;
         String var21;
         if (var2 == 0) {
            var21 = "area cleared in " + var15 + (this.totalBlocks >= 0 ? ", " + var19 + " blocks mined" : "");
         } else {
            ArrayList var22 = new ArrayList();
            if (var16 > 0) {
               var22.add(var16 + " next to lava or water, left so the area does not flood");
            }

            if (var18 > 0) {
               var22.add(var18 + " the server keeps putting back");
            }

            if (var6 > 0) {
               var22.add(var6 + " at the edges, where your drill would cut outside the area (add a plain pickaxe to clear them)");
            }

            if (var7 > 0) {
               var22.add(var7 + " it could not reach or see, the first at " + var8.toShortString());
            }

            var21 = "finished in "
               + var15
               + (this.totalBlocks >= 0 ? ", " + var19 + " blocks mined" : "")
               + ", but "
               + var2
               + " blocks are still standing: "
               + String.join("; ", var22);
         }

         if (var20 > 0) {
            var21 = var21 + ". The " + var20 + " blocks in the layers above where you started were left as they are";
         }

         this.sendMessage(var21 + ".");
         LarpLog.info(this.getName(), "Done: {}", var21);
         if (!var9.isEmpty()) {
            LarpLog.info(this.getName(), "Still standing: {}{}", String.join(", ", var9), var2 > var9.size() ? " and more" : "");
         }

         if (this.notifyWhenDoneSetting.getValue()) {
            NotificationManager.getInstance()
               .notify(
                  "Auto Area Miner",
                  var2 == 0 ? "Area cleared in " + var15 : "Finished with " + var2 + " blocks still standing",
                  var2 == 0 ? NotificationManager.Type.SUCCESS : NotificationManager.Type.WARNING,
                  8000L
               );
         }

         this.areaCleared = (boolean)1;
         this.disable();
      }
   }

   private boolean startFinalSweep() {
      LayerScanPlan.Region var1 = this.plan.getRegion();
      if (var1.volume() > 2000000L) {
         return false;
      }

      HashSet var2 = new HashSet();
      int var3 = 0;
      int var4 = this.plan.layerAt(this.startLayer).top();

      for (int var5 = var1.maxY(); var5 >= var1.minY(); var5--) {
         for (int var6 = var1.minX(); var6 <= var1.maxX(); var6++) {
            for (int var7 = var1.minZ(); var7 <= var1.maxZ(); var7++) {
               BlockPos var8 = new BlockPos(var6, var5, var7);
               if (BlockBreaker.canBreak(var8) && !this.hasAdjacentFluid(var8) && !this.restoredBlocks.contains(var8)) {
                  var2.add(var8);
                  if (var5 > var4) {
                     var3++;
                  }
               }
            }
         }
      }

      if (!var2.isEmpty() && var2.size() <= 20000) {
         String var11 = var3 == var2.size()
            ? var2.size() + " blocks in the layers above where you started"
            : (var3 > 0 ? var2.size() + " blocks (" + var3 + " of them above where you started)" : var2.size() + " blocks");
         this.sendMessage("checking the whole area: " + var11 + " are still standing, so the miner goes back for them.");
         LarpLog.info(
            this.getName(), "Final check: {} blocks still standing ({} above layer {}); mining them as a block set", var2.size(), var3, this.startLayer + 1
         );
         this.originalPlan = this.plan;
         this.originalBlockTarget = this.totalBlocks < 0 ? -1 : this.totalBlocks + var3;
         this.finalSweep = (boolean)1;
         this.blockSet = var2;
         this.blockSetName = "the area's leftovers";
         BlockPos var12 = this.findBlockSlot();
         int[] var13 = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
         int[] var14 = new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE};

         for (BlockPos var10 : var2) {
            var13[0] = Math.min(var13[0], var10.getX());
            var13[1] = Math.min(var13[1], var10.getY());
            var13[2] = Math.min(var13[2], var10.getZ());
            var14[0] = Math.max(var14[0], var10.getX());
            var14[1] = Math.max(var14[1], var10.getY());
            var14[2] = Math.max(var14[2], var10.getZ());
         }

         this.plan = new LayerScanPlan(
            LayerScanPlan.Region.fromCorners(var13, var14),
            this.layerHeightSetting.getValue().intValue(),
            this.passWidthSetting.getValue().intValue(),
            var12.getX(),
            var12.getZ()
         );
         this.pickaxeOnly = (boolean)1;
         this.currentLayer = this.startLayer = 0;
         this.minableBlocks.clear();
         this.fluidBlocks.clear();
         this.lastScanTick = -1000000000;
         this.targetBlock = this.walkTarget = null;
         this.targetState = null;
         this.clearRoute();
         this.collectingDrops = (boolean)0;
         this.dropCollectTicks = this.dropStallTicks = 0;
         this.ignoredDropIds.clear();
         this.setAsideUntil.clear();
         this.failureCounts.clear();
         this.abandonedBlocks.clear();
         this.drillFaceStates.clear();
         this.drillFaceActive = (boolean)0;
         this.walker.reset();
         this.lastBlockSetPlanTick = -1000000000;
         this.blockSetFailures = 0;
         this.blockSetTargets.clear();
         this.remainingBlocks = this.countRemainingBlockSet();
         this.lastCountTick = minecraft.player.tickCount;
         this.setStatus("Going back for " + var2.size() + " blocks");
         return true;
      } else {
         return false;
      }
   }

   private String getCurrentLayer() {
      return this.corner1Setting.getValue().trim() + "|" + this.corner2Setting.getValue().trim() + "|" + this.layerHeightSetting.getValue().intValue();
   }

   private int startFinalSweep() {
      LayerScanPlan.Region var1 = this.plan.getRegion();
      if (var1.volume() > 200000L) {
         return -2;
      }

      MutableBlockPos var2 = new MutableBlockPos();

      for (int var3 = var1.maxY(); var3 >= var1.minY(); var3--) {
         for (int var4 = var1.minX(); var4 <= var1.maxX(); var4++) {
            for (int var5 = var1.minZ(); var5 <= var1.maxZ(); var5++) {
               if (BlockBreaker.canBreak(var2.set(var4, var3, var5)) && !this.hasAdjacentFluid(var2)) {
                  return this.plan.layerIndexOf(var3);
               }
            }
         }
      }

      return -1;
   }

   private int getCurrentLayer(int var1) {
      LayerScanPlan.Region var2 = this.plan.getRegion();
      if (var2.volume() > 200000L) {
         return 0;
      }

      int var3 = 0;
      MutableBlockPos var4 = new MutableBlockPos();

      for (int var5 = var1 + 1; var5 <= var2.maxY(); var5++) {
         for (int var6 = var2.minX(); var6 <= var2.maxX(); var6++) {
            for (int var7 = var2.minZ(); var7 <= var2.maxZ(); var7++) {
               if (BlockBreaker.canBreak(var4.set(var6, var5, var7))) {
                  var3++;
               }
            }
         }
      }

      return var3;
   }

   private boolean clearRoute(BlockPos var1) {
      LayerScanPlan.Region var2 = this.plan.getRegion();
      if (!var2.containsColumn(var1.getX(), var1.getZ())) {
         return false;
      }

      MutableBlockPos var3 = new MutableBlockPos();

      for (int var4 = var1.getY() + 1; var4 <= var2.maxY(); var4++) {
         if (!minecraft.level.getBlockState(var3.set(var1.getX(), var4, var1.getZ())).getCollisionShape(minecraft.level, var3).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private void collectDrops() {
      BlockBreaker.release(this);
      LayerScanPlan.Region var1 = this.plan.getRegion();
      int var2 = this.getFootY();
      AABB var3 = new AABB(var1.minX() - 1, var2 - 1, var1.minZ() - 1, var1.maxX() + 2, var2 + 2, var1.maxZ() + 2);
      List var4 = minecraft.level.getEntitiesOfClass(ItemEntity.class, var3, var1x -> var1x.isAlive() && !this.ignoredDropIds.contains(var1x.getId()));
      if (!var4.isEmpty() && ++this.dropCollectTicks <= 1200) {
         Vec3 var5 = minecraft.player.position();
         ItemEntity var6 = var4.stream().min(Comparator.comparingDouble(var1x -> var1x.position().distanceToSqr(var5))).orElseThrow();
         BlockPos var7 = BlockPos.containing(var6.position());
         if (var7.getX() == minecraft.player.getBlockX() && var7.getZ() == minecraft.player.getBlockZ()) {
            this.findTunnelStand(Vec3.atBottomCenterOf(var7));
            if (++this.dropStallTicks > 30) {
               this.ignoredDropIds.add(var6.getId());
               this.dropStallTicks = 0;
            }

            this.setStatus("Picking up drops (" + var4.size() + " left)");
         } else {
            this.dropStallTicks = 0;
            if (this.route.isEmpty() || this.walkTarget == null || !this.walkTarget.equals(var7)) {
               List var8 = this.findRoute(var2, (var1x, var2x) -> var1x == var7.getX() && var2x == var7.getZ());
               if (var8 == null || var8.isEmpty()) {
                  this.ignoredDropIds.add(var6.getId());
                  this.clearRoute();
                  return;
               }

               this.route = var8;
               this.routeIndex = 0;
               this.walkTarget = var7;
            }

            this.breakBlock(var2);
            this.setStatus("Picking up drops (" + var4.size() + " left)");
         }
      } else {
         if (!var4.isEmpty()) {
            LarpLog.info(this.getName(), "Left {} drops after {} ticks of picking up", var4.size(), 1200);
         }

         this.finishArea();
      }
   }

   private BlockPos findTunnelStand(int var1) {
      int[] var2 = this.plan.laneBounds(0, var1);
      int var3 = (var2[0] + var2[1]) / 2;
      LayerScanPlan.Region var4 = this.plan.getRegion();
      int var5 = this.plan.rowDirection(0, var1) > 0
         ? (this.plan.rowDirection() ? var4.minX() : var4.minZ())
         : (this.plan.rowDirection() ? var4.maxX() : var4.maxZ());
      return this.plan.rowDirection() ? new BlockPos(var5, 0, var3) : new BlockPos(var3, 0, var5);
   }

   private void isInsideArea(LayerScanPlan.Layer var1) {
      if (!this.countRemainingBlockSet()
         || this.stairDisabled
         || !this.standOrder.stream().anyMatch(var0 -> var0.kind() == TunnelPlanner.StepKind.DESCEND)
         || !this.setAside(var1)) {
         BlockPos var2 = this.findBlockSlot();
         BlockPos var3 = var2.below();
         if (this.pillarBase == null) {
            BlockPos var4 = this.findTunnelStand(this.currentLayer);
            int var5 = var2.getY();
            List var6 = this.findRoute(
               var5, (var4x, var5x) -> var4x == var4.getX() && var5x == var4.getZ() && this.finishLayer(new BlockPos(var4x, var5 - 1, var5x), var1)
            );
            if (var6 == null) {
               var6 = this.findRoute(
                  var5, (var3x, var4x) -> this.plan.getRegion().containsColumn(var3x, var4x) && this.finishLayer(new BlockPos(var3x, var5 - 1, var4x), var1)
               );
            }

            if (var6 == null) {
               this.stopWithReason("there is no safe place to dig down into layer " + (this.currentLayer + 1) + "; stand on top of the area");
               return;
            }

            GridPathfinder.GridPos var7 = var6.isEmpty() ? new GridPathfinder.GridPos(var2.getX(), var2.getZ()) : (GridPathfinder.GridPos)var6.getLast();
            this.pillarBase = new BlockPos(var7.blockX(), var5, var7.blockZ());
            this.route = var6;
            this.routeIndex = 0;
            this.walkTarget = null;
            LarpLog.debug(this.getName(), "Digging down to layer {} at {}", this.currentLayer + 1, this.pillarBase.toShortString());
         }

         if (var2.getX() == this.pillarBase.getX() && var2.getZ() == this.pillarBase.getZ()) {
            this.clearRoute();
            if (!this.finishLayer(var3, var1)) {
               this.pillarBase = null;
               this.setStatus("Looking for a safe place to dig down");
            } else {
               Vec3 var8 = Vec3.atBottomCenterOf(var2);
               if (minecraft.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(var8.multiply(1.0, 0.0, 1.0)) > 0.03) {
                  this.findTunnelStand(var8);
                  this.setStatus("Lining up to dig down");
               } else if (!BlockBreaker.canBreak(var3)) {
                  this.setStatus("Waiting to drop into layer " + (this.currentLayer + 1));
               } else {
                  this.setTarget(var3);
                  this.drillFaceActive = (boolean)0;
                  this.breakBlock(var3, false, "Digging down to layer " + (this.currentLayer + 1));
                  this.pillarBase = var2.below();
               }
            }
         } else if (this.route.isEmpty()) {
            this.pillarBase = null;
            this.clearRoute();
         } else {
            this.breakBlock(var2.getY());
         }
      }
   }

   private void breakBlock(int var1) {
      LayerScanPlan.Layer var2 = new LayerScanPlan.Layer(this.currentLayer, var1, var1);
      this.getReach(var2);
   }

   private boolean finishLayer(BlockPos var1, LayerScanPlan.Layer var2) {
      if (this.plan.getRegion().contains(var1.getX(), var1.getY(), var1.getZ()) && minecraft.level.hasChunkAt(var1) && !this.isSetAside(var1)) {
         if (this.getPhaseName() && this.stopMovement() && BlockBreaker.canBreak(var1) && !this.stopWithReason(var1, Direction.UP)) {
            return false;
         }

         if (!BlockBreaker.canBreak(var1) && !minecraft.level.getBlockState(var1).getCollisionShape(minecraft.level, var1).isEmpty()) {
            return false;
         }

         for (Direction var6 : Direction.values()) {
            if (var6 != Direction.UP && !minecraft.level.getFluidState(var1.relative(var6)).isEmpty()) {
               return false;
            }
         }

         int var7 = var1.getY() + 1;

         for (int var8 = var1.getY() - 1; var8 >= var1.getY() - 3 - 1; var8--) {
            BlockPos var9 = new BlockPos(var1.getX(), var8, var1.getZ());
            if (!minecraft.level.getFluidState(var9).isEmpty()) {
               return false;
            }

            BlockState var10 = minecraft.level.getBlockState(var9);
            if (!var10.getCollisionShape(minecraft.level, var9).isEmpty()) {
               return var8 + 1 >= var2.bottom() && LayerScanPlan.isWithinReach(var7, var8, 3) && !var10.is(Blocks.MAGMA_BLOCK);
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private void findTunnelStand(Vec3 var1) {
      Vec3 var2 = minecraft.player.position();
      Vec3 var3 = var1.subtract(var2).multiply(1.0, 0.0, 1.0);
      double var4 = var3.length();
      double var6 = var4 < 1.0E-6 ? 0.0 : minecraft.player.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var3.normalize());
      this.sneaking = (boolean)1;
      boolean var8 = this.rotator.updateLatch(var4, var6, InteractionArbiter.ownsInteraction(this));
      this.moveDirection = var8 ? var3 : Vec3.ZERO;
   }

   private void clearRoute(LayerScanPlan.Layer var1) {
      BlockPos var2 = this.findBlockSlot();
      if (!this.placeBlocksSetting.getValue()) {
         this.stopWithReason("you are below layer " + (this.currentLayer + 1) + "; turn on Place Blocks to pillar up, or climb back up");
      } else if (this.findBlockSlot() < 0) {
         this.stopWithReason("you are below layer " + (this.currentLayer + 1) + " and there is no solid block in your hotbar to pillar up with");
      } else {
         BlockPos var3 = var2.above(2);
         if (this.setStatus(var3)) {
            Vec3 var4 = Vec3.atBottomCenterOf(var2);
            if (minecraft.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(var4.multiply(1.0, 0.0, 1.0)) > 0.03) {
               this.findTunnelStand(var4);
               this.setStatus("Lining up to pillar up");
            } else {
               BlockBreaker.release(this);
               if (!this.waitForMenuToClose("pillar up")) {
                  if (!this.findBlockSlot(this.findBlockSlot())) {
                     this.setStatus("Taking out a block to pillar up");
                  } else if (minecraft.player.getXRot() < 84.0F || this.hasAdjacentFluid() > 1.5F) {
                     this.setAsideLayer();
                     this.setStatus("Looking down to pillar up");
                  } else if (minecraft.player.tickCount < this.nextPlaceTick) {
                     this.setAsideLayer();
                     this.setStatus("Steadying before the next block");
                  } else {
                     this.pillarPitch = 86.0F + (float)this.random.nextDouble() * 3.0F;
                     this.pillarTarget = var2.immutable();
                     this.pillarJumpTick = minecraft.player.tickCount;
                     this.pillarBlockPlaced = (boolean)0;
                     this.swimUp = (boolean)1;
                     this.setStatus("Pillaring up to layer " + (this.currentLayer + 1));
                  }
               }
            }
         } else {
            if (this.plan.getRegion().contains(var3.getX(), var3.getY(), var3.getZ()) && BlockBreaker.canBreak(var3) && !this.hasAdjacentFluid(var3)) {
               this.setTarget(var3);
               this.drillFaceActive = (boolean)0;
               this.breakBlock(var3, false, "Clearing headroom to pillar up");
            } else {
               this.stopWithReason("there is no room to pillar up back to layer " + (this.currentLayer + 1));
            }
         }
      }
   }

   private void getArea() {
      this.setAsideLayer();
      this.sneaking = (boolean)0;
      int var1 = this.findBlockSlot();
      if (!this.pillarBlockPlaced
         && var1 >= 0
         && !minecraft.player.onGround()
         && minecraft.player.getY() >= this.pillarTarget.getY() + 1.05
         && minecraft.player.tickCount >= this.nextPlaceTick
         && NativePlacement.isReplaceable(this.pillarTarget)) {
         Item var2 = minecraft.player.getInventory().getItem(var1).getItem();
         BlockPos var3 = this.pillarTarget;
         NativePlacement.pruneInactive(
            this,
            var3,
            var2,
            this.getReach(),
            true,
            true,
            () -> NativePlacement.isReplaceable(var3) && minecraft.player.getY() >= var3.getY() + 1,
            () -> ((LocalPlayerMixin_4)minecraft.player).larpYLast() >= var3.getY() + 1.02 && this.hasAdjacentFluid() <= 1.5F,
            var2x -> {
               this.resetRestoreCounts(var3);
               if (var2x) {
                  this.pillarBlockPlaced = (boolean)1;
               }
            }
         );
      }

      if (minecraft.player.onGround() && minecraft.player.tickCount - this.pillarJumpTick > 2) {
         int var4 = this.getFootY() > this.pillarTarget.getY() ? 1 : 0;
         if (var4 == 0 && ++this.pillarStallCount >= 5) {
            this.pillarTarget = null;
            this.stopWithReason("pillaring up failed five times");
         } else {
            if (var4 != 0) {
               this.pillarStallCount = 0;
            }

            this.pillarTarget = null;
         }
      } else {
         if (minecraft.player.tickCount - this.pillarJumpTick > 30) {
            this.pillarTarget = null;
            this.pillarStallCount++;
         }
      }
   }

   private void setAsideLayer() {
      if (InteractionArbiter.ownsInteraction(this)) {
         float[] var1 = this.rotator
            .rotateWithMode(
               minecraft.player.getYRot(),
               minecraft.player.getXRot(),
               minecraft.player.getYRot(),
               this.pillarPitch,
               minecraft.player.getXRot() >= this.pillarPitch - 1.5F,
               true
            );
         RotationManager.requestForModule(this, RotationManager.Priority.BOT, var1[0], var1[1], 1.0);
      }
   }

   @Override
   protected boolean holdsToolBetweenBlocks() {
      return true;
   }

   @Override
   public boolean aimsAtOpenFaces(BlockPos var1) {
      if (this.countRemainingBlockSet()) {
         return true;
      } else if (this.plan != null && this.currentLayer < this.plan.layerCount() && !this.pickaxeOnly && this.findDrillSlot() >= 0 && !this.setTarget(var1)) {
         int var2 = this.plan.laneIndexOf(var1.getX(), var1.getZ(), this.currentLayer);
         return !var1.equals(this.getPendingStands(var2, this.plan.rowDirection() ? var1.getX() : var1.getZ(), this.plan.layerAt(this.currentLayer)));
      } else {
         return true;
      }
   }

   @Override
   public boolean aimsAtFaceCentre(BlockPos var1) {
      return var1.equals(this.drillTarget);
   }

   @Override
   public void digFinished(BlockPos var1) {
      if (this.plan != null && minecraft.player != null) {
         this.restoreMonitor.record_(var1.asLong());
         if (this.recentBreaks.size() < 256) {
            this.recentBreaks.put(var1.immutable(), minecraft.player.tickCount);
         }
      }
   }

   @Override
   public boolean aimFaceAllowed(BlockPos var1, Direction var2) {
      if (this.drillTarget != null && var1.equals(this.drillTarget)) {
         return var2 == this.drillFace;
      }

      if (this.plan != null && this.getPhaseName() && this.stopMovement()) {
         if (this.getPlan(minecraft.level.getBlockState(var1), this.findDrillSlot()) == this.findDrillSlot() && !this.stopWithReason(var1, var2)) {
            this.outsideCutBlocks.add(var1.immutable());
            return false;
         } else {
            return true;
         }
      } else {
         return true;
      }
   }

   private boolean isInReach(BlockPos var1) {
      double var2 = this.getReach();
      return minecraft.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= var2 * var2;
   }

   private boolean findTunnelStand() {
      for (BlockPos var2 : this.drillSupportBlocks) {
         if (BlockBreaker.canBreak(var2)) {
            return false;
         }
      }

      return true;
   }

   @Override
   public boolean trustsSupport(BlockPos var1) {
      if (minecraft.player == null) {
         return true;
      } else {
         return !var1.equals(this.targetBlock) && !this.drillFaceStates.containsKey(var1)
            ? minecraft.player.tickCount >= this.drillSupportUntilTick || !this.drillSupportBlocks.contains(var1)
            : false;
      }
   }

   @Override
   public boolean waitsForSentLook() {
      return true;
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (InteractionArbiter.ownsInteraction(this)) {
            if (this.jitterAim && this.targetBlock != null && this.targetBlock.equals(this.drillTarget)) {
               var1 = findDrillSlot(minecraft.player.getEyePosition(), this.drillTarget, this.drillFace, this.yawJitter, this.pitchJitter);
            }

            float[] var3 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var1);
            BlockHitResult var4 = this.targetBlock == null ? null : BlockBreaker.raycastLook(this.targetBlock, this.getReach());
            int var5 = var4 == null || this.drillTarget != null && this.targetBlock.equals(this.drillTarget) && var4.getDirection() != this.drillFace ? 0 : 1;
            float[] var6 = this.rotator.rotate(minecraft.player.getYRot(), minecraft.player.getXRot(), var3[0], var3[1], (boolean)var5);
            RotationManager.requestForModule(this, RotationManager.Priority.BOT, var6[0], var6[1], 1.0);
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = this.rotator.rotateWithMode(minecraft.player.getYRot(), minecraft.player.getXRot(), var1, var2, false, true);
         RotationManager.requestForModule(this, RotationManager.Priority.BOT, var3[0], var3[1], 1.0);
      }
   }

   public static Input findBlockSlot() {
      if (instance != null && instance.isEnabled()) {
         AutoAreaMiner var0 = instance;
         Minecraft var1 = Minecraft.getInstance();
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.plan != null
            && var1.player != null
            && var1.player == var0.player
            && var1.player.tickCount == var0.lastActiveTick
            && !ViewController.isFreecam()
            && !MovementCorrection.isRotationActive()
            && !(var1.gui.screen() instanceof AbstractContainerScreen)
            && (!RotationManager.hasRequest() || RotationManager.isActive(RotationManager.Priority.BOT))) {
            int var2 = var0.moveDirection.lengthSqr() > 1.0E-6 ? 1 : 0;
            if (var2 == 0 && !var0.swimUp && !var0.sneaking) {
               return null;
            }

            Input var3 = var2 != 0
               ? var0.rotator.directionToInput(var1.player.getYRot(), var0.moveDirection)
               : new Input(false, false, false, false, false, false, false);
            return new Input(var3.forward(), var3.backward(), var3.left(), var3.right(), var0.swimUp, var0.sneaking, var0.sprinting);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void sendMessage(String var1) {
      if (minecraft != null && minecraft.player != null) {
         minecraft.player.sendSystemMessage(Component.literal("Larp Auto Area Miner: " + var1));
      }
   }

   private void stopWithReason(String var1) {
      LarpLog.info(this.getName(), "Stopped: {}", var1);
      this.sendMessage("stopped, " + var1 + ".");
      NotificationManager.getInstance()
         .notifyDefault("Auto Area Miner stopped", var1.substring(0, 1).toUpperCase(Locale.ROOT) + var1.substring(1), NotificationManager.Type.WARNING);
      this.setStatus("Stopped: " + var1);
      this.disable();
   }

   private void mineLayer() {
      if (this.isInGame()) {
         if (this.isRuntimeActive() && this.plan != null && minecraft.level == this.level) {
            try {
               int var1 = Theme.scaleRounding(0.25);
               if (this.showAreaSetting.getValue()) {
                  LayerScanPlan.Region var2 = this.plan.getRegion();
                  Gizmos.cuboid(
                        new AABB(var2.minX(), var2.minY(), var2.minZ(), var2.maxX() + 1, var2.maxY() + 1, var2.maxZ() + 1),
                        GizmoStyle.strokeAndFill(ColorUtil.withAlpha(var1, 0.9), 1.5F, 0)
                     )
                     .setAlwaysOnTop();
                  if (this.currentLayer < this.plan.layerCount()) {
                     LayerScanPlan.Layer var3 = this.plan.layerAt(this.currentLayer);
                     Gizmos.cuboid(
                        new AABB(var2.minX(), var3.bottom(), var2.minZ(), var2.maxX() + 1, var3.top() + 1, var2.maxZ() + 1),
                        GizmoStyle.strokeAndFill(ColorUtil.withAlpha(var1, 0.5), 1.0F, ColorUtil.withAlpha(var1, 0.06))
                     );
                  }

                  if (this.targetBlock != null) {
                     Gizmos.cuboid(
                           new AABB(this.targetBlock).inflate(0.01),
                           GizmoStyle.strokeAndFill(
                              ColorUtil.withAlpha(this.drillFaceActive ? -14249 : var1, 1.0),
                              2.0F,
                              ColorUtil.withAlpha(this.drillFaceActive ? -14249 : var1, 0.18)
                           )
                        )
                        .setAlwaysOnTop();
                  }
               }

               if (this.showPathSetting.getValue() && this.routeIndex < this.route.size()) {
                  double var9 = minecraft.player.getY() + 0.05;
                  Vec3 var4 = new Vec3(minecraft.player.getX(), var9, minecraft.player.getZ());

                  for (int var5 = this.routeIndex; var5 < this.route.size(); var5++) {
                     GridPathfinder.GridPos var6 = this.route.get(var5);
                     Vec3 var7 = new Vec3(var6.blockX() + 0.5, var9, var6.blockZ() + 0.5);
                     Gizmos.line(var4, var7, ColorUtil.withAlpha(var1, 0.85), 2.0F).setAlwaysOnTop();
                     var4 = var7;
                  }
               }
            } catch (RuntimeException var8) {
               LarpLog.errorOnce(this.getName(), "Area overlay failed", var8);
            }
         } else {
            this.findBlockSlot();
         }
      }
   }

   private void findBlockSlot() {
      if (this.pickingCorners) {
         this.waitForFallingBlocks();
      } else if (System.currentTimeMillis() <= this.previewUntilMillis) {
         LayerScanPlan.Region var1 = this.getArea();
         if (var1 != null) {
            try {
               int var2 = Theme.scaleRounding(0.25);
               Gizmos.cuboid(
                     new AABB(var1.minX(), var1.minY(), var1.minZ(), var1.maxX() + 1, var1.maxY() + 1, var1.maxZ() + 1),
                     GizmoStyle.strokeAndFill(ColorUtil.withAlpha(var2, 0.95), 2.0F, ColorUtil.withAlpha(var2, 0.08))
                  )
                  .setAlwaysOnTop();

               for (String var6 : new String[]{this.corner1Setting.getValue(), this.corner2Setting.getValue()}) {
                  int[] var7 = LayerScanPlan.parseCoords(var6);
                  if (var7 != null) {
                     Gizmos.cuboid(
                           new AABB(var7[0], var7[1], var7[2], var7[0] + 1, var7[1] + 1, var7[2] + 1).inflate(0.02),
                           GizmoStyle.strokeAndFill(-1, 2.0F, ColorUtil.withAlpha(var2, 0.35))
                        )
                        .setAlwaysOnTop();
                  }
               }
            } catch (RuntimeException var8) {
               LarpLog.errorOnce(this.getName(), "Area preview failed", var8);
            }
         }
      }
   }

   private void waitForFallingBlocks() {
      try {
         int var1 = Theme.scaleRounding(0.25);
         int[] var2 = LayerScanPlan.parseCoords(this.corner1Setting.getValue());
         int[] var3 = LayerScanPlan.parseCoords(this.corner2Setting.getValue());
         int[] var4 = this.aimedBlock == null ? null : new int[]{this.aimedBlock.getX(), this.aimedBlock.getY(), this.aimedBlock.getZ()};
         int[] var5 = var2 != null ? var2 : var4;
         int[] var6 = var3 != null ? var3 : var4;
         if (var5 != null && var6 != null) {
            LayerScanPlan.Region var7 = LayerScanPlan.Region.fromCorners(var5, var6);
            Gizmos.cuboid(
                  new AABB(var7.minX(), var7.minY(), var7.minZ(), var7.maxX() + 1, var7.maxY() + 1, var7.maxZ() + 1),
                  GizmoStyle.strokeAndFill(ColorUtil.withAlpha(var1, 0.95), 2.0F, ColorUtil.withAlpha(var1, 0.08))
               )
               .setAlwaysOnTop();
         }

         for (int[] var10 : new int[][]{var2, var3}) {
            if (var10 != null) {
               Gizmos.cuboid(
                     new AABB(var10[0], var10[1], var10[2], var10[0] + 1, var10[1] + 1, var10[2] + 1).inflate(0.02),
                     GizmoStyle.strokeAndFill(-1, 2.0F, ColorUtil.withAlpha(var1, 0.35))
                  )
                  .setAlwaysOnTop();
            }
         }

         if (this.aimedBlock != null) {
            Gizmos.cuboid(new AABB(this.aimedBlock).inflate(0.03), GizmoStyle.strokeAndFill(-14249, 2.0F, 872400983)).setAlwaysOnTop();
         }
      } catch (RuntimeException var11) {
         LarpLog.errorOnce(this.getName(), "Corner picking overlay failed", var11);
      }
   }

   private int setAside() {
      if (this.blockSet != null) {
         return this.countRemainingBlockSet();
      }

      LayerScanPlan.Region var1 = this.plan == null ? this.getArea() : this.plan.getRegion();
      if (var1 != null && var1.volume() <= 200000L) {
         int var2 = 0;
         MutableBlockPos var3 = new MutableBlockPos();
         int var4 = this.plan == null ? var1.maxY() : this.plan.layerAt(this.startLayer).top();

         for (int var5 = var1.minY(); var5 <= var4; var5++) {
            for (int var6 = var1.minX(); var6 <= var1.maxX(); var6++) {
               for (int var7 = var1.minZ(); var7 <= var1.maxZ(); var7++) {
                  if (BlockBreaker.canBreak(var3.set(var6, var5, var7))) {
                     var2++;
                  }
               }
            }
         }

         return var2;
      } else {
         return -1;
      }
   }

   private String endCornerPicking() {
      StringBuilder var1 = new StringBuilder("Layer ")
         .append(Math.min(this.currentLayer + 1, this.plan.layerCount()))
         .append(" of ")
         .append(this.plan.layerCount());
      if (this.totalBlocks > 0 && this.remainingBlocks >= 0) {
         var1.append(", ").append(this.trackMovement()).append("% done");
      }

      var1.append(", ").append(clearRoute(System.currentTimeMillis() - this.startMillis));
      return var1.toString();
   }

   private int trackMovement() {
      return this.totalBlocks > 0 && this.remainingBlocks >= 0
         ? Math.clamp(Math.round(100.0 * (this.totalBlocks - this.remainingBlocks) / this.totalBlocks), 0, 100)
         : 0;
   }

   static String clearRoute(long var0) {
      return ModulePanel.formatDuration(var0);
   }

   @Override
   public ModulePanel.HudCard hudCard() {
      if (!this.isInGame()) {
         return null;
      }

      if (this.showProgressSetting.getValue() && this.isRuntimeActive() && this.plan != null && minecraft.level == this.level) {
         int var6 = this.totalBlocks > 0 && this.remainingBlocks >= 0 ? 1 : 0;
         String var7 = this.getStatus();
         return new ModulePanel.HudCard(
            "Layer " + Math.min(this.currentLayer + 1, this.plan.layerCount()) + " of " + this.plan.layerCount(),
            var6 != 0 ? this.trackMovement() + "%" : "",
            var7 != null && !var7.isBlank() ? var7 : "Starting",
            clearRoute(System.currentTimeMillis() - this.startMillis),
            var6 != 0 ? Math.clamp((double)(this.totalBlocks - this.remainingBlocks) / this.totalBlocks, 0.0, 1.0) : -1.0
         );
      }

      if (!this.pickingCorners) {
         return null;
      }

      LayerScanPlan.Region var1 = this.getArea();
      int var2 = (this.cornerPicked[0] ? 1 : 0) + (this.cornerPicked[1] ? 1 : 0);
      FreecamModule var3 = this.finishLayer();
      double var4 = var3 == null ? 0.0 : var3.getGuidedSpeed();
      return new ModulePanel.HudCard(
         var1 == null ? "Choose the area" : var1.describe(),
         var2 + " of 2",
         var2 == 2 ? "Esc to finish, scroll changes camera speed" : "Left click corner 1, right click corner 2",
         var4 > 0.0 ? Math.round(var4) + " b/s" : "",
         var2 / 2.0
      );
   }

   public LayerScanPlan getPlan() {
      return this.plan;
   }

   public int getCurrentLayer() {
      return this.currentLayer;
   }

   public String getPhaseName() {
      return this.phase.name();
   }

   private enum Phase {
      MINE_LAYER,
      ENTER_AREA,
      CLEAR_ROUTE;
   }
}
