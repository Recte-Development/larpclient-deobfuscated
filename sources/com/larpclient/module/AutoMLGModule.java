package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q099a584e4baa1bb9;
import com.larp.obf.qbac0b262827a15ac.qb131e08b200c8a57;
import com.larpclient._8;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.ti;
import com.larpclient.tq;
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
   public static final float S = 89.5F;
   public static final double v = -0.6;
   public static final int H = 3;
   public static final int Y = 40;
   private final d9 P = this.addSetting(new d9("Fall Distance", "Blocks fallen before the water bucket is readied", 8.0, 3.0, 40.0, 1.0));
   private final dh V = this.addSetting(new dh("Pick Up", "Pick the water back up after landing", true));
   private final ti X = new ti(this);
   private AutoMLGModule.f O = AutoMLGModule.f.W;
   private LocalPlayer y;
   private ClientLevel p;
   private float o;
   private int i;
   private String w = "Ready";

   public AutoMLGModule() {
      super("Auto MLG", "Place water below you before a long fall lands", Category.MOVEMENT, -1);
   }

   public static boolean n(boolean var0, boolean var1, double var2, double var4, double var6) {
      return !var0 && !var1 && var2 >= var6 && var4 < -0.6;
   }

   public static boolean V(boolean var0, boolean var1, int var2) {
      return (var0 || var1) && var2 >= 3;
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
      return this.isEnabled() ? this.w : "";
   }

   @Override
   public void onEnable() {
      this.y();
      this.y = mc.player;
      this.p = mc.level;
      this.w = "Ready";
      mt.K("AutoMLG", "Enabled; fall distance {} blocks, pick up={}", this.P.P().intValue(), this.V.P());
   }

   @Override
   public void onDisable() {
      _m.w(this);
      if (this.O != AutoMLGModule.f.W && this.y != null && this.y == getMc().player) {
         this.X.W(mc, true);
         this.y.setXRot(this.o);
      }

      this.y();
      this.y = null;
      this.p = null;
   }

   private void y() {
      _m.w(this);
      this.O = AutoMLGModule.f.W;
      this.i = 0;
   }

   private boolean T() {
      tq.y var1 = tq.l();
      return !_m.z() || mc.gui.screen() != null || var1 != tq.y.A;
   }

   @Override
   public void onTick() {
      if (this.y != mc.player || this.p != mc.level) {
         this.X.W(getMc(), false);
         this.y();
         this.y = mc.player;
         this.p = mc.level;
      }

      int var10000 = this.T();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         if (this.O != AutoMLGModule.f.W) {
            this.w = "Paused";
         }
      } else {
         switch (this.O) {
            case W:
               this.a();
               break;
            case B:
               this.T();
               break;
            case t:
               this.O();
               break;
            case A:
               this.z();
         }
      }
   }

   private void a() {
      Abilities var1 = this.y.getAbilities();
      if (!var1.flying
         && !var1.invulnerable
         && !this.y.isFallFlying()
         && !this.y.isPassenger()
         && n(this.y.onGround(), this.y.isInWater(), this.y.fallDistance, this.y.getDeltaMovement().y, this.P.P())) {
         if (_d.B(Items.WATER_BUCKET, true) == -1) {
            this.w = "No water bucket in hotbar or hands";
         } else if ((Boolean)this.p.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, this.y.blockPosition())) {
            this.w = "Water evaporates here";
         } else {
            this.o = this.y.getXRot();
            this.i = 0;
            this.O = AutoMLGModule.f.B;
            this.w = "Falling";
            this.y(Items.WATER_BUCKET);
            mt.U(
               "AutoMLG",
               "Armed after {} blocks at {} blocks per tick",
               String.format(Locale.ROOT, "%.1f", this.y.fallDistance),
               String.format(Locale.ROOT, "%.2f", this.y.getDeltaMovement().y)
            );
            this.T();
         }
      }
   }

   private void T() {
      if (!this.y.onGround() && !this.y.isInWater() && !(this.y.fallDistance <= 0.0)) {
         this.n();
         this.w = "Falling";
         if (this.X(this.y.getYRot(), 89.5F) != null) {
            _m.F(this, this::V, this::O);
         }
      } else {
         this.O = AutoMLGModule.f.A;
         this.z();
      }
   }

   private void O() {
      this.n();
      this.i++;
      if (this.i > 40) {
         this.w = "Pick up timed out";
         this.O = AutoMLGModule.f.A;
      } else if (V(this.y.onGround(), this.y.isInWater(), this.i)) {
         _m.F(this, this::R, () -> this.O == AutoMLGModule.f.t && this.a(Items.BUCKET));
      }
   }

   private void z() {
      int var1 = this.y.getInventory().getSelectedSlot();
      this.X.W(mc, true);
      if (mc.gameMode != null && var1 != this.y.getInventory().getSelectedSlot()) {
         ((q099a584e4baa1bb9)mc.gameMode).larpSyncCarriedItem();
      }

      m9.k(this, m9.s.Z, this.y.getYRot(), this.o, 1.0);
      this.y();
      this.w = "Ready";
   }

   private void n() {
      m9.k(this, m9.s.Z, this.y.getYRot(), 89.5F, 1.0);
   }

   private Vec3 X(float var1, float var2) {
      Vec3 var3 = this.y.getEyePosition();
      Vec3 var4 = var3.add(Vec3.directionFromRotation(var2, var1).scale(this.y.blockInteractionRange()));
      BlockHitResult var5 = this.p.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, this.y));
      return var5.getType() == Type.BLOCK ? var5.getLocation() : null;
   }

   private boolean O() {
      return this.O == AutoMLGModule.f.B
         && !this.y.onGround()
         && !this.y.isInWater()
         && Math.abs(this.y.getXRot() - 89.5F) < 0.5F
         && !this.y.isUsingItem()
         && this.a(Items.WATER_BUCKET)
         && this.X(this.y.getYRot(), this.y.getXRot()) != null;
   }

   private boolean a(Item var1) {
      int var2 = _d.B(var1, true);
      return var2 == -2 || var2 >= 0 && (var2 == this.y.getInventory().getSelectedSlot() || ti.V(this));
   }

   private InteractionHand y(Item var1) {
      int var2 = _d.B(var1, true);
      if (var2 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var2 < 0) {
         return null;
      } else {
         return var2 != this.y.getInventory().getSelectedSlot() && !this.X.e(this.y, var2) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private boolean d(Item var1) {
      InteractionHand var2 = this.y(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = mc.gameMode.useItem(this.y, var2).consumesAction();
      if (var3) {
         _8.G(this.y, var2);
         ((qb131e08b200c8a57)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void V() {
      if (this.O()) {
         if (!this.d(Items.WATER_BUCKET)) {
            this.w = "Water placement rejected";
         } else {
            this.O = this.V.P() ? AutoMLGModule.f.t : AutoMLGModule.f.A;
            this.i = 0;
            this.w = "Water placed";
            mt.U("AutoMLG", "Water placed after {} blocks", String.format(Locale.ROOT, "%.1f", this.y.fallDistance));
         }
      }
   }

   private void R() {
      boolean var1 = this.d(Items.BUCKET);
      this.w = var1 ? "Water picked up" : "Pick up rejected";
      this.O = AutoMLGModule.f.A;
      mt.U("AutoMLG", "Pick up {}", var1 ? "requested" : "rejected locally");
   }

   public enum f {
      W,
      B,
      t,
      A;
   }
}
