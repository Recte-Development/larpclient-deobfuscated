package com.larpclient.module;

import com.larpclient.z4;
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
   private final s Z = this.addSetting(new s("Players", "Trace players", true));
   private final s Q = this.addSetting(new s("Mobs", "Trace hostile mobs", false));
   private final s e = this.addSetting(new s("Animals", "Trace animals", false));
   private final e g = this.addSetting(new e("Range", "Max tracer range", 96.0, 16.0, 256.0, 8.0));
   private final e f = this.addSetting(new e("Max Entities", "Nearest targets drawn", 64.0, 8.0, 256.0, 8.0));
   private final z4 o = new z4(var1 -> this.addSetting(var1), false);

   public TracersModule() {
      super("Tracers", "Draw lines to entities", Category.RENDER, -1);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.S());
   }

   private void S() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            Camera var1 = mc.gameRenderer.mainCamera();
            Vector3f var2 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.rotation());
            Vec3 var3 = var1.position().add(var2.x * 0.5, var2.y * 0.5, var2.z * 0.5);
            float var4 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               for (Entity var6 : z4.G(
                  mc.level.entitiesForRendering(),
                  mc.player,
                  this.g.getValue(),
                  this.f.getValue().intValue(),
                  var1x -> var1x instanceof LivingEntity
                     && (
                        var1x instanceof Player
                           ? this.Z.getValue()
                           : (var1x instanceof Enemy ? this.Q.getValue() : var1x instanceof Animal && this.e.getValue())
                     )
               )) {
                  int var7 = var6 instanceof Player ? -4221441 : (var6 instanceof Enemy ? -943189 : -7677757);
                  int var8 = this.o.c(var7, mc.player.distanceTo(var6), this.g.getValue(), z4.g((LivingEntity)var6), var6.getId());
                  this.o.S(var3, z4.r(var6, var4).add(0.0, var6.getBbHeight() * 0.5, 0.0), var8);
               }
            } catch (RuntimeException var9) {
               com.larpclient.h.w("ESP", "Tracer overlay failed", var9);
            }
         }
      }
   }

   public List<q<?>> t() {
      return List.of(this.Z, this.Q, this.e);
   }

   public List<q<?>> W() {
      ArrayList var1 = new ArrayList<>(List.of(this.g));
      var1.addAll(this.o.f());
      var1.addAll(this.o.O());
      return List.copyOf(var1);
   }

   public List<q<?>> d() {
      return List.of(this.f);
   }
}
