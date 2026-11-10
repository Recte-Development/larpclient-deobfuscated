package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q1881b7c262e73011;
import com.larp.obf.qa300483f54d804a3.qd1e8e5ff4ac9c45f;
import com.larpclient.ki;
import com.larpclient.l9;
import com.larpclient.p_;
import java.util.Set;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class HoverTotemModule extends Module {
   private static HoverTotemModule n;
   private final s A = this.addSetting(new s("Hotbar Refill", "After filling the offhand, move hovered totems into the chosen hotbar slot", false));
   private final e p = this.addSetting(new e("Hotbar Slot", "Hotbar slot to refill, replacing its current item when needed", 1.0, 1.0, 9.0, 1.0));

   public HoverTotemModule() {
      super("HoverTotem", "Move hovered totems into your offhand or chosen hotbar slot", Category.COMBAT, -1);
      this.p.P(this.A::getValue);
      n = this;
   }

   public static HoverTotemModule K() {
      return n;
   }

   @Override
   public String getDisplayName() {
      return "Hover Totem";
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onEnable() {
      com.larpclient.h.U("HoverTotem", "Ready for native container hover swaps; AutoTotem has priority");
   }

   @Override
   public void onTick() {
      if (this.isInGame()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && !mc.player.hasInfiniteMaterials()
         && !p_.V()
         && mc.isWindowActive()
         && mc.gui.screen() instanceof AbstractContainerScreen var1
         && !AutoTotemModule.X()) {
         q1881b7c262e73011 var19 = (q1881b7c262e73011)mc.player;
         Input var3 = var19.larpLastSentInput();
         if (var3 != null && l9.u(var19.larpWasSprinting(), var3.forward(), var3.backward(), var3.left(), var3.right(), var3.jump(), var3.shift())) {
            AbstractContainerMenu var4 = var1.getMenu();
            if (var4 == mc.player.containerMenu && var4.stillValid(mc.player) && var4.getCarried().isEmpty() && mc.player.inventoryMenu.getCarried().isEmpty()) {
               Slot var5 = ((qd1e8e5ff4ac9c45f)var1).larpHoveredSlot();
               Slot var6 = q(var4, var5);
               if (var6 != null && var6.isActive() && !var6.isFake() && var6.mayPickup(mc.player)) {
                  int var7 = this.p.getValue().intValue();
                  int var8 = var6.container == mc.player.getInventory() ? var6.getContainerSlot() : -1;
                  int var10000 = var6.index;
                  boolean var10001 = var6.getItem().is(Items.TOTEM_OF_UNDYING);
                  boolean var10003 = mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING);
                  boolean var10004 = this.A.getValue();
                  Inventory var10006 = mc.player.getInventory();
                  byte var16 = 1;
                  int var15 = var7;
                  l9.n var9 = l9.M(var10000, var10001, var8, var10003, var10004, var7, var10006.getItem(var15 - var16).is(Items.TOTEM_OF_UNDYING));
                  if (var9 != null) {
                     int var20 = this.h(var4, var9.button());
                     boolean var18 = false;
                     byte var17 = (byte)var20;
                     var20 = Integer.signum(var17);
                     if ((var20 | -var20) >>> 31 != 0) {
                        ItemStack var10 = mc.player.getInventory().getItem(var9.button());
                        if (!var10.isEmpty()) {
                           if (!var6.mayPlace(var10)) {
                              return;
                           }

                           var20 = var10.getCount();
                           int var14 = var6.getMaxStackSize(var10);
                           int var13 = var20;
                           if ((-Integer.compare(var13, var14) >>> 31 ^ 1) == 0) {
                              return;
                           }
                        }

                        mc.gameMode.handleContainerInput(var4.containerId, var9.menuSlot(), var9.button(), ContainerInput.SWAP, mc.player);
                        Object[] var10002 = new Object[]{var9.menuSlot(), null, null};
                        int var10005 = var9.button();
                        byte var12 = 40;
                        int var11 = var10005;
                        var10005 = Integer.compare(var11, var12);
                        var10002[1] = (var10005 | -var10005) >>> 31 == 0 ? "offhand" : "hotbar " + (var9.button() + 1);
                        var10002[2] = var4.containerId;
                        com.larpclient.h.U("HoverTotem", "Swapped hovered menu slot {} to {} in container {}", var10002);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static Slot q(AbstractContainerMenu var0, Slot var1) {
      if (var1 != null && var1.getItem().is(Items.TOTEM_OF_UNDYING) && !ki.z(var1) && var1.index >= 0 && var1.index < var0.slots.size()) {
         Slot var2 = (Slot)var0.slots.get(var1.index);
         if (var2 != var1 && !(var1 instanceof ki.d)) {
            return null;
         } else {
            return var2.container == var1.container && var2.getContainerSlot() == var1.getContainerSlot() && ItemStack.matches(var2.getItem(), var1.getItem())
               ? var2
               : null;
         }
      } else {
         return null;
      }
   }

   private boolean h(AbstractContainerMenu var1, int var2) {
      if (var2 != 40 && ki.k()) {
         for (Slot var4 : var1.slots) {
            if (var4.container == mc.player.getInventory() && var4.getContainerSlot() == var2) {
               Slot var5 = ki.x(var1, var4);
               return var5.index == var4.index && !ki.z(var5) && ItemStack.matches(var5.getItem(), var4.getItem());
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
