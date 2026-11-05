package com.larp.obf.q5682c6a8aca9b6ec.qd12d1a2c4a911e9d;

import com.larpclient.jb;
import com.larpclient.lj;
import com.larpclient.mx;
import com.larpclient.r4;
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
public abstract class q2de1e99966bbeb3d {
   @Shadow
   private Slot destroyItemSlot;

   @WrapMethod(method = "slotClicked")
   private void v(Slot var1, int var2, int var3, ContainerInput var4, Operation<Void> var5) {
      lj var6 = lj.j();
      if (!mx.U()) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else if (!mx.m()) {
         Minecraft var7 = Minecraft.getInstance();
         Slot var8 = mx.C(var1);
         if (var8 != null && !(var8 instanceof mx.q) && var8.container == var7.player.getInventory()) {
            for (Slot var10 : var7.player.inventoryMenu.slots) {
               if (var10.container == var8.container && var10.getContainerSlot() == var8.getContainerSlot()) {
                  var8 = var10;
                  break;
               }
            }
         }

         boolean var13 = false;
         int var14 = var4 == ContainerInput.SWAP && var8 instanceof mx.q && var3 >= 0 && var3 < 9 ? mx.V(var3) : var3;
         if (var1 == this.destroyItemSlot && var4 == ContainerInput.QUICK_MOVE) {
            for (Slot var12 : var7.player.inventoryMenu.slots) {
               if (!var6.J(var7.player.inventoryMenu, var12.index, var3, var4)) {
                  var13 = true;
                  break;
               }
            }
         }

         if (var4 != ContainerInput.PICKUP || !var6.m(var7.player.inventoryMenu, var8, var3)) {
            if (var13
               || mx.r(var8)
               || var8 != null && var8.container == var7.player.getInventory() && !var6.J(var7.player.inventoryMenu, var8.index, var14, var4)
               || var4 == ContainerInput.PICKUP_ALL && var6.g(var7.player.inventoryMenu.getCarried())) {
               r4.g(
                  "FakeSpawner",
                  "Creative input blocked type={} nativeSlot={} physical={} clearReserved={}",
                  var4,
                  var8 == null ? -1 : var8.index,
                  var8 == null ? -1 : var8.getContainerSlot(),
                  var13
               );
               jb.v(Component.literal("This slot is reserved or awaiting real items. Move an available stack instead."), true);
            } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && var8 instanceof mx.q) {
               mx.b(var7.player.inventoryMenu, var8, var3);
            } else {
               mx.E(var8);
               var6.U(() -> var5.call(new Object[]{var1, var2, var3, var4}));
            }
         }
      }
   }
}
