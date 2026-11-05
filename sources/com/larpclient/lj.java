package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.module.FakeSpawnerModule;
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

public final class lj {
   private static final lj K_ = new lj();
   private static final int e = 45;
   private static final String S = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
   private static final long u = 1800L;
   private static final long Y = 650L;
   private static final int J = 64648;
   private static final int d = 16711680;
   private static final double KT = 75.0;
   private final Map<Long, Long> B = new HashMap<>();
   private final Map<Long, Map<Item, Integer>> j = new HashMap<>();
   private final Map<Long, Integer> f = new ConcurrentHashMap<>();
   private final Set<Long> I = ConcurrentHashMap.newKeySet();
   private final Map<Long, lj.c> v = new HashMap<>();
   private final Map<Long, Deque<lj.t>> O = new HashMap<>();
   private final Map<Integer, ItemStack> D = new HashMap<>();
   private final Map<Long, lj.y> H = new HashMap<>();
   private volatile Map<Long, SpawnerBlockEntity> W = new ConcurrentHashMap<>();
   private final Long2ObjectMap<List<SpawnerBlockEntity>> V = new Long2ObjectOpenHashMap();
   private final Map<Long, Long> z = new HashMap<>();
   private final Map<Long, Float> p = new HashMap<>();
   private final Map<Long, Integer> T = new HashMap<>();
   private int c;
   private final Set<Integer> U = new HashSet<>();
   private final Map<Integer, lj.p> n = new HashMap<>();
   private BlockPos a;
   private int s = -1;
   private final Map<Long, Integer> E = new HashMap<>();
   private final Map<Long, Double> b = new HashMap<>();
   private final Map<Long, Integer> N = new ConcurrentHashMap<>();
   private final Set<Long> r = new HashSet<>();
   private final Map<Item, Double> x = new HashMap<>();
   private final Set<Long> w = ConcurrentHashMap.newKeySet();
   private volatile Level g;
   private LocalPlayer l;
   private Item i;
   private String X;
   private volatile Map<Item, mb> Kf = Map.of();
   private volatile LongSet m = LongSets.EMPTY_SET;
   private volatile LongSet Q = LongSets.EMPTY_SET;
   private final Map<Long, Long> Kq = new ConcurrentHashMap<>();
   private final Map<Long, mb> KJ = new ConcurrentHashMap<>();
   private final Map<Long, lj.s> k = new HashMap<>();
   private final List<JsonElement> P = new ArrayList<>();
   private String C;
   private boolean h = (boolean)1;
   private long t;
   private long Ky;
   private long y;
   private long o;
   private long M;
   private long R;
   private final Set<ItemStack> L = Collections.newSetFromMap(new IdentityHashMap<>());
   private final Set<ItemStack> A = Collections.newSetFromMap(new WeakHashMap<>());
   private InteractionHand F;
   private int Z = -1;
   private int q = -1;
   private final Set<Long> G = ConcurrentHashMap.newKeySet();
   private long K;

   private static Path P() {
      return FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-piles.json");
   }

   void c(Container var1) {
      this.L.clear();
      this.A.clear();

      for (int var2 = 0; var2 < var1.getContainerSize(); var2++) {
         ItemStack var3 = var1.getItem(var2);
         if (!var3.isEmpty()) {
            this.L.add(var3);
         }
      }
   }

   public ItemStack B(ItemStack var1, ItemStack var2) {
      if (!var2.isEmpty() && (this.L.contains(var1) || this.A.contains(var1))) {
         this.A.add(var2);
         if (Boolean.getBoolean("larp.debug")) {
            this.M++;
         }
      }

      return var2;
   }

   public void T() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null) {
         this.c(var1.player.getInventory());
         if (!var1.player.containerMenu.getCarried().isEmpty()) {
            this.L.add(var1.player.containerMenu.getCarried());
         }

         mx.u();
      }
   }

   public void S() {
      this.G();
      if (Boolean.getBoolean("larp.debug")) {
         this.Ky++;
      }
   }

   public void S(int var1, int var2) {
      if (Boolean.getBoolean("larp.debug")) {
         this.y += var1;
         this.o += var2;
      }
   }

   public boolean A(ItemStack var1) {
      if (!this.B()) {
         return false;
      }

      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 != null && (var1 == var2.getMainHandItem() && this.U(var2.getInventory().getSelectedSlot()) || var1 == var2.getOffhandItem() && this.U(40));
   }

   public void c() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner reset must run on the client thread");
      }

      this.R();
      rm.U("controller cleanup");
      mx.B();
      this.L.clear();
      this.A.clear();
      this.W = new ConcurrentHashMap<>();
      this.V.clear();
      this.m = LongSets.EMPTY_SET;
      this.Q = LongSets.EMPTY_SET;
      this.g = null;
      this.s = -1;
      this.F = null;
      this.q = -1;
      this.T.clear();
      this.c = 0;
      this.G.clear();
      this.U.clear();
      this.n.clear();
      this.E.clear();
      this.b.clear();
      this.N.clear();
      this.r.clear();
      this.x.clear();
      this.w.clear();

      for (Long var3 : new ArrayList<>(this.p.keySet())) {
         this.v(BlockPos.of(var3));
      }

      int var6 = this.f.isEmpty() && this.I.isEmpty() ? 0 : 1;
      this.f.clear();
      this.KJ.clear();
      this.k.clear();
      this.P.clear();
      this.C = null;
      this.I.clear();
      this.B.clear();
      this.j.clear();
      this.v.clear();
      this.O.clear();
      this.D.clear();

      for (long var4 : this.Kq.keySet()) {
         this.Y(BlockPos.of(var4));
      }

      this.Kq.clear();
      this.Q = LongSets.EMPTY_SET;
      this.H.clear();
      this.p.clear();
      this.z.clear();
      this.a = null;
      this.l = null;
      this.i = null;
      if (var1 != null) {
         if (var1.gui.screen() instanceof oy || var1.gui.screen() instanceof x4) {
            var1.gui.setScreen(null);
         }

         if (var6 != 0 && var1.level != null) {
            var1.levelExtractor.allChanged();
         }
      }
   }

   private void Q() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner state must be updated on the client thread");
      }

      ClientLevel var2 = var1 == null ? null : var1.level;
      LocalPlayer var3 = var1 == null ? null : var1.player;
      Item var4 = this.m();
      if (var2 != this.g || var3 != this.l || !this.x().equals(this.X) || !this.B()) {
         this.c();
         this.g = var2;
         this.l = var3;
         this.i = var4;
         this.X = this.x();
         HashMap var5 = new HashMap();
         FakeSpawnerModule var6 = FakeSpawnerModule.w();
         if (var6 != null) {
            for (mb var10 : mb.values()) {
               String var11 = var6.w(var10.ordinal());
               if (var11 != null && !var11.isBlank()) {
                  var5.put(V(var11), var10);
               }
            }
         }

         this.Kf = Map.copyOf(var5);
         if (var2 != null && var3 != null && this.B()) {
            this.C = this.v(var1);
            this.e();
            this.h = (boolean)1;
         }
      }
   }

   private String v(Minecraft var1) {
      String var2 = var1.getCurrentServer() != null
         ? var1.getCurrentServer().ip
         : (
            var1.getSingleplayerServer() != null
               ? "singleplayer:" + var1.getSingleplayerServer().getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize()
               : null
         );
      return var2 == null ? null : var2 + "|" + var1.level.dimension().identifier() + "|" + var1.player.getUUID();
   }

   private void e() {
      if (this.C != null) {
         this.I(P());
      }
   }

   private void I(Path var1) {
      if (this.C != null && Files.exists(var1)) {
         try {
            JsonObject var2 = pi.Y(var1);
            if (var2.has("quarantined") && var2.get("quarantined").isJsonObject() && var2.getAsJsonObject("quarantined").has(this.C)) {
               for (JsonElement var4 : var2.getAsJsonObject("quarantined").getAsJsonArray(this.C)) {
                  this.P.add(var4.deepCopy());
               }
            }

            if (!var2.has(this.C)) {
               return;
            }

            int var20 = 0;

            for (JsonElement var5 : var2.getAsJsonArray(this.C)) {
               try {
                  JsonObject var6 = var5.getAsJsonObject();
                  long var7 = var6.get("pos").getAsLong();
                  mb var9 = mb.valueOf(var6.get("kind").getAsString());
                  int var10 = var6.get("count").getAsInt();
                  long var11 = var6.get("placedAt").getAsLong();
                  ArrayList var13 = new ArrayList();

                  for (JsonElement var15 : var6.getAsJsonArray("backing")) {
                     JsonObject var16 = var15.getAsJsonObject();
                     var13.add(
                        new lj.d(
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

                  if (var10 < 1 || var10 > 100000 || var10 > 1 && var13.isEmpty() || this.k.containsKey(var7)) {
                     throw new IllegalArgumentException("Invalid or conflicting saved pile");
                  }

                  this.k.put(var7, new lj.s(var7, var9, var10, var11, var13, var22));
               } catch (RuntimeException var18) {
                  var20++;
                  this.P.add(var5.deepCopy());
               }
            }

            if (var20 > 0) {
               r4.m("FakeSpawner", "Retaining {} unmatched pile records for recovery; no inventory was restored", var20);
            }
         } catch (Exception var19) {
            r4.r("FakeSpawner", "Pile load failed; retaining the file for recovery", var19);
         }
      }
   }

   private void R() {
      if (this.C != null) {
         this.R(P());
      }
   }

   private void R(Path var1) {
      if (this.C != null) {
         try {
            JsonObject var2 = Files.exists(var1) ? pi.Y(var1) : new JsonObject();
            JsonArray var3 = new JsonArray();
            if (var2.has(this.C) && !var2.get(this.C).isJsonArray()) {
               throw new IOException("Unrecognized saved pile data; original file retained");
            }

            if (var2.has("quarantined") && !var2.get("quarantined").isJsonObject()) {
               throw new IOException("Unrecognized pile quarantine data; original file retained");
            }

            JsonObject var4 = var2.has("quarantined") ? var2.getAsJsonObject("quarantined") : new JsonObject();
            if (var4.has(this.C) && !var4.get(this.C).isJsonArray()) {
               throw new IOException("Unrecognized retained pile records; original file retained");
            }

            JsonArray var5 = new JsonArray();

            for (JsonElement var7 : this.P) {
               var5.add(var7.deepCopy());
            }

            if (!var5.isEmpty()) {
               var4.add(this.C, var5);
               var2.add("quarantined", var4);
            }

            for (lj.s var18 : this.k.values()) {
               var3.add(var18.toJson());
            }

            for (Entry var19 : this.f.entrySet()) {
               long var8 = (Long)var19.getKey();
               if (!this.k.containsKey(var8) && (Integer)var19.getValue() >= 1) {
                  Deque var10 = this.O.get(var8);
                  if (var10 == null) {
                     var10 = new ArrayDeque();
                  }

                  if ((Integer)var19.getValue() <= 1 || !var10.isEmpty()) {
                     ArrayList var11 = new ArrayList();

                     for (lj.t var13 : var10) {
                        ItemStack var14 = this.D.get(var13.preferredSlot);
                        if (var14 == null || var14.isEmpty()) {
                           var11.clear();
                           break;
                        }

                        var11.add(
                           new lj.d(
                              var13.preferredSlot, BuiltInRegistries.ITEM.getKey(var13.stack.getItem()).toString(), var13.stack.getCount(), var14.getCount()
                           )
                        );
                     }

                     if (!var11.isEmpty() || (Integer)var19.getValue() == 1) {
                        HashMap var20 = new HashMap();
                        this.j.getOrDefault(var8, Map.of()).forEach((var1x, var2x) -> var20.put(BuiltInRegistries.ITEM.getKey(var1x).toString(), var2x));
                        var3.add(
                           new lj.s(
                                 var8,
                                 this.q(BlockPos.of(var8)),
                                 (Integer)var19.getValue(),
                                 this.B.getOrDefault(var8, System.currentTimeMillis()),
                                 var11,
                                 var20
                              )
                              .toJson()
                        );
                     }
                  }
               }
            }

            var2.add(this.C, var3);
            Files.createDirectories(var1.getParent());
            pi.U(var1, var2.toString());
         } catch (Exception var15) {
            r4.r("FakeSpawner", "Pile save failed; previous file retained", var15);
         }
      }
   }

   public void G() {
      this.h = (boolean)1;
   }

   private void s(Minecraft var1) {
      if (this.h && !this.k.isEmpty()) {
         this.h = (boolean)0;
         byte var2 = 0;

         for (lj.s var4 : new ArrayList<>(this.k.values())) {
            BlockPos var5 = BlockPos.of(var4.pos);
            if (var1.level.hasChunkAt(var5)) {
               LevelChunk var6 = var1.level.getChunkAt(var5);
               int var7 = var6.getSectionIndex(var5.getY());
               BlockState var8 = var7 >= 0 && var7 < var6.getSections().length
                  ? var6.getSection(var7).getBlockState(var5.getX() & 15, var5.getY() & 15, var5.getZ() & 15)
                  : Blocks.AIR.defaultBlockState();
               if (this.u(var8.getBlock().asItem()) == var4.kind) {
                  HashMap var9 = new HashMap();

                  for (lj.d var11 : var4.backing) {
                     if ((var11.slot < 0 || var11.slot >= 36) && var11.slot != 40
                        || var11.amount < 1
                        || var11.reserved < 1
                        || var11.amount > var11.reserved + 1
                        || var11.reserved > 64
                        || var9.containsKey(var11.slot)
                        || this.U(var11.slot)) {
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
                        mx.C();

                        for (lj.d var13 : var4.backing) {
                           ItemStack var14 = (ItemStack)var9.get(var13.slot);
                           this.D.put(var13.slot, var14);
                           var16.addLast(new lj.t(var13.slot, var14.copyWithCount(Math.min(var13.amount, var13.reserved))));
                        }

                        this.O.put(var4.pos, var16);
                        this.f.put(var4.pos, var4.count);
                        this.KJ.put(var4.pos, var4.kind);
                        this.B.put(var4.pos, var4.placedAt);
                        HashMap var18 = new HashMap();
                        var4.purchased.forEach((var1x, var2x) -> {
                           Identifier var3 = Identifier.tryParse(var1x);
                           if (var3 != null && BuiltInRegistries.ITEM.getValue(var3) != Items.AIR) {
                              var18.put((Item)BuiltInRegistries.ITEM.getValue(var3), var2x);
                           }
                        });
                        if (!var18.isEmpty()) {
                           this.j.put(var4.pos, var18);
                        }

                        this.k.remove(var4.pos);
                        this.Y(var4.pos);
                        this.v(var1.level, var5);
                        this.Y(var5);
                        mx.C();
                        var2 = 1;
                     }
                  }
               }
            }
         }

         if (var2 != 0) {
            this.R();
         }
      }
   }

   private lj() {
   }

   public static lj j() {
      return K_;
   }

   public boolean B() {
      FakeSpawnerModule var1 = FakeSpawnerModule.w();
      return var1 != null && var1.isRuntimeActive();
   }

   public boolean u(ItemStack var1) {
      return this.B() && this.G(var1);
   }

   public boolean G(ItemStack var1) {
      return this.q(var1) && (this.L.contains(var1) || this.A.contains(var1) || ja.Q(var1));
   }

   private boolean q(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && !hr.d(var1) && this.u(var1.getItem()) != null;
   }

   private String x() {
      FakeSpawnerModule var1 = FakeSpawnerModule.w();
      return var1.w(0) + "|" + var1.w(1) + "|" + var1.w(2);
   }

   public mb u(Item var1) {
      FakeSpawnerModule var2 = FakeSpawnerModule.w();

      for (mb var6 : mb.values()) {
         String var7 = var2.w(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1 == V(var7)) {
            return var6;
         }
      }

      return null;
   }

   public mb q(BlockPos var1) {
      return var1 == null ? mb.v : this.KJ.getOrDefault(var1.asLong(), mb.v);
   }

   private Item B(long var1) {
      return V(FakeSpawnerModule.w().w(this.q(BlockPos.of(var1)).ordinal()));
   }

   public Item m() {
      FakeSpawnerModule var1 = FakeSpawnerModule.w();
      return V(var1 == null ? null : var1.G());
   }

   public static Item V(String var0) {
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

   private Block L() {
      return this.m() instanceof BlockItem var1 ? var1.getBlock() : Blocks.COBBLESTONE;
   }

   public boolean K(BlockState var1) {
      return this.B() && var1 != null ? this.u(var1.getBlock().asItem()) != null || var1.is(Blocks.SPAWNER) : false;
   }

   public void J(BlockPos var1) {
      this.Q();
      this.w.add(var1.asLong());
      this.f.remove(var1.asLong());
      this.Y(var1.asLong());
      this.Y(var1);
   }

   public BlockState h(BlockPos var1, BlockState var2) {
      return var1 == null ? var2 : this.i(var1.asLong(), var2);
   }

   public BlockState i(long var1, BlockState var3) {
      if (!this.m.contains(var1) || !this.X() || var3 == null || this.G.contains(var1)) {
         return var3;
      }

      if (this.N.containsKey(var1)) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.w.contains(var1)) {
         return var3;
      }

      if (!this.I.contains(var1) || !var3.is(Blocks.SPAWNER) && !this.Kf.containsKey(var3.getBlock().asItem())) {
         int var4 = this.f.getOrDefault(var1, 0);
         if (var4 <= 0) {
            return var3;
         } else {
            return !var3.isAir() && !var3.is(Blocks.SPAWNER) && !this.Kf.containsKey(var3.getBlock().asItem()) ? var3 : Blocks.SPAWNER.defaultBlockState();
         }
      } else {
         return Blocks.AIR.defaultBlockState();
      }
   }

   public boolean X() {
      return (!this.f.isEmpty() || !this.I.isEmpty() || !this.N.isEmpty()) && this.B() && this.g == Minecraft.getInstance().level;
   }

   private void A() {
      LongOpenHashSet var1 = new LongOpenHashSet();

      for (long var3 : this.f.keySet()) {
         var1.add(var3);
      }

      for (long var9 : this.I) {
         var1.add(var9);
      }

      for (long var10 : this.N.keySet()) {
         var1.add(var10);
      }

      this.m = LongSets.unmodifiable(var1);
      LongOpenHashSet var8 = new LongOpenHashSet();

      for (long var4 : this.Kq.keySet()) {
         var8.add(var4);
      }

      this.Q = LongSets.unmodifiable(var8);
   }

   public void V(BlockPos var1, BlockState var2) {
      this.Q();
      if (var1 != null) {
         long var3 = var1.asLong();
         if (this.J(var3)) {
            this.c = 0;
            this.Y(var1);
            r4.g("FakeSpawner", "Final break {} at {}; server state={}", var2 != null && var2.isAir() ? "confirmed" : "corrected", var1, var2);
         }

         if (this.w.contains(var3)) {
            if (var2 != null && !var2.isAir()) {
               return;
            }

            jy.M(var1);
            this.w.remove(var3);
         }

         if (this.I.contains(var3)) {
            if (var2 != null && this.K(var2)) {
               this.c = 0;
            }

            this.H.put(var3, new lj.y(var1.immutable(), System.currentTimeMillis() + 10000L));
            this.I.remove(var3);
            this.Y(var1);
         }

         int var5 = this.f.getOrDefault(var3, 0);
         if (var2 != null && !var2.isAir()) {
            if (this.B()) {
               if (this.K(var2)) {
                  if (var5 <= 0 && !this.v.containsKey(var3)) {
                     return;
                  }

                  mb var6 = this.u(var2.getBlock().asItem());
                  if (var6 != null && this.KJ.put(var3, var6) != var6) {
                     this.Y(var3);
                  }

                  this.B.putIfAbsent(var3, System.currentTimeMillis());
                  lj.c var7 = this.v.remove(var3);
                  int var8 = var7 == null ? this.f.getOrDefault(var3, 1) : Math.max(1, var7.stackCount);
                  this.f.put(var3, Math.max(1, var8));
                  this.k("track:" + var3, 1500L, "trackWorldState fake pos={} state={} resolvedCount={} pending={}", var1, var2.getBlock(), var8, var7 != null);
                  if (var7 != null && var7.hiddenStack != null) {
                     this.Z(var3, var7.hiddenStack, true);
                  }

                  if (var7 != null) {
                     this.R();
                  }

                  this.v(Minecraft.getInstance() == null ? null : Minecraft.getInstance().level, var1);
                  if (var5 <= 0) {
                     this.Y(var1);
                  }
               } else {
                  this.V(var1, null);
               }
            }
         } else {
            if (var5 > 0) {
               this.g("trackWorldState removed pos={} previousCount={} state={}", var1, var5, var2);
            }

            if (var5 > 0 && !this.K(var1)) {
               this.X(var1);
            } else if (this.K(var1)) {
               this.W(var3, Integer.MAX_VALUE);
               mx.u();
               this.g("Placement rejected at {}; reservations released without inventory replay", var1);
            }

            this.B.remove(var3);
            this.j.remove(var3);
            this.f.remove(var3);
            this.v.remove(var3);
            this.O.remove(var3);
            this.Y(var3);
            if (var5 > 0) {
               this.Y(var1);
            }

            this.p.remove(var3);
            this.z.remove(var3);
            this.b.remove(var3);
            this.v(var1);
            if (var1.equals(this.a)) {
               this.a = null;
            }
         }
      }
   }

   public void D(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.Q();
      if (this.B() && var1 != null && this.G(var2)) {
         lj.s var5 = this.k.remove(var1.asLong());
         if (var5 != null) {
            this.P.add(var5.toJson());
            this.g("New placement at {} archives a superseded saved pile for recovery", var1);
         }

         this.KJ.put(var1.asLong(), this.u(var2.getItem()));
         ItemStack var6 = var4 ? var2.copy() : ItemStack.EMPTY;
         int var7 = var4 ? var2.getCount() : 1;
         this.v.put(var1.asLong(), new lj.c(var7, new lj.t(var3, var6), System.currentTimeMillis()));
      }
   }

   public void L(BlockPos var1, BlockState var2) {
      this.Q();
      lj.c var3 = this.v.get(var1.asLong());
      if (var3 != null && this.K(var2)) {
         this.I.remove(var1.asLong());
         this.f.put(var1.asLong(), var3.stackCount);
         this.B.putIfAbsent(var1.asLong(), var3.createdAtMs);
         this.v(this.g, var1);
         this.Y(var1);
      }
   }

   public void U() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.B() && var1.player != null) {
         for (Entry var3 : new ArrayList<>(this.v.entrySet())) {
            if (this.f.containsKey(var3.getKey())) {
               lj.c var4 = (lj.c)var3.getValue();
               if (var4.hiddenStack != null && !var4.hiddenStack.stack.isEmpty()) {
                  this.Z((Long)var3.getKey(), var4.hiddenStack, true);
               }

               this.f.put((Long)var3.getKey(), var4.stackCount);
            }
         }
      }
   }

   public void M() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null && this.B() && var1.level == this.g && var1.player == this.l && !rm.L()) {
         this.b(var1.player.getInventory());
         this.n
            .entrySet()
            .removeIf(
               var1x -> {
                  ItemStack var2 = var1.player.getInventory().getItem(var1x.getKey());
                  lj.p var3x = var1x.getValue();
                  return var1.player.tickCount >= var3x.expires
                     || !var2.isEmpty() && !var2.is(var3x.stack.getItem())
                     || var2.is(var3x.stack.getItem()) && var2.getCount() >= var3x.expected;
               }
            );

         for (Entry var3 : this.D.entrySet()) {
            if (!this.v.values().stream().anyMatch(var1x -> var1x.hiddenStack != null && var1x.hiddenStack.preferredSlot == (Integer)var3.getKey())) {
               ItemStack var4 = var1.player.getInventory().getItem((Integer)var3.getKey());
               if (var4.is(((ItemStack)var3.getValue()).getItem())
                  && var4.getCount() > ((ItemStack)var3.getValue()).getCount()
                  && this.s((Integer)var3.getKey(), var4.getCount() - ((ItemStack)var3.getValue()).getCount())) {
                  break;
               }
            }
         }
      }
   }

   void b(Container var1) {
      Iterator var2 = this.D.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         int var4 = (Integer)var3.getKey();
         ItemStack var5 = var1.getItem(var4);
         ItemStack var6 = (ItemStack)var3.getValue();
         if (var5.is(var6.getItem()) && var5.getCount() >= var6.getCount()) {
            this.c(var4, var5);
         } else {
            int var7 = var5.is(var6.getItem()) ? var5.getCount() : 0;
            int var8 = var6.getCount() - var7;
            if (var7 == 0) {
               var2.remove();
            } else {
               var3.setValue(var5.copyWithCount(var7));
            }

            for (Entry var10 : this.O.entrySet()) {
               ArrayDeque var11 = new ArrayDeque();

               for (lj.t var13 : (Deque)var10.getValue()) {
                  int var14 = var13.preferredSlot == var4 ? Math.min(var8, var13.stack.getCount()) : 0;
                  var8 -= var14;
                  if (var14 > 0) {
                     this.f.computeIfPresent((Long)var10.getKey(), (var1x, var2x) -> Math.max(1, var2x - var14));
                  }

                  if (var13.stack.getCount() > var14) {
                     var11.addLast(new lj.t(var13.preferredSlot, var13.stack.copyWithCount(var13.stack.getCount() - var14)));
                  }
               }

               ((Deque)var10.getValue()).clear();
               ((Deque)var10.getValue()).addAll(var11);
            }

            r4.g(
               "FakeSpawner",
               "Reservation corrected physical={} item={} old={} available={}; real inventory retained",
               var4,
               BuiltInRegistries.ITEM.getKey(var6.getItem()),
               var6.getCount(),
               var7
            );
            mx.u();
         }
      }
   }

   private boolean s(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = this.d();
      return var4 >= 0 && var3.player != null
         ? rm.H("separate reservation surplus", rm.M(var1, var4, var2, var3.player.getInventory().getItem(var1).getCount()), mx::u, mx::u)
         : false;
   }

   public boolean K(BlockPos var1) {
      return var1 != null && this.v.containsKey(var1.asLong());
   }

   public boolean c(BlockPos var1) {
      return this.B() && this.g == Minecraft.getInstance().level && var1 != null && this.I.contains(var1.asLong());
   }

   public void r(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 != null) {
         this.F = var1;
         this.Z = var2.getInventory().getSelectedSlot();
         this.q = var2.tickCount + 4;
      }
   }

   public boolean C(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return this.B()
         && var2 == this.l
         && var1 == this.F
         && var2.tickCount <= this.q
         && var2.getInventory().getSelectedSlot() == this.Z
         && this.G(var2.getItemInHand(var1));
   }

   public void g(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.B() && var2.level != null && var2.level.getBlockState(var1).isAir()) {
         if (this.Kq.put(var1.asLong(), var2.level.getGameTime() + 10L) == null) {
            this.Y(var1);
         }
      }
   }

   public BlockState r(BlockPos var1, BlockState var2) {
      return this.E(var1.asLong(), var2);
   }

   public BlockState E(long var1, BlockState var3) {
      return this.Q.contains(var1) && var3.isAir() && this.B() && this.g == Minecraft.getInstance().level ? Blocks.SPAWNER.defaultBlockState() : var3;
   }

   public boolean W(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.Q();
      if (this.B() && var1 != null && !this.K(var1) && this.J(var1) > 0 && this.G(var2) && this.u(var2.getItem()) == this.q(var1) && !this.U(var3) && !rm.L()) {
         long var5 = var1.asLong();
         int var7 = var4 ? var2.getCount() : 1;
         if (var7 < var2.getCount()) {
            int var8 = this.d();
            if (var8 < 0) {
               return false;
            }

            Item var9 = var2.getItem();
            return rm.H("single pile addition", rm.M(var3, var8, var7, var2.getCount()), () -> {
               Minecraft var6 = Minecraft.getInstance();
               ItemStack var7x = var6.player.getInventory().getItem(var8);
               if (this.f.containsKey(var5) && var7x.is(var9) && var7x.getCount() == var7) {
                  this.a(var5, var8, var7x, var7);
               }
            }, mx::u);
         } else {
            return this.a(var5, var3, var2, var7);
         }
      } else {
         return false;
      }
   }

   private boolean a(long var1, int var3, ItemStack var4, int var5) {
      if (!this.Z(var1, new lj.t(var3, var4.copy()), false)) {
         return false;
      }

      int var6 = (int)Math.min(2147483647L, (long)this.f.getOrDefault(var1, 1).intValue() + var5);
      this.f.put(var1, var6);
      this.B.putIfAbsent(var1, System.currentTimeMillis());
      this.M(Minecraft.getInstance().level, BlockPos.of(var1));
      this.O("You just added another spawner! There are now " + var6 + " spawners in the stack.");
      this.R();
      return true;
   }

   public boolean K() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null && var1.getMainHandItem().is(ItemTags.PICKAXES)) {
         ItemEnchantments var2 = (ItemEnchantments)var1.getMainHandItem().get(DataComponents.ENCHANTMENTS);
         Reference var3 = var1.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
         return var2 != null && var2.getLevel(var3) > 0;
      } else {
         return false;
      }
   }

   public boolean a(BlockPos var1, Direction var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.G(var1)) {
         return false;
      }

      long var4 = var1.asLong();
      if (this.N.containsKey(var4)) {
         return true;
      }

      if (this.K()) {
         this.E.remove(var4);
         this.b.remove(var4);
         this.r.remove(var4);
         return false;
      }

      if (this.r.contains(var4)) {
         return true;
      }

      int var6 = var3.player.tickCount;
      long var7 = var1.asLong();
      if (this.T.getOrDefault(var7, -1) == var6) {
         return true;
      }

      this.T.put(var7, var6);
      this.E.computeIfAbsent(var4, var1x -> var6);
      double var9 = var3.player.getAbilities().instabuild ? 1.0 : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
      double var11 = Math.min(1.0, this.b.getOrDefault(var4, 0.0) + var9);
      this.b.put(var4, var11);
      this.W(var3, var1, var2, var11);
      if (var11 >= 1.0) {
         this.E.remove(var4);
         this.b.remove(var4);
         this.r.add(var4);
         this.N.put(var4, var6 + 4);
         this.v(var1);
         this.C(var1, var2);
         this.O(var1, true);
         this.Y(var1);
      }

      return true;
   }

   private void O() {
      Minecraft var1 = Minecraft.getInstance();

      for (Entry var3 : new ArrayList<>(this.N.entrySet())) {
         if (var1.player.tickCount >= (Integer)var3.getValue()) {
            this.N.remove(var3.getKey());
            this.Y(BlockPos.of((Long)var3.getKey()));
            this.O("You cannot break spawners without a silk touch!");
         }
      }

      if (!var1.options.keyAttack.isDown()) {
         for (long var6 : this.E.keySet()) {
            this.v(BlockPos.of(var6));
         }

         this.E.clear();
         this.b.clear();
         this.r.clear();
      } else {
         this.E.keySet().removeIf(var2 -> {
            int var3x = var1.hitResult instanceof BlockHitResult var4 && var4.getBlockPos().asLong() == var2 ? 0 : 1;
            if (var3x != 0) {
               this.v(BlockPos.of(var2));
            }

            return (boolean)var3x;
         });
      }
   }

   public boolean f(BlockPos var1) {
      if (this.a(var1, null)) {
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      if (this.B() && var2 != null && var2.level != null && var2.player != null && var1 != null) {
         if (this.K(var2.level.getBlockState(var1)) && this.J(var1) > this.K()) {
            long var3 = var1.asLong();
            this.p.putIfAbsent(var3, 0.0F);
            this.W(var2, var1, null, 0.0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean z(BlockPos var1, Direction var2) {
      if (this.a(var1, var2)) {
         return true;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.B() && var3 != null && var3.level != null && var3.player != null && var1 != null) {
         if (this.G(var1) && var3.player.tickCount < this.c) {
            return true;
         }

         if (this.K(var3.level.getBlockState(var1)) && this.J(var1) > this.K()) {
            long var4 = var1.asLong();
            int var6 = var3.player.tickCount;
            if (this.T.getOrDefault(var4, -1) == var6) {
               return true;
            }

            this.T.put(var4, var6);
            float var7 = var3.player.getAbilities().instabuild ? 1.0F : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
            float var8 = Math.min(1.0F, this.p.getOrDefault(var4, 0.0F) + var7);
            this.p.put(var4, var8);
            this.W(var3, var1, var2, var8);
            if (var8 >= 1.0F) {
               if (this.X(var1, this.K())) {
                  this.c = var6 + 5;
                  this.O(var1, true);
               }

               this.p.remove(var4);
               this.z.remove(var4);
               this.v(var1);
            }

            return true;
         } else {
            if (var1 != null) {
               this.p.remove(var1.asLong());
               this.z.remove(var1.asLong());
               this.v(var1);
            }

            return false;
         }
      } else {
         if (var1 != null) {
            this.p.remove(var1.asLong());
            this.z.remove(var1.asLong());
            this.v(var1);
         }

         return false;
      }
   }

   public void X(BlockPos var1) {
      if (var1 != null && this.w.contains(var1.asLong())) {
         jy.M(var1);
      }

      if (var1 != null && this.I.contains(var1.asLong())) {
         this.H.put(var1.asLong(), new lj.y(var1.immutable(), System.currentTimeMillis() + 10000L));
      }

      if (var1 != null && this.J(var1) > 0) {
         this.H.put(var1.asLong(), new lj.y(var1.immutable(), System.currentTimeMillis() + 10000L));
         int var2 = this.f.getOrDefault(var1.asLong(), 0);
         lj.t var3 = this.W(var1.asLong(), var2);
         this.w(var1.asLong(), var2);
         this.i(new ItemStack(this.B(var1.asLong()), 1), 1);
         mx.u();
         this.p.remove(var1.asLong());
         this.z.remove(var1.asLong());
         this.v(var1);
      }
   }

   private int K() {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player == null || !var1.player.isShiftKeyDown() && !var1.options.keyShift.isDown() ? 1 : 64;
   }

   public boolean g(BlockPos var1) {
      return this.X(var1, 64);
   }

   private boolean X(BlockPos var1, int var2) {
      if (this.K(var1)) {
         return false;
      }

      if (this.G(var1)) {
         long var3 = var1.asLong();
         int var5 = this.f.getOrDefault(var3, 0);
         if (var5 <= 0) {
            return false;
         }

         int var6 = Math.min(var2, var5);
         Deque var7 = this.O.get(var3);
         if (var7 != null && !var7.isEmpty()) {
            ItemStack var8 = Minecraft.getInstance().player.getInventory().getItem(((lj.t)var7.getLast()).preferredSlot);
            int var9 = 0;

            for (lj.t var11 : var7.reversed()) {
               if (!ItemStack.isSameItemSameComponents(var8, Minecraft.getInstance().player.getInventory().getItem(var11.preferredSlot))) {
                  break;
               }

               var9 += var11.stack.getCount();
            }

            if (var9 < var6 && var9 < var5 - 1) {
               var6 = var9;
            }
         }

         if (!this.o(var3, var6)) {
            return false;
         }

         this.w(var3, var6);
         return true;
      } else {
         return false;
      }
   }

   private void w(long var1, int var3) {
      int var4 = Math.max(0, this.f.getOrDefault(var1, 0) - var3);
      BlockPos var5 = BlockPos.of(var1);
      if (var4 > 0) {
         this.f.put(var1, var4);
         this.B.put(var1, System.currentTimeMillis());
      } else {
         this.I.add(var1);
         this.f.remove(var1);
         this.v.remove(var1);
         this.B.remove(var1);
         this.j.remove(var1);
         this.O.remove(var1);
         this.Y(var1);
         this.Y(var5);
         if (var5.equals(this.a)) {
            this.a = null;
         }
      }

      this.H.put(var1, new lj.y(var5.immutable(), System.currentTimeMillis() + 10000L));
      this.R();
   }

   public boolean G(BlockPos var1) {
      if (var1 == null) {
         return false;
      }

      long var2 = var1.asLong();
      return this.f.getOrDefault(var2, 0) > 0 && !this.G.contains(var2) && this.g == Minecraft.getInstance().level && this.B();
   }

   public lj.a Y(BlockPos var1, SoundEvent var2, float var3, float var4) {
      if (var1 != null && var2 != null && this.G(var1)) {
         BlockState var5 = ((BlockItem)this.B(var1.asLong())).getBlock().defaultBlockState();
         if (var5 == null) {
            return null;
         } else {
            SoundType var6 = var5.getSoundType();
            SoundType var7 = Blocks.SPAWNER.defaultBlockState().getSoundType();
            if (S(var2, var6.getPlaceSound())) {
               return new lj.a(var7.getPlaceSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else if (S(var2, var6.getBreakSound())) {
               return new lj.a(var7.getBreakSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else {
               return S(var2, var6.getHitSound()) ? new lj.a(var7.getHitSound(), (var7.getVolume() + 1.0F) / 8.0F, var7.getPitch() * 0.5F) : null;
            }
         }
      } else {
         return null;
      }
   }

   private static boolean S(SoundEvent var0, SoundEvent var1) {
      return var0 == var1 || var0.location().equals(var1.location());
   }

   private void M(Level var1, BlockPos var2) {
      if (var1 != null && var2 != null && this.G(var2)) {
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

   private void O(BlockPos var1, boolean var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null && this.G(var1)) {
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

   public BlockState M(BlockPos var1, BlockState var2) {
      return this.h(var1, var2);
   }

   public SpawnerBlockEntity v(Level var1, BlockPos var2) {
      Map var3 = this.W;
      if (var1 != null && var1 == this.g && this.G(var2) && !this.N.containsKey(var2.asLong())) {
         long var4 = var2.asLong();
         SpawnerBlockEntity var6 = (SpawnerBlockEntity)var3.get(var4);
         if (var6 == null && Minecraft.getInstance().isSameThread()) {
            var6 = new SpawnerBlockEntity(var2.immutable(), Blocks.SPAWNER.defaultBlockState());
            var6.setEntityId(this.q(var2).u, var1.getRandom());
            var6.setLevel(var1);
            var6.getSpawner().getOrCreateDisplayEntity(var1, var2);
            var3.put(var4, var6);
            ((List)this.V.computeIfAbsent(ChunkPos.pack(var2.getX() >> 4, var2.getZ() >> 4), var0 -> new ArrayList())).add(var6);
         }

         return var3 == this.W && var1 == this.g && this.G(var2) ? var6 : null;
      } else {
         return null;
      }
   }

   private void Y(BlockPos var1) {
      this.A();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var2.level == this.g) {
         this.t++;
         var2.levelExtractor.setBlocksDirty(var1.getX() - 1, var1.getY() - 1, var1.getZ() - 1, var1.getX() + 1, var1.getY() + 1, var1.getZ() + 1);
      }
   }

   public Collection<SpawnerBlockEntity> u(Level var1) {
      return var1 == this.g && this.B() ? this.W.values() : List.of();
   }

   private void Y(long var1) {
      SpawnerBlockEntity var3 = this.W.remove(var1);
      if (var3 != null) {
         BlockPos var4 = var3.getBlockPos();
         long var5 = ChunkPos.pack(var4.getX() >> 4, var4.getZ() >> 4);
         List var7 = (List)this.V.get(var5);
         if (var7 != null) {
            var7.remove(var3);
            if (var7.isEmpty()) {
               this.V.remove(var5);
            }
         }
      }
   }

   public Collection<SpawnerBlockEntity> F(Level var1, Vec3 var2, int var3) {
      if (var1 == this.g && this.B() && !this.V.isEmpty()) {
         ArrayList var4 = null;
         int var5 = Mth.floor(var2.x - var3) >> 4;
         int var6 = Mth.floor(var2.x + var3) >> 4;
         int var7 = Mth.floor(var2.z - var3) >> 4;
         int var8 = Mth.floor(var2.z + var3) >> 4;

         for (int var9 = var5; var9 <= var6; var9++) {
            for (int var10 = var7; var10 <= var8; var10++) {
               List var11 = (List)this.V.get(ChunkPos.pack(var9, var10));
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

   public boolean d(BlockEntity var1) {
      if (var1 instanceof SpawnerBlockEntity var2) {
         BlockPos var3 = var2.getBlockPos();
         if (var3 == null) {
            return false;
         }

         SpawnerBlockEntity var4 = this.W.get(var3.asLong());
         return var4 == var1;
      } else {
         return false;
      }
   }

   public void T(String var1, BlockPos var2, BlockEntity var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 != null) {
            this.k(
               "request:" + var1 + ":" + var2.asLong(),
               1500L,
               "{} requested fake block entity pos={} result={} count={}",
               var1,
               var2,
               var3 != null ? var3.getClass().getSimpleName() : "null",
               this.J(var2)
            );
         }
      }
   }

   public void h(String var1, BlockPos var2, String var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 == null) {
            this.k("render:" + var1, 1500L, "renderer {} {}", var1, var3);
         } else {
            this.k("render:" + var1 + ":" + var2.asLong(), 1500L, "renderer {} pos={} count={} {}", var1, var2, this.J(var2), var3);
         }
      }
   }

   public void n(LevelChunk var1, Collection<BlockPos> var2) {
      this.Q();
      this.G();
      if (this.B() && var1 != null) {
         int var3 = var1.getPos().x();
         int var4 = var1.getPos().z();
         int var5 = var3 << 4;
         int var6 = var4 << 4;
         int var7 = var5 + 15;
         int var8 = var6 + 15;
         MutableBlockPos var9 = new MutableBlockPos();
         HashSet var10 = new HashSet<>(this.f.keySet());
         var10.addAll(this.I);

         for (Long var12 : var10) {
            BlockPos var13 = BlockPos.of(var12);
            int var14 = var13.getX();
            int var15 = var13.getZ();
            if (var14 >= var5 && var14 <= var7 && var15 >= var6 && var15 <= var8) {
               var9.set(var14, var13.getY(), var15);
               if (!var1.getLevel().isOutsideBuildHeight(var9)) {
                  this.V(var9, var1.getSection(var1.getSectionIndex(var9.getY())).getBlockState(var9.getX() & 15, var9.getY() & 15, var9.getZ() & 15));
               }
            }
         }

         if (var2 != null) {
            for (BlockPos var17 : new ArrayList(var2)) {
               if (var17 != null) {
                  this.V(var17, var1.getBlockState(var17));
               }
            }
         }
      }
   }

   public void L() {
      long var1 = Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
      this.Q();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var3.player != null && this.B()) {
         this.s(var3);
         this.T();

         for (Entry var5 : this.Kq.entrySet()) {
            if (var3.level.getGameTime() >= (Long)var5.getValue()) {
               this.Kq.remove(var5.getKey());
               this.Y(BlockPos.of((Long)var5.getKey()));
            }
         }

         this.O();
         if (!this.H.isEmpty() || !this.p.isEmpty() || !this.I.isEmpty()) {
            for (Entity var6 : var3.level.entitiesForRendering()) {
               if (var6 instanceof ItemEntity var8) {
                  this.o(var8);
               }
            }
         }

         this.g();
         this.H.entrySet().removeIf(var0 -> var0.getValue().untilMs < System.currentTimeMillis());
         this.U.removeIf(var1x -> !(var3.level.getEntity(var1x) instanceof ItemEntity));
         rm.P();
         this.M();

         for (Entry var17 : new ArrayList<>(this.v.entrySet())) {
            if (!this.f.containsKey(var17.getKey()) && System.currentTimeMillis() - ((lj.c)var17.getValue()).createdAtMs > 5000L) {
               this.V(BlockPos.of((Long)var17.getKey()), null);
            }
         }

         if (!var3.options.keyAttack.isDown()) {
            for (Long var18 : new ArrayList<>(this.p.keySet())) {
               this.v(BlockPos.of(var18));
            }

            this.p.clear();
            this.z.clear();
         }

         for (Long var19 : new ArrayList<>(this.p.keySet())) {
            if (!(var3.hitResult instanceof BlockHitResult var22 && var22.getBlockPos().equals(BlockPos.of(var19)))) {
               this.v(BlockPos.of(var19));
               this.p.remove(var19);
               this.z.remove(var19);
            }
         }

         if (var3 != null && var3.level != null && var3.player != null && this.B()) {
            for (BlockEntity var21 : this.F(var3.level, var3.player.position(), 32)) {
               if (var21 instanceof SpawnerBlockEntity var23
                  && var23.getBlockPos() != null
                  && var3.level.hasChunkAt(var23.getBlockPos())
                  && this.G(var23.getBlockPos())
                  && var23.getBlockPos().distToCenterSqr(var3.player.position()) <= 1024.0) {
                  SpawnerBlockEntity.clientTick(var3.level, var23.getBlockPos(), var23.getBlockState(), var23);
               }
            }

            long var14 = var3.level.getGameTime();

            for (BlockPos var26 : this.P(var3.player.blockPosition(), 16, 8)) {
               if ((var14 + Math.floorMod(var26.asLong(), 3L)) % 20L == 0L) {
                  this.P(var3.level, var26);
               }
            }
         } else {
            for (Long var20 : new ArrayList<>(this.p.keySet())) {
               this.v(BlockPos.of(var20));
            }

            this.p.clear();
            this.z.clear();
         }

         if (var1 != 0L) {
            long var15 = System.nanoTime() - var1;
            long var25 = System.currentTimeMillis();
            if (var25 >= this.R) {
               this.R = var25 + 5000L;
               this.g(
                  "Work counters: piles={} reservations={} mappingRebuilds={} terrainInvalidations={} inventoryUpdates={} renderCandidates={} renderExtractions={} displayCopies={} tickMicros={}",
                  this.f.size(),
                  this.D.size(),
                  mx.U(),
                  this.t,
                  this.Ky,
                  this.y,
                  this.o,
                  this.M,
                  var15 / 1000L
               );
            }

            if (var15 > 5000000L) {
               this.k("tick-spike", 2000L, "Client tick CPU spike micros={} piles={} reservations={}", var15 / 1000L, this.f.size(), this.D.size());
            }
         }
      } else {
         this.c();
      }
   }

   public int J(BlockPos var1) {
      return !this.G(var1) ? 0 : this.f.getOrDefault(var1.asLong(), 0);
   }

   public List<BlockPos> P(BlockPos var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();
      if (this.B() && var1 != null && !this.f.isEmpty()) {
         int var5 = var1.getX();
         int var6 = var1.getY();
         int var7 = var1.getZ();

         for (SpawnerBlockEntity var9 : this.F(this.g, Vec3.atCenterOf(var1), var2 + 1)) {
            BlockPos var10 = var9.getBlockPos();
            if (this.G(var10) && Math.abs(var10.getX() - var5) <= var2 && Math.abs(var10.getY() - var6) <= var3 && Math.abs(var10.getZ() - var7) <= var2) {
               var4.add(var10);
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   public boolean o(ItemEntity var1) {
      if (this.B() && var1 != null && this.q(var1.getItem()) && !ja.C(var1.getItem())) {
         long var2 = System.currentTimeMillis();
         if (this.U.contains(var1.getId())) {
            return true;
         }

         HashSet var4 = new HashSet<>(this.I);
         var4.addAll(this.p.keySet());

         for (long var6 : var4) {
            BlockPos var8 = BlockPos.of(var6);
            if (var1.distanceToSqr(var8.getX() + 0.5, var8.getY() + 0.5, var8.getZ() + 0.5) <= 4.84) {
               this.U.add(var1.getId());
               return true;
            }
         }

         this.H.entrySet().removeIf(var2x -> var2x.getValue().untilMs < var2);

         for (lj.y var14 : this.H.values()) {
            double var7 = var14.pos.getX() + 0.5 - var1.getX();
            double var9 = var14.pos.getY() + 0.5 - var1.getY();
            double var11 = var14.pos.getZ() + 0.5 - var1.getZ();
            if (var7 * var7 + var9 * var9 + var11 * var11 <= 4.840000000000001) {
               this.U.add(var1.getId());
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean H(ItemEntity var1) {
      return this.o(var1);
   }

   public ItemStack x(ItemStack var1) {
      if (!this.u(var1)) {
         return var1;
      }

      ItemStack var2 = new ItemStack(Items.SPAWNER, var1.getCount());
      var2.set(DataComponents.CUSTOM_NAME, this.O(var1));
      var2.set(DataComponents.LORE, new ItemLore(List.of(b(this.u(var1.getItem()).x, ChatFormatting.YELLOW))));
      if (this.O()) {
         var2.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
      }

      return var2;
   }

   public ItemStack M(ItemStack var1) {
      return var1;
   }

   public boolean p(ItemStack var1) {
      return this.B() && var1 != null && !var1.isEmpty() && !hr.d(var1) && this.y(var1) && this.x(var1) > 0L;
   }

   public Component f(long var1) {
      return b("~", ChatFormatting.DARK_GRAY).append(t("$ ", 65280)).append(b(this.m(var1), ChatFormatting.WHITE));
   }

   public Component f(ItemStack var1) {
      return this.w(this.n(var1));
   }

   public Component a(ItemStack var1) {
      long var2 = this.x(var1);
      long var4 = Math.max(1L, (var2 + 63L) / 64L);
      return b("Contains: " + var4 + " stack(s)", ChatFormatting.WHITE);
   }

   public Component K() {
      return b("Left Shift", ChatFormatting.GOLD).append(b(": ", ChatFormatting.GOLD)).append(b("view contents", ChatFormatting.WHITE));
   }

   public Component u() {
      return b("Left Alt + Left Shift", ChatFormatting.GOLD).append(b(": ", ChatFormatting.GOLD)).append(b("view contents", ChatFormatting.WHITE));
   }

   public Component d() {
      return b("Left Control", ChatFormatting.GOLD).append(b(": ", ChatFormatting.GOLD)).append(b("lock tooltip", ChatFormatting.WHITE));
   }

   public List<Component> z(ItemStack var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1.getHoverName());
      var3.add(this.f(var1));
      if (!var2) {
         return var3;
      } else {
         boolean var4 = this.f(341);
         boolean var5 = this.f(340);
         boolean var6 = this.f(342);
         if (var4) {
            return var3;
         } else if (var5 && var6) {
            var3.add(this.d());
            return var3;
         } else if (var5) {
            var3.add(this.a(var1));
            var3.add(this.u());
            var3.add(this.d());
            return var3;
         } else {
            var3.add(this.a(var1));
            var3.add(this.K());
            return var3;
         }
      }
   }

   public Component B() {
      FakeSpawnerModule var1 = FakeSpawnerModule.w();
      String var2 = var1 != null ? var1.h() : "§dSpawner";
      return O(var2);
   }

   public Component O(ItemStack var1) {
      Component var2 = (Component)var1.get(DataComponents.CUSTOM_NAME);
      if (var2 != null) {
         return Component.literal(var2.getString()).withStyle(new ChatFormatting[]{ChatFormatting.WHITE, ChatFormatting.ITALIC});
      }

      mb var3 = this.u(var1.getItem());
      return b("Spawner", ChatFormatting.LIGHT_PURPLE);
   }

   public List<Component> x(int var1, boolean var2, boolean var3) {
      return this.w(new ItemStack(this.m(), var1), var2, var3);
   }

   public List<Component> w(ItemStack var1, boolean var2, boolean var3) {
      int var4 = var1.getCount();
      ArrayList var5 = new ArrayList();
      var5.add(b(this.u(var1.getItem()).x, ChatFormatting.YELLOW));
      if (var2) {
         var5.add(b("~", ChatFormatting.DARK_GRAY).append(t("$ ", 65280)).append(b(this.u(var4), ChatFormatting.WHITE)));
      }

      var5.add(Component.empty());
      var5.add(b("Interact with Spawn Egg:", ChatFormatting.GRAY));
      var5.add(b(" Sets Mob Type", ChatFormatting.BLUE));
      if (var3) {
         var5.add(b("minecraft:spawner", ChatFormatting.DARK_GRAY));
         var5.add(b("13 component(s)", ChatFormatting.DARK_GRAY));
      }

      return var5;
   }

   public boolean J() {
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

   public boolean O() {
      FakeSpawnerModule var1 = FakeSpawnerModule.w();
      return var1 != null && var1.F();
   }

   public void q() {
      this.V(this.a);
   }

   public void V(BlockPos var1) {
      this.Q();
      Minecraft var2 = Minecraft.getInstance();
      if (this.B() && var2 != null && var2.level != null && var2.player != null) {
         this.a = var1 == null ? null : var1.immutable();
         long var3 = this.B(var1);
         int var5 = this.g(var1);
         mb var6 = this.q(var1);
         lj.r var7 = this.k(var3, var5, var6);
         Map var8 = var1 == null ? Map.of() : this.j.getOrDefault(var1.asLong(), Map.of());
         var7 = new lj.r(
            Math.max(0, var7.C - var8.getOrDefault(var6.N, 0) - fv.Z(var6.N)),
            Math.max(0, var7.u - var8.getOrDefault(var6.j, 0) - fv.Z(var6.j)),
            var7.c,
            var7.l
         );
         String var9 = var5 + " " + i(var6.x.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"));
         SimpleContainer var10 = new SimpleContainer(54);
         int var11 = 0;
         var11 = C(var10, var11, var6.N, var7.C);
         C(var10, var11, var6.j, var7.u);
         var10.setItem(
            48,
            t(
               Items.GOLD_INGOT.getDefaultInstance(),
               b("Sell All", ChatFormatting.WHITE),
               List.of(b("Click to sell all loot", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         var10.setItem(49, t(var6.y(), z(var5 + " " + var6.x.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"), 64648), this.p(var7, var6)));
         var10.setItem(
            50,
            t(
               Items.DROPPER.getDefaultInstance(),
               b("Drop Loot", ChatFormatting.WHITE),
               List.of(b("Click to drop all loot on the page", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         ChestMenu var12 = new ChestMenu(MenuType.GENERIC_9x6, 0, var2.player.getInventory(), var10, 6);
         var2.gui.setScreen(new oy(var12, var2.player.getInventory(), Component.literal(var9), var1));
      }
   }

   public void B(BlockPos var1, Item var2, int var3) {
      if (var1 != null && var2 != null && var3 > 0) {
         this.j.computeIfAbsent(var1.asLong(), var0 -> new HashMap<>()).merge(var2, var3, Integer::sum);
      }
   }

   public long n(ItemStack var1) {
      if (!this.B() || var1.isEmpty() || hr.d(var1)) {
         return 0L;
      }

      if (this.q(var1)) {
         return var1.getCount() * 1000L;
      }

      if (mb.j(var1.getItem())) {
         return this.t(var1.getItem(), var1.getCount());
      }

      ItemContainerContents var2 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
      return var2 == null ? 0L : var2.nonEmptyItemCopyStream().mapToLong(this::n).sum() * var1.getCount();
   }

   public Component w(long var1) {
      double var3 = var1 >= 1000000000L ? 1.0E9 : (var1 >= 1000000L ? 1000000.0 : (var1 >= 1000L ? 1000.0 : 1.0));
      String var5 = var3 == 1.0E9 ? "B" : (var3 == 1000000.0 ? "M" : (var3 == 1000.0 ? "K" : ""));
      return b("~", ChatFormatting.DARK_GRAY).append(t("$ ", 65280)).append(b(U(var1 / var3, var3 == 1.0 ? 0 : 1) + var5, ChatFormatting.WHITE));
   }

   public long r(int var1) {
      return this.t(Items.BONE, var1);
   }

   public long t(Item var1, int var2) {
      Double var3 = FakeSpawnerModule.w().e(BuiltInRegistries.ITEM.getKey(var1).toString());
      if (var3 != null) {
         return Math.round(var3 * Math.max(0, var2));
      }

      String var4 = BuiltInRegistries.ITEM.getKey(var1).getPath().replace('_', ' ');
      l7 var5 = l7.q();
      var5.m(var4, var0 -> {});
      double var6 = this.x.getOrDefault(var1, (double)var5.x(var4));
      return Math.round(var6 * Math.max(0, var2));
   }

   public void g() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gui.screen() != null && var1.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("order")) {
         AbstractContainerMenu var2 = var1.player.containerMenu;
         HashMap var3 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5.container != var1.player.getInventory()) {
               ItemStack var6 = var5.getItem();
               if (mb.j(var6.getItem())) {
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

         var3.forEach((var1x, var2x) -> this.x.merge(var1x, var2x, Math::max));
      }
   }

   public int z() {
      return this.P(Items.BONE);
   }

   private boolean t(ItemStack var1) {
      if (var1.has(DataComponents.CUSTOM_NAME)) {
         return false;
      }

      FakeSpawnerModule var2 = FakeSpawnerModule.w();

      for (mb var6 : mb.values()) {
         String var7 = var2.w(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1.is(var6.N)) {
            return true;
         }
      }

      return false;
   }

   private int P(Item var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.B() && var2.player != null) {
         int var3 = -1;
         int var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6.is(var1) && !var6.has(DataComponents.CUSTOM_NAME) && !yl.z(var6) && var6.getCount() > var4) {
               var3 = var5;
               var4 = var6.getCount();
            }
         }

         return this.s = var3;
      } else {
         return -1;
      }
   }

   public void x(int var1) {
   }

   public void c(AbstractContainerMenu var1, Slot var2) {
   }

   public void e(Slot var1) {
   }

   public void B(BlockPos var1) {
      if (var1 != null && this.q(var1.asLong())) {
         this.v(var1);
         this.Y(var1);
         r4.g("FakeSpawner", "Final break predicted at {}; retaining pile and reservations until server acknowledgment", var1);
      }
   }

   boolean q(long var1) {
      return this.f.getOrDefault(var1, 0) > 0 && this.G.add(var1);
   }

   boolean J(long var1) {
      return this.G.remove(var1);
   }

   public BlockState I(Level var1, BlockPos var2, BlockState var3) {
      if (this.B() && var1 == this.g && this.f.getOrDefault(var2.asLong(), 0) > 0 && var1 instanceof ClientLevel var4) {
         if (var1.isOutsideBuildHeight(var2)) {
            return Blocks.AIR.defaultBlockState();
         }

         LevelChunk var5 = var4.getChunkAt(var2);
         return var5.getSection(var5.getSectionIndex(var2.getY())).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15);
      } else {
         return var3;
      }
   }

   public void U(Runnable var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.B() && var2.player != null && var2.isSameThread()) {
         try {
            this.c(var2.player.getInventory(), var1);
         } finally {
            this.T();
         }
      } else {
         var1.run();
      }
   }

   void c(Container var1, Runnable var2) {
      try {
         var2.run();
      } finally {
         mx.u();
      }
   }

   private void c(int var1, ItemStack var2) {
      ItemStack var3 = this.D.get(var1);
      if (var3 != null && var2.is(var3.getItem()) && var2.getCount() >= var3.getCount() && !ItemStack.isSameItemSameComponents(var2, var3)) {
         this.D.put(var1, var2.copyWithCount(var3.getCount()));

         for (Deque var5 : this.O.values()) {
            ArrayDeque var6 = new ArrayDeque();

            for (lj.t var8 : var5) {
               var6.addLast(var8.preferredSlot == var1 && var8.stack.is(var2.getItem()) ? new lj.t(var1, var2.copyWithCount(var8.stack.getCount())) : var8);
            }

            var5.clear();
            var5.addAll(var6);
         }
      }
   }

   public boolean g(ItemStack var1) {
      return this.B() && !var1.isEmpty() && this.D.values().stream().anyMatch(var1x -> ItemStack.isSameItemSameComponents(var1, var1x));
   }

   public boolean H(int var1, int var2, int var3, ContainerInput var4) {
      if (!this.B() || rm.h()) {
         return true;
      }

      if (rm.L()) {
         return false;
      }

      LocalPlayer var5 = Minecraft.getInstance().player;
      return var5 != null && var5.containerMenu.containerId == var1 ? this.J(var5.containerMenu, var2, var3, var4) : true;
   }

   public boolean J(AbstractContainerMenu var1, int var2, int var3, ContainerInput var4) {
      if (!this.B() || rm.h()) {
         return true;
      }

      if (rm.L()) {
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
         if (var6.container == var5.getInventory() && (this.U(var6.getContainerSlot()) || fv.l(var6.getContainerSlot()) || this.N(var6.getContainerSlot()))) {
            return false;
         }
      }

      return var4 != ContainerInput.SWAP || !this.U(var3) && !fv.l(var3) && !this.N(var3)
         ? var4 != ContainerInput.PICKUP_ALL || !this.g(var1.getCarried())
         : false;
   }

   public void Q(int var1, Runnable var2) {
      this.U(var2);
   }

   public boolean S(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      ItemStack var4 = this.D.get(var2);
      if (var4 == null || var4.isEmpty()) {
         return false;
      }

      if (var3.player != null
         && var3.gameMode != null
         && var1 >= 9
         && var1 < 36
         && !this.U(var1)
         && var3.player.inventoryMenu.getCarried().isEmpty()
         && var3.player.containerMenu == var3.player.inventoryMenu) {
         if (this.t(var3.player.getInventory().getItem(var1))) {
            this.x(var2);
         }

         rm.L(() -> var3.gameMode.handleContainerInput(0, var1, var2, ContainerInput.SWAP, var3.player));
         if (var4 != null && !var4.isEmpty()) {
            this.D.remove(var2);
            this.D.put(var1, var4);

            for (Deque var6 : this.O.values()) {
               ArrayDeque var7 = new ArrayDeque();

               for (lj.t var9 : var6) {
                  var7.addLast(var9.preferredSlot == var2 ? new lj.t(var1, var9.stack) : var9);
               }

               var6.clear();
               var6.addAll(var7);
            }

            mx.u();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean U(int var1) {
      return this.B() && this.D.containsKey(var1) && !this.D.get(var1).isEmpty();
   }

   public int v(int var1, ItemStack var2) {
      ItemStack var3 = this.D.get(var1);
      return this.B() && var3 != null && var2.is(var3.getItem()) ? Math.max(0, var2.getCount() - var3.getCount()) : 0;
   }

   public ItemStack V(int var1, ItemStack var2) {
      if (!this.U(var1)) {
         return var2;
      }

      int var3 = this.v(var1, var2);
      return var3 == 0 ? ItemStack.EMPTY : ja.T(var2.copyWithCount(var3));
   }

   public boolean n(AbstractContainerMenu var1, List<rm.k> var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var1 == var3.player.inventoryMenu && var3.player.hasInfiniteMaterials() && var3.player.containerMenu instanceof ItemPickerMenu
         )
       {
         this.U(() -> {
            for (rm.k var4 : var2) {
               var1.clicked(var4.slot(), var4.button(), var4.type(), var3.player);
            }

            var1.broadcastChanges();
         });
         return true;
      } else {
         return false;
      }
   }

   public boolean m(AbstractContainerMenu var1, Slot var2, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var2 != null && var2.container == var4.player.getInventory() && var3 >= 0 && var3 <= 1) {
         int var5 = var2.getContainerSlot();
         ItemStack var6 = var4.player.getInventory().getItem(var5);
         int var7 = this.v(var5, var6);
         if (var7 != 0 && var1.getCarried().isEmpty()) {
            int var8 = var3 == 0 ? var7 : (var7 + 1) / 2;
            ArrayList var9 = new ArrayList();
            var9.add(new rm.k(var2.index, 0, ContainerInput.PICKUP));

            for (int var10 = 0; var10 < var6.getCount() - var8; var10++) {
               var9.add(new rm.k(var2.index, 1, ContainerInput.PICKUP));
            }

            int var11 = var6.getCount() - var7;
            if (this.n(var1, var9)) {
               r4.g("FakeSpawner", "Creative surplus picked physical={} amount={} reserved={}", var5, var8, var11);
               return true;
            } else {
               rm.N(var1, "pick unreserved surplus", var9, var6.copyWithCount(var8), mx::u, mx::u);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int d() {
      Inventory var1 = Minecraft.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.U(var2) && !this.N(var2) && var1.getItem(var2).isEmpty()) {
            return var2;
         }
      }

      return -1;
   }

   public int E(int var1) {
      return this.w(Items.BONE, var1);
   }

   public int w(Item var1, int var2) {
      return fv.n(var1, var2);
   }

   public boolean F(ItemStack var1) {
      return false;
   }

   public boolean t() {
      return this.P(1) == 1;
   }

   public int P(int var1) {
      return this.p(Items.BONE, var1);
   }

   public int p(Item var1, int var2) {
      return this.w(var1, var2);
   }

   public boolean t(int var1) {
      return var1 == 48 || var1 == 50;
   }

   public boolean E(Component var1, AbstractContainerMenu var2) {
      if (var1 != null && var2 != null) {
         String var3 = var1.getString().toLowerCase(Locale.ROOT);
         return var3.contains("sell") && var2.slots.size() >= 72;
      } else {
         return false;
      }
   }

   public Component e(Component var1) {
      return var1 == null ? Component.literal(i("place items in here to sell")) : Component.literal(i(var1.getString()));
   }

   public void J(Component var1, AbstractContainerMenu var2) {
      if (this.B() && this.E(var1, var2)) {
         long var3 = 0L;

         for (int var5 = 0; var5 < Math.max(0, var2.slots.size() - 36); var5++) {
            var3 += this.n(var2.getSlot(var5).getItem());
         }

         if (var3 > 0L) {
            ul.p(var3);
         }
      }
   }

   private long B(BlockPos var1) {
      long var2 = System.currentTimeMillis();
      return var1 == null ? var2 : this.B.computeIfAbsent(var1.asLong(), var2x -> var2);
   }

   private int g(BlockPos var1) {
      return var1 == null ? Math.max(1, FakeSpawnerModule.w().F()) : Math.max(1, this.f.getOrDefault(var1.asLong(), 1));
   }

   private lj.r u(long var1, int var3) {
      return this.k(var1, var3, mb.v);
   }

   private lj.r k(long var1, int var3, mb var4) {
      double var5 = this.N(var3);
      double var7 = var4.j == Items.AIR ? 0.0 : Math.max(0.15, var5 * 0.33);
      long var9 = Math.max(0L, System.currentTimeMillis() - var1);
      long var11 = var9 / 60000L;
      return new lj.r(Math.max(0, (int)(var11 * var5)), Math.max(0, (int)(var11 * var7)), var5, var7);
   }

   private double N(int var1) {
      int var2 = Math.max(1, var1);
      if (var2 == 1) {
         return 1.9;
      } else {
         return var2 <= 64 ? var2 * 1.9453125 : 124.5 + (var2 - 64) * 0.88671875;
      }
   }

   private static int C(SimpleContainer var0, int var1, Item var2, int var3) {
      int var4 = Math.max(0, var3);

      while (var4 > 0 && var1 < 45) {
         int var5 = Math.min(64, var4);
         var0.setItem(var1++, new ItemStack(var2, var5));
         var4 -= var5;
      }

      return var1;
   }

   private List<Component> p(lj.r var1, mb var2) {
      int var3 = var1 == null ? 0 : Math.max(0, var1.u);
      int var4 = var1 == null ? 0 : Math.max(0, var1.C);
      if (var3 <= 0 && var4 <= 0) {
         return List.of(t("(0% filled)", 64648));
      }

      ArrayList var5 = new ArrayList();
      if (var3 > 0) {
         var5.add(t(var3 + " ", 64648).append(var2.j.getName(new ItemStack(var2.j)).copy().withStyle(ChatFormatting.WHITE)));
      }

      if (var4 > 0) {
         var5.add(t(var4 + " ", 64648).append(var2.N.getName(new ItemStack(var2.N)).copy().withStyle(ChatFormatting.WHITE)));
      }

      var5.add(t("(0% filled)", 64648));
      return var5;
   }

   private boolean o(long var1, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && !rm.L() && var4.player.containerMenu == var4.player.inventoryMenu && var4.player.inventoryMenu.getCarried().isEmpty()) {
         Deque var5 = this.O.get(var1);
         if (var5 != null && !var5.isEmpty()) {
            LinkedHashMap var6 = new LinkedHashMap();
            int var7 = var3;

            for (lj.t var9 : var5.reversed()) {
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
               ItemStack var12 = this.D.get(var17.getKey());
               if (var12 != null
                  && var12.getCount() == (Integer)var17.getValue()
                  && var4.player.getInventory().getItem((Integer)var17.getKey()).getCount() == (Integer)var17.getValue()) {
                  var14 = (Integer)var17.getKey();
                  break;
               }
            }

            if (var14 < 0) {
               var14 = this.d();
            }

            if (var14 < 0) {
               return false;
            }

            ArrayList var16 = new ArrayList();

            for (Entry var20 : var6.entrySet()) {
               if ((Integer)var20.getKey() != var14) {
                  var16.addAll(
                     rm.M((Integer)var20.getKey(), var14, (Integer)var20.getValue(), var4.player.getInventory().getItem((Integer)var20.getKey()).getCount())
                  );
               }
            }

            if (var16.isEmpty()) {
               this.W(var1, var3);
               if (var7 > 0) {
                  this.i(new ItemStack(this.B(var1), var7), var7);
               }

               mx.u();
               return true;
            } else {
               int var19 = var14;
               int var21 = var7;
               rm.H("mining return", var16, () -> {
                  this.W(var1, var3);
                  this.w(var1, var3);
                  if (var21 > 0) {
                     this.i(new ItemStack(this.B(var1), var21), var21);
                  }

                  this.O(BlockPos.of(var1), true);
                  mx.u();
                  this.g("Mining return predicted pile={} physical={} amount={} worldPending={}", var1, var19, var13, var21);
               }, () -> this.M());
               return false;
            }
         } else {
            this.i(new ItemStack(this.B(var1), var3), var3);
            return true;
         }
      } else {
         return false;
      }
   }

   private void i(ItemStack var1, int var2) {
      if (var2 > 0 && !var1.isEmpty()) {
         Inventory var3 = Minecraft.getInstance().player.getInventory();
         int var4 = -1;

         for (int var5 = 0; var5 < 36; var5++) {
            if (!this.U(var5)) {
               ItemStack var6 = var3.getItem(var5);
               lj.p var7 = this.n.get(var5);
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
            var4 = this.d();
         }

         if (var4 >= 0) {
            int var8 = var3.getItem(var4).isEmpty() && !this.n.containsKey(var4) ? mx.i() : -1;
            this.e(var4, var1, 0, var2);
            if (var8 >= 0) {
               mx.z(var8, var4);
            }
         }
      }
   }

   private void e(int var1, ItemStack var2, int var3, int var4) {
      if (var4 > 0) {
         lj.p var5 = this.n.get(var1);
         int var6 = Minecraft.getInstance().player.getInventory().getItem(var1).getCount();
         int var7 = var5 != null && ItemStack.isSameItemSameComponents(var5.stack, var2) ? Math.max(var5.expected + var3, var6) : var6;
         this.n.put(var1, new lj.p(var2.copyWithCount(1), var7 + var4, Minecraft.getInstance().player.tickCount + 100));
         mx.C();
      }
   }

   public boolean N(int var1) {
      return this.n.containsKey(var1);
   }

   public ItemStack Q(int var1, ItemStack var2) {
      lj.p var3 = this.n.get(var1);
      return var3 != null && var2.isEmpty() ? ja.T(var3.stack.copyWithCount(var3.expected)) : var2;
   }

   private lj.t W(long var1, int var3) {
      Deque var4 = this.O.get(var1);
      if (var4 != null && !var4.isEmpty() && var3 > 0) {
         int var5 = var3;
         int var6 = -1;
         ItemStack var7 = ItemStack.EMPTY;

         while (var5 > 0 && !var4.isEmpty()) {
            lj.t var8 = (lj.t)var4.removeLast();
            if (var8 != null && var8.stack != null && !var8.stack.isEmpty()) {
               if (var6 < 0) {
                  var6 = var8.preferredSlot;
               }

               if (var7.isEmpty()) {
                  var7 = var8.stack.copy();
               }

               int var9 = Math.min(var5, var8.stack.getCount());
               ItemStack var10 = this.D.get(var8.preferredSlot);
               if (var10 != null && ItemStack.isSameItemSameComponents(var10, var8.stack)) {
                  if (var10.getCount() <= var9) {
                     this.D.remove(var8.preferredSlot);
                  } else {
                     this.D.put(var8.preferredSlot, var10.copyWithCount(var10.getCount() - var9));
                  }
               }

               var5 -= var9;
               int var11 = var8.stack.getCount() - var9;
               if (var11 > 0) {
                  var4.addLast(new lj.t(var8.preferredSlot, var8.stack.copyWithCount(var11)));
               }
            }
         }

         if (var4.isEmpty()) {
            this.O.remove(var1);
         }

         return var7.isEmpty() ? null : new lj.t(var6, var7.copyWithCount(var3));
      } else {
         return null;
      }
   }

   private boolean Z(long var1, lj.t var3, boolean var4) {
      if (var3 != null && !var3.stack.isEmpty()) {
         Minecraft var5 = Minecraft.getInstance();
         if (var5.player == null) {
            return false;
         }

         int var6 = var3.preferredSlot;
         if (var6 >= 0 && var6 < var5.player.getInventory().getContainerSize() && !this.D.containsKey(var6)) {
            ItemStack var7 = var5.player.getInventory().getItem(var6);
            int var8 = var3.stack.getCount();
            int var9 = var4 && !var5.player.hasInfiniteMaterials() ? var8 - 1 : var8;
            if (var9 <= 0) {
               return true;
            } else if (var7.is(var3.stack.getItem()) && var7.getCount() >= var9) {
               this.D.put(var6, var7.copyWithCount(var9));
               this.O.computeIfAbsent(var1, var0 -> new ArrayDeque<>()).addFirst(new lj.t(var6, var7.copyWithCount(var9)));
               mx.u();
               r4.g(
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

   private void C(BlockPos var1, Direction var2) {
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

   private void W(Minecraft var1, BlockPos var2, Direction var3, double var4) {
      if (var1 != null && var1.player != null && var1.levelRenderer != null && var2 != null) {
         int var6 = Mth.clamp((int)(var4 * 10.0), 0, 9);
         var1.level.destroyBlockProgress(var1.player.getId(), var2, var6);
         if (var4 <= 0.0) {
            return;
         }

         long var7 = var2.asLong();
         long var9 = var1.player.tickCount;
         long var11 = this.z.getOrDefault(var7, 0L);
         if (var9 - var11 >= 4L) {
            this.z.put(var7, var9);
            this.C(var2, var3);
            this.O(var2, false);
         }
      }
   }

   private void v(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var2.levelRenderer != null && var1 != null) {
         var2.level.destroyBlockProgress(var2.player.getId(), var1, -1);
      }
   }

   private void P(Level var1, BlockPos var2) {
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

   private void O(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 != null && !var1.isBlank()) {
         jb.v(t(var1, 16711680), false);
         jb.v(t(var1, 16711680), true);
      }
   }

   private void g(String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         r4.g("FakeSpawner", var1, var2);
      }
   }

   private void k(String var1, long var2, String var4, Object... var5) {
      if (Boolean.getBoolean("larp.debug")) {
         long var6 = System.currentTimeMillis();
         if (var6 >= this.K) {
            this.K = var6 + var2;
            this.g(var4, var5);
         }
      }
   }

   private static ItemStack t(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1);
      var0.set(DataComponents.RARITY, Rarity.COMMON);
      if (var2 != null && !var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component e(String var0, ChatFormatting var1) {
      return b(i(var0), var1);
   }

   private static MutableComponent b(String var0, ChatFormatting var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.applyFormat(var1).withItalic(false));
   }

   private static Component z(String var0, int var1) {
      return t(i(var0), var1);
   }

   private static MutableComponent t(String var0, int var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)).withItalic(false));
   }

   private static String i(String var0) {
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

   private static String U(double var0, int var2) {
      String var3 = String.format(Locale.US, "%." + var2 + "f", var0);

      while (var3.contains(".") && var3.endsWith("0")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      if (var3.endsWith(".")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3;
   }

   private static Component O(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         MutableComponent var1 = Component.empty();
         Style var2 = Style.EMPTY.withItalic(false);
         StringBuilder var3 = new StringBuilder();

         for (int var4 = 0; var4 < var0.length(); var4++) {
            char var5 = var0.charAt(var4);
            if ((var5 == 167 || var5 == '&') && var4 + 1 < var0.length()) {
               if (var5 == '&' && var4 + 7 < var0.length() && var0.charAt(var4 + 1) == '#') {
                  Integer var6 = v(var0.substring(var4 + 2, var4 + 8));
                  if (var6 != null) {
                     l(var1, var3, var2);
                     var2 = var2.withColor(TextColor.fromRgb(var6)).withItalic(false);
                     var4 += 7;
                     continue;
                  }
               }

               ChatFormatting var7 = ChatFormatting.getByCode(var0.charAt(var4 + 1));
               if (var7 != null) {
                  l(var1, var3, var2);
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

         l(var1, var3, var2);
         return var1;
      } else {
         return Component.empty();
      }
   }

   private static void l(MutableComponent var0, StringBuilder var1, Style var2) {
      if (var1.length() != 0) {
         var0.append(Component.literal(var1.toString()).setStyle(var2));
         var1.setLength(0);
      }
   }

   private static Integer v(String var0) {
      try {
         return Integer.parseInt(var0, 16);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private String u(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0;
      if (var5 >= 1.0E9) {
         return U(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return U(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? U(var5 / 1000.0, 1) + "K" : U(var5, 0);
      }
   }

   private String m(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0 + 75.0;
      if (var5 >= 1.0E9) {
         return U(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return U(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? U(var5 / 1000.0, 1) + "K" : U(var5, 0);
      }
   }

   private boolean y(ItemStack var1) {
      return var1 != null && !var1.isEmpty() ? var1.getItem() instanceof BlockItem var2 && var2.getBlock() instanceof ShulkerBoxBlock : false;
   }

   private long x(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         long var2 = 0L;
         ItemContainerContents var4 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
         if (var4 != null) {
            for (ItemStack var6 : var4.nonEmptyItemCopyStream().toList()) {
               if (var6 != null && !var6.isEmpty() && this.q(var6)) {
                  var2 += var6.getCount();
               }
            }
         }

         return var2;
      } else {
         return 0L;
      }
   }

   private int G(CompoundTag var1) {
      if (var1 == null) {
         return 0;
      } else if (var1.contains("count")) {
         return Math.max(0, var1.getIntOr("count", 0));
      } else {
         return var1.contains("Count") ? Math.max(0, var1.getIntOr("Count", 0)) : 0;
      }
   }

   private boolean f(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2 != null && var2.getWindow() != null ? r7.r(var2.getWindow(), var1) : false;
   }

   public record a(SoundEvent event, float volume, float pitch) {
   }

   private record c(int stackCount, lj.t hiddenStack, long createdAtMs) {
   }

   private record d(int slot, String item, int amount, int reserved) {
   }

   private record p(ItemStack stack, int expected, int expires) {
   }

   public static final class r {
      public final int C;
      public final int u;
      public final double c;
      public final double l;

      public r(int var1, int var2, double var3, double var5) {
         this.C = Math.max(0, var1);
         this.u = Math.max(0, var2);
         this.c = Math.max(0.0, var3);
         this.l = Math.max(0.0, var5);
      }
   }

   private record s(long pos, mb kind, int count, long placedAt, List<lj.d> backing, Map<String, Integer> purchased) {
      JsonObject toJson() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("pos", this.pos);
         var1.addProperty("kind", this.kind.name());
         var1.addProperty("count", this.count);
         var1.addProperty("placedAt", this.placedAt);
         JsonArray var2 = new JsonArray();

         for (lj.d var4 : this.backing) {
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

   private record t(int preferredSlot, ItemStack stack) {
   }

   private record y(BlockPos pos, long untilMs) {
   }
}
