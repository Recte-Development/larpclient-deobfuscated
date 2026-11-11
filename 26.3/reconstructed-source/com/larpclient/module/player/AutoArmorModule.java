package com.larpclient.module.player;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.Setting;
import com.larpclient.module.movement.RocketBooster;
import java.util.List;
import java.util.Set;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.Equippable;

public class AutoArmorModule extends Module {
   private int cooldownTicks = 0;
   private final BooleanSetting diamond = new BooleanSetting("Diamond", "Equip diamond armor upgrades", true);
   private final BooleanSetting netherite = new BooleanSetting("Netherite", "Equip netherite armor upgrades", true);
   private final BooleanSetting gold = new BooleanSetting("Gold", "Equip gold armor upgrades", true);
   private final BooleanSetting iron = new BooleanSetting("Iron", "Equip iron armor upgrades", true);
   private static final Set<Item> diamondArmor = Set.of(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS);
   private static final Set<Item> netheriteArmor = Set.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS);
   private static final Set<Item> goldArmor = Set.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS);
   private static final Set<Item> ironArmor = Set.of(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS);
   private static final Set<Item> otherArmor = Set.of(
      Items.CHAINMAIL_HELMET,
      Items.CHAINMAIL_CHESTPLATE,
      Items.CHAINMAIL_LEGGINGS,
      Items.CHAINMAIL_BOOTS,
      Items.COPPER_HELMET,
      Items.COPPER_CHESTPLATE,
      Items.COPPER_LEGGINGS,
      Items.COPPER_BOOTS,
      Items.TURTLE_HELMET
   );

   public AutoArmorModule() {
      super("AutoArmor", "Automatically equip better allowed armor", Category.PLAYER, -1);
      this.addSetting(this.diamond);
      this.addSetting(this.netherite);
      this.addSetting(this.gold);
      this.addSetting(this.iron);
   }

   public List<Setting<?>> getMaterialSettings() {
      return List.of(this.diamond, this.netherite, this.gold, this.iron);
   }

   public boolean isAllowedArmor(ItemStack var1, EquipmentSlot var2) {
      if (!isArmorForSlot(var1, var2)) {
         return false;
      } else {
         Item var3 = var1.getItem();
         if (diamondArmor.contains(var3)) {
            return this.diamond.getValue();
         } else if (netheriteArmor.contains(var3)) {
            return this.netherite.getValue();
         } else if (goldArmor.contains(var3)) {
            return this.gold.getValue();
         } else {
            return ironArmor.contains(var3) ? this.iron.getValue() : otherArmor.contains(var3);
         }
      }
   }

   @Override
   public void onDisable() {
      this.cooldownTicks = 0;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (minecraft.gui.screen() == null) {
            boolean var14 = false;
            int var13 = this.cooldownTicks;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               byte var16 = 1;
               int var15 = this.cooldownTicks;
               this.cooldownTicks = var15 - var16;
            } else {
               for (EquipmentSlot var4 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                  if (var4 != EquipmentSlot.CHEST || !RocketBooster.findRocket()) {
                     int var5 = this.getArmorSlotId(var4);
                     ItemStack var6 = minecraft.player.getItemBySlot(var4);
                     int var7 = getArmorValue(var6, var4);
                     int var8 = -1;
                     int var9 = var7;

                     for (int var10 = 0; var10 < 36; var10++) {
                        ItemStack var11 = minecraft.player.getInventory().getItem(var10);
                        if (this.isAllowedArmor(var11, var4)) {
                           int var12 = getArmorValue(var11, var4);
                           if (var12 > var9) {
                              var9 = var12;
                              var8 = var10;
                           }
                        }
                     }

                     if (var8 != -1) {
                        int var17 = minecraft.player.containerMenu.containerId;
                        int var18 = var8 < 9 ? var8 + 36 : var8;
                        minecraft.gameMode.handleContainerInput(var17, var18, 0, ContainerInput.PICKUP, minecraft.player);
                        minecraft.gameMode.handleContainerInput(var17, var5, 0, ContainerInput.PICKUP, minecraft.player);
                        if (!var6.isEmpty()) {
                           minecraft.gameMode.handleContainerInput(var17, var18, 0, ContainerInput.PICKUP, minecraft.player);
                        }

                        this.cooldownTicks = 3;
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean isArmorForSlot(ItemStack var0, EquipmentSlot var1) {
      if (var0.isEmpty()) {
         return false;
      }

      Item var2 = var0.getItem();
      if (!diamondArmor.contains(var2)
         && !netheriteArmor.contains(var2)
         && !goldArmor.contains(var2)
         && !ironArmor.contains(var2)
         && !otherArmor.contains(var2)) {
         return false;
      }

      Equippable var3 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      return var3 != null && var3.slot() == var1;
   }

   private static int getArmorValue(ItemStack var0, EquipmentSlot var1) {
      if (!var0.isEmpty() && isArmorForSlot(var0, var1)) {
         double[] var2 = new double[]{0.0};
         var0.forEachModifier(var1, (var1x, var2x) -> {
            if (var1x.is(Attributes.ARMOR) && var2x.operation() == Operation.ADD_VALUE) {
               var2[0] += var2x.amount();
            }
         });
         return (int)Math.round(var2[0]);
      } else {
         return -1;
      }
   }

   private int getArmorSlotId(EquipmentSlot var1) {
      return switch (var1) {
         case HEAD -> 5;
         case CHEST -> 6;
         case LEGS -> 7;
         case FEET -> 8;
         default -> -1;
      };
   }
}
