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

public final class m {
   private static final int W = 15728880;
   static final float F = 1.85F;
   static final float K = 0.15F;
   static final float g = 1.4F;
   static final float m = 0.15F;
   private final n4.m B = new n4.m();
   private n9.j x;
   private String q = "";
   private static int e = -1;
   private static final dj d = new dj(0L);

   static List<ChinaHatModule.y> Z() {
      ChinaHatModule var0 = ChinaHatModule.E();
      return var0 == null ? List.of() : var0.n();
   }

   static WingsModule.o O() {
      WingsModule var0 = WingsModule.i();
      return var0 == null ? null : var0.T();
   }

   static boolean T() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.W();
      return var0 != null && var0.i() || Z().stream().anyMatch(ChinaHatModule.y::dominus);
   }

   static a1 M() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.W();
      return var0 == null ? null : var0.l();
   }

   static double V(List<ChinaHatModule.y> var0, WingsModule.o var1) {
      double var2 = 0.0;

      for (ChinaHatModule.y var5 : var0) {
         var2 = Math.max(var2, var5.rise() + 0.05);
      }

      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 0.15F : 0.15F) * var1.size());
      }

      return var2;
   }

   static double P(List<ChinaHatModule.y> var0, WingsModule.o var1) {
      double var2 = 0.0;

      for (ChinaHatModule.y var5 : var0) {
         var2 = Math.max(var2, var5.radius() + 0.05);
      }

      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 1.4F : 1.85F) * var1.size());
      }

      return var2;
   }

   static PlayerSkin l(UUID var0, PlayerSkin var1) {
      PlayerSkin var2 = p3.n(var0, var1);
      SkinChangerModule var3 = SkinChangerModule.h();
      if (var3 != null) {
         var2 = var3.b(var2);
      }

      CapesModule var4 = CapesModule.E();
      return var4 == null ? var2 : var4.l(var2);
   }

   static void I(AvatarRenderState var0, o5.j var1) {
      float var2 = var1.walkSpeed() / 4.0F;
      var0.capeLean = Math.clamp(var2 * 300.0F, 0.0F, 150.0F);
      var0.capeLean2 = 0.0F;
      float var3 = var1.walkPosition() * 0.15F;
      float var4 = Math.min(0.1F, var2);
      var0.capeFlap = (float)Math.sin(var3 * 6.0F) * 32.0F * var4;
   }

   static Identifier U(PlayerSkin var0) {
      return var0.cape() != null && Minecraft.getInstance().options.isModelPartEnabled(PlayerModelPart.CAPE) ? var0.cape().texturePath() : null;
   }

   m.a u(Identifier var1, List<ChinaHatModule.y> var2, WingsModule.o var3, o5.j var4, PlayerCapeModel var5) {
      return this.r(var1, var2, var3, false, null, var4, var5);
   }

   m.a r(Identifier var1, List<ChinaHatModule.y> var2, WingsModule.o var3, boolean var4, a1 var5, o5.j var6, PlayerCapeModel var7) {
      if (var7 == null) {
         var1 = null;
      }

      float var8 = var6.ageInTicks();
      this.B.H(var8, false, var6.walkSpeed(), false, false, Vec3.ZERO);
      n9.d var9 = null;
      if (var3 != null && var3.dragon()) {
         if (this.x == null) {
            this.x = n9.j.C();
         }

         this.B.y(n9.L(this.B.N(), 0.0F, 0.0F, 0.0F));
         var9 = n9.z(this.B.m(), this.B.N(), 0.0F, 0.0F, 0.0F);
      }

      String var10 = var2.isEmpty() ? "none" : String.join("+", var2.stream().map(ChinaHatModule.y::label).toList());
      String var11 = (var1 == null ? "-" : var1)
         + "|"
         + var10
         + "|"
         + (var3 == null ? "-" : (var3.dragon() ? "dragon" : "luminous"))
         + "|"
         + var4
         + "|"
         + (var5 == null ? "-" : var5.legs());
      if (!var11.equals(this.q)) {
         this.q = var11;
         _q.z(
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

      int var12 = var3 == null ? 0 : WingsModule.V(var3.first(), var3.second(), var8 / 20.0);
      int var13 = var3 == null ? 0 : WingsModule.D(var3.first(), var3.second(), var8 / 20.0);
      return new m.a(var7, (Identifier)var1, var2, var3, var12, var13, this.x, var9, var8, this.B.N(), var4, var5);
   }

   public static void e(GuiSkinRenderState var0, Matrix4fc var1, PoseStack var2, SubmitNodeCollector var3) {
      if (var0.playerModel() instanceof fw var4 && var4.q() != null) {
         int var13 = nl.q;
         if (e != var13) {
            m.a var6 = var4.q();

            try {
               if (var6.cape() != null) {
                  var3.submitModel(var6.capeModel(), var4.a(), var2, RenderTypes.entitySolid(var6.cape()), 15728880, OverlayTexture.NO_OVERLAY, 0);
               }

               a1 var7 = var6.korblox();
               if (var7 != null) {
                  if (var7.right()) {
                     f3.t(var2, var3, var4.b(), var7, 15728880, OverlayTexture.NO_OVERLAY, false);
                  }

                  if (var7.left()) {
                     f3.t(var2, var3, var4.g(), var7, 15728880, OverlayTexture.NO_OVERLAY, true);
                  }
               }

               WingsModule.o var8 = var6.wings();
               if (var8 != null && var8.dragon()) {
                  PoseStack var9 = new PoseStack();
                  var9.last().set(var2.last());
                  var4.G().translateAndRotate(var9);
                  n9.j var10 = var6.dragon();
                  n9.d var11 = var6.angles();
                  var3.order(1).submitCustomGeometry(var9, n9.M(var8.dragonColor()), (var3x, var4x) -> {
                     PoseStack var5 = new PoseStack();
                     var5.last().set(var3x);
                     var10.Y(var11);
                     var10.d(var5, var4x, var8.size(), false, 15728880, var8.dragonColor());
                  });
               }

               m.q var14 = new m.q();
               d(var14, var4, new Matrix4f(var1).invert().mul(var2.last().pose()), var6);
               PoseStack var15 = new PoseStack();
               var15.last().pose().set(var1);
               var14.c(var3, var15, 2);
            } catch (RuntimeException var12) {
               e = var13;
               _q.o("Menu", "Account model cosmetics submission failed; body only until resource reload", var12);
            }
         }
      }
   }

   static void d(m.q var0, fw var1, Matrix4f var2, m.a var3) {
      if (!var3.hats().isEmpty()) {
         PoseStack var4 = new PoseStack();
         var4.last().pose().set(var2);
         var1.E().translateAndRotate(var4);
         var0.blend(_0.v.z);

         for (ChinaHatModule.y var6 : var3.hats()) {
            if (var6.dominus()) {
               var0.S(var4.last().pose(), var6.radius(), var6.top());
            } else if (var6.horns() != null) {
               np.u var11 = np.G();
               if (var11 != null) {
                  float var12 = (float)var6.radius();
                  Vector3f var13 = var2.transformDirection(0.0F, -1.0F, 0.0F, new Vector3f()).normalize();
                  Vector3f var14 = var4.last()
                     .pose()
                     .transformPosition(0.0F, -0.5F, var11.centerZ() * var12, new Vector3f())
                     .add(new Vector3f(var13).mul(0.24F * var12));
                  d.m(var3.age() / 20.0, var14.x, var14.y, var14.z, var13.x, var13.y, var13.z, var12 * var6.horns().size(), var6.horns().heat());
                  var0.J(var4.last().pose(), var6.radius(), var6.horns(), d);
               }
            } else if (var6.accessory() == null) {
               if (var6.kippah()) {
                  bg.j(var0, var4.last().pose(), var6.radius(), var6.height(), var6.offset(), var6.top(), var6.rim(), var6.outline(), 1.0);
               } else {
                  q8.n(var0, var4.last().pose(), var6.radius(), var6.height(), var6.offset(), var6.top(), var6.rim(), var6.outline());
               }
            } else {
               List var7 = sb.E(var6.accessory());
               int var8 = !var7.isEmpty() && ho.F("Menu") ? 1 : 0;
               sb.l(var0, var4.last().pose(), var6.accessory(), var6.radius(), (var6.top() >>> 24) / 255.0, (boolean)var8, 1.0, var3.age() / 20.0);
               if (var8 != 0) {
                  for (sb.r var10 : var7) {
                     var0.E(var10.coin(), sb.v(var4.last().pose(), var10, var6.radius(), new Matrix4f()));
                  }
               }
            }
         }
      }

      if (var3.wings() != null && !var3.wings().dragon()) {
         L(var0, var1, var2, var3, var3.wings());
      }
   }

   private static void L(m.q var0, fw var1, Matrix4f var2, m.a var3, WingsModule.o var4) {
      PoseStack var5 = new PoseStack();
      var5.last().pose().set(var2);
      var1.G().translateAndRotate(var5);
      n4.i var6 = n4.l(0.0F, 0.0F, 0.0F, 0.0F);
      n4.z var7 = n4.y(var3.age(), var3.walking(), 0.0F, 0.0F);
      n4.b[] var8 = new n4.b[2];
      byte var9 = -1;

      for (int var10 = 0; var9 <= 1; var10++) {
         Matrix4f var11 = new Matrix4f(var5.last().pose()).mul(n4.b(var6, var3.age(), var3.walking(), var4.size(), var9, false));
         var8[var10] = n4.f(Vec3.ZERO, var11, var9, var7, var3.primary(), var3.secondary(), var4.size());
         var9 += 2;
      }

      for (n4.t var12 : n4.t.values()) {
         for (n4.b var16 : var8) {
            n4.N(var0, var16, var12);
         }
      }
   }

   record a(
      PlayerCapeModel capeModel,
      Identifier cape,
      List<ChinaHatModule.y> hats,
      WingsModule.o wings,
      int primary,
      int secondary,
      n9.j dragon,
      n9.d angles,
      float age,
      float walking,
      boolean headless,
      a1 korblox
   ) {
   }

   static final class q implements ia {
      private final List<m.q.c> S = new ArrayList<>();
      private final List<m.q.d> Q = new ArrayList<>();
      private m.q.n u;
      private m.q.g I;
      private final Vector3f r = new Vector3f();
      private _0.v b = _0.v.z;
      private _0.v c;
      private boolean K;
      private boolean x;
      private float[] Y = new float[768];
      private int[] T = new int[256];
      private int q;

      @Override
      public void blend(_0.v var1) {
         this.b = var1;
      }

      @Override
      public void solid(boolean var1) {
         this.K = var1;
      }

      @Override
      public Vector3f viewer(Matrix4f var1, Vector3f var2) {
         new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, 1000.0F));
         if (var2.z > 0.0F) {
            new Matrix4f(var1).invert().transformPosition(var2.set(0.0F, 0.0F, -1000.0F));
         }

         return var2;
      }

      void E(int var1, Matrix4f var2) {
         this.Q.add(new m.q.d(var1, var2));
      }

      void J(Matrix4f var1, double var2, ChinaHatModule.q var4, dj var5) {
         this.u = new m.q.n(new Matrix4f(var1), var2, var4, var5);
      }

      void S(Matrix4f var1, double var2, int var4) {
         this.I = new m.q.g(new Matrix4f(var1), var2, var4);
      }

      @Override
      public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
         var1.transformPosition(var2, var3, var4, this.r);
         this.vertex(this.r.x, this.r.y, this.r.z, var5);
      }

      @Override
      public void vertex(double var1, double var3, double var5, int var7) {
         if (this.c != this.b || this.x != this.K) {
            this.W();
            this.c = this.b;
            this.x = this.K;
         }

         if (this.q == this.T.length) {
            this.T = Arrays.copyOf(this.T, this.q * 2);
            this.Y = Arrays.copyOf(this.Y, this.q * 6);
         }

         this.Y[this.q * 3] = (float)var1;
         this.Y[this.q * 3 + 1] = (float)var3;
         this.Y[this.q * 3 + 2] = (float)var5;
         this.T[this.q++] = var7;
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

      List<Vector3f> h() {
         this.W();
         ArrayList var1 = new ArrayList();

         for (m.q.c var3 : this.S) {
            for (int var4 = 0; var4 < var3.colors.length; var4++) {
               var1.add(new Vector3f(var3.points[var4 * 3], var3.points[var4 * 3 + 1], var3.points[var4 * 3 + 2]));
            }
         }

         return var1;
      }

      private void W() {
         if (this.c != null && this.q > 0) {
            this.S.add(new m.q.c(this.c, this.x, Arrays.copyOf(this.Y, this.q * 3), Arrays.copyOf(this.T, this.q)));
         }

         this.q = 0;
      }

      void c(SubmitNodeCollector var1, PoseStack var2, int var3) {
         this.W();
         m.q.n var4 = this.u;
         this.u = null;
         np.u var5 = np.G();
         int var6 = var4 != null && var5 != null && np.I(var4.look().horn(), var4.look().fire(), var4.look().core()) ? 1 : 0;
         if (var6 != 0) {
            var1.order(var3++)
               .submitCustomGeometry(var2, np.F(), (var2x, var3x) -> np.v(var3x, new Matrix4f(var2x.pose()).mul(var4.head()), var5, var4.scale()));
         }

         m.q.g var7 = this.I;
         this.I = null;
         np.u var8 = hu.j();
         if (var7 != null && var8 != null) {
            var1.order(var3++)
               .submitCustomGeometry(var2, hu.n(), (var2x, var3x) -> hu.E(var3x, new Matrix4f(var2x.pose()).mul(var7.head()), var8, var7.scale(), var7.tint()));
         }

         for (boolean var12 : new boolean[]{true, false}) {
            for (m.q.c var14 : this.S) {
               if (var14.solid == var12) {
                  var1.order(var3++).submitCustomGeometry(var2, var14.solid ? fz.H() : fz.j(var14.blend), (var1x, var2x) -> {
                     for (int var3x = 0; var3x < var14.colors.length; var3x++) {
                        var2x.addVertex(var1x, var14.points[var3x * 3], var14.points[var3x * 3 + 1], var14.points[var3x * 3 + 2]).setColor(var14.colors[var3x]);
                     }
                  });
               }
            }

            if (var12) {
               ir.j var18 = ho.P();
               if (var18 != null) {
                  for (m.q.d var15 : this.Q) {
                     ho.z var16 = ho.X.get(var15.coin());
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

         this.S.clear();
         this.Q.clear();
         if (var6 != 0) {
            var1.order(var3++)
               .submitCustomGeometry(
                  var2,
                  dj.H(),
                  (var1x, var2x) -> var4.fire()
                     .m(
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

      private record c(_0.v blend, boolean solid, float[] points, int[] colors) {
      }

      private record d(int coin, Matrix4f transform) {
      }

      private record g(Matrix4f head, double scale, int tint) {
      }

      private record n(Matrix4f head, double scale, ChinaHatModule.q look, dj fire) {
      }
   }
}
