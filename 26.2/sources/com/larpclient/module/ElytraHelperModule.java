package com.larpclient.module;

import com.larpclient.bc;
import com.larpclient.c5;
import com.larpclient.cu;
import com.larpclient.ta;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;

public final class ElytraHelperModule extends Module {
   static final int V = cu.j(180.0);
   static final int I = 10;
   static final int C = 3;
   private final v p = this.addSetting(new v("Firework Key", "Hold this key to use hotbar fireworks while gliding", -1));
   private final s q = this.addSetting(new s("Auto Start", "Start gliding when falling with an elytra equipped", true));
   private final s G = this.addSetting(new s("Fast Start", "Use a firework on the first automatic takeoff", true));
   private final s J = this.addSetting(new s("Combat Relaunch", "Jump and relaunch when landing with an elytra equipped", true));
   private final e w = this.addSetting(new e("Relaunch Delay", "Delay after the relaunch jump in milliseconds", 175.0, 0.0, 1000.0, 25.0));
   private final s B = this.addSetting(new s("Relaunch Firework", "Use a firework after the relaunch", true));
   private final s R = this.addSetting(new s("Auto Firework", "Use hotbar fireworks when flight speed drops", false));
   private final e c = this.addSetting(new e("Delay", "Minimum time between fireworks in milliseconds", 650.0, 100.0, 2000.0, 25.0));
   private final s u = this.addSetting(new s("Only With Target", "Automatic fireworks require the current Kill Aura target", false));
   private final s O = this.addSetting(new s("Only While Gliding", "Automatic fireworks require active gliding", true));
   private final cu L = new cu(this);
   private LocalPlayer M;
   private ClientLevel i;
   private int j = -1073741824;
   private int P;
   private int Y;
   private int v;
   private boolean t;
   private boolean Z;
   private boolean o;

   public ElytraHelperModule() {
      super("ElytraHelper", "Automate elytra takeoff and fireworks", Category.MOVEMENT, -1);
      this.G.P(this.q::getValue);
      this.w.P(this.J::getValue);
      this.B.P(this.J::getValue);
      this.c.P(this.R::getValue);
      this.u.P(this.R::getValue);
      this.O.P(this.R::getValue);
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onEnable() {
      this.I();
      this.L.l();
      this.M = mc.player;
      this.i = mc.level;
      this.j = mc.player == null ? -1073741824 : mc.player.tickCount;
   }

   @Override
   public void onDisable() {
      ta.s(this);
      this.I();
      this.L.l();
      this.M = null;
      this.i = null;
   }

   private void I() {
      this.L.y();
      this.Y = 0;
      this.t = (boolean)0;
      this.Z = (boolean)0;
      this.o = (boolean)0;
   }

   private boolean L() {
      return this.isInGame()
         && mc.player == this.M
         && mc.level == this.i
         && c5.J()
         && ta.Y()
         && !mc.player.getAbilities().flying
         && !mc.player.isPassenger()
         && !mc.player.isInWater()
         && !mc.player.isInLava()
         && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && !this.I();
   }

   private boolean I() {
      AirStuckModule var1 = AirStuckModule.T();
      ElytraMotionModule var2 = ElytraMotionModule.v();
      return var1 != null && var1.I() || var2 != null && var2.K();
   }

   @Override
   public void onTick() {
      this.L.m();
      if (this.L() && !cu.n(this)) {
         int var1 = mc.player.tickCount;
         if (this.Y != 0) {
            this.Y(var1);
         } else if (mc.player.onGround()) {
            this.t = (boolean)0;
            if (this.Z) {
               int var13 = var1 - this.P;
               byte var4 = 10;
               int var3 = var13;
               if ((-Integer.compare(var3, var4) >>> 31 ^ 1) == 0) {
                  this.Z = (boolean)0;
               }
            }

            if (this.J.getValue() && !this.Z) {
               this.L.d();
               this.P = var1;
               this.Z = (boolean)1;
               com.larpclient.h.U("ElytraHelper", "Relaunch jump through the jump key; waiting {} ticks", cu.j(this.w.getValue()));
            }
         } else {
            if (this.Z && !this.J.getValue()) {
               this.Z = (boolean)0;
            }

            if (this.Z) {
               int var12 = var1 - this.P;
               int var6 = cu.j(this.w.getValue());
               int var5 = var12;
               if (Integer.compare(var5, var6) >>> 31 == 0) {
                  this.M(this.B.getValue());
                  this.Z = (boolean)0;
                  this.t = (boolean)1;
               }
            } else if (this.q.getValue() && !mc.player.isFallFlying() && mc.player.fallDistance > 0.0) {
               this.M(this.G.getValue() && !this.t);
               this.t = (boolean)1;
            } else {
               int var2 = mc.player.isFallFlying() && bc.I(mc.getWindow().handle(), this.p.getValue()) ? 1 : 0;
               if (var2 != 0) {
                  int var10000 = var1 - this.j;
                  int var10 = V;
                  int var9 = var10000;
                  if (Integer.compare(var9, var10) >>> 31 == 0) {
                     this.A(() -> this.L() && mc.player.isFallFlying() && bc.I(mc.getWindow().handle(), this.p.getValue()));
                     return;
                  }
               }

               if (this.R.getValue() && this.r()) {
                  int var11 = var1 - this.j;
                  int var8 = cu.j(this.c.getValue());
                  int var7 = var11;
                  if (Integer.compare(var7, var8) >>> 31 == 0) {
                     this.A(() -> this.L() && this.r());
                  }
               }
            }
         }
      } else {
         ta.s(this);
         this.I();
      }
   }

   private void Y(int var1) {
      if (var1 > this.v) {
         ta.s(this);
         this.I();
         com.larpclient.h.U("ElytraHelper", "Takeoff timed out; released jump input");
      } else {
         if (this.Y == 2) {
            if (mc.player.onGround()) {
               this.I();
               return;
            }

            this.L.d();
            this.Y = 3;
         } else {
            boolean var2 = mc.player.isFallFlying();
            com.larpclient.h.U("ElytraHelper", "Takeoff {}; firework={}", var2 ? "started" : "did not start", this.o && var2);
            if (var2 && this.o) {
               this.A(() -> this.L() && mc.player.isFallFlying());
            }

            this.Y = 0;
            this.o = (boolean)0;
         }
      }
   }

   private void A(BooleanSupplier var1) {
      this.L.m(var1, 4.0, 0, () -> this.j = mc.player.tickCount);
   }

   private void M(boolean var1) {
      this.L.Z();
      this.o = var1;
      this.Y = 2;
      this.v = mc.player.tickCount + 3;
   }

   private boolean r() {
      if (this.O.getValue() && !mc.player.isFallFlying()) {
         return false;
      }

      LivingEntity var1 = R();
      if (this.u.getValue() && var1 == null) {
         return false;
      }

      double var2 = mc.player.getDeltaMovement().horizontalDistance() * 20.0;
      return var1 == null ? var2 <= 22.0 : !(mc.player.distanceTo(var1) < 5.0F) || !(var2 > var1.getDeltaMovement().horizontalDistance() * 20.0 * 0.9);
   }

   static LivingEntity R() {
      AuraModule var0 = AuraModule.V();
      LivingEntity var1 = var0 == null ? null : var0.u();
      return var1 != null && var1.isAlive() && !var1.isRemoved() && var1.level() == getMc().level ? var1 : null;
   }

   @Override
   public String getStatus() {
      if (this.Y != 0) {
         return "Takeoff";
      }

      if (this.Z) {
         return "Relaunch";
      }

      LocalPlayer var1 = mc == null ? null : mc.player;
      return var1 != null && var1 == this.M && this.R.getValue() && var1.isFallFlying() && !this.L.D() ? "No rockets" : "";
   }
}
