package com.larpclient;

import com.larp.obf.q5682c6a8aca9b6ec.q1e1bb13db291d59c.q6e1212582deeb4b9.q2b1d6e43b7854b4d;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelPart.Cube;
import net.minecraft.client.model.geom.ModelPart.Polygon;
import net.minecraft.client.model.geom.ModelPart.Vertex;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.HumanoidArm;

public final class pv {
   private static final float I = 4.0F;
   private static final float M = 6.0F;
   private final ModelPart[] G;
   private final ModelPart[] K;
   private final ModelPart[] k;
   private final ModelPart[] t;
   private final boolean[] f = new boolean[8];
   private final boolean[] w = new boolean[8];
   private boolean a;

   public pv(PlayerModel var1) {
      this.G = new ModelPart[]{var1.rightArm, var1.leftArm, var1.rightLeg, var1.leftLeg, var1.rightSleeve, var1.leftSleeve, var1.rightPants, var1.leftPants};
      this.K = new ModelPart[4];
      this.k = new ModelPart[4];
      this.t = new ModelPart[8];

      for (int var2 = 0; var2 < 4; var2++) {
         float var3 = var2 < 2 ? 4.0F : 6.0F;
         pv.i var4 = pv.i.of(this.G[var2]);
         if (var4 != null) {
            ModelPart var5 = this.G[var2 + 4];
            pv.i var6 = var5.getInitialPose().equals(PartPose.ZERO) ? pv.i.of(var5) : null;
            LinkedHashMap var7 = new LinkedHashMap();
            LinkedHashMap var8 = new LinkedHashMap();
            if (var6 != null) {
               this.t[var2] = new ModelPart(List.of(var6.upper(var3)), Map.of());
               this.t[var2 + 4] = new ModelPart(List.of(var6.lower(var3)), Map.of());
               var8.put("layer", this.t[var2 + 4]);
               this.w[var2 + 4] = (boolean)1;
            }

            this.k[var2] = new ModelPart(List.of(var4.lower(var3)), var8);
            this.k[var2].setInitialPose(PartPose.offset(0.0F, var3, 0.0F));
            this.k[var2].resetPose();
            var7.put("joint", this.k[var2]);
            if (this.t[var2] != null) {
               var7.put("layer", this.t[var2]);
            }

            ModelPart var9 = new ModelPart(List.of(var4.upper(var3)), var7);
            var9.visible = (boolean)0;
            this.K[var2] = var9;
            this.w[var2] = (boolean)1;
            ((q2b1d6e43b7854b4d)this.G[var2]).larpWalkChildren().put("larp_walk_joint", var9);
         }
      }
   }

   public static ModelPart O(int var0, boolean var1) {
      int var2 = var0 < 2 ? 1 : 0;
      int var3 = (var0 & 1) == 1 ? 1 : 0;
      int var4 = var2 != 0 && var1 ? 3 : 4;
      float var5 = var2 != 0 ? (var3 != 0 ? -1.0F : (var1 ? -2.0F : -3.0F)) : -2.0F;
      float var6 = var2 != 0 ? -2.0F : 0.0F;
      float var7 = var2 != 0 ? 4.0F : 6.0F;
      int var8 = var2 != 0 ? (var3 != 0 ? 32 : 40) : (var3 != 0 ? 16 : 0);
      int var9 = var3 != 0 ? 48 : 16;
      int var10 = var2 != 0 ? (var3 != 0 ? 48 : 40) : (var3 != 0 ? 0 : 0);
      int var11 = var3 != 0 ? 48 : 32;
      MeshDefinition var12 = new MeshDefinition();
      PartDefinition var13 = var12.getRoot()
         .addOrReplaceChild("upper", CubeListBuilder.create().texOffs(var8, var9).addBox(var5, var6, -2.0F, var4, 6.0F, 4.0F), PartPose.ZERO);
      var13.addOrReplaceChild(
         "layer", CubeListBuilder.create().texOffs(var10, var11).addBox(var5, var6, -2.0F, var4, 6.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.ZERO
      );
      PartDefinition var14 = var13.addOrReplaceChild(
         "joint", CubeListBuilder.create().texOffs(var8, var9 + 5).addBox(var5, -1.0F, -2.0F, var4, 7.0F, 4.0F), PartPose.offset(0.0F, var7, 0.0F)
      );
      var14.addOrReplaceChild(
         "layer", CubeListBuilder.create().texOffs(var10, var11 + 5).addBox(var5, -1.0F, -2.0F, var4, 7.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.ZERO
      );
      return LayerDefinition.create(var12, 64, 64).bakeRoot().getChild("upper");
   }

   public static Cube B(List<Cube> var0) {
      pv.i var1 = pv.i.of(var0);
      return var1 == null ? null : var1.whole();
   }

   public void q() {
      if (this.a) {
         for (int var1 = 0; var1 < 8; var1++) {
            if (this.w[var1]) {
               this.G[var1].skipDraw = this.f[var1];
            }
         }
      }

      for (ModelPart var4 : this.K) {
         if (var4 != null) {
            var4.visible = (boolean)0;
         }
      }

      this.a = (boolean)0;
   }

   public void n(HumanoidArm var1, PoseStack var2) {
      ModelPart var3 = this.k[var1 == HumanoidArm.RIGHT ? 0 : 1];
      if (this.a && var3 != null) {
         var2.translate(0.0, 0.25, 0.0);
         fx.b(var2, Axis.XP.rotation(var3.xRot));
         var2.translate(0.0, -0.25, 0.0);
      }
   }

   public void O(float[] var1, AvatarRenderState var2) {
      if (var1 != null) {
         for (int var3 = 0; var3 < 8; var3++) {
            if (this.w[var3]) {
               this.f[var3] = this.G[var3].skipDraw;
               this.G[var3].skipDraw = (boolean)1;
            }
         }

         for (int var4 = 0; var4 < 4; var4++) {
            if (this.K[var4] != null) {
               this.K[var4].visible = (boolean)1;
               this.k[var4].xRot = var1[var4];
               if (this.t[var4] != null) {
                  this.t[var4].visible = this.t[var4 + 4].visible = this.G[var4 + 4].visible;
               }
            }
         }

         this.a = (boolean)1;
      }
   }

   private record i(
      int u, int v, float x, float y, float z, float w, float h, float d, float growX, float growY, float growZ, boolean mirror, float texW, float texH
   ) {
      static pv.i of(ModelPart var0) {
         return of(((q2b1d6e43b7854b4d)var0).larpWalkCubes());
      }

      static pv.i of(List<Cube> var0) {
         if (var0.size() != 1) {
            return null;
         }

         Cube var1 = (Cube)var0.getFirst();
         float var2 = var1.maxX - var1.minX;
         float var3 = var1.maxY - var1.minY;
         float var4 = var1.maxZ - var1.minZ;
         if (!(var2 <= 0.0F) && !(var3 <= 0.0F) && !(var4 <= 0.0F)) {
            float var5 = Float.MAX_VALUE;
            float var6 = -Float.MAX_VALUE;
            float var7 = Float.MAX_VALUE;
            float var8 = -Float.MAX_VALUE;
            float var9 = Float.MAX_VALUE;
            float var10 = -Float.MAX_VALUE;
            float var11 = Float.MAX_VALUE;
            float var12 = Float.MAX_VALUE;

            for (Polygon var16 : var1.polygons) {
               for (Vertex var20 : var16.vertices()) {
                  var5 = Math.min(var5, var20.x());
                  var6 = Math.max(var6, var20.x());
                  var7 = Math.min(var7, var20.y());
                  var8 = Math.max(var8, var20.y());
                  var9 = Math.min(var9, var20.z());
                  var10 = Math.max(var10, var20.z());
                  var11 = Math.min(var11, var20.u());
                  var12 = Math.min(var12, var20.v());
               }
            }

            float var31 = 0.0F;
            float var32 = 0.0F;
            Boolean var33 = null;

            for (Polygon var37 : var1.polygons) {
               Vertex[] var38 = var37.vertices();
               float var21 = Float.MAX_VALUE;
               float var22 = -Float.MAX_VALUE;
               float var23 = Float.MAX_VALUE;
               float var24 = -Float.MAX_VALUE;
               byte var25 = 1;
               byte var26 = 1;

               for (Vertex var30 : var38) {
                  var21 = Math.min(var21, var30.u());
                  var22 = Math.max(var22, var30.u());
                  var23 = Math.min(var23, var30.v());
                  var24 = Math.max(var24, var30.v());
                  var25 &= Math.abs(var30.y() - var7) < 1.0E-4 ? 1 : 0;
                  var26 &= Math.abs(var30.x() - var38[0].x()) < 1.0E-4 ? 1 : 0;
               }

               if (var25 != 0 && var22 > var21 && var24 > var23) {
                  var31 = var2 / (var22 - var21);
                  var32 = var4 / (var24 - var23);
               }

               if (var26 != 0 && Math.abs(var21 - var11) < 1.0E-5) {
                  var33 = Math.abs(var38[0].x() - var6) < 1.0E-4;
               }
            }

            return !(var31 <= 0.0F) && !(var32 <= 0.0F) && var33 != null
               ? new pv.i(
                  Math.round(var11 * var31),
                  Math.round(var12 * var32),
                  var1.minX,
                  var1.minY,
                  var1.minZ,
                  var2,
                  var3,
                  var4,
                  (var6 - var5 - var2) / 2.0F,
                  (var8 - var7 - var3) / 2.0F,
                  (var10 - var9 - var4) / 2.0F,
                  var33,
                  Math.round(var31),
                  Math.round(var32)
               )
               : null;
         } else {
            return null;
         }
      }

      Cube whole() {
         return this.cube(this.y, this.h, 0);
      }

      Cube upper(float var1) {
         return this.cube(this.y, var1 - this.y, 0);
      }

      Cube lower(float var1) {
         float var2 = var1 - 1.0F;
         return this.cube(var2 - var1, this.y + this.h - var2, Math.round(var2 - this.y));
      }

      private Cube cube(float var1, float var2, int var3) {
         return new Cube(
            this.u,
            this.v + var3,
            this.x,
            var1,
            this.z,
            this.w,
            var2,
            this.d,
            this.growX,
            this.growY,
            this.growZ,
            this.mirror,
            this.texW,
            this.texH,
            EnumSet.allOf(Direction.class)
         );
      }
   }
}
