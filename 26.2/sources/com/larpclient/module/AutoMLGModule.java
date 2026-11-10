package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q541491ff38fb1637;
import com.larp.obf.qa300483f54d804a3.q7ae1dbb86ec789d8;
import com.larpclient.hu;
import com.larpclient.j6;
import com.larpclient.ks;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
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
   public static final double O = -0.6;
   public static final int p = 3;
   public static final int w = 40;
   private final e a = this.addSetting(new e("Fall Distance", "Blocks fallen before the water bucket is readied", 8.0, 3.0, 40.0, 1.0));
   private final s g = this.addSetting(new s("Pick Up", "Pick the water back up after landing", true));
   private final hu y = new hu(this);
   private AutoMLGModule.n G = AutoMLGModule.n.Y;
   private LocalPlayer t;
   private ClientLevel i;
   private float P;
   private int x;
   private String b = "Ready";

   public AutoMLGModule() {
      super("Auto MLG", "Place water below you before a long fall lands", Category.MOVEMENT, -1);
   }

   public static boolean K(boolean var0, boolean var1, double var2, double var4, double var6) {
      return !var0 && !var1 && var2 >= var6 && var4 < -0.6;
   }

   public static boolean w(boolean var0, boolean var1, int var2) {
      return (var0 || var1) && var2 >= 3;
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
      return this.isEnabled() ? this.b : "";
   }

   @Override
   public void onEnable() {
      this.D();
      this.t = mc.player;
      this.i = mc.level;
      this.b = "Ready";
      com.larpclient.h.A("AutoMLG", "Enabled; fall distance {} blocks, pick up={}", this.a.getValue().intValue(), this.g.getValue());
   }

   @Override
   public void onDisable() {
      ta.s(this);
      if (this.G != AutoMLGModule.n.Y && this.t != null && this.t == getMc().player) {
         this.y.V(mc, true);
         this.t.setXRot(this.P);
      }

      this.D();
      this.t = null;
      this.i = null;
   }

   private void D() {
      ta.s(this);
      this.G = AutoMLGModule.n.Y;
      this.x = 0;
   }

   private boolean c() {
      p_.o var1 = p_.A();
      return !ta.Y() || mc.gui.screen() != null || var1 != p_.o.T;
   }

   @Override
   public void onTick() {
      if (this.t != mc.player || this.i != mc.level) {
         this.y.V(getMc(), false);
         this.D();
         this.t = mc.player;
         this.i = mc.level;
      }

      int var10000 = this.c();
      boolean var2 = false;
      byte var1 = (byte)var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         if (this.G != AutoMLGModule.n.Y) {
            this.b = "Paused";
         }
      } else {
         switch (this.G) {
            case Y:
               this.l();
               break;
            case R:
               this.u();
               break;
            case g:
               this.H();
               break;
            case a:
               this.Z();
         }
      }
   }

   private void l() {
      Abilities var1 = this.t.getAbilities();
      if (!var1.flying
         && !var1.invulnerable
         && !this.t.isFallFlying()
         && !this.t.isPassenger()
         && K(this.t.onGround(), this.t.isInWater(), this.t.fallDistance, this.t.getDeltaMovement().y, this.a.getValue())) {
         if (t1.M(Items.WATER_BUCKET, true) == -1) {
            this.b = "No water bucket in hotbar or hands";
         } else if ((Boolean)this.i.environmentAttributes().getValue(EnvironmentAttributes.WATER_EVAPORATES, this.t.blockPosition())) {
            this.b = "Water evaporates here";
         } else {
            this.P = this.t.getXRot();
            this.x = 0;
            this.G = AutoMLGModule.n.R;
            this.b = "Falling";
            this.X(Items.WATER_BUCKET);
            com.larpclient.h.U(
               "AutoMLG",
               "Armed after {} blocks at {} blocks per tick",
               String.format(Locale.ROOT, "%.1f", this.t.fallDistance),
               String.format(Locale.ROOT, "%.2f", this.t.getDeltaMovement().y)
            );
            this.u();
         }
      }
   }

   private void u() {
      if (!this.t.onGround() && !this.t.isInWater() && !(this.t.fallDistance <= 0.0)) {
         this.P();
         this.b = "Falling";
         if (this.z(this.t.getYRot(), 89.5F) != null) {
            ta.F(this, this::N, this::L);
         }
      } else {
         this.G = AutoMLGModule.n.a;
         this.Z();
      }
   }

   private void H() {
      this.P();
      this.x++;
      if (this.x > 40) {
         this.b = "Pick up timed out";
         this.G = AutoMLGModule.n.a;
      } else if (w(this.t.onGround(), this.t.isInWater(), this.x)) {
         ta.F(this, this::s, () -> this.G == AutoMLGModule.n.g && this.F(Items.BUCKET));
      }
   }

   private void Z() {
      int var1 = this.t.getInventory().getSelectedSlot();
      this.y.V(mc, true);
      if (mc.gameMode != null && var1 != this.t.getInventory().getSelectedSlot()) {
         ((q541491ff38fb1637)mc.gameMode).larpSyncCarriedItem();
      }

      j6.U(this, j6.l.m, this.t.getYRot(), this.P, 1.0);
      this.D();
      this.b = "Ready";
   }

   private void P() {
      j6.U(this, j6.l.m, this.t.getYRot(), 89.5F, 1.0);
   }

   private Vec3 z(float var1, float var2) {
      Vec3 var3 = this.t.getEyePosition();
      Vec3 var4 = var3.add(Vec3.directionFromRotation(var2, var1).scale(this.t.blockInteractionRange()));
      BlockHitResult var5 = this.i.clip(new ClipContext(var3, var4, Block.OUTLINE, Fluid.NONE, this.t));
      return var5.getType() == Type.BLOCK ? var5.getLocation() : null;
   }

   private boolean L() {
      return this.G == AutoMLGModule.n.R
         && !this.t.onGround()
         && !this.t.isInWater()
         && Math.abs(this.t.getXRot() - 89.5F) < 0.5F
         && !this.t.isUsingItem()
         && this.F(Items.WATER_BUCKET)
         && this.z(this.t.getYRot(), this.t.getXRot()) != null;
   }

   private boolean F(Item var1) {
      int var2 = t1.M(var1, true);
      return var2 == -2 || var2 >= 0 && (var2 == this.t.getInventory().getSelectedSlot() || hu.k(this));
   }

   private InteractionHand X(Item var1) {
      int var2 = t1.M(var1, true);
      if (var2 == -2) {
         return InteractionHand.OFF_HAND;
      } else if (var2 < 0) {
         return null;
      } else {
         return var2 != this.t.getInventory().getSelectedSlot() && !this.y.O(this.t, var2) ? null : InteractionHand.MAIN_HAND;
      }
   }

   private boolean S(Item var1) {
      InteractionHand var2 = this.X(var1);
      if (var2 == null) {
         return false;
      }

      boolean var3 = mc.gameMode.useItem(this.t, var2).consumesAction();
      if (var3) {
         ks.p(this.t, var2);
         ((q7ae1dbb86ec789d8)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void N() {
      if (this.L()) {
         if (!this.S(Items.WATER_BUCKET)) {
            this.b = "Water placement rejected";
         } else {
            this.G = this.g.getValue() ? AutoMLGModule.n.g : AutoMLGModule.n.a;
            this.x = 0;
            this.b = "Water placed";
            com.larpclient.h.U("AutoMLG", "Water placed after {} blocks", String.format(Locale.ROOT, "%.1f", this.t.fallDistance));
         }
      }
   }

   private void s() {
      boolean var1 = this.S(Items.BUCKET);
      this.b = var1 ? "Water picked up" : "Pick up rejected";
      this.G = AutoMLGModule.n.a;
      com.larpclient.h.U("AutoMLG", "Pick up {}", var1 ? "requested" : "rejected locally");
   }

   public enum n {
      Y,
      R,
      g,
      a;
   }
}
