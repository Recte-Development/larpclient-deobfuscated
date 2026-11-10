package com.larpclient;

import com.larpclient.module.WingsModule;
import java.util.List;
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

public final class dl {
   static final double a = 1.45;
   private static final double e = 4.5;
   private static final double r = 0.09;
   private final o5 L;
   private final m N = new m();
   private fw A;
   private fw T;
   private PlayerCapeModel s;
   private int u = -1;
   private int h = -1;
   private o5.o x;
   private double W;
   private o5.z p;
   private o5.t O;
   private fw K;
   private Identifier B;
   private double G;
   private long C;

   public dl(o5 var1) {
      this.L = var1;
   }

   public static o5.t I(qa var0) {
      return o5.m(var0, m.V(m.Z(), m.O()));
   }

   public void D(GuiGraphicsExtractor var1, qa var2, ah var3, bd var4, int var5, int var6, boolean var7, boolean var8) {
      Minecraft var9 = Minecraft.getInstance();
      this.p = null;
      if (this.L.C() && n2.L(var9.getWindow().handle(), 0) != 1) {
         this.L.R();
      }

      List var10 = m.Z();
      WingsModule.o var11 = m.O();
      o5.t var12 = o5.m(var2, m.V(var10, var11));
      int var13 = var12.visible() && var9.gui.overlay() == null && var9.isWindowActive() ? 1 : 0;
      long var14 = System.nanoTime();
      o5.z var16 = this.L.a(var14, (boolean)var13, var7, var8, var3.F(), var12, var2.width(), var5, var6);
      this.W = var16.nameOpacity();
      this.L(var16);
      if (var12.visible() && var16.stage() != o5.o.f) {
         o5.m var17 = var16.toy();
         if (var17 != null) {
            this.W(var9, var17, var14);
         }

         _c.f var18 = var12.model();
         int var19 = Math.round(var12.centerX() + (float)var16.xOffset());
         double var20 = m.P(var10, var11);
         int var22 = Math.max(var12.renderWidth(), (int)Math.ceil(2.0 * var20 * var12.scale()));
         int var23 = var19 - var22 / 2;
         int var24 = o5.S(var12, m.V(var10, var11));
         if (var17 == null) {
            var1.enableScissor(var2.navigation(0).x() + var2.menuWidth() + 8, var24, var2.width(), var18.y() + var18.height() + 8);
         }

         try {
            if (var17 == null) {
               z(var1, var19, var12.floorY(), Math.min(var18.width() * 0.3F, var12.scale() * 0.62F), 1.0);
            }

            if (!this.I(var9)) {
               if (var17 == null) {
                  int var31 = Math.min(48, var18.width() / 2);
                  var4.K(var1, var19 - var31 / 2, var18.y() + var18.height() / 3, var31);
               }

               return;
            }

            PlayerSkin var25 = m.l(var9.getGameProfile().id(), var4.j());
            fw var26 = var25.model() == PlayerModelType.SLIM ? this.T : this.A;
            var26.H(var16.pose(), var17 == null ? null : var17.limbs());
            a1 var27 = m.M();
            if (var27 != null && !f3.M()) {
               var27 = null;
            }

            var26.K(this.N.r(m.U(var25), var10, var11, m.T(), var27, var16.pose(), this.s));
            if (var17 == null) {
               var26.j(0.0F, 0.0F, 0.0F);
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

            this.p = var16;
            this.O = var12;
            this.K = var26;
            this.B = var25.body().texturePath();
            this.G = Math.max(1.45, Math.max(0.85 + m.V(var10, var11) + 0.1, Math.hypot(var20, 0.6) + 0.05));
         } finally {
            if (var17 == null) {
               var1.disableScissor();
            }
         }
      }
   }

   public void I(GuiGraphicsExtractor var1) {
      if (this.p != null) {
         o5.m var2 = this.p.toy();
         o5.t var3 = this.O;
         float var4 = var3.scale();
         var1.nextStratum();
         double var5 = Math.abs(Math.sin(var2.roll()));
         double var7 = 1.0 / (1.0 + var2.lift() * 0.9);
         z(
            var1,
            (float)var2.x(),
            var3.floorY(),
            (float)(Math.min(var3.model().width() * 0.3F, var4 * 0.62F) * (1.0 + 0.9 * var5) / (1.0 + var2.lift() * 0.5)),
            var7
         );
         double var9 = this.p.pose().ageInTicks() / 20.0;
         v(var1, var2, var4, false, var9);
         o5.p var11 = o5.p.of(var2, var4, this.G);
         this.K.j(var2.roll(), var11.shiftX(), var11.shiftY());
         var1.skin(
            this.K, this.B, var4, 0.0F, this.p.pose().bodyYaw(), -1.0625F, var11.left(), var11.top(), var11.left() + var11.side(), var11.top() + var11.side()
         );
         var1.nextStratum();
         v(var1, var2, var4, true, var9);
      }
   }

   public double x() {
      return this.W;
   }

   public boolean O(double var1, double var3) {
      return this.L.o(var1, var3);
   }

   public boolean M() {
      return this.L.C();
   }

   public void N(double var1, double var3) {
      this.L.n(var1, var3);
   }

   public boolean I(double var1, double var3) {
      if (!this.L.K(var1, var3)) {
         return false;
      }

      this.L.n(var1, var3);
      return true;
   }

   public boolean A() {
      return this.L.R();
   }

   private void L(o5.z var1) {
      o5.o var2 = var1.stage();
      if (var2 != this.x) {
         o5.m var3 = var1.toy();
         switch (var2) {
            case m:
               if (this.x == o5.o.f) {
                  _q.z("Menu", "Account walk-on started after menu reveal; native left-facing stride");
               }
               break;
            case H:
               if (this.x == o5.o.a) {
                  _q.z("Menu", "Account walk-on settled; native idle and pointer tracking active");
               } else if (this.x == o5.o.l) {
                  _q.z("Menu", "Account model back home; account switcher restored");
               } else if (this.x != null && this.x.ordinal() > o5.o.H.ordinal()) {
                  _q.z("Menu", "Account model reset to its resting composition from {}", this.x);
               }
               break;
            case A:
               _q.z("Menu", "Account model picked up from {}; account switcher hidden", this.x);
               break;
            case x:
               _q.z(
                  "Menu",
                  "Account model thrown at {} blocks/s, spin {} rad/s",
                  String.format(Locale.ROOT, "%.1f", Math.hypot(var3.vx(), var3.vy())),
                  String.format(Locale.ROOT, "%.1f", var3.spin())
               );
               break;
            case j:
               _q.z("Menu", "Account model knocked down at roll {} deg{}", Math.round(Math.toDegrees(var3.roll())), var3.dizzy() > 0.0F ? ", dazed" : "");
               break;
            case h:
               if (this.x == o5.o.x) {
                  _q.z("Menu", "Account model landed on its feet");
               }
               break;
            case T:
               _q.z("Menu", "Account model running home from {} px away", Math.round(Math.abs(var1.xOffset())));
         }

         this.x = var2;
         _q.c("Menu", "Account preview: {}", this.x);
      }
   }

   private void W(Minecraft var1, o5.m var2, long var3) {
      if (!(var2.impact() < 4.5) && !(var3 - this.C < 9.0E7)) {
         this.C = var3;
         SoundEvent var5 = var2.impact() >= 12.0F ? SoundEvents.PLAYER_BIG_FALL : SoundEvents.PLAYER_SMALL_FALL;
         var1.getSoundManager().play(SimpleSoundInstance.forUI(var5, 1.0F, (float)Math.clamp(var2.impact() / 16.0F, 0.18, 0.7)));
      }
   }

   private static void v(GuiGraphicsExtractor var0, o5.m var1, float var2, boolean var3, double var4) {
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
                     RenderPipelines.GUI_TEXTURED,
                     com.larpclient.h.b(),
                     -32,
                     -32,
                     0.0F,
                     0.0F,
                     64,
                     64,
                     64,
                     64,
                     64,
                     64,
                     vv.N(-855310, var1.dizzy() * (0.75 + 0.25 * var13))
                  );
               } finally {
                  var0.pose().popMatrix();
               }
            }
         }
      }
   }

   private boolean I(Minecraft var1) {
      int var2 = nl.q;
      if (this.h == var2) {
         return false;
      }

      if (this.u == var2 && this.A != null) {
         return true;
      }

      try {
         EntityModelSet var3 = var1.getEntityModels();
         this.A = new fw(var3.bakeLayer(ModelLayers.PLAYER));
         this.T = new fw(var3.bakeLayer(ModelLayers.PLAYER_SLIM));
         this.s = new PlayerCapeModel(var3.bakeLayer(ModelLayers.PLAYER_CAPE));
         this.u = var2;
         _q.z("Menu", "Account skin preview ready; native wide/slim models and cape, resource generation {}", var2);
         return true;
      } catch (RuntimeException var4) {
         this.A = this.T = null;
         this.s = null;
         this.h = var2;
         _q.o("Menu", "Account model bake failed; showing the account portrait until reopen or resource reload", var4);
         return false;
      }
   }

   private static void z(GuiGraphicsExtractor var0, float var1, int var2, float var3, double var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2);
         var0.pose().scale(1.0F, 0.13F);

         for (int var6 = 8; var6 >= 1; var6--) {
            h5.M(var0, 0, 0, Math.max(1, Math.round(var3 * var6 / 8.0F)), vv.O(110663832, var4));
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   public void Q() {
      this.L.U();
   }

   public void A(boolean var1) {
      this.A = this.T = null;
      this.s = null;
      this.p = null;
      this.K = null;
      this.u = this.h = -1;
      this.L.R(var1);
   }
}
