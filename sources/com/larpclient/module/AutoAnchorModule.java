package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.h;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.pp;
import com.larpclient.tq;
import com.larpclient.tu;
import com.larpclient.wr;
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

public final class AutoAnchorModule extends Module implements pp.x, _d.s {
   private static final int B = 12;
   private static final int v = 2;
   private static AutoAnchorModule R;
   private final d9 p = this.V("Range", "Anchor reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final d9 z = this.V("Delay", "Ticks between confirmed anchor steps", 0.0, 0.0, 20.0, 1.0);
   private final dh c = this.W("Auto Switch", "Use hotbar items and restore the selected slot after each step", true);
   private final dh U = this.W("Safe Anchor", "Place a block between you and the anchor before detonating, where it leaves you least exposed", true);
   private final d6 S = this.addSetting(
      new d6("Shield Block", "Block placed as the shield; the blast hurts players before it breaks blocks", "Glowstone", "Glowstone", "Obsidian")
   );
   private final dh N = this.W("Players", "Target other survival players", true);
   private final dh H = this.W("Mobs", "Also target hostile mobs for combat or testing", false);
   private final d9 y = this.V("Target Range", "Search distance for opponents", 8.0, 2.0, 14.0, 0.5);
   private final d9 i = this.V("Min Damage", "Minimum estimated target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final dh J = this.W("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final d9 t = this.V("Max Self Damage", "Maximum estimated self damage in HP", 8.0, 0.0, 36.0, 0.5);
   private final dh u = this.W("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final d6 h = this.addSetting(
      new d6("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final pp l;
   private AutoAnchorModule.r F;
   private String o;
   private String I = "";
   private String P = "";
   private String A = "Waiting for a target";
   private BooleanSupplier E;
   private long K;
   private long D;
   private Object Q;
   private Object x;
   private Object C;
   private Object T;
   private int f;

   public AutoAnchorModule() {
      super("AutoAnchor", "Automatically place, charge and detonate respawn anchors", Category.COMBAT, -1);
      R = this;
      this.S.H(this.U::P);
      this.t.H(this.J::P);
      this.h.h(var1 -> {
         this.d("Rotation changed");
         this.x();
      });
      this.l = new pp(this, var1 -> this.addSetting(var1), 0.5, 0.3);
   }

   private d9 V(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   private dh W(String var1, String var2, boolean var3) {
      return this.addSetting(new dh(var1, var2, var3));
   }

   public static AutoAnchorModule S() {
      return R;
   }

   public List<df<?>> H() {
      return List.of(this.p, this.z, this.c, this.U, this.S);
   }

   public List<df<?>> j() {
      return List.of(this.N, this.H, this.y);
   }

   public List<df<?>> E() {
      return List.of(this.i, this.J, this.t, this.u);
   }

   public List<df<?>> k() {
      return List.of(this.h);
   }

   @Override
   public pp placementHighlight() {
      return this.l;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.A : "";
   }

   @Override
   public void onEnable() {
      this.Q = this.x = null;
      this.d("Waiting for a target");
      this.l.w();
      mt.K(
         "AutoAnchor",
         "Enabled; rotation={}, range={}, safeAnchor={}, minDamage={}, maxSelf={}",
         this.h.P(),
         this.p.P(),
         this.U.P(),
         this.i.P(),
         this.J.P() ? this.t.P() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.d("Waiting for a target");
      this.x();
      this.l.P();
      this.Q = this.x = null;
      mt.K("AutoAnchor", "Disabled; anchor intents and server waits cleared");
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (tq.y(tq.y.g)) {
            m9.c(m9.s.p, var1, var2, 1.0);
            if (m9.F(m9.s.p)) {
               this.C = mc.player;
               this.T = mc.level;
               this.f = mc.player.tickCount;
               tq.i(tq.y.g);
            } else {
               this.x();
            }
         }
      }
   }

   private void x() {
      this.C = this.T = null;
      tq.A(tq.y.g);
   }

   public boolean B() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.h.S("Visible")
            && mc.player != null
            && this.C == mc.player
            && this.T == mc.level
            && mc.player.tickCount >= this.f
            && mc.player.tickCount - this.f <= 1
            && (!this.u.P() || !mc.player.isUsingItem());
   }

   public float X(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && tq.l() == tq.y.g && m9.F(m9.s.p)) {
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
      this.l.m(null);
      _d.N(this);
      if (!this.isInGame()) {
         this.d("Waiting for a world");
      } else {
         if (this.Q != mc.player || this.x != mc.level) {
            this.d("Waiting for a target");
            this.Q = mc.player;
            this.x = mc.level;
         }

         try {
            this.d();
         } finally {
            int var10000 = this.A.equals(this.P);
            boolean var5 = false;
            byte var4 = (byte)var10000;
            var10000 = Integer.signum(var4);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.P = this.A;
               mt.U("AutoAnchor", "Status: {}", this.A);
            }
         }
      }
   }

   private void d() {
      long var1 = mc.level.getGameTime();
      if (this.o != null) {
         if (!this.E.getAsBoolean()) {
            if (var1 - this.K > 12L) {
               mt.K("AutoAnchor", "No server update after {} ticks for: {}; replanning", 12, this.o);
               this.d("Server did not confirm the last step");
               return;
            }

            if (this.F != null) {
               this.l.m(this.F.anchor());
            }

            this.A = "Waiting for " + this.o;
            return;
         }

         mt.U("AutoAnchor", "Server confirmed: {}", this.o);
         if (this.o.equals("detonation")) {
            this.F = null;
         }

         this.o = null;
         this.D = var1 + this.z.P().intValue();
      }

      String var3 = this.s();
      if (var3 != null) {
         this.F = null;
         this.A = var3;
      } else if (var1 < this.D) {
         if (this.F != null) {
            this.l.m(this.F.anchor());
         }
      } else {
         List var4 = this.X();
         if (var4.isEmpty()) {
            this.F = null;
            this.A = this.H.P() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)";
         } else {
            if (this.F != null) {
               this.F = this.O(this.F, var4);
            }

            if (this.F == null) {
               this.F = this.o(var4);
            }

            if (this.F == null) {
               this.A = this.I;
            } else {
               this.l.m(this.F.anchor());
               com.larpclient.h.z(this.F.target());
               this.n(this.F);
            }
         }
      }
   }

   private String s() {
      if (!_m.z() || !mc.isWindowActive()) {
         return "Paused: screen, focus or detached camera";
      } else if (this.u.P() && mc.player.isUsingItem()) {
         return "Paused while using an item";
      } else if (mc.player.isSecondaryUseActive()) {
         return "Paused while sneaking";
      } else if (this.t(mc.player.blockPosition())) {
         return "Anchors set spawn here, so they cannot explode";
      } else {
         return this.h.S("Visible") && !tq.y(tq.y.g) ? "Paused for another camera owner" : null;
      }
   }

   private void n(AutoAnchorModule.r var1) {
      BlockState var2 = mc.level.getBlockState(var1.anchor());
      BlockPos var3 = var1.anchor();
      if (!var2.is(Blocks.RESPAWN_ANCHOR)) {
         this.r(true, var3, Items.RESPAWN_ANCHOR, "anchor placement", () -> mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      } else if ((Integer)var2.getValue(RespawnAnchorBlock.CHARGE) == 0) {
         this.r(false, var3, Items.GLOWSTONE, "anchor charge", () -> this.i(var3) > 0);
      } else if (var1.shield() != null && _d.Z(var1.shield())) {
         BlockPos var5 = var1.shield();
         this.r(true, var5, this.g(), "shield placement", () -> !_d.Z(var5));
      } else {
         if (mc.player.getOffhandItem().is(Items.GLOWSTONE) && (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) < 4) {
            this.A = "Move offhand glowstone before detonation";
            return;
         }

         Item var4 = this.c();
         if (var4 == null) {
            this.A = "A hotbar item other than glowstone is required to detonate";
            return;
         }

         this.r(false, var3, var4, "detonation", () -> !mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      }
   }

   private void r(boolean var1, BlockPos var2, Item var3, String var4, BooleanSupplier var5) {
      AutoAnchorModule.r var6 = this.F;
      BooleanSupplier var7 = () -> this.F == var6 && this.o == null && this.s() == null;
      Consumer var8 = var5x -> {
         if (!var5x) {
            this.A = "Anchor " + var4 + " rejected; retrying";
         } else {
            this.o = var4;
            this.E = var5;
            this.K = mc.level.getGameTime();
            if (var4.equals("detonation")) {
               mt.K(
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
      boolean var9 = this.h.S("Visible");
      boolean var10 = var1
         ? _d.a(this, var2, var3, this.p.P(), var9, this.c.P(), var7, var8)
         : _d.U(this, var2, var3, this.p.P(), var9, this.c.P(), var7, var8);
      this.A = var10
         ? String.format(
            Locale.ROOT,
            "%s%s; %.1f HP on %s",
            Character.toUpperCase(var4.charAt(0)),
            var4.substring(1),
            this.F.damage(),
            this.F.target().getName().getString()
         )
         : (var9 ? "No reachable face for " + var4 : "Aim at the " + (var1 ? "support" : "anchor") + " for " + var4);
   }

   private boolean t(BlockPos var1) {
      return Boolean.TRUE.equals(mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, var1));
   }

   private int i(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.is(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   private Item g() {
      return this.S.S("Obsidian") ? Items.OBSIDIAN : Items.GLOWSTONE;
   }

   private Item c() {
      ItemStack var1 = mc.player.getOffhandItem();
      ItemStack var2 = mc.player.getMainHandItem();
      if (!var2.is(Items.GLOWSTONE)) {
         return var2.isEmpty() ? Items.AIR : var2.getItem();
      }

      if (this.c.P()) {
         for (int var3 = 0; var3 < 9; var3++) {
            ItemStack var4 = mc.player.getInventory().getItem(var3);
            if (!var4.is(Items.GLOWSTONE) && (var4.isEmpty() ? !var1.isEmpty() : !var1.is(var4.getItem()))) {
               return var4.isEmpty() ? Items.AIR : var4.getItem();
            }
         }
      }

      return null;
   }

   private boolean O(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != mc.level
         || mc.player.distanceTo(var1) > this.y.P()
         || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.N.P() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.F().x().V(MCFriendModule.class);
            return var3 == null || !var3.V(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.H.P() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> X() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.O(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private double o() {
      return Math.min(this.p.P(), mc.player.blockInteractionRange());
   }

   private boolean i(double var1) {
      return !this.J.P() || tu.b(var1, this.t.P(), mc.player.getHealth() + mc.player.getAbsorptionAmount());
   }

   private AutoAnchorModule.r a(BlockPos var1, BlockPos var2, List<LivingEntity> var3, double var4, double var6) {
      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var3) {
         double var13 = wr.x(var12, var1, var2);
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var8 == null ? null : new AutoAnchorModule.r(var1, var2, var8, var9, var4, var6);
   }

   private AutoAnchorModule.r O(AutoAnchorModule.r var1, List<LivingEntity> var2) {
      boolean var3 = mc.level.getBlockState(var1.anchor()).is(Blocks.RESPAWN_ANCHOR);
      if ((var3 || _d.Z(var1.anchor())) && !this.t(var1.anchor())) {
         if (this.g(var1.anchor(), var3) && this.r(var1.anchor(), var3)) {
            BlockPos var4 = var1.shield() == null || _d.Z(var1.shield()) && (!this.n(var1.shield()) || !this.v(var1.anchor(), var1.shield()))
               ? null
               : var1.shield();
            float var5 = wr.I(mc.player, var1.anchor(), var4);
            double var6 = wr.F(mc.player, var1.anchor(), var5);
            if (!this.i(var6)) {
               return null;
            }

            AutoAnchorModule.r var8 = this.a(var1.anchor(), var4, var2, var6, var5);
            return var8 != null && var8.damage() >= this.i.P() ? var8 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private AutoAnchorModule.r o(List<LivingEntity> var1) {
      Vec3 var2 = mc.player.getEyePosition();
      double var3 = this.o();
      int var5 = _d.B(Items.RESPAWN_ANCHOR, this.c.P()) != -1 ? 1 : 0;
      int var6 = _d.B(Items.GLOWSTONE, this.c.P()) != -1 ? 1 : 0;
      HashSet var7 = new HashSet();
      ArrayList var8 = new ArrayList();
      int var9 = 0;

      record e(BlockPos pos, double bound) {
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
                     && !this.t(var16)) {
                     boolean var17 = mc.level.getBlockState(var16).is(Blocks.RESPAWN_ANCHOR);
                     if (var17) {
                        var9++;
                     }

                     if (var17 ? this.g(var16, true) : var5 != 0 && var6 != 0 && _d.Z(var16)) {
                        double var18 = 0.0;

                        for (LivingEntity var21 : var1) {
                           var18 = Math.max(var18, tu.I(var21, tu.y(var21.position().distanceTo(Vec3.atCenterOf(var16)), 1.0, 5.0)));
                        }

                        if (var18 >= this.i.P()) {
                           var8.add(new e(var16, var18));
                        }
                     }
                  }
               }
            }
         }
      }

      if (var8.isEmpty()) {
         this.I = var5 == 0 && var9 == 0
            ? "No respawn anchors in hotbar or hands"
            : (var6 == 0 && var9 == 0 ? "No glowstone in hotbar or hands" : "No anchor spot in reach can reach Min Damage");
         return null;
      }

      var8.sort(Comparator.comparingDouble(e::bound).reversed());
      AutoAnchorModule.r var28 = null;
      double var29 = Double.MAX_VALUE;
      double var30 = 0.0;

      for (e var32 : var8) {
         if (var28 != null && var32.bound() <= var28.value()) {
            break;
         }

         BlockPos var33 = var32.pos();
         boolean var34 = mc.level.getBlockState(var33).is(Blocks.RESPAWN_ANCHOR);
         AutoAnchorModule.r var19 = this.a(var33, null, var1, 0.0, 0.0);
         if (var19 != null) {
            var30 = Math.max(var30, var19.damage());
            if (!(var19.damage() < this.i.P()) && (var28 == null || !(var19.damage() <= var28.value()))) {
               float var35 = wr.I(mc.player, var33, null);
               ArrayList var36 = new ArrayList();
               if (this.U.P() && var35 > 0.0F && _d.B(this.g(), this.c.P()) != -1) {
                  for (BlockPos var23 : wr.G(var33, mc.player.getEyePosition())) {
                     if (_d.Z(var23)) {
                        float var24 = wr.I(mc.player, var33, var23);
                        if (var24 < var35) {
                           var36.add(new AutoAnchorModule.u(var23, var24));
                        }
                     }
                  }

                  var36.sort(Comparator.comparingDouble(AutoAnchorModule.u::exposure));
               }

               var36.add(new AutoAnchorModule.u(null, var35));
               Boolean var37 = null;

               for (AutoAnchorModule.u var39 : var36) {
                  double var25 = wr.F(mc.player, var33, var39.exposure());
                  AutoAnchorModule.r var27 = var39.pos() == null
                     ? new AutoAnchorModule.r(var33, null, var19.target(), var19.damage(), var25, var35)
                     : this.a(var33, var39.pos(), var1, var25, var39.exposure());
                  if (var27 != null) {
                     var29 = Math.min(var29, var27.self());
                     if (this.i(var27.self()) && !(var27.damage() < this.i.P()) && (var28 == null || !(var27.value() <= var28.value()))) {
                        if (var37 == null) {
                           var37 = this.r(var33, var34);
                        }

                        if (!var37) {
                           break;
                        }

                        if (var27.shield() == null || this.n(var27.shield()) && this.v(var33, var27.shield())) {
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
         this.I = var29 != Double.MAX_VALUE && !this.i(var29)
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var29, this.t.P())
            : (
               var30 < this.i.P()
                  ? String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var30, this.i.P())
                  : (this.h.S("Visible") ? "No anchor spot with a reachable face or support" : "Aim at an anchor or at a support beside an anchor spot")
            );
      } else {
         mt.U(
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

   private boolean g(BlockPos var1, boolean var2) {
      int var3 = _d.B(Items.GLOWSTONE, this.c.P()) != -1 ? 1 : 0;
      return !var2 ? var3 != 0 && _d.B(Items.RESPAWN_ANCHOR, this.c.P()) != -1 : this.i(var1) > 0 || var3 != 0;
   }

   private boolean r(BlockPos var1, boolean var2) {
      boolean var3 = this.h.S("Visible");
      if (!var2) {
         return _d.g(var1, Items.RESPAWN_ANCHOR, this.p.P(), var3, this.c.P()) != null;
      }

      if (!var3) {
         return _m.F(var1, this.o()) != null;
      }

      Vec3 var4 = mc.player.getEyePosition();

      for (Vec3 var6 : AutoCrystalModule.B(var1, var4)) {
         if (_m.y(var1, this.o(), var6.subtract(var4)) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean n(BlockPos var1) {
      return _d.g(var1, this.g(), this.p.P(), this.h.S("Visible"), this.c.P()) != null;
   }

   private boolean v(BlockPos var1, BlockPos var2) {
      Vec3 var3 = mc.player.getEyePosition();
      AABB var4 = new AABB(var2);
      double var5 = this.o();

      for (Vec3 var8 : AutoCrystalModule.B(var1, var3)) {
         if (!(var3.distanceToSqr(var8) > var5 * var5) && !var4.clip(var3, var8).isPresent()) {
            BlockHitResult var9 = mc.level.clip(new ClipContext(var3, var8, Block.OUTLINE, Fluid.NONE, mc.player));
            if (var9.getType() == Type.MISS || var9.getBlockPos().equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   private void d(String var1) {
      _d.N(this);
      this.F = null;
      this.o = null;
      this.E = null;
      this.K = this.D = 0L;
      this.A = var1;
   }

   private record r(BlockPos anchor, BlockPos shield, LivingEntity target, double damage, double self, double exposure) {
      double value() {
         return this.damage - this.self * 0.25;
      }
   }

   private record u(BlockPos pos, float exposure) {
   }
}
