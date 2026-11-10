package com.larpclient.module;

import com.larpclient.c5;
import com.larpclient.cu;
import com.larpclient.ta;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public final class ElytraMotionModule extends Module {
   private static ElytraMotionModule r;
   private final e Y = this.addSetting(new e("Distance", "Distance from your eyes to the Kill Aura target", 3.0, 0.1, 6.0, 0.1));
   private final s L = this.addSetting(new s("Swap Chestplate", "Equip a chestplate while holding near the target", true));
   private final s q = this.addSetting(new s("Use Firework", "Use a hotbar firework when returning to flight", true));
   private final s F = this.addSetting(new s("Legit", "Retained reference preference; takeoff uses normal client actions", false));
   private LocalPlayer j;
   private LocalPlayer J;
   private ClientLevel p;
   private ClientLevel g;
   private int d = -1;
   private ItemStack a = ItemStack.EMPTY;
   private ItemStack e = ItemStack.EMPTY;
   private boolean i;
   private boolean m;
   private final cu O = new cu(this);

   public ElytraMotionModule() {
      super("ElytraMotion", "Stay near your Kill Aura target with elytra", Category.MOVEMENT, -1);
      r = this;
      this.q.P(this.L::getValue);
      this.F.P(() -> false);
   }

   public static ElytraMotionModule v() {
      return r;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.j;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   private boolean o() {
      LivingEntity var1 = ElytraHelperModule.R();
      return var1 != null && mc.player.getEyePosition().distanceTo(var1.position()) < this.Y.getValue();
   }

   public boolean K() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && !cu.n(this)
            && this.r()
            && c5.J()
            && !mc.player.isPassenger()
            && this.o()
            && (this.d >= 0 && this.J() || mc.player.isFallFlying());
   }

   @Override
   public void onEnable() {
      this.i = (boolean)0;
      this.m = (boolean)0;
      this.C();
      this.j = mc.player;
      this.p = mc.level;
   }

   private boolean r() {
      return this.isInGame() && mc.player == this.j && mc.level == this.p;
   }

   private boolean J() {
      return this.isInGame() && mc.player == this.J && mc.level == this.g;
   }

   @Override
   public void onDisable() {
      ta.s(this);
      if (this.H() && this.z()) {
         this.T();
      } else if (this.d >= 0) {
         com.larpclient.h.g("ElytraMotion", "Cleanup kept current equipment: owned swap no longer verifiable");
      }

      this.i = (boolean)0;
      this.m = (boolean)0;
      this.C();
      this.j = null;
      this.p = null;
   }

   @Override
   public void onTick() {
      if (!this.r()) {
         ta.s(this);
         this.i = (boolean)0;
         this.m = (boolean)0;
         this.C();
      } else if (c5.J()) {
         if (this.d >= 0 && !this.H()) {
            this.C();
            this.i = (boolean)0;
            com.larpclient.h.U("ElytraMotion", "Equipment changed externally; released swap ownership");
         }

         boolean var1 = this.K();
         if (var1) {
            mc.player.setDeltaMovement(Vec3.ZERO);
            boolean var3 = false;
            byte var2 = this.i;
            int var10 = Integer.signum(var2);
            if ((var10 | -var10) >>> 31 == 0) {
               com.larpclient.h.U("ElytraMotion", "Holding near Aura target");
            }

            this.i = (boolean)1;
            if (this.L.getValue() && this.d < 0 && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && this.i()) {
               ta.F(this, this::H, () -> this.K() && this.L.getValue() && this.i() && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA));
            }
         } else {
            if (this.i) {
               this.i = (boolean)0;
               boolean var9 = false;
               int var8 = this.d;
               this.m = (boolean)(Integer.signum(var8) >>> 31 == 0 ? 1 : 0);
               com.larpclient.h.U("ElytraMotion", "Target left hold range; restoring flight");
            }

            boolean var5 = false;
            int var4 = this.d;
            if (Integer.signum(var4) >>> 31 == 0 && this.i()) {
               ta.F(this, this::T, () -> !this.K() && this.H() && this.i());
            } else {
               boolean var7 = false;
               byte var6 = this.m;
               int var10000 = Integer.signum(var6);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.d < 0) {
                  if (!this.l()) {
                     this.m = (boolean)0;
                     return;
                  }

                  ta.F(this, () -> {
                     if (this.l()) {
                        this.O.I();
                        if (this.q.getValue()) {
                           this.O.K(4.0, 0, () -> {});
                        }

                        this.m = (boolean)0;
                        com.larpclient.h.U("ElytraMotion", "Flight restored; firework={}", this.q.getValue());
                     }
                  }, this::l);
               }
            }
         }
      }
   }

   private boolean l() {
      AirStuckModule var1 = AirStuckModule.T();
      return this.r()
         && c5.J()
         && ta.Y()
         && !mc.player.getAbilities().flying
         && !mc.player.onGround()
         && !mc.player.isInWater()
         && !mc.player.isInLava()
         && !mc.player.isPassenger()
         && !this.o()
         && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && (var1 == null || !var1.I());
   }

   private boolean i() {
      return this.z() && ta.Y();
   }

   private boolean z() {
      return this.r()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isUsingItem()
         && mc.player.containerMenu == mc.player.inventoryMenu
         && mc.player.inventoryMenu.getCarried().isEmpty();
   }

   private void H() {
      int var1 = this.w();
      if (var1 >= 0) {
         ItemStack var2 = mc.player.getItemBySlot(EquipmentSlot.CHEST).copy();
         ItemStack var3 = mc.player.inventoryMenu.getSlot(var1).getItem().copy();
         j(var1);
         if (ItemStack.isSameItemSameComponents(mc.player.getItemBySlot(EquipmentSlot.CHEST), var3)
            && ItemStack.isSameItemSameComponents(mc.player.inventoryMenu.getSlot(var1).getItem(), var2)) {
            this.d = var1;
            this.a = var2;
            this.e = var3;
            this.J = mc.player;
            this.g = mc.level;
            com.larpclient.h.U("ElytraMotion", "Equipped chestplate from slot {}", var1);
         } else {
            com.larpclient.h.g("ElytraMotion", "Chest swap prediction differed; kept current equipment");
         }
      }
   }

   private int w() {
      Item[] var1 = new Item[]{
         Items.NETHERITE_CHESTPLATE,
         Items.DIAMOND_CHESTPLATE,
         Items.IRON_CHESTPLATE,
         Items.GOLDEN_CHESTPLATE,
         Items.CHAINMAIL_CHESTPLATE,
         Items.LEATHER_CHESTPLATE
      };

      for (int var2 = 9; var2 < 45; var2++) {
         for (Item var6 : var1) {
            if (mc.player.inventoryMenu.getSlot(var2).getItem().is(var6)) {
               return var2;
            }
         }
      }

      return -1;
   }

   private boolean H() {
      return this.d >= 0 && this.J() && E(mc.player.getItemBySlot(EquipmentSlot.CHEST), this.e) && E(mc.player.inventoryMenu.getSlot(this.d).getItem(), this.a);
   }

   private static boolean E(ItemStack var0, ItemStack var1) {
      ItemStack var2 = var0.copy();
      var2.setDamageValue(var1.getDamageValue());
      return ItemStack.matches(var2, var1);
   }

   private void T() {
      if (this.H() && this.z()) {
         j(this.d);
         if (mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            com.larpclient.h.U("ElytraMotion", "Restored owned elytra");
            this.C();
         } else {
            com.larpclient.h.g("ElytraMotion", "Elytra restore prediction differed; retaining ownership for retry");
         }
      }
   }

   static void j(int var0) {
      Minecraft var1 = getMc();
      int var2 = var1.player.inventoryMenu.containerId;
      var1.gameMode.handleContainerInput(var2, var0, 0, ContainerInput.PICKUP, var1.player);
      var1.gameMode.handleContainerInput(var2, 6, 0, ContainerInput.PICKUP, var1.player);
      var1.gameMode.handleContainerInput(var2, var0, 0, ContainerInput.PICKUP, var1.player);
   }

   private void C() {
      this.d = -1;
      this.a = ItemStack.EMPTY;
      this.e = ItemStack.EMPTY;
      this.J = null;
      this.g = null;
   }

   @Override
   public String getStatus() {
      return this.i ? "Holding" : (this.m ? "Resume" : "");
   }
}
