package com.larpclient.util.data;

import com.larpclient.module.exploit.HigherLowerSwapper;
import com.larpclient.util.misc.RigLog;
import com.larpclient.util.world.DispenserRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;

@Environment(EnvType.CLIENT)
public final class HigherLowerMapTracker {
   private static final HigherLowerMapTracker instance = new HigherLowerMapTracker();
   public boolean paused;

   private HigherLowerMapTracker() {
   }

   public static HigherLowerMapTracker getInstance() {
      return instance;
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = HigherLowerSwapper.getInstance().getMapId(var6);
               if (!this.paused && var7 > 0) {
                  String var8 = Integer.toString(var7);
                  RigLog.logDispenserDrop(var1, DispenserRegistry.GameType.HIGHER_LOWER, "HigherLower", var4, var8, var8, this.paused);
               }
            }
         }
      }

      if (!this.paused) {
         HigherLowerSwapper.getInstance().clearQueue();
      }

      HigherLowerSwapper.getInstance().tick(var1);
   }
}
