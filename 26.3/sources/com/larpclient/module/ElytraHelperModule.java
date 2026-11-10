package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.nz;
import com.larpclient.p4;
import com.larpclient.sy;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;

public final class ElytraHelperModule extends Module {
   static final int Y = sy.p(180.0);
   static final int x = 10;
   static final int Z = 3;
   private final x y = this.addSetting(new x("Firework Key", "Hold this key to use hotbar fireworks while gliding", -1));
   private final m J = this.addSetting(new m("Auto Start", "Start gliding when falling with an elytra equipped", true));
   private final m C = this.addSetting(new m("Fast Start", "Use a firework on the first automatic takeoff", true));
   private final m f = this.addSetting(new m("Combat Relaunch", "Jump and relaunch when landing with an elytra equipped", true));
   private final b N = this.addSetting(new b("Relaunch Delay", "Delay after the relaunch jump in milliseconds", 175.0, 0.0, 1000.0, 25.0));
   private final m a = this.addSetting(new m("Relaunch Firework", "Use a firework after the relaunch", true));
   private final m b = this.addSetting(new m("Auto Firework", "Use hotbar fireworks when flight speed drops", false));
   private final b k = this.addSetting(new b("Delay", "Minimum time between fireworks in milliseconds", 650.0, 100.0, 2000.0, 25.0));
   private final m q = this.addSetting(new m("Only With Target", "Automatic fireworks require the current Kill Aura target", false));
   private final m l = this.addSetting(new m("Only While Gliding", "Automatic fireworks require active gliding", true));
   private final sy U = new sy(this);
   private LocalPlayer O;
   private ClientLevel t;
   private int c = -1073741824;
   private int r;
   private int s;
   private int I;
   private boolean R;
   private boolean d;
   private boolean z;

   public ElytraHelperModule() {
      super("ElytraHelper", "Automate elytra takeoff and fireworks", Category.MOVEMENT, -1);
      this.C.a(this.J::getValue);
      this.N.a(this.f::getValue);
      this.a.a(this.f::getValue);
      this.k.a(this.b::getValue);
      this.q.a(this.b::getValue);
      this.l.a(this.b::getValue);
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.k);
   }

   @Override
   public void onEnable() {
      this.z();
      this.U.x();
      this.O = mc.player;
      this.t = mc.level;
      this.c = mc.player == null ? -1073741824 : mc.player.tickCount;
   }

   @Override
   public void onDisable() {
      _p.e(this);
      this.z();
      this.U.x();
      this.O = null;
      this.t = null;
   }

   private void z() {
      this.U.c();
      this.s = 0;
      this.R = (boolean)0;
      this.d = (boolean)0;
      this.z = (boolean)0;
   }

   private boolean Z() {
      return this.isInGame()
         && mc.player == this.O
         && mc.level == this.t
         && nz.v()
         && _p.g()
         && !mc.player.getAbilities().flying
         && !mc.player.isPassenger()
         && !mc.player.isInWater()
         && !mc.player.isInLava()
         && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && !this.v();
   }

   private boolean v() {
      AirStuckModule var1 = AirStuckModule.Z();
      ElytraMotionModule var2 = ElytraMotionModule.n();
      return var1 != null && var1.J() || var2 != null && var2.Q();
   }

   @Override
   public void onTick() {
      this.U.v();
      if (this.Z() && !sy.L(this)) {
         int var1 = mc.player.tickCount;
         if (this.s != 0) {
            this.v(var1);
         } else if (mc.player.onGround()) {
            this.R = (boolean)0;
            if (this.d) {
               int var13 = var1 - this.r;
               byte var10 = 10;
               int var9 = var13;
               if ((-Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
                  this.d = (boolean)0;
               }
            }

            if (this.f.getValue() && !this.d) {
               this.U.T();
               this.r = var1;
               this.d = (boolean)1;
               _q.c("ElytraHelper", "Relaunch jump through the jump key; waiting {} ticks", sy.p(this.N.getValue()));
            }
         } else {
            if (this.d && !this.f.getValue()) {
               this.d = (boolean)0;
            }

            if (this.d) {
               int var12 = var1 - this.r;
               int var6 = sy.p(this.N.getValue());
               int var5 = var12;
               if (Integer.compare(var5, var6) >>> 31 == 0) {
                  this.R(this.a.getValue());
                  this.d = (boolean)0;
                  this.R = (boolean)1;
               }
            } else if (this.J.getValue() && !mc.player.isFallFlying() && mc.player.fallDistance > 0.0) {
               this.R(this.C.getValue() && !this.R);
               this.R = (boolean)1;
            } else {
               int var2 = mc.player.isFallFlying() && p4.Y(mc.getWindow().handle(), this.y.getValue()) ? 1 : 0;
               if (var2 != 0) {
                  int var10000 = var1 - this.c;
                  int var8 = Y;
                  int var7 = var10000;
                  if (Integer.compare(var7, var8) >>> 31 == 0) {
                     this.J(() -> this.Z() && mc.player.isFallFlying() && p4.Y(mc.getWindow().handle(), this.y.getValue()));
                     return;
                  }
               }

               if (this.b.getValue() && this.A()) {
                  int var11 = var1 - this.c;
                  int var4 = sy.p(this.k.getValue());
                  int var3 = var11;
                  if (Integer.compare(var3, var4) >>> 31 == 0) {
                     this.J(() -> this.Z() && this.A());
                  }
               }
            }
         }
      } else {
         _p.e(this);
         this.z();
      }
   }

   private void v(int var1) {
      if (var1 > this.I) {
         _p.e(this);
         this.z();
         _q.c("ElytraHelper", "Takeoff timed out; released jump input");
      } else {
         if (this.s == 2) {
            if (mc.player.onGround()) {
               this.z();
               return;
            }

            this.U.T();
            this.s = 3;
         } else {
            boolean var2 = mc.player.isFallFlying();
            _q.c("ElytraHelper", "Takeoff {}; firework={}", var2 ? "started" : "did not start", this.z && var2);
            if (var2 && this.z) {
               this.J(() -> this.Z() && mc.player.isFallFlying());
            }

            this.s = 0;
            this.z = (boolean)0;
         }
      }
   }

   private void J(BooleanSupplier var1) {
      this.U.O(var1, 4.0, 0, () -> this.c = mc.player.tickCount);
   }

   private void R(boolean var1) {
      this.U.d();
      this.z = var1;
      this.s = 2;
      this.I = mc.player.tickCount + 3;
   }

   private boolean A() {
      if (this.l.getValue() && !mc.player.isFallFlying()) {
         return false;
      }

      LivingEntity var1 = G();
      if (this.q.getValue() && var1 == null) {
         return false;
      }

      double var2 = mc.player.getDeltaMovement().horizontalDistance() * 20.0;
      return var1 == null ? var2 <= 22.0 : !(mc.player.distanceTo(var1) < 5.0F) || !(var2 > var1.getDeltaMovement().horizontalDistance() * 20.0 * 0.9);
   }

   static LivingEntity G() {
      AuraModule var0 = AuraModule.t();
      LivingEntity var1 = var0 == null ? null : var0.R();
      return var1 != null && var1.isAlive() && !var1.isRemoved() && var1.level() == getMc().level ? var1 : null;
   }

   @Override
   public String getStatus() {
      if (this.s != 0) {
         return "Takeoff";
      }

      if (this.d) {
         return "Relaunch";
      }

      LocalPlayer var1 = mc == null ? null : mc.player;
      return var1 != null && var1 == this.O && this.b.getValue() && var1.isFallFlying() && !this.U.T() ? "No rockets" : "";
   }
}
