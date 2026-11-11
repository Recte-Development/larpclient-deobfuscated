package com.larpclient.util.entity;

import com.larpclient.core.ViewController;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.movement.TargetStrafeModule;
import com.larpclient.module.player.ScaffoldModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class MovementCorrection {
   public static Vec2 rotateInput(Vec2 var0, float var1, float var2) {
      double var3 = Math.toRadians(var1 - var2);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new Vec2((float)(var0.x * var5 - var0.y * var7), (float)(var0.x * var7 + var0.y * var5));
   }

   public static Vec2 correctMoveVector(Vec2 var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player != null && !TargetStrafeModule.restoreInput() && isRotationActive() && !isStrictMovementActive()
         ? rotateInput(var0, ViewController.getPlayerYaw(), var1.player.getYRot())
         : var0;
   }

   public static boolean isRotationActive() {
      ViewController.Owner var0 = ViewController.update();
      if (var0 != ViewController.Owner.AUTO_TRAP
         && var0 != ViewController.Owner.AUTO_WEB
         && var0 != ViewController.Owner.SCAFFOLD
         && var0 != ViewController.Owner.AUTO_ANCHOR
         && var0 != ViewController.Owner.MACE
         && var0 != ViewController.Owner.AUTO_CRYSTAL
         && var0 != ViewController.Owner.PEARL_CATCH
         && var0 != ViewController.Owner.WIND_HOP
         && var0 != ViewController.Owner.RETURN
         && var0 != ViewController.Owner.AUTO_BUILDER) {
         AuraModule var1 = AuraModule.getInstance();
         return var1 != null && var1.getRotationProvider();
      } else {
         return true;
      }
   }

   public static boolean isStrictMovementActive() {
      if (!Float.isNaN(ScaffoldModule.getWalkYaw()) && !TargetStrafeModule.restoreInput()) {
         return true;
      }

      ViewController.Owner var0 = ViewController.update();
      return (
            var0 == ViewController.Owner.MACE
               || var0 == ViewController.Owner.AUTO_TRAP
               || var0 == ViewController.Owner.AUTO_WEB
               || var0 == ViewController.Owner.SCAFFOLD
               || var0 == ViewController.Owner.AUTO_ANCHOR
               || var0 == ViewController.Owner.AUTO_CRYSTAL
               || var0 == ViewController.Owner.PEARL_CATCH
               || var0 == ViewController.Owner.WIND_HOP
               || var0 == ViewController.Owner.RETURN
         )
         && !TargetStrafeModule.restoreInput();
   }

   public static Input correctInput(LocalPlayer var0, Input var1) {
      if (var0 != null && isStrictMovementActive()) {
         float var2 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
         float var3 = var1.left() == var1.right() ? 0.0F : (var1.left() ? 1.0F : -1.0F);
         if (var2 == 0.0F && var3 == 0.0F) {
            return var1;
         }

         double var4 = Mth.wrapDegrees(Math.toDegrees(AuraModule.getMoveYaw(getTargetYaw(), var2, var3)));
         float[] var6 = AuraModule.getMoveKeys(var4, var0.getYRot());
         var0.input.moveVector = new Vec2(var6[1], var6[0]).normalized();
         return new Input(var6[0] > 0.0F, var6[0] < 0.0F, var6[1] > 0.0F, var6[1] < 0.0F, var1.jump(), var1.shift(), var1.sprint());
      } else {
         return var1;
      }
   }

   private static float getTargetYaw() {
      float var0 = ScaffoldModule.getWalkYaw();
      return Float.isNaN(var0) ? ViewController.getPlayerYaw() : var0;
   }

   public static float getMovementYaw(float var0) {
      float var1 = ScaffoldModule.getWalkYaw();
      if (!Float.isNaN(var1)) {
         return var1;
      } else {
         return isRotationActive() ? ViewController.getPlayerYaw() : var0;
      }
   }

   private MovementCorrection() {
   }
}
