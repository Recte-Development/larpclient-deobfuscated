package com.larpclient.util.world;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.gui.screen.ConfirmSellScreen;
import com.larpclient.gui.screen.FakeSpawnerScreen;
import com.larpclient.hud.VisualInventory;
import com.larpclient.module.exploit.FakeSpawnerModule;
import com.larpclient.network.AuctionPriceClient;
import com.larpclient.render.model.WithdrawnBoneMarker;
import com.larpclient.render.world.ItemStackMarkers;
import com.larpclient.social.misc.PlayerMessages;
import com.larpclient.util.entity.DroppedItemTracker;
import com.larpclient.util.entity.FarmMobType;
import com.larpclient.util.inventory.ContainerTransaction;
import com.larpclient.util.inventory.FakeLootWithdrawer;
import com.larpclient.util.misc.AuctionUtil;
import com.larpclient.util.misc.JsonFiles;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PayNotice;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen.ItemPickerMenu;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class FakeSpawnerController {
   private static final FakeSpawnerController iNSTANCE = new FakeSpawnerController();
   private static final int mAX_LOOT_SLOTS = 45;
   private static final String sMALL_CAPS = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
   private static final long bREAK_GRACE_MS = 1800L;
   private static final long pLACE_GRACE_MS = 650L;
   private static final int aCCENT_COLOR = 64648;
   private static final int eRROR_COLOR = 16711680;
   private static final double pRICE_OFFSET = 75.0;
   private final Map<Long, Long> placedAtByPos = new HashMap<>();
   private final Map<Long, Map<Item, Integer>> purchasedByPos = new HashMap<>();
   private final Map<Long, Integer> stackCountByPos = new ConcurrentHashMap<>();
   private final Set<Long> brokenPositions = ConcurrentHashMap.newKeySet();
   private final Map<Long, FakeSpawnerController.PendingPlacement> pendingPlacements = new HashMap<>();
   private final Map<Long, Deque<FakeSpawnerController.SlotReservation>> reservationsByPos = new HashMap<>();
   private final Map<Integer, ItemStack> reservedStacks = new HashMap<>();
   private final Map<Long, FakeSpawnerController.RecentBreak> recentBreaks = new HashMap<>();
   private volatile Map<Long, SpawnerBlockEntity> spawnerEntities = new ConcurrentHashMap<>();
   private final Long2ObjectMap<List<SpawnerBlockEntity>> spawnersByChunk = new Long2ObjectOpenHashMap();
   private final Map<Long, Long> lastBreakEffectTick = new HashMap<>();
   private final Map<Long, Float> mineProgress = new HashMap<>();
   private final Map<Long, Integer> lastMineTick = new HashMap<>();
   private int mineCooldownTick;
   private final Set<Integer> trackedDropIds = new HashSet<>();
   private final Map<Integer, FakeSpawnerController.PendingRefund> pendingRefunds = new HashMap<>();
   private BlockPos openPilePos;
   private int boneSlot = -1;
   private final Map<Long, Integer> silkStartTick = new HashMap<>();
   private final Map<Long, Double> silkProgress = new HashMap<>();
   private final Map<Long, Integer> silkRejectUntil = new ConcurrentHashMap<>();
   private final Set<Long> silkBlockedPositions = new HashSet<>();
   private final Map<Item, Double> unitPrices = new HashMap<>();
   private final Set<Long> finalBreakPositions = ConcurrentHashMap.newKeySet();
   private volatile Level level;
   private LocalPlayer player;
   private Item fillerItem;
   private String settingsSignature;
   private volatile Map<Item, FarmMobType> kindByItem = Map.of();
   private volatile LongSet trackedPositions = LongSets.EMPTY_SET;
   private volatile LongSet ghostPositions = LongSets.EMPTY_SET;
   private final Map<Long, Long> ghostUntilTick = new ConcurrentHashMap<>();
   private final Map<Long, FarmMobType> kindByPos = new ConcurrentHashMap<>();
   private final Map<Long, FakeSpawnerController.SavedPile> savedPiles = new HashMap<>();
   private final List<JsonElement> quarantinedRecords = new ArrayList<>();
   private String saveKey;
   private boolean needsRestore = (boolean)1;
   private long terrainInvalidations;
   private long inventoryUpdates;
   private long renderCandidates;
   private long renderExtractions;
   private long displayCopies;
   private long nextReportAt;
   private final Set<ItemStack> knownStacks = Collections.newSetFromMap(new IdentityHashMap<>());
   private final Set<ItemStack> derivedStacks = Collections.newSetFromMap(new WeakHashMap<>());
   private InteractionHand useHand;
   private int useSlot = -1;
   private int useExpiryTick = -1;
   private final Set<Long> pendingFinalBreaks = ConcurrentHashMap.newKeySet();
   private long nextLogAt;

   private static Path getSavePath() {
      return FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-piles.json");
   }

   void trackInventoryStacks(Container var1) {
      this.knownStacks.clear();
      this.derivedStacks.clear();

      for (int var2 = 0; var2 < var1.getContainerSize(); var2++) {
         ItemStack var3 = var1.getItem(var2);
         if (!var3.isEmpty()) {
            this.knownStacks.add(var3);
         }
      }
   }

   public ItemStack trackDerivedStack(ItemStack var1, ItemStack var2) {
      if (!var2.isEmpty() && (this.knownStacks.contains(var1) || this.derivedStacks.contains(var1))) {
         this.derivedStacks.add(var2);
         if (Boolean.getBoolean("larp.debug")) {
            this.displayCopies++;
         }
      }

      return var2;
   }

   public void snapshotInventory() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null) {
         this.trackInventoryStacks(var1.player.getInventory());
         if (!var1.player.containerMenu.getCarried().isEmpty()) {
            this.knownStacks.add(var1.player.containerMenu.getCarried());
         }

         VisualInventory.isActive();
      }
   }

   public void onInventoryUpdate() {
      this.requestRestore();
      if (Boolean.getBoolean("larp.debug")) {
         this.inventoryUpdates++;
      }
   }

   public void recordRenderCounts(int var1, int var2) {
      if (Boolean.getBoolean("larp.debug")) {
         this.renderCandidates += var1;
         this.renderExtractions += var2;
      }
   }

   public boolean isReservedHeldStack(ItemStack var1) {
      if (!this.isActive()) {
         return false;
      }

      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 != null
         && (
            var1 == var2.getMainHandItem() && this.isReservedSlot(var2.getInventory().getSelectedSlot())
               || var1 == var2.getOffhandItem() && this.isReservedSlot(40)
         );
   }

   public void reset() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner reset must run on the client thread");
      }

      this.savePiles();
      ContainerTransaction.cancel("controller cleanup");
      VisualInventory.reset();
      this.knownStacks.clear();
      this.derivedStacks.clear();
      this.spawnerEntities = new ConcurrentHashMap<>();
      this.spawnersByChunk.clear();
      this.trackedPositions = LongSets.EMPTY_SET;
      this.ghostPositions = LongSets.EMPTY_SET;
      this.level = null;
      this.boneSlot = -1;
      this.useHand = null;
      this.useExpiryTick = -1;
      this.lastMineTick.clear();
      this.mineCooldownTick = 0;
      this.pendingFinalBreaks.clear();
      this.trackedDropIds.clear();
      this.pendingRefunds.clear();
      this.silkStartTick.clear();
      this.silkProgress.clear();
      this.silkRejectUntil.clear();
      this.silkBlockedPositions.clear();
      this.unitPrices.clear();
      this.finalBreakPositions.clear();

      for (Long var3 : new ArrayList<>(this.mineProgress.keySet())) {
         this.loadPiles(BlockPos.of(var3));
      }

      int var6 = this.stackCountByPos.isEmpty() && this.brokenPositions.isEmpty() ? 0 : 1;
      this.stackCountByPos.clear();
      this.kindByPos.clear();
      this.savedPiles.clear();
      this.quarantinedRecords.clear();
      this.saveKey = null;
      this.brokenPositions.clear();
      this.placedAtByPos.clear();
      this.purchasedByPos.clear();
      this.pendingPlacements.clear();
      this.reservationsByPos.clear();
      this.reservedStacks.clear();

      for (long var4 : this.ghostUntilTick.keySet()) {
         this.markBlockDirty(BlockPos.of(var4));
      }

      this.ghostUntilTick.clear();
      this.ghostPositions = LongSets.EMPTY_SET;
      this.recentBreaks.clear();
      this.mineProgress.clear();
      this.lastBreakEffectTick.clear();
      this.openPilePos = null;
      this.player = null;
      this.fillerItem = null;
      if (var1 != null) {
         if (var1.gui.screen() instanceof FakeSpawnerScreen || var1.gui.screen() instanceof ConfirmSellScreen) {
            var1.gui.setScreen(null);
         }

         if (var6 != 0 && var1.level != null) {
            var1.levelExtractor.allChanged();
         }
      }
   }

   private void syncState() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner state must be updated on the client thread");
      }

      ClientLevel var2 = var1 == null ? null : var1.level;
      LocalPlayer var3 = var1 == null ? null : var1.player;
      Item var4 = this.getFillerItem();
      if (var2 != this.level || var3 != this.player || !this.syncState().equals(this.settingsSignature) || !this.isActive()) {
         this.reset();
         this.level = var2;
         this.player = var3;
         this.fillerItem = var4;
         this.settingsSignature = this.syncState();
         HashMap var5 = new HashMap();
         FakeSpawnerModule var6 = FakeSpawnerModule.getInstance();
         if (var6 != null) {
            for (FarmMobType var10 : FarmMobType.values()) {
               String var11 = var6.getBlock(var10.ordinal());
               if (var11 != null && !var11.isBlank()) {
                  var5.put(resolveFillerItem(var11), var10);
               }
            }
         }

         this.kindByItem = Map.copyOf(var5);
         if (var2 != null && var3 != null && this.isActive()) {
            this.saveKey = this.buildSaveKey(var1);
            this.loadPiles();
            this.needsRestore = (boolean)1;
         }
      }
   }

   private String buildSaveKey(Minecraft var1) {
      String var2 = var1.getCurrentServer() != null
         ? var1.getCurrentServer().ip
         : (
            var1.getSingleplayerServer() != null
               ? "singleplayer:" + var1.getSingleplayerServer().getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize()
               : null
         );
      return var2 == null ? null : var2 + "|" + var1.level.dimension().identifier() + "|" + var1.player.getUUID();
   }

   private void loadPiles() {
      if (this.saveKey != null) {
         this.readPilesFrom(getSavePath());
      }
   }

   private void readPilesFrom(Path var1) {
      if (this.saveKey != null && Files.exists(var1)) {
         try {
            JsonObject var2 = JsonFiles.readJson(var1);
            if (var2.has("quarantined") && var2.get("quarantined").isJsonObject() && var2.getAsJsonObject("quarantined").has(this.saveKey)) {
               for (JsonElement var4 : var2.getAsJsonObject("quarantined").getAsJsonArray(this.saveKey)) {
                  this.quarantinedRecords.add(var4.deepCopy());
               }
            }

            if (!var2.has(this.saveKey)) {
               return;
            }

            int var20 = 0;

            for (JsonElement var5 : var2.getAsJsonArray(this.saveKey)) {
               try {
                  JsonObject var6 = var5.getAsJsonObject();
                  long var7 = var6.get("pos").getAsLong();
                  FarmMobType var9 = FarmMobType.valueOf(var6.get("kind").getAsString());
                  int var10 = var6.get("count").getAsInt();
                  long var11 = var6.get("placedAt").getAsLong();
                  ArrayList var13 = new ArrayList();

                  for (JsonElement var15 : var6.getAsJsonArray("backing")) {
                     JsonObject var16 = var15.getAsJsonObject();
                     var13.add(
                        new FakeSpawnerController.BackingSlot(
                           var16.get("slot").getAsInt(), var16.get("item").getAsString(), var16.get("amount").getAsInt(), var16.get("reserved").getAsInt()
                        )
                     );
                  }

                  HashMap var22 = new HashMap();
                  if (var6.has("purchased")) {
                     for (Entry var24 : var6.getAsJsonObject("purchased").entrySet()) {
                        int var17 = ((JsonElement)var24.getValue()).getAsInt();
                        if (var17 > 0) {
                           var22.put((String)var24.getKey(), var17);
                        }
                     }
                  }

                  if (var10 < 1 || var10 > 100000 || var10 > 1 && var13.isEmpty() || this.savedPiles.containsKey(var7)) {
                     throw new IllegalArgumentException("Invalid or conflicting saved pile");
                  }

                  this.savedPiles.put(var7, new FakeSpawnerController.SavedPile(var7, var9, var10, var11, var13, var22));
               } catch (RuntimeException var18) {
                  var20++;
                  this.quarantinedRecords.add(var5.deepCopy());
               }
            }

            if (var20 > 0) {
               LarpLog.warn("FakeSpawner", "Retaining {} unmatched pile records for recovery; no inventory was restored", var20);
            }
         } catch (Exception var19) {
            LarpLog.error("FakeSpawner", "Pile load failed; retaining the file for recovery", var19);
         }
      }
   }

   private void savePiles() {
      if (this.saveKey != null) {
         this.writePilesTo(getSavePath());
      }
   }

   private void writePilesTo(Path var1) {
      if (this.saveKey != null) {
         try {
            JsonObject var2 = Files.exists(var1) ? JsonFiles.readJson(var1) : new JsonObject();
            JsonArray var3 = new JsonArray();
            if (var2.has(this.saveKey) && !var2.get(this.saveKey).isJsonArray()) {
               throw new IOException("Unrecognized saved pile data; original file retained");
            }

            if (var2.has("quarantined") && !var2.get("quarantined").isJsonObject()) {
               throw new IOException("Unrecognized pile quarantine data; original file retained");
            }

            JsonObject var4 = var2.has("quarantined") ? var2.getAsJsonObject("quarantined") : new JsonObject();
            if (var4.has(this.saveKey) && !var4.get(this.saveKey).isJsonArray()) {
               throw new IOException("Unrecognized retained pile records; original file retained");
            }

            JsonArray var5 = new JsonArray();

            for (JsonElement var7 : this.quarantinedRecords) {
               var5.add(var7.deepCopy());
            }

            if (!var5.isEmpty()) {
               var4.add(this.saveKey, var5);
               var2.add("quarantined", var4);
            }

            for (FakeSpawnerController.SavedPile var18 : this.savedPiles.values()) {
               var3.add(var18.toJson());
            }

            for (Entry var19 : this.stackCountByPos.entrySet()) {
               long var8 = (Long)var19.getKey();
               if (!this.savedPiles.containsKey(var8) && (Integer)var19.getValue() >= 1) {
                  Deque var10 = this.reservationsByPos.get(var8);
                  if (var10 == null) {
                     var10 = new ArrayDeque();
                  }

                  if ((Integer)var19.getValue() <= 1 || !var10.isEmpty()) {
                     ArrayList var11 = new ArrayList();

                     for (FakeSpawnerController.SlotReservation var13 : var10) {
                        ItemStack var14 = this.reservedStacks.get(var13.preferredSlot);
                        if (var14 == null || var14.isEmpty()) {
                           var11.clear();
                           break;
                        }

                        var11.add(
                           new FakeSpawnerController.BackingSlot(
                              var13.preferredSlot, BuiltInRegistries.ITEM.getKey(var13.stack.getItem()).toString(), var13.stack.getCount(), var14.getCount()
                           )
                        );
                     }

                     if (!var11.isEmpty() || (Integer)var19.getValue() == 1) {
                        HashMap var20 = new HashMap();
                        this.purchasedByPos
                           .getOrDefault(var8, Map.of())
                           .forEach((var1x, var2x) -> var20.put(BuiltInRegistries.ITEM.getKey(var1x).toString(), var2x));
                        var3.add(
                           new FakeSpawnerController.SavedPile(
                                 var8,
                                 this.getKind(BlockPos.of(var8)),
                                 (Integer)var19.getValue(),
                                 this.placedAtByPos.getOrDefault(var8, System.currentTimeMillis()),
                                 var11,
                                 var20
                              )
                              .toJson()
                        );
                     }
                  }
               }
            }

            var2.add(this.saveKey, var3);
            Files.createDirectories(var1.getParent());
            JsonFiles.writeAtomic(var1, var2.toString());
         } catch (Exception var15) {
            LarpLog.error("FakeSpawner", "Pile save failed; previous file retained", var15);
         }
      }
   }

   public void requestRestore() {
      this.needsRestore = (boolean)1;
   }

   private void restorePiles(Minecraft var1) {
      if (this.needsRestore && !this.savedPiles.isEmpty()) {
         this.needsRestore = (boolean)0;
         byte var2 = 0;

         for (FakeSpawnerController.SavedPile var4 : new ArrayList<>(this.savedPiles.values())) {
            BlockPos var5 = BlockPos.of(var4.pos);
            if (var1.level.hasChunkAt(var5)) {
               LevelChunk var6 = var1.level.getChunkAt(var5);
               int var7 = var6.getSectionIndex(var5.getY());
               BlockState var8 = var7 >= 0 && var7 < var6.getSections().length
                  ? var6.getSection(var7).getBlockState(var5.getX() & 15, var5.getY() & 15, var5.getZ() & 15)
                  : Blocks.AIR.defaultBlockState();
               if (this.writePilesTo(var8.getBlock().asItem()) == var4.kind) {
                  HashMap var9 = new HashMap();

                  for (FakeSpawnerController.BackingSlot var11 : var4.backing) {
                     if ((var11.slot < 0 || var11.slot >= 36) && var11.slot != 40
                        || var11.amount < 1
                        || var11.reserved < 1
                        || var11.amount > var11.reserved + 1
                        || var11.reserved > 64
                        || var9.containsKey(var11.slot)
                        || this.isReservedSlot(var11.slot)) {
                        break;
                     }

                     ItemStack var12 = var1.player.getInventory().getItem(var11.slot);
                     if (var12.isEmpty() || var12.getCount() != var11.reserved || !BuiltInRegistries.ITEM.getKey(var12.getItem()).toString().equals(var11.item)
                        )
                      {
                        break;
                     }

                     var9.put(var11.slot, var12.copy());
                  }

                  if (var9.size() == var4.backing.size()) {
                     int var15 = var4.backing.stream().mapToInt(var0 -> Math.min(var0.amount, var0.reserved)).sum();
                     if (var15 == var4.count - 1 || var15 == var4.count) {
                        ArrayDeque var16 = new ArrayDeque();
                        VisualInventory.invalidate();

                        for (FakeSpawnerController.BackingSlot var13 : var4.backing) {
                           ItemStack var14 = (ItemStack)var9.get(var13.slot);
                           this.reservedStacks.put(var13.slot, var14);
                           var16.addLast(new FakeSpawnerController.SlotReservation(var13.slot, var14.copyWithCount(Math.min(var13.amount, var13.reserved))));
                        }

                        this.reservationsByPos.put(var4.pos, var16);
                        this.stackCountByPos.put(var4.pos, var4.count);
                        this.kindByPos.put(var4.pos, var4.kind);
                        this.placedAtByPos.put(var4.pos, var4.placedAt);
                        HashMap var18 = new HashMap();
                        var4.purchased.forEach((var1x, var2x) -> {
                           Identifier var3 = Identifier.tryParse(var1x);
                           if (var3 != null && BuiltInRegistries.ITEM.getValue(var3) != Items.AIR) {
                              var18.put((Item)BuiltInRegistries.ITEM.getValue(var3), var2x);
                           }
                        });
                        if (!var18.isEmpty()) {
                           this.purchasedByPos.put(var4.pos, var18);
                        }

                        this.savedPiles.remove(var4.pos);
                        this.removeSpawnerEntity(var4.pos);
                        this.savePiles(var1.level, var5);
                        this.markBlockDirty(var5);
                        VisualInventory.invalidate();
                        var2 = 1;
                     }
                  }
               }
            }
         }

         if (var2 != 0) {
            this.savePiles();
         }
      }
   }

   private FakeSpawnerController() {
   }

   public static FakeSpawnerController getInstance() {
      return iNSTANCE;
   }

   public boolean isActive() {
      FakeSpawnerModule var1 = FakeSpawnerModule.getInstance();
      return var1 != null && var1.isEnabled() && var1.isRuntimeActive();
   }

   public boolean isFakeSpawnerStack(ItemStack var1) {
      return this.isActive() && this.isActive(var1);
   }

   public boolean isActive(ItemStack var1) {
      return this.hasSilkTouch(var1) && (this.knownStacks.contains(var1) || this.derivedStacks.contains(var1) || ItemStackMarkers.isPreview(var1));
   }

   private boolean hasSilkTouch(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && !AuctionUtil.isAuctionItem(var1) && this.writePilesTo(var1.getItem()) != null;
   }

   private String syncState() {
      FakeSpawnerModule var1 = FakeSpawnerModule.getInstance();
      return var1.getBlock(0) + "|" + var1.getBlock(1) + "|" + var1.getBlock(2);
   }

   public FarmMobType writePilesTo(Item var1) {
      FakeSpawnerModule var2 = FakeSpawnerModule.getInstance();

      for (FarmMobType var6 : FarmMobType.values()) {
         String var7 = var2.getBlock(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1 == resolveFillerItem(var7)) {
            return var6;
         }
      }

      return null;
   }

   public FarmMobType getKind(BlockPos var1) {
      return var1 == null ? FarmMobType.sKELETON : this.kindByPos.getOrDefault(var1.asLong(), FarmMobType.sKELETON);
   }

   private Item getKindItem(long var1) {
      return resolveFillerItem(FakeSpawnerModule.getInstance().getBlock(this.getKind(BlockPos.of(var1)).ordinal()));
   }

   public Item getFillerItem() {
      FakeSpawnerModule var1 = FakeSpawnerModule.getInstance();
      return resolveFillerItem(var1 == null ? null : var1.getFirstBlock());
   }

   public static Item resolveFillerItem(String var0) {
      Identifier var1 = var0 == null ? null : Identifier.tryParse(var0);
      Item var2 = var1 == null ? Items.COBBLESTONE : (Item)BuiltInRegistries.ITEM.getValue(var1);
      return var2 instanceof BlockItem var3
            && var2 != Items.AIR
            && !var3.getBlock().defaultBlockState().isAir()
            && !(var3.getBlock() instanceof EntityBlock)
            && var3.getBlock() != Blocks.SPAWNER
         ? var2
         : Items.COBBLESTONE;
   }

   private Block loadPiles() {
      return this.getFillerItem() instanceof BlockItem var1 ? var1.getBlock() : Blocks.COBBLESTONE;
   }

   public boolean isFakeSpawnerBlock(BlockState var1) {
      return this.isActive() && var1 != null ? this.writePilesTo(var1.getBlock().asItem()) != null || var1.is(Blocks.SPAWNER) : false;
   }

   public void markFinalBreak(BlockPos var1) {
      this.syncState();
      this.finalBreakPositions.add(var1.asLong());
      this.stackCountByPos.remove(var1.asLong());
      this.removeSpawnerEntity(var1.asLong());
      this.markBlockDirty(var1);
   }

   public BlockState getFillerItem(BlockPos var1, BlockState var2) {
      return var1 == null ? var2 : this.readPilesFrom(var1.asLong(), var2);
   }

   public BlockState readPilesFrom(long var1, BlockState var3) {
      if (!this.trackedPositions.contains(var1) || !this.isFakeSpawnerBlock() || var3 == null || this.pendingFinalBreaks.contains(var1)) {
         return var3;
      }

      if (this.silkRejectUntil.containsKey(var1)) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.finalBreakPositions.contains(var1)) {
         return var3;
      }

      if (!this.brokenPositions.contains(var1) || !var3.is(Blocks.SPAWNER) && !this.kindByItem.containsKey(var3.getBlock().asItem())) {
         int var4 = this.stackCountByPos.getOrDefault(var1, 0);
         if (var4 <= 0) {
            return var3;
         } else {
            return !var3.isAir() && !var3.is(Blocks.SPAWNER) && !this.kindByItem.containsKey(var3.getBlock().asItem())
               ? var3
               : Blocks.SPAWNER.defaultBlockState();
         }
      } else {
         return Blocks.AIR.defaultBlockState();
      }
   }

   public boolean isFakeSpawnerBlock() {
      return (!this.stackCountByPos.isEmpty() || !this.brokenPositions.isEmpty() || !this.silkRejectUntil.isEmpty())
         && this.isActive()
         && this.level == Minecraft.getInstance().level;
   }

   private void rebuildPositionSets() {
      LongOpenHashSet var1 = new LongOpenHashSet();

      for (long var3 : this.stackCountByPos.keySet()) {
         var1.add(var3);
      }

      for (long var9 : this.brokenPositions) {
         var1.add(var9);
      }

      for (long var10 : this.silkRejectUntil.keySet()) {
         var1.add(var10);
      }

      this.trackedPositions = LongSets.unmodifiable(var1);
      LongOpenHashSet var8 = new LongOpenHashSet();

      for (long var4 : this.ghostUntilTick.keySet()) {
         var8.add(var4);
      }

      this.ghostPositions = LongSets.unmodifiable(var8);
   }

   public void getInstance(BlockPos var1, BlockState var2) {
      this.syncState();
      if (var1 != null) {
         long var3 = var1.asLong();
         if (this.requestRestore(var3)) {
            this.mineCooldownTick = 0;
            this.markBlockDirty(var1);
            LarpLog.debug("FakeSpawner", "Final break {} at {}; server state={}", var2 != null && var2.isAir() ? "confirmed" : "corrected", var1, var2);
         }

         if (this.finalBreakPositions.contains(var3)) {
            if (var2 != null && !var2.isAir()) {
               return;
            }

            DroppedItemTracker.trackPosition(var1);
            this.finalBreakPositions.remove(var3);
         }

         if (this.brokenPositions.contains(var3)) {
            if (var2 != null && this.isFakeSpawnerBlock(var2)) {
               this.mineCooldownTick = 0;
            }

            this.recentBreaks.put(var3, new FakeSpawnerController.RecentBreak(var1.immutable(), System.currentTimeMillis() + 10000L));
            this.brokenPositions.remove(var3);
            this.markBlockDirty(var1);
         }

         int var5 = this.stackCountByPos.getOrDefault(var3, 0);
         if (var2 != null && !var2.isAir()) {
            if (this.isActive()) {
               if (this.isFakeSpawnerBlock(var2)) {
                  if (var5 <= 0 && !this.pendingPlacements.containsKey(var3)) {
                     return;
                  }

                  FarmMobType var6 = this.writePilesTo(var2.getBlock().asItem());
                  if (var6 != null && this.kindByPos.put(var3, var6) != var6) {
                     this.removeSpawnerEntity(var3);
                  }

                  this.placedAtByPos.putIfAbsent(var3, System.currentTimeMillis());
                  FakeSpawnerController.PendingPlacement var7 = this.pendingPlacements.remove(var3);
                  int var8 = var7 == null ? this.stackCountByPos.getOrDefault(var3, 1) : Math.max(1, var7.stackCount);
                  this.stackCountByPos.put(var3, Math.max(1, var8));
                  this.rebuildPositionSets(
                     "track:" + var3, 1500L, "trackWorldState fake pos={} state={} resolvedCount={} pending={}", var1, var2.getBlock(), var8, var7 != null
                  );
                  if (var7 != null && var7.hiddenStack != null) {
                     this.reconcileInventory(var3, var7.hiddenStack, true);
                  }

                  if (var7 != null) {
                     this.savePiles();
                  }

                  this.savePiles(Minecraft.getInstance() == null ? null : Minecraft.getInstance().level, var1);
                  if (var5 <= 0) {
                     this.markBlockDirty(var1);
                  }
               } else {
                  this.getInstance(var1, null);
               }
            }
         } else {
            if (var5 > 0) {
               this.debugLog("trackWorldState removed pos={} previousCount={} state={}", var1, var5, var2);
            }

            if (var5 > 0 && !this.hasPendingPlacement(var1)) {
               this.writePilesTo(var1);
            } else if (this.hasPendingPlacement(var1)) {
               this.tick(var3, Integer.MAX_VALUE);
               VisualInventory.isActive();
               this.debugLog("Placement rejected at {}; reservations released without inventory replay", var1);
            }

            this.placedAtByPos.remove(var3);
            this.purchasedByPos.remove(var3);
            this.stackCountByPos.remove(var3);
            this.pendingPlacements.remove(var3);
            this.reservationsByPos.remove(var3);
            this.removeSpawnerEntity(var3);
            if (var5 > 0) {
               this.markBlockDirty(var1);
            }

            this.mineProgress.remove(var3);
            this.lastBreakEffectTick.remove(var3);
            this.silkProgress.remove(var3);
            this.loadPiles(var1);
            if (var1.equals(this.openPilePos)) {
               this.openPilePos = null;
            }
         }
      }
   }

   public void isReservedSlot(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.syncState();
      if (this.isActive() && var1 != null && this.isActive(var2)) {
         FakeSpawnerController.SavedPile var5 = this.savedPiles.remove(var1.asLong());
         if (var5 != null) {
            this.quarantinedRecords.add(var5.toJson());
            this.debugLog("New placement at {} archives a superseded saved pile for recovery", var1);
         }

         this.kindByPos.put(var1.asLong(), this.writePilesTo(var2.getItem()));
         ItemStack var6 = var4 ? var2.copy() : ItemStack.EMPTY;
         int var7 = var4 ? var2.getCount() : 1;
         this.pendingPlacements
            .put(
               var1.asLong(),
               new FakeSpawnerController.PendingPlacement(var7, new FakeSpawnerController.SlotReservation(var3, var6), System.currentTimeMillis())
            );
      }
   }

   public void getSavePath(BlockPos var1, BlockState var2) {
      this.syncState();
      FakeSpawnerController.PendingPlacement var3 = this.pendingPlacements.get(var1.asLong());
      if (var3 != null && this.isFakeSpawnerBlock(var2)) {
         this.brokenPositions.remove(var1.asLong());
         this.stackCountByPos.put(var1.asLong(), var3.stackCount);
         this.placedAtByPos.putIfAbsent(var1.asLong(), var3.createdAtMs);
         this.savePiles(this.level, var1);
         this.markBlockDirty(var1);
      }
   }

   public void trackInventoryStacks() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.isActive() && var1.player != null) {
         for (Entry var3 : new ArrayList<>(this.pendingPlacements.entrySet())) {
            if (this.stackCountByPos.containsKey(var3.getKey())) {
               FakeSpawnerController.PendingPlacement var4 = (FakeSpawnerController.PendingPlacement)var3.getValue();
               if (var4.hiddenStack != null && !var4.hiddenStack.stack.isEmpty()) {
                  this.reconcileInventory((Long)var3.getKey(), var4.hiddenStack, true);
               }

               this.stackCountByPos.put((Long)var3.getKey(), var4.stackCount);
            }
         }
      }
   }

   public void reconcileInventory() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread()
         && var1.player != null
         && this.isActive()
         && var1.level == this.level
         && var1.player == this.player
         && !ContainerTransaction.isActive()) {
         this.snapshotInventory(var1.player.getInventory());
         this.pendingRefunds
            .entrySet()
            .removeIf(
               var1x -> {
                  ItemStack var2 = var1.player.getInventory().getItem(var1x.getKey());
                  FakeSpawnerController.PendingRefund var3x = var1x.getValue();
                  return var1.player.tickCount >= var3x.expires
                     || !var2.isEmpty() && !var2.is(var3x.stack.getItem())
                     || var2.is(var3x.stack.getItem()) && var2.getCount() >= var3x.expected;
               }
            );

         for (Entry var3 : this.reservedStacks.entrySet()) {
            if (!this.pendingPlacements
               .values()
               .stream()
               .anyMatch(var1x -> var1x.hiddenStack != null && var1x.hiddenStack.preferredSlot == (Integer)var3.getKey())) {
               ItemStack var4 = var1.player.getInventory().getItem((Integer)var3.getKey());
               if (var4.is(((ItemStack)var3.getValue()).getItem())
                  && var4.getCount() > ((ItemStack)var3.getValue()).getCount()
                  && this.requestRestore((Integer)var3.getKey(), var4.getCount() - ((ItemStack)var3.getValue()).getCount())) {
                  break;
               }
            }
         }
      }
   }

   void snapshotInventory(Container var1) {
      Iterator var2 = this.reservedStacks.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         int var4 = (Integer)var3.getKey();
         ItemStack var5 = var1.getItem(var4);
         ItemStack var6 = (ItemStack)var3.getValue();
         if (var5.is(var6.getItem()) && var5.getCount() >= var6.getCount()) {
            this.onInventoryUpdate(var4, var5);
         } else {
            int var7 = var5.is(var6.getItem()) ? var5.getCount() : 0;
            int var8 = var6.getCount() - var7;
            if (var7 == 0) {
               var2.remove();
            } else {
               var3.setValue(var5.copyWithCount(var7));
            }

            for (Entry var10 : this.reservationsByPos.entrySet()) {
               ArrayDeque var11 = new ArrayDeque();

               for (FakeSpawnerController.SlotReservation var13 : (Deque)var10.getValue()) {
                  int var14 = var13.preferredSlot == var4 ? Math.min(var8, var13.stack.getCount()) : 0;
                  var8 -= var14;
                  if (var14 > 0) {
                     this.stackCountByPos.computeIfPresent((Long)var10.getKey(), (var1x, var2x) -> Math.max(1, var2x - var14));
                  }

                  if (var13.stack.getCount() > var14) {
                     var11.addLast(new FakeSpawnerController.SlotReservation(var13.preferredSlot, var13.stack.copyWithCount(var13.stack.getCount() - var14)));
                  }
               }

               ((Deque)var10.getValue()).clear();
               ((Deque)var10.getValue()).addAll(var11);
            }

            LarpLog.debug(
               "FakeSpawner",
               "Reservation corrected physical={} item={} old={} available={}; real inventory retained",
               var4,
               BuiltInRegistries.ITEM.getKey(var6.getItem()),
               var6.getCount(),
               var7
            );
            VisualInventory.isActive();
         }
      }
   }

   private boolean requestRestore(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = this.restorePiles();
      return var4 >= 0 && var3.player != null
         ? ContainerTransaction.beginInInventory(
            "separate reservation surplus",
            ContainerTransaction.begin(var1, var4, var2, var3.player.getInventory().getItem(var1).getCount()),
            VisualInventory::isActive,
            VisualInventory::isActive
         )
         : false;
   }

   public boolean hasPendingPlacement(BlockPos var1) {
      return var1 != null && this.pendingPlacements.containsKey(var1.asLong());
   }

   public boolean isFakeSpawnerBlock(BlockPos var1) {
      return this.isActive() && this.level == Minecraft.getInstance().level && var1 != null && this.brokenPositions.contains(var1.asLong());
   }

   public void loadPiles(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 != null) {
         this.useHand = var1;
         this.useSlot = var2.getInventory().getSelectedSlot();
         this.useExpiryTick = var2.tickCount + 4;
      }
   }

   public boolean wasUsedRecently(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return this.isActive()
         && var2 == this.player
         && var1 == this.useHand
         && var2.tickCount <= this.useExpiryTick
         && var2.getInventory().getSelectedSlot() == this.useSlot
         && this.isActive(var2.getItemInHand(var1));
   }

   public void restorePiles(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.isActive() && var2.level != null && var2.level.getBlockState(var1).isAir()) {
         if (this.ghostUntilTick.put(var1.asLong(), var2.level.getGameTime() + 10L) == null) {
            this.markBlockDirty(var1);
         }
      }
   }

   public BlockState loadPiles(BlockPos var1, BlockState var2) {
      return this.trackDerivedStack(var1.asLong(), var2);
   }

   public BlockState trackDerivedStack(long var1, BlockState var3) {
      return this.ghostPositions.contains(var1) && var3.isAir() && this.isActive() && this.level == Minecraft.getInstance().level
         ? Blocks.SPAWNER.defaultBlockState()
         : var3;
   }

   public boolean reconcileInventory(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.syncState();
      if (this.isActive()
         && var1 != null
         && !this.hasPendingPlacement(var1)
         && this.tick(var1) > 0
         && this.isActive(var2)
         && this.writePilesTo(var2.getItem()) == this.getKind(var1)
         && !this.isReservedSlot(var3)
         && !ContainerTransaction.isActive()) {
         long var5 = var1.asLong();
         int var7 = var4 ? var2.getCount() : 1;
         if (var7 < var2.getCount()) {
            int var8 = this.restorePiles();
            if (var8 < 0) {
               return false;
            }

            Item var9 = var2.getItem();
            return ContainerTransaction.beginInInventory("single pile addition", ContainerTransaction.begin(var3, var8, var7, var2.getCount()), () -> {
               Minecraft var6 = Minecraft.getInstance();
               ItemStack var7x = var6.player.getInventory().getItem(var8);
               if (this.stackCountByPos.containsKey(var5) && var7x.is(var9) && var7x.getCount() == var7) {
                  this.isFakeBlockEntity(var5, var8, var7x, var7);
               }
            }, VisualInventory::isActive);
         } else {
            return this.isFakeBlockEntity(var5, var3, var2, var7);
         }
      } else {
         return false;
      }
   }

   private boolean isFakeBlockEntity(long var1, int var3, ItemStack var4, int var5) {
      if (!this.reconcileInventory(var1, new FakeSpawnerController.SlotReservation(var3, var4.copy()), false)) {
         return false;
      }

      int var6 = (int)Math.min(2147483647L, (long)this.stackCountByPos.getOrDefault(var1, 1).intValue() + var5);
      this.stackCountByPos.put(var1, var6);
      this.placedAtByPos.putIfAbsent(var1, System.currentTimeMillis());
      this.tickBreakAttempts(Minecraft.getInstance().level, BlockPos.of(var1));
      this.reset("You just added another spawner! There are now " + var6 + " spawners in the stack.");
      this.savePiles();
      return true;
   }

   public boolean hasSilkTouch() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null && var1.getMainHandItem().is(ItemTags.PICKAXES)) {
         ItemEnchantments var2 = (ItemEnchantments)var1.getMainHandItem().get(DataComponents.ENCHANTMENTS);
         Reference var3 = var1.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
         return var2 != null && var2.getLevel(var3) > 0;
      } else {
         return false;
      }
   }

   public boolean tryBreakWithoutSilk(BlockPos var1, Direction var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.isFakeSpawnerAt(var1)) {
         return false;
      }

      long var4 = var1.asLong();
      if (this.silkRejectUntil.containsKey(var4)) {
         return true;
      }

      if (this.hasSilkTouch()) {
         this.silkStartTick.remove(var4);
         this.silkProgress.remove(var4);
         this.silkBlockedPositions.remove(var4);
         return false;
      }

      if (this.silkBlockedPositions.contains(var4)) {
         return true;
      }

      int var6 = var3.player.tickCount;
      long var7 = var1.asLong();
      if (this.lastMineTick.getOrDefault(var7, -1) == var6) {
         return true;
      }

      this.lastMineTick.put(var7, var6);
      this.silkStartTick.computeIfAbsent(var4, var1x -> var6);
      double var9 = var3.player.getAbilities().instabuild ? 1.0 : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
      double var11 = Math.min(1.0, this.silkProgress.getOrDefault(var4, 0.0) + var9);
      this.silkProgress.put(var4, var11);
      this.restorePiles(var3, var1, var2, var11);
      if (var11 >= 1.0) {
         this.silkStartTick.remove(var4);
         this.silkProgress.remove(var4);
         this.silkBlockedPositions.add(var4);
         this.silkRejectUntil.put(var4, var6 + 4);
         this.loadPiles(var1);
         this.getSavePath(var1, var2);
         this.openLootMenu(var1, true);
         this.markBlockDirty(var1);
      }

      return true;
   }

   private void tickBreakAttempts() {
      Minecraft var1 = Minecraft.getInstance();

      for (Entry var3 : new ArrayList<>(this.silkRejectUntil.entrySet())) {
         if (var1.player.tickCount >= (Integer)var3.getValue()) {
            this.silkRejectUntil.remove(var3.getKey());
            this.markBlockDirty(BlockPos.of((Long)var3.getKey()));
            this.reset("You cannot break spawners without a silk touch!");
         }
      }

      if (!var1.options.keyAttack.isDown()) {
         for (long var6 : this.silkStartTick.keySet()) {
            this.loadPiles(BlockPos.of(var6));
         }

         this.silkStartTick.clear();
         this.silkProgress.clear();
         this.silkBlockedPositions.clear();
      } else {
         this.silkStartTick.keySet().removeIf(var2 -> {
            int var3x = var1.hitResult instanceof BlockHitResult var4 && var4.getBlockPos().asLong() == var2 ? 0 : 1;
            if (var3x != 0) {
               this.loadPiles(BlockPos.of(var2));
            }

            return (boolean)var3x;
         });
      }
   }

   public boolean buildSaveKey(BlockPos var1) {
      if (this.tryBreakWithoutSilk(var1, null)) {
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      if (this.isActive() && var2 != null && var2.level != null && var2.player != null && var1 != null) {
         if (this.isFakeSpawnerBlock(var2.level.getBlockState(var1)) && this.tick(var1) > this.recordRenderCounts()) {
            long var3 = var1.asLong();
            this.mineProgress.putIfAbsent(var3, 0.0F);
            this.restorePiles(var2, var1, null, 0.0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean loadPiles(BlockPos var1, Direction var2) {
      if (this.tryBreakWithoutSilk(var1, var2)) {
         return true;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.isActive() && var3 != null && var3.level != null && var3.player != null && var1 != null) {
         if (this.isFakeSpawnerAt(var1) && var3.player.tickCount < this.mineCooldownTick) {
            return true;
         }

         if (this.isFakeSpawnerBlock(var3.level.getBlockState(var1)) && this.tick(var1) > this.recordRenderCounts()) {
            long var4 = var1.asLong();
            int var6 = var3.player.tickCount;
            if (this.lastMineTick.getOrDefault(var4, -1) == var6) {
               return true;
            }

            this.lastMineTick.put(var4, var6);
            float var7 = var3.player.getAbilities().instabuild ? 1.0F : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
            float var8 = Math.min(1.0F, this.mineProgress.getOrDefault(var4, 0.0F) + var7);
            this.mineProgress.put(var4, var8);
            this.restorePiles(var3, var1, var2, var8);
            if (var8 >= 1.0F) {
               if (this.removeSpawners(var1, this.recordRenderCounts())) {
                  this.mineCooldownTick = var6 + 5;
                  this.openLootMenu(var1, true);
               }

               this.mineProgress.remove(var4);
               this.lastBreakEffectTick.remove(var4);
               this.loadPiles(var1);
            }

            return true;
         } else {
            if (var1 != null) {
               this.mineProgress.remove(var1.asLong());
               this.lastBreakEffectTick.remove(var1.asLong());
               this.loadPiles(var1);
            }

            return false;
         }
      } else {
         if (var1 != null) {
            this.mineProgress.remove(var1.asLong());
            this.lastBreakEffectTick.remove(var1.asLong());
            this.loadPiles(var1);
         }

         return false;
      }
   }

   public void writePilesTo(BlockPos var1) {
      if (var1 != null && this.finalBreakPositions.contains(var1.asLong())) {
         DroppedItemTracker.trackPosition(var1);
      }

      if (var1 != null && this.brokenPositions.contains(var1.asLong())) {
         this.recentBreaks.put(var1.asLong(), new FakeSpawnerController.RecentBreak(var1.immutable(), System.currentTimeMillis() + 10000L));
      }

      if (var1 != null && this.tick(var1) > 0) {
         this.recentBreaks.put(var1.asLong(), new FakeSpawnerController.RecentBreak(var1.immutable(), System.currentTimeMillis() + 10000L));
         int var2 = this.stackCountByPos.getOrDefault(var1.asLong(), 0);
         FakeSpawnerController.SlotReservation var3 = this.tick(var1.asLong(), var2);
         this.removeFromPile(var1.asLong(), var2);
         this.trackDerivedStack(new ItemStack(this.getKindItem(var1.asLong()), 1), 1);
         VisualInventory.isActive();
         this.mineProgress.remove(var1.asLong());
         this.lastBreakEffectTick.remove(var1.asLong());
         this.loadPiles(var1);
      }
   }

   private int recordRenderCounts() {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player == null || !var1.player.isShiftKeyDown() && !var1.options.keyShift.isDown() ? 1 : 64;
   }

   public boolean breakWholeStack(BlockPos var1) {
      return this.removeSpawners(var1, 64);
   }

   private boolean removeSpawners(BlockPos var1, int var2) {
      if (this.hasPendingPlacement(var1)) {
         return false;
      }

      if (this.isFakeSpawnerAt(var1)) {
         long var3 = var1.asLong();
         int var5 = this.stackCountByPos.getOrDefault(var3, 0);
         if (var5 <= 0) {
            return false;
         }

         int var6 = Math.min(var2, var5);
         Deque var7 = this.reservationsByPos.get(var3);
         if (var7 != null && !var7.isEmpty()) {
            ItemStack var8 = Minecraft.getInstance().player.getInventory().getItem(((FakeSpawnerController.SlotReservation)var7.getLast()).preferredSlot);
            int var9 = 0;

            for (FakeSpawnerController.SlotReservation var11 : var7.reversed()) {
               if (!ItemStack.isSameItemSameComponents(var8, Minecraft.getInstance().player.getInventory().getItem(var11.preferredSlot))) {
                  break;
               }

               var9 += var11.stack.getCount();
            }

            if (var9 < var6 && var9 < var5 - 1) {
               var6 = var9;
            }
         }

         if (!this.getInstance(var3, var6)) {
            return false;
         }

         this.removeFromPile(var3, var6);
         return true;
      } else {
         return false;
      }
   }

   private void removeFromPile(long var1, int var3) {
      int var4 = Math.max(0, this.stackCountByPos.getOrDefault(var1, 0) - var3);
      BlockPos var5 = BlockPos.of(var1);
      if (var4 > 0) {
         this.stackCountByPos.put(var1, var4);
         this.placedAtByPos.put(var1, System.currentTimeMillis());
      } else {
         this.brokenPositions.add(var1);
         this.stackCountByPos.remove(var1);
         this.pendingPlacements.remove(var1);
         this.placedAtByPos.remove(var1);
         this.purchasedByPos.remove(var1);
         this.reservationsByPos.remove(var1);
         this.removeSpawnerEntity(var1);
         this.markBlockDirty(var5);
         if (var5.equals(this.openPilePos)) {
            this.openPilePos = null;
         }
      }

      this.recentBreaks.put(var1, new FakeSpawnerController.RecentBreak(var5.immutable(), System.currentTimeMillis() + 10000L));
      this.savePiles();
   }

   public boolean isFakeSpawnerAt(BlockPos var1) {
      if (var1 == null) {
         return false;
      }

      long var2 = var1.asLong();
      return this.stackCountByPos.getOrDefault(var2, 0) > 0
         && !this.pendingFinalBreaks.contains(var2)
         && this.level == Minecraft.getInstance().level
         && this.isActive();
   }

   public FakeSpawnerController.BlockSound markBlockDirty(BlockPos var1, SoundEvent var2, float var3, float var4) {
      if (var1 != null && var2 != null && this.isFakeSpawnerAt(var1)) {
         BlockState var5 = ((BlockItem)this.getKindItem(var1.asLong())).getBlock().defaultBlockState();
         if (var5 == null) {
            return null;
         } else {
            SoundType var6 = var5.getSoundType();
            SoundType var7 = Blocks.SPAWNER.defaultBlockState().getSoundType();
            if (loadPiles(var2, var6.getPlaceSound())) {
               return new FakeSpawnerController.BlockSound(var7.getPlaceSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else if (loadPiles(var2, var6.getBreakSound())) {
               return new FakeSpawnerController.BlockSound(var7.getBreakSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else {
               return loadPiles(var2, var6.getHitSound())
                  ? new FakeSpawnerController.BlockSound(var7.getHitSound(), (var7.getVolume() + 1.0F) / 8.0F, var7.getPitch() * 0.5F)
                  : null;
            }
         }
      } else {
         return null;
      }
   }

   private static boolean loadPiles(SoundEvent var0, SoundEvent var1) {
      return var0 == var1 || var0.location().equals(var1.location());
   }

   private void tickBreakAttempts(Level var1, BlockPos var2) {
      if (var1 != null && var2 != null && this.isFakeSpawnerAt(var2)) {
         SoundType var3 = Blocks.SPAWNER.defaultBlockState().getSoundType();
         var1.playLocalSound(
            var2.getX() + 0.5,
            var2.getY() + 0.5,
            var2.getZ() + 0.5,
            var3.getPlaceSound(),
            SoundSource.BLOCKS,
            (var3.getVolume() + 1.0F) / 2.0F,
            var3.getPitch() * 0.8F,
            false
         );
      }
   }

   private void openLootMenu(BlockPos var1, boolean var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null && this.isFakeSpawnerAt(var1)) {
         SoundType var4 = Blocks.SPAWNER.defaultBlockState().getSoundType();
         var3.level
            .playLocalSound(
               var1.getX() + 0.5,
               var1.getY() + 0.5,
               var1.getZ() + 0.5,
               var2 ? var4.getBreakSound() : var4.getHitSound(),
               SoundSource.BLOCKS,
               (var4.getVolume() + 1.0F) / (var2 ? 2.0F : 8.0F),
               var4.getPitch() * (var2 ? 0.8F : 0.5F),
               false
            );
      }
   }

   public BlockState getInstance(BlockPos var1, BlockState var2) {
      return this.getFillerItem(var1, var2);
   }

   public SpawnerBlockEntity savePiles(Level var1, BlockPos var2) {
      Map var3 = this.spawnerEntities;
      if (var1 != null && var1 == this.level && this.isFakeSpawnerAt(var2) && !this.silkRejectUntil.containsKey(var2.asLong())) {
         long var4 = var2.asLong();
         SpawnerBlockEntity var6 = (SpawnerBlockEntity)var3.get(var4);
         if (var6 == null && Minecraft.getInstance().isSameThread()) {
            var6 = new SpawnerBlockEntity(var2.immutable(), Blocks.SPAWNER.defaultBlockState());
            var6.setEntityId(this.getKind(var2).entityType, var1.getRandom());
            var6.setLevel(var1);
            var6.getSpawner().getOrCreateDisplayEntity(var1, var2);
            var3.put(var4, var6);
            ((List)this.spawnersByChunk.computeIfAbsent(ChunkPos.pack(var2.getX() >> 4, var2.getZ() >> 4), var0 -> new ArrayList())).add(var6);
         }

         return var3 == this.spawnerEntities && var1 == this.level && this.isFakeSpawnerAt(var2) ? var6 : null;
      } else {
         return null;
      }
   }

   private void markBlockDirty(BlockPos var1) {
      this.rebuildPositionSets();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var2.level == this.level) {
         this.terrainInvalidations++;
         var2.levelExtractor.setBlocksDirty(var1.getX() - 1, var1.getY() - 1, var1.getZ() - 1, var1.getX() + 1, var1.getY() + 1, var1.getZ() + 1);
      }
   }

   public Collection<SpawnerBlockEntity> getSavePath(Level var1) {
      return var1 == this.level && this.isActive() ? this.spawnerEntities.values() : List.of();
   }

   private void removeSpawnerEntity(long var1) {
      SpawnerBlockEntity var3 = this.spawnerEntities.remove(var1);
      if (var3 != null) {
         BlockPos var4 = var3.getBlockPos();
         long var5 = ChunkPos.pack(var4.getX() >> 4, var4.getZ() >> 4);
         List var7 = (List)this.spawnersByChunk.get(var5);
         if (var7 != null) {
            var7.remove(var3);
            if (var7.isEmpty()) {
               this.spawnersByChunk.remove(var5);
            }
         }
      }
   }

   public Collection<SpawnerBlockEntity> getInstance(Level var1, Vec3 var2, int var3) {
      if (var1 == this.level && this.isActive() && !this.spawnersByChunk.isEmpty()) {
         ArrayList var4 = null;
         int var5 = Mth.floor(var2.x - var3) >> 4;
         int var6 = Mth.floor(var2.x + var3) >> 4;
         int var7 = Mth.floor(var2.z - var3) >> 4;
         int var8 = Mth.floor(var2.z + var3) >> 4;

         for (int var9 = var5; var9 <= var6; var9++) {
            for (int var10 = var7; var10 <= var8; var10++) {
               List var11 = (List)this.spawnersByChunk.get(ChunkPos.pack(var9, var10));
               if (var11 != null) {
                  if (var4 == null) {
                     var4 = new ArrayList();
                  }

                  var4.addAll(var11);
               }
            }
         }

         return var4 == null ? List.of() : var4;
      } else {
         return List.of();
      }
   }

   public boolean isFakeBlockEntity(BlockEntity var1) {
      if (var1 instanceof SpawnerBlockEntity var2) {
         BlockPos var3 = var2.getBlockPos();
         if (var3 == null) {
            return false;
         }

         SpawnerBlockEntity var4 = this.spawnerEntities.get(var3.asLong());
         return var4 == var1;
      } else {
         return false;
      }
   }

   public void hasPendingPlacement(String var1, BlockPos var2, BlockEntity var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 != null) {
            this.rebuildPositionSets(
               "request:" + var1 + ":" + var2.asLong(),
               1500L,
               "{} requested fake block entity pos={} result={} count={}",
               var1,
               var2,
               var3 != null ? var3.getClass().getSimpleName() : "null",
               this.tick(var2)
            );
         }
      }
   }

   public void isFakeSpawnerAt(String var1, BlockPos var2, String var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 == null) {
            this.rebuildPositionSets("render:" + var1, 1500L, "renderer {} {}", var1, var3);
         } else {
            this.rebuildPositionSets("render:" + var1 + ":" + var2.asLong(), 1500L, "renderer {} pos={} count={} {}", var1, var2, this.tick(var2), var3);
         }
      }
   }

   public void isFakeSpawnerStack(LevelChunk var1, Collection<BlockPos> var2) {
      this.syncState();
      this.requestRestore();
      if (this.isActive() && var1 != null) {
         int var3 = var1.getPos().x();
         int var4 = var1.getPos().z();
         int var5 = var3 << 4;
         int var6 = var4 << 4;
         int var7 = var5 + 15;
         int var8 = var6 + 15;
         MutableBlockPos var9 = new MutableBlockPos();
         HashSet var10 = new HashSet<>(this.stackCountByPos.keySet());
         var10.addAll(this.brokenPositions);

         for (Long var12 : var10) {
            BlockPos var13 = BlockPos.of(var12);
            int var14 = var13.getX();
            int var15 = var13.getZ();
            if (var14 >= var5 && var14 <= var7 && var15 >= var6 && var15 <= var8) {
               var9.set(var14, var13.getY(), var15);
               if (!var1.getLevel().isOutsideBuildHeight(var9)) {
                  this.getInstance(var9, var1.getSection(var1.getSectionIndex(var9.getY())).getBlockState(var9.getX() & 15, var9.getY() & 15, var9.getZ() & 15));
               }
            }
         }

         if (var2 != null) {
            for (BlockPos var17 : new ArrayList(var2)) {
               if (var17 != null) {
                  this.getInstance(var17, var1.getBlockState(var17));
               }
            }
         }
      }
   }

   public void tick() {
      long var1 = Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
      this.syncState();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var3.player != null && this.isActive()) {
         this.restorePiles(var3);
         this.snapshotInventory();

         for (Entry var5 : this.ghostUntilTick.entrySet()) {
            if (var3.level.getGameTime() >= (Long)var5.getValue()) {
               this.ghostUntilTick.remove(var5.getKey());
               this.markBlockDirty(BlockPos.of((Long)var5.getKey()));
            }
         }

         this.tickBreakAttempts();
         if (!this.recentBreaks.isEmpty() || !this.mineProgress.isEmpty() || !this.brokenPositions.isEmpty()) {
            for (Entity var6 : var3.level.entitiesForRendering()) {
               if (var6 instanceof ItemEntity var8) {
                  this.isFakeSpawnerBlock(var8);
               }
            }
         }

         this.markFinalBreak();
         this.recentBreaks.entrySet().removeIf(var0 -> var0.getValue().untilMs < System.currentTimeMillis());
         this.trackedDropIds.removeIf(var1x -> !(var3.level.getEntity(var1x) instanceof ItemEntity));
         ContainerTransaction.tick();
         this.reconcileInventory();

         for (Entry var17 : new ArrayList<>(this.pendingPlacements.entrySet())) {
            if (!this.stackCountByPos.containsKey(var17.getKey())
               && System.currentTimeMillis() - ((FakeSpawnerController.PendingPlacement)var17.getValue()).createdAtMs > 5000L) {
               this.getInstance(BlockPos.of((Long)var17.getKey()), null);
            }
         }

         if (!var3.options.keyAttack.isDown()) {
            for (Long var18 : new ArrayList<>(this.mineProgress.keySet())) {
               this.loadPiles(BlockPos.of(var18));
            }

            this.mineProgress.clear();
            this.lastBreakEffectTick.clear();
         }

         for (Long var19 : new ArrayList<>(this.mineProgress.keySet())) {
            if (!(var3.hitResult instanceof BlockHitResult var22 && var22.getBlockPos().equals(BlockPos.of(var19)))) {
               this.loadPiles(BlockPos.of(var19));
               this.mineProgress.remove(var19);
               this.lastBreakEffectTick.remove(var19);
            }
         }

         if (var3 != null && var3.level != null && var3.player != null && this.isActive()) {
            for (BlockEntity var21 : this.getInstance(var3.level, var3.player.position(), 32)) {
               if (var21 instanceof SpawnerBlockEntity var23
                  && var23.getBlockPos() != null
                  && var3.level.hasChunkAt(var23.getBlockPos())
                  && this.isFakeSpawnerAt(var23.getBlockPos())
                  && var23.getBlockPos().distToCenterSqr(var3.player.position()) <= 1024.0) {
                  SpawnerBlockEntity.clientTick(var3.level, var23.getBlockPos(), var23.getBlockState(), var23);
               }
            }

            long var14 = var3.level.getGameTime();

            for (BlockPos var26 : this.tick(var3.player.blockPosition(), 16, 8)) {
               if ((var14 + Math.floorMod(var26.asLong(), 3L)) % 20L == 0L) {
                  this.tick(var3.level, var26);
               }
            }
         } else {
            for (Long var20 : new ArrayList<>(this.mineProgress.keySet())) {
               this.loadPiles(BlockPos.of(var20));
            }

            this.mineProgress.clear();
            this.lastBreakEffectTick.clear();
         }

         if (var1 != 0L) {
            long var15 = System.nanoTime() - var1;
            long var25 = System.currentTimeMillis();
            if (var25 >= this.nextReportAt) {
               this.nextReportAt = var25 + 5000L;
               this.debugLog(
                  "Work counters: piles={} reservations={} mappingRebuilds={} terrainInvalidations={} inventoryUpdates={} renderCandidates={} renderExtractions={} displayCopies={} tickMicros={}",
                  this.stackCountByPos.size(),
                  this.reservedStacks.size(),
                  VisualInventory.getSnapshotVersion(),
                  this.terrainInvalidations,
                  this.inventoryUpdates,
                  this.renderCandidates,
                  this.renderExtractions,
                  this.displayCopies,
                  var15 / 1000L
               );
            }

            if (var15 > 5000000L) {
               this.rebuildPositionSets(
                  "tick-spike",
                  2000L,
                  "Client tick CPU spike micros={} piles={} reservations={}",
                  var15 / 1000L,
                  this.stackCountByPos.size(),
                  this.reservedStacks.size()
               );
            }
         }
      } else {
         this.reset();
      }
   }

   public int tick(BlockPos var1) {
      return !this.isFakeSpawnerAt(var1) ? 0 : this.stackCountByPos.getOrDefault(var1.asLong(), 0);
   }

   public List<BlockPos> tick(BlockPos var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();
      if (this.isActive() && var1 != null && !this.stackCountByPos.isEmpty()) {
         int var5 = var1.getX();
         int var6 = var1.getY();
         int var7 = var1.getZ();

         for (SpawnerBlockEntity var9 : this.getInstance(this.level, Vec3.atCenterOf(var1), var2 + 1)) {
            BlockPos var10 = var9.getBlockPos();
            if (this.isFakeSpawnerAt(var10)
               && Math.abs(var10.getX() - var5) <= var2
               && Math.abs(var10.getY() - var6) <= var3
               && Math.abs(var10.getZ() - var7) <= var2) {
               var4.add(var10);
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   public boolean isFakeSpawnerBlock(ItemEntity var1) {
      if (this.isActive() && var1 != null && this.hasSilkTouch(var1.getItem()) && !ItemStackMarkers.isRealPurchase(var1.getItem())) {
         long var2 = System.currentTimeMillis();
         if (this.trackedDropIds.contains(var1.getId())) {
            return true;
         }

         HashSet var4 = new HashSet<>(this.brokenPositions);
         var4.addAll(this.mineProgress.keySet());

         for (long var6 : var4) {
            BlockPos var8 = BlockPos.of(var6);
            if (var1.distanceToSqr(var8.getX() + 0.5, var8.getY() + 0.5, var8.getZ() + 0.5) <= 4.84) {
               this.trackedDropIds.add(var1.getId());
               return true;
            }
         }

         this.recentBreaks.entrySet().removeIf(var2x -> var2x.getValue().untilMs < var2);

         for (FakeSpawnerController.RecentBreak var14 : this.recentBreaks.values()) {
            double var7 = var14.pos.getX() + 0.5 - var1.getX();
            double var9 = var14.pos.getY() + 0.5 - var1.getY();
            double var11 = var14.pos.getZ() + 0.5 - var1.getZ();
            if (var7 * var7 + var9 * var9 + var11 * var11 <= 4.840000000000001) {
               this.trackedDropIds.add(var1.getId());
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean isActive(ItemEntity var1) {
      return this.isFakeSpawnerBlock(var1);
   }

   public ItemStack removeSpawnerEntity(ItemStack var1) {
      if (!this.isFakeSpawnerStack(var1)) {
         return var1;
      }

      ItemStack var2 = new ItemStack(Items.SPAWNER, var1.getCount());
      var2.set(DataComponents.CUSTOM_NAME, this.getStackName(var1));
      var2.set(DataComponents.LORE, new ItemLore(List.of(syncState(this.writePilesTo(var1.getItem()).displayName, ChatFormatting.YELLOW))));
      if (this.tick()) {
         var2.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
      }

      return var2;
   }

   public ItemStack savePiles(ItemStack var1) {
      return var1;
   }

   public boolean recordRenderCounts(ItemStack var1) {
      return this.isActive()
         && var1 != null
         && !var1.isEmpty()
         && !AuctionUtil.isAuctionItem(var1)
         && this.tickBreakAttempts(var1)
         && this.reconcileInventory(var1) > 0L;
   }

   public Component savePiles(long var1) {
      return syncState("~", ChatFormatting.DARK_GRAY).append(getSavePath("$ ", 65280)).append(syncState(this.markFinalBreak(var1), ChatFormatting.WHITE));
   }

   public Component getSavePath(ItemStack var1) {
      return this.tickBreakAttempts(this.loadPiles(var1));
   }

   public Component restorePiles(ItemStack var1) {
      long var2 = this.reconcileInventory(var1);
      long var4 = Math.max(1L, (var2 + 63L) / 64L);
      return syncState("Contains: " + var4 + " stack(s)", ChatFormatting.WHITE);
   }

   public Component getFillerItem() {
      return syncState("Left Shift", ChatFormatting.GOLD).append(syncState(": ", ChatFormatting.GOLD)).append(syncState("view contents", ChatFormatting.WHITE));
   }

   public Component tick() {
      return syncState("Left Alt + Left Shift", ChatFormatting.GOLD)
         .append(syncState(": ", ChatFormatting.GOLD))
         .append(syncState("view contents", ChatFormatting.WHITE));
   }

   public Component recordRenderCounts() {
      return syncState("Left Control", ChatFormatting.GOLD)
         .append(syncState(": ", ChatFormatting.GOLD))
         .append(syncState("lock tooltip", ChatFormatting.WHITE));
   }

   public List<Component> removeFromPile(ItemStack var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1.getHoverName());
      var3.add(this.getSavePath(var1));
      if (!var2) {
         return var3;
      } else {
         boolean var4 = this.syncState(341);
         boolean var5 = this.syncState(340);
         boolean var6 = this.syncState(342);
         if (var4) {
            return var3;
         } else if (var5 && var6) {
            var3.add(this.recordRenderCounts());
            return var3;
         } else if (var5) {
            var3.add(this.restorePiles(var1));
            var3.add(this.tick());
            var3.add(this.recordRenderCounts());
            return var3;
         } else {
            var3.add(this.restorePiles(var1));
            var3.add(this.getFillerItem());
            return var3;
         }
      }
   }

   public Component debugLog() {
      FakeSpawnerModule var1 = FakeSpawnerModule.getInstance();
      String var2 = var1 != null ? var1.getUnitPrice() : "§dSpawner";
      return trackInventoryStacks(var2);
   }

   public Component getStackName(ItemStack var1) {
      Component var2 = (Component)var1.get(DataComponents.CUSTOM_NAME);
      if (var2 != null) {
         return Component.literal(var2.getString()).withStyle(new ChatFormatting[]{ChatFormatting.WHITE, ChatFormatting.ITALIC});
      }

      FarmMobType var3 = this.writePilesTo(var1.getItem());
      return syncState("Spawner", ChatFormatting.LIGHT_PURPLE);
   }

   public List<Component> isReservedSlot(int var1, boolean var2, boolean var3) {
      return this.isReservedSlot(new ItemStack(this.getFillerItem(), var1), var2, var3);
   }

   public List<Component> isReservedSlot(ItemStack var1, boolean var2, boolean var3) {
      int var4 = var1.getCount();
      ArrayList var5 = new ArrayList();
      var5.add(syncState(this.writePilesTo(var1.getItem()).displayName, ChatFormatting.YELLOW));
      if (var2) {
         var5.add(
            syncState("~", ChatFormatting.DARK_GRAY).append(getSavePath("$ ", 65280)).append(syncState(this.isFakeBlockEntity(var4), ChatFormatting.WHITE))
         );
      }

      var5.add(Component.empty());
      var5.add(syncState("Interact with Spawn Egg:", ChatFormatting.GRAY));
      var5.add(syncState(" Sets Mob Type", ChatFormatting.BLUE));
      if (var3) {
         var5.add(syncState("minecraft:spawner", ChatFormatting.DARK_GRAY));
         var5.add(syncState("13 component(s)", ChatFormatting.DARK_GRAY));
      }

      return var5;
   }

   public boolean markFinalBreak() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 == null) {
         return false;
      } else if (var1.gui.screen() instanceof AbstractContainerScreen var2) {
         Component var4 = var2.getTitle();
         return var4 == null ? false : var4.getString().toLowerCase(Locale.ROOT).contains("shulker");
      } else {
         return false;
      }
   }

   public boolean tick() {
      FakeSpawnerModule var1 = FakeSpawnerModule.getInstance();
      return var1 != null && var1.isEnchantGlint();
   }

   public void writePilesTo() {
      this.openLootMenu(this.openPilePos);
   }

   public void openLootMenu(BlockPos var1) {
      this.syncState();
      Minecraft var2 = Minecraft.getInstance();
      if (this.isActive() && var2 != null && var2.level != null && var2.player != null) {
         this.openPilePos = var1 == null ? null : var1.immutable();
         long var3 = this.recordRenderCounts(var1);
         int var5 = this.restorePiles(var1);
         FarmMobType var6 = this.getKind(var1);
         FakeSpawnerController.DropAccrual var7 = this.rebuildPositionSets(var3, var5, var6);
         Map var8 = var1 == null ? Map.of() : this.purchasedByPos.getOrDefault(var1.asLong(), Map.of());
         var7 = new FakeSpawnerController.DropAccrual(
            Math.max(0, var7.primaryCount - var8.getOrDefault(var6.primaryDrop, 0) - FakeLootWithdrawer.countFakeItems(var6.primaryDrop)),
            Math.max(0, var7.secondaryCount - var8.getOrDefault(var6.secondaryDrop, 0) - FakeLootWithdrawer.countFakeItems(var6.secondaryDrop)),
            var7.primaryRate,
            var7.secondaryRate
         );
         String var9 = var5 + " " + tryBreakWithoutSilk(var6.displayName.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"));
         SimpleContainer var10 = new SimpleContainer(54);
         int var11 = 0;
         var11 = getKindItem(var10, var11, var6.primaryDrop, var7.primaryCount);
         getKindItem(var10, var11, var6.secondaryDrop, var7.secondaryCount);
         var10.setItem(
            48,
            getUnreservedCount(
               Items.GOLD_INGOT.getDefaultInstance(),
               syncState("Sell All", ChatFormatting.WHITE),
               List.of(syncState("Click to sell all loot", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         var10.setItem(
            49,
            getUnreservedCount(
               var6.createIcon(),
               tickBreakAttempts(var5 + " " + var6.displayName.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"), 64648),
               this.debugLog(var7, var6)
            )
         );
         var10.setItem(
            50,
            getUnreservedCount(
               Items.DROPPER.getDefaultInstance(),
               syncState("Drop Loot", ChatFormatting.WHITE),
               List.of(syncState("Click to drop all loot on the page", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         ChestMenu var12 = new ChestMenu(MenuType.GENERIC_9x6, 0, var2.player.getInventory(), var10, 6);
         var2.gui.setScreen(new FakeSpawnerScreen(var12, var2.player.getInventory(), Component.literal(var9), var1));
      }
   }

   public void savePiles(BlockPos var1, Item var2, int var3) {
      if (var1 != null && var2 != null && var3 > 0) {
         this.purchasedByPos.computeIfAbsent(var1.asLong(), var0 -> new HashMap<>()).merge(var2, var3, Integer::sum);
      }
   }

   public long loadPiles(ItemStack var1) {
      if (!this.isActive() || var1.isEmpty() || AuctionUtil.isAuctionItem(var1)) {
         return 0L;
      }

      if (this.hasSilkTouch(var1)) {
         return var1.getCount() * 1000L;
      }

      if (FarmMobType.isDropItem(var1.getItem())) {
         return this.hasPendingPlacement(var1.getItem(), var1.getCount());
      }

      ItemContainerContents var2 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
      return var2 == null ? 0L : var2.nonEmptyItemCopyStream().mapToLong(this::loadPiles).sum() * var1.getCount();
   }

   public Component tickBreakAttempts(long var1) {
      double var3 = var1 >= 1000000000L ? 1.0E9 : (var1 >= 1000000L ? 1000000.0 : (var1 >= 1000L ? 1000.0 : 1.0));
      String var5 = var3 == 1.0E9 ? "B" : (var3 == 1000000.0 ? "M" : (var3 == 1000.0 ? "K" : ""));
      return syncState("~", ChatFormatting.DARK_GRAY)
         .append(getSavePath("$ ", 65280))
         .append(syncState(removeFromPile(var1 / var3, var3 == 1.0 ? 0 : 1) + var5, ChatFormatting.WHITE));
   }

   public long snapshotInventory(int var1) {
      return this.hasPendingPlacement(Items.BONE, var1);
   }

   public long hasPendingPlacement(Item var1, int var2) {
      Double var3 = FakeSpawnerModule.getInstance().getUnitPrice(BuiltInRegistries.ITEM.getKey(var1).toString());
      if (var3 != null) {
         return Math.round(var3 * Math.max(0, var2));
      }

      String var4 = BuiltInRegistries.ITEM.getKey(var1).getPath().replace('_', ' ');
      AuctionPriceClient var5 = AuctionPriceClient.getInstance();
      var5.requestPrice(var4, var0 -> {});
      double var6 = this.unitPrices.getOrDefault(var1, (double)var5.tick(var4));
      return Math.round(var6 * Math.max(0, var2));
   }

   public void markFinalBreak() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gui.screen() != null && var1.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("order")) {
         AbstractContainerMenu var2 = var1.player.containerMenu;
         HashMap var3 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5.container != var1.player.getInventory()) {
               ItemStack var6 = var5.getItem();
               if (FarmMobType.isDropItem(var6.getItem())) {
                  ItemLore var7 = (ItemLore)var6.get(DataComponents.LORE);
                  if (var7 != null) {
                     for (Component var9 : var7.lines()) {
                        String var10 = var9.getString().toLowerCase(Locale.ROOT);
                        if (var10.contains("each") || var10.contains("per item") || var10.contains("unit price")) {
                           Matcher var11 = Pattern.compile("\\$\\s*([0-9][0-9,]*(?:\\.[0-9]+)?)\\s*([kmb]?)").matcher(var10);
                           if (var11.find()) {
                              double var12 = Double.parseDouble(var11.group(1).replace(",", ""));

                              var12 *= switch (var11.group(2)) {
                                 case "k" -> 1000;
                                 case "m" -> 1000000;
                                 case "b" -> 1000000000;
                                 default -> 1;
                              };
                              if (Double.isFinite(var12) && var12 > 0.0) {
                                 var3.merge(var6.getItem(), var12, Math::max);
                              }
                           }
                        }
                     }
                  }
               }
            }
         }

         var3.forEach((var1x, var2x) -> this.unitPrices.merge(var1x, var2x, Math::max));
      }
   }

   public int removeFromPile() {
      return this.isFakeBlockEntity(Items.BONE);
   }

   private boolean trackDerivedStack(ItemStack var1) {
      if (var1.has(DataComponents.CUSTOM_NAME)) {
         return false;
      }

      FakeSpawnerModule var2 = FakeSpawnerModule.getInstance();

      for (FarmMobType var6 : FarmMobType.values()) {
         String var7 = var2.getBlock(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1.is(var6.primaryDrop)) {
            return true;
         }
      }

      return false;
   }

   private int isFakeBlockEntity(Item var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.isActive() && var2.player != null) {
         int var3 = -1;
         int var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6.is(var1) && !var6.has(DataComponents.CUSTOM_NAME) && !WithdrawnBoneMarker.isWithdrawnBone(var6) && var6.getCount() > var4) {
               var3 = var5;
               var4 = var6.getCount();
            }
         }

         return this.boneSlot = var3;
      } else {
         return -1;
      }
   }

   public void snapshotInventory(int var1) {
   }

   public void tickBreakAttempts(AbstractContainerMenu var1, Slot var2) {
   }

   public void snapshotInventory(Slot var1) {
   }

   public void isActive(BlockPos var1) {
      if (var1 != null && this.markPendingFinalBreak(var1.asLong())) {
         this.loadPiles(var1);
         this.markBlockDirty(var1);
         LarpLog.debug("FakeSpawner", "Final break predicted at {}; retaining pile and reservations until server acknowledgment", var1);
      }
   }

   boolean markPendingFinalBreak(long var1) {
      return this.stackCountByPos.getOrDefault(var1, 0) > 0 && this.pendingFinalBreaks.add(var1);
   }

   boolean requestRestore(long var1) {
      return this.pendingFinalBreaks.remove(var1);
   }

   public BlockState getStackName(Level var1, BlockPos var2, BlockState var3) {
      if (this.isActive() && var1 == this.level && this.stackCountByPos.getOrDefault(var2.asLong(), 0) > 0 && var1 instanceof ClientLevel var4) {
         if (var1.isOutsideBuildHeight(var2)) {
            return Blocks.AIR.defaultBlockState();
         }

         LevelChunk var5 = var4.getChunkAt(var2);
         return var5.getSection(var5.getSectionIndex(var2.getY())).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15);
      } else {
         return var3;
      }
   }

   public void isReservedHeldStack(Runnable var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.isActive() && var2.player != null && var2.isSameThread()) {
         try {
            this.removeSpawners(var2.player.getInventory(), var1);
         } finally {
            this.snapshotInventory();
         }
      } else {
         var1.run();
      }
   }

   void removeSpawners(Container var1, Runnable var2) {
      try {
         var2.run();
      } finally {
         VisualInventory.isActive();
      }
   }

   private void onInventoryUpdate(int var1, ItemStack var2) {
      ItemStack var3 = this.reservedStacks.get(var1);
      if (var3 != null && var2.is(var3.getItem()) && var2.getCount() >= var3.getCount() && !ItemStack.isSameItemSameComponents(var2, var3)) {
         this.reservedStacks.put(var1, var2.copyWithCount(var3.getCount()));

         for (Deque var5 : this.reservationsByPos.values()) {
            ArrayDeque var6 = new ArrayDeque();

            for (FakeSpawnerController.SlotReservation var8 : var5) {
               var6.addLast(
                  var8.preferredSlot == var1 && var8.stack.is(var2.getItem())
                     ? new FakeSpawnerController.SlotReservation(var1, var2.copyWithCount(var8.stack.getCount()))
                     : var8
               );
            }

            var5.clear();
            var5.addAll(var6);
         }
      }
   }

   public boolean restorePiles(ItemStack var1) {
      return this.isActive() && !var1.isEmpty() && this.reservedStacks.values().stream().anyMatch(var1x -> ItemStack.isSameItemSameComponents(var1, var1x));
   }

   public boolean snapshotInventory(int var1, int var2, int var3, ContainerInput var4) {
      if (!this.isActive() || ContainerTransaction.isInternalClick()) {
         return true;
      }

      if (ContainerTransaction.isActive()) {
         return false;
      }

      LocalPlayer var5 = Minecraft.getInstance().player;
      return var5 != null && var5.containerMenu.containerId == var1 ? this.isReservedSlot(var5.containerMenu, var2, var3, var4) : true;
   }

   public boolean isReservedSlot(AbstractContainerMenu var1, int var2, int var3, ContainerInput var4) {
      if (!this.isActive() || ContainerTransaction.isInternalClick()) {
         return true;
      }

      if (ContainerTransaction.isActive()) {
         return false;
      }

      LocalPlayer var5 = Minecraft.getInstance().player;
      if (var5 == null) {
         return true;
      }

      if (var1 != var5.containerMenu && var1 != var5.inventoryMenu) {
         return false;
      }

      if (var2 >= 0 && var2 < var1.slots.size()) {
         Slot var6 = var1.getSlot(var2);
         if (var6.container == var5.getInventory()
            && (
               this.isReservedSlot(var6.getContainerSlot())
                  || FakeLootWithdrawer.hasFakeStack(var6.getContainerSlot())
                  || this.readPilesFrom(var6.getContainerSlot())
            )) {
            return false;
         }
      }

      return var4 != ContainerInput.SWAP || !this.isReservedSlot(var3) && !FakeLootWithdrawer.hasFakeStack(var3) && !this.readPilesFrom(var3)
         ? var4 != ContainerInput.PICKUP_ALL || !this.restorePiles(var1.getCarried())
         : false;
   }

   public void isActive(int var1, Runnable var2) {
      this.isReservedHeldStack(var2);
   }

   public boolean restorePiles(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      ItemStack var4 = this.reservedStacks.get(var2);
      if (var4 == null || var4.isEmpty()) {
         return false;
      }

      if (var3.player != null
         && var3.gameMode != null
         && var1 >= 9
         && var1 < 36
         && !this.isReservedSlot(var1)
         && var3.player.inventoryMenu.getCarried().isEmpty()
         && var3.player.containerMenu == var3.player.inventoryMenu) {
         if (this.trackDerivedStack(var3.player.getInventory().getItem(var1))) {
            this.snapshotInventory(var2);
         }

         ContainerTransaction.runInternal(() -> var3.gameMode.handleContainerInput(0, var1, var2, ContainerInput.SWAP, var3.player));
         if (var4 != null && !var4.isEmpty()) {
            this.reservedStacks.remove(var2);
            this.reservedStacks.put(var1, var4);

            for (Deque var6 : this.reservationsByPos.values()) {
               ArrayDeque var7 = new ArrayDeque();

               for (FakeSpawnerController.SlotReservation var9 : var6) {
                  var7.addLast(var9.preferredSlot == var2 ? new FakeSpawnerController.SlotReservation(var1, var9.stack) : var9);
               }

               var6.clear();
               var6.addAll(var7);
            }

            VisualInventory.isActive();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean isReservedSlot(int var1) {
      return this.isActive() && this.reservedStacks.containsKey(var1) && !this.reservedStacks.get(var1).isEmpty();
   }

   public int getUnreservedCount(int var1, ItemStack var2) {
      ItemStack var3 = this.reservedStacks.get(var1);
      return this.isActive() && var3 != null && var2.is(var3.getItem()) ? Math.max(0, var2.getCount() - var3.getCount()) : 0;
   }

   public ItemStack syncState(int var1, ItemStack var2) {
      if (!this.isReservedSlot(var1)) {
         return var2;
      }

      int var3 = this.getUnreservedCount(var1, var2);
      return var3 == 0 ? ItemStack.EMPTY : ItemStackMarkers.markPreview(var2.copyWithCount(var3));
   }

   public boolean replayCreativeClicks(AbstractContainerMenu var1, List<ContainerTransaction.Click> var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var1 == var3.player.inventoryMenu && var3.player.hasInfiniteMaterials() && var3.player.containerMenu instanceof ItemPickerMenu
         )
       {
         this.isReservedHeldStack(() -> {
            for (ContainerTransaction.Click var4 : var2) {
               var1.clicked(var4.slot(), var4.button(), var4.type(), var3.player);
            }

            var1.broadcastChanges();
         });
         return true;
      } else {
         return false;
      }
   }

   public boolean getSavePath(AbstractContainerMenu var1, Slot var2, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var2 != null && var2.container == var4.player.getInventory() && var3 >= 0 && var3 <= 1) {
         int var5 = var2.getContainerSlot();
         ItemStack var6 = var4.player.getInventory().getItem(var5);
         int var7 = this.getUnreservedCount(var5, var6);
         if (var7 != 0 && var1.getCarried().isEmpty()) {
            int var8 = var3 == 0 ? var7 : (var7 + 1) / 2;
            ArrayList var9 = new ArrayList();
            var9.add(new ContainerTransaction.Click(var2.index, 0, ContainerInput.PICKUP));

            for (int var10 = 0; var10 < var6.getCount() - var8; var10++) {
               var9.add(new ContainerTransaction.Click(var2.index, 1, ContainerInput.PICKUP));
            }

            int var11 = var6.getCount() - var7;
            if (this.replayCreativeClicks(var1, var9)) {
               LarpLog.debug("FakeSpawner", "Creative surplus picked physical={} amount={} reserved={}", var5, var8, var11);
               return true;
            } else {
               ContainerTransaction.tick(var1, "pick unreserved surplus", var9, var6.copyWithCount(var8), VisualInventory::isActive, VisualInventory::isActive);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int restorePiles() {
      Inventory var1 = Minecraft.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.isReservedSlot(var2) && !this.readPilesFrom(var2) && var1.getItem(var2).isEmpty()) {
            return var2;
         }
      }

      return -1;
   }

   public int isActive(int var1) {
      return this.replayCreativeClicks(Items.BONE, var1);
   }

   public int replayCreativeClicks(Item var1, int var2) {
      return FakeLootWithdrawer.addFakeLoot(var1, var2);
   }

   public boolean debugLog(ItemStack var1) {
      return false;
   }

   public boolean debugLog() {
      return this.breakWholeStack(1) == 1;
   }

   public int breakWholeStack(int var1) {
      return this.isActive(Items.BONE, var1);
   }

   public int isActive(Item var1, int var2) {
      return this.replayCreativeClicks(var1, var2);
   }

   public boolean isFakeSpawnerBlock(int var1) {
      return var1 == 48 || var1 == 50;
   }

   public boolean markBlockDirty(Component var1, AbstractContainerMenu var2) {
      if (var1 != null && var2 != null) {
         String var3 = var1.getString().toLowerCase(Locale.ROOT);
         return var3.contains("sell") && var2.slots.size() >= 72;
      } else {
         return false;
      }
   }

   public Component removeSpawners(Component var1) {
      return var1 == null ? Component.literal(tryBreakWithoutSilk("place items in here to sell")) : Component.literal(tryBreakWithoutSilk(var1.getString()));
   }

   public void restorePiles(Component var1, AbstractContainerMenu var2) {
      if (this.isActive() && this.markBlockDirty(var1, var2)) {
         long var3 = 0L;

         for (int var5 = 0; var5 < Math.max(0, var2.slots.size() - 36); var5++) {
            var3 += this.loadPiles(var2.getSlot(var5).getItem());
         }

         if (var3 > 0L) {
            PayNotice.show(var3);
         }
      }
   }

   private long recordRenderCounts(BlockPos var1) {
      long var2 = System.currentTimeMillis();
      return var1 == null ? var2 : this.placedAtByPos.computeIfAbsent(var1.asLong(), var2x -> var2);
   }

   private int restorePiles(BlockPos var1) {
      return var1 == null ? Math.max(1, FakeSpawnerModule.getInstance().getPreviewCount()) : Math.max(1, this.stackCountByPos.getOrDefault(var1.asLong(), 1));
   }

   private FakeSpawnerController.DropAccrual readPilesFrom(long var1, int var3) {
      return this.rebuildPositionSets(var1, var3, FarmMobType.sKELETON);
   }

   private FakeSpawnerController.DropAccrual rebuildPositionSets(long var1, int var3, FarmMobType var4) {
      double var5 = this.wasUsedRecently(var3);
      double var7 = var4.secondaryDrop == Items.AIR ? 0.0 : Math.max(0.15, var5 * 0.33);
      long var9 = Math.max(0L, System.currentTimeMillis() - var1);
      long var11 = var9 / 60000L;
      return new FakeSpawnerController.DropAccrual(Math.max(0, (int)(var11 * var5)), Math.max(0, (int)(var11 * var7)), var5, var7);
   }

   private double wasUsedRecently(int var1) {
      int var2 = Math.max(1, var1);
      if (var2 == 1) {
         return 1.9;
      } else {
         return var2 <= 64 ? var2 * 1.9453125 : 124.5 + (var2 - 64) * 0.88671875;
      }
   }

   private static int getKindItem(SimpleContainer var0, int var1, Item var2, int var3) {
      int var4 = Math.max(0, var3);

      while (var4 > 0 && var1 < 45) {
         int var5 = Math.min(64, var4);
         var0.setItem(var1++, new ItemStack(var2, var5));
         var4 -= var5;
      }

      return var1;
   }

   private List<Component> debugLog(FakeSpawnerController.DropAccrual var1, FarmMobType var2) {
      int var3 = var1 == null ? 0 : Math.max(0, var1.secondaryCount);
      int var4 = var1 == null ? 0 : Math.max(0, var1.primaryCount);
      if (var3 <= 0 && var4 <= 0) {
         return List.of(getSavePath("(0% filled)", 64648));
      }

      ArrayList var5 = new ArrayList();
      if (var3 > 0) {
         var5.add(getSavePath(var3 + " ", 64648).append(var2.secondaryDrop.getName(new ItemStack(var2.secondaryDrop)).copy().withStyle(ChatFormatting.WHITE)));
      }

      if (var4 > 0) {
         var5.add(getSavePath(var4 + " ", 64648).append(var2.primaryDrop.getName(new ItemStack(var2.primaryDrop)).copy().withStyle(ChatFormatting.WHITE)));
      }

      var5.add(getSavePath("(0% filled)", 64648));
      return var5;
   }

   private boolean getInstance(long var1, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null
         && !ContainerTransaction.isActive()
         && var4.player.containerMenu == var4.player.inventoryMenu
         && var4.player.inventoryMenu.getCarried().isEmpty()) {
         Deque var5 = this.reservationsByPos.get(var1);
         if (var5 != null && !var5.isEmpty()) {
            LinkedHashMap var6 = new LinkedHashMap();
            int var7 = var3;

            for (FakeSpawnerController.SlotReservation var9 : var5.reversed()) {
               ItemStack var10 = var4.player.getInventory().getItem(var9.preferredSlot);
               if (!var10.is(var9.stack.getItem()) || var10.getCount() < var9.stack.getCount()) {
                  return false;
               }

               int var11 = Math.min(var7, var9.stack.getCount());
               var6.merge(var9.preferredSlot, var11, Integer::sum);
               var7 -= var11;
               if (var7 <= 0) {
                  break;
               }
            }

            int var13 = var3 - var7;
            int var14 = -1;

            for (Entry var17 : var6.entrySet()) {
               ItemStack var12 = this.reservedStacks.get(var17.getKey());
               if (var12 != null
                  && var12.getCount() == (Integer)var17.getValue()
                  && var4.player.getInventory().getItem((Integer)var17.getKey()).getCount() == (Integer)var17.getValue()) {
                  var14 = (Integer)var17.getKey();
                  break;
               }
            }

            if (var14 < 0) {
               var14 = this.restorePiles();
            }

            if (var14 < 0) {
               return false;
            }

            ArrayList var16 = new ArrayList();

            for (Entry var20 : var6.entrySet()) {
               if ((Integer)var20.getKey() != var14) {
                  var16.addAll(
                     ContainerTransaction.begin(
                        (Integer)var20.getKey(), var14, (Integer)var20.getValue(), var4.player.getInventory().getItem((Integer)var20.getKey()).getCount()
                     )
                  );
               }
            }

            if (var16.isEmpty()) {
               this.tick(var1, var3);
               if (var7 > 0) {
                  this.trackDerivedStack(new ItemStack(this.getKindItem(var1), var7), var7);
               }

               VisualInventory.isActive();
               return true;
            } else {
               int var19 = var14;
               int var21 = var7;
               ContainerTransaction.beginInInventory("mining return", var16, () -> {
                  this.tick(var1, var3);
                  this.removeFromPile(var1, var3);
                  if (var21 > 0) {
                     this.trackDerivedStack(new ItemStack(this.getKindItem(var1), var21), var21);
                  }

                  this.openLootMenu(BlockPos.of(var1), true);
                  VisualInventory.isActive();
                  this.debugLog("Mining return predicted pile={} physical={} amount={} worldPending={}", var1, var19, var13, var21);
               }, () -> this.reconcileInventory());
               return false;
            }
         } else {
            this.trackDerivedStack(new ItemStack(this.getKindItem(var1), var3), var3);
            return true;
         }
      } else {
         return false;
      }
   }

   private void trackDerivedStack(ItemStack var1, int var2) {
      if (var2 > 0 && !var1.isEmpty()) {
         Inventory var3 = Minecraft.getInstance().player.getInventory();
         int var4 = -1;

         for (int var5 = 0; var5 < 36; var5++) {
            if (!this.isReservedSlot(var5)) {
               ItemStack var6 = var3.getItem(var5);
               FakeSpawnerController.PendingRefund var7 = this.pendingRefunds.get(var5);
               if (var7 != null && ItemStack.isSameItemSameComponents(var7.stack, var1) && var7.expected + var2 <= var1.getMaxStackSize()) {
                  var4 = var5;
                  break;
               }

               if (var7 == null && !var6.isEmpty() && ItemStack.isSameItemSameComponents(var6, var1) && var6.getCount() + var2 <= var6.getMaxStackSize()) {
                  var4 = var5;
                  break;
               }
            }
         }

         if (var4 < 0) {
            var4 = this.restorePiles();
         }

         if (var4 >= 0) {
            int var8 = var3.getItem(var4).isEmpty() && !this.pendingRefunds.containsKey(var4) ? VisualInventory.findFreeSlot() : -1;
            this.addPendingRefund(var4, var1, 0, var2);
            if (var8 >= 0) {
               VisualInventory.swapSlots(var8, var4);
            }
         }
      }
   }

   private void addPendingRefund(int var1, ItemStack var2, int var3, int var4) {
      if (var4 > 0) {
         FakeSpawnerController.PendingRefund var5 = this.pendingRefunds.get(var1);
         int var6 = Minecraft.getInstance().player.getInventory().getItem(var1).getCount();
         int var7 = var5 != null && ItemStack.isSameItemSameComponents(var5.stack, var2) ? Math.max(var5.expected + var3, var6) : var6;
         this.pendingRefunds
            .put(var1, new FakeSpawnerController.PendingRefund(var2.copyWithCount(1), var7 + var4, Minecraft.getInstance().player.tickCount + 100));
         VisualInventory.invalidate();
      }
   }

   public boolean readPilesFrom(int var1) {
      return this.pendingRefunds.containsKey(var1);
   }

   public ItemStack reconcileInventory(int var1, ItemStack var2) {
      FakeSpawnerController.PendingRefund var3 = this.pendingRefunds.get(var1);
      return var3 != null && var2.isEmpty() ? ItemStackMarkers.markPreview(var3.stack.copyWithCount(var3.expected)) : var2;
   }

   private FakeSpawnerController.SlotReservation tick(long var1, int var3) {
      Deque var4 = this.reservationsByPos.get(var1);
      if (var4 != null && !var4.isEmpty() && var3 > 0) {
         int var5 = var3;
         int var6 = -1;
         ItemStack var7 = ItemStack.EMPTY;

         while (var5 > 0 && !var4.isEmpty()) {
            FakeSpawnerController.SlotReservation var8 = (FakeSpawnerController.SlotReservation)var4.removeLast();
            if (var8 != null && var8.stack != null && !var8.stack.isEmpty()) {
               if (var6 < 0) {
                  var6 = var8.preferredSlot;
               }

               if (var7.isEmpty()) {
                  var7 = var8.stack.copy();
               }

               int var9 = Math.min(var5, var8.stack.getCount());
               ItemStack var10 = this.reservedStacks.get(var8.preferredSlot);
               if (var10 != null && ItemStack.isSameItemSameComponents(var10, var8.stack)) {
                  if (var10.getCount() <= var9) {
                     this.reservedStacks.remove(var8.preferredSlot);
                  } else {
                     this.reservedStacks.put(var8.preferredSlot, var10.copyWithCount(var10.getCount() - var9));
                  }
               }

               var5 -= var9;
               int var11 = var8.stack.getCount() - var9;
               if (var11 > 0) {
                  var4.addLast(new FakeSpawnerController.SlotReservation(var8.preferredSlot, var8.stack.copyWithCount(var11)));
               }
            }
         }

         if (var4.isEmpty()) {
            this.reservationsByPos.remove(var1);
         }

         return var7.isEmpty() ? null : new FakeSpawnerController.SlotReservation(var6, var7.copyWithCount(var3));
      } else {
         return null;
      }
   }

   private boolean reconcileInventory(long var1, FakeSpawnerController.SlotReservation var3, boolean var4) {
      if (var3 != null && !var3.stack.isEmpty()) {
         Minecraft var5 = Minecraft.getInstance();
         if (var5.player == null) {
            return false;
         }

         int var6 = var3.preferredSlot;
         if (var6 >= 0 && var6 < var5.player.getInventory().getContainerSize() && !this.reservedStacks.containsKey(var6)) {
            ItemStack var7 = var5.player.getInventory().getItem(var6);
            int var8 = var3.stack.getCount();
            int var9 = var4 && !var5.player.hasInfiniteMaterials() ? var8 - 1 : var8;
            if (var9 <= 0) {
               return true;
            } else if (var7.is(var3.stack.getItem()) && var7.getCount() >= var9) {
               this.reservedStacks.put(var6, var7.copyWithCount(var9));
               this.reservationsByPos
                  .computeIfAbsent(var1, var0 -> new ArrayDeque<>())
                  .addFirst(new FakeSpawnerController.SlotReservation(var6, var7.copyWithCount(var9)));
               VisualInventory.isActive();
               LarpLog.debug(
                  "FakeSpawner",
                  "Reservation created pile={} physical={} item={} reserved={} real={}",
                  var1,
                  var6,
                  BuiltInRegistries.ITEM.getKey(var7.getItem()),
                  var9,
                  var7.getCount()
               );
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

   private void getSavePath(BlockPos var1, Direction var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.particleEngine != null && var3.level != null && var1 != null) {
         double var4 = var1.getX() + 0.5;
         double var6 = var1.getY() + 0.55;
         double var8 = var1.getZ() + 0.5;
         if (var2 != null) {
            var4 += var2.getStepX() * 0.42;
            var6 += var2.getStepY() * 0.42;
            var8 += var2.getStepZ() * 0.42;
         }

         BlockParticleOption var10 = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SPAWNER.defaultBlockState());

         for (int var11 = 0; var11 < 1; var11++) {
            var3.particleEngine
               .createParticle(
                  var10,
                  var4,
                  var6,
                  var8,
                  (var3.level.getRandom().nextDouble() - 0.5) * 0.14,
                  var3.level.getRandom().nextDouble() * 0.12 + 0.02,
                  (var3.level.getRandom().nextDouble() - 0.5) * 0.14
               );
         }
      }
   }

   private void restorePiles(Minecraft var1, BlockPos var2, Direction var3, double var4) {
      if (var1 != null && var1.player != null && var1.levelRenderer != null && var2 != null) {
         int var6 = Mth.clamp((int)(var4 * 10.0), 0, 9);
         var1.level.destroyBlockProgress(var1.player.getId(), var2, var6);
         if (var4 <= 0.0) {
            return;
         }

         long var7 = var2.asLong();
         long var9 = var1.player.tickCount;
         long var11 = this.lastBreakEffectTick.getOrDefault(var7, 0L);
         if (var9 - var11 >= 4L) {
            this.lastBreakEffectTick.put(var7, var9);
            this.getSavePath(var2, var3);
            this.openLootMenu(var2, false);
         }
      }
   }

   private void loadPiles(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var2.levelRenderer != null && var1 != null) {
         var2.level.destroyBlockProgress(var2.player.getId(), var1, -1);
      }
   }

   private void tick(Level var1, BlockPos var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.particleEngine != null && var2 != null) {
         double var4 = var2.getX() + 0.5 + (var1.getRandom().nextDouble() - 0.5) * 0.45;
         double var6 = var2.getY() + 0.35 + var1.getRandom().nextDouble() * 0.45;
         double var8 = var2.getZ() + 0.5 + (var1.getRandom().nextDouble() - 0.5) * 0.45;
         var3.particleEngine.createParticle(ParticleTypes.FLAME, var4, var6, var8, 0.0, 0.006, 0.0);
         if (var1.getRandom().nextInt(4) == 0) {
            var3.particleEngine.createParticle(ParticleTypes.SMOKE, var4, var6 + 0.02, var8, 0.0, 0.004, 0.0);
         }
      }
   }

   private void reset(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 != null && !var1.isBlank()) {
         PlayerMessages.sendMessage(getSavePath(var1, 16711680), false);
         PlayerMessages.sendMessage(getSavePath(var1, 16711680), true);
      }
   }

   private void debugLog(String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         LarpLog.debug("FakeSpawner", var1, var2);
      }
   }

   private void rebuildPositionSets(String var1, long var2, String var4, Object... var5) {
      if (Boolean.getBoolean("larp.debug")) {
         long var6 = System.currentTimeMillis();
         if (var6 >= this.nextLogAt) {
            this.nextLogAt = var6 + var2;
            this.debugLog(var4, var5);
         }
      }
   }

   private static ItemStack getUnreservedCount(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1);
      var0.set(DataComponents.RARITY, Rarity.COMMON);
      if (var2 != null && !var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component resolveFillerItem(String var0, ChatFormatting var1) {
      return syncState(tryBreakWithoutSilk(var0), var1);
   }

   private static MutableComponent syncState(String var0, ChatFormatting var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.applyFormat(var1).withItalic(false));
   }

   private static Component tickBreakAttempts(String var0, int var1) {
      return getSavePath(tryBreakWithoutSilk(var0), var1);
   }

   private static MutableComponent getSavePath(String var0, int var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)).withItalic(false));
   }

   private static String tryBreakWithoutSilk(String var0) {
      StringBuilder var1 = new StringBuilder();

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (var3 >= 'A' && var3 <= 'Z') {
            var1.append("ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".charAt(var3 - 'A'));
         } else if (var3 >= 'a' && var3 <= 'z') {
            var1.append("ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ".charAt(var3 - 'a'));
         } else {
            var1.append(var3);
         }
      }

      return var1.toString();
   }

   private static String removeFromPile(double var0, int var2) {
      String var3 = String.format(Locale.US, "%." + var2 + "f", var0);

      while (var3.contains(".") && var3.endsWith("0")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      if (var3.endsWith(".")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3;
   }

   private static Component trackInventoryStacks(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         MutableComponent var1 = Component.empty();
         Style var2 = Style.EMPTY.withItalic(false);
         StringBuilder var3 = new StringBuilder();

         for (int var4 = 0; var4 < var0.length(); var4++) {
            char var5 = var0.charAt(var4);
            if ((var5 == 167 || var5 == '&') && var4 + 1 < var0.length()) {
               if (var5 == '&' && var4 + 7 < var0.length() && var0.charAt(var4 + 1) == '#') {
                  Integer var6 = removeSpawnerEntity(var0.substring(var4 + 2, var4 + 8));
                  if (var6 != null) {
                     wasUsedRecently(var1, var3, var2);
                     var2 = var2.withColor(TextColor.fromRgb(var6)).withItalic(false);
                     var4 += 7;
                     continue;
                  }
               }

               ChatFormatting var7 = ChatFormatting.getByCode(var0.charAt(var4 + 1));
               if (var7 != null) {
                  wasUsedRecently(var1, var3, var2);
                  if (var7 == ChatFormatting.RESET) {
                     var2 = Style.EMPTY.withItalic(false);
                  } else if ("0123456789abcdef".indexOf(Character.toLowerCase(var0.charAt(var4 + 1))) >= 0) {
                     var2 = Style.EMPTY.applyFormat(var7).withItalic(false);
                  } else {
                     var2 = var2.applyFormat(var7).withItalic(false);
                  }

                  var4++;
                  continue;
               }
            }

            var3.append(var5);
         }

         wasUsedRecently(var1, var3, var2);
         return var1;
      } else {
         return Component.empty();
      }
   }

   private static void wasUsedRecently(MutableComponent var0, StringBuilder var1, Style var2) {
      if (var1.length() != 0) {
         var0.append(Component.literal(var1.toString()).setStyle(var2));
         var1.setLength(0);
      }
   }

   private static Integer removeSpawnerEntity(String var0) {
      try {
         return Integer.parseInt(var0, 16);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private String isFakeBlockEntity(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0;
      if (var5 >= 1.0E9) {
         return removeFromPile(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return removeFromPile(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? removeFromPile(var5 / 1000.0, 1) + "K" : removeFromPile(var5, 0);
      }
   }

   private String markFinalBreak(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0 + 75.0;
      if (var5 >= 1.0E9) {
         return removeFromPile(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return removeFromPile(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? removeFromPile(var5 / 1000.0, 1) + "K" : removeFromPile(var5, 0);
      }
   }

   private boolean tickBreakAttempts(ItemStack var1) {
      return var1 != null && !var1.isEmpty() ? var1.getItem() instanceof BlockItem var2 && var2.getBlock() instanceof ShulkerBoxBlock : false;
   }

   private long reconcileInventory(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         long var2 = 0L;
         ItemContainerContents var4 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
         if (var4 != null) {
            for (ItemStack var6 : var4.nonEmptyItemCopyStream().toList()) {
               if (var6 != null && !var6.isEmpty() && this.hasSilkTouch(var6)) {
                  var2 += var6.getCount();
               }
            }
         }

         return var2;
      } else {
         return 0L;
      }
   }

   private int tick(CompoundTag var1) {
      if (var1 == null) {
         return 0;
      } else if (var1.contains("count")) {
         return Math.max(0, var1.getIntOr("count", 0));
      } else {
         return var1.contains("Count") ? Math.max(0, var1.getIntOr("Count", 0)) : 0;
      }
   }

   private boolean syncState(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2 != null && var2.getWindow() != null ? KeyCodes.isKeyDown(var2.getWindow(), var1) : false;
   }

   private record BackingSlot(int slot, String item, int amount, int reserved) {
   }

   public record BlockSound(SoundEvent event, float volume, float pitch) {
   }

   public static final class DropAccrual {
      public final int primaryCount;
      public final int secondaryCount;
      public final double primaryRate;
      public final double secondaryRate;

      public DropAccrual(int var1, int var2, double var3, double var5) {
         this.primaryCount = Math.max(0, var1);
         this.secondaryCount = Math.max(0, var2);
         this.primaryRate = Math.max(0.0, var3);
         this.secondaryRate = Math.max(0.0, var5);
      }
   }

   private record PendingPlacement(int stackCount, FakeSpawnerController.SlotReservation hiddenStack, long createdAtMs) {
   }

   private record PendingRefund(ItemStack stack, int expected, int expires) {
   }

   private record RecentBreak(BlockPos pos, long untilMs) {
   }

   private record SavedPile(
      long pos, FarmMobType kind, int count, long placedAt, List<FakeSpawnerController.BackingSlot> backing, Map<String, Integer> purchased
   ) {
      JsonObject toJson() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("pos", this.pos);
         var1.addProperty("kind", this.kind.name());
         var1.addProperty("count", this.count);
         var1.addProperty("placedAt", this.placedAt);
         JsonArray var2 = new JsonArray();

         for (FakeSpawnerController.BackingSlot var4 : this.backing) {
            JsonObject var5 = new JsonObject();
            var5.addProperty("slot", var4.slot);
            var5.addProperty("item", var4.item);
            var5.addProperty("amount", var4.amount);
            var5.addProperty("reserved", var4.reserved);
            var2.add(var5);
         }

         var1.add("backing", var2);
         JsonObject var6 = new JsonObject();
         this.purchased.forEach(var6::addProperty);
         var1.add("purchased", var6);
         return var1;
      }
   }

   private record SlotReservation(int preferredSlot, ItemStack stack) {
   }
}
