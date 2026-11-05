package com.larpclient;

import com.larpclient.module.AuraModule;
import com.larpclient.module.TargetStrafeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class oo {
   public static Vec2 A(Vec2 var0, float var1, float var2) {
      double var3 = Math.toRadians(var1 - var2);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new Vec2((float)(var0.x * var5 - var0.y * var7), (float)(var0.x * var7 + var0.y * var5));
   }

   public static Vec2 L(Vec2 var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player != null && !TargetStrafeModule.D() && Z() && !L() ? A(var0, rr.h(), var1.player.getYRot()) : var0;
   }

   public static boolean Z() {
      rr.f var0 = rr.u();
      if (var0 != rr.f.e
         && var0 != rr.f.L
         && var0 != rr.f.x
         && var0 != rr.f.q
         && var0 != rr.f.T
         && var0 != rr.f.y
         && var0 != rr.f.H
         && var0 != rr.f.m
         && var0 != rr.f.E) {
         AuraModule var1 = AuraModule.G();
         return var1 != null && var1.l();
      } else {
         return true;
      }
   }

   public static boolean L() {
      rr.f var0 = rr.u();
      return (
            var0 == rr.f.T
               || var0 == rr.f.e
               || var0 == rr.f.L
               || var0 == rr.f.x
               || var0 == rr.f.q
               || var0 == rr.f.y
               || var0 == rr.f.H
               || var0 == rr.f.m
               || var0 == rr.f.E
         )
         && !TargetStrafeModule.D();
   }

   public static Input i(LocalPlayer var0, Input var1) {
      if (var0 != null && L()) {
         float var2 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
         float var3 = var1.left() == var1.right() ? 0.0F : (var1.left() ? 1.0F : -1.0F);
         if (var2 == 0.0F && var3 == 0.0F) {
            return var1;
         }

         double var4 = Mth.wrapDegrees(Math.toDegrees(AuraModule.P(rr.h(), var2, var3)));
         float[] var6 = AuraModule.g(var4, var0.getYRot());
         var0.input.moveVector = new Vec2(var6[1], var6[0]).normalized();
         return new Input(var6[0] > 0.0F, var6[0] < 0.0F, var6[1] > 0.0F, var6[1] < 0.0F, var1.jump(), var1.shift(), var1.sprint());
      } else {
         return var1;
      }
   }

   private oo() {
   }
}
