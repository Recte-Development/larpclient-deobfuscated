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

public final class dq extends ContainerScreen {
   private final BlockPos M;
   private final ni w;
   private final int O;
   private final int Q;
   private final long y;
   private ItemStack D = ItemStack.EMPTY;
   private int V = -1;
   private long T;
   private boolean m;

   public dq(BlockPos var1, ni var2, int var3, int var4) {
      super(G(var2, var3, var4), Minecraft.getInstance().player.getInventory(), Component.literal("Confirm Sell"));
      this.M = var1 == null ? null : var1.immutable();
      this.w = var2;
      this.O = var3;
      this.Q = var4;
      jn var5 = jn.g();
      this.y = var5.v(var2.S, var3) + var5.v(var2.W, var4);
   }

   private static ChestMenu G(ni var0, int var1, int var2) {
      SimpleContainer var3 = new SimpleContainer(27);
      var3.setItem(
         11,
         z(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.RED)),
            Component.literal("Cancel").withStyle(ChatFormatting.WHITE),
            List.of(Component.literal("Click to cancel").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      ArrayList var4 = new ArrayList();
      if (var2 > 0) {
         var4.add(x(var0.W, var2));
      }

      if (var1 > 0) {
         var4.add(x(var0.S, var1));
      }

      var3.setItem(13, z(var0.D(), Component.literal("Loot").withStyle(ChatFormatting.WHITE), var4));
      long var5 = jn.g().v(var0.S, var1) + jn.g().v(var0.W, var2);
      var3.setItem(
         15,
         z(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.LIME)),
            jn.g().y(var5),
            List.of(Component.literal("Click to sell items").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      return new ChestMenu(MenuType.GENERIC_9x3, 0, Minecraft.getInstance().player.getInventory(), var3, 3);
   }

   private static ItemStack z(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1.copy().withStyle(var0x -> var0x.withItalic(false)));
      if (!var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component x(Item var0, int var1) {
      String var2 = var0 == Items.ROTTEN_FLESH ? "Rotten Flesh" : var0.getName(new ItemStack(var0)).getString() + "s";
      return Component.literal(var1 + " ")
         .withStyle(var0x -> var0x.withColor(64648).withItalic(false))
         .append(Component.literal(var2).withStyle(ChatFormatting.WHITE));
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   private void u() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
      this.minecraft.gui.setScreen(null);
      jn.g().d(this.M);
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.D.isEmpty() && System.currentTimeMillis() >= this.T) {
         if (this.m) {
            this.u();
            return;
         }

         ((ChestMenu)this.menu).getSlot(this.V).set(this.D);
         this.D = ItemStack.EMPTY;
      }

      ((ChestMenu)this.menu).setCarried(this.D.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.D);
      super.extractRenderState(var1, var2, var3, var4);
      this.extractTooltip(var1, var2, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.D.isEmpty() && (j2.L(var1) == 0 || j2.L(var1) == 1)) {
         for (int var6 : new int[]{11, 13, 15}) {
            Slot var7 = ((ChestMenu)this.menu).getSlot(var6);
            int var8 = this.leftPos + var7.x;
            int var9 = this.topPos + var7.y;
            if (!(var1.x() < var8) && !(var1.x() >= var8 + 16) && !(var1.y() < var9) && !(var1.y() >= var9 + 16)) {
               if (var6 == 11) {
                  this.u();
                  return true;
               }

               if (var6 == 15) {
                  if (this.y <= 0L) {
                     iw.V(Component.literal("Spawner loot price unavailable").withStyle(ChatFormatting.RED), true);
                     return true;
                  }

                  jn.g().r(this.M, this.w.S, this.O);
                  jn.g().r(this.M, this.w.W, this.Q);
                  h4.C(this.y);
                  this.m = (boolean)1;
               }

               this.V = var6;
               this.D = var7.getItem().copy();
               var7.set(ItemStack.EMPTY);
               ((ChestMenu)this.menu).setCarried(this.D);
               this.T = System.currentTimeMillis() + 200L;
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
      this.u();
   }

   public void removed() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
   }
}
