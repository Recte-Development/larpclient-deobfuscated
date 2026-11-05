package com.larpclient.module;

import com.larpclient._8;
import com.larpclient._m;
import com.larpclient.cc;
import com.larpclient.mt;
import com.larpclient.te;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;

public final class ElytraHelperModule extends Module {
   private final dv D = this.addSetting(new dv("Firework Key", "Hold this key to use hotbar fireworks while gliding", -1));
   private final dh A = this.addSetting(new dh("Auto Start", "Start gliding when falling with an elytra equipped", true));
   private final dh Q = this.addSetting(new dh("Fast Start", "Use a firework on the first automatic takeoff", true));
   private final dh e = this.addSetting(new dh("Combat Relaunch", "Jump and relaunch when landing with an elytra equipped", true));
   private final d9 P = this.addSetting(new d9("Relaunch Delay", "Delay after the relaunch jump in milliseconds", 175.0, 0.0, 1000.0, 25.0));
   private final dh M = this.addSetting(new dh("Relaunch Firework", "Use a firework after the relaunch", true));
   private final dh G = this.addSetting(new dh("Auto Firework", "Use hotbar fireworks when flight speed drops", false));
   private final d9 N = this.addSetting(new d9("Delay", "Minimum time between fireworks in milliseconds", 650.0, 100.0, 2000.0, 25.0));
   private final dh x = this.addSetting(new dh("Only With Target", "Automatic fireworks require the current Kill Aura target", false));
   private final dh C = this.addSetting(new dh("Only While Gliding", "Automatic fireworks require active gliding", true));
   private LocalPlayer V;
   private ClientLevel k;
   private KeyMapping s;
   private boolean n;
   private long a;
   private long r;
   private static boolean S;
   private int y;
   private int i;
   private boolean t;
   private boolean E;
   private boolean q;
   private boolean K;

   public ElytraHelperModule() {
      super("ElytraHelper", "Automate elytra takeoff and fireworks", Category.MOVEMENT, -1);
      this.Q.H(this.A::P);
      this.P.H(this.e::P);
      this.M.H(this.e::P);
      this.N.H(this.G::P);
      this.x.H(this.G::P);
      this.C.H(this.G::P);
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public void onEnable() {
      this.t();
      this.V = mc.player;
      this.k = mc.level;
      this.a = System.nanoTime();
   }

   @Override
   public void onDisable() {
      _m.w(this);
      this.t();
      this.V = null;
      this.k = null;
   }

   private void t() {
      this.k();
      this.y = 0;
      this.t = (boolean)0;
      this.E = (boolean)0;
      this.q = (boolean)0;
   }

   private void k() {
      if (this.K && this.s != null && this.s.isDown() == this.n) {
         this.s.setDown(te.e(this.s));
      }

      this.K = (boolean)0;
      this.s = null;
   }

   private boolean M() {
      return this.isInGame()
         && mc.player == this.V
         && mc.level == this.k
         && te.i()
         && _m.z()
         && !mc.player.getAbilities().flying
         && !mc.player.isPassenger()
         && !mc.player.isInWater()
         && !mc.player.isInLava()
         && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && !this.S();
   }

   private boolean S() {
      AirStuckModule var1 = AirStuckModule.X();
      ElytraMotionModule var2 = ElytraMotionModule.d();
      return var1 != null && var1.U() || var2 != null && var2.z();
   }

   @Override
   public void onTick() {
      if (!this.M()) {
         _m.w(this);
         this.t();
      } else {
         long var1 = System.nanoTime();
         if (this.y != 0) {
            int var9 = this.i;
            int var8 = mc.player.tickCount;
            if ((-Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
               _m.w(this);
               this.t();
               mt.U("ElytraHelper", "Takeoff timed out; released jump input");
            } else {
               byte var11 = 1;
               int var10 = this.y;
               int var12 = Integer.compare(var10, var11);
               if ((var12 | -var12) >>> 31 == 0) {
                  if (mc.player.onGround()) {
                     this.t();
                     return;
                  }

                  _m.F(this, () -> {
                     if (this.y == 1 && this.M() && !mc.player.onGround()) {
                        q();
                        this.c(true);
                        this.y = 2;
                        if (this.q) {
                           E(() -> this.a = System.nanoTime());
                        }

                        mt.U("ElytraHelper", "Takeoff requested; firework={}", this.q);
                     }
                  }, () -> this.y == 1 && this.M() && !mc.player.onGround());
               } else {
                  this.k();
                  this.y = 0;
                  this.q = (boolean)0;
               }
            }
         } else if (mc.player.onGround()) {
            this.t = (boolean)0;
            if (this.e.P() && !this.E) {
               mc.player.jumpFromGround();
               this.r = var1;
               this.E = (boolean)1;
               mt.U("ElytraHelper", "Relaunch jump; waiting {} ms", this.P.P());
            }
         } else {
            if (this.E && !this.e.P()) {
               this.E = (boolean)0;
            }

            boolean var5 = false;
            byte var4 = this.E;
            int var10000 = Integer.signum(var4);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               if (var1 - this.r >= (long)(this.P.P() * 1000000.0)) {
                  this.v(this.M.P());
                  this.E = (boolean)0;
                  this.t = (boolean)1;
               }
            } else if (this.A.P() && !mc.player.isFallFlying() && mc.player.fallDistance > 0.0) {
               int var13;
               label69: {
                  if (this.Q.P()) {
                     boolean var7 = false;
                     byte var6 = this.t;
                     var13 = Integer.signum(var6);
                     if ((var13 | -var13) >>> 31 == 0) {
                        var13 = 1;
                        break label69;
                     }
                  }

                  var13 = 0;
               }

               this.v((boolean)var13);
               this.t = (boolean)1;
            } else {
               int var3 = mc.player.isFallFlying() && cc.N(mc.getWindow().handle(), this.D.P()) ? 1 : 0;
               if (var3 != 0 && var1 - this.a >= 180000000L) {
                  r(this, () -> this.M() && mc.player.isFallFlying() && cc.N(mc.getWindow().handle(), this.D.P()), () -> this.a = System.nanoTime());
               } else if (this.G.P() && this.t() && var1 - this.a >= (long)(this.N.P() * 1000000.0)) {
                  r(this, () -> this.M() && this.t(), () -> this.a = System.nanoTime());
               }
            }
         }
      }
   }

   private void c(boolean var1) {
      this.s = mc.options.keyJump;
      this.s.setDown(var1);
      this.n = var1;
      this.K = (boolean)1;
   }

   private void v(boolean var1) {
      this.c(false);
      this.q = var1;
      this.y = 1;
      this.i = mc.player.tickCount + 3;
   }

   private boolean t() {
      if (this.C.P() && !mc.player.isFallFlying()) {
         return false;
      }

      LivingEntity var1 = Y();
      if (this.x.P() && var1 == null) {
         return false;
      }

      double var2 = mc.player.getDeltaMovement().horizontalDistance() * 20.0;
      return var1 == null ? var2 <= 22.0 : !(mc.player.distanceTo(var1) < 5.0F) || !(var2 > var1.getDeltaMovement().horizontalDistance() * 20.0 * 0.9);
   }

   static LivingEntity Y() {
      AuraModule var0 = AuraModule.S();
      LivingEntity var1 = var0 == null ? null : var0.l();
      return var1 != null && var1.isAlive() && !var1.isRemoved() && var1.level() == getMc().level ? var1 : null;
   }

   static void q() {
      getMc().player.startFallFlying();
      getMc().player.connection.send(new ServerboundPlayerCommandPacket(getMc().player, Action.START_FALL_FLYING));
   }

   static void r(Module var0, BooleanSupplier var1, Runnable var2) {
      _m.F(var0, () -> E(var2), var1);
   }

   static void E(Runnable var0) {
      Minecraft var1 = getMc();
      if (_m.z() && !var1.player.isUsingItem()) {
         Inventory var2 = var1.player.getInventory();
         int var3 = -1;

         for (int var4 = 0; var4 < 9; var4++) {
            if (var2.getItem(var4).is(Items.FIREWORK_ROCKET)) {
               var3 = var4;
               break;
            }
         }

         if (var3 < 0) {
            if (!S) {
               mt.U("ElytraHelper", "Firework unavailable in hotbar");
            }

            S = (boolean)1;
         } else {
            S = (boolean)0;
            int var9 = var2.getSelectedSlot();
            var2.setSelectedSlot(var3);

            try {
               InteractionResult var5 = var1.gameMode.useItem(var1.player, InteractionHand.MAIN_HAND);
               if (var5.consumesAction()) {
                  _8.G(var1.player, InteractionHand.MAIN_HAND);
                  var0.run();
               }
            } finally {
               var2.setSelectedSlot(var9);
            }
         }
      }
   }

   @Override
   public String getStatus() {
      return this.y != 0 ? "Takeoff" : (this.E ? "Relaunch" : "");
   }
}
