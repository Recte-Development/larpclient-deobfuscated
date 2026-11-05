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

public final class x4 extends ContainerScreen {
   private final BlockPos E;
   private final mb k;
   private final int y;
   private final int f;
   private final long L;
   private ItemStack v = ItemStack.EMPTY;
   private int p = -1;
   private long s;
   private boolean l;

   public x4(BlockPos var1, mb var2, int var3, int var4) {
      super(U(var2, var3, var4), Minecraft.getInstance().player.getInventory(), Component.literal("Confirm Sell"));
      this.E = var1 == null ? null : var1.immutable();
      this.k = var2;
      this.y = var3;
      this.f = var4;
      lj var5 = lj.j();
      this.L = var5.t(var2.N, var3) + var5.t(var2.j, var4);
   }

   private static ChestMenu U(mb var0, int var1, int var2) {
      SimpleContainer var3 = new SimpleContainer(27);
      var3.setItem(
         11,
         y(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.RED)),
            Component.literal("Cancel").withStyle(ChatFormatting.WHITE),
            List.of(Component.literal("Click to cancel").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      ArrayList var4 = new ArrayList();
      if (var2 > 0) {
         var4.add(G(var0.j, var2));
      }

      if (var1 > 0) {
         var4.add(G(var0.N, var1));
      }

      var3.setItem(13, y(var0.y(), Component.literal("Loot").withStyle(ChatFormatting.WHITE), var4));
      long var5 = lj.j().t(var0.N, var1) + lj.j().t(var0.j, var2);
      var3.setItem(
         15,
         y(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.LIME)),
            lj.j().w(var5),
            List.of(Component.literal("Click to sell items").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      return new ChestMenu(MenuType.GENERIC_9x3, 0, Minecraft.getInstance().player.getInventory(), var3, 3);
   }

   private static ItemStack y(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1.copy().withStyle(var0x -> var0x.withItalic(false)));
      if (!var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component G(Item var0, int var1) {
      String var2 = var0 == Items.ROTTEN_FLESH ? "Rotten Flesh" : var0.getName(new ItemStack(var0)).getString() + "s";
      return Component.literal(var1 + " ")
         .withStyle(var0x -> var0x.withColor(64648).withItalic(false))
         .append(Component.literal(var2).withStyle(ChatFormatting.WHITE));
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   private void R() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
      this.minecraft.gui.setScreen(null);
      lj.j().V(this.E);
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.v.isEmpty() && System.currentTimeMillis() >= this.s) {
         if (this.l) {
            this.R();
            return;
         }

         ((ChestMenu)this.menu).getSlot(this.p).set(this.v);
         this.v = ItemStack.EMPTY;
      }

      ((ChestMenu)this.menu).setCarried(this.v.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.v);
      super.extractRenderState(var1, var2, var3, var4);
      this.extractTooltip(var1, var2, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.v.isEmpty() && (r7.y(var1) == 0 || r7.y(var1) == 1)) {
         for (int var6 : new int[]{11, 13, 15}) {
            Slot var7 = ((ChestMenu)this.menu).getSlot(var6);
            int var8 = this.leftPos + var7.x;
            int var9 = this.topPos + var7.y;
            if (!(var1.x() < var8) && !(var1.x() >= var8 + 16) && !(var1.y() < var9) && !(var1.y() >= var9 + 16)) {
               if (var6 == 11) {
                  this.R();
                  return true;
               }

               if (var6 == 15) {
                  if (this.L <= 0L) {
                     jb.v(Component.literal("Spawner loot price unavailable").withStyle(ChatFormatting.RED), true);
                     return true;
                  }

                  lj.j().B(this.E, this.k.N, this.y);
                  lj.j().B(this.E, this.k.j, this.f);
                  ul.p(this.L);
                  this.l = (boolean)1;
               }

               this.p = var6;
               this.v = var7.getItem().copy();
               var7.set(ItemStack.EMPTY);
               ((ChestMenu)this.menu).setCarried(this.v);
               this.s = System.currentTimeMillis() + 200L;
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
      this.R();
   }

   public void removed() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
   }
}
