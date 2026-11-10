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

public final class _n extends _2 {
   private static _n Y;
   private final com.larpclient.module.m B = this.C("Legit", "Select the best hotbar tool while smoothly aiming at each block", false);
   private final com.larpclient.module.o p = this.addSetting(
      new com.larpclient.module.o("Width", "Tunnel cross section; 1x1 is a crawl tunnel that needs a crawling start", "1x2", "1x1", "1x2", "3x3")
   );
   private final com.larpclient.module.b O = this.g("Reach", "How far ahead to mine", 4.0, 2.0, 6.0, 1.0);
   private final com.larpclient.module.m N = this.C("Base finder", "Walk a fixed heading, avoid hazards and stop at nearby storage clusters", false);
   private final com.larpclient.module.b A = this.g("Hazard scan", "Distance ahead to inspect lava, water and drops", 6.0, 3.0, 12.0, 1.0);
   private final com.larpclient.module.m f = this.C("Avoid lava", "Avoid lava beside, above and below the tunnel", true);
   private final com.larpclient.module.m y = this.C("Avoid water", "Avoid water that would flood the tunnel", true);
   private final com.larpclient.module.m E = this.C("Avoid drops", "Avoid drops of two or more blocks", true);
   private final com.larpclient.module.m c = this.C("Avoid gravel", "Avoid falling sand and gravel above the tunnel", false);
   private final com.larpclient.module.b W = this.g("Detour", "Maximum sideways distance for a safe return to the main heading", 4.0, 2.0, 12.0, 1.0);
   private final com.larpclient.module.m Z = this.d("Chests", "Include chests and trapped chests", true);
   private final com.larpclient.module.m G = this.d("Barrels", "Include barrels", true);
   private final com.larpclient.module.m w = this.d("Ender Chests", "Include ender chests", true);
   private final com.larpclient.module.m V = this.d("Hoppers", "Include hoppers", true);
   private final com.larpclient.module.m a = this.d("Spawners", "Include mob spawners", true);
   private final com.larpclient.module.m K = this.d("Shulker Boxes", "Include uncolored and all colored shulker boxes", true);
   private final com.larpclient.module.b v = this.g(
      "Blocks to flag", "Selected storage blocks within 12 blocks of each other needed for a find", 5.0, 1.0, 64.0, 1.0
   );
   private final com.larpclient.module.m i = this.C("Skip dungeons", "Ignore spawners on cobblestone floors or near cobwebs", true);
   private final com.larpclient.module.b x = this.g("Ignore near start", "Ignore finds this close to the starting point", 48.0, 0.0, 256.0, 8.0);
   private final com.larpclient.module.o C = this.addSetting(
      new com.larpclient.module.o("On find", "Report and stop, or disconnect after reporting", "Notify and stop", "Notify and stop", "Leave") {
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
   private final com.larpclient.module.m t = this.C("Log finds", "Append time, server, dimension and coordinates to config/larp-client/bases.txt", true);
   private final com.larpclient.module.b I = this.g(
      "Save tool at", "Stop before the selected tool has fewer remaining uses; zero allows breaking", 30.0, 0.0, 200.0, 5.0
   );
   private final com.larpclient.module.b q = this.g("Stop at health", "Stop when health reaches this value; zero disables the limit", 8.0, 0.0, 20.0, 1.0);
   private final ha<LevelChunk> T = new ha<>();
   private final Map<Long, _n.l> L = new HashMap<>();
   private final List<BlockPos> J = new ArrayList<>();
   private final ArrayDeque<BlockPos> P = new ArrayDeque<>();
   private ClientLevel U;
   private BlockPos F;
   private Vec3 b;
   private Direction H;
   private Direction k;
   private BlockPos m;
   private BlockPos l;
   private final hw R = new hw();
   private final SplittableRandom u = new SplittableRandom();
   private Vec3 o = Vec3.ZERO;
   private boolean r;
   private boolean D;
   private int g;
   private BlockPos X;
   private Vec3 e = Vec3.ZERO;
   private double s = 0.6;
   private final ha.j<LevelChunk> d = new ha.j<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return _n.this.U.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         _n.l var3 = _n.this.L.get(ChunkPos.pack(var1, var2));
         return var3 == null || _n.mc.player.tickCount - var3.tick >= 80L;
      }

      public int b(LevelChunk var1) {
         return var1.getSections().length;
      }

      public boolean P(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];
         return !var3.hasOnlyAir() && var3.maybeHas(_n.this::q);
      }

      @Override
      public void begin() {
         _n.this.J.clear();
      }

      public void P(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];

         for (int var4 = 0; var4 < 16; var4++) {
            for (int var5 = 0; var5 < 16; var5++) {
               for (int var6 = 0; var6 < 16; var6++) {
                  BlockState var7 = var3.getBlockState(var6, var4, var5);
                  if (_n.this.q(var7) && _n.this.J.size() < 256) {
                     BlockPos var8 = new BlockPos(var1.getPos().getMinBlockX() + var6, var1.getMinY() + var2 * 16 + var4, var1.getPos().getMinBlockZ() + var5);
                     if (!(_n.this.F.distSqr(var8) < _n.this.x.getValue() * _n.this.x.getValue())
                        && (!_n.this.i.getValue() || !var7.is(Blocks.SPAWNER) || !_n.this.c(var8))) {
                        _n.this.J.add(var8);
                     }
                  }
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         _n.this.L.put(ChunkPos.pack(var1, var2), new _n.l(List.copyOf(_n.this.J), _n.mc.player.tickCount));
      }
   };

   private com.larpclient.module.m d(final String var1, String var2, boolean var3) {
      return this.addSetting(new com.larpclient.module.m("Storage " + var1, var2, var3) {
         @Override
         public String getDisplayName() {
            return "Storage: " + var1;
         }
      });
   }

   public _n() {
      super("Auto Tunnel", "Mine and walk a centered 1x1, 1x2 or 3x3 tunnel, including while menus are open");
      Y = this;

      for (com.larpclient.module.r var2 : List.of(this.A, this.W, this.Z, this.G, this.w, this.V, this.a, this.K, this.v, this.x, this.C, this.t)) {
         var2.a(this.N::getValue);
      }

      this.i.a(() -> this.N.getValue() && this.a.getValue());
      this.N.w(var1 -> this.U());
      this.p.w(var1 -> this.U());

      for (com.larpclient.module.m var4 : List.of(this.Z, this.G, this.w, this.V, this.a, this.K, this.i)) {
         var4.w(var1 -> {
            this.T.X();
            this.L.clear();
            this.J.clear();
         });
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.U();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.U();
   }

   @Override
   public boolean allowsScreenInteractions() {
      return true;
   }

   public static boolean P() {
      if (Y != null && Y.isEnabled()) {
         _n var0 = Y;
         Minecraft var1 = Minecraft.getInstance();
         return var0 != null && var0.isRuntimeActive() && _p.D(var0) && var1.gui.overlay() == null && var1.player.connection.hasClientLoaded();
      } else {
         return false;
      }
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (!this.D) {
            double var3 = Math.min(this.O.getValue(), mc.player.blockInteractionRange());
            Vec3 var5 = var1;
            if (this.l != null) {
               if (!this.l.equals(this.X)) {
                  this.X = this.l;
                  this.e = new Vec3(this.u.nextDouble(-0.22, 0.22), this.u.nextDouble(-0.22, 0.22), this.u.nextDouble(-0.22, 0.22));
               }

               Vec3 var6 = var1.add(this.e);
               if (vs.s(this.l, var6, var3)) {
                  var5 = var6;
               }
            }

            float[] var9 = b6.R(mc.player.getEyePosition(), var5);
            int var7 = this.l != null && vs.r(this.l, var3) == null ? 0 : 1;
            float[] var8 = this.R.M(mc.player.getYRot(), mc.player.getXRot(), var9[0], var9[1], (boolean)var7);
            b6.P(this, b6.w.y, var8[0], var8[1], 1.0);
         }
      }
   }

   private void U() {
      _p.e(this);
      vs.B(this);
      this.r = this.D = (boolean)0;
      this.k = this.H = null;
      this.F = null;
      this.b = null;
      this.m = this.l = this.X = null;
      this.o = Vec3.ZERO;
      this.R.W();
      this.s = this.u.nextDouble(0.35, 1.1);
      this.P.clear();
      this.T.X();
      this.L.clear();
      this.J.clear();
      this.U = null;
      this.g = 0;
   }

   @Override
   public void onTick() {
      this.r = (boolean)0;
      this.o = Vec3.ZERO;
      if (this.Z() && !this.W()) {
         byte var1 = this.y();
         byte var12 = this.D;
         byte var11 = var1;
         int var10000 = Integer.compare(var11, var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.D = (boolean)var1;
            _q.z(
               this.getName(),
               var1 != 0
                  ? "Inventory menu open; holding position and facing, mining only the block under the crosshair"
                  : "Inventory menu closed; resuming walking and aiming"
            );
            if (var1 == 0) {
               this.R.W();
            }
         }

         if (this.U != mc.level || this.b != null && mc.player.position().distanceToSqr(this.b) > 64.0) {
            this.U();
            this.U = mc.level;
            this.F = mc.player.blockPosition();
            this.H = Direction.fromYRot(mc.player.getYRot());
         }

         Direction var2 = this.H;
         if (this.q.getValue() > 0.0 && mc.player.getHealth() <= this.q.getValue()) {
            this.H("Low health");
         } else {
            int var19 = this.p.w("1x1");
            boolean var14 = false;
            byte var13 = (byte)var19;
            var19 = Integer.signum(var13);
            if (((var19 | -var19) >>> 31 ^ 1) == 0) {
               int var21 = mc.player.isVisuallyCrawling();
               boolean var16 = false;
               byte var15 = (byte)var21;
               var21 = Integer.signum(var15);
               if ((var21 | -var21) >>> 31 == 0) {
                  vs.B(this);
                  this.R.W();
                  this.l = null;
                  this.P("Waiting: 1x1 needs a crawling start, such as under a trapdoor");
                  return;
               }
            }

            int var23 = this.N.getValue();
            boolean var18 = false;
            byte var17 = (byte)var23;
            var23 = Integer.signum(var17);
            if (((var23 | -var23) >>> 31 ^ 1) == 0) {
               this.f();
               if (!this.isRuntimeActive()) {
                  return;
               }

               var2 = this.x();
               if (var2 == null) {
                  return;
               }
            }

            if (this.k != var2 || this.m == null) {
               this.k = var2;
               this.m = mc.player.blockPosition().relative(var2);
               this.l = null;
               this.R.W();
               _q.z(this.getName(), "Following {} through lane {}, {} at Y {} ({})", var2, this.m.getX(), this.m.getZ(), this.m.getY(), this.p.getValue());
            }

            this.m = e(this.m, mc.player.position(), var2, this.G(this.m, var2));
            String var3 = this.x(this.m, var2);
            if (var3 != null) {
               vs.B(this);
               this.R.W();
               this.l = null;
               this.P("Waiting: " + var3);
            } else {
               double var4 = Math.min(this.O.getValue(), mc.player.blockInteractionRange());
               BlockPos var6 = this.l(var2, var4);
               if (var6 != null) {
                  if (this.I(var6)) {
                     this.H("Tool nearly broken");
                     return;
                  }

                  BlockPos var7 = var6;
                  Direction var8 = var2;
                  BlockPos var9 = this.m;
                  boolean var10 = vs.I(
                     this,
                     var6,
                     var4,
                     true,
                     12.0F,
                     this.B.getValue() || this.N.getValue(),
                     () -> !this.W() && this.x(var9, var8) == null && (!this.N.getValue() || !mc.level.getBlockState(var7).hasBlockEntity())
                  );
                  if (!var10) {
                     this.P("Waiting for a reachable tunnel face");
                  }
               } else {
                  vs.B(this);
               }

               this.l(var2, var6);
            }
         }
      } else {
         vs.B(this);
         this.R.W();
         this.l = null;
         this.P(this.W() ? "Paused for manual movement" : "Waiting for world or interaction control");
      }
   }

   private void l(Direction var1, BlockPos var2) {
      Vec3 var3 = Vec3.atBottomCenterOf(this.m);
      boolean var4 = this.G(this.m, var1);
      double var5 = hw.r(mc.player.position(), var3, var1) - (var4 ? -0.5 : 0.85);
      Vec3 var7 = mc.player.getDeltaMovement();
      double var8 = var7.x * var1.getStepX() + var7.z * var1.getStepZ();
      Vec3 var10 = hw.N(mc.player.position(), var3, var1);
      boolean var11 = this.R.Y();
      int var12 = _p.Q(this) && !this.D && !dt.x() && this.B(o(var10)) ? 1 : 0;
      this.r = this.R.g(var5, var8, (boolean)var12);
      this.o = this.r ? var10 : Vec3.ZERO;
      if (this.r != var11) {
         _q.c(this.getName(), this.r ? "Walk keys pressed; {} clear ahead" : "Walk keys released; {} clear ahead", String.format(Locale.ROOT, "%.2f", var5));
      }

      if (var2 == null && _p.Q(this)) {
         Vec3 var13 = var3.add(var1.getStepX() * 6, 0.0, var1.getStepZ() * 6);
         if (mc.player.tickCount % 60 == 0 && this.u.nextInt(4) == 0) {
            this.s = this.u.nextDouble(0.35, 1.1);
         }

         this.aimAt(new Vec3(var13.x, mc.player.getEyeY() - this.s, var13.z), 12.0F);
      }

      if (this.b != null && mc.player.position().distanceToSqr(this.b) < 1.0E-4 && this.r && var2 == null) {
         this.g++;
      } else {
         this.g = 0;
      }

      this.b = mc.player.position();
      if (var2 == null) {
         this.P(
            this.D
               ? "Holding still while an inventory menu is open"
               : (this.r ? (this.N.getValue() ? "Searching for bases" : "Following the cleared tunnel") : "Waiting at a protected or unreachable obstruction")
         );
      }

      if (this.g > 160) {
         this.H("Movement blocked; clear the route or choose a new heading");
      }
   }

   public static BlockPos e(BlockPos var0, Vec3 var1, Direction var2, boolean var3) {
      return var3 && hw.r(var1, Vec3.atBottomCenterOf(var0), var2) < 0.12 ? var0.relative(var2) : var0;
   }

   private boolean y() {
      return mc.gui.screen() instanceof AbstractContainerScreen;
   }

   private boolean W() {
      return mc != null
         && mc.player != null
         && mc.isWindowActive()
         && mc.gui.screen() == null
         && (
            nz.K(mc.options.keyUp)
               || nz.K(mc.options.keyDown)
               || nz.K(mc.options.keyLeft)
               || nz.K(mc.options.keyRight)
               || nz.K(mc.options.keyJump)
               || nz.K(mc.options.keyShift)
         );
   }

   private List<BlockPos> x(BlockPos var1, Direction var2) {
      return v(var1, var2, this.p.getValue());
   }

   public static int w(String var0) {
      return switch (var0) {
         case "1x1" -> 1;
         case "3x3" -> 3;
         default -> 2;
      };
   }

   public static List<BlockPos> v(BlockPos var0, Direction var1, String var2) {
      boolean var3 = var2.equals("3x3");
      int var4 = w(var2);
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

   private BlockPos l(Direction var1, double var2) {
      for (int var4 = 0; var4 < Math.ceil(var2); var4++) {
         BlockPos var5 = this.m.relative(var1, var4);
         if (!this.P.isEmpty() && hw.r(Vec3.atBottomCenterOf(var5), Vec3.atBottomCenterOf((Vec3i)this.P.peekFirst()), var1) < -0.01
            || this.x(var5, var1) != null) {
            break;
         }

         List var6 = this.x(var5, var1).stream().filter(var3 -> this.J(var3, var2)).toList();
         if (!var6.isEmpty()) {
            if (this.l != null && var6.contains(this.l)) {
               return this.l;
            }

            this.l = var6.stream().min(Comparator.comparingDouble(this::Z)).orElseThrow();
            return this.l;
         }

         if (!this.G(var5, var1)) {
            break;
         }
      }

      this.l = null;
      return null;
   }

   private boolean J(BlockPos var1, double var2) {
      return vs.W(var1)
         && mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= var2 * var2
         && (!this.N.getValue() || !mc.level.getBlockState(var1).hasBlockEntity())
         && vs.W(this, var1, var2) != null;
   }

   private double Z(BlockPos var1) {
      float[] var2 = b6.R(mc.player.getEyePosition(), Vec3.atCenterOf(var1));
      return Math.hypot(Mth.wrapDegrees(var2[0] - mc.player.getYRot()), var2[1] - mc.player.getXRot());
   }

   private boolean G(BlockPos var1, Direction var2) {
      for (BlockPos var4 : this.x(var1, var2)) {
         if (!mc.level.hasChunkAt(var4) || !mc.level.getBlockState(var4).getCollisionShape(mc.level, var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static Vec3 o(Vec3 var0) {
      return var0.normalize().scale(0.55);
   }

   private boolean B(Vec3 var1) {
      Vec3 var2 = var1.scale(0.65);
      AABB var3 = mc.player.getBoundingBox().expandTowards(var2);
      if (!mc.level.noCollision(mc.player, var3)) {
         return false;
      }

      BlockPos var4 = BlockPos.containing(mc.player.position().add(var2));
      return this.E(var4) && this.x(var4, this.k) == null;
   }

   private boolean I(BlockPos var1) {
      if (this.I.getValue() <= 0.0) {
         return false;
      }

      BlockState var2 = mc.level.getBlockState(var1);
      ItemStack var3 = mc.player.getMainHandItem();
      float var4 = var3.getDestroySpeed(var2) + (var3.isCorrectToolForDrops(var2) ? 1000 : 0);

      for (int var5 = 0; (this.B.getValue() || this.N.getValue()) && var5 < 9; var5++) {
         ItemStack var6 = mc.player.getInventory().getItem(var5);
         float var7 = var6.getDestroySpeed(var2) + (var6.isCorrectToolForDrops(var2) ? 1000 : 0);
         if (var7 > var4) {
            var3 = var6;
            var4 = var7;
         }
      }

      return var3.isDamageableItem() && var3.getMaxDamage() - var3.getDamageValue() <= this.I.getValue();
   }

   private boolean E(BlockPos var1) {
      if (!mc.level.hasChunkAt(var1)) {
         return false;
      }

      int var2 = Math.min(w(this.p.getValue()), 2);

      for (int var3 = 0; var3 < var2; var3++) {
         if (!mc.level.getBlockState(var1.above(var3)).getCollisionShape(mc.level, var1.above(var3)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private String x(BlockPos var1, Direction var2) {
      if (!mc.level.hasChunkAt(var1)) {
         return "Waiting for loaded terrain";
      }

      for (BlockPos var4 : this.x(var1, var2)) {
         for (Direction var8 : Direction.values()) {
            BlockPos var9 = var4.relative(var8);
            if (!mc.level.hasChunkAt(var9)) {
               return "Waiting for loaded terrain";
            }

            FluidState var10 = mc.level.getFluidState(var9);
            if (this.f.getValue() && var10.is(FluidTags.LAVA)) {
               return "Lava";
            }

            if (this.y.getValue() && var10.is(FluidTags.WATER)) {
               return "Water";
            }
         }

         FluidState var13 = mc.level.getFluidState(var4);
         if (this.f.getValue() && var13.is(FluidTags.LAVA)) {
            return "Lava";
         }

         if (this.y.getValue() && var13.is(FluidTags.WATER)) {
            return "Water";
         }
      }

      if (this.E.getValue()
         && k(
            mc.level.getBlockState(var1.below()).getCollisionShape(mc.level, var1.below()).isEmpty(),
            mc.level.getBlockState(var1.below(2)).getCollisionShape(mc.level, var1.below(2)).isEmpty()
         )) {
         return "Deep drop";
      }

      if (this.c.getValue()) {
         for (BlockPos var12 : this.x(var1, var2)) {
            if (mc.level.getBlockState(var12.above()).getBlock() instanceof FallingBlock) {
               return "Falling sand or gravel";
            }
         }
      }

      return null;
   }

   private Direction x() {
      BlockPos var1 = mc.player.blockPosition();

      while (
         !this.P.isEmpty()
            && mc.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(Vec3.atBottomCenterOf((Vec3i)this.P.peekFirst()).multiply(1.0, 0.0, 1.0)) < 0.08
      ) {
         this.P.removeFirst();
      }

      if (!this.P.isEmpty()) {
         Direction var9 = var1.equals(this.P.peekFirst()) ? this.k : s(var1, this.P.peekFirst());
         String var11 = this.x(var1.relative(var9), var9);
         if (var11 != null) {
            this.H("Detour blocked by " + var11.toLowerCase(Locale.ROOT));
            return null;
         } else {
            return var9;
         }
      } else {
         String var2 = null;

         for (int var3 = 1; var3 <= this.A.getValue().intValue(); var3++) {
            var2 = this.x(var1.relative(this.H, var3), this.H);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            return this.H;
         }

         if (var2.equals("Waiting for loaded terrain")) {
            vs.B(this);
            this.P(var2);
            return null;
         }

         for (int var10 = 1; var10 <= this.W.getValue().intValue(); var10++) {
            for (Direction var5 : List.of(this.H.getClockWise(), this.H.getCounterClockWise())) {
               int var6 = this.A.getValue().intValue() + 2;
               BlockPos var7 = var1.relative(var5, var10);
               BlockPos var8 = var7.relative(this.H, var6);
               if (this.O(var1, var5, var10) && this.O(var7, this.H, var6) && this.O(var8, var5.getOpposite(), var10)) {
                  this.P.add(var7);
                  this.P.add(var8);
                  this.P.add(var1.relative(this.H, var6));
                  _q.z(this.getName(), "Routing {} blocks around {} using {}", var10, var2, var5);
                  this.P("Detouring around " + var2.toLowerCase(Locale.ROOT));
                  return var5;
               }
            }
         }

         this.H("No safe detour around " + var2.toLowerCase(Locale.ROOT));
         return null;
      }
   }

   private boolean O(BlockPos var1, Direction var2, int var3) {
      for (int var4 = 1; var4 <= var3; var4++) {
         if (this.x(var1.relative(var2, var4), var2) != null) {
            return false;
         }
      }

      return true;
   }

   private static Direction s(BlockPos var0, BlockPos var1) {
      int var2 = var1.getX() - var0.getX();
      int var3 = var1.getZ() - var0.getZ();
      return Math.abs(var2) > Math.abs(var3) ? (var2 > 0 ? Direction.EAST : Direction.WEST) : (var3 > 0 ? Direction.SOUTH : Direction.NORTH);
   }

   public static Input q() {
      _n var0 = Y;
      if (var0 != null && var0.Z() && var0.r && !var0.D && var0.k != null && !ij.y() && !dt.x() && !var0.W()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var0.U == var1.level && !(var0.o.lengthSqr() < 1.0E-6) && var0.B(o(var0.o)) && (!b6.x() || b6.c(b6.w.y))) {
            Input var2 = var0.R.q(var1.player.getYRot(), var0.o);
            return var2.backward() ? null : var2;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Vec2 P() {
      Input var0 = q();
      return var0 == null ? null : hw.w(var0);
   }

   public static boolean k(boolean var0, boolean var1) {
      return var0 && var1;
   }

   public static boolean V(BlockPos var0, BlockPos var1) {
      return Math.abs((long)var1.getX() - var0.getX()) <= 12L
         && Math.abs((long)var1.getY() - var0.getY()) <= 12L
         && Math.abs((long)var1.getZ() - var0.getZ()) <= 12L;
   }

   public static Vec2 F(float var0, Direction var1) {
      double var2 = Math.toRadians(var0 - var1.toYRot());
      return new Vec2((float)Math.sin(var2), (float)Math.cos(var2));
   }

   private boolean q(BlockState var1) {
      Block var2 = var1.getBlock();
      return this.Z.getValue() && (var2 == Blocks.CHEST || var2 == Blocks.TRAPPED_CHEST)
         || this.G.getValue() && var2 == Blocks.BARREL
         || this.w.getValue() && var2 == Blocks.ENDER_CHEST
         || this.V.getValue() && var2 == Blocks.HOPPER
         || this.a.getValue() && var2 == Blocks.SPAWNER
         || this.K.getValue() && var2 instanceof ShulkerBoxBlock;
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

   private void f() {
      ChunkPos var1 = mc.player.chunkPosition();
      int var2 = Math.clamp(mc.options.getEffectiveRenderDistance(), 1, 12);
      this.L.entrySet().removeIf(var3x -> {
         ChunkPos var4 = ChunkPos.unpack(var3x.getKey());
         return Math.abs(var4.x() - var1.x()) > var2 || Math.abs(var4.z() - var1.z()) > var2 || this.U.getChunkSource().getChunkNow(var4.x(), var4.z()) == null;
      });
      this.T.b(this.d, var1.x(), var1.z(), var2, 2);
      if (mc.player.tickCount % 10 == 0) {
         ArrayList var3 = new ArrayList();

         for (_n.l var5 : this.L.values()) {
            for (BlockPos var7 : var5.positions) {
               if (var3.size() >= 4096) {
                  break;
               }

               if (mc.level.hasChunkAt(var7)
                  && this.q(mc.level.getBlockState(var7))
                  && this.F.distSqr(var7) >= this.x.getValue() * this.x.getValue()
                  && (!this.i.getValue() || !mc.level.getBlockState(var7).is(Blocks.SPAWNER) || !this.c(var7))) {
                  var3.add(var7);
               }
            }
         }

         HashMap var16 = new HashMap();

         for (BlockPos var19 : var3) {
            var16.computeIfAbsent(H(var19.getX(), var19.getY(), var19.getZ()), var0 -> new ArrayList<>()).add(var19);
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

            if (var21.size() >= this.v.getValue()) {
               this.G(var20, var21);
               return;
            }
         }
      }
   }

   private static String H(int var0, int var1, int var2) {
      return Math.floorDiv(var0, 12) + ":" + Math.floorDiv(var1, 12) + ":" + Math.floorDiv(var2, 12);
   }

   private void G(BlockPos var1, List<BlockPos> var2) {
      TreeMap var3 = new TreeMap();

      for (BlockPos var5 : var2) {
         var3.merge(BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var5).getBlock()).getPath(), 1, Integer::sum);
      }

      String var11 = var1.getX() + ", " + var1.getY() + ", " + var1.getZ();
      String var12 = var11 + " (" + var3.entrySet().stream().map(var0 -> var0.getValue() + " " + (String)var0.getKey()).collect(Collectors.joining(", ")) + ")";
      mc.player.sendSystemMessage(Component.literal("Larp Auto Tunnel: base found at " + var12));
      ib.L().N("Base found", var12, ib.v.x, 10000L);
      mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.4F));
      _q.z(this.getName(), "Base found at {}", var12);
      if (this.t.getValue()) {
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
            _q.o(this.getName(), "Could not append base find to " + var6, var10);
         }
      }

      boolean var13 = this.C.w("Leave");
      this.disable();
      if (var13) {
         mc.disconnectFromWorld(Component.literal("Larp Auto Tunnel found a base at " + var12));
      }
   }

   private void H(String var1) {
      _q.z(this.getName(), "Stopped: {}", var1);
      mc.player.sendSystemMessage(Component.literal("Larp Auto Tunnel stopped: " + var1));
      ib.L().t("Auto Tunnel stopped", var1, ib.v.o);
      this.disable();
      this.P(var1);
   }

   private record l(List<BlockPos> positions, long tick) {
   }
}
