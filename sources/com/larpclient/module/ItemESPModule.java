package com.larpclient.module;

import com.larpclient.mi;
import com.larpclient.r4;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;

public class ItemESPModule extends Module {
   private final lc V = this.addSetting(new lc("Range", "Max render range", 48.0, 8.0, 128.0, 8.0));
   private final lc D = this.addSetting(new lc("Max Items", "Nearest drops drawn", 128.0, 16.0, 512.0, 16.0));
   private final lp u = this.addSetting(new lp("Mode", "Drop highlight style", "Corners", "Box", "Corners", "Filled"));
   private final ln Z = this.addSetting(new ln("XP Orbs", "Include experience orbs", false));
   private final ln f = this.addSetting(new ln("Labels", "Item name, count and distance within 24 blocks", true));
   private final mi B = new mi(var1 -> this.addSetting(var1));

   public ItemESPModule() {
      super("ItemESP", "Highlight dropped items and experience orbs", Category.RENDER, -1);
      this.B.J(() -> true, () -> true, () -> true, () -> this.u.i("Filled"));
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.w());
   }

   private void w() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            float var1 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               int var2 = 0;

               for (Entity var4 : mi.T(
                  mc.level.entitiesForRendering(),
                  mc.player,
                  this.V.getValue(),
                  this.D.getValue().intValue(),
                  var1x -> var1x instanceof ItemEntity || this.Z.getValue() && var1x instanceof ExperienceOrb
               )) {
                  double var5 = mc.player.distanceTo(var4);
                  int var7 = this.B.k(var4 instanceof ItemEntity ? -3627265 : -7677757, var5, this.V.getValue(), 1.0, var4.getId());
                  AABB var8 = mi.R(var4, var1);
                  this.B.I(var8, this.u.getValue(), var7);
                  if (this.f.getValue() && var5 <= 24.0 && var4 instanceof ItemEntity var9 && var2++ < 24) {
                     ItemStack var10 = var9.getItem();
                     this.B
                        .t(
                           var8.getCenter().add(0.0, var8.getYsize() * 0.5 + 0.18, 0.0),
                           var10.getHoverName().getString() + " ×" + var10.getCount() + " · " + (int)var5 + "m",
                           var7
                        );
                  }
               }
            } catch (RuntimeException var11) {
               r4.T("ESP", "Item overlay failed", var11);
            }
         }
      }
   }

   public List<l6<?>> y() {
      return List.of(this.u, this.V, this.Z, this.f);
   }

   public List<l6<?>> K() {
      ArrayList var1 = new ArrayList<>(this.B.t());
      var1.addAll(this.B.J());
      var1.addAll(this.B.Q());
      return List.copyOf(var1);
   }

   public List<l6<?>> x() {
      return List.of(this.D);
   }
}
