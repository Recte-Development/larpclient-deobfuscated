package com.larpclient.mixin;

import com.larpclient.hud.VisualInventory;
import com.larpclient.social.misc.PlayerMessages;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.FakeSpawnerController;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin {
   @Shadow
   private Slot destroyItemSlot;

   @WrapMethod(method = "slotClicked")
   private void a(Slot var1, int var2, int var3, ContainerInput var4, Operation<Void> var5) {
      FakeSpawnerController var6 = FakeSpawnerController.getInstance();
      if (!VisualInventory.isActive()) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else if (!VisualInventory.isTransferBusy()) {
         Minecraft var7 = Minecraft.getInstance();
         Slot var8 = VisualInventory.unwrapSlot(var1);
         if (var8 != null && !(var8 instanceof VisualInventory.GapSlot) && var8.container == var7.player.getInventory()) {
            for (Slot var10 : var7.player.inventoryMenu.slots) {
               if (var10.container == var8.container && var10.getContainerSlot() == var8.getContainerSlot()) {
                  var8 = var10;
                  break;
               }
            }
         }

         boolean var13 = false;
         int var14 = var4 == ContainerInput.SWAP && var8 instanceof VisualInventory.GapSlot && var3 >= 0 && var3 < 9
            ? VisualInventory.getPhysicalSlot(var3)
            : var3;
         if (var1 == this.destroyItemSlot && var4 == ContainerInput.QUICK_MOVE) {
            for (Slot var12 : var7.player.inventoryMenu.slots) {
               if (!var6.isReservedSlot(var7.player.inventoryMenu, var12.index, var3, var4)) {
                  var13 = true;
                  break;
               }
            }
         }

         if (var4 != ContainerInput.PICKUP || !var6.getSavePath(var7.player.inventoryMenu, var8, var3)) {
            if (var13
               || VisualInventory.isVirtualGap(var8)
               || var8 != null && var8.container == var7.player.getInventory() && !var6.isReservedSlot(var7.player.inventoryMenu, var8.index, var14, var4)
               || var4 == ContainerInput.PICKUP_ALL && var6.restorePiles(var7.player.inventoryMenu.getCarried())) {
               LarpLog.debug(
                  "FakeSpawner",
                  "Creative input blocked type={} nativeSlot={} physical={} clearReserved={}",
                  var4,
                  var8 == null ? -1 : var8.index,
                  var8 == null ? -1 : var8.getContainerSlot(),
                  var13
               );
               PlayerMessages.sendMessage(Component.literal("This slot is reserved or awaiting real items. Move an available stack instead."), true);
            } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && var8 instanceof VisualInventory.GapSlot) {
               VisualInventory.swapSlots(var7.player.inventoryMenu, var8, var3);
            } else {
               VisualInventory.onSlotPickup(var8);
               var6.isReservedHeldStack(() -> var5.call(new Object[]{var1, var2, var3, var4}));
            }
         }
      }
   }
}
