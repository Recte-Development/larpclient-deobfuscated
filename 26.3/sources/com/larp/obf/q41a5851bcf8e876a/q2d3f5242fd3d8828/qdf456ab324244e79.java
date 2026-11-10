package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.hh;
import com.larpclient.hk;
import com.larpclient.ig;
import com.larpclient.p2;
import com.larpclient.pp;
import com.larpclient.q5;
import com.larpclient.qm;
import com.larpclient.qo;
import com.larpclient.sl;
import com.larpclient.vy;
import com.larpclient.wj;
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
public class qdf456ab324244e79 {
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
   private boolean m;
   private boolean A;

   @Shadow
   protected List<Component> getTooltipFromContainerItem(ItemStack var1) {
      throw new AssertionError();
   }

   @Inject(method = "onClose", at = @At("HEAD"), cancellable = true)
   private void o(CallbackInfo var1) {
      wj var2 = wj.m();
      Minecraft var3 = Minecraft.getInstance();
      AbstractContainerScreen var4 = (AbstractContainerScreen)this;
      if (q5.a().f(var4.getMenu())) {
         var3.gui.setScreen(null);
         var1.cancel();
      } else if (var2.J() && var2.M() && var4.getMenu() == var3.player.containerMenu && var4.getMenu().getCarried().isEmpty()) {
         var3.gui.setScreen(null);
         var1.cancel();
      } else {
         vy var5 = vy.a();
         if (var5.u(((AbstractContainerScreen)this).getMenu())) {
            Minecraft.getInstance().gui.setScreen(null);
            var1.cancel();
         } else if (var5.blocksClose()) {
            var1.cancel();
         }
      }
   }

   private boolean O(Slot var1) {
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

      hk var3 = hk.e();
      return var3.l() == hk.k.N && var3.v() == var2.containerId;
   }

   @ModifyVariable(method = "extractSlot", at = @At("STORE"), ordinal = 0)
   private ItemStack b(ItemStack var1, GuiGraphicsExtractor var2, Slot var3, int var4, int var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (var6.player != null && var3.container == var6.player.getInventory()) {
         ItemStack var7 = vy.a().getInventorySlotProjection(var3.getContainerSlot());
         if (var7 != null) {
            return var7;
         }

         ItemStack var8 = q5.a().v(var1);
         var8 = wj.m().F(var8);
         var8 = sl.X().V(var8);
         var8 = qo.Z().g(var3.getContainerSlot(), var8);
         var8 = p2.p().j(var8);
         var8 = pp.A().F(var8);
         ItemStack var9 = ig.r().q(var3.getContainerSlot(), var8);
         if (var9 != null) {
            var8 = var9;
         }

         return hh.R().o(var3.getContainerSlot(), var8);
      } else {
         return var1;
      }
   }

   @Inject(method = "extractSlot", at = @At("HEAD"), cancellable = true)
   private void n(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      if (var2 != null) {
         ItemStack var6 = var2.container instanceof Inventory ? null : qm.A(var2);
         if (var6 != null) {
            if (!var6.isEmpty()) {
               var1.item(var6, var2.x, var2.y);
               var1.itemDecorations(Minecraft.getInstance().font, var6, var2.x, var2.y);
            }

            var5.cancel();
            return;
         }

         hh var7 = hh.R();
         boolean var8 = var2.container instanceof Inventory && var7.U() && var2.getContainerSlot() == var7.t();
         if (var8) {
            boolean var9 = var3 - this.leftPos >= var2.x
               && var3 - this.leftPos < var2.x + 16
               && var4 - this.topPos >= var2.y
               && var4 - this.topPos < var2.y + 16;
            if (this.isQuickCrafting) {
               if (var9) {
                  this.A = true;
               }
            } else {
               this.A = false;
            }

            if (var9 || this.A) {
               ItemStack var10 = ((AbstractContainerScreen)this).getMenu().getCarried();
               if (!var10.isEmpty()) {
                  ItemStack var11 = this.j(var10);
                  var1.item(var11, var2.x, var2.y);
                  var1.itemDecorations(Minecraft.getInstance().font, var11, var2.x, var2.y);
                  var5.cancel();
                  return;
               }
            }
         }

         ItemStack var12 = q5.a().P(Minecraft.getInstance(), ((AbstractContainerScreen)this).getMenu(), var2);
         if (var12 == null) {
            var12 = wj.m().w(Minecraft.getInstance(), ((AbstractContainerScreen)this).getMenu(), var2);
         }

         if (var12 == null) {
            var12 = sl.X().o(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = qo.Z().G(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = p2.p().d(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = pp.A().P(Minecraft.getInstance(), var2);
         }

         if (var12 == null && !(var2.container instanceof Inventory) && this.O(var2)) {
            ItemStack var13 = ig.r().Y(Minecraft.getInstance(), var2.getContainerSlot(), var2.getItem());
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
      hh var7 = hh.R();
      if (var7.U() && !this.quickCraftSlots.isEmpty()) {
         int var8 = var7.t();
         this.quickCraftSlots.removeIf(var1x -> var1x.container instanceof Inventory && var1x.getContainerSlot() == var8);
      }
   }

   @Inject(method = "mouseReleased", at = @At("HEAD"))
   private void X(MouseButtonEvent var1, CallbackInfoReturnable<Boolean> var2) {
      hh var3 = hh.R();
      if (var3.U()) {
         int var4 = var3.t();
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
                  var3.I(this.j(var8));
               }
            }
         }
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void E(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      vy.a().y(((AbstractContainerScreen)this).getMenu());
      if (vy.a().blocksUserInteraction() || vy.a().u(((AbstractContainerScreen)this).getMenu())) {
         var5.cancel();
      } else if (var1 != null && var1.container instanceof Inventory) {
         hh var6 = hh.R();
         if (var6.U() && var1.getContainerSlot() == var6.t()) {
            if (var4 == ContainerInput.PICKUP && !var6.w().isEmpty()) {
               var6.m();
            }

            var5.cancel();
         }
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void q(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var4 == ContainerInput.CLONE && var1 != null) {
         ig var6 = ig.r();
         Minecraft var7 = Minecraft.getInstance();
         if (var1.container instanceof Inventory) {
            var6.U(var1.getItem(), var6.q(var1.getContainerSlot(), var1.getItem()));
         } else if (this.O(var1)) {
            var6.U(var1.getItem(), var6.Y(var7, var1.getContainerSlot(), var1.getItem()));
         }
      }
   }

   private ItemStack j(ItemStack var1) {
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
   private ItemStack k(AbstractContainerMenu var1, Operation<ItemStack> var2) {
      if (vy.a().hidesCursor(var1)) {
         return ItemStack.EMPTY;
      }

      ItemStack var3 = sl.X().V(qm.h((ItemStack)var2.call(new Object[]{var1})));
      var3 = qo.Z().e(var3);
      var3 = ig.r().u(var3);
      return pp.A().F(p2.p().t(var3));
   }

   @WrapOperation(
      method = "extractTooltip",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getCarried()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack d(AbstractContainerMenu var1, Operation<ItemStack> var2) {
      if (vy.a().hidesCursor(var1)) {
         return ItemStack.EMPTY;
      }

      ItemStack var3 = sl.X().V(qm.h((ItemStack)var2.call(new Object[]{var1})));
      var3 = qo.Z().e(var3);
      var3 = ig.r().u(var3);
      return pp.A().F(p2.p().t(var3));
   }

   @Inject(method = "extractTooltip", at = @At("HEAD"), cancellable = true)
   private void l(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.player != null && this.hoveredSlot != null) {
         ItemStack var6 = q5.a().P(var5, ((AbstractContainerScreen)this).getMenu(), this.hoveredSlot);
         if (var6 == null) {
            var6 = wj.m().w(var5, ((AbstractContainerScreen)this).getMenu(), this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = qm.A(this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = sl.X().o(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = qo.Z().G(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = p2.p().d(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = pp.A().P(var5, this.hoveredSlot);
         }

         if (var6 == null && this.hoveredSlot.container == var5.player.getInventory()) {
            var6 = vy.a().getInventorySlotProjection(this.hoveredSlot.getContainerSlot());
         }

         if (var6 != null) {
            AbstractContainerMenu var7 = ((AbstractContainerScreen)this).getMenu();
            if (!var6.isEmpty() && (var7.getCarried().isEmpty() || vy.a().hidesCursor(var7))) {
               this.m = true;

               try {
                  var1.setTooltipForNextFrame(
                     var5.font, this.getTooltipFromContainerItem(var6), var6.getTooltipImage(), var2, var3, (Identifier)var6.get(DataComponents.TOOLTIP_STYLE)
                  );
               } finally {
                  this.m = false;
               }
            }

            var4.cancel();
         }
      }
   }

   @Inject(method = "getTooltipFromContainerItem", at = @At("HEAD"), cancellable = true)
   private void v(ItemStack var1, CallbackInfoReturnable<List<Component>> var2) {
      if (!this.m && var1 != null && !var1.isEmpty()) {
         ItemStack var3 = null;
         if (this.hoveredSlot != null) {
            var3 = sl.X().o(Minecraft.getInstance(), this.hoveredSlot);
            if (var3 == null) {
               var3 = qo.Z().G(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null) {
               var3 = p2.p().d(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null) {
               var3 = pp.A().P(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null && this.hoveredSlot.container instanceof Inventory) {
               ItemStack var4 = qo.Z().g(this.hoveredSlot.getContainerSlot(), var1);
               var4 = sl.X().V(var4);
               ItemStack var5 = vy.a().getVisualOverride(var4);
               if (var5 != null) {
                  var4 = var5;
               }

               var4 = p2.p().j(var4);
               var4 = pp.A().F(var4);
               var4 = hh.R().o(this.hoveredSlot.getContainerSlot(), var4);
               if (var4 != var1) {
                  var3 = var4;
               }

               ItemStack var6 = ig.r().q(this.hoveredSlot.getContainerSlot(), var1);
               if (var3 == null && var6 != null) {
                  var3 = var6;
               }
            }

            if (var3 == null && this.O(this.hoveredSlot)) {
               var3 = ig.r().Y(Minecraft.getInstance(), this.hoveredSlot.getContainerSlot(), var1);
            }
         }

         if (var3 != null) {
            if (var3.isEmpty()) {
               var2.setReturnValue(List.of());
            } else {
               this.m = true;

               try {
                  var2.setReturnValue(this.getTooltipFromContainerItem(var3));
               } finally {
                  this.m = false;
               }
            }
         }
      }
   }
}
