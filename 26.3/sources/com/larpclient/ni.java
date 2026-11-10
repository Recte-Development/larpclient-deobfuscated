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

public enum ni {
   X("Skeleton", EntityTypes.SKELETON, Items.BONE, Items.ARROW, null),
   F("Iron Golem", EntityTypes.IRON_GOLEM, Items.IRON_INGOT, Items.AIR, "MHF_Golem"),
   j("Zombified Piglin", EntityTypes.ZOMBIFIED_PIGLIN, Items.ROTTEN_FLESH, Items.GOLD_NUGGET, "MHF_PigZombie");

   public final String N;
   public final EntityType<?> D;
   public final Item S;
   public final Item W;
   private final String T;
   private ResolvableProfile o;

   ni(String var3, EntityType<?> var4, Item var5, Item var6, String var7) {
      this.N = var3;
      this.D = var4;
      this.S = var5;
      this.W = var6;
      this.T = var7;
   }

   public ItemStack D() {
      ItemStack var1 = new ItemStack(this.T == null ? Items.SKELETON_SKULL : Items.PLAYER_HEAD);
      if (this.T != null) {
         if (this.o == null) {
            JsonObject var2 = new JsonObject();
            var2.addProperty("name", this.T);
            this.o = (ResolvableProfile)ResolvableProfile.CODEC.parse(JsonOps.INSTANCE, var2).getOrThrow();
         }

         var1.set(DataComponents.PROFILE, this.o);
         var1.set(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT.withHidden(DataComponents.PROFILE, true));
      }

      return var1;
   }

   public static boolean u(Item var0) {
      for (ni var4 : values()) {
         if (var0 == var4.S || var0 != Items.AIR && var0 == var4.W) {
            return true;
         }
      }

      return false;
   }
}
