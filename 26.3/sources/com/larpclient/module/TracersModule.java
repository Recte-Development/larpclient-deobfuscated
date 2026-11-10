package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.j5;
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
   private final m g = this.addSetting(new m("Players", "Trace players", true));
   private final m j = this.addSetting(new m("Mobs", "Trace hostile mobs", false));
   private final m Z = this.addSetting(new m("Animals", "Trace animals", false));
   private final b s = this.addSetting(new b("Range", "Max tracer range", 96.0, 16.0, 256.0, 8.0));
   private final b S = this.addSetting(new b("Max Entities", "Nearest targets drawn", 64.0, 8.0, 256.0, 8.0));
   private final j5 k = new j5(var1 -> this.addSetting(var1), false);

   public TracersModule() {
      super("Tracers", "Draw lines to entities", Category.RENDER, -1);
      LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var1 -> this.C());
   }

   private void C() {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame()) {
            Camera var1 = mc.gameRenderer.mainCamera();
            Vector3f var2 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var1.rotation());
            Vec3 var3 = var1.position().add(var2.x * 0.5, var2.y * 0.5, var2.z * 0.5);
            float var4 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);

            try {
               for (Entity var6 : j5.n(
                  mc.level.entitiesForRendering(),
                  mc.player,
                  this.s.getValue(),
                  this.S.getValue().intValue(),
                  var1x -> var1x instanceof LivingEntity
                     && (
                        var1x instanceof Player
                           ? this.g.getValue()
                           : (var1x instanceof Enemy ? this.j.getValue() : var1x instanceof Animal && this.Z.getValue())
                     )
               )) {
                  int var7 = var6 instanceof Player ? -4221441 : (var6 instanceof Enemy ? -943189 : -7677757);
                  int var8 = this.k.p(var7, mc.player.distanceTo(var6), this.s.getValue(), j5.w((LivingEntity)var6), var6.getId());
                  this.k.q(var3, j5.J(var6, var4).add(0.0, var6.getBbHeight() * 0.5, 0.0), var8);
               }
            } catch (RuntimeException var9) {
               _q.g("ESP", "Tracer overlay failed", var9);
            }
         }
      }
   }

   public List<r<?>> V() {
      return List.of(this.g, this.j, this.Z);
   }

   public List<r<?>> N() {
      ArrayList var1 = new ArrayList<>(List.of(this.s));
      var1.addAll(this.k.Q());
      var1.addAll(this.k.l());
      return List.copyOf(var1);
   }

   public List<r<?>> B() {
      return List.of(this.S);
   }
}
