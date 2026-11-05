package com.larpclient.module;

import com.larpclient._m;
import com.larpclient.mt;
import com.larpclient.te;
import java.util.Set;
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
   private static ElytraMotionModule i;
   private final d9 I = this.addSetting(new d9("Distance", "Distance from your eyes to the Kill Aura target", 3.0, 0.1, 6.0, 0.1));
   private final dh P = this.addSetting(new dh("Swap Chestplate", "Equip a chestplate while holding near the target", true));
   private final dh D = this.addSetting(new dh("Use Firework", "Use a hotbar firework when returning to flight", true));
   private final dh c = this.addSetting(new dh("Legit", "Retained reference preference; takeoff uses normal client actions", false));
   private LocalPlayer j;
   private LocalPlayer U;
   private ClientLevel C;
   private ClientLevel G;
   private int g = -1;
   private ItemStack w = ItemStack.EMPTY;
   private ItemStack W = ItemStack.EMPTY;
   private boolean T;
   private boolean x;

   public ElytraMotionModule() {
      super("ElytraMotion", "Stay near your Kill Aura target with elytra", Category.MOVEMENT, -1);
      i = this;
      this.D.H(this.P::P);
      this.c.H(() -> false);
   }

   public static ElytraMotionModule d() {
      return i;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.b;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   private boolean O() {
      LivingEntity var1 = ElytraHelperModule.Y();
      return var1 != null && mc.player.getEyePosition().distanceTo(var1.position()) < this.I.P();
   }

   public boolean z() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.p() && te.i() && !mc.player.isPassenger() && this.O() && (this.g >= 0 && this.S() || mc.player.isFallFlying());
   }

   @Override
   public void onEnable() {
      this.T = (boolean)0;
      this.x = (boolean)0;
      this.p();
      this.j = mc.player;
      this.C = mc.level;
   }

   private boolean p() {
      return this.isInGame() && mc.player == this.j && mc.level == this.C;
   }

   private boolean S() {
      return this.isInGame() && mc.player == this.U && mc.level == this.G;
   }

   @Override
   public void onDisable() {
      _m.w(this);
      if (this.Q() && this.W()) {
         this.e();
      } else if (this.g >= 0) {
         mt.T("ElytraMotion", "Cleanup kept current equipment: owned swap no longer verifiable");
      }

      this.T = (boolean)0;
      this.x = (boolean)0;
      this.p();
      this.j = null;
      this.C = null;
   }

   @Override
   public void onTick() {
      if (!this.p()) {
         _m.w(this);
         this.T = (boolean)0;
         this.x = (boolean)0;
         this.p();
      } else if (te.i()) {
         if (this.g >= 0 && !this.Q()) {
            this.p();
            this.T = (boolean)0;
            mt.U("ElytraMotion", "Equipment changed externally; released swap ownership");
         }

         boolean var1 = this.z();
         if (var1) {
            mc.player.setDeltaMovement(Vec3.ZERO);
            boolean var9 = false;
            byte var8 = this.T;
            int var11 = Integer.signum(var8);
            if ((var11 | -var11) >>> 31 == 0) {
               mt.U("ElytraMotion", "Holding near Aura target");
            }

            this.T = (boolean)1;
            if (this.P.P() && this.g < 0 && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && this.D()) {
               _m.F(this, this::l, () -> this.z() && this.P.P() && this.D() && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA));
            }
         } else {
            boolean var5 = false;
            byte var4 = this.T;
            int var10000 = Integer.signum(var4);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.T = (boolean)0;
               boolean var7 = false;
               int var6 = this.g;
               this.x = (boolean)(Integer.signum(var6) >>> 31 == 0 ? 1 : 0);
               mt.U("ElytraMotion", "Target left hold range; restoring flight");
            }

            if (this.g >= 0 && this.D()) {
               _m.F(this, this::e, () -> !this.z() && this.Q() && this.D());
            } else {
               boolean var3 = false;
               byte var2 = this.x;
               var10000 = Integer.signum(var2);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && this.g < 0) {
                  if (!this.q()) {
                     this.x = (boolean)0;
                     return;
                  }

                  _m.F(this, () -> {
                     if (this.q()) {
                        ElytraHelperModule.q();
                        if (this.D.P()) {
                           ElytraHelperModule.E(() -> {});
                        }

                        this.x = (boolean)0;
                        mt.U("ElytraMotion", "Flight restored; firework={}", this.D.P());
                     }
                  }, this::q);
               }
            }
         }
      }
   }

   private boolean q() {
      AirStuckModule var1 = AirStuckModule.X();
      return this.p()
         && te.i()
         && _m.z()
         && !mc.player.getAbilities().flying
         && !mc.player.onGround()
         && !mc.player.isInWater()
         && !mc.player.isInLava()
         && !mc.player.isPassenger()
         && !this.O()
         && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && (var1 == null || !var1.U());
   }

   private boolean D() {
      return this.W() && _m.z();
   }

   private boolean W() {
      return this.p()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isUsingItem()
         && mc.player.containerMenu == mc.player.inventoryMenu
         && mc.player.inventoryMenu.getCarried().isEmpty();
   }

   private void l() {
      int var1 = this.J();
      if (var1 >= 0) {
         ItemStack var2 = mc.player.getItemBySlot(EquipmentSlot.CHEST).copy();
         ItemStack var3 = mc.player.inventoryMenu.getSlot(var1).getItem().copy();
         this.T(var1);
         if (ItemStack.isSameItemSameComponents(mc.player.getItemBySlot(EquipmentSlot.CHEST), var3)
            && ItemStack.isSameItemSameComponents(mc.player.inventoryMenu.getSlot(var1).getItem(), var2)) {
            this.g = var1;
            this.w = var2;
            this.W = var3;
            this.U = mc.player;
            this.G = mc.level;
            mt.U("ElytraMotion", "Equipped chestplate from slot {}", var1);
         } else {
            mt.T("ElytraMotion", "Chest swap prediction differed; kept current equipment");
         }
      }
   }

   private int J() {
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

   private boolean Q() {
      return this.g >= 0 && this.S() && r(mc.player.getItemBySlot(EquipmentSlot.CHEST), this.W) && r(mc.player.inventoryMenu.getSlot(this.g).getItem(), this.w);
   }

   private static boolean r(ItemStack var0, ItemStack var1) {
      ItemStack var2 = var0.copy();
      var2.setDamageValue(var1.getDamageValue());
      return ItemStack.matches(var2, var1);
   }

   private void e() {
      if (this.Q() && this.W()) {
         this.T(this.g);
         if (mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            mt.U("ElytraMotion", "Restored owned elytra");
            this.p();
         } else {
            mt.T("ElytraMotion", "Elytra restore prediction differed; retaining ownership for retry");
         }
      }
   }

   private void T(int var1) {
      int var2 = mc.player.inventoryMenu.containerId;
      mc.gameMode.handleContainerInput(var2, var1, 0, ContainerInput.PICKUP, mc.player);
      mc.gameMode.handleContainerInput(var2, 6, 0, ContainerInput.PICKUP, mc.player);
      mc.gameMode.handleContainerInput(var2, var1, 0, ContainerInput.PICKUP, mc.player);
   }

   private void p() {
      this.g = -1;
      this.w = ItemStack.EMPTY;
      this.W = ItemStack.EMPTY;
      this.U = null;
      this.G = null;
   }

   @Override
   public String getStatus() {
      return this.T ? "Holding" : (this.x ? "Resume" : "");
   }
}
