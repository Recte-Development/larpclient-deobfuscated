package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.hp;
import com.larpclient.pe;
import com.larpclient.r4;
import com.larpclient.yu;
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
   private final la D = this.addSetting(new la("Firework Key", "Hold this key to use hotbar fireworks while gliding", -1));
   private final ln Z = this.addSetting(new ln("Auto Start", "Start gliding when falling with an elytra equipped", true));
   private final ln V = this.addSetting(new ln("Fast Start", "Use a firework on the first automatic takeoff", true));
   private final ln y = this.addSetting(new ln("Combat Relaunch", "Jump and relaunch when landing with an elytra equipped", true));
   private final lc d = this.addSetting(new lc("Relaunch Delay", "Delay after the relaunch jump in milliseconds", 175.0, 0.0, 1000.0, 25.0));
   private final ln K = this.addSetting(new ln("Relaunch Firework", "Use a firework after the relaunch", true));
   private final ln E = this.addSetting(new ln("Auto Firework", "Use hotbar fireworks when flight speed drops", false));
   private final lc h = this.addSetting(new lc("Delay", "Minimum time between fireworks in milliseconds", 650.0, 100.0, 2000.0, 25.0));
   private final ln N = this.addSetting(new ln("Only With Target", "Automatic fireworks require the current Kill Aura target", false));
   private final ln a = this.addSetting(new ln("Only While Gliding", "Automatic fireworks require active gliding", true));
   private LocalPlayer H;
   private ClientLevel w;
   private KeyMapping Q;
   private boolean X;
   private long p;
   private long l;
   private static boolean n;
   private int s;
   private int C;
   private boolean O;
   private boolean J;
   private boolean u;
   private boolean F;

   public ElytraHelperModule() {
      super("ElytraHelper", "Automate elytra takeoff and fireworks", Category.MOVEMENT, -1);
      this.V.x(this.Z::getValue);
      this.d.x(this.y::getValue);
      this.K.x(this.y::getValue);
      this.h.x(this.E::getValue);
      this.N.x(this.E::getValue);
      this.a.x(this.E::getValue);
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public void onEnable() {
      this.b();
      this.H = mc.player;
      this.w = mc.level;
      this.p = System.nanoTime();
   }

   @Override
   public void onDisable() {
      d8.K(this);
      this.b();
      this.H = null;
      this.w = null;
   }

   private void b() {
      this.z();
      this.s = 0;
      this.O = (boolean)0;
      this.J = (boolean)0;
      this.u = (boolean)0;
   }

   private void z() {
      if (this.F && this.Q != null && this.Q.isDown() == this.X) {
         this.Q.setDown(yu.X(this.Q));
      }

      this.F = (boolean)0;
      this.Q = null;
   }

   private boolean o() {
      return this.isInGame()
         && mc.player == this.H
         && mc.level == this.w
         && yu.r()
         && d8.a()
         && !mc.player.getAbilities().flying
         && !mc.player.isPassenger()
         && !mc.player.isInWater()
         && !mc.player.isInLava()
         && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && !this.K();
   }

   private boolean K() {
      AirStuckModule var1 = AirStuckModule.H();
      ElytraMotionModule var2 = ElytraMotionModule.S();
      return var1 != null && var1.D() || var2 != null && var2.m();
   }

   @Override
   public void onTick() {
      if (!this.o()) {
         d8.K(this);
         this.b();
      } else {
         long var1 = System.nanoTime();
         boolean var7 = false;
         int var6 = this.s;
         int var10000 = Integer.signum(var6);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            int var9 = this.C;
            int var8 = mc.player.tickCount;
            if ((-Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
               d8.K(this);
               this.b();
               r4.g("ElytraHelper", "Takeoff timed out; released jump input");
            } else {
               byte var5 = 1;
               int var4 = this.s;
               var10000 = Integer.compare(var4, var5);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  if (mc.player.onGround()) {
                     this.b();
                     return;
                  }

                  d8.A(this, () -> {
                     if (this.s == 1 && this.o() && !mc.player.onGround()) {
                        j();
                        this.E(true);
                        this.s = 2;
                        if (this.u) {
                           k(() -> this.p = System.nanoTime());
                        }

                        r4.g("ElytraHelper", "Takeoff requested; firework={}", this.u);
                     }
                  }, () -> this.s == 1 && this.o() && !mc.player.onGround());
               } else {
                  this.z();
                  this.s = 0;
                  this.u = (boolean)0;
               }
            }
         } else if (mc.player.onGround()) {
            this.O = (boolean)0;
            if (this.y.getValue()) {
               boolean var11 = false;
               byte var10 = this.J;
               var10000 = Integer.signum(var10);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  mc.player.jumpFromGround();
                  this.l = var1;
                  this.J = (boolean)1;
                  r4.g("ElytraHelper", "Relaunch jump; waiting {} ms", this.d.getValue());
               }
            }
         } else {
            if (this.J && !this.y.getValue()) {
               this.J = (boolean)0;
            }

            if (this.J) {
               if (var1 - this.l >= (long)(this.d.getValue() * 1000000.0)) {
                  this.K(this.K.getValue());
                  this.J = (boolean)0;
                  this.O = (boolean)1;
               }
            } else if (this.Z.getValue() && !mc.player.isFallFlying() && mc.player.fallDistance > 0.0) {
               this.K(this.V.getValue() && !this.O);
               this.O = (boolean)1;
            } else {
               int var3 = mc.player.isFallFlying() && pe.A(mc.getWindow().handle(), this.D.getValue()) ? 1 : 0;
               if (var3 != 0 && var1 - this.p >= 180000000L) {
                  A(this, () -> this.o() && mc.player.isFallFlying() && pe.A(mc.getWindow().handle(), this.D.getValue()), () -> this.p = System.nanoTime());
               } else if (this.E.getValue() && this.q() && var1 - this.p >= (long)(this.h.getValue() * 1000000.0)) {
                  A(this, () -> this.o() && this.q(), () -> this.p = System.nanoTime());
               }
            }
         }
      }
   }

   private void E(boolean var1) {
      this.Q = mc.options.keyJump;
      this.Q.setDown(var1);
      this.X = var1;
      this.F = (boolean)1;
   }

   private void K(boolean var1) {
      this.E(false);
      this.u = var1;
      this.s = 1;
      this.C = mc.player.tickCount + 3;
   }

   private boolean q() {
      if (this.a.getValue() && !mc.player.isFallFlying()) {
         return false;
      }

      LivingEntity var1 = V();
      if (this.N.getValue() && var1 == null) {
         return false;
      }

      double var2 = mc.player.getDeltaMovement().horizontalDistance() * 20.0;
      return var1 == null ? var2 <= 22.0 : !(mc.player.distanceTo(var1) < 5.0F) || !(var2 > var1.getDeltaMovement().horizontalDistance() * 20.0 * 0.9);
   }

   static LivingEntity V() {
      AuraModule var0 = AuraModule.G();
      LivingEntity var1 = var0 == null ? null : var0.G();
      return var1 != null && var1.isAlive() && !var1.isRemoved() && var1.level() == getMc().level ? var1 : null;
   }

   static void j() {
      getMc().player.startFallFlying();
      getMc().player.connection.send(new ServerboundPlayerCommandPacket(getMc().player, Action.START_FALL_FLYING));
   }

   static void A(Module var0, BooleanSupplier var1, Runnable var2) {
      d8.A(var0, () -> k(var2), var1);
   }

   static void k(Runnable var0) {
      Minecraft var1 = getMc();
      if (d8.a() && !var1.player.isUsingItem()) {
         Inventory var2 = var1.player.getInventory();
         int var3 = -1;

         for (int var4 = 0; var4 < 9; var4++) {
            if (var2.getItem(var4).is(Items.FIREWORK_ROCKET)) {
               var3 = var4;
               break;
            }
         }

         if (var3 < 0) {
            if (!n) {
               r4.g("ElytraHelper", "Firework unavailable in hotbar");
            }

            n = (boolean)1;
         } else {
            n = (boolean)0;
            int var9 = var2.getSelectedSlot();
            var2.setSelectedSlot(var3);

            try {
               InteractionResult var5 = var1.gameMode.useItem(var1.player, InteractionHand.MAIN_HAND);
               if (var5.consumesAction()) {
                  hp.Q(var1.player, InteractionHand.MAIN_HAND);
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
      return this.s != 0 ? "Takeoff" : (this.J ? "Relaunch" : "");
   }
}
