package com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a;

import com.larpclient._g;
import com.larpclient._u;
import com.larpclient.c6;
import com.larpclient.ep;
import com.larpclient.kq;
import com.larpclient.mi;
import com.larpclient.pj;
import com.larpclient.w5;
import com.larpclient.z;
import com.larpclient.ze;
import com.larpclient.zv;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerScreen.class)
public class q3b5e099dfd6a4c63 {
   @Shadow
   protected int leftPos;
   @Shadow
   protected int topPos;
   @Shadow
   protected Slot hoveredSlot;
   @Shadow
   protected Set<Slot> quickCraftSlots;
   @Shadow
   protected boolean isQuickCrafting;
   @Shadow
   private int quickCraftingType;
   private boolean h;
   private boolean g;

   @Shadow
   protected List<Component> getTooltipFromContainerItem(ItemStack var1) {
      throw new AssertionError();
   }

   @Inject(method = "onClose", at = @At("HEAD"), cancellable = true)
   private void K(CallbackInfo var1) {
      c6 var2 = c6.K();
      Minecraft var3 = Minecraft.getInstance();
      AbstractContainerScreen var4 = (AbstractContainerScreen)this;
      if (kq.I().T(var4.getMenu())) {
         var3.gui.setScreen(null);
         var1.cancel();
      } else if (var2.F() && var2.q() && var4.getMenu() == var3.player.containerMenu && var4.getMenu().getCarried().isEmpty()) {
         var3.gui.setScreen(null);
         var1.cancel();
      } else {
         ze var5 = ze.c();
         if (var5.h(((AbstractContainerScreen)this).getMenu())) {
            Minecraft.getInstance().gui.setScreen(null);
            var1.cancel();
         } else if (var5.blocksClose()) {
            var1.cancel();
         }
      }
   }

   private boolean R(Slot var1) {
      if (var1 == null || var1.getContainerSlot() < 0 || var1.getContainerSlot() > 8) {
         return false;
      }

      if (var1.container instanceof Inventory) {
         return false;
      }

      AbstractContainerMenu var2 = ((AbstractContainerScreen)this).getMenu();
      if (!(var2 instanceof DispenserMenu)) {
         return false;
      }

      ep var3 = ep.H();
      return var3.U() == ep.o.x && var3.V() == var2.containerId;
   }

   @ModifyVariable(method = "extractSlot", at = @At("STORE"), ordinal = 0)
   private ItemStack c(ItemStack var1, GuiGraphicsExtractor var2, Slot var3, int var4, int var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (var6.player != null && var3.container == var6.player.getInventory()) {
         ItemStack var7 = ze.c().getInventorySlotProjection(var3.getContainerSlot());
         if (var7 != null) {
            return var7;
         }

         ItemStack var8 = kq.I().R(var1);
         var8 = c6.K().f(var8);
         var8 = _g.E().i(var8);
         var8 = mi.L().Z(var3.getContainerSlot(), var8);
         var8 = zv.x().g(var8);
         var8 = _u.O().M(var8);
         ItemStack var9 = z.v().U(var3.getContainerSlot(), var8);
         if (var9 != null) {
            var8 = var9;
         }

         return pj.U().a(var3.getContainerSlot(), var8);
      } else {
         return var1;
      }
   }

   @Inject(method = "extractSlot", at = @At("HEAD"), cancellable = true)
   private void B(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      if (var2 != null) {
         ItemStack var6 = var2.container instanceof Inventory ? null : w5.c(var2);
         if (var6 != null) {
            if (!var6.isEmpty()) {
               var1.item(var6, var2.x, var2.y);
               var1.itemDecorations(Minecraft.getInstance().font, var6, var2.x, var2.y);
            }

            var5.cancel();
            return;
         }

         pj var7 = pj.U();
         boolean var8 = var2.container instanceof Inventory && var7.c() && var2.getContainerSlot() == var7.B();
         if (var8) {
            boolean var9 = var3 - this.leftPos >= var2.x
               && var3 - this.leftPos < var2.x + 16
               && var4 - this.topPos >= var2.y
               && var4 - this.topPos < var2.y + 16;
            if (this.isQuickCrafting) {
               if (var9) {
                  this.g = true;
               }
            } else {
               this.g = false;
            }

            if (var9 || this.g) {
               ItemStack var10 = ((AbstractContainerScreen)this).getMenu().getCarried();
               if (!var10.isEmpty()) {
                  ItemStack var11 = this.M(var10);
                  var1.item(var11, var2.x, var2.y);
                  var1.itemDecorations(Minecraft.getInstance().font, var11, var2.x, var2.y);
                  var5.cancel();
                  return;
               }
            }
         }

         ItemStack var12 = kq.I().l(Minecraft.getInstance(), ((AbstractContainerScreen)this).getMenu(), var2);
         if (var12 == null) {
            var12 = c6.K().W(Minecraft.getInstance(), ((AbstractContainerScreen)this).getMenu(), var2);
         }

         if (var12 == null) {
            var12 = _g.E().O(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = mi.L().i(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = zv.x().s(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = _u.O().H(Minecraft.getInstance(), var2);
         }

         if (var12 == null && !(var2.container instanceof Inventory) && this.R(var2)) {
            ItemStack var13 = z.v().L(Minecraft.getInstance(), var2.getContainerSlot(), var2.getItem());
            if (var13 != null) {
               var12 = var13;
            }
         }

         if (var12 != null) {
            if (var12.isEmpty()) {
               var5.cancel();
               return;
            }

            var1.item(var12, var2.x, var2.y);
            var1.itemDecorations(Minecraft.getInstance().font, var12, var2.x, var2.y);
            var5.cancel();
         }
      }
   }

   @Inject(method = "mouseDragged", at = @At("TAIL"))
   private void p(MouseButtonEvent var1, double var2, double var4, CallbackInfoReturnable<Boolean> var6) {
      pj var7 = pj.U();
      if (var7.c() && !this.quickCraftSlots.isEmpty()) {
         int var8 = var7.B();
         this.quickCraftSlots.removeIf(var1x -> var1x.container instanceof Inventory && var1x.getContainerSlot() == var8);
      }
   }

   @Inject(method = "mouseReleased", at = @At("HEAD"))
   private void A(MouseButtonEvent var1, CallbackInfoReturnable<Boolean> var2) {
      pj var3 = pj.U();
      if (var3.c()) {
         int var4 = var3.B();
         Slot var5 = null;

         for (Slot var7 : ((AbstractContainerScreen)this).getMenu().slots) {
            if (var7.container instanceof Inventory && var7.getContainerSlot() == var4) {
               var5 = var7;
               break;
            }
         }

         if (var5 != null) {
            if (var1.x() - this.leftPos >= var5.x
               && var1.x() - this.leftPos < var5.x + 16
               && var1.y() - this.topPos >= var5.y
               && var1.y() - this.topPos < var5.y + 16) {
               ItemStack var8 = ((AbstractContainerScreen)this).getMenu().getCarried();
               if (!var8.isEmpty()) {
                  var3.H(this.M(var8));
               }
            }
         }
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void T(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      ze.c().f(((AbstractContainerScreen)this).getMenu());
      if (ze.c().blocksUserInteraction() || ze.c().h(((AbstractContainerScreen)this).getMenu())) {
         var5.cancel();
      } else if (var1 != null && var1.container instanceof Inventory) {
         pj var6 = pj.U();
         if (var6.c() && var1.getContainerSlot() == var6.B()) {
            if (var4 == ContainerInput.PICKUP && !var6.I().isEmpty()) {
               var6.C();
            }

            var5.cancel();
         }
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void j(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var4 == ContainerInput.CLONE && var1 != null) {
         z var6 = z.v();
         Minecraft var7 = Minecraft.getInstance();
         if (var1.container instanceof Inventory) {
            var6.F(var1.getItem(), var6.U(var1.getContainerSlot(), var1.getItem()));
         } else if (this.R(var1)) {
            var6.F(var1.getItem(), var6.L(var7, var1.getContainerSlot(), var1.getItem()));
         }
      }
   }

   private ItemStack M(ItemStack var1) {
      int var2;
      if (this.quickCraftingType == 1) {
         var2 = 1;
      } else if (this.quickCraftingType == 0) {
         int var3 = this.quickCraftSlots.size();
         var2 = var3 <= 0 ? var1.getCount() : Math.max(1, var1.getCount() / var3);
      } else {
         var2 = var1.getCount();
      }

      return var1.copyWithCount(Math.max(1, Math.min(var2, var1.getMaxStackSize())));
   }

   @WrapOperation(
      method = "extractCarriedItem",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getCarried()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack m(AbstractContainerMenu var1, Operation<ItemStack> var2) {
      if (ze.c().hidesCursor(var1)) {
         return ItemStack.EMPTY;
      }

      ItemStack var3 = _g.E().i(w5.u((ItemStack)var2.call(new Object[]{var1})));
      var3 = mi.L().x(var3);
      var3 = z.v().d(var3);
      return _u.O().M(zv.x().M(var3));
   }

   @WrapOperation(
      method = "extractTooltip",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getCarried()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack z(AbstractContainerMenu var1, Operation<ItemStack> var2) {
      if (ze.c().hidesCursor(var1)) {
         return ItemStack.EMPTY;
      }

      ItemStack var3 = _g.E().i(w5.u((ItemStack)var2.call(new Object[]{var1})));
      var3 = mi.L().x(var3);
      var3 = z.v().d(var3);
      return _u.O().M(zv.x().M(var3));
   }

   @Inject(method = "extractTooltip", at = @At("HEAD"), cancellable = true)
   private void o(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.player != null && this.hoveredSlot != null) {
         ItemStack var6 = kq.I().l(var5, ((AbstractContainerScreen)this).getMenu(), this.hoveredSlot);
         if (var6 == null) {
            var6 = c6.K().W(var5, ((AbstractContainerScreen)this).getMenu(), this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = w5.c(this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = _g.E().O(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = mi.L().i(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = zv.x().s(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = _u.O().H(var5, this.hoveredSlot);
         }

         if (var6 == null && this.hoveredSlot.container == var5.player.getInventory()) {
            var6 = ze.c().getInventorySlotProjection(this.hoveredSlot.getContainerSlot());
         }

         if (var6 != null) {
            AbstractContainerMenu var7 = ((AbstractContainerScreen)this).getMenu();
            if (!var6.isEmpty() && (var7.getCarried().isEmpty() || ze.c().hidesCursor(var7))) {
               this.h = true;

               try {
                  var1.setTooltipForNextFrame(
                     var5.font, this.getTooltipFromContainerItem(var6), var6.getTooltipImage(), var2, var3, (Identifier)var6.get(DataComponents.TOOLTIP_STYLE)
                  );
               } finally {
                  this.h = false;
               }
            }

            var4.cancel();
         }
      }
   }

   @Inject(method = "getTooltipFromContainerItem", at = @At("HEAD"), cancellable = true)
   private void s(ItemStack var1, CallbackInfoReturnable<List<Component>> var2) {
      if (!this.h && var1 != null && !var1.isEmpty()) {
         ItemStack var3 = null;
         if (this.hoveredSlot != null) {
            var3 = _g.E().O(Minecraft.getInstance(), this.hoveredSlot);
            if (var3 == null) {
               var3 = mi.L().i(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null) {
               var3 = zv.x().s(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null) {
               var3 = _u.O().H(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null && this.hoveredSlot.container instanceof Inventory) {
               ItemStack var4 = mi.L().Z(this.hoveredSlot.getContainerSlot(), var1);
               var4 = _g.E().i(var4);
               ItemStack var5 = ze.c().getVisualOverride(var4);
               if (var5 != null) {
                  var4 = var5;
               }

               var4 = zv.x().g(var4);
               var4 = _u.O().M(var4);
               var4 = pj.U().a(this.hoveredSlot.getContainerSlot(), var4);
               if (var4 != var1) {
                  var3 = var4;
               }

               ItemStack var6 = z.v().U(this.hoveredSlot.getContainerSlot(), var1);
               if (var3 == null && var6 != null) {
                  var3 = var6;
               }
            }

            if (var3 == null && this.R(this.hoveredSlot)) {
               var3 = z.v().L(Minecraft.getInstance(), this.hoveredSlot.getContainerSlot(), var1);
            }
         }

         if (var3 != null) {
            if (var3.isEmpty()) {
               var2.setReturnValue(List.of());
            } else {
               this.h = true;

               try {
                  var2.setReturnValue(this.getTooltipFromContainerItem(var3));
               } finally {
                  this.h = false;
               }
            }
         }
      }
   }
}
