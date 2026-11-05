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

public final class dr {
   private static final dr W4 = new dr();
   private static final int Wz = 45;
   private static final String f = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
   private static final long c = 1800L;
   private static final long g = 650L;
   private static final int k = 64648;
   private static final int d = 16711680;
   private static final double K = 75.0;
   private final Map<Long, Long> O = new HashMap<>();
   private final Map<Long, Map<Item, Integer>> z = new HashMap<>();
   private final Map<Long, Integer> x = new ConcurrentHashMap<>();
   private final Set<Long> l = ConcurrentHashMap.newKeySet();
   private final Map<Long, dr.g> S = new HashMap<>();
   private final Map<Long, Deque<dr.c>> Y = new HashMap<>();
   private final Map<Integer, ItemStack> n = new HashMap<>();
   private final Map<Long, dr.n> e = new HashMap<>();
   private volatile Map<Long, SpawnerBlockEntity> I = new ConcurrentHashMap<>();
   private final Long2ObjectMap<List<SpawnerBlockEntity>> j = new Long2ObjectOpenHashMap();
   private final Map<Long, Long> F = new HashMap<>();
   private final Map<Long, Float> i = new HashMap<>();
   private final Map<Long, Integer> Z = new HashMap<>();
   private int a;
   private final Set<Integer> h = new HashSet<>();
   private final Map<Integer, dr.d> V = new HashMap<>();
   private BlockPos r;
   private int B = -1;
   private final Map<Long, Integer> m = new HashMap<>();
   private final Map<Long, Double> P = new HashMap<>();
   private final Map<Long, Integer> s = new ConcurrentHashMap<>();
   private final Set<Long> W = new HashSet<>();
   private final Map<Item, Double> G = new HashMap<>();
   private final Set<Long> C = ConcurrentHashMap.newKeySet();
   private volatile Level T;
   private LocalPlayer U;
   private Item X;
   private String N;
   private volatile Map<Item, cl> M = Map.of();
   private volatile LongSet b = LongSets.EMPTY_SET;
   private volatile LongSet Wp = LongSets.EMPTY_SET;
   private final Map<Long, Long> Wc = new ConcurrentHashMap<>();
   private final Map<Long, cl> y = new ConcurrentHashMap<>();
   private final Map<Long, dr.v> w = new HashMap<>();
   private final List<JsonElement> J = new ArrayList<>();
   private String WT;
   private boolean L = (boolean)1;
   private long v;
   private long H;
   private long u;
   private long R;
   private long t;
   private long q;
   private final Set<ItemStack> A = Collections.newSetFromMap(new IdentityHashMap<>());
   private final Set<ItemStack> Q = Collections.newSetFromMap(new WeakHashMap<>());
   private InteractionHand o;
   private int E = -1;
   private int p = -1;
   private final Set<Long> Wy = ConcurrentHashMap.newKeySet();
   private long D;

   private static Path r() {
      return FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-piles.json");
   }

   void J(Container var1) {
      this.A.clear();
      this.Q.clear();

      for (int var2 = 0; var2 < var1.getContainerSize(); var2++) {
         ItemStack var3 = var1.getItem(var2);
         if (!var3.isEmpty()) {
            this.A.add(var3);
         }
      }
   }

   public ItemStack m(ItemStack var1, ItemStack var2) {
      if (!var2.isEmpty() && (this.A.contains(var1) || this.Q.contains(var1))) {
         this.Q.add(var2);
         if (Boolean.getBoolean("larp.debug")) {
            this.t++;
         }
      }

      return var2;
   }

   public void e() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null) {
         this.J(var1.player.getInventory());
         if (!var1.player.containerMenu.getCarried().isEmpty()) {
            this.A.add(var1.player.containerMenu.getCarried());
         }

         u0.V();
      }
   }

   public void L() {
      this.X();
      if (Boolean.getBoolean("larp.debug")) {
         this.H++;
      }
   }

   public void m(int var1, int var2) {
      if (Boolean.getBoolean("larp.debug")) {
         this.u += var1;
         this.R += var2;
      }
   }

   public boolean M(ItemStack var1) {
      if (!this.H()) {
         return false;
      }

      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 != null && (var1 == var2.getMainHandItem() && this.t(var2.getInventory().getSelectedSlot()) || var1 == var2.getOffhandItem() && this.t(40));
   }

   public void u() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner reset must run on the client thread");
      }

      this.h();
      wt.L("controller cleanup");
      u0.u();
      this.A.clear();
      this.Q.clear();
      this.I = new ConcurrentHashMap<>();
      this.j.clear();
      this.b = LongSets.EMPTY_SET;
      this.Wp = LongSets.EMPTY_SET;
      this.T = null;
      this.B = -1;
      this.o = null;
      this.p = -1;
      this.Z.clear();
      this.a = 0;
      this.Wy.clear();
      this.h.clear();
      this.V.clear();
      this.m.clear();
      this.P.clear();
      this.s.clear();
      this.W.clear();
      this.G.clear();
      this.C.clear();

      for (Long var3 : new ArrayList<>(this.i.keySet())) {
         this.t(BlockPos.of(var3));
      }

      int var6 = this.x.isEmpty() && this.l.isEmpty() ? 0 : 1;
      this.x.clear();
      this.y.clear();
      this.w.clear();
      this.J.clear();
      this.WT = null;
      this.l.clear();
      this.O.clear();
      this.z.clear();
      this.S.clear();
      this.Y.clear();
      this.n.clear();

      for (long var4 : this.Wc.keySet()) {
         this.w(BlockPos.of(var4));
      }

      this.Wc.clear();
      this.Wp = LongSets.EMPTY_SET;
      this.e.clear();
      this.i.clear();
      this.F.clear();
      this.r = null;
      this.U = null;
      this.X = null;
      if (var1 != null) {
         if (var1.gui.screen() instanceof eu || var1.gui.screen() instanceof wk) {
            var1.gui.setScreen(null);
         }

         if (var6 != 0 && var1.level != null) {
            var1.levelExtractor.allChanged();
         }
      }
   }

   private void l() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner state must be updated on the client thread");
      }

      ClientLevel var2 = var1 == null ? null : var1.level;
      LocalPlayer var3 = var1 == null ? null : var1.player;
      Item var4 = this.N();
      if (var2 != this.T || var3 != this.U || !this.k().equals(this.N) || !this.H()) {
         this.u();
         this.T = var2;
         this.U = var3;
         this.X = var4;
         this.N = this.k();
         HashMap var5 = new HashMap();
         FakeSpawnerModule var6 = FakeSpawnerModule.T();
         if (var6 != null) {
            for (cl var10 : cl.values()) {
               String var11 = var6.x(var10.ordinal());
               if (var11 != null && !var11.isBlank()) {
                  var5.put(E(var11), var10);
               }
            }
         }

         this.M = Map.copyOf(var5);
         if (var2 != null && var3 != null && this.H()) {
            this.WT = this.v(var1);
            this.f();
            this.L = (boolean)1;
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

   private void f() {
      if (this.WT != null) {
         this.T(r());
      }
   }

   private void T(Path var1) {
      if (this.WT != null && Files.exists(var1)) {
         try {
            JsonObject var2 = eg.J(var1);
            if (var2.has("quarantined") && var2.get("quarantined").isJsonObject() && var2.getAsJsonObject("quarantined").has(this.WT)) {
               for (JsonElement var4 : var2.getAsJsonObject("quarantined").getAsJsonArray(this.WT)) {
                  this.J.add(var4.deepCopy());
               }
            }

            if (!var2.has(this.WT)) {
               return;
            }

            int var20 = 0;

            for (JsonElement var5 : var2.getAsJsonArray(this.WT)) {
               try {
                  JsonObject var6 = var5.getAsJsonObject();
                  long var7 = var6.get("pos").getAsLong();
                  cl var9 = cl.valueOf(var6.get("kind").getAsString());
                  int var10 = var6.get("count").getAsInt();
                  long var11 = var6.get("placedAt").getAsLong();
                  ArrayList var13 = new ArrayList();

                  for (JsonElement var15 : var6.getAsJsonArray("backing")) {
                     JsonObject var16 = var15.getAsJsonObject();
                     var13.add(
                        new dr.l(
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

                  if (var10 < 1 || var10 > 100000 || var10 > 1 && var13.isEmpty() || this.w.containsKey(var7)) {
                     throw new IllegalArgumentException("Invalid or conflicting saved pile");
                  }

                  this.w.put(var7, new dr.v(var7, var9, var10, var11, var13, var22));
               } catch (RuntimeException var18) {
                  var20++;
                  this.J.add(var5.deepCopy());
               }
            }

            if (var20 > 0) {
               mt.T("FakeSpawner", "Retaining {} unmatched pile records for recovery; no inventory was restored", var20);
            }
         } catch (Exception var19) {
            mt.P("FakeSpawner", "Pile load failed; retaining the file for recovery", var19);
         }
      }
   }

   private void h() {
      if (this.WT != null) {
         this.b(r());
      }
   }

   private void b(Path var1) {
      if (this.WT != null) {
         try {
            JsonObject var2 = Files.exists(var1) ? eg.J(var1) : new JsonObject();
            JsonArray var3 = new JsonArray();
            if (var2.has(this.WT) && !var2.get(this.WT).isJsonArray()) {
               throw new IOException("Unrecognized saved pile data; original file retained");
            }

            if (var2.has("quarantined") && !var2.get("quarantined").isJsonObject()) {
               throw new IOException("Unrecognized pile quarantine data; original file retained");
            }

            JsonObject var4 = var2.has("quarantined") ? var2.getAsJsonObject("quarantined") : new JsonObject();
            if (var4.has(this.WT) && !var4.get(this.WT).isJsonArray()) {
               throw new IOException("Unrecognized retained pile records; original file retained");
            }

            JsonArray var5 = new JsonArray();

            for (JsonElement var7 : this.J) {
               var5.add(var7.deepCopy());
            }

            if (!var5.isEmpty()) {
               var4.add(this.WT, var5);
               var2.add("quarantined", var4);
            }

            for (dr.v var18 : this.w.values()) {
               var3.add(var18.toJson());
            }

            for (Entry var19 : this.x.entrySet()) {
               long var8 = (Long)var19.getKey();
               if (!this.w.containsKey(var8) && (Integer)var19.getValue() >= 1) {
                  Deque var10 = this.Y.get(var8);
                  if (var10 == null) {
                     var10 = new ArrayDeque();
                  }

                  if ((Integer)var19.getValue() <= 1 || !var10.isEmpty()) {
                     ArrayList var11 = new ArrayList();

                     for (dr.c var13 : var10) {
                        ItemStack var14 = this.n.get(var13.preferredSlot);
                        if (var14 == null || var14.isEmpty()) {
                           var11.clear();
                           break;
                        }

                        var11.add(
                           new dr.l(
                              var13.preferredSlot, BuiltInRegistries.ITEM.getKey(var13.stack.getItem()).toString(), var13.stack.getCount(), var14.getCount()
                           )
                        );
                     }

                     if (!var11.isEmpty() || (Integer)var19.getValue() == 1) {
                        HashMap var20 = new HashMap();
                        this.z.getOrDefault(var8, Map.of()).forEach((var1x, var2x) -> var20.put(BuiltInRegistries.ITEM.getKey(var1x).toString(), var2x));
                        var3.add(
                           new dr.v(
                                 var8,
                                 this.P(BlockPos.of(var8)),
                                 (Integer)var19.getValue(),
                                 this.O.getOrDefault(var8, System.currentTimeMillis()),
                                 var11,
                                 var20
                              )
                              .toJson()
                        );
                     }
                  }
               }
            }

            var2.add(this.WT, var3);
            Files.createDirectories(var1.getParent());
            eg.o(var1, var2.toString());
         } catch (Exception var15) {
            mt.P("FakeSpawner", "Pile save failed; previous file retained", var15);
         }
      }
   }

   public void X() {
      this.L = (boolean)1;
   }

   private void w(Minecraft var1) {
      if (this.L && !this.w.isEmpty()) {
         this.L = (boolean)0;
         byte var2 = 0;

         for (dr.v var4 : new ArrayList<>(this.w.values())) {
            BlockPos var5 = BlockPos.of(var4.pos);
            if (var1.level.hasChunkAt(var5)) {
               LevelChunk var6 = var1.level.getChunkAt(var5);
               int var7 = var6.getSectionIndex(var5.getY());
               BlockState var8 = var7 >= 0 && var7 < var6.getSections().length
                  ? var6.getSection(var7).getBlockState(var5.getX() & 15, var5.getY() & 15, var5.getZ() & 15)
                  : Blocks.AIR.defaultBlockState();
               if (this.x(var8.getBlock().asItem()) == var4.kind) {
                  HashMap var9 = new HashMap();

                  for (dr.l var11 : var4.backing) {
                     if ((var11.slot < 0 || var11.slot >= 36) && var11.slot != 40
                        || var11.amount < 1
                        || var11.reserved < 1
                        || var11.amount > var11.reserved + 1
                        || var11.reserved > 64
                        || var9.containsKey(var11.slot)
                        || this.t(var11.slot)) {
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
                        u0.I();

                        for (dr.l var13 : var4.backing) {
                           ItemStack var14 = (ItemStack)var9.get(var13.slot);
                           this.n.put(var13.slot, var14);
                           var16.addLast(new dr.c(var13.slot, var14.copyWithCount(Math.min(var13.amount, var13.reserved))));
                        }

                        this.Y.put(var4.pos, var16);
                        this.x.put(var4.pos, var4.count);
                        this.y.put(var4.pos, var4.kind);
                        this.O.put(var4.pos, var4.placedAt);
                        HashMap var18 = new HashMap();
                        var4.purchased.forEach((var1x, var2x) -> {
                           Identifier var3 = Identifier.tryParse(var1x);
                           if (var3 != null && BuiltInRegistries.ITEM.getValue(var3) != Items.AIR) {
                              var18.put((Item)BuiltInRegistries.ITEM.getValue(var3), var2x);
                           }
                        });
                        if (!var18.isEmpty()) {
                           this.z.put(var4.pos, var18);
                        }

                        this.w.remove(var4.pos);
                        this.t(var4.pos);
                        this.K(var1.level, var5);
                        this.w(var5);
                        u0.I();
                        var2 = 1;
                     }
                  }
               }
            }
         }

         if (var2 != 0) {
            this.h();
         }
      }
   }

   private dr() {
   }

   public static dr z() {
      return W4;
   }

   public boolean H() {
      FakeSpawnerModule var1 = FakeSpawnerModule.T();
      return var1 != null && var1.isRuntimeActive();
   }

   public boolean H(ItemStack var1) {
      return this.H() && this.s(var1);
   }

   public boolean s(ItemStack var1) {
      return this.W(var1) && (this.A.contains(var1) || this.Q.contains(var1) || h2.I(var1));
   }

   private boolean W(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && !d1.d(var1) && this.x(var1.getItem()) != null;
   }

   private String k() {
      FakeSpawnerModule var1 = FakeSpawnerModule.T();
      return var1.x(0) + "|" + var1.x(1) + "|" + var1.x(2);
   }

   public cl x(Item var1) {
      FakeSpawnerModule var2 = FakeSpawnerModule.T();

      for (cl var6 : cl.values()) {
         String var7 = var2.x(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1 == E(var7)) {
            return var6;
         }
      }

      return null;
   }

   public cl P(BlockPos var1) {
      return var1 == null ? cl.A : this.y.getOrDefault(var1.asLong(), cl.A);
   }

   private Item g(long var1) {
      return E(FakeSpawnerModule.T().x(this.P(BlockPos.of(var1)).ordinal()));
   }

   public Item N() {
      FakeSpawnerModule var1 = FakeSpawnerModule.T();
      return E(var1 == null ? null : var1.J());
   }

   public static Item E(String var0) {
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

   private Block T() {
      return this.N() instanceof BlockItem var1 ? var1.getBlock() : Blocks.COBBLESTONE;
   }

   public boolean K(BlockState var1) {
      return this.H() && var1 != null ? this.x(var1.getBlock().asItem()) != null || var1.is(Blocks.SPAWNER) : false;
   }

   public void R(BlockPos var1) {
      this.l();
      this.C.add(var1.asLong());
      this.x.remove(var1.asLong());
      this.t(var1.asLong());
      this.w(var1);
   }

   public BlockState f(BlockPos var1, BlockState var2) {
      return var1 == null ? var2 : this.E(var1.asLong(), var2);
   }

   public BlockState E(long var1, BlockState var3) {
      if (!this.b.contains(var1) || !this.K() || var3 == null || this.Wy.contains(var1)) {
         return var3;
      }

      if (this.s.containsKey(var1)) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.C.contains(var1)) {
         return var3;
      }

      if (!this.l.contains(var1) || !var3.is(Blocks.SPAWNER) && !this.M.containsKey(var3.getBlock().asItem())) {
         int var4 = this.x.getOrDefault(var1, 0);
         if (var4 <= 0) {
            return var3;
         } else {
            return !var3.isAir() && !var3.is(Blocks.SPAWNER) && !this.M.containsKey(var3.getBlock().asItem()) ? var3 : Blocks.SPAWNER.defaultBlockState();
         }
      } else {
         return Blocks.AIR.defaultBlockState();
      }
   }

   public boolean K() {
      return (!this.x.isEmpty() || !this.l.isEmpty() || !this.s.isEmpty()) && this.H() && this.T == Minecraft.getInstance().level;
   }

   private void a() {
      LongOpenHashSet var1 = new LongOpenHashSet();

      for (long var3 : this.x.keySet()) {
         var1.add(var3);
      }

      for (long var9 : this.l) {
         var1.add(var9);
      }

      for (long var10 : this.s.keySet()) {
         var1.add(var10);
      }

      this.b = LongSets.unmodifiable(var1);
      LongOpenHashSet var8 = new LongOpenHashSet();

      for (long var4 : this.Wc.keySet()) {
         var8.add(var4);
      }

      this.Wp = LongSets.unmodifiable(var8);
   }

   public void K(BlockPos var1, BlockState var2) {
      this.l();
      if (var1 != null) {
         long var3 = var1.asLong();
         if (this.T(var3)) {
            this.a = 0;
            this.w(var1);
            mt.U("FakeSpawner", "Final break {} at {}; server state={}", var2 != null && var2.isAir() ? "confirmed" : "corrected", var1, var2);
         }

         if (this.C.contains(var3)) {
            if (var2 != null && !var2.isAir()) {
               return;
            }

            x7.W(var1);
            this.C.remove(var3);
         }

         if (this.l.contains(var3)) {
            if (var2 != null && this.K(var2)) {
               this.a = 0;
            }

            this.e.put(var3, new dr.n(var1.immutable(), System.currentTimeMillis() + 10000L));
            this.l.remove(var3);
            this.w(var1);
         }

         int var5 = this.x.getOrDefault(var3, 0);
         if (var2 != null && !var2.isAir()) {
            if (this.H()) {
               if (this.K(var2)) {
                  if (var5 <= 0 && !this.S.containsKey(var3)) {
                     return;
                  }

                  cl var6 = this.x(var2.getBlock().asItem());
                  if (var6 != null && this.y.put(var3, var6) != var6) {
                     this.t(var3);
                  }

                  this.O.putIfAbsent(var3, System.currentTimeMillis());
                  dr.g var7 = this.S.remove(var3);
                  int var8 = var7 == null ? this.x.getOrDefault(var3, 1) : Math.max(1, var7.stackCount);
                  this.x.put(var3, Math.max(1, var8));
                  this.z("track:" + var3, 1500L, "trackWorldState fake pos={} state={} resolvedCount={} pending={}", var1, var2.getBlock(), var8, var7 != null);
                  if (var7 != null && var7.hiddenStack != null) {
                     this.b(var3, var7.hiddenStack, true);
                  }

                  if (var7 != null) {
                     this.h();
                  }

                  this.K(Minecraft.getInstance() == null ? null : Minecraft.getInstance().level, var1);
                  if (var5 <= 0) {
                     this.w(var1);
                  }
               } else {
                  this.K(var1, null);
               }
            }
         } else {
            if (var5 > 0) {
               this.k("trackWorldState removed pos={} previousCount={} state={}", var1, var5, var2);
            }

            if (var5 > 0 && !this.V(var1)) {
               this.B(var1);
            } else if (this.V(var1)) {
               this.i(var3, Integer.MAX_VALUE);
               u0.V();
               this.k("Placement rejected at {}; reservations released without inventory replay", var1);
            }

            this.O.remove(var3);
            this.z.remove(var3);
            this.x.remove(var3);
            this.S.remove(var3);
            this.Y.remove(var3);
            this.t(var3);
            if (var5 > 0) {
               this.w(var1);
            }

            this.i.remove(var3);
            this.F.remove(var3);
            this.P.remove(var3);
            this.t(var1);
            if (var1.equals(this.r)) {
               this.r = null;
            }
         }
      }
   }

   public void F(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.l();
      if (this.H() && var1 != null && this.s(var2)) {
         dr.v var5 = this.w.remove(var1.asLong());
         if (var5 != null) {
            this.J.add(var5.toJson());
            this.k("New placement at {} archives a superseded saved pile for recovery", var1);
         }

         this.y.put(var1.asLong(), this.x(var2.getItem()));
         ItemStack var6 = var4 ? var2.copy() : ItemStack.EMPTY;
         int var7 = var4 ? var2.getCount() : 1;
         this.S.put(var1.asLong(), new dr.g(var7, new dr.c(var3, var6), System.currentTimeMillis()));
      }
   }

   public void d(BlockPos var1, BlockState var2) {
      this.l();
      dr.g var3 = this.S.get(var1.asLong());
      if (var3 != null && this.K(var2)) {
         this.l.remove(var1.asLong());
         this.x.put(var1.asLong(), var3.stackCount);
         this.O.putIfAbsent(var1.asLong(), var3.createdAtMs);
         this.K(this.T, var1);
         this.w(var1);
      }
   }

   public void P() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.H() && var1.player != null) {
         for (Entry var3 : new ArrayList<>(this.S.entrySet())) {
            if (this.x.containsKey(var3.getKey())) {
               dr.g var4 = (dr.g)var3.getValue();
               if (var4.hiddenStack != null && !var4.hiddenStack.stack.isEmpty()) {
                  this.b((Long)var3.getKey(), var4.hiddenStack, true);
               }

               this.x.put((Long)var3.getKey(), var4.stackCount);
            }
         }
      }
   }

   public void I() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null && this.H() && var1.level == this.T && var1.player == this.U && !wt.v()) {
         this.Y(var1.player.getInventory());
         this.V
            .entrySet()
            .removeIf(
               var1x -> {
                  ItemStack var2 = var1.player.getInventory().getItem(var1x.getKey());
                  dr.d var3x = var1x.getValue();
                  return var1.player.tickCount >= var3x.expires
                     || !var2.isEmpty() && !var2.is(var3x.stack.getItem())
                     || var2.is(var3x.stack.getItem()) && var2.getCount() >= var3x.expected;
               }
            );

         for (Entry var3 : this.n.entrySet()) {
            if (!this.S.values().stream().anyMatch(var1x -> var1x.hiddenStack != null && var1x.hiddenStack.preferredSlot == (Integer)var3.getKey())) {
               ItemStack var4 = var1.player.getInventory().getItem((Integer)var3.getKey());
               if (var4.is(((ItemStack)var3.getValue()).getItem())
                  && var4.getCount() > ((ItemStack)var3.getValue()).getCount()
                  && this.E((Integer)var3.getKey(), var4.getCount() - ((ItemStack)var3.getValue()).getCount())) {
                  break;
               }
            }
         }
      }
   }

   void Y(Container var1) {
      Iterator var2 = this.n.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         int var4 = (Integer)var3.getKey();
         ItemStack var5 = var1.getItem(var4);
         ItemStack var6 = (ItemStack)var3.getValue();
         if (var5.is(var6.getItem()) && var5.getCount() >= var6.getCount()) {
            this.t(var4, var5);
         } else {
            int var7 = var5.is(var6.getItem()) ? var5.getCount() : 0;
            int var8 = var6.getCount() - var7;
            if (var7 == 0) {
               var2.remove();
            } else {
               var3.setValue(var5.copyWithCount(var7));
            }

            for (Entry var10 : this.Y.entrySet()) {
               ArrayDeque var11 = new ArrayDeque();

               for (dr.c var13 : (Deque)var10.getValue()) {
                  int var14 = var13.preferredSlot == var4 ? Math.min(var8, var13.stack.getCount()) : 0;
                  var8 -= var14;
                  if (var14 > 0) {
                     this.x.computeIfPresent((Long)var10.getKey(), (var1x, var2x) -> Math.max(1, var2x - var14));
                  }

                  if (var13.stack.getCount() > var14) {
                     var11.addLast(new dr.c(var13.preferredSlot, var13.stack.copyWithCount(var13.stack.getCount() - var14)));
                  }
               }

               ((Deque)var10.getValue()).clear();
               ((Deque)var10.getValue()).addAll(var11);
            }

            mt.U(
               "FakeSpawner",
               "Reservation corrected physical={} item={} old={} available={}; real inventory retained",
               var4,
               BuiltInRegistries.ITEM.getKey(var6.getItem()),
               var6.getCount(),
               var7
            );
            u0.V();
         }
      }
   }

   private boolean E(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = this.G();
      return var4 >= 0 && var3.player != null
         ? wt.S("separate reservation surplus", wt.G(var1, var4, var2, var3.player.getInventory().getItem(var1).getCount()), u0::V, u0::V)
         : false;
   }

   public boolean V(BlockPos var1) {
      return var1 != null && this.S.containsKey(var1.asLong());
   }

   public boolean C(BlockPos var1) {
      return this.H() && this.T == Minecraft.getInstance().level && var1 != null && this.l.contains(var1.asLong());
   }

   public void H(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 != null) {
         this.o = var1;
         this.E = var2.getInventory().getSelectedSlot();
         this.p = var2.tickCount + 4;
      }
   }

   public boolean j(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return this.H()
         && var2 == this.U
         && var1 == this.o
         && var2.tickCount <= this.p
         && var2.getInventory().getSelectedSlot() == this.E
         && this.s(var2.getItemInHand(var1));
   }

   public void V(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.H() && var2.level != null && var2.level.getBlockState(var1).isAir()) {
         if (this.Wc.put(var1.asLong(), var2.level.getGameTime() + 10L) == null) {
            this.w(var1);
         }
      }
   }

   public BlockState e(BlockPos var1, BlockState var2) {
      return this.x(var1.asLong(), var2);
   }

   public BlockState x(long var1, BlockState var3) {
      return this.Wp.contains(var1) && var3.isAir() && this.H() && this.T == Minecraft.getInstance().level ? Blocks.SPAWNER.defaultBlockState() : var3;
   }

   public boolean C(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.l();
      if (this.H() && var1 != null && !this.V(var1) && this.Z(var1) > 0 && this.s(var2) && this.x(var2.getItem()) == this.P(var1) && !this.t(var3) && !wt.v()) {
         long var5 = var1.asLong();
         int var7 = var4 ? var2.getCount() : 1;
         if (var7 < var2.getCount()) {
            int var8 = this.G();
            if (var8 < 0) {
               return false;
            }

            Item var9 = var2.getItem();
            return wt.S("single pile addition", wt.G(var3, var8, var7, var2.getCount()), () -> {
               Minecraft var6 = Minecraft.getInstance();
               ItemStack var7x = var6.player.getInventory().getItem(var8);
               if (this.x.containsKey(var5) && var7x.is(var9) && var7x.getCount() == var7) {
                  this.Z(var5, var8, var7x, var7);
               }
            }, u0::V);
         } else {
            return this.Z(var5, var3, var2, var7);
         }
      } else {
         return false;
      }
   }

   private boolean Z(long var1, int var3, ItemStack var4, int var5) {
      if (!this.b(var1, new dr.c(var3, var4.copy()), false)) {
         return false;
      }

      int var6 = (int)Math.min(2147483647L, (long)this.x.getOrDefault(var1, 1).intValue() + var5);
      this.x.put(var1, var6);
      this.O.putIfAbsent(var1, System.currentTimeMillis());
      this.F(Minecraft.getInstance().level, BlockPos.of(var1));
      this.B("You just added another spawner! There are now " + var6 + " spawners in the stack.");
      this.h();
      return true;
   }

   public boolean k() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null && var1.getMainHandItem().is(ItemTags.PICKAXES)) {
         ItemEnchantments var2 = (ItemEnchantments)var1.getMainHandItem().get(DataComponents.ENCHANTMENTS);
         Reference var3 = var1.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
         return var2 != null && var2.getLevel(var3) > 0;
      } else {
         return false;
      }
   }

   public boolean V(BlockPos var1, Direction var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.O(var1)) {
         return false;
      }

      long var4 = var1.asLong();
      if (this.s.containsKey(var4)) {
         return true;
      }

      if (this.k()) {
         this.m.remove(var4);
         this.P.remove(var4);
         this.W.remove(var4);
         return false;
      }

      if (this.W.contains(var4)) {
         return true;
      }

      int var6 = var3.player.tickCount;
      long var7 = var1.asLong();
      if (this.Z.getOrDefault(var7, -1) == var6) {
         return true;
      }

      this.Z.put(var7, var6);
      this.m.computeIfAbsent(var4, var1x -> var6);
      double var9 = var3.player.getAbilities().instabuild ? 1.0 : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
      double var11 = Math.min(1.0, this.P.getOrDefault(var4, 0.0) + var9);
      this.P.put(var4, var11);
      this.Y(var3, var1, var2, var11);
      if (var11 >= 1.0) {
         this.m.remove(var4);
         this.P.remove(var4);
         this.W.add(var4);
         this.s.put(var4, var6 + 4);
         this.t(var1);
         this.M(var1, var2);
         this.M(var1, true);
         this.w(var1);
      }

      return true;
   }

   private void c() {
      Minecraft var1 = Minecraft.getInstance();

      for (Entry var3 : new ArrayList<>(this.s.entrySet())) {
         if (var1.player.tickCount >= (Integer)var3.getValue()) {
            this.s.remove(var3.getKey());
            this.w(BlockPos.of((Long)var3.getKey()));
            this.B("You cannot break spawners without a silk touch!");
         }
      }

      if (!var1.options.keyAttack.isDown()) {
         for (long var6 : this.m.keySet()) {
            this.t(BlockPos.of(var6));
         }

         this.m.clear();
         this.P.clear();
         this.W.clear();
      } else {
         this.m.keySet().removeIf(var2 -> {
            int var3x = var1.hitResult instanceof BlockHitResult var4 && var4.getBlockPos().asLong() == var2 ? 0 : 1;
            if (var3x != 0) {
               this.t(BlockPos.of(var2));
            }

            return (boolean)var3x;
         });
      }
   }

   public boolean Y(BlockPos var1) {
      if (this.V(var1, null)) {
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      if (this.H() && var2 != null && var2.level != null && var2.player != null && var1 != null) {
         if (this.K(var2.level.getBlockState(var1)) && this.Z(var1) > this.W()) {
            long var3 = var1.asLong();
            this.i.putIfAbsent(var3, 0.0F);
            this.Y(var2, var1, null, 0.0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean h(BlockPos var1, Direction var2) {
      if (this.V(var1, var2)) {
         return true;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.H() && var3 != null && var3.level != null && var3.player != null && var1 != null) {
         if (this.O(var1) && var3.player.tickCount < this.a) {
            return true;
         }

         if (this.K(var3.level.getBlockState(var1)) && this.Z(var1) > this.W()) {
            long var4 = var1.asLong();
            int var6 = var3.player.tickCount;
            if (this.Z.getOrDefault(var4, -1) == var6) {
               return true;
            }

            this.Z.put(var4, var6);
            float var7 = var3.player.getAbilities().instabuild ? 1.0F : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
            float var8 = Math.min(1.0F, this.i.getOrDefault(var4, 0.0F) + var7);
            this.i.put(var4, var8);
            this.Y(var3, var1, var2, var8);
            if (var8 >= 1.0F) {
               if (this.g(var1, this.W())) {
                  this.a = var6 + 5;
                  this.M(var1, true);
               }

               this.i.remove(var4);
               this.F.remove(var4);
               this.t(var1);
            }

            return true;
         } else {
            if (var1 != null) {
               this.i.remove(var1.asLong());
               this.F.remove(var1.asLong());
               this.t(var1);
            }

            return false;
         }
      } else {
         if (var1 != null) {
            this.i.remove(var1.asLong());
            this.F.remove(var1.asLong());
            this.t(var1);
         }

         return false;
      }
   }

   public void B(BlockPos var1) {
      if (var1 != null && this.C.contains(var1.asLong())) {
         x7.W(var1);
      }

      if (var1 != null && this.l.contains(var1.asLong())) {
         this.e.put(var1.asLong(), new dr.n(var1.immutable(), System.currentTimeMillis() + 10000L));
      }

      if (var1 != null && this.Z(var1) > 0) {
         this.e.put(var1.asLong(), new dr.n(var1.immutable(), System.currentTimeMillis() + 10000L));
         int var2 = this.x.getOrDefault(var1.asLong(), 0);
         dr.c var3 = this.i(var1.asLong(), var2);
         this.n(var1.asLong(), var2);
         this.m(new ItemStack(this.g(var1.asLong()), 1), 1);
         u0.V();
         this.i.remove(var1.asLong());
         this.F.remove(var1.asLong());
         this.t(var1);
      }
   }

   private int W() {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player == null || !var1.player.isShiftKeyDown() && !var1.options.keyShift.isDown() ? 1 : 64;
   }

   public boolean A(BlockPos var1) {
      return this.g(var1, 64);
   }

   private boolean g(BlockPos var1, int var2) {
      if (this.V(var1)) {
         return false;
      }

      if (this.O(var1)) {
         long var3 = var1.asLong();
         int var5 = this.x.getOrDefault(var3, 0);
         if (var5 <= 0) {
            return false;
         }

         int var6 = Math.min(var2, var5);
         Deque var7 = this.Y.get(var3);
         if (var7 != null && !var7.isEmpty()) {
            ItemStack var8 = Minecraft.getInstance().player.getInventory().getItem(((dr.c)var7.getLast()).preferredSlot);
            int var9 = 0;

            for (dr.c var11 : var7.reversed()) {
               if (!ItemStack.isSameItemSameComponents(var8, Minecraft.getInstance().player.getInventory().getItem(var11.preferredSlot))) {
                  break;
               }

               var9 += var11.stack.getCount();
            }

            if (var9 < var6 && var9 < var5 - 1) {
               var6 = var9;
            }
         }

         if (!this.A(var3, var6)) {
            return false;
         }

         this.n(var3, var6);
         return true;
      } else {
         return false;
      }
   }

   private void n(long var1, int var3) {
      int var4 = Math.max(0, this.x.getOrDefault(var1, 0) - var3);
      BlockPos var5 = BlockPos.of(var1);
      if (var4 > 0) {
         this.x.put(var1, var4);
         this.O.put(var1, System.currentTimeMillis());
      } else {
         this.l.add(var1);
         this.x.remove(var1);
         this.S.remove(var1);
         this.O.remove(var1);
         this.z.remove(var1);
         this.Y.remove(var1);
         this.t(var1);
         this.w(var5);
         if (var5.equals(this.r)) {
            this.r = null;
         }
      }

      this.e.put(var1, new dr.n(var5.immutable(), System.currentTimeMillis() + 10000L));
      this.h();
   }

   public boolean O(BlockPos var1) {
      if (var1 == null) {
         return false;
      }

      long var2 = var1.asLong();
      return this.x.getOrDefault(var2, 0) > 0 && !this.Wy.contains(var2) && this.T == Minecraft.getInstance().level && this.H();
   }

   public dr.p q(BlockPos var1, SoundEvent var2, float var3, float var4) {
      if (var1 != null && var2 != null && this.O(var1)) {
         BlockState var5 = ((BlockItem)this.g(var1.asLong())).getBlock().defaultBlockState();
         if (var5 == null) {
            return null;
         } else {
            SoundType var6 = var5.getSoundType();
            SoundType var7 = Blocks.SPAWNER.defaultBlockState().getSoundType();
            if (E(var2, var6.getPlaceSound())) {
               return new dr.p(var7.getPlaceSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else if (E(var2, var6.getBreakSound())) {
               return new dr.p(var7.getBreakSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else {
               return E(var2, var6.getHitSound()) ? new dr.p(var7.getHitSound(), (var7.getVolume() + 1.0F) / 8.0F, var7.getPitch() * 0.5F) : null;
            }
         }
      } else {
         return null;
      }
   }

   private static boolean E(SoundEvent var0, SoundEvent var1) {
      return var0 == var1 || var0.location().equals(var1.location());
   }

   private void F(Level var1, BlockPos var2) {
      if (var1 != null && var2 != null && this.O(var2)) {
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

   private void M(BlockPos var1, boolean var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null && this.O(var1)) {
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

   public BlockState A(BlockPos var1, BlockState var2) {
      return this.f(var1, var2);
   }

   public SpawnerBlockEntity K(Level var1, BlockPos var2) {
      Map var3 = this.I;
      if (var1 != null && var1 == this.T && this.O(var2) && !this.s.containsKey(var2.asLong())) {
         long var4 = var2.asLong();
         SpawnerBlockEntity var6 = (SpawnerBlockEntity)var3.get(var4);
         if (var6 == null && Minecraft.getInstance().isSameThread()) {
            var6 = new SpawnerBlockEntity(var2.immutable(), Blocks.SPAWNER.defaultBlockState());
            var6.setEntityId(this.P(var2).w, var1.getRandom());
            var6.setLevel(var1);
            var6.getSpawner().getOrCreateDisplayEntity(var1, var2);
            var3.put(var4, var6);
            ((List)this.j.computeIfAbsent(ChunkPos.pack(var2.getX() >> 4, var2.getZ() >> 4), var0 -> new ArrayList())).add(var6);
         }

         return var3 == this.I && var1 == this.T && this.O(var2) ? var6 : null;
      } else {
         return null;
      }
   }

   private void w(BlockPos var1) {
      this.a();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var2.level == this.T) {
         this.v++;
         var2.levelExtractor.setBlocksDirty(var1.getX() - 1, var1.getY() - 1, var1.getZ() - 1, var1.getX() + 1, var1.getY() + 1, var1.getZ() + 1);
      }
   }

   public Collection<SpawnerBlockEntity> D(Level var1) {
      return var1 == this.T && this.H() ? this.I.values() : List.of();
   }

   private void t(long var1) {
      SpawnerBlockEntity var3 = this.I.remove(var1);
      if (var3 != null) {
         BlockPos var4 = var3.getBlockPos();
         long var5 = ChunkPos.pack(var4.getX() >> 4, var4.getZ() >> 4);
         List var7 = (List)this.j.get(var5);
         if (var7 != null) {
            var7.remove(var3);
            if (var7.isEmpty()) {
               this.j.remove(var5);
            }
         }
      }
   }

   public Collection<SpawnerBlockEntity> S(Level var1, Vec3 var2, int var3) {
      if (var1 == this.T && this.H() && !this.j.isEmpty()) {
         ArrayList var4 = null;
         int var5 = Mth.floor(var2.x - var3) >> 4;
         int var6 = Mth.floor(var2.x + var3) >> 4;
         int var7 = Mth.floor(var2.z - var3) >> 4;
         int var8 = Mth.floor(var2.z + var3) >> 4;

         for (int var9 = var5; var9 <= var6; var9++) {
            for (int var10 = var7; var10 <= var8; var10++) {
               List var11 = (List)this.j.get(ChunkPos.pack(var9, var10));
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

   public boolean W(BlockEntity var1) {
      if (var1 instanceof SpawnerBlockEntity var2) {
         BlockPos var3 = var2.getBlockPos();
         if (var3 == null) {
            return false;
         }

         SpawnerBlockEntity var4 = this.I.get(var3.asLong());
         return var4 == var1;
      } else {
         return false;
      }
   }

   public void X(String var1, BlockPos var2, BlockEntity var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 != null) {
            this.z(
               "request:" + var1 + ":" + var2.asLong(),
               1500L,
               "{} requested fake block entity pos={} result={} count={}",
               var1,
               var2,
               var3 != null ? var3.getClass().getSimpleName() : "null",
               this.Z(var2)
            );
         }
      }
   }

   public void N(String var1, BlockPos var2, String var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 == null) {
            this.z("render:" + var1, 1500L, "renderer {} {}", var1, var3);
         } else {
            this.z("render:" + var1 + ":" + var2.asLong(), 1500L, "renderer {} pos={} count={} {}", var1, var2, this.Z(var2), var3);
         }
      }
   }

   public void I(LevelChunk var1, Collection<BlockPos> var2) {
      this.l();
      this.X();
      if (this.H() && var1 != null) {
         int var3 = var1.getPos().x();
         int var4 = var1.getPos().z();
         int var5 = var3 << 4;
         int var6 = var4 << 4;
         int var7 = var5 + 15;
         int var8 = var6 + 15;
         MutableBlockPos var9 = new MutableBlockPos();
         HashSet var10 = new HashSet<>(this.x.keySet());
         var10.addAll(this.l);

         for (Long var12 : var10) {
            BlockPos var13 = BlockPos.of(var12);
            int var14 = var13.getX();
            int var15 = var13.getZ();
            if (var14 >= var5 && var14 <= var7 && var15 >= var6 && var15 <= var8) {
               var9.set(var14, var13.getY(), var15);
               if (!var1.getLevel().isOutsideBuildHeight(var9)) {
                  this.K(var9, var1.getSection(var1.getSectionIndex(var9.getY())).getBlockState(var9.getX() & 15, var9.getY() & 15, var9.getZ() & 15));
               }
            }
         }

         if (var2 != null) {
            for (BlockPos var17 : new ArrayList(var2)) {
               if (var17 != null) {
                  this.K(var17, var1.getBlockState(var17));
               }
            }
         }
      }
   }

   public void k() {
      long var1 = Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
      this.l();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var3.player != null && this.H()) {
         this.w(var3);
         this.e();

         for (Entry var5 : this.Wc.entrySet()) {
            if (var3.level.getGameTime() >= (Long)var5.getValue()) {
               this.Wc.remove(var5.getKey());
               this.w(BlockPos.of((Long)var5.getKey()));
            }
         }

         this.c();
         if (!this.e.isEmpty() || !this.i.isEmpty() || !this.l.isEmpty()) {
            for (Entity var6 : var3.level.entitiesForRendering()) {
               if (var6 instanceof ItemEntity var8) {
                  this.L(var8);
               }
            }
         }

         this.B();
         this.e.entrySet().removeIf(var0 -> var0.getValue().untilMs < System.currentTimeMillis());
         this.h.removeIf(var1x -> !(var3.level.getEntity(var1x) instanceof ItemEntity));
         wt.C();
         this.I();

         for (Entry var17 : new ArrayList<>(this.S.entrySet())) {
            if (!this.x.containsKey(var17.getKey()) && System.currentTimeMillis() - ((dr.g)var17.getValue()).createdAtMs > 5000L) {
               this.K(BlockPos.of((Long)var17.getKey()), null);
            }
         }

         if (!var3.options.keyAttack.isDown()) {
            for (Long var18 : new ArrayList<>(this.i.keySet())) {
               this.t(BlockPos.of(var18));
            }

            this.i.clear();
            this.F.clear();
         }

         for (Long var19 : new ArrayList<>(this.i.keySet())) {
            if (!(var3.hitResult instanceof BlockHitResult var22 && var22.getBlockPos().equals(BlockPos.of(var19)))) {
               this.t(BlockPos.of(var19));
               this.i.remove(var19);
               this.F.remove(var19);
            }
         }

         if (var3 != null && var3.level != null && var3.player != null && this.H()) {
            for (BlockEntity var21 : this.S(var3.level, var3.player.position(), 32)) {
               if (var21 instanceof SpawnerBlockEntity var23
                  && var23.getBlockPos() != null
                  && var3.level.hasChunkAt(var23.getBlockPos())
                  && this.O(var23.getBlockPos())
                  && var23.getBlockPos().distToCenterSqr(var3.player.position()) <= 1024.0) {
                  SpawnerBlockEntity.clientTick(var3.level, var23.getBlockPos(), var23.getBlockState(), var23);
               }
            }

            long var14 = var3.level.getGameTime();

            for (BlockPos var26 : this.x(var3.player.blockPosition(), 16, 8)) {
               if ((var14 + Math.floorMod(var26.asLong(), 3L)) % 20L == 0L) {
                  this.p(var3.level, var26);
               }
            }
         } else {
            for (Long var20 : new ArrayList<>(this.i.keySet())) {
               this.t(BlockPos.of(var20));
            }

            this.i.clear();
            this.F.clear();
         }

         if (var1 != 0L) {
            long var15 = System.nanoTime() - var1;
            long var25 = System.currentTimeMillis();
            if (var25 >= this.q) {
               this.q = var25 + 5000L;
               this.k(
                  "Work counters: piles={} reservations={} mappingRebuilds={} terrainInvalidations={} inventoryUpdates={} renderCandidates={} renderExtractions={} displayCopies={} tickMicros={}",
                  this.x.size(),
                  this.n.size(),
                  u0.F(),
                  this.v,
                  this.H,
                  this.u,
                  this.R,
                  this.t,
                  var15 / 1000L
               );
            }

            if (var15 > 5000000L) {
               this.z("tick-spike", 2000L, "Client tick CPU spike micros={} piles={} reservations={}", var15 / 1000L, this.x.size(), this.n.size());
            }
         }
      } else {
         this.u();
      }
   }

   public int Z(BlockPos var1) {
      return !this.O(var1) ? 0 : this.x.getOrDefault(var1.asLong(), 0);
   }

   public List<BlockPos> x(BlockPos var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();
      if (this.H() && var1 != null && !this.x.isEmpty()) {
         int var5 = var1.getX();
         int var6 = var1.getY();
         int var7 = var1.getZ();

         for (SpawnerBlockEntity var9 : this.S(this.T, Vec3.atCenterOf(var1), var2 + 1)) {
            BlockPos var10 = var9.getBlockPos();
            if (this.O(var10) && Math.abs(var10.getX() - var5) <= var2 && Math.abs(var10.getY() - var6) <= var3 && Math.abs(var10.getZ() - var7) <= var2) {
               var4.add(var10);
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   public boolean L(ItemEntity var1) {
      if (this.H() && var1 != null && this.W(var1.getItem()) && !h2.M(var1.getItem())) {
         long var2 = System.currentTimeMillis();
         if (this.h.contains(var1.getId())) {
            return true;
         }

         HashSet var4 = new HashSet<>(this.l);
         var4.addAll(this.i.keySet());

         for (long var6 : var4) {
            BlockPos var8 = BlockPos.of(var6);
            if (var1.distanceToSqr(var8.getX() + 0.5, var8.getY() + 0.5, var8.getZ() + 0.5) <= 4.84) {
               this.h.add(var1.getId());
               return true;
            }
         }

         this.e.entrySet().removeIf(var2x -> var2x.getValue().untilMs < var2);

         for (dr.n var14 : this.e.values()) {
            double var7 = var14.pos.getX() + 0.5 - var1.getX();
            double var9 = var14.pos.getY() + 0.5 - var1.getY();
            double var11 = var14.pos.getZ() + 0.5 - var1.getZ();
            if (var7 * var7 + var9 * var9 + var11 * var11 <= 4.840000000000001) {
               this.h.add(var1.getId());
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean H(ItemEntity var1) {
      return this.L(var1);
   }

   public ItemStack v(ItemStack var1) {
      if (!this.H(var1)) {
         return var1;
      }

      ItemStack var2 = new ItemStack(Items.SPAWNER, var1.getCount());
      var2.set(DataComponents.CUSTOM_NAME, this.s(var1));
      var2.set(DataComponents.LORE, new ItemLore(List.of(f(this.x(var1.getItem()).Z, ChatFormatting.YELLOW))));
      if (this.l()) {
         var2.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
      }

      return var2;
   }

   public ItemStack t(ItemStack var1) {
      return var1;
   }

   public boolean m(ItemStack var1) {
      return this.H() && var1 != null && !var1.isEmpty() && !d1.d(var1) && this.c(var1) && this.T(var1) > 0L;
   }

   public Component c(long var1) {
      return f("~", ChatFormatting.DARK_GRAY).append(T("$ ", 65280)).append(f(this.u(var1), ChatFormatting.WHITE));
   }

   public Component d(ItemStack var1) {
      return this.X(this.e(var1));
   }

   public Component g(ItemStack var1) {
      long var2 = this.T(var1);
      long var4 = Math.max(1L, (var2 + 63L) / 64L);
      return f("Contains: " + var4 + " stack(s)", ChatFormatting.WHITE);
   }

   public Component h() {
      return f("Left Shift", ChatFormatting.GOLD).append(f(": ", ChatFormatting.GOLD)).append(f("view contents", ChatFormatting.WHITE));
   }

   public Component x() {
      return f("Left Alt + Left Shift", ChatFormatting.GOLD).append(f(": ", ChatFormatting.GOLD)).append(f("view contents", ChatFormatting.WHITE));
   }

   public Component d() {
      return f("Left Control", ChatFormatting.GOLD).append(f(": ", ChatFormatting.GOLD)).append(f("lock tooltip", ChatFormatting.WHITE));
   }

   public List<Component> w(ItemStack var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1.getHoverName());
      var3.add(this.d(var1));
      if (!var2) {
         return var3;
      } else {
         boolean var4 = this.y(341);
         boolean var5 = this.y(340);
         boolean var6 = this.y(342);
         if (var4) {
            return var3;
         } else if (var5 && var6) {
            var3.add(this.d());
            return var3;
         } else if (var5) {
            var3.add(this.g(var1));
            var3.add(this.x());
            var3.add(this.d());
            return var3;
         } else {
            var3.add(this.g(var1));
            var3.add(this.h());
            return var3;
         }
      }
   }

   public Component E() {
      FakeSpawnerModule var1 = FakeSpawnerModule.T();
      String var2 = var1 != null ? var1.X() : "§dSpawner";
      return O(var2);
   }

   public Component s(ItemStack var1) {
      Component var2 = (Component)var1.get(DataComponents.CUSTOM_NAME);
      if (var2 != null) {
         return Component.literal(var2.getString()).withStyle(new ChatFormatting[]{ChatFormatting.WHITE, ChatFormatting.ITALIC});
      }

      cl var3 = this.x(var1.getItem());
      return f("Spawner", ChatFormatting.LIGHT_PURPLE);
   }

   public List<Component> X(int var1, boolean var2, boolean var3) {
      return this.T(new ItemStack(this.N(), var1), var2, var3);
   }

   public List<Component> T(ItemStack var1, boolean var2, boolean var3) {
      int var4 = var1.getCount();
      ArrayList var5 = new ArrayList();
      var5.add(f(this.x(var1.getItem()).Z, ChatFormatting.YELLOW));
      if (var2) {
         var5.add(f("~", ChatFormatting.DARK_GRAY).append(T("$ ", 65280)).append(f(this.O(var4), ChatFormatting.WHITE)));
      }

      var5.add(Component.empty());
      var5.add(f("Interact with Spawn Egg:", ChatFormatting.GRAY));
      var5.add(f(" Sets Mob Type", ChatFormatting.BLUE));
      if (var3) {
         var5.add(f("minecraft:spawner", ChatFormatting.DARK_GRAY));
         var5.add(f("13 component(s)", ChatFormatting.DARK_GRAY));
      }

      return var5;
   }

   public boolean E() {
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

   public boolean l() {
      FakeSpawnerModule var1 = FakeSpawnerModule.T();
      return var1 != null && var1.d();
   }

   public void A() {
      this.S(this.r);
   }

   public void S(BlockPos var1) {
      this.l();
      Minecraft var2 = Minecraft.getInstance();
      if (this.H() && var2 != null && var2.level != null && var2.player != null) {
         this.r = var1 == null ? null : var1.immutable();
         long var3 = this.V(var1);
         int var5 = this.h(var1);
         cl var6 = this.P(var1);
         dr.m var7 = this.L(var3, var5, var6);
         Map var8 = var1 == null ? Map.of() : this.z.getOrDefault(var1.asLong(), Map.of());
         var7 = new dr.m(
            Math.max(0, var7.R - var8.getOrDefault(var6.V, 0) - z_.K(var6.V)),
            Math.max(0, var7.u - var8.getOrDefault(var6.S, 0) - z_.K(var6.S)),
            var7.h,
            var7.e
         );
         String var9 = var5 + " " + h(var6.Z.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"));
         SimpleContainer var10 = new SimpleContainer(54);
         int var11 = 0;
         var11 = h(var10, var11, var6.V, var7.R);
         h(var10, var11, var6.S, var7.u);
         var10.setItem(
            48,
            h(
               Items.GOLD_INGOT.getDefaultInstance(),
               f("Sell All", ChatFormatting.WHITE),
               List.of(f("Click to sell all loot", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         var10.setItem(49, h(var6.a(), t(var5 + " " + var6.Z.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"), 64648), this.E(var7, var6)));
         var10.setItem(
            50,
            h(
               Items.DROPPER.getDefaultInstance(),
               f("Drop Loot", ChatFormatting.WHITE),
               List.of(f("Click to drop all loot on the page", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         ChestMenu var12 = new ChestMenu(MenuType.GENERIC_9x6, 0, var2.player.getInventory(), var10, 6);
         var2.gui.setScreen(new eu(var12, var2.player.getInventory(), Component.literal(var9), var1));
      }
   }

   public void f(BlockPos var1, Item var2, int var3) {
      if (var1 != null && var2 != null && var3 > 0) {
         this.z.computeIfAbsent(var1.asLong(), var0 -> new HashMap<>()).merge(var2, var3, Integer::sum);
      }
   }

   public long e(ItemStack var1) {
      if (!this.H() || var1.isEmpty() || d1.d(var1)) {
         return 0L;
      }

      if (this.W(var1)) {
         return var1.getCount() * 1000L;
      }

      if (cl.b(var1.getItem())) {
         return this.d(var1.getItem(), var1.getCount());
      }

      ItemContainerContents var2 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
      return var2 == null ? 0L : var2.nonEmptyItemCopyStream().mapToLong(this::e).sum() * var1.getCount();
   }

   public Component X(long var1) {
      double var3 = var1 >= 1000000000L ? 1.0E9 : (var1 >= 1000000L ? 1000000.0 : (var1 >= 1000L ? 1000.0 : 1.0));
      String var5 = var3 == 1.0E9 ? "B" : (var3 == 1000000.0 ? "M" : (var3 == 1000.0 ? "K" : ""));
      return f("~", ChatFormatting.DARK_GRAY).append(T("$ ", 65280)).append(f(E(var1 / var3, var3 == 1.0 ? 0 : 1) + var5, ChatFormatting.WHITE));
   }

   public long G(int var1) {
      return this.d(Items.BONE, var1);
   }

   public long d(Item var1, int var2) {
      Double var3 = FakeSpawnerModule.T().G(BuiltInRegistries.ITEM.getKey(var1).toString());
      if (var3 != null) {
         return Math.round(var3 * Math.max(0, var2));
      }

      String var4 = BuiltInRegistries.ITEM.getKey(var1).getPath().replace('_', ' ');
      op var5 = op.a();
      var5.l(var4, var0 -> {});
      double var6 = this.G.getOrDefault(var1, (double)var5.X(var4));
      return Math.round(var6 * Math.max(0, var2));
   }

   public void B() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gui.screen() != null && var1.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("order")) {
         AbstractContainerMenu var2 = var1.player.containerMenu;
         HashMap var3 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5.container != var1.player.getInventory()) {
               ItemStack var6 = var5.getItem();
               if (cl.b(var6.getItem())) {
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

         var3.forEach((var1x, var2x) -> this.G.merge(var1x, var2x, Math::max));
      }
   }

   public int l() {
      return this.g(Items.BONE);
   }

   private boolean I(ItemStack var1) {
      if (var1.has(DataComponents.CUSTOM_NAME)) {
         return false;
      }

      FakeSpawnerModule var2 = FakeSpawnerModule.T();

      for (cl var6 : cl.values()) {
         String var7 = var2.x(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1.is(var6.V)) {
            return true;
         }
      }

      return false;
   }

   private int g(Item var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.H() && var2.player != null) {
         int var3 = -1;
         int var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6.is(var1) && !var6.has(DataComponents.CUSTOM_NAME) && !cy.t(var6) && var6.getCount() > var4) {
               var3 = var5;
               var4 = var6.getCount();
            }
         }

         return this.B = var3;
      } else {
         return -1;
      }
   }

   public void Q(int var1) {
   }

   public void X(AbstractContainerMenu var1, Slot var2) {
   }

   public void f(Slot var1) {
   }

   public void D(BlockPos var1) {
      if (var1 != null && this.e(var1.asLong())) {
         this.t(var1);
         this.w(var1);
         mt.U("FakeSpawner", "Final break predicted at {}; retaining pile and reservations until server acknowledgment", var1);
      }
   }

   boolean e(long var1) {
      return this.x.getOrDefault(var1, 0) > 0 && this.Wy.add(var1);
   }

   boolean T(long var1) {
      return this.Wy.remove(var1);
   }

   public BlockState a(Level var1, BlockPos var2, BlockState var3) {
      if (this.H() && var1 == this.T && this.x.getOrDefault(var2.asLong(), 0) > 0 && var1 instanceof ClientLevel var4) {
         if (var1.isOutsideBuildHeight(var2)) {
            return Blocks.AIR.defaultBlockState();
         }

         LevelChunk var5 = var4.getChunkAt(var2);
         return var5.getSection(var5.getSectionIndex(var2.getY())).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15);
      } else {
         return var3;
      }
   }

   public void K(Runnable var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.H() && var2.player != null && var2.isSameThread()) {
         try {
            this.n(var2.player.getInventory(), var1);
         } finally {
            this.e();
         }
      } else {
         var1.run();
      }
   }

   void n(Container var1, Runnable var2) {
      try {
         var2.run();
      } finally {
         u0.V();
      }
   }

   private void t(int var1, ItemStack var2) {
      ItemStack var3 = this.n.get(var1);
      if (var3 != null && var2.is(var3.getItem()) && var2.getCount() >= var3.getCount() && !ItemStack.isSameItemSameComponents(var2, var3)) {
         this.n.put(var1, var2.copyWithCount(var3.getCount()));

         for (Deque var5 : this.Y.values()) {
            ArrayDeque var6 = new ArrayDeque();

            for (dr.c var8 : var5) {
               var6.addLast(var8.preferredSlot == var1 && var8.stack.is(var2.getItem()) ? new dr.c(var1, var2.copyWithCount(var8.stack.getCount())) : var8);
            }

            var5.clear();
            var5.addAll(var6);
         }
      }
   }

   public boolean y(ItemStack var1) {
      return this.H() && !var1.isEmpty() && this.n.values().stream().anyMatch(var1x -> ItemStack.isSameItemSameComponents(var1, var1x));
   }

   public boolean v(int var1, int var2, int var3, ContainerInput var4) {
      if (!this.H() || wt.V()) {
         return true;
      }

      if (wt.v()) {
         return false;
      }

      LocalPlayer var5 = Minecraft.getInstance().player;
      return var5 != null && var5.containerMenu.containerId == var1 ? this.v(var5.containerMenu, var2, var3, var4) : true;
   }

   public boolean v(AbstractContainerMenu var1, int var2, int var3, ContainerInput var4) {
      if (!this.H() || wt.V()) {
         return true;
      }

      if (wt.v()) {
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
         if (var6.container == var5.getInventory() && (this.t(var6.getContainerSlot()) || z_.w(var6.getContainerSlot()) || this.M(var6.getContainerSlot()))) {
            return false;
         }
      }

      return var4 != ContainerInput.SWAP || !this.t(var3) && !z_.w(var3) && !this.M(var3)
         ? var4 != ContainerInput.PICKUP_ALL || !this.y(var1.getCarried())
         : false;
   }

   public void F(int var1, Runnable var2) {
      this.K(var2);
   }

   public boolean a(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      ItemStack var4 = this.n.get(var2);
      if (var4 == null || var4.isEmpty()) {
         return false;
      }

      if (var3.player != null
         && var3.gameMode != null
         && var1 >= 9
         && var1 < 36
         && !this.t(var1)
         && var3.player.inventoryMenu.getCarried().isEmpty()
         && var3.player.containerMenu == var3.player.inventoryMenu) {
         if (this.I(var3.player.getInventory().getItem(var1))) {
            this.Q(var2);
         }

         wt.Y(() -> var3.gameMode.handleContainerInput(0, var1, var2, ContainerInput.SWAP, var3.player));
         if (var4 != null && !var4.isEmpty()) {
            this.n.remove(var2);
            this.n.put(var1, var4);

            for (Deque var6 : this.Y.values()) {
               ArrayDeque var7 = new ArrayDeque();

               for (dr.c var9 : var6) {
                  var7.addLast(var9.preferredSlot == var2 ? new dr.c(var1, var9.stack) : var9);
               }

               var6.clear();
               var6.addAll(var7);
            }

            u0.V();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean t(int var1) {
      return this.H() && this.n.containsKey(var1) && !this.n.get(var1).isEmpty();
   }

   public int p(int var1, ItemStack var2) {
      ItemStack var3 = this.n.get(var1);
      return this.H() && var3 != null && var2.is(var3.getItem()) ? Math.max(0, var2.getCount() - var3.getCount()) : 0;
   }

   public ItemStack U(int var1, ItemStack var2) {
      if (!this.t(var1)) {
         return var2;
      }

      int var3 = this.p(var1, var2);
      return var3 == 0 ? ItemStack.EMPTY : h2.H(var2.copyWithCount(var3));
   }

   public boolean w(AbstractContainerMenu var1, List<wt.a> var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var1 == var3.player.inventoryMenu && var3.player.hasInfiniteMaterials() && var3.player.containerMenu instanceof ItemPickerMenu
         )
       {
         this.K(() -> {
            for (wt.a var4 : var2) {
               var1.clicked(var4.slot(), var4.button(), var4.type(), var3.player);
            }

            var1.broadcastChanges();
         });
         return true;
      } else {
         return false;
      }
   }

   public boolean p(AbstractContainerMenu var1, Slot var2, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var2 != null && var2.container == var4.player.getInventory() && var3 >= 0 && var3 <= 1) {
         int var5 = var2.getContainerSlot();
         ItemStack var6 = var4.player.getInventory().getItem(var5);
         int var7 = this.p(var5, var6);
         if (var7 != 0 && var1.getCarried().isEmpty()) {
            int var8 = var3 == 0 ? var7 : (var7 + 1) / 2;
            ArrayList var9 = new ArrayList();
            var9.add(new wt.a(var2.index, 0, ContainerInput.PICKUP));

            for (int var10 = 0; var10 < var6.getCount() - var8; var10++) {
               var9.add(new wt.a(var2.index, 1, ContainerInput.PICKUP));
            }

            int var11 = var6.getCount() - var7;
            if (this.w(var1, var9)) {
               mt.U("FakeSpawner", "Creative surplus picked physical={} amount={} reserved={}", var5, var8, var11);
               return true;
            } else {
               wt.p(var1, "pick unreserved surplus", var9, var6.copyWithCount(var8), u0::V, u0::V);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int G() {
      Inventory var1 = Minecraft.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.t(var2) && !this.M(var2) && var1.getItem(var2).isEmpty()) {
            return var2;
         }
      }

      return -1;
   }

   public int u(int var1) {
      return this.p(Items.BONE, var1);
   }

   public int p(Item var1, int var2) {
      return z_.p(var1, var2);
   }

   public boolean b(ItemStack var1) {
      return false;
   }

   public boolean u() {
      return this.W(1) == 1;
   }

   public int W(int var1) {
      return this.s(Items.BONE, var1);
   }

   public int s(Item var1, int var2) {
      return this.p(var1, var2);
   }

   public boolean Q(int var1) {
      return var1 == 48 || var1 == 50;
   }

   public boolean J(Component var1, AbstractContainerMenu var2) {
      if (var1 != null && var2 != null) {
         String var3 = var1.getString().toLowerCase(Locale.ROOT);
         return var3.contains("sell") && var2.slots.size() >= 72;
      } else {
         return false;
      }
   }

   public Component z(Component var1) {
      return var1 == null ? Component.literal(h("place items in here to sell")) : Component.literal(h(var1.getString()));
   }

   public void B(Component var1, AbstractContainerMenu var2) {
      if (this.H() && this.J(var1, var2)) {
         long var3 = 0L;

         for (int var5 = 0; var5 < Math.max(0, var2.slots.size() - 36); var5++) {
            var3 += this.e(var2.getSlot(var5).getItem());
         }

         if (var3 > 0L) {
            k9.S(var3);
         }
      }
   }

   private long V(BlockPos var1) {
      long var2 = System.currentTimeMillis();
      return var1 == null ? var2 : this.O.computeIfAbsent(var1.asLong(), var2x -> var2);
   }

   private int h(BlockPos var1) {
      return var1 == null ? Math.max(1, FakeSpawnerModule.T().m()) : Math.max(1, this.x.getOrDefault(var1.asLong(), 1));
   }

   private dr.m e(long var1, int var3) {
      return this.L(var1, var3, cl.A);
   }

   private dr.m L(long var1, int var3, cl var4) {
      double var5 = this.k(var3);
      double var7 = var4.S == Items.AIR ? 0.0 : Math.max(0.15, var5 * 0.33);
      long var9 = Math.max(0L, System.currentTimeMillis() - var1);
      long var11 = var9 / 60000L;
      return new dr.m(Math.max(0, (int)(var11 * var5)), Math.max(0, (int)(var11 * var7)), var5, var7);
   }

   private double k(int var1) {
      int var2 = Math.max(1, var1);
      if (var2 == 1) {
         return 1.9;
      } else {
         return var2 <= 64 ? var2 * 1.9453125 : 124.5 + (var2 - 64) * 0.88671875;
      }
   }

   private static int h(SimpleContainer var0, int var1, Item var2, int var3) {
      int var4 = Math.max(0, var3);

      while (var4 > 0 && var1 < 45) {
         int var5 = Math.min(64, var4);
         var0.setItem(var1++, new ItemStack(var2, var5));
         var4 -= var5;
      }

      return var1;
   }

   private List<Component> E(dr.m var1, cl var2) {
      int var3 = var1 == null ? 0 : Math.max(0, var1.u);
      int var4 = var1 == null ? 0 : Math.max(0, var1.R);
      if (var3 <= 0 && var4 <= 0) {
         return List.of(T("(0% filled)", 64648));
      }

      ArrayList var5 = new ArrayList();
      if (var3 > 0) {
         var5.add(T(var3 + " ", 64648).append(var2.S.getName(new ItemStack(var2.S)).copy().withStyle(ChatFormatting.WHITE)));
      }

      if (var4 > 0) {
         var5.add(T(var4 + " ", 64648).append(var2.V.getName(new ItemStack(var2.V)).copy().withStyle(ChatFormatting.WHITE)));
      }

      var5.add(T("(0% filled)", 64648));
      return var5;
   }

   private boolean A(long var1, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && !wt.v() && var4.player.containerMenu == var4.player.inventoryMenu && var4.player.inventoryMenu.getCarried().isEmpty()) {
         Deque var5 = this.Y.get(var1);
         if (var5 != null && !var5.isEmpty()) {
            LinkedHashMap var6 = new LinkedHashMap();
            int var7 = var3;

            for (dr.c var9 : var5.reversed()) {
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
               ItemStack var12 = this.n.get(var17.getKey());
               if (var12 != null
                  && var12.getCount() == (Integer)var17.getValue()
                  && var4.player.getInventory().getItem((Integer)var17.getKey()).getCount() == (Integer)var17.getValue()) {
                  var14 = (Integer)var17.getKey();
                  break;
               }
            }

            if (var14 < 0) {
               var14 = this.G();
            }

            if (var14 < 0) {
               return false;
            }

            ArrayList var16 = new ArrayList();

            for (Entry var20 : var6.entrySet()) {
               if ((Integer)var20.getKey() != var14) {
                  var16.addAll(
                     wt.G((Integer)var20.getKey(), var14, (Integer)var20.getValue(), var4.player.getInventory().getItem((Integer)var20.getKey()).getCount())
                  );
               }
            }

            if (var16.isEmpty()) {
               this.i(var1, var3);
               if (var7 > 0) {
                  this.m(new ItemStack(this.g(var1), var7), var7);
               }

               u0.V();
               return true;
            } else {
               int var19 = var14;
               int var21 = var7;
               wt.S("mining return", var16, () -> {
                  this.i(var1, var3);
                  this.n(var1, var3);
                  if (var21 > 0) {
                     this.m(new ItemStack(this.g(var1), var21), var21);
                  }

                  this.M(BlockPos.of(var1), true);
                  u0.V();
                  this.k("Mining return predicted pile={} physical={} amount={} worldPending={}", var1, var19, var13, var21);
               }, () -> this.I());
               return false;
            }
         } else {
            this.m(new ItemStack(this.g(var1), var3), var3);
            return true;
         }
      } else {
         return false;
      }
   }

   private void m(ItemStack var1, int var2) {
      if (var2 > 0 && !var1.isEmpty()) {
         Inventory var3 = Minecraft.getInstance().player.getInventory();
         int var4 = -1;

         for (int var5 = 0; var5 < 36; var5++) {
            if (!this.t(var5)) {
               ItemStack var6 = var3.getItem(var5);
               dr.d var7 = this.V.get(var5);
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
            var4 = this.G();
         }

         if (var4 >= 0) {
            int var8 = var3.getItem(var4).isEmpty() && !this.V.containsKey(var4) ? u0.D() : -1;
            this.T(var4, var1, 0, var2);
            if (var8 >= 0) {
               u0.w(var8, var4);
            }
         }
      }
   }

   private void T(int var1, ItemStack var2, int var3, int var4) {
      if (var4 > 0) {
         dr.d var5 = this.V.get(var1);
         int var6 = Minecraft.getInstance().player.getInventory().getItem(var1).getCount();
         int var7 = var5 != null && ItemStack.isSameItemSameComponents(var5.stack, var2) ? Math.max(var5.expected + var3, var6) : var6;
         this.V.put(var1, new dr.d(var2.copyWithCount(1), var7 + var4, Minecraft.getInstance().player.tickCount + 100));
         u0.I();
      }
   }

   public boolean M(int var1) {
      return this.V.containsKey(var1);
   }

   public ItemStack j(int var1, ItemStack var2) {
      dr.d var3 = this.V.get(var1);
      return var3 != null && var2.isEmpty() ? h2.H(var3.stack.copyWithCount(var3.expected)) : var2;
   }

   private dr.c i(long var1, int var3) {
      Deque var4 = this.Y.get(var1);
      if (var4 != null && !var4.isEmpty() && var3 > 0) {
         int var5 = var3;
         int var6 = -1;
         ItemStack var7 = ItemStack.EMPTY;

         while (var5 > 0 && !var4.isEmpty()) {
            dr.c var8 = (dr.c)var4.removeLast();
            if (var8 != null && var8.stack != null && !var8.stack.isEmpty()) {
               if (var6 < 0) {
                  var6 = var8.preferredSlot;
               }

               if (var7.isEmpty()) {
                  var7 = var8.stack.copy();
               }

               int var9 = Math.min(var5, var8.stack.getCount());
               ItemStack var10 = this.n.get(var8.preferredSlot);
               if (var10 != null && ItemStack.isSameItemSameComponents(var10, var8.stack)) {
                  if (var10.getCount() <= var9) {
                     this.n.remove(var8.preferredSlot);
                  } else {
                     this.n.put(var8.preferredSlot, var10.copyWithCount(var10.getCount() - var9));
                  }
               }

               var5 -= var9;
               int var11 = var8.stack.getCount() - var9;
               if (var11 > 0) {
                  var4.addLast(new dr.c(var8.preferredSlot, var8.stack.copyWithCount(var11)));
               }
            }
         }

         if (var4.isEmpty()) {
            this.Y.remove(var1);
         }

         return var7.isEmpty() ? null : new dr.c(var6, var7.copyWithCount(var3));
      } else {
         return null;
      }
   }

   private boolean b(long var1, dr.c var3, boolean var4) {
      if (var3 != null && !var3.stack.isEmpty()) {
         Minecraft var5 = Minecraft.getInstance();
         if (var5.player == null) {
            return false;
         }

         int var6 = var3.preferredSlot;
         if (var6 >= 0 && var6 < var5.player.getInventory().getContainerSize() && !this.n.containsKey(var6)) {
            ItemStack var7 = var5.player.getInventory().getItem(var6);
            int var8 = var3.stack.getCount();
            int var9 = var4 && !var5.player.hasInfiniteMaterials() ? var8 - 1 : var8;
            if (var9 <= 0) {
               return true;
            } else if (var7.is(var3.stack.getItem()) && var7.getCount() >= var9) {
               this.n.put(var6, var7.copyWithCount(var9));
               this.Y.computeIfAbsent(var1, var0 -> new ArrayDeque<>()).addFirst(new dr.c(var6, var7.copyWithCount(var9)));
               u0.V();
               mt.U(
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

   private void M(BlockPos var1, Direction var2) {
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

   private void Y(Minecraft var1, BlockPos var2, Direction var3, double var4) {
      if (var1 != null && var1.player != null && var1.levelRenderer != null && var2 != null) {
         int var6 = Mth.clamp((int)(var4 * 10.0), 0, 9);
         var1.level.destroyBlockProgress(var1.player.getId(), var2, var6);
         if (var4 <= 0.0) {
            return;
         }

         long var7 = var2.asLong();
         long var9 = var1.player.tickCount;
         long var11 = this.F.getOrDefault(var7, 0L);
         if (var9 - var11 >= 4L) {
            this.F.put(var7, var9);
            this.M(var2, var3);
            this.M(var2, false);
         }
      }
   }

   private void t(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var2.levelRenderer != null && var1 != null) {
         var2.level.destroyBlockProgress(var2.player.getId(), var1, -1);
      }
   }

   private void p(Level var1, BlockPos var2) {
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

   private void B(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 != null && !var1.isBlank()) {
         ws.c(T(var1, 16711680), false);
         ws.c(T(var1, 16711680), true);
      }
   }

   private void k(String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         mt.U("FakeSpawner", var1, var2);
      }
   }

   private void z(String var1, long var2, String var4, Object... var5) {
      if (Boolean.getBoolean("larp.debug")) {
         long var6 = System.currentTimeMillis();
         if (var6 >= this.D) {
            this.D = var6 + var2;
            this.k(var4, var5);
         }
      }
   }

   private static ItemStack h(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1);
      var0.set(DataComponents.RARITY, Rarity.COMMON);
      if (var2 != null && !var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component H(String var0, ChatFormatting var1) {
      return f(h(var0), var1);
   }

   private static MutableComponent f(String var0, ChatFormatting var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.applyFormat(var1).withItalic(false));
   }

   private static Component t(String var0, int var1) {
      return T(h(var0), var1);
   }

   private static MutableComponent T(String var0, int var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)).withItalic(false));
   }

   private static String h(String var0) {
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

   private static String E(double var0, int var2) {
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
                  Integer var6 = V(var0.substring(var4 + 2, var4 + 8));
                  if (var6 != null) {
                     X(var1, var3, var2);
                     var2 = var2.withColor(TextColor.fromRgb(var6)).withItalic(false);
                     var4 += 7;
                     continue;
                  }
               }

               ChatFormatting var7 = ChatFormatting.getByCode(var0.charAt(var4 + 1));
               if (var7 != null) {
                  X(var1, var3, var2);
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

         X(var1, var3, var2);
         return var1;
      } else {
         return Component.empty();
      }
   }

   private static void X(MutableComponent var0, StringBuilder var1, Style var2) {
      if (var1.length() != 0) {
         var0.append(Component.literal(var1.toString()).setStyle(var2));
         var1.setLength(0);
      }
   }

   private static Integer V(String var0) {
      try {
         return Integer.parseInt(var0, 16);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private String O(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0;
      if (var5 >= 1.0E9) {
         return E(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return E(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? E(var5 / 1000.0, 1) + "K" : E(var5, 0);
      }
   }

   private String u(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0 + 75.0;
      if (var5 >= 1.0E9) {
         return E(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return E(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? E(var5 / 1000.0, 1) + "K" : E(var5, 0);
      }
   }

   private boolean c(ItemStack var1) {
      return var1 != null && !var1.isEmpty() ? var1.getItem() instanceof BlockItem var2 && var2.getBlock() instanceof ShulkerBoxBlock : false;
   }

   private long T(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         long var2 = 0L;
         ItemContainerContents var4 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
         if (var4 != null) {
            for (ItemStack var6 : var4.nonEmptyItemCopyStream().toList()) {
               if (var6 != null && !var6.isEmpty() && this.W(var6)) {
                  var2 += var6.getCount();
               }
            }
         }

         return var2;
      } else {
         return 0L;
      }
   }

   private int T(CompoundTag var1) {
      if (var1 == null) {
         return 0;
      } else if (var1.contains("count")) {
         return Math.max(0, var1.getIntOr("count", 0));
      } else {
         return var1.contains("Count") ? Math.max(0, var1.getIntOr("Count", 0)) : 0;
      }
   }

   private boolean y(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2 != null && var2.getWindow() != null ? mo.N(var2.getWindow(), var1) : false;
   }

   private record c(int preferredSlot, ItemStack stack) {
   }

   private record d(ItemStack stack, int expected, int expires) {
   }

   private record g(int stackCount, dr.c hiddenStack, long createdAtMs) {
   }

   private record l(int slot, String item, int amount, int reserved) {
   }

   public static final class m {
      public final int R;
      public final int u;
      public final double h;
      public final double e;

      public m(int var1, int var2, double var3, double var5) {
         this.R = Math.max(0, var1);
         this.u = Math.max(0, var2);
         this.h = Math.max(0.0, var3);
         this.e = Math.max(0.0, var5);
      }
   }

   private record n(BlockPos pos, long untilMs) {
   }

   public record p(SoundEvent event, float volume, float pitch) {
   }

   private record v(long pos, cl kind, int count, long placedAt, List<dr.l> backing, Map<String, Integer> purchased) {
      JsonObject toJson() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("pos", this.pos);
         var1.addProperty("kind", this.kind.name());
         var1.addProperty("count", this.count);
         var1.addProperty("placedAt", this.placedAt);
         JsonArray var2 = new JsonArray();

         for (dr.l var4 : this.backing) {
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
}
