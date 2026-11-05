package com.larpclient;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class eu extends ContainerScreen {
   private final BlockPos R;
   private ItemStack H = ItemStack.EMPTY;
   private int T = -1;
   private long p;
   private long K;
   private int r;
   private int l;
   private boolean d;
   private static final SimpleContainer C = new SimpleContainer(1);

   private void R() {
      if (!this.H.isEmpty()) {
         if (this.T < 54) {
            ((ChestMenu)this.menu).getSlot(this.T).set(this.H);
         }

         this.H = ItemStack.EMPTY;
         this.T = -1;
      }
   }

   private void x(int var1) {
      this.T = var1;
      Slot var2 = ((ChestMenu)this.menu).getSlot(var1);
      this.H = (var1 < 54 ? var2 : u0.W(this.menu, var2)).getItem().copy();
      if (var1 < 54) {
         var2.set(ItemStack.EMPTY);
      }

      ((ChestMenu)this.menu).setCarried(this.H);
      this.p = System.currentTimeMillis() + 200L;
   }

   private int P(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 45; var3++) {
         if (((ChestMenu)this.menu).getSlot(var3).getItem().is(var1)) {
            var2 += ((ChestMenu)this.menu).getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   public eu(ChestMenu var1, Inventory var2, Component var3) {
      this(var1, var2, var3, null);
   }

   public eu(ChestMenu var1, Inventory var2, Component var3, BlockPos var4) {
      super(var1, var2, var3);
      this.R = var4 == null ? null : var4.immutable();
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.H.isEmpty() && System.currentTimeMillis() >= this.p) {
         this.R();
      }

      if (this.d && System.currentTimeMillis() >= this.K) {
         this.d = (boolean)0;
         this.minecraft.gui.setScreen(new wk(this.R, dr.z().P(this.R), this.r, this.l));
      } else {
         if (this.minecraft.player != null) {
            ((ChestMenu)this.menu).setCarried(this.H.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.H);
         }

         super.extractRenderState(var1, var2, var3, var4);
         this.extractTooltip(var1, var2, var3);
      }
   }

   protected void extractSlot(GuiGraphicsExtractor var1, Slot var2, int var3, int var4) {
      if (this.T >= 54 && var2.index == this.T && !this.H.isEmpty()) {
         Slot var5 = new Slot(C, 0, var2.x, var2.y);
         var5.index = var2.index;
         super.extractSlot(var1, var5, var3, var4);
      } else {
         super.extractSlot(var1, var2, var3, var4);
      }
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.H.isEmpty() && !this.d) {
         if (mo.Z(var1) != 0 && mo.Z(var1) != 1) {
            return true;
         }

         for (int var3 = 0; var3 < ((ChestMenu)this.menu).slots.size(); var3++) {
            Slot var4 = (Slot)((ChestMenu)this.menu).slots.get(var3);
            int var5 = this.leftPos + var4.x;
            int var6 = this.topPos + var4.y;
            if (!(var1.x() < var5) && !(var1.x() >= var5 + 16) && !(var1.y() < var6) && !(var1.y() >= var6 + 16)) {
               dr var7 = dr.z();
               if (var3 < 45 && cl.b(var4.getItem().getItem())) {
                  int var11 = !this.minecraft.options.keyShift.isDown() && !this.minecraft.player.isShiftKeyDown() ? 0 : 1;
                  int var12 = var11 != 0
                     ? var7.p(var4.getItem().getItem(), var4.getItem().getCount())
                     : var7.s(var4.getItem().getItem(), mo.Z(var1) == 1 ? 1 : var4.getItem().getCount());
                  if (var12 > 0) {
                     var4.getItem().shrink(var12);
                  }
               } else if ((var3 == 48 || var3 == 50) && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  cl var8 = var7.P(this.R);
                  int var9 = this.P(var8.V);
                  int var10 = this.P(var8.S);
                  if (var9 + var10 == 0) {
                     this.x(var3);
                     if (var3 == 48) {
                        ws.c(Component.literal("Spawner has no loot").withStyle(ChatFormatting.RED), true);
                     }
                  } else if (var3 == 48) {
                     this.x(var3);
                     this.r = var9;
                     this.l = var10;
                     this.K = this.p + 50L;
                     this.d = (boolean)1;
                  }
               } else if (var3 == 49 && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  this.x(var3);
               } else if (var3 < 54) {
                  var7.Q(var3);
               } else if (var3 >= 54 && !u0.W(this.menu, var4).getItem().isEmpty() && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  this.x(var3);
               }

               if (this.minecraft.player != null) {
                  ((ChestMenu)this.menu).setCarried(this.H.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.H);
               }
               break;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   protected void slotClicked(Slot var1, int var2, int var3, ContainerInput var4) {
   }

   public void onClose() {
      this.minecraft.gui.setScreen(null);
      z_.r(this.R);
   }

   public void removed() {
      this.R();
   }
}
