package com.larpclient.module.player;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.KeybindSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.misc.TextSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.math.BoxRaycastUtil;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.OrderMenuParser;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.NativePlacement;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo.Style;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class OrderDropperModule extends Module {
   public static final String mODE_DROP_ONLY = "Drop Only";
   public static final String mODE_FILL_SHULKERS = "Fill Shulkers";
   private static final int fINISH_ALIGN_TICKS = 20;
   private static final int cLICK_RESPONSE_TICKS = 60;
   private static final int mENU_LOAD_TICKS = 60;
   private static final int pICKUP_TIMEOUT_TICKS = 120;
   private static final int sTUCK_TICKS = 50;
   private static final int mAX_MENU_RELOADS = 3;
   private static final int mAX_OPEN_ATTEMPTS = 3;
   private static final int sELECTED_COLOR = -13180806;
   private static final int aCTIVE_COLOR = -11702;
   private static final int fULL_COLOR = -2061766;
   private static final int sKIPPED_COLOR = -1876386;
   private static final int lABEL_COLOR = -854792;
   private static final int rEMOVED_COLOR = -1017711;
   private static final double sELECT_RANGE = 24.0;
   private static final double lABEL_DISTANCE = 10.0;
   private static final int mAX_AREA_SIDE = 32;
   private static final long mARK_FADE_NANOS = 450000000L;
   private static final long hINT_INTERVAL_NANOS = 2000000000L;
   private static final long tAP_THRESHOLD_NANOS = 400000000L;
   private static final double hIGHLIGHT_INFLATE = 0.012;
   private static OrderDropperModule instance;
   private final ModeSetting mode = this.addSetting(
      new ModeSetting(
         "Mode",
         "Drop Only drops every page where you stand. Fill Shulkers drops a page at your feet, picks it up, stores it in the selected shulkers and walks back, until the orders are empty",
         "Fill Shulkers",
         "Drop Only",
         "Fill Shulkers"
      )
   );
   private final TextSetting orderItem = this.addSetting(
         new TextSetting("Order Item", "Item whose orders are emptied, such as totem_of_undying. Use the held-item button to fill it in", "")
      )
      .setTitle("totem_of_undying");
   private final KeybindSetting selectShulkerKey = this.addSetting(
      new KeybindSetting(
         "Select Shulker Key",
         "Hold and sweep your crosshair over shulker boxes to select them; a sweep that starts on a selected box removes instead. Tap it for click selection: right-click adds, left-click removes, sneak-click covers an area",
         -1
      )
   );
   private final NumberSetting clickDelay = this.addSetting(new NumberSetting("Click Delay", "Ticks between menu clicks and item moves", 4.0, 2.0, 20.0, 1.0));
   private final BooleanSetting stopWhenHurt = this.addSetting(
      new BooleanSetting("Stop When Hurt", "Stop at once when you take damage. Menus block Auto Totem, so this protects you while it works", true)
   );
   private final BooleanSetting showMessages = this.addSetting(
      new BooleanSetting("Show Messages", "Report orders, pages, trips and the final summary in chat", true)
   );
   private final NumberSetting fillDelay = this.addSetting(
      new NumberSetting(
         "Fill Delay", "Ticks between shulker transfers. Lower is faster. Reopens the box to verify the result before leaving", 2.0, 1.0, 20.0, 1.0
      )
   );
   private final BooleanSetting afkMode = this.addSetting(
      new BooleanSetting(
         "AFK Mode", "Release the mouse and keep running when you switch windows. Your pause-on-focus-loss preference is restored when stopped", false
      )
   );
   private final ModeSetting highlightStyle = this.addSetting(
      new ModeSetting(
         "Highlight Style",
         "Material worn by selected shulkers, shared with Self Visuals. Colors follow each box's state",
         "Camo",
         "Camo",
         "Glow",
         "Metallic",
         "Marble",
         "Glitter",
         "Solid",
         "Ghost"
      )
   );
   private final NumberSetting highlightOpacity = this.addSetting(
      new NumberSetting("Highlight Opacity", "How strongly the material covers selected shulkers", 0.85, 0.2, 1.0, 0.05)
   );
   private final List<BlockPos> selectedShulkers = new ArrayList<>();
   private final Set<BlockPos> fullShulkers = new HashSet<>();
   private final Map<BlockPos, Integer> freeSlots = new HashMap<>();
   private final Set<BlockPos> unavailableShulkers = new HashSet<>();
   private final Map<BlockPos, Object> scannedBlockEntities = new HashMap<>();
   private Level selectionLevel;
   private boolean selectKeyDown;
   private boolean selectKeyBlocked;
   private Boolean sweepAdding;
   private BlockPos lastSweepPos;
   private int sweepCount;
   private long selectKeyPressNanos;
   private final Map<BlockPos, OrderDropperModule.SelectionMark> selectionMarks = new HashMap<>();
   private boolean selecting;
   private boolean useReleased;
   private boolean attackReleased;
   private boolean prevUseDown;
   private boolean prevAttackDown;
   private boolean areaSelected;
   private BlockPos hoveredShulker;
   private BlockPos lastSelectedShulker;
   private BlockPos lastClickedShulker;
   private long lastHintNanos;
   private final List<double[]> highlightFaces = new ArrayList<>();
   private final List<Integer> highlightFaceColors = new ArrayList<>();
   private long highlightHash = Long.MIN_VALUE;
   private String cachedItemId;
   private Item cachedItem;
   private Object startPlayer;
   private Object startLevel;
   private Object startConnection;
   private OrderDropperModule.Phase phase;
   private String orderItemId = "";
   private String currentOrderKey = "";
   private String status = "Idle";
   private Vec3 startPos;
   private float startYaw;
   private float startPitch;
   private float lastHealth;
   private final Set<String> emptiedOrders = new HashSet<>();
   private final Set<Integer> reservedSlots = new HashSet<>();
   private boolean ordersDone;
   private boolean pageDropped;
   private boolean openRequested;
   private boolean menuCloseRequested;
   private int menuReopenCount;
   private int cursorHeldTicks;
   private int actionDelayTicks;
   private int phaseTicks;
   private int idleTicks;
   private int commandWaitTicks;
   private int commandAttempts;
   private int navigationRetries;
   private int nextPageCount;
   private OrderDropperModule.ClickKind pendingClick;
   private int pendingContainerId;
   private int pendingMenuHash;
   private int clickWaitTicks;
   private AbstractContainerMenu syncedMenu;
   private AbstractContainerMenu settledMenu;
   private int settledHash;
   private int settledTicks;
   private int loadWaitTicks;
   private int reloadAttempts;
   private BlockPos targetShulker;
   private double bestWalkDistance;
   private int stuckTicks;
   private int aimTicks;
   private int openWaitTicks;
   private int openFailures;
   private boolean walkForward;
   private boolean walkJump;
   private boolean walkSneak;
   private int pagesDropped;
   private int stacksTaken;
   private int stacksStored;
   private int tripCount;
   private boolean paused;
   private boolean scanning;
   private boolean storageFull;
   private boolean afkActive;
   private boolean savedPauseOnLostFocus;
   private Vec3 walkTarget;
   private final Set<Vec3> triedSpots = new HashSet<>();
   private boolean verifying;
   private boolean baselineTaken;
   private int shulkerItemCount;
   private int shulkerStackCount;
   private int remainingWaitTicks;
   private int transferCount;
   private int rejectedTransfers;
   private int closeRetries;
   private static final Direction[][] fACE_AXES = new Direction[6][];

   public OrderDropperModule() {
      super("Order Dropper", "Empty your DonutSMP orders for one item by dropping pages, or fill selected shulkers with them", Category.PLAYER, -1);
      instance = this;
      this.fillDelay.visibleWhen(this::isFillMode);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var1 -> this.onClientTick());
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.renderGizmos());
   }

   public List<Setting<?>> validateShulkers() {
      return List.of(this.mode, this.orderItem);
   }

   public List<Setting<?>> clickSlot() {
      return List.of(this.selectShulkerKey, this.fillDelay, this.highlightStyle, this.highlightOpacity);
   }

   public List<Setting<?>> isAfkActive() {
      return List.of(this.clickDelay, this.afkMode, this.stopWhenHurt, this.showMessages);
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public String getStatus() {
      return this.isRuntimeActive() ? this.status : (this.paused ? "Paused" : "Idle");
   }

   private boolean isFillMode() {
      return this.mode.isMode("Fill Shulkers");
   }

   public String describeSelection() {
      String var1 = OrderMenuParser.normalizeItemId(this.orderItem.getValue());
      String var2 = var1.isEmpty() ? "No order item chosen" : "Item: " + var1;
      int var3 = this.getSelectedShulkers().size();
      return var2
         + ". "
         + var3
         + (var3 == 1 ? " shulker" : " shulkers")
         + " selected"
         + (
            var3 == 0
               ? ""
               : ", " + this.freeSlots.size() + " scanned, " + this.fullShulkers.size() + " full, " + this.unavailableShulkers.size() + " unavailable"
         )
         + (this.paused ? ". Progress saved for this world session." : ".");
   }

   public void beginSelection() {
      if (!this.isInGame()) {
         throw new IllegalArgumentException("Join a world first.");
      }

      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      minecraft.gui.setScreen(null);
      this.startSelection();
   }

   public void setOrderItemFromHand() {
      if (this.isInGame() && !minecraft.player.getMainHandItem().isEmpty()) {
         this.orderItem.setText(updateSelectKey(minecraft.player.getMainHandItem()));
         LarpClient var1 = LarpClient.getInstance();
         if (var1 != null && var1.getConfigManager() != null) {
            var1.getConfigManager().saveActiveProfile();
         }
      } else {
         throw new IllegalArgumentException("Hold the order's item first.");
      }
   }

   public void clearShulkers() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before changing shulkers.");
      }

      this.resetProgress();
      this.selectedShulkers.clear();
      this.lastSelectedShulker = null;
   }

   public void resetProgress() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before resetting progress.");
      }

      this.restorePauseOnLostFocus();
      this.isMovementKeyDown();
      this.freeSlots.clear();
      this.fullShulkers.clear();
      this.unavailableShulkers.clear();
      this.scannedBlockEntities.clear();
      this.paused = (boolean)0;
   }

   public void rescan() {
      if (this.isRuntimeActive()) {
         throw new IllegalArgumentException("Turn Order Dropper off before rescanning shulkers.");
      }

      this.freeSlots.clear();
      this.fullShulkers.clear();
      this.unavailableShulkers.clear();
      this.scannedBlockEntities.clear();
   }

   private List<BlockPos> getSelectedShulkers() {
      Minecraft var1 = getMc();
      return var1 != null && var1.level == this.selectionLevel ? this.selectedShulkers : List.of();
   }

   @Override
   public void onEnable() {
      if (this.isInGame() && minecraft.player.connection != null) {
         if (this.selecting) {
            this.finishSelection();
         }

         String var1 = OrderMenuParser.normalizeItemId(this.orderItem.getValue());
         int var2 = this.paused
               && this.startPlayer == minecraft.player
               && this.startLevel == minecraft.level
               && this.startConnection == minecraft.player.connection
               && this.orderItemId.equals(var1)
               && this.isFillMode()
            ? 1
            : 0;
         if (var2 == 0) {
            this.isMovementKeyDown();
            this.paused = (boolean)0;
         }

         this.orderItemId = var1;
         if (this.orderItemId.isEmpty() || !BuiltInRegistries.ITEM.containsKey(Identifier.tryParse(this.orderItemId))) {
            this.refuseToStart("Choose the Order Item first, for example totem_of_undying, or hold it and use the held-item button.");
         } else if (this.isFillMode() && !((Item)BuiltInRegistries.ITEM.getValue(Identifier.parse(this.orderItemId))).canFitInsideContainerItems()) {
            this.refuseToStart("This item cannot go inside a shulker box. Choose Drop Only for shulker box orders.");
         } else if (this.isFillMode() && this.getSelectedShulkers().isEmpty()) {
            this.refuseToStart("Select at least one placed shulker box first: look at it and press the Select Shulker Key.");
         } else {
            this.startPlayer = minecraft.player;
            this.startLevel = minecraft.level;
            this.startConnection = minecraft.player.connection;
            if (var2 == 0) {
               this.startPos = minecraft.player.position();
               this.startYaw = minecraft.player.getYRot();
               this.startPitch = minecraft.player.getXRot();

               for (int var3 = 0; var3 < 36; var3++) {
                  if (this.isOrderItem(minecraft.player.getInventory().getItem(var3))) {
                     this.reservedSlots.add(var3);
                  }
               }
            }

            this.lastHealth = this.getAfkInput();
            if (var2 != 0 && this.storageFull) {
               this.ordersDone = (boolean)0;
               this.storageFull = (boolean)0;
            }

            this.paused = this.isFillMode();
            this.unavailableShulkers.clear();
            this.validateShulkers();
            this.scanning = (boolean)(this.isFillMode()
                  && this.getSelectedShulkers().stream().anyMatch(var1x -> !this.freeSlots.containsKey(var1x) && !this.unavailableShulkers.contains(var1x))
               ? 1
               : 0);
            if (this.scanning) {
               this.chooseShulker();
            } else if (var2 != 0 && this.fillShulker()) {
               this.chooseShulker();
            } else {
               this.setPhase(var2 != 0 ? OrderDropperModule.Phase.RETURN_TO_START : OrderDropperModule.Phase.ORDERS_MENU);
            }

            this.sendMessage(
               this.isFillMode()
                  ? (var2 != 0 ? "Resumed. " : "Started. ")
                     + "Emptying "
                     + this.orderItemId
                     + " orders into "
                     + this.getSelectedShulkers().size()
                     + " shulkers."
                  : "Started. Dropping every page of your " + this.orderItemId + " orders here."
            );
            LarpLog.info(
               "OrderDropper",
               "Started mode={} item={} shulkers={} start={}",
               this.mode.getValue(),
               this.orderItemId,
               this.getSelectedShulkers().size(),
               this.startPos
            );
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.startPlayer != null
         && this.isInGame()
         && minecraft.player == this.startPlayer
         && minecraft.player.containerMenu != minecraft.player.inventoryMenu
         && minecraft.player.containerMenu.getCarried().isEmpty()
         && this.renderGizmos()) {
         minecraft.player.closeContainer();
      }

      if (this.targetShulker != null && (this.phase == OrderDropperModule.Phase.FILL_SHULKER || this.verifying || this.transferCount > 0)) {
         this.freeSlots.remove(this.targetShulker);
      }

      this.restorePauseOnLostFocus();
      this.phase = null;
      this.walkForward = this.walkJump = this.walkSneak = (boolean)0;
      this.pendingClick = null;
      this.actionDelayTicks = 0;
      this.openRequested = this.menuCloseRequested = (boolean)0;
      this.menuReopenCount = this.idleTicks = this.commandWaitTicks = this.commandAttempts = this.navigationRetries = 0;
      this.targetShulker = null;
      this.walkTarget = null;
      this.triedSpots.clear();
      this.sendMessage();
   }

   private void isMovementKeyDown() {
      this.startPlayer = this.startLevel = this.startConnection = null;
      this.phase = null;
      this.startPos = null;
      this.status = "Idle";
      this.emptiedOrders.clear();
      this.reservedSlots.clear();
      this.ordersDone = this.pageDropped = this.openRequested = this.menuCloseRequested = (boolean)0;
      this.menuReopenCount = this.cursorHeldTicks = this.actionDelayTicks = this.phaseTicks = this.idleTicks = this.commandWaitTicks = this.commandAttempts = this.navigationRetries = this.nextPageCount = 0;
      this.pendingClick = null;
      this.clickWaitTicks = 0;
      this.targetShulker = null;
      this.currentOrderKey = "";
      this.stuckTicks = this.aimTicks = this.openWaitTicks = this.openFailures = 0;
      this.walkForward = this.walkJump = this.walkSneak = (boolean)0;
      this.pagesDropped = this.stacksTaken = this.stacksStored = this.tripCount = 0;
      this.scanning = this.verifying = this.baselineTaken = this.storageFull = (boolean)0;
      this.walkTarget = null;
      this.shulkerItemCount = this.shulkerStackCount = this.remainingWaitTicks = this.transferCount = this.rejectedTransfers = this.closeRetries = 0;
      this.triedSpots.clear();
      this.sendMessage();
   }

   @Override
   public void onTick() {
      this.walkForward = this.walkJump = this.walkSneak = (boolean)0;
      if (this.phase != null && this.isInGame() && minecraft.player.connection != null) {
         if (minecraft.player == this.startPlayer && minecraft.level == this.startLevel && minecraft.player.connection == this.startConnection) {
            this.getInstance();
            if (!minecraft.player.isAlive()) {
               this.stopWithReason("You died.");
            } else {
               float var1 = this.getAfkInput();
               if (this.stopWhenHurt.getValue()) {
                  float var13;
                  int var10000 = (var13 = var1 - (this.lastHealth - 0.01F)) == 0.0F ? 0 : (var13 < 0.0F ? -1 : 1);
                  boolean var3 = false;
                  int var2 = var10000;
                  if ((Integer.signum(var2) >>> 31 ^ 1) == 0) {
                     this.stopWithReason("You took damage.");
                     return;
                  }
               }

               this.lastHealth = var1;
               if (this.isMovementKeyDown()) {
                  this.stopWithReason("You moved.");
               } else if (this.startPos != null && minecraft.player.getY() < this.startPos.y - 2.5) {
                  this.stopWithReason("You fell below the start spot.");
               } else if (this.phase != OrderDropperModule.Phase.ORDERS_MENU
                  && minecraft.gui.screen() instanceof ContainerScreen
                  && OrderMenuParser.parseScreen(minecraft.gui.screen().getTitle().getString()) != OrderMenuParser.ScreenType.UNKNOWN) {
                  this.status = "Closing Orders before continuing";
                  int var10 = ++this.menuReopenCount;
                  byte var7 = 100;
                  int var6 = var10;
                  if ((-Integer.compare(var6, var7) >>> 31 ^ 1) == 0) {
                     this.stopWithReason("The Orders menu kept reopening.");
                  } else {
                     var10 = this.menuReopenCount % 4;
                     byte var5 = 1;
                     int var4 = var10;
                     var10 = Integer.compare(var4, var5);
                     if ((var10 | -var10) >>> 31 == 0) {
                        this.closeMenu();
                     }
                  }
               } else {
                  byte var9 = 1;
                  int var8 = this.phaseTicks;
                  this.phaseTicks = var8 + var9;
                  switch (this.phase) {
                     case ORDERS_MENU:
                        this.tickOrdersMenu();
                        break;
                     case PICKUP_DROPS:
                        this.skipShulker();
                        break;
                     case WALK_TO_SHULKER:
                        this.walkToShulker();
                        break;
                     case OPEN_SHULKER:
                        this.stopWithReason();
                        break;
                     case FILL_SHULKER:
                        this.fillShulker();
                        break;
                     case RETURN_TO_START:
                        this.describeSelection();
                        break;
                     case FINISH:
                        this.finish();
                  }
               }
            }
         } else {
            this.stopWithReason("The world changed.");
         }
      }
   }

   private void tickOrdersMenu() {
      if (this.actionDelayTicks > 0) {
         this.actionDelayTicks--;
      } else {
         Screen var1 = minecraft.gui.screen();
         if (this.menuCloseRequested) {
            if (var1 != null) {
               this.idleTicks = 0;
               if (++this.menuReopenCount > 100) {
                  this.stopWithReason("The Orders menu kept reopening.");
                  return;
               }

               if (this.menuReopenCount % 4 == 1 && this.renderGizmos()) {
                  this.closeMenu();
               }

               return;
            }

            if (++this.idleTicks < 4) {
               return;
            }

            this.menuCloseRequested = (boolean)0;
         }

         if (var1 == null) {
            if (this.pendingClick != null) {
               this.pendingClick = null;
            }

            if (this.isFillMode()) {
               if (this.pageDropped) {
                  this.setPhase(OrderDropperModule.Phase.PICKUP_DROPS);
                  return;
               }

               if (this.clearShulkers() == 0) {
                  if (this.fillShulker()) {
                     this.chooseShulker();
                  } else {
                     this.stopWithReason("Your inventory is full of other items. Make room and enable again.");
                  }

                  return;
               }

               if (this.countDroppedItems() > 0) {
                  this.setPhase(OrderDropperModule.Phase.PICKUP_DROPS);
                  return;
               }

               if (this.ordersDone) {
                  this.isWaitingForControl();
                  return;
               }

               if (minecraft.player.getXRot() < 88.0F) {
                  this.status = "Looking down";
                  RotationManager.requestForModule(this, RotationManager.Priority.ORDER_DROP, minecraft.player.getYRot(), 90.0F, 0.5);
                  return;
               }
            } else if (this.ordersDone) {
               this.countCarried();
               return;
            }

            if (++this.idleTicks >= 5) {
               if (this.commandWaitTicks <= 0 || ++this.commandWaitTicks >= 60) {
                  if (++this.commandAttempts > 3) {
                     this.stopWithReason("The /orders menu did not open after 3 tries.");
                  } else {
                     this.status = "Opening /orders";
                     minecraft.player.connection.sendCommand("orders");
                     this.commandWaitTicks = 1;
                     LarpLog.debug("OrderDropper", "Sent /orders (attempt {})", this.commandAttempts);
                  }
               }
            }
         } else {
            this.idleTicks = 0;
            if (!(var1 instanceof AbstractContainerScreen)) {
               this.status = "Waiting for the open screen to close";
            } else {
               ChestMenu var2 = this.isAfkActive();
               if (var2 == null) {
                  this.stopWithReason("A different menu opened: " + var1.getTitle().getString() + ".");
               } else {
                  this.commandAttempts = this.commandWaitTicks = 0;
                  if (this.pendingClick != null) {
                     int var5 = var2.containerId == this.pendingContainerId && restorePauseOnLostFocus(var2) == this.pendingMenuHash ? 0 : 1;
                     if (var5 != 0) {
                        this.pendingClick = null;
                        this.actionDelayTicks = this.getClickDelay();
                        this.navigationRetries = 0;
                     } else if (++this.clickWaitTicks > (this.pendingClick == OrderDropperModule.ClickKind.NAVIGATION ? 20 : 60)) {
                        if (this.pendingClick == OrderDropperModule.ClickKind.NAVIGATION && ++this.navigationRetries <= 3) {
                           this.pendingClick = null;
                        } else {
                           this.stopWithReason("The server did not respond to a menu click.");
                        }
                     }
                  } else {
                     ItemStack var3 = var2.getCarried();
                     if (var3.isEmpty()
                        || this.isOrderItem(var3) && OrderMenuParser.parseScreen(var1.getTitle().getString()) == OrderMenuParser.ScreenType.COLLECT_ITEMS) {
                        this.cursorHeldTicks = 0;
                        OrderMenuParser.ScreenType var4 = OrderMenuParser.parseScreen(var1.getTitle().getString());
                        if (this.setOrderItemFromHand(var2, var4)) {
                           switch (var4) {
                              case ORDERS_LIST:
                                 this.status = "Opening Your Orders";
                                 this.tickOrdersMenu(var2, OrderMenuParser.ButtonType.YOUR_ORDERS, OrderDropperModule.ClickKind.NAVIGATION);
                                 break;
                              case YOUR_ORDERS:
                                 this.reloadOrders(var2);
                                 break;
                              case EDIT_ORDER:
                                 this.status = "Opening Collect";
                                 this.tickOrdersMenu(var2, OrderMenuParser.ButtonType.COLLECT, OrderDropperModule.ClickKind.NAVIGATION);
                                 break;
                              case COLLECT_ITEMS:
                                 this.chooseShulker(var2);
                                 break;
                              case UNKNOWN:
                                 this.stopWithReason("An unexpected Orders menu opened: " + var1.getTitle().getString() + ".");
                           }
                        }
                     } else {
                        if (++this.cursorHeldTicks > 60) {
                           this.stopWithReason("The cursor still holds " + var3.getHoverName().getString() + ".");
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public static void sendChatMessage(int var0) {
      OrderDropperModule var1 = instance;
      Minecraft var2 = getMc();
      if (var1 != null && var1.phase == OrderDropperModule.Phase.ORDERS_MENU && var2 != null && var2.player != null) {
         AbstractContainerMenu var3 = var2.player.containerMenu;
         if (var3 instanceof ChestMenu && var3.containerId == var0) {
            var1.syncedMenu = var3;
            var1.settledTicks = 0;
         }
      }
   }

   private void sendMessage() {
      this.syncedMenu = this.settledMenu = null;
      this.settledHash = this.settledTicks = this.loadWaitTicks = this.reloadAttempts = 0;
   }

   private boolean setOrderItemFromHand(ChestMenu var1, OrderMenuParser.ScreenType var2) {
      int var3 = restorePauseOnLostFocus(var1);
      if (this.settledMenu == var1 && this.settledHash == var3) {
         this.settledTicks++;
      } else {
         this.settledMenu = var1;
         this.settledHash = var3;
         this.settledTicks = 0;
      }

      String var4 = null;
      if (this.syncedMenu != var1) {
         var4 = "server contents";
      } else if (this.settledTicks < 2) {
         var4 = "settled contents";
      } else if (var2 == OrderMenuParser.ScreenType.ORDERS_LIST && this.renderGizmos(var1, OrderMenuParser.ButtonType.YOUR_ORDERS) == -1) {
         var4 = "YOUR_ORDERS button";
      } else if (var2 == OrderMenuParser.ScreenType.EDIT_ORDER && this.renderGizmos(var1, OrderMenuParser.ButtonType.COLLECT) == -1) {
         var4 = "COLLECT button";
      } else if (var2 == OrderMenuParser.ScreenType.YOUR_ORDERS) {
         byte var5 = 0;

         for (int var6 = 0; var6 < Math.min(45, var1.getRowCount() * 9); var6++) {
            if (!var1.getSlot(var6).getItem().isEmpty()) {
               var5 = 1;
               break;
            }
         }

         if (var5 == 0) {
            var4 = "your order entries or empty-order placeholders";
         }
      }

      if (var4 == null) {
         this.loadWaitTicks = 0;
         return true;
      } else {
         this.status = "Waiting for Orders: " + var4;
         if (++this.loadWaitTicks <= 60) {
            return false;
         } else if (++this.reloadAttempts > 3) {
            this.stopWithReason("The Orders menu did not load " + var4 + " after 3 reloads. Progress retained.");
            return false;
         } else {
            LarpLog.warn(
               "OrderDropper",
               "Reloading Orders: missing={} title={} id={} synced={} retry={}/{}",
               var4,
               minecraft.gui.screen().getTitle().getString(),
               var1.containerId,
               this.syncedMenu == var1,
               this.reloadAttempts,
               3
            );
            this.loadWaitTicks = 0;
            this.reloadOrders();
            return false;
         }
      }
   }

   private void reloadOrders(ChestMenu var1) {
      for (int var2 = 0; var2 < 45 && var2 < var1.getRowCount() * 9; var2++) {
         ItemStack var3 = var1.getSlot(var2).getItem();
         if (!var3.isEmpty() && OrderMenuParser.isDelivered(updateSelectKey(var3), this.orderItemId, getSelectedShulkers(var3))) {
            String var4 = startSelection(var3);
            if (!this.emptiedOrders.contains(var4)) {
               this.currentOrderKey = var4;
               this.nextPageCount = 0;
               this.status = "Opening your " + var3.getHoverName().getString() + " order";
               LarpLog.debug("OrderDropper", "Opening order in slot {}: {}", var2, var4);
               this.clickSlot(var1, var2, ContainerInput.PICKUP, OrderDropperModule.ClickKind.NAVIGATION);
               return;
            }
         }
      }

      this.ordersDone = (boolean)1;
      this.sendMessage(
         this.emptiedOrders.isEmpty() ? "You have no " + this.orderItemId + " orders with items to collect." : "All " + this.orderItemId + " orders are empty."
      );
      LarpLog.info("OrderDropper", "Orders finished; emptied={} pages={} taken={}", this.emptiedOrders.size(), this.pagesDropped, this.stacksTaken);
      this.reloadOrders();
   }

   private void chooseShulker(ChestMenu var1) {
      int var2 = 0;
      int var3 = -1;

      for (int var4 = 0; var4 < 45; var4++) {
         if (!var1.getSlot(var4).getItem().isEmpty()) {
            var2++;
            if (var3 < 0) {
               var3 = var4;
            }
         }
      }

      int var6 = this.renderGizmos(var1, OrderMenuParser.ButtonType.DROP);
      int var5 = this.renderGizmos(var1, OrderMenuParser.ButtonType.NEXT_PAGE);
      if (!var1.getCarried().isEmpty()) {
         this.refuseToStart(var1);
      } else if (var2 != 0) {
         this.nextPageCount = 0;
         if (var6 >= 0) {
            if (this.isFillMode() && this.pageDropped) {
               this.reloadOrders();
            } else if (this.isFillMode() && var2 + this.countCarried() > this.onClientTick()) {
               this.sendMessage(
                  "The selected shulkers have room for " + this.onClientTick() + " stacks; this page has " + var2 + ". Select more shulkers to continue."
               );
               LarpLog.info("OrderDropper", "Not dropping: page={} carrying={} room={}", var2, this.countCarried(), this.onClientTick());
               this.ordersDone = (boolean)1;
               this.storageFull = (boolean)1;
               this.reloadOrders();
            } else {
               this.status = "Dropping page " + (this.pagesDropped + 1);
               this.tickOrdersMenu(var1, OrderMenuParser.ButtonType.DROP, OrderDropperModule.ClickKind.TRANSFER);
               this.pagesDropped++;
               this.pageDropped = (boolean)1;
               LarpLog.debug("OrderDropper", "Dropped page {} ({} stacks)", this.pagesDropped, var2);
            }
         } else if (this.clearShulkers() != 0) {
            this.status = "Taking items";
            this.clickSlot(var1, var3, ContainerInput.PICKUP, OrderDropperModule.ClickKind.TRANSFER);
         } else if (this.isFillMode() && this.fillShulker()) {
            this.reloadOrders();
         } else {
            this.stopWithReason("This order has no Drop button and your inventory is full. Make room and enable again.");
         }
      } else if (var5 >= 0 && ++this.nextPageCount <= 3) {
         this.status = "Next page";
         this.tickOrdersMenu(var1, OrderMenuParser.ButtonType.NEXT_PAGE, OrderDropperModule.ClickKind.TRANSFER);
      } else {
         this.emptiedOrders.add(this.currentOrderKey);
         this.sendMessage("Order emptied.");
         LarpLog.info("OrderDropper", "Order emptied after {} pages: {}", this.pagesDropped, this.currentOrderKey);
         this.finishOrder();
      }
   }

   private void refuseToStart(ChestMenu var1) {
      for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
         Slot var3 = var1.getSlot(var2);
         if (var3.container == minecraft.player.getInventory() && var3.getContainerSlot() < 36 && var3.getItem().isEmpty()) {
            this.clickSlot(var1, var2, ContainerInput.PICKUP, OrderDropperModule.ClickKind.TRANSFER);
            this.stacksTaken++;
            return;
         }
      }

      this.stopWithReason("No empty inventory slot for the item on the cursor.");
   }

   private int renderGizmos(ChestMenu var1, OrderMenuParser.ButtonType var2) {
      int var3 = -1;

      for (int var4 = var1.getRowCount() == 6 ? 45 : 0; var4 < var1.getRowCount() * 9; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty() && OrderMenuParser.classifyButton(updateSelectKey(var5), var5.getHoverName().getString()) == var2) {
            if (var3 >= 0) {
               return -2;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void tickOrdersMenu(ChestMenu var1, OrderMenuParser.ButtonType var2, OrderDropperModule.ClickKind var3) {
      int var4 = this.renderGizmos(var1, var2);
      if (var4 == -2) {
         this.stopWithReason("Two " + var2 + " buttons found; this menu layout needs review.");
      } else if (var4 < 0) {
         this.stopWithReason("No " + var2 + " button in this menu.");
      } else {
         this.clickSlot(var1, var4, ContainerInput.PICKUP, var3);
      }
   }

   private void clickSlot(AbstractContainerMenu var1, int var2, ContainerInput var3, OrderDropperModule.ClickKind var4) {
      ItemStack var5 = var1.getSlot(var2).getItem();
      if (var2 < 54
         && var1 instanceof ChestMenu
         && OrderMenuParser.classifyButton(updateSelectKey(var5), var5.getHoverName().getString()) == OrderMenuParser.ButtonType.SELL) {
         this.stopWithReason("Refused to click a Sell button.");
      } else {
         this.pendingClick = var4;
         this.pendingContainerId = var1.containerId;
         this.clickWaitTicks = 0;
         this.loadWaitTicks = this.reloadAttempts = 0;
         minecraft.gameMode.handleContainerInput(var1.containerId, var2, 0, var3, minecraft.player);
         this.pendingMenuHash = restorePauseOnLostFocus(var1);
      }
   }

   private void finishOrder() {
      this.currentOrderKey = "";
      this.nextPageCount = 0;
      this.reloadOrders();
   }

   private void reloadOrders() {
      this.menuCloseRequested = (boolean)1;
      this.menuReopenCount = 0;
      this.pendingClick = null;
      this.idleTicks = 0;
      this.commandWaitTicks = 0;
      this.commandAttempts = 0;
      this.closeMenu();
   }

   private void closeMenu() {
      if (minecraft.player.containerMenu != minecraft.player.inventoryMenu && minecraft.player.containerMenu.getCarried().isEmpty()) {
         minecraft.player.closeContainer();
      }
   }

   private ChestMenu isAfkActive() {
      if (minecraft.gui.screen() instanceof ContainerScreen var1
         && minecraft.player.containerMenu instanceof ChestMenu var2
         && var1.getMenu() == var2
         && (var2.getRowCount() == 6 || var2.getRowCount() == 3)) {
         OrderMenuParser.ScreenType var5 = OrderMenuParser.parseScreen(var1.getTitle().getString());
         return var5 == OrderMenuParser.ScreenType.UNKNOWN && !OrderMenuParser.stripFormatting(var1.getTitle().getString()).startsWith("orders") ? null : var2;
      } else {
         return null;
      }
   }

   private boolean renderGizmos() {
      return minecraft.gui.screen() instanceof ShulkerBoxScreen
         ? this.targetShulker != null
         : minecraft.gui.screen() != null && OrderMenuParser.parseScreen(minecraft.gui.screen().getTitle().getString()) != OrderMenuParser.ScreenType.UNKNOWN;
   }

   private void skipShulker() {
      this.status = "Picking up items";
      int var1 = this.countDroppedItems();
      if (this.clearShulkers() <= 0 || this.phaseTicks >= 120 || var1 <= 0 && this.phaseTicks >= 10) {
         if (var1 > 0 && this.clearShulkers() > 0) {
            LarpLog.warn("OrderDropper", "{} item stacks stayed out of pickup reach", var1);
         }

         if (this.clearShulkers() != 0 && (!this.ordersDone || !this.fillShulker())) {
            if (var1 > 0 && !this.fillShulker()) {
               this.stopWithReason("The dropped items landed out of pickup reach.");
            } else if (this.ordersDone) {
               this.isWaitingForControl();
            } else {
               this.countDroppedItems();
            }
         } else {
            this.chooseShulker();
         }
      }
   }

   private void countDroppedItems() {
      this.pageDropped = (boolean)0;
      this.setPhase(OrderDropperModule.Phase.ORDERS_MENU);
   }

   private void isWaitingForControl() {
      if (this.fillShulker()) {
         this.chooseShulker();
      } else {
         this.countCarried();
      }
   }

   private void chooseShulker() {
      this.targetShulker = null;
      this.validateShulkers();
      Vec3 var1 = minecraft.player.position();
      double var2 = Double.MAX_VALUE;

      for (BlockPos var5 : this.getSelectedShulkers()) {
         if (!this.unavailableShulkers.contains(var5) && (this.scanning ? !this.freeSlots.containsKey(var5) : !this.fullShulkers.contains(var5))) {
            double var6 = Vec3.atCenterOf(var5).distanceToSqr(var1);
            if (var6 < var2) {
               var2 = var6;
               this.targetShulker = var5;
            }
         }
      }

      if (this.targetShulker == null) {
         if (this.scanning) {
            this.scanning = (boolean)0;
            this.setPhase(OrderDropperModule.Phase.RETURN_TO_START);
         } else {
            this.storageFull = (boolean)1;
            if (this.onClientTick(this.startPos) > 0.4) {
               this.setPhase(OrderDropperModule.Phase.RETURN_TO_START);
               this.sendMessage("No usable shulkers remain; walking back.");
               this.ordersDone = (boolean)1;
            } else {
               this.stopWithReason("No usable shulkers remain. Select more or rescan, then enable again.");
            }
         }
      } else {
         if (!this.scanning) {
            this.tripCount++;
         }

         this.verifying = this.baselineTaken = (boolean)0;
         this.remainingWaitTicks = this.transferCount = this.rejectedTransfers = this.closeRetries = 0;
         this.walkTarget = null;
         this.triedSpots.clear();
         this.bestWalkDistance = Double.MAX_VALUE;
         this.stuckTicks = 0;
         this.setPhase(OrderDropperModule.Phase.WALK_TO_SHULKER);
         LarpLog.debug("OrderDropper", "Trip {} to shulker {} carrying {} stacks", this.tripCount, this.targetShulker.toShortString(), this.countCarried());
      }
   }

   private void walkToShulker() {
      if (!this.isShulker(this.targetShulker)) {
         this.skipShulker("is missing or unloaded");
      } else if (minecraft.gui.screen() != null) {
         this.status = "Close the screen to continue";
      } else if (!this.isWaitingForControl()) {
         this.status = this.scanning ? "Walking to scan shulker" : "Walking to shulker";
         if (this.reloadOrders() && this.fillShulker(minecraft.player.getEyePosition()) != null) {
            this.aimTicks = this.openFailures = 0;
            this.openRequested = (boolean)0;
            this.setPhase(OrderDropperModule.Phase.OPEN_SHULKER);
         } else {
            if (this.walkTarget != null && this.onClientTick(this.walkTarget) < 0.15 && Math.abs(minecraft.player.getY() - this.walkTarget.y) < 0.35) {
               this.triedSpots.add(this.walkTarget);
               LarpLog.debug(
                  "OrderDropper",
                  "Replanning shulker {} from eye={} after arriving at {}",
                  this.targetShulker.toShortString(),
                  minecraft.player.getEyePosition(),
                  this.walkTarget
               );
               this.walkTarget = null;
            }

            if (this.walkTarget == null) {
               this.walkTarget = this.setPhase();
               this.bestWalkDistance = Double.MAX_VALUE;
               this.stuckTicks = 0;
            }

            if (this.walkTarget == null) {
               LarpLog.warn(
                  "OrderDropper",
                  "No visible standing spot for {}: eye={} reach={} tried={}",
                  this.targetShulker.toShortString(),
                  minecraft.player.getEyePosition(),
                  this.getInteractionRange(),
                  this.triedSpots.size()
               );
               this.skipShulker("has no clear reachable standing spot");
            } else {
               this.walkTo(this.walkTarget, 0.1);
            }
         }
      }
   }

   private void stopWithReason() {
      if (!this.isShulker(this.targetShulker)) {
         this.skipShulker("is missing or unloaded");
      } else if (minecraft.gui.screen() instanceof ShulkerBoxScreen var1
         && minecraft.player.containerMenu instanceof ShulkerBoxMenu var2
         && var1.getMenu() == var2) {
         this.openRequested = (boolean)0;
         this.actionDelayTicks = this.getClickDelay();
         this.setPhase(OrderDropperModule.Phase.FILL_SHULKER);
      } else if (!this.reloadOrders() && !this.openRequested) {
         this.walkTarget = null;
         this.bestWalkDistance = Double.MAX_VALUE;
         this.stuckTicks = 0;
         this.setPhase(OrderDropperModule.Phase.WALK_TO_SHULKER);
      } else {
         this.status = "Opening shulker";
         if (this.openRequested) {
            if (++this.openWaitTicks <= 60) {
               return;
            }

            this.openRequested = (boolean)0;
            if (++this.openFailures >= 3) {
               this.skipShulker("did not open");
               return;
            }
         }

         if (minecraft.gui.screen() == null) {
            if (!this.isWaitingForControl()) {
               BlockHitResult var6 = this.fillShulker(minecraft.player.getEyePosition());
               if (var6 == null) {
                  this.walkTarget = null;
                  this.setPhase(OrderDropperModule.Phase.WALK_TO_SHULKER);
               } else {
                  Vec3 var7 = refuseToStart(this.targetShulker, var6);
                  float[] var9 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), var7);
                  RotationManager.requestForModule(this, RotationManager.Priority.ORDER_DROP, var9[0], var9[1], 0.5);
                  BlockHitResult var4 = InteractionArbiter.wasManualClaimed(this.targetShulker, this.getInteractionRange());
                  if (var4 == null) {
                     this.aimTicks = 0;
                     if (this.phaseTicks > 60) {
                        this.skipShulker("could not be aimed at");
                     }
                  } else if (++this.aimTicks >= 2 && !minecraft.player.isShiftKeyDown()) {
                     boolean var5 = InteractionArbiter.tryRunAction(this, () -> {
                        if (minecraft.gameMode.useItemOn(minecraft.player, InteractionHand.MAIN_HAND, var4).consumesAction()) {
                           SwingHelper.swingInteract(minecraft.player, InteractionHand.MAIN_HAND);
                        }
                     }, () -> minecraft.gui.screen() == null);
                     if (var5) {
                        this.openRequested = (boolean)1;
                        this.openWaitTicks = 0;
                     }
                  }
               }
            }
         }
      }
   }

   private void fillShulker() {
      if (minecraft.gui.screen() instanceof ShulkerBoxScreen var1 && minecraft.player.containerMenu instanceof ShulkerBoxMenu var2 && var1.getMenu() == var2) {
         this.status = this.scanning ? "Scanning shulker" : "Filling shulker";
         if (!var2.getCarried().isEmpty()) {
            this.stopWithReason("The cursor holds an item inside the shulker.");
         } else if (this.actionDelayTicks <= 0 || --this.actionDelayTicks <= 0) {
            if (this.scanning) {
               this.recordShulkerScan(var2);
               minecraft.player.closeContainer();
               this.chooseShulker();
            } else {
               if (this.verifying) {
                  int var7 = this.refuseToStart(var2);
                  int var4 = this.stopWithReason(var2);
                  int var5 = Math.max(0, var7 - this.shulkerItemCount);
                  this.stacksStored = this.stacksStored + Math.max(0, var4 - this.shulkerStackCount);
                  LarpLog.info(
                     "OrderDropper",
                     "Verified shulker {}: items added={} ({} -> {}), carrying={} stacks",
                     this.targetShulker.toShortString(),
                     var5,
                     this.shulkerItemCount,
                     var7,
                     this.countCarried()
                  );
                  this.shulkerItemCount = var7;
                  this.shulkerStackCount = var4;
                  this.recordShulkerScan(var2);
                  this.verifying = (boolean)0;
                  this.transferCount = 0;
                  if (var5 > 0) {
                     this.rejectedTransfers = 0;
                  } else if (++this.rejectedTransfers > 3 && this.fillShulker()) {
                     this.stopWithReason("Shulker transfers kept being rejected. Progress retained; check the server and enable again.");
                     return;
                  }
               }

               if (!this.baselineTaken) {
                  this.shulkerItemCount = this.refuseToStart(var2);
                  this.shulkerStackCount = this.stopWithReason(var2);
                  this.baselineTaken = (boolean)1;
               }

               if (this.transferCount >= 36) {
                  this.reopenToVerify();
               } else {
                  for (int var8 = 27; var8 < var2.slots.size(); var8++) {
                     Slot var10 = var2.getSlot(var8);
                     ItemStack var11 = var10.getItem();
                     if (this.isOrderItem(var11)
                        && (var10.container != minecraft.player.getInventory() || !this.reservedSlots.contains(var10.getContainerSlot()))
                        && this.chooseShulker(var2, var11)) {
                        minecraft.gameMode.handleContainerInput(var2.containerId, var8, 0, ContainerInput.QUICK_MOVE, minecraft.player);
                        this.transferCount++;
                        this.remainingWaitTicks = 0;
                        this.actionDelayTicks = this.fillDelay.getValue().intValue();
                        return;
                     }
                  }

                  if (!this.fillShulker() && ++this.remainingWaitTicks < Math.max(10, this.fillDelay.getValue().intValue())) {
                     this.status = "Waiting for remaining items";
                  } else if (this.transferCount > 0) {
                     this.reopenToVerify();
                  } else {
                     this.recordShulkerScan(var2);
                     boolean var9 = this.fillShulker();
                     if (var9) {
                        this.fullShulkers.add(this.targetShulker);
                        this.sendMessage("Shulker full (" + this.fullShulkers.size() + "/" + this.getSelectedShulkers().size() + ").");
                     }

                     minecraft.player.closeContainer();
                     LarpLog.debug("OrderDropper", "Closed shulker {}; full={} carrying={}", this.targetShulker.toShortString(), var9, this.countCarried());
                     if (var9) {
                        this.chooseShulker();
                     } else {
                        this.setPhase(OrderDropperModule.Phase.RETURN_TO_START);
                     }
                  }
               }
            }
         }
      } else {
         this.freeSlots.remove(this.targetShulker);
         if (++this.closeRetries > 3) {
            this.skipShulker("kept closing before filling finished");
         } else {
            this.walkTarget = null;
            this.bestWalkDistance = Double.MAX_VALUE;
            this.stuckTicks = 0;
            this.setPhase(OrderDropperModule.Phase.WALK_TO_SHULKER);
         }
      }
   }

   private void reopenToVerify() {
      this.verifying = (boolean)1;
      this.openRequested = (boolean)0;
      this.aimTicks = this.openFailures = 0;
      minecraft.player.closeContainer();
      this.setPhase(OrderDropperModule.Phase.OPEN_SHULKER);
      LarpLog.debug("OrderDropper", "Reopening {} to verify transfers", this.targetShulker.toShortString());
   }

   private int refuseToStart(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.isOrderItem(var1.getSlot(var3).getItem())) {
            var2 += var1.getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   private int stopWithReason(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (this.isOrderItem(var1.getSlot(var3).getItem())) {
            var2++;
         }
      }

      return var2;
   }

   private boolean chooseShulker(ShulkerBoxMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < 27; var3++) {
         Slot var4 = var1.getSlot(var3);
         ItemStack var5 = var4.getItem();
         if (var5.isEmpty() ? var4.mayPlace(var2) : ItemStack.isSameItemSameComponents(var5, var2) && var5.getCount() < var4.getMaxStackSize(var5)) {
            return true;
         }
      }

      return false;
   }

   private void skipShulker(String var1) {
      this.unavailableShulkers.add(this.targetShulker);
      this.freeSlots.remove(this.targetShulker);
      this.fullShulkers.remove(this.targetShulker);
      this.sendMessage("Skipped a shulker that " + var1 + ".");
      LarpLog.warn("OrderDropper", "Shulker {} {}; skipped", this.targetShulker.toShortString(), var1);
      this.chooseShulker();
   }

   private void describeSelection() {
      this.status = "Walking back";
      if (this.onClientTick(this.startPos) > 0.3) {
         this.walkTo(this.startPos, 0.3);
      } else if (this.isFillMode() && this.countDroppedItems() > 0 && this.clearShulkers() > 0) {
         this.setPhase(OrderDropperModule.Phase.PICKUP_DROPS);
      } else if (this.fillShulker()
         && this.getSelectedShulkers().stream().anyMatch(var1 -> !this.fullShulkers.contains(var1) && !this.unavailableShulkers.contains(var1))) {
         this.chooseShulker();
      } else if (!this.ordersDone) {
         this.countDroppedItems();
      } else {
         this.countCarried();
      }
   }

   private void countCarried() {
      if (this.isFillMode() && this.onClientTick(this.startPos) > 0.3) {
         this.setPhase(OrderDropperModule.Phase.RETURN_TO_START);
      } else if (this.isFillMode() && (this.fillShulker() || this.countDroppedItems() > 0)) {
         this.stopWithReason("Storage unavailable with items still to store. Select more shulkers or rescan, then enable again.");
      } else {
         this.setPhase(OrderDropperModule.Phase.FINISH);
      }
   }

   private void finish() {
      this.status = "Finishing";
      if (!this.isFillMode()
         || this.phaseTicks >= 20
         || !(Math.abs(Mth.wrapDegrees(minecraft.player.getYRot() - this.startYaw)) > 1.0F) && !(Math.abs(minecraft.player.getXRot() - this.startPitch) > 1.0F)
         )
       {
         this.sendChatMessage(
            "Done: "
               + this.pagesDropped
               + " pages dropped, "
               + this.stacksTaken
               + " stacks taken"
               + (this.isFillMode() ? ", " + this.stacksStored + " stacks stored in " + this.tripCount + " trips." : ".")
         );
         LarpLog.info("OrderDropper", "Finished pages={} taken={} stored={} trips={}", this.pagesDropped, this.stacksTaken, this.stacksStored, this.tripCount);
         this.paused = (boolean)0;
         this.disable();
      } else {
         RotationManager.requestForModule(this, RotationManager.Priority.ORDER_DROP, this.startYaw, this.startPitch, 0.5);
      }
   }

   private void walkTo(Vec3 var1, double var2) {
      if (!this.isWaitingForControl()) {
         double var4 = this.onClientTick(var1);
         if (var4 < this.bestWalkDistance - 0.05) {
            this.bestWalkDistance = var4;
            this.stuckTicks = 0;
         } else if (++this.stuckTicks > 50) {
            this.stopWithReason("The way is blocked; stopped where it was.");
            return;
         }

         Vec3 var6 = var1.subtract(minecraft.player.position());
         float var7 = (float)Math.toDegrees(Math.atan2(var6.z, var6.x)) - 90.0F;
         RotationManager.requestForModule(this, RotationManager.Priority.ORDER_DROP, var7, 25.0F, 0.35);
         this.walkForward = (boolean)(Math.abs(Mth.wrapDegrees(minecraft.player.getYRot() - var7)) < 35.0F ? 1 : 0);
         this.walkJump = (boolean)(this.walkForward && minecraft.player.horizontalCollision && minecraft.player.onGround() ? 1 : 0);
         this.walkSneak = (boolean)(var2 > 0.0 && var4 < 1.0 ? 1 : 0);
      }
   }

   public static Input getAfkInput() {
      if (instance != null && instance.isEnabled()) {
         OrderDropperModule var0 = instance;
         if (var0 == null
            || !var0.isRuntimeActive()
            || getMc().gui.screen() != null
            || ViewController.isFreecam()
            || RotationManager.hasRequest() && !RotationManager.isActive(RotationManager.Priority.ORDER_DROP)) {
            return null;
         } else if (!var0.walkForward) {
            return isAfkActive() ? new Input(false, false, false, false, false, false, false) : null;
         } else {
            return new Input(true, false, false, false, var0.walkJump, var0.walkSneak, false);
         }
      } else {
         return null;
      }
   }

   private boolean isMovementKeyDown() {
      if (minecraft.gui.screen() == null && minecraft.isWindowActive()) {
         Options var1 = minecraft.options;
         return PlayerInputUtil.isKeyDown(var1.keyUp)
            || PlayerInputUtil.isKeyDown(var1.keyDown)
            || PlayerInputUtil.isKeyDown(var1.keyLeft)
            || PlayerInputUtil.isKeyDown(var1.keyRight)
            || PlayerInputUtil.isKeyDown(var1.keyJump);
      } else {
         return false;
      }
   }

   private void onClientTick() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.getWindow() != null) {
         if (this.selectionLevel != null && var1.level != this.selectionLevel && !this.isRuntimeActive()) {
            this.selecting = (boolean)0;
            this.hoveredShulker = null;
            this.resetProgress();
            this.selectedShulkers.clear();
            this.selectionMarks.clear();
            this.lastSelectedShulker = null;
            this.selectionLevel = null;
         }

         if (this.selecting) {
            if (!this.isInGame() || var1.level != this.selectionLevel) {
               this.selecting = (boolean)0;
               this.hoveredShulker = null;
            } else if (!this.isRuntimeActive() && var1.gui.screen() == null) {
               if (System.nanoTime() - this.lastHintNanos > 2000000000L) {
                  this.showSelectionHint();
               }
            } else {
               this.finishSelection();
            }
         }
      }
   }

   private void ensureLevel() {
      if (this.selectionLevel != minecraft.level) {
         this.resetProgress();
         this.selectedShulkers.clear();
         this.selectionMarks.clear();
         this.lastSelectedShulker = null;
         this.selectionLevel = minecraft.level;
      }
   }

   private void startSelection() {
      this.ensureLevel();
      this.selecting = (boolean)1;
      this.lastSelectedShulker = this.lastClickedShulker = this.hoveredShulker = null;
      this.useReleased = this.attackReleased = this.prevUseDown = this.prevAttackDown = this.areaSelected = (boolean)0;
      if (minecraft.gameMode != null) {
         minecraft.gameMode.stopDestroyBlock();
      }

      this.showSelectionHint();
      LarpLog.debug("OrderDropper", "Selection started with {} shulkers", this.selectedShulkers.size());
   }

   private void finishSelection() {
      this.selecting = (boolean)0;
      this.hoveredShulker = this.lastClickedShulker = null;
      this.prevUseDown = this.prevAttackDown = this.areaSelected = (boolean)0;
      this.onClientTick("Shulker selection saved. " + this.selectedShulkers.size() + " selected.");
      LarpLog.info("OrderDropper", "Selection finished with {} shulkers", this.selectedShulkers.size());
   }

   private void showSelectionHint() {
      int var1 = this.selectShulkerKey.getValue();
      this.onClientTick(
         this.selectedShulkers.size()
            + " selected. Right-click adds, left-click removes, hold to sweep, sneak-click for an area. "
            + (BindCodes.isValidBind(var1) ? BindCodes.displayName(var1) + " or Esc" : "Esc")
            + " to finish."
      );
   }

   public static boolean skipShulker() {
      OrderDropperModule var0 = instance;
      return var0 != null && var0.selecting;
   }

   private void setPhase() {
      if (!this.isInGame()) {
         this.hoveredShulker = null;
         this.selectKeyDown = (boolean)0;
      } else {
         this.updateSelectKey();
         int var1 = this.selectKeyDown && !this.selectKeyBlocked ? 1 : 0;
         if ((this.selecting || var1 != 0) && minecraft.gui.screen() == null) {
            HitResult var2 = minecraft.player.pick(24.0, minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false), false);
            this.hoveredShulker = var2 instanceof BlockHitResult var3 && var2.getType() == Type.BLOCK && this.isShulker(var3.getBlockPos())
               ? var3.getBlockPos().immutable()
               : null;
            if (var1 != 0) {
               this.getInteractionRange(this.hoveredShulker);
            } else {
               boolean var7 = PlayerInputUtil.isKeyDown(minecraft.options.keyUse);
               boolean var4 = PlayerInputUtil.isKeyDown(minecraft.options.keyAttack);
               if (!var7) {
                  this.useReleased = (boolean)1;
               }

               if (!var4) {
                  this.attackReleased = (boolean)1;
               }

               int var5 = var7 && this.useReleased ? 1 : 0;
               int var6 = var4 && this.attackReleased && var5 == 0 ? 1 : 0;
               if ((var5 == 0 || this.prevUseDown) && (var6 == 0 || this.prevAttackDown)) {
                  if ((var5 != 0 || var6 != 0) && !this.areaSelected && this.hoveredShulker != null && !this.hoveredShulker.equals(this.lastClickedShulker)) {
                     if (this.describeSelection(this.hoveredShulker, (boolean)var5)) {
                        this.finishOrder((boolean)var5, 1);
                     }

                     this.lastClickedShulker = this.lastSelectedShulker = this.hoveredShulker;
                  }
               } else {
                  this.renderGizmos((boolean)var5);
               }

               this.prevUseDown = (boolean)var5;
               this.prevAttackDown = (boolean)var6;
            }
         } else {
            this.hoveredShulker = null;
         }
      }
   }

   private void updateSelectKey() {
      int var1 = this.selectShulkerKey.getValue();
      int var2 = BindCodes.isValidBind(var1)
            && minecraft.gui.screen() == null
            && minecraft.isWindowActive()
            && BindCodes.isPressed(minecraft.getWindow().handle(), var1)
         ? 1
         : 0;
      if (var2 != 0 && !this.selectKeyDown) {
         this.selectKeyBlocked = this.isRuntimeActive();
         if (this.selectKeyBlocked) {
            this.onClientTick("Turn Order Dropper off before changing shulkers.");
         } else {
            this.ensureLevel();
            this.sweepAdding = null;
            this.lastSweepPos = null;
            this.sweepCount = 0;
            this.selectKeyPressNanos = System.nanoTime();
         }
      } else if (var2 == 0 && this.selectKeyDown && !this.selectKeyBlocked) {
         if (this.sweepAdding == null && System.nanoTime() - this.selectKeyPressNanos < 400000000L) {
            if (this.selecting) {
               this.finishSelection();
            } else {
               this.startSelection();
            }
         } else if (this.sweepAdding != null) {
            LarpLog.info(
               "OrderDropper", "Key sweep {} {} shulkers; total {}", this.sweepAdding ? "added" : "removed", this.sweepCount, this.selectedShulkers.size()
            );
         }
      }

      this.selectKeyDown = (boolean)var2;
   }

   private void getInteractionRange(BlockPos var1) {
      if (var1 != null && !var1.equals(this.lastSweepPos)) {
         this.lastSweepPos = var1;
         if (this.sweepAdding == null) {
            this.sweepAdding = !this.selectedShulkers.contains(var1);
         }

         if (this.describeSelection(var1, this.sweepAdding)) {
            this.lastSelectedShulker = var1;
            this.finishOrder(this.sweepAdding, ++this.sweepCount);
         }
      }
   }

   private void renderGizmos(boolean var1) {
      this.lastClickedShulker = null;
      this.areaSelected = (boolean)0;
      if (this.hoveredShulker == null) {
         this.onClientTick("Look at a placed shulker box.");
      } else {
         if (PlayerInputUtil.isKeyDown(minecraft.options.keyShift) && this.lastSelectedShulker != null && !this.lastSelectedShulker.equals(this.hoveredShulker)
            )
          {
            this.areaSelected = (boolean)1;
            List var2 = OrderMenuParser.expandBox(getClickDelay(this.lastSelectedShulker), getClickDelay(this.hoveredShulker));
            if (var2.isEmpty()) {
               this.onClientTick("That area is too large. Keep each side within 32 blocks.");
               return;
            }

            int var3 = 0;

            for (OrderMenuParser.BlockCoord var5 : var2) {
               if (this.describeSelection(new BlockPos(var5.getX(), var5.getY(), var5.getZ()), var1)) {
                  var3++;
               }
            }

            this.finishOrder(var1, var3);
         } else {
            if (this.describeSelection(this.hoveredShulker, var1)) {
               this.finishOrder(var1, 1);
            } else {
               this.onClientTick((var1 ? "Already selected; left-click removes it. " : "Not selected. ") + this.selectedShulkers.size() + " selected.");
            }

            this.lastClickedShulker = this.hoveredShulker;
         }

         this.lastSelectedShulker = this.hoveredShulker;
      }
   }

   private boolean describeSelection(BlockPos var1, boolean var2) {
      if (var2 ? !this.selectedShulkers.contains(var1) && this.isShulker(var1) : this.selectedShulkers.remove(var1)) {
         if (var2) {
            this.selectedShulkers.add(var1.immutable());
         } else {
            this.fullShulkers.remove(var1);
            this.freeSlots.remove(var1);
            this.unavailableShulkers.remove(var1);
            this.scannedBlockEntities.remove(var1);
         }

         this.selectionMarks.put(var1.immutable(), new OrderDropperModule.SelectionMark(System.nanoTime(), var2));
         return true;
      } else {
         return false;
      }
   }

   private void finishOrder(boolean var1, int var2) {
      this.onClientTick((var1 ? "Added " : "Removed ") + var2 + (var2 == 1 ? " shulker. " : " shulkers. ") + this.selectedShulkers.size() + " selected.");
      LarpLog.debug("OrderDropper", "{} {} shulkers; total {}", var1 ? "Added" : "Removed", var2, this.selectedShulkers.size());
   }

   private static OrderMenuParser.BlockCoord getClickDelay(BlockPos var0) {
      return new OrderMenuParser.BlockCoord(var0.getX(), var0.getY(), var0.getZ());
   }

   private int refuseToStart(BlockPos var1, boolean var2) {
      return this.unavailableShulkers.contains(var1)
         ? -1876386
         : (this.fullShulkers.contains(var1) ? -2061766 : (this.isOrderItem(var1, var2) ? -11702 : -13180806));
   }

   private static int validateShulkers(int var0) {
      return var0 == -11702 ? -10733056 : (var0 == -2061766 ? -11918843 : (var0 == -1876386 ? -11923692 : -16040668));
   }

   private boolean isOrderItem(BlockPos var1, boolean var2) {
      return var2
         && var1.equals(this.targetShulker)
         && (
            this.phase == OrderDropperModule.Phase.WALK_TO_SHULKER
               || this.phase == OrderDropperModule.Phase.OPEN_SHULKER
               || this.phase == OrderDropperModule.Phase.FILL_SHULKER
         );
   }

   private String getShulkerLabel(BlockPos var1, boolean var2) {
      if (this.unavailableShulkers.contains(var1)) {
         return "Skipped";
      }

      if (this.isOrderItem(var1, var2)) {
         return this.scanning
            ? "Scanning"
            : (this.phase == OrderDropperModule.Phase.FILL_SHULKER ? "Filling" : (this.phase == OrderDropperModule.Phase.WALK_TO_SHULKER ? "Next" : "Opening"));
      }

      if (this.fullShulkers.contains(var1)) {
         return "Full";
      }

      Integer var3 = this.freeSlots.get(var1);
      return var3 == null ? null : var3 + " free";
   }

   private void renderGizmos() {
      Minecraft var1 = getMc();
      if (var1 != null && var1.level != null) {
         try {
            this.setPhase();
            if (var1.level != this.selectionLevel) {
               return;
            }

            long var2 = System.nanoTime();
            if (!this.selectedShulkers.isEmpty()) {
               this.chooseShulker(var1.gameRenderer.mainCamera().position());
            }

            this.tickOrdersMenu(var2);
            if (this.hoveredShulker != null) {
               this.drawHoverBox();
            }
         } catch (RuntimeException var4) {
            LarpLog.errorOnce("OrderDropper", "Shulker highlight failed", var4);
         }
      }
   }

   private void chooseShulker(Vec3 var1) {
      boolean var2 = this.isRuntimeActive();

      for (BlockPos var4 : this.selectedShulkers) {
         String var5 = this.getShulkerLabel(var4, var2);
         Vec3 var6 = Vec3.atCenterOf(var4);
         if (var5 != null && var1.distanceToSqr(var6) < 100.0) {
            Gizmos.billboardText(var5, var6, Style.forColorAndCentered(-854792).withScale(0.4F)).setAlwaysOnTop();
         }
      }
   }

   private void tickOrdersMenu(long var1) {
      Iterator var3 = this.selectionMarks.entrySet().iterator();

      while (var3.hasNext()) {
         Entry var4 = (Entry)var3.next();
         double var5 = 1.0 - (var1 - ((OrderDropperModule.SelectionMark)var4.getValue()).nanoTime()) / 4.5E8;
         if (var5 <= 0.0) {
            var3.remove();
         } else {
            int var7 = ((OrderDropperModule.SelectionMark)var4.getValue()).added() ? -13180806 : -1017711;
            Gizmos.cuboid(new AABB((BlockPos)var4.getKey()).inflate(0.02 + 0.06 * (1.0 - var5)), GizmoStyle.fill(ColorUtil.withAlpha(var7, 0.4 * var5)))
               .setAlwaysOnTop();
         }
      }
   }

   private void drawHoverBox() {
      boolean var1 = this.selectedShulkers.contains(this.hoveredShulker);
      Gizmos.cuboid(
            new AABB(this.hoveredShulker).inflate(0.012), GizmoStyle.strokeAndFill(-854792, 2.5F, ColorUtil.withAlpha(var1 ? -1017711 : -13180806, 0.2))
         )
         .setAlwaysOnTop();
      if (this.lastSelectedShulker != null
         && !this.lastSelectedShulker.equals(this.hoveredShulker)
         && !this.selectKeyDown
         && PlayerInputUtil.isKeyDown(minecraft.options.keyShift)) {
         int var2 = !OrderMenuParser.expandBox(getClickDelay(this.lastSelectedShulker), getClickDelay(this.hoveredShulker)).isEmpty() ? 1 : 0;
         AABB var3 = new AABB(this.lastSelectedShulker).minmax(new AABB(this.hoveredShulker)).inflate(0.03);
         Gizmos.cuboid(var3, GizmoStyle.strokeAndFill(var2 != 0 ? -854792 : -1017711, 1.5F, ColorUtil.withAlpha(var2 != 0 ? -854792 : -1017711, 0.06)))
            .setAlwaysOnTop();
      }
   }

   public static OrderDropperModule getInstance() {
      return instance;
   }

   public void fillShulker(PoseStack var1, EffectBatch var2, Vec3 var3) {
      if (this.isEnabled()) {
         Minecraft var4 = getMc();
         if (var4 != null && var4.level != null && var4.level == this.selectionLevel && !this.selectedShulkers.isEmpty()) {
            long var5 = PerfProfiler.startTimer();

            try {
               this.sendMessage(this.isRuntimeActive());
               VertexConsumer var7 = ModelVisualRenderer.renderEntity(var2, this.refuseToStart());
               Pose var8 = var1.last();
               double var9 = TickTimer.getTime();
               double var11 = var9 * 0.08 % 1.0;
               double var13 = 0.5 + 0.5 * Math.sin(var9 * 6.0);
               double var15 = this.highlightOpacity.getValue();

               for (int var17 = 0; var17 < this.highlightFaces.size(); var17++) {
                  double[] var18 = this.highlightFaces.get(var17);
                  int var19 = this.highlightFaceColors.get(var17);
                  double var20 = var19 == -11702 ? var15 * (0.7 + 0.3 * var13) : var15;

                  for (int var22 = 0; var22 < 4; var22++) {
                     int var23 = var22 * 5;
                     ModelVisualRenderer.putVertex(
                        var7,
                        var8,
                        (float)(var18[var23] - var3.x),
                        (float)(var18[var23 + 1] - var3.y),
                        (float)(var18[var23 + 2] - var3.z),
                        (float)var18[var23 + 3],
                        (float)var18[var23 + 4],
                        (float)var18[20],
                        (float)var18[21],
                        (float)var18[22],
                        var19,
                        validateShulkers(var19),
                        var20,
                        0.6,
                        1.0,
                        var11
                     );
                  }
               }
            } finally {
               PerfProfiler.endTimer("Order Dropper selection", var5);
            }
         }
      }
   }

   private ModelVisualRenderer.Material refuseToStart() {
      return switch ((String)this.highlightStyle.getValue()) {
         case "Glow" -> ModelVisualRenderer.Material.CHAMS_STYLE_2;
         case "Metallic" -> ModelVisualRenderer.Material.CHAMS_STYLE_3;
         case "Marble" -> ModelVisualRenderer.Material.CHAMS_STYLE_5;
         case "Glitter" -> ModelVisualRenderer.Material.CHAMS_STYLE_6;
         case "Solid" -> ModelVisualRenderer.Material.CHAMS_STYLE_1;
         case "Ghost" -> ModelVisualRenderer.Material.CHAMS_STYLE_7;
         default -> ModelVisualRenderer.Material.CHAMS_STYLE_4;
      };
   }

   private void sendMessage(boolean var1) {
      long var2 = this.selectedShulkers.size();

      for (BlockPos var5 : this.selectedShulkers) {
         var2 = var2 * 31L + var5.asLong() * 17L + this.refuseToStart(var5, var1);
      }

      if (var2 != this.highlightHash) {
         this.highlightHash = var2;
         this.highlightFaces.clear();
         this.highlightFaceColors.clear();
         HashSet var36 = new HashSet<>(this.selectedShulkers);

         for (BlockPos var6 : this.selectedShulkers) {
            int var7 = this.refuseToStart(var6, var1);

            for (Direction var11 : Direction.values()) {
               if (!var36.contains(var6.relative(var11))) {
                  Direction[] var12 = fACE_AXES[var11.ordinal()];
                  Direction var13 = var12[0];
                  Direction var14 = var12[1];
                  double var15 = var36.contains(var6.relative(var13.getOpposite())) ? 0.0 : -0.012;
                  double var17 = var36.contains(var6.relative(var13)) ? 1.0 : 1.012;
                  double var19 = var36.contains(var6.relative(var14.getOpposite())) ? 0.0 : -0.012;
                  double var21 = var36.contains(var6.relative(var14)) ? 1.0 : 1.012;
                  double[] var23 = new double[23];
                  double[][] var24 = new double[][]{{var15, var19}, {var17, var19}, {var17, var21}, {var15, var21}};

                  for (int var25 = 0; var25 < 4; var25++) {
                     double var26 = var24[var25][0];
                     double var28 = var24[var25][1];
                     double var30 = fillShulker(var6.getX(), var11.getStepX(), var13.getStepX(), var14.getStepX(), var26, var28);
                     double var32 = fillShulker(var6.getY(), var11.getStepY(), var13.getStepY(), var14.getStepY(), var26, var28);
                     double var34 = fillShulker(var6.getZ(), var11.getStepZ(), var13.getStepZ(), var14.getStepZ(), var26, var28);
                     var23[var25 * 5] = var30;
                     var23[var25 * 5 + 1] = var32;
                     var23[var25 * 5 + 2] = var34;
                     var23[var25 * 5 + 3] = Math.floorMod(
                           var13.getAxis() == Axis.X ? var6.getX() : (var13.getAxis() == Axis.Y ? var6.getY() : var6.getZ()), 256
                        )
                        + var26;
                     var23[var25 * 5 + 4] = Math.floorMod(
                           var14.getAxis() == Axis.X ? var6.getX() : (var14.getAxis() == Axis.Y ? var6.getY() : var6.getZ()), 256
                        )
                        + var28;
                  }

                  var23[20] = var11.getStepX();
                  var23[21] = var11.getStepY();
                  var23[22] = var11.getStepZ();
                  this.highlightFaces.add(var23);
                  this.highlightFaceColors.add(var7);
               }
            }
         }
      }
   }

   private static double fillShulker(int var0, int var1, int var2, int var3, double var4, double var6) {
      double var8 = var1 > 0 ? var0 + 1 + 0.012 : (var1 < 0 ? var0 - 0.012 : var0);
      return var8 + (var2 != 0 ? var4 : 0.0) + (var3 != 0 ? var6 : 0.0);
   }

   private void setPhase(OrderDropperModule.Phase var1) {
      if (this.phase != var1) {
         LarpLog.debug("OrderDropper", "Phase {} -> {}", this.phase, var1);
      }

      this.phase = var1;
      this.phaseTicks = 0;
      if (var1 == OrderDropperModule.Phase.WALK_TO_SHULKER || var1 == OrderDropperModule.Phase.RETURN_TO_START) {
         this.bestWalkDistance = Double.MAX_VALUE;
         this.stuckTicks = 0;
      }
   }

   private int getClickDelay() {
      return this.clickDelay.getValue().intValue();
   }

   private float getAfkInput() {
      return minecraft.player.getHealth() + minecraft.player.getAbsorptionAmount();
   }

   private double getInteractionRange() {
      return Math.min(minecraft.player.blockInteractionRange(), 4.5);
   }

   private double onClientTick(Vec3 var1) {
      Vec3 var2 = minecraft.player.position();
      return Math.hypot(var1.x - var2.x, var1.z - var2.z);
   }

   private boolean isShulker(BlockPos var1) {
      return var1 != null && minecraft.level.hasChunkAt(var1) && minecraft.level.getBlockState(var1).getBlock() instanceof ShulkerBoxBlock;
   }

   private boolean isOrderItem(ItemStack var1) {
      if (!var1.isEmpty() && !this.orderItemId.isEmpty()) {
         if (!this.orderItemId.equals(this.cachedItemId)) {
            Identifier var2 = Identifier.tryParse(this.orderItemId);
            this.cachedItem = var2 == null ? null : (Item)BuiltInRegistries.ITEM.getOptional(var2).orElse(null);
            this.cachedItemId = this.orderItemId;
         }

         return var1.getItem() == this.cachedItem;
      } else {
         return false;
      }
   }

   private int clearShulkers() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (minecraft.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private int countCarried() {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.reservedSlots.contains(var2) && this.isOrderItem(minecraft.player.getInventory().getItem(var2))) {
            var1++;
         }
      }

      return var1;
   }

   private boolean fillShulker() {
      return this.countCarried() > 0;
   }

   private int onClientTick() {
      this.validateShulkers();
      int var1 = 0;

      for (BlockPos var3 : this.getSelectedShulkers()) {
         if (!this.fullShulkers.contains(var3) && !this.unavailableShulkers.contains(var3)) {
            var1 += this.freeSlots.getOrDefault(var3, 0);
         }
      }

      return var1;
   }

   private void recordShulkerScan(ShulkerBoxMenu var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 27; var3++) {
         if (var1.getSlot(var3).getItem().isEmpty()) {
            var2++;
         }
      }

      this.freeSlots.put(this.targetShulker, var2);
      this.scannedBlockEntities.put(this.targetShulker, minecraft.level.getBlockEntity(this.targetShulker));
      this.unavailableShulkers.remove(this.targetShulker);
      if (var2 == 0) {
         this.fullShulkers.add(this.targetShulker);
      } else {
         this.fullShulkers.remove(this.targetShulker);
      }

      LarpLog.info("OrderDropper", "Scanned shulker {}: free slots={}", this.targetShulker.toShortString(), var2);
   }

   private void validateShulkers() {
      for (BlockPos var2 : this.getSelectedShulkers()) {
         if (!this.isShulker(var2)) {
            if (this.unavailableShulkers.add(var2)) {
               LarpLog.warn("OrderDropper", "Selected shulker {} is missing or unloaded; capacity removed", var2.toShortString());
            }

            this.freeSlots.remove(var2);
            this.fullShulkers.remove(var2);
            this.scannedBlockEntities.remove(var2);
         } else if (this.scannedBlockEntities.containsKey(var2) && this.scannedBlockEntities.get(var2) != minecraft.level.getBlockEntity(var2)) {
            this.freeSlots.remove(var2);
            this.fullShulkers.remove(var2);
            this.scannedBlockEntities.remove(var2);
         }
      }
   }

   private static AABB fillShulker(BlockPos var0, Direction var1) {
      return new AABB(var0).expandTowards(var1.getStepX() * 0.5, var1.getStepY() * 0.5, var1.getStepZ() * 0.5).inflate(0.1);
   }

   private AABB finishSelection() {
      return fillShulker(this.targetShulker, (Direction)minecraft.level.getBlockState(this.targetShulker).getValue(ShulkerBoxBlock.FACING));
   }

   private boolean reloadOrders() {
      return !this.finishSelection().intersects(minecraft.player.getBoundingBox());
   }

   private Vec3 setPhase() {
      return rescan(
         this.targetShulker,
         minecraft.player.position(),
         minecraft.player.getBoundingBox(),
         this.finishSelection(),
         var0 -> minecraft.level.noCollision(minecraft.player, var0),
         var1 -> !this.triedSpots.contains(var1) && this.fillShulker(var1.add(0.0, minecraft.player.getEyeHeight(), 0.0)) != null
      );
   }

   private BlockHitResult fillShulker(Vec3 var1) {
      double var2 = this.getInteractionRange();
      if (clickSlot(this.targetShulker, var1) > var2 * var2) {
         return null;
      }

      List var4 = minecraft.level
         .getEntities(
            minecraft.player, new AABB(this.targetShulker).minmax(new AABB(var1, var1)).inflate(1.0), var0 -> var0.isPickable() && !var0.isSpectator()
         );
      return finishSelection(this.targetShulker, var1, var2, (var1x, var2x) -> {
         BlockHitResult var3 = minecraft.level.clip(new ClipContext(var1x, var2x, Block.OUTLINE, Fluid.NONE, minecraft.player));
         if (var3.getType() != Type.BLOCK) {
            return null;
         }

         for (Entity var5 : var4) {
            if (BoxRaycastUtil.isBoxBlocking(var1x, var3.getLocation(), var5.getBoundingBox())) {
               return null;
            }
         }

         return var3;
      });
   }

   private static double clickSlot(BlockPos var0, Vec3 var1) {
      double var2 = Math.clamp(var1.x, var0.getX(), var0.getX() + 1.0);
      double var4 = Math.clamp(var1.y, var0.getY(), var0.getY() + 1.0);
      double var6 = Math.clamp(var1.z, var0.getZ(), var0.getZ() + 1.0);
      return var1.distanceToSqr(var2, var4, var6);
   }

   private static BlockHitResult finishSelection(BlockPos var0, Vec3 var1, double var2, BiFunction<Vec3, Vec3, BlockHitResult> var4) {
      if (Double.isFinite(var2) && !(var2 <= 0.0) && !(clickSlot(var0, var1) > var2 * var2 + 1.0E-7)) {
         ArrayList var5 = new ArrayList(28);
         var5.add(Vec3.atCenterOf(var0));
         Vec3 var6 = Vec3.atCenterOf(var0);

         for (Direction var10 : Direction.values()) {
            double var11 = (var1.x - var6.x) * var10.getStepX() + (var1.y - var6.y) * var10.getStepY() + (var1.z - var6.z) * var10.getStepZ();
            if (!(var11 <= 0.5)) {
               for (Vec3 var14 : NativePlacement.facePoints(var0, var10)) {
                  if (var1.distanceToSqr(var14) <= var2 * var2 + 1.0E-7) {
                     var5.add(var14);
                  }
               }
            }
         }

         for (Vec3 var16 : var5) {
            Vec3 var17 = var16.subtract(var1);
            if (!(var17.lengthSqr() < 1.0E-8)) {
               BlockHitResult var18 = (BlockHitResult)var4.apply(var1, var1.add(var17.normalize().scale(var2)));
               if (var18 != null
                  && var18.getType() == Type.BLOCK
                  && var18.getBlockPos().equals(var0)
                  && !(var1.distanceToSqr(var18.getLocation()) > var2 * var2 + 1.0E-7)) {
                  float[] var19 = RotationManager.calculateAngles(var1, refuseToStart(var0, var18));
                  BlockHitResult var12 = (BlockHitResult)var4.apply(var1, var1.add(Vec3.directionFromRotation(var19[1], var19[0]).scale(var2)));
                  if (var12 != null
                     && var12.getType() == Type.BLOCK
                     && var12.getBlockPos().equals(var0)
                     && var1.distanceToSqr(var12.getLocation()) <= var2 * var2 + 1.0E-7) {
                     return var18;
                  }
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private static Vec3 refuseToStart(BlockPos var0, BlockHitResult var1) {
      Vec3 var2 = var1.getLocation();
      return new Vec3(
         Math.clamp(var2.x, var0.getX() + 0.02, var0.getX() + 0.98),
         Math.clamp(var2.y, var0.getY() + 0.02, var0.getY() + 0.98),
         Math.clamp(var2.z, var0.getZ() + 0.02, var0.getZ() + 0.98)
      );
   }

   private static Vec3 rescan(BlockPos var0, Vec3 var1, AABB var2, AABB var3, Predicate<AABB> var4, Predicate<Vec3> var5) {
      Vec3 var6 = Vec3.atBottomCenterOf(var0);
      ArrayList var7 = new ArrayList(72);

      for (double var11 : new double[]{1.25, 1.8, 2.5}) {
         for (int var16 : new int[]{0, -1, 1}) {
            for (int var17 = -1; var17 <= 1; var17++) {
               for (int var18 = -1; var18 <= 1; var18++) {
                  if (var17 != 0 || var18 != 0) {
                     Vec3 var19 = new Vec3(var6.x + var17 * var11, var1.y + var16, var6.z + var18 * var11);
                     AABB var20 = var2.move(var19.subtract(var1));
                     if (!var3.intersects(var20) && var4.test(var20) && !var4.test(var20.move(0.0, -0.15, 0.0))) {
                        var7.add(var19);
                     }
                  }
               }
            }
         }
      }

      var7.sort(Comparator.comparingDouble(var1x -> var1x.distanceToSqr(var1)));

      for (Vec3 var22 : var7) {
         if (var5.test(var22)) {
            return var22;
         }
      }

      return null;
   }

   private boolean isWaitingForControl() {
      if (!InteractionArbiter.canInteract() || RotationManager.hasRequest() && !RotationManager.isActive(RotationManager.Priority.ORDER_DROP)) {
         this.status = "Waiting for camera or rotation control";
         this.phaseTicks = Math.max(0, this.phaseTicks - 1);
         return true;
      } else {
         return false;
      }
   }

   public static boolean isAfkActive() {
      if (instance != null && instance.isEnabled()) {
         OrderDropperModule var0 = instance;
         return var0 != null && var0.isRuntimeActive() && var0.phase != null && var0.afkMode.getValue();
      } else {
         return false;
      }
   }

   private void getInstance() {
      if (!this.afkMode.getValue()) {
         this.restorePauseOnLostFocus();
      } else {
         if (!this.afkActive) {
            this.savedPauseOnLostFocus = minecraft.options.pauseOnLostFocus;
            this.afkActive = (boolean)1;
            LarpLog.info("OrderDropper", "AFK mouse released; pause on focus loss temporarily disabled");
         }

         minecraft.options.pauseOnLostFocus = (boolean)0;
         if (minecraft.mouseHandler.isMouseGrabbed()) {
            minecraft.mouseHandler.releaseMouse();
         }
      }
   }

   private void restorePauseOnLostFocus() {
      if (this.afkActive) {
         minecraft.options.pauseOnLostFocus = this.savedPauseOnLostFocus;
         this.afkActive = (boolean)0;
         LarpLog.info("OrderDropper", "AFK ended; pause on focus loss restored");
      }
   }

   private int countDroppedItems() {
      Vec3 var1 = this.startPos == null ? minecraft.player.position() : this.startPos;
      AABB var2 = new AABB(var1, var1).inflate(2.5, 1.5, 2.5);
      return minecraft.level.getEntitiesOfClass(ItemEntity.class, var2, var1x -> var1x.isAlive() && this.isOrderItem(var1x.getItem())).size();
   }

   private static int restorePauseOnLostFocus(AbstractContainerMenu var0) {
      int var1 = 31 * var0.containerId + var0.getStateId();

      for (Slot var3 : var0.slots) {
         var1 = 31 * var1 + ItemStack.hashItemAndComponents(var3.getItem()) * 7 + var3.getItem().getCount();
      }

      ItemStack var4 = var0.getCarried();
      return 31 * var1 + ItemStack.hashItemAndComponents(var4) + var4.getCount();
   }

   private static List<String> getSelectedShulkers(ItemStack var0) {
      ItemLore var1 = (ItemLore)var0.get(DataComponents.LORE);
      if (var1 == null) {
         return List.of();
      }

      ArrayList var2 = new ArrayList();

      for (Component var4 : var1.lines()) {
         var2.add(var4.getString());
      }

      return var2;
   }

   private static String startSelection(ItemStack var0) {
      StringBuilder var1 = new StringBuilder(updateSelectKey(var0));

      for (String var3 : getSelectedShulkers(var0)) {
         if (!OrderMenuParser.stripFormatting(var3).contains("until")) {
            var1.append('|').append(var3);
         }
      }

      return var1.toString();
   }

   private static String updateSelectKey(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private void refuseToStart(String var1) {
      this.sendChatMessage(var1);
      LarpLog.info("OrderDropper", "Not started: {}", var1);
      this.disable();
   }

   private void stopWithReason(String var1) {
      LarpLog.warn("OrderDropper", "Stopped in phase {}: {}", this.phase, var1);
      this.sendChatMessage("Stopped: " + var1);
      this.disable();
   }

   private void onClientTick(String var1) {
      this.lastHintNanos = System.nanoTime();
      if (minecraft.player != null) {
         minecraft.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private void sendMessage(String var1) {
      if (this.showMessages.getValue()) {
         this.sendChatMessage(var1);
      }
   }

   private void sendChatMessage(String var1) {
      if (this.isInGame()) {
         minecraft.gui.chatListener().handleSystemMessage(Component.literal("[Order Dropper] " + var1), false);
      }
   }

   static {
      fACE_AXES[Direction.DOWN.ordinal()] = new Direction[]{Direction.EAST, Direction.SOUTH};
      fACE_AXES[Direction.UP.ordinal()] = new Direction[]{Direction.SOUTH, Direction.EAST};
      fACE_AXES[Direction.NORTH.ordinal()] = new Direction[]{Direction.UP, Direction.EAST};
      fACE_AXES[Direction.SOUTH.ordinal()] = new Direction[]{Direction.EAST, Direction.UP};
      fACE_AXES[Direction.WEST.ordinal()] = new Direction[]{Direction.SOUTH, Direction.UP};
      fACE_AXES[Direction.EAST.ordinal()] = new Direction[]{Direction.UP, Direction.SOUTH};
   }

   private enum ClickKind {
      NAVIGATION,
      TRANSFER;
   }

   private enum Phase {
      ORDERS_MENU,
      PICKUP_DROPS,
      WALK_TO_SHULKER,
      OPEN_SHULKER,
      FILL_SHULKER,
      RETURN_TO_START,
      FINISH;
   }

   private record SelectionMark(long at, boolean added) {
      private final long nanoTime;

      private SelectionMark(long at, boolean added) {
         this.nanoTime = at;
         this.added = added;
      }
   }
}
