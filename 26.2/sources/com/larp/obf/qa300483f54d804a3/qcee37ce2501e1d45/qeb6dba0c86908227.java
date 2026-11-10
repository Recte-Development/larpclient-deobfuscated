package com.larp.obf.qa300483f54d804a3.qcee37ce2501e1d45;

import com.larpclient.b8;
import com.larpclient.h;
import com.larpclient.k4;
import com.larpclient.ki;
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
public abstract class qeb6dba0c86908227 {
   @Shadow
   private Slot destroyItemSlot;

   @WrapMethod(method = "slotClicked")
   private void E(Slot var1, int var2, int var3, ContainerInput var4, Operation<Void> var5) {
      b8 var6 = b8.k();
      if (!ki.k()) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else if (!ki.y()) {
         Minecraft var7 = Minecraft.getInstance();
         Slot var8 = ki.f(var1);
         if (var8 != null && !(var8 instanceof ki.d) && var8.container == var7.player.getInventory()) {
            for (Slot var10 : var7.player.inventoryMenu.slots) {
               if (var10.container == var8.container && var10.getContainerSlot() == var8.getContainerSlot()) {
                  var8 = var10;
                  break;
               }
            }
         }

         boolean var13 = false;
         int var14 = var4 == ContainerInput.SWAP && var8 instanceof ki.d && var3 >= 0 && var3 < 9 ? ki.t(var3) : var3;
         if (var1 == this.destroyItemSlot && var4 == ContainerInput.QUICK_MOVE) {
            for (Slot var12 : var7.player.inventoryMenu.slots) {
               if (!var6.P(var7.player.inventoryMenu, var12.index, var3, var4)) {
                  var13 = true;
                  break;
               }
            }
         }

         if (var4 != ContainerInput.PICKUP || !var6.f(var7.player.inventoryMenu, var8, var3)) {
            if (var13
               || ki.z(var8)
               || var8 != null && var8.container == var7.player.getInventory() && !var6.P(var7.player.inventoryMenu, var8.index, var14, var4)
               || var4 == ContainerInput.PICKUP_ALL && var6.o(var7.player.inventoryMenu.getCarried())) {
               h.U(
                  "FakeSpawner",
                  "Creative input blocked type={} nativeSlot={} physical={} clearReserved={}",
                  var4,
                  var8 == null ? -1 : var8.index,
                  var8 == null ? -1 : var8.getContainerSlot(),
                  var13
               );
               k4.f(Component.literal("This slot is reserved or awaiting real items. Move an available stack instead."), true);
            } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && var8 instanceof ki.d) {
               ki.z(var7.player.inventoryMenu, var8, var3);
            } else {
               ki.K(var8);
               var6.z(() -> var5.call(new Object[]{var1, var2, var3, var4}));
            }
         }
      }
   }
}
