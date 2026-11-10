package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.qa2358e2c2070ffe8;
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

public final class j5 {
   private final com.larpclient.module.o p = new com.larpclient.module.o(
      "Color", "Theme follows your live accent colors", "Theme", "Theme", "Category", "Custom", "Distance", "Health", "Rainbow"
   );
   private final com.larpclient.module.h x = new com.larpclient.module.h("Custom Color", "Color used by Custom mode", -4291585);
   private final com.larpclient.module.b P = new com.larpclient.module.b("Line Width", "Outline width in pixels", 1.5, 0.5, 4.0, 0.25);
   private final com.larpclient.module.b s = new com.larpclient.module.b("Fill Opacity", "Soft fill for Filled mode", 0.12, 0.0, 0.5, 0.02);
   private final com.larpclient.module.m k = new com.larpclient.module.m(
      "Through Walls", "Show boxes and lines behind solid blocks; silhouettes always show through", true
   );

   public j5(Consumer<com.larpclient.module.r<?>> var1) {
      this(var1, true);
   }

   public j5(Consumer<com.larpclient.module.r<?>> var1, boolean var2) {
      this.x.a(() -> this.p.w("Custom"));

      for (com.larpclient.module.r var4 : List.of(this.p, this.x, this.P, this.k)) {
         var1.accept(var4);
      }

      if (var2) {
         var1.accept(this.s);
      }
   }

   public void q(BooleanSupplier var1, BooleanSupplier var2, BooleanSupplier var3, BooleanSupplier var4) {
      this.p.a(var1);
      this.x.a(() -> var1.getAsBoolean() && this.p.w("Custom"));
      this.P.a(var2);
      this.k.a(var3);
      this.s.a(var4);
   }

   public List<com.larpclient.module.r<?>> Q() {
      return List.of(this.p, this.x);
   }

   public List<com.larpclient.module.r<?>> l() {
      return List.of(this.P, this.k);
   }

   public List<com.larpclient.module.r<?>> M() {
      return List.of(this.s);
   }

   public boolean m() {
      return this.k.getValue();
   }

   public double r() {
      return this.s.getValue();
   }

   public float q() {
      return this.P.getValue().floatValue();
   }

   public int p(int var1, double var2, double var4, double var6, int var8) {
      int var9 = switch ((String)this.p.getValue()) {
         case "Category" -> var1;
         case "Custom" -> this.x.getValue();
         case "Distance" -> c.A(Math.clamp(var2 / var4, 0.0, 1.0));
         case "Health" -> vv.Q(-1017711, -7347266, var6);
         case "Rainbow" -> vv.b((float)(System.currentTimeMillis() % 6000L) / 6000.0F + var8 * 0.037F, 0.45F, 1.0F);
         default -> c.A(0.25);
      };
      return vv.O(var9, K(var2, var4));
   }

   public static double K(double var0, double var2) {
      if (!(var2 <= 0.0) && !(var0 >= var2)) {
         double var4 = Math.clamp((var0 / var2 - 0.65) / 0.35, 0.0, 1.0);
         return 1.0 - var4 * var4 * (3.0 - 2.0 * var4);
      } else {
         return 0.0;
      }
   }

   public static List<Entity> n(Iterable<Entity> var0, Entity var1, double var2, int var4, Predicate<Entity> var5) {
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

   public static Matrix4f I(Matrix4f var0) {
      Minecraft var1 = Minecraft.getInstance();
      GameRenderState var2 = var1.gameRenderer.gameRenderState();
      CameraRenderState var3 = var2.levelRenderState.cameraRenderState;
      if (var3 != null && var3.projectionMatrix != null && var3.viewRotationMatrix != null) {
         var0.set(var3.projectionMatrix);

         try {
            qa2358e2c2070ffe8 var4 = (qa2358e2c2070ffe8)var1.gameRenderer;
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
                  float var14 = var6.getSpinningEffectAngle(var7) * (float) (Math.PI / 180.0);
                  var0.rotate(var14, var13).scale(1.0F / var12, 1.0F, 1.0F).rotate(-var14, var13);
               }
            }
         } catch (RuntimeException var15) {
            _q.g("ESP", "Live world projection fell back to the unbobbed camera", var15);
            var0.set(var3.projectionMatrix);
         }

         return var0.mul(var3.viewRotationMatrix);
      } else {
         return var1.gameRenderer.mainCamera().getViewRotationProjectionMatrix(var0);
      }
   }

   public static Vec3 J(Entity var0, float var1) {
      return new Vec3(Mth.lerp(var1, var0.xOld, var0.getX()), Mth.lerp(var1, var0.yOld, var0.getY()), Mth.lerp(var1, var0.zOld, var0.getZ()));
   }

   public static AABB X(Entity var0, float var1) {
      return var0.getBoundingBox().move(J(var0, var1).subtract(var0.position())).inflate(0.025);
   }

   private void t(GizmoProperties var1) {
      if (this.k.getValue()) {
         var1.setAlwaysOnTop();
      }
   }

   public void q(Vec3 var1, Vec3 var2, int var3) {
      this.t(Gizmos.line(var1, var2, var3, this.q()));
   }

   public void L(AABB var1, String var2, int var3) {
      if (var3 >>> 24 != 0) {
         if (var2.equals("Corners")) {
            double var4 = var1.getXsize() * 0.25;
            double var6 = var1.getYsize() * 0.18;
            double var8 = var1.getZsize() * 0.25;

            for (int var10 = 0; var10 < 2; var10++) {
               for (int var11 = 0; var11 < 2; var11++) {
                  for (int var12 = 0; var12 < 2; var12++) {
                     Vec3 var13 = new Vec3(var10 == 0 ? var1.minX : var1.maxX, var11 == 0 ? var1.minY : var1.maxY, var12 == 0 ? var1.minZ : var1.maxZ);
                     this.q(var13, var13.add(var10 == 0 ? var4 : -var4, 0.0, 0.0), var3);
                     this.q(var13, var13.add(0.0, var11 == 0 ? var6 : -var6, 0.0), var3);
                     this.q(var13, var13.add(0.0, 0.0, var12 == 0 ? var8 : -var8), var3);
                  }
               }
            }
         } else {
            int var14 = var2.equals("Filled") ? vv.O(var3, this.s.getValue()) : 0;
            this.t(Gizmos.cuboid(var1, GizmoStyle.strokeAndFill(var3, this.q(), var14)));
         }
      }
   }

   public void u(AABB var1, double var2, int var4) {
      Vec3 var5 = new Vec3(var1.minX - 0.08, var1.minY, var1.minZ);
      this.t(Gizmos.line(var5, var5.add(0.0, var1.getYsize(), 0.0), vv.O(c.n, (var4 >>> 24) / 255.0), this.q() + 1.0F));
      int var6 = vv.Q(-1017711, -7347266, var2);
      this.q(var5, var5.add(0.0, var1.getYsize() * Math.clamp(var2, 0.0, 1.0), 0.0), vv.N(var6, (var4 >>> 24) / 255.0));
   }

   public void P(Vec3 var1, String var2, int var3) {
      this.t(Gizmos.billboardText(var2, var1, Style.forColorAndCentered(var3).withScale(0.7F)));
   }

   public static double w(LivingEntity var0) {
      return wu.D(var0);
   }
}
