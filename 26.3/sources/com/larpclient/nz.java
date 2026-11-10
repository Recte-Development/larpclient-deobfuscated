package com.larpclient;

import com.larpclient.module.FreecamModule;
import com.larpclient.module.Module;
import com.mojang.blaze3d.platform.InputConstants.Key;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3;

public final class nz {
   public static boolean X(String var0) {
      LarpClient var1 = LarpClient.N();
      if (var1 != null && var1.j() != null) {
         Module var2 = var1.j().U(var0);
         return var2 != null && var2.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean v() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.level != null && var0.player != null && var0.player.isAlive() && !ij.y();
   }

   public static boolean R() {
      return v();
   }

   public static boolean K(KeyMapping var0) {
      Minecraft var1 = Minecraft.getInstance();
      Key var2 = KeyMappingHelper.getBoundKeyOf(var0);
      if (var2.getValue() < 0) {
         return false;
      }

      return (boolean)(switch (var2.getType()) {
         case KEYBOARD -> n2.R(var1.getWindow().handle(), j2.S(var2.getValue())) == 1 ? 1 : 0;
         case MOUSE -> n2.L(var1.getWindow().handle(), j2.b(var2.getValue())) == 1 ? 1 : 0;
         default -> 0;
      });
   }

   public static Vec3 b() {
      Options var0 = Minecraft.getInstance().options;
      return o(var0.keyUp.isDown(), var0.keyDown.isDown(), var0.keyLeft.isDown(), var0.keyRight.isDown());
   }

   public static Vec3 l() {
      if (!ij.y()) {
         return b();
      }

      Input var0 = FreecamModule.h();
      return o(var0.forward(), var0.backward(), var0.left(), var0.right());
   }

   private static Vec3 o(boolean var0, boolean var1, boolean var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      double var5 = (var0 ? 1 : 0) - (var1 ? 1 : 0);
      double var7 = (var2 ? 1 : 0) - (var3 ? 1 : 0);
      double var9 = Math.toRadians(dt.H(var4.player.getYRot()));
      return new Vec3(-Math.sin(var9) * var5 + Math.cos(var9) * var7, 0.0, Math.cos(var9) * var5 + Math.sin(var9) * var7).normalize();
   }

   public static Vec3 g(Vec3 var0, Vec3 var1, double var2) {
      Vec3 var4 = var1.subtract(var0);
      return var0.add(var4.length() > var2 ? var4.normalize().scale(var2) : var4);
   }

   private nz() {
   }
}
