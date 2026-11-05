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

public final class e8 {
   private static final int X = 15728880;
   static final float n = 1.85F;
   static final float z = 0.15F;
   static final float g = 1.4F;
   static final float Q = 0.15F;
   private final rt.p M = new rt.p();
   private rn.k B;
   private String o = "";
   private static int F = -1;
   private static final jp C = new jp(0L);

   static ChinaHatModule.j P() {
      ChinaHatModule var0 = ChinaHatModule.J();
      return var0 == null ? null : var0.L();
   }

   static WingsModule.u r() {
      WingsModule var0 = WingsModule.i();
      return var0 == null ? null : var0.B();
   }

   static boolean J() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.U();
      return var0 != null && var0.g();
   }

   static fd E() {
      HeadlessKorbloxModule var0 = HeadlessKorbloxModule.U();
      return var0 == null ? null : var0.n();
   }

   static double C(ChinaHatModule.j var0, WingsModule.u var1) {
      double var2 = var0 == null ? 0.0 : var0.rise() + 0.05;
      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 0.15F : 0.15F) * var1.size());
      }

      return var2;
   }

   static double P(ChinaHatModule.j var0, WingsModule.u var1) {
      double var2 = var0 == null ? 0.0 : var0.radius() + 0.05;
      if (var1 != null) {
         var2 = Math.max(var2, (var1.dragon() ? 1.4F : 1.85F) * var1.size());
      }

      return var2;
   }

   static PlayerSkin r(UUID var0, PlayerSkin var1) {
      PlayerSkin var2 = x2.X(var0, var1);
      SkinChangerModule var3 = SkinChangerModule.b();
      if (var3 != null) {
         var2 = var3.b(var2);
      }

      CapesModule var4 = CapesModule.p();
      return var4 == null ? var2 : var4.v(var2);
   }

   static void d(AvatarRenderState var0, og.i var1) {
      float var2 = var1.walkSpeed() / 4.0F;
      var0.capeLean = Math.clamp(var2 * 300.0F, 0.0F, 150.0F);
      var0.capeLean2 = 0.0F;
      float var3 = var1.walkPosition() * 0.15F;
      float var4 = Math.min(0.1F, var2);
      var0.capeFlap = (float)Math.sin(var3 * 6.0F) * 32.0F * var4;
   }

   static Identifier x(PlayerSkin var0) {
      return var0.cape() != null && Minecraft.getInstance().options.isModelPartEnabled(PlayerModelPart.CAPE) ? var0.cape().texturePath() : null;
   }

   e8.b h(Identifier var1, ChinaHatModule.j var2, WingsModule.u var3, og.i var4, PlayerCapeModel var5) {
      return this.Q(var1, var2, var3, false, null, var4, var5);
   }

   e8.b Q(Identifier var1, ChinaHatModule.j var2, WingsModule.u var3, boolean var4, fd var5, og.i var6, PlayerCapeModel var7) {
      if (var7 == null) {
         var1 = null;
      }

      float var8 = var6.ageInTicks();
      this.M.P(var8, false, var6.walkSpeed(), false, false, Vec3.ZERO);
      rn.z var9 = null;
      if (var3 != null && var3.dragon()) {
         if (this.B == null) {
            this.B = rn.k.H();
         }

         this.M.r(rn.s(this.M.u(), 0.0F, 0.0F, 0.0F));
         var9 = rn.a(this.M.E(), this.M.u(), 0.0F, 0.0F, 0.0F);
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
      if (!var10.equals(this.o)) {
         this.o = var10;
         r4.V(
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

      int var11 = var3 == null ? 0 : WingsModule.A(var3.first(), var3.second(), var8 / 20.0);
      int var12 = var3 == null ? 0 : WingsModule.O(var3.first(), var3.second(), var8 / 20.0);
      return new e8.b(var7, (Identifier)var1, var2, var3, var11, var12, this.B, var9, var8, this.M.u(), var4, var5);
   }

   public static void d(GuiSkinRenderState var0, Matrix4fc var1, PoseStack var2, SubmitNodeCollector var3) {
      if (var0.playerModel() instanceof mn var4 && var4.s() != null) {
         int var13 = jk.v;
         if (F != var13) {
            e8.b var6 = var4.s();

            try {
               if (var6.cape() != null) {
                  var3.submitModel(var6.capeModel(), var4.K(), var2, RenderTypes.entitySolid(var6.cape()), 15728880, OverlayTexture.NO_OVERLAY, 0);
               }

               fd var7 = var6.korblox();
               if (var7 != null) {
                  if (var7.right()) {
                     fa.U(var2, var3, var4.I(), var7, 15728880, OverlayTexture.NO_OVERLAY, false);
                  }

                  if (var7.left()) {
                     fa.U(var2, var3, var4.t(), var7, 15728880, OverlayTexture.NO_OVERLAY, true);
                  }
               }

               WingsModule.u var8 = var6.wings();
               if (var8 != null && var8.dragon()) {
                  PoseStack var9 = new PoseStack();
                  var9.last().set(var2.last());
                  var4.z().translateAndRotate(var9);
                  rn.k var10 = var6.dragon();
                  rn.z var11 = var6.angles();
                  var3.order(1).submitCustomGeometry(var9, rn.X(var8.dragonColor()), (var3x, var4x) -> {
                     PoseStack var5 = new PoseStack();
                     var5.last().set(var3x);
                     var10.o(var11);
                     var10.p(var5, var4x, var8.size(), false, 15728880, var8.dragonColor());
                  });
               }

               e8.c var14 = new e8.c();
               Q(var14, var4, new Matrix4f(var1).invert().mul(var2.last().pose()), var6);
               PoseStack var15 = new PoseStack();
               var15.last().pose().set(var1);
               var14.f(var3, var15, 2);
            } catch (RuntimeException var12) {
               F = var13;
               r4.r("Menu", "Account model cosmetics submission failed; body only until resource reload", var12);
            }
         }
      }
   }

   static void Q(e8.c var0, mn var1, Matrix4f var2, e8.b var3) {
      ChinaHatModule.j var4 = var3.hat();
      if (var4 != null) {
         PoseStack var5 = new PoseStack();
         var5.last().pose().set(var2);
         var1.a().translateAndRotate(var5);
         var0.blend(fk.h.z);
         ju.h var6 = var4.horns() != null ? ju.k() : null;
         if (var6 != null) {
            float var7 = (float)var4.radius();
            Vector3f var8 = var2.transformDirection(0.0F, -1.0F, 0.0F, new Vector3f()).normalize();
            Vector3f var9 = var5.last().pose().transformPosition(0.0F, -0.5F, var6.centerZ() * var7, new Vector3f()).add(new Vector3f(var8).mul(0.24F * var7));
            C.M(var3.age() / 20.0, var9.x, var9.y, var9.z, var8.x, var8.y, var8.z, var7 * var4.horns().size(), var4.horns().heat());
            var0.l(var5.last().pose(), var4.radius(), var4.horns(), C);
         } else if (var4.accessory() != null) {
            List var11 = hw.S(var4.accessory());
            int var12 = !var11.isEmpty() && jz.u("Menu") ? 1 : 0;
            hw.V(var0, var5.last().pose(), var4.accessory(), var4.radius(), (var4.top() >>> 24) / 255.0, var12 == 0);
            if (var12 != 0) {
               for (hw.b var10 : var11) {
                  var0.L(var10.coin(), hw.t(var5.last().pose(), var10, var4.radius(), new Matrix4f()));
               }
            }
         } else if (var4.kippah()) {
            _e.u(var0, var5.last().pose(), var4.radius(), var4.height(), var4.offset(), var4.top(), var4.rim(), var4.outline());
         } else {
            dg.p(var0, var5.last().pose(), var4.radius(), var4.height(), var4.offset(), var4.top(), var4.rim(), var4.outline());
         }
      }

      if (var3.wings() != null && !var3.wings().dragon()) {
         z(var0, var1, var2, var3, var3.wings());
      }
   }

   private static void z(e8.c var0, mn var1, Matrix4f var2, e8.b var3, WingsModule.u var4) {
      PoseStack var5 = new PoseStack();
      var5.last().pose().set(var2);
      var1.z().translateAndRotate(var5);
      rt.j var6 = rt.k(0.0F, 0.0F, 0.0F, 0.0F);
      rt.b var7 = rt.c(var3.age(), var3.walking(), 0.0F, 0.0F);
      rt.i[] var8 = new rt.i[2];
      byte var9 = -1;

      for (int var10 = 0; var9 <= 1; var10++) {
         Matrix4f var11 = new Matrix4f(var5.last().pose()).mul(rt.A(var6, var3.age(), var3.walking(), var4.size(), var9, false));
         var8[var10] = rt.i(Vec3.ZERO, var11, var9, var7, var3.primary(), var3.secondary(), var4.size());
         var9 += 2;
      }

      for (rt.f var12 : rt.f.values()) {
         for (rt.i var16 : var8) {
            rt.X(var0, var16, var12);
         }
      }
   }

   record b(
      PlayerCapeModel capeModel,
      Identifier cape,
      ChinaHatModule.j hat,
      WingsModule.u wings,
      int primary,
      int secondary,
      rn.k dragon,
      rn.z angles,
      float age,
      float walking,
      boolean headless,
      fd korblox
   ) {
   }

   static final class c implements _c {
      private final List<e8.c.e> m = new ArrayList<>();
      private final List<e8.c.p> B = new ArrayList<>();
      private e8.c.l y;
      private final Vector3f J = new Vector3f();
      private fk.h d = fk.h.z;
      private fk.h v;
      private boolean f;
      private boolean j;
      private float[] N = new float[768];
      private int[] q = new int[256];
      private int r;

      @Override
      public void blend(fk.h var1) {
         this.d = var1;
      }

      @Override
      public void solid(boolean var1) {
         this.f = var1;
      }

      void L(int var1, Matrix4f var2) {
         this.B.add(new e8.c.p(var1, var2));
      }

      void l(Matrix4f var1, double var2, ChinaHatModule.e var4, jp var5) {
         this.y = new e8.c.l(new Matrix4f(var1), var2, var4, var5);
      }

      @Override
      public void modelVertex(Matrix4f var1, float var2, float var3, float var4, int var5) {
         var1.transformPosition(var2, var3, var4, this.J);
         this.vertex(this.J.x, this.J.y, this.J.z, var5);
      }

      @Override
      public void vertex(double var1, double var3, double var5, int var7) {
         if (this.v != this.d || this.j != this.f) {
            this.u();
            this.v = this.d;
            this.j = this.f;
         }

         if (this.r == this.q.length) {
            this.q = Arrays.copyOf(this.q, this.r * 2);
            this.N = Arrays.copyOf(this.N, this.r * 6);
         }

         this.N[this.r * 3] = (float)var1;
         this.N[this.r * 3 + 1] = (float)var3;
         this.N[this.r * 3 + 2] = (float)var5;
         this.q[this.r++] = var7;
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

      List<Vector3f> t() {
         this.u();
         ArrayList var1 = new ArrayList();

         for (e8.c.e var3 : this.m) {
            for (int var4 = 0; var4 < var3.colors.length; var4++) {
               var1.add(new Vector3f(var3.points[var4 * 3], var3.points[var4 * 3 + 1], var3.points[var4 * 3 + 2]));
            }
         }

         return var1;
      }

      private void u() {
         if (this.v != null && this.r > 0) {
            this.m.add(new e8.c.e(this.v, this.j, Arrays.copyOf(this.N, this.r * 3), Arrays.copyOf(this.q, this.r)));
         }

         this.r = 0;
      }

      void f(SubmitNodeCollector var1, PoseStack var2, int var3) {
         this.u();

         for (e8.c.e var5 : this.m) {
            var1.order(var3++).submitCustomGeometry(var2, var5.solid ? r6.a() : r6.U(var5.blend), (var1x, var2x) -> {
               for (int var3x = 0; var3x < var5.colors.length; var3x++) {
                  var2x.addVertex(var1x, var5.points[var3x * 3], var5.points[var3x * 3 + 1], var5.points[var3x * 3 + 2]).setColor(var5.colors[var3x]);
               }
            });
         }

         this.m.clear();
         u1.x var10 = jz.b();
         if (var10 != null) {
            for (e8.c.p var6 : this.B) {
               jz.d var7 = jz.u.get(var6.coin());
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

         this.B.clear();
         e8.c.l var12 = this.y;
         this.y = null;
         ju.h var13 = ju.k();
         if (var12 != null && var13 != null && ju.F(var12.look().horn(), var12.look().fire(), var12.look().core())) {
            var1.order(var3++)
               .submitCustomGeometry(var2, ju.G(), (var2x, var3x) -> ju.H(var3x, new Matrix4f(var2x.pose()).mul(var12.head()), var13, var12.scale()));
            var1.order(var3++)
               .submitCustomGeometry(
                  var2,
                  jp.p(),
                  (var1x, var2x) -> var12.fire()
                     .l(
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

      private record e(fk.h blend, boolean solid, float[] points, int[] colors) {
      }

      private record l(Matrix4f head, double scale, ChinaHatModule.e look, jp fire) {
      }

      private record p(int coin, Matrix4f transform) {
      }
   }
}
