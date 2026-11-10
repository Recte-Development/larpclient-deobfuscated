package com.larpclient;

import com.larp.obf.qa300483f54d804a3.q3d74463cab4b2ade;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.state.GameRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo.Style;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class z4 {
   private final com.larpclient.module.l U = new com.larpclient.module.l(
      "Color", "Theme follows your live accent colors", "Theme", "Theme", "Category", "Custom", "Distance", "Health", "Rainbow"
   );
   private final com.larpclient.module.i T = new com.larpclient.module.i("Custom Color", "Color used by Custom mode", -4291585);
   private final com.larpclient.module.e z = new com.larpclient.module.e("Line Width", "Outline width in pixels", 1.5, 0.5, 4.0, 0.25);
   private final com.larpclient.module.e k = new com.larpclient.module.e("Fill Opacity", "Soft fill for Filled mode", 0.12, 0.0, 0.5, 0.02);
   private final com.larpclient.module.s h = new com.larpclient.module.s(
      "Through Walls", "Show boxes and lines behind solid blocks; silhouettes always show through", true
   );

   public z4(Consumer<com.larpclient.module.q<?>> var1) {
      this(var1, true);
   }

   public z4(Consumer<com.larpclient.module.q<?>> var1, boolean var2) {
      this.T.P(() -> this.U.O("Custom"));

      for (com.larpclient.module.q var4 : List.of(this.U, this.T, this.z, this.h)) {
         var1.accept(var4);
      }

      if (var2) {
         var1.accept(this.k);
      }
   }

   public void s(BooleanSupplier var1, BooleanSupplier var2, BooleanSupplier var3, BooleanSupplier var4) {
      this.U.P(var1);
      this.T.P(() -> var1.getAsBoolean() && this.U.O("Custom"));
      this.z.P(var2);
      this.h.P(var3);
      this.k.P(var4);
   }

   public List<com.larpclient.module.q<?>> f() {
      return List.of(this.U, this.T);
   }

   public List<com.larpclient.module.q<?>> O() {
      return List.of(this.z, this.h);
   }

   public List<com.larpclient.module.q<?>> p() {
      return List.of(this.k);
   }

   public boolean t() {
      return this.h.getValue();
   }

   public double H() {
      return this.k.getValue();
   }

   public float g() {
      return this.z.getValue().floatValue();
   }

   public int c(int var1, double var2, double var4, double var6, int var8) {
      int var9 = switch ((String)this.U.getValue()) {
         case "Category" -> var1;
         case "Custom" -> this.T.getValue();
         case "Distance" -> ho.B(Math.clamp(var2 / var4, 0.0, 1.0));
         case "Health" -> uh.q(-1017711, -7347266, var6);
         case "Rainbow" -> uh.n((float)(System.currentTimeMillis() % 6000L) / 6000.0F + var8 * 0.037F, 0.45F, 1.0F);
         default -> ho.B(0.25);
      };
      return uh.F(var9, M(var2, var4));
   }

   public static double M(double var0, double var2) {
      if (!(var2 <= 0.0) && !(var0 >= var2)) {
         double var4 = Math.clamp((var0 / var2 - 0.65) / 0.35, 0.0, 1.0);
         return 1.0 - var4 * var4 * (3.0 - 2.0 * var4);
      } else {
         return 0.0;
      }
   }

   public static List<Entity> G(Iterable<Entity> var0, Entity var1, double var2, int var4, Predicate<Entity> var5) {
      double var6 = var2 * var2;
      ArrayList var8 = new ArrayList();

      for (Entity var10 : var0) {
         if (var10 != var1 && var10.isAlive() && var1.distanceToSqr(var10) <= var6 && var5.test(var10)) {
            var8.add(var10);
         }
      }

      if (var8.size() > 1) {
         var8.sort(Comparator.comparingDouble(var1::distanceToSqr));
      }

      return var8.size() > var4 ? List.copyOf(var8.subList(0, Math.max(0, var4))) : var8;
   }

   public static Matrix4f l(Matrix4f var0) {
      Minecraft var1 = Minecraft.getInstance();
      GameRenderState var2 = var1.gameRenderer.gameRenderState();
      CameraRenderState var3 = var2.levelRenderState.cameraRenderState;
      if (var3 != null && var3.projectionMatrix != null && var3.viewRotationMatrix != null) {
         var0.set(var3.projectionMatrix);

         try {
            q3d74463cab4b2ade var4 = (q3d74463cab4b2ade)var1.gameRenderer;
            PoseStack var5 = new PoseStack();
            var4.larpBobHurt(var3, var5);
            if (var2.optionsRenderState.bobView) {
               var4.larpBobView(var3, var5);
            }

            var0.mul(var5.last().pose());
            LocalPlayer var6 = var1.player;
            if (var6 != null) {
               float var7 = var1.getDeltaTracker().getGameTimeDeltaPartialTick(false);
               float var8 = var2.optionsRenderState.screenEffectScale;
               float var9 = Mth.lerp(var7, var6.oPortalEffectIntensity, var6.portalEffectIntensity);
               float var10 = var6.getEffectBlendFactor(MobEffects.NAUSEA, var7);
               float var11 = Math.max(var9, var10) * var8 * var8;
               if (var11 > 0.0F) {
                  float var12 = 5.0F / (var11 * var11 + 5.0F) - var11 * 0.04F;
                  var12 *= var12;
                  Vector3f var13 = new Vector3f(0.0F, Mth.SQRT_OF_TWO / 2.0F, Mth.SQRT_OF_TWO / 2.0F);
                  float var14 = (var4.larpSpinningEffectTime() + var7 * var4.larpSpinningEffectSpeed()) * (float) (Math.PI / 180.0);
                  var0.rotate(var14, var13).scale(1.0F / var12, 1.0F, 1.0F).rotate(-var14, var13);
               }
            }
         } catch (RuntimeException var15) {
            com.larpclient.h.w("ESP", "Live world projection fell back to the unbobbed camera", var15);
            var0.set(var3.projectionMatrix);
         }

         return var0.mul(var3.viewRotationMatrix);
      } else {
         return var1.gameRenderer.mainCamera().getViewRotationProjectionMatrix(var0);
      }
   }

   public static Vec3 r(Entity var0, float var1) {
      return new Vec3(Mth.lerp(var1, var0.xOld, var0.getX()), Mth.lerp(var1, var0.yOld, var0.getY()), Mth.lerp(var1, var0.zOld, var0.getZ()));
   }

   public static AABB O(Entity var0, float var1) {
      return var0.getBoundingBox().move(r(var0, var1).subtract(var0.position())).inflate(0.025);
   }

   private void A(GizmoProperties var1) {
      if (this.h.getValue()) {
         var1.setAlwaysOnTop();
      }
   }

   public void S(Vec3 var1, Vec3 var2, int var3) {
      this.A(Gizmos.line(var1, var2, var3, this.g()));
   }

   public void R(AABB var1, String var2, int var3) {
      if (var3 >>> 24 != 0) {
         if (var2.equals("Corners")) {
            double var4 = var1.getXsize() * 0.25;
            double var6 = var1.getYsize() * 0.18;
            double var8 = var1.getZsize() * 0.25;

            for (int var10 = 0; var10 < 2; var10++) {
               for (int var11 = 0; var11 < 2; var11++) {
                  for (int var12 = 0; var12 < 2; var12++) {
                     Vec3 var13 = new Vec3(var10 == 0 ? var1.minX : var1.maxX, var11 == 0 ? var1.minY : var1.maxY, var12 == 0 ? var1.minZ : var1.maxZ);
                     this.S(var13, var13.add(var10 == 0 ? var4 : -var4, 0.0, 0.0), var3);
                     this.S(var13, var13.add(0.0, var11 == 0 ? var6 : -var6, 0.0), var3);
                     this.S(var13, var13.add(0.0, 0.0, var12 == 0 ? var8 : -var8), var3);
                  }
               }
            }
         } else {
            int var14 = var2.equals("Filled") ? uh.F(var3, this.k.getValue()) : 0;
            this.A(Gizmos.cuboid(var1, GizmoStyle.strokeAndFill(var3, this.g(), var14)));
         }
      }
   }

   public void y(AABB var1, double var2, int var4) {
      Vec3 var5 = new Vec3(var1.minX - 0.08, var1.minY, var1.minZ);
      this.A(Gizmos.line(var5, var5.add(0.0, var1.getYsize(), 0.0), uh.F(ho.c, (var4 >>> 24) / 255.0), this.g() + 1.0F));
      int var6 = uh.q(-1017711, -7347266, var2);
      this.S(var5, var5.add(0.0, var1.getYsize() * Math.clamp(var2, 0.0, 1.0), 0.0), uh.H(var6, (var4 >>> 24) / 255.0));
   }

   public void d(Vec3 var1, String var2, int var3) {
      this.A(Gizmos.billboardText(var2, var1, Style.forColorAndCentered(var3).withScale(0.7F)));
   }

   public static double g(LivingEntity var0) {
      return zy.N(var0);
   }
}
