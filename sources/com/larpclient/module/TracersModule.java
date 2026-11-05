package com.larpclient.module;

import com.larpclient.dq;
import com.larpclient.mt;
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
   private final dh K = this.addSetting(new dh("Players", "Trace players", true));
   private final dh q = this.addSetting(new dh("Mobs", "Trace hostile mobs", false));
   private final dh b = this.addSetting(new dh("Animals", "Trace animals", false));
   private final d9 O = this.addSetting(new d9("Range", "Max tracer range", 96.0, 16.0, 256.0, 8.0));
   private final d9 r = this.addSetting(new d9("Max Entities", "Nearest targets drawn", 64.0, 8.0, 256.0, 8.0));
   private final dq g = new dq(var1 -> this.addSetting(var1), false);

   public TracersModule() {
      super("Tracers", "Draw lines to entities", Category.RENDER, -1);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.n());
   }

   private void n() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            Camera var1 = mc.gameRenderer.mainCamera();
            Vector3f var2 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.rotation());
            Vec3 var3 = var1.position().add(var2.x * 0.5, var2.y * 0.5, var2.z * 0.5);
            float var4 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               for (Entity var6 : dq.y(
                  mc.level.entitiesForRendering(),
                  mc.player,
                  this.O.P(),
                  this.r.P().intValue(),
                  var1x -> var1x instanceof LivingEntity
                     && (var1x instanceof Player ? this.K.P() : (var1x instanceof Enemy ? this.q.P() : var1x instanceof Animal && this.b.P()))
               )) {
                  int var7 = var6 instanceof Player ? -4221441 : (var6 instanceof Enemy ? -943189 : -7677757);
                  int var8 = this.g.w(var7, mc.player.distanceTo(var6), this.O.P(), dq.E((LivingEntity)var6), var6.getId());
                  this.g.n(var3, var6.getPosition(var4).add(0.0, var6.getBbHeight() * 0.5, 0.0), var8);
               }
            } catch (RuntimeException var9) {
               mt.B("ESP", "Tracer overlay failed", var9);
            }
         }
      }
   }

   public List<df<?>> c() {
      return List.of(this.K, this.q, this.b);
   }

   public List<df<?>> U() {
      ArrayList var1 = new ArrayList<>(List.of(this.O));
      var1.addAll(this.g.q());
      var1.addAll(this.g.g());
      return List.copyOf(var1);
   }

   public List<df<?>> J() {
      return List.of(this.r);
   }
}
