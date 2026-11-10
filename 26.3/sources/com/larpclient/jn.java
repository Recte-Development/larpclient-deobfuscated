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

public final class jn {
   private static final jn uK = new jn();
   private static final int K = 45;
   private static final String A = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀsᴛᴜᴠᴡxʏᴢ";
   private static final long N = 1800L;
   private static final long F = 650L;
   private static final int I = 64648;
   private static final int s = 16711680;
   private static final double m = 75.0;
   private final Map<Long, Long> j = new HashMap<>();
   private final Map<Long, Map<Item, Integer>> l = new HashMap<>();
   private final Map<Long, Integer> Y = new ConcurrentHashMap<>();
   private final Set<Long> H = ConcurrentHashMap.newKeySet();
   private final Map<Long, jn.o> a = new HashMap<>();
   private final Map<Long, Deque<jn.x>> T = new HashMap<>();
   private final Map<Integer, ItemStack> h = new HashMap<>();
   private final Map<Long, jn.u> L = new HashMap<>();
   private volatile Map<Long, SpawnerBlockEntity> d = new ConcurrentHashMap<>();
   private final Long2ObjectMap<List<SpawnerBlockEntity>> r = new Long2ObjectOpenHashMap();
   private final Map<Long, Long> B = new HashMap<>();
   private final Map<Long, Float> t = new HashMap<>();
   private final Map<Long, Integer> M = new HashMap<>();
   private int uy;
   private final Set<Integer> O = new HashSet<>();
   private final Map<Integer, jn.a> E = new HashMap<>();
   private BlockPos y;
   private int G = -1;
   private final Map<Long, Integer> w = new HashMap<>();
   private final Map<Long, Double> R = new HashMap<>();
   private final Map<Long, Integer> P = new ConcurrentHashMap<>();
   private final Set<Long> ud = new HashSet<>();
   private final Map<Item, Double> W = new HashMap<>();
   private final Set<Long> o = ConcurrentHashMap.newKeySet();
   private volatile Level k;
   private LocalPlayer un;
   private Item u;
   private String X;
   private volatile Map<Item, ni> f = Map.of();
   private volatile LongSet b = LongSets.EMPTY_SET;
   private volatile LongSet c = LongSets.EMPTY_SET;
   private final Map<Long, Long> J = new ConcurrentHashMap<>();
   private final Map<Long, ni> V = new ConcurrentHashMap<>();
   private final Map<Long, jn.w> S = new HashMap<>();
   private final List<JsonElement> x = new ArrayList<>();
   private String Q;
   private boolean C = (boolean)1;
   private long p;
   private long U;
   private long e;
   private long g;
   private long q;
   private long z;
   private final Set<ItemStack> Z = Collections.newSetFromMap(new IdentityHashMap<>());
   private final Set<ItemStack> u4 = Collections.newSetFromMap(new WeakHashMap<>());
   private InteractionHand D;
   private int v = -1;
   private int ux = -1;
   private final Set<Long> i = ConcurrentHashMap.newKeySet();
   private long n;

   private static Path n() {
      return FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-piles.json");
   }

   void F(Container var1) {
      this.Z.clear();
      this.u4.clear();

      for (int var2 = 0; var2 < var1.getContainerSize(); var2++) {
         ItemStack var3 = var1.getItem(var2);
         if (!var3.isEmpty()) {
            this.Z.add(var3);
         }
      }
   }

   public ItemStack q(ItemStack var1, ItemStack var2) {
      if (!var2.isEmpty() && (this.Z.contains(var1) || this.u4.contains(var1))) {
         this.u4.add(var2);
         if (Boolean.getBoolean("larp.debug")) {
            this.q++;
         }
      }

      return var2;
   }

   public void f() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null) {
         this.F(var1.player.getInventory());
         if (!var1.player.containerMenu.getCarried().isEmpty()) {
            this.Z.add(var1.player.containerMenu.getCarried());
         }

         _8.S();
      }
   }

   public void R() {
      this.z();
      if (Boolean.getBoolean("larp.debug")) {
         this.U++;
      }
   }

   public void k(int var1, int var2) {
      if (Boolean.getBoolean("larp.debug")) {
         this.e += var1;
         this.g += var2;
      }
   }

   public boolean e(ItemStack var1) {
      if (!this.a()) {
         return false;
      }

      LocalPlayer var2 = Minecraft.getInstance().player;
      return var2 != null && (var1 == var2.getMainHandItem() && this.G(var2.getInventory().getSelectedSlot()) || var1 == var2.getOffhandItem() && this.G(40));
   }

   public void Q() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner reset must run on the client thread");
      }

      this.r();
      q7.i("controller cleanup");
      _8.P();
      this.Z.clear();
      this.u4.clear();
      this.d = new ConcurrentHashMap<>();
      this.r.clear();
      this.b = LongSets.EMPTY_SET;
      this.c = LongSets.EMPTY_SET;
      this.k = null;
      this.G = -1;
      this.D = null;
      this.ux = -1;
      this.M.clear();
      this.uy = 0;
      this.i.clear();
      this.O.clear();
      this.E.clear();
      this.w.clear();
      this.R.clear();
      this.P.clear();
      this.ud.clear();
      this.W.clear();
      this.o.clear();

      for (Long var3 : new ArrayList<>(this.t.keySet())) {
         this.A(BlockPos.of(var3));
      }

      int var6 = this.Y.isEmpty() && this.H.isEmpty() ? 0 : 1;
      this.Y.clear();
      this.V.clear();
      this.S.clear();
      this.x.clear();
      this.Q = null;
      this.H.clear();
      this.j.clear();
      this.l.clear();
      this.a.clear();
      this.T.clear();
      this.h.clear();

      for (long var4 : this.J.keySet()) {
         this.J(BlockPos.of(var4));
      }

      this.J.clear();
      this.c = LongSets.EMPTY_SET;
      this.L.clear();
      this.t.clear();
      this.B.clear();
      this.y = null;
      this.un = null;
      this.u = null;
      if (var1 != null) {
         if (var1.gui.screen() instanceof w2 || var1.gui.screen() instanceof dq) {
            var1.gui.setScreen(null);
         }

         if (var6 != 0 && var1.level != null) {
            var1.levelExtractor.allChanged();
         }
      }
   }

   private void u() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1 != null && !var1.isSameThread()) {
         throw new IllegalStateException("Fake spawner state must be updated on the client thread");
      }

      ClientLevel var2 = var1 == null ? null : var1.level;
      LocalPlayer var3 = var1 == null ? null : var1.player;
      Item var4 = this.C();
      if (var2 != this.k || var3 != this.un || !this.u().equals(this.X) || !this.a()) {
         this.Q();
         this.k = var2;
         this.un = var3;
         this.u = var4;
         this.X = this.u();
         HashMap var5 = new HashMap();
         FakeSpawnerModule var6 = FakeSpawnerModule.O();
         if (var6 != null) {
            for (ni var10 : ni.values()) {
               String var11 = var6.I(var10.ordinal());
               if (var11 != null && !var11.isBlank()) {
                  var5.put(Y(var11), var10);
               }
            }
         }

         this.f = Map.copyOf(var5);
         if (var2 != null && var3 != null && this.a()) {
            this.Q = this.O(var1);
            this.A();
            this.C = (boolean)1;
         }
      }
   }

   private String O(Minecraft var1) {
      String var2 = var1.getCurrentServer() != null
         ? var1.getCurrentServer().ip
         : (
            var1.getSingleplayerServer() != null
               ? "singleplayer:" + var1.getSingleplayerServer().getWorldPath(LevelResource.ROOT).toAbsolutePath().normalize()
               : null
         );
      return var2 == null ? null : var2 + "|" + var1.level.dimension().identifier() + "|" + var1.player.getUUID();
   }

   private void A() {
      if (this.Q != null) {
         this.s(n());
      }
   }

   private void s(Path var1) {
      if (this.Q != null && Files.exists(var1)) {
         try {
            JsonObject var2 = ds.E(var1);
            if (var2.has("quarantined") && var2.get("quarantined").isJsonObject() && var2.getAsJsonObject("quarantined").has(this.Q)) {
               for (JsonElement var4 : var2.getAsJsonObject("quarantined").getAsJsonArray(this.Q)) {
                  this.x.add(var4.deepCopy());
               }
            }

            if (!var2.has(this.Q)) {
               return;
            }

            int var20 = 0;

            for (JsonElement var5 : var2.getAsJsonArray(this.Q)) {
               try {
                  JsonObject var6 = var5.getAsJsonObject();
                  long var7 = var6.get("pos").getAsLong();
                  ni var9 = ni.valueOf(var6.get("kind").getAsString());
                  int var10 = var6.get("count").getAsInt();
                  long var11 = var6.get("placedAt").getAsLong();
                  ArrayList var13 = new ArrayList();

                  for (JsonElement var15 : var6.getAsJsonArray("backing")) {
                     JsonObject var16 = var15.getAsJsonObject();
                     var13.add(
                        new jn.p(
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

                  if (var10 < 1 || var10 > 100000 || var10 > 1 && var13.isEmpty() || this.S.containsKey(var7)) {
                     throw new IllegalArgumentException("Invalid or conflicting saved pile");
                  }

                  this.S.put(var7, new jn.w(var7, var9, var10, var11, var13, var22));
               } catch (RuntimeException var18) {
                  var20++;
                  this.x.add(var5.deepCopy());
               }
            }

            if (var20 > 0) {
               _q.R("FakeSpawner", "Retaining {} unmatched pile records for recovery; no inventory was restored", var20);
            }
         } catch (Exception var19) {
            _q.o("FakeSpawner", "Pile load failed; retaining the file for recovery", var19);
         }
      }
   }

   private void r() {
      if (this.Q != null) {
         this.B(n());
      }
   }

   private void B(Path var1) {
      if (this.Q != null) {
         try {
            JsonObject var2 = Files.exists(var1) ? ds.E(var1) : new JsonObject();
            JsonArray var3 = new JsonArray();
            if (var2.has(this.Q) && !var2.get(this.Q).isJsonArray()) {
               throw new IOException("Unrecognized saved pile data; original file retained");
            }

            if (var2.has("quarantined") && !var2.get("quarantined").isJsonObject()) {
               throw new IOException("Unrecognized pile quarantine data; original file retained");
            }

            JsonObject var4 = var2.has("quarantined") ? var2.getAsJsonObject("quarantined") : new JsonObject();
            if (var4.has(this.Q) && !var4.get(this.Q).isJsonArray()) {
               throw new IOException("Unrecognized retained pile records; original file retained");
            }

            JsonArray var5 = new JsonArray();

            for (JsonElement var7 : this.x) {
               var5.add(var7.deepCopy());
            }

            if (!var5.isEmpty()) {
               var4.add(this.Q, var5);
               var2.add("quarantined", var4);
            }

            for (jn.w var18 : this.S.values()) {
               var3.add(var18.toJson());
            }

            for (Entry var19 : this.Y.entrySet()) {
               long var8 = (Long)var19.getKey();
               if (!this.S.containsKey(var8) && (Integer)var19.getValue() >= 1) {
                  Deque var10 = this.T.get(var8);
                  if (var10 == null) {
                     var10 = new ArrayDeque();
                  }

                  if ((Integer)var19.getValue() <= 1 || !var10.isEmpty()) {
                     ArrayList var11 = new ArrayList();

                     for (jn.x var13 : var10) {
                        ItemStack var14 = this.h.get(var13.preferredSlot);
                        if (var14 == null || var14.isEmpty()) {
                           var11.clear();
                           break;
                        }

                        var11.add(
                           new jn.p(
                              var13.preferredSlot, BuiltInRegistries.ITEM.getKey(var13.stack.getItem()).toString(), var13.stack.getCount(), var14.getCount()
                           )
                        );
                     }

                     if (!var11.isEmpty() || (Integer)var19.getValue() == 1) {
                        HashMap var20 = new HashMap();
                        this.l.getOrDefault(var8, Map.of()).forEach((var1x, var2x) -> var20.put(BuiltInRegistries.ITEM.getKey(var1x).toString(), var2x));
                        var3.add(
                           new jn.w(
                                 var8,
                                 this.T(BlockPos.of(var8)),
                                 (Integer)var19.getValue(),
                                 this.j.getOrDefault(var8, System.currentTimeMillis()),
                                 var11,
                                 var20
                              )
                              .toJson()
                        );
                     }
                  }
               }
            }

            var2.add(this.Q, var3);
            Files.createDirectories(var1.getParent());
            ds.q(var1, var2.toString());
         } catch (Exception var15) {
            _q.o("FakeSpawner", "Pile save failed; previous file retained", var15);
         }
      }
   }

   public void z() {
      this.C = (boolean)1;
   }

   private void o(Minecraft var1) {
      if (this.C && !this.S.isEmpty()) {
         this.C = (boolean)0;
         byte var2 = 0;

         for (jn.w var4 : new ArrayList<>(this.S.values())) {
            BlockPos var5 = BlockPos.of(var4.pos);
            if (var1.level.hasChunkAt(var5)) {
               LevelChunk var6 = var1.level.getChunkAt(var5);
               int var7 = var6.getSectionIndex(var5.getY());
               BlockState var8 = var7 >= 0 && var7 < var6.getSections().length
                  ? var6.getSection(var7).getBlockState(var5.getX() & 15, var5.getY() & 15, var5.getZ() & 15)
                  : Blocks.AIR.defaultBlockState();
               if (this.B(var8.getBlock().asItem()) == var4.kind) {
                  HashMap var9 = new HashMap();

                  for (jn.p var11 : var4.backing) {
                     if ((var11.slot < 0 || var11.slot >= 36) && var11.slot != 40
                        || var11.amount < 1
                        || var11.reserved < 1
                        || var11.amount > var11.reserved + 1
                        || var11.reserved > 64
                        || var9.containsKey(var11.slot)
                        || this.G(var11.slot)) {
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
                        _8.j();

                        for (jn.p var13 : var4.backing) {
                           ItemStack var14 = (ItemStack)var9.get(var13.slot);
                           this.h.put(var13.slot, var14);
                           var16.addLast(new jn.x(var13.slot, var14.copyWithCount(Math.min(var13.amount, var13.reserved))));
                        }

                        this.T.put(var4.pos, var16);
                        this.Y.put(var4.pos, var4.count);
                        this.V.put(var4.pos, var4.kind);
                        this.j.put(var4.pos, var4.placedAt);
                        HashMap var18 = new HashMap();
                        var4.purchased.forEach((var1x, var2x) -> {
                           Identifier var3 = Identifier.tryParse(var1x);
                           if (var3 != null && BuiltInRegistries.ITEM.getValue(var3) != Items.AIR) {
                              var18.put((Item)BuiltInRegistries.ITEM.getValue(var3), var2x);
                           }
                        });
                        if (!var18.isEmpty()) {
                           this.l.put(var4.pos, var18);
                        }

                        this.S.remove(var4.pos);
                        this.w(var4.pos);
                        this.r(var1.level, var5);
                        this.J(var5);
                        _8.j();
                        var2 = 1;
                     }
                  }
               }
            }
         }

         if (var2 != 0) {
            this.r();
         }
      }
   }

   private jn() {
   }

   public static jn g() {
      return uK;
   }

   public boolean a() {
      FakeSpawnerModule var1 = FakeSpawnerModule.O();
      return var1 != null && var1.isEnabled() && var1.isRuntimeActive();
   }

   public boolean l(ItemStack var1) {
      return this.a() && this.a(var1);
   }

   public boolean a(ItemStack var1) {
      return this.M(var1) && (this.Z.contains(var1) || this.u4.contains(var1) || nn.O(var1));
   }

   private boolean M(ItemStack var1) {
      return var1 != null && !var1.isEmpty() && !nb.u(var1) && this.B(var1.getItem()) != null;
   }

   private String u() {
      FakeSpawnerModule var1 = FakeSpawnerModule.O();
      return var1.I(0) + "|" + var1.I(1) + "|" + var1.I(2);
   }

   public ni B(Item var1) {
      FakeSpawnerModule var2 = FakeSpawnerModule.O();

      for (ni var6 : ni.values()) {
         String var7 = var2.I(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1 == Y(var7)) {
            return var6;
         }
      }

      return null;
   }

   public ni T(BlockPos var1) {
      return var1 == null ? ni.X : this.V.getOrDefault(var1.asLong(), ni.X);
   }

   private Item i(long var1) {
      return Y(FakeSpawnerModule.O().I(this.T(BlockPos.of(var1)).ordinal()));
   }

   public Item C() {
      FakeSpawnerModule var1 = FakeSpawnerModule.O();
      return Y(var1 == null ? null : var1.B());
   }

   public static Item Y(String var0) {
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

   private Block A() {
      return this.C() instanceof BlockItem var1 ? var1.getBlock() : Blocks.COBBLESTONE;
   }

   public boolean c(BlockState var1) {
      return this.a() && var1 != null ? this.B(var1.getBlock().asItem()) != null || var1.is(Blocks.SPAWNER) : false;
   }

   public void K(BlockPos var1) {
      this.u();
      this.o.add(var1.asLong());
      this.Y.remove(var1.asLong());
      this.w(var1.asLong());
      this.J(var1);
   }

   public BlockState C(BlockPos var1, BlockState var2) {
      return var1 == null ? var2 : this.s(var1.asLong(), var2);
   }

   public BlockState s(long var1, BlockState var3) {
      if (!this.b.contains(var1) || !this.c() || var3 == null || this.i.contains(var1)) {
         return var3;
      }

      if (this.P.containsKey(var1)) {
         return Blocks.AIR.defaultBlockState();
      }

      if (this.o.contains(var1)) {
         return var3;
      }

      if (!this.H.contains(var1) || !var3.is(Blocks.SPAWNER) && !this.f.containsKey(var3.getBlock().asItem())) {
         int var4 = this.Y.getOrDefault(var1, 0);
         if (var4 <= 0) {
            return var3;
         } else {
            return !var3.isAir() && !var3.is(Blocks.SPAWNER) && !this.f.containsKey(var3.getBlock().asItem()) ? var3 : Blocks.SPAWNER.defaultBlockState();
         }
      } else {
         return Blocks.AIR.defaultBlockState();
      }
   }

   public boolean c() {
      return (!this.Y.isEmpty() || !this.H.isEmpty() || !this.P.isEmpty()) && this.a() && this.k == Minecraft.getInstance().level;
   }

   private void S() {
      LongOpenHashSet var1 = new LongOpenHashSet();

      for (long var3 : this.Y.keySet()) {
         var1.add(var3);
      }

      for (long var9 : this.H) {
         var1.add(var9);
      }

      for (long var10 : this.P.keySet()) {
         var1.add(var10);
      }

      this.b = LongSets.unmodifiable(var1);
      LongOpenHashSet var8 = new LongOpenHashSet();

      for (long var4 : this.J.keySet()) {
         var8.add(var4);
      }

      this.c = LongSets.unmodifiable(var8);
   }

   public void g(BlockPos var1, BlockState var2) {
      this.u();
      if (var1 != null) {
         long var3 = var1.asLong();
         if (this.z(var3)) {
            this.uy = 0;
            this.J(var1);
            _q.c("FakeSpawner", "Final break {} at {}; server state={}", var2 != null && var2.isAir() ? "confirmed" : "corrected", var1, var2);
         }

         if (this.o.contains(var3)) {
            if (var2 != null && !var2.isAir()) {
               return;
            }

            qb.E(var1);
            this.o.remove(var3);
         }

         if (this.H.contains(var3)) {
            if (var2 != null && this.c(var2)) {
               this.uy = 0;
            }

            this.L.put(var3, new jn.u(var1.immutable(), System.currentTimeMillis() + 10000L));
            this.H.remove(var3);
            this.J(var1);
         }

         int var5 = this.Y.getOrDefault(var3, 0);
         if (var2 != null && !var2.isAir()) {
            if (this.a()) {
               if (this.c(var2)) {
                  if (var5 <= 0 && !this.a.containsKey(var3)) {
                     return;
                  }

                  ni var6 = this.B(var2.getBlock().asItem());
                  if (var6 != null && this.V.put(var3, var6) != var6) {
                     this.w(var3);
                  }

                  this.j.putIfAbsent(var3, System.currentTimeMillis());
                  jn.o var7 = this.a.remove(var3);
                  int var8 = var7 == null ? this.Y.getOrDefault(var3, 1) : Math.max(1, var7.stackCount);
                  this.Y.put(var3, Math.max(1, var8));
                  this.S("track:" + var3, 1500L, "trackWorldState fake pos={} state={} resolvedCount={} pending={}", var1, var2.getBlock(), var8, var7 != null);
                  if (var7 != null && var7.hiddenStack != null) {
                     this.Z(var3, var7.hiddenStack, true);
                  }

                  if (var7 != null) {
                     this.r();
                  }

                  this.r(Minecraft.getInstance() == null ? null : Minecraft.getInstance().level, var1);
                  if (var5 <= 0) {
                     this.J(var1);
                  }
               } else {
                  this.g(var1, null);
               }
            }
         } else {
            if (var5 > 0) {
               this.W("trackWorldState removed pos={} previousCount={} state={}", var1, var5, var2);
            }

            if (var5 > 0 && !this.v(var1)) {
               this.B(var1);
            } else if (this.v(var1)) {
               this.N(var3, Integer.MAX_VALUE);
               _8.S();
               this.W("Placement rejected at {}; reservations released without inventory replay", var1);
            }

            this.j.remove(var3);
            this.l.remove(var3);
            this.Y.remove(var3);
            this.a.remove(var3);
            this.T.remove(var3);
            this.w(var3);
            if (var5 > 0) {
               this.J(var1);
            }

            this.t.remove(var3);
            this.B.remove(var3);
            this.R.remove(var3);
            this.A(var1);
            if (var1.equals(this.y)) {
               this.y = null;
            }
         }
      }
   }

   public void G(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.u();
      if (this.a() && var1 != null && this.a(var2)) {
         jn.w var5 = this.S.remove(var1.asLong());
         if (var5 != null) {
            this.x.add(var5.toJson());
            this.W("New placement at {} archives a superseded saved pile for recovery", var1);
         }

         this.V.put(var1.asLong(), this.B(var2.getItem()));
         ItemStack var6 = var4 ? var2.copy() : ItemStack.EMPTY;
         int var7 = var4 ? var2.getCount() : 1;
         this.a.put(var1.asLong(), new jn.o(var7, new jn.x(var3, var6), System.currentTimeMillis()));
      }
   }

   public void n(BlockPos var1, BlockState var2) {
      this.u();
      jn.o var3 = this.a.get(var1.asLong());
      if (var3 != null && this.c(var2)) {
         this.H.remove(var1.asLong());
         this.Y.put(var1.asLong(), var3.stackCount);
         this.j.putIfAbsent(var1.asLong(), var3.createdAtMs);
         this.r(this.k, var1);
         this.J(var1);
      }
   }

   public void F() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.a() && var1.player != null) {
         for (Entry var3 : new ArrayList<>(this.a.entrySet())) {
            if (this.Y.containsKey(var3.getKey())) {
               jn.o var4 = (jn.o)var3.getValue();
               if (var4.hiddenStack != null && !var4.hiddenStack.stack.isEmpty()) {
                  this.Z((Long)var3.getKey(), var4.hiddenStack, true);
               }

               this.Y.put((Long)var3.getKey(), var4.stackCount);
            }
         }
      }
   }

   public void Z() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.isSameThread() && var1.player != null && this.a() && var1.level == this.k && var1.player == this.un && !q7.M()) {
         this.f(var1.player.getInventory());
         this.E
            .entrySet()
            .removeIf(
               var1x -> {
                  ItemStack var2 = var1.player.getInventory().getItem(var1x.getKey());
                  jn.a var3x = var1x.getValue();
                  return var1.player.tickCount >= var3x.expires
                     || !var2.isEmpty() && !var2.is(var3x.stack.getItem())
                     || var2.is(var3x.stack.getItem()) && var2.getCount() >= var3x.expected;
               }
            );

         for (Entry var3 : this.h.entrySet()) {
            if (!this.a.values().stream().anyMatch(var1x -> var1x.hiddenStack != null && var1x.hiddenStack.preferredSlot == (Integer)var3.getKey())) {
               ItemStack var4 = var1.player.getInventory().getItem((Integer)var3.getKey());
               if (var4.is(((ItemStack)var3.getValue()).getItem())
                  && var4.getCount() > ((ItemStack)var3.getValue()).getCount()
                  && this.z((Integer)var3.getKey(), var4.getCount() - ((ItemStack)var3.getValue()).getCount())) {
                  break;
               }
            }
         }
      }
   }

   void f(Container var1) {
      Iterator var2 = this.h.entrySet().iterator();

      while (var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         int var4 = (Integer)var3.getKey();
         ItemStack var5 = var1.getItem(var4);
         ItemStack var6 = (ItemStack)var3.getValue();
         if (var5.is(var6.getItem()) && var5.getCount() >= var6.getCount()) {
            this.R(var4, var5);
         } else {
            int var7 = var5.is(var6.getItem()) ? var5.getCount() : 0;
            int var8 = var6.getCount() - var7;
            if (var7 == 0) {
               var2.remove();
            } else {
               var3.setValue(var5.copyWithCount(var7));
            }

            for (Entry var10 : this.T.entrySet()) {
               ArrayDeque var11 = new ArrayDeque();

               for (jn.x var13 : (Deque)var10.getValue()) {
                  int var14 = var13.preferredSlot == var4 ? Math.min(var8, var13.stack.getCount()) : 0;
                  var8 -= var14;
                  if (var14 > 0) {
                     this.Y.computeIfPresent((Long)var10.getKey(), (var1x, var2x) -> Math.max(1, var2x - var14));
                  }

                  if (var13.stack.getCount() > var14) {
                     var11.addLast(new jn.x(var13.preferredSlot, var13.stack.copyWithCount(var13.stack.getCount() - var14)));
                  }
               }

               ((Deque)var10.getValue()).clear();
               ((Deque)var10.getValue()).addAll(var11);
            }

            _q.c(
               "FakeSpawner",
               "Reservation corrected physical={} item={} old={} available={}; real inventory retained",
               var4,
               BuiltInRegistries.ITEM.getKey(var6.getItem()),
               var6.getCount(),
               var7
            );
            _8.S();
         }
      }
   }

   private boolean z(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      int var4 = this.o();
      return var4 >= 0 && var3.player != null
         ? q7.B("separate reservation surplus", q7.I(var1, var4, var2, var3.player.getInventory().getItem(var1).getCount()), _8::S, _8::S)
         : false;
   }

   public boolean v(BlockPos var1) {
      return var1 != null && this.a.containsKey(var1.asLong());
   }

   public boolean c(BlockPos var1) {
      return this.a() && this.k == Minecraft.getInstance().level && var1 != null && this.H.contains(var1.asLong());
   }

   public void A(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 != null) {
         this.D = var1;
         this.v = var2.getInventory().getSelectedSlot();
         this.ux = var2.tickCount + 4;
      }
   }

   public boolean j(InteractionHand var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      return this.a()
         && var2 == this.un
         && var1 == this.D
         && var2.tickCount <= this.ux
         && var2.getInventory().getSelectedSlot() == this.v
         && this.a(var2.getItemInHand(var1));
   }

   public void o(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.a() && var2.level != null && var2.level.getBlockState(var1).isAir()) {
         if (this.J.put(var1.asLong(), var2.level.getGameTime() + 10L) == null) {
            this.J(var1);
         }
      }
   }

   public BlockState A(BlockPos var1, BlockState var2) {
      return this.q(var1.asLong(), var2);
   }

   public BlockState q(long var1, BlockState var3) {
      return this.c.contains(var1) && var3.isAir() && this.a() && this.k == Minecraft.getInstance().level ? Blocks.SPAWNER.defaultBlockState() : var3;
   }

   public boolean Z(BlockPos var1, ItemStack var2, int var3, boolean var4) {
      this.u();
      if (this.a() && var1 != null && !this.v(var1) && this.N(var1) > 0 && this.a(var2) && this.B(var2.getItem()) == this.T(var1) && !this.G(var3) && !q7.M()) {
         long var5 = var1.asLong();
         int var7 = var4 ? var2.getCount() : 1;
         if (var7 < var2.getCount()) {
            int var8 = this.o();
            if (var8 < 0) {
               return false;
            }

            Item var9 = var2.getItem();
            return q7.B("single pile addition", q7.I(var3, var8, var7, var2.getCount()), () -> {
               Minecraft var6 = Minecraft.getInstance();
               ItemStack var7x = var6.player.getInventory().getItem(var8);
               if (this.Y.containsKey(var5) && var7x.is(var9) && var7x.getCount() == var7) {
                  this.b(var5, var8, var7x, var7);
               }
            }, _8::S);
         } else {
            return this.b(var5, var3, var2, var7);
         }
      } else {
         return false;
      }
   }

   private boolean b(long var1, int var3, ItemStack var4, int var5) {
      if (!this.Z(var1, new jn.x(var3, var4.copy()), false)) {
         return false;
      }

      int var6 = (int)Math.min(2147483647L, (long)this.Y.getOrDefault(var1, 1).intValue() + var5);
      this.Y.put(var1, var6);
      this.j.putIfAbsent(var1, System.currentTimeMillis());
      this.y(Minecraft.getInstance().level, BlockPos.of(var1));
      this.Q("You just added another spawner! There are now " + var6 + " spawners in the stack.");
      this.r();
      return true;
   }

   public boolean M() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null && var1.getMainHandItem().is(ItemTags.PICKAXES)) {
         ItemEnchantments var2 = (ItemEnchantments)var1.getMainHandItem().get(DataComponents.ENCHANTMENTS);
         Reference var3 = var1.level().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
         return var2 != null && var2.getLevel(var3) > 0;
      } else {
         return false;
      }
   }

   public boolean p(BlockPos var1, Direction var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.x(var1)) {
         return false;
      }

      long var4 = var1.asLong();
      if (this.P.containsKey(var4)) {
         return true;
      }

      if (this.M()) {
         this.w.remove(var4);
         this.R.remove(var4);
         this.ud.remove(var4);
         return false;
      }

      if (this.ud.contains(var4)) {
         return true;
      }

      int var6 = var3.player.tickCount;
      long var7 = var1.asLong();
      if (this.M.getOrDefault(var7, -1) == var6) {
         return true;
      }

      this.M.put(var7, var6);
      this.w.computeIfAbsent(var4, var1x -> var6);
      double var9 = var3.player.getAbilities().instabuild ? 1.0 : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
      double var11 = Math.min(1.0, this.R.getOrDefault(var4, 0.0) + var9);
      this.R.put(var4, var11);
      this.o(var3, var1, var2, var11);
      if (var11 >= 1.0) {
         this.w.remove(var4);
         this.R.remove(var4);
         this.ud.add(var4);
         this.P.put(var4, var6 + 4);
         this.A(var1);
         this.n(var1, var2);
         this.d(var1, true);
         this.J(var1);
      }

      return true;
   }

   private void y() {
      Minecraft var1 = Minecraft.getInstance();

      for (Entry var3 : new ArrayList<>(this.P.entrySet())) {
         if (var1.player.tickCount >= (Integer)var3.getValue()) {
            this.P.remove(var3.getKey());
            this.J(BlockPos.of((Long)var3.getKey()));
            this.Q("You cannot break spawners without a silk touch!");
         }
      }

      if (!var1.options.keyAttack.isDown()) {
         for (long var6 : this.w.keySet()) {
            this.A(BlockPos.of(var6));
         }

         this.w.clear();
         this.R.clear();
         this.ud.clear();
      } else {
         this.w.keySet().removeIf(var2 -> {
            int var3x = var1.hitResult instanceof BlockHitResult var4 && var4.getBlockPos().asLong() == var2 ? 0 : 1;
            if (var3x != 0) {
               this.A(BlockPos.of(var2));
            }

            return (boolean)var3x;
         });
      }
   }

   public boolean O(BlockPos var1) {
      if (this.p(var1, null)) {
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      if (this.a() && var2 != null && var2.level != null && var2.player != null && var1 != null) {
         if (this.c(var2.level.getBlockState(var1)) && this.N(var1) > this.k()) {
            long var3 = var1.asLong();
            this.t.putIfAbsent(var3, 0.0F);
            this.o(var2, var1, null, 0.0);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean A(BlockPos var1, Direction var2) {
      if (this.p(var1, var2)) {
         return true;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.a() && var3 != null && var3.level != null && var3.player != null && var1 != null) {
         if (this.x(var1) && var3.player.tickCount < this.uy) {
            return true;
         }

         if (this.c(var3.level.getBlockState(var1)) && this.N(var1) > this.k()) {
            long var4 = var1.asLong();
            int var6 = var3.player.tickCount;
            if (this.M.getOrDefault(var4, -1) == var6) {
               return true;
            }

            this.M.put(var4, var6);
            float var7 = var3.player.getAbilities().instabuild ? 1.0F : Blocks.SPAWNER.defaultBlockState().getDestroyProgress(var3.player, var3.level, var1);
            float var8 = Math.min(1.0F, this.t.getOrDefault(var4, 0.0F) + var7);
            this.t.put(var4, var8);
            this.o(var3, var1, var2, var8);
            if (var8 >= 1.0F) {
               if (this.U(var1, this.k())) {
                  this.uy = var6 + 5;
                  this.d(var1, true);
               }

               this.t.remove(var4);
               this.B.remove(var4);
               this.A(var1);
            }

            return true;
         } else {
            if (var1 != null) {
               this.t.remove(var1.asLong());
               this.B.remove(var1.asLong());
               this.A(var1);
            }

            return false;
         }
      } else {
         if (var1 != null) {
            this.t.remove(var1.asLong());
            this.B.remove(var1.asLong());
            this.A(var1);
         }

         return false;
      }
   }

   public void B(BlockPos var1) {
      if (var1 != null && this.o.contains(var1.asLong())) {
         qb.E(var1);
      }

      if (var1 != null && this.H.contains(var1.asLong())) {
         this.L.put(var1.asLong(), new jn.u(var1.immutable(), System.currentTimeMillis() + 10000L));
      }

      if (var1 != null && this.N(var1) > 0) {
         this.L.put(var1.asLong(), new jn.u(var1.immutable(), System.currentTimeMillis() + 10000L));
         int var2 = this.Y.getOrDefault(var1.asLong(), 0);
         jn.x var3 = this.N(var1.asLong(), var2);
         this.I(var1.asLong(), var2);
         this.q(new ItemStack(this.i(var1.asLong()), 1), 1);
         _8.S();
         this.t.remove(var1.asLong());
         this.B.remove(var1.asLong());
         this.A(var1);
      }
   }

   private int k() {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player == null || !var1.player.isShiftKeyDown() && !var1.options.keyShift.isDown() ? 1 : 64;
   }

   public boolean E(BlockPos var1) {
      return this.U(var1, 64);
   }

   private boolean U(BlockPos var1, int var2) {
      if (this.v(var1)) {
         return false;
      }

      if (this.x(var1)) {
         long var3 = var1.asLong();
         int var5 = this.Y.getOrDefault(var3, 0);
         if (var5 <= 0) {
            return false;
         }

         int var6 = Math.min(var2, var5);
         Deque var7 = this.T.get(var3);
         if (var7 != null && !var7.isEmpty()) {
            ItemStack var8 = Minecraft.getInstance().player.getInventory().getItem(((jn.x)var7.getLast()).preferredSlot);
            int var9 = 0;

            for (jn.x var11 : var7.reversed()) {
               if (!ItemStack.isSameItemSameComponents(var8, Minecraft.getInstance().player.getInventory().getItem(var11.preferredSlot))) {
                  break;
               }

               var9 += var11.stack.getCount();
            }

            if (var9 < var6 && var9 < var5 - 1) {
               var6 = var9;
            }
         }

         if (!this.g(var3, var6)) {
            return false;
         }

         this.I(var3, var6);
         return true;
      } else {
         return false;
      }
   }

   private void I(long var1, int var3) {
      int var4 = Math.max(0, this.Y.getOrDefault(var1, 0) - var3);
      BlockPos var5 = BlockPos.of(var1);
      if (var4 > 0) {
         this.Y.put(var1, var4);
         this.j.put(var1, System.currentTimeMillis());
      } else {
         this.H.add(var1);
         this.Y.remove(var1);
         this.a.remove(var1);
         this.j.remove(var1);
         this.l.remove(var1);
         this.T.remove(var1);
         this.w(var1);
         this.J(var5);
         if (var5.equals(this.y)) {
            this.y = null;
         }
      }

      this.L.put(var1, new jn.u(var5.immutable(), System.currentTimeMillis() + 10000L));
      this.r();
   }

   public boolean x(BlockPos var1) {
      if (var1 == null) {
         return false;
      }

      long var2 = var1.asLong();
      return this.Y.getOrDefault(var2, 0) > 0 && !this.i.contains(var2) && this.k == Minecraft.getInstance().level && this.a();
   }

   public jn.m J(BlockPos var1, SoundEvent var2, float var3, float var4) {
      if (var1 != null && var2 != null && this.x(var1)) {
         BlockState var5 = ((BlockItem)this.i(var1.asLong())).getBlock().defaultBlockState();
         if (var5 == null) {
            return null;
         } else {
            SoundType var6 = var5.getSoundType();
            SoundType var7 = Blocks.SPAWNER.defaultBlockState().getSoundType();
            if (A(var2, var6.getPlaceSound())) {
               return new jn.m(var7.getPlaceSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else if (A(var2, var6.getBreakSound())) {
               return new jn.m(var7.getBreakSound(), (var7.getVolume() + 1.0F) / 2.0F, var7.getPitch() * 0.8F);
            } else {
               return A(var2, var6.getHitSound()) ? new jn.m(var7.getHitSound(), (var7.getVolume() + 1.0F) / 8.0F, var7.getPitch() * 0.5F) : null;
            }
         }
      } else {
         return null;
      }
   }

   private static boolean A(SoundEvent var0, SoundEvent var1) {
      return var0 == var1 || var0.location().equals(var1.location());
   }

   private void y(Level var1, BlockPos var2) {
      if (var1 != null && var2 != null && this.x(var2)) {
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

   private void d(BlockPos var1, boolean var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null && this.x(var1)) {
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

   public BlockState g(BlockPos var1, BlockState var2) {
      return this.C(var1, var2);
   }

   public SpawnerBlockEntity r(Level var1, BlockPos var2) {
      Map var3 = this.d;
      if (var1 != null && var1 == this.k && this.x(var2) && !this.P.containsKey(var2.asLong())) {
         long var4 = var2.asLong();
         SpawnerBlockEntity var6 = (SpawnerBlockEntity)var3.get(var4);
         if (var6 == null && Minecraft.getInstance().isSameThread()) {
            var6 = new SpawnerBlockEntity(var2.immutable(), Blocks.SPAWNER.defaultBlockState());
            var6.setEntityId(this.T(var2).D, var1.getRandom());
            var6.setLevel(var1);
            var6.getSpawner().getOrCreateDisplayEntity(var1, var2);
            var3.put(var4, var6);
            ((List)this.r.computeIfAbsent(ChunkPos.pack(var2.getX() >> 4, var2.getZ() >> 4), var0 -> new ArrayList())).add(var6);
         }

         return var3 == this.d && var1 == this.k && this.x(var2) ? var6 : null;
      } else {
         return null;
      }
   }

   private void J(BlockPos var1) {
      this.S();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var2.level == this.k) {
         this.p++;
         var2.levelExtractor.setBlocksDirty(var1.getX() - 1, var1.getY() - 1, var1.getZ() - 1, var1.getX() + 1, var1.getY() + 1, var1.getZ() + 1);
      }
   }

   public Collection<SpawnerBlockEntity> n(Level var1) {
      return var1 == this.k && this.a() ? this.d.values() : List.of();
   }

   private void w(long var1) {
      SpawnerBlockEntity var3 = this.d.remove(var1);
      if (var3 != null) {
         BlockPos var4 = var3.getBlockPos();
         long var5 = ChunkPos.pack(var4.getX() >> 4, var4.getZ() >> 4);
         List var7 = (List)this.r.get(var5);
         if (var7 != null) {
            var7.remove(var3);
            if (var7.isEmpty()) {
               this.r.remove(var5);
            }
         }
      }
   }

   public Collection<SpawnerBlockEntity> g(Level var1, Vec3 var2, int var3) {
      if (var1 == this.k && this.a() && !this.r.isEmpty()) {
         ArrayList var4 = null;
         int var5 = Mth.floor(var2.x - var3) >> 4;
         int var6 = Mth.floor(var2.x + var3) >> 4;
         int var7 = Mth.floor(var2.z - var3) >> 4;
         int var8 = Mth.floor(var2.z + var3) >> 4;

         for (int var9 = var5; var9 <= var6; var9++) {
            for (int var10 = var7; var10 <= var8; var10++) {
               List var11 = (List)this.r.get(ChunkPos.pack(var9, var10));
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

   public boolean b(BlockEntity var1) {
      if (var1 instanceof SpawnerBlockEntity var2) {
         BlockPos var3 = var2.getBlockPos();
         if (var3 == null) {
            return false;
         }

         SpawnerBlockEntity var4 = this.d.get(var3.asLong());
         return var4 == var1;
      } else {
         return false;
      }
   }

   public void v(String var1, BlockPos var2, BlockEntity var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 != null) {
            this.S(
               "request:" + var1 + ":" + var2.asLong(),
               1500L,
               "{} requested fake block entity pos={} result={} count={}",
               var1,
               var2,
               var3 != null ? var3.getClass().getSimpleName() : "null",
               this.N(var2)
            );
         }
      }
   }

   public void x(String var1, BlockPos var2, String var3) {
      if (Boolean.getBoolean("larp.debug")) {
         if (var2 == null) {
            this.S("render:" + var1, 1500L, "renderer {} {}", var1, var3);
         } else {
            this.S("render:" + var1 + ":" + var2.asLong(), 1500L, "renderer {} pos={} count={} {}", var1, var2, this.N(var2), var3);
         }
      }
   }

   public void l(LevelChunk var1, Collection<BlockPos> var2) {
      this.u();
      this.z();
      if (this.a() && var1 != null) {
         int var3 = var1.getPos().x();
         int var4 = var1.getPos().z();
         int var5 = var3 << 4;
         int var6 = var4 << 4;
         int var7 = var5 + 15;
         int var8 = var6 + 15;
         MutableBlockPos var9 = new MutableBlockPos();
         HashSet var10 = new HashSet<>(this.Y.keySet());
         var10.addAll(this.H);

         for (Long var12 : var10) {
            BlockPos var13 = BlockPos.of(var12);
            int var14 = var13.getX();
            int var15 = var13.getZ();
            if (var14 >= var5 && var14 <= var7 && var15 >= var6 && var15 <= var8) {
               var9.set(var14, var13.getY(), var15);
               if (!var1.getLevel().isOutsideBuildHeight(var9)) {
                  this.g(var9, var1.getSection(var1.getSectionIndex(var9.getY())).getBlockState(var9.getX() & 15, var9.getY() & 15, var9.getZ() & 15));
               }
            }
         }

         if (var2 != null) {
            for (BlockPos var17 : new ArrayList(var2)) {
               if (var17 != null) {
                  this.g(var17, var1.getBlockState(var17));
               }
            }
         }
      }
   }

   public void N() {
      long var1 = Boolean.getBoolean("larp.debug") ? System.nanoTime() : 0L;
      this.u();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var3.player != null && this.a()) {
         this.o(var3);
         this.f();

         for (Entry var5 : this.J.entrySet()) {
            if (var3.level.getGameTime() >= (Long)var5.getValue()) {
               this.J.remove(var5.getKey());
               this.J(BlockPos.of((Long)var5.getKey()));
            }
         }

         this.y();
         if (!this.L.isEmpty() || !this.t.isEmpty() || !this.H.isEmpty()) {
            for (Entity var6 : var3.level.entitiesForRendering()) {
               if (var6 instanceof ItemEntity var8) {
                  this.c(var8);
               }
            }
         }

         this.K();
         this.L.entrySet().removeIf(var0 -> var0.getValue().untilMs < System.currentTimeMillis());
         this.O.removeIf(var1x -> !(var3.level.getEntity(var1x) instanceof ItemEntity));
         q7.h();
         this.Z();

         for (Entry var17 : new ArrayList<>(this.a.entrySet())) {
            if (!this.Y.containsKey(var17.getKey()) && System.currentTimeMillis() - ((jn.o)var17.getValue()).createdAtMs > 5000L) {
               this.g(BlockPos.of((Long)var17.getKey()), null);
            }
         }

         if (!var3.options.keyAttack.isDown()) {
            for (Long var18 : new ArrayList<>(this.t.keySet())) {
               this.A(BlockPos.of(var18));
            }

            this.t.clear();
            this.B.clear();
         }

         for (Long var19 : new ArrayList<>(this.t.keySet())) {
            if (!(var3.hitResult instanceof BlockHitResult var22 && var22.getBlockPos().equals(BlockPos.of(var19)))) {
               this.A(BlockPos.of(var19));
               this.t.remove(var19);
               this.B.remove(var19);
            }
         }

         if (var3 != null && var3.level != null && var3.player != null && this.a()) {
            for (BlockEntity var21 : this.g(var3.level, var3.player.position(), 32)) {
               if (var21 instanceof SpawnerBlockEntity var23
                  && var23.getBlockPos() != null
                  && var3.level.hasChunkAt(var23.getBlockPos())
                  && this.x(var23.getBlockPos())
                  && var23.getBlockPos().distToCenterSqr(var3.player.position()) <= 1024.0) {
                  SpawnerBlockEntity.clientTick(var3.level, var23.getBlockPos(), var23.getBlockState(), var23);
               }
            }

            long var14 = var3.level.getGameTime();

            for (BlockPos var26 : this.N(var3.player.blockPosition(), 16, 8)) {
               if ((var14 + Math.floorMod(var26.asLong(), 3L)) % 20L == 0L) {
                  this.N(var3.level, var26);
               }
            }
         } else {
            for (Long var20 : new ArrayList<>(this.t.keySet())) {
               this.A(BlockPos.of(var20));
            }

            this.t.clear();
            this.B.clear();
         }

         if (var1 != 0L) {
            long var15 = System.nanoTime() - var1;
            long var25 = System.currentTimeMillis();
            if (var25 >= this.z) {
               this.z = var25 + 5000L;
               this.W(
                  "Work counters: piles={} reservations={} mappingRebuilds={} terrainInvalidations={} inventoryUpdates={} renderCandidates={} renderExtractions={} displayCopies={} tickMicros={}",
                  this.Y.size(),
                  this.h.size(),
                  _8.D(),
                  this.p,
                  this.U,
                  this.e,
                  this.g,
                  this.q,
                  var15 / 1000L
               );
            }

            if (var15 > 5000000L) {
               this.S("tick-spike", 2000L, "Client tick CPU spike micros={} piles={} reservations={}", var15 / 1000L, this.Y.size(), this.h.size());
            }
         }
      } else {
         this.Q();
      }
   }

   public int N(BlockPos var1) {
      return !this.x(var1) ? 0 : this.Y.getOrDefault(var1.asLong(), 0);
   }

   public List<BlockPos> N(BlockPos var1, int var2, int var3) {
      ArrayList var4 = new ArrayList();
      if (this.a() && var1 != null && !this.Y.isEmpty()) {
         int var5 = var1.getX();
         int var6 = var1.getY();
         int var7 = var1.getZ();

         for (SpawnerBlockEntity var9 : this.g(this.k, Vec3.atCenterOf(var1), var2 + 1)) {
            BlockPos var10 = var9.getBlockPos();
            if (this.x(var10) && Math.abs(var10.getX() - var5) <= var2 && Math.abs(var10.getY() - var6) <= var3 && Math.abs(var10.getZ() - var7) <= var2) {
               var4.add(var10);
            }
         }

         return var4;
      } else {
         return var4;
      }
   }

   public boolean c(ItemEntity var1) {
      if (this.a() && var1 != null && this.M(var1.getItem()) && !nn.i(var1.getItem())) {
         long var2 = System.currentTimeMillis();
         if (this.O.contains(var1.getId())) {
            return true;
         }

         HashSet var4 = new HashSet<>(this.H);
         var4.addAll(this.t.keySet());

         for (long var6 : var4) {
            BlockPos var8 = BlockPos.of(var6);
            if (var1.distanceToSqr(var8.getX() + 0.5, var8.getY() + 0.5, var8.getZ() + 0.5) <= 4.84) {
               this.O.add(var1.getId());
               return true;
            }
         }

         this.L.entrySet().removeIf(var2x -> var2x.getValue().untilMs < var2);

         for (jn.u var14 : this.L.values()) {
            double var7 = var14.pos.getX() + 0.5 - var1.getX();
            double var9 = var14.pos.getY() + 0.5 - var1.getY();
            double var11 = var14.pos.getZ() + 0.5 - var1.getZ();
            if (var7 * var7 + var9 * var9 + var11 * var11 <= 4.840000000000001) {
               this.O.add(var1.getId());
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   public boolean a(ItemEntity var1) {
      return this.c(var1);
   }

   public ItemStack w(ItemStack var1) {
      if (!this.l(var1)) {
         return var1;
      }

      ItemStack var2 = new ItemStack(Items.SPAWNER, var1.getCount());
      var2.set(DataComponents.CUSTOM_NAME, this.h(var1));
      var2.set(DataComponents.LORE, new ItemLore(List.of(u(this.B(var1.getItem()).N, ChatFormatting.YELLOW))));
      if (this.N()) {
         var2.set(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true);
      }

      return var2;
   }

   public ItemStack r(ItemStack var1) {
      return var1;
   }

   public boolean k(ItemStack var1) {
      return this.a() && var1 != null && !var1.isEmpty() && !nb.u(var1) && this.y(var1) && this.Z(var1) > 0L;
   }

   public Component r(long var1) {
      return u("~", ChatFormatting.DARK_GRAY).append(n("$ ", 65280)).append(u(this.K(var1), ChatFormatting.WHITE));
   }

   public Component n(ItemStack var1) {
      return this.y(this.A(var1));
   }

   public Component o(ItemStack var1) {
      long var2 = this.Z(var1);
      long var4 = Math.max(1L, (var2 + 63L) / 64L);
      return u("Contains: " + var4 + " stack(s)", ChatFormatting.WHITE);
   }

   public Component C() {
      return u("Left Shift", ChatFormatting.GOLD).append(u(": ", ChatFormatting.GOLD)).append(u("view contents", ChatFormatting.WHITE));
   }

   public Component N() {
      return u("Left Alt + Left Shift", ChatFormatting.GOLD).append(u(": ", ChatFormatting.GOLD)).append(u("view contents", ChatFormatting.WHITE));
   }

   public Component k() {
      return u("Left Control", ChatFormatting.GOLD).append(u(": ", ChatFormatting.GOLD)).append(u("lock tooltip", ChatFormatting.WHITE));
   }

   public List<Component> I(ItemStack var1, boolean var2) {
      ArrayList var3 = new ArrayList();
      var3.add(var1.getHoverName());
      var3.add(this.n(var1));
      if (!var2) {
         return var3;
      } else {
         boolean var4 = this.u(341);
         boolean var5 = this.u(340);
         boolean var6 = this.u(342);
         if (var4) {
            return var3;
         } else if (var5 && var6) {
            var3.add(this.k());
            return var3;
         } else if (var5) {
            var3.add(this.o(var1));
            var3.add(this.N());
            var3.add(this.k());
            return var3;
         } else {
            var3.add(this.o(var1));
            var3.add(this.C());
            return var3;
         }
      }
   }

   public Component W() {
      FakeSpawnerModule var1 = FakeSpawnerModule.O();
      String var2 = var1 != null ? var1.Q() : "§dSpawner";
      return F(var2);
   }

   public Component h(ItemStack var1) {
      Component var2 = (Component)var1.get(DataComponents.CUSTOM_NAME);
      if (var2 != null) {
         return Component.literal(var2.getString()).withStyle(new ChatFormatting[]{ChatFormatting.WHITE, ChatFormatting.ITALIC});
      }

      ni var3 = this.B(var1.getItem());
      return u("Spawner", ChatFormatting.LIGHT_PURPLE);
   }

   public List<Component> G(int var1, boolean var2, boolean var3) {
      return this.G(new ItemStack(this.C(), var1), var2, var3);
   }

   public List<Component> G(ItemStack var1, boolean var2, boolean var3) {
      int var4 = var1.getCount();
      ArrayList var5 = new ArrayList();
      var5.add(u(this.B(var1.getItem()).N, ChatFormatting.YELLOW));
      if (var2) {
         var5.add(u("~", ChatFormatting.DARK_GRAY).append(n("$ ", 65280)).append(u(this.b(var4), ChatFormatting.WHITE)));
      }

      var5.add(Component.empty());
      var5.add(u("Interact with Spawn Egg:", ChatFormatting.GRAY));
      var5.add(u(" Sets Mob Type", ChatFormatting.BLUE));
      if (var3) {
         var5.add(u("minecraft:spawner", ChatFormatting.DARK_GRAY));
         var5.add(u("13 component(s)", ChatFormatting.DARK_GRAY));
      }

      return var5;
   }

   public boolean K() {
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

   public boolean N() {
      FakeSpawnerModule var1 = FakeSpawnerModule.O();
      return var1 != null && var1.m();
   }

   public void B() {
      this.d(this.y);
   }

   public void d(BlockPos var1) {
      this.u();
      Minecraft var2 = Minecraft.getInstance();
      if (this.a() && var2 != null && var2.level != null && var2.player != null) {
         this.y = var1 == null ? null : var1.immutable();
         long var3 = this.k(var1);
         int var5 = this.o(var1);
         ni var6 = this.T(var1);
         jn.c var7 = this.S(var3, var5, var6);
         Map var8 = var1 == null ? Map.of() : this.l.getOrDefault(var1.asLong(), Map.of());
         var7 = new jn.c(
            Math.max(0, var7.y - var8.getOrDefault(var6.S, 0) - _v.P(var6.S)),
            Math.max(0, var7.S - var8.getOrDefault(var6.W, 0) - _v.P(var6.W)),
            var7.w,
            var7.B
         );
         String var9 = var5 + " " + p(var6.N.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"));
         SimpleContainer var10 = new SimpleContainer(54);
         int var11 = 0;
         var11 = i(var10, var11, var6.S, var7.y);
         i(var10, var11, var6.W, var7.S);
         var10.setItem(
            48,
            P(
               Items.GOLD_INGOT.getDefaultInstance(),
               u("Sell All", ChatFormatting.WHITE),
               List.of(u("Click to sell all loot", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         var10.setItem(49, P(var6.D(), y(var5 + " " + var6.N.toLowerCase(Locale.ROOT) + " spawner" + (var5 == 1 ? "" : "s"), 64648), this.W(var7, var6)));
         var10.setItem(
            50,
            P(
               Items.DROPPER.getDefaultInstance(),
               u("Drop Loot", ChatFormatting.WHITE),
               List.of(u("Click to drop all loot on the page", ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC))
            )
         );
         ChestMenu var12 = new ChestMenu(MenuType.GENERIC_9x6, 0, var2.player.getInventory(), var10, 6);
         var2.gui.setScreen(new w2(var12, var2.player.getInventory(), Component.literal(var9), var1));
      }
   }

   public void r(BlockPos var1, Item var2, int var3) {
      if (var1 != null && var2 != null && var3 > 0) {
         this.l.computeIfAbsent(var1.asLong(), var0 -> new HashMap<>()).merge(var2, var3, Integer::sum);
      }
   }

   public long A(ItemStack var1) {
      if (!this.a() || var1.isEmpty() || nb.u(var1)) {
         return 0L;
      }

      if (this.M(var1)) {
         return var1.getCount() * 1000L;
      }

      if (ni.u(var1.getItem())) {
         return this.v(var1.getItem(), var1.getCount());
      }

      ItemContainerContents var2 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
      return var2 == null ? 0L : var2.nonEmptyItemCopyStream().mapToLong(this::A).sum() * var1.getCount();
   }

   public Component y(long var1) {
      double var3 = var1 >= 1000000000L ? 1.0E9 : (var1 >= 1000000L ? 1000000.0 : (var1 >= 1000L ? 1000.0 : 1.0));
      String var5 = var3 == 1.0E9 ? "B" : (var3 == 1000000.0 ? "M" : (var3 == 1000.0 ? "K" : ""));
      return u("~", ChatFormatting.DARK_GRAY).append(n("$ ", 65280)).append(u(I(var1 / var3, var3 == 1.0 ? 0 : 1) + var5, ChatFormatting.WHITE));
   }

   public long f(int var1) {
      return this.v(Items.BONE, var1);
   }

   public long v(Item var1, int var2) {
      Double var3 = FakeSpawnerModule.O().Q(BuiltInRegistries.ITEM.getKey(var1).toString());
      if (var3 != null) {
         return Math.round(var3 * Math.max(0, var2));
      }

      String var4 = BuiltInRegistries.ITEM.getKey(var1).getPath().replace('_', ' ');
      h0 var5 = h0.u();
      var5.C(var4, var0 -> {});
      double var6 = this.W.getOrDefault(var1, (double)var5.m(var4));
      return Math.round(var6 * Math.max(0, var2));
   }

   public void K() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gui.screen() != null && var1.gui.screen().getTitle().getString().toLowerCase(Locale.ROOT).contains("order")) {
         AbstractContainerMenu var2 = var1.player.containerMenu;
         HashMap var3 = new HashMap();

         for (Slot var5 : var2.slots) {
            if (var5.container != var1.player.getInventory()) {
               ItemStack var6 = var5.getItem();
               if (ni.u(var6.getItem())) {
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

         var3.forEach((var1x, var2x) -> this.W.merge(var1x, var2x, Math::max));
      }
   }

   public int I() {
      return this.b(Items.BONE);
   }

   private boolean q(ItemStack var1) {
      if (var1.has(DataComponents.CUSTOM_NAME)) {
         return false;
      }

      FakeSpawnerModule var2 = FakeSpawnerModule.O();

      for (ni var6 : ni.values()) {
         String var7 = var2.I(var6.ordinal());
         if (var7 != null && !var7.isBlank() && var1.is(var6.S)) {
            return true;
         }
      }

      return false;
   }

   private int b(Item var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.a() && var2.player != null) {
         int var3 = -1;
         int var4 = 0;

         for (int var5 = 0; var5 < 36; var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6.is(var1) && !var6.has(DataComponents.CUSTOM_NAME) && !ox.F(var6) && var6.getCount() > var4) {
               var3 = var5;
               var4 = var6.getCount();
            }
         }

         return this.G = var3;
      } else {
         return -1;
      }
   }

   public void f(int var1) {
   }

   public void y(AbstractContainerMenu var1, Slot var2) {
   }

   public void f(Slot var1) {
   }

   public void a(BlockPos var1) {
      if (var1 != null && this.m(var1.asLong())) {
         this.A(var1);
         this.J(var1);
         _q.c("FakeSpawner", "Final break predicted at {}; retaining pile and reservations until server acknowledgment", var1);
      }
   }

   boolean m(long var1) {
      return this.Y.getOrDefault(var1, 0) > 0 && this.i.add(var1);
   }

   boolean z(long var1) {
      return this.i.remove(var1);
   }

   public BlockState h(Level var1, BlockPos var2, BlockState var3) {
      if (this.a() && var1 == this.k && this.Y.getOrDefault(var2.asLong(), 0) > 0 && var1 instanceof ClientLevel var4) {
         if (var1.isOutsideBuildHeight(var2)) {
            return Blocks.AIR.defaultBlockState();
         }

         LevelChunk var5 = var4.getChunkAt(var2);
         return var5.getSection(var5.getSectionIndex(var2.getY())).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15);
      } else {
         return var3;
      }
   }

   public void e(Runnable var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (this.a() && var2.player != null && var2.isSameThread()) {
         try {
            this.U(var2.player.getInventory(), var1);
         } finally {
            this.f();
         }
      } else {
         var1.run();
      }
   }

   void U(Container var1, Runnable var2) {
      try {
         var2.run();
      } finally {
         _8.S();
      }
   }

   private void R(int var1, ItemStack var2) {
      ItemStack var3 = this.h.get(var1);
      if (var3 != null && var2.is(var3.getItem()) && var2.getCount() >= var3.getCount() && !ItemStack.isSameItemSameComponents(var2, var3)) {
         this.h.put(var1, var2.copyWithCount(var3.getCount()));

         for (Deque var5 : this.T.values()) {
            ArrayDeque var6 = new ArrayDeque();

            for (jn.x var8 : var5) {
               var6.addLast(var8.preferredSlot == var1 && var8.stack.is(var2.getItem()) ? new jn.x(var1, var2.copyWithCount(var8.stack.getCount())) : var8);
            }

            var5.clear();
            var5.addAll(var6);
         }
      }
   }

   public boolean o(ItemStack var1) {
      return this.a() && !var1.isEmpty() && this.h.values().stream().anyMatch(var1x -> ItemStack.isSameItemSameComponents(var1, var1x));
   }

   public boolean f(int var1, int var2, int var3, ContainerInput var4) {
      if (!this.a() || q7.F()) {
         return true;
      }

      if (q7.M()) {
         return false;
      }

      LocalPlayer var5 = Minecraft.getInstance().player;
      return var5 != null && var5.containerMenu.containerId == var1 ? this.G(var5.containerMenu, var2, var3, var4) : true;
   }

   public boolean G(AbstractContainerMenu var1, int var2, int var3, ContainerInput var4) {
      if (!this.a() || q7.F()) {
         return true;
      }

      if (q7.M()) {
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
         if (var6.container == var5.getInventory() && (this.G(var6.getContainerSlot()) || _v.O(var6.getContainerSlot()) || this.s(var6.getContainerSlot()))) {
            return false;
         }
      }

      return var4 != ContainerInput.SWAP || !this.G(var3) && !_v.O(var3) && !this.s(var3)
         ? var4 != ContainerInput.PICKUP_ALL || !this.o(var1.getCarried())
         : false;
   }

   public void a(int var1, Runnable var2) {
      this.e(var2);
   }

   public boolean o(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      ItemStack var4 = this.h.get(var2);
      if (var4 == null || var4.isEmpty()) {
         return false;
      }

      if (var3.player != null
         && var3.gameMode != null
         && var1 >= 9
         && var1 < 36
         && !this.G(var1)
         && var3.player.inventoryMenu.getCarried().isEmpty()
         && var3.player.containerMenu == var3.player.inventoryMenu) {
         if (this.q(var3.player.getInventory().getItem(var1))) {
            this.f(var2);
         }

         q7.K(() -> var3.gameMode.handleContainerInput(0, var1, var2, ContainerInput.SWAP, var3.player));
         if (var4 != null && !var4.isEmpty()) {
            this.h.remove(var2);
            this.h.put(var1, var4);

            for (Deque var6 : this.T.values()) {
               ArrayDeque var7 = new ArrayDeque();

               for (jn.x var9 : var6) {
                  var7.addLast(var9.preferredSlot == var2 ? new jn.x(var1, var9.stack) : var9);
               }

               var6.clear();
               var6.addAll(var7);
            }

            _8.S();
         }

         return true;
      } else {
         return false;
      }
   }

   public boolean G(int var1) {
      return this.a() && this.h.containsKey(var1) && !this.h.get(var1).isEmpty();
   }

   public int P(int var1, ItemStack var2) {
      ItemStack var3 = this.h.get(var1);
      return this.a() && var3 != null && var2.is(var3.getItem()) ? Math.max(0, var2.getCount() - var3.getCount()) : 0;
   }

   public ItemStack u(int var1, ItemStack var2) {
      if (!this.G(var1)) {
         return var2;
      }

      int var3 = this.P(var1, var2);
      return var3 == 0 ? ItemStack.EMPTY : nn.F(var2.copyWithCount(var3));
   }

   public boolean H(AbstractContainerMenu var1, List<q7.l> var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var1 == var3.player.inventoryMenu && var3.player.hasInfiniteMaterials() && var3.player.containerMenu instanceof ItemPickerMenu
         )
       {
         this.e(() -> {
            for (q7.l var4 : var2) {
               var1.clicked(var4.slot(), var4.button(), var4.type(), var3.player);
            }

            var1.broadcastChanges();
         });
         return true;
      } else {
         return false;
      }
   }

   public boolean n(AbstractContainerMenu var1, Slot var2, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var2 != null && var2.container == var4.player.getInventory() && var3 >= 0 && var3 <= 1) {
         int var5 = var2.getContainerSlot();
         ItemStack var6 = var4.player.getInventory().getItem(var5);
         int var7 = this.P(var5, var6);
         if (var7 != 0 && var1.getCarried().isEmpty()) {
            int var8 = var3 == 0 ? var7 : (var7 + 1) / 2;
            ArrayList var9 = new ArrayList();
            var9.add(new q7.l(var2.index, 0, ContainerInput.PICKUP));

            for (int var10 = 0; var10 < var6.getCount() - var8; var10++) {
               var9.add(new q7.l(var2.index, 1, ContainerInput.PICKUP));
            }

            int var11 = var6.getCount() - var7;
            if (this.H(var1, var9)) {
               _q.c("FakeSpawner", "Creative surplus picked physical={} amount={} reserved={}", var5, var8, var11);
               return true;
            } else {
               q7.h(var1, "pick unreserved surplus", var9, var6.copyWithCount(var8), _8::S, _8::S);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private int o() {
      Inventory var1 = Minecraft.getInstance().player.getInventory();

      for (int var2 = 0; var2 < 36; var2++) {
         if (!this.G(var2) && !this.s(var2) && var1.getItem(var2).isEmpty()) {
            return var2;
         }
      }

      return -1;
   }

   public int a(int var1) {
      return this.H(Items.BONE, var1);
   }

   public int H(Item var1, int var2) {
      return _v.Y(var1, var2);
   }

   public boolean W(ItemStack var1) {
      return false;
   }

   public boolean W() {
      return this.E(1) == 1;
   }

   public int E(int var1) {
      return this.a(Items.BONE, var1);
   }

   public int a(Item var1, int var2) {
      return this.H(var1, var2);
   }

   public boolean c(int var1) {
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

   public Component U(Component var1) {
      return var1 == null ? Component.literal(p("place items in here to sell")) : Component.literal(p(var1.getString()));
   }

   public void o(Component var1, AbstractContainerMenu var2) {
      if (this.a() && this.J(var1, var2)) {
         long var3 = 0L;

         for (int var5 = 0; var5 < Math.max(0, var2.slots.size() - 36); var5++) {
            var3 += this.A(var2.getSlot(var5).getItem());
         }

         if (var3 > 0L) {
            h4.C(var3);
         }
      }
   }

   private long k(BlockPos var1) {
      long var2 = System.currentTimeMillis();
      return var1 == null ? var2 : this.j.computeIfAbsent(var1.asLong(), var2x -> var2);
   }

   private int o(BlockPos var1) {
      return var1 == null ? Math.max(1, FakeSpawnerModule.O().l()) : Math.max(1, this.Y.getOrDefault(var1.asLong(), 1));
   }

   private jn.c s(long var1, int var3) {
      return this.S(var1, var3, ni.X);
   }

   private jn.c S(long var1, int var3, ni var4) {
      double var5 = this.j(var3);
      double var7 = var4.W == Items.AIR ? 0.0 : Math.max(0.15, var5 * 0.33);
      long var9 = Math.max(0L, System.currentTimeMillis() - var1);
      long var11 = var9 / 60000L;
      return new jn.c(Math.max(0, (int)(var11 * var5)), Math.max(0, (int)(var11 * var7)), var5, var7);
   }

   private double j(int var1) {
      int var2 = Math.max(1, var1);
      if (var2 == 1) {
         return 1.9;
      } else {
         return var2 <= 64 ? var2 * 1.9453125 : 124.5 + (var2 - 64) * 0.88671875;
      }
   }

   private static int i(SimpleContainer var0, int var1, Item var2, int var3) {
      int var4 = Math.max(0, var3);

      while (var4 > 0 && var1 < 45) {
         int var5 = Math.min(64, var4);
         var0.setItem(var1++, new ItemStack(var2, var5));
         var4 -= var5;
      }

      return var1;
   }

   private List<Component> W(jn.c var1, ni var2) {
      int var3 = var1 == null ? 0 : Math.max(0, var1.S);
      int var4 = var1 == null ? 0 : Math.max(0, var1.y);
      if (var3 <= 0 && var4 <= 0) {
         return List.of(n("(0% filled)", 64648));
      }

      ArrayList var5 = new ArrayList();
      if (var3 > 0) {
         var5.add(n(var3 + " ", 64648).append(var2.W.getName(new ItemStack(var2.W)).copy().withStyle(ChatFormatting.WHITE)));
      }

      if (var4 > 0) {
         var5.add(n(var4 + " ", 64648).append(var2.S.getName(new ItemStack(var2.S)).copy().withStyle(ChatFormatting.WHITE)));
      }

      var5.add(n("(0% filled)", 64648));
      return var5;
   }

   private boolean g(long var1, int var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && !q7.M() && var4.player.containerMenu == var4.player.inventoryMenu && var4.player.inventoryMenu.getCarried().isEmpty()) {
         Deque var5 = this.T.get(var1);
         if (var5 != null && !var5.isEmpty()) {
            LinkedHashMap var6 = new LinkedHashMap();
            int var7 = var3;

            for (jn.x var9 : var5.reversed()) {
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
               ItemStack var12 = this.h.get(var17.getKey());
               if (var12 != null
                  && var12.getCount() == (Integer)var17.getValue()
                  && var4.player.getInventory().getItem((Integer)var17.getKey()).getCount() == (Integer)var17.getValue()) {
                  var14 = (Integer)var17.getKey();
                  break;
               }
            }

            if (var14 < 0) {
               var14 = this.o();
            }

            if (var14 < 0) {
               return false;
            }

            ArrayList var16 = new ArrayList();

            for (Entry var20 : var6.entrySet()) {
               if ((Integer)var20.getKey() != var14) {
                  var16.addAll(
                     q7.I((Integer)var20.getKey(), var14, (Integer)var20.getValue(), var4.player.getInventory().getItem((Integer)var20.getKey()).getCount())
                  );
               }
            }

            if (var16.isEmpty()) {
               this.N(var1, var3);
               if (var7 > 0) {
                  this.q(new ItemStack(this.i(var1), var7), var7);
               }

               _8.S();
               return true;
            } else {
               int var19 = var14;
               int var21 = var7;
               q7.B("mining return", var16, () -> {
                  this.N(var1, var3);
                  this.I(var1, var3);
                  if (var21 > 0) {
                     this.q(new ItemStack(this.i(var1), var21), var21);
                  }

                  this.d(BlockPos.of(var1), true);
                  _8.S();
                  this.W("Mining return predicted pile={} physical={} amount={} worldPending={}", var1, var19, var13, var21);
               }, () -> this.Z());
               return false;
            }
         } else {
            this.q(new ItemStack(this.i(var1), var3), var3);
            return true;
         }
      } else {
         return false;
      }
   }

   private void q(ItemStack var1, int var2) {
      if (var2 > 0 && !var1.isEmpty()) {
         Inventory var3 = Minecraft.getInstance().player.getInventory();
         int var4 = -1;

         for (int var5 = 0; var5 < 36; var5++) {
            if (!this.G(var5)) {
               ItemStack var6 = var3.getItem(var5);
               jn.a var7 = this.E.get(var5);
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
            var4 = this.o();
         }

         if (var4 >= 0) {
            int var8 = var3.getItem(var4).isEmpty() && !this.E.containsKey(var4) ? _8.y() : -1;
            this.X(var4, var1, 0, var2);
            if (var8 >= 0) {
               _8.k(var8, var4);
            }
         }
      }
   }

   private void X(int var1, ItemStack var2, int var3, int var4) {
      if (var4 > 0) {
         jn.a var5 = this.E.get(var1);
         int var6 = Minecraft.getInstance().player.getInventory().getItem(var1).getCount();
         int var7 = var5 != null && ItemStack.isSameItemSameComponents(var5.stack, var2) ? Math.max(var5.expected + var3, var6) : var6;
         this.E.put(var1, new jn.a(var2.copyWithCount(1), var7 + var4, Minecraft.getInstance().player.tickCount + 100));
         _8.j();
      }
   }

   public boolean s(int var1) {
      return this.E.containsKey(var1);
   }

   public ItemStack Z(int var1, ItemStack var2) {
      jn.a var3 = this.E.get(var1);
      return var3 != null && var2.isEmpty() ? nn.F(var3.stack.copyWithCount(var3.expected)) : var2;
   }

   private jn.x N(long var1, int var3) {
      Deque var4 = this.T.get(var1);
      if (var4 != null && !var4.isEmpty() && var3 > 0) {
         int var5 = var3;
         int var6 = -1;
         ItemStack var7 = ItemStack.EMPTY;

         while (var5 > 0 && !var4.isEmpty()) {
            jn.x var8 = (jn.x)var4.removeLast();
            if (var8 != null && var8.stack != null && !var8.stack.isEmpty()) {
               if (var6 < 0) {
                  var6 = var8.preferredSlot;
               }

               if (var7.isEmpty()) {
                  var7 = var8.stack.copy();
               }

               int var9 = Math.min(var5, var8.stack.getCount());
               ItemStack var10 = this.h.get(var8.preferredSlot);
               if (var10 != null && ItemStack.isSameItemSameComponents(var10, var8.stack)) {
                  if (var10.getCount() <= var9) {
                     this.h.remove(var8.preferredSlot);
                  } else {
                     this.h.put(var8.preferredSlot, var10.copyWithCount(var10.getCount() - var9));
                  }
               }

               var5 -= var9;
               int var11 = var8.stack.getCount() - var9;
               if (var11 > 0) {
                  var4.addLast(new jn.x(var8.preferredSlot, var8.stack.copyWithCount(var11)));
               }
            }
         }

         if (var4.isEmpty()) {
            this.T.remove(var1);
         }

         return var7.isEmpty() ? null : new jn.x(var6, var7.copyWithCount(var3));
      } else {
         return null;
      }
   }

   private boolean Z(long var1, jn.x var3, boolean var4) {
      if (var3 != null && !var3.stack.isEmpty()) {
         Minecraft var5 = Minecraft.getInstance();
         if (var5.player == null) {
            return false;
         }

         int var6 = var3.preferredSlot;
         if (var6 >= 0 && var6 < var5.player.getInventory().getContainerSize() && !this.h.containsKey(var6)) {
            ItemStack var7 = var5.player.getInventory().getItem(var6);
            int var8 = var3.stack.getCount();
            int var9 = var4 && !var5.player.hasInfiniteMaterials() ? var8 - 1 : var8;
            if (var9 <= 0) {
               return true;
            } else if (var7.is(var3.stack.getItem()) && var7.getCount() >= var9) {
               this.h.put(var6, var7.copyWithCount(var9));
               this.T.computeIfAbsent(var1, var0 -> new ArrayDeque<>()).addFirst(new jn.x(var6, var7.copyWithCount(var9)));
               _8.S();
               _q.c(
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

   private void o(Minecraft var1, BlockPos var2, Direction var3, double var4) {
      if (var1 != null && var1.player != null && var1.levelRenderer != null && var2 != null) {
         int var6 = Mth.clamp((int)(var4 * 10.0), 0, 9);
         var1.level.destroyBlockProgress(var1.player.getId(), var2, var6);
         if (var4 <= 0.0) {
            return;
         }

         long var7 = var2.asLong();
         long var9 = var1.player.tickCount;
         long var11 = this.B.getOrDefault(var7, 0L);
         if (var9 - var11 >= 4L) {
            this.B.put(var7, var9);
            this.n(var2, var3);
            this.d(var2, false);
         }
      }
   }

   private void A(BlockPos var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var2.levelRenderer != null && var1 != null) {
         var2.level.destroyBlockProgress(var2.player.getId(), var1, -1);
      }
   }

   private void N(Level var1, BlockPos var2) {
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

   private void Q(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null && var1 != null && !var1.isBlank()) {
         iw.V(n(var1, 16711680), false);
         iw.V(n(var1, 16711680), true);
      }
   }

   private void W(String var1, Object... var2) {
      if (Boolean.getBoolean("larp.debug")) {
         _q.c("FakeSpawner", var1, var2);
      }
   }

   private void S(String var1, long var2, String var4, Object... var5) {
      if (Boolean.getBoolean("larp.debug")) {
         long var6 = System.currentTimeMillis();
         if (var6 >= this.n) {
            this.n = var6 + var2;
            this.W(var4, var5);
         }
      }
   }

   private static ItemStack P(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1);
      var0.set(DataComponents.RARITY, Rarity.COMMON);
      if (var2 != null && !var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component Y(String var0, ChatFormatting var1) {
      return u(p(var0), var1);
   }

   private static MutableComponent u(String var0, ChatFormatting var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.applyFormat(var1).withItalic(false));
   }

   private static Component y(String var0, int var1) {
      return n(p(var0), var1);
   }

   private static MutableComponent n(String var0, int var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)).withItalic(false));
   }

   private static String p(String var0) {
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

   private static String I(double var0, int var2) {
      String var3 = String.format(Locale.US, "%." + var2 + "f", var0);

      while (var3.contains(".") && var3.endsWith("0")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      if (var3.endsWith(".")) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3;
   }

   private static Component F(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         MutableComponent var1 = Component.empty();
         Style var2 = Style.EMPTY.withItalic(false);
         StringBuilder var3 = new StringBuilder();

         for (int var4 = 0; var4 < var0.length(); var4++) {
            char var5 = var0.charAt(var4);
            if ((var5 == 167 || var5 == '&') && var4 + 1 < var0.length()) {
               if (var5 == '&' && var4 + 7 < var0.length() && var0.charAt(var4 + 1) == '#') {
                  Integer var6 = w(var0.substring(var4 + 2, var4 + 8));
                  if (var6 != null) {
                     j(var1, var3, var2);
                     var2 = var2.withColor(TextColor.fromRgb(var6)).withItalic(false);
                     var4 += 7;
                     continue;
                  }
               }

               ChatFormatting var7 = ChatFormatting.getByCode(var0.charAt(var4 + 1));
               if (var7 != null) {
                  j(var1, var3, var2);
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

         j(var1, var3, var2);
         return var1;
      } else {
         return Component.empty();
      }
   }

   private static void j(MutableComponent var0, StringBuilder var1, Style var2) {
      if (var1.length() != 0) {
         var0.append(Component.literal(var1.toString()).setStyle(var2));
         var1.setLength(0);
      }
   }

   private static Integer w(String var0) {
      try {
         return Integer.parseInt(var0, 16);
      } catch (NumberFormatException var2) {
         return null;
      }
   }

   private String b(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0;
      if (var5 >= 1.0E9) {
         return I(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return I(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? I(var5 / 1000.0, 1) + "K" : I(var5, 0);
      }
   }

   private String K(long var1) {
      long var3 = Math.max(1L, var1);
      double var5 = var3 * 1000.0 + 75.0;
      if (var5 >= 1.0E9) {
         return I(var5 / 1.0E9, 1) + "B";
      } else if (var5 >= 1000000.0) {
         return I(var5 / 1000000.0, 1) + "M";
      } else {
         return var5 >= 1000.0 ? I(var5 / 1000.0, 1) + "K" : I(var5, 0);
      }
   }

   private boolean y(ItemStack var1) {
      return var1 != null && !var1.isEmpty() ? var1.getItem() instanceof BlockItem var2 && var2.getBlock() instanceof ShulkerBoxBlock : false;
   }

   private long Z(ItemStack var1) {
      if (var1 != null && !var1.isEmpty()) {
         long var2 = 0L;
         ItemContainerContents var4 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
         if (var4 != null) {
            for (ItemStack var6 : var4.nonEmptyItemCopyStream().toList()) {
               if (var6 != null && !var6.isEmpty() && this.M(var6)) {
                  var2 += var6.getCount();
               }
            }
         }

         return var2;
      } else {
         return 0L;
      }
   }

   private int N(CompoundTag var1) {
      if (var1 == null) {
         return 0;
      } else if (var1.contains("count")) {
         return Math.max(0, var1.getIntOr("count", 0));
      } else {
         return var1.contains("Count") ? Math.max(0, var1.getIntOr("Count", 0)) : 0;
      }
   }

   private boolean u(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      return var2 != null && var2.getWindow() != null ? j2.U(var2.getWindow(), var1) : false;
   }

   private record a(ItemStack stack, int expected, int expires) {
   }

   public static final class c {
      public final int y;
      public final int S;
      public final double w;
      public final double B;

      public c(int var1, int var2, double var3, double var5) {
         this.y = Math.max(0, var1);
         this.S = Math.max(0, var2);
         this.w = Math.max(0.0, var3);
         this.B = Math.max(0.0, var5);
      }
   }

   public record m(SoundEvent event, float volume, float pitch) {
   }

   private record o(int stackCount, jn.x hiddenStack, long createdAtMs) {
   }

   private record p(int slot, String item, int amount, int reserved) {
   }

   private record u(BlockPos pos, long untilMs) {
   }

   private record w(long pos, ni kind, int count, long placedAt, List<jn.p> backing, Map<String, Integer> purchased) {
      JsonObject toJson() {
         JsonObject var1 = new JsonObject();
         var1.addProperty("pos", this.pos);
         var1.addProperty("kind", this.kind.name());
         var1.addProperty("count", this.count);
         var1.addProperty("placedAt", this.placedAt);
         JsonArray var2 = new JsonArray();

         for (jn.p var4 : this.backing) {
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

   private record x(int preferredSlot, ItemStack stack) {
   }
}
