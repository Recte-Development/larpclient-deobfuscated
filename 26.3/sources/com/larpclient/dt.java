package com.larpclient;

import com.larpclient.module.AuraModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class dt {
   public static Vec2 c(Vec2 var0, float var1, float var2) {
      double var3 = Math.toRadians(var1 - var2);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new Vec2((float)(var0.x * var5 - var0.y * var7), (float)(var0.x * var7 + var0.y * var5));
   }

   public static Vec2 Z(Vec2 var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player != null && !TargetStrafeModule.t() && x() && !q() ? c(var0, ij.f(), var1.player.getYRot()) : var0;
   }

   public static boolean x() {
      ij.r var0 = ij.L();
      if (var0 != ij.r.D
         && var0 != ij.r.s
         && var0 != ij.r.B
         && var0 != ij.r.I
         && var0 != ij.r.w
         && var0 != ij.r.p
         && var0 != ij.r.J
         && var0 != ij.r.q
         && var0 != ij.r.o
         && var0 != ij.r.z) {
         AuraModule var1 = AuraModule.t();
         return var1 != null && var1.h();
      } else {
         return true;
      }
   }

   public static boolean q() {
      if (!Float.isNaN(ScaffoldModule.Y()) && !TargetStrafeModule.t()) {
         return true;
      }

      ij.r var0 = ij.L();
      return (
            var0 == ij.r.w
               || var0 == ij.r.D
               || var0 == ij.r.s
               || var0 == ij.r.B
               || var0 == ij.r.I
               || var0 == ij.r.p
               || var0 == ij.r.J
               || var0 == ij.r.q
               || var0 == ij.r.o
         )
         && !TargetStrafeModule.t();
   }

   public static Input u(LocalPlayer var0, Input var1) {
      if (var0 != null && q()) {
         float var2 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
         float var3 = var1.left() == var1.right() ? 0.0F : (var1.left() ? 1.0F : -1.0F);
         if (var2 == 0.0F && var3 == 0.0F) {
            return var1;
         }

         double var4 = Mth.wrapDegrees(Math.toDegrees(AuraModule.b(A(), var2, var3)));
         float[] var6 = AuraModule.a(var4, var0.getYRot());
         var0.input.moveVector = new Vec2(var6[1], var6[0]).normalized();
         return new Input(var6[0] > 0.0F, var6[0] < 0.0F, var6[1] > 0.0F, var6[1] < 0.0F, var1.jump(), var1.shift(), var1.sprint());
      } else {
         return var1;
      }
   }

   private static float A() {
      float var0 = ScaffoldModule.Y();
      return Float.isNaN(var0) ? ij.f() : var0;
   }

   public static float H(float var0) {
      float var1 = ScaffoldModule.Y();
      if (!Float.isNaN(var1)) {
         return var1;
      } else {
         return x() ? ij.f() : var0;
      }
   }

   private dt() {
   }
}
