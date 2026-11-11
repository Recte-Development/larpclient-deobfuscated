package com.larpclient.module.render;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.entity.EspRenderer;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class TracersModule extends Module {
   private final BooleanSetting players = this.addSetting(new BooleanSetting("Players", "Trace players", true));
   private final BooleanSetting mobs = this.addSetting(new BooleanSetting("Mobs", "Trace hostile mobs", false));
   private final BooleanSetting animals = this.addSetting(new BooleanSetting("Animals", "Trace animals", false));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Max tracer range", 96.0, 16.0, 256.0, 8.0));
   private final NumberSetting maxEntities = this.addSetting(new NumberSetting("Max Entities", "Nearest targets drawn", 64.0, 8.0, 256.0, 8.0));
   private final EspRenderer espRenderer = new EspRenderer(var1 -> this.addSetting(var1), false);

   public TracersModule() {
      super("Tracers", "Draw lines to entities", Category.RENDER, -1);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.renderTracers());
   }

   private void renderTracers() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            Camera var1 = minecraft.gameRenderer.mainCamera();
            Vector3f var2 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.rotation());
            Vec3 var3 = var1.position().add(var2.x * 0.5, var2.y * 0.5, var2.z * 0.5);
            float var4 = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               for (Entity var6 : EspRenderer.nearbyEntities(
                  minecraft.level.entitiesForRendering(),
                  minecraft.player,
                  this.range.getValue(),
                  this.maxEntities.getValue().intValue(),
                  var1x -> var1x instanceof LivingEntity
                     && (
                        var1x instanceof Player
                           ? this.players.getValue()
                           : (var1x instanceof Enemy ? this.mobs.getValue() : var1x instanceof Animal && this.animals.getValue())
                     )
               )) {
                  int var7 = var6 instanceof Player ? -4221441 : (var6 instanceof Enemy ? -943189 : -7677757);
                  int var8 = this.espRenderer
                     .resolveColor(var7, minecraft.player.distanceTo(var6), this.range.getValue(), EspRenderer.healthFraction((LivingEntity)var6), var6.getId());
                  this.espRenderer.drawLine(var3, EspRenderer.interpolatedPosition(var6, var4).add(0.0, var6.getBbHeight() * 0.5, 0.0), var8);
               }
            } catch (RuntimeException var9) {
               LarpLog.errorOnce("ESP", "Tracer overlay failed", var9);
            }
         }
      }
   }

   public List<Setting<?>> getTargetSettings() {
      return List.of(this.players, this.mobs, this.animals);
   }

   public List<Setting<?>> getRangeSettings() {
      ArrayList var1 = new ArrayList<>(List.of(this.range));
      var1.addAll(this.espRenderer.colorSettings());
      var1.addAll(this.espRenderer.outlineSettings());
      return List.copyOf(var1);
   }

   public List<Setting<?>> getLimitSettings() {
      return List.of(this.maxEntities);
   }
}
