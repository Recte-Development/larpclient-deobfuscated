package com.larpclient;

import com.larpclient.module.df;
import com.larpclient.module.dh;
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

public final class wq extends kl {
   private static wq s;
   private final dh X = this.Y("Legit", "Select the best hotbar tool while smoothly aiming at each block", false);
   private final com.larpclient.module.d6 J = this.addSetting(
      new com.larpclient.module.d6("Width", "Tunnel cross section; 1x1 is a crawl tunnel that needs a crawling start", "1x2", "1x1", "1x2", "3x3")
   );
   private final com.larpclient.module.d9 w = this.H("Reach", "How far ahead to mine", 4.0, 2.0, 6.0, 1.0);
   private final dh d = this.Y("Base finder", "Walk a fixed heading, avoid hazards and stop at nearby storage clusters", false);
   private final com.larpclient.module.d9 M = this.H("Hazard scan", "Distance ahead to inspect lava, water and drops", 6.0, 3.0, 12.0, 1.0);
   private final dh T = this.Y("Avoid lava", "Avoid lava beside, above and below the tunnel", true);
   private final dh v = this.Y("Avoid water", "Avoid water that would flood the tunnel", true);
   private final dh p = this.Y("Avoid drops", "Avoid drops of two or more blocks", true);
   private final dh q = this.Y("Avoid gravel", "Avoid falling sand and gravel above the tunnel", false);
   private final com.larpclient.module.d9 j = this.H("Detour", "Maximum sideways distance for a safe return to the main heading", 4.0, 2.0, 12.0, 1.0);
   private final dh V = this.k("Chests", "Include chests and trapped chests", true);
   private final dh t = this.k("Barrels", "Include barrels", true);
   private final dh k = this.k("Ender Chests", "Include ender chests", true);
   private final dh g = this.k("Hoppers", "Include hoppers", true);
   private final dh x = this.k("Spawners", "Include mob spawners", true);
   private final dh S = this.k("Shulker Boxes", "Include uncolored and all colored shulker boxes", true);
   private final com.larpclient.module.d9 U = this.H(
      "Blocks to flag", "Selected storage blocks within 12 blocks of each other needed for a find", 5.0, 1.0, 64.0, 1.0
   );
   private final dh C = this.Y("Skip dungeons", "Ignore spawners on cobblestone floors or near cobwebs", true);
   private final com.larpclient.module.d9 N = this.H("Ignore near start", "Ignore finds this close to the starting point", 48.0, 0.0, 256.0, 8.0);
   private final com.larpclient.module.d6 L = this.addSetting(
      new com.larpclient.module.d6("On find", "Report and stop, or disconnect after reporting", "Notify and stop", "Notify and stop", "Leave") {
         @Override
         public String getModeLabel(String var1) {
            return var1.equals("Notify and stop") ? "Notify & stop" : var1;
         }

         @Override
         public String getValueAsString() {
            return this.getModeLabel(this.P());
         }
      }
   );
   private final dh I = this.Y("Log finds", "Append time, server, dimension and coordinates to config/larp-client/bases.txt", true);
   private final com.larpclient.module.d9 o = this.H(
      "Save tool at", "Stop before the selected tool has fewer remaining uses; zero allows breaking", 30.0, 0.0, 200.0, 5.0
   );
   private final com.larpclient.module.d9 h = this.H("Stop at health", "Stop when health reaches this value; zero disables the limit", 8.0, 0.0, 20.0, 1.0);
   private final pt<LevelChunk> F = new pt<>();
   private final Map<Long, wq.f> O = new HashMap<>();
   private final List<BlockPos> Y = new ArrayList<>();
   private final ArrayDeque<BlockPos> G = new ArrayDeque<>();
   private ClientLevel R;
   private BlockPos a;
   private Vec3 b;
   private Direction e;
   private Direction f;
   private BlockPos u;
   private BlockPos D;
   private final ob n = new ob();
   private final SplittableRandom r = new SplittableRandom();
   private Vec3 i = Vec3.ZERO;
   private boolean Z;
   private boolean m;
   private int y;
   private BlockPos B;
   private Vec3 Q = Vec3.ZERO;
   private double P = 0.6;
   private final pt.d<LevelChunk> H = new pt.d<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return wq.this.R.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         wq.f var3 = wq.this.O.get(ChunkPos.pack(var1, var2));
         return var3 == null || wq.mc.player.tickCount - var3.tick >= 80L;
      }

      public int u(LevelChunk var1) {
         return var1.getSections().length;
      }

      public boolean t(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];
         return !var3.hasOnlyAir() && var3.maybeHas(wq.this::X);
      }

      @Override
      public void begin() {
         wq.this.Y.clear();
      }

      public void O(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];

         for (int var4 = 0; var4 < 16; var4++) {
            for (int var5 = 0; var5 < 16; var5++) {
               for (int var6 = 0; var6 < 16; var6++) {
                  BlockState var7 = var3.getBlockState(var6, var4, var5);
                  if (wq.this.X(var7) && wq.this.Y.size() < 256) {
                     BlockPos var8 = new BlockPos(var1.getPos().getMinBlockX() + var6, var1.getMinY() + var2 * 16 + var4, var1.getPos().getMinBlockZ() + var5);
                     if (!(wq.this.a.distSqr(var8) < wq.this.N.P() * wq.this.N.P()) && (!wq.this.C.P() || !var7.is(Blocks.SPAWNER) || !wq.this.x(var8))) {
                        wq.this.Y.add(var8);
                     }
                  }
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         wq.this.O.put(ChunkPos.pack(var1, var2), new wq.f(List.copyOf(wq.this.Y), wq.mc.player.tickCount));
      }
   };

   private dh k(final String var1, String var2, boolean var3) {
      return this.addSetting(new dh("Storage " + var1, var2, var3) {
         @Override
         public String getDisplayName() {
            return "Storage: " + var1;
         }
      });
   }

   public wq() {
      super("Auto Tunnel", "Mine and walk a centered 1x1, 1x2 or 3x3 tunnel, including while menus are open");
      s = this;

      for (df var2 : List.of(this.M, this.j, this.V, this.t, this.k, this.g, this.x, this.S, this.U, this.N, this.L, this.I)) {
         var2.H(this.d::P);
      }

      this.C.H(() -> this.d.P() && this.x.P());
      this.d.h(var1 -> this.P());
      this.J.h(var1 -> this.P());

      for (dh var4 : List.of(this.V, this.t, this.k, this.g, this.x, this.S, this.C)) {
         var4.h(var1 -> {
            this.F.r();
            this.O.clear();
            this.Y.clear();
         });
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.P();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.P();
   }

   @Override
   public boolean allowsScreenInteractions() {
      return true;
   }

   public static boolean q() {
      if (s != null && s.isEnabled()) {
         wq var0 = s;
         Minecraft var1 = Minecraft.getInstance();
         return var0 != null && var0.isRuntimeActive() && _m.n(var0) && var1.gui.overlay() == null && var1.player.connection.hasClientLoaded();
      } else {
         return false;
      }
   }

   @Override
   void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (!this.m) {
            double var3 = Math.min(this.w.P(), mc.player.blockInteractionRange());
            Vec3 var5 = var1;
            if (this.D != null) {
               if (!this.D.equals(this.B)) {
                  this.B = this.D;
                  this.Q = new Vec3(this.r.nextDouble(-0.22, 0.22), this.r.nextDouble(-0.22, 0.22), this.r.nextDouble(-0.22, 0.22));
               }

               Vec3 var6 = var1.add(this.Q);
               if (wa.w(this.D, var6, var3)) {
                  var5 = var6;
               }
            }

            float[] var9 = m9.Z(mc.player.getEyePosition(), var5);
            int var7 = this.D != null && wa.j(this.D, var3) == null ? 0 : 1;
            float[] var8 = this.n.Z(mc.player.getYRot(), mc.player.getXRot(), var9[0], var9[1], (boolean)var7);
            m9.k(this, m9.s.u, var8[0], var8[1], 1.0);
         }
      }
   }

   private void P() {
      _m.w(this);
      wa.N(this);
      this.Z = this.m = (boolean)0;
      this.f = this.e = null;
      this.a = null;
      this.b = null;
      this.u = this.D = this.B = null;
      this.i = Vec3.ZERO;
      this.n.x();
      this.P = this.r.nextDouble(0.35, 1.1);
      this.G.clear();
      this.F.r();
      this.O.clear();
      this.Y.clear();
      this.R = null;
      this.y = 0;
   }

   @Override
   public void onTick() {
      this.Z = (boolean)0;
      this.i = Vec3.ZERO;
      int var10000 = this.N();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && !this.S()) {
         byte var1 = this.r();
         byte var14 = this.m;
         byte var13 = var1;
         var10000 = Integer.compare(var13, var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.m = (boolean)var1;
            mt.K(
               this.getName(),
               var1 != 0
                  ? "Inventory menu open; holding position and facing, mining only the block under the crosshair"
                  : "Inventory menu closed; resuming walking and aiming"
            );
            if (var1 == 0) {
               this.n.x();
            }
         }

         if (this.R != mc.level || this.b != null && mc.player.position().distanceToSqr(this.b) > 64.0) {
            this.P();
            this.R = mc.level;
            this.a = mc.player.blockPosition();
            this.e = Direction.fromYRot(mc.player.getYRot());
         }

         Direction var2 = this.e;
         if (this.h.P() > 0.0 && mc.player.getHealth() <= this.h.P()) {
            this.n("Low health");
         } else if (this.J.S("1x1") && !mc.player.isVisuallyCrawling()) {
            wa.N(this);
            this.n.x();
            this.D = null;
            this.w("Waiting: 1x1 needs a crawling start, such as under a trapdoor");
         } else {
            int var21 = this.d.P();
            boolean var16 = false;
            byte var15 = (byte)var21;
            var21 = Integer.signum(var15);
            if (((var21 | -var21) >>> 31 ^ 1) == 0) {
               this.p();
               if (!this.isRuntimeActive()) {
                  return;
               }

               var2 = this.E();
               if (var2 == null) {
                  return;
               }
            }

            if (this.f != var2 || this.u == null) {
               this.f = var2;
               this.u = mc.player.blockPosition().relative(var2);
               this.D = null;
               this.n.x();
               mt.K(this.getName(), "Following {} through lane {}, {} at Y {} ({})", var2, this.u.getX(), this.u.getZ(), this.u.getY(), this.J.P());
            }

            this.u = B(this.u, mc.player.position(), var2, this.z(this.u, var2));
            String var3 = this.g(this.u, var2);
            if (var3 != null) {
               wa.N(this);
               this.n.x();
               this.D = null;
               this.w("Waiting: " + var3);
            } else {
               double var4 = Math.min(this.w.P(), mc.player.blockInteractionRange());
               BlockPos var6 = this.t(var2, var4);
               if (var6 != null) {
                  if (this.v(var6)) {
                     this.n("Tool nearly broken");
                     return;
                  }

                  BlockPos var7 = var6;
                  Direction var8 = var2;
                  BlockPos var9 = this.u;
                  int var10005 = this.X.P();
                  boolean var18 = false;
                  byte var17 = (byte)var10005;
                  var10005 = Integer.signum(var17);
                  boolean var10 = wa.R(
                     this,
                     var6,
                     var4,
                     true,
                     12.0F,
                     (var10005 | -var10005) >>> 31 != 0 || this.d.P(),
                     () -> !this.S() && this.g(var9, var8) == null && (!this.d.P() || !mc.level.getBlockState(var7).hasBlockEntity())
                  );
                  if (!var10) {
                     this.w("Waiting for a reachable tunnel face");
                  }
               } else {
                  wa.N(this);
               }

               this.d(var2, var6);
            }
         }
      } else {
         wa.N(this);
         this.n.x();
         this.D = null;
         this.w(this.S() ? "Paused for manual movement" : "Waiting for world or interaction control");
      }
   }

   private void d(Direction var1, BlockPos var2) {
      Vec3 var3 = Vec3.atBottomCenterOf(this.u);
      boolean var4 = this.z(this.u, var1);
      double var5 = ob.a(mc.player.position(), var3, var1) - (var4 ? -0.5 : 0.85);
      Vec3 var7 = mc.player.getDeltaMovement();
      double var8 = var7.x * var1.getStepX() + var7.z * var1.getStepZ();
      Vec3 var10 = ob.U(mc.player.position(), var3, var1);
      boolean var11 = this.n.c();
      int var12 = _m.f(this) && !this.m && !ks.N() && this.D(V(var10)) ? 1 : 0;
      this.Z = this.n.d(var5, var8, (boolean)var12);
      this.i = this.Z ? var10 : Vec3.ZERO;
      if (this.Z != var11) {
         mt.U(this.getName(), this.Z ? "Walk keys pressed; {} clear ahead" : "Walk keys released; {} clear ahead", String.format(Locale.ROOT, "%.2f", var5));
      }

      if (var2 == null && _m.f(this)) {
         Vec3 var13 = var3.add(var1.getStepX() * 6, 0.0, var1.getStepZ() * 6);
         if (mc.player.tickCount % 60 == 0 && this.r.nextInt(4) == 0) {
            this.P = this.r.nextDouble(0.35, 1.1);
         }

         this.aimAt(new Vec3(var13.x, mc.player.getEyeY() - this.P, var13.z), 12.0F);
      }

      if (this.b != null && mc.player.position().distanceToSqr(this.b) < 1.0E-4 && this.Z && var2 == null) {
         this.y++;
      } else {
         this.y = 0;
      }

      this.b = mc.player.position();
      if (var2 == null) {
         this.w(
            this.m
               ? "Holding still while an inventory menu is open"
               : (this.Z ? (this.d.P() ? "Searching for bases" : "Following the cleared tunnel") : "Waiting at a protected or unreachable obstruction")
         );
      }

      if (this.y > 160) {
         this.n("Movement blocked; clear the route or choose a new heading");
      }
   }

   public static BlockPos B(BlockPos var0, Vec3 var1, Direction var2, boolean var3) {
      return var3 && ob.a(var1, Vec3.atBottomCenterOf(var0), var2) < 0.12 ? var0.relative(var2) : var0;
   }

   private boolean r() {
      return mc.gui.screen() instanceof AbstractContainerScreen;
   }

   private boolean S() {
      return mc != null
         && mc.player != null
         && mc.isWindowActive()
         && mc.gui.screen() == null
         && (
            te.e(mc.options.keyUp)
               || te.e(mc.options.keyDown)
               || te.e(mc.options.keyLeft)
               || te.e(mc.options.keyRight)
               || te.e(mc.options.keyJump)
               || te.e(mc.options.keyShift)
         );
   }

   private List<BlockPos> y(BlockPos var1, Direction var2) {
      return P(var1, var2, this.J.P());
   }

   public static int A(String var0) {
      return switch (var0) {
         case "1x1" -> 1;
         case "3x3" -> 3;
         default -> 2;
      };
   }

   public static List<BlockPos> P(BlockPos var0, Direction var1, String var2) {
      boolean var3 = var2.equals("3x3");
      int var4 = A(var2);
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

   private BlockPos t(Direction var1, double var2) {
      for (int var4 = 0; var4 < Math.ceil(var2); var4++) {
         BlockPos var5 = this.u.relative(var1, var4);
         if (!this.G.isEmpty() && ob.a(Vec3.atBottomCenterOf(var5), Vec3.atBottomCenterOf((Vec3i)this.G.peekFirst()), var1) < -0.01
            || this.g(var5, var1) != null) {
            break;
         }

         List var6 = this.y(var5, var1).stream().filter(var3 -> this.U(var3, var2)).toList();
         if (!var6.isEmpty()) {
            if (this.D != null && var6.contains(this.D)) {
               return this.D;
            }

            this.D = var6.stream().min(Comparator.comparingDouble(this::l)).orElseThrow();
            return this.D;
         }

         if (!this.z(var5, var1)) {
            break;
         }
      }

      this.D = null;
      return null;
   }

   private boolean U(BlockPos var1, double var2) {
      return wa.U(var1)
         && mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= var2 * var2
         && (!this.d.P() || !mc.level.getBlockState(var1).hasBlockEntity())
         && wa.U(this, var1, var2) != null;
   }

   private double l(BlockPos var1) {
      float[] var2 = m9.Z(mc.player.getEyePosition(), Vec3.atCenterOf(var1));
      return Math.hypot(Mth.wrapDegrees(var2[0] - mc.player.getYRot()), var2[1] - mc.player.getXRot());
   }

   private boolean z(BlockPos var1, Direction var2) {
      for (BlockPos var4 : this.y(var1, var2)) {
         if (!mc.level.hasChunkAt(var4) || !mc.level.getBlockState(var4).getCollisionShape(mc.level, var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static Vec3 V(Vec3 var0) {
      return var0.normalize().scale(0.55);
   }

   private boolean D(Vec3 var1) {
      Vec3 var2 = var1.scale(0.65);
      AABB var3 = mc.player.getBoundingBox().expandTowards(var2);
      if (!mc.level.noCollision(mc.player, var3)) {
         return false;
      }

      BlockPos var4 = BlockPos.containing(mc.player.position().add(var2));
      return this.a(var4) && this.g(var4, this.f) == null;
   }

   private boolean v(BlockPos var1) {
      if (this.o.P() <= 0.0) {
         return false;
      }

      BlockState var2 = mc.level.getBlockState(var1);
      ItemStack var3 = mc.player.getMainHandItem();
      float var4 = var3.getDestroySpeed(var2) + (var3.isCorrectToolForDrops(var2) ? 1000 : 0);

      for (int var5 = 0; (this.X.P() || this.d.P()) && var5 < 9; var5++) {
         ItemStack var6 = mc.player.getInventory().getItem(var5);
         float var7 = var6.getDestroySpeed(var2) + (var6.isCorrectToolForDrops(var2) ? 1000 : 0);
         if (var7 > var4) {
            var3 = var6;
            var4 = var7;
         }
      }

      return var3.isDamageableItem() && var3.getMaxDamage() - var3.getDamageValue() <= this.o.P();
   }

   private boolean a(BlockPos var1) {
      if (!mc.level.hasChunkAt(var1)) {
         return false;
      }

      int var2 = Math.min(A(this.J.P()), 2);

      for (int var3 = 0; var3 < var2; var3++) {
         if (!mc.level.getBlockState(var1.above(var3)).getCollisionShape(mc.level, var1.above(var3)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private String g(BlockPos var1, Direction var2) {
      if (!mc.level.hasChunkAt(var1)) {
         return "Waiting for loaded terrain";
      }

      for (BlockPos var4 : this.y(var1, var2)) {
         for (Direction var8 : Direction.values()) {
            BlockPos var9 = var4.relative(var8);
            if (!mc.level.hasChunkAt(var9)) {
               return "Waiting for loaded terrain";
            }

            FluidState var10 = mc.level.getFluidState(var9);
            if (this.T.P() && var10.is(FluidTags.LAVA)) {
               return "Lava";
            }

            if (this.v.P() && var10.is(FluidTags.WATER)) {
               return "Water";
            }
         }

         FluidState var13 = mc.level.getFluidState(var4);
         if (this.T.P() && var13.is(FluidTags.LAVA)) {
            return "Lava";
         }

         if (this.v.P() && var13.is(FluidTags.WATER)) {
            return "Water";
         }
      }

      if (this.p.P()
         && B(
            mc.level.getBlockState(var1.below()).getCollisionShape(mc.level, var1.below()).isEmpty(),
            mc.level.getBlockState(var1.below(2)).getCollisionShape(mc.level, var1.below(2)).isEmpty()
         )) {
         return "Deep drop";
      }

      if (this.q.P()) {
         for (BlockPos var12 : this.y(var1, var2)) {
            if (mc.level.getBlockState(var12.above()).getBlock() instanceof FallingBlock) {
               return "Falling sand or gravel";
            }
         }
      }

      return null;
   }

   private Direction E() {
      BlockPos var1 = mc.player.blockPosition();

      while (
         !this.G.isEmpty()
            && mc.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(Vec3.atBottomCenterOf((Vec3i)this.G.peekFirst()).multiply(1.0, 0.0, 1.0)) < 0.08
      ) {
         this.G.removeFirst();
      }

      if (!this.G.isEmpty()) {
         Direction var9 = var1.equals(this.G.peekFirst()) ? this.f : Y(var1, this.G.peekFirst());
         String var11 = this.g(var1.relative(var9), var9);
         if (var11 != null) {
            this.n("Detour blocked by " + var11.toLowerCase(Locale.ROOT));
            return null;
         } else {
            return var9;
         }
      } else {
         String var2 = null;

         for (int var3 = 1; var3 <= this.M.P().intValue(); var3++) {
            var2 = this.g(var1.relative(this.e, var3), this.e);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            return this.e;
         }

         if (var2.equals("Waiting for loaded terrain")) {
            wa.N(this);
            this.w(var2);
            return null;
         }

         for (int var10 = 1; var10 <= this.j.P().intValue(); var10++) {
            for (Direction var5 : List.of(this.e.getClockWise(), this.e.getCounterClockWise())) {
               int var6 = this.M.P().intValue() + 2;
               BlockPos var7 = var1.relative(var5, var10);
               BlockPos var8 = var7.relative(this.e, var6);
               if (this.e(var1, var5, var10) && this.e(var7, this.e, var6) && this.e(var8, var5.getOpposite(), var10)) {
                  this.G.add(var7);
                  this.G.add(var8);
                  this.G.add(var1.relative(this.e, var6));
                  mt.K(this.getName(), "Routing {} blocks around {} using {}", var10, var2, var5);
                  this.w("Detouring around " + var2.toLowerCase(Locale.ROOT));
                  return var5;
               }
            }
         }

         this.n("No safe detour around " + var2.toLowerCase(Locale.ROOT));
         return null;
      }
   }

   private boolean e(BlockPos var1, Direction var2, int var3) {
      for (int var4 = 1; var4 <= var3; var4++) {
         if (this.g(var1.relative(var2, var4), var2) != null) {
            return false;
         }
      }

      return true;
   }

   private static Direction Y(BlockPos var0, BlockPos var1) {
      int var2 = var1.getX() - var0.getX();
      int var3 = var1.getZ() - var0.getZ();
      return Math.abs(var2) > Math.abs(var3) ? (var2 > 0 ? Direction.EAST : Direction.WEST) : (var3 > 0 ? Direction.SOUTH : Direction.NORTH);
   }

   public static Input z() {
      wq var0 = s;
      if (var0 != null && var0.N() && var0.Z && !var0.m && var0.f != null && !tq.f() && !ks.N() && !var0.S()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var0.R == var1.level && !(var0.i.lengthSqr() < 1.0E-6) && var0.D(V(var0.i)) && (!m9.R() || m9.F(m9.s.u))) {
            Input var2 = var0.n.M(var1.player.getYRot(), var0.i);
            return var2.backward() ? null : var2;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Vec2 g() {
      Input var0 = z();
      return var0 == null ? null : ob.M(var0);
   }

   public static boolean B(boolean var0, boolean var1) {
      return var0 && var1;
   }

   public static boolean y(BlockPos var0, BlockPos var1) {
      return Math.abs((long)var1.getX() - var0.getX()) <= 12L
         && Math.abs((long)var1.getY() - var0.getY()) <= 12L
         && Math.abs((long)var1.getZ() - var0.getZ()) <= 12L;
   }

   public static Vec2 a(float var0, Direction var1) {
      double var2 = Math.toRadians(var0 - var1.toYRot());
      return new Vec2((float)Math.sin(var2), (float)Math.cos(var2));
   }

   private boolean X(BlockState var1) {
      Block var2 = var1.getBlock();
      return this.V.P() && (var2 == Blocks.CHEST || var2 == Blocks.TRAPPED_CHEST)
         || this.t.P() && var2 == Blocks.BARREL
         || this.k.P() && var2 == Blocks.ENDER_CHEST
         || this.g.P() && var2 == Blocks.HOPPER
         || this.x.P() && var2 == Blocks.SPAWNER
         || this.S.P() && var2 instanceof ShulkerBoxBlock;
   }

   private boolean x(BlockPos var1) {
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

   private void p() {
      ChunkPos var1 = mc.player.chunkPosition();
      int var2 = Math.clamp(mc.options.getEffectiveRenderDistance(), 1, 12);
      this.O.entrySet().removeIf(var3x -> {
         ChunkPos var4 = ChunkPos.unpack(var3x.getKey());
         return Math.abs(var4.x() - var1.x()) > var2 || Math.abs(var4.z() - var1.z()) > var2 || this.R.getChunkSource().getChunkNow(var4.x(), var4.z()) == null;
      });
      this.F.k(this.H, var1.x(), var1.z(), var2, 2);
      if (mc.player.tickCount % 10 == 0) {
         ArrayList var3 = new ArrayList();

         for (wq.f var5 : this.O.values()) {
            for (BlockPos var7 : var5.positions) {
               if (var3.size() >= 4096) {
                  break;
               }

               if (mc.level.hasChunkAt(var7)
                  && this.X(mc.level.getBlockState(var7))
                  && this.a.distSqr(var7) >= this.N.P() * this.N.P()
                  && (!this.C.P() || !mc.level.getBlockState(var7).is(Blocks.SPAWNER) || !this.x(var7))) {
                  var3.add(var7);
               }
            }
         }

         HashMap var16 = new HashMap();

         for (BlockPos var19 : var3) {
            var16.computeIfAbsent(Q(var19.getX(), var19.getY(), var19.getZ()), var0 -> new ArrayList<>()).add(var19);
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
                        if (y(var20, var15)) {
                           var21.add(var15);
                        }
                     }
                  }
               }
            }

            if (var21.size() >= this.U.P()) {
               this.d(var20, var21);
               return;
            }
         }
      }
   }

   private static String Q(int var0, int var1, int var2) {
      return Math.floorDiv(var0, 12) + ":" + Math.floorDiv(var1, 12) + ":" + Math.floorDiv(var2, 12);
   }

   private void d(BlockPos var1, List<BlockPos> var2) {
      TreeMap var3 = new TreeMap();

      for (BlockPos var5 : var2) {
         var3.merge(BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var5).getBlock()).getPath(), 1, Integer::sum);
      }

      String var11 = var1.getX() + ", " + var1.getY() + ", " + var1.getZ();
      String var12 = var11 + " (" + var3.entrySet().stream().map(var0 -> var0.getValue() + " " + (String)var0.getKey()).collect(Collectors.joining(", ")) + ")";
      mc.player.sendSystemMessage(Component.literal("Larp Auto Tunnel: base found at " + var12));
      oj.K().g("Base found", var12, oj.u.j, 10000L);
      mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.4F));
      mt.K(this.getName(), "Base found at {}", var12);
      if (this.I.P()) {
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
            mt.P(this.getName(), "Could not append base find to " + var6, var10);
         }
      }

      boolean var13 = this.L.S("Leave");
      this.disable();
      if (var13) {
         mc.disconnectFromWorld(Component.literal("Larp Auto Tunnel found a base at " + var12));
      }
   }

   private void n(String var1) {
      mt.K(this.getName(), "Stopped: {}", var1);
      mc.player.sendSystemMessage(Component.literal("Larp Auto Tunnel stopped: " + var1));
      oj.K().x("Auto Tunnel stopped", var1, oj.u.B);
      this.disable();
      this.w(var1);
   }

   private record f(List<BlockPos> positions, long tick) {
   }
}
