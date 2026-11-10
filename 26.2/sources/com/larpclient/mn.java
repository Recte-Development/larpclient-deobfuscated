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

public class mn extends ContainerScreen {
   private final BlockPos n;
   private ItemStack Q = ItemStack.EMPTY;
   private int Y = -1;
   private long j;
   private long g;
   private int X;
   private int A;
   private boolean v;
   private static final SimpleContainer V = new SimpleContainer(1);

   private void o() {
      if (!this.Q.isEmpty()) {
         if (this.Y < 54) {
            ((ChestMenu)this.menu).getSlot(this.Y).set(this.Q);
         }

         this.Q = ItemStack.EMPTY;
         this.Y = -1;
      }
   }

   private void i(int var1) {
      this.Y = var1;
      Slot var2 = ((ChestMenu)this.menu).getSlot(var1);
      this.Q = (var1 < 54 ? var2 : ki.x(this.menu, var2)).getItem().copy();
      if (var1 < 54) {
         var2.set(ItemStack.EMPTY);
      }

      ((ChestMenu)this.menu).setCarried(this.Q);
      this.j = System.currentTimeMillis() + 200L;
   }

   private int W(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 45; var3++) {
         if (((ChestMenu)this.menu).getSlot(var3).getItem().is(var1)) {
            var2 += ((ChestMenu)this.menu).getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   public mn(ChestMenu var1, Inventory var2, Component var3) {
      this(var1, var2, var3, null);
   }

   public mn(ChestMenu var1, Inventory var2, Component var3, BlockPos var4) {
      super(var1, var2, var3);
      this.n = var4 == null ? null : var4.immutable();
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.Q.isEmpty() && System.currentTimeMillis() >= this.j) {
         this.o();
      }

      if (this.v && System.currentTimeMillis() >= this.g) {
         this.v = (boolean)0;
         this.minecraft.gui.setScreen(new t3(this.n, b8.k().P(this.n), this.X, this.A));
      } else {
         if (this.minecraft.player != null) {
            ((ChestMenu)this.menu).setCarried(this.Q.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.Q);
         }

         super.extractRenderState(var1, var2, var3, var4);
         this.extractTooltip(var1, var2, var3);
      }
   }

   protected void extractSlot(GuiGraphicsExtractor var1, Slot var2, int var3, int var4) {
      if (this.Y >= 54 && var2.index == this.Y && !this.Q.isEmpty()) {
         Slot var5 = new Slot(V, 0, var2.x, var2.y);
         var5.index = var2.index;
         super.extractSlot(var1, var5, var3, var4);
      } else {
         super.extractSlot(var1, var2, var3, var4);
      }
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.Q.isEmpty() && !this.v) {
         if (k2.w(var1) != 0 && k2.w(var1) != 1) {
            return true;
         }

         for (int var3 = 0; var3 < ((ChestMenu)this.menu).slots.size(); var3++) {
            Slot var4 = (Slot)((ChestMenu)this.menu).slots.get(var3);
            int var5 = this.leftPos + var4.x;
            int var6 = this.topPos + var4.y;
            if (!(var1.x() < var5) && !(var1.x() >= var5 + 16) && !(var1.y() < var6) && !(var1.y() >= var6 + 16)) {
               b8 var7 = b8.k();
               if (var3 < 45 && ll.r(var4.getItem().getItem())) {
                  int var11 = !this.minecraft.options.keyShift.isDown() && !this.minecraft.player.isShiftKeyDown() ? 0 : 1;
                  int var12 = var11 != 0
                     ? var7.c(var4.getItem().getItem(), var4.getItem().getCount())
                     : var7.f(var4.getItem().getItem(), k2.w(var1) == 1 ? 1 : var4.getItem().getCount());
                  if (var12 > 0) {
                     var4.getItem().shrink(var12);
                  }
               } else if ((var3 == 48 || var3 == 50) && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  ll var8 = var7.P(this.n);
                  int var9 = this.W(var8.Z);
                  int var10 = this.W(var8.t);
                  if (var9 + var10 == 0) {
                     this.i(var3);
                     if (var3 == 48) {
                        k4.f(Component.literal("Spawner has no loot").withStyle(ChatFormatting.RED), true);
                     }
                  } else if (var3 == 48) {
                     this.i(var3);
                     this.X = var9;
                     this.A = var10;
                     this.g = this.j + 50L;
                     this.v = (boolean)1;
                  }
               } else if (var3 == 49 && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  this.i(var3);
               } else if (var3 < 54) {
                  var7.S(var3);
               } else if (var3 >= 54 && !ki.x(this.menu, var4).getItem().isEmpty() && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  this.i(var3);
               }

               if (this.minecraft.player != null) {
                  ((ChestMenu)this.menu).setCarried(this.Q.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.Q);
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
      mm.d(this.n);
   }

   public void removed() {
      this.o();
   }
}
