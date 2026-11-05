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

public final class wk extends ContainerScreen {
   private final BlockPos p;
   private final cl C;
   private final int P;
   private final int f;
   private final long E;
   private ItemStack n = ItemStack.EMPTY;
   private int R = -1;
   private long e;
   private boolean K;

   public wk(BlockPos var1, cl var2, int var3, int var4) {
      super(D(var2, var3, var4), Minecraft.getInstance().player.getInventory(), Component.literal("Confirm Sell"));
      this.p = var1 == null ? null : var1.immutable();
      this.C = var2;
      this.P = var3;
      this.f = var4;
      dr var5 = dr.z();
      this.E = var5.d(var2.V, var3) + var5.d(var2.S, var4);
   }

   private static ChestMenu D(cl var0, int var1, int var2) {
      SimpleContainer var3 = new SimpleContainer(27);
      var3.setItem(
         11,
         w(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.RED)),
            Component.literal("Cancel").withStyle(ChatFormatting.WHITE),
            List.of(Component.literal("Click to cancel").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      ArrayList var4 = new ArrayList();
      if (var2 > 0) {
         var4.add(c(var0.S, var2));
      }

      if (var1 > 0) {
         var4.add(c(var0.V, var1));
      }

      var3.setItem(13, w(var0.a(), Component.literal("Loot").withStyle(ChatFormatting.WHITE), var4));
      long var5 = dr.z().d(var0.V, var1) + dr.z().d(var0.S, var2);
      var3.setItem(
         15,
         w(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.LIME)),
            dr.z().X(var5),
            List.of(Component.literal("Click to sell items").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      return new ChestMenu(MenuType.GENERIC_9x3, 0, Minecraft.getInstance().player.getInventory(), var3, 3);
   }

   private static ItemStack w(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1.copy().withStyle(var0x -> var0x.withItalic(false)));
      if (!var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component c(Item var0, int var1) {
      String var2 = var0 == Items.ROTTEN_FLESH ? "Rotten Flesh" : var0.getName(new ItemStack(var0)).getString() + "s";
      return Component.literal(var1 + " ")
         .withStyle(var0x -> var0x.withColor(64648).withItalic(false))
         .append(Component.literal(var2).withStyle(ChatFormatting.WHITE));
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   private void g() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
      this.minecraft.gui.setScreen(null);
      dr.z().S(this.p);
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.n.isEmpty() && System.currentTimeMillis() >= this.e) {
         if (this.K) {
            this.g();
            return;
         }

         ((ChestMenu)this.menu).getSlot(this.R).set(this.n);
         this.n = ItemStack.EMPTY;
      }

      ((ChestMenu)this.menu).setCarried(this.n.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.n);
      super.extractRenderState(var1, var2, var3, var4);
      this.extractTooltip(var1, var2, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.n.isEmpty() && (mo.Z(var1) == 0 || mo.Z(var1) == 1)) {
         for (int var6 : new int[]{11, 13, 15}) {
            Slot var7 = ((ChestMenu)this.menu).getSlot(var6);
            int var8 = this.leftPos + var7.x;
            int var9 = this.topPos + var7.y;
            if (!(var1.x() < var8) && !(var1.x() >= var8 + 16) && !(var1.y() < var9) && !(var1.y() >= var9 + 16)) {
               if (var6 == 11) {
                  this.g();
                  return true;
               }

               if (var6 == 15) {
                  if (this.E <= 0L) {
                     ws.c(Component.literal("Spawner loot price unavailable").withStyle(ChatFormatting.RED), true);
                     return true;
                  }

                  dr.z().f(this.p, this.C.V, this.P);
                  dr.z().f(this.p, this.C.S, this.f);
                  k9.S(this.E);
                  this.K = (boolean)1;
               }

               this.R = var6;
               this.n = var7.getItem().copy();
               var7.set(ItemStack.EMPTY);
               ((ChestMenu)this.menu).setCarried(this.n);
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
      this.g();
   }

   public void removed() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
   }
}
