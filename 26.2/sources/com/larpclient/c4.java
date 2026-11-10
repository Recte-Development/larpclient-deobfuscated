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

public final class c4 {
   private static final int y = 15728880;
   static final float K = 1.85F;
   static final float R = 0.15F;
   static final float q = 1.4F;
   static final float r = 0.15F;
   private final l4.s B = new l4.s();
   private cw.d u;
   private String F = "";
   private static int A = -1;
   private static final ky D = new ky(0L);

   static List<ChinaHatModule.k> j() {
      ChinaHatModule var0 = ChinaHatModule.N();
      return var0 == null ? List.of() : var0.v();
   }

   static WingsModule.h W() {
      WingsModule var0 = WingsModule.D();
      return var0 == null ? null : var0.x();
   }

   static boolean a() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.I();
      return var0 != null && var0.F() || j().stream().anyMatch(ChinaHatModule.k::dominus);
   }

   static l8 V() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.I();
      return var0 == null ? null : var0.O();
   }

   static double L(List<ChinaHatModule.k> var0, WingsModule.h var1) {
      double var2 = 0.0;

      for (ChinaHatModule.k var5 : var0) {
         var2 = Math.max(var2, var5.rise() + 0.05);
      }

      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 0.15F : 0.15F) * var1.size());
      }

      return var2;
   }

   static double O(List<ChinaHatModule.k> var0, WingsModule.h var1) {
      double var2 = 0.0;

      for (ChinaHatModule.k var5 : var0) {
         var2 = Math.max(var2, var5.radius() + 0.05);
      }

      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 1.4F : 1.85F) * var1.size());
      }

      return var2;
   }

   static PlayerSkin V(UUID var0, PlayerSkin var1) {
      PlayerSkin var2 = ci.X(var0, var1);
      SkinChangerModule var3 = SkinChangerModule.X();
      if (var3 != null) {
         var2 = var3.L(var2);
      }

      CapesModule var4 = CapesModule.K();
      return var4 == null ? var2 : var4.b(var2);
   }

   static void V(AvatarRenderState var0, tx.u var1) {
      float var2 = var1.walkSpeed() / 4.0F;
      var0.capeLean = Math.clamp(var2 * 300.0F, 0.0F, 150.0F);
      var0.capeLean2 = 0.0F;
      float var3 = var1.walkPosition() * 0.15F;
      float var4 = Math.min(0.1F, var2);
      var0.capeFlap = (float)Math.sin(var3 * 6.0F) * 32.0F * var4;
   }

   static Identifier W(PlayerSkin var0) {
      return var0.cape() != null && Minecraft.getInstance().options.isModelPartEnabled(PlayerModelPart.CAPE) ? var0.cape().texturePath() : null;
   }

   c4.w m(Identifier var1, List<ChinaHatModule.k> var2, WingsModule.h var3, tx.u var4, PlayerCapeModel var5) {
      return this.E(var1, var2, var3, false, null, var4, var5);
   }

   c4.w E(Identifier var1, List<ChinaHatModule.k> var2, WingsModule.h var3, boolean var4, l8 var5, tx.u var6, PlayerCapeModel var7) {
      if (var7 == null) {
         var1 = null;
      }

      float var8 = var6.ageInTicks();
      this.B.s(var8, false, var6.walkSpeed(), false, false, Vec3.ZERO);
      cw.c var9 = null;
      if (var3 != null && var3.dragon()) {
         if (this.u == null) {
            this.u = cw.d.o();
         }

         this.B.M(cw.B(this.B.b(), 0.0F, 0.0F, 0.0F));
         var9 = cw.G(this.B.R(), this.B.b(), 0.0F, 0.0F, 0.0F);
      }

      String var10 = var2.isEmpty() ? "none" : String.join("+", var2.stream().map(ChinaHatModule.k::label).toList());
      String var11 = (var1 == null ? "-" : var1)
         + "|"
         + var10
         + "|"
         + (var3 == null ? "-" : (var3.dragon() ? "dragon" : "luminous"))
         + "|"
         + var4
         + "|"
         + (var5 == null ? "-" : var5.legs());
      if (!var11.equals(this.F)) {
         this.F = var11;
         h.A(
            "Menu",
            "Account model cosmetics: cape={}, hats={}, wings={}, headless={}, korblox={}",
            var1 == null ? "none" : var1,
            var10,
            var3 == null ? "none" : (var3.dragon() ? "Ender Dragon" : "Luminous"),
            var4,
            var5 == null ? "none" : (var5.right() && var5.left() ? "both legs" : (var5.right() ? "right leg" : "left leg"))
         );
      }

      if (var1 == null && var2.isEmpty() && var3 == null && !var4 && var5 == null) {
         return null;
      }

      int var12 = var3 == null ? 0 : WingsModule.g(var3.first(), var3.second(), var8 / 20.0);
      int var13 = var3 == null ? 0 : WingsModule.Q(var3.first(), var3.second(), var8 / 20.0);
      return new c4.w(var7, (Identifier)var1, var2, var3, var12, var13, this.u, var9, var8, this.B.b(), var4, var5);
   }

   public static void r(GuiSkinRenderState var0, Matrix4fc var1, PoseStack var2, SubmitNodeCollector var3) {
      if (var0.playerModel() instanceof g3 var4 && var4.b() != null) {
         int var13 = wa.s;
         if (A != var13) {
            c4.w var6 = var4.b();

            try {
               if (var6.cape() != null) {
                  var3.submitModel(var6.capeModel(), var4.U(), var2, RenderTypes.entitySolid(var6.cape()), 15728880, OverlayTexture.NO_OVERLAY, 0, null);
               }

               l8 var7 = var6.korblox();
               if (var7 != null) {
                  if (var7.right()) {
                     wg.j(var2, var3, var4.d(), var7, 15728880, OverlayTexture.NO_OVERLAY, false);
                  }

                  if (var7.left()) {
                     wg.j(var2, var3, var4.h(), var7, 15728880, OverlayTexture.NO_OVERLAY, true);
                  }
               }

               WingsModule.h var8 = var6.wings();
               if (var8 != null && var8.dragon()) {
                  PoseStack var9 = new PoseStack();
                  var9.last().set(var2.last());
                  var4.J().translateAndRotate(var9);
                  cw.d var10 = var6.dragon();
                  cw.c var11 = var6.angles();
                  var3.order(1).submitCustomGeometry(var9, cw.L(var8.dragonColor()), (var3x, var4x) -> {
                     PoseStack var5 = new PoseStack();
                     var5.last().set(var3x);
                     var10.F(var11);
                     var10.p(var5, var4x, var8.size(), false, 15728880, var8.dragonColor());
                  });
               }

               c4.x var14 = new c4.x();
               R(var14, var4, new Matrix4f(var1).invert().mul(var2.last().pose()), var6);
               PoseStack var15 = new PoseStack();
               var15.last().pose().set(var1);
               var14.a(var3, var15, 2);
            } catch (RuntimeException var12) {
               A = var13;
               h.m("Menu", "Account model cosmetics submission failed; body only until resource reload", var12);
            }
         }
      }
   }

   static void R(c4.x var0, g3 var1, Matrix4f var2, c4.w var3) {
      if (!var3.hats().isEmpty()) {
         PoseStack var4 = new PoseStack();
         var4.last().pose().set(var2);
         var1.i().translateAndRotate(var4);
         var0.blend(gl.c.K);

         for (ChinaHatModule.k var6 : var3.hats()) {
            if (var6.dominus()) {
               var0.D(var4.last().pose(), var6.radius(), var6.top());
            } else if (var6.horns() != null) {
               sf.a var11 = sf.m();
               if (var11 != null) {
                  float var12 = (float)var6.radius();
                  Vector3f var13 = var2.transformDirection(0.0F, -1.0F, 0.0F, new Vector3f()).normalize();
                  Vector3f var14 = var4.last()
                     .pose()
                     .transformPosition(0.0F, -0.5F, var11.centerZ() * var12, new Vector3f())
                     .add(new Vector3f(var13).mul(0.24F * var12));
                  D.H(var3.age() / 20.0, var14.x, var14.y, var14.z, var13.x, var13.y, var13.z, var12 * var6.horns().size(), var6.horns().heat());
                  var0.i(var4.last().pose(), var6.radius(), var6.horns(), D);
               }
            } else if (var6.accessory() == null) {
               if (var6.kippah()) {
                  cs.n(var0, var4.last().pose(), var6.radius(), var6.height(), var6.offset(), var6.top(), var6.rim(), var6.outline(), 1.0);
               } else {
                  tf.T(var0, var4.last().pose(), var6.radius(), var6.height(), var6.offset(), var6.top(), var6.rim(), var6.outline());
               }
            } else {
               List var7 = gm.b(var6.accessory());
               int var8 = !var7.isEmpty() && pv.T("Menu") ? 1 : 0;
               gm.P(var0, var4.last().pose(), var6.accessory(), var6.radius(), (var6.top() >>> 24) / 255.0, (boolean)var8, 1.0, var3.age() / 20.0);
               if (var8 != 0) {
                  for (gm.n var10 : var7) {
                     var0.Y(var10.coin(), gm.M(var4.last().pose(), var10, var6.radius(), new Matrix4f()));
                  }
               }
            }
         }
      }

      if (var3.wings() != null && !var3.wings().dragon()) {
         u(var0, var1, var2, var3, var3.wings());
      }
   }

   private static void u(c4.x var0, g3 var1, Matrix4f var2, c4.w var3, WingsModule.h var4) {
      PoseStack var5 = new PoseStack();
      var5.last().pose().set(var2);
      var1.J().translateAndRotate(var5);
      l4.u var6 = l4.m(0.0F, 0.0F, 0.0F, 0.0F);
      l4.o var7 = l4.I(var3.age(), var3.walking(), 0.0F, 0.0F);
      l4.q[] var8 = new l4.q[2];
      byte var9 = -1;

      for (int var10 = 0; var9 <= 1; var10++) {
         Matrix4f var11 = new Matrix4f(var5.last().pose()).mul(l4.n(var6, var3.age(), var3.walking(), var4.size(), var9, false));
         var8[var10] = l4.Q(Vec3.ZERO, var11, var9, var7, var3.primary(), var3.secondary(), var4.size());
         var9 += 2;
      }

      for (l4.w var12 : l4.w.values()) {
         for (l4.q var16 : var8) {
            l4.L(var0, var16, var12);
         }
      }
   }

   record w(
      PlayerCapeModel capeModel,
      Identifier cape,
      List<ChinaHatModule.k> hats,
      WingsModule.h wings,
      int primary,
      int secondary,
      cw.d dragon,
      cw.c angles,
      float age,
      float walking,
      boolean headless,
      l8 korblox
   ) {
   }

   static final class x implements com.larpclient.d {
      private final List<c4.x.g> B = new ArrayList<>();
      private final List<c4.x.p> S = new ArrayList<>();
      private c4.x.d x;
      private c4.x.l E;
      private final Vector3f Z = new Vector3f();
      private gl.c P = gl.c.K;
      private gl.c W;
      private boolean k;
      private boolean g;
      private float[] v = new float[768];
      private int[] t = new int[256];
      private int o;

      @Override
      public void blend(gl.c var1) {
         this.P = var1;
      }

      @Override
      public void solid(boolean var1) {
         this.k = var1;
      }

      @Override
      public Vector3f viewer(Matrix4f var1, Vector3f var2) {
         new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, 1000.0F));
         if (var2.z > 0.0F) {
            new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, -1000.0F));
         }

         return var2;
      }

      void Y(int var1, Matrix4f var2) {
         this.S.add(new c4.x.p(var1, var2));
      }

      void i(Matrix4f var1, double var2, ChinaHatModule.i var4, ky var5) {
         this.x = new c4.x.d(new Matrix4f(var1), var2, var4, var5);
      }

      void D(Matrix4f var1, double var2, int var4) {
         this.E = new c4.x.l(new Matrix4f(var1), var2, var4);
      }

      @Override
      public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
         var1.transformPosition(var2, var3, var4, this.Z);
         this.vertex(this.Z.x, this.Z.y, this.Z.z, var5);
      }

      @Override
      public void vertex(double var1, double var3, double var5, int var7) {
         if (this.W != this.P || this.g != this.k) {
            this.H();
            this.W = this.P;
            this.g = this.k;
         }

         if (this.o == this.t.length) {
            this.t = Arrays.copyOf(this.t, this.o * 2);
            this.v = Arrays.copyOf(this.v, this.o * 6);
         }

         this.v[this.o * 3] = (float)var1;
         this.v[this.o * 3 + 1] = (float)var3;
         this.v[this.o * 3 + 2] = (float)var5;
         this.t[this.o++] = var7;
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

      List<Vector3f> x() {
         this.H();
         ArrayList var1 = new ArrayList();

         for (c4.x.g var3 : this.B) {
            for (int var4 = 0; var4 < var3.colors.length; var4++) {
               var1.add(new Vector3f(var3.points[var4 * 3], var3.points[var4 * 3 + 1], var3.points[var4 * 3 + 2]));
            }
         }

         return var1;
      }

      private void H() {
         if (this.W != null && this.o > 0) {
            this.B.add(new c4.x.g(this.W, this.g, Arrays.copyOf(this.v, this.o * 3), Arrays.copyOf(this.t, this.o)));
         }

         this.o = 0;
      }

      void a(SubmitNodeCollector var1, PoseStack var2, int var3) {
         this.H();
         c4.x.d var4 = this.x;
         this.x = null;
         sf.a var5 = sf.m();
         int var6 = var4 != null && var5 != null && sf.N(var4.look().horn(), var4.look().fire(), var4.look().core()) ? 1 : 0;
         if (var6 != 0) {
            var1.order(var3++)
               .submitCustomGeometry(var2, sf.K(), (var2x, var3x) -> sf.Z(var3x, new Matrix4f(var2x.pose()).mul(var4.head()), var5, var4.scale()));
         }

         c4.x.l var7 = this.E;
         this.E = null;
         sf.a var8 = me.v();
         if (var7 != null && var8 != null) {
            var1.order(var3++)
               .submitCustomGeometry(var2, me.R(), (var2x, var3x) -> me.a(var3x, new Matrix4f(var2x.pose()).mul(var7.head()), var8, var7.scale(), var7.tint()));
         }

         for (boolean var12 : new boolean[]{true, false}) {
            for (c4.x.g var14 : this.B) {
               if (var14.solid == var12) {
                  var1.order(var3++).submitCustomGeometry(var2, var14.solid ? kj.R() : kj.Y(var14.blend), (var1x, var2x) -> {
                     for (int var3x = 0; var3x < var14.colors.length; var3x++) {
                        var2x.addVertex(var1x, var14.points[var3x * 3], var14.points[var3x * 3 + 1], var14.points[var3x * 3 + 2]).setColor(var14.colors[var3x]);
                     }
                  });
               }
            }

            if (var12) {
               co.g var18 = pv.v();
               if (var18 != null) {
                  for (c4.x.p var15 : this.S) {
                     pv.p var16 = pv.o.get(var15.coin());
                     var1.order(var3++)
                        .submitCustomGeometry(
                           var2,
                           var16.type(),
                           (var2x, var3x) -> {
                              Matrix4f var4x = new Matrix4f(var2x.pose()).mul(var15.transform());
                              float[] var5x = var18.vertices();
                              int[] var6x = var18.colors();

                              for (int var7x = 0; var7x < var6x.length; var7x++) {
                                 var3x.addVertex(var4x, var5x[var7x * 5], var5x[var7x * 5 + 1], var5x[var7x * 5 + 2])
                                    .setUv(var5x[var7x * 5 + 3], var5x[var7x * 5 + 4])
                                    .setColor(var6x[var7x]);
                              }
                           }
                        );
                  }
               }
            }
         }

         this.B.clear();
         this.S.clear();
         if (var6 != 0) {
            var1.order(var3++)
               .submitCustomGeometry(
                  var2,
                  ky.H(),
                  (var1x, var2x) -> var4.fire()
                     .i(
                        (var2xx, var4x, var6x, var8x, var10, var12x, var14x) -> {
                           float var15x = (float)Math.cos(var12x);
                           float var16x = (float)Math.sin(var12x);
                           float var17 = (float)var8x;
                           float var18x = (float)(var8x * var10);
                           float var19 = (float)var2xx;
                           float var20 = (float)var4x;
                           float var21 = (float)var6x;

                           for (int var22 = 0; var22 < 4; var22++) {
                              float var23 = var22 != 0 && var22 != 3 ? 1.0F : -1.0F;
                              float var24 = var22 < 2 ? -1.0F : 1.0F;
                              var2x.addVertex(
                                    var1x,
                                    var19 + var23 * var17 * var15x - var24 * var18x * var16x,
                                    var20 + var23 * var17 * var16x + var24 * var18x * var15x,
                                    var21
                                 )
                                 .setUv((var23 + 1.0F) * 0.5F, (var24 + 1.0F) * 0.5F)
                                 .setColor(var14x);
                           }
                        },
                        var4.look().fire(),
                        var4.look().core(),
                        1.0
                     )
               );
         }
      }

      private record d(Matrix4f head, double scale, ChinaHatModule.i look, ky fire) {
      }

      private record g(gl.c blend, boolean solid, float[] points, int[] colors) {
      }

      private record l(Matrix4f head, double scale, int tint) {
      }

      private record p(int coin, Matrix4f transform) {
      }
   }
}
