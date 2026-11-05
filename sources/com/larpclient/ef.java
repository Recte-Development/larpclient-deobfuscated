package com.larpclient;

import com.larpclient.module.AuraModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;

public final class ef {
   public static LivingEntity j(String var0) {
      Minecraft var1 = Minecraft.getInstance();
      LivingEntity var2 = null;
      if (var0.equals("Auto") || var0.equals("Aura")) {
         AuraModule var3 = AuraModule.G();
         if (var3 != null) {
            var2 = var3.G();
         }
      }

      if (var2 == null && (var0.equals("Auto") || var0.equals("Combat"))) {
         var2 = f8.m();
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
