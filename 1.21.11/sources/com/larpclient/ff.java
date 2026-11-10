package com.larpclient;

import com.larpclient.module.AuraModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import net.minecraft.class_10185;
import net.minecraft.class_241;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_746;

public final class ff {
   public static class_241 z(class_241 var0, float var1, float var2) {
      double var3 = Math.toRadians(var1 - var2);
      double var5 = Math.cos(var3);
      double var7 = Math.sin(var3);
      return new class_241((float)(var0.field_1343 * var5 - var0.field_1342 * var7), (float)(var0.field_1343 * var7 + var0.field_1342 * var5));
   }

   public static class_241 B(class_241 var0) {
      class_310 var1 = class_310.method_1551();
      return var1.field_1724 != null && !TargetStrafeModule.b() && e() && !C() ? z(var0, wp.o(), var1.field_1724.method_36454()) : var0;
   }

   public static boolean e() {
      wp.z var0 = wp.o();
      if (var0 != wp.z.v
         && var0 != wp.z.r
         && var0 != wp.z.e
         && var0 != wp.z.J
         && var0 != wp.z.m
         && var0 != wp.z.h
         && var0 != wp.z.V
         && var0 != wp.z.g
         && var0 != wp.z.u
         && var0 != wp.z.M) {
         AuraModule var1 = AuraModule.R();
         return var1 != null && var1.Z();
      } else {
         return true;
      }
   }

   public static boolean C() {
      if (!Float.isNaN(ScaffoldModule.f()) && !TargetStrafeModule.b()) {
         return true;
      }

      wp.z var0 = wp.o();
      return (
            var0 == wp.z.m
               || var0 == wp.z.v
               || var0 == wp.z.r
               || var0 == wp.z.e
               || var0 == wp.z.J
               || var0 == wp.z.h
               || var0 == wp.z.V
               || var0 == wp.z.g
               || var0 == wp.z.u
         )
         && !TargetStrafeModule.b();
   }

   public static class_10185 j(class_746 var0, class_10185 var1) {
      if (var0 != null && C()) {
         float var2 = var1.comp_3159() == var1.comp_3160() ? 0.0F : (var1.comp_3159() ? 1.0F : -1.0F);
         float var3 = var1.comp_3161() == var1.comp_3162() ? 0.0F : (var1.comp_3161() ? 1.0F : -1.0F);
         if (var2 == 0.0F && var3 == 0.0F) {
            return var1;
         }

         double var4 = class_3532.method_15338(Math.toDegrees(AuraModule.m(v(), var2, var3)));
         float[] var6 = AuraModule.V(var4, var0.method_36454());
         var0.field_3913.field_55868 = new class_241(var6[1], var6[0]).method_35581();
         return new class_10185(var6[0] > 0.0F, var6[0] < 0.0F, var6[1] > 0.0F, var6[1] < 0.0F, var1.comp_3163(), var1.comp_3164(), var1.comp_3165());
      } else {
         return var1;
      }
   }

   private static float v() {
      float var0 = ScaffoldModule.f();
      return Float.isNaN(var0) ? wp.o() : var0;
   }

   public static float H(float var0) {
      float var1 = ScaffoldModule.f();
      if (!Float.isNaN(var1)) {
         return var1;
      } else {
         return e() ? wp.o() : var0;
      }
   }

   private ff() {
   }
}
