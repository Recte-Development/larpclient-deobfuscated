package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class XRayModule extends Module {
   private static volatile XRayModule instance;
   private static final Map<Block, Integer> blockColors = new HashMap<>();
   private static final Map<Block, String> blockCategories = new LinkedHashMap<>();
   private static final String bASE_BLOCKS = "Base Blocks";
   private static final int mAX_LABELS = 32;
   private static volatile boolean active = (boolean)0;
   private static volatile String activeMode = "Both";
   private static volatile Set<Block> visibleBlocks = Set.of();
   private static volatile boolean terrainSeeThrough;
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "XRay rendering mode", "Both", "See Through", "ESP Outline", "Both"));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Scan radius", 32.0, 8.0, 64.0, 4.0));
   private final Map<String, BooleanSetting> categoryToggles = new LinkedHashMap<>();
   private final EspRenderer espRenderer;
   private final BooleanSetting oreColors;
   private final BooleanSetting labels;
   private final ModeSetting overlayStyle;
   private final NumberSetting maxOres;
   private final List<XRayModule.OreEntry> ores;
   private final PriorityQueue<XRayModule.OreEntry> scanQueue;
   private Set<Block> scanBlocks;
   private int filterMask;
   private boolean scanComplete;
   private BlockPos scanOrigin;
   private int scanRange;
   private int scanIndex;
   private int scanCooldown;
   private ClientLevel scanLevel;

   private static void registerBlock(String var0, int var1, Block... var2) {
      for (Block var6 : var2) {
         blockColors.put(var6, var1);
         blockCategories.put(var6, var0);
      }
   }

   public XRayModule() {
      super("XRay", "Highlight ores through blocks", Category.RENDER, -1);
      XRayModule var1 = this;
      this.espRenderer = new EspRenderer(var1x -> var1.addSetting(var1x));
      this.oreColors = this.addSetting(new BooleanSetting("Ore Colors", "Color each marker by what it is", true));
      this.labels = this.addSetting(new BooleanSetting("Labels", "Name, vein size and distance above each vein", true));
      this.overlayStyle = this.addSetting(new ModeSetting("Overlay Style", "Ore marker shape", "Box", "Box", "Corners", "Filled"));
      this.maxOres = this.addSetting(new NumberSetting("Max Ores", "Nearest ore markers retained", 512.0, 64.0, 2048.0, 64.0));
      this.ores = new ArrayList<>();
      this.scanQueue = new PriorityQueue<>(Comparator.comparingDouble(XRayModule.OreEntry::distanceSquared).reversed());
      this.scanBlocks = Set.of();
      this.filterMask = -1;
      Set var4 = Set.of("Coal", "Copper", "Quartz");

      for (String var3 : new LinkedHashSet<>(blockCategories.values())) {
         this.categoryToggles.put(var3, this.addSetting(new BooleanSetting(var3, "Show " + var3.toLowerCase(Locale.ROOT), !var4.contains(var3))));
      }

      BooleanSupplier var5 = () -> !this.mode.isMode("See Through");
      this.range.visibleWhen(var5);
      this.maxOres.visibleWhen(var5);
      this.overlayStyle.visibleWhen(var5);
      this.oreColors.visibleWhen(var5);
      this.labels.visibleWhen(var5);
      this.espRenderer
         .drawLine(() -> var5.getAsBoolean() && !this.oreColors.getValue(), var5, var5, () -> var5.getAsBoolean() && this.overlayStyle.isMode("Filled"));
      this.updateFilters();
      LevelRenderEvents.BEFORE_GIZMOS.register(this::renderOres);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> syncSeeThrough());
      instance = this;
   }

   @Override
   public void onEnable() {
      activeMode = this.mode.getValue();
      this.updateFilters();
      active = (boolean)1;
      syncSeeThrough();
      this.clearScan();
      if (isSeeThroughActive()) {
         this.renderOres();
      }

      String var1 = FabricLoader.getInstance()
         .getModContainer("sodium")
         .map(var0 -> "Sodium " + var0.getMetadata().getVersion().getFriendlyString())
         .orElse("vanilla");
      LarpLog.info("XRay", "Enabled in {} mode, {} block types shown, {} terrain", activeMode, visibleBlocks.size(), var1);
   }

   public List<Setting<?>> getModeSettings() {
      return List.of(this.mode, this.overlayStyle, this.range);
   }

   public List<Setting<?>> getCategorySettings() {
      return List.copyOf(this.categoryToggles.values());
   }

   public List<Setting<?>> getAppearanceSettings() {
      ArrayList var1 = new ArrayList<>(List.of(this.oreColors, this.labels));
      var1.addAll(this.espRenderer.colorSettings());
      var1.addAll(this.espRenderer.outlineSettings());
      var1.addAll(this.espRenderer.fillSettings());
      return List.copyOf(var1);
   }

   public List<Setting<?>> getLimitSettings() {
      return List.of(this.maxOres);
   }

   @Override
   public void onDisable() {
      active = (boolean)0;
      syncSeeThrough();
      this.clearScan();
      this.renderOres();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.mode.getValue();
         byte var2 = isSeeThroughActive();
         boolean var3 = this.updateFilters();
         int var10000 = var1.equals(activeMode);
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 == 0) {
            activeMode = var1;
            this.clearScan();
         }

         byte var4 = isSeeThroughActive();
         setSeeThrough((boolean)var4);
         byte var10 = var4;
         byte var9 = var2;
         var10000 = Integer.compare(var9, var10);
         if ((var10000 | -var10000) >>> 31 != 0 || var3 && var4 != 0) {
            LarpLog.debug("XRay", "Rebuilding terrain: mode {}, filters changed {}", activeMode, var3);
            this.renderOres();
         }

         int var15 = isEspActive();
         boolean var12 = false;
         byte var11 = (byte)var15;
         var15 = Integer.signum(var11);
         if (((var15 | -var15) >>> 31 ^ 1) == 0) {
            this.scanStep();
         } else {
            int var17 = this.ores.isEmpty();
            boolean var6 = false;
            byte var5 = (byte)var17;
            var17 = Integer.signum(var5);
            if (((var17 | -var17) >>> 31 ^ 1) != 0 || this.scanOrigin != null) {
               this.clearScan();
            }
         }
      }
   }

   private boolean updateFilters() {
      int var1 = 0;
      int var2 = 0;

      for (BooleanSetting var4 : this.categoryToggles.values()) {
         if (var4.getValue()) {
            var1 |= 1 << var2;
         }

         var2++;
      }

      if (var1 == this.filterMask) {
         return false;
      }

      this.filterMask = var1;
      HashSet var8 = new HashSet();
      HashSet var9 = new HashSet();

      for (Block var6 : blockColors.keySet()) {
         String var7 = blockCategories.get(var6);
         if (var7 == null) {
            var8.add(var6);
         } else if (this.categoryToggles.get(var7).getValue()) {
            var8.add(var6);
            var9.add(var6);
         }
      }

      visibleBlocks = Set.copyOf(var8);
      this.scanBlocks = Set.copyOf(var9);
      this.clearScan();
      return true;
   }

   private void clearScan() {
      this.ores.clear();
      this.scanQueue.clear();
      this.scanOrigin = null;
      this.scanLevel = null;
      this.scanIndex = this.scanCooldown = 0;
      this.scanComplete = (boolean)0;
   }

   private void scanStep() {
      int var1 = this.range.getValue().intValue();
      BlockPos var2 = minecraft.player.blockPosition();
      if (this.scanLevel != minecraft.level || this.scanOrigin == null || this.scanRange != var1 || this.scanOrigin.distSqr(var2) > 64.0) {
         this.clearScan();
         this.scanLevel = minecraft.level;
         this.scanOrigin = var2;
         this.scanRange = var1;
      }

      if (this.scanCooldown > 0) {
         this.scanCooldown--;
      } else {
         int var3 = var1 * 2 + 1;
         int var4 = var3 * var3 * var3;
         MutableBlockPos var5 = new MutableBlockPos();

         for (int var6 = 0; var6 < 8192 && this.scanIndex < var4; this.scanIndex++) {
            int var7 = this.scanIndex % var3;
            int var8 = this.scanIndex / var3 % var3;
            int var9 = this.scanIndex / (var3 * var3);
            int var10 = spiralOffset(var9);
            int var11 = spiralOffset(var7);
            int var12 = spiralOffset(var8);
            if (var10 * var10 + var11 * var11 + var12 * var12 <= var1 * var1) {
               var5.set(this.scanOrigin.getX() + var10, this.scanOrigin.getY() + var11, this.scanOrigin.getZ() + var12);
               LevelChunk var13 = minecraft.level.getChunkSource().getChunkNow(var5.getX() >> 4, var5.getZ() >> 4);
               if (var13 != null && var5.getY() >= minecraft.level.getMinY() && var5.getY() <= minecraft.level.getMaxY()) {
                  Block var14 = var13.getBlockState(var5).getBlock();
                  if (this.scanBlocks.contains(var14)) {
                     int var15 = 0xFF000000 | blockColors.get(var14);
                     this.scanQueue.add(new XRayModule.OreEntry(var5.immutable(), var14, var15, var5.distSqr(this.scanOrigin), 0));

                     while (this.scanQueue.size() > this.maxOres.getValue().intValue()) {
                        this.scanQueue.poll();
                     }
                  }
               }
            }

            var6++;
         }

         if (!this.scanComplete || this.scanIndex >= var4) {
            this.ores.clear();
            this.ores.addAll(this.scanQueue);
            this.ores.sort(Comparator.comparingDouble(XRayModule.OreEntry::distanceSquared));
            this.computeVeins();
         }

         if (this.scanIndex >= var4) {
            this.scanComplete = (boolean)1;
            this.scanIndex = 0;
            this.scanQueue.clear();
            this.scanCooldown = 10;
         }
      }
   }

   private void computeVeins() {
      HashMap var1 = new HashMap(this.ores.size() * 2);

      for (int var2 = 0; var2 < this.ores.size(); var2++) {
         var1.put(this.ores.get(var2).pos(), var2);
      }

      boolean[] var13 = new boolean[this.ores.size()];
      ArrayDeque var3 = new ArrayDeque();

      for (int var4 = 0; var4 < this.ores.size(); var4++) {
         if (!var13[var4]) {
            var13[var4] = (boolean)1;
            XRayModule.OreEntry var5 = this.ores.get(var4);
            String var6 = getBlockCategory(var5.block());
            int var7 = 0;
            var3.add(var4);

            while (!var3.isEmpty()) {
               BlockPos var8 = this.ores.get((Integer)var3.poll()).pos();
               var7++;

               for (int var9 = -1; var9 <= 1; var9++) {
                  for (int var10 = -1; var10 <= 1; var10++) {
                     for (int var11 = -1; var11 <= 1; var11++) {
                        Integer var12 = (Integer)var1.get(var8.offset(var9, var10, var11));
                        if (var12 != null && !var13[var12] && getBlockCategory(this.ores.get(var12).block()).equals(var6)) {
                           var13[var12] = (boolean)1;
                           var3.add(var12);
                        }
                     }
                  }
               }
            }

            this.ores.set(var4, var5.withVein(var7));
         }
      }
   }

   private static String getBlockCategory(Block var0) {
      String var1 = blockCategories.get(var0);
      return "Base Blocks".equals(var1) ? var0.getName().getString() : var1;
   }

   private static int spiralOffset(int var0) {
      return var0 == 0 ? 0 : (var0 % 2 == 1 ? (var0 + 1) / 2 : -var0 / 2);
   }

   private void renderOres(LevelRenderContext var1) {
      if (this.isOperational() && this.isInGame() && isEspActive() && this.scanLevel == minecraft.level) {
         try {
            int var2 = 0;
            int var3 = 0;
            double var4 = this.range.getValue();
            Vec3 var6 = minecraft.player.position();

            for (XRayModule.OreEntry var8 : this.ores) {
               BlockPos var9 = var8.pos();
               LevelChunk var10 = minecraft.level.getChunkSource().getChunkNow(var9.getX() >> 4, var9.getZ() >> 4);
               if (var10 != null && var10.getBlockState(var9).getBlock() == var8.block()) {
                  Vec3 var11 = Vec3.atCenterOf(var9);
                  double var12 = var6.distanceTo(var11);
                  if (!(var12 >= var4)) {
                     if (var2++ >= this.maxOres.getValue().intValue()) {
                        break;
                     }

                     int var14 = this.oreColors.getValue()
                        ? ColorUtil.multiplyAlpha(var8.color(), EspRenderer.distanceFade(var12, var4))
                        : this.espRenderer.resolveColor(var8.color(), var12, var4, 1.0, var9.hashCode());
                     this.espRenderer.drawBox(new AABB(var9).inflate(0.003), this.overlayStyle.getValue(), var14);
                     if (this.labels.getValue() && var8.vein() > 0 && var3 < 32 && var14 >>> 24 != 0) {
                        var3++;
                        String var15 = getBlockCategory(var8.block()) + (var8.vein() > 1 ? " x" + var8.vein() : "") + " · " + Math.round(var12) + "m";
                        this.espRenderer.drawLabel(var11.add(0.0, 0.85, 0.0), var15, var14);
                     }
                  }
               }
            }
         } catch (RuntimeException var16) {
            LarpLog.errorOnce("ESP", "Ore overlay failed", var16);
         }
      }
   }

   private void renderOres() {
      if (minecraft.level != null && minecraft.levelExtractor != null) {
         minecraft.levelExtractor.allChanged();
      }
   }

   private static void syncSeeThrough() {
      setSeeThrough(isSeeThroughActive());
   }

   private static void setSeeThrough(boolean var0) {
      if (var0 != terrainSeeThrough) {
         terrainSeeThrough = var0;
         LarpLog.debug("XRay", "Terrain see-through {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   public static boolean isActive() {
      return instance != null && instance.isEnabled() ? active : false;
   }

   public static boolean isSeeThroughActive() {
      return instance != null && instance.isEnabled() ? active && (activeMode.equals("See Through") || activeMode.equals("Both")) : false;
   }

   public static boolean isTerrainSeeThrough() {
      return terrainSeeThrough;
   }

   public static boolean isEspActive() {
      return instance != null && instance.isEnabled() ? active && (activeMode.equals("ESP Outline") || activeMode.equals("Both")) : false;
   }

   public static boolean isVisibleBlock(Block var0) {
      return visibleBlocks.contains(var0);
   }

   static {
      registerBlock("Diamond", 4910553, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE);
      registerBlock("Emerald", 1564002, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE);
      registerBlock("Gold", 16570170, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE);
      registerBlock("Iron", 14857359, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE);
      registerBlock("Redstone", 16724016, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
      registerBlock("Lapis", 4025343, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE);
      registerBlock("Copper", 15764043, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE);
      registerBlock("Coal", 10790052, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE);
      registerBlock("Ancient Debris", 12607548, Blocks.ANCIENT_DEBRIS);
      registerBlock("Quartz", 16052198, Blocks.NETHER_QUARTZ_ORE);
      registerBlock("Base Blocks", 4910553, Blocks.DIAMOND_BLOCK);
      registerBlock("Base Blocks", 1564002, Blocks.EMERALD_BLOCK);
      registerBlock("Base Blocks", 16570170, Blocks.GOLD_BLOCK);
      registerBlock("Base Blocks", 15132390, Blocks.IRON_BLOCK);
      registerBlock("Base Blocks", 10259596, Blocks.NETHERITE_BLOCK);
      registerBlock("Base Blocks", 16757820, Blocks.CHEST);
      registerBlock("Base Blocks", 11554047, Blocks.ENDER_CHEST);
      registerBlock("Base Blocks", 16727160, Blocks.SPAWNER);
      registerBlock("Base Blocks", 16734810, Blocks.TNT);

      for (Block var3 : new Block[]{Blocks.LAVA, Blocks.WATER, Blocks.OBSIDIAN, Blocks.BEDROCK}) {
         blockColors.put(var3, 16777215);
      }
   }

   private record OreEntry(BlockPos pos, Block block, int color, double distanceSquared, int vein) {
      XRayModule.OreEntry withVein(int var1) {
         return new XRayModule.OreEntry(this.pos, this.block, this.color, this.distanceSquared, var1);
      }
   }
}
