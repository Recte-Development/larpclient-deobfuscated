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
public class _6 {
   private static final _6 F = new _6();
   public boolean n;

   private _6() {
   }

   public static _6 G() {
      return F;
   }

   public void k() {
      ue.V().m();
   }

   public void N() {
      ue.V().J();
   }

   public void Z() {
      ue.V().clearQueue();
   }

   public String H() {
      return ue.V().pendingLabel();
   }

   public void l(Minecraft var1) {
      if (var1 != null && var1.level != null) {
         for (Entity var3 : var1.level.entitiesForRendering()) {
            if (var3 instanceof ItemEntity var4) {
               ItemStack var5 = var4.getItem();
               MapId var6 = (MapId)var5.get(DataComponents.MAP_ID);
               int var7 = ue.V().u(var6);
               boolean var8 = var5.is(Items.WITHER_SKELETON_SKULL);
               if (!this.n && var7 > 0 || var8) {
                  String var9 = var8 ? "Wither Skull (Lose)" : Integer.toString(var7);
                  hh.j(var1, f9.b.T, "Odd/Even", var4, var9, var9, this.n);
               }
            }
         }
      }

      if (!this.n) {
         ue.V().clearQueue();
      } else {
         ue.V().tick(var1);
      }
   }
}
