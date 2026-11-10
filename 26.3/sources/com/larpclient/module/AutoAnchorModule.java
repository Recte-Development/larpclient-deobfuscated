package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.ai;
import com.larpclient.b6;
import com.larpclient.ij;
import com.larpclient.pl;
import com.larpclient.vi;
import com.larpclient.wp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoAnchorModule extends Module implements ai.e, vi.d {
   private static final int z = 12;
   private static final int s = 2;
   private static AutoAnchorModule U;
   private final b x = this.i("Range", "Anchor reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final b L = this.i("Delay", "Ticks between confirmed anchor steps", 0.0, 0.0, 20.0, 1.0);
   private final m v = this.m("Auto Switch", "Use hotbar items and restore the selected slot after each step", true);
   private final m T = this.m("Safe Anchor", "Place a block between you and the anchor before detonating, where it leaves you least exposed", true);
   private final o D = this.addSetting(
      new o("Shield Block", "Block placed as the shield; the blast hurts players before it breaks blocks", "Glowstone", "Glowstone", "Obsidian")
   );
   private final m P = this.m("Players", "Target other survival players", true);
   private final m N = this.m("Mobs", "Also target hostile mobs for combat or testing", false);
   private final b t = this.i("Target Range", "Search distance for opponents", 8.0, 2.0, 14.0, 0.5);
   private final b C = this.i("Min Damage", "Minimum estimated target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final m Z = this.m("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final b Y = this.i("Max Self Damage", "Maximum estimated self damage in HP", 8.0, 0.0, 36.0, 0.5);
   private final m a = this.m("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final o E = this.addSetting(
      new o("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final ai k;
   private AutoAnchorModule.t l;
   private String R;
   private String Q = "";
   private String I = "";
   private String F = "Waiting for a target";
   private BooleanSupplier y;
   private long w;
   private long G;
   private Object J;
   private Object g;
   private Object n;
   private Object c;
   private int o;

   public AutoAnchorModule() {
      super("AutoAnchor", "Automatically place, charge and detonate respawn anchors", Category.COMBAT, -1);
      U = this;
      this.D.a(this.T::getValue);
      this.Y.a(this.Z::getValue);
      this.E.w(var1 -> {
         this.k("Rotation changed");
         this.r();
      });
      this.k = new ai(this, var1 -> this.addSetting(var1), 0.5, 0.3);
   }

   private b i(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   private m m(String var1, String var2, boolean var3) {
      return this.addSetting(new m(var1, var2, var3));
   }

   public static AutoAnchorModule q() {
      return U;
   }

   public List<r<?>> O() {
      return List.of(this.x, this.L, this.v, this.T, this.D);
   }

   public List<r<?>> F() {
      return List.of(this.P, this.N, this.t);
   }

   public List<r<?>> o() {
      return List.of(this.C, this.Z, this.Y, this.a);
   }

   public List<r<?>> G() {
      return List.of(this.E);
   }

   @Override
   public ai placementHighlight() {
      return this.k;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F, com.larpclient.module.l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.F : "";
   }

   @Override
   public void onEnable() {
      this.J = this.g = null;
      this.k("Waiting for a target");
      this.k.b();
      _q.z(
         "AutoAnchor",
         "Enabled; rotation={}, range={}, safeAnchor={}, minDamage={}, maxSelf={}",
         this.E.getValue(),
         this.x.getValue(),
         this.T.getValue(),
         this.C.getValue(),
         this.Z.getValue() ? this.Y.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.k("Waiting for a target");
      this.r();
      this.k.m();
      this.J = this.g = null;
      _q.z("AutoAnchor", "Disabled; anchor intents and server waits cleared");
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (ij.a(ij.r.I)) {
            b6.f(b6.w.F, var1, var2, 1.0);
            if (b6.c(b6.w.F)) {
               this.n = mc.player;
               this.c = mc.level;
               this.o = mc.player.tickCount;
               ij.V(ij.r.I);
            } else {
               this.r();
            }
         }
      }
   }

   private void r() {
      this.n = this.c = null;
      ij.W(ij.r.I);
   }

   public boolean K() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.E.w("Visible")
            && mc.player != null
            && this.n == mc.player
            && this.c == mc.level
            && mc.player.tickCount >= this.o
            && mc.player.tickCount - this.o <= 1
            && (!this.a.getValue() || !mc.player.isUsingItem());
   }

   public float Q(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ij.L() == ij.r.I && b6.c(b6.w.F)) {
            mc.player.setYHeadRot(mc.player.getYRot());
            return mc.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onTick() {
      this.k.H(null);
      vi.J(this);
      if (!this.isInGame()) {
         this.k("Waiting for a world");
      } else {
         if (this.J != mc.player || this.g != mc.level) {
            this.k("Waiting for a target");
            this.J = mc.player;
            this.g = mc.level;
         }

         try {
            this.D();
         } finally {
            int var10000 = this.F.equals(this.I);
            boolean var5 = false;
            byte var4 = (byte)var10000;
            var10000 = Integer.signum(var4);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.I = this.F;
               _q.c("AutoAnchor", "Status: {}", this.F);
            }
         }
      }
   }

   private void D() {
      long var1 = mc.level.getGameTime();
      if (this.R != null) {
         if (!this.y.getAsBoolean()) {
            if (var1 - this.w > 12L) {
               _q.z("AutoAnchor", "No server update after {} ticks for: {}; replanning", 12, this.R);
               this.k("Server did not confirm the last step");
               return;
            }

            if (this.l != null) {
               this.k.H(this.l.anchor());
            }

            this.F = "Waiting for " + this.R;
            return;
         }

         _q.c("AutoAnchor", "Server confirmed: {}", this.R);
         if (this.R.equals("detonation")) {
            this.l = null;
         }

         this.R = null;
         this.G = var1 + this.L.getValue().intValue();
      }

      String var3 = this.P();
      if (var3 != null) {
         this.l = null;
         this.F = var3;
      } else if (var1 < this.G) {
         if (this.l != null) {
            this.k.H(this.l.anchor());
         }
      } else {
         List var4 = this.e();
         if (var4.isEmpty()) {
            this.l = null;
            this.F = this.N.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)";
         } else {
            if (this.l != null) {
               this.l = this.R(this.l, var4);
            }

            if (this.l == null) {
               this.l = this.Z(var4);
            }

            if (this.l == null) {
               this.F = this.Q;
            } else {
               this.k.H(this.l.anchor());
               wp.S(this.l.target());
               this.T(this.l);
            }
         }
      }
   }

   private String P() {
      if (!_p.g() || !mc.isWindowActive()) {
         return "Paused: screen, focus or detached camera";
      } else if (this.a.getValue() && mc.player.isUsingItem()) {
         return "Paused while using an item";
      } else if (mc.player.isSecondaryUseActive()) {
         return "Paused while sneaking";
      } else if (this.I(mc.player.blockPosition())) {
         return "Anchors set spawn here, so they cannot explode";
      } else {
         return this.E.w("Visible") && !ij.a(ij.r.I) ? "Paused for another camera owner" : null;
      }
   }

   private void T(AutoAnchorModule.t var1) {
      BlockState var2 = mc.level.getBlockState(var1.anchor());
      BlockPos var3 = var1.anchor();
      if (!var2.is(Blocks.RESPAWN_ANCHOR)) {
         this.P(true, var3, Items.RESPAWN_ANCHOR, "anchor placement", () -> mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      } else if ((Integer)var2.getValue(RespawnAnchorBlock.CHARGE) == 0) {
         this.P(false, var3, Items.GLOWSTONE, "anchor charge", () -> this.Y(var3) > 0);
      } else if (var1.shield() != null && vi.i(var1.shield())) {
         BlockPos var5 = var1.shield();
         this.P(true, var5, this.I(), "shield placement", () -> !vi.i(var5));
      } else {
         if (mc.player.getOffhandItem().is(Items.GLOWSTONE) && (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) < 4) {
            this.F = "Move offhand glowstone before detonation";
            return;
         }

         Item var4 = this.V();
         if (var4 == null) {
            this.F = "A hotbar item other than glowstone is required to detonate";
            return;
         }

         this.P(false, var3, var4, "detonation", () -> !mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      }
   }

   private void P(boolean var1, BlockPos var2, Item var3, String var4, BooleanSupplier var5) {
      AutoAnchorModule.t var6 = this.l;
      BooleanSupplier var7 = () -> this.l == var6 && this.R == null && this.P() == null;
      Consumer var8 = var5x -> {
         if (!var5x) {
            this.F = "Anchor " + var4 + " rejected; retrying";
         } else {
            this.R = var4;
            this.y = var5;
            this.w = mc.level.getGameTime();
            if (var4.equals("detonation")) {
               _q.z(
                  "AutoAnchor",
                  "Detonating anchor at {}: {} HP on {}, {} HP to you, {}% exposed{}",
                  var2.toShortString(),
                  String.format(Locale.ROOT, "%.1f", var6.damage()),
                  var6.target().getName().getString(),
                  String.format(Locale.ROOT, "%.1f", var6.self()),
                  Math.round(var6.exposure() * 100.0),
                  var6.shield() == null ? "" : " behind a shield at " + var6.shield().toShortString()
               );
            }
         }
      };
      boolean var9 = this.E.w("Visible");
      boolean var10 = var1
         ? vi.a(this, var2, var3, this.x.getValue(), var9, this.v.getValue(), var7, var8)
         : vi.d(this, var2, var3, this.x.getValue(), var9, this.v.getValue(), var7, var8);
      this.F = var10
         ? String.format(
            Locale.ROOT,
            "%s%s; %.1f HP on %s",
            Character.toUpperCase(var4.charAt(0)),
            var4.substring(1),
            this.l.damage(),
            this.l.target().getName().getString()
         )
         : (var9 ? "No reachable face for " + var4 : "Aim at the " + (var1 ? "support" : "anchor") + " for " + var4);
   }

   private boolean I(BlockPos var1) {
      return Boolean.TRUE.equals(mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, var1));
   }

   private int Y(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.is(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   private Item I() {
      return this.D.w("Obsidian") ? Items.OBSIDIAN : Items.GLOWSTONE;
   }

   private Item V() {
      ItemStack var1 = mc.player.getOffhandItem();
      ItemStack var2 = mc.player.getMainHandItem();
      if (!var2.is(Items.GLOWSTONE)) {
         return var2.isEmpty() ? Items.AIR : var2.getItem();
      }

      if (this.v.getValue()) {
         for (int var3 = 0; var3 < 9; var3++) {
            ItemStack var4 = mc.player.getInventory().getItem(var3);
            if (!var4.is(Items.GLOWSTONE) && (var4.isEmpty() ? !var1.isEmpty() : !var1.is(var4.getItem()))) {
               return var4.isEmpty() ? Items.AIR : var4.getItem();
            }
         }
      }

      return null;
   }

   private boolean y(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != mc.level
         || mc.player.distanceTo(var1) > this.t.getValue()
         || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.P.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.N().j().y(MCFriendModule.class);
            return var3 == null || !var3.M(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.N.getValue() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> e() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.y(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private double B() {
      return Math.min(this.x.getValue(), mc.player.blockInteractionRange());
   }

   private boolean G(double var1) {
      return !this.Z.getValue() || com.larpclient.t.x(var1, this.Y.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount());
   }

   private AutoAnchorModule.t h(BlockPos var1, BlockPos var2, List<LivingEntity> var3, double var4, double var6) {
      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var3) {
         double var13 = pl.v(var12, var1, var2);
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var8 == null ? null : new AutoAnchorModule.t(var1, var2, var8, var9, var4, var6);
   }

   private AutoAnchorModule.t R(AutoAnchorModule.t var1, List<LivingEntity> var2) {
      boolean var3 = mc.level.getBlockState(var1.anchor()).is(Blocks.RESPAWN_ANCHOR);
      if ((var3 || vi.i(var1.anchor())) && !this.I(var1.anchor())) {
         if (this.i(var1.anchor(), var3) && this.e(var1.anchor(), var3)) {
            BlockPos var4 = var1.shield() == null || vi.i(var1.shield()) && (!this.O(var1.shield()) || !this.q(var1.anchor(), var1.shield()))
               ? null
               : var1.shield();
            float var5 = pl.s(mc.player, var1.anchor(), var4);
            double var6 = pl.B(mc.player, var1.anchor(), var5);
            if (!this.G(var6)) {
               return null;
            }

            AutoAnchorModule.t var8 = this.h(var1.anchor(), var4, var2, var6, var5);
            return var8 != null && var8.damage() >= this.C.getValue() ? var8 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private AutoAnchorModule.t Z(List<LivingEntity> var1) {
      Vec3 var2 = mc.player.getEyePosition();
      double var3 = this.B();
      int var5 = vi.u(Items.RESPAWN_ANCHOR, this.v.getValue()) != -1 ? 1 : 0;
      int var6 = vi.u(Items.GLOWSTONE, this.v.getValue()) != -1 ? 1 : 0;
      HashSet var7 = new HashSet();
      ArrayList var8 = new ArrayList();
      int var9 = 0;

      record l(BlockPos pos, double bound) {
      }

      for (LivingEntity var11 : var1) {
         BlockPos var12 = var11.blockPosition();

         for (int var13 = -1; var13 <= 2; var13++) {
            for (int var14 = -2; var14 <= 2; var14++) {
               for (int var15 = -2; var15 <= 2; var15++) {
                  BlockPos var16 = var12.offset(var14, var13, var15);
                  if (var7.add(var16)
                     && mc.level.hasChunkAt(var16)
                     && !(var2.distanceToSqr(Vec3.atCenterOf(var16)) > (var3 + 1.0) * (var3 + 1.0))
                     && !this.I(var16)) {
                     boolean var17 = mc.level.getBlockState(var16).is(Blocks.RESPAWN_ANCHOR);
                     if (var17) {
                        var9++;
                     }

                     if (var17 ? this.i(var16, true) : var5 != 0 && var6 != 0 && vi.i(var16)) {
                        double var18 = 0.0;

                        for (LivingEntity var21 : var1) {
                           var18 = Math.max(var18, com.larpclient.t.R(var21, com.larpclient.t.a(var21.position().distanceTo(Vec3.atCenterOf(var16)), 1.0, 5.0)));
                        }

                        if (var18 >= this.C.getValue()) {
                           var8.add(new l(var16, var18));
                        }
                     }
                  }
               }
            }
         }
      }

      if (var8.isEmpty()) {
         this.Q = var5 == 0 && var9 == 0
            ? "No respawn anchors in hotbar or hands"
            : (var6 == 0 && var9 == 0 ? "No glowstone in hotbar or hands" : "No anchor spot in reach can reach Min Damage");
         return null;
      }

      var8.sort(Comparator.comparingDouble(l::bound).reversed());
      AutoAnchorModule.t var28 = null;
      double var29 = Double.MAX_VALUE;
      double var30 = 0.0;

      for (l var32 : var8) {
         if (var28 != null && var32.bound() <= var28.value()) {
            break;
         }

         BlockPos var33 = var32.pos();
         boolean var34 = mc.level.getBlockState(var33).is(Blocks.RESPAWN_ANCHOR);
         AutoAnchorModule.t var19 = this.h(var33, null, var1, 0.0, 0.0);
         if (var19 != null) {
            var30 = Math.max(var30, var19.damage());
            if (!(var19.damage() < this.C.getValue()) && (var28 == null || !(var19.damage() <= var28.value()))) {
               float var35 = pl.s(mc.player, var33, null);
               ArrayList var36 = new ArrayList();
               if (this.T.getValue() && var35 > 0.0F && vi.u(this.I(), this.v.getValue()) != -1) {
                  for (BlockPos var23 : pl.o(var33, mc.player.getEyePosition())) {
                     if (vi.i(var23)) {
                        float var24 = pl.s(mc.player, var33, var23);
                        if (var24 < var35) {
                           var36.add(new AutoAnchorModule.c(var23, var24));
                        }
                     }
                  }

                  var36.sort(Comparator.comparingDouble(AutoAnchorModule.c::exposure));
               }

               var36.add(new AutoAnchorModule.c(null, var35));
               Boolean var37 = null;

               for (AutoAnchorModule.c var39 : var36) {
                  double var25 = pl.B(mc.player, var33, var39.exposure());
                  AutoAnchorModule.t var27 = var39.pos() == null
                     ? new AutoAnchorModule.t(var33, null, var19.target(), var19.damage(), var25, var35)
                     : this.h(var33, var39.pos(), var1, var25, var39.exposure());
                  if (var27 != null) {
                     var29 = Math.min(var29, var27.self());
                     if (this.G(var27.self()) && !(var27.damage() < this.C.getValue()) && (var28 == null || !(var27.value() <= var28.value()))) {
                        if (var37 == null) {
                           var37 = this.e(var33, var34);
                        }

                        if (!var37) {
                           break;
                        }

                        if (var27.shield() == null || this.O(var27.shield()) && this.q(var33, var27.shield())) {
                           var28 = var27;
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

      if (var28 == null) {
         this.Q = var29 != Double.MAX_VALUE && !this.G(var29)
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var29, this.Y.getValue())
            : (
               var30 < this.C.getValue()
                  ? String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var30, this.C.getValue())
                  : (this.E.w("Visible") ? "No anchor spot with a reachable face or support" : "Aim at an anchor or at a support beside an anchor spot")
            );
      } else {
         _q.c(
            "AutoAnchor",
            "Planned anchor at {}{}: {} HP on {}, {} HP to you",
            var28.anchor().toShortString(),
            var28.shield() == null ? "" : " with shield at " + var28.shield().toShortString(),
            String.format(Locale.ROOT, "%.1f", var28.damage()),
            var28.target().getName().getString(),
            String.format(Locale.ROOT, "%.1f", var28.self())
         );
      }

      return var28;
   }

   private boolean i(BlockPos var1, boolean var2) {
      int var3 = vi.u(Items.GLOWSTONE, this.v.getValue()) != -1 ? 1 : 0;
      return !var2 ? var3 != 0 && vi.u(Items.RESPAWN_ANCHOR, this.v.getValue()) != -1 : this.Y(var1) > 0 || var3 != 0;
   }

   private boolean e(BlockPos var1, boolean var2) {
      boolean var3 = this.E.w("Visible");
      if (!var2) {
         return vi.X(var1, Items.RESPAWN_ANCHOR, this.x.getValue(), var3, this.v.getValue()) != null;
      }

      if (!var3) {
         return _p.A(var1, this.B()) != null;
      }

      Vec3 var4 = mc.player.getEyePosition();

      for (Vec3 var6 : AutoCrystalModule.Y(var1, var4)) {
         if (_p.X(var1, this.B(), var6.subtract(var4)) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean O(BlockPos var1) {
      return vi.X(var1, this.I(), this.x.getValue(), this.E.w("Visible"), this.v.getValue()) != null;
   }

   private boolean q(BlockPos var1, BlockPos var2) {
      Vec3 var3 = mc.player.getEyePosition();
      AABB var4 = new AABB(var2);
      double var5 = this.B();

      for (Vec3 var8 : AutoCrystalModule.Y(var1, var3)) {
         if (!(var3.distanceToSqr(var8) > var5 * var5) && !var4.clip(var3, var8).isPresent()) {
            BlockHitResult var9 = mc.level.clip(new ClipContext(var3, var8, Block.OUTLINE, Fluid.NONE, mc.player));
            if (var9.getType() == Type.MISS || var9.getBlockPos().equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   private void k(String var1) {
      vi.J(this);
      this.l = null;
      this.R = null;
      this.y = null;
      this.w = this.G = 0L;
      this.F = var1;
   }

   private record c(BlockPos pos, float exposure) {
   }

   private record t(BlockPos anchor, BlockPos shield, LivingEntity target, double damage, double self, double exposure) {
      double value() {
         return this.damage - this.self * 0.25;
      }
   }
}
