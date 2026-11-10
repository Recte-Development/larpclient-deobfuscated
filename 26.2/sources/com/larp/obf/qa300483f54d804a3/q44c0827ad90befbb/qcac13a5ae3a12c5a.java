package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.bs;
import com.larpclient.cc;
import com.larpclient.cy;
import com.larpclient.g5;
import com.larpclient.gj;
import com.larpclient.lh;
import com.larpclient.mg;
import com.larpclient.pq;
import com.larpclient.pw;
import com.larpclient.t5;
import com.larpclient.we;
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
public class qcac13a5ae3a12c5a {
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
   private boolean P;
   private boolean X;

   @Shadow
   protected List<Component> getTooltipFromContainerItem(ItemStack var1) {
      throw new AssertionError();
   }

   @Inject(method = "onClose", at = @At("HEAD"), cancellable = true)
   private void a(CallbackInfo var1) {
      pw var2 = pw.S();
      Minecraft var3 = Minecraft.getInstance();
      AbstractContainerScreen var4 = (AbstractContainerScreen)this;
      if (lh.I().w(var4.getMenu())) {
         var3.gui.setScreen(null);
         var1.cancel();
      } else if (var2.K() && var2.X() && var4.getMenu() == var3.player.containerMenu && var4.getMenu().getCarried().isEmpty()) {
         var3.gui.setScreen(null);
         var1.cancel();
      } else {
         we var5 = we.Y();
         if (var5.p(((AbstractContainerScreen)this).getMenu())) {
            Minecraft.getInstance().gui.setScreen(null);
            var1.cancel();
         } else if (var5.blocksClose()) {
            var1.cancel();
         }
      }
   }

   private boolean V(Slot var1) {
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

      gj var3 = gj.c();
      return var3.E() == gj.d.g && var3.r() == var2.containerId;
   }

   @ModifyVariable(method = "extractSlot", at = @At("STORE"), ordinal = 0)
   private ItemStack S(ItemStack var1, GuiGraphicsExtractor var2, Slot var3, int var4, int var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (var6.player != null && var3.container == var6.player.getInventory()) {
         ItemStack var7 = we.Y().getInventorySlotProjection(var3.getContainerSlot());
         if (var7 != null) {
            return var7;
         }

         ItemStack var8 = lh.I().z(var1);
         var8 = pw.S().C(var8);
         var8 = cy.Q().D(var8);
         var8 = g5.a().y(var3.getContainerSlot(), var8);
         var8 = t5.O().U(var8);
         var8 = pq.Q().P(var8);
         ItemStack var9 = bs.R().W(var3.getContainerSlot(), var8);
         if (var9 != null) {
            var8 = var9;
         }

         return cc.z().u(var3.getContainerSlot(), var8);
      } else {
         return var1;
      }
   }

   @Inject(method = "extractSlot", at = @At("HEAD"), cancellable = true)
   private void C(GuiGraphicsExtractor var1, Slot var2, int var3, int var4, CallbackInfo var5) {
      if (var2 != null) {
         ItemStack var6 = var2.container instanceof Inventory ? null : mg.U(var2);
         if (var6 != null) {
            if (!var6.isEmpty()) {
               var1.item(var6, var2.x, var2.y);
               var1.itemDecorations(Minecraft.getInstance().font, var6, var2.x, var2.y);
            }

            var5.cancel();
            return;
         }

         cc var7 = cc.z();
         boolean var8 = var2.container instanceof Inventory && var7.v() && var2.getContainerSlot() == var7.W();
         if (var8) {
            boolean var9 = var3 - this.leftPos >= var2.x
               && var3 - this.leftPos < var2.x + 16
               && var4 - this.topPos >= var2.y
               && var4 - this.topPos < var2.y + 16;
            if (this.isQuickCrafting) {
               if (var9) {
                  this.X = true;
               }
            } else {
               this.X = false;
            }

            if (var9 || this.X) {
               ItemStack var10 = ((AbstractContainerScreen)this).getMenu().getCarried();
               if (!var10.isEmpty()) {
                  ItemStack var11 = this.p(var10);
                  var1.item(var11, var2.x, var2.y);
                  var1.itemDecorations(Minecraft.getInstance().font, var11, var2.x, var2.y);
                  var5.cancel();
                  return;
               }
            }
         }

         ItemStack var12 = lh.I().j(Minecraft.getInstance(), ((AbstractContainerScreen)this).getMenu(), var2);
         if (var12 == null) {
            var12 = pw.S().k(Minecraft.getInstance(), ((AbstractContainerScreen)this).getMenu(), var2);
         }

         if (var12 == null) {
            var12 = cy.Q().E(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = g5.a().i(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = t5.O().m(Minecraft.getInstance(), var2);
         }

         if (var12 == null) {
            var12 = pq.Q().Y(Minecraft.getInstance(), var2);
         }

         if (var12 == null && !(var2.container instanceof Inventory) && this.V(var2)) {
            ItemStack var13 = bs.R().u(Minecraft.getInstance(), var2.getContainerSlot(), var2.getItem());
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
   private void A(MouseButtonEvent var1, double var2, double var4, CallbackInfoReturnable<Boolean> var6) {
      cc var7 = cc.z();
      if (var7.v() && !this.quickCraftSlots.isEmpty()) {
         int var8 = var7.W();
         this.quickCraftSlots.removeIf(var1x -> var1x.container instanceof Inventory && var1x.getContainerSlot() == var8);
      }
   }

   @Inject(method = "mouseReleased", at = @At("HEAD"))
   private void t(MouseButtonEvent var1, CallbackInfoReturnable<Boolean> var2) {
      cc var3 = cc.z();
      if (var3.v()) {
         int var4 = var3.W();
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
                  var3.M(this.p(var8));
               }
            }
         }
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"), cancellable = true)
   private void Q(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      we.Y().e(((AbstractContainerScreen)this).getMenu());
      if (we.Y().blocksUserInteraction() || we.Y().p(((AbstractContainerScreen)this).getMenu())) {
         var5.cancel();
      } else if (var1 != null && var1.container instanceof Inventory) {
         cc var6 = cc.z();
         if (var6.v() && var1.getContainerSlot() == var6.W()) {
            if (var4 == ContainerInput.PICKUP && !var6.g().isEmpty()) {
               var6.n();
            }

            var5.cancel();
         }
      }
   }

   @Inject(method = "slotClicked(Lnet/minecraft/world/inventory/Slot;IILnet/minecraft/world/inventory/ContainerInput;)V", at = @At("HEAD"))
   private void e(Slot var1, int var2, int var3, ContainerInput var4, CallbackInfo var5) {
      if (var4 == ContainerInput.CLONE && var1 != null) {
         bs var6 = bs.R();
         Minecraft var7 = Minecraft.getInstance();
         if (var1.container instanceof Inventory) {
            var6.E(var1.getItem(), var6.W(var1.getContainerSlot(), var1.getItem()));
         } else if (this.V(var1)) {
            var6.E(var1.getItem(), var6.u(var7, var1.getContainerSlot(), var1.getItem()));
         }
      }
   }

   private ItemStack p(ItemStack var1) {
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
   private ItemStack K(AbstractContainerMenu var1, Operation<ItemStack> var2) {
      if (we.Y().hidesCursor(var1)) {
         return ItemStack.EMPTY;
      }

      ItemStack var3 = cy.Q().D(mg.J((ItemStack)var2.call(new Object[]{var1})));
      var3 = g5.a().E(var3);
      var3 = bs.R().n(var3);
      return pq.Q().P(t5.O().k(var3));
   }

   @WrapOperation(
      method = "extractTooltip",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getCarried()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack q(AbstractContainerMenu var1, Operation<ItemStack> var2) {
      if (we.Y().hidesCursor(var1)) {
         return ItemStack.EMPTY;
      }

      ItemStack var3 = cy.Q().D(mg.J((ItemStack)var2.call(new Object[]{var1})));
      var3 = g5.a().E(var3);
      var3 = bs.R().n(var3);
      return pq.Q().P(t5.O().k(var3));
   }

   @Inject(method = "extractTooltip", at = @At("HEAD"), cancellable = true)
   private void J(GuiGraphicsExtractor var1, int var2, int var3, CallbackInfo var4) {
      Minecraft var5 = Minecraft.getInstance();
      if (var5.player != null && this.hoveredSlot != null) {
         ItemStack var6 = lh.I().j(var5, ((AbstractContainerScreen)this).getMenu(), this.hoveredSlot);
         if (var6 == null) {
            var6 = pw.S().k(var5, ((AbstractContainerScreen)this).getMenu(), this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = mg.U(this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = cy.Q().E(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = g5.a().i(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = t5.O().m(var5, this.hoveredSlot);
         }

         if (var6 == null) {
            var6 = pq.Q().Y(var5, this.hoveredSlot);
         }

         if (var6 == null && this.hoveredSlot.container == var5.player.getInventory()) {
            var6 = we.Y().getInventorySlotProjection(this.hoveredSlot.getContainerSlot());
         }

         if (var6 != null) {
            AbstractContainerMenu var7 = ((AbstractContainerScreen)this).getMenu();
            if (!var6.isEmpty() && (var7.getCarried().isEmpty() || we.Y().hidesCursor(var7))) {
               this.P = true;

               try {
                  var1.setTooltipForNextFrame(
                     var5.font, this.getTooltipFromContainerItem(var6), var6.getTooltipImage(), var2, var3, (Identifier)var6.get(DataComponents.TOOLTIP_STYLE)
                  );
               } finally {
                  this.P = false;
               }
            }

            var4.cancel();
         }
      }
   }

   @Inject(method = "getTooltipFromContainerItem", at = @At("HEAD"), cancellable = true)
   private void g(ItemStack var1, CallbackInfoReturnable<List<Component>> var2) {
      if (!this.P && var1 != null && !var1.isEmpty()) {
         ItemStack var3 = null;
         if (this.hoveredSlot != null) {
            var3 = cy.Q().E(Minecraft.getInstance(), this.hoveredSlot);
            if (var3 == null) {
               var3 = g5.a().i(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null) {
               var3 = t5.O().m(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null) {
               var3 = pq.Q().Y(Minecraft.getInstance(), this.hoveredSlot);
            }

            if (var3 == null && this.hoveredSlot.container instanceof Inventory) {
               ItemStack var4 = g5.a().y(this.hoveredSlot.getContainerSlot(), var1);
               var4 = cy.Q().D(var4);
               ItemStack var5 = we.Y().getVisualOverride(var4);
               if (var5 != null) {
                  var4 = var5;
               }

               var4 = t5.O().U(var4);
               var4 = pq.Q().P(var4);
               var4 = cc.z().u(this.hoveredSlot.getContainerSlot(), var4);
               if (var4 != var1) {
                  var3 = var4;
               }

               ItemStack var6 = bs.R().W(this.hoveredSlot.getContainerSlot(), var1);
               if (var3 == null && var6 != null) {
                  var3 = var6;
               }
            }

            if (var3 == null && this.V(this.hoveredSlot)) {
               var3 = bs.R().u(Minecraft.getInstance(), this.hoveredSlot.getContainerSlot(), var1);
            }
         }

         if (var3 != null) {
            if (var3.isEmpty()) {
               var2.setReturnValue(List.of());
            } else {
               this.P = true;

               try {
                  var2.setReturnValue(this.getTooltipFromContainerItem(var3));
               } finally {
                  this.P = false;
               }
            }
         }
      }
   }
}
