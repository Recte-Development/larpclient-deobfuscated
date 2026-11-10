package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.j6;
import com.larpclient.kz;
import com.larpclient.lx;
import com.larpclient.mr;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
import com.larpclient.zu;
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

public final class AutoAnchorModule extends Module implements mr.n, t1.s {
   private static final int r = 12;
   private static final int T = 2;
   private static AutoAnchorModule M;
   private final e k = this.r("Range", "Anchor reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final e x = this.r("Delay", "Ticks between confirmed anchor steps", 0.0, 0.0, 20.0, 1.0);
   private final s u = this.e("Auto Switch", "Use hotbar items and restore the selected slot after each step", true);
   private final s d = this.e("Safe Anchor", "Place a block between you and the anchor before detonating, where it leaves you least exposed", true);
   private final l B = this.addSetting(
      new l("Shield Block", "Block placed as the shield; the blast hurts players before it breaks blocks", "Glowstone", "Glowstone", "Obsidian")
   );
   private final s Y = this.e("Players", "Target other survival players", true);
   private final s Q = this.e("Mobs", "Also target hostile mobs for combat or testing", false);
   private final e z = this.r("Target Range", "Search distance for opponents", 8.0, 2.0, 14.0, 0.5);
   private final e J = this.r("Min Damage", "Minimum estimated target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final s R = this.e("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final e C = this.r("Max Self Damage", "Maximum estimated self damage in HP", 8.0, 0.0, 36.0, 0.5);
   private final s f = this.e("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final l I = this.addSetting(
      new l("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final mr i;
   private AutoAnchorModule.h w;
   private String F;
   private String y = "";
   private String p = "";
   private String o = "Waiting for a target";
   private BooleanSupplier K;
   private long c;
   private long Z;
   private Object X;
   private Object N;
   private Object q;
   private Object P;
   private int G;

   public AutoAnchorModule() {
      super("AutoAnchor", "Automatically place, charge and detonate respawn anchors", Category.COMBAT, -1);
      M = this;
      this.B.P(this.d::getValue);
      this.C.P(this.R::getValue);
      this.I.o(var1 -> {
         this.J("Rotation changed");
         this.c();
      });
      this.i = new mr(this, var1 -> this.addSetting(var1), 0.5, 0.3);
   }

   private e r(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   private s e(String var1, String var2, boolean var3) {
      return this.addSetting(new s(var1, var2, var3));
   }

   public static AutoAnchorModule y() {
      return M;
   }

   public List<q<?>> Z() {
      return List.of(this.k, this.x, this.u, this.d, this.B);
   }

   public List<q<?>> A() {
      return List.of(this.Y, this.Q, this.z);
   }

   public List<q<?>> W() {
      return List.of(this.J, this.R, this.C, this.f);
   }

   public List<q<?>> Q() {
      return List.of(this.I);
   }

   @Override
   public mr placementHighlight() {
      return this.i;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K, m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.o : "";
   }

   @Override
   public void onEnable() {
      this.X = this.N = null;
      this.J("Waiting for a target");
      this.i.t();
      com.larpclient.h.A(
         "AutoAnchor",
         "Enabled; rotation={}, range={}, safeAnchor={}, minDamage={}, maxSelf={}",
         this.I.getValue(),
         this.k.getValue(),
         this.d.getValue(),
         this.J.getValue(),
         this.R.getValue() ? this.C.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.J("Waiting for a target");
      this.c();
      this.i.c();
      this.X = this.N = null;
      com.larpclient.h.A("AutoAnchor", "Disabled; anchor intents and server waits cleared");
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (p_.I(p_.o.d)) {
            j6.C(j6.l.p, var1, var2, 1.0);
            if (j6.g(j6.l.p)) {
               this.q = mc.player;
               this.P = mc.level;
               this.G = mc.player.tickCount;
               p_.p(p_.o.d);
            } else {
               this.c();
            }
         }
      }
   }

   private void c() {
      this.q = this.P = null;
      p_.Y(p_.o.d);
   }

   public boolean O() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.I.O("Visible")
            && mc.player != null
            && this.q == mc.player
            && this.P == mc.level
            && mc.player.tickCount >= this.G
            && mc.player.tickCount - this.G <= 1
            && (!this.f.getValue() || !mc.player.isUsingItem());
   }

   public float d(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && p_.A() == p_.o.d && j6.g(j6.l.p)) {
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
      this.i.q(null);
      t1.v(this);
      if (!this.isInGame()) {
         this.J("Waiting for a world");
      } else {
         if (this.X != mc.player || this.N != mc.level) {
            this.J("Waiting for a target");
            this.X = mc.player;
            this.N = mc.level;
         }

         try {
            this.X();
         } finally {
            int var10000 = this.o.equals(this.p);
            boolean var5 = false;
            byte var4 = (byte)var10000;
            var10000 = Integer.signum(var4);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.p = this.o;
               com.larpclient.h.U("AutoAnchor", "Status: {}", this.o);
            }
         }
      }
   }

   private void X() {
      long var1 = mc.level.getGameTime();
      if (this.F != null) {
         if (!this.K.getAsBoolean()) {
            if (var1 - this.c > 12L) {
               com.larpclient.h.A("AutoAnchor", "No server update after {} ticks for: {}; replanning", 12, this.F);
               this.J("Server did not confirm the last step");
               return;
            }

            if (this.w != null) {
               this.i.q(this.w.anchor());
            }

            this.o = "Waiting for " + this.F;
            return;
         }

         com.larpclient.h.U("AutoAnchor", "Server confirmed: {}", this.F);
         if (this.F.equals("detonation")) {
            this.w = null;
         }

         this.F = null;
         this.Z = var1 + this.x.getValue().intValue();
      }

      String var3 = this.W();
      if (var3 != null) {
         this.w = null;
         this.o = var3;
      } else if (var1 < this.Z) {
         if (this.w != null) {
            this.i.q(this.w.anchor());
         }
      } else {
         List var4 = this.X();
         if (var4.isEmpty()) {
            this.w = null;
            this.o = this.Q.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)";
         } else {
            if (this.w != null) {
               this.w = this.C(this.w, var4);
            }

            if (this.w == null) {
               this.w = this.t(var4);
            }

            if (this.w == null) {
               this.o = this.y;
            } else {
               this.i.q(this.w.anchor());
               kz.H(this.w.target());
               this.T(this.w);
            }
         }
      }
   }

   private String W() {
      if (!ta.Y() || !mc.isWindowActive()) {
         return "Paused: screen, focus or detached camera";
      } else if (this.f.getValue() && mc.player.isUsingItem()) {
         return "Paused while using an item";
      } else if (mc.player.isSecondaryUseActive()) {
         return "Paused while sneaking";
      } else if (this.v(mc.player.blockPosition())) {
         return "Anchors set spawn here, so they cannot explode";
      } else {
         return this.I.O("Visible") && !p_.I(p_.o.d) ? "Paused for another camera owner" : null;
      }
   }

   private void T(AutoAnchorModule.h var1) {
      BlockState var2 = mc.level.getBlockState(var1.anchor());
      BlockPos var3 = var1.anchor();
      if (!var2.is(Blocks.RESPAWN_ANCHOR)) {
         this.o(true, var3, Items.RESPAWN_ANCHOR, "anchor placement", () -> mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      } else if ((Integer)var2.getValue(RespawnAnchorBlock.CHARGE) == 0) {
         this.o(false, var3, Items.GLOWSTONE, "anchor charge", () -> this.S(var3) > 0);
      } else if (var1.shield() != null && t1.t(var1.shield())) {
         BlockPos var5 = var1.shield();
         this.o(true, var5, this.F(), "shield placement", () -> !t1.t(var5));
      } else {
         if (mc.player.getOffhandItem().is(Items.GLOWSTONE) && (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) < 4) {
            this.o = "Move offhand glowstone before detonation";
            return;
         }

         Item var4 = this.H();
         if (var4 == null) {
            this.o = "A hotbar item other than glowstone is required to detonate";
            return;
         }

         this.o(false, var3, var4, "detonation", () -> !mc.level.getBlockState(var3).is(Blocks.RESPAWN_ANCHOR));
      }
   }

   private void o(boolean var1, BlockPos var2, Item var3, String var4, BooleanSupplier var5) {
      AutoAnchorModule.h var6 = this.w;
      BooleanSupplier var7 = () -> this.w == var6 && this.F == null && this.W() == null;
      Consumer var8 = var5x -> {
         if (!var5x) {
            this.o = "Anchor " + var4 + " rejected; retrying";
         } else {
            this.F = var4;
            this.K = var5;
            this.c = mc.level.getGameTime();
            if (var4.equals("detonation")) {
               com.larpclient.h.A(
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
      boolean var9 = this.I.O("Visible");
      boolean var10 = var1
         ? t1.K(this, var2, var3, this.k.getValue(), var9, this.u.getValue(), var7, var8)
         : t1.G(this, var2, var3, this.k.getValue(), var9, this.u.getValue(), var7, var8);
      this.o = var10
         ? String.format(
            Locale.ROOT,
            "%s%s; %.1f HP on %s",
            Character.toUpperCase(var4.charAt(0)),
            var4.substring(1),
            this.w.damage(),
            this.w.target().getName().getString()
         )
         : (var9 ? "No reachable face for " + var4 : "Aim at the " + (var1 ? "support" : "anchor") + " for " + var4);
   }

   private boolean v(BlockPos var1) {
      return Boolean.TRUE.equals(mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, var1));
   }

   private int S(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.is(Blocks.RESPAWN_ANCHOR) ? (Integer)var2.getValue(RespawnAnchorBlock.CHARGE) : 0;
   }

   private Item F() {
      return this.B.O("Obsidian") ? Items.OBSIDIAN : Items.GLOWSTONE;
   }

   private Item H() {
      ItemStack var1 = mc.player.getOffhandItem();
      ItemStack var2 = mc.player.getMainHandItem();
      if (!var2.is(Items.GLOWSTONE)) {
         return var2.isEmpty() ? Items.AIR : var2.getItem();
      }

      if (this.u.getValue()) {
         for (int var3 = 0; var3 < 9; var3++) {
            ItemStack var4 = mc.player.getInventory().getItem(var3);
            if (!var4.is(Items.GLOWSTONE) && (var4.isEmpty() ? !var1.isEmpty() : !var1.is(var4.getItem()))) {
               return var4.isEmpty() ? Items.AIR : var4.getItem();
            }
         }
      }

      return null;
   }

   private boolean S(LivingEntity var1) {
      if (var1 == mc.player
         || !var1.isAlive()
         || var1.isRemoved()
         || var1.isSpectator()
         || var1.level() != mc.level
         || mc.player.distanceTo(var1) > this.z.getValue()
         || mc.player.isAlliedTo(var1)) {
         return false;
      }

      if (var1 instanceof Player var2) {
         if (this.Y.getValue() && !var2.getAbilities().invulnerable) {
            MCFriendModule var3 = LarpClient.R().z().p(MCFriendModule.class);
            return var3 == null || !var3.S(var2.getName().getString());
         } else {
            return false;
         }
      } else {
         return this.Q.getValue() && var1 instanceof Enemy;
      }
   }

   private List<LivingEntity> X() {
      ArrayList var1 = new ArrayList();

      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof LivingEntity var4 && this.S(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.player::distanceToSqr));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private double Y() {
      return Math.min(this.k.getValue(), mc.player.blockInteractionRange());
   }

   private boolean S(double var1) {
      return !this.R.getValue() || zu.P(var1, this.C.getValue(), mc.player.getHealth() + mc.player.getAbsorptionAmount());
   }

   private AutoAnchorModule.h m(BlockPos var1, BlockPos var2, List<LivingEntity> var3, double var4, double var6) {
      LivingEntity var8 = null;
      double var9 = 0.0;

      for (LivingEntity var12 : var3) {
         double var13 = lx.C(var12, var1, var2);
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var8 == null ? null : new AutoAnchorModule.h(var1, var2, var8, var9, var4, var6);
   }

   private AutoAnchorModule.h C(AutoAnchorModule.h var1, List<LivingEntity> var2) {
      boolean var3 = mc.level.getBlockState(var1.anchor()).is(Blocks.RESPAWN_ANCHOR);
      if ((var3 || t1.t(var1.anchor())) && !this.v(var1.anchor())) {
         if (this.Q(var1.anchor(), var3) && this.U(var1.anchor(), var3)) {
            BlockPos var4 = var1.shield() == null || t1.t(var1.shield()) && (!this.e(var1.shield()) || !this.h(var1.anchor(), var1.shield()))
               ? null
               : var1.shield();
            float var5 = lx.T(mc.player, var1.anchor(), var4);
            double var6 = lx.B(mc.player, var1.anchor(), var5);
            if (!this.S(var6)) {
               return null;
            }

            AutoAnchorModule.h var8 = this.m(var1.anchor(), var4, var2, var6, var5);
            return var8 != null && var8.damage() >= this.J.getValue() ? var8 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private AutoAnchorModule.h t(List<LivingEntity> var1) {
      Vec3 var2 = mc.player.getEyePosition();
      double var3 = this.Y();
      int var5 = t1.M(Items.RESPAWN_ANCHOR, this.u.getValue()) != -1 ? 1 : 0;
      int var6 = t1.M(Items.GLOWSTONE, this.u.getValue()) != -1 ? 1 : 0;
      HashSet var7 = new HashSet();
      ArrayList var8 = new ArrayList();
      int var9 = 0;

      record q(BlockPos pos, double bound) {
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
                     && !this.v(var16)) {
                     boolean var17 = mc.level.getBlockState(var16).is(Blocks.RESPAWN_ANCHOR);
                     if (var17) {
                        var9++;
                     }

                     if (var17 ? this.Q(var16, true) : var5 != 0 && var6 != 0 && t1.t(var16)) {
                        double var18 = 0.0;

                        for (LivingEntity var21 : var1) {
                           var18 = Math.max(var18, zu.E(var21, zu.z(var21.position().distanceTo(Vec3.atCenterOf(var16)), 1.0, 5.0)));
                        }

                        if (var18 >= this.J.getValue()) {
                           var8.add(new q(var16, var18));
                        }
                     }
                  }
               }
            }
         }
      }

      if (var8.isEmpty()) {
         this.y = var5 == 0 && var9 == 0
            ? "No respawn anchors in hotbar or hands"
            : (var6 == 0 && var9 == 0 ? "No glowstone in hotbar or hands" : "No anchor spot in reach can reach Min Damage");
         return null;
      }

      var8.sort(Comparator.comparingDouble(q::bound).reversed());
      AutoAnchorModule.h var28 = null;
      double var29 = Double.MAX_VALUE;
      double var30 = 0.0;

      for (q var32 : var8) {
         if (var28 != null && var32.bound() <= var28.value()) {
            break;
         }

         BlockPos var33 = var32.pos();
         boolean var34 = mc.level.getBlockState(var33).is(Blocks.RESPAWN_ANCHOR);
         AutoAnchorModule.h var19 = this.m(var33, null, var1, 0.0, 0.0);
         if (var19 != null) {
            var30 = Math.max(var30, var19.damage());
            if (!(var19.damage() < this.J.getValue()) && (var28 == null || !(var19.damage() <= var28.value()))) {
               float var35 = lx.T(mc.player, var33, null);
               ArrayList var36 = new ArrayList();
               if (this.d.getValue() && var35 > 0.0F && t1.M(this.F(), this.u.getValue()) != -1) {
                  for (BlockPos var23 : lx.E(var33, mc.player.getEyePosition())) {
                     if (t1.t(var23)) {
                        float var24 = lx.T(mc.player, var33, var23);
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
                  double var25 = lx.B(mc.player, var33, var39.exposure());
                  AutoAnchorModule.h var27 = var39.pos() == null
                     ? new AutoAnchorModule.h(var33, null, var19.target(), var19.damage(), var25, var35)
                     : this.m(var33, var39.pos(), var1, var25, var39.exposure());
                  if (var27 != null) {
                     var29 = Math.min(var29, var27.self());
                     if (this.S(var27.self()) && !(var27.damage() < this.J.getValue()) && (var28 == null || !(var27.value() <= var28.value()))) {
                        if (var37 == null) {
                           var37 = this.U(var33, var34);
                        }

                        if (!var37) {
                           break;
                        }

                        if (var27.shield() == null || this.e(var27.shield()) && this.h(var33, var27.shield())) {
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
         this.y = var29 != Double.MAX_VALUE && !this.S(var29)
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var29, this.C.getValue())
            : (
               var30 < this.J.getValue()
                  ? String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var30, this.J.getValue())
                  : (this.I.O("Visible") ? "No anchor spot with a reachable face or support" : "Aim at an anchor or at a support beside an anchor spot")
            );
      } else {
         com.larpclient.h.U(
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

   private boolean Q(BlockPos var1, boolean var2) {
      int var3 = t1.M(Items.GLOWSTONE, this.u.getValue()) != -1 ? 1 : 0;
      return !var2 ? var3 != 0 && t1.M(Items.RESPAWN_ANCHOR, this.u.getValue()) != -1 : this.S(var1) > 0 || var3 != 0;
   }

   private boolean U(BlockPos var1, boolean var2) {
      boolean var3 = this.I.O("Visible");
      if (!var2) {
         return t1.c(var1, Items.RESPAWN_ANCHOR, this.k.getValue(), var3, this.u.getValue()) != null;
      }

      if (!var3) {
         return ta.D(var1, this.Y()) != null;
      }

      Vec3 var4 = mc.player.getEyePosition();

      for (Vec3 var6 : AutoCrystalModule.B(var1, var4)) {
         if (ta.t(var1, this.Y(), var6.subtract(var4)) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean e(BlockPos var1) {
      return t1.c(var1, this.F(), this.k.getValue(), this.I.O("Visible"), this.u.getValue()) != null;
   }

   private boolean h(BlockPos var1, BlockPos var2) {
      Vec3 var3 = mc.player.getEyePosition();
      AABB var4 = new AABB(var2);
      double var5 = this.Y();

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

   private void J(String var1) {
      t1.v(this);
      this.w = null;
      this.F = null;
      this.K = null;
      this.c = this.Z = 0L;
      this.o = var1;
   }

   private record h(BlockPos anchor, BlockPos shield, LivingEntity target, double damage, double self, double exposure) {
      double value() {
         return this.damage - this.self * 0.25;
      }
   }

   private record t(BlockPos pos, float exposure) {
   }
}
