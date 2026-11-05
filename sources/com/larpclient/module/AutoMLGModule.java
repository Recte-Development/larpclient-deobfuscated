package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q143ef5d8ac4bd5b8;
import com.larp.obf.q5682c6a8aca9b6ec.qa1ad45ac3ead4599;
import com.larpclient.d8;
import com.larpclient.ep;
import com.larpclient.hp;
import com.larpclient.m;
import com.larpclient.o_;
import com.larpclient.r4;
import com.larpclient.rr;
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
   public static final float i = 89.5F;
   public static final double j = -0.6;
   public static final int Q = 3;
   public static final int L = 40;
   private final lc d = this.addSetting(new lc("Fall Distance", "Blocks fallen before the water bucket is readied", 8.0, 3.0, 40.0, 1.0));
   private final ln v = this.addSetting(new ln("Pick Up", "Pick the water back up after landing", true));
   private final o_ l = new o_(this);
   private AutoMLGModule.w V = AutoMLGModule.w.h;
   private LocalPlayer O;
   private ClientLevel G;
   private float F;
   private int n;
   private String J = "Ready";

   public AutoMLGModule() {
      super("Auto MLG", "Place water below you before a long fall lands", Category.MOVEMENT, -1);
   }

   public static boolean E(boolean var0, boolean var1, double var2, double var4, double var6) {
      return !var0 && !var1 && var2 >= var6 && var4 < -0.6;
   }

   public static boolean r(boolean var0, boolean var1, int var2) {
      return (var0 || var1) && var2 >= 3;
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
      return this.isEnabled() ? this.J : "";
   }

   @Override
   public void onEnable() {
      this.S();
      this.O = mc.player;
      this.G = mc.level;
      this.J = "Ready";
      r4.V("AutoMLG", "Enabled; fall distance {} blocks, pick up={}", this.d.getValue().intValue(), this.v.getValue());
   }

   @Override
   public void onDisable() {
      d8.K(this);
      if (this.V != AutoMLGModule.w.h && this.O != null && this.O == getMc().player) {
         this.l.m(mc, true);
         this.O.setXRot(this.F);
      }

      this.S();
      this.O = null;
      this.G = null;
   }

   private void S() {
      d8.K(this);
      this.V = AutoMLGModule.w.h;
      this.n = 0;
   }

   private boolean V() {
      rr.f var1 = rr.u();
      return !d8.a() || mc.gui.screen() != null || var1 != rr.f.i;
   }

   @Override
   public void onTick() {
      if (this.O != mc.player || this.G != mc.level) {
         this.l.m(getMc(), false);
         this.S();
         this.O = mc.player;
         this.G = mc.level;
      }

      int var10000 = this.V();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         if (this.V != AutoMLGModule.w.h) {
            this.J = "Paused";
         }
      } else {
         switch (this.V) {
            case h:
               this.j();
               break;
            case O:
               this.r();
               break;
            case L:
               this.M();
               break;
            case x:
               this.h();
         }
      }
   }

   private void j() {
      Abilities var1 = this.O.getAbilities();
      if (!var1.flying
         && !var1.invulnerable
         && !this.O.isFallFlying()
         && !this.O.isPassenger()
         && E(this.O.onGround(), this.O.isInWater(), this.O.fallDistance, this.O.getDeltaMovement().y, this.d.getValue())) {
         if (m.Q(Items.WATER_BUCKET, true) == -1) {
            this.J = "No water bucket in hotbar or hands";
         } else if ((Boolean)this.G.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, this.O.blockPosition())) {
            this.J = "Water evaporates here";
         } else {
            this.F = this.O.getXRot();
            this.n = 0;
            this.V = AutoMLGModule.w.O;
            this.J = "Falling";
            this.k(Items.WATER_BUCKET);
            r4.g(
               "AutoMLG",
               "Armed after {} blocks at {} blocks per tick",
               String.format(Locale.ROOT, "%.1f", this.O.fallDistance),
               String.format(Locale.ROOT, "%.2f", this.O.getDeltaMovement().y)
            );
            this.r();
         }
      }
   }

   private void r() {
      if (!this.O.onGround() && !this.O.isInWater() && !(this.O.fallDistance <= 0.0)) {
         this.q();
         this.J = "Falling";
         if (this.N(this.O.getYRot(), 89.5F) != null) {
            d8.A(this, this::T, this::z);
         }
      } else {
         this.V = AutoMLGModule.w.x;
         this.h();
      }
   }

   private void M() {
      this.q();
      this.n++;
      if (this.n > 40) {
         this.J = "Pick up timed out";
         this.V = AutoMLGModule.w.x;
      } else if (r(this.O.onGround(), this.O.isInWater(), this.n)) {
         d8.A(this, this::d, () -> this.V == AutoMLGModule.w.L && this.V(Items.BUCKET));
      }
   }

   private void h() {
      int var1 = this.O.getInventory().getSelectedSlot();
      this.l.m(mc, true);
      if (mc.gameMode != null && var1 != this.O.getInventory().getSelectedSlot()) {
         ((q143ef5d8ac4bd5b8)mc.gameMode).larpSyncCarriedItem();
      }

      ep.t(this, ep.z.F, this.O.getYRot(), this.F, 1.0);
      this.S();
      this.J = "Ready";
   }

   private void q() {
      ep.t(this, ep.z.F, this.O.getYRot(), 89.5F, 1.0);
   }

   private Vec3 N(float var1, float var2) {
      Vec3 var3 = this.O.getEyePosition();
      Vec3 var4 = var3.add(Vec3.directionFromRotation(var2, var1).scale(this.O.blockInteractionRange()));
      BlockHitResult var5 = this.G.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, this.O));
      return var5.getType() == Type.BLOCK ? var5.getLocation() : null;
   }

   private boolean z() {
      return this.V == AutoMLGModule.w.O
         && !this.O.onGround()
         && !this.O.isInWater()
         && Math.abs(this.O.getXRot() - 89.5F) < 0.5F
         && !this.O.isUsingItem()
         && this.V(Items.WATER_BUCKET)
         && this.N(this.O.getYRot(), this.O.getXRot()) != null;
   }

   private boolean V(Item var1) {
      int var2 = m.Q(var1, true);
      return var2 == -2 || var2 >= 0 && (var2 == this.O.getInventory().getSelectedSlot() || o_.N(this));
   }

   private InteractionHand k(Item var1) {
      int var2 = m.Q(var1, true);
      if (var2 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var2 < 0) {
         return null;
      } else {
         return var2 != this.O.getInventory().getSelectedSlot() && !this.l.s(this.O, var2) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private boolean P(Item var1) {
      InteractionHand var2 = this.k(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = mc.gameMode.useItem(this.O, var2).consumesAction();
      if (var3) {
         hp.Q(this.O, var2);
         ((qa1ad45ac3ead4599)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void T() {
      if (this.z()) {
         if (!this.P(Items.WATER_BUCKET)) {
            this.J = "Water placement rejected";
         } else {
            this.V = this.v.getValue() ? AutoMLGModule.w.L : AutoMLGModule.w.x;
            this.n = 0;
            this.J = "Water placed";
            r4.g("AutoMLG", "Water placed after {} blocks", String.format(Locale.ROOT, "%.1f", this.O.fallDistance));
         }
      }
   }

   private void d() {
      boolean var1 = this.P(Items.BUCKET);
      this.J = var1 ? "Water picked up" : "Pick up rejected";
      this.V = AutoMLGModule.w.x;
      r4.g("AutoMLG", "Pick up {}", var1 ? "requested" : "rejected locally");
   }

   public enum w {
      h,
      O,
      L,
      x;
   }
}
