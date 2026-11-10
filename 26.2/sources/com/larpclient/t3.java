package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.ItemLike;

public final class t3 extends ContainerScreen {
   private final BlockPos j;
   private final ll A;
   private final int v;
   private final int R;
   private final long z;
   private ItemStack K = ItemStack.EMPTY;
   private int m = -1;
   private long e;
   private boolean C;

   public t3(BlockPos var1, ll var2, int var3, int var4) {
      super(F(var2, var3, var4), Minecraft.getInstance().player.getInventory(), Component.literal("Confirm Sell"));
      this.j = var1 == null ? null : var1.immutable();
      this.A = var2;
      this.v = var3;
      this.R = var4;
      b8 var5 = b8.k();
      this.z = var5.n(var2.Z, var3) + var5.n(var2.t, var4);
   }

   private static ChestMenu F(ll var0, int var1, int var2) {
      SimpleContainer var3 = new SimpleContainer(27);
      var3.setItem(
         11,
         p(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.RED)),
            Component.literal("Cancel").withStyle(ChatFormatting.WHITE),
            List.of(Component.literal("Click to cancel").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      ArrayList var4 = new ArrayList();
      if (var2 > 0) {
         var4.add(W(var0.t, var2));
      }

      if (var1 > 0) {
         var4.add(W(var0.Z, var1));
      }

      var3.setItem(13, p(var0.m(), Component.literal("Loot").withStyle(ChatFormatting.WHITE), var4));
      long var5 = b8.k().n(var0.Z, var1) + b8.k().n(var0.t, var2);
      var3.setItem(
         15,
         p(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.LIME)),
            b8.k().O(var5),
            List.of(Component.literal("Click to sell items").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      return new ChestMenu(MenuType.GENERIC_9x3, 0, Minecraft.getInstance().player.getInventory(), var3, 3);
   }

   private static ItemStack p(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1.copy().withStyle(var0x -> var0x.withItalic(false)));
      if (!var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component W(Item var0, int var1) {
      String var2 = var0 == Items.ROTTEN_FLESH ? "Rotten Flesh" : var0.getName(new ItemStack(var0)).getString() + "s";
      return Component.literal(var1 + " ")
         .withStyle(var0x -> var0x.withColor(64648).withItalic(false))
         .append(Component.literal(var2).withStyle(ChatFormatting.WHITE));
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   private void X() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
      this.minecraft.gui.setScreen(null);
      b8.k().V(this.j);
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.K.isEmpty() && System.currentTimeMillis() >= this.e) {
         if (this.C) {
            this.X();
            return;
         }

         ((ChestMenu)this.menu).getSlot(this.m).set(this.K);
         this.K = ItemStack.EMPTY;
      }

      ((ChestMenu)this.menu).setCarried(this.K.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.K);
      super.extractRenderState(var1, var2, var3, var4);
      this.extractTooltip(var1, var2, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.K.isEmpty() && (k2.w(var1) == 0 || k2.w(var1) == 1)) {
         for (int var6 : new int[]{11, 13, 15}) {
            Slot var7 = ((ChestMenu)this.menu).getSlot(var6);
            int var8 = this.leftPos + var7.x;
            int var9 = this.topPos + var7.y;
            if (!(var1.x() < var8) && !(var1.x() >= var8 + 16) && !(var1.y() < var9) && !(var1.y() >= var9 + 16)) {
               if (var6 == 11) {
                  this.X();
                  return true;
               }

               if (var6 == 15) {
                  if (this.z <= 0L) {
                     k4.f(Component.literal("Spawner loot price unavailable").withStyle(ChatFormatting.RED), true);
                     return true;
                  }

                  b8.k().S(this.j, this.A.Z, this.v);
                  b8.k().S(this.j, this.A.t, this.R);
                  za.j(this.z);
                  this.C = (boolean)1;
               }

               this.m = var6;
               this.K = var7.getItem().copy();
               var7.set(ItemStack.EMPTY);
               ((ChestMenu)this.menu).setCarried(this.K);
               this.e = System.currentTimeMillis() + 200L;
               return true;
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
      this.X();
   }

   public void removed() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
   }
}
