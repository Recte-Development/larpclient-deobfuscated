package com.larpclient;

import com.larpclient.module.CapesModule;
import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.HeadlessKorbloxModule;
import com.larpclient.module.SkinChangerModule;
import com.larpclient.module.WingsModule;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.gui.pip.GuiSkinRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector3f;

public final class pa {
   private static final int R = 15728880;
   static final float Q = 1.85F;
   static final float z = 0.15F;
   static final float d = 1.4F;
   static final float b = 0.15F;
   private final ut.r N = new ut.r();
   private hk.t n;
   private String f = "";
   private static int V = -1;
   private static final db i = new db(0L);

   static ChinaHatModule.d X() {
      ChinaHatModule var0 = ChinaHatModule.i();
      return var0 == null ? null : var0.w();
   }

   static WingsModule.n X() {
      WingsModule var0 = WingsModule.d();
      return var0 == null ? null : var0.X();
   }

   static boolean d() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.k();
      return var0 != null && var0.c();
   }

   static u3 X() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.k();
      return var0 == null ? null : var0.o();
   }

   static double Y(ChinaHatModule.d var0, WingsModule.n var1) {
      double var2 = var0 == null ? 0.0 : var0.rise() + 0.05;
      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 0.15F : 0.15F) * var1.size());
      }

      return var2;
   }

   static double o(ChinaHatModule.d var0, WingsModule.n var1) {
      double var2 = var0 == null ? 0.0 : var0.radius() + 0.05;
      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 1.4F : 1.85F) * var1.size());
      }

      return var2;
   }

   static PlayerSkin g(UUID var0, PlayerSkin var1) {
      PlayerSkin var2 = t7.n(var0, var1);
      SkinChangerModule var3 = SkinChangerModule.X();
      if (var3 != null) {
         var2 = var3.F(var2);
      }

      CapesModule var4 = CapesModule.u();
      return var4 == null ? var2 : var4.T(var2);
   }

   static void T(AvatarRenderState var0, zt.n var1) {
      float var2 = var1.walkSpeed() / 4.0F;
      var0.capeLean = Math.clamp(var2 * 300.0F, 0.0F, 150.0F);
      var0.capeLean2 = 0.0F;
      float var3 = var1.walkPosition() * 0.15F;
      float var4 = Math.min(0.1F, var2);
      var0.capeFlap = (float)Math.sin(var3 * 6.0F) * 32.0F * var4;
   }

   static Identifier h(PlayerSkin var0) {
      return var0.cape() != null && Minecraft.getInstance().options.isModelPartEnabled(PlayerModelPart.CAPE) ? var0.cape().texturePath() : null;
   }

   pa.h I(Identifier var1, ChinaHatModule.d var2, WingsModule.n var3, zt.n var4, PlayerCapeModel var5) {
      return this.a(var1, var2, var3, false, null, var4, var5);
   }

   pa.h a(Identifier var1, ChinaHatModule.d var2, WingsModule.n var3, boolean var4, u3 var5, zt.n var6, PlayerCapeModel var7) {
      if (var7 == null) {
         var1 = null;
      }

      float var8 = var6.ageInTicks();
      this.N.i(var8, false, var6.walkSpeed(), false, false, Vec3.ZERO);
      hk.v var9 = null;
      if (var3 != null && var3.dragon()) {
         if (this.n == null) {
            this.n = hk.t.T();
         }

         this.N.I(hk.g(this.N.d(), 0.0F, 0.0F, 0.0F));
         var9 = hk.w(this.N.l(), this.N.d(), 0.0F, 0.0F, 0.0F);
      }

      String var10 = (var1 == null ? "-" : var1)
         + "|"
         + (var2 == null ? "-" : var2.label())
         + "|"
         + (var3 == null ? "-" : (var3.dragon() ? "dragon" : "luminous"))
         + "|"
         + var4
         + "|"
         + (var5 == null ? "-" : var5.legs());
      if (!var10.equals(this.f)) {
         this.f = var10;
         mt.K(
            "Menu",
            "Account model cosmetics: cape={}, hat={}, wings={}, headless={}, korblox={}",
            var1 == null ? "none" : var1,
            var2 == null ? "none" : var2.label(),
            var3 == null ? "none" : (var3.dragon() ? "Ender Dragon" : "Luminous"),
            var4,
            var5 == null ? "none" : (var5.right() && var5.left() ? "both legs" : (var5.right() ? "right leg" : "left leg"))
         );
      }

      if (var1 == null && var2 == null && var3 == null && !var4 && var5 == null) {
         return null;
      }

      int var11 = var3 == null ? 0 : WingsModule.F(var3.first(), var3.second(), var8 / 20.0);
      int var12 = var3 == null ? 0 : WingsModule.G(var3.first(), var3.second(), var8 / 20.0);
      return new pa.h(var7, (Identifier)var1, var2, var3, var11, var12, this.n, var9, var8, this.N.d(), var4, var5);
   }

   public static void G(GuiSkinRenderState var0, Matrix4fc var1, PoseStack var2, SubmitNodeCollector var3) {
      if (var0.playerModel() instanceof w_ var4 && var4.y() != null) {
         int var13 = m5.k;
         if (V != var13) {
            pa.h var6 = var4.y();

            try {
               if (var6.cape() != null) {
                  var3.submitModel(var6.capeModel(), var4.d(), var2, RenderTypes.entitySolid(var6.cape()), 15728880, OverlayTexture.NO_OVERLAY, 0);
               }

               u3 var7 = var6.korblox();
               if (var7 != null) {
                  if (var7.right()) {
                     uf.A(var2, var3, var4.G(), var7, 15728880, OverlayTexture.NO_OVERLAY, false);
                  }

                  if (var7.left()) {
                     uf.A(var2, var3, var4.N(), var7, 15728880, OverlayTexture.NO_OVERLAY, true);
                  }
               }

               WingsModule.n var8 = var6.wings();
               if (var8 != null && var8.dragon()) {
                  PoseStack var9 = new PoseStack();
                  var9.last().set(var2.last());
                  var4.r().translateAndRotate(var9);
                  hk.t var10 = var6.dragon();
                  hk.v var11 = var6.angles();
                  var3.order(1).submitCustomGeometry(var9, hk.Y(var8.dragonColor()), (var3x, var4x) -> {
                     PoseStack var5 = new PoseStack();
                     var5.last().set(var3x);
                     var10.A(var11);
                     var10.u(var5, var4x, var8.size(), false, 15728880, var8.dragonColor());
                  });
               }

               pa.i var14 = new pa.i();
               Q(var14, var4, new Matrix4f(var1).invert().mul(var2.last().pose()), var6);
               PoseStack var15 = new PoseStack();
               var15.last().pose().set(var1);
               var14.y(var3, var15, 2);
            } catch (RuntimeException var12) {
               V = var13;
               mt.P("Menu", "Account model cosmetics submission failed; body only until resource reload", var12);
            }
         }
      }
   }

   static void Q(pa.i var0, w_ var1, Matrix4f var2, pa.h var3) {
      ChinaHatModule.d var4 = var3.hat();
      if (var4 != null) {
         PoseStack var5 = new PoseStack();
         var5.last().pose().set(var2);
         var1.O().translateAndRotate(var5);
         var0.blend(pf.x.S);
         ml.w var6 = var4.horns() != null ? ml.u() : null;
         if (var6 != null) {
            float var7 = (float)var4.radius();
            Vector3f var8 = var2.transformDirection(0.0F, -1.0F, 0.0F, new Vector3f()).normalize();
            Vector3f var9 = var5.last().pose().transformPosition(0.0F, -0.5F, var6.centerZ() * var7, new Vector3f()).add(new Vector3f(var8).mul(0.24F * var7));
            i.S(var3.age() / 20.0, var9.x, var9.y, var9.z, var8.x, var8.y, var8.z, var7 * var4.horns().size(), var4.horns().heat());
            var0.E(var5.last().pose(), var4.radius(), var4.horns(), i);
         } else if (var4.accessory() != null) {
            List var11 = _6.V(var4.accessory());
            int var12 = !var11.isEmpty() && pr.I("Menu") ? 1 : 0;
            _6.R(var0, var5.last().pose(), var4.accessory(), var4.radius(), (var4.top() >>> 24) / 255.0, var12 == 0);
            if (var12 != 0) {
               for (_6.y var10 : var11) {
                  var0.w(var10.coin(), _6.E(var5.last().pose(), var10, var4.radius(), new Matrix4f()));
               }
            }
         } else if (var4.kippah()) {
            xc.g(var0, var5.last().pose(), var4.radius(), var4.height(), var4.offset(), var4.top(), var4.rim(), var4.outline());
         } else {
            ub.F(var0, var5.last().pose(), var4.radius(), var4.height(), var4.offset(), var4.top(), var4.rim(), var4.outline());
         }
      }

      if (var3.wings() != null && !var3.wings().dragon()) {
         U(var0, var1, var2, var3, var3.wings());
      }
   }

   private static void U(pa.i var0, w_ var1, Matrix4f var2, pa.h var3, WingsModule.n var4) {
      PoseStack var5 = new PoseStack();
      var5.last().pose().set(var2);
      var1.r().translateAndRotate(var5);
      ut.i var6 = ut.O(0.0F, 0.0F, 0.0F, 0.0F);
      ut.c var7 = ut.Z(var3.age(), var3.walking(), 0.0F, 0.0F);
      ut.v[] var8 = new ut.v[2];
      byte var9 = -1;

      for (int var10 = 0; var9 <= 1; var10++) {
         Matrix4f var11 = new Matrix4f(var5.last().pose()).mul(ut.G(var6, var3.age(), var3.walking(), var4.size(), var9, false));
         var8[var10] = ut.m(Vec3.ZERO, var11, var9, var7, var3.primary(), var3.secondary(), var4.size());
         var9 += 2;
      }

      for (ut.k var12 : ut.k.values()) {
         for (ut.v var16 : var8) {
            ut.Q(var0, var16, var12);
         }
      }
   }

   record h(
      PlayerCapeModel capeModel,
      Identifier cape,
      ChinaHatModule.d hat,
      WingsModule.n wings,
      int primary,
      int secondary,
      hk.t dragon,
      hk.v angles,
      float age,
      float walking,
      boolean headless,
      u3 korblox
   ) {
   }

   static final class i implements s {
      private final List<pa.i.x> Q = new ArrayList<>();
      private final List<pa.i.v> g = new ArrayList<>();
      private pa.i.k X;
      private final Vector3f V = new Vector3f();
      private pf.x i = pf.x.S;
      private pf.x W;
      private boolean e;
      private boolean M;
      private float[] f = new float[768];
      private int[] m = new int[256];
      private int q;

      @Override
      public void blend(pf.x var1) {
         this.i = var1;
      }

      @Override
      public void solid(boolean var1) {
         this.e = var1;
      }

      void w(int var1, Matrix4f var2) {
         this.g.add(new pa.i.v(var1, var2));
      }

      void E(Matrix4f var1, double var2, ChinaHatModule.z var4, db var5) {
         this.X = new pa.i.k(new Matrix4f(var1), var2, var4, var5);
      }

      @Override
      public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
         var1.transformPosition(var2, var3, var4, this.V);
         this.vertex(this.V.x, this.V.y, this.V.z, var5);
      }

      @Override
      public void vertex(double var1, double var3, double var5, int var7) {
         if (this.W != this.i || this.M != this.e) {
            this.e();
            this.W = this.i;
            this.M = this.e;
         }

         if (this.q == this.m.length) {
            this.m = Arrays.copyOf(this.m, this.q * 2);
            this.f = Arrays.copyOf(this.f, this.q * 6);
         }

         this.f[this.q * 3] = (float)var1;
         this.f[this.q * 3 + 1] = (float)var3;
         this.f[this.q * 3 + 2] = (float)var5;
         this.m[this.q++] = var7;
      }

      @Override
      public void ribbon(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
         double var7 = var2.x - var1.x;
         double var9 = var2.y - var1.y;
         double var11 = var2.z - var1.z;
         if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
            double var13 = Math.hypot(var7, var9);
            double var15 = var13 < 1.0E-8 ? 0.5 : var9 / var13 / 2.0;
            double var17 = var13 < 1.0E-8 ? 0.0 : -var7 / var13 / 2.0;
            this.vertex(var1.x + var15 * var3, var1.y + var17 * var3, var1.z, var5);
            this.vertex(var2.x + var15 * var3, var2.y + var17 * var3, var2.z, var6);
            this.vertex(var2.x - var15 * var3, var2.y - var17 * var3, var2.z, var6);
            this.vertex(var1.x - var15 * var3, var1.y - var17 * var3, var1.z, var5);
         }
      }

      List<Vector3f> H() {
         this.e();
         ArrayList var1 = new ArrayList();

         for (pa.i.x var3 : this.Q) {
            for (int var4 = 0; var4 < var3.colors.length; var4++) {
               var1.add(new Vector3f(var3.points[var4 * 3], var3.points[var4 * 3 + 1], var3.points[var4 * 3 + 2]));
            }
         }

         return var1;
      }

      private void e() {
         if (this.W != null && this.q > 0) {
            this.Q.add(new pa.i.x(this.W, this.M, Arrays.copyOf(this.f, this.q * 3), Arrays.copyOf(this.m, this.q)));
         }

         this.q = 0;
      }

      void y(SubmitNodeCollector var1, PoseStack var2, int var3) {
         this.e();

         for (pa.i.x var5 : this.Q) {
            var1.order(var3++).submitCustomGeometry(var2, var5.solid ? ki.M() : ki.x(var5.blend), (var1x, var2x) -> {
               for (int var3x = 0; var3x < var5.colors.length; var3x++) {
                  var2x.addVertex(var1x, var5.points[var3x * 3], var5.points[var3x * 3 + 1], var5.points[var3x * 3 + 2]).setColor(var5.colors[var3x]);
               }
            });
         }

         this.Q.clear();
         zl.j var10 = pr.S();
         if (var10 != null) {
            for (pa.i.v var6 : this.g) {
               pr.h var7 = pr.x.get(var6.coin());
               var1.order(var3++)
                  .submitCustomGeometry(
                     var2,
                     var7.type(),
                     (var2x, var3x) -> {
                        Matrix4f var4 = new Matrix4f(var2x.pose()).mul(var6.transform());
                        float[] var5x = var10.vertices();
                        int[] var6x = var10.colors();

                        for (int var7x = 0; var7x < var6x.length; var7x++) {
                           var3x.addVertex(var4, var5x[var7x * 5], var5x[var7x * 5 + 1], var5x[var7x * 5 + 2])
                              .setUv(var5x[var7x * 5 + 3], var5x[var7x * 5 + 4])
                              .setColor(var6x[var7x]);
                        }
                     }
                  );
            }
         }

         this.g.clear();
         pa.i.k var12 = this.X;
         this.X = null;
         ml.w var13 = ml.u();
         if (var12 != null && var13 != null && ml.k(var12.look().horn(), var12.look().fire(), var12.look().core())) {
            var1.order(var3++)
               .submitCustomGeometry(var2, ml.s(), (var2x, var3x) -> ml.T(var3x, new Matrix4f(var2x.pose()).mul(var12.head()), var13, var12.scale()));
            var1.order(var3++)
               .submitCustomGeometry(
                  var2,
                  db.Q(),
                  (var1x, var2x) -> var12.fire()
                     .z(
                        (var2xx, var4, var6x, var8, var10x, var12x, var14) -> {
                           float var15 = (float)Math.cos(var12x);
                           float var16 = (float)Math.sin(var12x);
                           float var17 = (float)var8;
                           float var18 = (float)(var8 * var10x);
                           float var19 = (float)var2xx;
                           float var20 = (float)var4;
                           float var21 = (float)var6x;

                           for (int var22 = 0; var22 < 4; var22++) {
                              float var23 = var22 != 0 && var22 != 3 ? 1.0F : -1.0F;
                              float var24 = var22 < 2 ? -1.0F : 1.0F;
                              var2x.addVertex(
                                    var1x, var19 + var23 * var17 * var15 - var24 * var18 * var16, var20 + var23 * var17 * var16 + var24 * var18 * var15, var21
                                 )
                                 .setUv((var23 + 1.0F) * 0.5F, (var24 + 1.0F) * 0.5F)
                                 .setColor(var14);
                           }
                        },
                        var12.look().fire(),
                        var12.look().core(),
                        1.0
                     )
               );
         }
      }

      private record k(Matrix4f head, double scale, ChinaHatModule.z look, db fire) {
      }

      private record v(int coin, Matrix4f transform) {
      }

      private record x(pf.x blend, boolean solid, float[] points, int[] colors) {
      }
   }
}
