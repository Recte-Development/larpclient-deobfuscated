package com.larpclient;

import com.larp.obf.qbac0b262827a15ac.q12d31c89547aa6a5;
import com.larpclient.module.AntiTrapModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.Category;
import com.larpclient.module.IgnoreEntityModule;
import com.larpclient.module.MCFriendModule;
import com.larpclient.module.Module;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.dh;
import com.larpclient.module.dm;
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

public final class _d {
   private static final Map<Module, _d.t> Y = new LinkedHashMap<>();
   private static final Map<Module, String> l = new HashMap<>();
   private static final Map<Module, Long> v = new HashMap<>();
   private static final Direction[] G = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};

   public static boolean o(Object var0, Object var1, Object var2, Object var3, int var4, int var5) {
      int var6 = var5 - var4;
      return var0 != null && var0 == var1 && var2 != null && var2 == var3 && var6 >= 0 && var6 <= 1;
   }

   public static String T(Module var0) {
      return l.getOrDefault(var0, "No supported face within native reach");
   }

   private static void K(Module var0, String var1) {
      l.put(var0, var1);
   }

   private static void y(Module var0, String var1) {
      K(var0, var1);
      long var2 = System.nanoTime();
      long var4 = v.getOrDefault(var0, Long.MIN_VALUE);
      if (var4 == Long.MIN_VALUE || var2 - var4 >= 2000000000L) {
         v.put(var0, var2);
         mt.K(var0.getName(), "Placement paused: {}", var1);
      }
   }

   public static void N(Module var0) {
      Y.remove(var0);
      _m.w(var0);
   }

   public static void d() {
      Y.clear();
      l.clear();
      v.clear();
   }

   public static boolean p(Module var0) {
      _d.t var1 = Y.get(var0);
      Minecraft var2 = Minecraft.getInstance();
      return var1 != null && var2.player != null && o(var1.world, var2.level, var1.player, var2.player, var1.tick, var2.player.tickCount);
   }

   public static int B(Item var0, boolean var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 == null) {
         return -1;
      }

      int var3 = var2.getInventory().getSelectedSlot();
      if (l(var2.getMainHandItem(), var0)) {
         return var3;
      }

      if (l(var2.getOffhandItem(), var0)) {
         return -2;
      }

      if (var1) {
         for (int var4 = 0; var4 < 9; var4++) {
            if (l(var2.getInventory().getItem(var4), var0)) {
               return var4;
            }
         }
      }

      return -1;
   }

   private static boolean l(ItemStack var0, Item var1) {
      return var1 == Items.AIR ? var0.isEmpty() : var0.is(var1);
   }

   public static boolean Z(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.level != null
         && var1.level.hasChunkAt(var0)
         && !var1.level.isOutsideBuildHeight(var0)
         && var1.level.getWorldBorder().isWithinBounds(var0)
         && var1.level.getBlockState(var0).canBeReplaced();
   }

   private static BlockHitResult C(BlockPos var0, Vec3 var1, double var2, boolean var4) {
      return !_m.z() ? null : k(Minecraft.getInstance().player.getEyePosition(), var0, var1, var2, var4);
   }

   private static BlockHitResult k(Vec3 var0, BlockPos var1, Vec3 var2, double var3, boolean var5) {
      if (!_m.z()) {
         return null;
      }

      Minecraft var6 = Minecraft.getInstance();
      double var7 = Math.min(var3, var6.player.blockInteractionRange());
      Vec3 var9 = var0.add(var2.normalize().scale(var7));
      BlockHitResult var10 = var6.level.clip(new ClipContext(var0, var9, Block.OUTLINE, Fluid.NONE, var6.player));
      if (var10.getType() == Type.BLOCK && var10.getBlockPos().equals(var1) && !AntiTrapModule.z(var6.level.getBlockState(var1))) {
         if (var5) {
            return var10;
         }

         for (Entity var12 : var6.level.getEntities(var6.player, new AABB(var0, var10.getLocation()).inflate(1.0))) {
            if (var12.isPickable()
               && !var12.isSpectator()
               && !AntiTrapModule.l(var12)
               && !IgnoreEntityModule.P(var12)
               && p1.H(var0, var10.getLocation(), var12.getBoundingBox())) {
               return null;
            }
         }

         return var10;
      } else {
         return null;
      }
   }

   public static BlockHitResult Q(BlockPos var0, Vec3 var1, Vec3 var2, double var3, boolean var5, _d.c var6) {
      if (!var6.replaceable(var0)) {
         return null;
      }

      for (Direction var10 : Direction.values()) {
         BlockPos var11 = var0.relative(var10);
         if (var6.usableSupport(var11)) {
            Direction var12 = var10.getOpposite();

            for (Vec3 var14 : k(var11, var12)) {
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

   public static List<Vec3> k(BlockPos var0, Direction var1) {
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

   public static boolean A(BlockPos var0) {
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

   public static BlockHitResult d(BlockPos var0, Item var1, double var2, boolean var4) {
      return g(var0, var1, var2, var4, true);
   }

   public static BlockHitResult g(BlockPos var0, final Item var1, double var2, boolean var4, final boolean var5) {
      if (var1 instanceof BlockItem && _m.z()) {
         Minecraft var6 = Minecraft.getInstance();
         return Q(var0, var6.player.getEyePosition(), var6.player.getLookAngle(), Math.min(var2, var6.player.blockInteractionRange()), var4, new _d.c() {
            @Override
            public boolean replaceable(BlockPos var1x) {
               return _d.Z(var1x);
            }

            @Override
            public boolean usableSupport(BlockPos var1x) {
               return _d.A(var1x);
            }

            @Override
            public BlockHitResult ray(BlockPos var1x, Vec3 var2x, double var3) {
               return _d.C(var1x, var2x, var3, true);
            }

            @Override
            public boolean contextMatches(BlockHitResult var1x, BlockPos var2x) {
               return _d.g(var1x, var2x, var1, var5);
            }
         });
      } else {
         return null;
      }
   }

   private static boolean g(BlockHitResult var0, BlockPos var1, Item var2, boolean var3) {
      return Z(var0, var2, var3, var1) != null;
   }

   public static _d.p s(BlockHitResult var0, Item var1, boolean var2) {
      return Z(var0, var1, var2, null);
   }

   private static _d.p Z(BlockHitResult var0, Item var1, boolean var2, BlockPos var3) {
      Minecraft var4 = Minecraft.getInstance();
      int var5 = B(var1, var2);
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
            ? new _d.p(var10, var11)
            : null;
      } else {
         return null;
      }
   }

   public static BlockHitResult Y(BlockPos var0, Vec3 var1, double var2) {
      return C(var0, var1, var2, true);
   }

   public static BlockHitResult q(Vec3 var0, BlockPos var1, Vec3 var2, double var3) {
      return k(var0, var1, var2, var3, true);
   }

   public static boolean t(BlockPos var0, double var1) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (var3 == null) {
         return false;
      }

      q12d31c89547aa6a5 var4 = (q12d31c89547aa6a5)var3;
      float var5 = var4.larpYRotLast();
      float var6 = var4.larpXRotLast();
      return Float.isFinite(var5) && Float.isFinite(var6) && C(var0, Vec3.directionFromRotation(var6, var5), var1, true) != null;
   }

   public static boolean a(Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      return F(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   public static boolean F(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (B(var2, var6) == -1) {
         K(var0, "Required item missing from held hands or hotbar");
         return false;
      } else {
         BlockHitResult var10 = g(var1, var2, var3, var5, var6);
         if (var10 == null) {
            K(var0, "No valid supported face, collision-free position or aim within native reach");
            return false;
         } else {
            return s(var0, var1, var10, var2, var3, true, var5, var6, var7, var8, var9);
         }
      }
   }

   public static boolean U(Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      if (_m.z() && B(var2, var6) != -1) {
         Minecraft var9 = Minecraft.getInstance();
         Vec3 var10 = var5 ? Vec3.atCenterOf(var1).subtract(var9.player.getEyePosition()) : var9.player.getLookAngle();
         BlockHitResult var11 = C(var1, var10, var3, false);
         if (var11 == null && var5) {
            var11 = c(var1, var9.player.getEyePosition(), var3);
         }

         return var11 == null ? false : s(var0, var1, var11, var2, var3, false, var5, var6, var7, () -> true, var8);
      } else {
         return false;
      }
   }

   private static BlockHitResult c(BlockPos var0, Vec3 var1, double var2) {
      Vec3 var4 = Vec3.atCenterOf(var0);

      for (Direction var8 : G) {
         Vec3 var9 = new Vec3(var8.getStepX(), var8.getStepY(), var8.getStepZ());
         if (!(var1.subtract(var4.add(var9.scale(0.5))).dot(var9) <= 0.0)) {
            for (Vec3 var11 : k(var0, var8)) {
               BlockHitResult var12 = C(var0, var11.subtract(var1), var2, false);
               if (var12 != null && var12.getDirection() == var8) {
                  return var12;
               }
            }
         }
      }

      return null;
   }

   private static boolean s(
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
      _d.t var13 = new _d.t(var0, var1.immutable(), var2, var3, var4, var6, var7, var9, var10, var11, var12.level, var12.player, var12.player.tickCount);
      Y.put(var0, var13);
      _m.F(var0, () -> o(var13, var8), () -> M(var13) && var13.actionReady.getAsBoolean() && B(var3, var8) != -1 && t(var13, var8) != null);
      return true;
   }

   private static boolean M(_d.t var0) {
      Minecraft var1 = Minecraft.getInstance();
      return Y.get(var0.source) == var0
         && o(var0.world, var1.level, var0.player, var1.player, var0.tick, var1.player == null ? 0 : var1.player.tickCount)
         && var0.source.isRuntimeActive()
         && _m.z()
         && var1.isWindowActive()
         && var0.valid.getAsBoolean();
   }

   private static BlockHitResult t(_d.t var0, boolean var1) {
      if (!M(var0)) {
         return null;
      }

      Minecraft var2 = Minecraft.getInstance();
      BlockHitResult var3 = C(var0.hit.getBlockPos(), var2.player.getLookAngle(), var0.range, var0.place);
      if (var3 != null && var3.getDirection() == var0.hit.getDirection()) {
         if (var0.place && !var2.player.isShiftKeyDown()) {
            BlockState var4 = var2.level.getBlockState(var3.getBlockPos());
            if (var4.hasBlockEntity() || var4.getBlock() instanceof RespawnAnchorBlock || var4.getBlock() instanceof CraftingTableBlock) {
               return null;
            }
         }

         if (!var0.place || Z(var0.target) && g(var3, var0.target, var0.item, var1)) {
            return var3;
         }

         y(var0.source, "Native placement context rejects the current block position, support or entity collision");
         return null;
      } else {
         y(var0.source, "Current look does not reach the planned support face; check competing rotations or Crosshair aim");
         return null;
      }
   }

   public static void h() {
      Y.values().removeIf(var0x -> {
         if (M(var0x)) {
            return false;
         }

         y(var0x.source, "Intent expired or its target, player, world, focus or pause condition changed");
         return true;
      });
      Optional var0 = Y.values()
         .stream()
         .filter(var0x -> var0x.visible && M(var0x) && _m.f(var0x.source))
         .max(Comparator.comparingInt(var0x -> _m.a(var0x.source.getName())));
      if (var0.isEmpty() || !(((_d.t)var0.get()).source instanceof com.larpclient.module.f)) {
         tq.A(tq.y.J);
      }

      if (var0.isEmpty() || !(((_d.t)var0.get()).source instanceof com.larpclient.module.w)) {
         tq.A(tq.y.a);
      }

      if (var0.isEmpty() || !(((_d.t)var0.get()).source instanceof AutoAnchorModule)) {
         tq.A(tq.y.g);
      }

      if (var0.isPresent() && !(((_d.t)var0.get()).source instanceof ScaffoldModule)) {
         tq.A(tq.y.Y);
      }

      if (!var0.isEmpty()) {
         _d.t var1 = (_d.t)var0.get();
         Minecraft var2 = Minecraft.getInstance();
         float[] var3 = m9.Z(var2.player.getEyePosition(), var1.hit.getLocation());
         if (var1.source instanceof _d.s var4) {
            var4.aimPlacement(var3[0], var3[1]);
         } else {
            m9.c(m9.s.p, var3[0], var3[1], 1.0);
         }

         m9.j();
      }
   }

   private static void o(_d.t var0, boolean var1) {
      if (var0.actionReady.getAsBoolean()) {
         BlockHitResult var2 = t(var0, var1);
         int var3 = B(var0.item, var1);
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
                  _8.G(var4.player, var6);
               }

               var0.completed.accept(var7);
            } finally {
               var4.player.getInventory().setSelectedSlot(var5);
               Y.remove(var0.source, var0);
            }
         } else {
            y(var0.source, "Execution lost its valid support ray or inventory item");
         }
      }
   }

   private _d() {
   }

   public interface c {
      boolean replaceable(BlockPos var1);

      boolean usableSupport(BlockPos var1);

      BlockHitResult ray(BlockPos var1, Vec3 var2, double var3);

      boolean contextMatches(BlockHitResult var1, BlockPos var2);
   }

   public record p(BlockPos target, BlockState state) {
   }

   public interface s {
      void aimPlacement(float var1, float var2);
   }

   private record t(
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

   public abstract static class u extends Module implements _d.s {
      protected final com.larpclient.module.d9 j = this.S("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1);
      protected final com.larpclient.module.d9 l = this.S("Target Range", "Search distance for survival opponents", 6.0, 1.0, 12.0, 0.5);
      protected final com.larpclient.module.d9 P;
      protected final com.larpclient.module.d6 M;
      protected final dh d;
      protected final dh w;
      protected final dh G;
      private long m;
      private String v = "Waiting for a target";

      protected u(String var1, String var2) {
         this(var1, var2, 0);
      }

      protected u(String var1, String var2, int var3) {
         super(var1, var2, Category.COMBAT, -1);
         this.P = this.S("Delay Per Action", "Ticks between native placement attempts", var3, 0.0, 20.0, 1.0);
         this.M = this.addSetting(
            new com.larpclient.module.d6("Rotation", "Visible turns the player; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
         );
         this.d = this.p("Auto Switch", "Use hotbar blocks and restore the selected slot", true);
         this.w = this.p("Pause Using Item", "Pause while eating or using an item", true);
         this.G = this.p("Pause Mining", "Pause while the attack key is held", true);
      }

      protected void aim(float var1, float var2) {
         if (this.isEnabled()) {
            m9.c(m9.s.p, var1, var2, 1.0);
         }
      }

      @Override
      public final void aimPlacement(float var1, float var2) {
         if (this.isEnabled()) {
            this.aim(var1, var2);
         }
      }

      protected final void L(boolean var1) {
         this.m = mc.level.getGameTime() + Math.max(1, this.P.P().intValue());
         this.e(var1 ? "Waiting for world update" : "Placement rejected; retrying");
      }

      protected final com.larpclient.module.d9 S(String var1, String var2, double var3, double var5, double var7, double var9) {
         return this.addSetting(new com.larpclient.module.d9(var1, var2, var3, var5, var7, var9));
      }

      protected final dh p(String var1, String var2, boolean var3) {
         return this.addSetting(new dh(var1, var2, var3));
      }

      @Override
      public Module.f getTickPhase() {
         return Module.f.Z;
      }

      @Override
      public Set<dm> getNetworkImpacts() {
         return Set.of(dm.j);
      }

      protected final void e(String var1) {
         if (!var1.equals(this.v) && !var1.startsWith("Preparing") && !var1.equals("Waiting for world update")) {
            _d.y(this, var1);
         }

         this.v = var1;
      }

      @Override
      public String getStatus() {
         return this.v;
      }

      protected final boolean s() {
         return !this.isEnabled() ? false : this.F() && mc.level.getGameTime() >= this.m;
      }

      protected final boolean F() {
         return !this.isEnabled()
            ? false
            : _m.z() && mc.isWindowActive() && (!this.w.P() || !mc.player.isUsingItem()) && (!this.G.P() || !mc.options.keyAttack.isDown());
      }

      protected final boolean C(BlockPos var1, Item var2) {
         return !this.isEnabled() ? false : _d.B(var2, this.d.P()) != -1 && _d.g(var1, var2, this.j.P(), this.M.S("Visible"), this.d.P()) != null;
      }

      protected final boolean p(Player var1) {
         if (var1 != null
            && var1 != mc.player
            && var1.level() == mc.level
            && var1.isAlive()
            && !var1.isSpectator()
            && !var1.isCreative()
            && !var1.isRemoved()
            && !(mc.player.distanceToSqr(var1) > this.l.P() * this.l.P())) {
            MCFriendModule var2 = LarpClient.F().x().V(MCFriendModule.class);
            return var2 == null || !var2.V(var1.getName().getString());
         } else {
            return false;
         }
      }

      protected final Player a() {
         return mc.level.players().stream().filter(this::p).min(Comparator.comparingDouble(mc.player::distanceToSqr)).orElse(null);
      }

      protected final boolean I(BlockPos var1, Item var2, Player var3) {
         return this.isEnabled() ? _d.a(this, var1, var2, this.j.P(), this.M.S("Visible"), this.d.P(), () -> this.s() && this.p(var3), this::L) : false;
      }

      @Override
      public void onEnable() {
         this.m = 0L;
         _d.v.remove(this);
         _d.l.remove(this);
         mt.K(this.getName(), "Native placement enabled; rotation={}, range={}, autoSwitch={}", this.M.P(), this.j.P(), this.d.P());
      }

      @Override
      public void onDisable() {
         _d.N(this);
         this.m = 0L;
         this.v = "Waiting for a target";
         mt.U(this.getName(), "Placement intents cleared");
      }
   }
}
