package com.larp.obf.q41a5851bcf8e876a.q1342f79d4278119d;

import com.larpclient._8;
import com.larpclient._q;
import com.larpclient.iw;
import com.larpclient.jn;
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
public abstract class q3db95ad7f3e24b2f {
   @Shadow
   private Slot destroyItemSlot;

   @WrapMethod(method = "slotClicked")
   private void a(Slot var1, int var2, int var3, ContainerInput var4, Operation<Void> var5) {
      jn var6 = jn.g();
      if (!_8.S()) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else if (!_8.e()) {
         Minecraft var7 = Minecraft.getInstance();
         Slot var8 = _8.K(var1);
         if (var8 != null && !(var8 instanceof _8.k) && var8.container == var7.player.getInventory()) {
            for (Slot var10 : var7.player.inventoryMenu.slots) {
               if (var10.container == var8.container && var10.getContainerSlot() == var8.getContainerSlot()) {
                  var8 = var10;
                  break;
               }
            }
         }

         boolean var13 = false;
         int var14 = var4 == ContainerInput.SWAP && var8 instanceof _8.k && var3 >= 0 && var3 < 9 ? _8.t(var3) : var3;
         if (var1 == this.destroyItemSlot && var4 == ContainerInput.QUICK_MOVE) {
            for (Slot var12 : var7.player.inventoryMenu.slots) {
               if (!var6.G(var7.player.inventoryMenu, var12.index, var3, var4)) {
                  var13 = true;
                  break;
               }
            }
         }

         if (var4 != ContainerInput.PICKUP || !var6.n(var7.player.inventoryMenu, var8, var3)) {
            if (var13
               || _8.z(var8)
               || var8 != null && var8.container == var7.player.getInventory() && !var6.G(var7.player.inventoryMenu, var8.index, var14, var4)
               || var4 == ContainerInput.PICKUP_ALL && var6.o(var7.player.inventoryMenu.getCarried())) {
               _q.c(
                  "FakeSpawner",
                  "Creative input blocked type={} nativeSlot={} physical={} clearReserved={}",
                  var4,
                  var8 == null ? -1 : var8.index,
                  var8 == null ? -1 : var8.getContainerSlot(),
                  var13
               );
               iw.V(Component.literal("This slot is reserved or awaiting real items. Move an available stack instead."), true);
            } else if (var4 == ContainerInput.SWAP && var3 >= 0 && var3 < 9 && var8 instanceof _8.k) {
               _8.k(var7.player.inventoryMenu, var8, var3);
            } else {
               _8.L(var8);
               var6.e(() -> var5.call(new Object[]{var1, var2, var3, var4}));
            }
         }
      }
   }
}
