package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;

@Environment(EnvType.CLIENT)
public final class k {
   private static final k W = new k();
   public boolean h;

   private k() {
   }

   public static k D() {
      return W;
   }

   public void S(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = vh.R().V(var6);
               if (!this.h && var7 > 0) {
                  String var8 = Integer.toString(var7);
                  bn.y(var1, hk.k.b, "HigherLower", var4, var8, var8, this.h);
               }
            }
         }
      }

      if (!this.h) {
         vh.R().clearQueue();
      }

      vh.R().tick(var1);
   }
}
