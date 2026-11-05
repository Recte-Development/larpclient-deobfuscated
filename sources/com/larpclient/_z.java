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
public final class _z {
   private static final _z F = new _z();
   public boolean p;

   private _z() {
   }

   public static _z e() {
      return F;
   }

   public void I(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = z8.l().Y(var6);
               if (!this.p && var7 > 0) {
                  String var8 = Integer.toString(var7);
                  xa.J(var1, ep.o.c, "HigherLower", var4, var8, var8, this.p);
               }
            }
         }
      }

      if (!this.p) {
         z8.l().clearQueue();
      }

      z8.l().tick(var1);
   }
}
