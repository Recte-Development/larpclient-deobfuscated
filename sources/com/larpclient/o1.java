package com.larpclient;

import net.minecraft.client.renderer.state.LightmapRenderState;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class o1 {
   private o1() {
   }

   public static void d(LightmapRenderState var0, int var1, float var2) {
      if (var0.needsUpdate && !(var2 <= 0.0F) && (var1 & 16777215) != 16777215) {
         float var3 = Math.clamp(var2, 0.0F, 1.0F);
         float var4 = 1.0F + ((var1 >>> 16 & 0xFF) / 255.0F - 1.0F) * var3;
         float var5 = 1.0F + ((var1 >>> 8 & 0xFF) / 255.0F - 1.0F) * var3;
         float var6 = 1.0F + ((var1 & 0xFF) / 255.0F - 1.0F) * var3;
         var0.skyLightColor = v(var0.skyLightColor, var4, var5, var6);
         var0.blockLightTint = v(var0.blockLightTint, var4, var5, var6);
         var0.ambientColor = v(var0.ambientColor, var4, var5, var6);
         var0.nightVisionColor = v(var0.nightVisionColor, var4, var5, var6);
      }
   }

   private static Vector3fc v(Vector3fc var0, float var1, float var2, float var3) {
      return new Vector3f(var0.x() * var1, var0.y() * var2, var0.z() * var3);
   }
}
