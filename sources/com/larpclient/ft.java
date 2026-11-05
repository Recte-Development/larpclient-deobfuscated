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
public final class ft {
   private static final ft F = new ft();
   public boolean x;

   private ft() {
   }

   public static ft V() {
      return F;
   }

   public void g(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = um.T().u(var6);
               if (!this.x && var7 > 0) {
                  String var8 = Integer.toString(var7);
                  hh.j(var1, f9.b.B, "HigherLower", var4, var8, var8, this.x);
               }
            }
         }
      }

      if (!this.x) {
         um.T().clearQueue();
      }

      um.T().tick(var1);
   }
}
