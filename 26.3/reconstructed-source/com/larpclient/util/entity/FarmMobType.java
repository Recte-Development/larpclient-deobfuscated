package com.larpclient.util.entity;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.item.component.TooltipDisplay;

public enum FarmMobType {
   sKELETON("Skeleton", EntityTypes.SKELETON, Items.BONE, Items.ARROW, null),
   iRON_GOLEM("Iron Golem", EntityTypes.IRON_GOLEM, Items.IRON_INGOT, Items.AIR, "MHF_Golem"),
   zOMBIFIED_PIGLIN("Zombified Piglin", EntityTypes.ZOMBIFIED_PIGLIN, Items.ROTTEN_FLESH, Items.GOLD_NUGGET, "MHF_PigZombie");

   public final String displayName;
   public final EntityType<?> entityType;
   public final Item primaryDrop;
   public final Item secondaryDrop;
   private final String skullOwner;
   private ResolvableProfile profile;

   FarmMobType(String var3, EntityType<?> var4, Item var5, Item var6, String var7) {
      this.displayName = var3;
      this.entityType = var4;
      this.primaryDrop = var5;
      this.secondaryDrop = var6;
      this.skullOwner = var7;
   }

   public ItemStack createIcon() {
      ItemStack var1 = new ItemStack(this.skullOwner == null ? Items.SKELETON_SKULL : Items.PLAYER_HEAD);
      if (this.skullOwner != null) {
         if (this.profile == null) {
            JsonObject var2 = new JsonObject();
            var2.addProperty("name", this.skullOwner);
            this.profile = (ResolvableProfile)ResolvableProfile.CODEC.parse(JsonOps.INSTANCE, var2).getOrThrow();
         }

         var1.set(DataComponents.PROFILE, this.profile);
         var1.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.PROFILE, true));
      }

      return var1;
   }

   public static boolean isDropItem(Item var0) {
      for (FarmMobType var4 : values()) {
         if (var0 == var4.primaryDrop || var0 != Items.AIR && var0 == var4.secondaryDrop) {
            return true;
         }
      }

      return false;
   }
}
