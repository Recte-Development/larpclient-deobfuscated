package com.larpclient.module;

import com.larpclient.sy;
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
   private int x = 0;
   private final m m = new m("Diamond", "Equip diamond armor upgrades", true);
   private final m p = new m("Netherite", "Equip netherite armor upgrades", true);
   private final m u = new m("Gold", "Equip gold armor upgrades", true);
   private final m h = new m("Iron", "Equip iron armor upgrades", true);
   private static final Set<Item> O = Set.of(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS);
   private static final Set<Item> U = Set.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS);
   private static final Set<Item> B = Set.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS);
   private static final Set<Item> G = Set.of(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS);
   private static final Set<Item> X = Set.of(
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
      this.addSetting(this.m);
      this.addSetting(this.p);
      this.addSetting(this.u);
      this.addSetting(this.h);
   }

   public List<r<?>> I() {
      return List.of(this.m, this.p, this.u, this.h);
   }

   public boolean g(ItemStack var1, EquipmentSlot var2) {
      if (!w(var1, var2)) {
         return false;
      } else {
         Item var3 = var1.getItem();
         if (O.contains(var3)) {
            return this.m.getValue();
         } else if (U.contains(var3)) {
            return this.p.getValue();
         } else if (B.contains(var3)) {
            return this.u.getValue();
         } else {
            return G.contains(var3) ? this.h.getValue() : X.contains(var3);
         }
      }
   }

   @Override
   public void onDisable() {
      this.x = 0;
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (mc.gui.screen() == null) {
            boolean var14 = false;
            int var13 = this.x;
            if ((-Integer.signum(var13) >>> 31 ^ 1) == 0) {
               byte var16 = 1;
               int var15 = this.x;
               this.x = var15 - var16;
            } else {
               for (EquipmentSlot var4 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
                  if (var4 != EquipmentSlot.CHEST || !sy.w()) {
                     int var5 = this.R(var4);
                     ItemStack var6 = mc.player.getItemBySlot(var4);
                     int var7 = D(var6, var4);
                     int var8 = -1;
                     int var9 = var7;

                     for (int var10 = 0; var10 < 36; var10++) {
                        ItemStack var11 = mc.player.getInventory().getItem(var10);
                        if (this.g(var11, var4)) {
                           int var12 = D(var11, var4);
                           if (var12 > var9) {
                              var9 = var12;
                              var8 = var10;
                           }
                        }
                     }

                     if (var8 != -1) {
                        int var17 = mc.player.containerMenu.containerId;
                        int var18 = var8 < 9 ? var8 + 36 : var8;
                        mc.gameMode.handleContainerInput(var17, var18, 0, ContainerInput.PICKUP, mc.player);
                        mc.gameMode.handleContainerInput(var17, var5, 0, ContainerInput.PICKUP, mc.player);
                        if (!var6.isEmpty()) {
                           mc.gameMode.handleContainerInput(var17, var18, 0, ContainerInput.PICKUP, mc.player);
                        }

                        this.x = 3;
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean w(ItemStack var0, EquipmentSlot var1) {
      if (var0.isEmpty()) {
         return false;
      }

      Item var2 = var0.getItem();
      if (!O.contains(var2) && !U.contains(var2) && !B.contains(var2) && !G.contains(var2) && !X.contains(var2)) {
         return false;
      }

      Equippable var3 = (Equippable)var0.get(DataComponents.EQUIPPABLE);
      return var3 != null && var3.slot() == var1;
   }

   private static int D(ItemStack var0, EquipmentSlot var1) {
      if (!var0.isEmpty() && w(var0, var1)) {
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

   private int R(EquipmentSlot var1) {
      return switch (var1) {
         case HEAD -> 5;
         case CHEST -> 6;
         case LEGS -> 7;
         case FEET -> 8;
         default -> -1;
      };
   }
}
