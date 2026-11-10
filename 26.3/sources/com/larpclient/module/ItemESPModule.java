package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.j5;
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
   private final b q = this.addSetting(new b("Range", "Max render range", 48.0, 8.0, 128.0, 8.0));
   private final b O = this.addSetting(new b("Max Items", "Nearest drops drawn", 128.0, 16.0, 512.0, 16.0));
   private final o X = this.addSetting(new o("Mode", "Drop highlight style", "Corners", "Box", "Corners", "Filled"));
   private final m D = this.addSetting(new m("XP Orbs", "Include experience orbs", false));
   private final m u = this.addSetting(new m("Labels", "Item name, count and distance within 24 blocks", true));
   private final j5 J = new j5(var1 -> this.addSetting(var1));

   public ItemESPModule() {
      super("ItemESP", "Highlight dropped items and experience orbs", Category.RENDER, -1);
      this.J.q(() -> true, () -> true, () -> true, () -> this.X.w("Filled"));
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.H());
   }

   private void H() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            float var1 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               int var2 = 0;

               for (Entity var4 : j5.n(
                  mc.level.entitiesForRendering(),
                  mc.player,
                  this.q.getValue(),
                  this.O.getValue().intValue(),
                  var1x -> var1x instanceof ItemEntity || this.D.getValue() && var1x instanceof ExperienceOrb
               )) {
                  double var5 = mc.player.distanceTo(var4);
                  int var7 = this.J.p(var4 instanceof ItemEntity ? -3627265 : -7677757, var5, this.q.getValue(), 1.0, var4.getId());
                  AABB var8 = j5.X(var4, var1);
                  this.J.L(var8, this.X.getValue(), var7);
                  if (this.u.getValue() && var5 <= 24.0 && var4 instanceof ItemEntity var9 && var2++ < 24) {
                     ItemStack var10 = var9.getItem();
                     this.J
                        .P(
                           var8.getCenter().add(0.0, var8.getYsize() * 0.5 + 0.18, 0.0),
                           var10.getHoverName().getString() + " ×" + var10.getCount() + " · " + (int)var5 + "m",
                           var7
                        );
                  }
               }
            } catch (RuntimeException var11) {
               _q.g("ESP", "Item overlay failed", var11);
            }
         }
      }
   }

   public List<r<?>> p() {
      return List.of(this.X, this.q, this.D, this.u);
   }

   public List<r<?>> b() {
      ArrayList var1 = new ArrayList<>(this.J.Q());
      var1.addAll(this.J.l());
      var1.addAll(this.J.M());
      return List.copyOf(var1);
   }

   public List<r<?>> m() {
      return List.of(this.O);
   }
}
