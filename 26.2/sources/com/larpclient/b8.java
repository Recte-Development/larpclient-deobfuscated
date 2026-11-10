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

public final class b8 {
   private static final b8 v3 = new b8();
   private static final int F = 45;
   private static final String e = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
   private static final long p = 1800L;
   private static final long i = 650L;
   private static final int H = 64648;
   private static final int b = 16711680;
   private static final double E = 75.0;
   private final Map<Long, Long> s = new HashMap<>();
   private final Map<Long, Map<Item, Integer>> S = new HashMap<>();
   private final Map<Long, Integer> v = new ConcurrentHashMap<>();
   private final Set<Long> Y = ConcurrentHashMap.newKeySet();
   private final Map<Long, b8.f> V = new HashMap<>();
   private final Map<Long, Deque<b8.v>> q = new HashMap<>();
   private final Map<Integer, ItemStack> w = new HashMap<>();
   private final Map<Long, b8.b> J = new HashMap<>();
   private volatile Map<Long, SpawnerBlockEntity> m = new ConcurrentHashMap<>();
   private final Long2ObjectMap<List<SpawnerBlockEntity>> k = new Long2ObjectOpenHashMap();
   private final Map<Long, Long> c = new HashMap<>();
   private final Map<Long, Float> A = new HashMap<>();
   private final Map<Long, Integer> n = new HashMap<>();
   private int a;
   private final Set<Integer> D = new HashSet<>();
   private final Map<Integer, b8.r> K = new HashMap<>();
   private BlockPos j;
   private int I = -1;
   private final Map<Long, Integer> P = new HashMap<>();
   private final Map<Long, Double> vm = new HashMap<>();
   private final Map<Long, Integer> t = new ConcurrentHashMap<>();
   private final Set<Long> W = new HashSet<>();
   private final Map<Item, Double> h = new HashMap<>();
   private final Set<Long> u = ConcurrentHashMap.newKeySet();
   private volatile Level U;
   private LocalPlayer Z;
   private Item vJ;
   private String G;
   private volatile Map<Item, ll> y = Map.of();
   private volatile LongSet x = LongSets.EMPTY_SET;
   private volatile LongSet r = LongSets.EMPTY_SET;
   private final Map<Long, Long> vR = new ConcurrentHashMap<>();
   private final Map<Long, ll> M = new ConcurrentHashMap<>();
   private final Map<Long, b8.g> z = new HashMap<>();
   private final List<JsonElement> C = new ArrayList<>();
   private String vQ;
   private boolean L = (boolean)1;
   private long O;
   private long R;
   private long T;
   private long vP;
   private long f;
   private long g;
   private final Set<ItemStack> l = Collections.newSetFromMap(new IdentityHashMap<>());
   private final Set<ItemStack> N = Collections.newSetFromMap(new WeakHashMap<>());
   private InteractionHand o;
   private int B = -1;
   private int X = -1;
   private final Set<Long> d = ConcurrentHashMap.newKeySet();
   private long Q;

   private static Path W() {
      return FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-piles.json");
   }

   void f(Container var1) {
      this.l.clear();
      this.N.clear();

      for (int var2 = 0; var2 < var1.getContainerSize(); var2++) {
         ItemStack var3 = var1.getItem(var2);
         if (!var3.isEmpty()) {
            this.l.add(var3);
         }
      }
   }

   public ItemStack c(ItemStack var1, ItemStack var2) {
      if (!var2.isEmpty() && (this.l.contains(var1) || this.N.contains(var1))) {
         this.N.add(var2);
         if (Boolean.getBoolean("larp.debug")) {
            this.f++;
         }
      }

      return var2;
   }

   public void P() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null) {
         this.f(var1.player.getInventory());
         if (!var1.player.containerMenu.getCarried().isEmpty()) {
            this.l.add(var1.player.containerMenu.getCarried());
         }

         ki.n();
      }
   }

   public void J() {
      this.G();
      if (Boolean.getBoolean("larp.debug")) {
         this.R++;
      }
   }

   public void H(int var1, int var2) {
      if (Boolean.getBoolean("larp.debug")) {
         this.T += var1;
         this.vP += var2;
      }
   }

   public boolean a(ItemStack var1) {
      if (!this.J()) {
         return false;
      }

      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 != null && (var1 == var2.getMainHandItem() && this.t(var2.getInventory().getSelectedSlot()) || var1 == var2.getOffhandItem() && this.t(40));
   }

   public void m() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner reset must run on the client thread");
      }

      this.A();
      m3.J("controller cleanup");
      ki.X();
      this.l.clear();
      this.N.clear();
      this.m = new ConcurrentHashMap<>();
      this.k.clear();
      this.x = LongSets.EMPTY_SET;
      this.r = LongSets.EMPTY_SET;
      this.U = null;
      this.I = -1;
      this.o = null;
      this.X = -1;
      this.n.clear();
      this.a = 0;
      this.d.clear();
      this.D.clear();
      this.K.clear();
      this.P.clear();
      this.vm.clear();
      this.t.clear();
      this.W.clear();
      this.h.clear();
      this.u.clear();

      for (Long var3 : new ArrayList<>(this.A.keySet())) {
         this.z(BlockPos.of(var3));
      }

      int var6 = this.v.isEmpty() && this.Y.isEmpty() ? 0 : 1;
      this.v.clear();
      this.M.clear();
      this.z.clear();
      this.C.clear();
      this.vQ = null;
      this.Y.clear();
      this.s.clear();
      this.S.clear();
      this.V.clear();
      this.q.clear();
      this.w.clear();

      for (long var4 : this.vR.keySet()) {
         this.r(BlockPos.of(var4));
      }

      this.vR.clear();
      this.r = LongSets.EMPTY_SET;
      this.J.clear();
      this.A.clear();
      this.c.clear();
      this.j = null;
      this.Z = null;
      this.vJ = null;
      if (var1 != null) {
         if (var1.gui.screen() instanceof mn || var1.gui.screen() instanceof t3) {
            var1.gui.setScreen(null);
         }

         if (var6 != 0 && var1.level != null) {
            var1.levelExtractor.allChanged();
         }
      }
   }

   private void c() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner state must be updated on the client thread");
      }

      ClientLevel var2 = var1 == null ? null : var1.level;
      LocalPlayer var3 = var1 == null ? null : var1.player;
      Item var4 = this.G();
      if (var2 != this.U || var3 != this.Z || !this.O().equals(this.G) || !this.J()) {
         this.m();
         this.U = var2;
         this.Z = var3;
         this.vJ = var4;
         this.G = this.O();
         HashMap var5 = new HashMap();
         FakeSpawnerModule var6 = FakeSpawnerModule.M();
         if (var6 != null) {
            for (ll var10 : ll.values()) {
               String var11 = var6.Y(var10.ordinal());
               if (var11 != null && !var11.isBlank()) {
                  var5.put(C(var11), var10);
               }
            }
         }

         this.y = Map.copyOf(var5);
         if (var2 != null && var3 != null && this.J()) {
            this.vQ = this.A(var1);
            this.s();
            this.L = (boolean)1;
         }
      }
   }

   private String A(Minecraft var1) {
      String var2 = var1.getCurrentServer() != null
         ? var1.getCurrentServer().ip
         : (
            var1.getSingleplayerServer() != null
               ? "singleplayer:" + var1.getSingleplayerServer().getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize()
               : null
         );
      return var2 == null ? null : var2 + "|" + var1.level.dimension().identifier() + "|" + var1.player.getUUID();
   }

   private void s() {
      if (this.vQ != null) {
         this.p(W());
      }
   }

   private void p(Path var1) {
      if (this.vQ != null && Files.exists(var1)) {
         try {
            JsonObject var2 = l7.k(var1);
            if (var2.has("quarantined") && var2.get("quarantined").isJsonObject() && var2.getAsJsonObject("quarantined").has(this.vQ)) {
               for (JsonElement var4 : var2.getAsJsonObject("quarantined").getAsJsonArray(this.vQ)) {
                  this.C.add(var4.deepCopy());
               }
            }

            if (!var2.has(this.vQ)) {
               return;
            }

            int var20 = 0;

            for (JsonElement var5 : var2.getAsJsonArray(this.vQ)) {
               try {
                  JsonObject var6 = var5.getAsJsonObject();
                  long var7 = var6.get("pos").getAsLong();
                  ll var9 = ll.valueOf(var6.get("kind").getAsString());
                  int var10 = var6.get("count").getAsInt();
                  long var11 = var6.get("placedAt").getAsLong();
                  ArrayList var13 = new ArrayList();

                  for (JsonElement var15 : var6.getAsJsonArray("backing")) {
                     JsonObject var16 = var15.getAsJsonObject();
                     var13.add(
                        new b8.l(
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

                  if (var10 < 1 || var10 > 100000 || var10 > 1 && var13.isEmpty() || this.z.containsKey(var7)) {
                     throw new IllegalArgumentException("Invalid or conflicting saved pile");
                  }

                  this.z.put(var7, new b8.g(var7, var9, var10, var11, var13, var22));
               } catch (RuntimeException var18) {
                  var20++;
                  this.C.add(var5.deepCopy());
               }
            }

            if (var20 > 0) {
               com.larpclient.h.g("FakeSpawner", "Retaining {} unmatched pile records for recovery; no inventory was restored", var20);
            }
         } catch (Exception var19) {
            com.larpclient.h.m("FakeSpawner", "Pile load failed; retaining the file for recovery", var19);
         }
      }
   }

   private void A() {
      if (this.vQ != null) {
         this.Y(W());
      }
   }

   private void Y(Path var1) {
      if (this.vQ != null) {
         try {
            JsonObject var2 = Files.exists(var1) ? l7.k(var1) : new JsonObject();
            JsonArray var3 = new JsonArray();
            if (var2.has(this.vQ) && !var2.get(this.vQ).isJsonArray()) {
               throw new IOException("Unrecognized saved pile data; original file retained");
            }

            if (var2.has("quarantined") && !var2.get("quarantined").isJsonObject()) {
               throw new IOException("Unrecognized pile quarantine data; original file retained");
            }

            JsonObject var4 = var2.has("quarantined") ? var2.getAsJsonObject("quarantined") : new JsonObject();
            if (var4.has(this.vQ) && !var4.get(this.vQ).isJsonArray()) {
               throw new IOException("Unrecognized retained pile records; original file retained");
            }

            JsonArray var5 = new JsonArray();

            for (JsonElement var7 : this.C) {
               var5.add(var7.deepCopy());
            }

            if (!var5.isEmpty()) {
               var4.add(this.vQ, var5);
               var2.add("quarantined", var4);
            }

            for (b8.g var18 : this.z.values()) {
               var3.add(var18.toJson());
            }

            for (Entry var19 : this.v.entrySet()) {
               long var8 = (Long)var19.getKey();
               if (!this.z.containsKey(var8) && (Integer)var19.getValue() >= 1) {
                  Deque var10 = this.q.get(var8);
                  if (var10 == null) {
                     var10 = new ArrayDeque();
                  }

                  if ((Integer)var19.getValue() <= 1 || !var10.isEmpty()) {
                     ArrayList var11 = new ArrayList();

                     for (b8.v var13 : var10) {
                        ItemStack var14 = this.w.get(var13.preferredSlot);
                        if (var14 == null || var14.isEmpty()) {
                           var11.clear();
                           break;
                        }

                        var11.add(
                           new b8.l(
                              var13.preferredSlot, BuiltInRegistries.ITEM.getKey(var13.stack.getItem()).toString(), var13.stack.getCount(), var14.getCount()
                           )
                        );
                     }

                     if (!var11.isEmpty() || (Integer)var19.getValue() == 1) {
                        HashMap var20 = new HashMap();
                        this.S.getOrDefault(var8, Map.of()).forEach((var1x, var2x) -> var20.put(BuiltInRegistries.ITEM.getKey(var1x).toString(), var2x));
                        var3.add(
                           new b8.g(
                                 var8,
                                 this.P(BlockPos.of(var8)),
                                 (Integer)var19.getValue(),
                                 this.s.getOrDefault(var8, System.currentTimeMillis()),
                                 var11,
                                 var20
                              )
                              .toJson()
                        );
                     }
                  }
               }
            }

            var2.add(this.vQ, var3);
            Files.createDirectories(var1.getParent());
            l7.O(var1, var2.toString());
         } catch (Exception var15) {
            com.larpclient.h.m("FakeSpawner", "Pile save failed; previous file retained", var15);
         }
      }
   }

   public void G() {
      this.L = (boolean)1;
   }

   private void N(Minecraft var1) {
      if (this.L && !this.z.isEmpty()) {
         this.L = (boolean)0;
         byte var2 = 0;

         for (b8.g var4 : new ArrayList<>(this.z.values())) {
            BlockPos var5 = BlockPos.of(var4.pos);
            if (var1.level.hasChunkAt(var5)) {
               LevelChunk var6 = var1.level.getChunkAt(var5);
               int var7 = var6.getSectionIndex(var5.getY());
               BlockState var8 = var7 >= 0 && var7 < var6.getSections().length
                  ? var6.getSection(var7).getBlockState(var5.getX() & 15, var5.getY() & 15, var5.getZ() & 15)
                  : Blocks.AIR.defaultBlockState();
               if (this.K(var8.getBlock().asItem()) == var4.kind) {
                  HashMap var9 = new HashMap();

                  for (b8.l var11 : var4.backing) {
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
                        ki.I();

                        for (b8.l var13 : var4.backing) {
                           ItemStack var14 = (ItemStack)var9.get(var13.slot);
                           this.w.put(var13.slot, var14);
                           var16.addLast(new b8.v(var13.slot, var14.copyWithCount(Math.min(var13.amount, var13.reserved))));
                        }

                        this.q.put(var4.pos, var16);
                        this.v.put(var4.pos, var4.count);
                        this.M.put(var4.pos, var4.kind);
                        this.s.put(var4.pos, var4.placedAt);
                        HashMap var18 = new HashMap();
                        var4.purchased.forEach((var1x, var2x) -> {
                           Identifier var3 = Identifier.tryParse(var1x);
                           if (var3 != null && BuiltInRegistries.ITEM.getValue(var3) != Items.AIR) {
                              var18.put((Item)BuiltInRegistries.ITEM.getValue(var3), var2x);
                           }
                        });
                        if (!var18.isEmpty()) {
                           this.S.put(var4.pos, var18);
                        }

                        this.z.remove(var4.pos);
                        this.i(var4.pos);
                        this.n(var1.level, var5);
                        this.r(var5);
                        ki.I();
                        var2 = 1;
                     }
                  }
               }
            }
         }

         if (var2 != 0) {
            this.A();
         }
      }
   }

   private b8() {
   }

   public static b8 k() {
      return v3;
   }

   public boolean J() {
      FakeSpawnerModule var1 = FakeSpawnerModule.M();
      return var1 != null && var1.isEnabled() && var1.isRuntimeActive();
   }

   public boolean r(ItemStack var1) {
      return this.J() && this.m(var1);
   }

   public boolean m(ItemStack var1) {
      return this.Q(var1) && (this.l.contains(var1) || this.N.contains(var1) || gz.w(var1));
   }

   private boolean Q(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && !px.T(var1) && this.K(var1.getItem()) != null;
   }

   private String O() {
      FakeSpawnerModule var1 = FakeSpawnerModule.M();
      return var1.Y(0) + "|" + var1.Y(1) + "|" + var1.Y(2);
   }

   public ll K(Item var1) {
      FakeSpawnerModule var2 = FakeSpawnerModule.M();

      for (ll var6 : ll.values()) {
         String var7 = var2.Y(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1 == C(var7)) {
            return var6;
         }
      }

      return null;
   }

   public ll P(BlockPos var1) {
      return var1 == null ? ll.o : this.M.getOrDefault(var1.asLong(), ll.o);
   }

   private Item L(long var1) {
      return C(FakeSpawnerModule.M().Y(this.P(BlockPos.of(var1)).ordinal()));
   }

   public Item G() {
      FakeSpawnerModule var1 = FakeSpawnerModule.M();
      return C(var1 == null ? null : var1.F());
   }

   public static Item C(String var0) {
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

   private Block k() {
      return this.G() instanceof BlockItem var1 ? var1.getBlock() : Blocks.COBBLESTONE;
   }

   public boolean B(BlockState var1) {
      return this.J() && var1 != null ? this.K(var1.getBlock().asItem()) != null || var1.is(Blocks.SPAWNER) : false;
   }

   public void S(BlockPos var1) {
      this.c();
      this.u.add(var1.asLong());
      this.v.remove(var1.asLong());
      this.i(var1.asLong());
      this.r(var1);
   }

   public BlockState n(BlockPos var1, BlockState var2) {
      return var1 == null ? var2 : this.n(var1.asLong(), var2);
   }

   public BlockState n(long var1, BlockState var3) {
      if (!this.x.contains(var1) || !this.M() || var3 == null || this.d.contains(var1)) {
         return var3;
      }

      if (this.t.containsKey(var1)) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.u.contains(var1)) {
         return var3;
      }

      if (!this.Y.contains(var1) || !var3.is(Blocks.SPAWNER) && !this.y.containsKey(var3.getBlock().asItem())) {
         int var4 = this.v.getOrDefault(var1, 0);
         if (var4 <= 0) {
            return var3;
         } else {
            return !var3.isAir() && !var3.is(Blocks.SPAWNER) && !this.y.containsKey(var3.getBlock().asItem()) ? var3 : Blocks.SPAWNER.defaultBlockState();
         }
      } else {
         return Blocks.AIR.defaultBlockState();
      }
   }

   public boolean M() {
      return (!this.v.isEmpty() || !this.Y.isEmpty() || !this.t.isEmpty()) && this.J() && this.U == Minecraft.getInstance().level;
   }

   private void u() {
      LongOpenHashSet var1 = new LongOpenHashSet();

      for (long var3 : this.v.keySet()) {
         var1.add(var3);
      }

      for (long var9 : this.Y) {
         var1.add(var9);
      }

      for (long var10 : this.t.keySet()) {
         var1.add(var10);
      }

      this.x = LongSets.unmodifiable(var1);
      LongOpenHashSet var8 = new LongOpenHashSet();

      for (long var4 : this.vR.keySet()) {
         var8.add(var4);
      }

      this.r = LongSets.unmodifiable(var8);
   }

   public void X(BlockPos var1, BlockState var2) {
      this.c();
      if (var1 != null) {
         long var3 = var1.asLong();
         if (this.w(var3)) {
            this.a = 0;
            this.r(var1);
            com.larpclient.h.U("FakeSpawner", "Final break {} at {}; server state={}", var2 != null && var2.isAir() ? "confirmed" : "corrected", var1, var2);
         }

         if (this.u.contains(var3)) {
            if (var2 != null && !var2.isAir()) {
               return;
            }

            bo.W(var1);
            this.u.remove(var3);
         }

         if (this.Y.contains(var3)) {
            if (var2 != null && this.B(var2)) {
               this.a = 0;
            }

            this.J.put(var3, new b8.b(var1.immutable(), System.currentTimeMillis() + 10000L));
            this.Y.remove(var3);
            this.r(var1);
         }

         int var5 = this.v.getOrDefault(var3, 0);
         if (var2 != null && !var2.isAir()) {
            if (this.J()) {
               if (this.B(var2)) {
                  if (var5 <= 0 && !this.V.containsKey(var3)) {
                     return;
                  }

                  ll var6 = this.K(var2.getBlock().asItem());
                  if (var6 != null && this.M.put(var3, var6) != var6) {
                     this.i(var3);
                  }

                  this.s.putIfAbsent(var3, System.currentTimeMillis());
                  b8.f var7 = this.V.remove(var3);
                  int var8 = var7 == null ? this.v.getOrDefault(var3, 1) : Math.max(1, var7.stackCount);
                  this.v.put(var3, Math.max(1, var8));
                  this.d("track:" + var3, 1500L, "trackWorldState fake pos={} state={} resolvedCount={} pending={}", var1, var2.getBlock(), var8, var7 != null);
                  if (var7 != null && var7.hiddenStack != null) {
                     this.E(var3, var7.hiddenStack, true);
                  }

                  if (var7 != null) {
                     this.A();
                  }

                  this.n(Minecraft.getInstance() == null ? null : Minecraft.getInstance().level, var1);
                  if (var5 <= 0) {
                     this.r(var1);
                  }
               } else {
                  this.X(var1, null);
               }
            }
         } else {
            if (var5 > 0) {
               this.u("trackWorldState removed pos={} previousCount={} state={}", var1, var5, var2);
            }

            if (var5 > 0 && !this.k(var1)) {
               this.e(var1);
            } else if (this.k(var1)) {
               this.Q(var3, Integer.MAX_VALUE);
               ki.n();
               this.u("Placement rejected at {}; reservations released without inventory replay", var1);
            }

            this.s.remove(var3);
            this.S.remove(var3);
            this.v.remove(var3);
            this.V.remove(var3);
            this.q.remove(var3);
            this.i(var3);
            if (var5 > 0) {
               this.r(var1);
            }

            this.A.remove(var3);
            this.c.remove(var3);
            this.vm.remove(var3);
            this.z(var1);
            if (var1.equals(this.j)) {
               this.j = null;
            }
         }
      }
   }

   public void f(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.c();
      if (this.J() && var1 != null && this.m(var2)) {
         b8.g var5 = this.z.remove(var1.asLong());
         if (var5 != null) {
            this.C.add(var5.toJson());
            this.u("New placement at {} archives a superseded saved pile for recovery", var1);
         }

         this.M.put(var1.asLong(), this.K(var2.getItem()));
         ItemStack var6 = var4 ? var2.copy() : ItemStack.EMPTY;
         int var7 = var4 ? var2.getCount() : 1;
         this.V.put(var1.asLong(), new b8.f(var7, new b8.v(var3, var6), System.currentTimeMillis()));
      }
   }

   public void d(BlockPos var1, BlockState var2) {
      this.c();
      b8.f var3 = this.V.get(var1.asLong());
      if (var3 != null && this.B(var2)) {
         this.Y.remove(var1.asLong());
         this.v.put(var1.asLong(), var3.stackCount);
         this.s.putIfAbsent(var1.asLong(), var3.createdAtMs);
         this.n(this.U, var1);
         this.r(var1);
      }
   }

   public void E() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.J() && var1.player != null) {
         for (Entry var3 : new ArrayList<>(this.V.entrySet())) {
            if (this.v.containsKey(var3.getKey())) {
               b8.f var4 = (b8.f)var3.getValue();
               if (var4.hiddenStack != null && !var4.hiddenStack.stack.isEmpty()) {
                  this.E((Long)var3.getKey(), var4.hiddenStack, true);
               }

               this.v.put((Long)var3.getKey(), var4.stackCount);
            }
         }
      }
   }

   public void g() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null && this.J() && var1.level == this.U && var1.player == this.Z && !m3.u()) {
         this.m(var1.player.getInventory());
         this.K
            .entrySet()
            .removeIf(
               var1x -> {
                  ItemStack var2 = var1.player.getInventory().getItem(var1x.getKey());
                  b8.r var3x = var1x.getValue();
                  return var1.player.tickCount >= var3x.expires
                     || !var2.isEmpty() && !var2.is(var3x.stack.getItem())
                     || var2.is(var3x.stack.getItem()) && var2.getCount() >= var3x.expected;
               }
            );

         for (Entry var3 : this.w.entrySet()) {
            if (!this.V.values().stream().anyMatch(var1x -> var1x.hiddenStack != null && var1x.hiddenStack.preferredSlot == (Integer)var3.getKey())) {
               ItemStack var4 = var1.player.getInventory().getItem((Integer)var3.getKey());
               if (var4.is(((ItemStack)var3.getValue()).getItem())
                  && var4.getCount() > ((ItemStack)var3.getValue()).getCount()
                  && this.Y((Integer)var3.getKey(), var4.getCount() - ((ItemStack)var3.getValue()).getCount())) {
                  break;
               }
            }
         }
      }
   }

   void m(Container var1) {
      Iterator var2 = this.w.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         int var4 = (Integer)var3.getKey();
         ItemStack var5 = var1.getItem(var4);
         ItemStack var6 = (ItemStack)var3.getValue();
         if (var5.is(var6.getItem()) && var5.getCount() >= var6.getCount()) {
            this.F(var4, var5);
         } else {
            int var7 = var5.is(var6.getItem()) ? var5.getCount() : 0;
            int var8 = var6.getCount() - var7;
            if (var7 == 0) {
               var2.remove();
            } else {
               var3.setValue(var5.copyWithCount(var7));
            }

            for (Entry var10 : this.q.entrySet()) {
               ArrayDeque var11 = new ArrayDeque();

               for (b8.v var13 : (Deque)var10.getValue()) {
                  int var14 = var13.preferredSlot == var4 ? Math.min(var8, var13.stack.getCount()) : 0;
                  var8 -= var14;
                  if (var14 > 0) {
                     this.v.computeIfPresent((Long)var10.getKey(), (var1x, var2x) -> Math.max(1, var2x - var14));
                  }

                  if (var13.stack.getCount() > var14) {
                     var11.addLast(new b8.v(var13.preferredSlot, var13.stack.copyWithCount(var13.stack.getCount() - var14)));
                  }
               }

               ((Deque)var10.getValue()).clear();
               ((Deque)var10.getValue()).addAll(var11);
            }

            com.larpclient.h.U(
               "FakeSpawner",
               "Reservation corrected physical={} item={} old={} available={}; real inventory retained",
               var4,
               BuiltInRegistries.ITEM.getKey(var6.getItem()),
               var6.getCount(),
               var7
            );
            ki.n();
         }
      }
   }

   private boolean Y(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = this.j();
      return var4 >= 0 && var3.player != null
         ? m3.j("separate reservation surplus", m3.h(var1, var4, var2, var3.player.getInventory().getItem(var1).getCount()), ki::n, ki::n)
         : false;
   }

   public boolean k(BlockPos var1) {
      return var1 != null && this.V.containsKey(var1.asLong());
   }

   public boolean C(BlockPos var1) {
      return this.J() && this.U == Minecraft.getInstance().level && var1 != null && this.Y.contains(var1.asLong());
   }

   public void D(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 != null) {
         this.o = var1;
         this.B = var2.getInventory().getSelectedSlot();
         this.X = var2.tickCount + 4;
      }
   }

   public boolean k(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return this.J()
         && var2 == this.Z
         && var1 == this.o
         && var2.tickCount <= this.X
         && var2.getInventory().getSelectedSlot() == this.B
         && this.m(var2.getItemInHand(var1));
   }

   public void j(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.J() && var2.level != null && var2.level.getBlockState(var1).isAir()) {
         if (this.vR.put(var1.asLong(), var2.level.getGameTime() + 10L) == null) {
            this.r(var1);
         }
      }
   }

   public BlockState T(BlockPos var1, BlockState var2) {
      return this.N(var1.asLong(), var2);
   }

   public BlockState N(long var1, BlockState var3) {
      return this.r.contains(var1) && var3.isAir() && this.J() && this.U == Minecraft.getInstance().level ? Blocks.SPAWNER.defaultBlockState() : var3;
   }

   public boolean H(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.c();
      if (this.J() && var1 != null && !this.k(var1) && this.T(var1) > 0 && this.m(var2) && this.K(var2.getItem()) == this.P(var1) && !this.t(var3) && !m3.u()) {
         long var5 = var1.asLong();
         int var7 = var4 ? var2.getCount() : 1;
         if (var7 < var2.getCount()) {
            int var8 = this.j();
            if (var8 < 0) {
               return false;
            }

            Item var9 = var2.getItem();
            return m3.j("single pile addition", m3.h(var3, var8, var7, var2.getCount()), () -> {
               Minecraft var6 = Minecraft.getInstance();
               ItemStack var7x = var6.player.getInventory().getItem(var8);
               if (this.v.containsKey(var5) && var7x.is(var9) && var7x.getCount() == var7) {
                  this.x(var5, var8, var7x, var7);
               }
            }, ki::n);
         } else {
            return this.x(var5, var3, var2, var7);
         }
      } else {
         return false;
      }
   }

   private boolean x(long var1, int var3, ItemStack var4, int var5) {
      if (!this.E(var1, new b8.v(var3, var4.copy()), false)) {
         return false;
      }

      int var6 = (int)Math.min(2147483647L, (long)this.v.getOrDefault(var1, 1).intValue() + var5);
      this.v.put(var1, var6);
      this.s.putIfAbsent(var1, System.currentTimeMillis());
      this.r(Minecraft.getInstance().level, BlockPos.of(var1));
      this.M("You just added another spawner! There are now " + var6 + " spawners in the stack.");
      this.A();
      return true;
   }

   public boolean o() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null && var1.getMainHandItem().is(ItemTags.PICKAXES)) {
         ItemEnchantments var2 = (ItemEnchantments)var1.getMainHandItem().get(DataComponents.ENCHANTMENTS);
         Reference var3 = var1.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
         return var2 != null && var2.getLevel(var3) > 0;
      } else {
         return false;
      }
   }

   public boolean P(BlockPos var1, Direction var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.a(var1)) {
         return false;
      }

      long var4 = var1.asLong();
      if (this.t.containsKey(var4)) {
         return true;
      }

      if (this.o()) {
         this.P.remove(var4);
         this.vm.remove(var4);
         this.W.remove(var4);
         return false;
      }

      if (this.W.contains(var4)) {
         return true;
      }

      int var6 = var3.player.tickCount;
      long var7 = var1.asLong();
      if (this.n.getOrDefault(var7, -1) == var6) {
         return true;
      }

      this.n.put(var7, var6);
      this.P.computeIfAbsent(var4, var1x -> var6);
      double var9 = var3.player.getAbilities().instabuild ? 1.0 : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
      double var11 = Math.min(1.0, this.vm.getOrDefault(var4, 0.0) + var9);
      this.vm.put(var4, var11);
      this.f(var3, var1, var2, var11);
      if (var11 >= 1.0) {
         this.P.remove(var4);
         this.vm.remove(var4);
         this.W.add(var4);
         this.t.put(var4, var6 + 4);
         this.z(var1);
         this.n(var1, var2);
         this.Q(var1, true);
         this.r(var1);
      }

      return true;
   }

   private void U() {
      Minecraft var1 = Minecraft.getInstance();

      for (Entry var3 : new ArrayList<>(this.t.entrySet())) {
         if (var1.player.tickCount >= (Integer)var3.getValue()) {
            this.t.remove(var3.getKey());
            this.r(BlockPos.of((Long)var3.getKey()));
            this.M("You cannot break spawners without a silk touch!");
         }
      }

      if (!var1.options.keyAttack.isDown()) {
         for (long var6 : this.P.keySet()) {
            this.z(BlockPos.of(var6));
         }

         this.P.clear();
         this.vm.clear();
         this.W.clear();
      } else {
         this.P.keySet().removeIf(var2 -> {
            int var3x = var1.hitResult instanceof BlockHitResult var4 && var4.getBlockPos().asLong() == var2 ? 0 : 1;
            if (var3x != 0) {
               this.z(BlockPos.of(var2));
            }

            return (boolean)var3x;
         });
      }
   }

   public boolean q(BlockPos var1) {
      if (this.P(var1, null)) {
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      if (this.J() && var2 != null && var2.level != null && var2.player != null && var1 != null) {
         if (this.B(var2.level.getBlockState(var1)) && this.T(var1) > this.A()) {
            long var3 = var1.asLong();
            this.A.putIfAbsent(var3, 0.0F);
            this.f(var2, var1, null, 0.0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean l(BlockPos var1, Direction var2) {
      if (this.P(var1, var2)) {
         return true;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.J() && var3 != null && var3.level != null && var3.player != null && var1 != null) {
         if (this.a(var1) && var3.player.tickCount < this.a) {
            return true;
         }

         if (this.B(var3.level.getBlockState(var1)) && this.T(var1) > this.A()) {
            long var4 = var1.asLong();
            int var6 = var3.player.tickCount;
            if (this.n.getOrDefault(var4, -1) == var6) {
               return true;
            }

            this.n.put(var4, var6);
            float var7 = var3.player.getAbilities().instabuild ? 1.0F : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
            float var8 = Math.min(1.0F, this.A.getOrDefault(var4, 0.0F) + var7);
            this.A.put(var4, var8);
            this.f(var3, var1, var2, var8);
            if (var8 >= 1.0F) {
               if (this.A(var1, this.A())) {
                  this.a = var6 + 5;
                  this.Q(var1, true);
               }

               this.A.remove(var4);
               this.c.remove(var4);
               this.z(var1);
            }

            return true;
         } else {
            if (var1 != null) {
               this.A.remove(var1.asLong());
               this.c.remove(var1.asLong());
               this.z(var1);
            }

            return false;
         }
      } else {
         if (var1 != null) {
            this.A.remove(var1.asLong());
            this.c.remove(var1.asLong());
            this.z(var1);
         }

         return false;
      }
   }

   public void e(BlockPos var1) {
      if (var1 != null && this.u.contains(var1.asLong())) {
         bo.W(var1);
      }

      if (var1 != null && this.Y.contains(var1.asLong())) {
         this.J.put(var1.asLong(), new b8.b(var1.immutable(), System.currentTimeMillis() + 10000L));
      }

      if (var1 != null && this.T(var1) > 0) {
         this.J.put(var1.asLong(), new b8.b(var1.immutable(), System.currentTimeMillis() + 10000L));
         int var2 = this.v.getOrDefault(var1.asLong(), 0);
         b8.v var3 = this.Q(var1.asLong(), var2);
         this.y(var1.asLong(), var2);
         this.o(new ItemStack(this.L(var1.asLong()), 1), 1);
         ki.n();
         this.A.remove(var1.asLong());
         this.c.remove(var1.asLong());
         this.z(var1);
      }
   }

   private int A() {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player == null || !var1.player.isShiftKeyDown() && !var1.options.keyShift.isDown() ? 1 : 64;
   }

   public boolean S(BlockPos var1) {
      return this.A(var1, 64);
   }

   private boolean A(BlockPos var1, int var2) {
      if (this.k(var1)) {
         return false;
      }

      if (this.a(var1)) {
         long var3 = var1.asLong();
         int var5 = this.v.getOrDefault(var3, 0);
         if (var5 <= 0) {
            return false;
         }

         int var6 = Math.min(var2, var5);
         Deque var7 = this.q.get(var3);
         if (var7 != null && !var7.isEmpty()) {
            ItemStack var8 = Minecraft.getInstance().player.getInventory().getItem(((b8.v)var7.getLast()).preferredSlot);
            int var9 = 0;

            for (b8.v var11 : var7.reversed()) {
               if (!ItemStack.isSameItemSameComponents(var8, Minecraft.getInstance().player.getInventory().getItem(var11.preferredSlot))) {
                  break;
               }

               var9 += var11.stack.getCount();
            }

            if (var9 < var6 && var9 < var5 - 1) {
               var6 = var9;
            }
         }

         if (!this.F(var3, var6)) {
            return false;
         }

         this.y(var3, var6);
         return true;
      } else {
         return false;
      }
   }

   private void y(long var1, int var3) {
      int var4 = Math.max(0, this.v.getOrDefault(var1, 0) - var3);
      BlockPos var5 = BlockPos.of(var1);
      if (var4 > 0) {
         this.v.put(var1, var4);
         this.s.put(var1, System.currentTimeMillis());
      } else {
         this.Y.add(var1);
         this.v.remove(var1);
         this.V.remove(var1);
         this.s.remove(var1);
         this.S.remove(var1);
         this.q.remove(var1);
         this.i(var1);
         this.r(var5);
         if (var5.equals(this.j)) {
            this.j = null;
         }
      }

      this.J.put(var1, new b8.b(var5.immutable(), System.currentTimeMillis() + 10000L));
      this.A();
   }

   public boolean a(BlockPos var1) {
      if (var1 == null) {
         return false;
      }

      long var2 = var1.asLong();
      return this.v.getOrDefault(var2, 0) > 0 && !this.d.contains(var2) && this.U == Minecraft.getInstance().level && this.J();
   }

   public b8.o o(BlockPos var1, SoundEvent var2, float var3, float var4) {
      if (var1 != null && var2 != null && this.a(var1)) {
         BlockState var5 = ((BlockItem)this.L(var1.asLong())).getBlock().defaultBlockState();
         if (var5 == null) {
            return null;
         } else {
            SoundType var6 = var5.getSoundType();
            SoundType var7 = Blocks.SPAWNER.defaultBlockState().getSoundType();
            if (x(var2, var6.getPlaceSound())) {
               return new b8.o(var7.getPlaceSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else if (x(var2, var6.getBreakSound())) {
               return new b8.o(var7.getBreakSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else {
               return x(var2, var6.getHitSound()) ? new b8.o(var7.getHitSound(), (var7.getVolume() + 1.0F) / 8.0F, var7.getPitch() * 0.5F) : null;
            }
         }
      } else {
         return null;
      }
   }

   private static boolean x(SoundEvent var0, SoundEvent var1) {
      return var0 == var1 || var0.location().equals(var1.location());
   }

   private void r(Level var1, BlockPos var2) {
      if (var1 != null && var2 != null && this.a(var2)) {
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

   private void Q(BlockPos var1, boolean var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null && this.a(var1)) {
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

   public BlockState w(BlockPos var1, BlockState var2) {
      return this.n(var1, var2);
   }

   public SpawnerBlockEntity n(Level var1, BlockPos var2) {
      Map var3 = this.m;
      if (var1 != null && var1 == this.U && this.a(var2) && !this.t.containsKey(var2.asLong())) {
         long var4 = var2.asLong();
         SpawnerBlockEntity var6 = (SpawnerBlockEntity)var3.get(var4);
         if (var6 == null && Minecraft.getInstance().isSameThread()) {
            var6 = new SpawnerBlockEntity(var2.immutable(), Blocks.SPAWNER.defaultBlockState());
            var6.setEntityId(this.P(var2).v, var1.getRandom());
            var6.setLevel(var1);
            var6.getSpawner().getOrCreateDisplayEntity(var1, var2);
            var3.put(var4, var6);
            ((List)this.k.computeIfAbsent(ChunkPos.pack(var2.getX() >> 4, var2.getZ() >> 4), var0 -> new ArrayList())).add(var6);
         }

         return var3 == this.m && var1 == this.U && this.a(var2) ? var6 : null;
      } else {
         return null;
      }
   }

   private void r(BlockPos var1) {
      this.u();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var2.level == this.U) {
         this.O++;
         var2.levelExtractor.setBlocksDirty(var1.getX() - 1, var1.getY() - 1, var1.getZ() - 1, var1.getX() + 1, var1.getY() + 1, var1.getZ() + 1);
      }
   }

   public Collection<SpawnerBlockEntity> A(Level var1) {
      return var1 == this.U && this.J() ? this.m.values() : List.of();
   }

   private void i(long var1) {
      SpawnerBlockEntity var3 = this.m.remove(var1);
      if (var3 != null) {
         BlockPos var4 = var3.getBlockPos();
         long var5 = ChunkPos.pack(var4.getX() >> 4, var4.getZ() >> 4);
         List var7 = (List)this.k.get(var5);
         if (var7 != null) {
            var7.remove(var3);
            if (var7.isEmpty()) {
               this.k.remove(var5);
            }
         }
      }
   }

   public Collection<SpawnerBlockEntity> O(Level var1, Vec3 var2, int var3) {
      if (var1 == this.U && this.J() && !this.k.isEmpty()) {
         ArrayList var4 = null;
         int var5 = Mth.floor(var2.x - var3) >> 4;
         int var6 = Mth.floor(var2.x + var3) >> 4;
         int var7 = Mth.floor(var2.z - var3) >> 4;
         int var8 = Mth.floor(var2.z + var3) >> 4;

         for (int var9 = var5; var9 <= var6; var9++) {
            for (int var10 = var7; var10 <= var8; var10++) {
               List var11 = (List)this.k.get(ChunkPos.pack(var9, var10));
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

   public boolean i(BlockEntity var1) {
      if (var1 instanceof SpawnerBlockEntity var2) {
         BlockPos var3 = var2.getBlockPos();
         if (var3 == null) {
            return false;
         }

         SpawnerBlockEntity var4 = this.m.get(var3.asLong());
         return var4 == var1;
      } else {
         return false;
      }
   }

   public void Q(String var1, BlockPos var2, BlockEntity var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 != null) {
            this.d(
               "request:" + var1 + ":" + var2.asLong(),
               1500L,
               "{} requested fake block entity pos={} result={} count={}",
               var1,
               var2,
               var3 != null ? var3.getClass().getSimpleName() : "null",
               this.T(var2)
            );
         }
      }
   }

   public void d(String var1, BlockPos var2, String var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 == null) {
            this.d("render:" + var1, 1500L, "renderer {} {}", var1, var3);
         } else {
            this.d("render:" + var1 + ":" + var2.asLong(), 1500L, "renderer {} pos={} count={} {}", var1, var2, this.T(var2), var3);
         }
      }
   }

   public void t(LevelChunk var1, Collection<BlockPos> var2) {
      this.c();
      this.G();
      if (this.J() && var1 != null) {
         int var3 = var1.getPos().x();
         int var4 = var1.getPos().z();
         int var5 = var3 << 4;
         int var6 = var4 << 4;
         int var7 = var5 + 15;
         int var8 = var6 + 15;
         MutableBlockPos var9 = new MutableBlockPos();
         HashSet var10 = new HashSet<>(this.v.keySet());
         var10.addAll(this.Y);

         for (Long var12 : var10) {
            BlockPos var13 = BlockPos.of(var12);
            int var14 = var13.getX();
            int var15 = var13.getZ();
            if (var14 >= var5 && var14 <= var7 && var15 >= var6 && var15 <= var8) {
               var9.set(var14, var13.getY(), var15);
               if (!var1.getLevel().isOutsideBuildHeight(var9)) {
                  this.X(var9, var1.getSection(var1.getSectionIndex(var9.getY())).getBlockState(var9.getX() & 15, var9.getY() & 15, var9.getZ() & 15));
               }
            }
         }

         if (var2 != null) {
            for (BlockPos var17 : new ArrayList(var2)) {
               if (var17 != null) {
                  this.X(var17, var1.getBlockState(var17));
               }
            }
         }
      }
   }

   public void l() {
      long var1 = Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
      this.c();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var3.player != null && this.J()) {
         this.N(var3);
         this.P();

         for (Entry var5 : this.vR.entrySet()) {
            if (var3.level.getGameTime() >= (Long)var5.getValue()) {
               this.vR.remove(var5.getKey());
               this.r(BlockPos.of((Long)var5.getKey()));
            }
         }

         this.U();
         if (!this.J.isEmpty() || !this.A.isEmpty() || !this.Y.isEmpty()) {
            for (Entity var6 : var3.level.entitiesForRendering()) {
               if (var6 instanceof ItemEntity var8) {
                  this.o(var8);
               }
            }
         }

         this.o();
         this.J.entrySet().removeIf(var0 -> var0.getValue().untilMs < System.currentTimeMillis());
         this.D.removeIf(var1x -> !(var3.level.getEntity(var1x) instanceof ItemEntity));
         m3.f();
         this.g();

         for (Entry var17 : new ArrayList<>(this.V.entrySet())) {
            if (!this.v.containsKey(var17.getKey()) && System.currentTimeMillis() - ((b8.f)var17.getValue()).createdAtMs > 5000L) {
               this.X(BlockPos.of((Long)var17.getKey()), null);
            }
         }

         if (!var3.options.keyAttack.isDown()) {
            for (Long var18 : new ArrayList<>(this.A.keySet())) {
               this.z(BlockPos.of(var18));
            }

            this.A.clear();
            this.c.clear();
         }

         for (Long var19 : new ArrayList<>(this.A.keySet())) {
            if (!(var3.hitResult instanceof BlockHitResult var22 && var22.getBlockPos().equals(BlockPos.of(var19)))) {
               this.z(BlockPos.of(var19));
               this.A.remove(var19);
               this.c.remove(var19);
            }
         }

         if (var3 != null && var3.level != null && var3.player != null && this.J()) {
            for (BlockEntity var21 : this.O(var3.level, var3.player.position(), 32)) {
               if (var21 instanceof SpawnerBlockEntity var23
                  && var23.getBlockPos() != null
                  && var3.level.hasChunkAt(var23.getBlockPos())
                  && this.a(var23.getBlockPos())
                  && var23.getBlockPos().distToCenterSqr(var3.player.position()) <= 1024.0) {
                  SpawnerBlockEntity.clientTick(var3.level, var23.getBlockPos(), var23.getBlockState(), var23);
               }
            }

            long var14 = var3.level.getGameTime();

            for (BlockPos var26 : this.O(var3.player.blockPosition(), 16, 8)) {
               if ((var14 + Math.floorMod(var26.asLong(), 3L)) % 20L == 0L) {
                  this.d(var3.level, var26);
               }
            }
         } else {
            for (Long var20 : new ArrayList<>(this.A.keySet())) {
               this.z(BlockPos.of(var20));
            }

            this.A.clear();
            this.c.clear();
         }

         if (var1 != 0L) {
            long var15 = System.nanoTime() - var1;
            long var25 = System.currentTimeMillis();
            if (var25 >= this.g) {
               this.g = var25 + 5000L;
               this.u(
                  "Work counters: piles={} reservations={} mappingRebuilds={} terrainInvalidations={} inventoryUpdates={} renderCandidates={} renderExtractions={} displayCopies={} tickMicros={}",
                  this.v.size(),
                  this.w.size(),
                  ki.x(),
                  this.O,
                  this.R,
                  this.T,
                  this.vP,
                  this.f,
                  var15 / 1000L
               );
            }

            if (var15 > 5000000L) {
               this.d("tick-spike", 2000L, "Client tick CPU spike micros={} piles={} reservations={}", var15 / 1000L, this.v.size(), this.w.size());
            }
         }
      } else {
         this.m();
      }
   }

   public int T(BlockPos var1) {
      return !this.a(var1) ? 0 : this.v.getOrDefault(var1.asLong(), 0);
   }

   public List<BlockPos> O(BlockPos var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();
      if (this.J() && var1 != null && !this.v.isEmpty()) {
         int var5 = var1.getX();
         int var6 = var1.getY();
         int var7 = var1.getZ();

         for (SpawnerBlockEntity var9 : this.O(this.U, Vec3.atCenterOf(var1), var2 + 1)) {
            BlockPos var10 = var9.getBlockPos();
            if (this.a(var10) && Math.abs(var10.getX() - var5) <= var2 && Math.abs(var10.getY() - var6) <= var3 && Math.abs(var10.getZ() - var7) <= var2) {
               var4.add(var10);
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   public boolean o(ItemEntity var1) {
      if (this.J() && var1 != null && this.Q(var1.getItem()) && !gz.W(var1.getItem())) {
         long var2 = System.currentTimeMillis();
         if (this.D.contains(var1.getId())) {
            return true;
         }

         HashSet var4 = new HashSet<>(this.Y);
         var4.addAll(this.A.keySet());

         for (long var6 : var4) {
            BlockPos var8 = BlockPos.of(var6);
            if (var1.distanceToSqr(var8.getX() + 0.5, var8.getY() + 0.5, var8.getZ() + 0.5) <= 4.84) {
               this.D.add(var1.getId());
               return true;
            }
         }

         this.J.entrySet().removeIf(var2x -> var2x.getValue().untilMs < var2);

         for (b8.b var14 : this.J.values()) {
            double var7 = var14.pos.getX() + 0.5 - var1.getX();
            double var9 = var14.pos.getY() + 0.5 - var1.getY();
            double var11 = var14.pos.getZ() + 0.5 - var1.getZ();
            if (var7 * var7 + var9 * var9 + var11 * var11 <= 4.840000000000001) {
               this.D.add(var1.getId());
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean A(ItemEntity var1) {
      return this.o(var1);
   }

   public ItemStack D(ItemStack var1) {
      if (!this.r(var1)) {
         return var1;
      }

      ItemStack var2 = new ItemStack(Items.SPAWNER, var1.getCount());
      var2.set(DataComponents.CUSTOM_NAME, this.f(var1));
      var2.set(DataComponents.LORE, new ItemLore(List.of(M(this.K(var1.getItem()).n, ChatFormatting.YELLOW))));
      if (this.I()) {
         var2.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
      }

      return var2;
   }

   public ItemStack s(ItemStack var1) {
      return var1;
   }

   public boolean u(ItemStack var1) {
      return this.J() && var1 != null && !var1.isEmpty() && !px.T(var1) && this.D(var1) && this.g(var1) > 0L;
   }

   public Component S(long var1) {
      return M("~", ChatFormatting.DARK_GRAY).append(o("$ ", 65280)).append(M(this.j(var1), ChatFormatting.WHITE));
   }

   public Component K(ItemStack var1) {
      return this.O(this.x(var1));
   }

   public Component v(ItemStack var1) {
      long var2 = this.g(var1);
      long var4 = Math.max(1L, (var2 + 63L) / 64L);
      return M("Contains: " + var4 + " stack(s)", ChatFormatting.WHITE);
   }

   public Component r() {
      return M("Left Shift", ChatFormatting.GOLD).append(M(": ", ChatFormatting.GOLD)).append(M("view contents", ChatFormatting.WHITE));
   }

   public Component W() {
      return M("Left Alt + Left Shift", ChatFormatting.GOLD).append(M(": ", ChatFormatting.GOLD)).append(M("view contents", ChatFormatting.WHITE));
   }

   public Component N() {
      return M("Left Control", ChatFormatting.GOLD).append(M(": ", ChatFormatting.GOLD)).append(M("lock tooltip", ChatFormatting.WHITE));
   }

   public List<Component> G(ItemStack var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1.getHoverName());
      var3.add(this.K(var1));
      if (!var2) {
         return var3;
      } else {
         boolean var4 = this.i(341);
         boolean var5 = this.i(340);
         boolean var6 = this.i(342);
         if (var4) {
            return var3;
         } else if (var5 && var6) {
            var3.add(this.N());
            return var3;
         } else if (var5) {
            var3.add(this.v(var1));
            var3.add(this.W());
            var3.add(this.N());
            return var3;
         } else {
            var3.add(this.v(var1));
            var3.add(this.r());
            return var3;
         }
      }
   }

   public Component k() {
      FakeSpawnerModule var1 = FakeSpawnerModule.M();
      String var2 = var1 != null ? var1.w() : "§dSpawner";
      return C(var2);
   }

   public Component f(ItemStack var1) {
      Component var2 = (Component)var1.get(DataComponents.CUSTOM_NAME);
      if (var2 != null) {
         return Component.literal(var2.getString()).withStyle(new ChatFormatting[]{ChatFormatting.WHITE, ChatFormatting.ITALIC});
      }

      ll var3 = this.K(var1.getItem());
      return M("Spawner", ChatFormatting.LIGHT_PURPLE);
   }

   public List<Component> J(int var1, boolean var2, boolean var3) {
      return this.I(new ItemStack(this.G(), var1), var2, var3);
   }

   public List<Component> I(ItemStack var1, boolean var2, boolean var3) {
      int var4 = var1.getCount();
      ArrayList var5 = new ArrayList();
      var5.add(M(this.K(var1.getItem()).n, ChatFormatting.YELLOW));
      if (var2) {
         var5.add(M("~", ChatFormatting.DARK_GRAY).append(o("$ ", 65280)).append(M(this.S((long)var4), ChatFormatting.WHITE)));
      }

      var5.add(Component.empty());
      var5.add(M("Interact with Spawn Egg:", ChatFormatting.GRAY));
      var5.add(M(" Sets Mob Type", ChatFormatting.BLUE));
      if (var3) {
         var5.add(M("minecraft:spawner", ChatFormatting.DARK_GRAY));
         var5.add(M("13 component(s)", ChatFormatting.DARK_GRAY));
      }

      return var5;
   }

   public boolean U() {
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

   public boolean I() {
      FakeSpawnerModule var1 = FakeSpawnerModule.M();
      return var1 != null && var1.w();
   }

   public void w() {
      this.V(this.j);
   }

   public void V(BlockPos var1) {
      this.c();
      Minecraft var2 = Minecraft.getInstance();
      if (this.J() && var2 != null && var2.level != null && var2.player != null) {
         this.j = var1 == null ? null : var1.immutable();
         long var3 = this.u(var1);
         int var5 = this.d(var1);
         ll var6 = this.P(var1);
         b8.i var7 = this.Q(var3, var5, var6);
         Map var8 = var1 == null ? Map.of() : this.S.getOrDefault(var1.asLong(), Map.of());
         var7 = new b8.i(
            Math.max(0, var7.w - var8.getOrDefault(var6.Z, 0) - mm.N(var6.Z)),
            Math.max(0, var7.A - var8.getOrDefault(var6.t, 0) - mm.N(var6.t)),
            var7.a,
            var7.Y
         );
         String var9 = var5 + " " + u(var6.n.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"));
         SimpleContainer var10 = new SimpleContainer(54);
         int var11 = 0;
         var11 = n(var10, var11, var6.Z, var7.w);
         n(var10, var11, var6.t, var7.A);
         var10.setItem(
            48,
            g(
               Items.GOLD_INGOT.getDefaultInstance(),
               M("Sell All", ChatFormatting.WHITE),
               List.of(M("Click to sell all loot", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         var10.setItem(49, g(var6.m(), U(var5 + " " + var6.n.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"), 64648), this.J(var7, var6)));
         var10.setItem(
            50,
            g(
               Items.DROPPER.getDefaultInstance(),
               M("Drop Loot", ChatFormatting.WHITE),
               List.of(M("Click to drop all loot on the page", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         ChestMenu var12 = new ChestMenu(MenuType.GENERIC_9x6, 0, var2.player.getInventory(), var10, 6);
         var2.gui.setScreen(new mn(var12, var2.player.getInventory(), Component.literal(var9), var1));
      }
   }

   public void S(BlockPos var1, Item var2, int var3) {
      if (var1 != null && var2 != null && var3 > 0) {
         this.S.computeIfAbsent(var1.asLong(), var0 -> new HashMap<>()).merge(var2, var3, Integer::sum);
      }
   }

   public long x(ItemStack var1) {
      if (!this.J() || var1.isEmpty() || px.T(var1)) {
         return 0L;
      }

      if (this.Q(var1)) {
         return var1.getCount() * 1000L;
      }

      if (ll.r(var1.getItem())) {
         return this.n(var1.getItem(), var1.getCount());
      }

      ItemContainerContents var2 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
      return var2 == null ? 0L : var2.nonEmptyItemCopyStream().mapToLong(this::x).sum() * var1.getCount();
   }

   public Component O(long var1) {
      double var3 = var1 >= 1000000000L ? 1.0E9 : (var1 >= 1000000L ? 1000000.0 : (var1 >= 1000L ? 1000.0 : 1.0));
      String var5 = var3 == 1.0E9 ? "B" : (var3 == 1000000.0 ? "M" : (var3 == 1000.0 ? "K" : ""));
      return M("~", ChatFormatting.DARK_GRAY).append(o("$ ", 65280)).append(M(l(var1 / var3, var3 == 1.0 ? 0 : 1) + var5, ChatFormatting.WHITE));
   }

   public long R(int var1) {
      return this.n(Items.BONE, var1);
   }

   public long n(Item var1, int var2) {
      Double var3 = FakeSpawnerModule.M().e(BuiltInRegistries.ITEM.getKey(var1).toString());
      if (var3 != null) {
         return Math.round(var3 * Math.max(0, var2));
      }

      String var4 = BuiltInRegistries.ITEM.getKey(var1).getPath().replace('_', ' ');
      kb var5 = kb.h();
      var5.Z(var4, var0 -> {});
      double var6 = this.h.getOrDefault(var1, (double)var5.P(var4));
      return Math.round(var6 * Math.max(0, var2));
   }

   public void o() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gui.screen() != null && var1.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("order")) {
         AbstractContainerMenu var2 = var1.player.containerMenu;
         HashMap var3 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5.container != var1.player.getInventory()) {
               ItemStack var6 = var5.getItem();
               if (ll.r(var6.getItem())) {
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

         var3.forEach((var1x, var2x) -> this.h.merge(var1x, var2x, Math::max));
      }
   }

   public int x() {
      return this.Q(Items.BONE);
   }

   private boolean c(ItemStack var1) {
      if (var1.has(DataComponents.CUSTOM_NAME)) {
         return false;
      }

      FakeSpawnerModule var2 = FakeSpawnerModule.M();

      for (ll var6 : ll.values()) {
         String var7 = var2.Y(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1.is(var6.Z)) {
            return true;
         }
      }

      return false;
   }

   private int Q(Item var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.J() && var2.player != null) {
         int var3 = -1;
         int var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6.is(var1) && !var6.has(DataComponents.CUSTOM_NAME) && !zs.t(var6) && var6.getCount() > var4) {
               var3 = var5;
               var4 = var6.getCount();
            }
         }

         return this.I = var3;
      } else {
         return -1;
      }
   }

   public void Y(int var1) {
   }

   public void r(AbstractContainerMenu var1, Slot var2) {
   }

   public void E(Slot var1) {
   }

   public void Z(BlockPos var1) {
      if (var1 != null && this.D(var1.asLong())) {
         this.z(var1);
         this.r(var1);
         com.larpclient.h.U("FakeSpawner", "Final break predicted at {}; retaining pile and reservations until server acknowledgment", var1);
      }
   }

   boolean D(long var1) {
      return this.v.getOrDefault(var1, 0) > 0 && this.d.add(var1);
   }

   boolean w(long var1) {
      return this.d.remove(var1);
   }

   public BlockState I(Level var1, BlockPos var2, BlockState var3) {
      if (this.J() && var1 == this.U && this.v.getOrDefault(var2.asLong(), 0) > 0 && var1 instanceof ClientLevel var4) {
         if (var1.isOutsideBuildHeight(var2)) {
            return Blocks.AIR.defaultBlockState();
         }

         LevelChunk var5 = var4.getChunkAt(var2);
         return var5.getSection(var5.getSectionIndex(var2.getY())).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15);
      } else {
         return var3;
      }
   }

   public void z(Runnable var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.J() && var2.player != null && var2.isSameThread()) {
         try {
            this.J(var2.player.getInventory(), var1);
         } finally {
            this.P();
         }
      } else {
         var1.run();
      }
   }

   void J(Container var1, Runnable var2) {
      try {
         var2.run();
      } finally {
         ki.n();
      }
   }

   private void F(int var1, ItemStack var2) {
      ItemStack var3 = this.w.get(var1);
      if (var3 != null && var2.is(var3.getItem()) && var2.getCount() >= var3.getCount() && !ItemStack.isSameItemSameComponents(var2, var3)) {
         this.w.put(var1, var2.copyWithCount(var3.getCount()));

         for (Deque var5 : this.q.values()) {
            ArrayDeque var6 = new ArrayDeque();

            for (b8.v var8 : var5) {
               var6.addLast(var8.preferredSlot == var1 && var8.stack.is(var2.getItem()) ? new b8.v(var1, var2.copyWithCount(var8.stack.getCount())) : var8);
            }

            var5.clear();
            var5.addAll(var6);
         }
      }
   }

   public boolean o(ItemStack var1) {
      return this.J() && !var1.isEmpty() && this.w.values().stream().anyMatch(var1x -> ItemStack.isSameItemSameComponents(var1, var1x));
   }

   public boolean a(int var1, int var2, int var3, ContainerInput var4) {
      if (!this.J() || m3.d()) {
         return true;
      }

      if (m3.u()) {
         return false;
      }

      LocalPlayer var5 = Minecraft.getInstance().player;
      return var5 != null && var5.containerMenu.containerId == var1 ? this.P(var5.containerMenu, var2, var3, var4) : true;
   }

   public boolean P(AbstractContainerMenu var1, int var2, int var3, ContainerInput var4) {
      if (!this.J() || m3.d()) {
         return true;
      }

      if (m3.u()) {
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
         if (var6.container == var5.getInventory() && (this.t(var6.getContainerSlot()) || mm.o(var6.getContainerSlot()) || this.C(var6.getContainerSlot()))) {
            return false;
         }
      }

      return var4 != ContainerInput.SWAP || !this.t(var3) && !mm.o(var3) && !this.C(var3)
         ? var4 != ContainerInput.PICKUP_ALL || !this.o(var1.getCarried())
         : false;
   }

   public void j(int var1, Runnable var2) {
      this.z(var2);
   }

   public boolean c(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      ItemStack var4 = this.w.get(var2);
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
         if (this.c(var3.player.getInventory().getItem(var1))) {
            this.Y(var2);
         }

         m3.W(() -> var3.gameMode.handleContainerInput(0, var1, var2, ContainerInput.SWAP, var3.player));
         if (var4 != null && !var4.isEmpty()) {
            this.w.remove(var2);
            this.w.put(var1, var4);

            for (Deque var6 : this.q.values()) {
               ArrayDeque var7 = new ArrayDeque();

               for (b8.v var9 : var6) {
                  var7.addLast(var9.preferredSlot == var2 ? new b8.v(var1, var9.stack) : var9);
               }

               var6.clear();
               var6.addAll(var7);
            }

            ki.n();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean t(int var1) {
      return this.J() && this.w.containsKey(var1) && !this.w.get(var1).isEmpty();
   }

   public int l(int var1, ItemStack var2) {
      ItemStack var3 = this.w.get(var1);
      return this.J() && var3 != null && var2.is(var3.getItem()) ? Math.max(0, var2.getCount() - var3.getCount()) : 0;
   }

   public ItemStack N(int var1, ItemStack var2) {
      if (!this.t(var1)) {
         return var2;
      }

      int var3 = this.l(var1, var2);
      return var3 == 0 ? ItemStack.EMPTY : gz.r(var2.copyWithCount(var3));
   }

   public boolean n(AbstractContainerMenu var1, List<m3.g> var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var1 == var3.player.inventoryMenu && var3.player.hasInfiniteMaterials() && var3.player.containerMenu instanceof ItemPickerMenu
         )
       {
         this.z(() -> {
            for (m3.g var4 : var2) {
               var1.clicked(var4.slot(), var4.button(), var4.type(), var3.player);
            }

            var1.broadcastChanges();
         });
         return true;
      } else {
         return false;
      }
   }

   public boolean f(AbstractContainerMenu var1, Slot var2, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var2 != null && var2.container == var4.player.getInventory() && var3 >= 0 && var3 <= 1) {
         int var5 = var2.getContainerSlot();
         ItemStack var6 = var4.player.getInventory().getItem(var5);
         int var7 = this.l(var5, var6);
         if (var7 != 0 && var1.getCarried().isEmpty()) {
            int var8 = var3 == 0 ? var7 : (var7 + 1) / 2;
            ArrayList var9 = new ArrayList();
            var9.add(new m3.g(var2.index, 0, ContainerInput.PICKUP));

            for (int var10 = 0; var10 < var6.getCount() - var8; var10++) {
               var9.add(new m3.g(var2.index, 1, ContainerInput.PICKUP));
            }

            int var11 = var6.getCount() - var7;
            if (this.n(var1, var9)) {
               com.larpclient.h.U("FakeSpawner", "Creative surplus picked physical={} amount={} reserved={}", var5, var8, var11);
               return true;
            } else {
               m3.t(var1, "pick unreserved surplus", var9, var6.copyWithCount(var8), ki::n, ki::n);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int j() {
      Inventory var1 = Minecraft.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.t(var2) && !this.C(var2) && var1.getItem(var2).isEmpty()) {
            return var2;
         }
      }

      return -1;
   }

   public int a(int var1) {
      return this.c(Items.BONE, var1);
   }

   public int c(Item var1, int var2) {
      return mm.O(var1, var2);
   }

   public boolean U(ItemStack var1) {
      return false;
   }

   public boolean s() {
      return this.L(1) == 1;
   }

   public int L(int var1) {
      return this.f(Items.BONE, var1);
   }

   public int f(Item var1, int var2) {
      return this.c(var1, var2);
   }

   public boolean S(int var1) {
      return var1 == 48 || var1 == 50;
   }

   public boolean A(Component var1, AbstractContainerMenu var2) {
      if (var1 != null && var2 != null) {
         String var3 = var1.getString().toLowerCase(Locale.ROOT);
         return var3.contains("sell") && var2.slots.size() >= 72;
      } else {
         return false;
      }
   }

   public Component q(Component var1) {
      return var1 == null ? Component.literal(u("place items in here to sell")) : Component.literal(u(var1.getString()));
   }

   public void V(Component var1, AbstractContainerMenu var2) {
      if (this.J() && this.A(var1, var2)) {
         long var3 = 0L;

         for (int var5 = 0; var5 < Math.max(0, var2.slots.size() - 36); var5++) {
            var3 += this.x(var2.getSlot(var5).getItem());
         }

         if (var3 > 0L) {
            za.j(var3);
         }
      }
   }

   private long u(BlockPos var1) {
      long var2 = System.currentTimeMillis();
      return var1 == null ? var2 : this.s.computeIfAbsent(var1.asLong(), var2x -> var2);
   }

   private int d(BlockPos var1) {
      return var1 == null ? Math.max(1, FakeSpawnerModule.M().x()) : Math.max(1, this.v.getOrDefault(var1.asLong(), 1));
   }

   private b8.i W(long var1, int var3) {
      return this.Q(var1, var3, ll.o);
   }

   private b8.i Q(long var1, int var3, ll var4) {
      double var5 = this.b(var3);
      double var7 = var4.t == Items.AIR ? 0.0 : Math.max(0.15, var5 * 0.33);
      long var9 = Math.max(0L, System.currentTimeMillis() - var1);
      long var11 = var9 / 60000L;
      return new b8.i(Math.max(0, (int)(var11 * var5)), Math.max(0, (int)(var11 * var7)), var5, var7);
   }

   private double b(int var1) {
      int var2 = Math.max(1, var1);
      if (var2 == 1) {
         return 1.9;
      } else {
         return var2 <= 64 ? var2 * 1.9453125 : 124.5 + (var2 - 64) * 0.88671875;
      }
   }

   private static int n(SimpleContainer var0, int var1, Item var2, int var3) {
      int var4 = Math.max(0, var3);

      while (var4 > 0 && var1 < 45) {
         int var5 = Math.min(64, var4);
         var0.setItem(var1++, new ItemStack(var2, var5));
         var4 -= var5;
      }

      return var1;
   }

   private List<Component> J(b8.i var1, ll var2) {
      int var3 = var1 == null ? 0 : Math.max(0, var1.A);
      int var4 = var1 == null ? 0 : Math.max(0, var1.w);
      if (var3 <= 0 && var4 <= 0) {
         return List.of(o("(0% filled)", 64648));
      }

      ArrayList var5 = new ArrayList();
      if (var3 > 0) {
         var5.add(o(var3 + " ", 64648).append(var2.t.getName(new ItemStack(var2.t)).copy().withStyle(ChatFormatting.WHITE)));
      }

      if (var4 > 0) {
         var5.add(o(var4 + " ", 64648).append(var2.Z.getName(new ItemStack(var2.Z)).copy().withStyle(ChatFormatting.WHITE)));
      }

      var5.add(o("(0% filled)", 64648));
      return var5;
   }

   private boolean F(long var1, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && !m3.u() && var4.player.containerMenu == var4.player.inventoryMenu && var4.player.inventoryMenu.getCarried().isEmpty()) {
         Deque var5 = this.q.get(var1);
         if (var5 != null && !var5.isEmpty()) {
            LinkedHashMap var6 = new LinkedHashMap();
            int var7 = var3;

            for (b8.v var9 : var5.reversed()) {
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
               ItemStack var12 = this.w.get(var17.getKey());
               if (var12 != null
                  && var12.getCount() == (Integer)var17.getValue()
                  && var4.player.getInventory().getItem((Integer)var17.getKey()).getCount() == (Integer)var17.getValue()) {
                  var14 = (Integer)var17.getKey();
                  break;
               }
            }

            if (var14 < 0) {
               var14 = this.j();
            }

            if (var14 < 0) {
               return false;
            }

            ArrayList var16 = new ArrayList();

            for (Entry var20 : var6.entrySet()) {
               if ((Integer)var20.getKey() != var14) {
                  var16.addAll(
                     m3.h((Integer)var20.getKey(), var14, (Integer)var20.getValue(), var4.player.getInventory().getItem((Integer)var20.getKey()).getCount())
                  );
               }
            }

            if (var16.isEmpty()) {
               this.Q(var1, var3);
               if (var7 > 0) {
                  this.o(new ItemStack(this.L(var1), var7), var7);
               }

               ki.n();
               return true;
            } else {
               int var19 = var14;
               int var21 = var7;
               m3.j("mining return", var16, () -> {
                  this.Q(var1, var3);
                  this.y(var1, var3);
                  if (var21 > 0) {
                     this.o(new ItemStack(this.L(var1), var21), var21);
                  }

                  this.Q(BlockPos.of(var1), true);
                  ki.n();
                  this.u("Mining return predicted pile={} physical={} amount={} worldPending={}", var1, var19, var13, var21);
               }, () -> this.g());
               return false;
            }
         } else {
            this.o(new ItemStack(this.L(var1), var3), var3);
            return true;
         }
      } else {
         return false;
      }
   }

   private void o(ItemStack var1, int var2) {
      if (var2 > 0 && !var1.isEmpty()) {
         Inventory var3 = Minecraft.getInstance().player.getInventory();
         int var4 = -1;

         for (int var5 = 0; var5 < 36; var5++) {
            if (!this.t(var5)) {
               ItemStack var6 = var3.getItem(var5);
               b8.r var7 = this.K.get(var5);
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
            var4 = this.j();
         }

         if (var4 >= 0) {
            int var8 = var3.getItem(var4).isEmpty() && !this.K.containsKey(var4) ? ki.n() : -1;
            this.f(var4, var1, 0, var2);
            if (var8 >= 0) {
               ki.g(var8, var4);
            }
         }
      }
   }

   private void f(int var1, ItemStack var2, int var3, int var4) {
      if (var4 > 0) {
         b8.r var5 = this.K.get(var1);
         int var6 = Minecraft.getInstance().player.getInventory().getItem(var1).getCount();
         int var7 = var5 != null && ItemStack.isSameItemSameComponents(var5.stack, var2) ? Math.max(var5.expected + var3, var6) : var6;
         this.K.put(var1, new b8.r(var2.copyWithCount(1), var7 + var4, Minecraft.getInstance().player.tickCount + 100));
         ki.I();
      }
   }

   public boolean C(int var1) {
      return this.K.containsKey(var1);
   }

   public ItemStack Q(int var1, ItemStack var2) {
      b8.r var3 = this.K.get(var1);
      return var3 != null && var2.isEmpty() ? gz.r(var3.stack.copyWithCount(var3.expected)) : var2;
   }

   private b8.v Q(long var1, int var3) {
      Deque var4 = this.q.get(var1);
      if (var4 != null && !var4.isEmpty() && var3 > 0) {
         int var5 = var3;
         int var6 = -1;
         ItemStack var7 = ItemStack.EMPTY;

         while (var5 > 0 && !var4.isEmpty()) {
            b8.v var8 = (b8.v)var4.removeLast();
            if (var8 != null && var8.stack != null && !var8.stack.isEmpty()) {
               if (var6 < 0) {
                  var6 = var8.preferredSlot;
               }

               if (var7.isEmpty()) {
                  var7 = var8.stack.copy();
               }

               int var9 = Math.min(var5, var8.stack.getCount());
               ItemStack var10 = this.w.get(var8.preferredSlot);
               if (var10 != null && ItemStack.isSameItemSameComponents(var10, var8.stack)) {
                  if (var10.getCount() <= var9) {
                     this.w.remove(var8.preferredSlot);
                  } else {
                     this.w.put(var8.preferredSlot, var10.copyWithCount(var10.getCount() - var9));
                  }
               }

               var5 -= var9;
               int var11 = var8.stack.getCount() - var9;
               if (var11 > 0) {
                  var4.addLast(new b8.v(var8.preferredSlot, var8.stack.copyWithCount(var11)));
               }
            }
         }

         if (var4.isEmpty()) {
            this.q.remove(var1);
         }

         return var7.isEmpty() ? null : new b8.v(var6, var7.copyWithCount(var3));
      } else {
         return null;
      }
   }

   private boolean E(long var1, b8.v var3, boolean var4) {
      if (var3 != null && !var3.stack.isEmpty()) {
         Minecraft var5 = Minecraft.getInstance();
         if (var5.player == null) {
            return false;
         }

         int var6 = var3.preferredSlot;
         if (var6 >= 0 && var6 < var5.player.getInventory().getContainerSize() && !this.w.containsKey(var6)) {
            ItemStack var7 = var5.player.getInventory().getItem(var6);
            int var8 = var3.stack.getCount();
            int var9 = var4 && !var5.player.hasInfiniteMaterials() ? var8 - 1 : var8;
            if (var9 <= 0) {
               return true;
            } else if (var7.is(var3.stack.getItem()) && var7.getCount() >= var9) {
               this.w.put(var6, var7.copyWithCount(var9));
               this.q.computeIfAbsent(var1, var0 -> new ArrayDeque<>()).addFirst(new b8.v(var6, var7.copyWithCount(var9)));
               ki.n();
               com.larpclient.h.U(
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

   private void n(BlockPos var1, Direction var2) {
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

   private void f(Minecraft var1, BlockPos var2, Direction var3, double var4) {
      if (var1 != null && var1.player != null && var1.levelRenderer != null && var2 != null) {
         int var6 = Mth.clamp((int)(var4 * 10.0), 0, 9);
         var1.level.destroyBlockProgress(var1.player.getId(), var2, var6);
         if (var4 <= 0.0) {
            return;
         }

         long var7 = var2.asLong();
         long var9 = var1.player.tickCount;
         long var11 = this.c.getOrDefault(var7, 0L);
         if (var9 - var11 >= 4L) {
            this.c.put(var7, var9);
            this.n(var2, var3);
            this.Q(var2, false);
         }
      }
   }

   private void z(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var2.levelRenderer != null && var1 != null) {
         var2.level.destroyBlockProgress(var2.player.getId(), var1, -1);
      }
   }

   private void d(Level var1, BlockPos var2) {
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

   private void M(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 != null && !var1.isBlank()) {
         k4.f(o(var1, 16711680), false);
         k4.f(o(var1, 16711680), true);
      }
   }

   private void u(String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         com.larpclient.h.U("FakeSpawner", var1, var2);
      }
   }

   private void d(String var1, long var2, String var4, Object... var5) {
      if (Boolean.getBoolean("larp.debug")) {
         long var6 = System.currentTimeMillis();
         if (var6 >= this.Q) {
            this.Q = var6 + var2;
            this.u(var4, var5);
         }
      }
   }

   private static ItemStack g(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1);
      var0.set(DataComponents.RARITY, Rarity.COMMON);
      if (var2 != null && !var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component g(String var0, ChatFormatting var1) {
      return M(u(var0), var1);
   }

   private static MutableComponent M(String var0, ChatFormatting var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.applyFormat(var1).withItalic(false));
   }

   private static Component U(String var0, int var1) {
      return o(u(var0), var1);
   }

   private static MutableComponent o(String var0, int var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)).withItalic(false));
   }

   private static String u(String var0) {
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

   private static String l(double var0, int var2) {
      String var3 = String.format(Locale.US, "%." + var2 + "f", var0);

      while (var3.contains(".") && var3.endsWith("0")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      if (var3.endsWith(".")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3;
   }

   private static Component C(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         MutableComponent var1 = Component.empty();
         Style var2 = Style.EMPTY.withItalic(false);
         StringBuilder var3 = new StringBuilder();

         for (int var4 = 0; var4 < var0.length(); var4++) {
            char var5 = var0.charAt(var4);
            if ((var5 == 167 || var5 == '&') && var4 + 1 < var0.length()) {
               if (var5 == '&' && var4 + 7 < var0.length() && var0.charAt(var4 + 1) == '#') {
                  Integer var6 = J(var0.substring(var4 + 2, var4 + 8));
                  if (var6 != null) {
                     O(var1, var3, var2);
                     var2 = var2.withColor(TextColor.fromRgb(var6)).withItalic(false);
                     var4 += 7;
                     continue;
                  }
               }

               ChatFormatting var7 = ChatFormatting.getByCode(var0.charAt(var4 + 1));
               if (var7 != null) {
                  O(var1, var3, var2);
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

         O(var1, var3, var2);
         return var1;
      } else {
         return Component.empty();
      }
   }

   private static void O(MutableComponent var0, StringBuilder var1, Style var2) {
      if (var1.length() != 0) {
         var0.append(Component.literal(var1.toString()).setStyle(var2));
         var1.setLength(0);
      }
   }

   private static Integer J(String var0) {
      try {
         return Integer.parseInt(var0, 16);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private String S(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0;
      if (var5 >= 1.0E9) {
         return l(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return l(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? l(var5 / 1000.0, 1) + "K" : l(var5, 0);
      }
   }

   private String j(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0 + 75.0;
      if (var5 >= 1.0E9) {
         return l(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return l(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? l(var5 / 1000.0, 1) + "K" : l(var5, 0);
      }
   }

   private boolean D(ItemStack var1) {
      return var1 != null && !var1.isEmpty() ? var1.getItem() instanceof BlockItem var2 && var2.getBlock() instanceof ShulkerBoxBlock : false;
   }

   private long g(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         long var2 = 0L;
         ItemContainerContents var4 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
         if (var4 != null) {
            for (ItemStack var6 : var4.nonEmptyItemCopyStream().toList()) {
               if (var6 != null && !var6.isEmpty() && this.Q(var6)) {
                  var2 += var6.getCount();
               }
            }
         }

         return var2;
      } else {
         return 0L;
      }
   }

   private int O(CompoundTag var1) {
      if (var1 == null) {
         return 0;
      } else if (var1.contains("count")) {
         return Math.max(0, var1.getIntOr("count", 0));
      } else {
         return var1.contains("Count") ? Math.max(0, var1.getIntOr("Count", 0)) : 0;
      }
   }

   private boolean i(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2 != null && var2.getWindow() != null ? k2.w(var2.getWindow(), var1) : false;
   }

   private record b(BlockPos pos, long untilMs) {
   }

   private record f(int stackCount, b8.v hiddenStack, long createdAtMs) {
   }

   private record g(long pos, ll kind, int count, long placedAt, List<b8.l> backing, Map<String, Integer> purchased) {
      JsonObject toJson() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("pos", this.pos);
         var1.addProperty("kind", this.kind.name());
         var1.addProperty("count", this.count);
         var1.addProperty("placedAt", this.placedAt);
         JsonArray var2 = new JsonArray();

         for (b8.l var4 : this.backing) {
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

   public static final class i {
      public final int w;
      public final int A;
      public final double a;
      public final double Y;

      public i(int var1, int var2, double var3, double var5) {
         this.w = Math.max(0, var1);
         this.A = Math.max(0, var2);
         this.a = Math.max(0.0, var3);
         this.Y = Math.max(0.0, var5);
      }
   }

   private record l(int slot, String item, int amount, int reserved) {
   }

   public record o(SoundEvent event, float volume, float pitch) {
   }

   private record r(ItemStack stack, int expected, int expires) {
   }

   private record v(int preferredSlot, ItemStack stack) {
   }
}
