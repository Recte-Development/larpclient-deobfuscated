package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q2013b2ade2f9112e;
import com.larp.obf.q41a5851bcf8e876a.q71cffb54eb30bf94;
import com.larpclient._8;
import com.larpclient._q;
import com.larpclient.d9;
import com.larpclient.ij;
import java.util.Set;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class HoverTotemModule extends Module {
   private static HoverTotemModule p;
   private final m F = this.addSetting(new m("Hotbar Refill", "After filling the offhand, move hovered totems into the chosen hotbar slot", false));
   private final b X = this.addSetting(new b("Hotbar Slot", "Hotbar slot to refill, replacing its current item when needed", 1.0, 1.0, 9.0, 1.0));

   public HoverTotemModule() {
      super("HoverTotem", "Move hovered totems into your offhand or chosen hotbar slot", Category.COMBAT, -1);
      this.X.a(this.F::getValue);
      p = this;
   }

   public static HoverTotemModule m() {
      return p;
   }

   @Override
   public String getDisplayName() {
      return "Hover Totem";
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public void onEnable() {
      _q.c("HoverTotem", "Ready for native container hover swaps; AutoTotem has priority");
   }

   @Override
   public void onTick() {
      if (this.isInGame()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && !mc.player.hasInfiniteMaterials()
         && !ij.y()
         && mc.isWindowActive()
         && mc.gui.screen() instanceof AbstractContainerScreen var1
         && !AutoTotemModule.k()) {
         q71cffb54eb30bf94 var19 = (q71cffb54eb30bf94)mc.player;
         Input var3 = var19.larpLastSentInput();
         if (var3 != null && d9.F(var19.larpWasSprinting(), var3.forward(), var3.backward(), var3.left(), var3.right(), var3.jump(), var3.shift())) {
            AbstractContainerMenu var4 = var1.getMenu();
            if (var4 == mc.player.containerMenu) {
               int var10000 = var4.stillValid(mc.player);
               boolean var18 = false;
               byte var17 = (byte)var10000;
               var10000 = Integer.signum(var17);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && var4.getCarried().isEmpty() && mc.player.inventoryMenu.getCarried().isEmpty()) {
                  Slot var5 = ((q2013b2ade2f9112e)var1).larpHoveredSlot();
                  Slot var6 = g(var4, var5);
                  if (var6 != null && var6.isActive() && !var6.isFake() && var6.mayPickup(mc.player)) {
                     int var7 = this.X.getValue().intValue();
                     int var8 = var6.container == mc.player.getInventory() ? var6.getContainerSlot() : -1;
                     d9.h var9 = d9.R(
                        var6.index,
                        var6.getItem().is(Items.TOTEM_OF_UNDYING),
                        var8,
                        mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING),
                        this.F.getValue(),
                        var7,
                        mc.player.getInventory().getItem(var7 - 1).is(Items.TOTEM_OF_UNDYING)
                     );
                     if (var9 != null && this.O(var4, var9.button())) {
                        ItemStack var10 = mc.player.getInventory().getItem(var9.button());
                        int var21 = var10.isEmpty();
                        boolean var12 = false;
                        byte var11 = (byte)var21;
                        var21 = Integer.signum(var11);
                        if ((var21 | -var21) >>> 31 == 0) {
                           if (!var6.mayPlace(var10)) {
                              return;
                           }

                           var21 = var10.getCount();
                           int var14 = var6.getMaxStackSize(var10);
                           int var13 = var21;
                           if ((-Integer.compare(var13, var14) >>> 31 ^ 1) == 0) {
                              return;
                           }
                        }

                        mc.gameMode.handleContainerInput(var4.containerId, var9.menuSlot(), var9.button(), ContainerInput.SWAP, mc.player);
                        Object[] var10002 = new Object[]{var9.menuSlot(), null, null};
                        int var10005 = var9.button();
                        byte var16 = 40;
                        int var15 = var10005;
                        var10005 = Integer.compare(var15, var16);
                        var10002[1] = (var10005 | -var10005) >>> 31 == 0 ? "offhand" : "hotbar " + (var9.button() + 1);
                        var10002[2] = var4.containerId;
                        _q.c("HoverTotem", "Swapped hovered menu slot {} to {} in container {}", var10002);
                        return;
                     }

                     return;
                  }

                  return;
               }
            }
         }
      }
   }

   private static Slot g(AbstractContainerMenu var0, Slot var1) {
      if (var1 != null && var1.getItem().is(Items.TOTEM_OF_UNDYING) && !_8.z(var1) && var1.index >= 0 && var1.index < var0.slots.size()) {
         Slot var2 = (Slot)var0.slots.get(var1.index);
         if (var2 != var1 && !(var1 instanceof _8.k)) {
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

   private boolean O(AbstractContainerMenu var1, int var2) {
      if (var2 != 40 && _8.S()) {
         for (Slot var4 : var1.slots) {
            if (var4.container == mc.player.getInventory() && var4.getContainerSlot() == var2) {
               Slot var5 = _8.W(var1, var4);
               return var5.index == var4.index && !_8.z(var5) && ItemStack.matches(var5.getItem(), var4.getItem());
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
