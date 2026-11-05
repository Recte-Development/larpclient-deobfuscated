package com.larpclient.module;

import com.larpclient.mi;
import com.larpclient.r4;
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
   private final ln m = this.addSetting(new ln("Players", "Trace players", true));
   private final ln c = this.addSetting(new ln("Mobs", "Trace hostile mobs", false));
   private final ln Y = this.addSetting(new ln("Animals", "Trace animals", false));
   private final lc L = this.addSetting(new lc("Range", "Max tracer range", 96.0, 16.0, 256.0, 8.0));
   private final lc t = this.addSetting(new lc("Max Entities", "Nearest targets drawn", 64.0, 8.0, 256.0, 8.0));
   private final mi H = new mi(var1 -> this.addSetting(var1), false);

   public TracersModule() {
      super("Tracers", "Draw lines to entities", Category.RENDER, -1);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.L());
   }

   private void L() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            Camera var1 = mc.gameRenderer.mainCamera();
            Vector3f var2 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.rotation());
            Vec3 var3 = var1.position().add(var2.x * 0.5, var2.y * 0.5, var2.z * 0.5);
            float var4 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               for (Entity var6 : mi.T(
                  mc.level.entitiesForRendering(),
                  mc.player,
                  this.L.getValue(),
                  this.t.getValue().intValue(),
                  var1x -> var1x instanceof LivingEntity
                     && (
                        var1x instanceof Player
                           ? this.m.getValue()
                           : (var1x instanceof Enemy ? this.c.getValue() : var1x instanceof Animal && this.Y.getValue())
                     )
               )) {
                  int var7 = var6 instanceof Player ? -4221441 : (var6 instanceof Enemy ? -943189 : -7677757);
                  int var8 = this.H.k(var7, mc.player.distanceTo(var6), this.L.getValue(), mi.D((LivingEntity)var6), var6.getId());
                  this.H.D(var3, var6.getPosition(var4).add(0.0, var6.getBbHeight() * 0.5, 0.0), var8);
               }
            } catch (RuntimeException var9) {
               r4.T("ESP", "Tracer overlay failed", var9);
            }
         }
      }
   }

   public List<l6<?>> H() {
      return List.of(this.m, this.c, this.Y);
   }

   public List<l6<?>> I() {
      ArrayList var1 = new ArrayList<>(List.of(this.L));
      var1.addAll(this.H.t());
      var1.addAll(this.H.J());
      return List.copyOf(var1);
   }

   public List<l6<?>> B() {
      return List.of(this.t);
   }
}
