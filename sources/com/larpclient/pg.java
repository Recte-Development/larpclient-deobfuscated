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
public class pg {
   private static final pg B = new pg();
   public boolean G;

   private pg() {
   }

   public static pg G() {
      return B;
   }

   public void J() {
      zu.v().t();
   }

   public void I() {
      zu.v().l();
   }

   public void w() {
      zu.v().clearQueue();
   }

   public String D() {
      return zu.v().pendingLabel();
   }

   public void m(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = zu.v().Y(var6);
               boolean var8 = var5.is(Items.WITHER_SKELETON_SKULL);
               if (!this.G && var7 > 0 || var8) {
                  String var9 = var8 ? "Wither Skull (Lose)" : Integer.toString(var7);
                  xa.J(var1, ep.o.K, "Odd/Even", var4, var9, var9, this.G);
               }
            }
         }
      }

      if (!this.G) {
         zu.v().clearQueue();
      } else {
         zu.v().tick(var1);
      }
   }
}
