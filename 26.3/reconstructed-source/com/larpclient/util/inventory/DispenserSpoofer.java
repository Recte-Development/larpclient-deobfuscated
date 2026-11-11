package com.larpclient.util.inventory;

import com.larpclient.util.misc.DropperGameSpoofer;
import com.larpclient.util.world.DispenserRegistry;
import java.util.stream.IntStream;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class DispenserSpoofer {
   private static final DispenserSpoofer instance = new DispenserSpoofer();
   public Item firstItem = Items.STONE;
   public Item secondItem = Items.COBBLESTONE;
   public Item middleItem = Items.DIAMOND;
   public DispenserSpoofer.SlotType selectedSlotType = DispenserSpoofer.SlotType.FIRST;
   public boolean enabled;
   private final DropperGameSpoofer engine = new DropperGameSpoofer(true);
   private final DispenserSpoofer.SlotType[] layout = new DispenserSpoofer.SlotType[9];
   private boolean sendingClick;
   private boolean wasActive;

   public DispenserSpoofer() {
      this.resetLayout();
   }

   public static DispenserSpoofer getInstance() {
      return instance;
   }

   public void cycleSlotType() {
      this.selectedSlotType = this.selectedSlotType.next();
   }

   public Item selectedItem() {
      return switch (this.selectedSlotType) {
         case FIRST -> this.firstItem;
         case SECOND -> this.secondItem;
         case MIDDLE -> this.middleItem;
      };
   }

   public boolean captureLayout(Minecraft var1, BlockPos var2, DispenserMenu var3) {
      this.engine.selectedRank = this.selectedSlotType.ordinal() + 1;
      if (var2 != null && var1 != null) {
         if (this.engine.isReady() && var2.equals(this.engine.hasDropper(0)) && this.engine.hasDrawn(var1)) {
            this.engine.trackMenu(var3, 0, true);
            return true;
         }

         Item var4 = null;
         Item var5 = null;
         Item var6 = null;
         int var7 = 0;

         for (int var8 = 0; var8 < 9; var8++) {
            ItemStack var9 = var3.getSlot(var8).getItem();
            if (!var9.isEmpty()) {
               var7++;
               if (var8 == 4) {
                  var6 = var9.getItem();
               } else if (var4 == null) {
                  var4 = var9.getItem();
               } else if (var9.getItem() != var4 && var5 == null) {
                  var5 = var9.getItem();
               }
            }
         }

         if (var7 == 0) {
            return false;
         }

         if (!this.engine.isReady() || !var2.equals(this.engine.hasDropper(0)) || var7 == 9) {
            if (var4 != null) {
               this.firstItem = var4;
            }

            if (var5 != null) {
               this.secondItem = var5;
            }

            if (var6 != null) {
               this.middleItem = var6;
            }
         }

         for (int var10 = 0; var10 < 9; var10++) {
            ItemStack var11 = var3.getSlot(var10).getItem();
            if (!var11.isEmpty()) {
               this.setSlotType(
                  var10,
                  var10 == 4
                     ? DispenserSpoofer.SlotType.MIDDLE
                     : (var11.is(this.secondItem) ? DispenserSpoofer.SlotType.SECOND : DispenserSpoofer.SlotType.FIRST)
               );
            }
         }

         this.engine.setupWithItems(var1, var2, var3, this.firstItem, this.secondItem, this.middleItem, this.encodeLayout());
         return true;
      } else {
         return false;
      }
   }

   public void resetLayout() {
      this.loadLayout(new int[]{1, 1, 2, 1, 3, 2, 1, 2, 2});
   }

   public DispenserSpoofer.SlotType getSlotType(int var1) {
      return var1 >= 0 && var1 < 9 ? this.layout[var1] : null;
   }

   public void setSlotType(int var1, DispenserSpoofer.SlotType var2) {
      if (var1 >= 0 && var1 < 9) {
         this.layout[var1] = var2;
      }
   }

   public void clearSlot(int var1) {
      this.setSlotType(var1, null);
   }

   public int[] encodeLayout() {
      int[] var1 = new int[9];

      for (int var2 = 0; var2 < 9; var2++) {
         var1[var2] = this.layout[var2] == null ? 0 : this.layout[var2].ordinal() + 1;
      }

      return var1;
   }

   public void loadLayout(int[] var1) {
      if (var1 != null && var1.length == 9) {
         for (int var2 = 0; var2 < 9; var2++) {
            this.layout[var2] = var1[var2] >= 1 && var1[var2] <= 3 ? DispenserSpoofer.SlotType.values()[var1[var2] - 1] : null;
         }
      } else {
         this.resetLayout();
      }
   }

   public int[] slotsOfType(DispenserSpoofer.SlotType var1) {
      return IntStream.range(0, 9).filter(var2 -> var1 != null && this.layout[var2] == var1).toArray();
   }

   public void onTick() {
   }

   public ItemStack spoofDisplayStack(ItemStack var1, boolean var2) {
      return this.spoofStack(var1);
   }

   public String spoofName(ItemStack var1, String var2) {
      return var2;
   }

   public static ItemStack buildSpoofStack(Item var0, ItemStack var1, boolean var2) {
      ItemStack var3 = new ItemStack(var0, var1.getCount());

      for (int var4 = 1; var4 <= 3; var4++) {
         ItemStack var5 = instance.engine.getSetupStack(var4);
         if (var5.is(var0)) {
            var3 = var5.copyWithCount(var1.getCount());
            break;
         }
      }

      if (var2 && var1.has(DataComponents.CUSTOM_NAME)) {
         var3.set(DataComponents.CUSTOM_NAME, (Component)var1.get(DataComponents.CUSTOM_NAME));
      }

      return var3;
   }

   private boolean getSlotType(Minecraft var1, DispenserMenu var2) {
      DispenserRegistry var3 = DispenserRegistry.getInstance();
      BlockPos var4 = this.engine.hasDropper(0);
      return this.enabled
         && var4 != null
         && var1 != null
         && var1.player != null
         && var2 == var1.player.containerMenu
         && var2.containerId == var3.getActiveKey()
         && var3.getActiveKey(var1, var4).equals(var3.getActiveKey());
   }

   public void updateActive(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (!this.enabled) {
            if (this.wasActive) {
               this.engine.reset(var1);
            }

            this.wasActive = (boolean)0;
         } else {
            this.wasActive = (boolean)1;
            this.engine.selectedRank = this.selectedSlotType.ordinal() + 1;
            this.engine.tick(var1);
         }
      } else {
         if (this.wasActive && var1 != null) {
            this.engine.reset(var1);
         }

         this.wasActive = (boolean)0;
      }
   }

   public ItemStack spoofStack(ItemStack var1) {
      return this.enabled && var1 != null && !var1.isEmpty() ? this.engine.getDisplayStack(var1) : var1;
   }

   public ItemStack spoofTooltipStack(ItemStack var1) {
      return this.spoofStack(var1);
   }

   public boolean isTotemBucketSpoof(ItemStack var1) {
      return (var1.is(Items.WATER_BUCKET) || var1.is(Items.LAVA_BUCKET)) && this.spoofStack(var1).is(Items.TOTEM_OF_UNDYING);
   }

   public ItemStack spoofForEntity(LivingEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      return var3 != null && var1 == var3.player ? this.spoofStack(var2) : var2;
   }

   public ItemStack spoofItemEntity(ItemEntity var1, ItemStack var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.enabled && var1 != null && var3 != null && var3.level != null) {
         ItemStack var4 = this.engine.getItemEntityDisplay(var3, var1);
         return var4 == var1.getItem() ? var2 : var4;
      } else {
         return var2;
      }
   }

   public ItemStack spoofSlot(Minecraft var1, Slot var2) {
      return var1 != null
            && var1.player != null
            && var2.container != var1.player.getInventory()
            && var1.player.containerMenu instanceof DispenserMenu var3
            && var2.getContainerSlot() >= 0
            && var2.getContainerSlot() < 9
            && this.getSlotType(var1, var3)
         ? this.engine.getMenuSlotDisplay(var1, var3, 0, var2.getContainerSlot())
         : null;
   }

   public boolean redirectClick(Minecraft var1, int var2, int var3, int var4, ContainerInput var5) {
      if (!this.sendingClick
         && var1 != null
         && var1.player != null
         && var1.gameMode != null
         && var1.player.containerMenu instanceof DispenserMenu var6
         && var6.containerId == var2
         && var3 >= 0
         && var3 < 9
         && this.getSlotType(var1, var6)) {
         int var12 = this.engine.trackMenu(var1, var6, 0, var3);
         int var8 = var5 == ContainerInput.SWAP && var4 >= 0 && var4 < 9 ? var4 : (var5 == ContainerInput.PICKUP && var4 == 0 ? 36 : -1);
         if (var8 >= 0 && this.engine.trySwapClick(var1, var6, var12, var8)) {
            return true;
         }

         if (var12 == var3) {
            return false;
         }

         this.sendingClick = (boolean)1;

         try {
            var1.gameMode.handleContainerInput(var2, var12, var4, var5, var1.player);
         } finally {
            this.sendingClick = (boolean)0;
         }

         return true;
      } else {
         return false;
      }
   }

   public enum SlotType {
      FIRST("1st"),
      SECOND("2nd"),
      MIDDLE("Middle");

      public final String LABEL;

      SlotType(String var3) {
         this.LABEL = var3;
      }

      public DispenserSpoofer.SlotType next() {
         return switch (this) {
            case FIRST -> MIDDLE;
            case SECOND -> FIRST;
            case MIDDLE -> SECOND;
         };
      }
   }
}
