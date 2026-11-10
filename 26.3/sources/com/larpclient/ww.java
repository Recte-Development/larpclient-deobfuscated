package com.larpclient;

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
public class ww {
   private static final ww V = new ww();
   public boolean K;

   private ww() {
   }

   public static ww J() {
      return V;
   }

   public void B() {
      vz.E().C();
   }

   public void z() {
      vz.E().Q();
   }

   public void i() {
      vz.E().clearQueue();
   }

   public String J() {
      return vz.E().pendingLabel();
   }

   public void A(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = vz.E().V(var6);
               boolean var8 = var5.is(Items.WITHER_SKELETON_SKULL);
               if (!this.K && var7 > 0 || var8) {
                  String var9 = var8 ? "Wither Skull (Lose)" : Integer.toString(var7);
                  bn.y(var1, hk.k.i, "Odd/Even", var4, var9, var9, this.K);
               }
            }
         }
      }

      if (!this.K) {
         vz.E().clearQueue();
      } else {
         vz.E().tick(var1);
      }
   }
}
