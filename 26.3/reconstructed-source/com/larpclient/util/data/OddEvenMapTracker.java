package com.larpclient.util.data;

import com.larpclient.module.exploit.OddEvenCardForce;
import com.larpclient.util.misc.RigLog;
import com.larpclient.util.world.DispenserRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

@Environment(EnvType.CLIENT)
public class OddEvenMapTracker {
   private static final OddEvenMapTracker iNSTANCE = new OddEvenMapTracker();
   public boolean enabled;

   private OddEvenMapTracker() {
   }

   public static OddEvenMapTracker getInstance() {
      return iNSTANCE;
   }

   public void start() {
      OddEvenCardForce.getInstance().forceOdd();
   }

   public void confirm() {
      OddEvenCardForce.getInstance().forceEven();
   }

   public void clearQueue() {
      OddEvenCardForce.getInstance().clearQueue();
   }

   public String getInstance() {
      return OddEvenCardForce.getInstance().pendingLabel();
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = OddEvenCardForce.getInstance().getMapId(var6);
               boolean var8 = var5.is(Items.WITHER_SKELETON_SKULL);
               if (!this.enabled && var7 > 0 || var8) {
                  String var9 = var8 ? "Wither Skull (Lose)" : Integer.toString(var7);
                  RigLog.logDispenserDrop(var1, DispenserRegistry.GameType.ODD_EVEN, "Odd/Even", var4, var9, var9, this.enabled);
               }
            }
         }
      }

      if (!this.enabled) {
         OddEvenCardForce.getInstance().clearQueue();
      } else {
         OddEvenCardForce.getInstance().tick(var1);
      }
   }
}
