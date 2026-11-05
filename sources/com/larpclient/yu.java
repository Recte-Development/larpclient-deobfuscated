package com.larpclient;

import com.larpclient.module.Module;
import com.mojang.blaze3d.platform.InputConstants.Key;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;

public final class yu {
   public static boolean G(String var0) {
      LarpClient var1 = LarpClient.y();
      if (var1 != null && var1.e() != null) {
         Module var2 = var1.e().O(var0);
         return var2 != null && var2.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean r() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.level != null && var0.player != null && var0.player.isAlive() && !rr.o();
   }

   public static boolean X() {
      return r();
   }

   public static boolean X(KeyMapping var0) {
      Minecraft var1 = Minecraft.getInstance();
      Key var2 = KeyMappingHelper.getBoundKeyOf(var0);
      if (var2.getValue() < 0) {
         return false;
      }

      return (boolean)(switch (var2.getType()) {
         case KEYBOARD -> eo.F(var1.getWindow().handle(), r7.u(var2.getValue())) == 1 ? 1 : 0;
         case MOUSE -> eo.Y(var1.getWindow().handle(), r7.w(var2.getValue())) == 1 ? 1 : 0;
         default -> 0;
      });
   }

   public static Vec3 q() {
      Minecraft var0 = Minecraft.getInstance();
      double var1 = (var0.options.keyUp.isDown() ? 1 : 0) - (var0.options.keyDown.isDown() ? 1 : 0);
      double var3 = (var0.options.keyLeft.isDown() ? 1 : 0) - (var0.options.keyRight.isDown() ? 1 : 0);
      float var5 = oo.Z() ? rr.h() : var0.player.getYRot();
      double var6 = Math.toRadians(var5);
      return new Vec3(-Math.sin(var6) * var1 + Math.cos(var6) * var3, 0.0, Math.cos(var6) * var1 + Math.sin(var6) * var3).normalize();
   }

   public static Vec3 y(Vec3 var0, Vec3 var1, double var2) {
      Vec3 var4 = var1.subtract(var0);
      return var0.add(var4.length() > var2 ? var4.normalize().scale(var2) : var4);
   }

   private yu() {
   }
}
