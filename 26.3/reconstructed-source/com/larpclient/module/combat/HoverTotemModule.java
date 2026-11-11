package com.larpclient.module.combat;

import com.larpclient.core.ViewController;
import com.larpclient.hud.VisualInventory;
import com.larpclient.mixin.AbstractContainerScreenMixin;
import com.larpclient.mixin.LocalPlayerMixin_4;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.inventory.HotbarSwapPlanner;
import com.larpclient.util.misc.LarpLog;
import java.util.Set;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class HoverTotemModule extends Module {
   private static HoverTotemModule instance;
   private final BooleanSetting hotbarRefill = this.addSetting(
      new BooleanSetting("Hotbar Refill", "After filling the offhand, move hovered totems into the chosen hotbar slot", false)
   );
   private final NumberSetting hotbarSlot = this.addSetting(
      new NumberSetting("Hotbar Slot", "Hotbar slot to refill, replacing its current item when needed", 1.0, 1.0, 9.0, 1.0)
   );

   public HoverTotemModule() {
      super("HoverTotem", "Move hovered totems into your offhand or chosen hotbar slot", Category.COMBAT, -1);
      this.hotbarSlot.visibleWhen(this.hotbarRefill::getValue);
      instance = this;
   }

   public static HoverTotemModule getInstance() {
      return instance;
   }

   @Override
   public String getDisplayName() {
      return "Hover Totem";
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onEnable() {
      LarpLog.debug("HoverTotem", "Ready for native container hover swaps; AutoTotem has priority");
   }

   @Override
   public void onTick() {
      if (this.isInGame()
         && minecraft.gameMode != null
         && minecraft.player.isAlive()
         && !minecraft.player.isSpectator()
         && !minecraft.player.hasInfiniteMaterials()
         && !ViewController.isFreecam()
         && minecraft.isWindowActive()
         && minecraft.gui.screen() instanceof AbstractContainerScreen var1
         && !AutoTotemModule.hasPendingSwap()) {
         LocalPlayerMixin_4 var19 = (LocalPlayerMixin_4)minecraft.player;
         Input var3 = var19.larpLastSentInput();
         if (var3 != null
            && HotbarSwapPlanner.noModifiersHeld(
               var19.larpWasSprinting(), var3.forward(), var3.backward(), var3.left(), var3.right(), var3.jump(), var3.shift()
            )) {
            AbstractContainerMenu var4 = var1.getMenu();
            if (var4 == minecraft.player.containerMenu) {
               int var10000 = var4.stillValid(minecraft.player);
               boolean var18 = false;
               byte var17 = (byte)var10000;
               var10000 = Integer.signum(var17);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && var4.getCarried().isEmpty() && minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  Slot var5 = ((AbstractContainerScreenMixin)var1).larpHoveredSlot();
                  Slot var6 = resolveHoveredTotemSlot(var4, var5);
                  if (var6 != null && var6.isActive() && !var6.isFake() && var6.mayPickup(minecraft.player)) {
                     int var7 = this.hotbarSlot.getValue().intValue();
                     int var8 = var6.container == minecraft.player.getInventory() ? var6.getContainerSlot() : -1;
                     HotbarSwapPlanner.SlotSwap var9 = HotbarSwapPlanner.planSwap(
                        var6.index,
                        var6.getItem().is(Items.TOTEM_OF_UNDYING),
                        var8,
                        minecraft.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING),
                        this.hotbarRefill.getValue(),
                        var7,
                        minecraft.player.getInventory().getItem(var7 - 1).is(Items.TOTEM_OF_UNDYING)
                     );
                     if (var9 != null && this.isHotbarSlotSwappable(var4, var9.button())) {
                        ItemStack var10 = minecraft.player.getInventory().getItem(var9.button());
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

                        minecraft.gameMode.handleContainerInput(var4.containerId, var9.menuSlot(), var9.button(), ContainerInput.SWAP, minecraft.player);
                        Object[] var10002 = new Object[]{var9.menuSlot(), null, null};
                        int var10005 = var9.button();
                        byte var16 = 40;
                        int var15 = var10005;
                        var10005 = Integer.compare(var15, var16);
                        var10002[1] = (var10005 | -var10005) >>> 31 == 0 ? "offhand" : "hotbar " + (var9.button() + 1);
                        var10002[2] = var4.containerId;
                        LarpLog.debug("HoverTotem", "Swapped hovered menu slot {} to {} in container {}", var10002);
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

   private static Slot resolveHoveredTotemSlot(AbstractContainerMenu var0, Slot var1) {
      if (var1 != null && var1.getItem().is(Items.TOTEM_OF_UNDYING) && !VisualInventory.isVirtualGap(var1) && var1.index >= 0 && var1.index < var0.slots.size()
         )
       {
         Slot var2 = (Slot)var0.slots.get(var1.index);
         if (var2 != var1 && !(var1 instanceof VisualInventory.GapSlot)) {
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

   private boolean isHotbarSlotSwappable(AbstractContainerMenu var1, int var2) {
      if (var2 != 40 && VisualInventory.isActive()) {
         for (Slot var4 : var1.slots) {
            if (var4.container == minecraft.player.getInventory() && var4.getContainerSlot() == var2) {
               Slot var5 = VisualInventory.wrapSlot(var1, var4);
               return var5.index == var4.index && !VisualInventory.isVirtualGap(var5) && ItemStack.matches(var5.getItem(), var4.getItem());
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
