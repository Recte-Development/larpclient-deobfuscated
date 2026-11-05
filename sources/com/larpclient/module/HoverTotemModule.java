package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q6be60df302bad6e2;
import com.larp.obf.q5682c6a8aca9b6ec.qac882a24641eea19;
import com.larpclient.lr;
import com.larpclient.mx;
import com.larpclient.r4;
import com.larpclient.rr;
import java.util.Set;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class HoverTotemModule extends Module {
   private static HoverTotemModule i;
   private final ln F = this.addSetting(new ln("Hotbar Refill", "After filling the offhand, move hovered totems into the chosen hotbar slot", false));
   private final lc X = this.addSetting(new lc("Hotbar Slot", "Hotbar slot to refill, replacing its current item when needed", 1.0, 1.0, 9.0, 1.0));

   public HoverTotemModule() {
      super("HoverTotem", "Move hovered totems into your offhand or chosen hotbar slot", Category.COMBAT, -1);
      this.X.x(this.F::getValue);
      i = this;
   }

   public static HoverTotemModule U() {
      return i;
   }

   @Override
   public String getDisplayName() {
      return "Hover Totem";
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public void onEnable() {
      r4.g("HoverTotem", "Ready for native container hover swaps; AutoTotem has priority");
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.gameMode != null && mc.player.isAlive()) {
         int var10000 = mc.player.isSpectator();
         boolean var12 = false;
         byte var11 = (byte)var10000;
         var10000 = Integer.signum(var11);
         if ((var10000 | -var10000) >>> 31 == 0
            && !mc.player.hasInfiniteMaterials()
            && !rr.o()
            && mc.isWindowActive()
            && mc.gui.screen() instanceof AbstractContainerScreen var1) {
            int var21 = AutoTotemModule.F();
            boolean var14 = false;
            byte var13 = (byte)var21;
            var21 = Integer.signum(var13);
            if (((var21 | -var21) >>> 31 ^ 1) != 0) {
               qac882a24641eea19 var19 = (qac882a24641eea19)mc.player;
               Input var3 = var19.larpLastSentInput();
               if (var3 != null && lr.H(var19.larpWasSprinting(), var3.forward(), var3.backward(), var3.left(), var3.right(), var3.jump(), var3.shift())) {
                  AbstractContainerMenu var4 = var1.getMenu();
                  if (var4 == mc.player.containerMenu
                     && var4.stillValid(mc.player)
                     && var4.getCarried().isEmpty()
                     && mc.player.inventoryMenu.getCarried().isEmpty()) {
                     Slot var5 = ((q6be60df302bad6e2)var1).larpHoveredSlot();
                     Slot var6 = K(var4, var5);
                     if (var6 != null && var6.isActive() && !var6.isFake() && var6.mayPickup(mc.player)) {
                        int var7 = this.X.getValue().intValue();
                        int var8 = var6.container == mc.player.getInventory() ? var6.getContainerSlot() : -1;
                        lr.n var9 = lr.s(
                           var6.index,
                           var6.getItem().is(Items.TOTEM_OF_UNDYING),
                           var8,
                           mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING),
                           this.F.getValue(),
                           var7,
                           mc.player.getInventory().getItem(var7 - 1).is(Items.TOTEM_OF_UNDYING)
                        );
                        if (var9 != null && this.R(var4, var9.button())) {
                           ItemStack var10 = mc.player.getInventory().getItem(var9.button());
                           if (!var10.isEmpty()) {
                              if (!var6.mayPlace(var10)) {
                                 return;
                              }

                              var21 = var10.getCount();
                              int var18 = var6.getMaxStackSize(var10);
                              int var17 = var21;
                              if ((-Integer.compare(var17, var18) >>> 31 ^ 1) == 0) {
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
                           r4.g("HoverTotem", "Swapped hovered menu slot {} to {} in container {}", var10002);
                           return;
                        }

                        return;
                     }

                     return;
                  }

                  return;
               }

               return;
            }
         }
      }
   }

   private static Slot K(AbstractContainerMenu var0, Slot var1) {
      if (var1 != null && var1.getItem().is(Items.TOTEM_OF_UNDYING) && !mx.r(var1) && var1.index >= 0 && var1.index < var0.slots.size()) {
         Slot var2 = (Slot)var0.slots.get(var1.index);
         if (var2 != var1 && !(var1 instanceof mx.q)) {
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

   private boolean R(AbstractContainerMenu var1, int var2) {
      if (var2 != 40 && mx.U()) {
         for (Slot var4 : var1.slots) {
            if (var4.container == mc.player.getInventory() && var4.getContainerSlot() == var2) {
               Slot var5 = mx.v(var1, var4);
               return var5.index == var4.index && !mx.r(var5) && ItemStack.matches(var5.getItem(), var4.getItem());
            }
         }

         return false;
      } else {
         return true;
      }
   }
}
