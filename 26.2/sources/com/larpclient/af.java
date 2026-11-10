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

public final class af extends ar {
   private static af A;
   private final com.larpclient.module.s O = this.U("Legit", "Select the best hotbar tool while smoothly aiming at each block", false);
   private final com.larpclient.module.l j = this.addSetting(
      new com.larpclient.module.l("Width", "Tunnel cross section; 1x1 is a crawl tunnel that needs a crawling start", "1x2", "1x1", "1x2", "3x3")
   );
   private final com.larpclient.module.e w = this.Z("Reach", "How far ahead to mine", 4.0, 2.0, 6.0, 1.0);
   private final com.larpclient.module.s d = this.U("Base finder", "Walk a fixed heading, avoid hazards and stop at nearby storage clusters", false);
   private final com.larpclient.module.e u = this.Z("Hazard scan", "Distance ahead to inspect lava, water and drops", 6.0, 3.0, 12.0, 1.0);
   private final com.larpclient.module.s P = this.U("Avoid lava", "Avoid lava beside, above and below the tunnel", true);
   private final com.larpclient.module.s Z = this.U("Avoid water", "Avoid water that would flood the tunnel", true);
   private final com.larpclient.module.s Y = this.U("Avoid drops", "Avoid drops of two or more blocks", true);
   private final com.larpclient.module.s n = this.U("Avoid gravel", "Avoid falling sand and gravel above the tunnel", false);
   private final com.larpclient.module.e R = this.Z("Detour", "Maximum sideways distance for a safe return to the main heading", 4.0, 2.0, 12.0, 1.0);
   private final com.larpclient.module.s W = this.j("Chests", "Include chests and trapped chests", true);
   private final com.larpclient.module.s e = this.j("Barrels", "Include barrels", true);
   private final com.larpclient.module.s G = this.j("Ender Chests", "Include ender chests", true);
   private final com.larpclient.module.s r = this.j("Hoppers", "Include hoppers", true);
   private final com.larpclient.module.s p = this.j("Spawners", "Include mob spawners", true);
   private final com.larpclient.module.s c = this.j("Shulker Boxes", "Include uncolored and all colored shulker boxes", true);
   private final com.larpclient.module.e x = this.Z(
      "Blocks to flag", "Selected storage blocks within 12 blocks of each other needed for a find", 5.0, 1.0, 64.0, 1.0
   );
   private final com.larpclient.module.s L = this.U("Skip dungeons", "Ignore spawners on cobblestone floors or near cobwebs", true);
   private final com.larpclient.module.e Q = this.Z("Ignore near start", "Ignore finds this close to the starting point", 48.0, 0.0, 256.0, 8.0);
   private final com.larpclient.module.l s = this.addSetting(
      new com.larpclient.module.l("On find", "Report and stop, or disconnect after reporting", "Notify and stop", "Notify and stop", "Leave") {
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
   private final com.larpclient.module.s I = this.U("Log finds", "Append time, server, dimension and coordinates to config/larp-client/bases.txt", true);
   private final com.larpclient.module.e a = this.Z(
      "Save tool at", "Stop before the selected tool has fewer remaining uses; zero allows breaking", 30.0, 0.0, 200.0, 5.0
   );
   private final com.larpclient.module.e i = this.Z("Stop at health", "Stop when health reaches this value; zero disables the limit", 8.0, 0.0, 20.0, 1.0);
   private final tu<LevelChunk> E = new tu<>();
   private final Map<Long, af.x> f = new HashMap<>();
   private final List<BlockPos> o = new ArrayList<>();
   private final ArrayDeque<BlockPos> F = new ArrayDeque<>();
   private ClientLevel q;
   private BlockPos D;
   private Vec3 b;
   private Direction M;
   private Direction T;
   private BlockPos m;
   private BlockPos J;
   private final hc h = new hc();
   private final SplittableRandom B = new SplittableRandom();
   private Vec3 g = Vec3.ZERO;
   private boolean z;
   private boolean S;
   private int y;
   private BlockPos V;
   private Vec3 X = Vec3.ZERO;
   private double H = 0.6;
   private final tu.o<LevelChunk> U = new tu.o<LevelChunk>() {
      public LevelChunk loaded(int var1, int var2) {
         return af.this.q.getChunkSource().getChunkNow(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         af.x var3 = af.this.f.get(ChunkPos.pack(var1, var2));
         return var3 == null || af.mc.player.tickCount - var3.tick >= 80L;
      }

      public int T(LevelChunk var1) {
         return var1.getSections().length;
      }

      public boolean Q(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];
         return !var3.hasOnlyAir() && var3.maybeHas(af.this::q);
      }

      @Override
      public void begin() {
         af.this.o.clear();
      }

      public void S(LevelChunk var1, int var2) {
         LevelChunkSection var3 = var1.getSections()[var2];

         for (int var4 = 0; var4 < 16; var4++) {
            for (int var5 = 0; var5 < 16; var5++) {
               for (int var6 = 0; var6 < 16; var6++) {
                  BlockState var7 = var3.getBlockState(var6, var4, var5);
                  if (af.this.q(var7) && af.this.o.size() < 256) {
                     BlockPos var8 = new BlockPos(var1.getPos().getMinBlockX() + var6, var1.getMinY() + var2 * 16 + var4, var1.getPos().getMinBlockZ() + var5);
                     if (!(af.this.D.distSqr(var8) < af.this.Q.getValue() * af.this.Q.getValue())
                        && (!af.this.L.getValue() || !var7.is(Blocks.SPAWNER) || !af.this.S(var8))) {
                        af.this.o.add(var8);
                     }
                  }
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         af.this.f.put(ChunkPos.pack(var1, var2), new af.x(List.copyOf(af.this.o), af.mc.player.tickCount));
      }
   };

   private com.larpclient.module.s j(final String var1, String var2, boolean var3) {
      return this.addSetting(new com.larpclient.module.s("Storage " + var1, var2, var3) {
         @Override
         public String getDisplayName() {
            return "Storage: " + var1;
         }
      });
   }

   public af() {
      super("Auto Tunnel", "Mine and walk a centered 1x1, 1x2 or 3x3 tunnel, including while menus are open");
      A = this;

      for (com.larpclient.module.q var2 : List.of(this.u, this.R, this.W, this.e, this.G, this.r, this.p, this.c, this.x, this.Q, this.s, this.I)) {
         var2.P(this.d::getValue);
      }

      this.L.P(() -> this.d.getValue() && this.p.getValue());
      this.d.o(var1 -> this.P());
      this.j.o(var1 -> this.P());

      for (com.larpclient.module.s var4 : List.of(this.W, this.e, this.G, this.r, this.p, this.c, this.L)) {
         var4.o(var1 -> {
            this.E.y();
            this.f.clear();
            this.o.clear();
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

   public static boolean B() {
      if (A != null && A.isEnabled()) {
         af var0 = A;
         Minecraft var1 = Minecraft.getInstance();
         return var0 != null && var0.isRuntimeActive() && ta.S(var0) && var1.gui.overlay() == null && var1.player.connection.hasClientLoaded();
      } else {
         return false;
      }
   }

   @Override
   protected void aimAt(Vec3 var1, float var2) {
      if (this.isEnabled()) {
         if (!this.S) {
            double var3 = Math.min(this.w.getValue(), mc.player.blockInteractionRange());
            Vec3 var5 = var1;
            if (this.J != null) {
               if (!this.J.equals(this.V)) {
                  this.V = this.J;
                  this.X = new Vec3(this.B.nextDouble(-0.22, 0.22), this.B.nextDouble(-0.22, 0.22), this.B.nextDouble(-0.22, 0.22));
               }

               Vec3 var6 = var1.add(this.X);
               if (l0.O(this.J, var6, var3)) {
                  var5 = var6;
               }
            }

            float[] var9 = j6.s(mc.player.getEyePosition(), var5);
            int var7 = this.J != null && l0.u(this.J, var3) == null ? 0 : 1;
            float[] var8 = this.h.Q(mc.player.getYRot(), mc.player.getXRot(), var9[0], var9[1], (boolean)var7);
            j6.U(this, j6.l.w, var8[0], var8[1], 1.0);
         }
      }
   }

   private void P() {
      ta.s(this);
      l0.h(this);
      this.z = this.S = (boolean)0;
      this.T = this.M = null;
      this.D = null;
      this.b = null;
      this.m = this.J = this.V = null;
      this.g = Vec3.ZERO;
      this.h.x();
      this.H = this.B.nextDouble(0.35, 1.1);
      this.F.clear();
      this.E.y();
      this.f.clear();
      this.o.clear();
      this.q = null;
      this.y = 0;
   }

   @Override
   public void onTick() {
      this.z = (boolean)0;
      this.g = Vec3.ZERO;
      int var10000 = this.I();
      boolean var12 = false;
      byte var11 = (byte)var10000;
      var10000 = Integer.signum(var11);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && !this.R()) {
         byte var1 = this.q();
         byte var16 = this.S;
         byte var15 = var1;
         var10000 = Integer.compare(var15, var16);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.S = (boolean)var1;
            String var21 = this.getName();
            boolean var18 = false;
            byte var17 = var1;
            int var23 = Integer.signum(var17);
            com.larpclient.h.A(
               var21,
               ((var23 | -var23) >>> 31 ^ 1) == 0
                  ? "Inventory menu open; holding position and facing, mining only the block under the crosshair"
                  : "Inventory menu closed; resuming walking and aiming"
            );
            if (var1 == 0) {
               this.h.x();
            }
         }

         if (this.q != mc.level || this.b != null && mc.player.position().distanceToSqr(this.b) > 64.0) {
            this.P();
            this.q = mc.level;
            this.D = mc.player.blockPosition();
            this.M = Direction.fromYRot(mc.player.getYRot());
         }

         Direction var2 = this.M;
         if (this.i.getValue() > 0.0 && mc.player.getHealth() <= this.i.getValue()) {
            this.z("Low health");
         } else if (this.j.O("1x1") && !mc.player.isVisuallyCrawling()) {
            l0.h(this);
            this.h.x();
            this.J = null;
            this.e("Waiting: 1x1 needs a crawling start, such as under a trapdoor");
         } else {
            if (this.d.getValue()) {
               this.t();
               if (!this.isRuntimeActive()) {
                  return;
               }

               var2 = this.h();
               if (var2 == null) {
                  return;
               }
            }

            if (this.T != var2 || this.m == null) {
               this.T = var2;
               this.m = mc.player.blockPosition().relative(var2);
               this.J = null;
               this.h.x();
               com.larpclient.h.A(
                  this.getName(), "Following {} through lane {}, {} at Y {} ({})", var2, this.m.getX(), this.m.getZ(), this.m.getY(), this.j.getValue()
               );
            }

            this.m = V(this.m, mc.player.position(), var2, this.Q(this.m, var2));
            String var3 = this.B(this.m, var2);
            if (var3 != null) {
               l0.h(this);
               this.h.x();
               this.J = null;
               this.e("Waiting: " + var3);
            } else {
               double var4 = Math.min(this.w.getValue(), mc.player.blockInteractionRange());
               BlockPos var6 = this.x(var2, var4);
               if (var6 != null) {
                  if (this.d(var6)) {
                     this.z("Tool nearly broken");
                     return;
                  }

                  BlockPos var7 = var6;
                  Direction var8 = var2;
                  BlockPos var9 = this.m;
                  boolean var10 = l0.M(
                     this,
                     var6,
                     var4,
                     true,
                     12.0F,
                     this.O.getValue() || this.d.getValue(),
                     () -> !this.R() && this.B(var9, var8) == null && (!this.d.getValue() || !mc.level.getBlockState(var7).hasBlockEntity())
                  );
                  if (!var10) {
                     this.e("Waiting for a reachable tunnel face");
                  }
               } else {
                  l0.h(this);
               }

               this.t(var2, var6);
            }
         }
      } else {
         l0.h(this);
         this.h.x();
         this.J = null;
         int var10001 = this.R();
         boolean var14 = false;
         byte var13 = (byte)var10001;
         var10001 = Integer.signum(var13);
         this.e(((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "Paused for manual movement" : "Waiting for world or interaction control");
      }
   }

   private void t(Direction var1, BlockPos var2) {
      Vec3 var3 = Vec3.atBottomCenterOf(this.m);
      boolean var4 = this.Q(this.m, var1);
      double var5 = hc.m(mc.player.position(), var3, var1) - (var4 ? -0.5 : 0.85);
      Vec3 var7 = mc.player.getDeltaMovement();
      double var8 = var7.x * var1.getStepX() + var7.z * var1.getStepZ();
      Vec3 var10 = hc.H(mc.player.position(), var3, var1);
      boolean var11 = this.h.D();
      int var12 = ta.E(this) && !this.S && !cj.b() && this.d(j(var10)) ? 1 : 0;
      this.z = this.h.u(var5, var8, (boolean)var12);
      this.g = this.z ? var10 : Vec3.ZERO;
      if (this.z != var11) {
         com.larpclient.h.U(
            this.getName(), this.z ? "Walk keys pressed; {} clear ahead" : "Walk keys released; {} clear ahead", String.format(Locale.ROOT, "%.2f", var5)
         );
      }

      if (var2 == null && ta.E(this)) {
         Vec3 var13 = var3.add(var1.getStepX() * 6, 0.0, var1.getStepZ() * 6);
         if (mc.player.tickCount % 60 == 0 && this.B.nextInt(4) == 0) {
            this.H = this.B.nextDouble(0.35, 1.1);
         }

         this.aimAt(new Vec3(var13.x, mc.player.getEyeY() - this.H, var13.z), 12.0F);
      }

      if (this.b != null && mc.player.position().distanceToSqr(this.b) < 1.0E-4 && this.z && var2 == null) {
         this.y++;
      } else {
         this.y = 0;
      }

      this.b = mc.player.position();
      if (var2 == null) {
         this.e(
            this.S
               ? "Holding still while an inventory menu is open"
               : (this.z ? (this.d.getValue() ? "Searching for bases" : "Following the cleared tunnel") : "Waiting at a protected or unreachable obstruction")
         );
      }

      if (this.y > 160) {
         this.z("Movement blocked; clear the route or choose a new heading");
      }
   }

   public static BlockPos V(BlockPos var0, Vec3 var1, Direction var2, boolean var3) {
      return var3 && hc.m(var1, Vec3.atBottomCenterOf(var0), var2) < 0.12 ? var0.relative(var2) : var0;
   }

   private boolean q() {
      return mc.gui.screen() instanceof AbstractContainerScreen;
   }

   private boolean R() {
      return mc != null
         && mc.player != null
         && mc.isWindowActive()
         && mc.gui.screen() == null
         && (
            c5.J(mc.options.keyUp)
               || c5.J(mc.options.keyDown)
               || c5.J(mc.options.keyLeft)
               || c5.J(mc.options.keyRight)
               || c5.J(mc.options.keyJump)
               || c5.J(mc.options.keyShift)
         );
   }

   private List<BlockPos> w(BlockPos var1, Direction var2) {
      return G(var1, var2, this.j.getValue());
   }

   public static int I(String var0) {
      return switch (var0) {
         case "1x1" -> 1;
         case "3x3" -> 3;
         default -> 2;
      };
   }

   public static List<BlockPos> G(BlockPos var0, Direction var1, String var2) {
      boolean var3 = var2.equals("3x3");
      int var4 = I(var2);
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

   private BlockPos x(Direction var1, double var2) {
      for (int var4 = 0; var4 < Math.ceil(var2); var4++) {
         BlockPos var5 = this.m.relative(var1, var4);
         if (!this.F.isEmpty() && hc.m(Vec3.atBottomCenterOf(var5), Vec3.atBottomCenterOf((Vec3i)this.F.peekFirst()), var1) < -0.01
            || this.B(var5, var1) != null) {
            break;
         }

         List var6 = this.w(var5, var1).stream().filter(var3 -> this.T(var3, var2)).toList();
         if (!var6.isEmpty()) {
            if (this.J != null && var6.contains(this.J)) {
               return this.J;
            }

            this.J = var6.stream().min(Comparator.comparingDouble(this::D)).orElseThrow();
            return this.J;
         }

         if (!this.Q(var5, var1)) {
            break;
         }
      }

      this.J = null;
      return null;
   }

   private boolean T(BlockPos var1, double var2) {
      return l0.B(var1)
         && mc.player.getEyePosition().distanceToSqr(Vec3.atCenterOf(var1)) <= var2 * var2
         && (!this.d.getValue() || !mc.level.getBlockState(var1).hasBlockEntity())
         && l0.L(this, var1, var2) != null;
   }

   private double D(BlockPos var1) {
      float[] var2 = j6.s(mc.player.getEyePosition(), Vec3.atCenterOf(var1));
      return Math.hypot(Mth.wrapDegrees(var2[0] - mc.player.getYRot()), var2[1] - mc.player.getXRot());
   }

   private boolean Q(BlockPos var1, Direction var2) {
      for (BlockPos var4 : this.w(var1, var2)) {
         if (!mc.level.hasChunkAt(var4) || !mc.level.getBlockState(var4).getCollisionShape(mc.level, var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static Vec3 j(Vec3 var0) {
      return var0.normalize().scale(0.55);
   }

   private boolean d(Vec3 var1) {
      Vec3 var2 = var1.scale(0.65);
      AABB var3 = mc.player.getBoundingBox().expandTowards(var2);
      if (!mc.level.noCollision(mc.player, var3)) {
         return false;
      }

      BlockPos var4 = BlockPos.containing(mc.player.position().add(var2));
      return this.R(var4) && this.B(var4, this.T) == null;
   }

   private boolean d(BlockPos var1) {
      if (this.a.getValue() <= 0.0) {
         return false;
      }

      BlockState var2 = mc.level.getBlockState(var1);
      ItemStack var3 = mc.player.getMainHandItem();
      float var4 = var3.getDestroySpeed(var2) + (var3.isCorrectToolForDrops(var2) ? 1000 : 0);

      for (int var5 = 0; (this.O.getValue() || this.d.getValue()) && var5 < 9; var5++) {
         ItemStack var6 = mc.player.getInventory().getItem(var5);
         float var7 = var6.getDestroySpeed(var2) + (var6.isCorrectToolForDrops(var2) ? 1000 : 0);
         if (var7 > var4) {
            var3 = var6;
            var4 = var7;
         }
      }

      return var3.isDamageableItem() && var3.getMaxDamage() - var3.getDamageValue() <= this.a.getValue();
   }

   private boolean R(BlockPos var1) {
      if (!mc.level.hasChunkAt(var1)) {
         return false;
      }

      int var2 = Math.min(I(this.j.getValue()), 2);

      for (int var3 = 0; var3 < var2; var3++) {
         if (!mc.level.getBlockState(var1.above(var3)).getCollisionShape(mc.level, var1.above(var3)).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private String B(BlockPos var1, Direction var2) {
      if (!mc.level.hasChunkAt(var1)) {
         return "Waiting for loaded terrain";
      }

      for (BlockPos var4 : this.w(var1, var2)) {
         for (Direction var8 : Direction.values()) {
            BlockPos var9 = var4.relative(var8);
            if (!mc.level.hasChunkAt(var9)) {
               return "Waiting for loaded terrain";
            }

            FluidState var10 = mc.level.getFluidState(var9);
            if (this.P.getValue() && var10.is(FluidTags.LAVA)) {
               return "Lava";
            }

            if (this.Z.getValue() && var10.is(FluidTags.WATER)) {
               return "Water";
            }
         }

         FluidState var13 = mc.level.getFluidState(var4);
         if (this.P.getValue() && var13.is(FluidTags.LAVA)) {
            return "Lava";
         }

         if (this.Z.getValue() && var13.is(FluidTags.WATER)) {
            return "Water";
         }
      }

      if (this.Y.getValue()
         && e(
            mc.level.getBlockState(var1.below()).getCollisionShape(mc.level, var1.below()).isEmpty(),
            mc.level.getBlockState(var1.below(2)).getCollisionShape(mc.level, var1.below(2)).isEmpty()
         )) {
         return "Deep drop";
      }

      if (this.n.getValue()) {
         for (BlockPos var12 : this.w(var1, var2)) {
            if (mc.level.getBlockState(var12.above()).getBlock() instanceof FallingBlock) {
               return "Falling sand or gravel";
            }
         }
      }

      return null;
   }

   private Direction h() {
      BlockPos var1 = mc.player.blockPosition();

      while (
         !this.F.isEmpty()
            && mc.player.position().multiply(1.0, 0.0, 1.0).distanceToSqr(Vec3.atBottomCenterOf((Vec3i)this.F.peekFirst()).multiply(1.0, 0.0, 1.0)) < 0.08
      ) {
         this.F.removeFirst();
      }

      if (!this.F.isEmpty()) {
         Direction var9 = var1.equals(this.F.peekFirst()) ? this.T : p(var1, this.F.peekFirst());
         String var11 = this.B(var1.relative(var9), var9);
         if (var11 != null) {
            this.z("Detour blocked by " + var11.toLowerCase(Locale.ROOT));
            return null;
         } else {
            return var9;
         }
      } else {
         String var2 = null;

         for (int var3 = 1; var3 <= this.u.getValue().intValue(); var3++) {
            var2 = this.B(var1.relative(this.M, var3), this.M);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            return this.M;
         }

         if (var2.equals("Waiting for loaded terrain")) {
            l0.h(this);
            this.e(var2);
            return null;
         }

         for (int var10 = 1; var10 <= this.R.getValue().intValue(); var10++) {
            for (Direction var5 : List.of(this.M.getClockWise(), this.M.getCounterClockWise())) {
               int var6 = this.u.getValue().intValue() + 2;
               BlockPos var7 = var1.relative(var5, var10);
               BlockPos var8 = var7.relative(this.M, var6);
               if (this.X(var1, var5, var10) && this.X(var7, this.M, var6) && this.X(var8, var5.getOpposite(), var10)) {
                  this.F.add(var7);
                  this.F.add(var8);
                  this.F.add(var1.relative(this.M, var6));
                  com.larpclient.h.A(this.getName(), "Routing {} blocks around {} using {}", var10, var2, var5);
                  this.e("Detouring around " + var2.toLowerCase(Locale.ROOT));
                  return var5;
               }
            }
         }

         this.z("No safe detour around " + var2.toLowerCase(Locale.ROOT));
         return null;
      }
   }

   private boolean X(BlockPos var1, Direction var2, int var3) {
      for (int var4 = 1; var4 <= var3; var4++) {
         if (this.B(var1.relative(var2, var4), var2) != null) {
            return false;
         }
      }

      return true;
   }

   private static Direction p(BlockPos var0, BlockPos var1) {
      int var2 = var1.getX() - var0.getX();
      int var3 = var1.getZ() - var0.getZ();
      return Math.abs(var2) > Math.abs(var3) ? (var2 > 0 ? Direction.EAST : Direction.WEST) : (var3 > 0 ? Direction.SOUTH : Direction.NORTH);
   }

   public static Input B() {
      af var0 = A;
      if (var0 != null && var0.I() && var0.z && !var0.S && var0.T != null && !p_.V() && !cj.b() && !var0.R()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var0.q == var1.level && !(var0.g.lengthSqr() < 1.0E-6) && var0.d(j(var0.g)) && (!j6.j() || j6.g(j6.l.w))) {
            Input var2 = var0.h.e(var1.player.getYRot(), var0.g);
            return var2.backward() ? null : var2;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static Vec2 K() {
      Input var0 = B();
      return var0 == null ? null : hc.D(var0);
   }

   public static boolean e(boolean var0, boolean var1) {
      return var0 && var1;
   }

   public static boolean v(BlockPos var0, BlockPos var1) {
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
      return this.W.getValue() && (var2 == Blocks.CHEST || var2 == Blocks.TRAPPED_CHEST)
         || this.e.getValue() && var2 == Blocks.BARREL
         || this.G.getValue() && var2 == Blocks.ENDER_CHEST
         || this.r.getValue() && var2 == Blocks.HOPPER
         || this.p.getValue() && var2 == Blocks.SPAWNER
         || this.c.getValue() && var2 instanceof ShulkerBoxBlock;
   }

   private boolean S(BlockPos var1) {
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

   private void t() {
      ChunkPos var1 = mc.player.chunkPosition();
      int var2 = Math.clamp(mc.options.getEffectiveRenderDistance(), 1, 12);
      this.f.entrySet().removeIf(var3x -> {
         ChunkPos var4 = ChunkPos.unpack(var3x.getKey());
         return Math.abs(var4.x() - var1.x()) > var2 || Math.abs(var4.z() - var1.z()) > var2 || this.q.getChunkSource().getChunkNow(var4.x(), var4.z()) == null;
      });
      this.E.n(this.U, var1.x(), var1.z(), var2, 2);
      if (mc.player.tickCount % 10 == 0) {
         ArrayList var3 = new ArrayList();

         for (af.x var5 : this.f.values()) {
            for (BlockPos var7 : var5.positions) {
               if (var3.size() >= 4096) {
                  break;
               }

               if (mc.level.hasChunkAt(var7)
                  && this.q(mc.level.getBlockState(var7))
                  && this.D.distSqr(var7) >= this.Q.getValue() * this.Q.getValue()
                  && (!this.L.getValue() || !mc.level.getBlockState(var7).is(Blocks.SPAWNER) || !this.S(var7))) {
                  var3.add(var7);
               }
            }
         }

         HashMap var16 = new HashMap();

         for (BlockPos var19 : var3) {
            var16.computeIfAbsent(Y(var19.getX(), var19.getY(), var19.getZ()), var0 -> new ArrayList<>()).add(var19);
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
                        if (v(var20, var15)) {
                           var21.add(var15);
                        }
                     }
                  }
               }
            }

            if (var21.size() >= this.x.getValue()) {
               this.t(var20, var21);
               return;
            }
         }
      }
   }

   private static String Y(int var0, int var1, int var2) {
      return Math.floorDiv(var0, 12) + ":" + Math.floorDiv(var1, 12) + ":" + Math.floorDiv(var2, 12);
   }

   private void t(BlockPos var1, List<BlockPos> var2) {
      TreeMap var3 = new TreeMap();

      for (BlockPos var5 : var2) {
         var3.merge(BuiltInRegistries.BLOCK.getKey(mc.level.getBlockState(var5).getBlock()).getPath(), 1, Integer::sum);
      }

      String var11 = var1.getX() + ", " + var1.getY() + ", " + var1.getZ();
      String var12 = var11 + " (" + var3.entrySet().stream().map(var0 -> var0.getValue() + " " + (String)var0.getKey()).collect(Collectors.joining(", ")) + ")";
      mc.player.sendSystemMessage(Component.literal("Larp Auto Tunnel: base found at " + var12));
      s0.s().k("Base found", var12, s0.u.m, 10000L);
      mc.getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.4F));
      com.larpclient.h.A(this.getName(), "Base found at {}", var12);
      if (this.I.getValue()) {
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
            com.larpclient.h.m(this.getName(), "Could not append base find to " + var6, var10);
         }
      }

      boolean var13 = this.s.O("Leave");
      this.disable();
      if (var13) {
         mc.disconnectFromWorld(Component.literal("Larp Auto Tunnel found a base at " + var12));
      }
   }

   private void z(String var1) {
      com.larpclient.h.A(this.getName(), "Stopped: {}", var1);
      mc.player.sendSystemMessage(Component.literal("Larp Auto Tunnel stopped: " + var1));
      s0.s().V("Auto Tunnel stopped", var1, s0.u.p);
      this.disable();
      this.e(var1);
   }

   private record x(List<BlockPos> positions, long tick) {
   }
}
