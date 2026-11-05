package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.monster.dragon.EnderDragonModel;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

public final class rn {
   public static final Identifier d = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon.png");
   public static final float b = 0.15F;
   public static final float X = 2.25F;
   public static final float m = 3.4F;
   public static final float M = 4.2F;
   public static final int A = 48;
   public static final float f = 50.0F;
   public static final float N = 64.0F;

   public static float p(boolean var0) {
      return var0 ? 4.2F : 3.4F;
   }

   public static float u(float var0) {
      return 2.1F + 4.0F * (float)Math.sqrt(2.0) * 0.15F * var0;
   }

   public static float s(float var0, float var1, float var2, float var3) {
      float var4 = a(var0);
      float var5 = a(var1);
      float var6 = a(var2);
      float var7 = a(var3);
      float var8 = 0.12566371F * (1.0F + 0.45F * var4 + 1.1F * var6) * (1.0F - 0.25F * var5);
      return T(var8, 0.09817477F, var7);
   }

   public static rn.z a(float var0, float var1, float var2, float var3, float var4) {
      float var5 = a(var1);
      float var6 = a(var2);
      float var7 = a(var3);
      float var8 = a(var4);
      float var9 = T(-85.0F, -78.0F, var5);
      float var10 = -12.0F - 6.0F * var5 - 10.0F * var6 - 4.0F * var7 * (1.0F - var5);
      float var11 = -28.0F + 16.0F * var6;
      float var12 = -6.0F + 6.0F * var5 + 2.0F * var6;
      float var13 = (0.16F + 0.06F * var5 + 0.3F * var7 * (1.0F - 0.35F * var5)) * (1.0F - 0.6F * var6);
      var9 = T(var9, -85.0F, var8);
      var10 = T(var10, -8.0F, var8);
      var11 = T(var11, -4.0F, var8);
      var12 = T(var12, -5.0F, var8);
      var13 = T(var13, 0.1F, var8);
      return new rn.z(
         k(var9) - (float)Math.cos(var0) * 0.2F * var13,
         k(var10) + (float)Math.sin(var0 - 0.6F) * 0.24F * var13,
         k(var11) - (float)(Math.sin(var0) + 0.125) * 0.8F * var13,
         k(var12) - (float)(Math.sin(var0 + 2.0F) + 0.5) * 0.75F * 0.6F * var13
      );
   }

   public static float A(float var0, boolean var1, float var2) {
      return var1 ? var0 * Math.clamp(var2 * var2 / 100.0F, 0.0F, 1.0F) : var0;
   }

   public static float W(int var0) {
      double var1 = var0 * 0.6180339887498949 % 1.0;
      return (float)((var1 < 0.0 ? var1 + 1.0 : var1) * 2.0 * Math.PI);
   }

   public static RenderType X(int var0) {
      return var0 >>> 24 == 255 ? RenderTypes.entityCutout(d) : RenderTypes.entityTranslucent(d);
   }

   private static float a(float var0) {
      return Math.clamp(var0, 0.0F, 1.0F);
   }

   private static float T(float var0, float var1, float var2) {
      return var0 + (var1 - var0) * var2;
   }

   private static float k(float var0) {
      return var0 * (float) (Math.PI / 180.0);
   }

   private rn() {
   }

   public static final class k {
      private final ModelPart R;
      private final ModelPart y;
      private final ModelPart O;
      private final ModelPart W;

      public k(ModelPart var1) {
         ModelPart var2 = var1.getChild("body");
         this.R = var2.getChild("left_wing");
         this.y = this.R.getChild("left_wing_tip");
         this.O = var2.getChild("right_wing");
         this.W = this.O.getChild("right_wing_tip");
      }

      public static rn.k H() {
         return new rn.k(EnderDragonModel.createBodyLayer().bakeRoot());
      }

      public ModelPart Z(int var1) {
         return var1 > 0 ? this.R : this.O;
      }

      public void o(rn.z var1) {
         this.R.setPos(0.0F, 0.0F, 0.0F);
         this.O.setPos(0.0F, 0.0F, 0.0F);
         this.R.setRotation(var1.wingX(), var1.wingY(), var1.wingZ());
         this.O.setRotation(var1.wingX(), -var1.wingY(), -var1.wingZ());
         this.y.setRotation(0.0F, 0.0F, var1.tipZ());
         this.W.setRotation(0.0F, 0.0F, -var1.tipZ());
      }

      public static void u(PoseStack var0, int var1, float var2, boolean var3) {
         var0.translate(var1 * 2.25F / 16.0F, rn.p(var3) / 16.0F, rn.u(var2) / 16.0F);
         float var4 = 0.15F * var2;
         var0.scale(var4, var4, var4);
      }

      public void p(PoseStack var1, VertexConsumer var2, float var3, boolean var4, int var5, int var6) {
         for (byte var7 = -1; var7 <= 1; var7 += 2) {
            var1.pushPose();

            try {
               u(var1, var7, var3, var4);
               this.Z(var7).render(var1, var2, var5, OverlayTexture.NO_OVERLAY, var6);
            } finally {
               var1.popPose();
            }
         }
      }
   }

   public record z(float wingX, float wingY, float wingZ, float tipZ) {
   }
}
