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

public enum cl {
   A("Skeleton", EntityTypes.SKELETON, Items.BONE, Items.ARROW, null),
   k("Iron Golem", EntityTypes.IRON_GOLEM, Items.IRON_INGOT, Items.AIR, "MHF_Golem"),
   h("Zombified Piglin", EntityTypes.ZOMBIFIED_PIGLIN, Items.ROTTEN_FLESH, Items.GOLD_NUGGET, "MHF_PigZombie");

   public final String Z;
   public final EntityType<?> w;
   public final Item V;
   public final Item S;
   private final String t;
   private ResolvableProfile i;

   cl(String var3, EntityType<?> var4, Item var5, Item var6, String var7) {
      this.Z = var3;
      this.w = var4;
      this.V = var5;
      this.S = var6;
      this.t = var7;
   }

   public ItemStack a() {
      ItemStack var1 = new ItemStack(this.t == null ? Items.SKELETON_SKULL : Items.PLAYER_HEAD);
      if (this.t != null) {
         if (this.i == null) {
            JsonObject var2 = new JsonObject();
            var2.addProperty("name", this.t);
            this.i = (ResolvableProfile)ResolvableProfile.CODEC.parse(JsonOps.INSTANCE, var2).getOrThrow();
         }

         var1.set(DataComponents.PROFILE, this.i);
         var1.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.PROFILE, true));
      }

      return var1;
   }

   public static boolean b(Item var0) {
      for (cl var4 : values()) {
         if (var0 == var4.V || var0 != Items.AIR && var0 == var4.S) {
            return true;
         }
      }

      return false;
   }
}
