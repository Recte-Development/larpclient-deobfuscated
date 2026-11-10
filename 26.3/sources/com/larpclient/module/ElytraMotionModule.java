package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.nz;
import com.larpclient.sy;
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
   private static ElytraMotionModule j;
   private final b A = this.addSetting(new b("Distance", "Distance from your eyes to the Kill Aura target", 3.0, 0.1, 6.0, 0.1));
   private final m e = this.addSetting(new m("Swap Chestplate", "Equip a chestplate while holding near the target", true));
   private final m s = this.addSetting(new m("Use Firework", "Use a hotbar firework when returning to flight", true));
   private final m z = this.addSetting(new m("Legit", "Retained reference preference; takeoff uses normal client actions", false));
   private LocalPlayer w;
   private LocalPlayer d;
   private ClientLevel N;
   private ClientLevel T;
   private int K = -1;
   private ItemStack X = ItemStack.EMPTY;
   private ItemStack Y = ItemStack.EMPTY;
   private boolean W;
   private boolean p;
   private final sy R = new sy(this);

   public ElytraMotionModule() {
      super("ElytraMotion", "Stay near your Kill Aura target with elytra", Category.MOVEMENT, -1);
      j = this;
      this.s.a(this.e::getValue);
      this.z.a(() -> false);
   }

   public static ElytraMotionModule n() {
      return j;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.N;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   private boolean F() {
      LivingEntity var1 = ElytraHelperModule.G();
      return var1 != null && mc.player.getEyePosition().distanceTo(var1.position()) < this.A.getValue();
   }

   public boolean Q() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && !sy.L(this)
            && this.x()
            && nz.v()
            && !mc.player.isPassenger()
            && this.F()
            && (this.K >= 0 && this.I() || mc.player.isFallFlying());
   }

   @Override
   public void onEnable() {
      this.W = (boolean)0;
      this.p = (boolean)0;
      this.z();
      this.w = mc.player;
      this.N = mc.level;
   }

   private boolean x() {
      return this.isInGame() && mc.player == this.w && mc.level == this.N;
   }

   private boolean I() {
      return this.isInGame() && mc.player == this.d && mc.level == this.T;
   }

   @Override
   public void onDisable() {
      _p.e(this);
      if (this.M() && this.r()) {
         this.Y();
      } else if (this.K >= 0) {
         _q.R("ElytraMotion", "Cleanup kept current equipment: owned swap no longer verifiable");
      }

      this.W = (boolean)0;
      this.p = (boolean)0;
      this.z();
      this.w = null;
      this.N = null;
   }

   @Override
   public void onTick() {
      if (!this.x()) {
         _p.e(this);
         this.W = (boolean)0;
         this.p = (boolean)0;
         this.z();
      } else if (nz.v()) {
         if (this.K >= 0 && !this.M()) {
            this.z();
            this.W = (boolean)0;
            _q.c("ElytraMotion", "Equipment changed externally; released swap ownership");
         }

         boolean var1 = this.Q();
         if (var1) {
            mc.player.setDeltaMovement(Vec3.ZERO);
            boolean var5 = false;
            byte var4 = this.W;
            int var10 = Integer.signum(var4);
            if ((var10 | -var10) >>> 31 == 0) {
               _q.c("ElytraMotion", "Holding near Aura target");
            }

            this.W = (boolean)1;
            if (this.e.getValue()) {
               boolean var7 = false;
               int var6 = this.K;
               if ((Integer.signum(var6) >>> 31 ^ 1) == 0 && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA) && this.u()) {
                  _p.w(this, this::a, () -> this.Q() && this.e.getValue() && this.u() && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA));
               }
            }
         } else {
            boolean var9 = false;
            byte var8 = this.W;
            int var10000 = Integer.signum(var8);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.W = (boolean)0;
               this.p = (boolean)(this.K >= 0 ? 1 : 0);
               _q.c("ElytraMotion", "Target left hold range; restoring flight");
            }

            if (this.K >= 0 && this.u()) {
               _p.w(this, this::Y, () -> !this.Q() && this.M() && this.u());
            } else if (this.p) {
               boolean var3 = false;
               int var2 = this.K;
               if ((Integer.signum(var2) >>> 31 ^ 1) == 0) {
                  if (!this.a()) {
                     this.p = (boolean)0;
                     return;
                  }

                  _p.w(this, () -> {
                     if (this.a()) {
                        this.R.Z();
                        if (this.s.getValue()) {
                           this.R.H(4.0, 0, () -> {});
                        }

                        this.p = (boolean)0;
                        _q.c("ElytraMotion", "Flight restored; firework={}", this.s.getValue());
                     }
                  }, this::a);
               }
            }
         }
      }
   }

   private boolean a() {
      AirStuckModule var1 = AirStuckModule.Z();
      return this.x()
         && nz.v()
         && _p.g()
         && !mc.player.getAbilities().flying
         && !mc.player.onGround()
         && !mc.player.isInWater()
         && !mc.player.isInLava()
         && !mc.player.isPassenger()
         && !this.F()
         && mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)
         && (var1 == null || !var1.J());
   }

   private boolean u() {
      return this.r() && _p.g();
   }

   private boolean r() {
      return this.x()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isUsingItem()
         && mc.player.containerMenu == mc.player.inventoryMenu
         && mc.player.inventoryMenu.getCarried().isEmpty();
   }

   private void a() {
      int var1 = this.A();
      if (var1 >= 0) {
         ItemStack var2 = mc.player.getItemBySlot(EquipmentSlot.CHEST).copy();
         ItemStack var3 = mc.player.inventoryMenu.getSlot(var1).getItem().copy();
         K(var1);
         if (ItemStack.isSameItemSameComponents(mc.player.getItemBySlot(EquipmentSlot.CHEST), var3)
            && ItemStack.isSameItemSameComponents(mc.player.inventoryMenu.getSlot(var1).getItem(), var2)) {
            this.K = var1;
            this.X = var2;
            this.Y = var3;
            this.d = mc.player;
            this.T = mc.level;
            _q.c("ElytraMotion", "Equipped chestplate from slot {}", var1);
         } else {
            _q.R("ElytraMotion", "Chest swap prediction differed; kept current equipment");
         }
      }
   }

   private int A() {
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

   private boolean M() {
      return this.K >= 0 && this.I() && T(mc.player.getItemBySlot(EquipmentSlot.CHEST), this.Y) && T(mc.player.inventoryMenu.getSlot(this.K).getItem(), this.X);
   }

   private static boolean T(ItemStack var0, ItemStack var1) {
      ItemStack var2 = var0.copy();
      var2.setDamageValue(var1.getDamageValue());
      return ItemStack.matches(var2, var1);
   }

   private void Y() {
      if (this.M() && this.r()) {
         K(this.K);
         if (mc.player.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA)) {
            _q.c("ElytraMotion", "Restored owned elytra");
            this.z();
         } else {
            _q.R("ElytraMotion", "Elytra restore prediction differed; retaining ownership for retry");
         }
      }
   }

   static void K(int var0) {
      Minecraft var1 = getMc();
      int var2 = var1.player.inventoryMenu.containerId;
      var1.gameMode.handleContainerInput(var2, var0, 0, ContainerInput.PICKUP, var1.player);
      var1.gameMode.handleContainerInput(var2, 6, 0, ContainerInput.PICKUP, var1.player);
      var1.gameMode.handleContainerInput(var2, var0, 0, ContainerInput.PICKUP, var1.player);
   }

   private void z() {
      this.K = -1;
      this.X = ItemStack.EMPTY;
      this.Y = ItemStack.EMPTY;
      this.d = null;
      this.T = null;
   }

   @Override
   public String getStatus() {
      return this.W ? "Holding" : (this.p ? "Resume" : "");
   }
}
