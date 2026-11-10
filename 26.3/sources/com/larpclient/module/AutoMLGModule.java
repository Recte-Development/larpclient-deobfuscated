package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q9b8c1cd7fe41113a;
import com.larp.obf.q41a5851bcf8e876a.qc90ed4dd4cb15de4;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.h8;
import com.larpclient.ij;
import com.larpclient.s6;
import com.larpclient.vi;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AutoMLGModule extends Module {
   public static final float w = 89.5F;
   public static final double h = -0.6;
   public static final int s = 3;
   public static final int i = 40;
   private final b r = this.addSetting(new b("Fall Distance", "Blocks fallen before the water bucket is readied", 8.0, 3.0, 40.0, 1.0));
   private final m X = this.addSetting(new m("Pick Up", "Pick the water back up after landing", true));
   private final s6 b = new s6(this);
   private AutoMLGModule.t M = AutoMLGModule.t.w;
   private LocalPlayer P;
   private ClientLevel m;
   private float T;
   private int K;
   private String C = "Ready";

   public AutoMLGModule() {
      super("Auto MLG", "Place water below you before a long fall lands", Category.MOVEMENT, -1);
   }

   public static boolean t(boolean var0, boolean var1, double var2, double var4, double var6) {
      return !var0 && !var1 && var2 >= var6 && var4 < -0.6;
   }

   public static boolean k(boolean var0, boolean var1, int var2) {
      return (var0 || var1) && var2 >= 3;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.C : "";
   }

   @Override
   public void onEnable() {
      this.h();
      this.P = mc.player;
      this.m = mc.level;
      this.C = "Ready";
      _q.z("AutoMLG", "Enabled; fall distance {} blocks, pick up={}", this.r.getValue().intValue(), this.X.getValue());
   }

   @Override
   public void onDisable() {
      _p.e(this);
      if (this.M != AutoMLGModule.t.w && this.P != null && this.P == getMc().player) {
         this.b.X(mc, true);
         this.P.setXRot(this.T);
      }

      this.h();
      this.P = null;
      this.m = null;
   }

   private void h() {
      _p.e(this);
      this.M = AutoMLGModule.t.w;
      this.K = 0;
   }

   private boolean t() {
      ij.r var1 = ij.L();
      return !_p.g() || mc.gui.screen() != null || var1 != ij.r.X;
   }

   @Override
   public void onTick() {
      if (this.P != mc.player || this.m != mc.level) {
         this.b.X(getMc(), false);
         this.h();
         this.P = mc.player;
         this.m = mc.level;
      }

      int var10000 = this.t();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         if (this.M != AutoMLGModule.t.w) {
            this.C = "Paused";
         }
      } else {
         switch (this.M) {
            case w:
               this.g();
               break;
            case u:
               this.C();
               break;
            case r:
               this.P();
               break;
            case V:
               this.w();
         }
      }
   }

   private void g() {
      Abilities var1 = this.P.getAbilities();
      if (!var1.flying
         && !var1.invulnerable
         && !this.P.isFallFlying()
         && !this.P.isPassenger()
         && t(this.P.onGround(), this.P.isInWater(), this.P.fallDistance, this.P.getDeltaMovement().y, this.r.getValue())) {
         if (vi.u(Items.WATER_BUCKET, true) == -1) {
            this.C = "No water bucket in hotbar or hands";
         } else if ((Boolean)this.m.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, this.P.blockPosition())) {
            this.C = "Water evaporates here";
         } else {
            this.T = this.P.getXRot();
            this.K = 0;
            this.M = AutoMLGModule.t.u;
            this.C = "Falling";
            this.t(Items.WATER_BUCKET);
            _q.c(
               "AutoMLG",
               "Armed after {} blocks at {} blocks per tick",
               String.format(Locale.ROOT, "%.1f", this.P.fallDistance),
               String.format(Locale.ROOT, "%.2f", this.P.getDeltaMovement().y)
            );
            this.C();
         }
      }
   }

   private void C() {
      if (!this.P.onGround() && !this.P.isInWater() && !(this.P.fallDistance <= 0.0)) {
         this.f();
         this.C = "Falling";
         if (this.w(this.P.getYRot(), 89.5F) != null) {
            _p.w(this, this::c, this::v);
         }
      } else {
         this.M = AutoMLGModule.t.V;
         this.w();
      }
   }

   private void P() {
      this.f();
      this.K++;
      if (this.K > 40) {
         this.C = "Pick up timed out";
         this.M = AutoMLGModule.t.V;
      } else if (k(this.P.onGround(), this.P.isInWater(), this.K)) {
         _p.w(this, this::v, () -> this.M == AutoMLGModule.t.r && this.W(Items.BUCKET));
      }
   }

   private void w() {
      int var1 = this.P.getInventory().getSelectedSlot();
      this.b.X(mc, true);
      if (mc.gameMode != null && var1 != this.P.getInventory().getSelectedSlot()) {
         ((q9b8c1cd7fe41113a)mc.gameMode).larpSyncCarriedItem();
      }

      b6.P(this, b6.w.X, this.P.getYRot(), this.T, 1.0);
      this.h();
      this.C = "Ready";
   }

   private void f() {
      b6.P(this, b6.w.X, this.P.getYRot(), 89.5F, 1.0);
   }

   private Vec3 w(float var1, float var2) {
      Vec3 var3 = this.P.getEyePosition();
      Vec3 var4 = var3.add(Vec3.directionFromRotation(var2, var1).scale(this.P.blockInteractionRange()));
      BlockHitResult var5 = this.m.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, this.P));
      return var5.getType() == Type.BLOCK ? var5.getLocation() : null;
   }

   private boolean v() {
      return this.M == AutoMLGModule.t.u
         && !this.P.onGround()
         && !this.P.isInWater()
         && Math.abs(this.P.getXRot() - 89.5F) < 0.5F
         && !this.P.isUsingItem()
         && this.W(Items.WATER_BUCKET)
         && this.w(this.P.getYRot(), this.P.getXRot()) != null;
   }

   private boolean W(Item var1) {
      int var2 = vi.u(var1, true);
      return var2 == -2 || var2 >= 0 && (var2 == this.P.getInventory().getSelectedSlot() || s6.L(this));
   }

   private InteractionHand t(Item var1) {
      int var2 = vi.u(var1, true);
      if (var2 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var2 < 0) {
         return null;
      } else {
         return var2 != this.P.getInventory().getSelectedSlot() && !this.b.h(this.P, var2) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private boolean U(Item var1) {
      InteractionHand var2 = this.t(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = mc.gameMode.useItem(this.P, var2).consumesAction();
      if (var3) {
         h8.i(this.P, var2);
         ((qc90ed4dd4cb15de4)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void c() {
      if (this.v()) {
         if (!this.U(Items.WATER_BUCKET)) {
            this.C = "Water placement rejected";
         } else {
            this.M = this.X.getValue() ? AutoMLGModule.t.r : AutoMLGModule.t.V;
            this.K = 0;
            this.C = "Water placed";
            _q.c("AutoMLG", "Water placed after {} blocks", String.format(Locale.ROOT, "%.1f", this.P.fallDistance));
         }
      }
   }

   private void v() {
      boolean var1 = this.U(Items.BUCKET);
      this.C = var1 ? "Water picked up" : "Pick up rejected";
      this.M = AutoMLGModule.t.V;
      _q.c("AutoMLG", "Pick up {}", var1 ? "requested" : "rejected locally");
   }

   public enum t {
      w,
      u,
      r,
      V;
   }
}
