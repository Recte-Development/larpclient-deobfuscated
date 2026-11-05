package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.ep;
import com.larpclient.f8;
import com.larpclient.hy;
import com.larpclient.m;
import com.larpclient.od;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.uj;
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

public final class AutoAnchorModule extends Module implements hy.c, m.s {
   private static final int r = 12;
   private static final int e = 2;
   private static AutoAnchorModule M;
   private final lc V = this.M("Range", "Anchor reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final lc f = this.M("Delay", "Ticks between confirmed anchor steps", 0.0, 0.0, 20.0, 1.0);
   private final ln Z = this.H("Auto Switch", "Use hotbar items and restore the selected slot after each step", true);
   private final ln F = this.H("Safe Anchor", "Place a block between you and the anchor before detonating, where it leaves you least exposed", true);
   private final lp N = this.addSetting(
      new lp("Shield Block", "Block placed as the shield; the blast hurts players before it breaks blocks", "Glowstone", "Glowstone", "Obsidian")
   );
   private final ln t = this.H("Players", "Target other survival players", true);
   private final ln Q = this.H("Mobs", "Also target hostile mobs for combat or testing", false);
   private final lc L = this.M("Target Range", "Search distance for opponents", 8.0, 2.0, 14.0, 0.5);
   private final lc n = this.M("Min Damage", "Minimum estimated target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final ln R = this.H("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final lc a = this.M("Max Self Damage", "Maximum estimated self damage in HP", 8.0, 0.0, 36.0, 0.5);
   private final ln O = this.H("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final lp H = this.addSetting(
      new lp("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final hy l;
   private AutoAnchorModule.k u;
   private String C;
   private String Y = "";
   private String U = "";
   private String o = "Waiting for a target";
   private BooleanSupplier h;
   private long c;
   private long J;
   private Object G;
   private Object b;
   private Object p;
   private Object k;
   private int q;

   public AutoAnchorModule() {
      super("AutoAnchor", "Automatically place, charge and detonate respawn anchors", Category.COMBAT, -1);
      M = this;
      this.N.x(this.F::getValue);
      this.a.x(this.R::getValue);
      this.H.W(var1 -> {
         this.x("Rotation changed");
         this.r();
      });
      this.l = new hy(this, var1 -> this.addSetting(var1), 0.5, 0.3);
   }

   private lc M(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   private ln H(String var1, String var2, boolean var3) {
      return this.addSetting(new ln(var1, var2, var3));
   }

   public static AutoAnchorModule y() {
      return M;
   }

   public List<l6<?>> I() {
      return List.of(this.V, this.f, this.Z, this.F, this.N);
   }

   public List<l6<?>> t() {
      return List.of(this.t, this.Q, this.L);
   }

   public List<l6<?>> Z() {
      return List.of(this.n, this.R, this.a, this.O);
   }

   public List<l6<?>> e() {
      return List.of(this.H);
   }

   @Override
   public hy placementHighlight() {
      return this.l;
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.o : "";
   }

   @Override
   public void onEnable() {
      this.G = this.b = null;
      this.x("Waiting for a target");
      this.l.S();
      r4.V(
         "AutoAnchor",
         "Enabled; rotation={}, range={}, safeAnchor={}, minDamage={}, maxSelf={}",
         this.H.getValue(),
         this.V.getValue(),
         this.F.getValue(),
         this.n.getValue(),
         this.R.getValue() ? this.a.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.x("Waiting for a target");
      this.r();
      this.l.C();
      this.G = this.b = null;
      r4.V("AutoAnchor", "Disabled; anchor intents and server waits cleared");
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (rr.j(rr.f.q)) {
            ep.C(ep.z.d, var1, var2, 1.0);
            if (ep.W(ep.z.d)) {
               this.p = mc.player;
               this.k = mc.level;
               this.q = mc.player.tickCount;
               rr.W(rr.f.q);
            } else {
               this.r();
            }
         }
      }
   }

   private void r() {
      this.p = this.k = null;
      rr.N(rr.f.q);
   }

   public boolean J() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.H.i("Visible")
            && mc.player != null
            && this.p == mc.player
            && this.k == mc.level
            && mc.player.tickCount >= this.q
            && mc.player.tickCount - this.q <= 1
            && (!this.O.getValue() || !mc.player.isUsingItem());
   }

   public float Y(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && rr.u() == rr.f.q && ep.W(ep.z.d)) {
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
      this.l.i(null);
      m.g(this);
      if (!this.isInGame()) {
         this.x("Waiting for a world");
      } else {
         if (this.G != mc.player || this.b != mc.level) {
            this.x("Waiting for a target");
            this.G = mc.player;
            this.b = mc.level;
         }

         try {
            this.M();
         } finally {
            int var10000 = this.o.equals(this.U);
            boolean var5 = false;
            byte var4 = (byte)var10000;
            var10000 = Integer.signum(var4);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.U = this.o;
               r4.g("AutoAnchor", "Status: {}", this.o);
            }
         }
      }
   }

   private void M() {
      long var1 = mc.level.getGameTime();
      if (this.C != null) {
         if (!this.h.getAsBoolean()) {
            if (var1 - this.c > 12L) {
               r4.V("AutoAnchor", "No server update after {} ticks for: {}; replanning", 12, this.C);
               this.x("Server did not confirm the last step");
               return;
            }

            if (this.u != null) {
               this.l.i(this.u.anchor());
            }

            this.o = "Waiting for " + this.C;
            return;
         }

         r4.g("AutoAnchor", "Server confirmed: {}", this.C);
         if (this.C.equals("detonation")) {
            this.u = null;
         }

         this.C = null;
         this.J = var1 + this.f.getValue().intValue();
      }

      String var3 = this.b();
      if (var3 != null) {
         this.u = null;
         this.o = var3;
      } else if (var1 < this.J) {
         if (this.u != null) {
            this.l.i(this.u.anchor());
         }
      } else {
         List var4 = this.n();
         if (var4.isEmpty()) {
            this.u = null;
            this.o = this.Q.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)";
         } else {
            if (this.u != null) {
               this.u = this.q(this.u, var4);
            }

            if (this.u == null) {
               this.u = this.g(var4);
            }

            if (this.u == null) {
               this.o = this.Y;
            } else {
               this.l.i(this.u.anchor());
               f8.s(this.u.target());
               this.I(this.u);
            }
         }
      }
   }

   private String b() {
      if (!d8.a() || !mc.isWindowActive()) {
         return "Paused: screen, focus or detached camera";
      } else if (this.O.getValue() && mc.player.isUsingItem()) {
         return "Paused while using an item";
      } else if (mc.player.isSecondaryUseActive()) {
         return "Paused while sneaking";
      } else if (this.k(mc.player.blockPosition())) {
         return "Anchors set spawn here, so they cannot explode";
      } else {
         return this.H.i("Visible") && !rr.j(rr.f.q) ? "Paused for another camera owner" : null;
      }
   }

   private void I(AutoAnchorModule.k var1) {
      BlockState var2 = mc.level.getBlockState(var1.anchor());
      BlockPos var3 = var1.anchor();
      if (!var2.is(Blocks.RESPAWN_ANCHOR)) {
         this.Q(true, var3, Items.RESPAWN_ANCHOR, "anchor placement", () -> mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      } else if ((Integer)var2.getValue(RespawnAnchorBlock.CHARGE) == 0) {
         this.Q(false, var3, Items.GLOWSTONE, "anchor charge", () -> this.n(var3) > 0);
      } else if (var1.shield() != null && m.Q(var1.shield())) {
         BlockPos var5 = var1.shield();
         this.Q(true, var5, this.K(), "shield placement", () -> !m.Q(var5));
      } else {
         if (mc.player.getOffhandItem().is(Items.GLOWSTONE) && (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) < 4) {
            this.o = "Move offhand glowstone before detonation";
            return;
         }

         Item var4 = this.V();
         if (var4 == null) {
            this.o = "A hotbar item other than glowstone is required to detonate";
            return;
         }

         this.Q(false, var3, var4, "detonation", () -> !mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      }
   }

   private void Q(boolean var1, BlockPos var2, Item var3, String var4, BooleanSupplier var5) {
      AutoAnchorModule.k var6 = this.u;
      BooleanSupplier var7 = () -> this.u == var6 && this.C == null && this.b() == null;
      Consumer var8 = var5x -> {
         if (!var5x) {
            this.o = "Anchor " + var4 + " rejected; retrying";
         } else {
            this.C = var4;
            this.h = var5;
            this.c = mc.level.getGameTime();
            if (var4.equals("detonation")) {
               r4.V(
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
      boolean var9 = this.H.i("Visible");
      boolean var10 = var1
         ? m.U(this, var2, var3, this.V.getValue(), var9, this.Z.getValue(), var7, var8)
         : m.e(this, var2, var3, this.V.getValue(), var9, this.Z.getValue(), var7, var8);
      this.o = var10
         ? String.format(
            Locale.ROOT,
            "%s%s; %.1f HP on %s",
            Character.toUpperCase(var4.charAt(0)),
            var4.substring(1),
            this.u.damage(),
            this.u.target().getName().getString()
         )
         : (var9 ? "No reachable face for " + var4 : "Aim at the " + (var1 ? "support" : "anchor") + " for " + var4);
   }

   private boolean k(BlockPos var1) {
      return Boolean.TRUE.equals(mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, var1));
   }

   private int n(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.is(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   private Item K() {
      return this.N.i("Obsidian") ? Items.OBSIDIAN : Items.GLOWSTONE;
   }

   private Item V() {
      ItemStack var1 = mc.player.getOffhandItem();
      ItemStack var2 = mc.player.getMainHandItem();
      if (!var2.is(Items.GLOWSTONE)) {
         return var2.isEmpty() ? Items.AIR : var2.getItem();
      }

      if (this.Z.getValue()) {
         for (int var3 = 0; var3 < 9; var3++) {
            ItemStack var4 = mc.player.getInventory().getItem(var3);
            if (!var4.is(Items.GLOWSTONE) && (var4.isEmpty() ? !var1.isEmpty() : !var1.is(var4.getItem()))) {
               return var4.isEmpty() ? Items.AIR : var4.getItem();
            }
         }
      }

      return null;
   }

   private boolean R(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != mc.level
         || mc.player.distanceTo(var1) > this.L.getValue()
         || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.t.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.y().e().y(MCFriendModule.class);
            return var3 == null || !var3.e(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.Q.getValue() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> n() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.R(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private double i() {
      return Math.min(this.V.getValue(), mc.player.blockInteractionRange());
   }

   private boolean G(double var1) {
      return !this.R.getValue() || od.p(var1, this.a.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount());
   }

   private AutoAnchorModule.k B(BlockPos var1, BlockPos var2, List<LivingEntity> var3, double var4, double var6) {
      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var3) {
         double var13 = uj.D(var12, var1, var2);
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var8 == null ? null : new AutoAnchorModule.k(var1, var2, var8, var9, var4, var6);
   }

   private AutoAnchorModule.k q(AutoAnchorModule.k var1, List<LivingEntity> var2) {
      boolean var3 = mc.level.getBlockState(var1.anchor()).is(Blocks.RESPAWN_ANCHOR);
      if ((var3 || m.Q(var1.anchor())) && !this.k(var1.anchor())) {
         if (this.x(var1.anchor(), var3) && this.H(var1.anchor(), var3)) {
            BlockPos var4 = var1.shield() == null || m.Q(var1.shield()) && (!this.a(var1.shield()) || !this.x(var1.anchor(), var1.shield()))
               ? null
               : var1.shield();
            float var5 = uj.P(mc.player, var1.anchor(), var4);
            double var6 = uj.z(mc.player, var1.anchor(), var5);
            if (!this.G(var6)) {
               return null;
            }

            AutoAnchorModule.k var8 = this.B(var1.anchor(), var4, var2, var6, var5);
            return var8 != null && var8.damage() >= this.n.getValue() ? var8 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private AutoAnchorModule.k g(List<LivingEntity> var1) {
      Vec3 var2 = mc.player.getEyePosition();
      double var3 = this.i();
      int var5 = m.Q(Items.RESPAWN_ANCHOR, this.Z.getValue()) != -1 ? 1 : 0;
      int var6 = m.Q(Items.GLOWSTONE, this.Z.getValue()) != -1 ? 1 : 0;
      HashSet var7 = new HashSet();
      ArrayList var8 = new ArrayList();
      int var9 = 0;

      record s(BlockPos pos, double bound) {
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
                     && !this.k(var16)) {
                     boolean var17 = mc.level.getBlockState(var16).is(Blocks.RESPAWN_ANCHOR);
                     if (var17) {
                        var9++;
                     }

                     if (var17 ? this.x(var16, true) : var5 != 0 && var6 != 0 && m.Q(var16)) {
                        double var18 = 0.0;

                        for (LivingEntity var21 : var1) {
                           var18 = Math.max(var18, od.o(var21, od.O(var21.position().distanceTo(Vec3.atCenterOf(var16)), 1.0, 5.0)));
                        }

                        if (var18 >= this.n.getValue()) {
                           var8.add(new s(var16, var18));
                        }
                     }
                  }
               }
            }
         }
      }

      if (var8.isEmpty()) {
         this.Y = var5 == 0 && var9 == 0
            ? "No respawn anchors in hotbar or hands"
            : (var6 == 0 && var9 == 0 ? "No glowstone in hotbar or hands" : "No anchor spot in reach can reach Min Damage");
         return null;
      }

      var8.sort(Comparator.comparingDouble(s::bound).reversed());
      AutoAnchorModule.k var28 = null;
      double var29 = Double.MAX_VALUE;
      double var30 = 0.0;

      for (s var32 : var8) {
         if (var28 != null && var32.bound() <= var28.value()) {
            break;
         }

         BlockPos var33 = var32.pos();
         boolean var34 = mc.level.getBlockState(var33).is(Blocks.RESPAWN_ANCHOR);
         AutoAnchorModule.k var19 = this.B(var33, null, var1, 0.0, 0.0);
         if (var19 != null) {
            var30 = Math.max(var30, var19.damage());
            if (!(var19.damage() < this.n.getValue()) && (var28 == null || !(var19.damage() <= var28.value()))) {
               float var35 = uj.P(mc.player, var33, null);
               ArrayList var36 = new ArrayList();
               if (this.F.getValue() && var35 > 0.0F && m.Q(this.K(), this.Z.getValue()) != -1) {
                  for (BlockPos var23 : uj.X(var33, mc.player.getEyePosition())) {
                     if (m.Q(var23)) {
                        float var24 = uj.P(mc.player, var33, var23);
                        if (var24 < var35) {
                           var36.add(new AutoAnchorModule.t(var23, var24));
                        }
                     }
                  }

                  var36.sort(Comparator.comparingDouble(AutoAnchorModule.t::exposure));
               }

               var36.add(new AutoAnchorModule.t(null, var35));
               Boolean var37 = null;

               for (AutoAnchorModule.t var39 : var36) {
                  double var25 = uj.z(mc.player, var33, var39.exposure());
                  AutoAnchorModule.k var27 = var39.pos() == null
                     ? new AutoAnchorModule.k(var33, null, var19.target(), var19.damage(), var25, var35)
                     : this.B(var33, var39.pos(), var1, var25, var39.exposure());
                  if (var27 != null) {
                     var29 = Math.min(var29, var27.self());
                     if (this.G(var27.self()) && !(var27.damage() < this.n.getValue()) && (var28 == null || !(var27.value() <= var28.value()))) {
                        if (var37 == null) {
                           var37 = this.H(var33, var34);
                        }

                        if (!var37) {
                           break;
                        }

                        if (var27.shield() == null || this.a(var27.shield()) && this.x(var33, var27.shield())) {
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
         this.Y = var29 != Double.MAX_VALUE && !this.G(var29)
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var29, this.a.getValue())
            : (
               var30 < this.n.getValue()
                  ? String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var30, this.n.getValue())
                  : (this.H.i("Visible") ? "No anchor spot with a reachable face or support" : "Aim at an anchor or at a support beside an anchor spot")
            );
      } else {
         r4.g(
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

   private boolean x(BlockPos var1, boolean var2) {
      int var3 = m.Q(Items.GLOWSTONE, this.Z.getValue()) != -1 ? 1 : 0;
      return !var2 ? var3 != 0 && m.Q(Items.RESPAWN_ANCHOR, this.Z.getValue()) != -1 : this.n(var1) > 0 || var3 != 0;
   }

   private boolean H(BlockPos var1, boolean var2) {
      boolean var3 = this.H.i("Visible");
      if (!var2) {
         return m.J(var1, Items.RESPAWN_ANCHOR, this.V.getValue(), var3, this.Z.getValue()) != null;
      }

      if (!var3) {
         return d8.F(var1, this.i()) != null;
      }

      Vec3 var4 = mc.player.getEyePosition();

      for (Vec3 var6 : AutoCrystalModule.d(var1, var4)) {
         if (d8.e(var1, this.i(), var6.subtract(var4)) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean a(BlockPos var1) {
      return m.J(var1, this.K(), this.V.getValue(), this.H.i("Visible"), this.Z.getValue()) != null;
   }

   private boolean x(BlockPos var1, BlockPos var2) {
      Vec3 var3 = mc.player.getEyePosition();
      AABB var4 = new AABB(var2);
      double var5 = this.i();

      for (Vec3 var8 : AutoCrystalModule.d(var1, var3)) {
         if (!(var3.distanceToSqr(var8) > var5 * var5) && !var4.clip(var3, var8).isPresent()) {
            BlockHitResult var9 = mc.level.clip(new ClipContext(var3, var8, Block.OUTLINE, Fluid.NONE, mc.player));
            if (var9.getType() == Type.MISS || var9.getBlockPos().equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   private void x(String var1) {
      m.g(this);
      this.u = null;
      this.C = null;
      this.h = null;
      this.c = this.J = 0L;
      this.o = var1;
   }

   private record k(BlockPos anchor, BlockPos shield, LivingEntity target, double damage, double self, double exposure) {
      double value() {
         return this.damage - this.self * 0.25;
      }
   }

   private record t(BlockPos pos, float exposure) {
   }
}
