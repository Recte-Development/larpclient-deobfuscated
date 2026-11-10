package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.q71cffb54eb30bf94;
import com.larpclient.module.AntiTrapModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.Category;
import com.larpclient.module.IgnoreEntityModule;
import com.larpclient.module.MCFriendModule;
import com.larpclient.module.Module;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.kb;
import com.larpclient.module.kv;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.CraftingTableBlock;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class vi {
   private static final Map<Module, vi.g> M = new LinkedHashMap<>();
   private static final Map<Module, String> g = new HashMap<>();
   private static final Map<Module, Long> r = new HashMap<>();
   private static final Direction[] i = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};

   public static boolean A(Object var0, Object var1, Object var2, Object var3, int var4, int var5) {
      int var6 = var5 - var4;
      return var0 != null && var0 == var1 && var2 != null && var2 == var3 && var6 >= 0 && var6 <= 1;
   }

   public static String k(Module var0) {
      return g.getOrDefault(var0, "No supported face within native reach");
   }

   private static void m(Module var0, String var1) {
      g.put(var0, var1);
   }

   private static void d(Module var0, String var1) {
      m(var0, var1);
      long var2 = System.nanoTime();
      long var4 = r.getOrDefault(var0, Long.MIN_VALUE);
      if (var4 == Long.MIN_VALUE || var2 - var4 >= 2000000000L) {
         r.put(var0, var2);
         _q.z(var0.getName(), "Placement paused: {}", var1);
      }
   }

   public static void J(Module var0) {
      M.remove(var0);
      _p.e(var0);
   }

   public static void o() {
      M.clear();
      g.clear();
      r.clear();
   }

   public static void M() {
      M.values().removeIf(var0 -> !var0.source.isRuntimeActive() || !var0.source.allowsScreenInteractions());
   }

   public static void C() {
      M.values().removeIf(var0 -> !_p.t(var0.source));
   }

   private static boolean G(Module var0) {
      return var0 == null ? _p.g() : _p.D(var0);
   }

   public static boolean m(Module var0) {
      vi.g var1 = M.get(var0);
      Minecraft var2 = Minecraft.getInstance();
      return var1 != null && var2.player != null && A(var1.world, var2.level, var1.player, var2.player, var1.tick, var2.player.tickCount);
   }

   public static int u(Item var0, boolean var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 == null) {
         return -1;
      }

      int var3 = var2.getInventory().getSelectedSlot();
      if (V(var2.getMainHandItem(), var0)) {
         return var3;
      }

      if (V(var2.getOffhandItem(), var0)) {
         return -2;
      }

      if (var1) {
         for (int var4 = 0; var4 < 9; var4++) {
            if (V(var2.getInventory().getItem(var4), var0)) {
               return var4;
            }
         }
      }

      return -1;
   }

   private static boolean V(ItemStack var0, Item var1) {
      return var1 == Items.AIR ? var0.isEmpty() : var0.is(var1);
   }

   public static boolean i(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.level != null
         && var1.level.hasChunkAt(var0)
         && !var1.level.isOutsideBuildHeight(var0)
         && var1.level.getWorldBorder().isWithinBounds(var0)
         && var1.level.getBlockState(var0).canBeReplaced();
   }

   public static boolean O(BlockPos var0, Item var1) {
      if (i(var0)) {
         return true;
      }

      Minecraft var2 = Minecraft.getInstance();
      return var1 instanceof BlockItem var3
         && var2.level != null
         && var2.level.hasChunkAt(var0)
         && !var2.level.isOutsideBuildHeight(var0)
         && var2.level.getWorldBorder().isWithinBounds(var0)
         && var2.level.getBlockState(var0).is(var3.getBlock());
   }

   private static BlockHitResult q(Module var0, BlockPos var1, Vec3 var2, double var3, boolean var5) {
      return !G(var0) ? null : m(var0, Minecraft.getInstance().player.getEyePosition(), var1, var2, var3, var5);
   }

   private static BlockHitResult m(Module var0, Vec3 var1, BlockPos var2, Vec3 var3, double var4, boolean var6) {
      if (!G(var0)) {
         return null;
      }

      Minecraft var7 = Minecraft.getInstance();
      double var8 = Math.min(var4, var7.player.blockInteractionRange());
      Vec3 var10 = var1.add(var3.normalize().scale(var8));
      BlockHitResult var11 = var7.level.clip(new ClipContext(var1, var10, Block.OUTLINE, Fluid.NONE, var7.player));
      if (var11.getType() == Type.BLOCK && var11.getBlockPos().equals(var2) && !AntiTrapModule.I(var7.level.getBlockState(var2))) {
         if (var6) {
            return var11;
         }

         for (Entity var13 : var7.level.getEntities(var7.player, new AABB(var1, var11.getLocation()).inflate(1.0))) {
            if (var13.isPickable()
               && !var13.isSpectator()
               && !AntiTrapModule.j(var13)
               && !IgnoreEntityModule.u(var13)
               && jz.P(var1, var11.getLocation(), var13.getBoundingBox())) {
               return null;
            }
         }

         return var11;
      } else {
         return null;
      }
   }

   public static BlockHitResult H(BlockPos var0, Vec3 var1, Vec3 var2, double var3, boolean var5, vi.f var6) {
      if (!var6.replaceable(var0)) {
         return null;
      }

      for (Direction var10 : Direction.values()) {
         BlockPos var11 = var0.relative(var10);
         if (var6.usableSupport(var11)) {
            Direction var12 = var10.getOpposite();

            for (Vec3 var14 : O(var11, var12)) {
               BlockHitResult var15 = var6.ray(var11, var5 ? var14.subtract(var1) : var2, var3);
               if (var15 != null && var15.getDirection() == var12 && var6.contextMatches(var15, var0)) {
                  return var15;
               }

               if (!var5) {
                  break;
               }
            }
         }
      }

      return null;
   }

   public static List<Vec3> O(BlockPos var0, Direction var1) {
      Vec3 var2 = Vec3.atCenterOf(var0).add(new Vec3(var1.getStepX(), var1.getStepY(), var1.getStepZ()).scale(0.499));
      ArrayList var3 = new ArrayList(9);

      for (double var7 : new double[]{0.0, -0.42, 0.42}) {
         for (double var12 : new double[]{0.0, -0.42, 0.42}) {
            var3.add(switch (var1.getAxis()) {
               case X -> var2.add(0.0, var7, var12);
               case Y -> var2.add(var7, 0.0, var12);
               case Z -> var2.add(var7, var12, 0.0);
               default -> throw new MatchException(null, null);
            });
         }
      }

      return var3;
   }

   private static boolean M(Module var0, BlockPos var1) {
      return !(var0 instanceof vi.d var2 && !var2.trustsSupport(var1));
   }

   public static boolean k(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (!var1.level.hasChunkAt(var0)) {
         return false;
      }

      BlockState var2 = var1.level.getBlockState(var0);
      return !var2.canBeReplaced()
         && (
            var1.player.isShiftKeyDown()
               || !var2.hasBlockEntity() && !(var2.getBlock() instanceof RespawnAnchorBlock) && !(var2.getBlock() instanceof CraftingTableBlock)
         );
   }

   public static BlockHitResult c(BlockPos var0, Item var1, double var2, boolean var4) {
      return X(var0, var1, var2, var4, true);
   }

   public static BlockHitResult X(BlockPos var0, Item var1, double var2, boolean var4, boolean var5) {
      return B(null, var0, var1, var2, var4, var5);
   }

   public static BlockHitResult B(final Module var0, BlockPos var1, final Item var2, double var3, boolean var5, final boolean var6) {
      if (var2 instanceof BlockItem && G(var0)) {
         Minecraft var7 = Minecraft.getInstance();
         return H(var1, var7.player.getEyePosition(), var7.player.getLookAngle(), Math.min(var3, var7.player.blockInteractionRange()), var5, new vi.f() {
            @Override
            public boolean replaceable(BlockPos var1) {
               return vi.i(var1);
            }

            @Override
            public boolean usableSupport(BlockPos var1) {
               return vi.k(var1) && vi.M(var0, var1);
            }

            @Override
            public BlockHitResult ray(BlockPos var1, Vec3 var2x, double var3x) {
               return vi.q(var0, var1, var2x, var3x, true);
            }

            @Override
            public boolean contextMatches(BlockHitResult var1, BlockPos var2x) {
               return vi.M(var1, var2x, var2, var6);
            }
         });
      } else {
         return null;
      }
   }

   private static boolean M(BlockHitResult var0, BlockPos var1, Item var2, boolean var3) {
      return w(var0, var2, var3, var1) != null;
   }

   public static vi.a U(BlockHitResult var0, Item var1, boolean var2) {
      return w(var0, var1, var2, null);
   }

   private static vi.a w(BlockHitResult var0, Item var1, boolean var2, BlockPos var3) {
      Minecraft var4 = Minecraft.getInstance();
      int var5 = u(var1, var2);
      if (var5 != -1 && var1 instanceof BlockItem var6) {
         ItemStack var7 = var5 == -2 ? var4.player.getOffhandItem() : var4.player.getInventory().getItem(var5);
         InteractionHand var8 = var5 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
         BlockPlaceContext var9 = new BlockPlaceContext(var4.player, var8, var7, var0);
         if (!var9.canPlace()) {
            return null;
         }

         BlockPos var10 = var9.getClickedPos();
         if (var3 != null && !var10.equals(var3)) {
            return null;
         }

         BlockState var11 = var6.getBlock().getStateForPlacement(var9);
         return var11 != null && var11.canSurvive(var4.level, var10) && var4.level.isUnobstructed(var11, var10, CollisionContext.placementContext(var4.player))
            ? new vi.a(var10, var11)
            : null;
      } else {
         return null;
      }
   }

   public static BlockHitResult W(BlockPos var0, Vec3 var1, double var2) {
      return i(null, var0, var1, var2);
   }

   public static BlockHitResult i(Module var0, BlockPos var1, Vec3 var2, double var3) {
      return q(var0, var1, var2, var3, true);
   }

   public static BlockHitResult E(Vec3 var0, BlockPos var1, Vec3 var2, double var3) {
      return y(null, var0, var1, var2, var3);
   }

   public static BlockHitResult y(Module var0, Vec3 var1, BlockPos var2, Vec3 var3, double var4) {
      return m(var0, var1, var2, var3, var4, true);
   }

   public static boolean Q(BlockPos var0, double var1) {
      return V(null, var0, var1);
   }

   public static boolean V(Module var0, BlockPos var1, double var2) {
      LocalPlayer var4 = Minecraft.getInstance().player;
      if (var4 == null) {
         return false;
      }

      q71cffb54eb30bf94 var5 = (q71cffb54eb30bf94)var4;
      float var6 = var5.larpYRotLast();
      float var7 = var5.larpXRotLast();
      return Float.isFinite(var6) && Float.isFinite(var7) && q(var0, var1, Vec3.directionFromRotation(var7, var6), var2, true) != null;
   }

   public static boolean a(Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      return M(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   public static boolean M(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (u(var2, var6) == -1) {
         m(var0, "Required item missing from held hands or hotbar");
         return false;
      } else {
         BlockHitResult var10 = B(var0, var1, var2, var3, var5, var6);
         if (var10 == null) {
            m(var0, "No valid supported face, collision-free position or aim within native reach");
            return false;
         } else {
            return Z(var0, var1, var10, var2, var3, true, var5, var6, var7, var8, var9);
         }
      }
   }

   public static boolean V(
      Module var0, BlockPos var1, BlockHitResult var2, Item var3, double var4, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (u(var3, var6) == -1) {
         m(var0, "Required item missing from held hands or hotbar");
         return false;
      }

      if (var2 != null && var3 instanceof BlockItem && G(var0) && O(var1, var3) && M(var2, var1, var3, var6)) {
         return Z(var0, var1, var2, var3, var4, true, true, var6, var7, var8, var9);
      }

      m(var0, "The chosen support face no longer accepts this block");
      return false;
   }

   public static boolean i(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (G(var0) && u(var2, var6) != -1) {
         Minecraft var10 = Minecraft.getInstance();
         Vec3 var11 = var5 ? Vec3.atCenterOf(var1).subtract(var10.player.getEyePosition()) : var10.player.getLookAngle();
         BlockHitResult var12 = q(var0, var1, var11, var3, false);
         if (var12 == null && var5) {
            var12 = r(var0, var1, var10.player.getEyePosition(), var3);
         }

         return var12 == null ? false : Z(var0, var1, var12, var2, var3, false, var5, var6, var7, var8, var9);
      } else {
         return false;
      }
   }

   public static boolean d(Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      return i(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   private static BlockHitResult r(Module var0, BlockPos var1, Vec3 var2, double var3) {
      Vec3 var5 = Vec3.atCenterOf(var1);

      for (Direction var9 : i) {
         Vec3 var10 = new Vec3(var9.getStepX(), var9.getStepY(), var9.getStepZ());
         if (!(var2.subtract(var5.add(var10.scale(0.5))).dot(var10) <= 0.0)) {
            for (Vec3 var12 : O(var1, var9)) {
               BlockHitResult var13 = q(var0, var1, var12.subtract(var2), var3, false);
               if (var13 != null && var13.getDirection() == var9) {
                  return var13;
               }
            }
         }
      }

      return null;
   }

   private static boolean Z(
      Module var0,
      BlockPos var1,
      BlockHitResult var2,
      Item var3,
      double var4,
      boolean var6,
      boolean var7,
      boolean var8,
      BooleanSupplier var9,
      BooleanSupplier var10,
      Consumer<Boolean> var11
   ) {
      Minecraft var12 = Minecraft.getInstance();
      vi.g var13 = new vi.g(var0, var1.immutable(), var2, var3, var4, var6, var7, var9, var10, var11, var12.level, var12.player, var12.player.tickCount);
      M.put(var0, var13);
      _p.w(var0, () -> u(var13, var8), () -> q(var13) && var13.actionReady.getAsBoolean() && u(var3, var8) != -1 && M(var13, var8) != null);
      return true;
   }

   private static boolean q(vi.g var0) {
      Minecraft var1 = Minecraft.getInstance();
      return M.get(var0.source) == var0
         && A(var0.world, var1.level, var0.player, var1.player, var0.tick, var1.player == null ? 0 : var1.player.tickCount)
         && var0.source.isRuntimeActive()
         && _p.D(var0.source)
         && (var1.isWindowActive() || var0.source.allowsScreenInteractions())
         && var0.valid.getAsBoolean();
   }

   private static BlockHitResult M(vi.g var0, boolean var1) {
      if (!q(var0)) {
         return null;
      }

      Minecraft var2 = Minecraft.getInstance();
      BlockHitResult var3 = q(var0.source, var0.hit.getBlockPos(), var2.player.getLookAngle(), var0.range, var0.place);
      if (var3 != null && var3.getDirection() == var0.hit.getDirection()) {
         if (!var0.place || k(var3.getBlockPos()) && M(var0.source, var3.getBlockPos())) {
            if (var0.place && var0.source instanceof vi.d var4 && var4.waitsForSentLook()) {
               q71cffb54eb30bf94 var8 = (q71cffb54eb30bf94)var2.player;
               BlockHitResult var6 = q(
                  var0.source, var0.hit.getBlockPos(), Vec3.directionFromRotation(var8.larpXRotLast(), var8.larpYRotLast()), var0.range, true
               );
               if (var6 == null || var6.getDirection() != var0.hit.getDirection()) {
                  d(
                     var0.source,
                     "The look the server has does not reach the support face "
                        + var0.hit.getDirection().getSerializedName()
                        + " of "
                        + var0.hit.getBlockPos().toShortString()
                        + " yet; waiting a tick"
                  );
                  return null;
               }
            }

            if (var0.place && !var2.player.isShiftKeyDown()) {
               BlockState var7 = var2.level.getBlockState(var3.getBlockPos());
               if (var7.hasBlockEntity() || var7.getBlock() instanceof RespawnAnchorBlock || var7.getBlock() instanceof CraftingTableBlock) {
                  return null;
               }
            }

            if (!var0.place || O(var0.target, var0.item) && M(var3, var0.target, var0.item, var1)) {
               return var3;
            }

            d(var0.source, "Native placement context rejects the current block position, support or entity collision");
            return null;
         } else {
            d(var0.source, "The support " + var3.getBlockPos().toShortString() + " is gone or not trusted yet; waiting instead of an air place");
            return null;
         }
      } else {
         d(
            var0.source,
            "Current look does not reach the planned support face "
               + var0.hit.getDirection().getSerializedName()
               + " of "
               + var0.hit.getBlockPos().toShortString()
               + (var3 == null ? " (looking at no block of it)" : " (looking at its " + var3.getDirection().getSerializedName() + " face)")
               + "; check competing rotations or Crosshair aim"
         );
         return null;
      }
   }

   public static void T() {
      M.values().removeIf(var0x -> {
         if (q(var0x)) {
            return false;
         }

         d(var0x.source, "Intent expired or its target, player, world, focus or pause condition changed");
         return true;
      });
      Optional var0 = M.values()
         .stream()
         .filter(var0x -> var0x.visible && q(var0x) && _p.Q(var0x.source))
         .max(Comparator.comparingInt(var0x -> _p.x(var0x.source.getName())));
      if (var0.isEmpty() || !(((vi.g)var0.get()).source instanceof kv)) {
         ij.W(ij.r.D);
      }

      if (var0.isEmpty() || !(((vi.g)var0.get()).source instanceof kb)) {
         ij.W(ij.r.s);
      }

      if (var0.isEmpty() || !(((vi.g)var0.get()).source instanceof AutoAnchorModule)) {
         ij.W(ij.r.I);
      }

      if (var0.isPresent() && !(((vi.g)var0.get()).source instanceof ScaffoldModule)) {
         ij.W(ij.r.B);
      }

      if (!var0.isEmpty()) {
         vi.g var1 = (vi.g)var0.get();
         Minecraft var2 = Minecraft.getInstance();
         float[] var3 = b6.R(var2.player.getEyePosition(), var1.hit.getLocation());
         if (var1.source instanceof vi.d var4) {
            var4.aimPlacement(var3[0], var3[1]);
         } else {
            b6.f(b6.w.F, var3[0], var3[1], 1.0);
         }

         b6.e();
      }
   }

   private static void u(vi.g var0, boolean var1) {
      if (var0.actionReady.getAsBoolean()) {
         BlockHitResult var2 = M(var0, var1);
         int var3 = u(var0.item, var1);
         if (var2 != null && var3 != -1) {
            Minecraft var4 = Minecraft.getInstance();
            int var5 = var4.player.getInventory().getSelectedSlot();

            try {
               if (var3 >= 0) {
                  var4.player.getInventory().setSelectedSlot(var3);
               }

               InteractionHand var6 = var3 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
               boolean var7 = var4.gameMode.useItemOn(var4.player, var6, var2).consumesAction();
               if (var7) {
                  h8.i(var4.player, var6);
               }

               var0.completed.accept(var7);
            } finally {
               var4.player.getInventory().setSelectedSlot(var5);
               M.remove(var0.source, var0);
            }
         } else {
            d(var0.source, "Execution lost its valid support ray or inventory item");
         }
      }
   }

   private vi() {
   }

   public record a(BlockPos target, BlockState state) {
   }

   public interface d {
      void aimPlacement(float var1, float var2);

      default boolean trustsSupport(BlockPos var1) {
         return true;
      }

      default boolean waitsForSentLook() {
         return false;
      }
   }

   public interface f {
      boolean replaceable(BlockPos var1);

      boolean usableSupport(BlockPos var1);

      BlockHitResult ray(BlockPos var1, Vec3 var2, double var3);

      boolean contextMatches(BlockHitResult var1, BlockPos var2);
   }

   private record g(
      Module source,
      BlockPos target,
      BlockHitResult hit,
      Item item,
      double range,
      boolean place,
      boolean visible,
      BooleanSupplier valid,
      BooleanSupplier actionReady,
      Consumer<Boolean> completed,
      Object world,
      Object player,
      int tick
   ) {
   }

   public abstract static class n extends Module implements vi.d {
      protected final com.larpclient.module.b T = this.x("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1);
      protected final com.larpclient.module.b h = this.x("Target Range", "Search distance for survival opponents", 6.0, 1.0, 12.0, 0.5);
      protected final com.larpclient.module.b u;
      protected final com.larpclient.module.o O;
      protected final com.larpclient.module.m a;
      protected final com.larpclient.module.m g;
      protected final com.larpclient.module.m o;
      private long R;
      private String b = "Waiting for a target";

      protected n(String var1, String var2) {
         this(var1, var2, 0);
      }

      protected n(String var1, String var2, int var3) {
         super(var1, var2, Category.COMBAT, -1);
         this.u = this.x("Delay Per Action", "Ticks between native placement attempts", var3, 0.0, 20.0, 1.0);
         this.O = this.addSetting(
            new com.larpclient.module.o("Rotation", "Visible turns the player; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
         );
         this.a = this.O("Auto Switch", "Use hotbar blocks and restore the selected slot", true);
         this.g = this.O("Pause Using Item", "Pause while eating or using an item", true);
         this.o = this.O("Pause Mining", "Pause while the attack key is held", true);
      }

      protected void aim(float var1, float var2) {
         if (this.isEnabled()) {
            b6.f(b6.w.F, var1, var2, 1.0);
         }
      }

      @Override
      public final void aimPlacement(float var1, float var2) {
         if (this.isEnabled()) {
            this.aim(var1, var2);
         }
      }

      protected final void K(boolean var1) {
         this.R = mc.level.getGameTime() + Math.max(1, this.u.getValue().intValue());
         this.M(var1 ? "Waiting for world update" : "Placement rejected; retrying");
      }

      protected final com.larpclient.module.b x(String var1, String var2, double var3, double var5, double var7, double var9) {
         return this.addSetting(new com.larpclient.module.b(var1, var2, var3, var5, var7, var9));
      }

      protected final com.larpclient.module.m O(String var1, String var2, boolean var3) {
         return this.addSetting(new com.larpclient.module.m(var1, var2, var3));
      }

      @Override
      public Module.a getTickPhase() {
         return Module.a.T;
      }

      @Override
      public Set<com.larpclient.module.l> getNetworkImpacts() {
         return Set.of(com.larpclient.module.l.k);
      }

      protected final void M(String var1) {
         if (!var1.equals(this.b) && !var1.startsWith("Preparing") && !var1.equals("Waiting for world update")) {
            vi.d(this, var1);
         }

         this.b = var1;
      }

      @Override
      public String getStatus() {
         return this.b;
      }

      protected final boolean G() {
         return !this.isEnabled() ? false : this.Z() && mc.level.getGameTime() >= this.R;
      }

      protected final boolean Z() {
         return !this.isEnabled()
            ? false
            : _p.g() && mc.isWindowActive() && (!this.g.getValue() || !mc.player.isUsingItem()) && (!this.o.getValue() || !mc.options.keyAttack.isDown());
      }

      protected final boolean y(BlockPos var1, Item var2) {
         return !this.isEnabled()
            ? false
            : vi.u(var2, this.a.getValue()) != -1 && vi.X(var1, var2, this.T.getValue(), this.O.w("Visible"), this.a.getValue()) != null;
      }

      protected final boolean j(Player var1) {
         if (var1 != null
            && var1 != mc.player
            && var1.level() == mc.level
            && var1.isAlive()
            && !var1.isSpectator()
            && !var1.isCreative()
            && !var1.isRemoved()
            && !(mc.player.distanceToSqr(var1) > this.h.getValue() * this.h.getValue())) {
            MCFriendModule var2 = LarpClient.N().j().y(MCFriendModule.class);
            return var2 == null || !var2.M(var1.getName().getString());
         } else {
            return false;
         }
      }

      protected final Player P() {
         return mc.level.players().stream().filter(this::j).min(Comparator.comparingDouble(mc.player::distanceToSqr)).orElse(null);
      }

      protected final boolean n(BlockPos var1, Item var2, Player var3) {
         return this.isEnabled()
            ? vi.a(this, var1, var2, this.T.getValue(), this.O.w("Visible"), this.a.getValue(), () -> this.G() && this.j(var3), this::K)
            : false;
      }

      @Override
      public void onEnable() {
         this.R = 0L;
         vi.r.remove(this);
         vi.g.remove(this);
         _q.z(this.getName(), "Native placement enabled; rotation={}, range={}, autoSwitch={}", this.O.getValue(), this.T.getValue(), this.a.getValue());
      }

      @Override
      public void onDisable() {
         vi.J(this);
         this.R = 0L;
         this.b = "Waiting for a target";
         _q.c(this.getName(), "Placement intents cleared");
      }
   }
}
