package com.larpclient;

import com.larpclient.module.ChinaHatModule;
import com.larpclient.module.WingsModule;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.player.PlayerCapeModel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

public final class mv {
   static final double e = 1.45;
   private static final double a = 4.5;
   private static final double w = 0.09;
   private final zt y;
   private final pa r = new pa();
   private w_ T;
   private w_ f;
   private PlayerCapeModel Q;
   private int V = -1;
   private int J = -1;
   private zt.e R;
   private double W;
   private zt.l K;
   private zt.q v;
   private w_ c;
   private Identifier B;
   private double n;
   private long o;

   public mv(zt var1) {
      this.y = var1;
   }

   public static zt.q G(o4 var0) {
      return zt.B(var0, pa.Y(pa.X(), pa.X()));
   }

   public void V(GuiGraphicsExtractor var1, o4 var2, oh var3, cn var4, int var5, int var6, boolean var7, boolean var8) {
      Minecraft var9 = Minecraft.getInstance();
      this.K = null;
      if (this.y.h() && hy.Y(var9.getWindow().handle(), 0) != 1) {
         this.y.Y();
      }

      ChinaHatModule.d var10 = pa.X();
      WingsModule.n var11 = pa.X();
      zt.q var12 = zt.B(var2, pa.Y(var10, var11));
      int var13 = var12.visible() && var9.gui.overlay() == null && var9.isWindowActive() ? 1 : 0;
      long var14 = System.nanoTime();
      zt.l var16 = this.y.U(var14, (boolean)var13, var7, var8, var3.g(), var12, var2.width(), var5, var6);
      this.W = var16.nameOpacity();
      this.V(var16);
      if (var12.visible() && var16.stage() != zt.e.g) {
         zt.t var17 = var16.toy();
         if (var17 != null) {
            this.p(var9, var17, var14);
         }

         p5.w var18 = var12.model();
         int var19 = Math.round(var12.centerX() + (float)var16.xOffset());
         double var20 = pa.o(var10, var11);
         int var22 = Math.max(var12.renderWidth(), (int)Math.ceil(2.0 * var20 * var12.scale()));
         int var23 = var19 - var22 / 2;
         int var24 = zt.V(var12, pa.Y(var10, var11));
         if (var17 == null) {
            var1.enableScissor(var2.navigation(0).x() + var2.menuWidth() + 8, var24, var2.width(), var18.y() + var18.height() + 8);
         }

         try {
            if (var17 == null) {
               Y(var1, var19, var12.floorY(), Math.min(var18.width() * 0.3F, var12.scale() * 0.62F), 1.0);
            }

            if (!this.h(var9)) {
               if (var17 == null) {
                  int var31 = Math.min(48, var18.width() / 2);
                  var4.W(var1, var19 - var31 / 2, var18.y() + var18.height() / 3, var31);
               }

               return;
            }

            PlayerSkin var25 = pa.g(var9.getGameProfile().id(), var4.x());
            w_ var26 = var25.model() == PlayerModelType.SLIM ? this.f : this.T;
            var26.x(var16.pose(), var17 == null ? null : var17.limbs());
            u3 var27 = pa.X();
            if (var27 != null && !uf.J()) {
               var27 = null;
            }

            var26.r(this.r.a(pa.h(var25), var10, var11, pa.d(), var27, var16.pose(), this.Q));
            if (var17 == null) {
               var26.h(0.0F, 0.0F, 0.0F);
               var1.skin(
                  var26,
                  var25.body().texturePath(),
                  var12.scale(),
                  0.0F,
                  var16.pose().bodyYaw(),
                  -1.0625F,
                  var23,
                  var24,
                  var23 + var22,
                  var18.y() + var18.height()
               );
               return;
            }

            this.K = var16;
            this.v = var12;
            this.c = var26;
            this.B = var25.body().texturePath();
            this.n = Math.max(1.45, Math.max(0.85 + pa.Y(var10, var11) + 0.1, Math.hypot(var20, 0.6) + 0.05));
         } finally {
            if (var17 == null) {
               var1.disableScissor();
            }
         }
      }
   }

   public void K(GuiGraphicsExtractor var1) {
      if (this.K != null) {
         zt.t var2 = this.K.toy();
         zt.q var3 = this.v;
         float var4 = var3.scale();
         var1.nextStratum();
         double var5 = Math.abs(Math.sin(var2.roll()));
         double var7 = 1.0 / (1.0 + var2.lift() * 0.9);
         Y(
            var1,
            (float)var2.x(),
            var3.floorY(),
            (float)(Math.min(var3.model().width() * 0.3F, var4 * 0.62F) * (1.0 + 0.9 * var5) / (1.0 + var2.lift() * 0.5)),
            var7
         );
         double var9 = this.K.pose().ageInTicks() / 20.0;
         s(var1, var2, var4, false, var9);
         zt.i var11 = zt.i.of(var2, var4, this.n);
         this.c.h(var2.roll(), var11.shiftX(), var11.shiftY());
         var1.skin(
            this.c, this.B, var4, 0.0F, this.K.pose().bodyYaw(), -1.0625F, var11.left(), var11.top(), var11.left() + var11.side(), var11.top() + var11.side()
         );
         var1.nextStratum();
         s(var1, var2, var4, true, var9);
      }
   }

   public double M() {
      return this.W;
   }

   public boolean d(double var1, double var3) {
      return this.y.H(var1, var3);
   }

   public boolean J() {
      return this.y.h();
   }

   public void O(double var1, double var3) {
      this.y.F(var1, var3);
   }

   public boolean s(double var1, double var3) {
      if (!this.y.w(var1, var3)) {
         return false;
      }

      this.y.F(var1, var3);
      return true;
   }

   public boolean W() {
      return this.y.Y();
   }

   private void V(zt.l var1) {
      zt.e var2 = var1.stage();
      if (var2 != this.R) {
         zt.t var3 = var1.toy();
         switch (var2) {
            case V:
               if (this.R == zt.e.g) {
                  mt.K("Menu", "Account walk-on started after menu reveal; native left-facing stride");
               }
               break;
            case N:
               if (this.R == zt.e.K) {
                  mt.K("Menu", "Account walk-on settled; native idle and pointer tracking active");
               } else if (this.R == zt.e.p) {
                  mt.K("Menu", "Account model back home; account switcher restored");
               } else if (this.R != null && this.R.ordinal() > zt.e.N.ordinal()) {
                  mt.K("Menu", "Account model reset to its resting composition from {}", this.R);
               }
               break;
            case y:
               mt.K("Menu", "Account model picked up from {}; account switcher hidden", this.R);
               break;
            case j:
               mt.K(
                  "Menu",
                  "Account model thrown at {} blocks/s, spin {} rad/s",
                  String.format(Locale.ROOT, "%.1f", Math.hypot(var3.vx(), var3.vy())),
                  String.format(Locale.ROOT, "%.1f", var3.spin())
               );
               break;
            case a:
               mt.K("Menu", "Account model knocked down at roll {} deg{}", Math.round(Math.toDegrees(var3.roll())), var3.dizzy() > 0.0F ? ", dazed" : "");
               break;
            case r:
               if (this.R == zt.e.j) {
                  mt.K("Menu", "Account model landed on its feet");
               }
               break;
            case u:
               mt.K("Menu", "Account model running home from {} px away", Math.round(Math.abs(var1.xOffset())));
         }

         this.R = var2;
         mt.U("Menu", "Account preview: {}", this.R);
      }
   }

   private void p(Minecraft var1, zt.t var2, long var3) {
      if (!(var2.impact() < 4.5) && !(var3 - this.o < 9.0E7)) {
         this.o = var3;
         SoundEvent var5 = var2.impact() >= 12.0F ? SoundEvents.PLAYER_BIG_FALL : SoundEvents.PLAYER_SMALL_FALL;
         var1.getSoundManager().play(SimpleSoundInstance.forUI(var5, 1.0F, (float)Math.clamp(var2.impact() / 16.0F, 0.18, 0.7)));
      }
   }

   private static void s(GuiGraphicsExtractor var0, zt.t var1, float var2, boolean var3, double var4) {
      if (!(var1.dizzy() <= 0.0F)) {
         double var6 = Math.cos(var1.roll());
         double var8 = Math.sin(var1.roll());

         for (int var10 = 0; var10 < 3; var10++) {
            double var11 = var4 * 4.2 + var10 * Math.PI * 2.0 / 3.0;
            double var13 = Math.sin(var11);
            if ((var13 > 0.0 ? 1 : 0) == var3) {
               double var15 = Math.cos(var11) * 0.42;
               double var17 = -0.99 + var13 * 0.08;
               float var19 = (float)(var1.x() + (var15 * var6 - var17 * var8) * var2);
               float var20 = (float)(var1.y() + (var15 * var8 + var17 * var6) * var2);
               float var21 = (float)(var2 * (0.13 + 0.03 * var13) / 64.0);
               var0.pose().pushMatrix();

               try {
                  var0.pose().translate(var19, var20);
                  var0.pose().rotate((float)(var4 * 3.0 + var10));
                  var0.pose().scale(var21, var21);
                  var0.blit(
                     RenderPipelines.GUI_TEXTURED, pw.k(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, _3.A(-855310, var1.dizzy() * (0.75 + 0.25 * var13))
                  );
               } finally {
                  var0.pose().popMatrix();
               }
            }
         }
      }
   }

   private boolean h(Minecraft var1) {
      int var2 = m5.k;
      if (this.J == var2) {
         return false;
      }

      if (this.V == var2 && this.T != null) {
         return true;
      }

      try {
         EntityModelSet var3 = var1.getEntityModels();
         this.T = new w_(var3.bakeLayer(ModelLayers.PLAYER));
         this.f = new w_(var3.bakeLayer(ModelLayers.PLAYER_SLIM));
         this.Q = new PlayerCapeModel(var3.bakeLayer(ModelLayers.PLAYER_CAPE));
         this.V = var2;
         mt.K("Menu", "Account skin preview ready; native wide/slim models and cape, resource generation {}", var2);
         return true;
      } catch (RuntimeException var4) {
         this.T = this.f = null;
         this.Q = null;
         this.J = var2;
         mt.P("Menu", "Account model bake failed; showing the account portrait until reopen or resource reload", var4);
         return false;
      }
   }

   private static void Y(GuiGraphicsExtractor var0, float var1, int var2, float var3, double var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2);
         var0.pose().scale(1.0F, 0.13F);

         for (int var6 = 8; var6 >= 1; var6--) {
            c7.K(var0, 0, 0, Math.max(1, Math.round(var3 * var6 / 8.0F)), _3.U(110663832, var4));
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   public void X() {
      this.y.n();
   }

   public void D(boolean var1) {
      this.T = this.f = null;
      this.Q = null;
      this.K = null;
      this.c = null;
      this.V = this.J = -1;
      this.y.O(var1);
   }
}
