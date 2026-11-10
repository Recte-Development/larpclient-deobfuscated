package com.larpclient;

import com.larpclient.module.AuraModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.phys.Vec2;

public final class cj {
   public static Vec2 t(Vec2 var0, float var1, float var2) {
      double var3 = Math.toRadians(var1 - var2);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new Vec2((float)(var0.x * var5 - var0.y * var7), (float)(var0.x * var7 + var0.y * var5));
   }

   public static Vec2 a(Vec2 var0) {
      Minecraft var1 = Minecraft.getInstance();
      return var1.player != null && !TargetStrafeModule.q() && b() && !D() ? t(var0, p_.z(), var1.player.getYRot()) : var0;
   }

   public static boolean b() {
      p_.o var0 = p_.A();
      if (var0 != p_.o.G
         && var0 != p_.o.h
         && var0 != p_.o.k
         && var0 != p_.o.d
         && var0 != p_.o.K
         && var0 != p_.o.D
         && var0 != p_.o.Y
         && var0 != p_.o.y
         && var0 != p_.o.R
         && var0 != p_.o.i) {
         AuraModule var1 = AuraModule.V();
         return var1 != null && var1.e();
      } else {
         return true;
      }
   }

   public static boolean D() {
      if (!Float.isNaN(ScaffoldModule.X()) && !TargetStrafeModule.q()) {
         return true;
      }

      p_.o var0 = p_.A();
      return (
            var0 == p_.o.K
               || var0 == p_.o.G
               || var0 == p_.o.h
               || var0 == p_.o.k
               || var0 == p_.o.d
               || var0 == p_.o.D
               || var0 == p_.o.Y
               || var0 == p_.o.y
               || var0 == p_.o.R
         )
         && !TargetStrafeModule.q();
   }

   public static Input w(LocalPlayer var0, Input var1) {
      if (var0 != null && D()) {
         float var2 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
         float var3 = var1.left() == var1.right() ? 0.0F : (var1.left() ? 1.0F : -1.0F);
         if (var2 == 0.0F && var3 == 0.0F) {
            return var1;
         }

         double var4 = Mth.wrapDegrees(Math.toDegrees(AuraModule.h(f(), var2, var3)));
         float[] var6 = AuraModule.i(var4, var0.getYRot());
         var0.input.moveVector = new Vec2(var6[1], var6[0]).normalized();
         return new Input(var6[0] > 0.0F, var6[0] < 0.0F, var6[1] > 0.0F, var6[1] < 0.0F, var1.jump(), var1.shift(), var1.sprint());
      } else {
         return var1;
      }
   }

   private static float f() {
      float var0 = ScaffoldModule.X();
      return Float.isNaN(var0) ? p_.z() : var0;
   }

   public static float U(float var0) {
      float var1 = ScaffoldModule.X();
      if (!Float.isNaN(var1)) {
         return var1;
      } else {
         return b() ? p_.z() : var0;
      }
   }

   private cj() {
   }
}
