package com.larpclient;

import com.larp.obf.q5682c6a8aca9b6ec.qac882a24641eea19;
import com.larpclient.module.AntiTrapModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.Category;
import com.larpclient.module.IgnoreEntityModule;
import com.larpclient.module.MCFriendModule;
import com.larpclient.module.Module;
import com.larpclient.module.ScaffoldModule;
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

public final class m {
   private static final Map<Module, m.i> y = new LinkedHashMap<>();
   private static final Map<Module, String> B = new HashMap<>();
   private static final Map<Module, Long> p = new HashMap<>();
   private static final Direction[] k = new Direction[]{Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST, Direction.DOWN};

   public static boolean X(Object var0, Object var1, Object var2, Object var3, int var4, int var5) {
      int var6 = var5 - var4;
      return var0 != null && var0 == var1 && var2 != null && var2 == var3 && var6 >= 0 && var6 <= 1;
   }

   public static String N(Module var0) {
      return B.getOrDefault(var0, "No supported face within native reach");
   }

   private static void d(Module var0, String var1) {
      B.put(var0, var1);
   }

   private static void F(Module var0, String var1) {
      d(var0, var1);
      long var2 = System.nanoTime();
      long var4 = p.getOrDefault(var0, Long.MIN_VALUE);
      if (var4 == Long.MIN_VALUE || var2 - var4 >= 2000000000L) {
         p.put(var0, var2);
         r4.V(var0.getName(), "Placement paused: {}", var1);
      }
   }

   public static void g(Module var0) {
      y.remove(var0);
      d8.K(var0);
   }

   public static void l() {
      y.clear();
      B.clear();
      p.clear();
   }

   public static boolean h(Module var0) {
      m.i var1 = y.get(var0);
      Minecraft var2 = Minecraft.getInstance();
      return var1 != null && var2.player != null && X(var1.world, var2.level, var1.player, var2.player, var1.tick, var2.player.tickCount);
   }

   public static int Q(Item var0, boolean var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 == null) {
         return -1;
      }

      int var3 = var2.getInventory().getSelectedSlot();
      if (E(var2.getMainHandItem(), var0)) {
         return var3;
      }

      if (E(var2.getOffhandItem(), var0)) {
         return -2;
      }

      if (var1) {
         for (int var4 = 0; var4 < 9; var4++) {
            if (E(var2.getInventory().getItem(var4), var0)) {
               return var4;
            }
         }
      }

      return -1;
   }

   private static boolean E(ItemStack var0, Item var1) {
      return var1 == Items.AIR ? var0.isEmpty() : var0.is(var1);
   }

   public static boolean Q(BlockPos var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.level != null
         && var1.level.hasChunkAt(var0)
         && !var1.level.isOutsideBuildHeight(var0)
         && var1.level.getWorldBorder().isWithinBounds(var0)
         && var1.level.getBlockState(var0).canBeReplaced();
   }

   private static BlockHitResult P(BlockPos var0, Vec3 var1, double var2, boolean var4) {
      return !d8.a() ? null : L(Minecraft.getInstance().player.getEyePosition(), var0, var1, var2, var4);
   }

   private static BlockHitResult L(Vec3 var0, BlockPos var1, Vec3 var2, double var3, boolean var5) {
      if (!d8.a()) {
         return null;
      }

      Minecraft var6 = Minecraft.getInstance();
      double var7 = Math.min(var3, var6.player.blockInteractionRange());
      Vec3 var9 = var0.add(var2.normalize().scale(var7));
      BlockHitResult var10 = var6.level.clip(new ClipContext(var0, var9, Block.OUTLINE, Fluid.NONE, var6.player));
      if (var10.getType() == Type.BLOCK && var10.getBlockPos().equals(var1) && !AntiTrapModule.V(var6.level.getBlockState(var1))) {
         if (var5) {
            return var10;
         }

         for (Entity var12 : var6.level.getEntities(var6.player, new AABB(var0, var10.getLocation()).inflate(1.0))) {
            if (var12.isPickable()
               && !var12.isSpectator()
               && !AntiTrapModule.A(var12)
               && !IgnoreEntityModule.G(var12)
               && d_.c(var0, var10.getLocation(), var12.getBoundingBox())) {
               return null;
            }
         }

         return var10;
      } else {
         return null;
      }
   }

   public static BlockHitResult s(BlockPos var0, Vec3 var1, Vec3 var2, double var3, boolean var5, m.d var6) {
      if (!var6.replaceable(var0)) {
         return null;
      }

      for (Direction var10 : Direction.values()) {
         BlockPos var11 = var0.relative(var10);
         if (var6.usableSupport(var11)) {
            Direction var12 = var10.getOpposite();

            for (Vec3 var14 : S(var11, var12)) {
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

   public static List<Vec3> S(BlockPos var0, Direction var1) {
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

   public static boolean S(BlockPos var0) {
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

   public static BlockHitResult V(BlockPos var0, Item var1, double var2, boolean var4) {
      return J(var0, var1, var2, var4, true);
   }

   public static BlockHitResult J(BlockPos var0, final Item var1, double var2, boolean var4, final boolean var5) {
      if (var1 instanceof BlockItem && d8.a()) {
         Minecraft var6 = Minecraft.getInstance();
         return s(var0, var6.player.getEyePosition(), var6.player.getLookAngle(), Math.min(var2, var6.player.blockInteractionRange()), var4, new m.d() {
            @Override
            public boolean replaceable(BlockPos var1x) {
               return m.Q(var1x);
            }

            @Override
            public boolean usableSupport(BlockPos var1x) {
               return m.S(var1x);
            }

            @Override
            public BlockHitResult ray(BlockPos var1x, Vec3 var2x, double var3) {
               return m.P(var1x, var2x, var3, true);
            }

            @Override
            public boolean contextMatches(BlockHitResult var1x, BlockPos var2x) {
               return m.C(var1x, var2x, var1, var5);
            }
         });
      } else {
         return null;
      }
   }

   private static boolean C(BlockHitResult var0, BlockPos var1, Item var2, boolean var3) {
      return W(var0, var2, var3, var1) != null;
   }

   public static m.r D(BlockHitResult var0, Item var1, boolean var2) {
      return W(var0, var1, var2, null);
   }

   private static m.r W(BlockHitResult var0, Item var1, boolean var2, BlockPos var3) {
      Minecraft var4 = Minecraft.getInstance();
      int var5 = Q(var1, var2);
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
            ? new m.r(var10, var11)
            : null;
      } else {
         return null;
      }
   }

   public static BlockHitResult c(BlockPos var0, Vec3 var1, double var2) {
      return P(var0, var1, var2, true);
   }

   public static BlockHitResult Y(Vec3 var0, BlockPos var1, Vec3 var2, double var3) {
      return L(var0, var1, var2, var3, true);
   }

   public static boolean G(BlockPos var0, double var1) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (var3 == null) {
         return false;
      }

      qac882a24641eea19 var4 = (qac882a24641eea19)var3;
      float var5 = var4.larpYRotLast();
      float var6 = var4.larpXRotLast();
      return Float.isFinite(var5) && Float.isFinite(var6) && P(var0, Vec3.directionFromRotation(var6, var5), var1, true) != null;
   }

   public static boolean U(Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      return J(var0, var1, var2, var3, var5, var6, var7, () -> true, var8);
   }

   public static boolean J(
      Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, BooleanSupplier var8, Consumer<Boolean> var9
   ) {
      if (Q(var2, var6) == -1) {
         d(var0, "Required item missing from held hands or hotbar");
         return false;
      } else {
         BlockHitResult var10 = J(var1, var2, var3, var5, var6);
         if (var10 == null) {
            d(var0, "No valid supported face, collision-free position or aim within native reach");
            return false;
         } else {
            return X(var0, var1, var10, var2, var3, true, var5, var6, var7, var8, var9);
         }
      }
   }

   public static boolean e(Module var0, BlockPos var1, Item var2, double var3, boolean var5, boolean var6, BooleanSupplier var7, Consumer<Boolean> var8) {
      if (d8.a() && Q(var2, var6) != -1) {
         Minecraft var9 = Minecraft.getInstance();
         Vec3 var10 = var5 ? Vec3.atCenterOf(var1).subtract(var9.player.getEyePosition()) : var9.player.getLookAngle();
         BlockHitResult var11 = P(var1, var10, var3, false);
         if (var11 == null && var5) {
            var11 = p(var1, var9.player.getEyePosition(), var3);
         }

         return var11 == null ? false : X(var0, var1, var11, var2, var3, false, var5, var6, var7, () -> true, var8);
      } else {
         return false;
      }
   }

   private static BlockHitResult p(BlockPos var0, Vec3 var1, double var2) {
      Vec3 var4 = Vec3.atCenterOf(var0);

      for (Direction var8 : k) {
         Vec3 var9 = new Vec3(var8.getStepX(), var8.getStepY(), var8.getStepZ());
         if (!(var1.subtract(var4.add(var9.scale(0.5))).dot(var9) <= 0.0)) {
            for (Vec3 var11 : S(var0, var8)) {
               BlockHitResult var12 = P(var0, var11.subtract(var1), var2, false);
               if (var12 != null && var12.getDirection() == var8) {
                  return var12;
               }
            }
         }
      }

      return null;
   }

   private static boolean X(
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
      m.i var13 = new m.i(var0, var1.immutable(), var2, var3, var4, var6, var7, var9, var10, var11, var12.level, var12.player, var12.player.tickCount);
      y.put(var0, var13);
      d8.A(var0, () -> J(var13, var8), () -> l(var13) && var13.actionReady.getAsBoolean() && Q(var3, var8) != -1 && n(var13, var8) != null);
      return true;
   }

   private static boolean l(m.i var0) {
      Minecraft var1 = Minecraft.getInstance();
      return y.get(var0.source) == var0
         && X(var0.world, var1.level, var0.player, var1.player, var0.tick, var1.player == null ? 0 : var1.player.tickCount)
         && var0.source.isRuntimeActive()
         && d8.a()
         && var1.isWindowActive()
         && var0.valid.getAsBoolean();
   }

   private static BlockHitResult n(m.i var0, boolean var1) {
      if (!l(var0)) {
         return null;
      }

      Minecraft var2 = Minecraft.getInstance();
      BlockHitResult var3 = P(var0.hit.getBlockPos(), var2.player.getLookAngle(), var0.range, var0.place);
      if (var3 != null && var3.getDirection() == var0.hit.getDirection()) {
         if (var0.place && !var2.player.isShiftKeyDown()) {
            BlockState var4 = var2.level.getBlockState(var3.getBlockPos());
            if (var4.hasBlockEntity() || var4.getBlock() instanceof RespawnAnchorBlock || var4.getBlock() instanceof CraftingTableBlock) {
               return null;
            }
         }

         if (!var0.place || Q(var0.target) && C(var3, var0.target, var0.item, var1)) {
            return var3;
         }

         F(var0.source, "Native placement context rejects the current block position, support or entity collision");
         return null;
      } else {
         F(var0.source, "Current look does not reach the planned support face; check competing rotations or Crosshair aim");
         return null;
      }
   }

   public static void W() {
      y.values().removeIf(var0x -> {
         if (l(var0x)) {
            return false;
         }

         F(var0x.source, "Intent expired or its target, player, world, focus or pause condition changed");
         return true;
      });
      Optional var0 = y.values()
         .stream()
         .filter(var0x -> var0x.visible && l(var0x) && d8.m(var0x.source))
         .max(Comparator.comparingInt(var0x -> d8.J(var0x.source.getName())));
      if (var0.isEmpty() || !(((m.i)var0.get()).source instanceof com.larpclient.module.t)) {
         rr.N(rr.f.e);
      }

      if (var0.isEmpty() || !(((m.i)var0.get()).source instanceof com.larpclient.module.k)) {
         rr.N(rr.f.L);
      }

      if (var0.isEmpty() || !(((m.i)var0.get()).source instanceof AutoAnchorModule)) {
         rr.N(rr.f.q);
      }

      if (var0.isPresent() && !(((m.i)var0.get()).source instanceof ScaffoldModule)) {
         rr.N(rr.f.x);
      }

      if (!var0.isEmpty()) {
         m.i var1 = (m.i)var0.get();
         Minecraft var2 = Minecraft.getInstance();
         float[] var3 = ep.g(var2.player.getEyePosition(), var1.hit.getLocation());
         if (var1.source instanceof m.s var4) {
            var4.aimPlacement(var3[0], var3[1]);
         } else {
            ep.C(ep.z.d, var3[0], var3[1], 1.0);
         }

         ep.u();
      }
   }

   private static void J(m.i var0, boolean var1) {
      if (var0.actionReady.getAsBoolean()) {
         BlockHitResult var2 = n(var0, var1);
         int var3 = Q(var0.item, var1);
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
                  hp.Q(var4.player, var6);
               }

               var0.completed.accept(var7);
            } finally {
               var4.player.getInventory().setSelectedSlot(var5);
               y.remove(var0.source, var0);
            }
         } else {
            F(var0.source, "Execution lost its valid support ray or inventory item");
         }
      }
   }

   private m() {
   }

   public interface d {
      boolean replaceable(BlockPos var1);

      boolean usableSupport(BlockPos var1);

      BlockHitResult ray(BlockPos var1, Vec3 var2, double var3);

      boolean contextMatches(BlockHitResult var1, BlockPos var2);
   }

   private record i(
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

   public abstract static class o extends Module implements m.s {
      protected final com.larpclient.module.lc U = this.z("Range", "Placement reach capped by native block reach", 4.1, 1.0, 6.0, 0.1);
      protected final com.larpclient.module.lc b = this.z("Target Range", "Search distance for survival opponents", 6.0, 1.0, 12.0, 0.5);
      protected final com.larpclient.module.lc S;
      protected final com.larpclient.module.lp Y;
      protected final com.larpclient.module.ln x;
      protected final com.larpclient.module.ln w;
      protected final com.larpclient.module.ln d;
      private long L;
      private String H = "Waiting for a target";

      protected o(String var1, String var2) {
         this(var1, var2, 0);
      }

      protected o(String var1, String var2, int var3) {
         super(var1, var2, Category.COMBAT, -1);
         this.S = this.z("Delay Per Action", "Ticks between native placement attempts", var3, 0.0, 20.0, 1.0);
         this.Y = this.addSetting(
            new com.larpclient.module.lp("Rotation", "Visible turns the player; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
         );
         this.x = this.h("Auto Switch", "Use hotbar blocks and restore the selected slot", true);
         this.w = this.h("Pause Using Item", "Pause while eating or using an item", true);
         this.d = this.h("Pause Mining", "Pause while the attack key is held", true);
      }

      protected void aim(float var1, float var2) {
         if (this.isEnabled()) {
            ep.C(ep.z.d, var1, var2, 1.0);
         }
      }

      @Override
      public final void aimPlacement(float var1, float var2) {
         if (this.isEnabled()) {
            this.aim(var1, var2);
         }
      }

      protected final void a(boolean var1) {
         this.L = mc.level.getGameTime() + Math.max(1, this.S.getValue().intValue());
         this.H(var1 ? "Waiting for world update" : "Placement rejected; retrying");
      }

      protected final com.larpclient.module.lc z(String var1, String var2, double var3, double var5, double var7, double var9) {
         return this.addSetting(new com.larpclient.module.lc(var1, var2, var3, var5, var7, var9));
      }

      protected final com.larpclient.module.ln h(String var1, String var2, boolean var3) {
         return this.addSetting(new com.larpclient.module.ln(var1, var2, var3));
      }

      @Override
      public Module.b getTickPhase() {
         return Module.b.w;
      }

      @Override
      public Set<com.larpclient.module.ld> getNetworkImpacts() {
         return Set.of(com.larpclient.module.ld.c);
      }

      protected final void H(String var1) {
         if (!var1.equals(this.H) && !var1.startsWith("Preparing") && !var1.equals("Waiting for world update")) {
            m.F(this, var1);
         }

         this.H = var1;
      }

      @Override
      public String getStatus() {
         return this.H;
      }

      protected final boolean K() {
         return !this.isEnabled() ? false : this.i() && mc.level.getGameTime() >= this.L;
      }

      protected final boolean i() {
         return !this.isEnabled()
            ? false
            : d8.a() && mc.isWindowActive() && (!this.w.getValue() || !mc.player.isUsingItem()) && (!this.d.getValue() || !mc.options.keyAttack.isDown());
      }

      protected final boolean f(BlockPos var1, Item var2) {
         return !this.isEnabled()
            ? false
            : m.Q(var2, this.x.getValue()) != -1 && m.J(var1, var2, this.U.getValue(), this.Y.i("Visible"), this.x.getValue()) != null;
      }

      protected final boolean b(Player var1) {
         if (var1 != null
            && var1 != mc.player
            && var1.level() == mc.level
            && var1.isAlive()
            && !var1.isSpectator()
            && !var1.isCreative()
            && !var1.isRemoved()
            && !(mc.player.distanceToSqr(var1) > this.b.getValue() * this.b.getValue())) {
            MCFriendModule var2 = LarpClient.y().e().y(MCFriendModule.class);
            return var2 == null || !var2.e(var1.getName().getString());
         } else {
            return false;
         }
      }

      protected final Player o() {
         return mc.level.players().stream().filter(this::b).min(Comparator.comparingDouble(mc.player::distanceToSqr)).orElse(null);
      }

      protected final boolean H(BlockPos var1, Item var2, Player var3) {
         return this.isEnabled()
            ? m.U(this, var1, var2, this.U.getValue(), this.Y.i("Visible"), this.x.getValue(), () -> this.K() && this.b(var3), this::a)
            : false;
      }

      @Override
      public void onEnable() {
         this.L = 0L;
         m.p.remove(this);
         m.B.remove(this);
         r4.V(this.getName(), "Native placement enabled; rotation={}, range={}, autoSwitch={}", this.Y.getValue(), this.U.getValue(), this.x.getValue());
      }

      @Override
      public void onDisable() {
         m.g(this);
         this.L = 0L;
         this.H = "Waiting for a target";
         r4.g(this.getName(), "Placement intents cleared");
      }
   }

   public record r(BlockPos target, BlockState state) {
   }

   public interface s {
      void aimPlacement(float var1, float var2);
   }
}
