package com.larpclient;

import com.larpclient.module.AuraModule;
import com.larpclient.module.TargetStrafeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class ks {
   public static Vec2 H(Vec2 var0, float var1, float var2) {
      double var3 = Math.toRadians(var1 - var2);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new Vec2((float)(var0.x * var5 - var0.y * var7), (float)(var0.x * var7 + var0.y * var5));
   }

   public static Vec2 V(Vec2 var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player != null && !TargetStrafeModule.Z() && N() && !X() ? H(var0, tq.z(), var1.player.getYRot()) : var0;
   }

   public static boolean N() {
      tq.y var0 = tq.l();
      if (var0 != tq.y.J
         && var0 != tq.y.a
         && var0 != tq.y.Y
         && var0 != tq.y.g
         && var0 != tq.y.e
         && var0 != tq.y.K
         && var0 != tq.y.N
         && var0 != tq.y.B
         && var0 != tq.y.P) {
         AuraModule var1 = AuraModule.S();
         return var1 != null && var1.R();
      } else {
         return true;
      }
   }

   public static boolean X() {
      tq.y var0 = tq.l();
      return (
            var0 == tq.y.e
               || var0 == tq.y.J
               || var0 == tq.y.a
               || var0 == tq.y.Y
               || var0 == tq.y.g
               || var0 == tq.y.K
               || var0 == tq.y.N
               || var0 == tq.y.B
               || var0 == tq.y.P
         )
         && !TargetStrafeModule.Z();
   }

   public static Input p(LocalPlayer var0, Input var1) {
      if (var0 != null && X()) {
         float var2 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
         float var3 = var1.left() == var1.right() ? 0.0F : (var1.left() ? 1.0F : -1.0F);
         if (var2 == 0.0F && var3 == 0.0F) {
            return var1;
         }

         double var4 = Mth.wrapDegrees(Math.toDegrees(AuraModule.V(tq.z(), var2, var3)));
         float[] var6 = AuraModule.l(var4, var0.getYRot());
         var0.input.moveVector = new Vec2(var6[1], var6[0]).normalized();
         return new Input(var6[0] > 0.0F, var6[0] < 0.0F, var6[1] > 0.0F, var6[1] < 0.0F, var1.jump(), var1.shift(), var1.sprint());
      } else {
         return var1;
      }
   }

   private ks() {
   }
}
