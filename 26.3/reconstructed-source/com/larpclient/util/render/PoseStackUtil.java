package com.larpclient.util.render;

import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;

public final class PoseStackUtil {
   public static void rotate(PoseStack var0, Quaternionfc var1) {
      var0.rotate(var1);
   }

   public static void multiply(PoseStack var0, Matrix4fc var1) {
      var0.mulPose(var1);
   }

   private PoseStackUtil() {
   }
}
