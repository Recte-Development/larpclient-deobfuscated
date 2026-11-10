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
public final class k6 {
   private static final k6 d = new k6();
   public boolean Q;

   private k6() {
   }

   public static k6 d() {
      return d;
   }

   public void k(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = wc.s().q(var6);
               if (!this.Q && var7 > 0) {
                  String var8 = Integer.toString(var7);
                  te.M(var1, gj.d.Z, "HigherLower", var4, var8, var8, this.Q);
               }
            }
         }
      }

      if (!this.Q) {
         wc.s().clearQueue();
      }

      wc.s().tick(var1);
   }
}
