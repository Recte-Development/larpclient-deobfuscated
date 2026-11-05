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

public enum mb {
   v("Skeleton", EntityTypes.SKELETON, Items.BONE, Items.ARROW, null),
   p("Iron Golem", EntityTypes.IRON_GOLEM, Items.IRON_INGOT, Items.AIR, "MHF_Golem"),
   R("Zombified Piglin", EntityTypes.ZOMBIFIED_PIGLIN, Items.ROTTEN_FLESH, Items.GOLD_NUGGET, "MHF_PigZombie");

   public final String x;
   public final EntityType<?> u;
   public final Item N;
   public final Item j;
   private final String K;
   private ResolvableProfile S;

   mb(String var3, EntityType<?> var4, Item var5, Item var6, String var7) {
      this.x = var3;
      this.u = var4;
      this.N = var5;
      this.j = var6;
      this.K = var7;
   }

   public ItemStack y() {
      ItemStack var1 = new ItemStack(this.K == null ? Items.SKELETON_SKULL : Items.PLAYER_HEAD);
      if (this.K != null) {
         if (this.S == null) {
            JsonObject var2 = new JsonObject();
            var2.addProperty("name", this.K);
            this.S = (ResolvableProfile)ResolvableProfile.CODEC.parse(JsonOps.INSTANCE, var2).getOrThrow();
         }

         var1.set(DataComponents.PROFILE, this.S);
         var1.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.PROFILE, true));
      }

      return var1;
   }

   public static boolean j(Item var0) {
      for (mb var4 : values()) {
         if (var0 == var4.N || var0 != Items.AIR && var0 == var4.j) {
            return true;
         }
      }

      return false;
   }
}
