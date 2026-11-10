package com.larpclient;

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

public enum ll {
   o("Skeleton", EntityTypes.SKELETON, Items.BONE, Items.ARROW, null),
   C("Iron Golem", EntityTypes.IRON_GOLEM, Items.IRON_INGOT, Items.AIR, "MHF_Golem"),
   f("Zombified Piglin", EntityTypes.ZOMBIFIED_PIGLIN, Items.ROTTEN_FLESH, Items.GOLD_NUGGET, "MHF_PigZombie");

   public final String n;
   public final EntityType<?> v;
   public final Item Z;
   public final Item t;
   private final String O;
   private ResolvableProfile y;

   ll(String var3, EntityType<?> var4, Item var5, Item var6, String var7) {
      this.n = var3;
      this.v = var4;
      this.Z = var5;
      this.t = var6;
      this.O = var7;
   }

   public ItemStack m() {
      ItemStack var1 = new ItemStack(this.O == null ? Items.SKELETON_SKULL : Items.PLAYER_HEAD);
      if (this.O != null) {
         if (this.y == null) {
            JsonObject var2 = new JsonObject();
            var2.addProperty("name", this.O);
            this.y = (ResolvableProfile)ResolvableProfile.CODEC.parse(JsonOps.INSTANCE, var2).getOrThrow();
         }

         var1.set(DataComponents.PROFILE, this.y);
         var1.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.PROFILE, true));
      }

      return var1;
   }

   public static boolean r(Item var0) {
      for (ll var4 : values()) {
         if (var0 == var4.Z || var0 != Items.AIR && var0 == var4.t) {
            return true;
         }
      }

      return false;
   }
}
