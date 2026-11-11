package com.larpclient.util.data;

import com.larpclient.render.model.CustomItemModel;
import com.larpclient.render.model.KnifeHandTransform;
import com.larpclient.render.model.KnifeMesh;
import com.larpclient.util.render.PoseStackUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record HandPoseSample(Matrix4f weapon, Matrix4f arm) {
   public static HandPoseSample sample(KnifeMesh var0, CustomItemModel var1, int var2, float var3, int var4, float var5) {
      PoseStack var6 = new PoseStack();
      var0.root().transform(var6, var2, var3, var4);
      float var7 = var0.scale() * var5;
      var6.translate(var4 * var1.offsetX * var0.scale(), var1.offsetY * var0.scale(), var1.offsetZ * var0.scale());
      var6.scale(var7, var7, var7);
      var6.translate(-var4 * var1.offsetX, -var1.offsetY, -var1.offsetZ);
      PoseStack var8 = new PoseStack();
      var0.arm().transform(var8, var2, var3, var4);
      if (var0.armFit() != null) {
         var0.armFit().translate(var8, var2, var3, var4);
      }

      Quaternionf var9 = KnifeHandTransform.computePalmRotation(var0);
      Vector3f var10 = KnifeHandTransform.computeGripOffset(var0, var1, var9, var5);
      PoseStackUtil.rotate(var8, new Quaternionf(var9.x, var4 * var9.y, var4 * var9.z, var9.w));
      var8.translate(var4 * var10.x, var10.y, var10.z);
      return new HandPoseSample(new Matrix4f(var6.last().pose()), new Matrix4f(var8.last().pose()));
   }
}
