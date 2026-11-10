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
public class lj {
   private static final lj W = new lj();
   public boolean j;

   private lj() {
   }

   public static lj M() {
      return W;
   }

   public void R() {
      w3.M().Q();
   }

   public void S() {
      w3.M().u();
   }

   public void H() {
      w3.M().clearQueue();
   }

   public String P() {
      return w3.M().pendingLabel();
   }

   public void Q(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = w3.M().q(var6);
               boolean var8 = var5.is(Items.WITHER_SKELETON_SKULL);
               if (!this.j && var7 > 0 || var8) {
                  String var9 = var8 ? "Wither Skull (Lose)" : Integer.toString(var7);
                  te.M(var1, gj.d.b, "Odd/Even", var4, var9, var9, this.j);
               }
            }
         }
      }

      if (!this.j) {
         w3.M().clearQueue();
      } else {
         w3.M().tick(var1);
      }
   }
}
