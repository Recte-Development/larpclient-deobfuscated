package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.eg;
import com.larpclient.f2;
import com.larpclient.f9;
import com.larpclient.hk;
import com.larpclient.js;
import com.larpclient.mm;
import com.larpclient.oi;
import com.larpclient.ro;
import com.larpclient.ug;
import com.larpclient.xa;
import com.larpclient.yg;
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
public class qbece535916516ff9 {
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
   private boolean Y;
   private boolean J;

   @Shadow
   protected List<Component> getTooltipFromContainerItem(ItemStack var1) {
      throw new AssertionError();
   }

   @Inject(method = "onClose", at = @At("HEAD"), cancellable = true)
   private void d(CallbackInfo var1) {
      ro var2 = ro.H();
      Minecraft var3 = Minecraft.getInstance();
      AbstractContainerScreen var4 = (AbstractContainerScreen)this;
      if (mm.W().D(var4.getMenu())) {
         var3.gui.setScreen(null);
         var1.cancel();
      } else if (var2.z() && var2.h() && var4.getMenu() == var3.player.containerMenu && var4.getMenu().getCarried().isEmpty()) {
         var3.gui.setScreen(null);
         var1.cancel();
      } else {
         ug var5 = ug.x();
         if (var5.C(((AbstractContainerScreen)this).getMenu())) {
            Minecraft.getInstance().gui.setScreen(null);
            var1.cancel();
         } else if (var5.blocksClose()) {
            var1.cancel();
         }
      }
   }

   private boolean I(Slot var1) {
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

      f9 var3 = f9.W();
      return var3.o() == f9.b.F && var3.i() == var2.containerId;
   }

   @ModifyVariable(method = "extractSlot", at = @At("STORE"), ordinal = 0)
   private ItemStack p(ItemStack var1, GuiGraphicsExtractor var2, Slot var3, int var4, int var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (var6.player != null && var3.container == var6.player.getInventory()) {
         ItemStack var7 = ug.x().getInventorySlotProjection(var3.getContainerSlot());
         if (var7 != null) {
            return var7;
         }

         ItemStack var8 = mm.W().u(var1);
         var8 = ro.H().n(var8);
         var8 = hk.G().B(var8);
         var8 = oi.A().y(var3.getContainerSlot(), var8);
         var8 = xa.r().I(var8);
         var8 = js.A().T(var8);
         ItemStack var9 = f2.f().P(var3.getContainerSlot(), var8);
         if (var9 != null) {
            var8 = var9;
         }

         return eg.y().t(var3.getContainerSlot(), var8);
      } else {
         return var1;
      }
   }

   @Inject(method = "extractSlot", at = @At("HEAD"), cancellable = true)
   private void S(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      if (var2 != null) {
         ItemStack var6 = var2.container instanceof Inventory ? null : yg.j(var2);
         if (var6 != null) {
            if (!var6.isEmpty()) {
               var1.item(var6, var2.x, var2.y);
               var1.itemDecorations(Minecraft.getInstance().font, var6, var2.x, var2.y);
            }

            var5.cancel();
            return;
         }

         eg var7 = eg.y();
         boolean var8 = var2.container instanceof Inventory && var7.G() && var2.getContainerSlot() == var7.g();
         if (var8) {
            boolean var9 = var3 - this.leftPos >= var2.x
               && var3 - this.leftPos < var2.x + 16
               && var4 - this.topPos >= var2.y
               && var4 - this.topPos < var2.y + 16;
            if (this.isQuickCrafting) {
               if (var9) {
                  this.J = true;
               }
            } else {
               this.J = false;
            }

            if (var9 || this.J) {
               ItemStack var10 = ((AbstractContainerScreen)this).getMenu().getCarried();
               if (!var10.isEmpty()) {
                  ItemStack var11 = this.T(var10);
                  var1.item(var11, var2.x, var2.y);
                  var1.itemDecorations(Minecraft.getInstance().font, var11, var2.x, var2.y);
                  var5.cancel();
                  return;
               }
            }
         }

         ItemStack var12 = mm.W().t(Minecraft.getInstance(), ((AbstractContainerScreen)this).getMenu(), var2);
         if (var12 == null) {
            var12 = ro.H().F(Minecraft.getInstance(), ((AbstractContainerScreen)this).getMenu(), var2);
         }

         if (var12 == null) {
            var12 = hk.G().n(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = oi.A().A(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = xa.r().a(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = js.A().u(Minecraft.getInstance(), var2);
         }

         if (var12 == null && !(var2.container instanceof Inventory) && this.I(var2)) {
            ItemStack var13 = f2.f().b(Minecraft.getInstance(), var2.getContainerSlot(), var2.getItem());
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
   private void Z(MouseButtonEvent var1, double var2, double var4, CallbackInfoReturnable<Boolean> var6) {
      eg var7 = eg.y();
      if (var7.G() && !this.quickCraftSlots.isEmpty()) {
         int var8 = var7.g();
         this.quickCraftSlots.removeIf(var1x -> var1x.container instanceof Inventory && var1x.getContainerSlot() == var8);
      }
   }

   @Inject(method = "mouseReleased", at = @At("HEAD"))
   private void h(MouseButtonEvent var1, CallbackInfoReturnable<Boolean> var2) {
      eg var3 = eg.y();
      if (var3.G()) {
         int var4 = var3.g();
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
                  var3.l(this.T(var8));
               }
            }
         }
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void W(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      ug.x().v(((AbstractContainerScreen)this).getMenu());
      if (ug.x().blocksUserInteraction() || ug.x().C(((AbstractContainerScreen)this).getMenu())) {
         var5.cancel();
      } else if (var1 != null && var1.container instanceof Inventory) {
         eg var6 = eg.y();
         if (var6.G() && var1.getContainerSlot() == var6.g()) {
            if (var4 == ContainerInput.PICKUP && !var6.l().isEmpty()) {
               var6.s();
            }

            var5.cancel();
         }
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void x(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var4 == ContainerInput.CLONE && var1 != null) {
         f2 var6 = f2.f();
         Minecraft var7 = Minecraft.getInstance();
         if (var1.container instanceof Inventory) {
            var6.i(var1.getItem(), var6.P(var1.getContainerSlot(), var1.getItem()));
         } else if (this.I(var1)) {
            var6.i(var1.getItem(), var6.b(var7, var1.getContainerSlot(), var1.getItem()));
         }
      }
   }

   private ItemStack T(ItemStack var1) {
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
   private ItemStack D(AbstractContainerMenu var1, Operation<ItemStack> var2) {
      if (ug.x().hidesCursor(var1)) {
         return ItemStack.EMPTY;
      }

      ItemStack var3 = hk.G().B(yg.Z((ItemStack)var2.call(new Object[]{var1})));
      var3 = oi.A().W(var3);
      var3 = f2.f().f(var3);
      return js.A().T(xa.r().f(var3));
   }

   @WrapOperation(
      method = "extractTooltip",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getCarried()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack e(AbstractContainerMenu var1, Operation<ItemStack> var2) {
      if (ug.x().hidesCursor(var1)) {
         return ItemStack.EMPTY;
      }

      ItemStack var3 = hk.G().B(yg.Z((ItemStack)var2.call(new Object[]{var1})));
      var3 = oi.A().W(var3);
      var3 = f2.f().f(var3);
      return js.A().T(xa.r().f(var3));
   }

   @Inject(method = "extractTooltip", at = @At("HEAD"), cancellable = true)
   private void S(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.player != null && this.hoveredSlot != null) {
         ItemStack var6 = mm.W().t(var5, ((AbstractContainerScreen)this).getMenu(), this.hoveredSlot);
         if (var6 == null) {
            var6 = ro.H().F(var5, ((AbstractContainerScreen)this).getMenu(), this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = yg.j(this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = hk.G().n(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = oi.A().A(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = xa.r().a(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = js.A().u(var5, this.hoveredSlot);
         }

         if (var6 == null && this.hoveredSlot.container == var5.player.getInventory()) {
            var6 = ug.x().getInventorySlotProjection(this.hoveredSlot.getContainerSlot());
         }

         if (var6 != null) {
            AbstractContainerMenu var7 = ((AbstractContainerScreen)this).getMenu();
            if (!var6.isEmpty() && (var7.getCarried().isEmpty() || ug.x().hidesCursor(var7))) {
               this.Y = true;

               try {
                  var1.setTooltipForNextFrame(
                     var5.font, this.getTooltipFromContainerItem(var6), var6.getTooltipImage(), var2, var3, (Identifier)var6.get(DataComponents.TOOLTIP_STYLE)
                  );
               } finally {
                  this.Y = false;
               }
            }

            var4.cancel();
         }
      }
   }

   @Inject(method = "getTooltipFromContainerItem", at = @At("HEAD"), cancellable = true)
   private void y(ItemStack var1, CallbackInfoReturnable<List<Component>> var2) {
      if (!this.Y && var1 != null && !var1.isEmpty()) {
         ItemStack var3 = null;
         if (this.hoveredSlot != null) {
            var3 = hk.G().n(Minecraft.getInstance(), this.hoveredSlot);
            if (var3 == null) {
               var3 = oi.A().A(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null) {
               var3 = xa.r().a(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null) {
               var3 = js.A().u(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null && this.hoveredSlot.container instanceof Inventory) {
               ItemStack var4 = oi.A().y(this.hoveredSlot.getContainerSlot(), var1);
               var4 = hk.G().B(var4);
               ItemStack var5 = ug.x().getVisualOverride(var4);
               if (var5 != null) {
                  var4 = var5;
               }

               var4 = xa.r().I(var4);
               var4 = js.A().T(var4);
               var4 = eg.y().t(this.hoveredSlot.getContainerSlot(), var4);
               if (var4 != var1) {
                  var3 = var4;
               }

               ItemStack var6 = f2.f().P(this.hoveredSlot.getContainerSlot(), var1);
               if (var3 == null && var6 != null) {
                  var3 = var6;
               }
            }

            if (var3 == null && this.I(this.hoveredSlot)) {
               var3 = f2.f().b(Minecraft.getInstance(), this.hoveredSlot.getContainerSlot(), var1);
            }
         }

         if (var3 != null) {
            if (var3.isEmpty()) {
               var2.setReturnValue(List.of());
            } else {
               this.Y = true;

               try {
                  var2.setReturnValue(this.getTooltipFromContainerItem(var3));
               } finally {
                  this.Y = false;
               }
            }
         }
      }
   }
}
