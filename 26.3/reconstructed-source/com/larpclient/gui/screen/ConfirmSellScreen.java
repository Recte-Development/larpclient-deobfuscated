package com.larpclient.gui.screen;

import com.larpclient.social.misc.PlayerMessages;
import com.larpclient.util.entity.FarmMobType;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.PayNotice;
import com.larpclient.util.world.FakeSpawnerController;
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

public final class ConfirmSellScreen extends ContainerScreen {
   private final BlockPos spawnerPos;
   private final FarmMobType loot;
   private final int primaryCount;
   private final int secondaryCount;
   private final long totalPrice;
   private ItemStack pendingStack = ItemStack.EMPTY;
   private int pendingSlot = -1;
   private long pendingUntil;
   private boolean selling;

   public ConfirmSellScreen(BlockPos var1, FarmMobType var2, int var3, int var4) {
      super(buildMenu(var2, var3, var4), Minecraft.getInstance().player.getInventory(), Component.literal("Confirm Sell"));
      this.spawnerPos = var1 == null ? null : var1.immutable();
      this.loot = var2;
      this.primaryCount = var3;
      this.secondaryCount = var4;
      FakeSpawnerController var5 = FakeSpawnerController.getInstance();
      this.totalPrice = var5.hasPendingPlacement(var2.primaryDrop, var3) + var5.hasPendingPlacement(var2.secondaryDrop, var4);
   }

   private static ChestMenu buildMenu(FarmMobType var0, int var1, int var2) {
      SimpleContainer var3 = new SimpleContainer(27);
      var3.setItem(
         11,
         createItem(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.RED)),
            Component.literal("Cancel").withStyle(ChatFormatting.WHITE),
            List.of(Component.literal("Click to cancel").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      ArrayList var4 = new ArrayList();
      if (var2 > 0) {
         var4.add(lootLine(var0.secondaryDrop, var2));
      }

      if (var1 > 0) {
         var4.add(lootLine(var0.primaryDrop, var1));
      }

      var3.setItem(13, createItem(var0.createIcon(), Component.literal("Loot").withStyle(ChatFormatting.WHITE), var4));
      long var5 = FakeSpawnerController.getInstance().hasPendingPlacement(var0.primaryDrop, var1)
         + FakeSpawnerController.getInstance().hasPendingPlacement(var0.secondaryDrop, var2);
      var3.setItem(
         15,
         createItem(
            new ItemStack((ItemLike)Items.STAINED_GLASS_PANE.pick(DyeColor.LIME)),
            FakeSpawnerController.getInstance().tickBreakAttempts(var5),
            List.of(Component.literal("Click to sell items").withStyle(new ChatFormatting[]{ChatFormatting.GRAY, ChatFormatting.ITALIC}))
         )
      );
      return new ChestMenu(MenuType.GENERIC_9x3, 0, Minecraft.getInstance().player.getInventory(), var3, 3);
   }

   private static ItemStack createItem(ItemStack var0, Component var1, List<Component> var2) {
      var0.set(DataComponents.CUSTOM_NAME, var1.copy().withStyle(var0x -> var0x.withItalic(false)));
      if (!var2.isEmpty()) {
         var0.set(DataComponents.LORE, new ItemLore(var2));
      }

      return var0;
   }

   private static Component lootLine(Item var0, int var1) {
      String var2 = var0 == Items.ROTTEN_FLESH ? "Rotten Flesh" : var0.getName(new ItemStack(var0)).getString() + "s";
      return Component.literal(var1 + " ")
         .withStyle(var0x -> var0x.withColor(64648).withItalic(false))
         .append(Component.literal(var2).withStyle(ChatFormatting.WHITE));
   }

   protected void addItemSlotMouseAction(ItemSlotMouseAction var1) {
   }

   private void closeScreen() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
      this.minecraft.gui.setScreen(null);
      FakeSpawnerController.getInstance().openLootMenu(this.spawnerPos);
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.pendingStack.isEmpty() && System.currentTimeMillis() >= this.pendingUntil) {
         if (this.selling) {
            this.closeScreen();
            return;
         }

         ((ChestMenu)this.menu).getSlot(this.pendingSlot).set(this.pendingStack);
         this.pendingStack = ItemStack.EMPTY;
      }

      ((ChestMenu)this.menu).setCarried(this.pendingStack.isEmpty() ? this.minecraft.player.inventoryMenu.getCarried() : this.pendingStack);
      super.extractRenderState(var1, var2, var3, var4);
      this.extractTooltip(var1, var2, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.pendingStack.isEmpty() && (KeyCodes.mouseButtonOf(var1) == 0 || KeyCodes.mouseButtonOf(var1) == 1)) {
         for (int var6 : new int[]{11, 13, 15}) {
            Slot var7 = ((ChestMenu)this.menu).getSlot(var6);
            int var8 = this.leftPos + var7.x;
            int var9 = this.topPos + var7.y;
            if (!(var1.x() < var8) && !(var1.x() >= var8 + 16) && !(var1.y() < var9) && !(var1.y() >= var9 + 16)) {
               if (var6 == 11) {
                  this.closeScreen();
                  return true;
               }

               if (var6 == 15) {
                  if (this.totalPrice <= 0L) {
                     PlayerMessages.sendMessage(Component.literal("Spawner loot price unavailable").withStyle(ChatFormatting.RED), true);
                     return true;
                  }

                  FakeSpawnerController.getInstance().savePiles(this.spawnerPos, this.loot.primaryDrop, this.primaryCount);
                  FakeSpawnerController.getInstance().savePiles(this.spawnerPos, this.loot.secondaryDrop, this.secondaryCount);
                  PayNotice.show(this.totalPrice);
                  this.selling = (boolean)1;
               }

               this.pendingSlot = var6;
               this.pendingStack = var7.getItem().copy();
               var7.set(ItemStack.EMPTY);
               ((ChestMenu)this.menu).setCarried(this.pendingStack);
               this.pendingUntil = System.currentTimeMillis() + 200L;
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
      this.closeScreen();
   }

   public void removed() {
      ((ChestMenu)this.menu).setCarried(ItemStack.EMPTY);
   }
}
