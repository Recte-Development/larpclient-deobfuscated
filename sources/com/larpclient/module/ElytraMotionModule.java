package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.r4;
import com.larpclient.yu;
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
   private static ElytraMotionModule s;
   private final lc O = this.addSetting(new lc("Distance", "Distance from your eyes to the Kill Aura target", 3.0, 0.1, 6.0, 0.1));
   private final ln J = this.addSetting(new ln("Swap Chestplate", "Equip a chestplate while holding near the target", true));
   private final ln G = this.addSetting(new ln("Use Firework", "Use a hotbar firework when returning to flight", true));
   private final ln E = this.addSetting(new ln("Legit", "Retained reference preference; takeoff uses normal client actions", false));
   private LocalPlayer H;
   private LocalPlayer w;
   private ClientLevel B;
   private ClientLevel T;
   private int v = -1;
   private ItemStack d = ItemStack.EMPTY;
   private ItemStack S = ItemStack.EMPTY;
   private boolean U;
   private boolean I;

   public ElytraMotionModule() {
      super("ElytraMotion", "Stay near your Kill Aura target with elytra", Category.MOVEMENT, -1);
      s = this;
      this.G.x(this.J::getValue);
      this.E.x(() -> false);
   }

   public static ElytraMotionModule S() {
      return s;
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.N;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   private boolean v() {
      LivingEntity var1 = ElytraHelperModule.V();
      return var1 != null && mc.player.getEyePosition().distanceTo(var1.position()) < this.O.getValue();
   }

   public boolean m() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.V() && yu.r() && !mc.player.isPassenger() && this.v() && (this.v >= 0 && this.S() || mc.player.isFallFlying());
   }

   @Override
   public void onEnable() {
      this.U = (boolean)0;
      this.I = (boolean)0;
      this.H();
      this.H = mc.player;
      this.B = mc.level;
   }

   private boolean V() {
      return this.isInGame() && mc.player == this.H && mc.level == this.B;
   }

   private boolean S() {
      return this.isInGame() && mc.player == this.w && mc.level == this.T;
   }

   @Override
   public void onDisable() {
      d8.K(this);
      if (this.F() && this.t()) {
         this.T();
      } else if (this.v >= 0) {
         r4.m("ElytraMotion", "Cleanup kept current equipment: owned swap no longer verifiable");
      }

      this.U = (boolean)0;
      this.I = (boolean)0;
      this.H();
      this.H = null;
      this.B = null;
   }

   @Override
   public void onTick() {
      if (!this.V()) {
         d8.K(this);
         this.U = (boolean)0;
         this.I = (boolean)0;
         this.H();
      } else if (yu.r()) {
         if (this.v >= 0 && !this.F()) {
            this.H();
            this.U = (boolean)0;
            r4.g("ElytraMotion", "Equipment changed externally; released swap ownership");
         }

         boolean var1 = this.m();
         if (var1) {
            mc.player.setDeltaMovement(Vec3.ZERO);
            if (!this.U) {
               r4.g("ElytraMotion", "Holding near Aura target");
            }

            this.U = (boolean)1;
            if (this.J.getValue()) {
               boolean var3 = false;
               int var2 = this.v;
               if ((Integer.signum(var2) >>> 31 ^ 1) == 0 && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && this.n()) {
                  d8.A(this, this::w, () -> this.m() && this.J.getValue() && this.n() && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA));
               }
            }
         } else {
            boolean var5 = false;
            byte var4 = this.U;
            int var10000 = Integer.signum(var4);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.U = (boolean)0;
               boolean var7 = false;
               int var6 = this.v;
               this.I = (boolean)(Integer.signum(var6) >>> 31 == 0 ? 1 : 0);
               r4.g("ElytraMotion", "Target left hold range; restoring flight");
            }

            boolean var9 = false;
            int var8 = this.v;
            if (Integer.signum(var8) >>> 31 == 0 && this.n()) {
               d8.A(this, this::T, () -> !this.m() && this.F() && this.n());
            } else if (this.I && this.v < 0) {
               if (!this.q()) {
                  this.I = (boolean)0;
                  return;
               }

               d8.A(this, () -> {
                  if (this.q()) {
                     ElytraHelperModule.j();
                     if (this.G.getValue()) {
                        ElytraHelperModule.k(() -> {});
                     }

                     this.I = (boolean)0;
                     r4.g("ElytraMotion", "Flight restored; firework={}", this.G.getValue());
                  }
               }, this::q);
            }
         }
      }
   }

   private boolean q() {
      AirStuckModule var1 = AirStuckModule.H();
      return this.V()
         && yu.r()
         && d8.a()
         && !mc.player.getAbilities().flying
         && !mc.player.onGround()
         && !mc.player.isInWater()
         && !mc.player.isInLava()
         && !mc.player.isPassenger()
         && !this.v()
         && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && (var1 == null || !var1.D());
   }

   private boolean n() {
      return this.t() && d8.a();
   }

   private boolean t() {
      return this.V()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isUsingItem()
         && mc.player.containerMenu == mc.player.inventoryMenu
         && mc.player.inventoryMenu.getCarried().isEmpty();
   }

   private void w() {
      int var1 = this.h();
      if (var1 >= 0) {
         ItemStack var2 = mc.player.getItemBySlot(EquipmentSlot.CHEST).copy();
         ItemStack var3 = mc.player.inventoryMenu.getSlot(var1).getItem().copy();
         this.f(var1);
         if (ItemStack.isSameItemSameComponents(mc.player.getItemBySlot(EquipmentSlot.CHEST), var3)
            && ItemStack.isSameItemSameComponents(mc.player.inventoryMenu.getSlot(var1).getItem(), var2)) {
            this.v = var1;
            this.d = var2;
            this.S = var3;
            this.w = mc.player;
            this.T = mc.level;
            r4.g("ElytraMotion", "Equipped chestplate from slot {}", var1);
         } else {
            r4.m("ElytraMotion", "Chest swap prediction differed; kept current equipment");
         }
      }
   }

   private int h() {
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

   private boolean F() {
      return this.v >= 0 && this.S() && n(mc.player.getItemBySlot(EquipmentSlot.CHEST), this.S) && n(mc.player.inventoryMenu.getSlot(this.v).getItem(), this.d);
   }

   private static boolean n(ItemStack var0, ItemStack var1) {
      ItemStack var2 = var0.copy();
      var2.setDamageValue(var1.getDamageValue());
      return ItemStack.matches(var2, var1);
   }

   private void T() {
      if (this.F() && this.t()) {
         this.f(this.v);
         if (mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            r4.g("ElytraMotion", "Restored owned elytra");
            this.H();
         } else {
            r4.m("ElytraMotion", "Elytra restore prediction differed; retaining ownership for retry");
         }
      }
   }

   private void f(int var1) {
      int var2 = mc.player.inventoryMenu.containerId;
      mc.gameMode.handleContainerInput(var2, var1, 0, ContainerInput.PICKUP, mc.player);
      mc.gameMode.handleContainerInput(var2, 6, 0, ContainerInput.PICKUP, mc.player);
      mc.gameMode.handleContainerInput(var2, var1, 0, ContainerInput.PICKUP, mc.player);
   }

   private void H() {
      this.v = -1;
      this.d = ItemStack.EMPTY;
      this.S = ItemStack.EMPTY;
      this.w = null;
      this.T = null;
   }

   @Override
   public String getStatus() {
      return this.U ? "Holding" : (this.I ? "Resume" : "");
   }
}
