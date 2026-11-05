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

public final class xu {
   static final double m = 1.45;
   private static final double T = 4.5;
   private static final double J = 0.09;
   private final og N;
   private final e8 l = new e8();
   private mn w;
   private mn S;
   private PlayerCapeModel H;
   private int e = -1;
   private int Q = -1;
   private og.e Y;
   private double i;
   private og.s B;
   private og.u c;
   private mn W;
   private Identifier r;
   private double j;
   private long o;

   public xu(og var1) {
      this.N = var1;
   }

   public static og.u V(_q var0) {
      return og.p(var0, e8.C(e8.P(), e8.r()));
   }

   public void q(GuiGraphicsExtractor var1, _q var2, hi var3, yd var4, int var5, int var6, boolean var7, boolean var8) {
      Minecraft var9 = Minecraft.getInstance();
      this.B = null;
      if (this.N.L() && eo.Y(var9.getWindow().handle(), 0) != 1) {
         this.N.g();
      }

      ChinaHatModule.j var10 = e8.P();
      WingsModule.u var11 = e8.r();
      og.u var12 = og.p(var2, e8.C(var10, var11));
      int var13 = var12.visible() && var9.gui.overlay() == null && var9.isWindowActive() ? 1 : 0;
      long var14 = System.nanoTime();
      og.s var16 = this.N.D(var14, (boolean)var13, var7, var8, var3.K(), var12, var2.width(), var5, var6);
      this.i = var16.nameOpacity();
      this.L(var16);
      if (var12.visible() && var16.stage() != og.e.H) {
         og.q var17 = var16.toy();
         if (var17 != null) {
            this.p(var9, var17, var14);
         }

         o6.b var18 = var12.model();
         int var19 = Math.round(var12.centerX() + (float)var16.xOffset());
         double var20 = e8.P(var10, var11);
         int var22 = Math.max(var12.renderWidth(), (int)Math.ceil(2.0 * var20 * var12.scale()));
         int var23 = var19 - var22 / 2;
         int var24 = og.T(var12, e8.C(var10, var11));
         if (var17 == null) {
            var1.enableScissor(var2.navigation(0).x() + var2.menuWidth() + 8, var24, var2.width(), var18.y() + var18.height() + 8);
         }

         try {
            if (var17 == null) {
               A(var1, var19, var12.floorY(), Math.min(var18.width() * 0.3F, var12.scale() * 0.62F), 1.0);
            }

            if (!this.v(var9)) {
               if (var17 == null) {
                  int var31 = Math.min(48, var18.width() / 2);
                  var4.n(var1, var19 - var31 / 2, var18.y() + var18.height() / 3, var31);
               }

               return;
            }

            PlayerSkin var25 = e8.r(var9.getGameProfile().id(), var4.O());
            mn var26 = var25.model() == PlayerModelType.SLIM ? this.S : this.w;
            var26.o(var16.pose(), var17 == null ? null : var17.limbs());
            fd var27 = e8.E();
            if (var27 != null && !fa.t()) {
               var27 = null;
            }

            var26.L(this.l.Q(e8.x(var25), var10, var11, e8.J(), var27, var16.pose(), this.H));
            if (var17 == null) {
               var26.y(0.0F, 0.0F, 0.0F);
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

            this.B = var16;
            this.c = var12;
            this.W = var26;
            this.r = var25.body().texturePath();
            this.j = Math.max(1.45, Math.max(0.85 + e8.C(var10, var11) + 0.1, Math.hypot(var20, 0.6) + 0.05));
         } finally {
            if (var17 == null) {
               var1.disableScissor();
            }
         }
      }
   }

   public void N(GuiGraphicsExtractor var1) {
      if (this.B != null) {
         og.q var2 = this.B.toy();
         og.u var3 = this.c;
         float var4 = var3.scale();
         var1.nextStratum();
         double var5 = Math.abs(Math.sin(var2.roll()));
         double var7 = 1.0 / (1.0 + var2.lift() * 0.9);
         A(
            var1,
            (float)var2.x(),
            var3.floorY(),
            (float)(Math.min(var3.model().width() * 0.3F, var4 * 0.62F) * (1.0 + 0.9 * var5) / (1.0 + var2.lift() * 0.5)),
            var7
         );
         double var9 = this.B.pose().ageInTicks() / 20.0;
         E(var1, var2, var4, false, var9);
         og.h var11 = og.h.of(var2, var4, this.j);
         this.W.y(var2.roll(), var11.shiftX(), var11.shiftY());
         var1.skin(
            this.W, this.r, var4, 0.0F, this.B.pose().bodyYaw(), -1.0625F, var11.left(), var11.top(), var11.left() + var11.side(), var11.top() + var11.side()
         );
         var1.nextStratum();
         E(var1, var2, var4, true, var9);
      }
   }

   public double d() {
      return this.i;
   }

   public boolean w(double var1, double var3) {
      return this.N.m(var1, var3);
   }

   public boolean A() {
      return this.N.L();
   }

   public void L(double var1, double var3) {
      this.N.x(var1, var3);
   }

   public boolean S(double var1, double var3) {
      if (!this.N.c(var1, var3)) {
         return false;
      }

      this.N.x(var1, var3);
      return true;
   }

   public boolean O() {
      return this.N.g();
   }

   private void L(og.s var1) {
      og.e var2 = var1.stage();
      if (var2 != this.Y) {
         og.q var3 = var1.toy();
         switch (var2) {
            case e:
               if (this.Y == og.e.H) {
                  r4.V("Menu", "Account walk-on started after menu reveal; native left-facing stride");
               }
               break;
            case n:
               if (this.Y == og.e.y) {
                  r4.V("Menu", "Account walk-on settled; native idle and pointer tracking active");
               } else if (this.Y == og.e.E) {
                  r4.V("Menu", "Account model back home; account switcher restored");
               } else if (this.Y != null && this.Y.ordinal() > og.e.n.ordinal()) {
                  r4.V("Menu", "Account model reset to its resting composition from {}", this.Y);
               }
               break;
            case d:
               r4.V("Menu", "Account model picked up from {}; account switcher hidden", this.Y);
               break;
            case a:
               r4.V(
                  "Menu",
                  "Account model thrown at {} blocks/s, spin {} rad/s",
                  String.format(Locale.ROOT, "%.1f", Math.hypot(var3.vx(), var3.vy())),
                  String.format(Locale.ROOT, "%.1f", var3.spin())
               );
               break;
            case I:
               r4.V("Menu", "Account model knocked down at roll {} deg{}", Math.round(Math.toDegrees(var3.roll())), var3.dizzy() > 0.0F ? ", dazed" : "");
               break;
            case B:
               if (this.Y == og.e.a) {
                  r4.V("Menu", "Account model landed on its feet");
               }
               break;
            case V:
               r4.V("Menu", "Account model running home from {} px away", Math.round(Math.abs(var1.xOffset())));
         }

         this.Y = var2;
         r4.g("Menu", "Account preview: {}", this.Y);
      }
   }

   private void p(Minecraft var1, og.q var2, long var3) {
      if (!(var2.impact() < 4.5) && !(var3 - this.o < 9.0E7)) {
         this.o = var3;
         SoundEvent var5 = var2.impact() >= 12.0F ? SoundEvents.PLAYER_BIG_FALL : SoundEvents.PLAYER_SMALL_FALL;
         var1.getSoundManager().play(SimpleSoundInstance.forUI(var5, 1.0F, (float)Math.clamp(var2.impact() / 16.0F, 0.18, 0.7)));
      }
   }

   private static void E(GuiGraphicsExtractor var0, og.q var1, float var2, boolean var3, double var4) {
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
                     RenderPipelines.GUI_TEXTURED, dm.Q(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, oh.i(-855310, var1.dizzy() * (0.75 + 0.25 * var13))
                  );
               } finally {
                  var0.pose().popMatrix();
               }
            }
         }
      }
   }

   private boolean v(Minecraft var1) {
      int var2 = jk.v;
      if (this.Q == var2) {
         return false;
      }

      if (this.e == var2 && this.w != null) {
         return true;
      }

      try {
         EntityModelSet var3 = var1.getEntityModels();
         this.w = new mn(var3.bakeLayer(ModelLayers.PLAYER));
         this.S = new mn(var3.bakeLayer(ModelLayers.PLAYER_SLIM));
         this.H = new PlayerCapeModel(var3.bakeLayer(ModelLayers.PLAYER_CAPE));
         this.e = var2;
         r4.V("Menu", "Account skin preview ready; native wide/slim models and cape, resource generation {}", var2);
         return true;
      } catch (RuntimeException var4) {
         this.w = this.S = null;
         this.H = null;
         this.Q = var2;
         r4.r("Menu", "Account model bake failed; showing the account portrait until reopen or resource reload", var4);
         return false;
      }
   }

   private static void A(GuiGraphicsExtractor var0, float var1, int var2, float var3, double var4) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var1, var2);
         var0.pose().scale(1.0F, 0.13F);

         for (int var6 = 8; var6 >= 1; var6--) {
            fr.J(var0, 0, 0, Math.max(1, Math.round(var3 * var6 / 8.0F)), oh.S(110663832, var4));
         }
      } finally {
         var0.pose().popMatrix();
      }
   }

   public void P() {
      this.N.j();
   }

   public void L(boolean var1) {
      this.w = this.S = null;
      this.H = null;
      this.B = null;
      this.W = null;
      this.e = this.Q = -1;
      this.N.O(var1);
   }
}
