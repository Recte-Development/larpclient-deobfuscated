package com.larpclient.module;

import com.larpclient.dq;
import com.larpclient.mt;
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
   private final d9 g = this.addSetting(new d9("Range", "Max render range", 48.0, 8.0, 128.0, 8.0));
   private final d9 C = this.addSetting(new d9("Max Items", "Nearest drops drawn", 128.0, 16.0, 512.0, 16.0));
   private final d6 z = this.addSetting(new d6("Mode", "Drop highlight style", "Corners", "Box", "Corners", "Filled"));
   private final dh i = this.addSetting(new dh("XP Orbs", "Include experience orbs", false));
   private final dh E = this.addSetting(new dh("Labels", "Item name, count and distance within 24 blocks", true));
   private final dq B = new dq(var1 -> this.addSetting(var1));

   public ItemESPModule() {
      super("ItemESP", "Highlight dropped items and experience orbs", Category.RENDER, -1);
      this.B.o(() -> true, () -> true, () -> true, () -> this.z.S("Filled"));
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.p());
   }

   private void p() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            float var1 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               int var2 = 0;

               for (Entity var4 : dq.y(
                  mc.level.entitiesForRendering(),
                  mc.player,
                  this.g.P(),
                  this.C.P().intValue(),
                  var1x -> var1x instanceof ItemEntity || this.i.P() && var1x instanceof ExperienceOrb
               )) {
                  double var5 = mc.player.distanceTo(var4);
                  int var7 = this.B.w(var4 instanceof ItemEntity ? -3627265 : -7677757, var5, this.g.P(), 1.0, var4.getId());
                  AABB var8 = dq.k(var4, var1);
                  this.B.O(var8, this.z.P(), var7);
                  if (this.E.P() && var5 <= 24.0 && var4 instanceof ItemEntity var9 && var2++ < 24) {
                     ItemStack var10 = var9.getItem();
                     this.B
                        .d(
                           var8.getCenter().add(0.0, var8.getYsize() * 0.5 + 0.18, 0.0),
                           var10.getHoverName().getString() + " ×" + var10.getCount() + " · " + (int)var5 + "m",
                           var7
                        );
                  }
               }
            } catch (RuntimeException var11) {
               mt.B("ESP", "Item overlay failed", var11);
            }
         }
      }
   }

   public List<df<?>> h() {
      return List.of(this.z, this.g, this.i, this.E);
   }

   public List<df<?>> F() {
      ArrayList var1 = new ArrayList<>(this.B.q());
      var1.addAll(this.B.g());
      var1.addAll(this.B.L());
      return List.copyOf(var1);
   }

   public List<df<?>> N() {
      return List.of(this.C);
   }
}
