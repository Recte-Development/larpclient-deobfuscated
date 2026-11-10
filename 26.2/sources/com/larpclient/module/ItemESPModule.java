package com.larpclient.module;

import com.larpclient.z4;
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
   private final e S = this.addSetting(new e("Range", "Max render range", 48.0, 8.0, 128.0, 8.0));
   private final e r = this.addSetting(new e("Max Items", "Nearest drops drawn", 128.0, 16.0, 512.0, 16.0));
   private final l f = this.addSetting(new l("Mode", "Drop highlight style", "Corners", "Box", "Corners", "Filled"));
   private final s T = this.addSetting(new s("XP Orbs", "Include experience orbs", false));
   private final s d = this.addSetting(new s("Labels", "Item name, count and distance within 24 blocks", true));
   private final z4 G = new z4(var1 -> this.addSetting(var1));

   public ItemESPModule() {
      super("ItemESP", "Highlight dropped items and experience orbs", Category.RENDER, -1);
      this.G.s(() -> true, () -> true, () -> true, () -> this.f.O("Filled"));
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.H());
   }

   private void H() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            float var1 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               int var2 = 0;

               for (Entity var4 : z4.G(
                  mc.level.entitiesForRendering(),
                  mc.player,
                  this.S.getValue(),
                  this.r.getValue().intValue(),
                  var1x -> var1x instanceof ItemEntity || this.T.getValue() && var1x instanceof ExperienceOrb
               )) {
                  double var5 = mc.player.distanceTo(var4);
                  int var7 = this.G.c(var4 instanceof ItemEntity ? -3627265 : -7677757, var5, this.S.getValue(), 1.0, var4.getId());
                  AABB var8 = z4.O(var4, var1);
                  this.G.R(var8, this.f.getValue(), var7);
                  if (this.d.getValue() && var5 <= 24.0 && var4 instanceof ItemEntity var9 && var2++ < 24) {
                     ItemStack var10 = var9.getItem();
                     this.G
                        .d(
                           var8.getCenter().add(0.0, var8.getYsize() * 0.5 + 0.18, 0.0),
                           var10.getHoverName().getString() + " ×" + var10.getCount() + " · " + (int)var5 + "m",
                           var7
                        );
                  }
               }
            } catch (RuntimeException var11) {
               com.larpclient.h.w("ESP", "Item overlay failed", var11);
            }
         }
      }
   }

   public List<q<?>> r() {
      return List.of(this.f, this.S, this.T, this.d);
   }

   public List<q<?>> E() {
      ArrayList var1 = new ArrayList<>(this.G.f());
      var1.addAll(this.G.O());
      var1.addAll(this.G.p());
      return List.copyOf(var1);
   }

   public List<q<?>> o() {
      return List.of(this.r);
   }
}
