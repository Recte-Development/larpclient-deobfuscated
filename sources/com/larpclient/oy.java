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

public class oy extends ContainerScreen {
   private final BlockPos B;
   private ItemStack A = ItemStack.EMPTY;
   private int E = -1;
   private long h;
   private long d;
   private int M;
   private int V;
   private boolean S;
   private static final SimpleContainer D = new SimpleContainer(1);

   private void i() {
      if (!this.A.isEmpty()) {
         if (this.E < 54) {
            ((ChestMenu)this.menu).getSlot(this.E).set(this.A);
         }

         this.A = ItemStack.EMPTY;
         this.E = -1;
      }
   }

   private void n(int var1) {
      this.E = var1;
      Slot var2 = ((ChestMenu)this.menu).getSlot(var1);
      this.A = (var1 < 54 ? var2 : mx.v(this.menu, var2)).getItem().copy();
      if (var1 < 54) {
         var2.set(ItemStack.EMPTY);
      }

      ((ChestMenu)this.menu).setCarried(this.A);
      this.h = System.currentTimeMillis() + 200L;
   }

   private int F(Item var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 45; var3++) {
         if (((ChestMenu)this.menu).getSlot(var3).getItem().is(var1)) {
            var2 += ((ChestMenu)this.menu).getSlot(var3).getItem().getCount();
         }
      }

      return var2;
   }

   public oy(ChestMenu var1, Inventory var2, Component var3) {
      this(var1, var2, var3, null);
   }

   public oy(ChestMenu var1, Inventory var2, Component var3, BlockPos var4) {
      super(var1, var2, var3);
      this.B = var4 == null ? null : var4.immutable();
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.A.isEmpty() && System.currentTimeMillis() >= this.h) {
         this.i();
      }

      if (this.S && System.currentTimeMillis() >= this.d) {
         this.S = (boolean)0;
         this.minecraft.gui.setScreen(new x4(this.B, lj.j().q(this.B), this.M, this.V));
      } else {
         if (this.minecraft.player != null) {
            ((ChestMenu)this.menu).setCarried(this.A.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.A);
         }

         super.extractRenderState(var1, var2, var3, var4);
         this.extractTooltip(var1, var2, var3);
      }
   }

   protected void extractSlot(GuiGraphicsExtractor var1, Slot var2, int var3, int var4) {
      if (this.E >= 54 && var2.index == this.E && !this.A.isEmpty()) {
         Slot var5 = new Slot(D, 0, var2.x, var2.y);
         var5.index = var2.index;
         super.extractSlot(var1, var5, var3, var4);
      } else {
         super.extractSlot(var1, var2, var3, var4);
      }
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.A.isEmpty() && !this.S) {
         if (r7.y(var1) != 0 && r7.y(var1) != 1) {
            return true;
         }

         for (int var3 = 0; var3 < ((ChestMenu)this.menu).slots.size(); var3++) {
            Slot var4 = (Slot)((ChestMenu)this.menu).slots.get(var3);
            int var5 = this.leftPos + var4.x;
            int var6 = this.topPos + var4.y;
            if (!(var1.x() < var5) && !(var1.x() >= var5 + 16) && !(var1.y() < var6) && !(var1.y() >= var6 + 16)) {
               lj var7 = lj.j();
               if (var3 < 45 && mb.j(var4.getItem().getItem())) {
                  int var11 = !this.minecraft.options.keyShift.isDown() && !this.minecraft.player.isShiftKeyDown() ? 0 : 1;
                  int var12 = var11 != 0
                     ? var7.w(var4.getItem().getItem(), var4.getItem().getCount())
                     : var7.p(var4.getItem().getItem(), r7.y(var1) == 1 ? 1 : var4.getItem().getCount());
                  if (var12 > 0) {
                     var4.getItem().shrink(var12);
                  }
               } else if ((var3 == 48 || var3 == 50) && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  mb var8 = var7.q(this.B);
                  int var9 = this.F(var8.N);
                  int var10 = this.F(var8.j);
                  if (var9 + var10 == 0) {
                     this.n(var3);
                     if (var3 == 48) {
                        jb.v(Component.literal("Spawner has no loot").withStyle(ChatFormatting.RED), true);
                     }
                  } else if (var3 == 48) {
                     this.n(var3);
                     this.M = var9;
                     this.V = var10;
                     this.d = this.h + 50L;
                     this.S = (boolean)1;
                  }
               } else if (var3 == 49 && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  this.n(var3);
               } else if (var3 < 54) {
                  var7.t(var3);
               } else if (var3 >= 54 && !mx.v(this.menu, var4).getItem().isEmpty() && this.minecraft.player.inventoryMenu.getCarried().isEmpty()) {
                  this.n(var3);
               }

               if (this.minecraft.player != null) {
                  ((ChestMenu)this.menu).setCarried(this.A.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.A);
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
      fv.P(this.B);
   }

   public void removed() {
      this.i();
   }
}
