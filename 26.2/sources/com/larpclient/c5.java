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
import org.lwjgl.glfw.GLFW;

public final class c5 {
   public static boolean t(String var0) {
      LarpClient var1 = LarpClient.R();
      if (var1 != null && var1.z() != null) {
         Module var2 = var1.z().d(var0);
         return var2 != null && var2.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean J() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.level != null && var0.player != null && var0.player.isAlive() && !p_.V();
   }

   public static boolean w() {
      return J();
   }

   public static boolean J(KeyMapping var0) {
      Minecraft var1 = Minecraft.getInstance();
      Key var2 = KeyMappingHelper.getBoundKeyOf(var0);
      if (var2.getValue() < 0) {
         return false;
      }

      return (boolean)(switch (var2.getType()) {
         case KEYSYM -> GLFW.glfwGetKey(var1.getWindow().handle(), k2.J(var2.getValue())) == 1 ? 1 : 0;
         case MOUSE -> GLFW.glfwGetMouseButton(var1.getWindow().handle(), k2.I(var2.getValue())) == 1 ? 1 : 0;
         default -> 0;
      });
   }

   public static Vec3 D() {
      Options var0 = Minecraft.getInstance().options;
      return Y(var0.keyUp.isDown(), var0.keyDown.isDown(), var0.keyLeft.isDown(), var0.keyRight.isDown());
   }

   public static Vec3 H() {
      if (!p_.V()) {
         return D();
      }

      Input var0 = FreecamModule.f();
      return Y(var0.forward(), var0.backward(), var0.left(), var0.right());
   }

   private static Vec3 Y(boolean var0, boolean var1, boolean var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      double var5 = (var0 ? 1 : 0) - (var1 ? 1 : 0);
      double var7 = (var2 ? 1 : 0) - (var3 ? 1 : 0);
      double var9 = Math.toRadians(cj.U(var4.player.getYRot()));
      return new Vec3(-Math.sin(var9) * var5 + Math.cos(var9) * var7, 0.0, Math.cos(var9) * var5 + Math.sin(var9) * var7).normalize();
   }

   public static Vec3 S(Vec3 var0, Vec3 var1, double var2) {
      Vec3 var4 = var1.subtract(var0);
      return var0.add(var4.length() > var2 ? var4.normalize().scale(var2) : var4);
   }

   private c5() {
   }
}
