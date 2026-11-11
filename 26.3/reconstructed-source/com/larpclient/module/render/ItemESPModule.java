package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.misc.LarpLog;
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
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Max render range", 48.0, 8.0, 128.0, 8.0));
   private final NumberSetting maxItems = this.addSetting(new NumberSetting("Max Items", "Nearest drops drawn", 128.0, 16.0, 512.0, 16.0));
   private final ModeSetting mode = this.addSetting(new ModeSetting("Mode", "Drop highlight style", "Corners", "Box", "Corners", "Filled"));
   private final BooleanSetting xpOrbs = this.addSetting(new BooleanSetting("XP Orbs", "Include experience orbs", false));
   private final BooleanSetting labels = this.addSetting(new BooleanSetting("Labels", "Item name, count and distance within 24 blocks", true));
   private final EspRenderer espRenderer = new EspRenderer(var1 -> this.addSetting(var1));

   public ItemESPModule() {
      super("ItemESP", "Highlight dropped items and experience orbs", Category.RENDER, -1);
      this.espRenderer.drawLine(() -> true, () -> true, () -> true, () -> this.mode.isMode("Filled"));
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.renderItems());
   }

   private void renderItems() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            float var1 = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               int var2 = 0;

               for (Entity var4 : EspRenderer.nearbyEntities(
                  minecraft.level.entitiesForRendering(),
                  minecraft.player,
                  this.range.getValue(),
                  this.maxItems.getValue().intValue(),
                  var1x -> var1x instanceof ItemEntity || this.xpOrbs.getValue() && var1x instanceof ExperienceOrb
               )) {
                  double var5 = minecraft.player.distanceTo(var4);
                  int var7 = this.espRenderer.resolveColor(var4 instanceof ItemEntity ? -3627265 : -7677757, var5, this.range.getValue(), 1.0, var4.getId());
                  AABB var8 = EspRenderer.interpolatedBox(var4, var1);
                  this.espRenderer.drawBox(var8, this.mode.getValue(), var7);
                  if (this.labels.getValue() && var5 <= 24.0 && var4 instanceof ItemEntity var9 && var2++ < 24) {
                     ItemStack var10 = var9.getItem();
                     this.espRenderer
                        .drawLabel(
                           var8.getCenter().add(0.0, var8.getYsize() * 0.5 + 0.18, 0.0),
                           var10.getHoverName().getString() + " ×" + var10.getCount() + " · " + (int)var5 + "m",
                           var7
                        );
                  }
               }
            } catch (RuntimeException var11) {
               LarpLog.errorOnce("ESP", "Item overlay failed", var11);
            }
         }
      }
   }

   public List<Setting<?>> getMainSettings() {
      return List.of(this.mode, this.range, this.xpOrbs, this.labels);
   }

   public List<Setting<?>> getColorSettings() {
      ArrayList var1 = new ArrayList<>(this.espRenderer.colorSettings());
      var1.addAll(this.espRenderer.outlineSettings());
      var1.addAll(this.espRenderer.fillSettings());
      return List.copyOf(var1);
   }

   public List<Setting<?>> getLimitSettings() {
      return List.of(this.maxItems);
   }
}
