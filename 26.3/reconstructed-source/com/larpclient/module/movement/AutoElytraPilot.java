package com.larpclient.module.movement;

import com.larpclient.LarpClient;
import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.gui.clickgui.ModulePanel;
import com.larpclient.gui.theme.Theme;
import com.larpclient.hud.NotificationManager;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.TextSetting;
import com.larpclient.module.player.AuctionBuyer;
import com.larpclient.module.player.AutoSustain;
import com.larpclient.module.world.WorldAutomationModule;
import com.larpclient.render.world.WorldPathView;
import com.larpclient.util.entity.ElytraFlightPlanner;
import com.larpclient.util.entity.ElytraPhysics;
import com.larpclient.util.entity.MovementCorrection;
import com.larpclient.util.entity.PlayerInputUtil;
import com.larpclient.util.entity.SwingHelper;
import com.larpclient.util.math.RotationSmoother2;
import com.larpclient.util.misc.AuctionParser;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.world.SafeSpotFinder;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.VoxelShape;

public final class AutoElytraPilot extends WorldAutomationModule implements ModulePanel {
   private static AutoElytraPilot instance;
   private static boolean hooksRegistered;
   static final int mIN_ROCKET_BOOSTS = 2;
   static final int mAX_NOTICE_SECONDS = 10;
   static final double pITCH_TOLERANCE = 10.0;
   static final double lANDING_LOOKAHEAD = 110.0;
   static final double aRRIVAL_RADIUS = 72.0;
   static final double nEAR_DESTINATION = 30.0;
   static final int mAX_TAKEOFF_ATTEMPTS = 6;
   static final int mIN_ROCKETS_TO_TAKE_OFF = 4;
   static final int sTOP_TASK_TIMEOUT_TICKS = 400;
   static final int bUY_TIMEOUT_TICKS = 3600;
   static final double sKIP_RESTOCK_DISTANCE = 1500.0;
   static final int nEAR_GROUND_TICKS = 200;
   private final TextSetting destinationSetting = this.addSetting(
      new TextSetting("Destination", "Where to fly, as X Z (for example 1200 -3400). X Y Z works too; the height is not used", "").setTitle("X Z")
   );
   private final NumberSetting flightHeightSetting = this.addSliderSetting(
      "Flight Height",
      "Height to cruise at. Lower on long trips over flat land saves rockets; higher clears mountains and loads fewer chunks",
      290.0,
      180.0,
      320.0,
      10.0
   );
   private final BooleanSetting autoMendingSetting = this.addToggleSetting(
      "Auto Mending", "Land on the way and throw XP bottles from your hotbar to repair a Mending elytra long before it can break", true
   );
   private final BooleanSetting logOffOnArrivalSetting = this.addToggleSetting(
      "Log Off On Arrival", "Leave the server after landing, with a trip summary. Also leaves if the trip has to stop. Never in singleplayer", true
   );
   private final BooleanSetting waypointLineSetting = this.addToggleSetting(
      "Waypoint Line", "Draw the planned flight, a beacon at the destination and the landing spot", true
   );
   private final BooleanSetting statusCardSetting = this.addToggleSetting(
      "Status Card", "Show a card at the top of the screen with distance, time left, rockets and elytra health", true
   );
   private final ModeSetting notificationsSetting = this.addSetting(
      new ModeSetting("Notifications", "How the pilot tells you what it is doing", "Toast", "Toast", "Chat", "Off")
   );
   private final BooleanSetting autoBuyRocketsSetting = this.addToggleSetting(
      "Auto Buy Rockets", "On DonutSMP, buy Flight Duration 3 rockets from /ah on the ground when you run low, within the price limit and budget", false
   );
   private final NumberSetting rocketStacksSetting = this.addSliderSetting(
      "Rocket Stacks", "How many stacks of rockets to keep; it buys up to this many", 3.0, 1.0, 3.0, 1.0
   );
   private final BooleanSetting autoBuyXpBottlesSetting = this.addToggleSetting(
      "Auto Buy XP Bottles", "On DonutSMP, buy XP bottles from /ah on the ground to mend a Mending elytra, within the price limit and budget", false
   );
   private final NumberSetting xpBottleStacksSetting = this.addSliderSetting(
      "XP Bottle Stacks", "How many stacks of XP bottles to keep; it buys up to this many", 2.0, 1.0, 3.0, 1.0
   );
   private final ModeSetting priceLimitSetting = this.addSetting(
      new ModeSetting(
         "Price Limit",
         "Most it pays per stack of 64. Cheap: rockets $35K, XP $320K. Normal: rockets $60K, XP $450K. High: rockets $150K, XP $700K",
         "Normal",
         AuctionParser.speedTiers.toArray(String[]::new)
      )
   );
   private final ModeSetting tripBudgetSetting = this.addSetting(
      new ModeSetting("Trip Budget", "Most money it spends on one trip", "$2M", AuctionParser.priceLimitOptions.toArray(String[]::new))
   );
   private final AuctionBuyer shopBuyer = new AuctionBuyer(this.getName());
   private final RocketBooster rocketController = new RocketBooster(this).boost(true);
   private final ElytraFlightPlanner flightPlanner = new ElytraFlightPlanner();
   private final ElytraFlightPlanner.Goal planParams = new ElytraFlightPlanner.Goal();
   private final ElytraPhysics.FlightState flightState = new ElytraPhysics.FlightState();
   private final RotationSmoother2 rotator = new RotationSmoother2();
   private final AutoSustain consumables = new AutoSustain(this);
   private final Map<Long, Integer> surfaceHeightCache = new HashMap<>();
   private final List<Vec3> rejectedLandingSpots = new ArrayList<>();
   private final List<Vec3> predictedPath = new ArrayList<>();
   private AutoElytraPilot.Phase phase = AutoElytraPilot.Phase.PREPARE;
   private AutoElytraPilot.StopMode stopMode = AutoElytraPilot.StopMode.NONE;
   private AutoElytraPilot.StopTask stopTask = AutoElytraPilot.StopTask.NONE;
   private double destinationX;
   private double destinationZ;
   private boolean tripActive;
   private Object level;
   private Object player;
   private Object connection;
   private ResourceKey<Level> dimension;
   private int tickCounter;
   private int phaseStartTick;
   private int stopTaskStartTick;
   private int lastPlanTick = -1000;
   private int lastLandingSearchTick = -1000;
   private int landingSearchCount;
   private ElytraFlightPlanner.Trajectory flightPlan;
   private ElytraFlightPlanner.Aim landingPlan;
   private int lastLandingPlanTick = -1000;
   private int badLandingPlanCount;
   private int goAroundCount;
   private Vec3 landingSpot;
   private boolean landingInWater;
   private boolean circling;
   private double bestHeightAboveSpot = Double.MAX_VALUE;
   private int lastLandingProgressTick;
   private int lastRocketTick = -1000;
   private int rocketBoostEndTick = -1000;
   private int lastRocketDuration = 3;
   private int rocketHoldUntilTick = -1000;
   private int lastJumpTick = -1000;
   private int takeoffJumpCount;
   private int takeoffAttempts;
   private Float takeoffYaw;
   private boolean takeoffStraightUp;
   private float targetYaw;
   private float targetPitch;
   private boolean hasLookTarget;
   private boolean pressForward;
   private boolean pressJump;
   private int lastInputTick = Integer.MIN_VALUE;
   private int swimStartTick = -1;
   private Vec3 shoreTarget;
   private int bestMendDurability;
   private int lastMendProgressTick;
   private int mendFailureCount;
   private int xpBottlesUsed;
   private int lastSwapTick = -1000;
   private float lastHealth = 20.0F;
   private int lastDamageTick = -1000;
   private Vec3 lastPosition;
   private Vec3 lastVelocity;
   private int rubberbandCount;
   private int rubberbandWindowStart;
   private double distanceFlown;
   private double startDistance;
   private double speedEstimate;
   private long startMillis;
   private String stopReason = "";
   private String pendingStopReason;
   private String lastStatus;
   private long moneySpent;
   private int rocketsBought;
   private int xpBottlesBought;
   private final EnumSet<AuctionParser.AuctionItem> failedPurchases = EnumSet.noneOf(AuctionParser.AuctionItem.class);
   private String lastNoticeText = "";
   private int lastNoticeTick = -10000;
   private int startingRockets;
   private int rocketsUsed;
   private final List<AuctionParser.AuctionItem> restockQueue = new ArrayList<>();
   private long restockSpent;
   private int restockBought;
   private boolean budgetExhausted;
   static final double lANDING_MARGIN = 48.0;
   private int cachedMaxHeight = Integer.MIN_VALUE;
   private int lastHeightScanTick = -1000;

   public AutoElytraPilot() {
      super("Auto Elytra Pilot", "Fly to an X Z on an elytra: takeoff, cruise, landing, rockets and mending stops");
      instance = this;
      this.destinationSetting.setDisplayName("Destination (X Z)");
      this.rocketStacksSetting.visibleWhen(this.autoBuyRocketsSetting::getValue);
      this.xpBottleStacksSetting.visibleWhen(this.autoBuyXpBottlesSetting::getValue);
      this.priceLimitSetting.visibleWhen(() -> this.autoBuyRocketsSetting.getValue() || this.autoBuyXpBottlesSetting.getValue());
      this.tripBudgetSetting.visibleWhen(() -> this.autoBuyRocketsSetting.getValue() || this.autoBuyXpBottlesSetting.getValue());
      if (!hooksRegistered) {
         hooksRegistered = (boolean)1;
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> {
            AutoElytraPilot var2 = instance;
            if (!var1 && var2 != null && var2.shopBuyer.isRunning()) {
               AuctionBuyer.onChatMessage(var0);
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            AutoElytraPilot var1 = instance;
            if (var1 != null) {
               var1.setPhase();
            }
         });
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> {
            AutoElytraPilot var1 = instance;
            if (var1 != null) {
               var1.steerToward();
            }
         });
      }
   }

   public static AutoElytraPilot getInstance() {
      return instance;
   }

   @Override
   public boolean allowsScreenInteractions() {
      return minecraft == null || !(WorldPathView.currentScreen() instanceof AbstractContainerScreen);
   }

   @Override
   public void disable() {
      this.pendingStopReason = null;
      super.disable();
   }

   public static double[] parseDestination(String var0) {
      if (var0 == null) {
         return null;
      }

      String[] var1 = var0.trim().replace(',', ' ').split("\\s+");
      if (var1.length != 2 && var1.length != 3) {
         return null;
      }

      double[] var2 = new double[var1.length];

      try {
         for (int var3 = 0; var3 < var1.length; var3++) {
            var2[var3] = Double.parseDouble(var1[var3]);
         }
      } catch (NumberFormatException var7) {
         return null;
      }

      double var8 = var2[0];
      double var5 = var2[var1.length - 1];
      return Double.isFinite(var8) && Double.isFinite(var5) && !(Math.abs(var8) > 2.9999984E7) && !(Math.abs(var5) > 2.9999984E7)
         ? new double[]{var8, var5}
         : null;
   }

   static String formatCoords(double var0, double var2) {
      return (long)Math.floor(var0) + " " + (long)Math.floor(var2);
   }

   static String formatCount(double var0) {
      return String.format(Locale.ROOT, "%,d", Math.round(var0));
   }

   @Override
   public List<ModulePanel.Command> commands() {
      return List.of(
         new ModulePanel.Command("Set the destination", "Fly to where you look", this::setDestinationFromView),
         new ModulePanel.Command("Set the destination", "Remember where you stand", this::parseDestination),
         new ModulePanel.Command("Set the destination", "Clear destination", this::clearDestination),
         new ModulePanel.Command("Buying on DonutSMP", "Buy rockets and XP now", this::restockNow)
      );
   }

   private String setDestinationFromView() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the pilot off before changing the destination.";
      }

      Vec3 var1 = minecraft.player.getEyePosition();
      Vec3 var2 = minecraft.player.getLookAngle();
      double var3 = Math.max(64.0, minecraft.options.getEffectiveRenderDistance() * 16.0);
      BlockHitResult var5 = minecraft.level.clip(new ClipContext(var1, var1.add(var2.scale(var3)), Block.OUTLINE, Fluid.ANY, minecraft.player));
      Vec3 var6;
      String var7;
      if (var5.getType() == Type.BLOCK) {
         var6 = var5.getLocation();
         var7 = "the block you look at";
      } else {
         Vec3 var8 = new Vec3(var2.x, 0.0, var2.z);
         if (var8.lengthSqr() < 1.0E-6) {
            return "Look toward the horizon, not straight up or down.";
         }

         var6 = minecraft.player.position().add(var8.normalize().scale(1000.0));
         var7 = "1,000 blocks along your view (nothing loaded is in sight there)";
      }

      this.destinationSetting.setText(formatCoords(var6.x, var6.z));
      this.refreshUi();
      double var10 = Math.hypot(var6.x - minecraft.player.getX(), var6.z - minecraft.player.getZ());
      LarpLog.info(this.getName(), "Destination set from the view to {} ({} blocks)", this.destinationSetting.getValue(), formatCount(var10));
      return "Destination "
         + this.destinationSetting.getValue().replace(' ', ',').replace(",", ", ")
         + ", "
         + var7
         + ", "
         + formatCount(var10)
         + " blocks away.";
   }

   private String parseDestination() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the pilot off before changing the destination.";
      }

      this.destinationSetting.setText(formatCoords(minecraft.player.getX(), minecraft.player.getZ()));
      this.refreshUi();
      return "Destination set to where you stand: " + this.destinationSetting.getValue().replace(" ", ", ") + ". Fly away and turn the pilot on to come back.";
   }

   private String restockNow() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "The pilot buys on its own during a trip.";
      }

      if (!this.restockQueue.isEmpty()) {
         return "Already buying; move to stop it.";
      }

      if (!this.autoBuyRocketsSetting.getValue() && !this.autoBuyXpBottlesSetting.getValue()) {
         return "Turn on Auto Buy Rockets or Auto Buy XP Bottles first.";
      }

      if (minecraft.player.onGround() && !(minecraft.player.getDeltaMovement().horizontalDistance() > 0.01)) {
         if (this.autoBuyRocketsSetting.getValue()) {
            this.restockQueue.add(AuctionParser.AuctionItem.FIREWORK);
         }

         if (this.autoBuyXpBottlesSetting.getValue()) {
            this.restockQueue.add(AuctionParser.AuctionItem.EXP_BOTTLE);
         }

         this.restockSpent = 0L;
         this.restockBought = 0;
         LarpLog.info(this.getName(), "Buying now: {}", this.restockQueue);
         return "Buying "
            + (this.autoBuyRocketsSetting.getValue() ? "rockets" : "")
            + (this.restockQueue.size() == 2 ? " and " : "")
            + (this.autoBuyXpBottlesSetting.getValue() ? "XP bottles" : "")
            + " on /ah, at most "
            + this.priceLimitSetting.getValue().toLowerCase(Locale.ROOT)
            + " prices and "
            + AuctionParser.formatPrice(AuctionParser.parsePriceLimit(this.tripBudgetSetting.getValue()))
            + ". Moving stops it.";
      } else {
         return "Stand still on the ground first.";
      }
   }

   private void setPhase() {
      if (!this.restockQueue.isEmpty()) {
         if (!this.isInGame() || this.isRuntimeActive()) {
            this.shopBuyer.abort("the restock was interrupted");
            this.restockQueue.clear();
         } else if (this.isPlayerSteering()) {
            this.shopBuyer.abort("you moved");
            this.finishManualRestock("Stopped buying: you moved.");
         } else {
            AuctionParser.AuctionItem var1 = this.restockQueue.getFirst();
            int var2 = var1 == AuctionParser.AuctionItem.FIREWORK ? 1 : 0;
            if (!this.shopBuyer.isRunning()) {
               int var9 = var2 != 0 ? RocketBooster.countRockets(minecraft.player) : this.clearDestination();
               int var4 = (var2 != 0 ? this.finishTrip() : this.getXpBottleTarget()) - var9;
               long var5 = AuctionParser.parsePriceLimit(this.tripBudgetSetting.getValue());
               long var7 = var5 < 0L ? -1L : Math.max(0L, var5 - this.restockSpent);
               if (var4 > 0 && var7 != 0L) {
                  this.shopBuyer.startKind(var1, var4, AuctionParser.stackPriceThreshold(this.priceLimitSetting.getValue(), var1), var7);
               } else {
                  this.restockQueue.removeFirst();
                  if (this.restockQueue.isEmpty()) {
                     this.finishManualRestock(null);
                  }
               }
            } else if (this.shopBuyer.tick() != AuctionBuyer.State.RUNNING) {
               this.restockSpent = this.restockSpent + this.shopBuyer.getSpent();
               this.restockBought = this.restockBought + this.shopBuyer.getBoughtCount();
               String var3 = var2 != 0 ? "rockets" : "XP bottles";
               if (this.shopBuyer.getBoughtCount() > 0) {
                  this.notify(
                     NotificationManager.Type.SUCCESS,
                     "Bought " + this.shopBuyer.getBoughtCount() + " " + var3 + " for " + AuctionParser.formatPrice(this.shopBuyer.getSpent())
                  );
               }

               if (this.shopBuyer.getState() == AuctionBuyer.State.FAILED) {
                  this.notify(NotificationManager.Type.WARNING, "Could not buy " + var3 + ": " + this.shopBuyer.onChatMessage());
               }

               this.restockQueue.removeFirst();
               if (this.restockQueue.isEmpty() || this.shopBuyer.isOutOfMoney()) {
                  this.finishManualRestock(null);
               }
            }
         }
      }
   }

   private void finishManualRestock(String var1) {
      this.restockQueue.clear();
      String var2 = var1 != null
         ? var1
         : (
            this.restockBought == 0
               ? "Bought nothing."
               : "Done buying: " + this.restockBought + " items for " + AuctionParser.formatPrice(this.restockSpent) + "."
         );
      LarpLog.info(this.getName(), "Buying now ended: {}", var2);
      this.sendMessage(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
   }

   private String clearDestination() {
      if (this.isRuntimeActive()) {
         return "Turn the pilot off before clearing the destination.";
      }

      this.destinationSetting.setText("");
      this.refreshUi();
      return "Destination cleared.";
   }

   private void refreshUi() {
      LarpClient var1 = LarpClient.getInstance();
      if (var1 != null && var1.getConfigManager() != null) {
         var1.getConfigManager().saveActiveProfile();
      }
   }

   @Override
   public String summary() {
      double[] var1 = parseDestination(this.destinationSetting.getValue());
      if (var1 == null) {
         return this.destinationSetting.getValue().isBlank()
            ? "Type a destination as X Z, or press Fly to where you look."
            : "\"" + this.destinationSetting.getValue() + "\" is not a destination; type it as X Z, for example 1200 -3400.";
      }

      String var2 = "Destination: " + formatCoords(var1[0], var1[1]).replace(" ", ", ");
      if (!this.isInGame()) {
         return var2 + ".";
      }

      double var3 = Math.hypot(var1[0] - minecraft.player.getX(), var1[1] - minecraft.player.getZ());
      return this.isRuntimeActive() && this.tripActive
         ? var2 + ". " + this.isFlyingReady() + "."
         : var2 + ", " + formatCount(var3) + " blocks away. Turn the pilot on to fly there.";
   }

   @Override
   public String detail() {
      if (!this.isInGame()) {
         return "Needs an elytra and firework rockets in your offhand or hotbar.";
      }

      ItemStack var1 = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
      String var2 = var1.is(Items.ELYTRA)
         ? "elytra " + AutoSustain.getRemainingDurability(var1) + " of " + var1.getMaxDamage() + " uses" + (AutoSustain.hasMending(var1) ? " (Mending)" : "")
         : (this.sendMessage() >= 0 ? "elytra in your hotbar, not worn yet" : "no elytra worn");
      return "Rockets " + RocketBooster.countRockets(minecraft.player) + ", XP bottles " + this.clearDestination() + ", " + var2 + ".";
   }

   @Override
   public List<ModulePanel.SettingGroup> settingGroups() {
      return List.of(
         new ModulePanel.SettingGroup("Destination", List.of(this.destinationSetting)),
         new ModulePanel.SettingGroup("While flying", List.of(this.autoMendingSetting, this.logOffOnArrivalSetting)),
         new ModulePanel.SettingGroup(
            "Buying on DonutSMP",
            List.of(
               this.autoBuyRocketsSetting,
               this.rocketStacksSetting,
               this.autoBuyXpBottlesSetting,
               this.xpBottleStacksSetting,
               this.priceLimitSetting,
               this.tripBudgetSetting
            )
         ),
         new ModulePanel.SettingGroup("Display", List.of(this.waypointLineSetting, this.statusCardSetting, this.notificationsSetting)),
         ModulePanel.SettingGroup.advanced("Advanced", List.of(this.flightHeightSetting))
      );
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.resetState();
      if (this.pendingStopReason != null) {
         String var11 = this.pendingStopReason;
         this.pendingStopReason = null;
         this.abortStart("it stopped earlier because " + var11 + ". Turn it on again to fly from here");
      } else {
         double[] var1 = parseDestination(this.destinationSetting.getValue());
         if (var1 == null) {
            this.abortStart(
               this.destinationSetting.getValue().isBlank() ? "type a destination as X Z first" : "type the destination as X Z, for example 1200 -3400"
            );
         } else {
            String var2 = ModulePanel.findConflictingModule(this);
            if (var2 != null) {
               this.abortStart("turn off " + var2 + " first; one bot runs at a time");
            } else if (minecraft.level.dimension() == Level.NETHER) {
               this.abortStart("it flies in the Overworld and the End, not the Nether");
            } else if (!this.hasUsableElytra() && this.sendMessage() < 0) {
               this.abortStart("wear an elytra, or put one in your hotbar");
            } else {
               int var3 = RocketBooster.countRockets(minecraft.player);
               if (var3 == 0 && !this.autoBuyRocketsSetting.getValue()) {
                  RocketBooster.RocketSearch var12 = RocketBooster.findRocket(minecraft.player, 0);
                  this.abortStart(
                     var12.starsOnly()
                        ? "your rockets have firework stars, which hurt when boosting; use plain rockets"
                        : (
                           this.getElytraDurability()
                              ? "put firework rockets in your offhand or hotbar; the pilot never clicks inventory slots"
                              : "you need firework rockets in your offhand or hotbar"
                        )
                  );
               } else {
                  this.destinationX = Math.floor(var1[0]) + 0.5;
                  this.destinationZ = Math.floor(var1[1]) + 0.5;
                  double var4 = this.getSurfaceHeight();
                  if (var4 <= 30.0) {
                     this.abortStart("you are already at the destination");
                  } else {
                     this.tripActive = (boolean)1;
                     RocketBooster.claimHotbar(this);
                     ArrayList var6 = new ArrayList();

                     for (String var8 : List.of("ElytraFly", "ElytraHelper", "ElytraMotion")) {
                        Module var9 = LarpClient.getInstance().getModuleManager().getByName(var8);
                        if (var9 != null && var9.isEnabled()) {
                           var6.add(var8);
                        }
                     }

                     if (!var6.isEmpty()) {
                        LarpLog.info(this.getName(), "{} paused while the pilot flies", String.join(", ", var6));
                     }

                     this.level = minecraft.level;
                     this.player = minecraft.player;
                     this.connection = minecraft.getConnection() == null ? null : minecraft.getConnection().getConnection();
                     this.dimension = minecraft.level.dimension();
                     this.startMillis = System.currentTimeMillis();
                     this.startDistance = var4;
                     this.startingRockets = var3;
                     this.lastHealth = minecraft.player.getHealth();
                     this.lastPosition = minecraft.player.position();
                     this.consumables.start();
                     this.setPhase(AutoElytraPilot.Phase.PREPARE);
                     this.notify(
                        NotificationManager.Type.INFO,
                        "Flying to " + formatCoords(this.destinationX, this.destinationZ).replace(" ", ", ") + " (" + formatCount(var4) + " blocks)"
                     );
                     this.sendMessage(
                        "flying to "
                           + formatCoords(this.destinationX, this.destinationZ).replace(" ", ", ")
                           + ", "
                           + formatCount(var4)
                           + " blocks, at height "
                           + this.flightHeightSetting.getValue().intValue()
                           + " with "
                           + var3
                           + " rockets."
                     );
                     ArrayList var13 = new ArrayList();
                     double var14 = var4 / 1000.0 * 2.6 + 5.0;
                     if (var3 < var14 && !this.autoBuyRocketsSetting.getValue()) {
                        var13.add("about " + (int)Math.ceil(var14) + " rockets are needed and you have " + var3 + "; it lands safely when they run out");
                     }

                     if (this.getElytraDurability()) {
                        var13.add("rockets in your main inventory are not used, only the offhand and hotbar");
                     }

                     if (!var6.isEmpty()) {
                        var13.add(String.join(" and ", var6) + (var6.size() == 1 ? " pauses" : " pause") + " while the pilot flies and resumes after it lands");
                     }

                     ItemStack var10 = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
                     if (this.autoMendingSetting.getValue()
                        && var10.is(Items.ELYTRA)
                        && AutoSustain.hasMending(var10)
                        && this.clearDestination() == 0
                        && !this.autoBuyXpBottlesSetting.getValue()
                        && AutoSustain.getRemainingDurability(var10) < getInstance(var4) / (1 + this.tryDeployOnFall()) + this.tickLand()) {
                        var13.add("no XP bottles in the hotbar to mend the elytra with on the way");
                     }

                     if (var10.is(Items.ELYTRA)
                        && !AutoSustain.hasMending(var10)
                        && this.sendMessage() < 0
                        && AutoSustain.getRemainingDurability(var10) < getInstance(var4)) {
                        var13.add("the elytra has " + AutoSustain.getRemainingDurability(var10) + " uses left, no Mending and no spare, so it may land early");
                     }

                     if (AutoSustain.findHotbarSlot(var0 -> var0.get(DataComponents.FOOD) != null) < 0) {
                        var13.add("no food in the hotbar");
                     }

                     if (this.autoBuyRocketsSetting.getValue() || this.autoBuyXpBottlesSetting.getValue()) {
                        var13.add(
                           "it may buy "
                              + (this.autoBuyRocketsSetting.getValue() ? "rockets" : "")
                              + (this.autoBuyRocketsSetting.getValue() && this.autoBuyXpBottlesSetting.getValue() ? " and " : "")
                              + (this.autoBuyXpBottlesSetting.getValue() ? "XP bottles" : "")
                              + " from /ah on the ground, at most "
                              + this.priceLimitSetting.getValue().toLowerCase(Locale.ROOT)
                              + " prices and "
                              + AuctionParser.formatPrice(AuctionParser.parsePriceLimit(this.tripBudgetSetting.getValue()))
                              + " this trip"
                        );
                     }

                     if (!var13.isEmpty()) {
                        this.sendMessage("heads up: " + String.join("; ", var13) + ".");
                     }

                     LarpLog.info(
                        this.getName(),
                        "Started: to {} from {}, {} blocks, height {}, {} rockets, elytra {} uses, {}",
                        formatCoords(this.destinationX, this.destinationZ),
                        minecraft.player.blockPosition().toShortString(),
                        formatCount(var4),
                        this.flightHeightSetting.getValue().intValue(),
                        var3,
                        this.getElytraDurability(),
                        this.dimension
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.tripActive) {
         this.pendingStopReason = "the world, the player or the connection changed mid-flight";
         LarpLog.info(this.getName(), "Interrupted in {} after {} blocks: {}", this.phase, formatCount(this.distanceFlown), this.pendingStopReason);
      }

      super.onDisable();
      this.consumables.reset();
      this.rocketController.reset();
      this.resetState();
      if (this.lastStatus != null) {
         this.setStatus(this.lastStatus);
         this.lastStatus = null;
      }
   }

   private void resetState() {
      RocketBooster.releaseHotbar(this);
      this.shopBuyer.abort("the trip ended");
      this.moneySpent = 0L;
      this.rocketsBought = this.xpBottlesBought = 0;
      this.failedPurchases.clear();
      this.tripActive = (boolean)0;
      this.level = this.player = this.connection = null;
      this.dimension = null;
      this.phase = AutoElytraPilot.Phase.PREPARE;
      this.stopMode = AutoElytraPilot.StopMode.NONE;
      this.stopTask = AutoElytraPilot.StopTask.NONE;
      this.tickCounter = this.phaseStartTick = this.stopTaskStartTick = 0;
      this.lastPlanTick = this.lastLandingSearchTick = this.lastLandingPlanTick = -1000;
      this.landingSearchCount = 0;
      this.flightPlan = null;
      this.landingPlan = null;
      this.badLandingPlanCount = this.goAroundCount = 0;
      this.landingSpot = null;
      this.landingInWater = this.circling = (boolean)0;
      this.bestHeightAboveSpot = Double.MAX_VALUE;
      this.lastLandingProgressTick = 0;
      this.lastRocketTick = this.rocketBoostEndTick = this.rocketHoldUntilTick = -1000;
      this.lastRocketDuration = 3;
      this.lastJumpTick = -1000;
      this.takeoffJumpCount = this.takeoffAttempts = 0;
      this.takeoffYaw = null;
      this.takeoffStraightUp = (boolean)1;
      this.hasLookTarget = (boolean)0;
      this.pressForward = this.pressJump = (boolean)0;
      this.lastInputTick = Integer.MIN_VALUE;
      this.swimStartTick = -1;
      this.shoreTarget = null;
      this.bestMendDurability = this.lastMendProgressTick = this.mendFailureCount = this.xpBottlesUsed = 0;
      this.lastSwapTick = -1000;
      this.lastDamageTick = -1000;
      this.lastPosition = this.lastVelocity = null;
      this.rubberbandCount = 0;
      this.rubberbandWindowStart = 0;
      this.distanceFlown = this.startDistance = this.speedEstimate = 0.0;
      this.stopReason = "";
      this.rejectedLandingSpots.clear();
      this.predictedPath.clear();
      this.surfaceHeightCache.clear();
      this.rotator.reset();
      this.startingRockets = this.rocketsUsed = 0;
      this.cachedMaxHeight = Integer.MIN_VALUE;
      this.lastHeightScanTick = -1000;
   }

   private void abortStart(String var1) {
      LarpLog.info(this.getName(), "Not started: {}", var1);
      this.sendMessage("not started, " + var1 + ".");
      NotificationManager.getInstance().notifyDefault("Auto Elytra Pilot", discardLandingSpot(var1), NotificationManager.Type.WARNING);
      this.setStatus("Not started: " + var1);
      this.lastStatus = this.getStatus();
      this.disable();
   }

   @Override
   public void onTick() {
      this.rocketController.releaseHotbar();
      this.pressForward = this.pressJump = (boolean)0;
      this.hasLookTarget = (boolean)0;
      boolean var2 = false;
      byte var1 = this.tripActive;
      int var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (minecraft.level == this.level && minecraft.player == this.player) {
            if (minecraft.level.dimension() != this.dimension) {
               this.finishTrip(false, "you changed dimension");
            } else if (!minecraft.player.isAlive()) {
               this.finishTrip(false, "you died");
            } else {
               byte var4 = 1;
               int var3 = this.tickCounter;
               this.tickCounter = var3 + var4;
               this.lastInputTick = minecraft.player.tickCount;
               this.sendMessage();
               if (ViewController.isFreecam()) {
                  this.setStatus("Paused while Freecam is on");
               } else if (this.isPlayerSteering()) {
                  this.rotator.reset();
                  this.setStatus("Paused while you steer");
               } else {
                  if (!this.parseDestination() && !this.tryDeployOnFall()) {
                     switch (this.phase) {
                        case PREPARE:
                           this.tickPrepare();
                           break;
                        case TAKEOFF:
                           this.tickTakeoff();
                           break;
                        case CLIMB:
                           this.setStatus();
                           break;
                        case CRUISE:
                           this.tickCruise();
                           break;
                        case DESCEND:
                           this.tickDescend();
                           break;
                        case LAND:
                           this.tickLand();
                           break;
                        case STOP_TASK:
                           this.formatCount();
                           break;
                        case ARRIVED:
                           this.tickArrived();
                           break;
                        case STOPPING:
                           this.tickStopping();
                     }
                  }

                  int var9 = this.isEnabled();
                  boolean var6 = false;
                  byte var5 = (byte)var9;
                  var9 = Integer.signum(var5);
                  if (((var9 | -var9) >>> 31 ^ 1) == 0) {
                     boolean var8 = false;
                     byte var7 = this.tripActive;
                     var9 = Integer.signum(var7);
                     if (((var9 | -var9) >>> 31 ^ 1) == 0) {
                        this.applyLook();
                     }
                  }
               }
            }
         } else {
            this.finishTrip(false, "the world or the player changed");
         }
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
         );
   }

   private void setPhase(AutoElytraPilot.Phase var1) {
      if (var1 == AutoElytraPilot.Phase.LAND && this.phase != AutoElytraPilot.Phase.LAND) {
         this.lastLandingPlanTick = -1000;
         this.badLandingPlanCount = 0;
      }

      if (var1 == AutoElytraPilot.Phase.STOP_TASK && this.phase != AutoElytraPilot.Phase.STOP_TASK) {
         this.stopTask = AutoElytraPilot.StopTask.NONE;
         this.failedPurchases.clear();
      }

      if (var1 == AutoElytraPilot.Phase.TAKEOFF) {
         this.takeoffJumpCount = 0;
         this.takeoffAttempts++;
         this.takeoffYaw = null;
         this.landingSpot = null;
         this.goAroundCount = 0;
         this.lastLandingSearchTick = -1000;
         this.landingSearchCount = 0;
         if (this.stopMode != AutoElytraPilot.StopMode.LAND_SAFELY) {
            this.stopMode = AutoElytraPilot.StopMode.NONE;
         }
      }

      if (var1 == AutoElytraPilot.Phase.CRUISE) {
         this.takeoffAttempts = 0;
      }

      if (var1 == AutoElytraPilot.Phase.DESCEND && this.phase != AutoElytraPilot.Phase.DESCEND) {
         this.bestHeightAboveSpot = Double.MAX_VALUE;
         this.lastLandingProgressTick = this.tickCounter;
      }

      if (var1 != AutoElytraPilot.Phase.DESCEND) {
         this.circling = (boolean)0;
      }

      if (var1 != this.phase) {
         LarpLog.info(
            this.getName(),
            "{} -> {} at {} ({} blocks to go, job {})",
            this.phase,
            var1,
            minecraft.player.blockPosition().toShortString(),
            formatCount(this.getSurfaceHeight()),
            this.stopMode
         );
      }

      this.phase = var1;
      this.phaseStartTick = this.tickCounter;
   }

   private int hasUsableElytra() {
      return this.tickCounter - this.phaseStartTick;
   }

   private void tickPrepare() {
      if (minecraft.player.isInWater()) {
         this.isPlayerSteering();
      } else if (this.isGliding()) {
         this.setPhase(AutoElytraPilot.Phase.CLIMB);
      } else if (!minecraft.player.onGround()) {
         this.setStatus("Waiting to land");
      } else {
         this.setStatus("Getting ready");
         if (!this.hasUsableElytra()) {
            if (!this.setStatus()) {
               this.finishTrip(false, "there is no usable elytra left");
            }
         } else if (this.getSurfaceHeight() <= 30.0) {
            this.stopMode = AutoElytraPilot.StopMode.NONE;
            this.setPhase(AutoElytraPilot.Phase.ARRIVED);
         } else if (this.hasUsableElytra() >= 4) {
            AutoElytraPilot.StopTask var1 = this.chooseStopTask(true);
            if (var1 != AutoElytraPilot.StopTask.NONE) {
               this.stopMode = AutoElytraPilot.StopMode.LAND_FOR_SERVICE;
               this.setPhase(AutoElytraPilot.Phase.STOP_TASK);
            } else if (RocketBooster.countRockets(minecraft.player) == 0) {
               this.finishTrip(false, this.notify());
            } else {
               int var2 = this.getElytraDurability();
               if (var2 >= 0 && var2 < 30) {
                  this.finishTrip(false, "the elytra is almost broken and cannot be mended here");
               } else if (this.takeoffAttempts >= 6) {
                  this.finishTrip(false, "it could not take off here; it needs open sky above you");
               } else {
                  this.stopMode = AutoElytraPilot.StopMode.NONE;
                  this.setPhase(AutoElytraPilot.Phase.TAKEOFF);
               }
            }
         }
      }
   }

   private void tickTakeoff() {
      if (minecraft.player.isInWater()) {
         this.isPlayerSteering();
      } else if (this.isGliding()) {
         this.setPhase(AutoElytraPilot.Phase.CLIMB);
      } else if (!this.hasUsableElytra()) {
         this.setPhase(AutoElytraPilot.Phase.PREPARE);
      } else if (!this.rocketController.hasRocket()) {
         this.setPhase(AutoElytraPilot.Phase.PREPARE);
      } else {
         this.setStatus("Taking off");
         if (minecraft.player.onGround()) {
            if (this.tickCounter - this.lastJumpTick >= 14) {
               if (this.takeoffJumpCount >= 5) {
                  this.finishTrip(false, "it could not take off here; it needs open sky above you");
               } else {
                  this.takeoffStraightUp = this.discardLandingSpot(16);
                  if (!this.takeoffStraightUp) {
                     this.takeoffYaw = this.openElytra();
                     if (this.takeoffYaw == null) {
                        this.finishTrip(false, "there is no open sky to take off from; stand somewhere open");
                        return;
                     }
                  }

                  float var1 = this.takeoffStraightUp ? minecraft.player.getYRot() : this.takeoffYaw;
                  float var2 = this.takeoffStraightUp ? -88.0F : -35.0F;
                  this.steerToward(var1, var2);
                  this.rocketController.selectRocketSlot(3);
                  if (Math.abs(Mth.wrapDegrees(var1 - minecraft.player.getYRot())) <= 10.0F && Math.abs(var2 - minecraft.player.getXRot()) <= 10.0F) {
                     this.rocketController.hasRocket();
                     this.lastJumpTick = this.tickCounter;
                     this.takeoffJumpCount++;
                     LarpLog.debug(
                        this.getName(),
                        "Takeoff jump {} ({})",
                        this.takeoffJumpCount,
                        this.takeoffStraightUp ? "straight up" : "toward open sky at yaw " + (int)var1
                     );
                  }
               }
            }
         } else {
            this.steerToward(this.takeoffStraightUp ? minecraft.player.getYRot() : this.takeoffYaw, this.takeoffStraightUp ? -88.0F : -35.0F);
            if (minecraft.player.getDeltaMovement().y < 0.12 && this.tickCounter - this.lastJumpTick >= 2) {
               this.openElytra();
            }
         }
      }
   }

   private void openElytra() {
      if (this.tickCounter - this.lastJumpTick >= 2) {
         this.rocketController.hasRocket();
         this.lastJumpTick = this.tickCounter;
      } else {
         this.rocketController.setJump();
      }
   }

   private void setStatus() {
      if (this.isFlyingReady()) {
         double var1 = this.getSurfaceHeight();
         this.planParams.cruiseCeiling = this.isPlayerSteering(var1);
         if (!this.tickTakeoff()) {
            double var3 = minecraft.player.getY();
            boolean var5 = this.getSurfaceHeight();
            this.setStatus("Climbing to " + (int)(Math.round(this.planParams.cruiseCeiling / 10.0) * 10L));
            if (var3 >= this.planParams.cruiseCeiling - 15.0 || !var5 && this.hasUsableElytra() > 600) {
               this.setPhase(var1 <= this.getXpBottleTarget() ? AutoElytraPilot.Phase.DESCEND : AutoElytraPilot.Phase.CRUISE);
            } else if (var5) {
               this.steerToward(minecraft.player.getYRot(), -80.0F);
               if (this.setDestinationFromView() && minecraft.player.getXRot() < -60.0F) {
                  this.useRocket();
               }
            } else {
               this.steerToward(this.destinationX, this.destinationZ, Double.NaN, true);
            }
         }
      }
   }

   private void tickCruise() {
      if (this.isFlyingReady()) {
         double var1 = this.getSurfaceHeight();
         this.planParams.cruiseCeiling = this.isPlayerSteering(var1);
         if (!this.tickTakeoff()) {
            if (var1 <= this.getXpBottleTarget()) {
               this.stopMode = AutoElytraPilot.StopMode.NONE;
               this.landingSpot = null;
               this.goAroundCount = 0;
               this.setPhase(AutoElytraPilot.Phase.DESCEND);
            } else {
               this.setStatus("Flying to " + formatCoords(this.destinationX, this.destinationZ).replace(" ", ", "));
               this.isGliding();
               this.steerToward(this.destinationX, this.destinationZ, Double.NaN, true);
            }
         }
      }
   }

   private void tickDescend() {
      if (this.isFlyingReady()) {
         if (this.stopMode != AutoElytraPilot.StopMode.NONE || !this.tickTakeoff()) {
            int var1 = this.stopMode != AutoElytraPilot.StopMode.NONE ? 1 : 0;
            if (this.landingSpot == null && this.tickCounter - this.lastLandingSearchTick >= (var1 != 0 ? 10 : 20)) {
               this.formatCount((boolean)var1);
            }

            double var2 = this.landingSpot != null ? this.landingSpot.x : this.destinationX;
            double var4 = this.landingSpot != null ? this.landingSpot.z : this.destinationZ;
            double var6 = this.landingSpot != null ? this.landingSpot.y : this.getPhase(var2, var4);
            double var8 = Math.hypot(var2 - minecraft.player.getX(), var4 - minecraft.player.getZ());
            double var10 = minecraft.player.getY() - var6;
            this.planParams.cruiseCeiling = this.isPlayerSteering(this.getSurfaceHeight());
            this.setStatus(
               this.stopMode == AutoElytraPilot.StopMode.NONE
                  ? (this.landingSpot == null ? "Descending, looking for a landing spot" : "Descending to land")
                  : (this.stopMode == AutoElytraPilot.StopMode.LAND_FOR_SERVICE ? "Landing for a stop" : "Landing to stop safely")
            );
            if (this.landingSpot == null && this.stopMode != AutoElytraPilot.StopMode.NONE) {
               this.steerToward(this.destinationX, this.destinationZ, Double.NaN, var1 == 0 || RocketBooster.countRockets(minecraft.player) > 3);
            } else {
               if (this.landingSpot != null) {
                  if (var10 < this.bestHeightAboveSpot - 3.0) {
                     this.bestHeightAboveSpot = var10;
                     this.lastLandingProgressTick = this.tickCounter;
                  } else if (this.tickCounter - this.lastLandingProgressTick > 700) {
                     this.notify(NotificationManager.Type.INFO, "Cannot get down to this landing spot, picking another");
                     this.discardLandingSpot();
                     this.lastLandingProgressTick = this.tickCounter;
                     return;
                  }

                  if (var8 <= 50.0 && var10 <= 28.0 && var10 >= -1.0) {
                     this.setPhase(AutoElytraPilot.Phase.LAND);
                     return;
                  }
               }

               double var12 = var6 + 14.0 + Math.max(0.0, var8 - 45.0) * 0.095;
               double var14 = minecraft.player.getY() - var12;
               if (!this.circling && var8 < 170.0 && var10 > 32.0 && (var14 > 22.0 || var8 < 60.0)) {
                  this.circling = (boolean)1;
                  LarpLog.debug(this.getName(), "Circling down: {} blocks above the slope, {} across", (int)var14, (int)var8);
               }

               if (this.circling && (var10 <= 26.0 || var14 < 8.0 && var8 >= 60.0 || var8 > 260.0)) {
                  this.circling = (boolean)0;
               }

               if (this.circling) {
                  double var16 = Math.atan2(minecraft.player.getZ() - var4, minecraft.player.getX() - var2) + Math.toRadians(55.0);
                  this.planParams.maxEndSpeed = var14 > 40.0 ? 3.0 : 1.9;
                  this.steerToward(var2 + Math.cos(var16) * 42.0, var4 + Math.sin(var16) * 42.0, var6, true);
               } else {
                  this.planParams.maxEndSpeed = 1.45;
                  this.steerToward(var2, var4, var6, true);
               }
            }
         }
      }
   }

   private void tickLand() {
      if (minecraft.player.isInWater()) {
         if (minecraft.player.isUnderWater()) {
            this.pressJump = (boolean)1;
         } else {
            this.restockNow();
         }
      } else if (minecraft.player.onGround() && !this.isGliding()) {
         this.restockNow();
      } else if (!this.isGliding()) {
         this.setStatus("Landing");
         if (minecraft.player.getDeltaMovement().y < -0.3 && !minecraft.player.hasEffect(MobEffects.LEVITATION)) {
            this.openElytra();
         }
      } else if (this.landingSpot == null) {
         this.setPhase(AutoElytraPilot.Phase.DESCEND);
      } else {
         double var1 = Math.hypot(this.landingSpot.x - minecraft.player.getX(), this.landingSpot.z - minecraft.player.getZ());
         double var3 = minecraft.player.getY() - this.landingSpot.y;
         if (!(var3 < -4.0) && this.hasUsableElytra() <= 700 && this.badLandingPlanCount <= 24 && !(var1 > 90.0)) {
            this.setStatus("Landing (" + (int)var1 + " blocks out, " + (int)Math.max(0.0, var3) + " up)");
            if (this.tickCounter - this.lastLandingPlanTick >= 2 || this.landingPlan == null) {
               this.lastLandingPlanTick = this.tickCounter;
               this.syncFlightState();
               this.landingPlan = this.flightPlanner
                  .planLanding(
                     this::getSurfaceHeight,
                     this::isLandable,
                     this.flightState,
                     this.landingSpot.x,
                     this.landingSpot.y,
                     this.landingSpot.z,
                     this.getRocketBoostTicksLeft(),
                     this.useRocket()
                  );
               this.badLandingPlanCount = this.landingPlan.cost() >= 900.0 ? this.badLandingPlanCount + 1 : 0;
            }

            this.steerToward(this.landingPlan.yaw(), this.landingPlan.pitch());
         } else {
            this.goAround(var3 < -4.0 ? "below the spot" : (var1 > 90.0 ? "too far from the spot" : "the approach did not work out"));
         }
      }
   }

   private void restockNow() {
      if (this.stopMode == AutoElytraPilot.StopMode.NONE) {
         double var1 = this.landingSpot == null ? Double.MAX_VALUE : Math.hypot(this.landingSpot.x - this.destinationX, this.landingSpot.z - this.destinationZ);
         double var3 = this.landingSpot == null
            ? Double.MAX_VALUE
            : Math.hypot(this.landingSpot.x - minecraft.player.getX(), this.landingSpot.z - minecraft.player.getZ());
         if (this.getSurfaceHeight() <= 72.0 || var3 <= 25.0 || this.getSurfaceHeight() <= var1 + 20.0) {
            this.setPhase(AutoElytraPilot.Phase.ARRIVED);
            return;
         }

         LarpLog.info(this.getName(), "Touched down {} blocks short of the spot; taking off again", formatCount(var3));
         if (this.landingSpot != null) {
            this.discardLandingSpot();
         }

         this.setPhase(AutoElytraPilot.Phase.PREPARE);
      } else if (this.stopMode == AutoElytraPilot.StopMode.LAND_SAFELY) {
         if (this.getSurfaceHeight() <= 72.0) {
            LarpLog.info(this.getName(), "Stopped at the destination; counting it as arrived ({})", this.stopReason);
            this.setPhase(AutoElytraPilot.Phase.ARRIVED);
         } else {
            this.setPhase(AutoElytraPilot.Phase.STOPPING);
         }
      } else {
         this.setPhase(AutoElytraPilot.Phase.STOP_TASK);
      }
   }

   private void goAround(String var1) {
      this.goAroundCount++;
      this.notify(NotificationManager.Type.INFO, "Go-around (" + var1 + ")");
      if (this.goAroundCount >= 4) {
         this.discardLandingSpot();
         this.goAroundCount = 0;
      }

      this.setPhase(AutoElytraPilot.Phase.DESCEND);
   }

   private void formatCount() {
      if (minecraft.player.isInWater()) {
         this.isPlayerSteering();
      } else if (this.isGliding()) {
         this.setPhase(AutoElytraPilot.Phase.LAND);
      } else if (!minecraft.player.onGround()) {
         this.setStatus("Waiting to land");
      } else if (this.isThreatened()) {
         this.consumables.stopEating();
         this.shopBuyer.abort("a threat at the stop");
         int var3 = RocketBooster.countRockets(minecraft.player) < 2
               || !this.hasUsableElytra()
               || this.getElytraDurability() >= 0 && this.getElytraDurability() < 30
            ? 0
            : 1;
         if (var3 != 0) {
            this.notify(NotificationManager.Type.WARNING, "Danger at the landing spot, taking off");
            this.rejectedLandingSpots.add(minecraft.player.position());
            this.setPhase(AutoElytraPilot.Phase.TAKEOFF);
         } else {
            this.finishTrip(false, "a mob reached the stop and there was no way to take off");
         }
      } else {
         if (this.stopTask == AutoElytraPilot.StopTask.NONE) {
            this.stopTask = this.chooseStopTask(false);
            this.stopTaskStartTick = this.tickCounter;
            if (this.stopTask == AutoElytraPilot.StopTask.NONE) {
               if (!this.hasUsableElytra()) {
                  if (this.setStatus()) {
                     return;
                  }

                  this.finishTrip(false, "there is no usable elytra left");
                  return;
               }

               if (RocketBooster.countRockets(minecraft.player) == 0) {
                  this.finishTrip(false, this.notify());
                  return;
               }

               int var2 = this.getElytraDurability();
               if (var2 >= 0 && var2 < 40) {
                  this.finishTrip(false, "the elytra is too worn to fly on and cannot be mended");
                  return;
               }

               if (this.getSurfaceHeight() <= 30.0) {
                  this.stopMode = AutoElytraPilot.StopMode.NONE;
                  this.setPhase(AutoElytraPilot.Phase.ARRIVED);
                  return;
               }

               this.stopMode = AutoElytraPilot.StopMode.NONE;
               this.notify(
                  NotificationManager.Type.SUCCESS,
                  "Stop done: " + RocketBooster.countRockets(minecraft.player) + " rockets, " + this.clearDestination() + " XP bottles"
               );
               this.setPhase(AutoElytraPilot.Phase.TAKEOFF);
               return;
            }

            LarpLog.info(this.getName(), "Stop task: {}", this.stopTask);
         }

         int var1 = this.stopTask != AutoElytraPilot.StopTask.BUY_ROCKETS && this.stopTask != AutoElytraPilot.StopTask.BUY_XP_BOTTLES ? 0 : 1;
         if (this.tickCounter - this.stopTaskStartTick > (var1 != 0 ? 3600 : 400)) {
            LarpLog.info(this.getName(), "Stop task {} timed out", this.stopTask);
            if (var1 != 0) {
               this.shopBuyer.abort("it took too long");
               this.failedPurchases
                  .add(this.stopTask == AutoElytraPilot.StopTask.BUY_ROCKETS ? AuctionParser.AuctionItem.FIREWORK : AuctionParser.AuctionItem.EXP_BOTTLE);
            }

            this.clearDestination();
         } else {
            switch (this.stopTask) {
               case EAT:
                  this.setStatus("Eating");
                  if (!this.consumables.tryEat(4)) {
                     this.clearDestination();
                  }
                  break;
               case MEND_ELYTRA:
                  this.tickMend();
                  break;
               case SWAP_ELYTRA:
                  this.setStatus("Putting on the spare elytra");
                  if (!this.setStatus()) {
                     this.clearDestination();
                  }
                  break;
               case BUY_ROCKETS:
                  this.buyItem(AuctionParser.AuctionItem.FIREWORK);
                  break;
               case BUY_XP_BOTTLES:
                  this.buyItem(AuctionParser.AuctionItem.EXP_BOTTLE);
                  break;
               default:
                  this.stopTask = AutoElytraPilot.StopTask.NONE;
            }
         }
      }
   }

   private void clearDestination() {
      this.consumables.stopEating();
      this.stopTask = AutoElytraPilot.StopTask.NONE;
   }

   private boolean sendMessage(AuctionParser.AuctionItem var1) {
      boolean var2 = var1 == AuctionParser.AuctionItem.FIREWORK ? this.autoBuyRocketsSetting.getValue() : this.autoBuyXpBottlesSetting.getValue();
      return var2 && !this.failedPurchases.contains(var1) && !this.budgetExhausted && this.tickLand() > 0L;
   }

   private long tickLand() {
      long var1 = AuctionParser.parsePriceLimit(this.tripBudgetSetting.getValue());
      return var1 < 0L ? Long.MAX_VALUE : Math.max(0L, var1 - this.moneySpent);
   }

   private int finishTrip() {
      return this.rocketStacksSetting.getValue().intValue() * 64;
   }

   private int getXpBottleTarget() {
      return this.xpBottleStacksSetting.getValue().intValue() * 64;
   }

   private void buyItem(AuctionParser.AuctionItem var1) {
      int var2 = var1 == AuctionParser.AuctionItem.FIREWORK ? 1 : 0;
      if (!this.shopBuyer.isRunning()) {
         int var7 = var2 != 0 ? RocketBooster.countRockets(minecraft.player) : this.clearDestination();
         int var8 = (var2 != 0 ? this.finishTrip() : this.getXpBottleTarget()) - var7;
         if (var8 <= 0) {
            this.clearDestination();
         } else {
            long var5 = this.tickLand();
            this.shopBuyer
               .startKind(var1, var8, AuctionParser.stackPriceThreshold(this.priceLimitSetting.getValue(), var1), var5 == Long.MAX_VALUE ? -1L : var5);
            this.setStatus("Buying " + (var2 != 0 ? "rockets" : "XP bottles") + " on /ah");
         }
      } else {
         AuctionBuyer.State var3 = this.shopBuyer.tick();
         this.setStatus("Buying " + (var2 != 0 ? "rockets" : "XP bottles") + " on /ah: " + this.shopBuyer.getBoughtCount() + " so far");
         if (var3 != AuctionBuyer.State.RUNNING) {
            this.moneySpent = this.moneySpent + this.shopBuyer.getSpent();
            if (var2 != 0) {
               this.rocketsBought = this.rocketsBought + this.shopBuyer.getBoughtCount();
            } else {
               this.xpBottlesBought = this.xpBottlesBought + this.shopBuyer.getBoughtCount();
            }

            if (this.shopBuyer.isOutOfMoney()) {
               this.budgetExhausted = (boolean)1;
            }

            String var4 = var2 != 0 ? "rockets" : "XP bottles";
            if (this.shopBuyer.getBoughtCount() > 0) {
               this.notify(
                  NotificationManager.Type.SUCCESS,
                  "Bought "
                     + this.shopBuyer.getBoughtCount()
                     + " "
                     + var4
                     + " for "
                     + AuctionParser.formatPrice(this.shopBuyer.getSpent())
                     + (this.shopBuyer.onChatMessage().isEmpty() ? "" : " (" + this.shopBuyer.onChatMessage() + ")")
               );
            }

            if (var3 == AuctionBuyer.State.FAILED || !this.shopBuyer.onChatMessage().isEmpty()) {
               this.failedPurchases.add(var1);
            }

            if (var3 == AuctionBuyer.State.FAILED) {
               this.notify(NotificationManager.Type.WARNING, "Could not buy " + var4 + ": " + this.shopBuyer.onChatMessage());
            }

            this.clearDestination();
         }
      }
   }

   private AutoElytraPilot.StopTask chooseStopTask(boolean var1) {
      LocalPlayer var2 = minecraft.player;
      if ((var2.getFoodData().getFoodLevel() <= 10 || var2.getHealth() < 15.0F && var2.getFoodData().getFoodLevel() < 20)
         && AutoSustain.findHotbarSlot(var0 -> var0.get(DataComponents.FOOD) != null) >= 0
         && this.tickPrepare()) {
         return AutoElytraPilot.StopTask.EAT;
      } else {
         int var3 = this.getElytraDurability();
         int var4 = this.tickArrived();
         boolean var5 = this.canMend();
         int var6 = var5 && this.sendMessage(AuctionParser.AuctionItem.EXP_BOTTLE) ? 1 : 0;
         if (var3 >= 0 && var3 <= this.tickLand() && (!var5 || this.clearDestination() <= 0 && var6 == 0) && this.sendMessage() >= 0) {
            return AutoElytraPilot.StopTask.SWAP_ELYTRA;
         } else {
            int var7 = var5 && var3 >= 0 && var3 < var4 * (var1 ? 0.9 : 0.97) ? 1 : 0;
            int var8 = RocketBooster.countRockets(minecraft.player);
            if (var7 != 0 && this.clearDestination() == 0 && var6 != 0) {
               return AutoElytraPilot.StopTask.BUY_XP_BOTTLES;
            } else if (var7 != 0 && this.clearDestination() > 0) {
               return AutoElytraPilot.StopTask.MEND_ELYTRA;
            } else if (this.getSurfaceHeight() < 1500.0 && var8 >= 4) {
               return AutoElytraPilot.StopTask.NONE;
            } else if (var8 < this.finishTrip() / 2 && this.sendMessage(AuctionParser.AuctionItem.FIREWORK)) {
               return AutoElytraPilot.StopTask.BUY_ROCKETS;
            } else {
               return var6 != 0 && this.clearDestination() < this.getXpBottleTarget() / 2
                  ? AutoElytraPilot.StopTask.BUY_XP_BOTTLES
                  : AutoElytraPilot.StopTask.NONE;
            }
         }
      }
   }

   private boolean tickPrepare() {
      return !minecraft.player.isUsingItem() || minecraft.player.getUseItem().get(DataComponents.FOOD) != null;
   }

   private void tickMend() {
      int var1 = this.getElytraDurability();
      int var2 = this.tickArrived();
      ItemStack var3 = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
      if (var1 >= 0 && var3.isDamaged() && var1 < var2) {
         if (this.tickCounter - this.stopTaskStartTick < 2) {
            this.bestMendDurability = var1;
            this.lastMendProgressTick = this.tickCounter;
         }

         if (var1 > this.bestMendDurability) {
            this.bestMendDurability = var1;
            this.lastMendProgressTick = this.tickCounter;
         }

         if (this.tickCounter - this.lastMendProgressTick > 200) {
            this.mendFailureCount++;
            this.notify(NotificationManager.Type.WARNING, "Mending stopped working here (other Mending gear may be taking the XP)");
            this.clearDestination();
         } else {
            this.setStatus("Mending the elytra: " + var1 * 10 / Math.max(1, var2) * 10 + "%");
            if (!this.consumables.throwXpBottle(() -> this.xpBottlesUsed++)) {
               LarpLog.info(this.getName(), "Mending ended at {} of {} uses: no XP bottles left in the hotbar", var1, var2);
               this.clearDestination();
            }
         }
      } else {
         if (var1 >= 0) {
            this.mendFailureCount = 0;
            this.notify(NotificationManager.Type.SUCCESS, "Elytra repaired");
         }

         this.clearDestination();
      }
   }

   private void tickArrived() {
      this.setStatus("Arrived");
      if (minecraft.player.isInWater()) {
         if (minecraft.player.isUnderWater()) {
            this.pressJump = (boolean)1;
         } else if (this.hasUsableElytra() > 20) {
            this.finishTrip(true, null);
         }
      } else if (!minecraft.player.onGround()) {
         if (this.isGliding()) {
            this.stopMode = AutoElytraPilot.StopMode.NONE;
            this.setPhase(AutoElytraPilot.Phase.LAND);
         }
      } else {
         Vec3 var1 = minecraft.player.getDeltaMovement();
         if (this.hasUsableElytra() >= 10 || Math.hypot(var1.x, var1.z) <= 0.03) {
            this.finishTrip(true, null);
         }
      }
   }

   private void tickStopping() {
      this.setStatus("Stopping: " + this.stopReason);
      if (!minecraft.player.onGround() && !minecraft.player.isInWater()) {
         if (this.isGliding()) {
            this.setPhase(AutoElytraPilot.Phase.LAND);
         }
      } else {
         this.finishTrip(false, this.stopReason.isEmpty() ? "the trip could not go on" : this.stopReason);
      }
   }

   private void steerToward(double var1, double var3, double var5, boolean var7) {
      if (this.tickCounter - this.lastPlanTick >= 2 || this.flightPlan == null) {
         this.lastPlanTick = this.tickCounter;
         this.surfaceHeightCache.clear();
         this.syncFlightState();
         this.planParams.targetX = var1;
         this.planParams.targetZ = var3;
         this.planParams.targetY = var5;
         this.planParams.clearance = 14.0;
         int var8 = this.dimension == Level.END ? 1 : 0;
         this.planParams.flightCeiling = var8 != 0 ? 185.0 : 262.0;
         if (!Double.isNaN(var5)) {
            this.planParams.flightCeiling = Math.min(this.planParams.flightCeiling, Math.max(this.canMend(), var5) + 48.0);
         }

         this.planParams.minEndY = var8 != 0 ? 35.0 : Double.NEGATIVE_INFINITY;
         this.planParams.rocketCount = this.lastRocketDuration;
         this.planParams.lookPitch = this.useRocket();
         this.planParams.useRockets = (boolean)(!var7
               || this.tickCounter < this.rocketHoldUntilTick
               || !this.openElytra() && !(minecraft.player.getY() < this.planParams.cruiseCeiling - 50.0)
            ? 0
            : 1);
         int var9 = RocketBooster.countRockets(minecraft.player);
         this.planParams.rocketBonus = var9 < 12 ? 140.0 : (var9 < 32 ? 95.0 : 70.0);
         this.flightPlan = this.flightPlanner
            .plan(this::getSurfaceHeight, this.flightState, this.getRocketBoostTicksLeft(), this.setDestinationFromView(), this.planParams);
         this.finishManualRestock();
      }

      this.steerToward(this.flightPlan.yaw(), this.flightPlan.pitch());
      if (this.flightPlan.rocket()
         && this.setDestinationFromView()
         && Math.abs(minecraft.player.getXRot() - this.flightPlan.pitch()) < 20.0F
         && Math.abs(Mth.wrapDegrees(this.flightPlan.yaw() - minecraft.player.getYRot())) < 30.0F) {
         this.useRocket();
      }
   }

   private void finishManualRestock() {
      this.predictedPath.clear();
      ElytraPhysics.FlightState var1 = new ElytraPhysics.FlightState().copy(this.flightState);
      int var2 = this.flightPlan.rocket() ? ElytraPhysics.boostDurationTicks(this.planParams.rocketCount) : this.getRocketBoostTicksLeft();

      for (int var3 = 0; var3 < 60; var3++) {
         var1.yaw = ElytraPhysics.stepYaw(var1.yaw, this.flightPlan.yaw(), 12.0F);
         var1.pitch = ElytraPhysics.stepYaw(var1.pitch, this.flightPlan.pitch(), 9.0F);
         ElytraPhysics.lookY(var1, this.planParams.lookPitch);
         ElytraPhysics.advancePosition(var1);
         if (var2-- > 0) {
            ElytraPhysics.applyFireworkBoost(var1);
         }

         if (var3 % 3 == 0) {
            this.predictedPath.add(new Vec3(var1.posX, var1.posY, var1.posZ));
         }
      }
   }

   private void syncFlightState() {
      Vec3 var1 = minecraft.player.getDeltaMovement();
      this.flightState
         .reset(
            minecraft.player.getX(),
            minecraft.player.getY(),
            minecraft.player.getZ(),
            var1.x,
            var1.y,
            var1.z,
            minecraft.player.getYRot(),
            minecraft.player.getXRot()
         );
   }

   private double useRocket() {
      return minecraft.player.hasEffect(MobEffects.SLOW_FALLING) && minecraft.player.getDeltaMovement().y <= 0.0 ? 0.01 : 0.08;
   }

   private boolean formatCoords() {
      if (this.tickCounter < this.rocketBoostEndTick - 12 && this.tickCounter - this.lastRocketTick < 4) {
         return true;
      }

      if (this.tickCounter - this.lastRocketTick > 80) {
         return false;
      }

      LocalPlayer var1 = minecraft.player;
      return !minecraft.level
         .getEntitiesOfClass(
            FireworkRocketEntity.class, var1.getBoundingBox().inflate(3.0), var1x -> var1x.isAlive() && var1x.getOwner() == var1 && !var1x.isShotAtAngle()
         )
         .isEmpty();
   }

   private int getRocketBoostTicksLeft() {
      return this.formatCoords() ? Math.max(0, this.rocketBoostEndTick - this.tickCounter) : 0;
   }

   private boolean setDestinationFromView() {
      return this.isGliding()
         && this.rocketController.hasRocket()
         && this.tickCounter - this.lastRocketTick >= 10
         && !this.formatCoords()
         && !minecraft.player.isUsingItem()
         && this.allowsScreenInteractions();
   }

   private void useRocket() {
      this.rocketController.selectRocketSlot(3);
      RocketBooster.RocketSearch var1 = RocketBooster.findRocket(minecraft.player, 3);
      int var2 = var1.rocket() == null ? 3 : Math.max(1, var1.rocket().duration());
      this.rocketController
         .scheduleBoost(() -> this.isRuntimeActive() && this.tripActive && this.isGliding() && minecraft.player == this.player, 10.0, 3, () -> {
            this.lastRocketTick = this.tickCounter;
            this.lastRocketDuration = var2;
            this.rocketBoostEndTick = this.tickCounter + ElytraPhysics.boostDurationTicks(var2);
            this.rocketsUsed++;
         });
   }

   private void steerToward(float var1, float var2) {
      this.targetYaw = var1;
      this.targetPitch = Mth.clamp(var2, -90.0F, 90.0F);
      this.hasLookTarget = (boolean)1;
   }

   private void applyLook() {
      if (this.hasLookTarget && InteractionArbiter.ownsInteraction(this)) {
         float var1 = Math.abs(Mth.wrapDegrees(this.targetYaw - minecraft.player.getYRot())) + Math.abs(this.targetPitch - minecraft.player.getXRot());
         float[] var2 = this.rotator.rotate(minecraft.player.getYRot(), minecraft.player.getXRot(), this.targetYaw, this.targetPitch, var1 < 1.5F);
         RotationManager.requestForModule(this, RotationManager.Priority.BOT, var2[0], var2[1], 1.0);
      }
   }

   private boolean isGliding() {
      return minecraft.player.isFallFlying() && !minecraft.player.isInWater();
   }

   private boolean isFlyingReady() {
      if (this.isGliding()) {
         return true;
      }

      if (!minecraft.player.onGround()) {
         if (minecraft.player.isInWater()) {
            this.setPhase(AutoElytraPilot.Phase.PREPARE);
            return false;
         }

         this.setStatus("Opening the elytra");
         if (minecraft.player.getDeltaMovement().y < 0.0 && !minecraft.player.hasEffect(MobEffects.LEVITATION)) {
            this.openElytra();
         }

         return false;
      } else {
         if (this.phase != AutoElytraPilot.Phase.LAND && this.phase != AutoElytraPilot.Phase.DESCEND) {
            this.setPhase(AutoElytraPilot.Phase.PREPARE);
         } else {
            this.restockNow();
         }

         return false;
      }
   }

   private boolean tryDeployOnFall() {
      if (this.phase == AutoElytraPilot.Phase.TAKEOFF || this.isGliding() || minecraft.player.onGround() || minecraft.player.hasEffect(MobEffects.LEVITATION)) {
         return false;
      }

      if (!this.hasUsableElytra() || minecraft.player.onClimbable() || minecraft.player.isInWater()) {
         return false;
      }

      if (minecraft.player.getDeltaMovement().y > -0.5) {
         return false;
      }

      if (minecraft.player.fallDistance < 3.0 && !this.tickStopping(10)) {
         return false;
      }

      this.setStatus("Falling: opening the elytra");
      this.openElytra();
      this.steerToward(minecraft.player.getYRot(), 8.0F);
      return true;
   }

   private boolean tickStopping(int var1) {
      BlockPos var2 = minecraft.player.blockPosition();

      for (int var3 = 1; var3 <= var1; var3++) {
         if (!minecraft.level.getBlockState(var2.below(var3)).getCollisionShape(minecraft.level, var2.below(var3)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private boolean parseDestination() {
      int var1 = this.getElytraDurability();
      if (this.isGliding() && var1 >= 0 && var1 <= 6 && this.tickCounter - this.lastSwapTick >= 6) {
         this.lastSwapTick = this.tickCounter;
         if (this.sendMessage() < 0) {
            return false;
         }

         LarpLog.info(this.getName(), "Elytra at {} uses mid-air; swapping in the spare", var1);
         return this.setStatus();
      } else {
         return false;
      }
   }

   private void isGliding() {
      int var1 = minecraft.player.getFoodData().getFoodLevel();
      if (var1 > 10 && !minecraft.player.isUsingItem()) {
         this.consumables.stopEating();
      } else if (!this.formatCoords() && !(minecraft.player.getY() < 70.0) && (this.flightPlan == null || !this.flightPlan.wide())) {
         if (this.consumables.tryEat(4)) {
            this.setStatus("Eating while cruising");
         }
      }
   }

   private boolean tickTakeoff() {
      if (this.phase == AutoElytraPilot.Phase.DESCEND && this.stopMode != AutoElytraPilot.StopMode.NONE) {
         return false;
      }

      int var1 = this.getElytraDurability();
      int var2 = RocketBooster.countRockets(minecraft.player);
      double var3 = this.getSurfaceHeight();
      int var5 = this.tryDeployOnFall();
      int var6 = (var1 < 0 || var1 > getInstance(var3) / (1 + var5) * 1.35 + 25.0) && var2 > var3 / 1000.0 * 2.6 + 5.0 ? 1 : 0;
      int var7 = var1 >= 0 && var1 <= (var5 >= 1 ? 40 : 60) ? 1 : 0;
      int var8 = var2 <= 3 ? 1 : 0;
      if (var6 != 0 && var7 == 0 && var8 == 0) {
         return false;
      }

      int var9 = var1 >= 0 && var1 <= this.tickLand() ? 1 : 0;
      int var10 = !this.canMend() || this.clearDestination() <= 0 && !this.sendMessage(AuctionParser.AuctionItem.EXP_BOTTLE) ? 0 : 1;
      boolean var11 = this.sendMessage(AuctionParser.AuctionItem.FIREWORK);
      AutoElytraPilot.StopMode var12 = null;
      if (var9 == 0 || var10 == 0 && this.sendMessage() < 0) {
         if (var9 != 0 && var1 <= 40) {
            var12 = AutoElytraPilot.StopMode.LAND_SAFELY;
         }
      } else {
         var12 = AutoElytraPilot.StopMode.LAND_FOR_SERVICE;
      }

      if (var12 == null && var2 <= 20 && var11) {
         var12 = AutoElytraPilot.StopMode.LAND_FOR_SERVICE;
      }

      if (var12 == null && var2 <= 6) {
         var12 = AutoElytraPilot.StopMode.LAND_SAFELY;
      }

      if (var12 == null && var7 != 0) {
         var12 = var10 == 0 && this.sendMessage() < 0 ? AutoElytraPilot.StopMode.LAND_SAFELY : AutoElytraPilot.StopMode.LAND_FOR_SERVICE;
      }

      if (var12 == null && var8 != 0) {
         var12 = var11 ? AutoElytraPilot.StopMode.LAND_FOR_SERVICE : AutoElytraPilot.StopMode.LAND_SAFELY;
      }

      if (var12 == null) {
         return false;
      }

      if (var12 == AutoElytraPilot.StopMode.LAND_SAFELY && this.stopReason.isEmpty()) {
         this.stopReason = var2 <= 6 ? this.notify() : "the elytra is worn out and cannot be mended";
      }

      this.stopMode = var12;
      this.landingSpot = null;
      this.goAroundCount = 0;
      this.lastLandingSearchTick = -1000;
      this.notify(
         NotificationManager.Type.INFO,
         var12 == AutoElytraPilot.StopMode.LAND_FOR_SERVICE
            ? "Landing for a stop (" + (var9 != 0 ? "mend or swap the elytra" : "elytra low") + ")"
            : "Landing to stop safely: " + this.stopReason
      );
      this.setPhase(AutoElytraPilot.Phase.DESCEND);
      return true;
   }

   static double getInstance(double var0) {
      return var0 / 27.0 + 50.0;
   }

   private int tickLand() {
      int var1 = this.tryDeployOnFall();
      int var2 = this.tickArrived();
      return Math.max(var1 >= 3 ? 50 : (var1 >= 1 ? 80 : 110), (int)(var2 * (var1 >= 3 ? 0.15 : (var1 >= 1 ? 0.2 : 0.27))));
   }

   private boolean canMend() {
      return this.autoMendingSetting.getValue() && AutoSustain.hasMending(minecraft.player.getItemBySlot(EquipmentSlot.CHEST)) && this.mendFailureCount < 3;
   }

   private String notify() {
      int var1 = RocketBooster.countRockets(minecraft.player);
      return var1 == 0 ? "it ran out of rockets" : "only " + var1 + " rockets are left";
   }

   private boolean isThreatened() {
      if (this.tickCounter - this.lastDamageTick < 30 && minecraft.player.getHealth() < 14.0F) {
         return true;
      }

      AABB var1 = minecraft.player.getBoundingBox().inflate(9.0, 4.0, 9.0);

      for (Entity var3 : minecraft.level.getEntities(minecraft.player, var1, var0 -> var0.isAlive() && var0 instanceof Enemy)) {
         if (!(var3 instanceof NeutralMob var4 && !var4.isAngry()) && var3 instanceof LivingEntity var5 && minecraft.player.hasLineOfSight(var5)) {
            return true;
         }
      }

      return false;
   }

   private void formatCount(boolean var1) {
      this.lastLandingSearchTick = this.tickCounter;
      double var2 = minecraft.player.getX();
      double var4 = minecraft.player.getZ();
      SafeSpotFinder.TerrainView var7 = new SafeSpotFinder.TerrainView() {
         @Override
         public int surface(int var1, int var2x) {
            return AutoElytraPilot.this.getSurfaceHeight(var1, var2x);
         }

         @Override
         public SafeSpotFinder.GroundType ground(int var1, int var2x, int var3) {
            return AutoElytraPilot.this.discardLandingSpot(var1, var2x, var3);
         }
      };
      SafeSpotFinder.SafeSpot var6;
      if (this.stopMode == AutoElytraPilot.StopMode.NONE) {
         if (this.getSurfaceHeight(Mth.floor(this.destinationX), Mth.floor(this.destinationZ)) == Integer.MIN_VALUE) {
            return;
         }

         this.landingSearchCount++;
         var6 = SafeSpotFinder.find(var7, this.destinationX, this.destinationZ, var2, var4, 96, false, false);
         if (var6 == null) {
            var6 = SafeSpotFinder.find(var7, this.destinationX, this.destinationZ, var2, var4, 96, false, true);
         }

         if (var6 == null) {
            var6 = SafeSpotFinder.find(var7, this.destinationX, this.destinationZ, var2, var4, 160, false, true);
         }

         if (var6 == null) {
            var6 = SafeSpotFinder.find(var7, this.destinationX, this.destinationZ, var2, var4, 160, true, false);
         }

         if (var6 == null && this.landingSearchCount >= 12) {
            var6 = SafeSpotFinder.find(var7, this.destinationX, this.destinationZ, var2, var4, 300, true, true);
         }

         if (var6 == null && this.landingSearchCount >= 24) {
            var6 = SafeSpotFinder.find(var7, var2, var4, var2, var4, 220, true, true);
         }

         if (var6 == null && this.landingSearchCount >= 40) {
            this.stopReason = "there is no safe place to land near the destination";
            this.stopMode = AutoElytraPilot.StopMode.LAND_SAFELY;
            return;
         }
      } else {
         Vec3 var8 = Vec3.directionFromRotation(0.0F, minecraft.player.getYRot());
         double var9 = var1 ? 30.0 : 110.0;
         double var11 = var2 + var8.x * var9;
         double var13 = var4 + var8.z * var9;
         var6 = SafeSpotFinder.find(var7, var11, var13, var2, var4, var1 ? 120 : 150, false, false);
         if (var6 == null) {
            var6 = SafeSpotFinder.find(var7, var11, var13, var2, var4, var1 ? 120 : 150, false, true);
         }

         if (var6 == null && var1) {
            var6 = SafeSpotFinder.find(var7, var2, var4, var2, var4, 200, true, true);
         }
      }

      for (int var15 = 0; var6 != null && var15 < 4 && this.chooseStopTask(var6); var15++) {
         double var16 = Math.toRadians(90 * var15 + 45);
         var6 = SafeSpotFinder.find(
            var7,
            var6.blockX() + Math.cos(var16) * 60.0,
            var6.blockZ() + Math.sin(var16) * 60.0,
            var2,
            var4,
            90,
            this.stopMode != AutoElytraPilot.StopMode.NONE && var1,
            true
         );
      }

      if (var6 != null && !this.chooseStopTask(var6)) {
         this.landingSpot = new Vec3(var6.blockX() + 0.5, var6.blockY(), var6.blockZ() + 0.5);
         this.landingInWater = var6.water();
         LarpLog.info(
            this.getName(),
            "Landing spot {} {} {}{} ({} blocks from the destination, score {})",
            var6.blockX(),
            var6.blockY(),
            var6.blockZ(),
            var6.water() ? " in water" : "",
            formatCount(Math.hypot(this.landingSpot.x - this.destinationX, this.landingSpot.z - this.destinationZ)),
            String.format(Locale.ROOT, "%.1f", var6.score())
         );
      }
   }

   private boolean chooseStopTask(SafeSpotFinder.SafeSpot var1) {
      for (Vec3 var3 : this.rejectedLandingSpots) {
         if (Math.hypot(var3.x - var1.blockX() - 0.5, var3.z - var1.blockZ() - 0.5) < 12.0) {
            return true;
         }
      }

      return false;
   }

   private void discardLandingSpot() {
      if (this.landingSpot != null) {
         this.rejectedLandingSpots.add(this.landingSpot);
         LarpLog.info(this.getName(), "Landing spot {} set aside", BlockPos.containing(this.landingSpot).toShortString());
      }

      this.landingSpot = null;
      this.circling = (boolean)0;
      this.bestHeightAboveSpot = Double.MAX_VALUE;
   }

   private boolean isLandable(int var1, int var2, int var3) {
      SafeSpotFinder.GroundType var4 = this.discardLandingSpot(var1, var2, var3);
      return var4 == SafeSpotFinder.GroundType.WATER
         ? this.landingInWater || this.stopMode == AutoElytraPilot.StopMode.LAND_SAFELY
         : var4 != SafeSpotFinder.GroundType.LAVA && var4 != SafeSpotFinder.GroundType.HAZARD;
   }

   int getSurfaceHeight(int var1, int var2) {
      long var3 = (long)var1 << 32 ^ var2 & 4294967295L;
      Integer var5 = this.surfaceHeightCache.get(var3);
      if (var5 != null) {
         return var5;
      }

      LevelChunk var6 = minecraft.level.getChunkSource().getChunk(var1 >> 4, var2 >> 4, ChunkStatus.FULL, false);
      int var7;
      if (var6 == null) {
         var7 = Integer.MIN_VALUE;
      } else {
         int var8 = var6.getHeight(Types.MOTION_BLOCKING, var1 & 15, var2 & 15) + 1;
         var7 = var8 <= minecraft.level.getMinY() ? minecraft.level.getMinY() - 64 : var8;
      }

      this.surfaceHeightCache.put(var3, var7);
      return var7;
   }

   private double getPhase(double var1, double var3) {
      int var5 = this.getSurfaceHeight(Mth.floor(var1), Mth.floor(var3));
      return var5 != Integer.MIN_VALUE && var5 > minecraft.level.getMinY() + 1 ? var5 : (this.dimension == Level.END ? 60.0 : 68.0);
   }

   SafeSpotFinder.GroundType discardLandingSpot(int var1, int var2, int var3) {
      if (var2 - 1 < minecraft.level.getMinY()) {
         return SafeSpotFinder.GroundType.INVALID;
      }

      BlockPos var4 = new BlockPos(var1, var2 - 1, var3);
      BlockState var5 = minecraft.level.getBlockState(var4);
      FluidState var6 = var5.getFluidState();
      if (var6.is(FluidTags.LAVA)) {
         return SafeSpotFinder.GroundType.LAVA;
      }

      if (var6.is(FluidTags.WATER)) {
         return SafeSpotFinder.GroundType.WATER;
      }

      if (var5.is(BlockTags.LEAVES)) {
         return SafeSpotFinder.GroundType.LEAVES;
      }

      if (var5.is(Blocks.MAGMA_BLOCK)
         || var5.is(BlockTags.CAMPFIRES)
         || var5.is(Blocks.CACTUS)
         || var5.is(Blocks.SWEET_BERRY_BUSH)
         || var5.is(BlockTags.FIRE)
         || var5.is(Blocks.POWDER_SNOW)
         || var5.is(Blocks.POINTED_DRIPSTONE)
         || var5.is(Blocks.WITHER_ROSE)
         || var5.is(Blocks.COBWEB)) {
         return SafeSpotFinder.GroundType.HAZARD;
      }

      if (!var5.is(Blocks.SLIME_BLOCK) && !var5.is(Blocks.HONEY_BLOCK)) {
         VoxelShape var7 = var5.getCollisionShape(minecraft.level, var4);
         if (var7.isEmpty()) {
            return SafeSpotFinder.GroundType.INVALID;
         } else {
            AABB var8 = var7.bounds();
            if (!(var8.maxY > 1.01) && !(var8.maxY < 0.45) && !(var8.maxX - var8.minX < 0.9) && !(var8.maxZ - var8.minZ < 0.9)) {
               BlockState var9 = minecraft.level.getBlockState(var4.above());
               return var9.getFluidState().isEmpty() && var9.getCollisionShape(minecraft.level, var4.above()).isEmpty()
                  ? SafeSpotFinder.GroundType.SOLID
                  : SafeSpotFinder.GroundType.INVALID;
            } else {
               return SafeSpotFinder.GroundType.INVALID;
            }
         }
      } else {
         return SafeSpotFinder.GroundType.INVALID;
      }
   }

   private boolean openElytra() {
      Vec3 var1 = minecraft.player.getDeltaMovement();
      double var2 = Math.hypot(var1.x, var1.z);
      Vec3 var4 = var2 > 0.2 ? new Vec3(var1.x / var2, 0.0, var1.z / var2) : Vec3.directionFromRotation(0.0F, minecraft.player.getYRot());
      int var5 = Mth.floor(minecraft.player.getX() + var4.x * 110.0);
      int var6 = Mth.floor(minecraft.player.getZ() + var4.z * 110.0);
      return minecraft.level.getChunkSource().getChunk(var5 >> 4, var6 >> 4, ChunkStatus.FULL, false) != null;
   }

   private int canMend() {
      if (this.tickCounter - this.lastHeightScanTick >= 20 || this.cachedMaxHeight == Integer.MIN_VALUE) {
         this.lastHeightScanTick = this.tickCounter;
         int var1 = minecraft.player.getBlockX();
         int var2 = minecraft.player.getBlockZ();
         int var3 = Integer.MIN_VALUE;

         for (short var4 = -160; var4 <= 160; var4 += 16) {
            for (short var5 = -160; var5 <= 160; var5 += 16) {
               int var6 = this.getSurfaceHeight(var1 + var4, var2 + var5);
               if (var6 != Integer.MIN_VALUE) {
                  var3 = Math.max(var3, var6);
               }
            }
         }

         this.cachedMaxHeight = var3 == Integer.MIN_VALUE ? (int)this.getPhase(this.destinationX, this.destinationZ) : var3;
      }

      return this.cachedMaxHeight;
   }

   private boolean getSurfaceHeight() {
      int var1 = minecraft.player.getBlockX();
      int var2 = minecraft.player.getBlockZ();
      int var3 = Integer.MIN_VALUE;

      for (byte var4 = -24; var4 <= 24; var4 += 6) {
         for (byte var5 = -24; var5 <= 24; var5 += 6) {
            var3 = Math.max(var3, this.getSurfaceHeight(var1 + var4, var2 + var5));
         }
      }

      return var3 != Integer.MIN_VALUE && minecraft.player.getY() < var3 + 20 && this.hasUsableElytra() < 200;
   }

   private boolean discardLandingSpot(int var1) {
      AABB var2 = minecraft.player.getBoundingBox().deflate(0.02, 0.0, 0.02);
      int var3 = Mth.floor(minecraft.player.getY() + 0.6);

      for (int var4 = Mth.floor(var2.minX); var4 <= Mth.floor(var2.maxX); var4++) {
         for (int var5 = Mth.floor(var2.minZ); var5 <= Mth.floor(var2.maxZ); var5++) {
            for (int var6 = var3; var6 <= var3 + var1; var6++) {
               BlockPos var7 = new BlockPos(var4, var6, var5);
               if (!minecraft.level.getBlockState(var7).getCollisionShape(minecraft.level, var7).isEmpty()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private Float openElytra() {
      Vec3 var1 = minecraft.player.getEyePosition();
      if (this.refreshUi(var1, new Vec3(0.0, 1.0, 0.0), 2.2)) {
         return null;
      }

      Vec3 var2 = minecraft.player.position();
      float var3 = minecraft.player.getYRot();

      for (float var7 : new float[]{0.0F, 45.0F, -45.0F, 90.0F, -90.0F, 135.0F, -135.0F, 180.0F}) {
         Vec3 var8 = Vec3.directionFromRotation(-32.0F, var3 + var7);
         Vec3 var9 = new Vec3(-var8.z, 0.0, var8.x).normalize().scale(0.35);
         byte var10 = 0;

         for (Vec3 var14 : new Vec3[]{
            var2.add(0.0, 1.2, 0.0), var2.add(0.0, 1.8, 0.0), var2.add(0.0, 1.2, 0.0).add(var9), var2.add(0.0, 1.2, 0.0).subtract(var9)
         }) {
            if (this.refreshUi(var14, var8, 22.0)) {
               var10 = 1;
               break;
            }
         }

         if (var10 == 0) {
            return var3 + var7;
         }
      }

      return null;
   }

   private boolean refreshUi(Vec3 var1, Vec3 var2, double var3) {
      BlockHitResult var5 = minecraft.level.clip(new ClipContext(var1, var1.add(var2.normalize().scale(var3)), Block.COLLIDER, Fluid.NONE, minecraft.player));
      return var5.getType() == Type.BLOCK;
   }

   private boolean hasUsableElytra() {
      ItemStack var1 = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) && LivingEntity.canGlideUsing(var1, EquipmentSlot.CHEST);
   }

   private int getElytraDurability() {
      ItemStack var1 = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) && var1.isDamageableItem() ? var1.getMaxDamage() - var1.getDamageValue() : -1;
   }

   private int tickArrived() {
      ItemStack var1 = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);
      return var1.is(Items.ELYTRA) ? var1.getMaxDamage() : 432;
   }

   private int tryDeployOnFall() {
      ItemStack var1 = minecraft.player.getItemBySlot(EquipmentSlot.CHEST);

      for (Entry var3 : var1.getEnchantments().entrySet()) {
         if (((Holder)var3.getKey()).is(Enchantments.UNBREAKING)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   private int sendMessage() {
      int var1 = Math.max(0, this.getElytraDurability());
      int var2 = -1;
      int var3 = var1;

      for (int var4 = 0; var4 < 9; var4++) {
         ItemStack var5 = minecraft.player.getInventory().getItem(var4);
         if (var5.is(Items.ELYTRA) && LivingEntity.canGlideUsing(var5, EquipmentSlot.CHEST)) {
            int var6 = var5.getMaxDamage() - var5.getDamageValue();
            if (var6 >= 60 && var6 > var3 + 20) {
               var2 = var4;
               var3 = var6;
            }
         }
      }

      return var2;
   }

   private boolean setStatus() {
      int var1 = this.sendMessage();
      if (var1 < 0) {
         return false;
      }

      InteractionArbiter.queueAction(this, () -> {
         int var1x = this.sendMessage();
         if (var1x >= 0) {
            Inventory var2 = minecraft.player.getInventory();
            var2.setSelectedSlot(var1x);
            if (minecraft.gameMode.useItem(minecraft.player, InteractionHand.MAIN_HAND).consumesAction()) {
               SwingHelper.swingInteract(minecraft.player, InteractionHand.MAIN_HAND);
               LarpLog.info(this.getName(), "Put on the spare elytra from hotbar slot {}", var1x + 1);
               this.notify(NotificationManager.Type.INFO, "Put on the spare elytra");
            }
         }
      }, () -> this.isRuntimeActive() && this.tripActive && minecraft.player == this.player);
      return true;
   }

   private int clearDestination() {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = minecraft.player.getInventory().getItem(var2);
         if (var3.is(Items.EXPERIENCE_BOTTLE)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   private boolean getElytraDurability() {
      for (int var1 = 9; var1 < 36; var1++) {
         if (RocketBooster.isPlainRocket(minecraft.player.getInventory().getItem(var1))) {
            return true;
         }
      }

      return false;
   }

   private void isPlayerSteering() {
      if (this.swimStartTick < 0) {
         this.swimStartTick = this.tickCounter;
      }

      if (this.tickCounter - this.swimStartTick > 1200) {
         this.finishTrip(false, "it was stuck in water and could not reach the shore");
      } else {
         this.setStatus("Swimming to shore");
         this.pressJump = (boolean)1;
         if (this.tickCounter % 20 == 0 || this.shoreTarget == null) {
            this.shoreTarget = this.tickMend(32);
         }

         if (this.shoreTarget == null) {
            this.steerToward(minecraft.player.getYRot(), 0.0F);
         } else {
            float[] var1 = RotationManager.calculateAngles(minecraft.player.getEyePosition(), this.shoreTarget);
            this.steerToward(var1[0], 0.0F);
            if (Math.abs(Mth.wrapDegrees(var1[0] - minecraft.player.getYRot())) < 30.0F) {
               this.pressForward = (boolean)1;
            }
         }
      }
   }

   private Vec3 tickMend(int var1) {
      int var2 = minecraft.player.getBlockX();
      int var3 = minecraft.player.getBlockZ();
      Vec3 var4 = null;
      double var5 = Double.MAX_VALUE;

      for (int var7 = -var1; var7 <= var1; var7 += 2) {
         for (int var8 = -var1; var8 <= var1; var8 += 2) {
            int var9 = this.getSurfaceHeight(var2 + var7, var3 + var8);
            if (var9 != Integer.MIN_VALUE && this.discardLandingSpot(var2 + var7, var9, var3 + var8) == SafeSpotFinder.GroundType.SOLID) {
               double var10 = var7 * var7 + var8 * var8;
               if (var10 < var5) {
                  var5 = var10;
                  var4 = new Vec3(var2 + var7 + 0.5, var9, var3 + var8 + 0.5);
               }
            }
         }
      }

      return var4;
   }

   public static Input setDestinationFromView() {
      if (instance != null && instance.isEnabled()) {
         AutoElytraPilot var0 = instance;
         Minecraft var1 = Minecraft.getInstance();
         return var0 != null
               && var0.isRuntimeActive()
               && var0.tripActive
               && var1.player != null
               && var1.player == var0.player
               && var1.player.tickCount == var0.lastInputTick
               && (var0.pressForward || var0.pressJump)
               && !ViewController.isFreecam()
               && !MovementCorrection.isRotationActive()
               && !(WorldPathView.currentScreen() instanceof AbstractContainerScreen)
            ? new Input(var0.pressForward, false, false, false, var0.pressJump, false, false)
            : null;
      } else {
         return null;
      }
   }

   private double getSurfaceHeight() {
      return Math.hypot(this.destinationX - minecraft.player.getX(), this.destinationZ - minecraft.player.getZ());
   }

   private double isPlayerSteering(double var1) {
      double var3 = this.getPhase(this.destinationX, this.destinationZ);
      double var5 = var3 + 14.0 + Math.max(0.0, var1 - 45.0) * 0.095 + 30.0;
      return Math.max(this.dimension == Level.END ? 150.0 : 140.0, Math.min(this.flightHeightSetting.getValue(), var5));
   }

   private double getXpBottleTarget() {
      double var1 = this.getPhase(this.destinationX, this.destinationZ);
      double var3 = Math.max(0.0, minecraft.player.getY() - var1 - 14.0);
      return 45.0 + var3 / 0.095 + 60.0;
   }

   private void sendMessage() {
      Vec3 var1 = minecraft.player.position();
      if (this.lastPosition != null) {
         double var2 = Math.hypot(var1.x - this.lastPosition.x, var1.z - this.lastPosition.z);
         if (this.lastVelocity != null && var1.distanceTo(this.lastPosition.add(this.lastVelocity)) > 4.0 && this.isGliding()) {
            if (this.tickCounter - this.rubberbandWindowStart > 200) {
               this.rubberbandWindowStart = this.tickCounter;
               this.rubberbandCount = 0;
            }

            this.rubberbandCount++;
            if (this.rubberbandCount >= 3 && this.tickCounter >= this.rocketHoldUntilTick) {
               this.rocketHoldUntilTick = this.tickCounter + 60;
               LarpLog.info(this.getName(), "The server pulled the player back {} times in 10 seconds; holding rockets for 3 seconds", this.rubberbandCount);
            }
         } else if (var2 < 10.0) {
            this.distanceFlown += var2;
         }

         double var4 = (Math.hypot(this.destinationX - this.lastPosition.x, this.destinationZ - this.lastPosition.z) - this.getSurfaceHeight()) * 20.0;
         this.speedEstimate = this.speedEstimate == 0.0 ? var4 : this.speedEstimate * 0.97 + var4 * 0.03;
      }

      this.lastPosition = var1;
      this.lastVelocity = minecraft.player.getDeltaMovement();
      float var6 = minecraft.player.getHealth();
      if (var6 < this.lastHealth) {
         this.lastDamageTick = this.tickCounter;
      }

      this.lastHealth = var6;
   }

   private String isFlyingReady() {
      double var1 = this.getSurfaceHeight();
      StringBuilder var3 = new StringBuilder(formatCount(var1)).append(" blocks left");
      if (this.speedEstimate > 2.0 && this.phase != AutoElytraPilot.Phase.ARRIVED) {
         var3.append(", about ").append(ModulePanel.formatDuration((long)(var1 / this.speedEstimate * 1000.0)));
      }

      return var3.toString();
   }

   private void finishTrip(boolean var1, String var2) {
      if (!this.tripActive) {
         this.disable();
      } else {
         String var3 = ModulePanel.formatDuration(System.currentTimeMillis() - this.startMillis);
         String var4 = BlockPos.containing(minecraft.player.position()).toShortString();
         String var5 = formatCount(this.distanceFlown)
            + " blocks in "
            + var3
            + ", "
            + this.rocketsUsed
            + " rockets"
            + (this.xpBottlesUsed > 0 ? ", " + this.xpBottlesUsed + " XP bottles" : "")
            + (
               this.moneySpent > 0L
                  ? ", bought "
                     + (this.rocketsBought > 0 ? this.rocketsBought + " rockets" : "")
                     + (this.rocketsBought > 0 && this.xpBottlesBought > 0 ? " and " : "")
                     + (this.xpBottlesBought > 0 ? this.xpBottlesBought + " XP bottles" : "")
                     + " for "
                     + AuctionParser.formatPrice(this.moneySpent)
                  : ""
            );
         String var6 = var1
            ? "arrived at " + formatCoords(this.destinationX, this.destinationZ).replace(" ", ", ") + ": " + var5 + "."
            : "stopped at " + var4 + ", " + var2 + ". " + discardLandingSpot(var5) + ".";
         LarpLog.info(this.getName(), var1 ? "Arrived: {}" : "Stopped: {}", var6);
         this.sendMessage(var6);
         this.notify(var1 ? NotificationManager.Type.SUCCESS : NotificationManager.Type.WARNING, discardLandingSpot(var6));
         this.setStatus(var1 ? "Arrived" : "Stopped: " + var2);
         this.lastStatus = this.getStatus();
         int var7 = !this.logOffOnArrivalSetting.getValue()
               || minecraft.getConnection() == null
               || minecraft.isLocalServer()
               || !var1 && !minecraft.player.onGround() && !minecraft.player.isInWater()
            ? 0
            : 1;
         this.tripActive = (boolean)0;
         this.disable();
         if (var7 != 0) {
            LarpLog.info(this.getName(), "Logging off after the trip");
            minecraft.getConnection().getConnection().disconnect(Component.literal("Larp Auto Elytra Pilot: " + var6));
         } else if (this.logOffOnArrivalSetting.getValue() && minecraft.isLocalServer()) {
            LarpLog.info(this.getName(), "Log Off On Arrival skipped in singleplayer");
         }
      }
   }

   private void sendMessage(String var1) {
      if (minecraft != null && minecraft.player != null) {
         minecraft.player.sendSystemMessage(Component.literal("Larp Auto Elytra Pilot: " + var1));
      }
   }

   private void notify(NotificationManager.Type var1, String var2) {
      LarpLog.info(this.getName(), "Notice: {}", var2);
      if (!var2.equals(this.lastNoticeText) || this.tickCounter - this.lastNoticeTick >= 100) {
         this.lastNoticeText = var2;
         this.lastNoticeTick = this.tickCounter;
         switch ((String)this.notificationsSetting.getValue()) {
            case "Toast":
               NotificationManager.getInstance().notify("Auto Elytra Pilot", var2, var1, 5000L);
               break;
            case "Chat":
               this.sendMessage(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
         }
      }
   }

   private static String discardLandingSpot(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1);
   }

   private void steerToward() {
      if (this.isInGame() && this.waypointLineSetting.getValue() && this.isRuntimeActive() && this.tripActive && minecraft.level == this.level) {
         try {
            int var1 = Theme.scaleRounding(0.25);
            double var2 = this.getPhase(this.destinationX, this.destinationZ);
            Gizmos.line(
                  new Vec3(this.destinationX, var2, this.destinationZ),
                  new Vec3(this.destinationX, var2 + 256.0, this.destinationZ),
                  ColorUtil.withAlpha(var1, 0.9),
                  3.0F
               )
               .setAlwaysOnTop();
            Vec3 var4 = minecraft.player.position();
            if (!this.predictedPath.isEmpty() && this.phase != AutoElytraPilot.Phase.LAND && minecraft.player.isFallFlying()) {
               for (Vec3 var6 : this.predictedPath) {
                  Gizmos.line(var4, var6, ColorUtil.withAlpha(var1, 0.8), 2.0F).setAlwaysOnTop();
                  var4 = var6;
               }
            }

            Gizmos.line(var4, new Vec3(this.destinationX, Math.max(var2, var4.y - 40.0), this.destinationZ), ColorUtil.withAlpha(var1, 0.35), 1.5F)
               .setAlwaysOnTop();
            if (this.landingSpot != null) {
               Gizmos.cuboid(
                     new AABB(
                        this.landingSpot.x - 1.5,
                        this.landingSpot.y,
                        this.landingSpot.z - 1.5,
                        this.landingSpot.x + 1.5,
                        this.landingSpot.y + 0.1,
                        this.landingSpot.z + 1.5
                     ),
                     GizmoStyle.strokeAndFill(-14249, 2.0F, 872400983)
                  )
                  .setAlwaysOnTop();
            }
         } catch (RuntimeException var7) {
            LarpLog.errorOnce(this.getName(), "Waypoint line failed", var7);
         }
      }
   }

   @Override
   public ModulePanel.HudCard hudCard() {
      if (this.isInGame() && this.statusCardSetting.getValue() && this.isRuntimeActive() && this.tripActive && minecraft.level == this.level) {
         double var1 = this.startDistance <= 0.0 ? -1.0 : Math.clamp(1.0 - this.getSurfaceHeight() / this.startDistance, 0.0, 1.0);
         int var3 = this.getElytraDurability();
         String var4 = this.getStatus();
         return new ModulePanel.HudCard(
            this.isFlyingReady(),
            var1 < 0.0 ? "" : Math.round(var1 * 100.0) + "%",
            var4 != null && !var4.isBlank() ? var4 : "Starting",
            RocketBooster.countRockets(minecraft.player) + " rockets" + (var3 >= 0 ? ", elytra " + var3 + "/" + this.tickArrived() : ""),
            var1
         );
      } else {
         return null;
      }
   }

   public AutoElytraPilot.Phase getPhase() {
      return this.phase;
   }

   public boolean tickCruise() {
      return this.tripActive;
   }

   public double finishTrip() {
      return this.tripActive && minecraft != null && minecraft.player != null ? this.getSurfaceHeight() : -1.0;
   }

   public int tickMend() {
      return this.rocketsUsed;
   }

   public Vec3 isLandable() {
      return this.landingSpot;
   }

   public enum Phase {
      PREPARE,
      TAKEOFF,
      CLIMB,
      CRUISE,
      DESCEND,
      LAND,
      STOP_TASK,
      ARRIVED,
      STOPPING;
   }

   enum StopMode {
      NONE,
      LAND_FOR_SERVICE,
      LAND_SAFELY;
   }

   enum StopTask {
      NONE,
      EAT,
      MEND_ELYTRA,
      SWAP_ELYTRA,
      BUY_ROCKETS,
      BUY_XP_BOTTLES;
   }
}
