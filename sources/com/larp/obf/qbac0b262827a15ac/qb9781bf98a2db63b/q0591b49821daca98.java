package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
import com.larpclient.mt;
import com.larpclient.u0;
import com.larpclient.ws;
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
public abstract class q0591b49821daca98 {
   @Shadow
   private Slot destroyItemSlot;

   @WrapMethod(method = "slotClicked")
   private void T(Slot var1, int var2, int var3, ContainerInput var4, Operation<Void> var5) {
      dr var6 = dr.z();
      if (!u0.w()) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else if (!u0.f()) {
         Minecraft var7 = Minecraft.getInstance();
         Slot var8 = u0.n(var1);
         if (var8 != null && !(var8 instanceof u0.m) && var8.container == var7.player.getInventory()) {
            for (Slot var10 : var7.player.inventoryMenu.slots) {
               if (var10.container == var8.container && var10.getContainerSlot() == var8.getContainerSlot()) {
                  var8 = var10;
                  break;
               }
            }
         }

         boolean var13 = false;
         int var14 = var4 == ContainerInput.SWAP && var8 instanceof u0.m && var3 >= 0 && var3 < 9 ? u0.f(var3) : var3;
         if (var1 == this.destroyItemSlot && var4 == ContainerInput.QUICK_MOVE) {
            for (Slot var12 : var7.player.inventoryMenu.slots) {
               if (!var6.v(var7.player.inventoryMenu, var12.index, var3, var4)) {
                  var13 = true;
                  break;
               }
            }
         }

         if (var4 != ContainerInput.PICKUP || !var6.p(var7.player.inventoryMenu, var8, var3)) {
            if (var13
               || u0.w(var8)
               || var8 != null && var8.container == var7.player.getInventory() && !var6.v(var7.player.inventoryMenu, var8.index, var14, var4)
               || var4 == ContainerInput.PICKUP_ALL && var6.y(var7.player.inventoryMenu.getCarried())) {
               mt.U(
                  "FakeSpawner",
                  "Creative input blocked type={} nativeSlot={} physical={} clearReserved={}",
                  var4,
                  var8 == null ? -1 : var8.index,
                  var8 == null ? -1 : var8.getContainerSlot(),
                  var13
               );
               ws.c(Component.literal("This slot is reserved or awaiting real items. Move an available stack instead."), true);
            } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && var8 instanceof u0.m) {
               u0.i(var7.player.inventoryMenu, var8, var3);
            } else {
               u0.I(var8);
               var6.K(() -> var5.call(new Object[]{var1, var2, var3, var4}));
            }
         }
      }
   }
}
