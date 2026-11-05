package com.larpclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public final class k extends s {
   private static k x;
   private final com.larpclient.module.ln G = this.S("Legit", "Select the best hotbar tool while smoothly aiming at each block", false);
   private final com.larpclient.module.lp T = this.addSetting(
      new com.larpclient.module.lp("Width", "Tunnel cross section; 1x1 is a crawl tunnel that needs a crawling start", "1x2", "1x1", "1x2", "3x3")
   );
   private final com.larpclient.module.lc c = this.H("Reach", "How far ahead to mine", 4.0, 2.0, 6.0, 1.0);
   private final com.larpclient.module.ln d = this.S("Base finder", "Walk a fixed heading, avoid hazards and stop at nearby storage clusters", false);
   private final com.larpclient.module.lc K = this.H("Hazard scan", "Distance ahead to inspect lava, water and drops", 6.0, 3.0, 12.0, 1.0);
   private final com.larpclient.module.ln t = this.S("Avoid lava", "Avoid lava beside, above and below the tunnel", true);
   private final com.larpclient.module.ln r = this.S("Avoid water", "Avoid water that would flood the tunnel", true);
   private final com.larpclient.module.ln D = this.S("Avoid drops", "Avoid drops of two or more blocks", true);
   private final com.larpclient.module.ln v = this.S("Avoid gravel", "Avoid falling sand and gravel above the tunnel", false);
   private final com.larpclient.module.lc z = this.H("Detour", "Maximum sideways distance for a safe return to the main heading", 4.0, 2.0, 12.0, 1.0);
   private final com.larpclient.module.ln w = this.O("Chests", "Include chests and trapped chests", true);
   private final com.larpclient.module.ln q = this.O("Barrels", "Include barrels", true);
   private final com.larpclient.module.ln S = this.O("Ender Chests", "Include ender chests", true);
   private final com.larpclient.module.ln i = this.O("Hoppers", "Include hoppers", true);
   private final com.larpclient.module.ln O = this.O("Spawners", "Include mob spawners", true);
   private final com.larpclient.module.ln Q = this.O("Shulker Boxes", "Include uncolored and all colored shulker boxes", true);
   private final com.larpclient.module.lc C = this.H(
      "Blocks to flag", "Selected storage blocks within 12 blocks of each other needed for a find", 5.0, 1.0, 64.0, 1.0
   );
   private final com.larpclient.module.ln N = this.S("Skip dungeons", "Ignore spawners on cobblestone floors or near cobwebs", true);
   private final com.larpclient.module.lc A = this.H("Ignore near start", "Ignore finds this close to the starting point", 48.0, 0.0, 256.0, 8.0);
   private final com.larpclient.module.lp Z = this.addSetting(
      new com.larpclient.module.lp("On find", "Report and stop, or disconnect after reporting", "Notify and stop", "Notify and stop", "Leave") {
         @Override
         public String getModeLabel(String var1) {
            return var1.equals("Notify and stop") ? "Notify & stop" : var1;
         }

         @Override
         public String getValueAsString() {
            return this.getModeLabel(this.getValue());
         }
      }
   );
   private final com.larpclient.module.ln m = this.S("Log finds", "Append time, server, dimension and coordinates to config/larp-client/bases.txt", true);
   private final com.larpclient.module.lc X = this.H(
      "Save tool at", "Stop before the selected tool has fewer remaining uses; zero allows breaking", 30.0, 0.0, 200.0, 5.0
   );
   private final com.larpclient.module.lc J = this.H("Stop at health", "Stop when health reaches this value; zero disables the limit", 8.0, 0.0, 20.0, 1.0);
   private final xz<LevelChunk> g = new xz<>();
   private final Map<Long, k.m> E = new HashMap<>();
   private final List<BlockPos> p = new ArrayList<>();
   private final ArrayDeque<BlockPos> l = new ArrayDeque<>();
   private ClientLevel V;
   private BlockPos L;
   private Vec3 M;
   private Direction b;
   private Direction P;
   private BlockPos j;
   private BlockPos u;
   private final ei n = new ei();
   private final SplittableRandom e = new SplittableRandom();
   private Vec3 a = Vec3.ZERO;
   private boolean y;
   private boolean B;
   private int s;
   private BlockPos W;
   private Vec3 f = Vec3.ZERO;
   private double I = 0.6;
   private final xz.b<LevelChunk> o = new xz.b<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return k.this.V.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         k.m var3 = k.this.E.get(ChunkPos.pack(var1, var2));
         return var3 == null || k.mc.player.tickCount - var3.tick >= 80L;
      }

      public int G(LevelChunk var1) {
         return var1.getSections().length;
      }

      public boolean b(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];
         return !var3.hasOnlyAir() && var3.maybeHas(k.this::q);
      }

      @Override
      public void begin() {
         k.this.p.clear();
      }

      public void S(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];

         for (int var4 = 0; var4 < 16; var4++) {
            for (int var5 = 0; var5 < 16; var5++) {
               for (int var6 = 0; var6 < 16; var6++) {
                  BlockState var7 = var3.getBlockState(var6, var4, var5);
                  if (k.this.q(var7) && k.this.p.size() < 256) {
                     BlockPos var8 = new BlockPos(var1.getPos().getMinBlockX() + var6, var1.getMinY() + var2 * 16 + var4, var1.getPos().getMinBlockZ() + var5);
                     if (!(k.this.L.distSqr(var8) < k.this.A.getValue() * k.this.A.getValue())
                        && (!k.this.N.getValue() || !var7.is(Blocks.SPAWNER) || !k.this.c(var8))) {
                        k.this.p.add(var8);
                     }
                  }
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         k.this.E.put(ChunkPos.pack(var1, var2), new k.m(List.copyOf(k.this.p), k.mc.player.tickCount));
      }
   };

   private com.larpclient.module.ln O(final String var1, String var2, boolean var3) {
      return this.addSetting(new com.larpclient.module.ln("Storage " + var1, var2, var3) {
         @Override
         public String getDisplayName() {
            return "Storage: " + var1;
         }
      });
   }

   public k() {
      super("Auto Tunnel", "Mine and walk a centered 1x1, 1x2 or 3x3 tunnel, including while menus are open");
      x = this;

      for (com.larpclient.module.l6 var2 : List.of(this.K, this.z, this.w, this.q, this.S, this.i, this.O, this.Q, this.C, this.A, this.Z, this.m)) {
         var2.x(this.d::getValue);
      }

      this.N.x(() -> this.d.getValue() && this.O.getValue());
      this.d.W(var1 -> this.a());
      this.T.W(var1 -> this.a());

      for (com.larpclient.module.ln var4 : List.of(this.w, this.q, this.S, this.i, this.O, this.Q, this.N)) {
         var4.W(var1 -> {
            this.g.V();
            this.E.clear();
            this.p.clear();
         });
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.a();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.a();
   }

   @Override
   public boolean allowsScreenInteractions() {
      return true;
   }

   public static boolean V() {
      if (x != null && x.isEnabled()) {
         k var0 = x;
         Minecraft var1 = Minecraft.getInstance();
         return var0 != null && var0.isRuntimeActive() && d8.r(var0) && var1.gui.overlay() == null && var1.player.connection.hasClientLoaded();
      } else {
         return false;
      }
   }

   void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (!this.B) {
            double var3 = Math.min(this.c.getValue(), mc.player.blockInteractionRange());
            Vec3 var5 = var1;
            if (this.u != null) {
               if (!this.u.equals(this.W)) {
                  this.W = this.u;
                  this.f = new Vec3(this.e.nextDouble(-0.22, 0.22), this.e.nextDouble(-0.22, 0.22), this.e.nextDouble(-0.22, 0.22));
               }

               Vec3 var6 = var1.add(this.f);
               if (yx.R(this.u, var6, var3)) {
                  var5 = var6;
               }
            }

            float[] var9 = ep.g(mc.player.getEyePosition(), var5);
            int var7 = this.u != null && yx.B(this.u, var3) == null ? 0 : 1;
            float[] var8 = this.n.Y(mc.player.getYRot(), mc.player.getXRot(), var9[0], var9[1], (boolean)var7);
            ep.t(this, ep.z.z, var8[0], var8[1], 1.0);
         }
      }
   }

   private void a() {
      d8.K(this);
      yx.H(this);
      this.y = this.B = (boolean)0;
      this.P = this.b = null;
      this.L = null;
      this.M = null;
      this.j = this.u = this.W = null;
      this.a = Vec3.ZERO;
      this.n.A();
      this.I = this.e.nextDouble(0.35, 1.1);
      this.l.clear();
      this.g.V();
      this.E.clear();
      this.p.clear();
      this.V = null;
      this.s = 0;
   }

   @Override
   public void onTick() {
      this.y = (boolean)0;
      this.a = Vec3.ZERO;
      int var10000 = this.G();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         int var20 = this.w();
         boolean var14 = false;
         byte var13 = (byte)var20;
         var20 = Integer.signum(var13);
         if (((var20 | -var20) >>> 31 ^ 1) != 0) {
            byte var1 = this.Z();
            byte var16 = this.B;
            byte var15 = var1;
            var20 = Integer.compare(var15, var16);
            if (((var20 | -var20) >>> 31 ^ 1) == 0) {
               this.B = (boolean)var1;
               r4.V(
                  this.getName(),
                  var1 != 0
                     ? "Inventory menu open; holding position and facing, mining only the block under the crosshair"
                     : "Inventory menu closed; resuming walking and aiming"
               );
               if (var1 == 0) {
                  this.n.A();
               }
            }

            if (this.V != mc.level || this.M != null && mc.player.position().distanceToSqr(this.M) > 64.0) {
               this.a();
               this.V = mc.level;
               this.L = mc.player.blockPosition();
               this.b = Direction.fromYRot(mc.player.getYRot());
            }

            Direction var2 = this.b;
            if (this.J.getValue() > 0.0 && mc.player.getHealth() <= this.J.getValue()) {
               this.i("Low health");
               return;
            }

            if (this.T.i("1x1") && !mc.player.isVisuallyCrawling()) {
               yx.H(this);
               this.n.A();
               this.u = null;
               this.I("Waiting: 1x1 needs a crawling start, such as under a trapdoor");
               return;
            }

            if (this.d.getValue()) {
               this.Q();
               if (!this.isRuntimeActive()) {
                  return;
               }

               var2 = this.B();
               if (var2 == null) {
                  return;
               }
            }

            if (this.P != var2 || this.j == null) {
               this.P = var2;
               this.j = mc.player.blockPosition().relative(var2);
               this.u = null;
               this.n.A();
               r4.V(this.getName(), "Following {} through lane {}, {} at Y {} ({})", var2, this.j.getX(), this.j.getZ(), this.j.getY(), this.T.getValue());
            }

            this.j = b(this.j, mc.player.position(), var2, this.q(this.j, var2));
            String var3 = this.E(this.j, var2);
            if (var3 != null) {
               yx.H(this);
               this.n.A();
               this.u = null;
               this.I("Waiting: " + var3);
               return;
            }

            double var4 = Math.min(this.c.getValue(), mc.player.blockInteractionRange());
            BlockPos var6 = this.c(var2, var4);
            if (var6 != null) {
               if (this.g(var6)) {
                  this.i("Tool nearly broken");
                  return;
               }

               BlockPos var7 = var6;
               Direction var8 = var2;
               BlockPos var9 = this.j;
               int var10005 = this.G.getValue();
               boolean var18 = false;
               byte var17 = (byte)var10005;
               var10005 = Integer.signum(var17);
               boolean var10 = yx.Y(
                  this,
                  var6,
                  var4,
                  true,
                  12.0F,
                  (var10005 | -var10005) >>> 31 != 0 || this.d.getValue(),
                  () -> !this.w() && this.E(var9, var8) == null && (!this.d.getValue() || !mc.level.getBlockState(var7).hasBlockEntity())
               );
               if (!var10) {
                  this.I("Waiting for a reachable tunnel face");
               }
            } else {
               yx.H(this);
            }

            this.S(var2, var6);
            return;
         }
      }

      yx.H(this);
      this.n.A();
      this.u = null;
      this.I(this.w() ? "Paused for manual movement" : "Waiting for world or interaction control");
   }

   private void S(Direction var1, BlockPos var2) {
      Vec3 var3 = Vec3.atBottomCenterOf(this.j);
      boolean var4 = this.q(this.j, var1);
      double var5 = ei.k(mc.player.position(), var3, var1) - (var4 ? -0.5 : 0.85);
      Vec3 var7 = mc.player.getDeltaMovement();
      double var8 = var7.x * var1.getStepX() + var7.z * var1.getStepZ();
      Vec3 var10 = ei.r(mc.player.position(), var3, var1);
      boolean var11 = this.n.F();
      int var12 = d8.m(this) && !this.B && !oo.Z() && this.A(d(var10)) ? 1 : 0;
      this.y = this.n.m(var5, var8, (boolean)var12);
      this.a = this.y ? var10 : Vec3.ZERO;
      if (this.y != var11) {
         r4.g(this.getName(), this.y ? "Walk keys pressed; {} clear ahead" : "Walk keys released; {} clear ahead", String.format(Locale.ROOT, "%.2f", var5));
      }

      if (var2 == null && d8.m(this)) {
         Vec3 var13 = var3.add(var1.getStepX() * 6, 0.0, var1.getStepZ() * 6);
         if (mc.player.tickCount % 60 == 0 && this.e.nextInt(4) == 0) {
            this.I = this.e.nextDouble(0.35, 1.1);
         }

         this.aimAt(new Vec3(var13.x, mc.player.getEyeY() - this.I, var13.z), 12.0F);
      }

      if (this.M != null && mc.player.position().distanceToSqr(this.M) < 1.0E-4 && this.y && var2 == null) {
         this.s++;
      } else {
         this.s = 0;
      }

      this.M = mc.player.position();
      if (var2 == null) {
         this.I(
            this.B
               ? "Holding still while an inventory menu is open"
               : (this.y ? (this.d.getValue() ? "Searching for bases" : "Following the cleared tunnel") : "Waiting at a protected or unreachable obstruction")
         );
      }

      if (this.s > 160) {
         this.i("Movement blocked; clear the route or choose a new heading");
      }
   }

   public static BlockPos b(BlockPos var0, Vec3 var1, Direction var2, boolean var3) {
      return var3 && ei.k(var1, Vec3.atBottomCenterOf(var0), var2) < 0.12 ? var0.relative(var2) : var0;
   }

   private boolean Z() {
      return mc.gui.screen() instanceof AbstractContainerScreen;
   }

   private boolean w() {
      return mc != null
         && mc.player != null
         && mc.isWindowActive()
         && mc.gui.screen() == null
         && (
            yu.X(mc.options.keyUp)
               || yu.X(mc.options.keyDown)
               || yu.X(mc.options.keyLeft)
               || yu.X(mc.options.keyRight)
               || yu.X(mc.options.keyJump)
               || yu.X(mc.options.keyShift)
         );
   }

   private List<BlockPos> G(BlockPos var1, Direction var2) {
      return o(var1, var2, this.T.getValue());
   }

   public static int J(String var0) {
      return switch (var0) {
         case "1x1" -> 1;
         case "3x3" -> 3;
         default -> 2;
      };
   }

   public static List<BlockPos> o(BlockPos var0, Direction var1, String var2) {
      boolean var3 = var2.equals("3x3");
      int var4 = J(var2);
      ArrayList var5 = new ArrayList(var3 ? 9 : var4);

      for (int var6 = 0; var6 < var4; var6++) {
         var5.add(var0.above(var6));
         if (var3) {
            var5.add(var0.relative(var1.getClockWise()).above(var6));
            var5.add(var0.relative(var1.getCounterClockWise()).above(var6));
         }
      }

      return var5;
   }

   private BlockPos c(Direction var1, double var2) {
      for (int var4 = 0; var4 < Math.ceil(var2); var4++) {
         BlockPos var5 = this.j.relative(var1, var4);
         if (!this.l.isEmpty() && ei.k(Vec3.atBottomCenterOf(var5), Vec3.atBottomCenterOf((Vec3i)this.l.peekFirst()), var1) < -0.01
            || this.E(var5, var1) != null) {
            break;
         }

         List var6 = this.G(var5, var1).stream().filter(var3 -> this.M(var3, var2)).toList();
         if (!var6.isEmpty()) {
            if (this.u != null && var6.contains(this.u)) {
               return this.u;
            }

            this.u = var6.stream().min(Comparator.comparingDouble(this::X)).orElseThrow();
            return this.u;
         }

         if (!this.q(var5, var1)) {
            break;
         }
      }

      this.u = null;
      return null;
   }

   private boolean M(BlockPos var1, double var2) {
      return yx.w(var1)
         && mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= var2 * var2
         && (!this.d.getValue() || !mc.level.getBlockState(var1).hasBlockEntity())
         && yx.C(this, var1, var2) != null;
   }

   private double X(BlockPos var1) {
      float[] var2 = ep.g(mc.player.getEyePosition(), Vec3.atCenterOf(var1));
      return Math.hypot(Mth.wrapDegrees(var2[0] - mc.player.getYRot()), var2[1] - mc.player.getXRot());
   }

   private boolean q(BlockPos var1, Direction var2) {
      for (BlockPos var4 : this.G(var1, var2)) {
         if (!mc.level.hasChunkAt(var4) || !mc.level.getBlockState(var4).getCollisionShape(mc.level, var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static Vec3 d(Vec3 var0) {
      return var0.normalize().scale(0.55);
   }

   private boolean A(Vec3 var1) {
      Vec3 var2 = var1.scale(0.65);
      AABB var3 = mc.player.getBoundingBox().expandTowards(var2);
      if (!mc.level.noCollision(mc.player, var3)) {
         return false;
      }

      BlockPos var4 = BlockPos.containing(mc.player.position().add(var2));
      return this.e(var4) && this.E(var4, this.P) == null;
   }

   private boolean g(BlockPos var1) {
      if (this.X.getValue() <= 0.0) {
         return false;
      }

      BlockState var2 = mc.level.getBlockState(var1);
      ItemStack var3 = mc.player.getMainHandItem();
      float var4 = var3.getDestroySpeed(var2) + (var3.isCorrectToolForDrops(var2) ? 1000 : 0);

      for (int var5 = 0; (this.G.getValue() || this.d.getValue()) && var5 < 9; var5++) {
         ItemStack var6 = mc.player.getInventory().getItem(var5);
         float var7 = var6.getDestroySpeed(var2) + (var6.isCorrectToolForDrops(var2) ? 1000 : 0);
         if (var7 > var4) {
            var3 = var6;
            var4 = var7;
         }
      }

      return var3.isDamageableItem() && var3.getMaxDamage() - var3.getDamageValue() <= this.X.getValue();
   }

   private boolean e(BlockPos var1) {
      if (!mc.level.hasChunkAt(var1)) {
         return false;
      }

      int var2 = Math.min(J(this.T.getValue()), 2);

      for (int var3 = 0; var3 < var2; var3++) {
         if (!mc.level.getBlockState(var1.above(var3)).getCollisionShape(mc.level, var1.above(var3)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private String E(BlockPos var1, Direction var2) {
      if (!mc.level.hasChunkAt(var1)) {
         return "Waiting for loaded terrain";
      }

      for (BlockPos var4 : this.G(var1, var2)) {
         for (Direction var8 : Direction.values()) {
            BlockPos var9 = var4.relative(var8);
            if (!mc.level.hasChunkAt(var9)) {
               return "Waiting for loaded terrain";
            }

            FluidState var10 = mc.level.getFluidState(var9);
            if (this.t.getValue() && var10.is(FluidTags.LAVA)) {
               return "Lava";
            }

            if (this.r.getValue() && var10.is(FluidTags.WATER)) {
               return "Water";
            }
         }

         FluidState var13 = mc.level.getFluidState(var4);
         if (this.t.getValue() && var13.is(FluidTags.LAVA)) {
            return "Lava";
         }

         if (this.r.getValue() && var13.is(FluidTags.WATER)) {
            return "Water";
         }
      }

      if (this.D.getValue()
         && m(
            mc.level.getBlockState(var1.below()).getCollisionShape(mc.level, var1.below()).isEmpty(),
            mc.level.getBlockState(var1.below(2)).getCollisionShape(mc.level, var1.below(2)).isEmpty()
         )) {
         return "Deep drop";
      }

      if (this.v.getValue()) {
         for (BlockPos var12 : this.G(var1, var2)) {
            if (mc.level.getBlockState(var12.above()).getBlock() instanceof FallingBlock) {
               return "Falling sand or gravel";
            }
         }
      }

      return null;
   }

   private Direction B() {
      BlockPos var1 = mc.player.blockPosition();

      while (
         !this.l.isEmpty()
            && mc.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(Vec3.atBottomCenterOf((Vec3i)this.l.peekFirst()).multiply(1.0, 0.0, 1.0)) < 0.08
      ) {
         this.l.removeFirst();
      }

      if (!this.l.isEmpty()) {
         Direction var9 = var1.equals(this.l.peekFirst()) ? this.P : D(var1, this.l.peekFirst());
         String var11 = this.E(var1.relative(var9), var9);
         if (var11 != null) {
            this.i("Detour blocked by " + var11.toLowerCase(Locale.ROOT));
            return null;
         } else {
            return var9;
         }
      } else {
         String var2 = null;

         for (int var3 = 1; var3 <= this.K.getValue().intValue(); var3++) {
            var2 = this.E(var1.relative(this.b, var3), this.b);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            return this.b;
         }

         if (var2.equals("Waiting for loaded terrain")) {
            yx.H(this);
            this.I(var2);
            return null;
         }

         for (int var10 = 1; var10 <= this.z.getValue().intValue(); var10++) {
            for (Direction var5 : List.of(this.b.getClockWise(), this.b.getCounterClockWise())) {
               int var6 = this.K.getValue().intValue() + 2;
               BlockPos var7 = var1.relative(var5, var10);
               BlockPos var8 = var7.relative(this.b, var6);
               if (this.L(var1, var5, var10) && this.L(var7, this.b, var6) && this.L(var8, var5.getOpposite(), var10)) {
                  this.l.add(var7);
                  this.l.add(var8);
                  this.l.add(var1.relative(this.b, var6));
                  r4.V(this.getName(), "Routing {} blocks around {} using {}", var10, var2, var5);
                  this.I("Detouring around " + var2.toLowerCase(Locale.ROOT));
                  return var5;
               }
            }
         }

         this.i("No safe detour around " + var2.toLowerCase(Locale.ROOT));
         return null;
      }
   }

   private boolean L(BlockPos var1, Direction var2, int var3) {
      for (int var4 = 1; var4 <= var3; var4++) {
         if (this.E(var1.relative(var2, var4), var2) != null) {
            return false;
         }
      }

      return true;
   }

   private static Direction D(BlockPos var0, BlockPos var1) {
      int var2 = var1.getX() - var0.getX();
      int var3 = var1.getZ() - var0.getZ();
      return Math.abs(var2) > Math.abs(var3) ? (var2 > 0 ? Direction.EAST : Direction.WEST) : (var3 > 0 ? Direction.SOUTH : Direction.NORTH);
   }

   public static Input E() {
      k var0 = x;
      if (var0 != null && var0.G() && var0.y && !var0.B && var0.P != null && !rr.o() && !oo.Z() && !var0.w()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var0.V == var1.level && !(var0.a.lengthSqr() < 1.0E-6) && var0.A(d(var0.a)) && (!ep.f() || ep.W(ep.z.z))) {
            Input var2 = var0.n.c(var1.player.getYRot(), var0.a);
            return var2.backward() ? null : var2;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Vec2 F() {
      Input var0 = E();
      return var0 == null ? null : ei.r(var0);
   }

   public static boolean m(boolean var0, boolean var1) {
      return var0 && var1;
   }

   public static boolean V(BlockPos var0, BlockPos var1) {
      return Math.abs((long)var1.getX() - var0.getX()) <= 12L
         && Math.abs((long)var1.getY() - var0.getY()) <= 12L
         && Math.abs((long)var1.getZ() - var0.getZ()) <= 12L;
   }

   public static Vec2 t(float var0, Direction var1) {
      double var2 = Math.toRadians(var0 - var1.toYRot());
      return new Vec2((float)Math.sin(var2), (float)Math.cos(var2));
   }

   private boolean q(BlockState var1) {
      Block var2 = var1.getBlock();
      return this.w.getValue() && (var2 == Blocks.CHEST || var2 == Blocks.TRAPPED_CHEST)
         || this.q.getValue() && var2 == Blocks.BARREL
         || this.S.getValue() && var2 == Blocks.ENDER_CHEST
         || this.i.getValue() && var2 == Blocks.HOPPER
         || this.O.getValue() && var2 == Blocks.SPAWNER
         || this.Q.getValue() && var2 instanceof ShulkerBoxBlock;
   }

   private boolean c(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1.below());
      if (!var2.is(Blocks.COBBLESTONE) && !var2.is(Blocks.MOSSY_COBBLESTONE)) {
         for (BlockPos var4 : BlockPos.betweenClosed(var1.offset(-2, -1, -2), var1.offset(2, 1, 2))) {
            if (mc.level.hasChunkAt(var4) && mc.level.getBlockState(var4).is(Blocks.COBWEB)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private void Q() {
      ChunkPos var1 = mc.player.chunkPosition();
      int var2 = Math.clamp(mc.options.getEffectiveRenderDistance(), 1, 12);
      this.E.entrySet().removeIf(var3x -> {
         ChunkPos var4 = ChunkPos.unpack(var3x.getKey());
         return Math.abs(var4.x() - var1.x()) > var2 || Math.abs(var4.z() - var1.z()) > var2 || this.V.getChunkSource().getChunkNow(var4.x(), var4.z()) == null;
      });
      this.g.a(this.o, var1.x(), var1.z(), var2, 2);
      if (mc.player.tickCount % 10 == 0) {
         ArrayList var3 = new ArrayList();

         for (k.m var5 : this.E.values()) {
            for (BlockPos var7 : var5.positions) {
               if (var3.size() >= 4096) {
                  break;
               }

               if (mc.level.hasChunkAt(var7)
                  && this.q(mc.level.getBlockState(var7))
                  && this.L.distSqr(var7) >= this.A.getValue() * this.A.getValue()
                  && (!this.N.getValue() || !mc.level.getBlockState(var7).is(Blocks.SPAWNER) || !this.c(var7))) {
                  var3.add(var7);
               }
            }
         }

         HashMap var16 = new HashMap();

         for (BlockPos var19 : var3) {
            var16.computeIfAbsent(G(var19.getX(), var19.getY(), var19.getZ()), var0 -> new ArrayList<>()).add(var19);
         }

         for (BlockPos var20 : var3) {
            ArrayList var21 = new ArrayList();
            int var8 = Math.floorDiv(var20.getX(), 12);
            int var9 = Math.floorDiv(var20.getY(), 12);
            int var10 = Math.floorDiv(var20.getZ(), 12);

            for (int var11 = var8 - 1; var11 <= var8 + 1; var11++) {
               for (int var12 = var9 - 1; var12 <= var9 + 1; var12++) {
                  for (int var13 = var10 - 1; var13 <= var10 + 1; var13++) {
                     for (BlockPos var15 : var16.getOrDefault(var11 + ":" + var12 + ":" + var13, List.of())) {
                        if (V(var20, var15)) {
                           var21.add(var15);
                        }
                     }
                  }
               }
            }

            if (var21.size() >= this.C.getValue()) {
               this.E(var20, var21);
               return;
            }
         }
      }
   }

   private static String G(int var0, int var1, int var2) {
      return Math.floorDiv(var0, 12) + ":" + Math.floorDiv(var1, 12) + ":" + Math.floorDiv(var2, 12);
   }

   private void E(BlockPos var1, List<BlockPos> var2) {
      TreeMap var3 = new TreeMap();

      for (BlockPos var5 : var2) {
         var3.merge(BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var5).getBlock()).getPath(), 1, Integer::sum);
      }

      String var11 = var1.getX() + ", " + var1.getY() + ", " + var1.getZ();
      String var12 = var11 + " (" + var3.entrySet().stream().map(var0 -> var0.getValue() + " " + (String)var0.getKey()).collect(Collectors.joining(", ")) + ")";
      mc.player.sendSystemMessage(Component.literal("Larp Auto Tunnel: base found at " + var12));
      hg.B().Y("Base found", var12, hg.u.K, 10000L);
      mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.4F));
      r4.V(this.getName(), "Base found at {}", var12);
      if (this.m.getValue()) {
         Path var6 = FabricLoader.getInstance().getConfigDir().resolve("larp-client/bases.txt");
         ServerData var7 = mc.getCurrentServer();
         String var8 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            + "  "
            + (var7 == null ? "singleplayer" : var7.ip)
            + "  "
            + mc.level.dimension().identifier()
            + "  "
            + var12
            + System.lineSeparator();

         try {
            Files.createDirectories(var6.getParent());
            Files.writeString(var6, var8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
         } catch (IOException var10) {
            r4.r(this.getName(), "Could not append base find to " + var6, var10);
         }
      }

      boolean var13 = this.Z.i("Leave");
      this.disable();
      if (var13) {
         mc.disconnectFromWorld(Component.literal("Larp Auto Tunnel found a base at " + var12));
      }
   }

   private void i(String var1) {
      r4.V(this.getName(), "Stopped: {}", var1);
      mc.player.sendSystemMessage(Component.literal("Larp Auto Tunnel stopped: " + var1));
      hg.B().o("Auto Tunnel stopped", var1, hg.u.Z);
      this.disable();
      this.I(var1);
   }

   private record m(List<BlockPos> positions, long tick) {
   }
}
