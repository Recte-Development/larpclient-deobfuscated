package com.larpclient.util.entity;

import com.larpclient.module.combat.AuraModule;
import com.larpclient.render.misc.VisualsRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public final class TargetResolver {
   public static LivingEntity resolveTarget(String var0) {
      Minecraft var1 = Minecraft.getInstance();
      LivingEntity var2 = null;
      if (var0.equals("Auto") || var0.equals("Aura")) {
         AuraModule var3 = AuraModule.getInstance();
         if (var3 != null) {
            var2 = var3.applyMoveFix();
         }
      }

      if (var2 == null && (var0.equals("Auto") || var0.equals("Combat"))) {
         var2 = VisualsRenderer.getRecentTarget();
      }

      if (var2 == null
         && (var0.equals("Auto") || var0.equals("Crosshair"))
         && var1.hitResult instanceof EntityHitResult var6
         && var6.getEntity() instanceof LivingEntity var4) {
         var2 = var4;
      }

      return var2 != null && var2.isAlive() && var2.level() == var1.level && var1.player != null && var2.distanceToSqr(var1.player) < 4096.0 ? var2 : null;
   }
}
