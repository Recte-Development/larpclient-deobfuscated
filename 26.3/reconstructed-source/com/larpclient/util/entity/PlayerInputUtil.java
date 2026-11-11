package com.larpclient.util.entity;

import com.larpclient.LarpClient;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.Module;
import com.larpclient.module.render.FreecamModule;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.misc.SdlWindowShim;
import com.mojang.blaze3d.platform.InputConstants.Key;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec3;

public final class PlayerInputUtil {
   public static boolean isModuleActive(String var0) {
      LarpClient var1 = LarpClient.getInstance();
      if (var1 != null && var1.getModuleManager() != null) {
         Module var2 = var1.getModuleManager().getByName(var0);
         return var2 != null && var2.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean isInWorld() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.level != null && var0.player != null && var0.player.isAlive() && !ViewController.isFreecam();
   }

   public static boolean canAct() {
      return isInWorld();
   }

   public static boolean isKeyDown(KeyMapping var0) {
      Minecraft var1 = Minecraft.getInstance();
      Key var2 = KeyMappingHelper.getBoundKeyOf(var0);
      if (var2.getValue() < 0) {
         return false;
      }

      return (boolean)(switch (var2.getType()) {
         case KEYBOARD -> SdlWindowShim.getKeyState(var1.getWindow().handle(), KeyCodes.fromScancode(var2.getValue())) == 1 ? 1 : 0;
         case MOUSE -> SdlWindowShim.getMouseButtonState(var1.getWindow().handle(), KeyCodes.fromMinecraftButton(var2.getValue())) == 1 ? 1 : 0;
         default -> 0;
      });
   }

   public static Vec3 getInputDirection() {
      Options var0 = Minecraft.getInstance().options;
      return directionFromKeys(var0.keyUp.isDown(), var0.keyDown.isDown(), var0.keyLeft.isDown(), var0.keyRight.isDown());
   }

   public static Vec3 getMovementDirection() {
      if (!ViewController.isFreecam()) {
         return getInputDirection();
      }

      Input var0 = FreecamModule.getControlInput();
      return directionFromKeys(var0.forward(), var0.backward(), var0.left(), var0.right());
   }

   private static Vec3 directionFromKeys(boolean var0, boolean var1, boolean var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      double var5 = (var0 ? 1 : 0) - (var1 ? 1 : 0);
      double var7 = (var2 ? 1 : 0) - (var3 ? 1 : 0);
      double var9 = Math.toRadians(MovementCorrection.getMovementYaw(var4.player.getYRot()));
      return new Vec3(-Math.sin(var9) * var5 + Math.cos(var9) * var7, 0.0, Math.cos(var9) * var5 + Math.sin(var9) * var7).normalize();
   }

   public static Vec3 stepToward(Vec3 var0, Vec3 var1, double var2) {
      Vec3 var4 = var1.subtract(var0);
      return var0.add(var4.length() > var2 ? var4.normalize().scale(var2) : var4);
   }

   private PlayerInputUtil() {
   }
}
